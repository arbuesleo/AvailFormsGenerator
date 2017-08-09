package com.avail.availForms.pojo;

import java.util.List;

import com.avail.availForms.enuns.TipoEntidade;

public class EntidadePojo {
	
	private String labelClazz;
	private List<CampoPojo> campos;
	private TipoEntidade tipo;
	private Boolean editavel;
	private String nomeClazz;
	private String longNomeClazz;
	private ListagemPojo dadosListagem;
	private Boolean requerido;
	
	public EntidadePojo(String labelClazz, List<CampoPojo> campos, TipoEntidade tipo, Boolean editavel, String nomeClazz, String longNomeClazz, ListagemPojo dadosListagem, Boolean requerido) {
		super();
		this.labelClazz = labelClazz;
		this.campos = campos;
		this.tipo = tipo;
		this.editavel = editavel;
		this.nomeClazz = nomeClazz;
		this.longNomeClazz = longNomeClazz;
		this.dadosListagem = dadosListagem;
		this.requerido = requerido;
	}
	
	public String getLabelClazz() {
		return labelClazz;
	}
	public void setLabelClazz(String labelClazz) {
		this.labelClazz = labelClazz;
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

	public String getNomeClazz() {
		return nomeClazz;
	}

	public void setNomeClazz(String nomeClazz) {
		this.nomeClazz = nomeClazz;
	}

	public String getLongNomeClazz() {
		return longNomeClazz;
	}

	public void setLongNomeClazz(String longNomeClazz) {
		this.longNomeClazz = longNomeClazz;
	}

	public ListagemPojo getDadosListagem() {
		return dadosListagem;
	}

	public void setDadosListagem(ListagemPojo dadosListagem) {
		this.dadosListagem = dadosListagem;
	}

	public Boolean getRequerido() {
		return requerido;
	}

	public void setRequerido(Boolean requerido) {
		this.requerido = requerido;
	}
	
}
