package com.algoritmusistemos.gvdis.web.forms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;

public class ZinynoReiksmeForm extends ActionForm
{
	private String id;
	private String kodas;	
	private String pavadinimas;	
	private String komentaras;	

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getKodas() { return kodas; }
	public void setKodas(String kodas) { this.kodas = kodas; }

	public String getKomentaras() { return komentaras; }
	public void setKomentaras(String komentaras) { this.komentaras = komentaras; }

	public String getPavadinimas() { return pavadinimas; }
	public void setPavadinimas(String pavadinimas) { this.pavadinimas = pavadinimas; }

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		if ("".equals(kodas)){
			errors.add("kodas", new ActionMessage("err.kodas"));
			request.setAttribute("errKodas", "on");
		}
		if ("".equals(pavadinimas)){
			errors.add("pavadinimas", new ActionMessage("err.pavadinimas"));
			request.setAttribute("errPavadinimas", "on");
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		Long actZinynoReiksmeId = (Long)session.getAttribute("actZinynoReiksmeId");
		if (actZinynoReiksmeId == null){
			id = "";
			kodas = "";
			pavadinimas = "";
			komentaras = "";
		}
		else {
			ZinynoReiksme zr = ZinynaiDelegator.getInstance().getZinynoReiksme(request, actZinynoReiksmeId.longValue());
			id = String.valueOf(zr.getId());
			kodas = zr.getKodas();
			pavadinimas = zr.getPavadinimas();
			komentaras = zr.getKomentaras();
		}
	}
}

