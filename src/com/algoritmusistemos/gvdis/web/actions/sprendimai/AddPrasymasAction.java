package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.algoritmusistemos.gvdis.web.delegators.ParameterDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.forms.FilterPrasymaiForm;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class AddPrasymasAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception 
	{
		HttpSession session = request.getSession();
	    session.setAttribute("POPUP_STATE", Constants.SPR_ADD_PRASYMAS);	    
	   
    	int limitDays = ParameterDelegator.getInstance().getNumberParameter(request, "GVDIS_PRASYMU_AMZIUS");
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    	sdf.setLenient(false);
    	Calendar calendar = Calendar.getInstance();    
        // Rûðiavimas, puslapiavimas
        Paging paging = new Paging();
        Ordering ordering = (Ordering)session.getAttribute("add_prasymai_ordering");
        if (ordering == null){
            ordering = new Ordering();
        }
        if (ordering.getColumn() == null) ordering.setColumn("data, id"); // Pagal nutylëjimà
        paging.init(request);
        ordering.init(request);
        session.setAttribute("add_prasymai_paging", paging);
        session.setAttribute("add_prasymai_ordering", ordering);

        // Tikriname filtravimo parametrus 
    	Date dataNuo, dataIki;    	
        FilterPrasymaiForm fForm = (FilterPrasymaiForm)form;

        try { dataNuo = sdf.parse(fForm.getDataNuo()); }
    	catch (ParseException pe){ dataNuo = null;  }

    	try { dataIki = sdf.parse(fForm.getDataIki()); }
    	catch (ParseException pe){ dataIki = null;} 
    	try{
    	if(dataIki == null && dataNuo == null){
    		dataIki = sdf.parse((String)session.getAttribute("addPDataIki"));
    		dataNuo = sdf.parse((String)session.getAttribute("addPDataNuo"));
    	}
    	}catch (Exception pe){ dataIki = null; dataNuo=null;} 
    	if(dataIki == null && dataNuo == null){
    		dataIki = new Date(); 
    		fForm.setDataIki(sdf.format(dataIki));
    		calendar.setTime(dataIki);
        	calendar.add(Calendar.DATE, -5);
        	dataNuo = calendar.getTime();
        	fForm.setDataNuo(sdf.format(dataNuo));
    	}
    	
        session.setAttribute("addPDataNuo",fForm.getDataNuo());
        session.setAttribute("addPDataIki",fForm.getDataIki());    
        
        List prasymai = SprendimaiDelegator.getInstance().getPrasymai(
        	request, paging, ordering,
        	dataNuo, dataIki, 
        	0, -1,
        	null, null
        );
        calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -limitDays);
        if (prasymai != null) for (Iterator it = prasymai.iterator(); it.hasNext(); ){
        	PrasymasKeistiDuomenis prasymas = (PrasymasKeistiDuomenis)it.next();
        	if (prasymas.getBusena() == 0 && prasymas.getInsDate().before(calendar.getTime())){
        		prasymas.setPasenes(true);
        	}
        	else {
        		prasymas.setPasenes(false);
        	}
        }
        request.setAttribute("prasymai", prasymai);

		
		return mapping.findForward(Constants.CONTINUE);
	}

}
