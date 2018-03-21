package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class PazymaForm extends ActionForm
{
	private String data;	
	private String prasymoData;	
	private String prasymoRegNr;	
	private String asmKodas;	
	private String vaikai;
	private String pastabos;

	public String getData() { return data; }
	public void setData(String data) { this.data = data; }

	public String getAsmKodas() { return asmKodas; }
	public void setAsmKodas(String asmKodas) { this.asmKodas = asmKodas; }

	public String getVaikai() { return vaikai; }
	public void setVaikai(String vaikai) { this.vaikai = vaikai; }

	public String getPastabos() { return pastabos; }
	public void setPastabos(String pastabos) { this.pastabos = UtilDelegator.trim(pastabos, 2000); }

	public String getPrasymoData() { return prasymoData; }
	public void setPrasymoData(String prasymoData) { this.prasymoData = prasymoData; }
	
	public String getPrasymoRegNr() { return prasymoRegNr; }
	public void setPrasymoRegNr(String prasymoRegNr) { this.prasymoRegNr = prasymoRegNr; }

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		// Jei perþiûrim jau esaèià paþymà, nieko validuoti nereikia -
		//  nors laukai tuðti, viskas bus uþkrauta pagal ID
		if (request.getParameter("id") != null){   
			return null;
		}
		
		ActionErrors errors = new ActionErrors();
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		Date pazymosData = null;
		int itrauktiVaikus = 0;

		try {
			pazymosData = format.parse(data);
		}
        catch (ParseException pe){
			errors.add("data", new ActionMessage("err.data"));
        	request.setAttribute("errData", "");
        }
		
        if (prasymoData != null && !"".equals(prasymoData)) try {
			format.parse(prasymoData);
		}
        catch (ParseException pe){
			errors.add("prasymoData", new ActionMessage("err.prasymodata"));
        	request.setAttribute("errPrasymoData", "");
        }

		try {
			Long.parseLong(asmKodas);
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, asmKodas);
			HttpSession session = request.getSession(); 
			int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
			long asmNr = 0;
            asmNr  = asmuo.getAsmNr();
            //Tieto:2012-05-10 Tikrinama ar pazyma isduota siandien. Jei isduota, tuomet kitos formuoti neleidziama
			if("".equals(getVaikai())){
				itrauktiVaikus = 0;
			}
			else{
				itrauktiVaikus = 1;
			}

			boolean siandienGvPazymaIsduota = PazymosDelegator.getInstance().getSiosDienosGvPazyma(request, asmNr, itrauktiVaikus, pazymosData);
			boolean siandienGvnaPazymaIsduota = PazymosDelegator.getInstance().getSiosDienosGvnaPazyma(request, asmNr, itrauktiVaikus, pazymosData);

			if(siandienGvPazymaIsduota || siandienGvnaPazymaIsduota){
				errors.add("title", new ActionMessage("error.pazymaSiandienIsduota"));
				request.setAttribute("error.pazymaSiandienIsduota", "");
			}
			//End Tieto:2012-05-10
			
			if (userStatus != UserDelegator.USER_GLOBAL){			         
		        Long istId = (Long)session.getAttribute("userIstaigaId");		       	        
	            if(!UserDelegator.getInstance().isAsmuoInIstaiga(request, asmuo, istId)){	            	
	            	errors.add("title", new ActionMessage("error.neprieinamas"));
    				request.setAttribute("error.neprieinamas", "");
	            }
			}

			// jei asmuo mires, paþymos iðduoti negalima
			// PASTABA: kai bus padaryta paþyma apie paskutinæ gv. ðis apribojimas
			//          turi bûti nuimtas, taip pat pazGvForm.jsp 
			//          <logic:present name="error.asmuoMires"> apdorojimas
			//          Apribojimas turi islikti GVNA pazymoms
			if (session.getAttribute(Constants.CENTER_STATE).equals(Constants.PAZ_GV_FORM))
				if (asmuo.getAsmMirtiesData() != null) {
					errors.add("title", new ActionMessage("error.asmuoMires"));
					request.setAttribute("error.asmuoMires", "");
				}
			if (session.getAttribute(Constants.CENTER_STATE).equals(Constants.PAZ_MIRE_FORM))
				if (asmuo.getAsmMirtiesData() == null) {
					errors.add("title", new ActionMessage("error.asmuoNeMires"));
					request.setAttribute("error.asmuoNeMires", "");
				}
		}
		catch (NumberFormatException nfe){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("errAsmKodas", "");
		}
		catch (ObjectNotFoundException onfe){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("errAsmKodas", "");
		}
		catch (DatabaseException dbe){
			//errors.add("title", new ActionMessage("error.asmKodas"));
			//request.setAttribute("errAsmKodas", "");
	}

        return errors;
	}	
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		data = sdf.format(new Date());
		pastabos = "";
		asmKodas = "";
		vaikai = "";
	}
}

