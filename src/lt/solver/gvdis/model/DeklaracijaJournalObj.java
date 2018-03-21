package lt.solver.gvdis.model;

import java.util.ArrayList;


public class DeklaracijaJournalObj {

	private long id;
	private String regNr;
	private java.sql.Timestamp deklaracijosData;
	private String asmenvardis;
	private String asm_tipas_adr;
	private int busena; //si busena skirasi nuo gvdis_deklaracijos lenteles. 
	private String busenaStr;
	private String istaigaStr;
	private String pastabos;
	private String gyvSalisKodas; //salis, kur asmuo gyvena- kodas
	private String gyvSalisStr; //salis, kur asmuo gyvena
	
	private ArrayList veiksmai;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRegNr() {
		return regNr;
	}
	public void setRegNr(String regNr) {
		this.regNr = regNr;
	}
	public java.sql.Timestamp getDeklaracijosData() {
		return deklaracijosData;
	}
	public void setDeklaracijosData(java.sql.Timestamp deklaracijosData) {
		this.deklaracijosData = deklaracijosData;
	}
	public int getBusena() {
		return busena;
	}
	public void setBusena(int busena) {
		this.busena = busena;
	}
	public String getIstaigaStr() {
		return istaigaStr;
	}
	public void setIstaigaStr(String istaigaStr) {
		this.istaigaStr = istaigaStr;
	}
	public String getAsmenvardis() {
		return asmenvardis;
	}
	public void setAsmenvardis(String asmenvardis) {
		this.asmenvardis = asmenvardis;
	}


	public String getAsm_tipas_adr() {
		return asm_tipas_adr;
	}
	public void setAsm_tipas_adr(String asmTipasAdr) {
		asm_tipas_adr = asmTipasAdr;
	}
	public String getBusenaStr() {
		switch (busena) {
			case 1: busenaStr = "Galioja"; break;
			case 2: busenaStr = "Nebegalioja"; break;
			case 3: busenaStr = "Nebaigta ávesti"; break;
			case 4: busenaStr = "Negalioja"; break;
			case 5: busenaStr = "Atmesta"; break;
			default: busenaStr = "Nebaigta ávesti"; break;
		}
		return busenaStr;
	}
	public ArrayList getVeiksmai() {
		return veiksmai;
	}
	public void setVeiksmai(ArrayList veiksmai) {
		this.veiksmai = veiksmai;
	}
	public String getPastabos() {
		return pastabos;
	}
	public void setPastabos(String pastabos) {
		this.pastabos = pastabos;
	}
	public String getGyvSalisKodas() {
		return gyvSalisKodas;
	}
	public void setGyvSalisKodas(String gyvSalisKodas) {
		this.gyvSalisKodas = gyvSalisKodas;
	}
	public String getGyvSalisStr() {
		return gyvSalisStr;
	}
	public void setGyvSalisStr(String gyvSalisStr) {
		this.gyvSalisStr = gyvSalisStr;
	}

	
}
