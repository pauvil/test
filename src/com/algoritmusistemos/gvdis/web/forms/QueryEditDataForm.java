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
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;

public class QueryEditDataForm extends ActionForm
{
	private String confirm;
	private String gvtAsmNr;
	private String gvtNr;	
	private String gvtTipas;	
	private String dataNuo;	
	private String dataIki;	
    private String addressAdr;
    private String addressTer;
    private String addressString;
    private String valstybe;
    private String valstybePast;
	private String gvtKampoNr;
	private String avdNr;

	public String getGvtAsmNr() { return gvtAsmNr; }
	public void setGvtAsmNr(String gvtAsmNr) { this.gvtAsmNr = gvtAsmNr; }

	public String getAvdNr() { return avdNr; }
	public void setAvdNr(String avdNr) { this.avdNr = avdNr; }

	public String getGvtNr() { return gvtNr; }
	public void setGvtNr(String gvtNr) { this.gvtNr = gvtNr; }
	
	public String getAddressAdr() { return addressAdr; }
	public void setAddressAdr(String addressAdr) { this.addressAdr = addressAdr; }
	
	public String getGvtKampoNr() {	return gvtKampoNr; }
	public void setGvtKampoNr(String gvtKampoNr) { this.gvtKampoNr = gvtKampoNr; }		

	public String getAddressTer() { return addressTer; }
	public void setAddressTer(String addressTer) { this.addressTer = addressTer; }

	public String getAddressString() { return addressString; }
	public void setAddressString(String addressString) { this.addressString = addressString; }

	public String getDataNuo() { return dataNuo; }
	public void setDataNuo(String dataNuo) { this.dataNuo = dataNuo; }

	public String getDataIki() { return dataIki; }
	public void setDataIki(String dataIki) { this.dataIki = dataIki; }

	public String getGvtTipas() { return gvtTipas; }
	public void setGvtTipas(String gvtTipas) { this.gvtTipas = gvtTipas; }

	public String getValstybe() { return valstybe; }
	public void setValstybe(String valstybe) { this.valstybe = valstybe; }
	
	public String getValstybePast() { return valstybePast; }
	public void setValstybePast(String valstybePast) { this.valstybePast = valstybePast; }
	
	public String getConfirm() { return confirm; }
	public void setConfirm(String confirm) { this.confirm = confirm; }
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		try {
			Date dNuo = format.parse(dataNuo);
			if (dNuo.after(new Date())){
				errors.add("dataNuo", new ActionMessage("err.data"));
	        	request.setAttribute("errDataNuo", "");
			}
		}
        catch (ParseException pe){
			errors.add("dataNuo", new ActionMessage("err.data"));
        	request.setAttribute("errDataNuo", "");
        }
		if (!"".equals(gvtNr) && gvtNr != null || !"".equals(dataIki)) try {
			Date dIki = format.parse(dataIki);
			if (dIki.after(new Date())){
				errors.add("dataIki", new ActionMessage("err.data"));
	        	request.setAttribute("errDataIki", "");
			}
		}
        catch (ParseException pe){
			errors.add("dataIki", new ActionMessage("err.data"));
        	request.setAttribute("errDataIki", "");
        }
        if (errors.isEmpty()) try {			// 
        	Date data1 = format.parse(dataNuo);
        	Date data2 = format.parse(dataIki);
        	if (data1.getTime() > data2.getTime()){
    			errors.add("dataIki", new ActionMessage("err.data"));
            	request.setAttribute("errDataIki", "");
        	}
        	
        }
        catch (ParseException pe){
        }
        
        if ("R".equals(getGvtTipas())){
            if (getAddressTer().length() < 1 && getAddressAdr().length() < 1 ){
                errors.add("addressString", new ActionMessage("errors.emptyAddress"));
            	request.setAttribute("errAdresas", "");
            }
        }
        
        return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		Long actGvtNr = (Long)session.getAttribute("actGvtNr");
		Long actGvtAsmNr = (Long)session.getAttribute("actGvtAsmNr");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		request.setAttribute("gv_keitimas_mode", "show_input");
		
		if (actGvtAsmNr != null && actGvtNr != null) try {
        	GyvenamojiVieta gvt = QueryDelegator.getInstance().getGyvenamojiVieta(request, actGvtAsmNr.longValue(), actGvtNr.longValue());
        	gvtAsmNr = String.valueOf(gvt.getGvtAsmNr());
        	gvtNr = String.valueOf(gvt.getGvtNr());	
        	gvtTipas = gvt.getGvtTipas();	
        	dataNuo = gvt.getGvtDataNuo() != null ? sdf.format(gvt.getGvtDataNuo()) : "";	
        	dataIki = gvt.getGvtDataIki() != null ? sdf.format(gvt.getGvtDataIki()) : "";	
            addressTer = gvt.getGvtAtvNr() != null ? String.valueOf(gvt.getGvtAtvNr()) : "";  
            addressAdr = gvt.getGvtAdvNr() != null ? String.valueOf(gvt.getGvtAdvNr()) : "";
            gvtKampoNr = gvt.getGvtKampoNr() != null ? String.valueOf(gvt.getGvtKampoNr()) : "";
            //try {
            	//addressString = QueryDelegator.getInstance().getAddressString(request, gvt);
            	addressString = AdresaiDelegator.getInstance().getAsmAddress(gvt.getAsmuo().getAsmNr(),
            			gvt.getGvtNr(),request);
            /*}
            catch (DatabaseException e){
            	addressString = "";
            }
            */
            valstybe = gvt.getValstybe() != null ? gvt.getValstybe().getKodas() : "";
            valstybePast = gvt.getGvtAdrUzsenyje();
            confirm = "0";
		}
		catch (ObjectNotFoundException onfe){
        	gvtAsmNr = "";
        	gvtNr = "";	
        	gvtTipas = "O";	
        	dataNuo = "";	
        	dataIki = "";	
            addressTer = "";  
            addressAdr = "";  
            gvtKampoNr = "";
            addressString = "";
            valstybe = "";
            valstybePast = "";
            confirm = "0";
		}
		else {
        	if (actGvtAsmNr != null){
        		gvtAsmNr = String.valueOf(actGvtAsmNr);
        	}
        	else {
        		gvtAsmNr = "";
        	}
        	gvtNr = "";	
        	gvtTipas = "O";	
        	dataNuo = "";	
        	dataIki = "";	
            addressTer = "";
            addressAdr = "";
            gvtKampoNr = "";
            addressString = "";
            valstybe = "";
            valstybePast = "";
            confirm = "0";
		}
	}
}

