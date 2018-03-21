package com.algoritmusistemos.gvdis.web.forms;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.SprendimaiDAL;
import lt.solver.gvdis.util.Helper;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;

public class SprendimasForm extends ActionForm {
	
	private String id;
	private String regNr;	
	private String data;	
	private String naikinimoData;	
	private String priezastis;
	private String tipas;
	private String prieme;
	private String addressType;
    private String addressTer;
    private String addressAdr;
    private String addressString;
    private String valstybe;
    private String valstybePast;
	private String pastabos;
	private String [] asmenys = new String[]{};
	private String prasymas;
	private String prasymoPavadinimas;
	private String fixTipas;
	private String gvtKampoNr;
	private String asmenysSuNeteisingaisDuomenimis = "";
    private String addressAdrNaikinamas;
    private String addressTerNaikinamas;
    private String addressStringNaikinamas;
	
	public String getAsmenysSuNeteisingaisDuomenimis() { return asmenysSuNeteisingaisDuomenimis; }
	public void setAsmenysSuNeteisingaisDuomenimis(String asmenysSuNeteisingaisDuomenimis) { this.asmenysSuNeteisingaisDuomenimis = asmenysSuNeteisingaisDuomenimis; }
	public String getPrasymas() {
		return prasymas;
	}
	public void setPrasymas(String prasymas) {
		this.prasymas = prasymas;
	}
	public String getAddressAdr() { return addressAdr; }
	public void setAddressAdr(String addressAdr) { this.addressAdr = addressAdr; }
	
	public String getGvtKampoNr() {	return gvtKampoNr; }
	public void setGvtKampoNr(String gvtKampoNr) { this.gvtKampoNr = gvtKampoNr; }		

	public String getAddressString() { return addressString; }
	public void setAddressString(String addressString) { this.addressString = addressString; }

	public String getAddressTer() {	return addressTer; }
	public void setAddressTer(String addressTer) { this.addressTer = addressTer; }

	public String[] getAsmenys() { return asmenys; }
	public void setAsmenys(String[] asmenys) { this.asmenys = asmenys; }

	public String getData() { return data; }
	public void setData(String data) { this.data = data; }

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getNaikinimoData() { return naikinimoData; }
	public void setNaikinimoData(String naikinimoData) { this.naikinimoData = naikinimoData; }

	public String getPastabos() { return pastabos; }
	public void setPastabos(String pastabos) { this.pastabos = UtilDelegator.trim(pastabos, 2000); }

	public String getPrieme() {	return prieme; }
	public void setPrieme(String prieme) { this.prieme = prieme; }

	public String getPriezastis() {	return priezastis; }
	public void setPriezastis(String priezastis) { this.priezastis = priezastis; }

	public String getRegNr() {return regNr; }
	public void setRegNr(String regNr) { this.regNr = regNr; }

	public String getTipas() { return tipas; }
	public void setTipas(String tipas) { this.tipas = tipas; }

	public String getValstybe() { return valstybe; }
	public void setValstybe(String valstybe) { this.valstybe = valstybe; }
	
	public String getValstybePast() { return valstybePast; }
	public void setValstybePast(String valstybePast) { this.valstybePast = UtilDelegator.trim(valstybePast, 240); }
	
	public String getAddressType() { return addressType; }
	public void setAddressType(String addressType) { this.addressType = addressType; }

	
	public String getAddressAdrNaikinamas() { return addressAdrNaikinamas; }
	public void setAddressAdrNaikinamas(String addressAdrNaikinamas) { this.addressAdrNaikinamas = addressAdrNaikinamas; }

	public String getAddressTerNaikinamas() { return addressTerNaikinamas; }
	public void setAddressTerNaikinamas(String addressTerNaikinamas) { this.addressTerNaikinamas = addressTerNaikinamas; }
	
