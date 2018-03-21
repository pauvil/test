package com.algoritmusistemos.gvdis.web.forms;

import org.apache.struts.action.ActionForm;

public class CreateGvdisAprasymasForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private String versija;
	private String modulioId;

	public String getVersija() {
		return versija;
	}

	public void setVersija(String versija) {
		this.versija = versija;
	}

	public String getModulioId() {
		return modulioId;
	}

	public void setModulioId(String modulioId) {
		this.modulioId = modulioId;
	}
	
	
}
