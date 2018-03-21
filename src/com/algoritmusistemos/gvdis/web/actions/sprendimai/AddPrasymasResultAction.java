package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class AddPrasymasResultAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession session = request.getSession();
	    session.setAttribute("POPUP_STATE", Constants.SPR_ADD_PRASYMAS_RESULT);
	    String id = request.getParameter("id");
	    PrasymasKeistiDuomenis prasymas = SprendimaiDelegator.getInstance().getPrasymas(request, Long.parseLong(id));
	  
	    String asmenys[] = new String[prasymas.getAsmenys().size()];
		int i = 0;
		for (Iterator it=prasymas.getAsmenys().iterator(); it.hasNext(); ){
			Asmuo asmuo = (Asmuo)it.next();
			asmenys[i++] = String.valueOf(asmuo.getAsmNr());
		}
		//if(!((SprendimasForm) form).getFixTipas().equals(""))
		request.setAttribute("prasymas", prasymas);
		request.setAttribute("asmenys", asmenys);
		
		return mapping.findForward(Constants.CONTINUE);
	}

}
