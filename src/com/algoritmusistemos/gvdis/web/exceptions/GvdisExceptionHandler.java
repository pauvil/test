package com.algoritmusistemos.gvdis.web.exceptions;

import gnu.regexp.RE;
import gnu.regexp.REMatch;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import com.algoritmusistemos.gvdis.web.Constants;

public class GvdisExceptionHandler extends ExceptionHandler
{
	public ActionForward execute(Exception e, ExceptionConfig ae, ActionMapping mapping,
								 ActionForm form,HttpServletRequest request,
								 HttpServletResponse response)
		throws ServletException
	{
		HttpSession session = request.getSession();
		String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
		request.getSession().setAttribute(Constants.CENTER_STATE_OLD, state);		
		request.getSession().setAttribute(Constants.CENTER_STATE, Constants.EXCEPTION);		
		
		String rootMsg = "";
		Exception tempException = null;
		Exception e3 = null;
		String errorMessage = "";
		String friendlyErr = " ";
		if (e instanceof DatabaseException){
			errorMessage = "Ávyko klaida duomenø bazëje."; // I.N.
			if (e.getMessage().indexOf("GVSPR_REG_NR_UK") > 0 ||
					e.getMessage().indexOf("GVPAZ_REG_NR_UK") > 0 ||
					e.getMessage().indexOf("GVNAPAZ_REG_NR_UK") > 0) {
				//Solver yra musu dominantys konstreintai
				//Keiciame klaidos pranesima
				friendlyErr = "(Toks registracijos numeris duomenø bazëje jau yra) ";
			}
			try{
				rootMsg = rootMessage(e);	
			}catch(Exception e2){
				if(e2.getMessage() != null && !e2.getMessage().equals(""))
					e3 = e2;
				else e3 = new Exception(e2);
			}
			if(!"".equals(rootMsg))
				tempException = new Exception(rootMsg);
		}
		else if (e instanceof InternalException){
			errorMessage = "Ávyko vidinë sistemos klaida.";
		}
		else if (e instanceof PermissionDeniedException){
			errorMessage = "Neturite teisiø atlikti ðá veiksmà.";
		}
		else if (e instanceof ObjectNotFoundException || e instanceof org.hibernate.ObjectNotFoundException){
			errorMessage = "Nurodytas áraðas nerastas.";
		}
		else {
			errorMessage = "Vykdant ðià operacijà ávyko klaida.";
		}
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		request.setAttribute("friendlyErrorMessage", friendlyErr);
		request.setAttribute("errorMessage", errorMessage);
		request.setAttribute("stackTrace", sw.getBuffer().toString());
		if(tempException != null){
			request.setAttribute("lastException", tempException);
			request.setAttribute("removeKreiptis", ""); //Tuo atveju panaikinti kreipini "Kreipkitës á sistemos administratoriø"
		}
		else
			request.setAttribute("lastException", e);
		if(e3 != null){
			StringWriter sw1 = new StringWriter();
			PrintWriter pw1 = new PrintWriter(sw1);
			e3.printStackTrace(pw1);		
			request.setAttribute("e3StackTrace", sw1.getBuffer().toString());
			request.setAttribute("e3Exception", e3);
		}
		return mapping.findForward("exception");		
	}

	private String rootMessage(Exception e) throws Exception{
		String causeMsg = "";
		int count = 0;
		Exception temp = e;
		//Pattern pattern = Pattern.compile("ORA-20\\d+");
		RE pattern = new RE("ORA-\\d+");
		while(count < 5 && temp != null){
			String str = temp.getMessage();
			//Matcher match = pattern.matcher(str);	
			REMatch match = null;
			if(str != null)
				match = pattern.getMatch(str);
			
			if(match != null){
				//String strArr[] = pattern.split(str);
				
				int end = match.getEndIndex();
				RE pattern2 = new RE("ORA-Err");
				match = pattern2.getMatch(str);
				int start=-1;
				if(match != null)
					start = match.getStartIndex();
				//String strArr = match.toString();
				/*String strTemp = strArr[1];
				strArr = strTemp.split("ORA-");
				strTemp = strArr[0];
				causeMsg = strTemp.substring(1);*/
				if(match != null)
					causeMsg = str.substring(end + 1, start -1); //sitaip !
				else 
					causeMsg = str.substring(end + 1);
				break;
			}
			count++;
			temp = (Exception) temp.getCause();
		}
		return causeMsg;
	}

}
