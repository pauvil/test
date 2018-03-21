package com.algoritmusistemos.gvdis.web.actions.sprendimai;

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
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.AsmKodasForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class AddPersonAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException, DatabaseException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute("POPUP_STATE", Constants.SPR_ADD_PERSON_RESULTS);
	    
	    AsmKodasForm aForm = (AsmKodasForm)form;
	    String kodas = aForm.getAsmKodas();
	    Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, kodas);
	    request.setAttribute("asmuo", asmuo);
	    request.setAttribute("adresas", QueryDelegator.getInstance().getAsmGvtAdresa(request, asmuo.getAsmNr()));
        // Audituojame ðio asmens duomenø perþiûrà
    	long auditId = AuditDelegator.getInstance().auditQueryByCode(request, kodas, Constants.AUDIT_ASM_KODAS, "");
    	AuditDelegator.getInstance().auditPersonResult(request, auditId, asmuo);

    	return mapping.findForward(Constants.CONTINUE);
	}
}
