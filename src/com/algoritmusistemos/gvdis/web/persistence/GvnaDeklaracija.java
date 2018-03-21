package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

public class GvnaDeklaracija extends Deklaracija
{
	private String savivaldybePastabos;
	private String priezastys;
	private TeritorinisVienetas savivaldybe;
	//private Timestamp itrauktiIkiDiena;
	
	
	public GvnaDeklaracija(){}
	
	public void setSavivaldybePastabos(String  savivaldybePastabos){this.savivaldybePastabos = savivaldybePastabos;}
	public String getSavivaldybePastabos(){return this.savivaldybePastabos;}	
	
	public void setPriezastys(String  priezastys){this.priezastys = priezastys;}
	public String getPriezastys(){return this.priezastys;}	
	
	public void setSavivaldybe(TeritorinisVienetas  savivaldybe){this.savivaldybe = savivaldybe;}
	public TeritorinisVienetas getSavivaldybe(){return this.savivaldybe;}
/*
	public Timestamp getItrauktiIkiDiena() {
		return itrauktiIkiDiena;
	}

	public void setItrauktiIkiDiena(Timestamp itrauktiIkiDiena) {
		this.itrauktiIkiDiena = itrauktiIkiDiena;
	}	
	*/
}
