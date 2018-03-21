package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class SprendimasKeistiDuomenis extends GvdisBase 
{
	private String regNr;
	private Timestamp data;	
	private long tipas;
	private Timestamp naikinimoData;	
	private String prieme;
	private String pastabos;	
	private Istaiga istaiga;
	private ZinynoReiksme priezastis;
	private PrasymasKeistiDuomenis prasymas;
	private Set gyvenamosiosVietos = new HashSet();
	private Set asmenys = new HashSet();
	
	public SprendimasKeistiDuomenis(){}
	
	public void setRegNr(String regNr){this.regNr = regNr;}
	public String getRegNr(){return this.regNr;}

	public void setData(Timestamp data){this.data = data;}
	public Timestamp getData(){return this.data;}
	
	public void setTipas(long tipas){this.tipas = tipas;}
	public long getTipas(){return this.tipas;}	

	public void setNaikinimoData(Timestamp naikinimoData){this.naikinimoData = naikinimoData;}
	public Timestamp getNaikinimoData(){return this.naikinimoData;}

	public void setPrieme(String prieme){this.prieme = prieme;}
	public String getPrieme(){return this.prieme;}	

	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}		

	public void setIstaiga(Istaiga istaiga){this.istaiga = istaiga;}
	public Istaiga getIstaiga(){return this.istaiga;}
	
	public PrasymasKeistiDuomenis getPrasymas() { return prasymas; }
	public void setPrasymas(PrasymasKeistiDuomenis prasymas) { this.prasymas = prasymas; }

	public Set getAsmenys() { return asmenys; }
	public void setAsmenys(Set asmenys) { this.asmenys = asmenys; }

	public Set getGyvenamosiosVietos() { return gyvenamosiosVietos; }
	public void setGyvenamosiosVietos(Set gyvenamosiosVietos) { this.gyvenamosiosVietos = gyvenamosiosVietos; }
	public GyvenamojiVieta getGyvenamojiVieta()
	{
		if (gyvenamosiosVietos.isEmpty()){
			return null;
		}
		else {
			return (GyvenamojiVieta)gyvenamosiosVietos.iterator().next();
		}
	}

	public ZinynoReiksme getPriezastis() { return priezastis; }
	public void setPriezastis(ZinynoReiksme priezastis) { this.priezastis = priezastis; }

	public String getCalcTipasStr()
	{
		switch ((int)tipas){
			case 0: return "Taisyti duomenis";
			case 1: return "Keisti duomenis";
			case 2: return "Naikinti duomenis";
			case 3: return "Naikinti GVNA duomenis";
			default : return null;
		}
	}
}