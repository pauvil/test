package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AprMeniuGrupe implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String pavadinimas;
	private Set meniuGrupes = new HashSet();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPavadinimas() {
		return pavadinimas;
	}

	public void setPavadinimas(String pavadinimas) {
		this.pavadinimas = pavadinimas;
	}

	public Set getMeniuGrupes() {
		return meniuGrupes;
	}

	public void setMeniuGrupes(Set meniuGrupes) {
		this.meniuGrupes = meniuGrupes;
	}
	
}
