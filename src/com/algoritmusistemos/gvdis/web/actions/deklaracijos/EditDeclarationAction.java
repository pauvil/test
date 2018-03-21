package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

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
import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;

public class EditDeclarationAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException, InternalException, DatabaseException
	{
	    HttpSession session = request.getSession();
	    session.removeAttribute("lastDeclarationId");
	    session.removeAttribute("asmensDokumentai");
		session.removeAttribute("asmuo");
		session.removeAttribute("ankstesnisAdresas");
		session.removeAttribute("asm_id");
		session.removeAttribute("activeDeclaration");
		session.removeAttribute("idForEdit");
		request.removeAttribute("redaguoti_data"); //ju.k 2007.09.28
		session.removeAttribute("selectedRysisSuGv");
		session.removeAttribute("checkedSavininkoIgaliotinis");
	   
		String type = request.getParameter("type");
    	Deklaracija deklaracija = null;
    	Long id = Long.valueOf(request.getParameter("id"));

	    if ("A".equals(type)){
		    AtvykimoDeklaracija dek = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(id, request);
	    	session.setAttribute(Constants.CENTER_STATE, Constants.IN_DECLARATION_FORM);
		    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);
		    session.setAttribute("declaration_mode", Constants.IN_DECLARATION_FORM);
		    deklaracija = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(id, request);
			String pilietybe = "";
			try {
				pilietybe = deklaracija.getPilietybe().getPilietybe();
			} catch (Exception e){}
			session.setAttribute("deklaracija_pilietybe",pilietybe);
			
			if (deklaracija.getBusena()==1) //ju.k 2007.09.28
			{request.setAttribute("redaguoti_data","false");}
			String s = Long.toString(dek.getRysysSuGv());
			String s2 = Long.toString(dek.getSavininkoIgaliotinis());
			session.setAttribute("selectedRysisSuGv",s);
			session.setAttribute("checkedSavininkoIgaliotinis",s2);
			if (dek.getRysysSuGv()!=3){
				dek.setRysysSuGvKita("");	
				}
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
			String deklGaliojimoData ="";
			/*
			if (null != deklaracija.getDeklaracijaGalioja())
			{
				deklaracija.setDeklaracijaGalioja(dek.getDeklaracijaGalioja());
				deklGaliojimoData = dateFormat.format(deklaracija.getDeklaracijaGalioja());
				session.setAttribute("deklaracijaGaliojaMetai", deklGaliojimoData.substring(0, 3));
				request.setAttribute("deklaracijaGaliojaMetai", deklGaliojimoData.substring(0, 3));
				session.setAttribute("deklaracijaGaliojaMenuo", deklGaliojimoData.substring(5, 6));
				session.setAttribute("deklaracijaGaliojaDiena", deklGaliojimoData.substring(8, 9));
				String metai = deklGaliojimoData.substring(0, 4);
				String menuo = deklGaliojimoData.substring(5, 7);
				String diena = deklGaliojimoData.substring(8, 10);
				String data = metai + menuo + diena;
			}
			*/
	    }
	    else if ("I".equals(type)){
	    	session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_FORM);	
	    	request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);
		    session.setAttribute("declaration_mode", Constants.OUT_DECLARATION_FORM);
		    deklaracija = DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(id, request);
			if (deklaracija.getBusena()==1) //ju.k 2007.09.28
			{request.setAttribute("redaguoti_data","false");}
			session.setAttribute("ankstesne_gv",deklaracija.getAnkstesneGV().getPavadinimas());
			session.setAttribute("ankstesne_gv_pastabos",deklaracija.getAnkstesneVietaValstybesPastabos());			
			String pilietybe = "";
			try {
				pilietybe = deklaracija.getPilietybe().getPilietybe();
			} catch (Exception e){}
			session.setAttribute("deklaracija_pilietybe",pilietybe);
	    }
	    else if ("N".equals(type)){
	    	session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_FORM);
	    	request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);
		    session.setAttribute("declaration_mode", Constants.GVNA_DECLARATION_FORM);
			deklaracija = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(id, request);
		    if (deklaracija.getBusena()==1) //ju.k 2007.09.28
			{request.setAttribute("redaguoti_data","false");}
		    
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
		    
	    	//session.setAttribute("savivaldybes", AdresaiDelegator.getInstance().getRajonoSavivaldybes(request)); //kom ju.k 2007.08.24
		    deklaracija = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(id, request);
		    GvnaDeklaracija gvnaDeklaracija = (GvnaDeklaracija)deklaracija;
			session.setAttribute("savivaldybes_pav",gvnaDeklaracija.getSavivaldybe().getTerPav());	
			session.setAttribute("savivaldybes_pav",gvnaDeklaracija.getSavivaldybe().getTerPav());
			if(null != deklaracija.getAnkstesneGV())session.setAttribute("ankstesne_gv_pavadinimas",deklaracija.getAnkstesneGV().getPavadinimas());			
			String pilietybe = "";
			try {
				pilietybe = deklaracija.getPilietybe().getPilietybe();
			} catch (Exception e){}
			session.setAttribute("deklaracija_pilietybe",pilietybe);
	    }
	    else {
	    	throw new InternalException("Neteisingai paduoti deklaracijos parametrai");
	    }
	    
		if (null != deklaracija.getGyvenamojiVieta() && null != deklaracija.getGyvenamojiVieta().getAsmuo() && deklaracija.getLaikinasAsmuo() == null){
			if (deklaracija.getGyvenamojiVieta().getAsmuo() != null) { 
				deklaracija.getGyvenamojiVieta().getAsmuo().setVardasPavarde(deklaracija.getAsmenvardis());
			}
			Asmuo asmuo = deklaracija.getGyvenamojiVieta().getAsmuo();
			session.setAttribute("asmuo", asmuo);
			List dokumentai = DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, asmuo, deklaracija.getDokumentoNr());
			session.setAttribute("asmensDokumentai", dokumentai);
			Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), id.longValue(), request);
			session.setAttribute("ankstesnisAdresas", adr);
		    session.setAttribute("asm_type", "const");
		    session.setAttribute("currentAdr", new Long(0));
		}
		else {
		    session.setAttribute(Constants.CENTER_STATE, Constants.CANNOT_EDIT_DECLARATION);
			return mapping.findForward(Constants.CANNOT_EDIT_DECLARATION);
			//throw new InternalException("Ðios deklaracijos redaguoti negalima");
		}
		session.setAttribute("activeDeclaration", deklaracija);
	    session.setAttribute("idForEdit", id);
		session.setAttribute("declaration",deklaracija);
		session.setAttribute("deklaracija_oficialus_pavadinimas",deklaracija.getIstaiga().getOficialusPavadinimasRead());

	    return mapping.findForward(Constants.CONTINUE);
	}
}
