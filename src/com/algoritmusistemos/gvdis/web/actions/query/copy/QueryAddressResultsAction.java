package com.algoritmusistemos.gvdis.web.actions.query.copy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.forms.QueryAddressForm;
import com.algoritmusistemos.gvdis.web.utils.Ordering;

public class QueryAddressResultsAction extends Action
{

    public QueryAddressResultsAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    	throws DatabaseException
    {
    	    	
	        HttpSession session = request.getSession();
	        session.setAttribute("CENTER_STATE", Constants.QUERY_ADDRESS_RESULTS);
	        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
	        Ordering ordering = (Ordering)session.getAttribute("query_address_results_ordering");
	        if (ordering == null){
	            ordering = new Ordering();
	        }
	        if (ordering.getColumn() == null){
	        	ordering.setColumn("AVD_PAVARDE"); // Pagal nutylëjimà
	        }
	        ordering.setDirection("desc");
	        ordering.init(request);	
	        session.setAttribute("query_address_results_ordering", ordering);
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			format.setLenient(false);
	        QueryAddressForm qForm = (QueryAddressForm)form;
	        Long adrNr, terNr;
			Date data;
   	
    	
	        try {
	        	adrNr = Long.valueOf(qForm.getAddressAdr());
	        }
	        catch (Exception e){
	        	adrNr = null;
	        }
	        try {
	        	terNr = Long.valueOf(qForm.getAddressTer());
	        }
	        catch (Exception e){
	        	terNr = null;
	        }
	        try {
	        	data = format.parse(qForm.getData());
	        }
	        catch (Exception e){
	        	data = new Date();
	        }
	
	        // Audituojame bandymà paþiûrëti duomenis 
	        long auditId = AuditDelegator.getInstance().auditQueryByAddress(request, terNr, adrNr, null, qForm.getPriezKodas(), qForm.getPriezAprasymas());
	        session.setAttribute("lastAuditId", new Long(auditId));
	        List gyventojai = QueryDelegator.getInstance().getResidents(request, ordering, adrNr, terNr, data);
	        request.setAttribute("gyventojai", gyventojai);
	        request.setAttribute("addressString", request.getParameter("addressString"));

	        
        return mapping.findForward("continue");
    }
}
