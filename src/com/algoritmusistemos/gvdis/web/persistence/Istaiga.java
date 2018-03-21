package com.algoritmusistemos.gvdis.web.persistence;

import java.util.HashSet;
import java.util.Set;

public class Istaiga extends GvdisBase 
{

	private long tipas;
	private String pavadinimas;
	private String pilnasPavadinimas;
	private String oficialusPavadinimas;
	private String pastabos;
	private String rekvizSpausdinimui;
	private Istaiga istaiga;
	private Set istaigos = new HashSet();
	
	public Istaiga(){}
	
	public void setTipas(long tipas){this.tipas = tipas;}
	public long getTipas(){return this.tipas;}
	
	public void setPavadinimas(String pavadinimas){this.pavadinimas = pavadinimas;}
	public String getPavadinimas(){return this.pavadinimas;}

	public void setPilnasPavadinimas(String pilnasPavadinimas){this.pilnasPavadinimas = pilnasPavadinimas;}
	public String getPilnasPavadinimas(){return this.pilnasPavadinimas;}
	
	public void setRekvizSpausdinimui(String rekvizSpausdinimui){this.rekvizSpausdinimui = rekvizSpausdinimui;}
	public String getRekvizSpausdinimui(){return this.rekvizSpausdinimui;}
	
	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}	

	public String getOficialusPavadinimas() { return oficialusPavadinimas; }
	public void setOficialusPavadinimas(String oficialusPavadinimas) { this.oficialusPavadinimas = oficialusPavadinimas; }

	public String getOficialusPavadinimasRead() { 
		if (oficialusPavadinimas != null && !oficialusPavadinimas.equals("")) {
			return oficialusPavadinimas;
		}
		else {
			return pilnasPavadinimas;
		}
	}
	
	public void setIstaiga(Istaiga istaiga){this.istaiga = istaiga;}
	public Istaiga getIstaiga(){return this.istaiga;}	
	
	public void setIstaigos(Set istaigos){this.istaigos = istaigos;}
	public Set getIstaigos(){return this.istaigos;}		
}

