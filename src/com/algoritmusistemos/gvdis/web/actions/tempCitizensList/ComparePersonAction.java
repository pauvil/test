package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.AsmKodasForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;

public class ComparePersonAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
		HttpSession session = request.getSession();
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_ASSIGN_GR);
		AsmKodasForm akf = (AsmKodasForm)form;
		Deklaracija deklaracija = (Deklaracija)session.getAttribute("declaration");

		Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request,akf.getAsmKodas());
		session.setAttribute("asmuo",asmuo);
		session.setAttribute("laikinasAsmuo",deklaracija.getLaikinasAsmuo());
		session.setAttribute("asmensDokumentai",DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request, asmuo, deklaracija.getDokumentoNr()));
		if(DeklaracijosDelegator.getInstance(request).hasNotCompletedDeclartions(asmuo,request))
			request.setAttribute("hasNotCompletedDeclartions","");
		
		Date minData = QueryDelegator.getInstance().minValidDeclDate(request, (Asmuo)asmuo); //ju.k 2007.09.27
		Date dekldata = deklaracija.getDeklaravimoData();
		if (dekldata.before(minData)){
			request.setAttribute("kertasilaikotarpis","");
		}
		
        //if(0 != asmuo.getGyvenamosiosVietos().size())request.setAttribute("hasDeclaredResidence",""); //kom ju.k 2007.09.27
		//is comparePerson.jsp <logic:present name="hasDeclaredResidence">
		//<span class="error">Asmuo turi deklaruotà gyvenamàjà vietà. Jo susieti su Gyventojø registru negalima.</span>
    	//</logic:present>
		//<logic:notPresent name="hasDeclaredResidence">
		session.setAttribute(Constants.CENTER_STATE, Constants.COMPARE_PERSON);
	    return (mapping.findForward(Constants.CONTINUE));		
	}
}

