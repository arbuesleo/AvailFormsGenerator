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

import org.springframework.jdbc.core.JdbcTemplate;

import com.avail.availForms.annotation.CampoForm;
import com.avail.availForms.annotation.Form;
import com.avail.availForms.annotation.ListagemForm;
import com.avail.availForms.enuns.TipoEntidade;
import com.avail.availForms.pojo.CampoPojo;
import com.avail.availForms.pojo.EntidadePojo;
import com.avail.availForms.pojo.FormPojo;
import com.avail.availForms.pojo.ListagemPojo;

public class CreateForms {

	private static int levelClazz = 0;

	public static FormPojo getDadosForm(Class<?> clazz) throws Exception {
		
		levelClazz = 0;
		
		if (!clazz.isAnnotationPresent(Form.class)) {
			throw new Exception("Anotação Form não está presente na entidade.");
		}

		List<EntidadePojo> entidades = new ArrayList<EntidadePojo>();

		entidades.add(new EntidadePojo(clazz.getAnnotation(Form.class).nomeEntidade(), new ArrayList<CampoPojo>(),
				TipoEntidade.PRINCIPAL, true, clazz.getName()));

		entidades.get(0).setCampos(getCampos(clazz, entidades, levelClazz));

		return new FormPojo(clazz.getAnnotation(Form.class).orientacao().toString(), entidades);
	}

	private static List<CampoPojo> getCampos(Class<?> clazz, List<EntidadePojo> entidades, int levelClazz)
			throws Exception {
		List<CampoPojo> campos = new ArrayList<CampoPojo>();
		if (levelClazz < 2) {
			for (Field campo : clazz.getDeclaredFields()) {
				if (campo.isAnnotationPresent(CampoForm.class) && containsAnotationsRelacionamento(campo)) {
					throw new Exception(
							"Um campo não pode conter a anotação @CampoForm e ao mesmo tempo ser um relacionamento.");
				}
				if (campo.isAnnotationPresent(CampoForm.class)) {
					CampoForm campoForm = campo.getAnnotation(CampoForm.class);
					campos.add(new CampoPojo(campoForm.label(), isRequerido(campo), campo.getName(), getOpcoes(campo),
							campoForm.tipo().name(), getTamanho(campo), campoForm.editavel(), campoForm.pesquisavel()));
				} else if (containsAnotationsRelacionamento(campo)) {
					Boolean isFormInList = false;
					if (List.class.equals(campo.getType())) {
						isFormInList = getTypeList(campo).isAnnotationPresent(Form.class);
					}
					if (campo.getType().getClass().isAnnotationPresent(Form.class) || isFormInList) {
						Class<?> clazzEntidadeFilha;
						if (campoIsList(campo)) {
							clazzEntidadeFilha = getTypeList(campo);
						} else {
							clazzEntidadeFilha = campo.getType().getClass();
						}

						List<CampoPojo> camposEntidadeFilha = getCampos(clazzEntidadeFilha, entidades, levelClazz++);
						entidades.add(new EntidadePojo(getEntidadeNome(campo), camposEntidadeFilha,
								getTipoEntidade(campo), true, clazzEntidadeFilha.getName()));

					}
				}
			}
		}
		return campos;
	}

	public static ListagemPojo getDadosListagem(Class<?> clazz, JdbcTemplate jdbcTemplate) throws Exception {

		if (clazz.isAnnotationPresent(ListagemForm.class)) {

			String viewNome = clazz.getAnnotation(ListagemForm.class).viewNome();
			String entidadeNome = clazz.getAnnotation(ListagemForm.class).nomeEntidade();

			List<String> campos = jdbcTemplate.queryForList(
					"SELECT column_name FROM information_schema.columns WHERE table_name =" + viewNome, String.class);

			return new ListagemPojo(entidadeNome, viewNome, campos.toArray(new String[0]));

		} else {
			throw new Exception("Anotação @ListagemForm não está presente na entidade.");
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
			if(campo.getType().isEnum()) {
				try {
					Class<?> c = Class.forName(campo.getType().getCanonicalName());
					Object[] opcoesEnum = c.getEnumConstants();
					String [] opcoes = new String[opcoesEnum.length];
					int i = 0;
					for(Object opcao : opcoesEnum) {
						opcoes[i] = new String(opcao.toString());
						i++;
					}
					return opcoes;
				} catch (ClassNotFoundException e) {
					return campo.getAnnotation(CampoForm.class).opcoes();
				}				
			}else {
				return campo.getAnnotation(CampoForm.class).opcoes();
			}
		}
	}

	private static boolean containsAnotationsRelacionamento(Field campo) {
		return campo.isAnnotationPresent(ManyToMany.class) | campo.isAnnotationPresent(ManyToOne.class)
				| campo.isAnnotationPresent(OneToOne.class) | campo.isAnnotationPresent(OneToMany.class);
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
		if (campoIsList(campo)) {
			return getTypeList(campo).getAnnotation(Form.class).nomeEntidade();
		} else {
			return campo.getType().getClass().getAnnotation(Form.class).nomeEntidade();
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
