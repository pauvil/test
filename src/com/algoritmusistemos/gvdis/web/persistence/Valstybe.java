package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;

public class Valstybe implements Serializable
{
	private String kodas;
	private String pavadinimas;
	private String pilietybe;

	public Valstybe(){}

	public void setKodas(String kodas){this.kodas = kodas;}	
	public String getKodas(){return this.kodas;}
	
	public void setPavadinimas(String pavadinimas){this.pavadinimas = pavadinimas;}	
	public String getPavadinimas(){return this.pavadinimas;}		

	public void setPilietybe(String pilietybe){this.pilietybe = pilietybe;}	
	public String getPilietybe(){return this.pilietybe;}
}
