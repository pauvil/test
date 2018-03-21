package com.algoritmusistemos.gvdis.web.forms;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.DeklaracijosDAL;
import lt.solver.gvdis.model.PilietybesObj;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AsmensDokumentas;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;
import com.algoritmusistemos.gvdis.web.persistence.Valstybe;
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;

public class DeklaracijaForm extends ActionForm 
{
	protected SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	protected SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	protected SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	protected SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	
	private String asmensKodas;
	private String documentId;
	
	private String gimimoMetai;
	private String gimimoMenuo;	
	private String gimimoData;	
	
	private String lytis;	
	private String vardas;	
	private String pavarde;
	//private String kitiVardai; --ju.k 2007.06.05	
	private String ankstesnePavarde;
	private String pilietybe;	
	private String pilietybePavadinimas;	
	private String telefonas;	
	private String email;	
	
	
	private String asmensDokumentoNumeris;	
	private String asmensDokumentoIsdave;
	
	private String dokumentoTipas;
	private String dokumentoRusis;
	private String asmensDokumentoIsdavimoMetai;
	private String asmensDokumentoIsdavimoMenuo;
	private String asmensDokumentoIsdavimoData;	

	private String leidimoGaliojimoMetai;
	private String leidimoGaliojimoMenuo;
	private String leidimoGaliojimoData;	
	
	private String pastabos;	

	private String ankstesneGyvenamojiVieta;	
	private String kitaGyvenamojiVietaAprasymas;	

	private String atvykoIsUzsienioSalis;	
	private String atvykoIsUzsienioTextarea;	
	
	private String deklaracijaPateikta;
	private String pateikejoVardas;	
	private String pateikejoPavarde;	
	private String deklaravimoMetai;
	private String deklaravimoMenuo;
	private String deklaravimoData;
	
	private String pageidaujuDokumenta;	
	
	private String gavimoMetai;
	private String gavimoMenuo;
	private String gavimoData;	
	
	private String atmetimoPriezastys;
	private String saltinis;

	private String deklaracijaGaliojaMetai;
	private String deklaracijaGaliojaMenuo;
	private String deklaracijaGaliojaDiena;
	private String unikalusPastatoNr;
	private String unikPastatoNr1;
	private String unikPastatoNr2;
	private String unikPastatoNr3;
	private String unikPastatoNr4;
	private Timestamp deklaracijaGaliojaIkiSena;
	
	public DeklaracijaForm()
	{
		super();
		dateFormat.setLenient(false);
		yearFormat.setLenient(false);
		monthFormat.setLenient(false);
		dayFormat.setLenient(false);
	}

	public void setUnikalusPastatoNr(String unikalusPastatoNr) {
		this.unikalusPastatoNr = unikalusPastatoNr;		
	}
	
	public void setUnikPastatoNr1(String uniklausPastatoNr1){ this.unikPastatoNr1 = uniklausPastatoNr1;}
	public void setUnikPastatoNr2(String uniklausPastatoNr2){ this.unikPastatoNr2 = uniklausPastatoNr2;}
	public void setUnikPastatoNr3(String uniklausPastatoNr3){ this.unikPastatoNr3 = uniklausPastatoNr3;}
	public void setUnikPastatoNr4(String uniklausPastatoNr4){ this.unikPastatoNr4 = uniklausPastatoNr4;}
	
	public String getTelefonas() {return telefonas;	}
	public void setTelefonas(String telefonas) {this.telefonas = telefonas;	}

	public String getEmail() {	return email;}
	public void setEmail(String email) {this.email = email;	}
	
	public void setDocumentId(String documentId){this.documentId = documentId;}
	public String getDocumentId(){return this.documentId;}	

	public void setAsmensKodas(String asmensKodas){this.asmensKodas = asmensKodas;}
	public String getAsmensKodas(){return this.asmensKodas;}	
	
	public void setLytis(String lytis){this.lytis = lytis;}
	public String getLytis(){return this.lytis;}	
	
	public void setVardas(String vardas){this.vardas = vardas;}
	public String getVardas(){return this.vardas;}	
	
	public void setPavarde(String pavarde){this.pavarde = pavarde;}
	public String getPavarde(){return this.pavarde;}	
	
	//public void setKitiVardai(String kitiVardai){this.kitiVardai = kitiVardai;} --ju.k 2007.06.05
	//public String getKitiVardai(){return this.kitiVardai;}	--ju.k 2007.06.05
	
	public void setAnkstesnePavarde(String ankstesnePavarde){this.ankstesnePavarde = ankstesnePavarde;}
	public String getAnkstesnePavarde(){return this.ankstesnePavarde;}	
	
	public void setPilietybe(String pilietybe){this.pilietybe = pilietybe;}
	public String getPilietybe(){return this.pilietybe;}		

	public void setPilietybePavadinimas(String pilietybePavadinimas){this.pilietybePavadinimas = pilietybePavadinimas;}
	public String getPilietybePavadinimas(){return this.pilietybePavadinimas;}		

	public void setGimimoMetai(String gimimoMetai){this.gimimoMetai = gimimoMetai;}
	public String getGimimoMetai(){return this.gimimoMetai;}	
	
	public void setGimimoMenuo(String gimimoMenuo){this.gimimoMenuo = gimimoMenuo;}
	public String getGimimoMenuo(){return this.gimimoMenuo;}		
	
