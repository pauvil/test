package com.algoritmusistemos.gvdis.web.forms;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;


public class GvnaDeklaracijaForm extends DeklaracijaForm
{
	private String savivaldybePastabos;
	private String priezastys;
	private String savivaldybe;
	private String pilietybe;
	private String deklaracijaGaliojaMetai;
	private String deklaracijaGaliojaMenuo;
	private String deklaracijaGaliojaDiena;
	
	public void setSavivaldybePastabos(String savivaldybePastabos){this.savivaldybePastabos = UtilDelegator.trim(savivaldybePastabos, 2000);}
	public String getSavivaldybePastabos(){return this.savivaldybePastabos;}		
	
	public void setPriezastys(String priezastys){this.priezastys = UtilDelegator.trim(priezastys, 2000);}
	public String getPriezastys(){return this.priezastys;}		

	public void setSavivaldybe(String savivaldybe){this.savivaldybe = savivaldybe;}
	public String getSavivaldybe(){return this.savivaldybe;}	
	
	public void setPilietybe(String pilietybe){this.pilietybe = pilietybe;}
	public String getPilietybe(){return this.pilietybe;}	

	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request);
		HttpSession session = request.getSession();
		// Validuojame tik tada, kai redaguojama baigta deklaracija
		if (session.getAttribute("activeDeclaration") != null){
			/*Asmuo asmuo = (Asmuo)session.getAttribute("asmuo");
			if ("".equals(getAsmensDokumentoNumeris())){
				UtilDelegator.setError("error.asmensDokumentoNumeris", errors, request);
			}
		
			if (!QueryDelegator.getInstance().hasDocument(request, asmuo, getAsmensDokumentoNumeris())){
				UtilDelegator.setError("error.asmensDokumentoNumeris", errors, request);
			}
			
			if ("".equals(getAsmensDokumentoIsdave())){
				UtilDelegator.setError("error.asmensDokumentoIsdave", errors, request);
			}*/
			
			if (!"0".equals(getDeklaracijaPateikta())){
				if ("".equals(getPateikejoVardas())){
					UtilDelegator.setError("error.pateikejoVardas", errors, request);
				}
				if ("".equals(getPateikejoPavarde())){
					UtilDelegator.setError("error.pateikejoPavarde", errors, request);
				}
			}
		}
		else{
			try {
				Asmuo asmuo1;		
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
					}					
				}
				if (vieta != null && "K".equals(vieta.getGvtTipas())) {
					if (getSavivaldybe().equals(String.valueOf(vieta.getGvtAtvNr()))) {
						//Isjungta, tam kad galima buti deklaruoti tuoje pacioje savivaldybeje
						//UtilDelegator.setError("error.galiojantisAdresas", errors, request);
					}
					
				}
				} catch (ObjectNotFoundException e) {			
					e.printStackTrace();
				}
				
		}
		if ("save".equals(request.getParameter("mode")))
		{
			if("-1".equals(savivaldybe))UtilDelegator.setError("error.savivaldybeNotSelected", errors, request);
			if ("-1".equals(getPilietybe())) {
				UtilDelegator.setError("error.nenurodytaPilietybe", errors, request);  
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
	    		GvnaDeklaracija d = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(l,request);	    	
	    		setSavivaldybePastabos(d.getSavivaldybePastabos());
	    		setPriezastys(d.getPriezastys());
	    		setSavivaldybe(String.valueOf(d.getSavivaldybe().getTerNr()));
	    	}
	    	catch (ObjectNotFoundException onfe){
	    		onfe.printStackTrace();
	    	}		
	    }
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