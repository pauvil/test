package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;

public class ViewSprendimasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InternalException
	{	
		HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_VIEW_SPRENDIMAS);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE);
	    SprendimasKeistiDuomenis sprendimas = null;

	    try {
	    	Long id = Long.valueOf((String)request.getParameter("id"));
	    	sprendimas = SprendimaiDelegator.getInstance().getSprendimas(request, id.longValue());
	    	sprendimas.getInsDate(); // gali jau buti istrintas
	    	request.setAttribute("sprendimas", sprendimas);
	    	if (sprendimas.getGyvenamojiVieta() != null 
	    		&& !sprendimas.getCalcTipasStr().equals("Naikinti duomenis")){
	    		String addressString = QueryDelegator.getInstance().getAddressString(request, sprendimas.getGyvenamojiVieta());
	    		request.setAttribute("addressString", addressString);
	    	}
	    }
	    catch (Exception e){
	    	throw new InternalException("Blogi sprendimo parametrai", e);
	    }
    	
		// Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	if (sprendimas != null) try {
    		for (Iterator it = sprendimas.getGyvenamosiosVietos().iterator(); it.hasNext(); ){
    			GyvenamojiVieta gvt = (GyvenamojiVieta)it.next();
        		Asmuo asmuo = gvt.getAsmuo();
        		//asmuo.setVardasPavarde(request, sprendimas.getData());
        		long auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(asmuo.getAsmKodas()), Constants.AUDIT_SPREND_PERZIURA, "Sprendimo perþiûra");
        		AuditDelegator.getInstance().auditPersonResult(request, auditId, asmuo);
    		}
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}
    	return (mapping.findForward(Constants.CONTINUE));
	}
}
