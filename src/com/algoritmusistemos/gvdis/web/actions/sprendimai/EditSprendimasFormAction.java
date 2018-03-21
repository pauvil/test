package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;

public class EditSprendimasFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InternalException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_EDIT_SPRENDIMAS_FORM);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		
		session.removeAttribute("priezastys");
    	session.setAttribute("createSprendimas", null);
	    
	    try {
	    	Set priezastys = ZinynaiDelegator.getInstance().getZinynoReiksmes(request, "SPRENDIMO_PRIEZASTIS");
	    	session.setAttribute("priezastys", priezastys);

	    	List valstybes = UtilDelegator.getInstance().getValstybes(request);
	    	session.setAttribute("valstybes", valstybes);

	    	Long id = Long.valueOf((String)request.getParameter("id"));
	    	session.setAttribute("actSprendimasId", id);
	    	SprendimasKeistiDuomenis spr = SprendimaiDelegator.getInstance().getSprendimas(request, id.longValue());
	    	spr.getInsDate(); // gali jau buti istrinta
	    }
	    catch (Exception e){
	    	throw new InternalException("Blogi sprendimo parametrai", e);
	    }
    	
		// Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	try {
	    	Long id = Long.valueOf((String)request.getParameter("id"));
	    	SprendimasKeistiDuomenis sprendimas = SprendimaiDelegator.getInstance().getSprendimas(request, id.longValue());
    		for (Iterator it = sprendimas.getGyvenamosiosVietos().iterator(); it.hasNext(); ){
    			GyvenamojiVieta gvt = (GyvenamojiVieta)it.next();
        		Asmuo asmuo = gvt.getAsmuo();
        		long auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(asmuo.getAsmKodas()), Constants.AUDIT_SPREND_REDAGAVIMAS, "Sprendimo redagavimas");
        		AuditDelegator.getInstance().auditPersonResult(request, auditId, asmuo);
    		}
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}

    	return (mapping.findForward(Constants.CONTINUE));
	}
}