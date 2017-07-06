package com.avail.availForms.pojo;

import java.util.List;

public class FormPojo {
	
	private String orientacao;
	List<EntidadePojo> entidades;
	
	public FormPojo(String orientacao, List<EntidadePojo> entidades) {
		super();
		this.orientacao = orientacao;
		this.entidades = entidades;
	}

	public String getOrientacao() {
		return orientacao;
	}

	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}

	public List<EntidadePojo> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<EntidadePojo> entidades) {
		this.entidades = entidades;
	}
}
