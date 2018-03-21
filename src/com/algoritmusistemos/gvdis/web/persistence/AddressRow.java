package com.algoritmusistemos.gvdis.web.persistence;

public class AddressRow 
{
	private long nr;
	private String pavadinimas;
	private String tipas;
	private String tipNr;
	private Long gvtKampoNr;
	
	public AddressRow()
	{
	}
	
	public AddressRow(long nr, String name, String tipas, String tipNr, Long gvtKampoNr)
	{
		this.nr = nr;
		this.pavadinimas = name;
		this.tipas = tipas;
		this.tipNr = tipNr;
		this.gvtKampoNr = gvtKampoNr;
	}

	public long getNr() { return nr; }
	public void setNr(long nr) { this.nr = nr; }

	public String getPavadinimas() { return pavadinimas; }
	public void setPavadinimas(String pavadinimas) { this.pavadinimas = pavadinimas; }

	public String getTipas() { return tipas; }
	public void setTipas(String tipas) { this.tipas = tipas; }

	public String getTipNr() { return tipNr; }
	public void setTipNr(String tipNr) { this.tipNr = tipNr; }
	
	public Long getGvtKampoNr() { return gvtKampoNr; }
	public void setGvtKampoNr(Long gvtKampoNr) { this.gvtKampoNr = gvtKampoNr; }	
}
