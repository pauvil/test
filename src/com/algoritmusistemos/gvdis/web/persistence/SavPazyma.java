package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;

public class SavPazyma extends GvdisBase {
	private String regNr;
	private Timestamp pazymosData;	
	private String pastabos;	
	private String prasymoRegNr;	
	private Istaiga istaiga;
	private Long gvtAdvNr;
	private Long gvtAtvNr;
	private Long gvtKampoNr;
	private String calcAdresas;
	
	public Long getGvtAdvNr() {
		return gvtAdvNr;
	}
	public void setGvtAdvNr(Long gvtAdvNr) {
		this.gvtAdvNr = gvtAdvNr;
	}
	public Long getGvtKampoNr() {
		return gvtKampoNr;
	}
	public void setGvtKampoNr(Long gvtKampoNr) {
		this.gvtKampoNr = gvtKampoNr;
	}
	public Long getGvtAtvNr() {
		return gvtAtvNr;
	}
	public void setGvtAtvNr(Long gvtAtvNr) {
		this.gvtAtvNr = gvtAtvNr;
	}
	public Istaiga getIstaiga() {
		return istaiga;
	}
	public void setIstaiga(Istaiga istaiga) {
		this.istaiga = istaiga;
	}
	public String getPastabos() {
		return pastabos;
	}
	public void setPastabos(String pastabos) {
		this.pastabos = pastabos;
	}
	public Timestamp getPazymosData() {
		return pazymosData;
	}
	public void setPazymosData(Timestamp pazymosData) {
		this.pazymosData = pazymosData;
	}	
	public String getPrasymoRegNr() {
		return prasymoRegNr;
	}
	public void setPrasymoRegNr(String prasymoRegNr) {
		this.prasymoRegNr = prasymoRegNr;
	}
	public String getRegNr() {
		return regNr;
	}
	public void setRegNr(String regNr) {
		this.regNr = regNr;
	}
	public String getCalcAdresas(){		
		return calcAdresas;		
	}
	public void setCalcAdresas(String calcAdresas) {
		this.calcAdresas = calcAdresas;
	}
	public String getCalcAdresas(HttpServletRequest req) {
		String rez = "";
		GyvenamojiVieta gvt = new GyvenamojiVieta();
		gvt.setGvtAdvNr(getGvtAdvNr());
		gvt.setGvtKampoNr(getGvtKampoNr());
		gvt.setGvtAtvNr(getGvtAtvNr());
		try {
			rez = QueryDelegator.getInstance().getAddressString(req, gvt);
		} catch (DatabaseException e) {					
			e.printStackTrace();
		}
		return rez;
	}
}
