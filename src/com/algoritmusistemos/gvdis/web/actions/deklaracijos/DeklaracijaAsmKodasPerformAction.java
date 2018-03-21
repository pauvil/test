package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.AsmKodasForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.forms.AtvDeklaracijaForm;
public class DeklaracijaAsmKodasPerformAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
	    HttpSession session = request.getSession();
	    AsmKodasForm akf = (AsmKodasForm)form;
		session.removeAttribute("activeDeclaration");
		session.removeAttribute("asmensDokumentai");
		session.removeAttribute("asmuo");
		session.removeAttribute("ankstesnisAdresas");
		session.removeAttribute("selectedRysisSuGv");
		session.removeAttribute("checkedSavininkoIgaliotinis");
		session.removeAttribute("savewserror");
		session.removeAttribute("print");
		
	    String type = "";
	    if(((String)session.getAttribute("declaration_mode")).equals(Constants.IN_DECLARATION_FORM))
	    {
		    session.setAttribute(Constants.CENTER_STATE, Constants.IN_DECLARATION_FORM);
		    type = "A";
	    }
	    
	    if(((String)session.getAttribute("declaration_mode")).equals(Constants.OUT_DECLARATION_FORM))
	    {
	    	session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_FORM);	
		    type = "I";
	    }

	    if(((String)session.getAttribute("declaration_mode")).equals(Constants.CHNG_OUT_DECLARATION_FORM))
	    {
	    	session.setAttribute(Constants.CENTER_STATE, Constants.CHNG_OUT_DECLARATION_FORM);	
		    type = "I";
	    }
	    
	    
	    if(((String)session.getAttribute("declaration_mode")).equals(Constants.GVNA_DECLARATION_FORM))
	    {
	    	session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_FORM);
	    	
	        //--->>>ju.k 2007.08.24
		    Long istId = null;
	        Integer userStatus = (Integer)session.getAttribute("userStatus");
	        if (userStatus != null && userStatus.intValue() == UserDelegator.USER_GLOBAL)
		    	istId = null;
	        else
	    		istId = (Long)session.getAttribute("userIstaigaId");
			
	        // Gauname gilesnius adresus ðiam adresui
	        List sav = QueryDelegator.getInstance().getSav(request, istId);
	        session.setAttribute("savivaldybes", sav);
	        //<<<---
	    	
	    	
	    	//session.setAttribute("savivaldybes",AdresaiDelegator.getInstance().getRajonoSavivaldybes(request));
		    type = "N";
	    } 
	    String[] st = DeklaracijosDelegator.getInstance(request).chkAsmDekl(Long.parseLong(akf.getAsmKodas()),((Long)request.getSession().getAttribute("userIstaigaId")).longValue(),type,request);
	    
		Asmuo asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(akf.getAsmKodas()),request);
		if (null != asmuo){
			session.setAttribute("asmuo",asmuo);
			Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request);
			session.setAttribute("ankstesnisAdresas", adr);
			List dokumentai = DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, asmuo, "");
			session.setAttribute("asmensDokumentai", dokumentai);
			
		}
	    session.setAttribute("asm_type","const");
	    session.setAttribute("currentAdr",new Long(0));

	    if(!"0".equals(st[0]))
	    {
	    	try{
	        	response.sendRedirect("CompleteDeclaration.do?id="+st[0]);
	        }catch(IOException ioe){ioe.printStackTrace();}
	        return null;
	    }
	    if(null != st[1])
	    {
	    	request.setAttribute("warning",st[1]);
	    	session.setAttribute(Constants.CENTER_STATE, Constants.HAVE_NOT_COMPLETED_DECLARATION);
	    	return (mapping.findForward(Constants.CONTINUE_WARNING));
	    }

	    return (mapping.findForward(Constants.CONTINUE));
	}
}
