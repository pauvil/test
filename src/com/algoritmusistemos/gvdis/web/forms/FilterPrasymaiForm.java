package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.ParameterDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;

public class FilterPrasymaiForm extends ActionForm
{
	private String dataNuo;
	private String dataIki;
	private String tipas;
	private String busena;
	private String savivaldybe;
	private String seniunija;
	
	public String getBusena() { return busena; }
	public void setBusena(String busena) { this.busena = busena; }

	public String getDataIki() { return dataIki; }
	public void setDataIki(String dataIki) { this.dataIki = dataIki; }

	public String getDataNuo() { return dataNuo; }
	public void setDataNuo(String dataNuo) { this.dataNuo = dataNuo; }

	public String getTipas() { return tipas; }
	public void setTipas(String tipas) { this.tipas = tipas; }

	public String getSavivaldybe() { return savivaldybe; }
	public void setSavivaldybe(String savivaldybe) { this.savivaldybe = savivaldybe; }

	public String getSeniunija() { return seniunija; }
	public void setSeniunija(String seniunija) { this.seniunija = seniunija; }

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		if (!"".equals(dataNuo)) try {
			format.parse(dataNuo);
		}
        catch (ParseException pe){
        	request.setAttribute("errDataNuo", "");
        }

        if (!"".equals(dataIki)) try {
			format.parse(dataIki);
		}
        catch (ParseException pe){
        	request.setAttribute("errDataIki", "");
        }
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) 
    {
        setTipas("");        
        setBusena("");
        
        int laikotarpis = 1;
        int userStatus = ((Integer)request.getSession().getAttribute("userStatus")).intValue();
		if (userStatus == UserDelegator.USER_GLOBAL) {
			try {
				laikotarpis = ParameterDelegator.getInstance().getNumberParameter(request,
						"GVDIS_ZURNALU_LAIKOTARPIS_GL");
			} catch (Exception e) {
				laikotarpis = ParameterDelegator.getInstance().getNumberParameter(request,
						"GVDIS_ZURNALU_LAIKOTARPIS");
			}
		} else {
			laikotarpis = ParameterDelegator.getInstance().getNumberParameter(request,
					"GVDIS_ZURNALU_LAIKOTARPIS");
		}
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        setDataIki(sdf.format(now));
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -laikotarpis);
        Date fromDate = calendar.getTime();
        setDataNuo(sdf.format(fromDate));
        
        HttpSession sesija = request.getSession();
        String tempStr = (String)sesija.getAttribute("rjSavivaldybe");
        if(tempStr != null)
        	setSavivaldybe(tempStr);
        tempStr = (String)sesija.getAttribute("rjDataNuo");
        if(tempStr != null)
        	setDataNuo(tempStr);
        tempStr = (String)sesija.getAttribute("rjDataIki");
        if(tempStr != null)
        	setDataIki(tempStr);       
        tempStr = (String)sesija.getAttribute("rjSeniunija");
        if(tempStr != null)
        	setSeniunija(tempStr);    	
    }
}
