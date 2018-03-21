package com.algoritmusistemos.gvdis.web.actions.nebaigtosdeklaracijos;

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
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;

public class CompleteDeclarationAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException,PermissionDeniedException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});	
	    HttpSession session = request.getSession();

	    Long l = new Long(request.getParameter("id"));
	    session.setAttribute("idForEdit",l);
	    
	    session.removeAttribute("lastDeclarationId");
		session.removeAttribute("activeDeclaration");
		session.removeAttribute("asmuo");
		session.removeAttribute("ankstesnisAdresas");
		request.removeAttribute("redaguoti_data"); //ju.k 2007.09.28
		
		Deklaracija d = DeklaracijosDelegator.getInstance(request).getDeklaracija(l,request);
		//System.out.println(d.getSaltinis());
		//d.getSaltinis()
	    if (null == session.getAttribute("asmuo")){
	    	if (d.getAsmuo() != null && d.getAsmenvardis() != null){
		    	d.getAsmuo().setVardasPavarde(d.getAsmenvardis());
		    	session.setAttribute("asmuo",d.getAsmuo());
	    	}
            if (d.getAsmuo() == null){
            	d.getLaikinasAsmuo().getGimimoData(); //netrinti, butina kad veiktu laikino asmens gimimo datos paemimimas chkNepilnametisReturn f-joj(2010-04-02)
	    		session.setAttribute("asmuo",d.getLaikinasAsmuo());	
	    	}


	    }
	    //session.setAttribute("saltinis", d.getSaltinis());
	    if (d instanceof AtvykimoDeklaracija){
		    session.setAttribute(Constants.CENTER_STATE, Constants.IN_DECLARATION_FORM);
		    session.setAttribute("declaration_mode",Constants.IN_DECLARATION_FORM);
		    if(0 == d.getBusena())
			{
		    	
				if(0 == d.getTmpGvtAtvNr().intValue() && 0 != d.getTmpGvtAdvNr().intValue())
				{
					session.setAttribute("addressId",Long.valueOf(String.valueOf(d.getTmpGvtAdvNr())));
					session.setAttribute("elementType","A");
				}
				else
				{
					session.setAttribute("addressId",Long.valueOf(String.valueOf(d.getTmpGvtAtvNr())));
					session.setAttribute("elementType","T");
				}
			}
		    if(null != d.getAsmuo())
		    {
			    String[] st = DeklaracijosDelegator.getInstance(request).chkAsmDekl(d.getAsmuo().getAsmNr(),((Long)request.getSession().getAttribute("userIstaigaId")).longValue(),"A",request);
				Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(d.getAsmuo().getAsmNr(), Long.parseLong(st[0]) , request);
				session.setAttribute("ankstesnisAdresas", adr);
				List dokumentai = DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, d.getAsmuo(), d.getDokumentoNr());
				session.setAttribute("asmensDokumentai", dokumentai);
		    }
			return (mapping.findForward(Constants.IN_DECLARATION_FORM));
	    }
    	if(d instanceof IsvykimoDeklaracija)
	    {    		
		    session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_FORM);
		    session.setAttribute("declaration_mode",Constants.OUT_DECLARATION_FORM);
		    if(null != d.getAsmuo())
		    {
			    String[] st = DeklaracijosDelegator.getInstance(request).chkAsmDekl(d.getAsmuo().getAsmNr(),((Long)request.getSession().getAttribute("userIstaigaId")).longValue(),"A",request);
				Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(d.getAsmuo().getAsmNr(), Long.parseLong(st[0]) , request);
				session.setAttribute("ankstesnisAdresas", adr);
				List dokumentai = DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, d.getAsmuo(), d.getDokumentoNr());
				session.setAttribute("asmensDokumentai", dokumentai);
		    }
		    return (mapping.findForward(Constants.OUT_DECLARATION_FORM));		    
	    }
    		
    	if(d instanceof GvnaDeklaracija)
	    {
		    session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_FORM);
		    session.setAttribute("declaration_mode",Constants.GVNA_DECLARATION_FORM);
		    
		    // Nustatyti savivaldybiu sarasa
		    Long istId = null;
	        Integer userStatus = (Integer)session.getAttribute("userStatus");
	        if (userStatus != null && userStatus.intValue() == UserDelegator.USER_GLOBAL)
		    	istId = null;
	        else
	    		istId = (Long)session.getAttribute("userIstaigaId");
	        List sav = QueryDelegator.getInstance().getSav(request, istId);
	    	session.setAttribute("savivaldybes", sav);
	    	
		    if(null != d.getAsmuo())
		    {	    	
			    String[] st = DeklaracijosDelegator.getInstance(request).chkAsmDekl(d.getAsmuo().getAsmNr(),((Long)request.getSession().getAttribute("userIstaigaId")).longValue(),"A",request);
				Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(d.getAsmuo().getAsmNr(), Long.parseLong(st[0]) , request);
				session.setAttribute("ankstesnisAdresas", adr);	    	
				List dokumentai = DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, d.getAsmuo(), d.getDokumentoNr());
				session.setAttribute("asmensDokumentai", dokumentai);
		    }
			return (mapping.findForward(Constants.GVNA_DECLARATION_FORM));		    
	    }
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
