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
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;

public class GvnaDeclarationViewAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException,InternalException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_VIEW);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
	    
	    Long id = null;
	    
	    try
	    {
	    	id = Long.valueOf(request.getParameter("id"));
	    }catch(NumberFormatException nfe)
	    {
	    	throw new ObjectNotFoundException("GvnaDeklaracija with id["+request.getParameter("id")+"] not found");
	    }
	    

	    GvnaDeklaracija gd = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(id,request);
	    //DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(request, gd); // I.N. 2010.01.25
	    request.setAttribute("gvnaDeclaration",gd);
	    if(null != gd.getAsmuo())
	    {
	    	gd.getAsmuo().setVardasPavarde(gd.getAsmenvardis());
	    	request.setAttribute("asmuoForView",
	    			UserDelegator.getInstance().getAsmuoByAsmKodas(gd.getAsmuo().getAsmKodas().longValue(),request));
	    }

		// Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	if (gd != null) try {
    			long auditId = 0;
    			if(null != gd.getAsmuo())
    			{
	    			auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(gd.getAsmuo().getAsmKodas()), Constants.AUDIT_DEKL_PERZIURA, "GVNA deklaracijos perþiûra");
	    			AuditDelegator.getInstance().auditPersonResult(request, auditId, gd.getAsmuo());
    			}
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}

 	    	

	    
	    return (mapping.findForward(Constants.CONTINUE));
	}
}