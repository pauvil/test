package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis.AxisFault;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;
import java.sql.Timestamp;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.IsvDeklaracijaForm;
import com.algoritmusistemos.gvdis.web.persistence.GvdisBase;
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class IsvDeklaracijaPerformAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException,DatabaseException,InternalException,PermissionDeniedException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK", "RL_GVDIS_UZ_REIK_MINIST_TVARK"});		
	    IsvDeklaracijaForm idf = (IsvDeklaracijaForm)form;
		HttpSession session = request.getSession(); 	  
		
		GvdisBase asmuo = (GvdisBase)session.getAttribute("asmuo");
		Set errors = new HashSet(); 
		Set warnings = new HashSet();
		
		String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));

		Long l = (Long)session.getAttribute("idForEdit");
		
		// Validacija
/*		try {
		if (DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, Long.parseLong(idf.getDocumentId())).getDokBusena()=="N") {
			warnings.add("Pasirinktas asmens dokumentas yra negaliojantis.");
		}
		}
		catch (NumberFormatException nfe) {}
		catch (NullPointerException npe) {}
*/		
	    if(!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) {
			if (idf.getDocumentId() == null || "".equals(idf.getDocumentId()) || "-1".equals(idf.getDocumentId())){
				String saltinis;
				if (l == null) {
					saltinis = "";
				} else {
					saltinis = String.valueOf(DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(l, request).getSaltinis());
				}
				if ("".equals(idf.getAsmensDokumentoNumeris())){
					if (QueryDelegator.HOME_COUNTRY.equals(idf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
						warnings.add("Nenurodytas asmens dokumento numeris.");
					}
					//else { //kom ju.k 2007.06.30
					//	errors.add("Nenurodytas asmens dokumento numeris.");
					//}
				}
				
	
				else if (l != null && DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(l, request).getSaltinis() != null && saltinis.equals("1")) { // el dekl
					if (QueryDelegator.HOME_COUNTRY.equals(idf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
						if (!QueryDelegator.getInstance().hasDocument(request, asmuo, idf.getAsmensDokumentoNumeris())){
							warnings.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu.");
						}
					}
				} else if (QueryDelegator.HOME_COUNTRY.equals(idf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametisFull(request)){ // ne el. dekl.
					if (!QueryDelegator.getInstance().hasDocument(request, asmuo, idf.getAsmensDokumentoNumeris())){
						warnings.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu.");
					}
	
				}
		
				
				if ("".equals(idf.getAsmensDokumentoIsdave())){
					if (QueryDelegator.HOME_COUNTRY.equals(idf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
						warnings.add("Nenurodyta asmens dokumentà iðdavusi ástaiga.");
					}
					//else { //kom 2007.06.30
					//	errors.add("Nenurodyta asmens dokumentà iðdavusi ástaiga.");
					//}
				}
			}
			
			if (!"0".equals(idf.getDeklaracijaPateikta())){
				if ("".equals(idf.getPateikejoVardas()) || "".equals(idf.getPateikejoPavarde())){
					errors.add("Nenurodytas praðymà pateikusio asmens vardas ir pavardë (praðyme paþymëta, kad praðymas pateiktas kito asmens)"); 
				}
			} 
	    } 
	    else {
	    	// nustatom l
	    	l = new Long(-1);
	    }
	    	

	    session.setAttribute("declErrors", errors);
		session.setAttribute("declWarnings", warnings);

		IsvykimoDeklaracija deklaracija = null;
		
	    if (null != l) {	 
	    	if(!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) {
		    	if ("reject".equals(request.getParameter("mode"))){
		    		try {
		    			DeklaracijosDelegator.getInstance(request).rejectDeclaration(l, idf.getAtmetimoPriezastys(), request);
		    		} catch ( AxisFault e){
		    			//request.setAttribute("error", e.getMessage());
		    		} catch ( RemoteException e){
		    			//request.setAttribute("error", e.getMessage());
		    		}
		    		
		    		request.setAttribute("id", l);
		    		session.setAttribute("CENTER_STATE", Constants.REJECT_DEKLARACIJA);
		    		return mapping.findForward(Constants.REJECT_DEKLARACIJA);
		    	}
		    	
		    	deklaracija = DeklaracijosDelegator.getInstance(request).updateIsvDeclaration(
			    		l,
			    		idf.getAnkstesnePavarde(),
			    		idf.getPilietybe(),
			    		idf.getDokumentoRusis(),
			    		idf.getAsmensDokumentoNumeris(),
			    		idf.getAsmensDokumentoIsdavimoMetai(),
			    		idf.getAsmensDokumentoIsdavimoMenuo(),
			    		idf.getAsmensDokumentoIsdavimoData(),
			    		idf.getAsmensDokumentoIsdave(),	    		
			    		idf.getLeidimoGaliojimoMetai(),
			    		idf.getLeidimoGaliojimoMenuo(),
			    		idf.getLeidimoGaliojimoData(),
			    		idf.getPastabos(),
			    		idf.getIsvykimoMetai(),
			    		idf.getIsvykimoMenuo(),
			    		idf.getIsvykimoData(),			
			    		idf.getAnkstesneGyvenamojiVieta(),
			    		idf.getKitaGyvenamojiVietaAprasymas(),
			    		idf.getAtvykoIsUzsienioSalis(),
			    		idf.getAtvykoIsUzsienioTextarea(),
			    		idf.getDeklaracijaPateikta(),
			    		idf.getPateikejoVardas(),
			    		idf.getPateikejoPavarde(),
			    		idf.getDeklaravimoMetai(),
			    		idf.getDeklaravimoMenuo(),
			    		idf.getDeklaravimoData(),
			    		idf.getGavimoMetai(),
			    		idf.getGavimoMenuo(),
			    		idf.getGavimoData(),
			    		idf.getTelefonas(),
			    		idf.getEmail(),
			    		errors.isEmpty(),
						(session.getAttribute("activeDeclaration") != null),
			    		request
			    );	    	
				// Uþfiksuojame asmens duomenø perþiûros faktà auditui
		    	if (deklaracija != null) try {
		    			long auditId = 0;
		    			if(null != deklaracija.getAsmuo())
		    			{
		    				auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(deklaracija.getAsmuo().getAsmNr()), Constants.AUDIT_DEKL_REDAGAVIMAS, "Iðvykimo deklaracijos redagavimas");
		    				AuditDelegator.getInstance().auditPersonResult(request, auditId, deklaracija.getAsmuo());
		    			}
		    	}
		    	catch (Exception nfe){
		    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
		    	}	    	
		    }
		    else {
		    	// sukurti gyvenamà vietà.
		    	Session sess = HibernateUtils.currentSession(request);
		    	Transaction tx = sess.beginTransaction();
		    		
		    	QueryDelegator qdeleg = new QueryDelegator();
		    	
				Date data = new Date();
		    	SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		    	formatter.setLenient(false);
		    	
		    	//System.out.println("data " +idf.getDeklaravimoMetai());
		    	//System.out.println("data " +idf.getDeklaravimoMenuo());
		    	//System.out.println("data " +idf.getDeklaravimoData());

	    		data = CalendarUtils.getParsedDate(idf.getDeklaravimoMetai(), idf.getDeklaravimoMenuo(), idf.getDeklaravimoData());
	    		
	    		Timestamp deklData = null;
	    		if (data != null) {
	    			deklData = new Timestamp(data.getTime());
	    		}
				DeklaracijosDelegator dekdelegator = new DeklaracijosDelegator();
				
		    	long tmp = 0;
		    	try {
		    		dekdelegator.createGyvenamojiVietaFull(
							qdeleg.getAsmuoByCode(request, idf.getAsmensKodas()).getAsmNr(),
							tmp, //gvt_nr
							"U",
							idf.getAtvykoIsUzsienioSalis(),
							deklData,
							tmp, //adrv
							tmp, //terv
							"0", //igaliotinis              
							"",//savininkoTipas,
							tmp, //savAsmKodasN
							tmp, //jurAsmKodasN
							"", //pastabos
							"N",
							"",  
							Long.parseLong("-1"), //gyv viet keitimas uzsienyje -1 naudojamas tik tikrinimui daugiau niekur
							"", //kitaSalis.getKodas()						
							tmp,
							tmp, //isv.getId()
							sess,
							tmp,
							null
					);		    		
		    				
			    	tx.commit();    		
		    	}
		    	catch (GenericJDBCException e){
		    		tx.rollback();
		    		throw new DatabaseException(e.getCause().getMessage(),e.getCause());
		    	}
		    	catch (Exception e){
		    		tx.rollback();
		    		throw new DatabaseException(e.getMessage(), e);
		    	}
		    }
	    }	
	    else {	
	    	if(null != session.getAttribute("canSave")){
	    		
	    			deklaracija = DeklaracijosDelegator.getInstance(request).saveIsvDeclaration(
					    		asmuo,
					    		idf.getAnkstesnePavarde(),
					    		idf.getPilietybe(),
					    		idf.getDokumentoRusis(),
					    		idf.getAsmensDokumentoNumeris(),
					    		idf.getAsmensDokumentoIsdavimoMetai(),
					    		idf.getAsmensDokumentoIsdavimoMenuo(),
					    		idf.getAsmensDokumentoIsdavimoData(),
					    		idf.getAsmensDokumentoIsdave(),	    		
					    		idf.getLeidimoGaliojimoMetai(),
					    		idf.getLeidimoGaliojimoMenuo(),
					    		idf.getLeidimoGaliojimoData(),
					    		idf.getPastabos(),
					    		idf.getIsvykimoMetai(),
					    		idf.getIsvykimoMenuo(),
					    		idf.getIsvykimoData(),			
					    		idf.getAnkstesneGyvenamojiVieta(),
					    		
					    		idf.getKitaGyvenamojiVietaAprasymas(),
					    		idf.getAtvykoIsUzsienioSalis(),
					    		idf.getAtvykoIsUzsienioTextarea(),
					    		idf.getDeklaracijaPateikta(),
					    		idf.getPateikejoVardas(),
					    		idf.getPateikejoPavarde(),
					    		idf.getDeklaravimoMetai(),
					    		idf.getDeklaravimoMenuo(),
					    		idf.getDeklaravimoData(),
					    		idf.getGavimoMetai(),
					    		idf.getGavimoMenuo(),
					    		idf.getGavimoData(),
					    		idf.getTelefonas(),
					    		idf.getEmail(),				
					    		errors.isEmpty(),
					    		false,
					    		null,
					    		null,
					    		request
					    	);

	    		

			//Jeigu deklaracija buvo spausdinama, galima jà áraðyti
			if(null == session.getAttribute("print"))
				session.removeAttribute("canSave");

	    }
	    	    else return mapping.findForward(Constants.ALREADY_SAVED);
	    	
    	}

	    if(!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)){ 
	    	session.setAttribute("lastDeclarationId",String.valueOf(deklaracija.getId()));
	    }
	    
		if(null != session.getAttribute("print"))
		{
			if (deklaracija.getAsmuo() != null && deklaracija.getAsmenvardis()!= null){ 
				deklaracija.getAsmuo().setVardasPavarde(deklaracija.getAsmenvardis());
			}
			session.setAttribute("declaration",deklaracija);
			//DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(request, deklaracija); // I.N. 2010.01.25
			//session.setAttribute("ankstesne_gv",deklaracija.getAnkstesneGV().getPavadinimas());
			//session.setAttribute("ankstesne_gv_pastabos",deklaracija.getAnkstesneVietaValstybesPastabos());			
			String pilietybe = "";
			try {
				pilietybe = deklaracija.getPilietybe().getPilietybe();
			} catch (Exception e){}
			session.setAttribute("deklaracija_pilietybe",pilietybe);
			session.setAttribute("deklaracija_oficialus_pavadinimas",deklaracija.getIstaiga().getOficialusPavadinimasRead());	
			
			return mapping.findForward(Constants.GENERATE_PDF);
		}
		else return mapping.findForward(Constants.CONTINUE);		
	}
}