	public void setGimimoData(String gimimoData){this.gimimoData = gimimoData;}
	public String getGimimoData(){return this.gimimoData;}	
	
	public void setDokumentoTipas(String tipas){this.dokumentoTipas = tipas;}
	public String getDokumentoTipas(){return this.dokumentoTipas;}		

	public void setDokumentoRusis(String rusis){this.dokumentoRusis = rusis;}
	public String getDokumentoRusis(){return this.dokumentoRusis;}		

	public void setAsmensDokumentoNumeris(String asmensDokumentoNumeris){this.asmensDokumentoNumeris = asmensDokumentoNumeris;}
	public String getAsmensDokumentoNumeris(){return this.asmensDokumentoNumeris;}		

	public void setAsmensDokumentoIsdavimoMetai(String asmensDokumentoIsdavimoMetai){this.asmensDokumentoIsdavimoMetai = asmensDokumentoIsdavimoMetai;}
	public String getAsmensDokumentoIsdavimoMetai(){return this.asmensDokumentoIsdavimoMetai;}		
	
	public void setAsmensDokumentoIsdavimoMenuo(String asmensDokumentoIsdavimoMenuo){this.asmensDokumentoIsdavimoMenuo = asmensDokumentoIsdavimoMenuo;}
	public String getAsmensDokumentoIsdavimoMenuo(){return this.asmensDokumentoIsdavimoMenuo;}		
	
	public void setAsmensDokumentoIsdavimoData(String asmensDokumentoIsdavimoData){this.asmensDokumentoIsdavimoData = asmensDokumentoIsdavimoData;}
	public String getAsmensDokumentoIsdavimoData(){return this.asmensDokumentoIsdavimoData;}	
	
	public void setAsmensDokumentoIsdave(String asmensDokumentoIsdave){this.asmensDokumentoIsdave = asmensDokumentoIsdave;}
	public String getAsmensDokumentoIsdave(){return this.asmensDokumentoIsdave;}	
	
	public void setLeidimoGaliojimoMetai(String leidimoGaliojimoMetai){this.leidimoGaliojimoMetai = leidimoGaliojimoMetai;}
	public String getLeidimoGaliojimoMetai(){return this.leidimoGaliojimoMetai;}		
	
	public void setLeidimoGaliojimoMenuo(String leidimoGaliojimoMenuo){this.leidimoGaliojimoMenuo = leidimoGaliojimoMenuo;}
	public String getLeidimoGaliojimoMenuo(){return this.leidimoGaliojimoMenuo;}		
	
	public void setLeidimoGaliojimoData(String leidimoGaliojimoData){this.leidimoGaliojimoData = leidimoGaliojimoData;}
	public String getLeidimoGaliojimoData(){return this.leidimoGaliojimoData;}
	
	public void setDeklaracijaGaliojaIkiSena(Timestamp deklaracijaGalioja) {
		this.deklaracijaGaliojaIkiSena = deklaracijaGalioja;
		
	}
	
	public Timestamp getDeklaracijaGaliojaIkiSena()
	{
		return deklaracijaGaliojaIkiSena;
	}
	
	public void setDeklaracijaGaliojaMetai(String deklaracijaGaliojaMetai){this.deklaracijaGaliojaMetai = deklaracijaGaliojaMetai;}
	public String getDeklaracijaGaliojaMetai(){return this.deklaracijaGaliojaMetai;}		
	
	public void setDeklaracijaGaliojaMenuo(String deklaracijaGaliojaMenuo){this.deklaracijaGaliojaMenuo = deklaracijaGaliojaMenuo;}
	public String getDeklaracijaGaliojaMenuo(){return this.deklaracijaGaliojaMenuo;}		
	
	public void setDeklaracijaGaliojaDiena(String deklaracijaGaliojaDiena){this.deklaracijaGaliojaDiena= deklaracijaGaliojaDiena;}
	public String getDeklaracijaGaliojaDiena(){return this.deklaracijaGaliojaDiena;}

	public void setPastabos(String pastabos){this.pastabos = UtilDelegator.trim(pastabos, 2000);}
	public String getPastabos(){return this.pastabos;}	
	
	public void setAnkstesneGyvenamojiVieta(String ankstesneGyvenamojiVieta){this.ankstesneGyvenamojiVieta = ankstesneGyvenamojiVieta;}
	public String getAnkstesneGyvenamojiVieta(){return this.ankstesneGyvenamojiVieta;}	
	
	public void setKitaGyvenamojiVietaAprasymas(String kitaGyvenamojiVietaAprasymas){this.kitaGyvenamojiVietaAprasymas = UtilDelegator.trim(kitaGyvenamojiVietaAprasymas, 240);}
	public String getKitaGyvenamojiVietaAprasymas(){return this.kitaGyvenamojiVietaAprasymas;}
	
	public void setAtvykoIsUzsienioSalis(String atvykoIsUzsienioSalis){this.atvykoIsUzsienioSalis = atvykoIsUzsienioSalis;}
	public String getAtvykoIsUzsienioSalis(){return this.atvykoIsUzsienioSalis;}
	
	public void setAtvykoIsUzsienioTextarea(String atvykoIsUzsienioTextarea){this.atvykoIsUzsienioTextarea = UtilDelegator.trim(atvykoIsUzsienioTextarea, 240);}
	public String getAtvykoIsUzsienioTextarea(){return this.atvykoIsUzsienioTextarea;}	
	
