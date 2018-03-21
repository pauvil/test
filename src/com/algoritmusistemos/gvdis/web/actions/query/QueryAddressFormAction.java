package com.algoritmusistemos.gvdis.web.actions.query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;

public class QueryAddressFormAction extends Action
{

    public QueryAddressFormAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ObjectNotFoundException
    {
        HttpSession session = request.getSession();
        session.setAttribute("menu_cell","queryaddressform");
        session.setAttribute("CENTER_STATE", Constants.QUERY_ADDRESS_FORM);
        request.setAttribute("priezastys", ZinynaiDelegator.getInstance().getZinynoReiksmes(request, "AUDITO_PRIEZASTIS"));
        return mapping.findForward("continue");
    }
}
