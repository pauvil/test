package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.QueryAddressForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SavPazyma;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.utils.Ordering;

public class SavPazymaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_SAV);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		
		QueryAddressForm qForm = (QueryAddressForm)form;
		Long adrNr, terNr;
		Date data;      
		request.setAttribute("nenurodytaDeklData","N");
		
       if (request.getParameter("id") == null || "".equals(request.getParameter("id"))) {
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
        	/*String istaigaId = UserDelegator.getInstance().getDarbIstaiga(request)[0];
        	SavPazyma pazyma = new SavPazyma();
        	pazyma.setGvtAdvNr(adrNr);
        	pazyma.setGvtAtvNr(terNr);
        	pazyma.setPazymosData(new Timestamp(data.getTime()));
			pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(new Long(istaigaId).longValue(), request));
			request.setAttribute("savPazyma", pazyma);	*/
			request.setAttribute("addressString", request.getParameter("addressString"));
			session.setAttribute("registrNr", "");
       }
       else{
    	   long id = Long.parseLong(request.getParameter("id"));
    	   SavPazyma pazyma = PazymosDelegator.getInstance().getSavPazyma(request, id);
    	   adrNr = pazyma.getGvtAdvNr();          
           terNr = pazyma.getGvtAtvNr();
           try {
           	data = pazyma.getPazymosData();
           }
           catch (Exception e){
           	data = new Date();
           }
           request.setAttribute("addressString", pazyma.getCalcAdresas(request));
           request.setAttribute("perziura","taip");
           session.setAttribute("registrNr", pazyma.getRegNr());
       }
		

        List gyventojai = QueryDelegator.getInstance().getResidents(request, new Ordering(),adrNr, terNr, data);

        /*
         * Tieto: 2012-10-05
         * Patikrinama ar yra deklaruota gyvenamoji vieta GVT_DATA_NUO = NULL
         * Jei yra, rodomas pranesimas vartotojui (pries spausdinant pazyma patalpu savininkui)"
         * "Yra asmenø, kuriø deklaravimo data nenurodyta. Ar tikrai norite spausdinti paþymà?"
         */
        
		List gyvVieta = HibernateUtils.currentSession(request).createQuery(
		"from GyvenamojiVieta gvt where " +
		"gvt.gvtAdvNr = :adrNr and " +
		"gvt.gvtAtvNr is null and " +
		"gvt.gvtTipas = 'R' and " +
		"((gvt.gvtDataNuo <= :data and nvl(gvt.gvtDataIki, sysdate) >= :data or :data is null and gvt.gvtDataIki is null)" + 
		  " or (gvt.gvtDataNuo is null and gvt.gvtRegData <=:data and nvl(gvt.gvtDataIki, sysdate) >= :data) )"
				)
		    .setParameter("adrNr", adrNr, Hibernate.LONG)
			.setTimestamp("data", data)
			.list();

		for (Iterator it = gyvVieta.iterator(); it.hasNext(); ){
			GyvenamojiVieta gvt1 = (GyvenamojiVieta)it.next();
			if(gvt1.getGvtDataNuo() == null){
				request.setAttribute("nenurodytaDeklData","T");
				break;
			}
		}
        
        session.setAttribute("gyventojai", gyventojai);
        GyvenamojiVieta gvt = new GyvenamojiVieta();
        gvt.setGvtAdvNr(adrNr);
        gvt.setGvtAtvNr(terNr);
        gvt.setGvtRegData(new Timestamp(data.getTime()));
        session.setAttribute("gyvenamojiVieta", gvt);        
     
        
		// Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	try {
    		long auditId = AuditDelegator.getInstance().auditQueryByAddress(request, terNr, adrNr, null, Constants.AUDIT_SAV_PAZ_PERZIURA, "Ataskaitos savinikams perþiûra");
    		for (Iterator it = gyventojai.iterator(); it.hasNext(); ){
    			Asmuo gyventojas = (Asmuo)it.next();
        		AuditDelegator.getInstance().auditPersonResult(request, auditId, gyventojas);
    		}
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}

    	return mapping.findForward(Constants.CONTINUE);
	}
}
