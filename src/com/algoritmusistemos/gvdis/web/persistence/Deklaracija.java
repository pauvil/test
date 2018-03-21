package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

public class Deklaracija extends GvdisBase 
{
	public static final int TYPE_VALID = 1;
	public static final int TYPE_OLD = 2;
	public static final int TYPE_UNFINISHED = 3;
	public static final int TYPE_NOTVALID = 4;
	public static final int ELECTRONIC_NOTVALID = 5;
	
	private String regNr;
	private String unikalusPastatoNr;
	private long busena;
	private Timestamp gavimoData;	
	private Timestamp deklaravimoData;
	private String dokumentoIsdavejas;
	private String dokumentoRusis = "";	
	private String dokumentoNr = "";	
	private Timestamp dokumentoData;	
	private Timestamp dokumentoGaliojimas;
	private Long pateike;	
	private String pateikeVardas;		
	private String pateikePavarde;
	private Long pageidaujaPazymos;	
	private Long ankstesneVietaTipas;
	private String ankstesneVietaValstybesPastabos;	
	private String ankstesneVietaKita;		
	private String pastabos;	
	private LaikinasAsmuo laikinasAsmuo;
	private Asmuo asmuo;
	private Asmenvardis asmenvardis; 
	private ZinynoReiksme dokumentoTipas;
	private Istaiga istaiga;
	private Valstybe pilietybe;	
	private Valstybe ankstesneGV;
	private GyvenamojiVieta gyvenamojiVieta;
	private String ankstesnePavarde;
	private Long gvIsvDklId;
	private String telefonas;
	private String email;
	
	private Timestamp deklaracijaGalioja;
	
	private Long gvtAsmNrAnkstesne;
	private Long gvtNrAnkstesne;
	
	private Long tmpGvtAdvNr;
	private Long tmpGvtKampoNr;
	private Long tmpGvtAtvNr;
	
	
	private Long saltinis;
	private String procesoid;	
	
	private String atmetimoPriezastys;
	
	private AsmensDokumentas asmensDokumentas;
	
	public Deklaracija(){}
	
	public void setRegNr(String regNr){this.regNr = regNr;}
	public String getRegNr(){return this.regNr;}
	
	public void setBusena(long busena){this.busena = busena;}
	public long getBusena(){return this.busena;}	

	public void setGavimoData(Timestamp gavimoData){this.gavimoData = gavimoData;}
	public Timestamp getGavimoData(){return this.gavimoData;}	
	
	public void setDeklaravimoData(Timestamp deklaravimoData){this.deklaravimoData = deklaravimoData;}
	public Timestamp getDeklaravimoData(){return this.deklaravimoData;}	
	
	public void setDokumentoIsdavejas(String dokumentoIsdavejas){this.dokumentoIsdavejas = dokumentoIsdavejas;}
	public String getDokumentoIsdavejas(){return this.dokumentoIsdavejas;}
	
	public void setDokumentoNr(String dokumentoNr){this.dokumentoNr = dokumentoNr;}
	public String getDokumentoNr(){return this.dokumentoNr;}	
	
	public void setDokumentoRusis(String dokumentoRusis){this.dokumentoRusis = dokumentoRusis;}
	public String getDokumentoRusis(){return this.dokumentoRusis;}	

	public void setDokumentoData(Timestamp dokumentoData){this.dokumentoData = dokumentoData;}
	public Timestamp getDokumentoData(){return this.dokumentoData;}	
	
	public void setDokumentoGaliojimas(Timestamp dokumentoGaliojimas){this.dokumentoGaliojimas = dokumentoGaliojimas;}
	public Timestamp getDokumentoGaliojimas(){return this.dokumentoGaliojimas;}		
	
	public void setPateike(Long pateike){this.pateike = pateike;}
	public Long getPateike(){	
		if (this.pateike==null){
			this.pateike = new Long(0);
		}
			return this.pateike;
	}	
	public void setPateikeVardas(String pateikeVardas){this.pateikeVardas = pateikeVardas;}
	public String getPateikeVardas(){return this.pateikeVardas;}	
	
	public void setPateikePavarde(String pateikePavarde){this.pateikePavarde = pateikePavarde;}
	public String getPateikePavarde(){return this.pateikePavarde;}	
	
