package com.algoritmusistemos.gvdis.web.forms;

import java.util.Iterator;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;

public class AsmKodasForm extends ActionForm
{
	private String asmKodas;

	/////////////////////////////////////
	public void setAsmKodas(String asmKodas){this.asmKodas = asmKodas;}
	public String getAsmKodas(){return this.asmKodas;}	

	
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		//long asmid = 0;
		Asmuo asmuo = null;
		String print = request.getParameter("print");
		
		if (!"on".equals(print)){
		HttpSession session = request.getSession();
		if (Constants.IN_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
			session.setAttribute(Constants.CENTER_STATE, Constants.IN_DECLARATION_CODE_FORM);
		if (Constants.OUT_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
			session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_CODE_FORM);
		if (Constants.CHNG_OUT_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
			session.setAttribute(Constants.CENTER_STATE, Constants.CHNG_OUT_DECLARATION_CODE_FORM);
		if (Constants.GVNA_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
			session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_CODE_FORM);
		}
		
		if (null == asmKodas || 0 == asmKodas.length()){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("error.asmKodas", "");
		}
		try {
			Long.parseLong(asmKodas); // reikalingas, kad patikrinti ar asm kodas yra number
			asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, asmKodas);
			
			// patikrinti ar asmuo yra mires
			// jei taip - uzfiksuoti klaida
			if (asmuo.getAsmMirtiesData() != null) {
				errors.add("title", new ActionMessage("error.asmuoMires"));
				request.setAttribute("error.asmuoMires", "");
			}
			else
				request.getSession().setAttribute("canSave","true");
		}
		catch (NumberFormatException nfe){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("error.asmKodas", "");
		}
		catch (ObjectNotFoundException onfe){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("error.asmKodas", "");
		}
		if(errors.isEmpty())
		{
			if(Constants.GVNA_DECLARATION_CODE_FORM.
		    		equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))))
		    {
				if(DeklaracijosDelegator.getInstance(request).isPersonHaveDeclaredResidence(request,asmuo.getAsmNr()))
				{
					errors.add("title", new ActionMessage("error.cantFillGVNA"));
					request.setAttribute("error.cantFillGVNA", "");	
				}		    	
		    }
		    else if(Constants.OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))) || 
		    		Constants.CHNG_OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))))
		    {
		    	GyvenamojiVieta vt = null;		
		    	GyvenamojiVieta vtTikrinimui = null;	
		    	GyvenamojiVieta vtNegalioja = null;	
		    	// V.L. 2009-10-19 
		    	// Papildyta tikrinimu paskutinës gyvenamosios vietos (nors ir negaliojanèia GV)
		    	GyvenamojiVieta vtPaskutineRegistruota = null;
		    	try {
					Set gv = asmuo.getGyvenamosiosVietos();
		        	for (Iterator it=gv.iterator(); it.hasNext(); ){
		        		vt = (GyvenamojiVieta)it.next();
		        		if (vtPaskutineRegistruota==null){
		        			//pgl hibernate sarisi asmuo-gv, paskutine gv visada pirma
		        			vtPaskutineRegistruota = vt;
		        		}

		        		if (vt.getGvtDataIki() == null){
		        			vtTikrinimui = vt;
		        			break;
		        		} 
		        		else {
		        			vtNegalioja = vt;
		        		}
		        			
		        	}
					if (vtTikrinimui!=null) {
						if (Constants.OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))) && ("U".equals(vtTikrinimui.getGvtTipas()) || "V".equals(vtTikrinimui.getGvtTipas()))) {
							errors.add("title", new ActionMessage("error.jauIsvykes"));
							request.setAttribute("error.jauIsvykes", "");	
						}

						//if (Constants.CHNG_OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))) && !("U".equals(vtTikrinimui.getGvtTipas()) || "V".equals(vtTikrinimui.getGvtTipas()))) {
						// praso praleisti 'U', 'V' ir nieko
						if (Constants.CHNG_OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))) && !("U".equals(vtTikrinimui.getGvtTipas()) || "V".equals(vtTikrinimui.getGvtTipas()))) {
							errors.add("title", new ActionMessage("error.neraIsvykes"));
							request.setAttribute("error.neraIsvykes", "");	
						}
					}
					else { // nieko
						if (!Constants.CHNG_OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE)))) {
							if (vtPaskutineRegistruota!=null){
								if (Constants.OUT_DECLARATION_CODE_FORM.equals(String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE))) && ("U".equals(vtPaskutineRegistruota.getGvtTipas()) || "V".equals(vtPaskutineRegistruota.getGvtTipas()))) {
									errors.add("title", new ActionMessage("error.jauIsvykes"));
									request.setAttribute("error.jauIsvykes", "");	
								}
							}
							else {
								Constants.Println(request, "error.neraDeklaraves");
								errors.add("title", new ActionMessage("error.neraDeklaraves"));
								request.setAttribute("error.neraDeklaraves", "");
							}
						}
						else {
							if (vtNegalioja != null){ // yra negaliojantis
								errors.add("title", new ActionMessage("error.neraIsvykes"));
								request.setAttribute("error.neraIsvykes", "");
							}
						}
					}
					
				}
		    	catch (NullPointerException npe) {
		    		errors.add("title", new ActionMessage("error.asmKodas"));
					request.setAttribute("error.asmKodas", "");
		    	}
		    }
		    /*
		    String state = String.valueOf(request.getSession().getAttribute(Constants.CENTER_STATE));
		    if(Constants.GVNA_DECLARATION_CODE_FORM.equals(state)
		    	|| Constants.OUT_DECLARATION_CODE_FORM.equals(state)	
		    	|| Constants.IN_DECLARATION_CODE_FORM.equals(state)
		    	|| Constants.GVNA_DECLARATION_FORM.equals(state)
		    	|| Constants.OUT_DECLARATION_FORM.equals(state)	
		    	|| Constants.IN_DECLARATION_FORM.equals(state)){
		    	if(asmuo.getAsmMirtiesData() != null){
					errors.add("title", new ActionMessage("error.asmuoMires"));
					request.setAttribute("error.asmuoMires", "");
				}
		    }
		    */
		}
		/*if (!errors.isEmpty() && !"on".equals(print)){
			HttpSession session = request.getSession();
			if (Constants.IN_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
				session.setAttribute(Constants.CENTER_STATE, Constants.IN_DECLARATION_CODE_FORM);
			if (Constants.OUT_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
				session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_CODE_FORM);
			if (Constants.CHNG_OUT_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
				session.setAttribute(Constants.CENTER_STATE, Constants.CHNG_OUT_DECLARATION_CODE_FORM);
			if (Constants.GVNA_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE))))
				session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_CODE_FORM);
			
		}
		*/
		return errors;
	}
}

