package com.algoritmusistemos.gvdis.web.actions.deklaracijos.PDF;



import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.reports.PDFReports;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
	   


public class GeneratePDFAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException, DatabaseException,InternalException
	{
		HttpSession session = request.getSession(); 
		session.removeAttribute("print");
		Deklaracija deklaracija = (Deklaracija)session.getAttribute("declaration");
		//PDFGenerator generator = new PDFGenerator(deklaracija,this.getServlet(),request);
    	//generator.generateDeklaracijaPDF(response);
		
		response.setContentType("application/pdf");
		String separator = System.getProperty("file.separator");
		String pathToJasperReports = this.getServlet().getServletConfig().getServletContext().getRealPath("") + separator + "files" + separator;
		String darbuotojas = session.getAttribute("userName")+" "+session.getAttribute("userSurname"); 
		Address adr = (Address) session.getAttribute("ankstesnisAdresas");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		String galiojaIKI="";
		GyvenamojiVieta gvt = null;
		String ankstAdresas = "";
		if (adr != null)
		{
			gvt = QueryDelegator.getInstance().getGyvenamojiVieta(request, deklaracija.getAsmuo().getAsmNr(), adr.getId());
			ankstAdresas = adr.getAdr();
		}
    	PDFReports reports = new PDFReports(deklaracija, pathToJasperReports, darbuotojas, 
    			String.valueOf(session.getAttribute("savivaldybes_pav")), ankstAdresas /*ankstesne_gv_pavadinimas"*/);    
    	if(null != gvt)
    		if(null != gvt.getGvtGaliojaIki())
	    	{
				galiojaIKI = dateFormat.format(gvt.getGvtGaliojaIki());
				reports.setDeklaracijaGaliojaIki(galiojaIKI);
	    	}
    	try {    		
			reports.makePdf(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
   	
		session.removeAttribute("declaration");
		session.removeAttribute("deklaracija_pilietybe");			
		//session.removeAttribute("ankstesne_gv");
		//session.removeAttribute("ankstesne_gv_pastabos");			
		session.removeAttribute("deklaracija_pilietybe");
		session.removeAttribute("savivaldybes_pav");	
		session.removeAttribute("savivaldybes_pav");
		session.removeAttribute("ankstesne_gv_pavadinimas");			
		session.removeAttribute("deklaracija_pilietybe");
		session.removeAttribute("deklaracija_oficialus_pavadinimas");		
		
    	return null;
	}
}
