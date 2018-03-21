package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;


public class AtvDeklaracijaForm extends DeklaracijaForm
{
	private String atvykimoMetai;
	private String atvykimoMenuo;
	private String atvykimoData;	

	private String rysisSuGv;
	private String rysysSuGvKita;	
	
	private String savininkoTipas;
	private String savininkoIgaliotinis;	

	private String jurAsmPavadinimas;
	private String jurAsmKodas;	
	private String jurAsmAdresas;
	
	private String savVardas;
	private String savAsmKodas;	
	private String jurGyvenamojiVieta;

	private String savininkoParasoMetai;
	private String savininkoParasoMenuo;
	private String savininkoParasoData;

	private String addressId;
	private String addressType;
	private String gvtKampoNr;
	
	private String pilietybe;
	private String gvtNrAnkstesne;
	
	private String patalposNaudPlotas;
	private String unikPastatoNr1;
	private String unikPastatoNr2;
	private String unikPastatoNr3;
	private String unikPastatoNr4;
	
	private String savininkas1;
	private String savininkasKodas1;
	private String savininkas2;
	private String savininkasKodas2;
	private String savininkas3;
	private String savininkasKodas3;
	private String savininkas4;
	private String savininkasKodas4;

	private String deklaracijaGaliojaMetai;
	private String deklaracijaGaliojaMenuo;
	private String deklaracijaGaliojaDiena;	

	
	public String getGvtNrAnkstesne() {	return this.gvtNrAnkstesne;	}
	public void setGvtNrAnkstesne(String gvtNrAnkstesne) {	this.gvtNrAnkstesne = gvtNrAnkstesne;}
	public String getPatalposNaudPlotas() {	return this.patalposNaudPlotas;	}
	public void setPatalposNaudPlotas(String patalposNaudPlotas) {	this.patalposNaudPlotas = patalposNaudPlotas;}
	
	public void setGvtKampoNr(String gvtKampoNr){this.gvtKampoNr = gvtKampoNr;}
	public String getGvtKampoNr(){return this.gvtKampoNr;}	
	
	public void setAtvykimoMetai(String atvykimoMetai){this.atvykimoMetai = atvykimoMetai;}
	public String getAtvykimoMetai(){return this.atvykimoMetai;}		
	
	public void setAtvykimoMenuo(String atvykimoMenuo){this.atvykimoMenuo = atvykimoMenuo;}
	public String getAtvykimoMenuo(){return this.atvykimoMenuo;}		
	
	public void setAtvykimoData(String atvykimoData){this.atvykimoData = atvykimoData;}
	public String getAtvykimoData(){return this.atvykimoData;}	
	
	public void setPilietybe(String pilietybe){this.pilietybe = pilietybe;}
	public String getPilietybe(){return this.pilietybe;}	
	
	public void setRysisSuGv(String rysisSuGv){this.rysisSuGv = rysisSuGv;}
	public String getRysisSuGv(){return this.rysisSuGv;}
	
	public void setRysysSuGvKita(String rysysSuGvKita){this.rysysSuGvKita = UtilDelegator.trim(rysysSuGvKita, 500);}
	public String getRysysSuGvKita(){return this.rysysSuGvKita;}
	
	public void setSavininkoTipas(String savininkoTipas){this.savininkoTipas = savininkoTipas;}
	public String getSavininkoTipas(){return this.savininkoTipas;}	
	
	public void setSavininkoIgaliotinis(String savininkoIgaliotinas){this.savininkoIgaliotinis = savininkoIgaliotinas;}
	public String getSavininkoIgaliotinis(){return this.savininkoIgaliotinis;}	
	
	public void setJurAsmPavadinimas(String jurAsmPavadinimas){this.jurAsmPavadinimas = UtilDelegator.trim(jurAsmPavadinimas, 500);}
	public String getJurAsmPavadinimas(){return this.jurAsmPavadinimas;}	
	
	public void setJurAsmKodas(String jurAsmKodas){this.jurAsmKodas = UtilDelegator.trim(jurAsmKodas, 100);}
	public String getJurAsmKodas(){return this.jurAsmKodas;}	

