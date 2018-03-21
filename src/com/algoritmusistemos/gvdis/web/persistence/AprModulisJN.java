package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;
import java.util.Date;

public class AprModulisJN implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long versijaId;
	private Long modulisId;
	private String aprasymas;
	private String logikosAprasymas;
	private Date sukurimoData;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersijaId() {
		return versijaId;
	}
	public void setVersijaId(Long versijaId) {
		this.versijaId = versijaId;
	}
	public Long getModulisId() {
		return modulisId;
	}
	public void setModulisId(Long modulisId) {
		this.modulisId = modulisId;
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
	public Date getSukurimoData() {
		return sukurimoData;
	}
	public void setSukurimoData(Date sukurimoData) {
		this.sukurimoData = sukurimoData;
	}
	
	

}
