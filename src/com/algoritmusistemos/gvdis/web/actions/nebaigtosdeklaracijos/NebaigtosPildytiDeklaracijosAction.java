package com.algoritmusistemos.gvdis.web.actions.nebaigtosdeklaracijos;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.utils.Ordering;

public class NebaigtosPildytiDeklaracijosAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
	    HttpSession session = request.getSession();
        //session.setAttribute("menu_cell","NotCompletedDeclarationsList");
        session.removeAttribute("prohibited");
	    session.setAttribute(Constants.CENTER_STATE, Constants.NOT_COMPLETED_DECLARTIONS_LIST);
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_UNFINISHED_DECLARATIONS);
        Ordering ordering = (Ordering)session.getAttribute("not_completed_declarations_ordering");
        String deklTipas = request.getParameter("deklTipas");
		
        if (null == deklTipas ){
			deklTipas = (String)request.getAttribute("deklTipas");
		}
        
		if ("atv".equals(deklTipas)){
			session.setAttribute("menu_cell","NebaigtosAtvDeklaracijos");
		} else if ("isv".equals(deklTipas)){
			session.setAttribute("menu_cell","NebaigtosIsvDeklaracijos");
		}

		if (ordering == null){
            ordering = new Ordering();
        }
        
        Long savivaldybe = null;
	    Long seniunija = null;
	    
	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
	    try { 
	    	switch (userStatus){
	    	case UserDelegator.USER_GLOBAL: 
	    		savivaldybe = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	case UserDelegator.USER_SAV:
	    		savivaldybe = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	default: savivaldybe = null;
	    	}
	    } 
	    catch (NumberFormatException ignore){}
	    
	    try {  
	    	switch (userStatus){
	    	case UserDelegator.USER_SEN:
	    		seniunija = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	}
	    } 
	    catch (NumberFormatException ignore){}
	    
        if (ordering.getColumn() == null) ordering.setColumn("gavimoData"); // Pagal nutylëjimà
        ordering.init(request);
        session.setAttribute("not_completed_declarations_ordering", ordering);
        
        List l = new ArrayList(); 
        
	    if ((deklTipas!=null)&(!"".equals(deklTipas))){
        	l = DeklaracijosDelegator.getInstance(request).getNebaigtosIvestiDeklaracijos(request, ordering, deklTipas, savivaldybe,seniunija);      
        }
	    
	    request.setAttribute("notCompletedDeklarations", l);
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
