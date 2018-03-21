package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;

public class AprDbObjektas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long modulisId;
	private String objektas;
	private String parametrai;
	private String tipas;
	private String schema;
	private String komentarai;
	
	public AprDbObjektas(){}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getModulisId() {
		return modulisId;
	}
	public void setModulisId(Long modulisId) {
		this.modulisId = modulisId;
	}
	public String getObjektas() {
		return objektas;
	}
	public void setObjektas(String objektas) {
		this.objektas = objektas;
	}
	public String getParametrai() {
		return parametrai;
	}
	public void setParametrai(String parametrai) {
		this.parametrai = parametrai;
	}
	public String getTipas() {
		return tipas;
	}
	public void setTipas(String tipas) {
		this.tipas = tipas;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getKomentarai() {
		return komentarai;
	}
	public void setKomentarai(String komentarai) {
		this.komentarai = komentarai;
	}
	
	

}
