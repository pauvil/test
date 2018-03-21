package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;


public class LaikinasAsmuo extends GvdisBase 
{
	private String lytis;
	private String vardas;
	private String pavarde;	
	//private String kitiVardai;
	private String asmensKodas;
	private String pastabos;	
	private Timestamp gimimoData;
	private Valstybe pilietybe;
	private Deklaracija deklaracija;
	
	public LaikinasAsmuo(){}
	
	public void setLytis(String lytis){this.lytis = lytis;}
	public String getLytis(){return this.lytis;}	
	
	public void setVardas(String vardas){this.vardas = vardas;}
	public String getVardas(){return this.vardas;}
	
	public void setPavarde(String pavarde){this.pavarde = pavarde;}
	public String getPavarde(){return this.pavarde;}	
	
//	public void setKitiVardai(String kitiVardai){this.kitiVardai = kitiVardai;}
//	public String getKitiVardai(){return this.kitiVardai;}	
	
	public void setAsmensKodas(String asmensKodas){this.asmensKodas = asmensKodas;}
	public String getAsmensKodas(){return this.asmensKodas;}	
	
	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}	
	
	public void setGimimoData(Timestamp gimimoData){this.gimimoData = gimimoData;}
	public Timestamp getGimimoData(){return this.gimimoData;}	

	public void setPilietybe(Valstybe pilietybe){this.pilietybe = pilietybe;}	
	public Valstybe getPilietybe(){return this.pilietybe;}	
	
	public void setDeklaracija(Deklaracija deklaracija){this.deklaracija = deklaracija;}	
	public Deklaracija getDeklaracija(){return this.deklaracija;}	
}