	public String getAddressStringNaikinamas() { return addressStringNaikinamas; }
	public void setAddressStringNaikinamas(String addressStringNaikinamas) { this.addressStringNaikinamas = addressStringNaikinamas;}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		HttpSession session = request.getSession();
		String langas = (String)session.getAttribute("createSprendimas");
		
		boolean doubleSprendimai = SprendimaiDAL.getInstance().checkDoubleSprendimai(request, regNr);
		
		if (langas != null) {
			//naujas sprendimas
			if (doubleSprendimai) {
				//dubliuojasi sprendimai- neleidziame ivesti
				errors.add("regNr", new ActionMessage("errRegNrDouble"));
				request.setAttribute("errRegNrDouble", "");
			}
			boolean uzimtasPrasymas = SprendimaiDAL.getInstance().checkPrasymaiUsage(request, 
									Helper.string2Long(prasymas, 0));
			if (uzimtasPrasymas) {
				//kazkas jau spejo priimti sprendima siam prasymui!
				errors.add("prasymas", new ActionMessage("errPrasymasOccupied"));
				request.setAttribute("errPrasymasOccupied", "");
			}
			
		} else {
			//redaguojamas sprendimas
			String oldRegNr = (String)session.getAttribute("oldRegNr");
			if (!oldRegNr.equalsIgnoreCase(regNr)) {
				if (doubleSprendimai) {
					//dubliuojasi sprendimai- neleidziame ivesti
					errors.add("regNr", new ActionMessage("errRegNrDouble"));
					request.setAttribute("errRegNrDouble", "");
				}
			}
			session.removeAttribute("oldRegNr");
		}
		/*
		if (doubleSprendimai && langas != null) {
			//dubliuojasi sprendimai- neleidziame ivesti
			errors.add("regNr", new ActionMessage("errRegNrDouble"));
			request.setAttribute("errRegNrDouble", "");
		}
		*/
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		
		try {
			Date d = format.parse(data);
			if(new Timestamp(System.currentTimeMillis()).before(d))
			{
				errors.add("title", new ActionMessage("error.biggerCreateData"));
				request.setAttribute("error.biggerCreateData", "");
			}
		}
        catch (ParseException pe){
			errors.add("data", new ActionMessage("err.data"));
        	request.setAttribute("errData", "");
        }
        
		try {
			Date n = format.parse(naikinimoData);
			if(new Timestamp(System.currentTimeMillis()).before(n))
			{
				errors.add("naikinimoData1", new ActionMessage("error.biggerNaikData"));
				request.setAttribute("error.biggerNaikData", "");
			}
		}
        catch (ParseException pe){
			errors.add("naikinimoData", new ActionMessage("err.data"));
        	request.setAttribute("errNaikinimoData", "");
        }
        
        if (!"2".equals(getTipas()) && !"3".equals(getTipas()) && "0".equals(getAddressType())){
            if(getAddressAdr().length() < 1 && getAddressTer().length() < 1){
                errors.add("addressString", new ActionMessage("errors.emptyAddress"));
            	request.setAttribute("errAdresas", "");
            }
        }

        if (asmenys == null || asmenys.length == 0){
			errors.add("asmenys", new ActionMessage("err.asmenys"));
        	request.setAttribute("errAsmenys", "");
        }           
        
        if(priezastis.equals("36")){
        	if(prasymas == null || "".equals(prasymas)){
        		errors.add("errPrasymas", new ActionMessage("errors.errPrasymas"));
            	request.setAttribute("errPrasymas", "");
        	}
        }
        if(false == errors.isEmpty()) return errors;
		//////////////////////
	    
