package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

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
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class DocumentsListAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
	    HttpSession session = request.getSession();
		Asmuo asmuo = (Asmuo)session.getAttribute("asmuo");
		session.setAttribute("asmensDokumentai", DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, asmuo, ""));
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
