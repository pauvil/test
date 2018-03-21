package lt.solver.gvdis.model;

import java.util.ArrayList;

public class PazymaJournalObj {
	private long id;
	private String regNr;
	private java.sql.Timestamp pazymosData;
	private String asmenvardis;
	private String istaigaStr;
	private String pastabos;
	
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
	public java.sql.Timestamp getPazymosData() {
		return pazymosData;
	}
	public void setPazymosData(java.sql.Timestamp pazymosData) {
		this.pazymosData = pazymosData;
	}
	public String getAsmenvardis() {
		return asmenvardis;
	}
	public void setAsmenvardis(String asmenvardis) {
		this.asmenvardis = asmenvardis;
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
	
}
