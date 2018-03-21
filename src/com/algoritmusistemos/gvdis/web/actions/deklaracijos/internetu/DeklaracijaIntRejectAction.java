package com.algoritmusistemos.gvdis.web.actions.deklaracijos.internetu;

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
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;

public class DeklaracijaIntRejectAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		session.setAttribute("CENTER_STATE", Constants.REJECT_DEKLARACIJA);	

		long id = Long.parseLong(request.getParameter("id"));
		Deklaracija deklaracija = null;
		try {
			deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracija(new Long(id), request);
			request.getSession().setAttribute("deklaracija", deklaracija);
			request.getSession().setAttribute("id", new Long(deklaracija.getId()));			
			request.setAttribute("gvtAsmNrAnkstesne", deklaracija.getGvtAsmNrAnkstesne());
		}
		catch (ObjectNotFoundException e) {
			e.printStackTrace(); /* I.N. 2010.01.18 */
		}
				
		return (mapping.findForward(Constants.CONTINUE));
	}
}