	public void setDeklaracijaPateikta(String deklaracijaPateikta){this.deklaracijaPateikta = deklaracijaPateikta;}
	public String getDeklaracijaPateikta(){return this.deklaracijaPateikta;}		
	
	public void setPateikejoVardas(String pateikejoVardas){this.pateikejoVardas = pateikejoVardas;}
	public String getPateikejoVardas(){return this.pateikejoVardas;}	
	
	public void setPateikejoPavarde(String pateikejoPavarde){this.pateikejoPavarde = pateikejoPavarde;}
	public String getPateikejoPavarde(){return this.pateikejoPavarde;}	
	
	public void setDeklaravimoMetai(String deklaravimoMetai){this.deklaravimoMetai = deklaravimoMetai;}
	public String getDeklaravimoMetai(){return this.deklaravimoMetai;}		
	
	public void setDeklaravimoMenuo(String deklaravimoMenuo){this.deklaravimoMenuo = deklaravimoMenuo;}
	public String getDeklaravimoMenuo(){return this.deklaravimoMenuo;}		
	
	public void setDeklaravimoData(String deklaravimoData){this.deklaravimoData = deklaravimoData;}
	public String getDeklaravimoData(){return this.deklaravimoData;}	
	
	public void setPageidaujuDokumenta(String pageidaujuDokumenta){this.pageidaujuDokumenta = pageidaujuDokumenta;}
	public String getPageidaujuDokumenta(){return this.pageidaujuDokumenta;}		
	
	public void setGavimoMetai(String gavimoMetai){this.gavimoMetai = gavimoMetai;}
	public String getGavimoMetai(){return this.gavimoMetai;}		
	
	public void setGavimoMenuo(String gavimoMenuo){this.gavimoMenuo = gavimoMenuo;}
	public String getGavimoMenuo(){return this.gavimoMenuo;}		
	
	public void setGavimoData(String gavimoData){this.gavimoData = gavimoData;}
	public String getGavimoData(){return this.gavimoData;}
	
	public String getAtmetimoPriezastys() {
		return atmetimoPriezastys;
	}

	public void setAtmetimoPriezastys(String atmetimoPriezastys) {
		this.atmetimoPriezastys = atmetimoPriezastys;
	}	

	public String getSaltinis() {
		return saltinis;
	}

	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		
		Object asmuo = session.getAttribute("asmuo");
		
		String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));

		
		if("-1".equals(documentId))
			documentId = null;
		
		// Date currentDate = new Date(); I.N.
		//try {
			//if (!(this instanceof IsvDeklaracijaForm)) {
				//if (documentId != null && !"".equals(documentId)) {					
					//String docId = null;
					//if (session.getAttribute("idForEdit") != null) { // Redaguojam deklaracija
						//Deklaracija d = DeklaracijosDelegator.getInstance(request).getDeklaracija((Long) session.getAttribute("idForEdit"), request);
						//docId = DeklaracijosDelegator.getInstance(request).getDocumentIdByNr(request,d.getDokumentoNr());
					//}
					//if (!documentId.equals(docId)) {
						//AsmensDokumentas dok = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, new Long(getDocumentId()).longValue());
						//V.L. uþkomentuota, nes turi iðsaugoti ir negaliojanti dokumenta
						/*if(dok != null && !dok.arGaliojantis())
							UtilDelegator.setError("error.negaliojantisDokumentas", errors, request);*/
						
						/*if (dok != null
								&& (dok.getDokGaliojaIki() != null && currentDate.getTime() > dok.getDokGaliojaIki().getTime())
								|| ("N".equals(dok.getDokBusena())))
							UtilDelegator.setError("error.negaliojantisDokumentas", errors, request);*/
					//}