	public void setJurAsmAdresas(String jurAsmAdresas){this.jurAsmAdresas = UtilDelegator.trim(jurAsmAdresas, 500); }
	public String getJurAsmAdresas(){return this.jurAsmAdresas;}	
	
	public void setSavVardas(String savVardas){this.savVardas = UtilDelegator.trim(savVardas, 500);}
	public String getSavVardas(){return this.savVardas;}	

	public void setSavAsmKodas(String savAsmKodas){this.savAsmKodas = UtilDelegator.trim(savAsmKodas, 100);}
	public String getSavAsmKodas(){return this.savAsmKodas;}	
	
	public void setJurGyvenamojiVieta(String jurGyvenamojiVieta){this.jurGyvenamojiVieta = UtilDelegator.trim(jurGyvenamojiVieta, 500);}
	public String getJurGyvenamojiVieta(){return this.jurGyvenamojiVieta;}	

	public void setSavininkoParasoMetai(String savaninkoParasoMetai){this.savininkoParasoMetai = savaninkoParasoMetai;}
	public String getSavininkoParasoMetai(){return this.savininkoParasoMetai;}		
	
	public void setSavininkoParasoMenuo(String savaninkoParasoMenuo){this.savininkoParasoMenuo = savaninkoParasoMenuo;}
	public String getSavininkoParasoMenuo(){return this.savininkoParasoMenuo;}		
	
	public void setSavininkoParasoData(String savaninkoParasoData){this.savininkoParasoData = savaninkoParasoData;}
	public String getSavininkoParasoData(){return this.savininkoParasoData;}
	
	public void setAddressId(String addressId){this.addressId = addressId;}
	public String getAddressId(){return this.addressId;}

