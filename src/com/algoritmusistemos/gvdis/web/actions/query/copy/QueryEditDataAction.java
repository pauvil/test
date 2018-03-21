package com.algoritmusistemos.gvdis.web.actions.query.copy;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.forms.QueryEditDataForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class QueryEditDataAction extends Action
{
    public QueryEditDataAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws InternalException, DatabaseException
    {
    	QueryEditDataForm qForm = (QueryEditDataForm)form;
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    	sdf.setLenient(false);
    	try {
    		long gvtNr = Long.parseLong(qForm.getGvtNr());
    		long gvtAsmNr = Long.parseLong(qForm.getGvtAsmNr());
    		Date dataNuo = sdf.parse(qForm.getDataNuo());	
    		Date dataIki = sdf.parse(qForm.getDataIki());
    		String gvtTipas = qForm.getGvtTipas();
    		String vlsKodas = qForm.getValstybe();
    		String vlsPastabos = qForm.getValstybePast();
    		
    		Long terNr, adrNr, gvtKampoNr;
    		try { terNr = Long.valueOf(qForm.getAddressTer()); }
    		catch (Exception e){ terNr = null; } 

    		try { adrNr = Long.valueOf(qForm.getAddressAdr()); }
    		catch (Exception e){ adrNr = null; }
    		
    		try { gvtKampoNr = Long.valueOf(qForm.getGvtKampoNr()); }
    		catch (Exception e){ gvtKampoNr = null; } 
    		
    		if(!"R".equals(qForm.getGvtTipas()))
    		{
    			terNr = null;
    			adrNr = null;
    			gvtKampoNr = null;
    		}
    		
        	if ("0".equals(qForm.getConfirm())){
        		String message = QueryDelegator.getInstance().alterGyvenamojiVieta(
        			request, gvtNr, gvtAsmNr, dataNuo, dataIki, gvtTipas, terNr, adrNr, gvtKampoNr, vlsKodas, vlsPastabos, false 
				);
        		if (!message.equalsIgnoreCase("")) {
        			request.setAttribute("message", message); //klaida
        			request.setAttribute("gv_keitimas_mode", "show_error"); //klaida
        		} else {
        			request.setAttribute("gv_keitimas_mode", "show_confirm");
        		}
        		
                return mapping.findForward("confirm");
        	}
        	else {
        		String message = QueryDelegator.getInstance().alterGyvenamojiVieta(
            		request, gvtNr, gvtAsmNr, dataNuo, dataIki, gvtTipas, terNr, adrNr, gvtKampoNr, vlsKodas, vlsPastabos, true 
    			);
        		request.setAttribute("message", message);
        		Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, gvtAsmNr);
        		return new ActionForward("/querypersonresults.do?asmKodas=" + asmuo.getAsmKodas(), true);
        	}
    	}
    	catch (DatabaseException dbe){
    		throw new DatabaseException(dbe);
    	}
    	catch (Exception e){
    		throw new InternalException("Perduoti nekorektiðki parametrai", e);
    	}
    }
}
