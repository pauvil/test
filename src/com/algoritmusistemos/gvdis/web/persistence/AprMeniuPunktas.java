package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;
import java.util.Set;

public class AprMeniuPunktas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long meniuGrupeId;
	private String pavadinimas;
	private Set meniuPunktai;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMeniuGrupeId() {
		return meniuGrupeId;
	}
	public void setMeniuGrupeId(Long meniuGrupeId) {
		this.meniuGrupeId = meniuGrupeId;
	}
	public String getPavadinimas() {
		return pavadinimas;
	}
	public void setPavadinimas(String pavadinimas) {
		this.pavadinimas = pavadinimas;
	}
	public Set getMeniuPunktai() {
		return meniuPunktai;
	}
	public void setMeniuPunktai(Set meniuPunktai) {
		this.meniuPunktai = meniuPunktai;
	}
	
	
	
}
