package com.algoritmusistemos.gvdis.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class PrasymasForm extends ActionForm
{
	private String id;
	private String regNr;	
	private String data;	
	private String tipas;
	private String prasytojas;
	private String dokumentas;
	private String adresas;
	private String pastabos;
	private String [] asmenys = new String[]{};
	private String [] prasytojai = new String[]{};
	private String adresoKodas;
    private String addressTer;
    private String addressAdr;
    private String addressString;
    private String gvtKampoNr;

	public String getAdresas() { return adresas; }
	public void setAdresas(String adresas) { this.adresas = UtilDelegator.trim(adresas, 1000); }

	public String getData() { return data; }
	public void setData(String data) { this.data = data; }

	public String getDokumentas() { return dokumentas; }
	public void setDokumentas(String dokumentas) { this.dokumentas = UtilDelegator.trim(dokumentas, 1000); }

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getPastabos() { return pastabos; }
	public void setPastabos(String pastabos) { this.pastabos = UtilDelegator.trim(pastabos, 2000); }

	public String getPrasytojas() { return prasytojas; }
	public void setPrasytojas(String prasytojas) { this.prasytojas = UtilDelegator.trim(prasytojas, 1000); }

	public String getRegNr() { return regNr; }
	public void setRegNr(String regNr) { this.regNr = regNr; }

	public String getTipas() { return tipas; }
	public void setTipas(String tipas) { this.tipas = tipas; }

	public String[] getAsmenys() { return asmenys; }
	public void setAsmenys(String[] asmenys) { this.asmenys = asmenys;}
	
	public String[] getPrasytojai() { return prasytojai; }
	public void setPrasytojai(String[] prasytojai) { this.prasytojai = prasytojai;}
	
	public String getAdresoKodas() { return adresoKodas; }
	public void setAdresoKodas(String adresoKodas) { this.adresoKodas = adresoKodas; }

	public String getAddressAdr() {	return addressAdr; }
	public void setAddressAdr(String addressAdr) { this.addressAdr = addressAdr; }
	
	public String getGvtKampoNr() {	return gvtKampoNr; }
	public void setAGvtKampoNr(String gvtKampoNr) { this.gvtKampoNr = gvtKampoNr; }	

	public String getAddressString() { return addressString; }
	public void setAddressString(String addressString) { this.addressString = addressString; }

	public String getAddressTer() { return addressTer; }
	public void setAddressTer(String addressTer) { this.addressTer = addressTer; }


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		if ("".equals(prasytojas)){
			errors.add("prasytojas", new ActionMessage("err.prasytojas"));
			request.setAttribute("errPrasytojas", "on");
		}
		
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		try {
			format.parse(data);
		}
        catch (ParseException pe){
			errors.add("data", new ActionMessage("err.data"));
        	request.setAttribute("errData", "");
        }
        
        if (asmenys == null || asmenys.length == 0){
			errors.add("asmenys", new ActionMessage("err.asmenys"));
        	request.setAttribute("errAsmenys", "");
        }
 // Naikinti GVNA duomenis tikrinimas, ar visi asmenys turi galiojanti GVNA prasyma
		  try {
			if ("3".equals(getTipas())){
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
				if (totalGvnas != getAsmenys().length) 
					UtilDelegator.setError("error.deletingEmtptyGvna", errors, request);
			}
	
		  }catch (Exception pe){
			  pe.printStackTrace();
		  }	
		  
// Naikinti duomenis tikrinimas, ar nebandoma naikinti savo duomenu
		  try {
			  if ("3".equals(getTipas()) || "2".equals(getTipas())){
				  boolean asmuoSutampa=false;
				  for (int j=0; j<getPrasytojai().length; j++) {				  
					  for (int i=0; i<getAsmenys().length; i++){
						  if (Long.parseLong(getAsmenys()[i])==Long.parseLong(getPrasytojai()[j])) {
							  asmuoSutampa=true;
							  break;
						  }
					  }
					  if (asmuoSutampa) break;
				  }
				  if (asmuoSutampa) {
					  UtilDelegator.setError("error.deletingOwnGvna", errors, request);
				  }
			  }
		}catch (Exception pe){
		  pe.printStackTrace();
		}	
		
		try{
			if ("2".equals(getTipas())){
				if(getAddressString()==null || "".equals(getAddressString())){
					errors.add("addressString", new ActionMessage("errors.emptyAddress"));
					request.setAttribute("badAddressString", "");
				}
			}
		} catch (Exception pe){
			  pe.printStackTrace();
		}
        return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		Long actPrasymasId = (Long)session.getAttribute("actPrasymasId");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		if (actPrasymasId == null){
			id = "";
			regNr = "";
			data = sdf.format(new Date());
			tipas = "";
			prasytojas = "";
			dokumentas = "";
			pastabos = "";
			adresas = "";
			asmenys = new String[] {} ;
		}
		else {
			PrasymasKeistiDuomenis pras = SprendimaiDelegator.getInstance().getPrasymas(request, actPrasymasId.longValue());
			id = String.valueOf(pras.getId());
			regNr = pras.getRegNr();
			data = sdf.format(pras.getData());
			tipas = String.valueOf(pras.getTipas());
			prasytojas = pras.getPrasytojas();
			adresas = pras.getNaujasAdresas();
			dokumentas = pras.getPrasytojoDokumentas();
			pastabos = pras.getPastabos();
			asmenys = new String[pras.getAsmenys().size()];
			addressString = pras.getNaikinamasAdresas();
			
			if(pras.getGvtAdvNr() != null){
				addressAdr = String.valueOf(pras.getGvtAdvNr());	
			} else {
				addressAdr = "";
			}
			if(pras.getGvtAtvNr() != null){
				addressTer = String.valueOf(pras.getGvtAtvNr());	
			} else {
				addressTer = "";
			}
			

			int i = 0;
			for (Iterator it=pras.getAsmenys().iterator(); it.hasNext();){
				Asmuo asm = (Asmuo)it.next();
				asmenys[i++] = String.valueOf(asm.getAsmNr());
			}
		}
	}
}

