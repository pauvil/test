package com.algoritmusistemos.gvdis.web.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.GvdisAprasymasForm;
import com.algoritmusistemos.gvdis.web.persistence.AprModulisJN;

public class GvdisAprasymasJnAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws InternalException, PermissionDeniedException
		{
		    HttpSession session = request.getSession();
		    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_GVDIS_APRASYMAS_JN);
			UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
			
			long modulioId = Long.parseLong(request.getParameter("modulioId"));
			long versijosId = Long.parseLong(request.getParameter("versijosId"));
			
			List aprModulisJn = AprasymaiDelegator.getInstance().getAprModulisJN(request, modulioId, versijosId);
			
			List dbObjektaiJn = AprasymaiDelegator.getInstance().getDbObjektaiJN(request, modulioId, versijosId);
			session.setAttribute("aprModulisJn",aprModulisJn);
			session.setAttribute("dbObjektaiJn", dbObjektaiJn);

		    return (mapping.findForward(Constants.CONTINUE));
		}
}
