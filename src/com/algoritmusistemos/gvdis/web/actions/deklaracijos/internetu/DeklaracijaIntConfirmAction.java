package com.algoritmusistemos.gvdis.web.actions.deklaracijos.internetu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;

public class DeklaracijaIntConfirmAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		session.setAttribute("CENTER_STATE", Constants.CONFIRM_DEKLARACIJA);	
		
		return (mapping.findForward(Constants.CONTINUE));
	}
}
