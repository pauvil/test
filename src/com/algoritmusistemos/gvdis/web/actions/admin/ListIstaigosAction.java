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
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class ListIstaigosAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException,DatabaseException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
        session.setAttribute("menu_cell","istaigos");
	    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_LIST_ISTAIGOS);
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
	    
        // Rûðiavimas, puslapiavimas
        Paging paging = new Paging();
        Ordering ordering = (Ordering)session.getAttribute("istaigos_ordering");
        if (ordering == null){
            ordering = new Ordering();
        }
        if (ordering.getColumn() == null) ordering.setColumn("id"); // Pagal nutylëjimà
        paging.init(request);
        ordering.init(request);
        session.setAttribute("istaigos_paging", paging);
        session.setAttribute("istaigos_ordering", ordering);
	    
        List istaigos = ZinynaiDelegator.getInstance().getIstaigos(request, paging, ordering);
        request.setAttribute("istaigos", istaigos);
	    
	    return mapping.findForward(Constants.CONTINUE);
	}
}
