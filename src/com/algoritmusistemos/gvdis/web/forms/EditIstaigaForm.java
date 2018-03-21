package com.algoritmusistemos.gvdis.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EditIstaigaForm extends ActionForm
{
	private String oficialusPavadinimas;
	private String rekvizSpausdinimui;

	public void setOficialusPavadinimas(String oficialusPavadinimas){this.oficialusPavadinimas = oficialusPavadinimas;}
	public String getOficialusPavadinimas(){return this.oficialusPavadinimas;}
	
	public void setRekvizSpausdinimui(String rekvizSpausdinimui){this.rekvizSpausdinimui = rekvizSpausdinimui;}
	public String getRekvizSpausdinimui(){return this.rekvizSpausdinimui;}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		oficialusPavadinimas = "";
		rekvizSpausdinimui = "";
	}
}

