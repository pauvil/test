package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.lang.NumberFormatException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis.AxisFault;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.AtvDeklaracijaForm;
import com.algoritmusistemos.gvdis.web.persistence.AsmensDokumentas;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GvdisBase;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;


public class AtvDeklaracijaPerformAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException, DatabaseException,InternalException,PermissionDeniedException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		HttpSession session = request.getSession(); 
		AtvDeklaracijaForm adf = (AtvDeklaracijaForm)form;
		GvdisBase asmuo = (GvdisBase)session.getAttribute("asmuo");
		session.removeAttribute("gvPazyma");
		
		Set errors = new HashSet(); 
		Set warnings = new HashSet();

		Long l = (Long)session.getAttribute("idForEdit");

		// Validacija
		//if (adf.getDocumentId() == null || "".equals(adf.getDocumentId())){ //kom ju.k. 2007.06.30
			//if ("".equals(adf.getAsmensDokumentoNumeris())){ //kom ju.k. 2007.06.30
			//	errors.add("Nenurodytas asmens dokumento numeris."); 
			//}
			
			//if (!QueryDelegator.getInstance().hasDocument(request, asmuo, adf.getAsmensDokumentoNumeris())){ //ju.k 2007.06.30
			//	errors.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu."); 
			//}
			
			//if ("".equals(adf.getAsmensDokumentoIsdave())){ //ju.k 2007.06.30
			//	errors.add("Nenurodyta asmens dokumentà iðdavusi ástaiga."); 
			//}
		//}	
		
		Constants.Println(request, "atvDeklFormKampNR:   " + adf.getGvtKampoNr());
		
		
		try {
			/*
			if (DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, Long.parseLong(adf.getDocumentId())).getDokBusena().equals("N")) {
			//warnings.add("Pasirinktas asmens dokumentas yra negaliojantis.");
			errors.add("Pasirinktas asmens dokumentas yra negaliojantis.");	
			}
		*/
			AsmensDokumentas asmDoc = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, Long.parseLong(adf.getDocumentId()));
			if (asmDoc.getDokBusena().equals("N")) {
				Timestamp deklData = CalendarUtils.getParsedDate(adf.getDeklaravimoMetai(), 
																adf.getDeklaravimoMenuo(), 
																adf.getDeklaravimoData());
				if (deklData.after(asmDoc.getDokNegaliojaNuo())) 
					errors.add("Pasirinktas asmens dokumentas yra negaliojantis.");	
			}
		}
		catch (NumberFormatException nfe) {}
		catch (NullPointerException npe) {} 
		
		if("-1".equals(adf.getDocumentId())) 
			adf.setDocumentId(null);
		if ( adf.getDocumentId() == null || "".equals(adf.getDocumentId())){
			if ("".equals(adf.getAsmensDokumentoNumeris())){
				if (QueryDelegator.HOME_COUNTRY.equals(adf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
					//warnings.add("Nenurodytas asmens dokumento numeris.");
					errors.add("Nenurodytas asmens dokumento numeris.");
				}
				//else { //kom ju.k 2007.06.30
				//	errors.add("Nenurodytas asmens dokumento numeris.");
				//}
			}
			
			else //if (DeklaracijosDelegator.getInstance().getAtvykimoDeklaracija(l, request).getSaltinis() == null || !DeklaracijosDelegator.getInstance().getAtvykimoDeklaracija(l, request).getSaltinis().equals(Long.valueOf(1))) {
				if (QueryDelegator.HOME_COUNTRY.equals(adf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
					if (QueryDelegator.getInstance().hasDocument(request, asmuo, adf.getAsmensDokumentoNumeris())){
					//warnings.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu.");
					errors.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu.");
					}
				}
//			}
			if ("".equals(adf.getAsmensDokumentoIsdave())){
				if (QueryDelegator.HOME_COUNTRY.equals(adf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
					warnings.add("Nenurodyta asmens dokumentà iðdavusi ástaiga.");
				}
				//else { //kom 2007.06.30
				//	errors.add("Nenurodyta asmens dokumentà iðdavusi ástaiga.");
				//}
			}
		}

		//	Ar pilnameèiui nebandoma pildyti deklaracija vieno ið tëvø ar globëjø
		if (!DeklaracijosDelegator.getInstance(request).chkNepilnametisFull(request) &&
				("1".equals(adf.getDeklaracijaPateikta()))
			){
			errors.add("Pilnameèio asmens deklaracija negali bûti pildoma vieno ið tëvø(átëviø).");
		}
		else if (!"0".equals(adf.getDeklaracijaPateikta())){
			if ("".equals(adf.getPateikejoVardas()) || "".equals(adf.getPateikejoPavarde())){
				errors.add("Nenurodyti praðymà pateikusio asmens vardas ir pavardë (praðyme paþymëta, kad praðymas pateiktas kito asmens)."); 
			}
		}
		
		if ("".equals(adf.getAddressId())){
			errors.add("Nesuformuotas naujas gyvenamosios vietos adresas."); 
		}
		
		if (null == adf.getSavininkoTipas() || "".equals(adf.getSavininkoTipas())){
			errors.add("Nenurodytas patalpø savininko tipas."); 
		}
		else if (!"0".equals(adf.getRysisSuGv())) {
			if ("0".equals(adf.getSavininkoTipas())){
				/*V.L. 2009-10-02 pgl. MANTIS klaidà ID 0002360 */
				//if ("".equals(adf.getJurAsmPavadinimas()) || "".equals(adf.getJurAsmKodas()) || "".equals(adf.getJurAsmAdresas())){
				if ("".equals(adf.getJurAsmKodas()) || "".equals(adf.getJurAsmAdresas())){
					errors.add("Nenurodyti patalpø savininko (juridinio asmens) atributai."); 
				}
			}
			else {
				/*V.L. 2009-10-02 pgl. MANTIS klaidà ID 0002360 */
				//if ("".equals(adf.getSavVardas()) || "".equals(adf.getSavAsmKodas())){
				if ("".equals(adf.getSavAsmKodas())){
					errors.add("Nenurodyti patalpø savininko (fizinio asmens) atributai."); 
				}
				// V.L. 2009-10-19 jei þinom savininko AK nustatom ir saviniko vardà pavardæ
				// V.L. 2009-10-23 pasirodo lauke gali bûti keli AK, nustatymà uþkomentuojam
				/*else{
					if ("".equals(adf.getSavVardas())){
						Asmuo svAsmuo = null;
						svAsmuo = QueryDelegator.getInstance().getAsmuoByCode(request, adf.getSavAsmKodas());
						adf.setSavVardas(svAsmuo.getVardas()+' '+svAsmuo.getPavarde());
						System.out.println("SavVardas " + svAsmuo.getVardas()+' '+svAsmuo.getPavarde());
					}
				}*/
			}
		}
		session.setAttribute("declErrors", errors);
		session.setAttribute("declWarnings", warnings);
		
		AtvykimoDeklaracija deklaracija = null;
		if (null != l){
			if ("reject".equals(request.getParameter("mode"))){
				try {
					DeklaracijosDelegator.getInstance(request).rejectDeclaration(l, adf.getAtmetimoPriezastys(), request);
				} catch ( AxisFault e){
	    			request.setAttribute("error", e.getMessage());
	    		} catch ( RemoteException e){
	    			request.setAttribute("error", e.getMessage());
	    		}
	    		request.setAttribute("id", l);
	    		session.setAttribute("CENTER_STATE", Constants.REJECT_DEKLARACIJA);
				session.removeAttribute("prohibited");
	    		return mapping.findForward(Constants.REJECT_DEKLARACIJA);
	    	}
	    	
			deklaracija = DeklaracijosDelegator.getInstance(request).updateAtvDeclaration(
					l,
					adf.getAnkstesnePavarde(),
					adf.getPilietybe(),
					adf.getDokumentoRusis(),
					adf.getAsmensDokumentoNumeris(),
					adf.getAsmensDokumentoIsdavimoMetai(),
					adf.getAsmensDokumentoIsdavimoMenuo(),
					adf.getAsmensDokumentoIsdavimoData(),
					
					adf.getAsmensDokumentoIsdave(),	    		
					adf.getLeidimoGaliojimoMetai(),
					adf.getLeidimoGaliojimoMenuo(),
					adf.getLeidimoGaliojimoData(),
					
					adf.getAtvykimoMetai(),
					adf.getAtvykimoMenuo(),
					adf.getAtvykimoData(),
					adf.getPastabos(),
					
					adf.getAnkstesneGyvenamojiVieta(),
					adf.getKitaGyvenamojiVietaAprasymas(),
					adf.getAtvykoIsUzsienioSalis(),
					adf.getAtvykoIsUzsienioTextarea(),
					
					adf.getDeklaracijaPateikta(),
					adf.getPateikejoVardas(),
					adf.getPateikejoPavarde(),
					adf.getDeklaravimoMetai(),
					adf.getDeklaravimoMenuo(),
					adf.getDeklaravimoData(),
					adf.getPageidaujuDokumenta(),
					
					adf.getRysisSuGv(),
					adf.getRysysSuGvKita(),
					adf.getSavininkoTipas(),
					adf.getSavininkoIgaliotinis(),
					adf.getJurAsmKodas(),
					adf.getJurAsmPavadinimas(),
					adf.getJurAsmAdresas(),
					
					adf.getSavVardas(),
					adf.getSavAsmKodas(),
					adf.getJurGyvenamojiVieta(),
					
					adf.getGavimoMetai(),
					adf.getGavimoMenuo(),
					adf.getGavimoData(),
					
					adf.getAddressId(),
					adf.getAddressType(),
					adf.getGvtKampoNr(),
					adf.getTelefonas(),
					adf.getEmail(),
					adf.getDeklaracijaGaliojaMetai(),
					adf.getDeklaracijaGaliojaMenuo(),
					adf.getDeklaracijaGaliojaDiena(),
					
					adf.getSavininkas1(),
					adf.getSavininkas2(),
					adf.getSavininkas3(),
					adf.getSavininkas4(),
					adf.getSavininkasKodas1(),
					adf.getSavininkasKodas2(),
					adf.getSavininkasKodas3(),
					adf.getSavininkasKodas4(),
					adf.getUnikPastatoNr1(),
					adf.getUnikPastatoNr2(),
					adf.getUnikPastatoNr3(),
					adf.getUnikPastatoNr4(),
					
					errors.isEmpty(),
					(session.getAttribute("activeDeclaration") != null),
					request					
			);
			session.removeAttribute("prohibited");
			// Uþfiksuojame asmens duomenø perþiûros faktà auditui
	    	if (deklaracija != null) try {
	    			long auditId = 0;
	    			if(null != deklaracija.getAsmuo())
	    			{
	    				auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(deklaracija.getAsmuo().getAsmNr()), Constants.AUDIT_DEKL_REDAGAVIMAS, "Atvykimo deklaracijos redagavimas");
	    				AuditDelegator.getInstance().auditPersonResult(request, auditId, deklaracija.getAsmuo());
	    			}
	    	}
	    	catch (Exception nfe){
	    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
	    	}
	    	
			// Pasiruoðiame paþymà formavimui
			if (deklaracija.getPageidaujaPazymos().intValue() == 1 && errors.isEmpty())
			{
				GvPazyma pazyma = new GvPazyma();
				pazyma.setPazymosData(deklaracija.getDeklaravimoData());
				pazyma.setGyvenamojiVieta(deklaracija.getGyvenamojiVieta());
				//pazyma.getGyvenamojiVieta().getAsmuo().getAsmKodas()
				pazyma.setDeklaracija(deklaracija);
				session.setAttribute("gvPazyma", pazyma);
			}	    	
		}
		else {
			if(null != session.getAttribute("canSave"))
			{
				
				deklaracija = DeklaracijosDelegator.getInstance(request).saveAtvDeclaration(
						asmuo,
						adf.getAnkstesnePavarde(),
						adf.getPilietybe(),
						adf.getDokumentoRusis(),
						adf.getAsmensDokumentoNumeris(),
						adf.getAsmensDokumentoIsdavimoMetai(),
						adf.getAsmensDokumentoIsdavimoMenuo(),
						adf.getAsmensDokumentoIsdavimoData(),
						
						adf.getAsmensDokumentoIsdave(),	    		
						adf.getLeidimoGaliojimoMetai(),
						adf.getLeidimoGaliojimoMenuo(),
						adf.getLeidimoGaliojimoData(),
						
						adf.getAtvykimoMetai(),
						adf.getAtvykimoMenuo(),
						adf.getAtvykimoData(),
						adf.getPastabos(),
						
						adf.getAnkstesneGyvenamojiVieta(),
						adf.getKitaGyvenamojiVietaAprasymas(),
						adf.getAtvykoIsUzsienioSalis(),
						adf.getAtvykoIsUzsienioTextarea(),
						
						adf.getDeklaracijaPateikta(),
						adf.getPateikejoVardas(),
						adf.getPateikejoPavarde(),
						adf.getDeklaravimoMetai(),
						adf.getDeklaravimoMenuo(),
						adf.getDeklaravimoData(),
						adf.getPageidaujuDokumenta(),
						
						adf.getRysisSuGv(),
						adf.getRysysSuGvKita(),
						adf.getSavininkoTipas(),
						adf.getSavininkoIgaliotinis(),
						adf.getJurAsmKodas(),
						adf.getJurAsmPavadinimas(),
						adf.getJurAsmAdresas(),
						
						adf.getSavVardas(),
						adf.getSavAsmKodas(),
						adf.getJurGyvenamojiVieta(),
						
						adf.getGavimoMetai(),
						adf.getGavimoMenuo(),
						adf.getGavimoData(),
						
						adf.getAddressId(),
						adf.getAddressType(),
						adf.getGvtKampoNr(),
						adf.getTelefonas(),
						adf.getEmail(),
						
						adf.getSavininkas1(),
						adf.getSavininkasKodas1(),
						adf.getUnikPastatoNr1(),
						adf.getUnikPastatoNr2(),
						adf.getUnikPastatoNr3(),
						adf.getUnikPastatoNr4(),
						adf.getDeklaracijaGaliojaMetai(),
						adf.getDeklaracijaGaliojaMenuo(),
						adf.getDeklaracijaGaliojaDiena(),
						errors.isEmpty(),
						false, //ne internetu pateikiama deklaracija
						null,
						null,
						request
				);
				
				if(null == session.getAttribute("print"))
				{
					// Pasiruoðiame paþymà formavimui
					if (deklaracija.getPageidaujaPazymos().intValue() == 1 && errors.isEmpty())
					{
						GvPazyma pazyma = new GvPazyma();
						pazyma.setPazymosData(deklaracija.getDeklaravimoData());
						pazyma.setGyvenamojiVieta(deklaracija.getGyvenamojiVieta());
						//pazyma.getGyvenamojiVieta().getAsmuo().getAsmKodas()
						pazyma.setDeklaracija(deklaracija);
						session.setAttribute("gvPazyma", pazyma);
					}
					if(errors.size()>0){
						session.removeAttribute("gvPazyma");
					}
				}
				//Jeigu deklaracija buvo spausdinama, galimë jà áraðyti
				if(null == session.getAttribute("print"))
					session.removeAttribute("canSave");
			}
			else return mapping.findForward(Constants.ALREADY_SAVED);
		}
		session.setAttribute("lastDeclarationId",String.valueOf(deklaracija.getId()));
		if(null != session.getAttribute("print"))
		{
			if (deklaracija.getAsmuo() != null && deklaracija.getAsmenvardis() != null){
				deklaracija.getAsmuo().setVardasPavarde(deklaracija.getAsmenvardis());
			}
			session.setAttribute("declaration",deklaracija);
			//DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(request, deklaracija); // I.N. 2010.01.25
			/*String vardai[] = deklaracija.getAsmuo().getVardasPavardeByDate(request, deklaracija.getAtvykimoData());
			session.setAttribute("asmVardas", vardai[0]);
			session.setAttribute("asmPavarde", vardai[1]);*/
			
			String pilietybe = "";
			try {
				pilietybe = deklaracija.getPilietybe().getPilietybe();
			} catch (Exception e){}
			session.setAttribute("deklaracija_pilietybe",pilietybe);
			
			session.setAttribute("deklaracija_oficialus_pavadinimas",deklaracija.getIstaiga().getOficialusPavadinimasRead());			
			
			return mapping.findForward(Constants.GENERATE_PDF);
		}
		else
		{ 
			session.removeAttribute("prohibited");
			return mapping.findForward(Constants.CONTINUE);
		}		
	}
}
