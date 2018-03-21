package lt.solver.gvdis.web.actions.ataskaitos;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.AtaskaitosDAL;
import lt.solver.gvdis.dal.CommonObjDAL;
import lt.solver.gvdis.model.KaimaiObj;
import lt.solver.gvdis.web.forms.Report13Form;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;


public class Report13Action  extends Action {
	  
	private void outputCsv(HttpServletRequest req, HttpServletResponse resp, List results)   {
	
		resp.setContentType("application/vnd.ms-excel; charset=utf-16le");
			resp.addHeader("Content-disposition", "attachment; filename=ataskaita.csv");
			try {
				//PrintWriter out = new PrintWriter(resp.getOutputStream());
				PrintWriter out = resp.getWriter();
				out.write("\ufeff"); 		//BOM
				out.println("Nr.\tGrupë\tVyrø\tMoterø\tViso");
				for (Iterator it=results.iterator(); it.hasNext();){
					KaimaiObj kaimas = (KaimaiObj)it.next();
					out.println(kaimas.getNr() + "\t" + kaimas.getGrupe() + "\t" + kaimas.getVyru() + "\t" 
							+ kaimas.getMoteru() + "\t" + kaimas.getViso());
				}
				out.flush();
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
	}
	   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
    		HttpServletResponse response) 
     	throws PermissionDeniedException, DatabaseException
    { 
    	
    	UserDelegator.checkPermission(request, "RL_GVDIS_GL_TVARK"); //RL_GVDIS_ADMIN
    	
	    HttpSession session = request.getSession();
    	session.setAttribute("menu_cell","report13");
	    session.setAttribute(Constants.CENTER_STATE, Constants.REPORT13);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_STATISTICS);
	    
	    Report13Form reportsForm = (Report13Form) form;
	    session.setAttribute("rjSavivaldybe",reportsForm.getSavivaldybe());
	    session.setAttribute("rjRaportData",reportsForm.getRaportData());
	    session.setAttribute("rjSeniunija",reportsForm.getSeniunija());
	    session.setAttribute("rjKaimas",reportsForm.getKaimas());
	    
	    request.setAttribute("reportTitle", "Gyventojø skaièius teritorijoje");
	    
	    
	    Date raportoData = null;
	    Long savivaldybe = null;
	    Long seniunija = null;
	    Long kaimas = null;
	    
	    SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
	    formatter.setLenient(false);
	    
	    try { raportoData = formatter.parse(reportsForm.getRaportData()); }
	    catch (ParseException ignore){}
	    try {
			savivaldybe = Long.valueOf(reportsForm.getSavivaldybe());
		} catch (NumberFormatException ignore) {}
	    try {
			seniunija = Long.valueOf(reportsForm.getSeniunija());
		} catch (NumberFormatException ignore) {}
		try {
			if (seniunija == new Long(0)) 
				kaimas = new Long(0);
			else 
				kaimas = Long.valueOf(reportsForm.getKaimas());
		} catch (NumberFormatException ignore) {}
		
		// Uþkrauname savivaldybiø, seniûnijø kaimu sàraðà
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
        
        List kaimai = new ArrayList();
        if (seniunija != null && seniunija.longValue() != 0) try {
        	kaimai = CommonObjDAL.getInstance().getTeritorijos(request, seniunija);
        }catch (Exception ex){
        	ex.printStackTrace();
        }
        request.setAttribute("reportKaimai", kaimai);
        
        // Rezultatø formavimas
	    List results = null;
	    if (request.getAttribute("error") == null){ 
	    	if (kaimas != null && kaimas.longValue() != 0) {
	    		results = AtaskaitosDAL.getInstance().getTeritorijosReport13(request, kaimas, raportoData);
	    	}
	    }
	    request.setAttribute("results", results);
	    
	    if ("csv".equals(request.getParameter("output"))){
	    	outputCsv(request, response, results);
	    	return null;
	    }
	    else {
	    	return mapping.findForward(Constants.CONTINUE);
	    }
    }

}