	public void setPageidaujaPazymos(Long pageidaujaPazymos){this.pageidaujaPazymos = pageidaujaPazymos;}
	public Long getPageidaujaPazymos(){
		if (this.pageidaujaPazymos==null){
			this.pageidaujaPazymos = new Long(0);
		}
			return this.pageidaujaPazymos;
	}	
	
	public void setAnkstesneVietaTipas(Long ankstesneVietaTipas){this.ankstesneVietaTipas = ankstesneVietaTipas;}
	public Long getAnkstesneVietaTipas(){
		if (this.ankstesneVietaTipas==null){
			this.ankstesneVietaTipas = new Long(0);
		}
			return this.ankstesneVietaTipas;
	}
	
	public void setAnkstesneVietaValstybesPastabos(String ankstesneVietaValstybesPastabos){this.ankstesneVietaValstybesPastabos = ankstesneVietaValstybesPastabos;}
	public String getAnkstesneVietaValstybesPastabos(){return this.ankstesneVietaValstybesPastabos;}	
	
	public void setAnkstesneVietaKita(String ankstesneVietaKita){this.ankstesneVietaKita = ankstesneVietaKita;}
	public String getAnkstesneVietaKita(){return this.ankstesneVietaKita;}	
	
	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}	

	public void setLaikinasAsmuo(LaikinasAsmuo laikinasAsmuo){this.laikinasAsmuo = laikinasAsmuo;}
	public LaikinasAsmuo getLaikinasAsmuo(){return this.laikinasAsmuo;}	
	
	public void setAsmuo(Asmuo asmuo){this.asmuo = asmuo;}
	public Asmuo getAsmuo(){return this.asmuo;}

	public void setAsmenvardis(Asmenvardis asmenvardis){this.asmenvardis = asmenvardis;}
	public Asmenvardis getAsmenvardis(){return this.asmenvardis;}
	
	public void setDokumentoTipas(ZinynoReiksme dokumentoTipas){this.dokumentoTipas = dokumentoTipas;}
	public ZinynoReiksme getDokumentoTipas(){return this.dokumentoTipas;}		
	
	public void setIstaiga(Istaiga istaiga){this.istaiga = istaiga;}
	public Istaiga getIstaiga(){return this.istaiga;}	
	
	public void setPilietybe(Valstybe pilietybe){this.pilietybe = pilietybe;}
	public Valstybe getPilietybe(){return this.pilietybe;}	
	 
	public void setAnkstesneGV(Valstybe ankstesneGV){this.ankstesneGV = ankstesneGV;}
	public Valstybe getAnkstesneGV(){return this.ankstesneGV;}

	public GyvenamojiVieta getGyvenamojiVieta() { return gyvenamojiVieta; }
	public void setGyvenamojiVieta(GyvenamojiVieta gyvenamojiVieta) { this.gyvenamojiVieta = gyvenamojiVieta; }

	public void setGvtAsmNrAnkstesne(Long gvtAsmNrAnkstesne){this.gvtAsmNrAnkstesne = gvtAsmNrAnkstesne;}
	public Long getGvtAsmNrAnkstesne(){return this.gvtAsmNrAnkstesne;}

	public void setGvtNrAnkstesne(Long gvtNrAnkstesne){this.gvtNrAnkstesne = gvtNrAnkstesne;}
	public Long getGvtNrAnkstesne(){return this.gvtNrAnkstesne;}

	public void setTmpGvtAdvNr(Long tmpGvtAdvNr){this.tmpGvtAdvNr = tmpGvtAdvNr;}
	public Long getTmpGvtAdvNr(){
		if (this.tmpGvtAdvNr==null){
			this.tmpGvtAdvNr = new Long(0);
		}
			return this.tmpGvtAdvNr;
	}
	
	public void setTmpGvtKampoNr(Long tmpGvtKampoNr){this.tmpGvtKampoNr = tmpGvtKampoNr;}
	public Long getTmpGvtKampoNr(){return this.tmpGvtKampoNr;}	

	public void setTmpGvtAtvNr(Long tmpGvtAtvNr){this.tmpGvtAtvNr = tmpGvtAtvNr;}
	public Long getTmpGvtAtvNr(){
		if (this.tmpGvtAtvNr== null ){
			this.tmpGvtAtvNr = new Long(0);
		}
			return this.tmpGvtAtvNr;
	}

	public Long getSaltinis() {return saltinis;	}
	public void setSaltinis(Long saltinis) {this.saltinis = saltinis;}

	
	public String getCalcAsmuo()
	{
 		String retVal = "";
 		try {
 				if (getAsmenvardis() != null && getAsmenvardis().getAvdNr() != 0 ){
	 				retVal = asmenvardis.getAvdVardas() + " " + asmenvardis.getAvdPavarde(); 
 				}
 				else if (null != getAsmuo()){
	 				retVal = asmuo.getVardas() + " " + asmuo.getPavarde(); 
	 			}
	 			else if (gyvenamojiVieta != null && gyvenamojiVieta.getAsmuo() != null){
	 				Asmuo asmuo = gyvenamojiVieta.getAsmuo();
	 				retVal = asmuo.getVardas() + " " + asmuo.getPavarde(); 
	 			}
	 			else if (laikinasAsmuo != null){
	 				retVal = laikinasAsmuo.getVardas() + " " + laikinasAsmuo.getPavarde();
	 			}
	 			else {
	 				retVal = "Neþinomas asmuo";
	 			}
 		}
 		catch (Exception e){
 		}
		return retVal;
	}
	
	public int getCalcTipas()
	{
		int retVal = TYPE_UNFINISHED;
		// Jei yra sukurta gyvenamoji vieta ir busena
		// lygi 1 vadinasi deklaracija pabaigta
		if (gyvenamojiVieta != null && busena == 1){
			if (gyvenamojiVieta.getGvtDataIki() == null){
				retVal = TYPE_VALID;
			}
			else {
				retVal = TYPE_OLD;
			}
		}
		// Laikinam asmeniui gyvenamoji vieta nesukuriama
		// taciau deklaracija vistiek gali buti galiojanti
		// (kai busena 1);
		else if (laikinasAsmuo != null && busena == 1)
				retVal = TYPE_NOTVALID;
		else if(2 == busena && (null != saltinis && 1 == saltinis.longValue()))
			retVal = ELECTRONIC_NOTVALID;
		return retVal;
	}
	
	public String getCalcTipasStr()
	{
		int tipas = getCalcTipas();
		switch (tipas){
			case TYPE_OLD: 			  return "Nebegalioja";
			case TYPE_VALID: 		  return "Galioja";
			case TYPE_UNFINISHED:	  return "Nebaigta ávesti";
			case TYPE_NOTVALID:		  return "Negalioja";
			case ELECTRONIC_NOTVALID: return "Atmesta";
			default:				return null;
		}
	}
	

	public String getAnkstesnePavarde() {
		return ankstesnePavarde;
	}

	public void setAnkstesnePavarde(String ankstensnePavarde) {
		this.ankstesnePavarde = ankstensnePavarde;
	}

	public String getProcesoid() {
		return procesoid;
	}

	public void setProcesoid(String procesoid) {
		this.procesoid = procesoid;
	}
	
	public String getTelefonas()
	{
		return telefonas;		
	}
	
	public void setTelefonas(String telefonas)
	{		
		this.telefonas = telefonas;
	}
	
	public String getEmail()
	{
		return email;		
	}
	
	public void setEmail(String email)
	{		
		this.email = email;
	}
	
	public String getAtmetimoPriezastys() {
		return atmetimoPriezastys;
	}

	public void setAtmetimoPriezastys(String atmetimoPriezastys) {
		this.atmetimoPriezastys = atmetimoPriezastys;
	}
	
	public void setAsmensDokumentas(AsmensDokumentas asmensDokumentas){this.asmensDokumentas = asmensDokumentas;}
	public AsmensDokumentas getAsmensDokumentas(){return this.asmensDokumentas;}
	
	public Long getGvIsvDklId() {
		return gvIsvDklId;
	}

	public void setGvIsvDklId(Long gvIsvDklId) {
		this.gvIsvDklId = gvIsvDklId;
	}
	
	public Timestamp getDeklaracijaGalioja() {
		return deklaracijaGalioja;
	}

	public void setDeklaracijaGalioja(Timestamp deklaracijaGalioja) {
		this.deklaracijaGalioja = deklaracijaGalioja;
	}
	
	public String getUnikalusPastatoNr(){return this.unikalusPastatoNr;}
	public void setUnikalusPastatoNr(String unikalusPastatoNr){this.unikalusPastatoNr = unikalusPastatoNr;}

}
