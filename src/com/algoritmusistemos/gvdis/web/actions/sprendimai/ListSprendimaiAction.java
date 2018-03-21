package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.FilterPrasymaiForm;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class ListSprendimaiAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException,	DatabaseException
	{
	    HttpSession session = request.getSession();
        session.setAttribute("menu_cell","sprendimai");
	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_LIST_SPRENDIMAI);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE);
	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    	sdf.setLenient(false);
	    
        // Rûðiavimas, puslapiavimas
        Paging paging = new Paging();
        Ordering ordering = (Ordering)session.getAttribute("sprendimai_ordering");
        if (ordering == null){
            ordering = new Ordering();
        }
        if (ordering.getColumn() == null) ordering.setColumn("data, id"); // Pagal nutylëjimà
        paging.init(request);
        ordering.init(request);
        session.setAttribute("sprendimai_paging", paging);
        session.setAttribute("sprendimai_ordering", ordering);

        // Tikriname filtravimo parametrus 
    	Date dataNuo, dataIki;
    	int tipas;
    	Istaiga savivaldybe = null, seniunija = null;
        FilterPrasymaiForm fForm = (FilterPrasymaiForm)form;

        try { dataNuo = sdf.parse(fForm.getDataNuo()); }
    	catch (ParseException pe){ dataNuo = null; }

    	try { dataIki = sdf.parse(fForm.getDataIki()); }
    	catch (ParseException pe){ dataIki = null; }
    	
    	try { tipas = Integer.parseInt(fForm.getTipas()); }
    	catch (Exception pe){ tipas = -1; }
    	
    	session.setAttribute("rjSavivaldybe",fForm.getSavivaldybe());
        session.setAttribute("rjDataNuo",fForm.getDataNuo());
        session.setAttribute("rjDataIki",fForm.getDataIki());      
        session.setAttribute("rjSeniunija",fForm.getSeniunija());
        
       	Long savId = null, senId = null;
   	    try { 
   	    	switch (userStatus){
   	    	case UserDelegator.USER_GLOBAL: 
   	    		savId = Long.valueOf(fForm.getSavivaldybe());
   	    		break;
   	    	case UserDelegator.USER_SAV:
   	    		savId = (Long)session.getAttribute("userIstaigaId");
   	    		break;
   	    	default: savId = null;
   	    	}
   	    } 
   	    catch (NumberFormatException ignore){}

   	    try {  
    	   	switch (userStatus){
    	   	case UserDelegator.USER_SEN:
    	   		senId = (Long)session.getAttribute("userIstaigaId");
    	   		break;
    	   	default:
    	   		senId = Long.valueOf(fForm.getSeniunija());
    	   	}
    	} 
    	catch (NumberFormatException ignore){}
    	    
    	try {
    		savivaldybe = JournalDelegator.getInstance().getIstaiga(request, savId.longValue());
       	}
       	catch (Exception e2){
       		savivaldybe = null;
       	}
    	try {
        	seniunija = JournalDelegator.getInstance().getIstaiga(request, senId.longValue());
    	}
    	catch (Exception e1){
    		seniunija = null;
    	}

        // Uþkrauname savivaldybiø ir seniûnijø sàraðà
        request.setAttribute("savivaldybes", JournalDelegator.getInstance().getSavivaldybes(request));
        List seniunijos = new ArrayList();
        try {
        	seniunijos = JournalDelegator.getInstance().getSeniunijos(request, savivaldybe);
        }
        catch (Exception ex){
        }
        request.setAttribute("seniunijos", seniunijos);

        List sprendimai = SprendimaiDelegator.getInstance().getSprendimai(
        		request, paging, ordering,
        		dataNuo, dataIki, 
        		tipas,
        		savivaldybe, seniunija
        );
        request.setAttribute("sprendimai", sprendimai);

        return mapping.findForward(Constants.CONTINUE);
	}
}
