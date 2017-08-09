package com.avail.availForms.pojo;

import java.util.List;

public class ListagemPojo {
	
	private String nomeEntidade;
	private List<String> labels;
	private List<String> camposPesquisa;
	private String longNameClazz;
	
	public ListagemPojo(String nomeEntidade, List<String> camposPesquisa, List<String> labels, String longNameClazz) {
		super();
		this.nomeEntidade = nomeEntidade;
		this.camposPesquisa = camposPesquisa;
		this.labels = labels;
		this.longNameClazz = longNameClazz;
	}

	public String getNomeEntidade() {
		return nomeEntidade;
	}

	public void setNomeEntidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}	

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<String>getCamposPesquisa() {
		return camposPesquisa;
	}

	public void setCamposPesquisa(List<String> camposPesquisa) {
		this.camposPesquisa = camposPesquisa;
	}

	public String getLongNameClazz() {
		return longNameClazz;
	}

	public void setLongNameClazz(String longNameClazz) {
		this.longNameClazz = longNameClazz;
	}	
}
