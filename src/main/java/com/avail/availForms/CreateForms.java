package com.avail.availForms;

import java.lang.reflect.Field;
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

	public static FormPojo getDadosForm(Class<?> clazz) throws Exception {

		if (!clazz.isAnnotationPresent(Form.class)) {
			throw new Exception("Anotação Form não está presente na entidade.");
		}

		List<EntidadePojo> entidades = new ArrayList<EntidadePojo>();

		entidades.add(new EntidadePojo(clazz.getAnnotation(Form.class).nomeEntidade(), new ArrayList<CampoPojo>(),
				TipoEntidade.PRINCIPAL, true));

		entidades.get(0).setCampos(getCampos(clazz, entidades));

		return new FormPojo(clazz.getAnnotation(Form.class).orientacao().toString(), entidades);
	}

	private static List<CampoPojo> getCampos(Class<?> clazz, List<EntidadePojo> entidades) throws Exception {
		List<CampoPojo> campos = new ArrayList<CampoPojo>();
		for (Field campo : clazz.getFields()) {
			if (campo.isAnnotationPresent(CampoForm.class) && containsAnotationsRelacionamento(campo)) {
				throw new Exception(
						"Um campo não pode conter a anotação @CampoForm e ao mesmo tempo ser um relacionamento.");
			}
			if (campo.isAnnotationPresent(CampoForm.class)) {
				CampoForm campoForm = campo.getAnnotation(CampoForm.class);
				campos.add(new CampoPojo(campoForm.label(), isRequerido(campo), campo.getName(), getOpcoes(campo),
						campoForm.tipo().name(), getTamanho(campo), campoForm.editavel(), campoForm.pesquisavel()));
			} else if (containsAnotationsRelacionamento(campo)) {
				if (campo.getClass().isAnnotationPresent(Form.class)) {
					List<CampoPojo> camposEntidadeFilha = getCampos(campo.getClass(), entidades);
					entidades.add(new EntidadePojo(campo.getClass().getAnnotation(Form.class).nomeEntidade(),
							camposEntidadeFilha, getTipoEntidade(campo), true));
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
			return new String[] { "Sim, N�o" };
		} else {
			return campo.getAnnotation(CampoForm.class).opcoes();
		}
	}

	private static boolean containsAnotationsRelacionamento(Field campo) {
		return campo.isAnnotationPresent(ManyToMany.class) | campo.isAnnotationPresent(ManyToOne.class)
				| campo.isAnnotationPresent(OneToOne.class) | campo.isAnnotationPresent(OneToMany.class);
	}

	@SuppressWarnings("unlikely-arg-type")
	private static TipoEntidade getTipoEntidade(Field campo) {
		if (campo.isAnnotationPresent(ManyToMany.class)) {
			if (campo.getAnnotation(ManyToMany.class).cascade() != null) {
				if (campo.getAnnotation(ManyToMany.class).cascade().equals(CascadeType.ALL)) {
					return TipoEntidade.ADICIONAVEL_MUITOS;
				}
			}
			return TipoEntidade.PESQUISAVEL_MUITOS;
		} else if (campo.isAnnotationPresent(ManyToOne.class)) {
			if (campo.getAnnotation(ManyToOne.class).cascade() != null) {
				if (campo.getAnnotation(ManyToOne.class).cascade().equals(CascadeType.ALL)) {
					return TipoEntidade.ADICIONAVEL_UM;
				}
			}
			return TipoEntidade.PESQUISAVEL_UM;
		} else if (campo.isAnnotationPresent(OneToOne.class)) {
			if (campo.getAnnotation(OneToOne.class).cascade() != null) {
				if (campo.getAnnotation(OneToOne.class).cascade().equals(CascadeType.ALL)) {
					return TipoEntidade.ADICIONAVEL_UM;
				}
			}
			return TipoEntidade.PESQUISAVEL_UM;
		} else if (campo.isAnnotationPresent(OneToMany.class)) {
			if (campo.getAnnotation(OneToMany.class).cascade() != null) {
				if (campo.getAnnotation(OneToMany.class).cascade().equals(CascadeType.ALL)) {
					return TipoEntidade.ADICIONAVEL_MUITOS;
				}
			}
			return TipoEntidade.PESQUISAVEL_MUITOS;
		}
		return null;
	}
}
