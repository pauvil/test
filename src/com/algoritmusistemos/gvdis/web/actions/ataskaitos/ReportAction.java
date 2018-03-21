package com.algoritmusistemos.gvdis.web.actions.ataskaitos;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.model.Report02Result;
import lt.solver.gvdis.model.Report08Result;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AtaskaitosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.ReportsForm;
import com.algoritmusistemos.gvdis.web.objects.Report07Result;
import com.algoritmusistemos.gvdis.web.objects.SkaiciausAtaskaitosEilute;
import com.algoritmusistemos.gvdis.web.objects.SkaiciausAtaskaitosEiluteGrupuota;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.ReportResult;

public class ReportAction extends Action 
{
    public ReportAction()
    {
    }
    
    private void outputCsv(HttpServletRequest req, HttpServletResponse resp, List results, int type, boolean grouped)
    {
    	//resp.setCharacterEncoding("UTF-8"); //
    	resp.setContentType("application/vnd.ms-excel; charset=utf-16le");
		resp.addHeader("Content-disposition", "attachment; filename=ataskaita.csv");
		String grupavimas = req.getParameter("grupavimas");
		if(type == 7 && grupavimas != null && !grupavimas.equals("0")){
			try {
				//PrintWriter out = new PrintWriter(resp.getOutputStream());
				PrintWriter out = resp.getWriter();
				out.write("\ufeff"); 		//BOM
				out.println("Nr.\tGrupë\tTaisymo\tKeitimo\tNaikinimo\tViso");
				int i = 0;
				for (Iterator it=results.iterator(); it.hasNext();){
					Report07Result result = (Report07Result)it.next();
					out.println(++i + "\t" + result.getName() + "\t" + result.getTaisymo()+ "\t" 
							+ result.getKeitimo()+ "\t" + result.getNaikinimo()+ "\t"+ result.getViso());
				}
				out.flush();
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
		else if (type == 2) {
			try {
				PrintWriter out = resp.getWriter();
				out.write("\ufeff"); 		//BOM
				out.println("Nr.\tGrupë\tDeklaracijas pateikæ deklaravimo ástaigose\tDeklaracijas pateikæ elektroniniu bûdu\tViso");
				int i = 0;
				for (Iterator it=results.iterator(); it.hasNext();){
					Report02Result result = (Report02Result)it.next();
					out.println(++i + "\t" + result.getName() + "\t" + result.getPerGvdis() + 
									"\t" + result.getElektroninis() + "\t" + result.getCount());
				}
				out.flush();
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
		else if (type == 8) {
			try {
				PrintWriter out = resp.getWriter();
				out.write("\ufeff"); 		//BOM
				out.println("Nr.\tGrupë\tPateikta praðymø\tGalioja laikotarpio pabaigoje");
				int i = 0;
				for (Iterator it=results.iterator(); it.hasNext();){
					Report08Result result = (Report08Result)it.next();
					out.println(++i + "\t" + result.getName() + "\t" + result.getCount()+ "\t" 
							+ result.getGalioja());
				}
				out.flush();
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
		else if(type == 12){
			try {
				//PrintWriter out = new PrintWriter(resp.getOutputStream());
				
				PrintWriter out = resp.getWriter();
				out.write("\ufeff"); 		//BOM				
				String data = (String) req.getSession().getAttribute("laikotarpioData");
				String sav = (String) req.getSession().getAttribute("savPavadinimas");
				String sen = (String) req.getSession().getAttribute("senPavadinimas");
				
				if (grouped) {   					
					if("yes".equals(req.getParameter("gvnt")))
						out.println("Savivaldybëse á apskaità átrauktø asmenø skaièius " + ((null == data)?"":data));
					else if(req.getAttribute("printSeniunijose") != null)
						out.println("Seniûnijose gyv. vietà deklaravusiø asmenø skaièius " + ((null == data)?"":data));
					else
						out.println(((null == sav)?"Savivaldybëse ":(sav+" seniûnijose ")) + "gyv. vietà deklaravusiø asmenø skaièius " + ((null == data)?"":data));
					out.println();					
					
					out.println("Nr.\tGrupë\tIð viso\tiki 7 m.\t7 - 16 m.\t16 - 18 m.\t18 - 25 m.\t25 - 45 m." +
							       "\t45 - 65 m.\t65 - 85 m.\tnuo 85 m.");
					int i = 0;
					for (Iterator it = results.iterator(); it.hasNext();) {
						SkaiciausAtaskaitosEiluteGrupuota result = (SkaiciausAtaskaitosEiluteGrupuota) it.next();
						out.println(++i + "\t" + result.getGrupe() + "\t" + result.getViso() + "\t"
								+ result.getM0_7() + "\t" + result.getM7_16() + "\t" + result.getM16_18() + "\t" + result.getM18_25()
								+ "\t" + result.getM25_45() + "\t" + result.getM45_65() + "\t" + result.getM65_85()
								+ "\t" + result.getM85_0() 
								);
					}
				} else {
					if("yes".equals(req.getParameter("gvnt")))
						out.println(((null == sav)?"":sav) + " á apskaità átrauktø asmenø skaièius " + ((null == data)?"":data));
					else
						out.println(((null == sav)?"":sav) + ((null == sen)?"":( ", " + sen)) + " gyv. vietà deklaravusiø asmenø skaièius " + ((null == data)?"":data));
					out.println();
					
					out.println("Nr.\tGrupë\tVyrø\tMoterø\tViso");
					int i = 0;
					for (Iterator it = results.iterator(); it.hasNext();) {
						SkaiciausAtaskaitosEilute result = (SkaiciausAtaskaitosEilute) it.next();
						out.println(++i + "\t" + result.getGrupe() + "\t" + result.getVyru() + "\t"
								+ result.getMoteru() + "\t" + result.getViso());
					}
				}
				out.flush();
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
		else{
			try {
				//PrintWriter out = new PrintWriter(resp.getOutputStream());
				PrintWriter out = resp.getWriter();
				out.write("\ufeff"); 		//BOM
				out.println("Nr.\tGrupë\tSkaièius grupëje");
				int i = 0;
				for (Iterator it=results.iterator(); it.hasNext();){
					ReportResult result = (ReportResult)it.next();
					out.println(++i + "\t" + result.getName() + "\t" + result.getCount());
				}
				out.flush();
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}

		}
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, ObjectNotFoundException
	{
    	
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.REPORT);
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_STATISTICS);
	    SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
	    formatter.setLenient(false);
	    ReportsForm reportsForm = (ReportsForm)form;
	    session.setAttribute("rjSavivaldybe",reportsForm.getSavivaldybe());
        session.setAttribute("rjDataNuo",reportsForm.getDataNuo());
        session.setAttribute("rjDataIki",reportsForm.getDataIki());      
        session.setAttribute("rjSeniunija",reportsForm.getSeniunija());
        
        // Nustatome ataskaitos tipà
        int reportType;
        try {
        	reportType = Integer.parseInt(request.getParameter("reportType"));
        }
        catch (Exception e){
        	reportType = AtaskaitosDelegator.TYPE_0801;
        }
        request.setAttribute("reportType", new Integer(reportType));

        // Puslapio antraðtë
        switch (reportType){
        	case AtaskaitosDelegator.TYPE_0801:
        		request.setAttribute("reportTitle", "Gyvenamàjà vietà deklaravusiø gyventojø skaièiaus ataskaita");
                session.setAttribute("menu_cell","report01");
        		break;
        	case AtaskaitosDelegator.TYPE_0802:
        		request.setAttribute("reportTitle", "Iðvykimà ið LR deklaravusiø gyventojø skaièiaus ataskaita");
                session.setAttribute("menu_cell","report02");        		
        		break;
        	case AtaskaitosDelegator.TYPE_0803:
        		request.setAttribute("reportTitle", "Asmenys, kuriø pateiktos iðvykimo deklaracijos galioja");
                session.setAttribute("menu_cell","report03");        		
        		break;
        	case AtaskaitosDelegator.TYPE_0805:
        		request.setAttribute("reportTitle", "Iðduotø paþymø apie asmens gyvenamàjà vietà skaièiaus ataskaita");
                session.setAttribute("menu_cell","report05");        		
        		break;
        	case AtaskaitosDelegator.TYPE_0807:
        		request.setAttribute("reportTitle", "Priimtø sprendimø dël deklaravimo duomenø taisymo, keitimo ir naikinimo ataskaita");
                session.setAttribute("menu_cell","report07");        		
        		break;
        	case AtaskaitosDelegator.TYPE_0808:
        		request.setAttribute("reportTitle", "Átrauktø á GVNA apskaità gyventojø skaièiaus ataskaita");
                session.setAttribute("menu_cell","report08");
        		break;
        	case AtaskaitosDelegator.TYPE_0810:
        		request.setAttribute("reportTitle", "Iðduotø paþymø apie átraukimà á GVNA apskaità skaièiaus ataskaita");
                session.setAttribute("menu_cell","report10");        		
        		break;
        	case AtaskaitosDelegator.TYPE_0811:
        		request.setAttribute("reportTitle", "Asmenø, kuriø deklaravimo duomenys turi bûti naikinami, ataskaita");
                session.setAttribute("menu_cell","report11");
        		break;
        	case AtaskaitosDelegator.TYPE_0812:
        		request.setAttribute("reportTitle", "Gyventojø skaièius seniûnijos (savivaldybës) teritorijoje");
                session.setAttribute("menu_cell","report12");
        		break;
        }
       
        // Parametrø paëmimas 
	    Date dataNuo = null;
	    Date dataIki = null;
	    Date gimDataNuo = null;
	    Date gimDataIki = null;
	    Long savivaldybe = null;
	    Long seniunija = null;
	    Long deklTipas = null;
	    Long grupavimas = null;
	    Date ataskaita12data = null;
	    
	    try { dataNuo = formatter.parse(reportsForm.getDataNuo()); }
	    catch (ParseException ignore){}
	    
	    try { ataskaita12data = formatter.parse(reportsForm.getAtaskaita12data());	}
	    catch (ParseException ignore){
	    	/* Negalima ignoruoti, nes paskui si data parsinama. A.S. paliktas bug'as 
				ataskaita12data = Calendar.getInstance().getTime();
			*/
	    }
	    
	    try { dataIki = formatter.parse(reportsForm.getDataIki()); }
	    catch (ParseException ignore){}
	    
	    try { gimDataNuo = formatter.parse(reportsForm.getGimDataNuo()); }
	    catch (ParseException ignore){}
	    
	    try { gimDataIki = formatter.parse(reportsForm.getGimDataIki()); }
	    catch (ParseException ignore){}
	    
	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
	    try { 
	    	switch (userStatus){
	    	case UserDelegator.USER_GLOBAL: 
	    		savivaldybe = Long.valueOf(reportsForm.getSavivaldybe());
	    		break;
	    	case UserDelegator.USER_SAV:
	    		savivaldybe = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	default: savivaldybe = null;
	    	}
	    } 
	    catch (NumberFormatException ignore){}
	    
	    try {  
	    	switch (userStatus){
	    	case UserDelegator.USER_SEN:
	    		seniunija = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	default:
	    		seniunija = Long.valueOf(reportsForm.getSeniunija());
	    	}
	    } 
	    catch (NumberFormatException ignore){}

	    try { deklTipas = Long.valueOf(reportsForm.getDeklTipas()); } 
	    catch (NumberFormatException ignore){}

	    try { grupavimas = Long.valueOf(reportsForm.getGrupavimas()); } 
	    catch (NumberFormatException ignore){}
	    
        // Uþkrauname savivaldybiø ir seniûnijø sàraðà
        request.setAttribute("reportSavivaldybes", JournalDelegator.getInstance().getSavivaldybes(request));
        List seniunijos = new ArrayList();
        if (savivaldybe != null && savivaldybe.longValue() != 0) try {
        	Istaiga objSavivaldybe = JournalDelegator.getInstance().getIstaiga(request, savivaldybe.longValue());
        	seniunijos = JournalDelegator.getInstance().getSeniunijos(request, objSavivaldybe);
        }
        catch (Exception ex){
        	ex.printStackTrace();
        }
        request.setAttribute("reportSeniunijos", seniunijos);

        // Rezultatø formavimas
	    List results = null; 
	    List resultsGVNA = null;
	    if (request.getAttribute("error") == null){ 
	    	switch (reportType){
	    	case AtaskaitosDelegator.TYPE_0801:
	    		results = AtaskaitosDelegator.getInstance().report0801(
	    				request, dataNuo, dataIki, savivaldybe, seniunija, 
	    				deklTipas, gimDataNuo, gimDataIki, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0802:
	    		results = AtaskaitosDelegator.getInstance().report0802(
	    				request, dataNuo, dataIki, savivaldybe, seniunija, gimDataNuo, gimDataIki, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0803:
	    		results = AtaskaitosDelegator.getInstance().report0803(
	    				request, dataNuo, dataIki, savivaldybe, seniunija, gimDataNuo, gimDataIki, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0805:
	    		results = AtaskaitosDelegator.getInstance().report0805(
	    				request, dataNuo, dataIki, savivaldybe, seniunija, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0807:
	    		results = AtaskaitosDelegator.getInstance().report0807(
	    				request, dataNuo, dataIki, savivaldybe, seniunija, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0808:
	    		results = AtaskaitosDelegator.getInstance().report0808(
	    				request, dataNuo, dataIki, savivaldybe, seniunija,
	    				gimDataNuo, gimDataIki, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0810:
	    		results = AtaskaitosDelegator.getInstance().report0810(
	    				request, dataNuo, dataIki, savivaldybe, seniunija, grupavimas
	    		);
	    		break;
	    	case AtaskaitosDelegator.TYPE_0812:
	    		
	    		results = AtaskaitosDelegator.getInstance().report0812(
	    				request, ataskaita12data,savivaldybe, seniunija, "R", grupavimas, userStatus);
	    		resultsGVNA = AtaskaitosDelegator.getInstance().report0812(request, ataskaita12data,
						savivaldybe, seniunija, "K", grupavimas, userStatus);
	    		request.setAttribute("results", results);
	    		request.setAttribute("resultsGVNA", resultsGVNA);
					    			    		
	    		//Exceliui papildoma info
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			String laikotarpioData = sdf.format(ataskaita12data);
    			session.setAttribute("laikotarpioData", laikotarpioData);
    			
    			session.removeAttribute("savPavadinimas");	
    			session.removeAttribute("senPavadinimas");
    			
	    		if(userStatus == UserDelegator.USER_SEN && seniunija != null){	    			
	    			Istaiga sen = (Istaiga)UtilDelegator.getInstance().getIstaiga(seniunija.longValue(), request);
	    			long sav = sen.getIstaiga().getId();  					
		    		Istaiga istSavivaldybe = JournalDelegator.getInstance().getIstaiga(request, sav);
		    		String savPavadinimas = "";
		    		if (istSavivaldybe != null)
		    			savPavadinimas = istSavivaldybe.getPavadinimas();
		    		session.setAttribute("savPavadinimas", savPavadinimas);		
		    		session.setAttribute("senPavadinimas", sen.getPavadinimas());
	    		}	
	    		else if(savivaldybe != null){
	    			Istaiga sav = (Istaiga)UtilDelegator.getInstance().getIstaiga(savivaldybe.longValue(), request);
	    			if(sav != null)
	    				session.setAttribute("savPavadinimas", sav.getPavadinimas());	
	    			if(seniunija != null){
	    				Istaiga sen = (Istaiga)UtilDelegator.getInstance().getIstaiga(seniunija.longValue(), request);
	    				if(sen != null){
	    					session.setAttribute("senPavadinimas", sen.getPavadinimas());	
	    				}
	    			}
	    		}
	    		break;
	    	}	 
	    	//Solver
	    	if(grupavimas != null && reportType == AtaskaitosDelegator.TYPE_0807 && grupavimas.longValue() > 0 )
	    		request.setAttribute("results07", results);	  
	    	else if (reportType == AtaskaitosDelegator.TYPE_0808)
    			request.setAttribute("results08", results);
	    	else if (reportType == AtaskaitosDelegator.TYPE_0802)
	    		request.setAttribute("results02", results);
	    	else
	    		request.setAttribute("results", results);
	    }
	    
	    boolean grouped = "yes".equals(request.getParameter("grouped"))?true:false;
	    if(grouped && grupavimas != null && grupavimas.intValue() == 3 && (seniunija == null || seniunija.longValue() == 0) && (savivaldybe == null || savivaldybe.longValue()== 0))
	    	request.setAttribute("printSeniunijose", "dummy");
	    
	    if ("csv".equals(request.getParameter("output"))){
	    	if("yes".equals(request.getParameter("gvnt")))
	    		outputCsv(request, response, resultsGVNA, reportType, grouped);	    	
	    	else
	    		outputCsv(request, response, results, reportType, grouped);
	    	return null;
	    }
	    else {
	    	return mapping.findForward(Constants.CONTINUE);
	    }
	}
}