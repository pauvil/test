package com.algoritmusistemos.gvdis.web.forms;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.Constants;


public class LaikiniAsmenysDeklaracijaForm extends ActionForm
{
	private String vardas;	
	private String pavarde;	
//	private String kitiVardai;	
	private String lytis;
	private String pilietybe;
	private String gimimoData;
	private String pastabos;	
	
	public void setVardas(String vardas){this.vardas = vardas;}
	public String getVardas(){return this.vardas;}	
	
	public void setPavarde(String pavarde){this.pavarde = pavarde;}
	public String getPavarde(){return this.pavarde;}	
	
//	public void setKitiVardai(String kitiVardai){this.kitiVardai = kitiVardai;}
//	public String getKitiVardai(){return this.kitiVardai;}	
	
	public void setLytis(String lytis){this.lytis = lytis;}
	public String getLytis(){return this.lytis;}	

	public void setPilietybe(String pilietybe){this.pilietybe = pilietybe;}
	public String getPilietybe(){return this.pilietybe;}	
	
	public void setGimimoData(String gimimoData){this.gimimoData = gimimoData;}
	public String getGimimoData(){return this.gimimoData;}
	
	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}	


	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		dateFormat.setLenient(false);
		if (null == vardas || 0 == vardas.length()){
			errors.add("title", new ActionMessage("error.missingVardas"));
			request.setAttribute("error.missingVardas", "");
		}
		if (null == pavarde || 0 == pavarde.length()){
			errors.add("title", new ActionMessage("error.missingPavarde"));
			request.setAttribute("error.missingPavarde", "");			
		}		
		if (null == lytis || 0 == lytis.length()){
			errors.add("title", new ActionMessage("error.missingLytis"));
			request.setAttribute("error.missingLytis", "");			
		}			
		if (null == pilietybe || 0 == pilietybe.length()){
			errors.add("title", new ActionMessage("error.missingPilietybe"));
			request.setAttribute("error.missingPilietybe", "");			
		}	
		
		try {
				Date d = dateFormat.parse(gimimoData);
				if(new Timestamp(System.currentTimeMillis()).before(d))
				{
					errors.add("title", new ActionMessage("error.biggerGimimoData"));
					request.setAttribute("error.biggerGimimoData", "");
				}
		}
		catch (ParseException pe){
			errors.add("title", new ActionMessage("error.missingGimimoData"));
			request.setAttribute("error.missingGimimoData", "");			
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{

	}
}