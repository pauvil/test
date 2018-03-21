package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

public class AtvykimoDeklaracija extends Deklaracija
{
	private Timestamp atvykimoData;
	private long rysysSuGv;
	private String rysysSuGvKita;
	private long savininkoTipas;
	private long savininkoIgaliotinis;
	private String jaPavadinimas;
	private String jaKodas;
	private String jaBuveine;
	private String faVardasPavarde;
	private String faKodas;
	private String faAdresas;
	private Timestamp savininkoParasoData;
	
	private Timestamp deklaracijaGalioja;
	
	private String unikalusPastatoNr; 
	private String savininkas1;
	private String savininkasKodas1;
	private String savininkas2;
	private String savininkasKodas2;
	private String savininkas3;
	private String savininkasKodas3;
	private String savininkas4;
	private String savininkasKodas4;
	
	
	public AtvykimoDeklaracija(){}
	
	public void setAtvykimoData(Timestamp atvykimoData){this.atvykimoData = atvykimoData;}
	public Timestamp getAtvykimoData(){return this.atvykimoData;}	
	
	public void setRysysSuGv(long rysysSuGv){this.rysysSuGv = rysysSuGv;}
	public long getRysysSuGv(){return this.rysysSuGv;}
	
	public void setRysysSuGvKita(String rysysSuGvKita){this.rysysSuGvKita = rysysSuGvKita;}
	public String getRysysSuGvKita(){return this.rysysSuGvKita;}	
	
	public void setSavininkoTipas(long savininkoTipas){this.savininkoTipas = savininkoTipas;}
	public long getSavininkoTipas(){return this.savininkoTipas;}	
	
	public void setSavininkoIgaliotinis(long savininkoIgaliotinis){this.savininkoIgaliotinis = savininkoIgaliotinis;}
	public long getSavininkoIgaliotinis(){return this.savininkoIgaliotinis;}	
	
	
	
	public void setJaPavadinimas(String  jaPavadinimas){this.jaPavadinimas = jaPavadinimas;}
	public String getJaPavadinimas(){return this.jaPavadinimas;}	
	
	public void setJaKodas(String  jaKodas){this.jaKodas = jaKodas;}
	public String getJaKodas(){return this.jaKodas;}	
	
	public void setJaBuveine(String  jaBuveine){this.jaBuveine = jaBuveine;}
	public String getJaBuveine(){return this.jaBuveine;}	
	
	public void setFaVardasPavarde(String  faVardasPavarde){this.faVardasPavarde = faVardasPavarde;}
	public String getFaVardasPavarde(){return this.faVardasPavarde;}	
	
	public void setFaKodas(String  faKodas){this.faKodas = faKodas;}
	public String getFaKodas(){return this.faKodas;}	
	
	public void setFaAdresas(String faAdresas){this.faAdresas = faAdresas;}
	public String getFaAdresas(){return this.faAdresas;}	
	
	public void setSavininkoParasoData(Timestamp savininkoParasoData){this.savininkoParasoData = savininkoParasoData;}
	public Timestamp getSavininkoParasoData(){return this.savininkoParasoData;}

	public String getUnikalusPastatoNr() {
		return unikalusPastatoNr;
	}

	public void setUnikalusPastatoNr(String unikalusPastatoNr) {
		this.unikalusPastatoNr = unikalusPastatoNr;
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
/*
	public Timestamp getDeklaracijaGalioja() {
		return deklaracijaGalioja;
	}

	public void setDeklaracijaGalioja(Timestamp deklaracijaGalioja) {
		this.deklaracijaGalioja = deklaracijaGalioja;
	}
*/
}
