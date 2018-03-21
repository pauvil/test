package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.forms.LaikiniAsmenysDeklaracijaForm;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;

public class LaikiniAsmenysDeklaracijaPerformAction  extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws DatabaseException
	{
		HttpSession session = request.getSession();
		session.removeAttribute("activeDeclaration");
		session.removeAttribute("asmensDokumentai");
		session.removeAttribute("asmuo");
		session.removeAttribute("ankstesnisAdresas");
		session.removeAttribute("savewserror");
		
	    LaikiniAsmenysDeklaracijaForm ladf = (LaikiniAsmenysDeklaracijaForm)form;

	    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	    
	    Timestamp birthDate = null;
	    try {
	    	birthDate = new Timestamp(dateFormat.parse(ladf.getGimimoData()).getTime()); 
	    }
	    catch (ParseException pe){
	    	pe.printStackTrace(); /* I.N. 2010.01.18 */
	    	//docDate = new Timestamp((new Date()).getTime());
	    }

	    LaikinasAsmuo la =  DeklaracijosDelegator.getInstance(request).addLaikinasAsmuo(
	    		ladf.getVardas(),ladf.getPavarde(),
	    		//ladf.getKitiVardai(),
	    		ladf.getLytis(),ladf.getPilietybe(),
	    		 birthDate,ladf.getPastabos(),
				request);
	    session.setAttribute("asmuo",la);
		request.getSession().setAttribute("canSave","true");
	    
	    if(Constants.IN_DECLARATION_FORM.equals((String)session.getAttribute("declaration_mode")))
	    	return (mapping.findForward(Constants.CONTINUE_IN));
	    
	    if(Constants.OUT_DECLARATION_FORM.equals((String)session.getAttribute("declaration_mode")))
	    	return (mapping.findForward(Constants.CONTINUE_OUT));	
	    
	    if(Constants.GVNA_DECLARATION_FORM.equals((String)session.getAttribute("declaration_mode")))
	    {
		    Long istId = null;
	        Integer userStatus = (Integer)session.getAttribute("userStatus");
	        if (userStatus != null && userStatus.intValue() == UserDelegator.USER_GLOBAL)
		    	istId = null;
	        else
	    		istId = (Long)session.getAttribute("userIstaigaId");
			
	        // Gauname gilesnius adresus ðiam adresui
	        List sav = QueryDelegator.getInstance().getSav(request, istId);
	        session.setAttribute("savivaldybes", sav);
    		return (mapping.findForward(Constants.CONTINUE_GVNA));
	    }
	    return mapping.findForward(Constants.CONTINUE); 
	}
}