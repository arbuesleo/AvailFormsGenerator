package com.avail.availForms.pojo;

public class CampoPojo {
	
	private String label;
	private Boolean requerido;
	private String[] opcoes;
	private String tipoCampo;
	private Integer tamanho;
	private Boolean editavel;
	private String nome;
	private String nomeClazzPesquisa; 
	private ListagemPojo dadosListagem;
	private Integer quantidadeImagens;
	
	public ListagemPojo getDadosListagem() {
		return dadosListagem;
	}
	public void setDadosListagem(ListagemPojo dadosListagem) {
		this.dadosListagem = dadosListagem;
	}
	public CampoPojo(String label, Boolean requerido, String nome,String[] opcoes, String tipoCampo, Integer tamanho,
			Boolean editavel) {
		super();
		this.label = label;
		this.requerido = requerido;
		this.opcoes = opcoes;
		this.tipoCampo = tipoCampo;
		this.tamanho = tamanho;
		this.editavel = editavel;
		this.nome = nome;
	}
	public String getNomeClazzPesquisa() {
		return nomeClazzPesquisa;
	}
	public void setNomeClazzPesquisa(String nomeClazzPesquisa) {
		this.nomeClazzPesquisa = nomeClazzPesquisa;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getQuantidadeImagens() {
		return quantidadeImagens;
	}
	public void setQuantidadeImagens(Integer quantidadeImagens) {
		this.quantidadeImagens = quantidadeImagens;
	}
}
