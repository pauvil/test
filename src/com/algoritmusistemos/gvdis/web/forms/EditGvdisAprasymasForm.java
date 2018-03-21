package com.algoritmusistemos.gvdis.web.forms;

import org.apache.struts.action.ActionForm;

public class EditGvdisAprasymasForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	
	private String aprasymas;
	private String logikosAprasymas;

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

}