//				}
//			}
//		} catch (NumberFormatException e) {
//		} catch (ObjectNotFoundException e) {
//		}
		
		request.setAttribute("ankstesneVietaTipas",ankstesneGyvenamojiVieta);
		if (null == pageidaujuDokumenta || 0 == pageidaujuDokumenta.length()){
			UtilDelegator.setError("error.pageidaujuDokumenta", errors, request);
		}
		
		if ("print".equals(request.getParameter("mode")))
		{
			request.getSession().setAttribute("print", "true");
		}
		
		if ("reject".equals(request.getParameter("mode")))
		{
			if(atmetimoPriezastys.length()<6){
				UtilDelegator.setError("error.atmetimoPriezastis", errors, request);
			}
		}
		
		if ("save".equals(request.getParameter("mode"))){
			/*if (null == dokumentoTipas || 0 == dokumentoTipas.length()){
				UtilDelegator.setError("error.dokumentoTipas", errors, request);
			}*/
						
		    if(!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) {
				if (getDocumentId() == null || "".equals(getDocumentId())){				
					if (!"".equals(asmensDokumentoIsdavimoMetai) || !"".equals(asmensDokumentoIsdavimoMenuo) || !"".equals(asmensDokumentoIsdavimoData)) try {
						dateFormat.parse(asmensDokumentoIsdavimoMetai + "-" + asmensDokumentoIsdavimoMenuo + "-" + asmensDokumentoIsdavimoData);
					}
					catch (ParseException pe){
						UtilDelegator.setError("error.asmensDokumentoIsdavimoData", errors, request);
					}
					if (null != dokumentoTipas && 0 < dokumentoTipas.length()){
						long znrId = Long.parseLong(dokumentoTipas);
						ZinynoReiksme znr = ZinynaiDelegator.getInstance().getZinynoReiksme(request, znrId);
						if ("LL".equals(znr.getKodas()) || "LL_EB".equals(znr.getKodas()) || "LL_ES".equals(znr.getKodas())) try {
							dateFormat.parse(leidimoGaliojimoMetai + "-" + leidimoGaliojimoMenuo + "-" + leidimoGaliojimoData);
						}
						catch (ParseException pe){
							UtilDelegator.setError("error.leidimoGaliojimoData", errors, request);
						}
					}
				}
			
			
				// Nepilnametis negali deklaruoti pats
				if("0".equals(getDeklaracijaPateikta())) {
					if (DeklaracijosDelegator.getInstance(request).chkNepilnametis(request)) {
							UtilDelegator.setError("error.deklaracijaPateiktaVaiko", errors, request);
					}
				}	 
				
				// netikrinti pateiktu e. dekl kitas tikrinti 
				if (getSaltinis() == null) {
					if ("0".equals(getDeklaracijaPateikta()) && 
							!DeklaracijosDelegator.getInstance(request).chkNepilnametis(request) && 
							(getPilietybe().equals("LTU") || getPilietybe().equals("-1"))){ //-1 kai jsp pirmas irasas (nera pilietybes) 
							//Pilnametis turi pateikti dokumenta
							if (!DeklaracijosDelegator.getInstance(request).chkNepilnametis(request) && null == getDocumentId()) 
								UtilDelegator.setError("error.deklaracijaNepateiktasAsmensDokumentas", errors, request);
							else if(getAsmensDokumentoNumeris()=="" || getAsmensDokumentoIsdave()=="" || getAsmensDokumentoIsdavimoMetai()=="" || getAsmensDokumentoIsdavimoMenuo()=="" || getAsmensDokumentoIsdavimoData()=="")
						  		UtilDelegator.setError("error.deklaracijaNepateiktasAsmensDokumentas", errors, request);
						  		else if (null != getDocumentId() && 0 < getDocumentId().length()) {
						  			String dokRusis = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, Long.parseLong(getDocumentId())).getDokRusiesKodas();
						  			if (!Constants.OUT_DECLARATION_FORM.equals(String.valueOf(session.getAttribute(Constants.CENTER_STATE)))){  
						  				if (!DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, Long.parseLong(getDocumentId())).arGaliojantis()){
						  					UtilDelegator.setError("error.deklaracijaNepateiktasAsmensDokumentas", errors, request);}
						  			}
						  			if (!dokRusis.equals("P") && !dokRusis.equals("I")){
						  				UtilDelegator.setError("error.deklaracijaNepateiktasAsmensDokumentas", errors, request);}
						   	}	
				     }
				}
				
				
				// Jei deklaracija (atvykimo, iðvykimo) pateikta ne asmeniðkai (vieno ið tëvø, globëjo, kito teisëto atstovo), tai bûtina nurodyti deklaracijà pateikusio asmens vardà ir pavardæ
				if("1".equals(getDeklaracijaPateikta()) || "2".equals(getDeklaracijaPateikta()) || "3".equals(getDeklaracijaPateikta()) ) {
					// uþ asmená, kuriam jau yra 16 metø deklaracijà pildo vienas ið tëvø
						
					if ("1".equals(getDeklaracijaPateikta()) && !DeklaracijosDelegator.getInstance(request).chkNepilnametisFull(request)) {
						UtilDelegator.setError("error.deklaracijaPateiktaTevu", errors, request);
					} else {
						if (getPateikejoVardas() == null || "".equals(getPateikejoVardas()) ) {
							UtilDelegator.setError("error.pateikejoVardas", errors, request);
						}
						if (getPateikejoPavarde() == null || "".equals(getPateikejoPavarde()) ) {
							UtilDelegator.setError("error.pateikejoPavarde", errors, request);
						}
					}
				}
		    }
		}
		
		if (null == deklaracijaPateikta || 0 == deklaracijaPateikta.length()){
			UtilDelegator.setError("error.deklaracijaPateikta", errors, request);
		}
		//Tikrinam deklaracija galioja iki data gali buti ne mazesne negu sysdate + 1 month
		Date deklGaliojimoIkiData = CalendarUtils.getParsedDate( getDeklaracijaGaliojaMetai() ,getDeklaracijaGaliojaMenuo(),getDeklaracijaGaliojaDiena());
		if (deklGaliojimoIkiData != null)
		{
			if (deklGaliojimoIkiData.before(new Date()))
				UtilDelegator.setError("error.deklaravimoGaliojimoDataBefore", errors, request);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 1); 
			Date deklPliusMenuo = calendar.getTime();
			
			if( deklPliusMenuo.after(deklGaliojimoIkiData) ) {
				UtilDelegator.setError("error.deklaravimoGaliojimoDataBefore", errors, request);
			}
			if (session.getAttribute("activeDeclaration") != null && getDeklaracijaGaliojaIkiSena() != null){
				if( deklGaliojimoIkiData.before(getDeklaracijaGaliojaIkiSena()))
					UtilDelegator.setError("error.deklaravimoGaliojimoDataEdit", errors, request);
			}
			
		}
		Date deklData = CalendarUtils.getParsedDate(getDeklaravimoMetai(),getDeklaravimoMenuo(),getDeklaravimoData());
		// Ar deklaravimo data daugiau uz sysdate
		if (deklData.after(new Date())) {
			UtilDelegator.setError("error.deklaravimoDataMax", errors, request);
		}
		try {
 			boolean didelisSkirtumas = false;
			if (session.getAttribute("idForEdit")==null || ((Integer)session.getAttribute("userStatus")).intValue()==UserDelegator.USER_GLOBAL) {							
				int userStatus1 = ((Integer)session.getAttribute("userStatus")).intValue(); //ju.k 2007.09.26

				if ("save".equals(request.getParameter("mode")) && userStatus1 != UserDelegator.USER_GLOBAL) {
					
					// Deklaracijos data
					
					
					// Ar deklaravimo data daugiau uz sysdate
					if (deklData.after(new Date())) {
						UtilDelegator.setError("error.deklaravimoDataMax", errors, request);
					}
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(deklData);
					calendar.add(Calendar.DAY_OF_MONTH, 7); //Solver romasj 2011.kovo.02. CR3a
					//calendar.add(Calendar.DAY_OF_MONTH, 28);
					deklData = calendar.getTime();
					
					if (deklData.before(new Date())) {
						UtilDelegator.setError("error.didelisSkirtumas", errors, request);
						didelisSkirtumas=true;
					}
				}
			}			
			if (!didelisSkirtumas) {				
				deklData = CalendarUtils.getParsedDate(getDeklaravimoMetai(),getDeklaravimoMenuo(),getDeklaravimoData());
				Calendar cal = Calendar.getInstance();
				cal.set(2007,Calendar.JULY,1);
				Date pradzia = cal.getTime();
				if (deklData.before(pradzia)) {
					if ("save".equals(request.getParameter("mode"))) {
						UtilDelegator.setError("error.sistemaNeveike", errors, request);					
					}
				}
			}
		}
		catch (NullPointerException npe) {
			if ("save".equals(request.getParameter("mode")) && (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM) || (Address)session.getAttribute("ankstesnisAdresas") != null)) {
				UtilDelegator.setError("error.deklaravimoData", errors, request);
			}
			else {
				setDeklaravimoMetai("");
				setDeklaravimoMenuo("");
				setDeklaravimoData("");
			}
		}
		
		if ("save".equals(request.getParameter("mode"))){
			if (asmuo instanceof Asmuo  && session.getAttribute("activeDeclaration") == null){  // Netikriname redaguojamoms deklaracijoms
				try {
					deklData = dateFormat.parse(deklaravimoMetai + "-" + deklaravimoMenuo + "-" + deklaravimoData);
					Date minData = QueryDelegator.getInstance().minValidDeclDate(request, (Asmuo)asmuo);
					// þiûrime, ar reikia palikti vietos fiktyviam áraðui
					if ("1".equals(ankstesneGyvenamojiVieta)) try { 
						Address ankstesnisAdr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(((Asmuo)asmuo).getAsmNr(), 0, request);
						if (ankstesnisAdr != null && ankstesnisAdr.getId() > 0){
							GyvenamojiVieta gvt = QueryDelegator.getInstance().getGyvenamojiVieta(request, ((Asmuo)asmuo).getAsmNr(), ankstesnisAdr.getId());
							String countryCode = gvt.getValstybe().getKodas();
							if (!QueryDelegator.HOME_COUNTRY.equals(atvykoIsUzsienioSalis) && !atvykoIsUzsienioSalis.equals(countryCode)){
								Calendar cal = Calendar.getInstance();
								cal.setTime(minData);
								cal.add(Calendar.DATE, 1);
								minData = cal.getTime();
							}
						}
					}
					catch (Exception e){
					}
					if (QueryDelegator.getInstance().isGvtDataNuo(request, (Asmuo) asmuo, deklData))
						UtilDelegator.setError("error.dataEgzistuoja", errors, request);
					else {
						//int userStatus = ((Integer) session.getAttribute("userStatus")).intValue(); I.N. 											
						if (deklData.before(minData)/* && userStatus != UserDelegator.USER_GLOBAL*/) {
							UtilDelegator.setError("error.deklaravimoDataMin", errors, request);
						}
					}
//					
//					String isv_dekl = (String)session.getAttribute("isvdekl"); //ju.k 2007.09.26
//					if (deklData.after(new Date()) && isv_dekl == null && userStatus != UserDelegator.USER_GLOBAL){
//						UtilDelegator.setError("error.deklaravimoDataMax", errors, request);
//					}
//					
//					if (isv_dekl != null && userStatus != UserDelegator.USER_GLOBAL) { //ju.k 2007.09.26
//						Date deklData_i_ateiti = new Date();
//						Calendar cal_i_ateiti = Calendar.getInstance();
//						cal_i_ateiti.setTime(deklData_i_ateiti);
//						cal_i_ateiti.add(Calendar.DATE,7);
//						deklData_i_ateiti = cal_i_ateiti.getTime();
//						if (deklData.after(deklData_i_ateiti)) {
//							UtilDelegator.setError("error.deklaravimoDataMax", errors, request);
//						    }
//				   }
//					
//                    }


				}
				catch (Exception pe){		
					if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM) || (Address)session.getAttribute("ankstesnisAdresas") != null){
						UtilDelegator.setError("error.deklaravimoData", errors, request);
					}
				}
			}
		}
		
		
		if ("save".equals(request.getParameter("mode"))){
		    if(!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) {
		    	if (null == ankstesneGyvenamojiVieta || 0 == ankstesneGyvenamojiVieta.length()){
		    		UtilDelegator.setError("error.ankstesneGyvenamojiVieta", errors, request);
		    	}
		    	else if ("2".equals(ankstesneGyvenamojiVieta)){
				/*if (null == kitaGyvenamojiVietaAprasymas || 0 == kitaGyvenamojiVietaAprasymas.length()){
					UtilDelegator.setError("error.kitaGyvenamojiVietaAprasymas", errors, request);
				}*/
		    	}
		    }
		}
		
		// Jei perduotas dokumento ID, ið jo uþpildome kitus laukus
    	if (getDocumentId() != null && !"".equals(getDocumentId())) try {
    		long id = Long.parseLong(getDocumentId());
    		AsmensDokumentas dok = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, id);
    		// Jei dokumentas negaliojantis, laukai paliekami tusti
    		// V.L. 2009-10-12 laukus uþpildome visais atvejais
    		//if (dok.getDokBusena().equals("G") && dok.getDokGaliojaIki().after(new Date())) {
				setAsmensDokumentoNumeris(dok.getDokNum());
				setPilietybe(dok.getPilietybe(request) != null ? dok.getPilietybe(request).getKodas() : "");
				setPilietybePavadinimas(dok.getPilietybe(request) != null ? dok.getPilietybe(request).getPilietybe() : "");
				setAsmensDokumentoIsdave(dok.getDokTarnyba() != null ? dok.getDokTarnyba() : (dok.getTarnyba() != null ? dok.getTarnyba() : ""));
				setDokumentoRusis(dok.getDokRusis());
				if (dok.getDokIsdData() != null){
					setAsmensDokumentoIsdavimoMetai(yearFormat.format(dok.getDokIsdData()));
					setAsmensDokumentoIsdavimoMenuo(monthFormat.format(dok.getDokIsdData()));
					setAsmensDokumentoIsdavimoData(dayFormat.format(dok.getDokIsdData()));
				}
				if (dok.getDokGaliojaIki() != null){
					setLeidimoGaliojimoMetai(yearFormat.format(dok.getDokGaliojaIki()));
					setLeidimoGaliojimoMenuo(monthFormat.format(dok.getDokGaliojaIki()));
					setLeidimoGaliojimoData(dayFormat.format(dok.getDokGaliojaIki()));
				}
    		//}
    	}
    	catch (Exception e){
    	}
    	else {
    		setDokumentoRusis("Kitas");
    	}
		   	
		return errors;
	}	
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
	    HttpSession session = request.getSession();
	    try {
	    	request.setAttribute("dokumentuTipai", ZinynaiDelegator.getInstance().getZinynoReiksmes(request, "DOKUMENTO_TIPAS"));
	    }
	    catch (ObjectNotFoundException e){
	    	e.printStackTrace();
	    }
		Long l = (Long)session.getAttribute("idForEdit");
	    if (null != l) {		// Redaguojame anksèiau ávestà, bet nebaigtà pildyti deklaracijà 
	    	try {
	    		this.saltinis = String.valueOf(DeklaracijosDelegator.getInstance(request).getDeklaracija(l, request).getSaltinis());
	    		Deklaracija d = DeklaracijosDelegator.getInstance(request).getDeklaracija(l,request);
				request.setAttribute("ankstesneVietaTipas",String.valueOf(d.getAnkstesneVietaTipas()));
	    		if (null != d.getAsmuo()){
		    		setAsmensKodas(String.valueOf(d.getAsmuo().getAsmKodas()));
		    		
		    		String[] asmenvardziai = new String[2];
				/*
		    		try {
						asmenvardziai = d.getAsmuo().getVardasPavardeByDate(request, d.getDeklaravimoData(), d.getDokumentoNr());
					} catch (DatabaseException e) {						
						e.printStackTrace();
					}
					*/ // I.N. 2010.01.25
		    		String vardas = asmenvardziai[0]; String pavarde = asmenvardziai[1]; 
		    		if((vardas != null && !"".equals(vardas)) && (pavarde != null && !"".equals(pavarde))){
		    			setVardas(vardas);
		    			setPavarde(pavarde);
		    		}
		    		else{
		    			d.getAsmuo().setVardasPavarde(d.getAsmenvardis());
		    			setVardas(d.getAsmuo().getVardas());		    		
		    			setPavarde(d.getAsmuo().getPavarde());
		    		}
		    		//setAnkstesnePavarde(d.getAsmuo().getPavardePrev());
		    		setLytis(d.getAsmuo().getAsmLytis());
	    			if (null != d.getAsmuo().getAsmGimData()){
			    		setGimimoMetai(yearFormat.format(d.getAsmuo().getAsmGimData()));
			    		setGimimoMenuo(monthFormat.format(d.getAsmuo().getAsmGimData()));
			    		setGimimoData(dayFormat.format(d.getAsmuo().getAsmGimData()));
	    			}
	    		}
	    		else {
		    		setGimimoMetai(yearFormat.format(d.getLaikinasAsmuo().getGimimoData()));
		    		setGimimoMenuo(monthFormat.format(d.getLaikinasAsmuo().getGimimoData()));
		    		setGimimoData(dayFormat.format(d.getLaikinasAsmuo().getGimimoData()));
		    		setVardas(d.getLaikinasAsmuo().getVardas());
		    		setPavarde(d.getLaikinasAsmuo().getPavarde());
		    		setLytis(d.getLaikinasAsmuo().getLytis());
		    		
	    		}	    		
 	    		if (null == d.getPilietybe().getKodas()){
 	    			/*setPilietybe(QueryDelegator.HOME_COUNTRY);
 	    			setPilietybePavadinimas("LIETUVOS");*/
 	    		}
 	    		else {
 	    			setPilietybe(d.getPilietybe().getKodas());
 	    			setPilietybePavadinimas(d.getPilietybe().getPilietybe());
 	    		}
	    		
	    		setAsmensDokumentoNumeris(d.getDokumentoNr());
	    		String docId = DeklaracijosDelegator.getInstance(request).getDocumentIdByNr(request, d.getDokumentoNr());
	    		if(!"".equals(docId))
	    			setDocumentId(DeklaracijosDelegator.getInstance(request).getDocumentIdByNr(request, d.getDokumentoNr()));
	    		else
	    			setDocumentId("-1");
	    		if(d.getAnkstesnePavarde() != null && !d.getAnkstesnePavarde().equals("")){
	    			setAnkstesnePavarde(d.getAnkstesnePavarde());
	    		}
	    		setDokumentoRusis(d.getDokumentoRusis());
	    		if (null != d.getDokumentoData()){
	    			setAsmensDokumentoIsdavimoMetai(yearFormat.format(d.getDokumentoData()));
	    			setAsmensDokumentoIsdavimoMenuo(monthFormat.format(d.getDokumentoData()));
	    			setAsmensDokumentoIsdavimoData(dayFormat.format(d.getDokumentoData()));	 
	    		}
	    		setAsmensDokumentoIsdave(d.getDokumentoIsdavejas());
	    		if (null != d.getDokumentoGaliojimas()){
		    		setLeidimoGaliojimoMetai(yearFormat.format(d.getDokumentoGaliojimas()));
		    		setLeidimoGaliojimoMenuo(monthFormat.format(d.getDokumentoGaliojimas()));	    			
		    		setLeidimoGaliojimoData(dayFormat.format(d.getDokumentoGaliojimas()));	    			
	    		}
	    		setDeklaracijaPateikta(String.valueOf(d.getPateike()));
	    		if(0 != d.getPateike().intValue()){
	    			setPateikejoVardas(d.getPateikeVardas());
	    			setPateikejoPavarde(d.getPateikePavarde());
	    		}	    		
	    		if (null != d.getDeklaravimoData()){
		    		setDeklaravimoMetai(yearFormat.format(d.getDeklaravimoData()));
		    		setDeklaravimoMenuo(monthFormat.format(d.getDeklaravimoData()));	    			
		    		setDeklaravimoData(dayFormat.format(d.getDeklaravimoData()));	    			
	    		}
	    		if (null != d.getGavimoData()){
			    	setGavimoMetai(yearFormat.format(d.getGavimoData()));
			    	setGavimoMenuo(monthFormat.format(d.getGavimoData()));
			    	setGavimoData(dayFormat.format(d.getGavimoData()));	    			
	    		}	
	    		if (null != d.getDeklaracijaGalioja()){
	    			setDeklaracijaGaliojaIkiSena(d.getDeklaracijaGalioja());
	    			setDeklaracijaGaliojaMetai(yearFormat.format(d.getDeklaracijaGalioja()));
	    			setDeklaracijaGaliojaMenuo(monthFormat.format(d.getDeklaracijaGalioja()));
	    			setDeklaracijaGaliojaDiena(dayFormat.format(d.getDeklaracijaGalioja()));	    			
	    		}
	    		if (null != d.getTelefonas())
	    		{
	    			setTelefonas(d.getTelefonas());
	    		}
	    		if (null != d.getEmail()){
	    			setEmail(d.getEmail());
	    		}
	    		if (null != d.getUnikalusPastatoNr()){
    				String uniqHouseCode = d.getUnikalusPastatoNr();
    				int startBruksniukas = uniqHouseCode.indexOf("-"); 
    				setUnikPastatoNr1(uniqHouseCode.substring(0,startBruksniukas));
    			 	int pabaigaBruksniukas = uniqHouseCode.indexOf("-", startBruksniukas + 1);
    			 	setUnikPastatoNr2(uniqHouseCode.substring(startBruksniukas + 1,pabaigaBruksniukas));
    			 	startBruksniukas = pabaigaBruksniukas;
    			 	pabaigaBruksniukas = uniqHouseCode.indexOf(":", startBruksniukas + 1);	 	
    			 	if(pabaigaBruksniukas < 0)    			 		
    			 		setUnikPastatoNr3(uniqHouseCode.substring(startBruksniukas +1,uniqHouseCode.length()));
    			 	else
    			 		setUnikPastatoNr3(uniqHouseCode.substring(startBruksniukas +1, pabaigaBruksniukas));
    			 	startBruksniukas = pabaigaBruksniukas;
    			 	pabaigaBruksniukas = uniqHouseCode.indexOf(":");
    			 	if(pabaigaBruksniukas > 0)
    			 		setUnikPastatoNr4(uniqHouseCode.substring(startBruksniukas + 1, uniqHouseCode.length()));
    			}  			

	    		
		 	    setPastabos(d.getPastabos());
		 	    setAnkstesneGyvenamojiVieta(String.valueOf(d.getAnkstesneVietaTipas()));
	    		setKitaGyvenamojiVietaAprasymas(d.getAnkstesneVietaKita());
	    		if (null != d.getAnkstesneGV()){
	    			setAtvykoIsUzsienioSalis(((Valstybe)d.getAnkstesneGV()).getKodas());
	    		}
	    		setAtvykoIsUzsienioTextarea(d.getAnkstesneVietaValstybesPastabos());
	    		setPageidaujuDokumenta(String.valueOf(d.getPageidaujaPazymos()));
	    		
    		
	    	}
	    	catch(ObjectNotFoundException onfe){
	    		onfe.printStackTrace();
	    	}
	    }
	    else {		// Pildome naujà deklaracijà
			String type = (String)session.getAttribute("asm_type");
			Long id = (Long)session.getAttribute("asm_id");
			setPageidaujuDokumenta("0");
    		setDeklaracijaPateikta("0");
    		setDeklaravimoMetai(yearFormat.format(new Date()));
    		setDeklaravimoMenuo(monthFormat.format(new Date()));	    			
    		setDeklaravimoData(dayFormat.format(new Date()));
    		if(((String)session.getAttribute("declaration_mode")).equals(Constants.GVNA_DECLARATION_FORM))
    	    {
    			Calendar kalendorius = Calendar.getInstance();
    			kalendorius.setTime(new Date());
    			kalendorius.add(Calendar.MONTH, 12);    			
    			setDeklaracijaGaliojaMetai(yearFormat.format(kalendorius.getTime()));
    			setDeklaracijaGaliojaMenuo(monthFormat.format(kalendorius.getTime()));
    			setDeklaracijaGaliojaDiena(dayFormat.format(kalendorius.getTime()));
    	    }

    		Address ankstesnisAdresas = (Address)session.getAttribute("ankstesnisAdresas");
	 	    if (ankstesnisAdresas == null || ankstesnisAdresas.getAdr() == null){
	 	    	setAnkstesneGyvenamojiVieta("2");
	 	    }
	 	    else {
	 	    	setAnkstesneGyvenamojiVieta("0");
	 	    }
	 	    
	 	    if ("const".equals(type)){
	 	    	try {
	 	    		Asmuo asm = (Asmuo)session.getAttribute("asmuo");
	 	    		Asmuo a = UserDelegator.getInstance().getAsmuoByAsmKodas(asm.getAsmKodas().longValue(),request);
	 	    		//setAnkstesnePavarde(a.getPavardePrev());
	 	    		
					setAsmensKodas(String.valueOf(a.getAsmKodas()));
					setVardas(a.getVardas());
					setPavarde(a.getPavarde());
					setLytis(a.getAsmLytis());
					if (a.getAsmGimData() != null){
						setGimimoMetai(yearFormat.format(a.getAsmGimData()));
						setGimimoMenuo(monthFormat.format(a.getAsmGimData()));
						setGimimoData(dayFormat.format(a.getAsmGimData()));
					}
					
					//CR6 Solver 2011.04.28 - naujai deklaracijai pilietybe imama is GR, ne is dokumentu
					PilietybesObj pilietybe = 
								DeklaracijosDAL.getInstance().getAsmensGRPilietybe(request, a.getAsmNr());
					setPilietybe(pilietybe.getKodas());
					setPilietybePavadinimas(pilietybe.getPilietybe());
					
					/* 
					List dokumentai = DeklaracijosDelegator.getInstance(request).getAsmensDokumentai(request,a, "");
					if(0 != dokumentai.size())
					{
						AsmensDokumentas ad = (AsmensDokumentas)dokumentai.iterator().next();
			    		setPilietybe(ad.getPilietybe(request) != null ? ad.getPilietybe(request).getKodas() : "");
			    		setPilietybePavadinimas(ad.getPilietybe(request) != null ? ad.getPilietybe(request).getPilietybe() : "");
						
					}
					else
					{
	 	    			//setPilietybe(QueryDelegator.HOME_COUNTRY);
	 	    			//setPilietybePavadinimas("LIETUVOS");
					}
					 */
	 	    	}
	 	    	catch(ObjectNotFoundException onfe){
	 	    		onfe.printStackTrace();
	 	    	}
	 	    	catch(DatabaseException de){
	 	    		de.printStackTrace();
	 	    	}
	 	    	
	 	    }
	 	    else
			if ("temp".equals(type)){	// Deklaruoja "laikinas" asmuo
				if (null != id){
					try	{
						LaikinasAsmuo la = DeklaracijosDelegator.getInstance(request).getLaikinasAsmuo(id,request);
						setVardas(la.getVardas());
						setPavarde(la.getPavarde());
						//if (null != la.getKitiVardai()) setKitiVardai(la.getKitiVardai());
						setLytis(la.getLytis());
						setPilietybe(la.getPilietybe().getKodas());
						
						setGimimoMetai(yearFormat.format(la.getGimimoData()));
						setGimimoMenuo(monthFormat.format(la.getGimimoData()));
						setGimimoData(dayFormat.format(la.getGimimoData()));
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
	    }
	}








}
