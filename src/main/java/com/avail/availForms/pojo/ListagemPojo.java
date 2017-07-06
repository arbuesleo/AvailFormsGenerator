package com.avail.availForms.pojo;

public class ListagemPojo {
	
	private String nomeEntidade;
	private String nomeView;
	//TODO: SELECT column_name FROM information_schema.columns WHERE table_name = 'nomeView';
	private String[] camposPesquisa;
	
	public ListagemPojo(String nomeEntidade, String nomeView, String[] camposPesquisa) {
		super();
		this.nomeEntidade = nomeEntidade;
		this.nomeView = nomeView;
		this.camposPesquisa = camposPesquisa;
	}

	public String getNomeEntidade() {
		return nomeEntidade;
	}

	public void setNomeEntidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}

	public String getNomeView() {
		return nomeView;
	}

	public void setNomeView(String nomeView) {
		this.nomeView = nomeView;
	}

	public String[] getCamposPesquisa() {
		return camposPesquisa;
	}

	public void setCamposPesquisa(String[] camposPesquisa) {
		this.camposPesquisa = camposPesquisa;
	}
	
}
