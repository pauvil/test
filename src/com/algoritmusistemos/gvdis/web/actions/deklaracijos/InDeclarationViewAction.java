package com.algoritmusistemos.gvdis.web.actions.deklaracijos;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;

public class InDeclarationViewAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException,InternalException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.IN_DECLARATION_VIEW);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
	    
	    Long id = null;
	    
	    try
	    {
	    	id = Long.valueOf(request.getParameter("id"));
	    }catch(NumberFormatException nfe)
	    {
	    	throw new ObjectNotFoundException("AtvykimoDeklaracija with id["+request.getParameter("id")+"] not found");
	    }
	    
	    AtvykimoDeklaracija ad = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(id,request);
	    //DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(request, ad); // I.N. 2010.01.25
	    request.setAttribute("inDeclaration",ad);
	    if(null != ad.getAsmuo())
	    {
//	    	request.setAttribute("asmuoForView",
//	    			UserDelegator.getInstance().getAsmuoByAsmKodas(ad.getAsmuo().getAsmKodas().longValue(),request));
	    	ad.getAsmuo().setVardasPavarde(ad.getAsmenvardis());
	    	request.setAttribute("asmuoForView", ad.getAsmuo());
	    }
	    // Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	if (ad != null) try {
    			long auditId = 0;
    			if(null != ad.getAsmuo())
    			{
	    			auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(ad.getAsmuo().getAsmKodas()), Constants.AUDIT_DEKL_PERZIURA, "Atvykimo deklaracijos perþiûra");
	    			AuditDelegator.getInstance().auditPersonResult(request, auditId, ad.getAsmuo());
    			}
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}	    
	    
	    
	    
	    return (mapping.findForward(Constants.CONTINUE));
	}
}

