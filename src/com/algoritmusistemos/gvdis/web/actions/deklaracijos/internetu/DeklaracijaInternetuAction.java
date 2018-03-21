package com.algoritmusistemos.gvdis.web.actions.deklaracijos.internetu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.DeklaracijaInternetuForm;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class DeklaracijaInternetuAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_WEB_DEKL);
		String deklTipas = request.getParameter("deklTipas");
		if (null == deklTipas ){
			deklTipas = (String)request.getAttribute("deklTipas");
		}
		
		//if(null != request.getParameter("rodyti")) {
		if (true){
			try{ 				
				List deklaracijos = null;				
				if ("atv".equals(deklTipas)){
					session.setAttribute("menu_cell","AtvDeklaracijaInternetuAction");
					deklaracijos = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracijaInternetu(request, (DeklaracijaInternetuForm)form);	
				} else if ("isv".equals(deklTipas)){
					session.setAttribute("menu_cell","IsvDeklaracijaInternetuAction");
					deklaracijos = DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracijaInternetu(request, (DeklaracijaInternetuForm)form);
				} else if ("gvna".equals(deklTipas)){
					deklaracijos = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracijaInternetu(request, (DeklaracijaInternetuForm)form);
				} 			
				request.setAttribute("deklaracijos", deklaracijos);
			}
			catch (ObjectNotFoundException e ){
			}
		}
		
		Ordering ordering = (Ordering)session.getAttribute("query_atvdeklaracijos_internetu_ordering");
        if (ordering == null){
            ordering = new Ordering();
        }
        if (ordering.getColumn() == null) ordering.setColumn("REG_NR"); // Pagal nutylëjimà
        ordering.setDirection("desc");
        ordering.init(request);
        
        Paging paging = (Paging)session.getAttribute("query_atvdeklaracijos_internetu_paging");
        if (ordering == null){
            ordering = new Ordering();
        }
        if (ordering.getColumn() == null) ordering.setColumn("REG_NR"); // Pagal nutylëjimà
        ordering.setDirection("desc");
        ordering.init(request);
        
        
        session.setAttribute("query_atvdeklaracijos_internetu_ordering", ordering);
        session.setAttribute("query_atvdeklaracijos_internetu_paging", paging);        
        
		session.setAttribute("CENTER_STATE", Constants.DEKLARACIJA_INTERNETU);

		return (mapping.findForward(Constants.CONTINUE));
	}

}
