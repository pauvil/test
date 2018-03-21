package com.algoritmusistemos.gvdis.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TestAsmKodasForm1 extends ActionForm {
	private String asmKodas;
	private String submitas;
	public String getSubmitas() {
		return submitas;
	}

	public void setSubmitas(String submitas) {
		this.submitas = submitas;
	}

	public String getAsmKodas() {
		return asmKodas;
	}

	public void setAsmKodas(String asmKodas) {
		this.asmKodas = asmKodas;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {		
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		return null;
	}

}
