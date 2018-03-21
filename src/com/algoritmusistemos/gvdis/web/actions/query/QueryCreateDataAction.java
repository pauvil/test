package com.algoritmusistemos.gvdis.web.actions.query;

import java.sql.Timestamp;
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

public class QueryCreateDataAction extends Action
{
    public QueryCreateDataAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws InternalException, DatabaseException
    {
    	QueryEditDataForm qForm = (QueryEditDataForm)form;
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    	sdf.setLenient(false);
    	try {
    		long gvtAsmNr = Long.parseLong(qForm.getGvtAsmNr());
    		Date dataNuo = sdf.parse(qForm.getDataNuo());	
    		
    		Date dataIki = null;
    		try {
    			dataIki = sdf.parse(qForm.getDataIki());
    		} 
    		catch (Exception e){}
    		
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
    		
    		if ("".equals(vlsKodas)){
    			vlsKodas = QueryDelegator.HOME_COUNTRY;
    		}

    		boolean ok = QueryDelegator.getInstance().checkNewRecord(request, gvtAsmNr, dataNuo, dataIki);
    		if (ok){
    			QueryDelegator.getInstance().createGyvenamojiVieta(
    					request, gvtAsmNr, gvtTipas, vlsKodas, new Timestamp(dataNuo.getTime()), (dataIki == null)?null:new Timestamp(dataIki.getTime()), adrNr, terNr, gvtKampoNr, vlsPastabos
    			);
    			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, gvtAsmNr);
    			return new ActionForward("/querypersonresults.do?asmKodas=" + asmuo.getAsmKodas(), true);
    		}
    		else {
        		request.setAttribute("message", "Negalima sukurti áraðo su tokiomis datomis - yra kitø já perdengianèiø áraðø");
                return mapping.findForward("confirm");
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
