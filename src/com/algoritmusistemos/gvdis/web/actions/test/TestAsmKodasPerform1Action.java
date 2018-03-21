package com.algoritmusistemos.gvdis.web.actions.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.forms.TestAsmKodasForm1;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class TestAsmKodasPerform1Action extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});	
		HttpSession session = request.getSession();       
		session.setAttribute(Constants.CENTER_STATE, Constants.TEST_IN_DECLARATION_VIEW);	   
		TestAsmKodasForm1 akf = (TestAsmKodasForm1) form;		
					
		Asmuo asmuo1 = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(akf.getAsmKodas()),request);
		session.setAttribute("asmuo", asmuo1);		
		return mapping.findForward(Constants.CONTINUE);
	}

}
