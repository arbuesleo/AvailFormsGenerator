package com.avail.availForms;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.avail.availForms.annotation.CampoForm;
import com.avail.availForms.annotation.Form;
import com.avail.availForms.annotation.Image;
import com.avail.availForms.annotation.Relacionamento;
import com.avail.availForms.enuns.TipoCampo;
import com.avail.availForms.enuns.TipoEntidade;
import com.avail.availForms.pojo.CampoPojo;
import com.avail.availForms.pojo.EntidadePojo;
import com.avail.availForms.pojo.FormPojo;
import com.avail.availForms.pojo.ListagemPojo;

public class CreateForms {

	private static List<CampoPojo> camposAux = new ArrayList<CampoPojo>();

	public static FormPojo getDadosForm(Class<?> clazz) throws Exception {

		if (!clazz.isAnnotationPresent(Form.class)) {
			throw new Exception("Anotação Form não está presente na entidade.");
		}

		List<EntidadePojo> entidades = new ArrayList<EntidadePojo>();

		entidades.add(new EntidadePojo(clazz.getAnnotation(Form.class).nomeEntidade(), new ArrayList<CampoPojo>(),
				TipoEntidade.PRINCIPAL, true, clazz.getSimpleName(), clazz.getName(), getDadosListagem(clazz), false));

		entidades.get(0).setCampos(getCampos(clazz, entidades, true));

		return new FormPojo(clazz.getAnnotation(Form.class).orientacao().toString(), entidades);
	}

	private static List<CampoPojo> getCampos(Class<?> clazz, List<EntidadePojo> entidades, Boolean buscaRelacionamentos)
			throws Exception {
		List<CampoPojo> campos = new ArrayList<CampoPojo>();
		for (Field campo : clazz.getDeclaredFields()) {
			if (campo.isAnnotationPresent(CampoForm.class) && containsAnotationsRelacionamento(campo)) {
				throw new Exception("Um campo não pode conter a anotação @CampoForm e ao mesmo tempo ser um relacionamento.");
			}
			if (campo.isAnnotationPresent(CampoForm.class)) {
				
				CampoForm campoForm = campo.getAnnotation(CampoForm.class);
				campos.add(new CampoPojo(campoForm.label(), isRequerido(campo), campo.getName(), getOpcoes(campo), campoForm.tipo().name(), getTamanho(campo), campoForm.editavel()));
				
			} else if (containsAnotationsRelacionamento(campo) && buscaRelacionamentos) {
				
				String nomeAdicional = "";
				if(campo.isAnnotationPresent(Relacionamento.class)) {
					nomeAdicional = campo.getAnnotation(Relacionamento.class).nomeAdicional();
				}
				
				Boolean isFormInList = false;
				Boolean buscaRelacionamentoFilhos = (getTipoEntidade(campo) == TipoEntidade.PRINCIPAL || getTipoEntidade(campo) == TipoEntidade.ADICIONAVEL_UM || getTipoEntidade(campo) == TipoEntidade.ADICIONAVEL_MUITOS);
				if (List.class.equals(campo.getType())) {
				
					isFormInList = getTypeList(campo).isAnnotationPresent(Form.class);
				}
				
				if (campo.isAnnotationPresent(Image.class)) {
				
					CampoPojo campoAux = new CampoPojo(campo.getAnnotation(Image.class).label(),(campo.isAnnotationPresent(NotBlank.class) || campo.isAnnotationPresent(NotNull.class)),campo.getName(), null, TipoCampo.IMAGEM.name(), 0, Boolean.TRUE);
					campoAux.setQuantidadeImagens(campo.getAnnotation(Image.class).qtdImgs());
					camposAux.add(campoAux);
					
				} else if (campo.getType().getClass().isAnnotationPresent(Form.class) || campo.getType().isAnnotationPresent(Form.class) || isFormInList) {

					Class<?> clazzEntidadeFilha;
					
					if (campoIsList(campo)) {
						
						clazzEntidadeFilha = getTypeList(campo);
						
					} else {						
						if (campo.getType().getClass() instanceof Class) {
							clazzEntidadeFilha = campo.getType();
						} else {
							clazzEntidadeFilha = campo.getType().getClass();
						}
					}
					if (getTipoEntidade(campo) == TipoEntidade.PESQUISAVEL_UM) {
						CampoPojo campoAux = new CampoPojo(campo.getType().getAnnotation(Form.class).nomeEntidade() + " - " + nomeAdicional, (campo.isAnnotationPresent(NotBlank.class) || campo.isAnnotationPresent(NotNull.class)),campo.getName(), null, TipoCampo.PESQUISA.name(), 0, Boolean.FALSE);
						campoAux.setDadosListagem(getDadosListagem(campo.getType()));
						camposAux.add(campoAux);

					} else {						
						List<CampoPojo> camposEntidadeFilha = getCampos(clazzEntidadeFilha, entidades, buscaRelacionamentoFilhos);						
						entidades.add(new EntidadePojo(getEntidadeNome(campo), camposEntidadeFilha, getTipoEntidade(campo), true, campo.getName(), clazzEntidadeFilha.getName(), getDadosListagem(clazzEntidadeFilha), (campo.isAnnotationPresent(NotBlank.class) || campo.isAnnotationPresent(NotNull.class))));
					}
					
					if (camposAux.size() > 0) {
						for (CampoPojo camp : camposAux) {
							campos.add(camp);
						}
						camposAux = new ArrayList<CampoPojo>();
					}

				}
			}
		}
		return campos;
	}

