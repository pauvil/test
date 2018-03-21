package com.algoritmusistemos.gvdis.web.actions.nebaigtosdeklaracijos;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;

public class DeleteDeclarationAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException,DatabaseException
	{
	    Long l = new Long(request.getParameter("id"));
	    Deklaracija d = DeklaracijosDelegator.getInstance(request).getDeklaracija(l, request);
	    HttpSession session = request.getSession();
	    if (d.getGyvenamojiVieta() == null){
	    	if(-1 == DeklaracijosDelegator.getInstance(request).deleteDeclaration(request, d))
	    	{
	    	    session.setAttribute(Constants.CENTER_STATE, Constants.CANNOT_DELETE_DECLARATION);	    		
	    	    return (mapping.findForward(Constants.CANNOT_DELETE_DECLARATION));
	    	}
	    }
	    
        String deklTipas = request.getParameter("deklTipas");
        if (null == deklTipas ){
			deklTipas = (String)request.getAttribute("deklTipas");
		}

	    try {
		    if("atv".equals(deklTipas)){
		    	response.sendRedirect("NotCompletedDeclarationsList.do?deklTipas=atv");
		    }
		    if("isv".equals(deklTipas)){
		    	response.sendRedirect("NotCompletedDeclarationsList.do?deklTipas=isv");
		    }
		    
	    } catch (IOException e) {
			e.printStackTrace();
		}

	    //return (mapping.findForward(Constants.CONTINUE));
	    return null;
	}
}
