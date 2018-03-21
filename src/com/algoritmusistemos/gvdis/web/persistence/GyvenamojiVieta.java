package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.LazyInitializationException;
import org.hibernate.ObjectNotFoundException;

public class GyvenamojiVieta implements Serializable 
{
	private long gvtNr; 
	private long gvtAsmNr; 
	private String gvtTipas;	
	private Long gvtVrtKodas;	
	private Timestamp gvtDataNuo;
	private Timestamp gvtDataIki;	
	private Timestamp gvtRegData;
	private Timestamp gvtModData;
	private String gvtPazyma;	
	private String gvtAdrUzsenyje;	
	private String gvtGatKodas;	
	private String gvtNamas;
	private String gvtKorpusas;
	private String gvtButas;
	private Long gvtRejKodasTer;
	private String gvtRejKodoTipas;
	private Long gvtAdvNr;
	private Long gvtAtvNr;
	private Long gvtKampoNr;
	private Timestamp gvtGaliojaIki;
	
	private Asmuo asmuo;
	private Deklaracija deklaracija;
	private Set sprendimai = new HashSet();
	private Valstybe valstybe;
	
	public GyvenamojiVieta(){}
	
	public String getSaltinis()
	{
		String retVal = "";
		if (deklaracija != null){
			if (deklaracija instanceof AtvykimoDeklaracija){
				retVal = "Deklaracija apie atvykim� � LR arba gyvenamosios vietos pakeitim� LR";
			}
			else if (deklaracija instanceof IsvykimoDeklaracija){
				retVal = "Deklaracija apie i�vykim� i� LR";
			}
			else if (deklaracija instanceof GvnaDeklaracija){
				retVal = "Pra�ymas �traukti � GVNA apskait�";
			}
		}
		else {
			retVal = "�ra�as gyventoj� registre";
		}
		
			if (getSprendimas() != null) {
				if (getSprendimas().getTipas() < 2){
					retVal = "Sprendimas d�l deklaravimo duomen� taisymo, keitimo ar naikinimo";
				}
			}
		
		return retVal;
	}
	
	public void setGvtNr(long gvtNr){this.gvtNr = gvtNr;}
	public long getGvtNr(){return this.gvtNr;}

	public long getGvtAsmNr() {	return gvtAsmNr;}
	public void setGvtAsmNr(long gvtAsmNr) {this.gvtAsmNr = gvtAsmNr;}

