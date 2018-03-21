package com.algoritmusistemos.gvdis.web.DTO;

import java.io.Serializable;

public class AsmeninisDokumentas implements Serializable
{
	private String tipas;
	private String numeris;	
	private String isdavimoData;	
	private String dokIsdave;	
	private String dokBusena;
	private String dokGaliojaIki;	
	public AsmeninisDokumentas
	(
		String tipas,
		String numeris,	
		String isdavimoData,	
		String dokIsdave,	
		String dokBusena,
		String dokGaliojaIki		
	)
	{
		this.tipas = tipas;
		this.numeris = numeris;	
		this.isdavimoData = isdavimoData;	
		this.dokIsdave = dokIsdave;	
		this.dokBusena = dokBusena;
		this.dokGaliojaIki = dokGaliojaIki;
	}
	public String getTipas(){ return this.tipas;}
	public String getNumeris(){ return this.numeris;}	
	public String getIsdavimoData(){ return this.isdavimoData;}	
	public String getDokIsdave(){ return this.dokIsdave;}	
	public String getDokBusena(){ return this.dokBusena;}
	public String getDokGaliojaIki(){ return this.dokGaliojaIki;}	
}