	public void setAddressType(String addressType){this.addressType = addressType;}
	public String getAddressType(){return this.addressType;}

	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping,request);
		HttpSession session = request.getSession();
		session.removeAttribute("selectedRysisSuGv");
		session.removeAttribute("checkedSavininkoIgaliotinis");
		int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();		
		
		if (null == rysisSuGv || 0 == rysisSuGv.length()){
			UtilDelegator.setError("error.rysisSuGv",errors,request);
		}
		//todo: atvykimo datos tikrinimas
		/*
		try {
			dateFormat.parse(getAtvykimoMetai() + "-" + getAtvykimoMenuo() + "-" + getAtvykimoData());
		}
		catch (ParseException pe){
			if ("save".equals(request.getParameter("mode"))) {
				UtilDelegator.setError("error.atvykimoData", errors, request);
			}
			else {
				setAtvykimoMetai ("");
				setAtvykimoMenuo ("");
				setAtvykimoData ("");
			}
		}
		
		try {
			if (CalendarUtils.getParsedDate(getAtvykimoMetai(), getAtvykimoMenuo(), getAtvykimoData()).after(CalendarUtils.getParsedDate(super.getDeklaravimoMetai(),super.getDeklaravimoMenuo(),super.getDeklaravimoData())) &&  userStatus != UserDelegator.USER_GLOBAL) {		
				UtilDelegator.setError("error.didesneData", errors, request);			
			}
		}
		catch (NullPointerException npe){
			if ("save".equals(request.getParameter("mode"))) {
				UtilDelegator.setError("error.atvykimoData", errors, request);
			}
			else {
				setAtvykimoMetai ("");
				setAtvykimoMenuo ("");
				setAtvykimoData ("");
			}
		}
		*/
		if ("save".equals(request.getParameter("mode")))
		{
			if ("-1".equals(getPilietybe())) {
				UtilDelegator.setError("error.nenurodytaPilietybe", errors, request);  
			   }	
		}
		session.setAttribute("selectedRysisSuGv",getRysisSuGv());
		if("0".equals(getSavininkoIgaliotinis())){ 
			session.setAttribute("checkedSavininkoIgaliotinis","1");
	        }
			else{
		    session.setAttribute("checkedSavininkoIgaliotinis",getSavininkoIgaliotinis());	
			}
		
		if (!"3".equals(getRysisSuGv())){
			setRysysSuGvKita("");	
			}
		if ("0".equals(getSavininkoTipas())){
			setSavVardas("");
			setSavAsmKodas("");
			setJurGyvenamojiVieta("");
			}
			else {
			setJurAsmPavadinimas("");
			setJurAsmKodas("");
			setJurAsmAdresas("");
			}
		//==================
		// Ar nenurodytas adresas kaip gatve be namo numerio
		/*if("T".equals(getAddressType())){
			TeritorinisVienetas ter = AdresaiDelegator.getInstance().getTerVieneta(Long.valueOf(getAddressId()), request);
			if("G".equals(ter.getTerTipas()))
				UtilDelegator.setError("error.gatveBeAdreso", errors, request);
		} */
		
		//==================
		// Ar neatitinka adresas is galiojancios gyvenamosios vietos?
		Long l = (Long) session.getAttribute("idForEdit"); // NETIKRINAM kai deklraracija REDAGUOJAMA
		Asmuo asmuo1;
		if (l == null) {
			try {
				asmuo1 = QueryDelegator.getInstance().getAsmuoByCode(request, getAsmensKodas());
				Set gyvenamosiosVietos = asmuo1.getGyvenamosiosVietos();
				GyvenamojiVieta tempVieta = null;
				GyvenamojiVieta vieta = null;
				long nr = 0;
				for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) {
					tempVieta = (GyvenamojiVieta) it.next();
					if(tempVieta.getGvtNr() > nr && tempVieta.getGvtDataIki() == null ){
						nr = tempVieta.getGvtNr();
						vieta = tempVieta;
						System.out.println(nr + " " + vieta.getGvtButas() + " " + vieta.getGvtNamas());
					}					
				}
				if (vieta != null && "R".equals(vieta.getGvtTipas())) {
					if (getAddressType().equals("T")
							&& getAddressId().equals(String.valueOf(vieta.getGvtAtvNr()))) {
						UtilDelegator.setError("error.galiojantisAdresas", errors, request);
					}
					if (getAddressType().equals("A")
							&& getAddressId().equals(String.valueOf(vieta.getGvtAdvNr()))) {
						UtilDelegator.setError("error.galiojantisAdresas", errors, request);
					}
				}
			} catch (ObjectNotFoundException e) {
			}
		} else {
			try{
				if(getGvtNrAnkstesne()!=null && !getGvtNrAnkstesne().equals("null") && !getGvtNrAnkstesne().equals("")){
					asmuo1 = QueryDelegator.getInstance().getAsmuoByCode(request, getAsmensKodas());
					
					GyvenamojiVieta ankstesnesGvtAdvNr = QueryDelegator.getInstance().getGyvenamojiVieta(request, asmuo1.getAsmNr(), Long.parseLong(gvtNrAnkstesne));
					
					if (ankstesnesGvtAdvNr != null && "R".equals(ankstesnesGvtAdvNr.getGvtTipas())) {
						if (getAddressType().equals("T")
								&& getAddressId().equals(String.valueOf(ankstesnesGvtAdvNr.getGvtAtvNr()))) {
							UtilDelegator.setError("error.galiojantisAdresas", errors, request);
						}
						if (getAddressType().equals("A")
								&& getAddressId().equals(String.valueOf(ankstesnesGvtAdvNr.getGvtAdvNr()))) {
							UtilDelegator.setError("error.galiojantisAdresas", errors, request);
						}
					}
				}
			} catch (ObjectNotFoundException e) {
			}
		}
    	
		// ==================
		// Validuojame tik tada, kai redaguojama baigta deklaracija
		if (session.getAttribute("activeDeclaration") != null){
			//Asmuo asmuo = (Asmuo)session.getAttribute("asmuo"); I.N.
			
			//if ("".equals(getAsmensDokumentoNumeris())){ //kom ju.k 2007.06.30  
			//	UtilDelegator.setError("error.asmensDokumentoNumeris", errors, request);
			//}
		
			//if (!QueryDelegator.getInstance().hasDocument(request, asmuo, getAsmensDokumentoNumeris())){  //kom ju.k 2007.06.30 
			//	UtilDelegator.setError("error.asmensDokumentoNumeris", errors, request);
			//}
			
			//if ("".equals(getAsmensDokumentoIsdave())){ //kom ju.k 2007.06.30
			//	UtilDelegator.setError("error.asmensDokumentoIsdave", errors, request);
			//}
			
			if (!"0".equals(getDeklaracijaPateikta())){
				if ("".equals(getPateikejoVardas())){
					UtilDelegator.setError("error.pateikejoVardas", errors, request);
				}
				if ("".equals(getPateikejoPavarde())){
					UtilDelegator.setError("error.pateikejoPavarde", errors, request);
				}
			}
			
			if ("".equals(getAddressId())){
				UtilDelegator.setError("error.addressId", errors, request);
			}
			
			if (null == getSavininkoTipas() || "".equals(getSavininkoTipas())){
				UtilDelegator.setError("error.savininkoTipas", errors, request);
			}
			else if (!"0".equals(getRysisSuGv())) {
				if ("0".equals(getSavininkoTipas())){
					if ("".equals(getJurAsmPavadinimas()) || "".equals(getJurAsmKodas()) || "".equals(getJurAsmAdresas())){
						UtilDelegator.setError("error.savininkoTipas", errors, request);
					}
				}
				else {
					if ("".equals(getSavVardas()) || "".equals(getSavAsmKodas())){
						UtilDelegator.setError("error.savininkoTipas", errors, request);
					}
				}
			}
		}
		
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		super.reset(mapping,request);
		HttpSession session = request.getSession();		
		Long l = (Long)session.getAttribute("idForEdit");
		if (null != l){
			try {
				AtvykimoDeklaracija d = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(l,request);
				if(null != d.getSaltinis())
				if(1 == d.getSaltinis().longValue())session.setAttribute("prohibited","true");
				else session.removeAttribute("prohibited");
				if (null != d.getAtvykimoData()){
					setAtvykimoMetai(yearFormat.format(d.getAtvykimoData()));
					setAtvykimoMenuo(monthFormat.format(d.getAtvykimoData()));	    			
					setAtvykimoData(dayFormat.format(d.getAtvykimoData()));	    			
				} 
				
				if (null != d.getGyvenamojiVieta()){
					if ('A' == d.getGyvenamojiVieta().getTipas()){
						setAddressId(String.valueOf(d.getGyvenamojiVieta().getGvtAdvNr()));
						if (null != d.getGyvenamojiVieta().getGvtKampoNr()) {
							setGvtKampoNr(String.valueOf(d.getGyvenamojiVieta().getGvtKampoNr()));
						}
					}
					else { 
						setAddressId(String.valueOf(d.getGyvenamojiVieta().getGvtAtvNr()));
					}
					setAddressType(String.valueOf(d.getGyvenamojiVieta().getTipas()));
				}
				else {
					if (d.getTmpGvtAtvNr().intValue() > 0){
						setAddressId(String.valueOf(d.getTmpGvtAtvNr()));
						setAddressType("T");
					}
					else if (d.getTmpGvtAdvNr().intValue() > 0){
						setAddressId(String.valueOf(d.getTmpGvtAdvNr()));
						if (d.getTmpGvtKampoNr() != null) {
							setGvtKampoNr(String.valueOf(d.getTmpGvtKampoNr()));
						}
						setAddressType("A");
					}
				}
				
				setRysisSuGv(String.valueOf(d.getRysysSuGv()));
				setRysysSuGvKita(d.getRysysSuGvKita());
				setSavininkoTipas(String.valueOf(d.getSavininkoTipas()));
				setSavininkoIgaliotinis(String.valueOf(d.getSavininkoIgaliotinis()));
				
				setJurAsmPavadinimas(d.getJaPavadinimas());
				setJurAsmKodas(d.getJaKodas());
				setJurAsmAdresas(d.getJaBuveine());
				
				setSavVardas(d.getFaVardasPavarde());
				setSavAsmKodas(d.getFaKodas());
				setJurGyvenamojiVieta(d.getFaAdresas());
				setGvtNrAnkstesne(String.valueOf(d.getGvtNrAnkstesne()));
			
				if (null != d.getSavininkoParasoData()){
					setSavininkoParasoMetai(yearFormat.format(d.getSavininkoParasoData()));
					setSavininkoParasoMenuo(monthFormat.format(d.getSavininkoParasoData()));	    			
					setSavininkoParasoData(dayFormat.format(d.getSavininkoParasoData()));	    			
				}
			}
			catch (ObjectNotFoundException onfe){
				onfe.printStackTrace();
			}
		}
		else {
			//setAtvykimoMetai(yearFormat.format(new Date()));
			//setAtvykimoMenuo(monthFormat.format(new Date()));	    			
			//setAtvykimoData(dayFormat.format(new Date()));
			//setSavininkoParasoMetai(yearFormat.format(new Date()));
			//setSavininkoParasoMenuo(monthFormat.format(new Date()));	    			
			//setSavininkoParasoData(dayFormat.format(new Date()));
			setRysisSuGv("0");
			setSavininkoTipas("1");
			setSavininkoIgaliotinis("0");
			setAddressType("");
			setAddressId("");
			setGvtKampoNr("");
			
		}
	}
	public String getUnikPastatoNr1() {
		return unikPastatoNr1;
	}
	public void setUnikPastatoNr1(String unikPastatoNr1) {
		this.unikPastatoNr1 = unikPastatoNr1;
	}
	public String getUnikPastatoNr2() {
		return unikPastatoNr2;
	}
	public void setUnikPastatoNr2(String unikPastatoNr2) {
		this.unikPastatoNr2 = unikPastatoNr2;
	}
	public String getUnikPastatoNr3() {
		return unikPastatoNr3;
	}
	public void setUnikPastatoNr3(String unikPastatoNr3) {
		this.unikPastatoNr3 = unikPastatoNr3;
	}
	public String getUnikPastatoNr4() {
		return unikPastatoNr4;
	}
	public void setUnikPastatoNr4(String unikPastatoNr4) {
		this.unikPastatoNr4 = unikPastatoNr4;
	}
	public String getSavininkas1() {
		return savininkas1;
	}
	public void setSavininkas1(String savininkas1) {
		this.savininkas1 = savininkas1;
	}
	public String getSavininkasKodas1() {
		return savininkasKodas1;
	}
	public void setSavininkasKodas1(String savininkasKodas1) {
		this.savininkasKodas1 = savininkasKodas1;
	}
	public String getSavininkas2() {
		return savininkas2;
	}
	public void setSavininkas2(String savininkas2) {
		this.savininkas2 = savininkas2;
	}
	public String getSavininkasKodas2() {
		return savininkasKodas2;
	}
	public void setSavininkasKodas2(String savininkasKodas2) {
		this.savininkasKodas2 = savininkasKodas2;
	}
	public String getSavininkas3() {
		return savininkas3;
	}
	public void setSavininkas3(String savininkas3) {
		this.savininkas3 = savininkas3;
	}
	public String getSavininkasKodas3() {
		return savininkasKodas3;
	}
	public void setSavininkasKodas3(String savininkasKodas3) {
		this.savininkasKodas3 = savininkasKodas3;
	}
	public String getSavininkas4() {
		return savininkas4;
	}
	public void setSavininkas4(String savininkas4) {
		this.savininkas4 = savininkas4;
	}
	public String getSavininkasKodas4() {
		return savininkasKodas4;
	}
	public void setSavininkasKodas4(String savininkasKodas4) {
		this.savininkasKodas4 = savininkasKodas4;
	}
	public String getDeklaracijaGaliojaMetai() {
		return deklaracijaGaliojaMetai;
	}
	public void setDeklaracijaGaliojaMetai(String deklaracijaGaliojaMetai) {
		this.deklaracijaGaliojaMetai = deklaracijaGaliojaMetai;
	}
	public String getDeklaracijaGaliojaMenuo() {
		return deklaracijaGaliojaMenuo;
	}
	public void setDeklaracijaGaliojaMenuo(String deklaracijaGaliojaMenuo) {
		this.deklaracijaGaliojaMenuo = deklaracijaGaliojaMenuo;
	}
	public String getDeklaracijaGaliojaDiena() {
		return deklaracijaGaliojaDiena;
	}
	public void setDeklaracijaGaliojaDiena(String deklaracijaGaliojaDiena) {
		this.deklaracijaGaliojaDiena = deklaracijaGaliojaDiena;
	}
}