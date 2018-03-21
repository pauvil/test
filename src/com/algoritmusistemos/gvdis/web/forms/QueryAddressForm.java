package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;

public class QueryAddressForm extends ActionForm {
	
    private String addressTer;
    private String addressAdr;
    private String addressString;
    private String priezKodas;
    private String priezAprasymas;
    private String data;
	private String gvtKampoNr;

    public QueryAddressForm() {
    }

    public String getData() { return data; }
	public void setData(String data) { this.data = data; }

	public String getAddressAdr() {	return addressAdr; }
	public void setAddressAdr(String addressAdr) { this.addressAdr = addressAdr; }
	
	public String getGvtKampoNr() {	return gvtKampoNr; }
	public void setAGvtKampoNr(String gvtKampoNr) { this.gvtKampoNr = gvtKampoNr; }	

	public String getAddressString() { return addressString; }
	public void setAddressString(String addressString) { this.addressString = addressString; }

	public String getAddressTer() { return addressTer; }
	public void setAddressTer(String addressTer) { this.addressTer = addressTer; }

	public String getPriezAprasymas() { return priezAprasymas;}
    public void setPriezAprasymas(String priezAprasymas){ this.priezAprasymas = UtilDelegator.trim(priezAprasymas, 250); }

    public String getPriezKodas() { return priezKodas; }
    public void setPriezKodas(String priezKodas) {this.priezKodas = priezKodas; }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        long gvtAdvNr = 0;
        Date pazymosData = null; 
        
        if (request.getParameter("id") == null || "".equals(request.getParameter("id"))) {
			if (getAddressAdr().length() < 1 && getAddressTer().length() < 1) {
				errors.add("addressString", new ActionMessage("errors.emptyAddress"));
				request.setAttribute("badAddressString", "");
			}
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			format.setLenient(false);
			if (!"".equals(data))
				try {
					pazymosData = format.parse(data);
				} catch (ParseException pe) {
					errors.add("data", new ActionMessage("err.data"));
					request.setAttribute("errData", "");
				}
			
			// Tieto 2012-04-27: tikrinti, ar tokios paþymos ðiandien praðoma pirmà kartà;
			// tikrinti, ar pilnas adresas (negalima formuoti paþymos visam kaimui, be nurodyto namo).
			HttpSession session = request.getSession();
	    	if (session.getAttribute(Constants.CENTER_STATE).equals(Constants.PAZ_SAV_FORM) 
	    			|| session.getAttribute(Constants.CENTER_STATE).equals(Constants.PAZ_SAV)) {
	    		try {
					if(!addressAdr.equals("")){
						gvtAdvNr = Long.parseLong(addressAdr);
						boolean siandienSavPazymaIsduota = PazymosDelegator.getInstance().getSiosDienosSavPazyma(request, gvtAdvNr, pazymosData);
						if(siandienSavPazymaIsduota){
							errors.add("title", new ActionMessage("error.pazymaSiandienIsduota"));
							request.setAttribute("error.pazymaSiandienIsduota", "");
						}
					} else {
						errors.add("title", new ActionMessage("error.pazymaNetikslusAdresas"));
						request.setAttribute("error.pazymaNetikslusAdresas", "");
					}
				} catch(DatabaseException e){
					e.printStackTrace();
				}
	    	}
			//End Tieto 2012-04-27
		}
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        addressString = "";
        priezKodas = "";
        priezAprasymas = "";
        addressAdr = "";
        addressTer = "";
        data = sdf.format(new Date());     
    }
}
