package lt.solver.gvdis.model;

import java.util.ArrayList;

public class SprendimaiJournalObj {
	private long id;
	private String regNr;
	private java.sql.Timestamp sprendimoData;
	private String tipas;
	private String istaigaStr;
	private String pastabos;
	private java.sql.Timestamp insData;
	private String prasytojas;
	
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
	public java.sql.Timestamp getSprendimoData() {
		return sprendimoData;
	}
	public void setSprendimoData(java.sql.Timestamp sprendimoData) {
		this.sprendimoData = sprendimoData;
	}
	public String getTipas() {
		return tipas;
	}
	public void setTipas(String tipas) {
		this.tipas = tipas;
	}
	public String getIstaigaStr() {
		return istaigaStr;
	}
	public void setIstaigaStr(String istaigaStr) {
		this.istaigaStr = istaigaStr;
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
	public java.sql.Timestamp getInsData() {
		return insData;
	}
	public void setInsData(java.sql.Timestamp insData) {
		this.insData = insData;
	}
	public String getPrasytojas() {
		return prasytojas;
	}
	public void setPrasytojas(String prasytojas) {
		this.prasytojas = prasytojas;
	}
	
}