	    boolean klaida =false;
	    if (langas != null) {  
        	int asmCount = getAsmenys().length;
        	// 2012-05-09 Tieto: pridëtas 0 ir 1 tipai; pridëtas asmenø su neteisingais duomenimis sàraðas;
        	// klaidø praneðimai pakeisti á tikslesnius
	   	    if(getTipas().equals("0") || getTipas().equals("1") || getTipas().equals("2")){ // Taisyti, Keisti, Naikinti
	   	    	try{
	   	    		asmenysSuNeteisingaisDuomenimis = "";
	   	    		boolean nesutampaAdresas = false;
	   	    		int countGVTWithR = 0;
	   	    		String tmpGvtAdvNr;
	     			String tmpGvtAtvNr;
	   	    		for (int i=0; i<asmCount; i++){
	   	    			boolean yraDuomenys = false;
	   	    			//Tieto paraiska nr. 7
	   	    			nesutampaAdresas = false; // tikrinimui, ad naikinamas adresas sutampa su paskutine deklaruota gyv. vieta.
	   	    			long id = Long.parseLong(getAsmenys()[i]);
	   	    			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
	   	    			Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();		
	   	    			for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) {
		   	     			GyvenamojiVieta gv = (GyvenamojiVieta) it.next();
		   	     			if (gv.getGvtTipas().equals("R") && gv.getGvtDataIki() == null) { // Deklaruota galiojanti gyvenamoji vieta
		   	     				countGVTWithR++;
		   	     				yraDuomenys = true;
		   	     				//Tieto paraiska nr. 7; Reikia susirinkti visus
		   	     				//galiojancius adresus ir po to tikrinti su ivestu naikinamu adresu....
		   	     				tmpGvtAdvNr = (gv.getGvtAdvNr() == null) ? "" : gv.getGvtAdvNr().toString();
		   	     				tmpGvtAtvNr = (gv.getGvtAtvNr() == null) ? "" : gv.getGvtAtvNr().toString();
		   	     				if((!tmpGvtAdvNr.equals(getAddressAdrNaikinamas())) || (!tmpGvtAtvNr.equals(getAddressTerNaikinamas()))){ 
		   	     					nesutampaAdresas = true;
		   	     				}
		   	     				break;
		   	     			}
		   	     		}
	   	    			/*for (Iterator it=gyvenamosiosVietos.iterator(); it.hasNext(); ){
			        		GyvenamojiVieta vt = (GyvenamojiVieta)it.next();			        		
			        		if (null == vt.getGvtDataIki()){
			        			if(!"R".equals(vt.getGvtTipas())){
			        				errors.add("title", new ActionMessage("error.neprieinamasNaikinimui"));
		    	    				request.setAttribute("error.neprieinamasNaikinimui", "");
		    	    				klaida = true;
			        			}
			        			break;
			        		}
		        		}*/
	   	    			if (!yraDuomenys) {
		   	     			if (!asmenysSuNeteisingaisDuomenimis.equals("")) {
		   	     				asmenysSuNeteisingaisDuomenimis = asmenysSuNeteisingaisDuomenimis.concat(", ");
		   	     			}
		   	     			asmenysSuNeteisingaisDuomenimis = asmenysSuNeteisingaisDuomenimis.concat(asmuo.getVardas() + " " + asmuo.getPavarde() + " a.k." + asmuo.getAsmKodas());
		   	     		}
	   	    			if(klaida == true) break; //Reikia rasti tik viena klaida
	   	    			int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();    	    			
	   	    			if (userStatus != UserDelegator.USER_GLOBAL){
	   	    				if(DeklaracijosDelegator.getInstance(request).isPersonResidenceInIstaiga(request,asmuo)){
	   	    					errors.add("title", new ActionMessage("error.neprieinamasNaikinimuiNepriklausoIstaigai"));
	   	    					request.setAttribute("error.neprieinamasNaikinimuiNepriklausoIstaigai", "");
	   	    					/*errors.add("title", new ActionMessage("error.neprieinamasNaikinimui"));
    	    					request.setAttribute("error.neprieinamasNaikinimui", "");*/
	   	    					break;
	   	    				}  
	   	    			}
	   	    		}
	   	    		
	   	    		if(getTipas().equals("2") && nesutampaAdresas){
   	    				errors.add("title", new ActionMessage("error.nesutampaNaikinamasAdresas"));
   	    				request.setAttribute("error.nesutampaNaikinamasAdresas", "");
   	    				klaida = true;
	   	    		}

	   	    		if (asmCount != countGVTWithR) {		
   	    				errors.add("title", new ActionMessage("error.neprieinamasKeitimuiTrukstaDeklaracijos"));
   	    				request.setAttribute("error.neprieinamasKeitimuiTrukstaDeklaracijos", "");
   	    				klaida = true;
   	    			}
	   	    	}catch(ObjectNotFoundException onfe){		
	   	    		onfe.printStackTrace();
	   	    	}		
	   	    } else if ("3".equals(getTipas())) { 
    	    	try{
					boolean haveGvna;
					int totalGvnas = 0;
					for (int i=0; i<getAsmenys().length; i++){
						haveGvna = false;
						long id = Long.parseLong(getAsmenys()[i]);
						Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);						
						
						Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();
						for (Iterator it=gyvenamosiosVietos.iterator(); it.hasNext(); ){
			        		GyvenamojiVieta vt = (GyvenamojiVieta)it.next();			        		
			        		if (null == vt.getGvtDataIki()&& vt.getGvtTipas().equals("K"))
			        		{
			        			haveGvna = true;
			        			break;
			        		}
			        	}
						if (haveGvna) totalGvnas++;
					}
					if (totalGvnas != getAsmenys().length) {
						errors.add("title", new ActionMessage("error.deletingEmtptyGvna"));
						request.setAttribute("error.deletingEmtptyGvna", "");
					}
    	    	}catch(ObjectNotFoundException onfe){	
    	    		onfe.printStackTrace();
    	    	}
			}
	   	    try{
				for (int i=0; i<getAsmenys().length; i++){
					long id = Long.parseLong(getAsmenys()[i]);
					Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);						
					Set gyvVietos = asmuo.getGyvenamosiosVietos();
					GyvenamojiVieta tempVieta = null;
					GyvenamojiVieta vieta = null;
					long nr = 0;
					for (Iterator it = gyvVietos.iterator(); it.hasNext();) {
						tempVieta = (GyvenamojiVieta) it.next();
						if(tempVieta.getGvtNr() > nr && tempVieta.getGvtDataIki() == null ){
							nr = tempVieta.getGvtNr();
							vieta = tempVieta;
						}					
					}
					if (vieta != null && "R".equals(vieta.getGvtTipas())) {
						if (!"".equals(getAddressTer())
								&& getAddressTer().equals(String.valueOf(vieta.getGvtAtvNr()))) {
							UtilDelegator.setError("error.galiojantisAdresas", errors, request);
						}
						if (!"".equals(getAddressAdr())
								&& getAddressAdr().equals(String.valueOf(vieta.getGvtAdvNr()))) {
							UtilDelegator.setError("error.galiojantisAdresas", errors, request);
						}
					}	         
				
					try {
						Date deklData = format.parse(data);
					  	if (tipas.equals("35") ||  UserDelegator.chkPermission(request, "RL_GVDIS_GL_TVARK")){
							deklData = format.parse(naikinimoData);
						}
					
						Date minData = QueryDelegator.getInstance().minValidDeclDate(request, (Asmuo)asmuo);
						
						if (deklData.before(minData)) {
							//UtilDelegator.setError("error.decizionDateLessThanDeclaration", errors, request);
						}
						
						/*if ("2".equals(getTipas())){ //I.N. createSprendimasForm.jsp si klaida uzkomentuota nuo 2009-02-03 v1.30
							boolean haveGv = false;
							Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();
							for (Iterator it=gyvenamosiosVietos.iterator(); it.hasNext(); ){
								GyvenamojiVieta vt = (GyvenamojiVieta)it.next();			        		
								if (null == vt.getGvtDataIki())
									haveGv = true;		        		
							}
							if (false == haveGv) {
								UtilDelegator.setError("error.deletingEmtptyGv", errors, request);
							}
						}*/
					} catch (Exception pe){
						pe.printStackTrace();
					}				
					//sprendimas.getAsmenys().add(asmuo);
				}
			}catch(ObjectNotFoundException onfe){
				onfe.printStackTrace();
			}
		
			// 2012-05-09 Tieto: uþkomentuota, nes 0, 1 ir 2 tipui klaida apimanti ðá tipà gaudoma auksèiau.
	   	    // 3 tipui ði klaida taip pat gaudoma aukðèiau, tik su kitu klaidos praneðimu.
	        /*try {
	    		int countGVTWithK = 0;
	    		
	        	for (int i = 0; i < asmCount; i++) {
	        		long id = Long.parseLong(getAsmenys()[i]);
	        		Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);						
	
	        		Set gyvVietos = asmuo.getGyvenamosiosVietos();
	
	        		for (Iterator it = gyvVietos.iterator(); it.hasNext();) {
	        			GyvenamojiVieta gv = (GyvenamojiVieta) it.next();
	        			if (gv.getGvtTipas().equals("K") && gv.getGvtDataIki() == null) {
	        				countGVTWithK++;
	        				break;
	        			}
	        		}       		
	        	}
	        	
	        	if (countGVTWithK != 0) {
	    			if (asmCount != countGVTWithK) {
	    				errors.add("title", new ActionMessage("error.neprieinamasNaikinimui"));
						request.setAttribute("error.neprieinamasNaikinimui", "");
	    			}
	    		}
	        } catch(Exception ex) {
	        	ex.printStackTrace();
	        }*/
		}
       
		Iterator iter = errors.get();
		while(iter.hasNext()) {
			ActionMessage am = (ActionMessage)iter.next();
			Constants.Println(request, "ERROR:"+am.getKey());
		}   
       
       return errors;      
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		HttpSession session = request.getSession();

		session.removeAttribute("priezastys");

		Long actSprendimasId = (Long)session.getAttribute("actSprendimasId");
		Long actPrasymasId = (Long)session.getAttribute("actPrasymasId");		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		
		
		Set priezastys = new HashSet();
		if (actSprendimasId != null){			
		
			SprendimasKeistiDuomenis sprend = SprendimaiDelegator.getInstance().getSprendimas(request, actSprendimasId.longValue());
			request.getSession().setAttribute("actSprendimas", sprend);
			try {
				prasymas = String.valueOf(sprend.getPrasymas().getId());
			} catch (NullPointerException e) {
			}
			id = String.valueOf(sprend.getId());
			regNr = sprend.getRegNr();
			//issaugome patikrinimui, ar jis buvo keistas
			request.getSession().setAttribute("oldRegNr", regNr);
			
			data = sdf.format(sprend.getData());
			if (sprend.getNaikinimoData() != null && !sprend.getNaikinimoData().equals("")){
				naikinimoData = sdf.format(sprend.getNaikinimoData());
			} else {
				naikinimoData = "";
			}
			tipas = String.valueOf(sprend.getTipas());
			
			//Priklausomai nuo tipo pasirenkame prieþastis
			String priezTipas = "SPRENDIMO_PRIEZASTIS";
			if (sprend.getCalcTipasStr().equals("Naikinti GVNA duomenis")) {
				priezTipas = "SPRENDIMO_PRIEZASTIS_GVNA";
			}
			
			try {
				priezastys = ZinynaiDelegator.getInstance().getZinynoReiksmes(request, priezTipas);
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			}
			
			
			prieme = sprend.getPrieme();
			priezastis = String.valueOf(sprend.getPriezastis().getId());
			pastabos = sprend.getPastabos();
			asmenys = new String[sprend.getGyvenamosiosVietos().size()];
			int i = 0;
			for (Iterator it=sprend.getGyvenamosiosVietos().iterator(); it.hasNext();){
				GyvenamojiVieta gvt = (GyvenamojiVieta)it.next();
				if (i == 0){
					if (!"V".equals(gvt.getGvtTipas())){
						addressType = "0";
						addressAdr = (gvt.getGvtAdvNr() == null) ? "" : gvt.getGvtAdvNr().toString();
						addressTer = (gvt.getGvtAtvNr() == null) ? "" : gvt.getGvtAtvNr().toString();
						gvtKampoNr = (gvt.getGvtKampoNr() == null) ? "" : gvt.getGvtKampoNr().toString();
						try {
							addressString = QueryDelegator.getInstance().getAddressString(request, gvt);
						}
						catch (DatabaseException dbe){
							dbe.printStackTrace();
							addressString = "Nebegaliojantis adresas";
						}
					}
					else {
						addressType = "1";
						valstybe = gvt.getValstybe().getKodas();
						valstybePast = gvt.getGvtAdrUzsenyje();
					}
				}
				sprend.getAsmenys().add(gvt.getAsmuo());
				asmenys[i++] = String.valueOf(gvt.getAsmuo().getAsmNr());
			}
		}
		else if (actPrasymasId != null){
			PrasymasKeistiDuomenis prasymas = SprendimaiDelegator.getInstance().getPrasymas(request, actPrasymasId.longValue());
			
			String sprendPriezastis = "SPRENDIMO_PRIEZASTIS";
			if (prasymas.getTipas() == 3) {
				sprendPriezastis = "SPRENDIMO_PRIEZASTIS_GVNA";
			}
			
			try {
				priezastys = ZinynaiDelegator.getInstance().getZinynoReiksmes(request, sprendPriezastis);
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			}
			
			this.prasymas = String.valueOf(prasymas.getId());
			this.prasymoPavadinimas = prasymas.getRegNr();
			id = "";
			regNr = "";
			data = sdf.format(new Date());
			naikinimoData = data;
			tipas = String.valueOf(prasymas.getTipas());
			if(prasymas.getTipas()==2)
				priezastis = "36";
			else
				priezastis = "";
			prieme = "";
			pastabos = prasymas.getPastabos();

			//Tieto paraiska nr. 7 
			if(prasymas.getTipas()==2){
				if(prasymas.getGvtAdvNr()!=null){
					addressAdrNaikinamas = String.valueOf(prasymas.getGvtAdvNr());
				} else {
					addressAdrNaikinamas =  "";
				}
				
				if(prasymas.getGvtAtvNr() != null){
					addressTerNaikinamas = String.valueOf(prasymas.getGvtAtvNr());
				} else {
					addressTerNaikinamas = "";
				}
				addressStringNaikinamas = prasymas.getNaikinamasAdresas();
			} 

			addressAdr = "";
			addressTer = "";
			gvtKampoNr = "";
			addressString = "";
			valstybe = "";
			valstybePast = "";
			addressType = "0";
			asmenys = new String[prasymas.getAsmenys().size()];
			int i = 0;
			for (Iterator it=prasymas.getAsmenys().iterator(); it.hasNext(); ){
				Asmuo asmuo = (Asmuo)it.next();
				asmenys[i++] = String.valueOf(asmuo.getAsmNr());
			}
			
		}
		else {
			id = "";
			regNr = "";
			data = sdf.format(new Date());
			naikinimoData = sdf.format(new Date());
			tipas = "";
			priezastis = "";
			prieme = "";
			pastabos = "";
			addressAdr = "";
			addressTer = "";
			gvtKampoNr = "";
			addressString = "";
			asmenys = new String[] {};
			valstybe = "";
			valstybePast = "";
			addressType = "0";
			addressAdrNaikinamas =  "";
			addressTerNaikinamas =  "";
			addressStringNaikinamas = "";
		}
		
		session.setAttribute("priezastys", priezastys);
		
	}
	public String getPrasymoPavadinimas() {
		return prasymoPavadinimas;
	}
	public void setPrasymoPavadinimas(String prasymoPavadinimas) {
		this.prasymoPavadinimas = prasymoPavadinimas;
	}
	public String getFixTipas() {
		return fixTipas;
	}
	public void setFixTipas(String fixTipas) {
		this.fixTipas = fixTipas;
	}
}
