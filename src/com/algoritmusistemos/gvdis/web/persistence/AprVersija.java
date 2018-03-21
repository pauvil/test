package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;
import java.util.Date;

public class AprVersija implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long versijosId;
	private String versija;
	private Date data;
	private Long modulisId;
	

	public Long getVersijosId() {
		return versijosId;
	}
	public void setVersijosId(Long versijosId) {
		this.versijosId = versijosId;
	}
	public String getVersija() {
		return versija;
	}
	public void setVersija(String versija) {
		this.versija = versija;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Long getModulisId() {
		return modulisId;
	}
	public void setModulisId(Long modulisId) {
		this.modulisId = modulisId;
	}
	
	
}
