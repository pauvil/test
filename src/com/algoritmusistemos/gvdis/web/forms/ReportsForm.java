package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AtaskaitosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ParameterDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class ReportsForm extends ActionForm
{
	private String dataNuo;
	private String dataIki;	
	private String savivaldybe;	
	private String seniunija;	
	private String deklTipas;	
	private String gimDataNuo;	
	private String gimDataIki;	
	private String grupavimas;	
	private String view;
	private String ataskaita12data;

	public String getAtaskaita12data() {
		return ataskaita12data;
	}

	public void setAtaskaita12data(String ataskaita12data) {
		this.ataskaita12data = ataskaita12data;
	}

	public String getDataIki() 
	{
		return dataIki;
	}

	public void setDataIki(String dataIki) 
	{
		this.dataIki = dataIki;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getDataNuo() 
	{
		return dataNuo;
	}

	public void setDataNuo(String dataNuo) 
	{
		this.dataNuo = dataNuo;
	}

	public String getDeklTipas() 
	{
		return deklTipas;
	}

	public void setDeklTipas(String deklTipas) 
	{
		this.deklTipas = deklTipas;
	}

	public String getGimDataIki() 
	{
		return gimDataIki;
	}

	public void setGimDataIki(String gimDataIki) 
	{
		this.gimDataIki = gimDataIki;
	}

	public String getGimDataNuo() 
	{
		return gimDataNuo;
	}

	public void setGimDataNuo(String gimDataNuo) 
	{
		this.gimDataNuo = gimDataNuo;
	}

	public String getGrupavimas() 
	{
		return grupavimas;
	}

	public void setGrupavimas(String grupavimas) 
	{
		this.grupavimas = grupavimas;
	}

	public String getSavivaldybe() 
	{
		return savivaldybe;
	}

	public void setSavivaldybe(String savivaldybe) 
	{
		this.savivaldybe = savivaldybe;
	}

	public String getSeniunija() 
	{
		return seniunija;
	}

	public void setSeniunija(String seniunija) 
	{
		this.seniunija = seniunija;
	}

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
        	request.setAttribute("error", "");
        }

        if (!"".equals(dataIki))try {
			format.parse(dataIki);
		}
        catch (ParseException pe){
        	request.setAttribute("errDataIki", "");
        	request.setAttribute("error", "");
        }

        if (!"".equals(gimDataNuo))try {
			format.parse(gimDataNuo);
		}
        catch (ParseException pe){
        	request.setAttribute("errGimDataNuo", "");
        	request.setAttribute("error", "");
        }

        if (!"".equals(gimDataIki))try {
			format.parse(gimDataIki);
		}
        catch (ParseException pe){
        	request.setAttribute("errGimDataIki", "");
        	request.setAttribute("error", "");
        }

        return errors;
	}
	
    public void reset(ActionMapping mapping, HttpServletRequest request) 
    {
    	view = "";
    	dataNuo = "";
    	dataIki = "";
    	savivaldybe = "";
    	seniunija = "";
    	deklTipas = "0";	
    	gimDataNuo = "";
    	gimDataIki = "";
    	grupavimas = "";
    	ataskaita12data = "";
    	
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
		
		int reportType;
        try {
        	reportType = Integer.parseInt(request.getParameter("reportType"));
        }
        catch (Exception e){
        	reportType = AtaskaitosDelegator.TYPE_0801;
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
         
         if(reportType == AtaskaitosDelegator.TYPE_0812){
        	 List laikotarpiaiTemp = HibernateUtils.currentSession(request).createSQLQuery(
        				"select trunc(t.laikotarpis) from gvdis_ataskaita t group by trunc(t.laikotarpis) order by trunc(t.laikotarpis) asc").
        	       		list();
        	 List laikotarpiai = new ArrayList();
        	 Date maxDate = new Date(0);
        	 for (Iterator iter = laikotarpiaiTemp.iterator(); iter.hasNext();) {
				Date data = (Date) iter.next();
				if(maxDate.before(data)) maxDate = data;
				HashMap hm = new HashMap();
				hm.put("laikotarpis", data);
				laikotarpiai.add(hm);
			}
        	ataskaita12data = maxDate.toString();
        	request.setAttribute("laikotarpiai", laikotarpiai);
         }
    }
}
