package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;
import java.util.Date;

public class AprModulis implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long modulioId;
	private Long meniuPunktasId;
	private String pavadinimas;
	private String aprasymas;
	private String logikosAprasymas;
	private String versija;
	private Date sukurimoData;
	private Date koregavimoData;
	

	public Long getModulioId() {
		return modulioId;
	}
	public void setModulioId(Long modulioId) {
		this.modulioId = modulioId;
	}
	public Long getMeniuPunktasId() {
		return meniuPunktasId;
	}
	public void setMeniuPunktasId(Long meniuPunktasId) {
		this.meniuPunktasId = meniuPunktasId;
	}
	public String getPavadinimas() {
		return pavadinimas;
	}
	public void setPavadinimas(String pavadinimas) {
		this.pavadinimas = pavadinimas;
	}
	public String getAprasymas() {
		return aprasymas;
	}
	public void setAprasymas(String aprasymas) {
		this.aprasymas = aprasymas;
	}
	public String getLogikosAprasymas() {
		return logikosAprasymas;
	}
	public void setLogikosAprasymas(String logikosAprasymas) {
		this.logikosAprasymas = logikosAprasymas;
	}
	public String getVersija() {
		return versija;
	}
	public void setVersija(String versija) {
		this.versija = versija;
	}
	public Date getSukurimoData() {
		return sukurimoData;
	}
	public void setSukurimoData(Date sukurimoData) {
		this.sukurimoData = sukurimoData;
	}
	public Date getKoregavimoData() {
		return koregavimoData;
	}
	public void setKoregavimoData(Date koregavimoData) {
		this.koregavimoData = koregavimoData;
	}
	
	
	
}
