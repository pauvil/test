package lt.solver.gvdis.web.forms;

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

public class Report13Form extends ActionForm {
	
	private String raportData;
	private String savivaldybe;	
	private String seniunija;	
	private String kaimas;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		try {
			format.parse(raportData);
		}
        catch (ParseException pe){
        	request.setAttribute("erraportData", "");
        	request.setAttribute("error", "");
        }
        
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		raportData = "";
		savivaldybe = "";	
		seniunija = "";	
		kaimas = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date now = new Date();
        setRaportData(sdf.format(now));
        
        HttpSession sesija = request.getSession();
        
        String tempStr = (String)sesija.getAttribute("rjSavivaldybe");
        if(tempStr != null)
        	setSavivaldybe(tempStr);
        tempStr = (String)sesija.getAttribute("rjRaportData");
        if(tempStr != null)
        	setRaportData(tempStr);
        tempStr = (String)sesija.getAttribute("rjSeniunija");
        if(tempStr != null) {
        	setSeniunija(tempStr);
        }
        tempStr = (String)sesija.getAttribute("rjKaimas");
        if(tempStr != null)
        	setKaimas(tempStr);
        
	}
	
	public String getRaportData() {
		return raportData;
	}
	public void setRaportData(String raportData) {
		this.raportData = raportData;
	}
	public String getSavivaldybe() {
		return savivaldybe;
	}
	public void setSavivaldybe(String savivaldybe) {
		this.savivaldybe = savivaldybe;
	}
	public String getSeniunija() {
		return seniunija;
	}
	public void setSeniunija(String seniunija) {
		this.seniunija = seniunija;
	}
	public String getKaimas() {
		return kaimas;
	}
	public void setKaimas(String kaimas) {
		this.kaimas = kaimas;
	}	

	
	
}
