package com.algoritmusistemos.gvdis.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EditGvdisAprDbObjektaiForm extends ActionForm {


	private static final long serialVersionUID = 1L;
	
	private String modulioId;
	private String objektas;
	private String parametrai;
	private String tipas;
	private String schema;
	private String komentarai;
	
	
	
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

	public String getModulioId() {
		return modulioId;
	}

	public void setModulioId(String modulioId) {
		this.modulioId = modulioId;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
 
	}
	
}
