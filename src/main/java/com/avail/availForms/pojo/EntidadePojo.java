package com.avail.availForms.pojo;

import java.util.List;

import com.avail.availForms.enuns.TipoEntidade;

public class EntidadePojo {
	
	private String nome;
	private List<CampoPojo> campos;
	private TipoEntidade tipo;
	private Boolean editavel;
	
	public EntidadePojo(String nome, List<CampoPojo> campos, TipoEntidade tipo, Boolean editavel) {
		super();
		this.nome = nome;
		this.campos = campos;
		this.tipo = tipo;
		this.editavel = editavel;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<CampoPojo> getCampos() {
		return campos;
	}
	public void setCampos(List<CampoPojo> campos) {
		this.campos = campos;
	}
	public TipoEntidade getTipo() {
		return tipo;
	}
	public void setTipo(TipoEntidade tipo) {
		this.tipo = tipo;
	}
	public Boolean getEditavel() {
		return editavel;
	}
	public void setEditavel(Boolean editavel) {
		this.editavel = editavel;
	}
}
