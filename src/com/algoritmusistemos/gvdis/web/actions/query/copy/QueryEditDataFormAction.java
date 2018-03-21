package com.algoritmusistemos.gvdis.web.actions.query.copy;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;

public class QueryEditDataFormAction extends Action
{
    public QueryEditDataFormAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ObjectNotFoundException, InternalException, DatabaseException
    {
        HttpSession session = request.getSession();
        session.setAttribute("CENTER_STATE", "QUERY_EDIT_DATA");
        session.removeAttribute("actGyvenamojiVieta");

        try {
        	long asmNr = Long.parseLong(request.getParameter("gvtAsmNr"));
        	long gvtNr = Long.parseLong(request.getParameter("gvtNr"));

        	GyvenamojiVieta gyvenamojiVieta = QueryDelegator.getInstance().getGyvenamojiVieta(request, asmNr, gvtNr);
        	session.setAttribute("actGvtNr", new Long(gyvenamojiVieta.getGvtNr()));
        	session.setAttribute("actGvtAsmNr", new Long(gyvenamojiVieta.getGvtAsmNr()));
        	session.setAttribute("actGyvenamojiVieta", gyvenamojiVieta);

	    	List valstybes = UtilDelegator.getInstance().getValstybes(request);
	    	session.setAttribute("valstybes", valstybes);

	    	try {
        		long auditId = ((Long)session.getAttribute("lastAuditId")).longValue();
        		AuditDelegator.getInstance().auditGvtResult(request, auditId, gyvenamojiVieta);
        	}
        	catch (Exception nfe){
        		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
        	}
        }
        catch (NumberFormatException nfe){
        	throw new InternalException("Perduoti neteisingi parametrai", nfe);
        }
        
        return mapping.findForward("continue");
    }
}
