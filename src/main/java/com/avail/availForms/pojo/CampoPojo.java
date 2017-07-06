package com.avail.availForms.pojo;

public class CampoPojo {
	
	private String label;
	private Boolean requerido;
	private String[] opcoes;
	private String tipoCampo;
	private Integer tamanho;
	private Boolean editavel;
	private Boolean pesquisavel;
	private String nome;
	
	public CampoPojo(String label, Boolean requerido, String nome,String[] opcoes, String tipoCampo, Integer tamanho,
			Boolean editavel, Boolean pesquisavel) {
		super();
		this.label = label;
		this.requerido = requerido;
		this.opcoes = opcoes;
		this.tipoCampo = tipoCampo;
		this.tamanho = tamanho;
		this.editavel = editavel;
		this.pesquisavel = pesquisavel;
		this.nome = nome;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Boolean getRequerido() {
		return requerido;
	}
	public void setRequerido(Boolean requerido) {
		this.requerido = requerido;
	}
	public String[] getOpcoes() {
		return opcoes;
	}
	public void setOpcoes(String[] opcoes) {
		this.opcoes = opcoes;
	}
	public String getTipoCampo() {
		return tipoCampo;
	}
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	public Integer getTamanho() {
		return tamanho;
	}
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}
	public Boolean getEditavel() {
		return editavel;
	}
	public void setEditavel(Boolean editavel) {
		this.editavel = editavel;
	}
	public Boolean getPesquisavel() {
		return pesquisavel;
	}
	public void setPesquisavel(Boolean pesquisavel) {
		this.pesquisavel = pesquisavel;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
