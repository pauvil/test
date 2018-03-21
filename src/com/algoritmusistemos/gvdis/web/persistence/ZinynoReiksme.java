package com.algoritmusistemos.gvdis.web.persistence;

public class ZinynoReiksme extends GvdisBase 
{
	private long eilesNr;
	private String kodas;
	private String pavadinimas;
	private String komentaras;	
	private Zinynas zinynas;
	
	public ZinynoReiksme(){}
	
	public void setEilesNr(long eilesNr){this.eilesNr = eilesNr;}
	public long getEilesNr(){return this.eilesNr;}	
	
	public void setKodas(String kodas){this.kodas = kodas;}	
	public String getKodas(){return this.kodas;}

	public void setPavadinimas(String pavadinimas){this.pavadinimas = pavadinimas;}
	public String getPavadinimas(){return this.pavadinimas;}
	
	public void setKomentaras(String komentaras){this.komentaras = komentaras;}
	public String getKomentaras(){return this.komentaras;}

	public Zinynas getZinynas() { return zinynas; }
	public void setZinynas(Zinynas zinynas) { this.zinynas = zinynas; }	
}

