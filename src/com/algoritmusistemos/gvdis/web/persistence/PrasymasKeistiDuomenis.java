package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


public class PrasymasKeistiDuomenis extends GvdisBase 
{
	private String regNr;
	private Timestamp data;	
	private long tipas;
	private String prasytojas;
	private long busena;	
	private String prasytojoDokumentas;	
	private String naujasAdresas;
	private String pastabos;
	private SprendimasKeistiDuomenis sprendimas;
	private Set asmenys = new HashSet();
	private Istaiga istaiga;
	private boolean pasenes;
	private String naikinamasAdresas;
	private Long gvtAdvNr;
	private Long gvtAtvNr;
	
	public PrasymasKeistiDuomenis(){}
	
	public void setRegNr(String regNr){this.regNr = regNr;}
	public String getRegNr(){return this.regNr;}

	public void setData(Timestamp data){this.data = data;}
	public Timestamp getData(){return this.data;}
	
	public void setTipas(long tipas){this.tipas = tipas;}
	public long getTipas(){return this.tipas;}	
	public String getTipasStr()
	{
		switch ((int)getTipas()){
			case 0 : return "Taisyti duomenis";
			case 1 : return "Keisti duomenis";
			case 2 : return "Naikinti duomenis";
			case 3 : return "Naikinti GVNA duomenis";
		}
		return null;
	}

	public void setPrasytojas(String prasytojas){this.prasytojas = prasytojas;}
	public String getPrasytojas(){return this.prasytojas;}

	public void setBusena(long busena){this.busena = busena;}
	public long getBusena(){return this.busena;}	
	public String getBusenaStr()
	{
		switch ((int)getBusena()){
			case 0 : return "Naujas praðymas";
			case 1 : return "Pagal praðymà priimtas sprendimas";
			case 2 : return "Atmestas praðymas";
		}
		return null;
	}
	
	public void setPrasytojoDokumentas(String prasytojoDokumentas){this.prasytojoDokumentas = prasytojoDokumentas;}
	public String getPrasytojoDokumentas(){return this.prasytojoDokumentas;}

	public void setNaujasAdresas(String naujasAdresas){this.naujasAdresas = naujasAdresas;}
	public String getNaujasAdresas(){return this.naujasAdresas;}	

	public void setPastabos(String pastabos){this.pastabos = pastabos;}
	public String getPastabos(){return this.pastabos;}

	public SprendimasKeistiDuomenis getSprendimas() { return sprendimas; }
	public void setSprendimas(SprendimasKeistiDuomenis sprendimas) { this.sprendimas = sprendimas; }

	public Set getAsmenys() { return asmenys; }
	public void setAsmenys(Set asmenys) { this.asmenys = asmenys; }

	public void setIstaiga(Istaiga istaiga){this.istaiga = istaiga;}
	public Istaiga getIstaiga(){return this.istaiga;}

	public boolean isPasenes() { return pasenes; }
	public void setPasenes(boolean pasenes) { this.pasenes = pasenes; }
	
	public String getNaikinamasAdresas() { return naikinamasAdresas;}
	public void setNaikinamasAdresas(String naikinamasAdresas) { this.naikinamasAdresas = naikinamasAdresas;}

	public Long getGvtAdvNr() {	return this.gvtAdvNr;}
	public void setGvtAdvNr(Long gvtAdvNr) {this.gvtAdvNr = gvtAdvNr;}

	public Long getGvtAtvNr() {	return this.gvtAtvNr;}
	public void setGvtAtvNr(Long gvtAtvNr) {this.gvtAtvNr = gvtAtvNr;}
}