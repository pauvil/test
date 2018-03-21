package com.algoritmusistemos.gvdis.web.actions.query;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;

public class QueryCreateDataFormAction extends Action
{
    public QueryCreateDataFormAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ObjectNotFoundException, InternalException, DatabaseException
    {
        HttpSession session = request.getSession();
        session.setAttribute("CENTER_STATE", "QUERY_CREATE_DATA");
        session.removeAttribute("actGvtAsmNr");
        session.removeAttribute("actGvtNr");

        try {
        	long asmNr = Long.parseLong(request.getParameter("gvtAsmNr"));
        	session.setAttribute("actGvtAsmNr", new Long(asmNr));

	    	List valstybes = UtilDelegator.getInstance().getValstybes(request);
	    	session.setAttribute("valstybes", valstybes);
        }
        catch (NumberFormatException nfe){
        	throw new InternalException("Perduoti neteisingi parametrai", nfe);
        }
        
        return mapping.findForward("continue");
    }
}
