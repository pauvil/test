package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

public class GvnaPazyma extends GvdisBase 
{
	private String regNr;
	private Timestamp pazymosData;	
	private long itrauktiVaikus;	
	private String prasymoRegNr;	
	private Timestamp prasymoData;	
	private String pastabos;	
	private Istaiga istaiga;
	private GyvenamojiVieta gyvenamojiVieta;
	private GvnaDeklaracija deklaracija;

	public GvnaPazyma(){}
	
	public void setRegNr(String regNr){this.regNr = regNr;}
	public String getRegNr(){return this.regNr;}

	public void setPazymosData(Timestamp pazymosData){this.pazymosData = pazymosData;}
	public Timestamp getPazymosData(){return this.pazymosData;}
	
	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}	

	public void setItrauktiVaikus(long itrauktiVaikus){this.itrauktiVaikus = itrauktiVaikus;}
	public long getItrauktiVaikus(){return this.itrauktiVaikus;}		

	public void setPrasymoRegNr(String prasymoRegNr){this.prasymoRegNr = prasymoRegNr;}
	public String getPrasymoRegNr(){return this.prasymoRegNr;}	
	
	public void setPrasymoData(Timestamp prasymoData){this.prasymoData = prasymoData;}
	public Timestamp getPrasymoData(){return this.prasymoData;}

	public void setIstaiga(Istaiga istaiga){this.istaiga = istaiga;}
	public Istaiga getIstaiga(){return this.istaiga;}	

	public GyvenamojiVieta getGyvenamojiVieta() { return gyvenamojiVieta; }
	public void setGyvenamojiVieta(GyvenamojiVieta gyvenamojiVieta) { this.gyvenamojiVieta = gyvenamojiVieta; }

	public GvnaDeklaracija getDeklaracija() { return deklaracija; }
	public void setDeklaracija(GvnaDeklaracija deklaracija) { this.deklaracija = deklaracija; }

	public String getCalcAsmuo()
	{
		String retVal = "";
		try {
			if (gyvenamojiVieta != null && gyvenamojiVieta.getAsmuo() != null){
				Asmuo asm = gyvenamojiVieta.getAsmuo();
				retVal = asm.getVardas() + " " + asm.getPavarde();
			}
			else if (deklaracija != null){
				if (deklaracija.getAsmuo() != null){
					retVal = deklaracija.getAsmuo().getVardas() + " " + deklaracija.getAsmuo().getPavarde();
				}
				else if (deklaracija.getLaikinasAsmuo() != null){
					retVal = deklaracija.getLaikinasAsmuo().getVardas() + " " + deklaracija.getLaikinasAsmuo().getPavarde();
				}
			}
		}
		catch (Exception e){
		}
		return retVal;
	}
}