	public static ListagemPojo getDadosListagem(Class<?> clazz) throws Exception {
		List<String> camposPesquisa = new ArrayList<String>();
		List<String> labels = new ArrayList<String>();

		if (clazz.isAnnotationPresent(Form.class)) {
			for (Field campo : clazz.getDeclaredFields()) {
				
				if (campo.isAnnotationPresent(CampoForm.class)) {
					CampoForm campoForm = campo.getAnnotation(CampoForm.class);
					if (campoForm.listagem()) {
						camposPesquisa.add(campo.getName());
						labels.add(campoForm.label());
					}
				}
				
			}
			return new ListagemPojo(clazz.getAnnotation(Form.class).nomeEntidade(), camposPesquisa, labels,
					clazz.getName());
		} else {
			throw new Exception("Anotação Form não está presente na entidade.");
		}
	}

	private static boolean isRequerido(Field campo) {
		if (campo.isAnnotationPresent(Column.class)) {
			if (!campo.getAnnotation(Column.class).nullable()) {
				return true;
			}
		}
		return false;
	}

	private static Integer getTamanho(Field campo) {
		if (campo.isAnnotationPresent(Column.class)) {
			return campo.getAnnotation(Column.class).length();
		}
		return 255;
	}

	private static String[] getOpcoes(Field campo) {
		if (campo.getType() == Boolean.class || campo.getType() == boolean.class) {
			
			return new String[] { "Sim, Não" };
			
		} else {
			if (campo.getType().isEnum()) {
				try {
					Class<?> c = Class.forName(campo.getType().getCanonicalName());
					Object[] opcoesEnum = c.getEnumConstants();
					String[] opcoes = new String[opcoesEnum.length];
					int i = 0;
					for (Object opcao : opcoesEnum) {
						opcoes[i] = new String(opcao.toString());
						i++;
					}
					return opcoes;
				} catch (ClassNotFoundException e) {
					return campo.getAnnotation(CampoForm.class).opcoes();
				}
			} else {
				return campo.getAnnotation(CampoForm.class).opcoes();
			}
		}
	}

	private static boolean containsAnotationsRelacionamento(Field campo) {
		boolean ignorar = false;
		if(campo.isAnnotationPresent(Relacionamento.class)) {
			ignorar = campo.getAnnotation(Relacionamento.class).ignorar();
		}
		return (campo.isAnnotationPresent(ManyToMany.class) | campo.isAnnotationPresent(ManyToOne.class)
				| campo.isAnnotationPresent(OneToOne.class) | campo.isAnnotationPresent(OneToMany.class)) && !ignorar;
	}

	private static TipoEntidade getTipoEntidade(Field campo) {
		if (campo.isAnnotationPresent(ManyToMany.class)) {
			if (campo.getAnnotation(ManyToMany.class).cascade() != null) {
				if (isCascadeAll(campo.getAnnotation(ManyToMany.class).cascade())) {
					return TipoEntidade.ADICIONAVEL_MUITOS;
				}
			}
			return TipoEntidade.PESQUISAVEL_MUITOS;
		} else if (campo.isAnnotationPresent(ManyToOne.class)) {
			if (campo.getAnnotation(ManyToOne.class).cascade() != null) {
				if (isCascadeAll(campo.getAnnotation(ManyToOne.class).cascade())) {
					return TipoEntidade.ADICIONAVEL_UM;
				}
			}
			return TipoEntidade.PESQUISAVEL_UM;
		} else if (campo.isAnnotationPresent(OneToOne.class)) {
			if (campo.getAnnotation(OneToOne.class).cascade() != null) {
				if (isCascadeAll(campo.getAnnotation(OneToOne.class).cascade())) {
					return TipoEntidade.ADICIONAVEL_UM;
				}
			}
			return TipoEntidade.PESQUISAVEL_UM;
		} else if (campo.isAnnotationPresent(OneToMany.class)) {
			if (campo.getAnnotation(OneToMany.class).cascade() != null) {
				if (isCascadeAll(campo.getAnnotation(OneToMany.class).cascade())) {
					return TipoEntidade.ADICIONAVEL_MUITOS;
				}
			}
			return TipoEntidade.PESQUISAVEL_MUITOS;
		}
		return null;
	}

	private static Class<?> getTypeList(Field campo) {
		ParameterizedType parameter = (ParameterizedType) campo.getGenericType();
		return (Class<?>) parameter.getActualTypeArguments()[0];
	}

	private static boolean campoIsList(Field campo) {
		return List.class.equals(campo.getType());
	}

	private static String getEntidadeNome(Field campo) {
		String nomeAdicional = "";
		if(campo.isAnnotationPresent(Relacionamento.class)) {
			nomeAdicional = campo.getAnnotation(Relacionamento.class).nomeAdicional();
		}
		if (campoIsList(campo)) {
			return getTypeList(campo).getAnnotation(Form.class).nomeEntidade() + (nomeAdicional.equals("")? "" : " - ") + nomeAdicional;
		} else {
			return campo.getType().getClass().getAnnotation(Form.class).nomeEntidade() + " - " + nomeAdicional;
		}
	}

	private static boolean isCascadeAll(CascadeType[] cascade) {
		for (CascadeType cascadeType : cascade) {
			if (cascadeType.equals(CascadeType.ALL)) {
				return true;
			}
		}
		return false;
	}
}
