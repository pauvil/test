package com.algoritmusistemos.gvdis.web.persistence;

import java.util.HashSet;
import java.util.Set;

public class Zinynas extends GvdisBase 
{
	private String kodas;
	private String pavadinimas;
	private String komentaras;	
	private Set zinynoReiksmes = new HashSet();
	
	public Zinynas(){}
	
	public void setKodas(String kodas){this.kodas = kodas;}
	public String getKodas(){return this.kodas;}	
	
	public void setPavadinimas(String pavadinimas){this.pavadinimas = pavadinimas;}
	public String getPavadinimas(){return this.pavadinimas;}
	
	public void setKomentaras(String komentaras){this.komentaras = komentaras;}
	public String getKomentaras(){return this.komentaras;}	
	
	public void setZinynoReiksmes(Set zinynoReiksmes){this.zinynoReiksmes = zinynoReiksmes;}
	public Set getZinynoReiksmes(){return this.zinynoReiksmes;}		
	
}