	public void setGvtTipas(String gvtTipas){this.gvtTipas = gvtTipas;}
	public String getGvtTipas(){return this.gvtTipas;}
	public String getGvtTipasString()
	{
		String retVal = "";
		if ("A".equals(gvtTipas)) retVal = "Atliko bausm�";
		else if ("B".equals(gvtTipas)) retVal = "Be nuolatin�s gyv. vietos";
		else if ("C".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinus leidim� gyventi LR)";
		else if ("D".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinta d�l asmens mirties)";
		else if ("G".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinta pasibaigus leidimo laikinai" +
				" gyventi LR galiojimo laikui)";
		else if ("H".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinta atsisakius i�duoti/pakeisti ledim� gyventi LR)";
		else if ("I".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinta teismo sprendimu)";
		else if ("K".equals(gvtTipas)) retVal = "�trauktas � gyv. vietos nedeklaravusi� asmen� apskait�";
		else if ("M".equals(gvtTipas)) retVal = "Terminuotas �ra�ymas";
		else if ("N".equals(gvtTipas)) retVal = "Buvo nurod�s faktin� gyvenam�j� viet�";
		else if ("O".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (anuliuota kaip klaidinga)";
		else if ("P".equals(gvtTipas)) retVal = "Prognozuojama gyv. vieta Lietuvoje";
		else if ("R".equals(gvtTipas)) retVal = "Deklaravo gyvenam�j� viet�";
		else if ("S".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinta savininko pra�ymu)";
		else if ("T".equals(gvtTipas)) retVal = "Tarnavo kariuomen�je";
		else if ("U".equals(gvtTipas)) retVal = "Nuolat gyvena(-o) u�sienio valstyb�je";
		else if ("V".equals(gvtTipas)) retVal = "I�vyko � u�sienio valstyb�";
		else if ("Z".equals(gvtTipas)) retVal = "Buvo deklarav�s gyv. viet� (panaikinta netekus LR pilietyb�s ir" +
				" nepateikus pra�ymo i�duoti leidim� gyventi LR per nustatyt� termin�)";
		return retVal;
	}
	
	public void setGvtVrtKodas(Long gvtVrtKodas){this.gvtVrtKodas = gvtVrtKodas;}
	public Long getGvtVrtKodas(){return this.gvtVrtKodas;}	

	public void setGvtDataNuo(Timestamp gvtDataNuo){this.gvtDataNuo = gvtDataNuo;}
	public Timestamp getGvtDataNuo(){return this.gvtDataNuo;}	
	
	public void setGvtDataIki(Timestamp gvtDataIki){this.gvtDataIki = gvtDataIki;}
	public Timestamp getGvtDataIki(){return this.gvtDataIki;}	

	public void setGvtRegData(Timestamp gvtRegData){this.gvtRegData = gvtRegData;}
	public Timestamp getGvtRegData(){return this.gvtRegData;}	
	
	public void setGvtModData(Timestamp gvtModData){this.gvtModData = gvtModData;}
	public Timestamp getGvtModData(){return this.gvtModData;}	
	
	public void setGvtPazyma(String gvtPazyma){this.gvtPazyma = gvtPazyma;}
	public String getGvtPazyma(){return this.gvtPazyma;}	
	
	public void setGvtAdrUzsenyje(String gvtAdrUzsenyje){this.gvtAdrUzsenyje = gvtAdrUzsenyje;}
	public String getGvtAdrUzsenyje(){return this.gvtAdrUzsenyje;}	
	
	public void setGvtGatKodas(String gvtGatKodas){this.gvtGatKodas = gvtGatKodas;}
	public String getGvtGatKodas(){return this.gvtGatKodas;}	
	
	public void setGvtNamas(String gvtNamas){this.gvtNamas = gvtNamas;}
	public String getGvtNamas(){return this.gvtNamas;}	
	
	public void setGvtKorpusas(String gvtKorpusas){this.gvtKorpusas = gvtKorpusas;}
	public String getGvtKorpusas(){return this.gvtKorpusas;}	
	
	public void setGvtButas(String gvtButas){this.gvtButas = gvtButas;}
	public String getGvtButas(){return this.gvtButas;}
	
	public void setGvtRejKodasTer(Long gvtRejKodasTer){this.gvtRejKodasTer = gvtRejKodasTer;}
	public Long getGvtRejKodasTer(){return this.gvtRejKodasTer;}
	
	public void setGvtRejKodoTipas(String gvtRejKodoTipas){this.gvtRejKodoTipas = gvtRejKodoTipas;}
	public String getGvtRejKodoTipas(){return this.gvtRejKodoTipas;}

	public Deklaracija getDeklaracija() { return deklaracija; }
	public void setDeklaracija(Deklaracija deklaracija) { this.deklaracija = deklaracija; }

	public Long getGvtAdvNr() {	return gvtAdvNr; }
	public void setGvtAdvNr(Long gvtAdvNr) { this.gvtAdvNr = gvtAdvNr; }

	public Long getGvtAtvNr() { return gvtAtvNr; }
	public void setGvtAtvNr(Long gvtAtvNr) { this.gvtAtvNr = gvtAtvNr; }

	public Long getGvtKampoNr() { return gvtKampoNr; }
	public void setGvtKampoNr(Long gvtKampoNr) { this.gvtKampoNr = gvtKampoNr; }	
	
	public char getTipas()
	{
		if(null == gvtAdvNr && null != gvtAtvNr)return 'T';
		if(null != gvtAdvNr && null == gvtAtvNr)return 'A';
		return ' ';
	}
	
	
	public Set getSprendimai() { return sprendimai; }
	public void setSprendimai(Set sprendimai) { this.sprendimai = sprendimai; }
	public SprendimasKeistiDuomenis getSprendimas() 
	{ 
		try{			
		if (sprendimai.isEmpty()){
			return null;
		}
		else {
			return (SprendimasKeistiDuomenis)sprendimai.iterator().next();
		}
		}catch(ObjectNotFoundException ex){
			return null;
		}
		catch(LazyInitializationException ex){
			return null;
		}
	}

	public Valstybe getValstybe() { return valstybe; }
	public void setValstybe(Valstybe valstybe) { this.valstybe = valstybe; }

	public Asmuo getAsmuo() { return asmuo; }
	public void setAsmuo(Asmuo asmuo) {	this.asmuo = asmuo;	}	
	public void setGvtGaliojaIki(Timestamp gvtGaliojaIki){ this.gvtGaliojaIki = gvtGaliojaIki;}
	public Timestamp getGvtGaliojaIki(){return gvtGaliojaIki;}
}
