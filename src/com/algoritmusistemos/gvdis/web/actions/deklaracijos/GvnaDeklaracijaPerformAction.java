package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.rmi.RemoteException;
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
import com.algoritmusistemos.gvdis.web.forms.GvnaDeklaracijaForm;
import com.algoritmusistemos.gvdis.web.persistence.GvdisBase;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvnaPazyma;

public class GvnaDeklaracijaPerformAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException, DatabaseException,InternalException,PermissionDeniedException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		HttpSession session = request.getSession(); 	    
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_GVNA);	    
	    GvnaDeklaracijaForm gdf = (GvnaDeklaracijaForm)form;
		GvdisBase asmuo = (GvdisBase)session.getAttribute("asmuo");
		Set errors = new HashSet(); 
		Set warnings = new HashSet(); 
		session.removeAttribute("gvnaPazyma");
		// Validacija
		try {
		
			if (DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, Long.parseLong(gdf.getDocumentId())).getDokBusena().equals("N")) {
//				warnings.add("Pasirinktas asmens dokumentas yra negaliojantis.");
				errors.add("Pasirinktas asmens dokumentas yra negaliojantis.");
			}
		}
		catch (NumberFormatException nfe) {}
		catch (NullPointerException npe) {}

		if("-1".equals(gdf.getDocumentId())) 
			gdf.setDocumentId(null);		
		
		if (gdf.getDocumentId() == null || "".equals(gdf.getDocumentId())){
			if ("".equals(gdf.getAsmensDokumentoNumeris())){
				if (QueryDelegator.HOME_COUNTRY.equals(gdf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
					errors.add("Nenurodytas asmens dokumento numeris.");
				}
				//else { //kom ju.k 2007.06.30
				//	errors.add("Nenurodytas asmens dokumento numeris.");
				//}
			}
			
			else if (!QueryDelegator.getInstance().hasDocument(request, asmuo, gdf.getAsmensDokumentoNumeris())){
				if (QueryDelegator.HOME_COUNTRY.equals(gdf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
					errors.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu.");
				}
				//else {
				//	errors.add("Klaidingas asmens dokumento numeris - ðiam asmeniui nëra iðduotas asmens dokumentas su tokiu numeriu.");
				//}
			}
			
			if ("".equals(gdf.getAsmensDokumentoIsdave())){
				if (QueryDelegator.HOME_COUNTRY.equals(gdf.getPilietybe()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)){
					warnings.add("Nenurodyta asmens dokumentà iðdavusi ástaiga.");
				}
				//else { //kom 2007.06.30
				//	errors.add("Nenurodyta asmens dokumentà iðdavusi ástaiga.");
				//}
			}
		}

		//	Ar pilnameèiui nebandoma pildyti deklaracija vieno ið tëvø ar globëjø
		if (!DeklaracijosDelegator.getInstance(request).chkNepilnametisFull(request) &&
				("1".equals(gdf.getDeklaracijaPateikta()))
			){
			errors.add("Pilnameèio asmens deklaracija negali bûti pildoma vieno ið tëvø(átëviø).");
		}
		else if (!"0".equals(gdf.getDeklaracijaPateikta())){
			if ("".equals(gdf.getPateikejoVardas()) || "".equals(gdf.getPateikejoPavarde())){
				errors.add("Nenurodyti praðymà pateikusio asmens vardas ir pavardë (praðyme paþymëta, kad praðymas pateiktas kito asmens)."); 
			}
		}
		session.setAttribute("declErrors", errors);
		session.setAttribute("declWarnings", warnings);
		GvnaDeklaracija deklaracija = null;
		
		Long l = (Long)session.getAttribute("idForEdit");
	    if (null != l){
	    	if ("reject".equals(request.getParameter("mode"))){
	    		try {
	    			DeklaracijosDelegator.getInstance(request).rejectDeclaration(l, gdf.getAtmetimoPriezastys(), request);
	    		} catch ( AxisFault e){
	    			request.setAttribute("wserror", e.getMessage());
	    		} catch ( RemoteException e){
	    			request.setAttribute("wserror", e.getMessage());
	    		}
	    		
	    		request.setAttribute("id", l);
	    		session.setAttribute("CENTER_STATE", Constants.REJECT_DEKLARACIJA);
	    		return mapping.findForward(Constants.REJECT_DEKLARACIJA);
	    	}
	    	deklaracija = DeklaracijosDelegator.getInstance(request).updateGvnaDeclaration(
	    		l,
	    		gdf.getAnkstesnePavarde(),
	    		gdf.getPilietybe(),
	    		gdf.getSavivaldybe(),
	    		gdf.getDokumentoRusis(),

	    		gdf.getAsmensDokumentoNumeris(),
	    		gdf.getAsmensDokumentoIsdavimoMetai(),
	    		gdf.getAsmensDokumentoIsdavimoMenuo(),
	    		gdf.getAsmensDokumentoIsdavimoData(),

	    		gdf.getAsmensDokumentoIsdave(),	    		
	    		gdf.getLeidimoGaliojimoMetai(),
	    		gdf.getLeidimoGaliojimoMenuo(),
	    		gdf.getLeidimoGaliojimoData(),

	    		gdf.getPastabos(),
	    		gdf.getSavivaldybePastabos(),
	    		gdf.getPriezastys(),
					
	    		gdf.getAnkstesneGyvenamojiVieta(),
	    		gdf.getKitaGyvenamojiVietaAprasymas(),
	    		gdf.getAtvykoIsUzsienioSalis(),
	    		gdf.getAtvykoIsUzsienioTextarea(),
		    		
	    		gdf.getDeklaracijaPateikta(),
	    		gdf.getPateikejoVardas(),
	    		gdf.getPateikejoPavarde(),
	    		gdf.getDeklaravimoMetai(),
	    		gdf.getDeklaravimoMenuo(),
	    		gdf.getDeklaravimoData(),
	    		gdf.getPageidaujuDokumenta(),
		    		
	    		gdf.getGavimoMetai(),
	    		gdf.getGavimoMenuo(),
	    		gdf.getGavimoData(),
	    		gdf.getTelefonas(),
	    		gdf.getEmail(),
	    		gdf.getDeklaracijaGaliojaMetai(),
	    		gdf.getDeklaracijaGaliojaMenuo(),
	    		gdf.getDeklaracijaGaliojaDiena(),

	    		errors.isEmpty(),
				(session.getAttribute("activeDeclaration") != null),
	    		request
    		);	    	
			// Uþfiksuojame asmens duomenø perþiûros faktà auditui
	    	if (deklaracija != null) try {
	    			long auditId = 0;
	    			if(null != deklaracija.getAsmuo())
	    			{
	    				auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(deklaracija.getAsmuo().getAsmNr()), Constants.AUDIT_DEKL_REDAGAVIMAS, "GVNA deklaracijos redagavimas");
	    				AuditDelegator.getInstance().auditPersonResult(request, auditId, deklaracija.getAsmuo());
	    			}
	    	}
	    	catch (Exception nfe){
	    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
	    	}
	    	
			if(null == session.getAttribute("print"))
			{
		    	// Pasiruoðiame paþymà formavimui
		    	if (deklaracija.getPageidaujaPazymos().intValue() == 1 && errors.isEmpty() ){
		    		GvnaPazyma pazyma = new GvnaPazyma();
		    		pazyma.setPazymosData(deklaracija.getDeklaravimoData());
		    		pazyma.setGyvenamojiVieta(deklaracija.getGyvenamojiVieta());
		    		pazyma.setDeklaracija(deklaracija);
		    		session.setAttribute("gvnaPazyma", pazyma);
		    	}
			}	    	
	    }
	    else {
	    	if (null != session.getAttribute("canSave")){
		    	deklaracija = DeklaracijosDelegator.getInstance(request).saveGvnaDeclaration(
		    		asmuo,
		    		gdf.getAnkstesnePavarde(),
		    		gdf.getPilietybe(),
		    		gdf.getSavivaldybe(),
		    		gdf.getDokumentoRusis(),
	
		    		gdf.getAsmensDokumentoNumeris(),
		    		gdf.getAsmensDokumentoIsdavimoMetai(),
		    		gdf.getAsmensDokumentoIsdavimoMenuo(),
		    		gdf.getAsmensDokumentoIsdavimoData(),
	
		    		gdf.getAsmensDokumentoIsdave(),	    		
		    		gdf.getLeidimoGaliojimoMetai(),
		    		gdf.getLeidimoGaliojimoMenuo(),
		    		gdf.getLeidimoGaliojimoData(),
	
		    		gdf.getPastabos(),
		    		gdf.getSavivaldybePastabos(),
		    		gdf.getPriezastys(),
					
		    		gdf.getAnkstesneGyvenamojiVieta(),
		    		gdf.getKitaGyvenamojiVietaAprasymas(),
		    		gdf.getAtvykoIsUzsienioSalis(),
		    		gdf.getAtvykoIsUzsienioTextarea(),
		    		
		    		gdf.getDeklaracijaPateikta(),
		    		gdf.getPateikejoVardas(),
		    		gdf.getPateikejoPavarde(),
		    		gdf.getDeklaravimoMetai(),
		    		gdf.getDeklaravimoMenuo(),
		    		gdf.getDeklaravimoData(),
		    		gdf.getPageidaujuDokumenta(),
		    		
		    		gdf.getGavimoMetai(),
		    		gdf.getGavimoMenuo(),
		    		gdf.getGavimoData(),

		    		gdf.getTelefonas(),
		    		gdf.getEmail(),
		    		gdf.getDeklaracijaGaliojaMetai(),
		    		gdf.getDeklaracijaGaliojaMenuo(),
		    		gdf.getDeklaracijaGaliojaDiena(),

	
		    		errors.isEmpty(),
		    		request
		    	);
				if(null == session.getAttribute("print"))
				{
			    	// Pasiruoðiame paþymà formavimui
			    	if (deklaracija.getPageidaujaPazymos().intValue() == 1 && errors.isEmpty()){
			    		GvnaPazyma pazyma = new GvnaPazyma();
			    		pazyma.setPazymosData(deklaracija.getDeklaravimoData());
			    		pazyma.setGyvenamojiVieta(deklaracija.getGyvenamojiVieta());
			    		pazyma.setDeklaracija(deklaracija);
			    		session.setAttribute("gvnaPazyma", pazyma);
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
			if("-1".equals(gdf.getSavivaldybe()))
			session.setAttribute("savivaldybes_pav"," ");
			else
			session.setAttribute("savivaldybes_pav",deklaracija.getSavivaldybe().getTerPav());
			
			if(null != deklaracija.getAnkstesneGV())session.setAttribute("ankstesne_gv_pavadinimas",deklaracija.getAnkstesneGV().getPavadinimas());			
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
