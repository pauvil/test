package com.algoritmusistemos.gvdis.web.actions.ataskaitos;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AtaskaitosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.ReportsForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class Report11Action extends Action 
{
    private void outputCsv(HttpServletRequest req, HttpServletResponse resp, List results)
    {
		resp.setContentType("application/vnd.ms-excel; charset=utf-16le");
		resp.addHeader("Content-disposition", "attachment; filename=ataskaita.csv");
		try {
			//PrintWriter out = new PrintWriter(resp.getOutputStream());
			PrintWriter out = resp.getWriter();
			out.write("\ufeff"); 		//BOM
			out.println("Nr.\tAsmens kodas\tVardas\tPavardë");
			int i = 0;
			for (Iterator it=results.iterator(); it.hasNext();){
				Asmuo asmuo = (Asmuo)it.next();
				out.println(++i + "\t" + asmuo.getAsmKodas() + "\t" + asmuo.getVardas() + "\t" + asmuo.getPavarde());
			}
			out.flush();
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, ObjectNotFoundException
	{
	    HttpSession session = request.getSession();
        session.setAttribute("menu_cell","report11");
	    session.setAttribute(Constants.CENTER_STATE, Constants.REPORT11);
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_STATISTICS);	    
	    SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
	    formatter.setLenient(false);
	    ReportsForm reportsForm = (ReportsForm)form;
	    session.setAttribute("rjSavivaldybe",reportsForm.getSavivaldybe());
        // Uþkrauname savivaldybiø ir seniûnijø sàraðà
        request.setAttribute("reportSavivaldybes", JournalDelegator.getInstance().getSavivaldybes(request));

        // Parametrø paëmimas 
	    Long savivaldybe = null;
	    Long seniunija = null;

	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
	    try { 
	    	switch (userStatus){
	    	case UserDelegator.USER_GLOBAL: 	    		
	    		if ("".equals(reportsForm.getView()) == true) return mapping.findForward(Constants.CONTINUE);
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
	    catch (NumberFormatException ignore){ignore.printStackTrace(); /* I.N. 2010.01.18 */}

        //List seniunijos = new ArrayList(); //kom ju.k 2007.09.24 uzkomentuotas seniuniju saraso formavimas
        //try {
        // 	Istaiga objSavivaldybe = JournalDelegator.getInstance().getIstaiga(request, savivaldybe.longValue());
        //	seniunijos = JournalDelegator.getInstance().getSeniunijos(request, objSavivaldybe);
        //    }
        //catch (Exception ex){
        //	ex.printStackTrace();
        //}
        //request.setAttribute("reportSeniunijos", seniunijos);
        //is jsp:
        //	<logic:lessThan name="userStatus" value="2">
        //<tr>
        //	<td width="10%"></td>
        // 	<td>
        //		<b>Seniûnija:</b>
        //	</td>
        //	<td colspan="2">
        //		<html:select property="seniunija" styleClass="input" onchange="document.ReportsForm.submit();">
        //			<html:option value="0">--- Visos ---</html:option>
        //			<html:options collection="reportSeniunijos" property="id" labelProperty="pavadinimas" />
        //		</html:select>
        //	</td>
        //</tr>
        //</logic:lessThan>
        

        // Rezultatø formavimas
	    List results = null; 
	    if (request.getAttribute("error") == null){ 
    		results = AtaskaitosDelegator.getInstance().report0811(request, savivaldybe, seniunija);
	    	request.setAttribute("results", results);
	    }

	    if ("csv".equals(request.getParameter("output"))){
	    	outputCsv(request, response, results);
	    	return null;
	    }
	    else {
	    	return mapping.findForward(Constants.CONTINUE);
	    }
	}
}