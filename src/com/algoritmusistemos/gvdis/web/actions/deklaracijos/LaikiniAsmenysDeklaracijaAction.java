package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;

public class LaikiniAsmenysDeklaracijaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.TEMP_CITIZEN_DECLARATION);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);
	    session.setAttribute("salys",UtilDelegator.getInstance().getValstybes(request));
		request.getSession().setAttribute("asm_type","temp");
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
