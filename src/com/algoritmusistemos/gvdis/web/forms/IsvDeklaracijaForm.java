package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
//import com.algoritmusistemos.gvdis.web.persistence.Asmuo; I.N.
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;
import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.persistence.Valstybe;


public class IsvDeklaracijaForm extends DeklaracijaForm
{
	private String isvykimoMetai;
	private String isvykimoMenuo;
	private String isvykimoData;	
	private String pilietybe;
	
	public void setIsvykimoMetai(String isvykimoMetai){this.isvykimoMetai = isvykimoMetai;}
	public String getIsvykimoMetai(){return this.isvykimoMetai;}		
	
	public void setIsvykimoMenuo(String isvykimoMenuo){this.isvykimoMenuo = isvykimoMenuo;}
	public String getIsvykimoMenuo(){return this.isvykimoMenuo;}		
	
	public void setIsvykimoData(String isvykimoData){this.isvykimoData = isvykimoData;}
	public String getIsvykimoData(){return this.isvykimoData;}	

	public void setPilietybe(String pilietybe){this.pilietybe = pilietybe;}
	public String getPilietybe(){return this.pilietybe;}	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request);
		HttpSession session = request.getSession();
		
		// Tikrina tik norint iðsaugoti deklaracijà. Norint spausdinti deklaracijà nustatyti ðalies,ið kurios atvyko nebûtina.
		String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
		
		if ("save".equals(request.getParameter("mode"))) {
			
			if(getAtvykoIsUzsienioSalis()==null || getAtvykoIsUzsienioSalis().equals(""))
				UtilDelegator.setError("error.numatomaSalis", errors, request);
		
			if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) {	
				try {
					dateFormat.parse(isvykimoMetai + "-" + isvykimoMenuo + "-" + isvykimoData);
				}
				catch (ParseException pe){
					UtilDelegator.setError("error.isvykimoData", errors, request);
				}
			}	
						
			try {
				Date deklData = new Date();
				deklData = CalendarUtils.getParsedDate(getDeklaravimoMetai(),getDeklaravimoMenuo(),getDeklaravimoData());			
				
				if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) {
					try {
						Date isvykData = new Date();
						isvykData = CalendarUtils.getParsedDate(getIsvykimoMetai(), getIsvykimoMenuo(), getIsvykimoData());
						
						Date maxIsvykData;
						Calendar calendar = Calendar.getInstance();
						
						int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();					
						
						calendar.setTime(deklData);
						calendar.add(Calendar.DAY_OF_MONTH, 7);
						maxIsvykData= calendar.getTime();
						
						if (isvykData.after(maxIsvykData) && userStatus != UserDelegator.USER_GLOBAL) {		
							UtilDelegator.setError("error.isvykimoDataMax", errors, request);			
						}
					}
					catch (NullPointerException npe){					
						UtilDelegator.setError("error.isvykimoData", errors, request);
					}
				}
				else { // keitimas gyvenamosios vietos
					Address ankstesnisAdresas = (Address)session.getAttribute("ankstesnisAdresas");
					
					Valstybe valstybe = UtilDelegator.getInstance().getValstybe(getAtvykoIsUzsienioSalis(), request);
					
					if(valstybe != null && ankstesnisAdresas != null && ankstesnisAdresas.getAdr().equals(valstybe.getPavadinimas()))
						UtilDelegator.setError("error.vienodaSalis", errors, request);
					
				}
			}
			catch (NullPointerException npe){
				if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM) || (Address)session.getAttribute("ankstesnisAdresas") != null) {
					UtilDelegator.setError("error.deklaravimoData", errors, request);
				}
			}				
			
			if ("-1".equals(getPilietybe())) {
				UtilDelegator.setError("error.nenurodytaPilietybe", errors, request);  
			   }	
		}
		
		// Validuojame tik tada, kai redaguojama baigta deklaracija
		if (session.getAttribute("activeDeclaration") != null){
			
			//Asmuo asmuo = (Asmuo)session.getAttribute("asmuo"); I.N
			//if ("".equals(getAsmensDokumentoNumeris())){ //ju.k 2007.06.30
			//	UtilDelegator.setError("error.asmensDokumentoNumeris", errors, request);
			//}
		
			//if (!QueryDelegator.getInstance().hasDocument(request, asmuo, getAsmensDokumentoNumeris())){ //ju.k 2007.06.30
			//	UtilDelegator.setError("error.asmensDokumentoNumeris", errors, request);
			//}
			
			//if ("".equals(getAsmensDokumentoIsdave())){ //ju.k 2007.06.30
			//	UtilDelegator.setError("error.asmensDokumentoIsdave", errors, request);
			//}
			
			if (!"0".equals(getDeklaracijaPateikta())){
				if ("".equals(getPateikejoVardas())){
					UtilDelegator.setError("error.pateikejoVardas", errors, request);
				}
				if ("".equals(getPateikejoPavarde())){
					UtilDelegator.setError("error.pateikejoPavarde", errors, request);
				}
			}
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		super.reset(mapping,request);
		HttpSession session = request.getSession();
	    Long l = (Long)session.getAttribute("idForEdit");
	    if (null != l){
	    	try {
	    		IsvykimoDeklaracija d = DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(l,request);	    	
	    		if (null != d.getIsvykimoData()){
	    			setIsvykimoMetai(yearFormat.format(d.getIsvykimoData()));
	    			setIsvykimoMenuo(monthFormat.format(d.getIsvykimoData()));
	    			setIsvykimoData(dayFormat.format(d.getIsvykimoData()));	 
	    		}
	    		if(null != d.getSaltinis())
				if(1 == d.getSaltinis().longValue())session.setAttribute("prohibited","true");
				else session.removeAttribute("prohibited");
	    	}
	    	catch(ObjectNotFoundException onfe){
	    		onfe.printStackTrace();
	    	}		
	    }
	}
}