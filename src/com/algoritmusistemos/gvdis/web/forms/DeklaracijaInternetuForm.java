package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.Constants;

public class DeklaracijaInternetuForm extends ActionForm{
	private String dataNuo;
	private String dataIki;
	private String deklTipas;
	
	public String getDeklTipas() {
		return deklTipas;
	}
	public void setDeklTipas(String deklTipas) {
		this.deklTipas = deklTipas;
	}
	public String getDataIki() {
		return dataIki;
	}
	public void setDataIki(String dataIki) {
		this.dataIki = dataIki;
	}
	public String getDataNuo() {
		return dataNuo;
	}
	public void setDataNuo(String dataNuo) {
		this.dataNuo = dataNuo;
	}
		
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		
		this.setDeklTipas((String)arg1.getParameter("deklTipas"));		
	}
	
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		
		ActionErrors errors = new ActionErrors();
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		if (!"".equals(dataNuo) && (dataNuo != null)){
			try {
				format.parse(dataNuo);
			}
	        catch (ParseException pe){
	        	request.setAttribute("errDataNuo", "");
	        	request.setAttribute("deklInternetuErrors", "");
	        	errors.add("errDataNuo", new ActionMessage("kfladjf jfkaslflfjksd"));
	        }
		}
		if (!"".equals(dataIki) && (dataIki != null)){
	        try {
				format.parse(dataIki);
	        }
	        catch (ParseException pe){
	        	request.setAttribute("errDataIki", "");
	        	request.setAttribute("deklInternetuErrors", "");
	        	errors.add("errDataIki", new ActionMessage("kfladjf jfkaslflfjksd"));
	        }
		}
		return errors;		
	}
	
	
}
