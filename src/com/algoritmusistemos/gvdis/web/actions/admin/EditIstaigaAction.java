package com.algoritmusistemos.gvdis.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.EditIstaigaForm;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;

public class EditIstaigaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, PermissionDeniedException
	{
		EditIstaigaForm iForm = (EditIstaigaForm)form;
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
		long id = Long.parseLong(request.getParameter("id"));
		Istaiga istaiga = JournalDelegator.getInstance().getIstaiga(request, id);
		istaiga.setOficialusPavadinimas(iForm.getOficialusPavadinimas());
		istaiga.setRekvizSpausdinimui(iForm.getRekvizSpausdinimui());
		ZinynaiDelegator.getInstance().saveIstaiga(request, istaiga);
		
	    return mapping.findForward(Constants.CONTINUE);
	}
}
