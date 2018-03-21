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
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;

public class OutDeclarationViewAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException,InternalException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_VIEW);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
	    
	    Long id = null;
	    
	    try
	    {
	    	id = Long.valueOf(request.getParameter("id"));
	    }catch(NumberFormatException nfe)
	    {
	    	throw new ObjectNotFoundException("IsvykimoDeklaracija with id["+request.getParameter("id")+"] not found");
	    }
	    
	    IsvykimoDeklaracija isd = DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(id,request);
	    //DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(request, isd); // I.N. 2010.01.25
	    request.setAttribute("outDeclaration",isd);
	    if(null != isd.getAsmuo())
	    {
			isd.getAsmuo().setVardasPavarde(isd.getAsmenvardis());
	    	request.setAttribute("asmuoForView",
	    			UserDelegator.getInstance().getAsmuoByAsmKodas(isd.getAsmuo().getAsmKodas().longValue(),request));
	    }
		// U�fiksuojame asmens duomen� per�i�ros fakt� auditui
    	if (isd != null) try {
    			long auditId = 0;
    			if(null != isd.getAsmuo())
    			{
	    			auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(isd.getAsmuo().getAsmKodas()), Constants.AUDIT_DEKL_PERZIURA, "I�vykimo deklaracijos per�i�ra");
	    			AuditDelegator.getInstance().auditPersonResult(request, auditId, isd.getAsmuo());
    			}
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neu�registruota paie�kos u�klausa auditui. Gri�kite � paie�kos form� ir pabandykite dar kart�.", nfe);
    	}	    
	    
	    
	    return (mapping.findForward(Constants.CONTINUE));
	}
}