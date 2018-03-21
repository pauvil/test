package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

public class IsvykimoDeklaracija extends Deklaracija
{
	private Timestamp isvykimoData;
	private String isvykimasAteityje;
	
	public String getIsvykimasAteityje() {
		return isvykimasAteityje;
	}

	public void setIsvykimasAteityje(String isvykimasAteityje) {
		this.isvykimasAteityje = isvykimasAteityje;
	}

	public IsvykimoDeklaracija(){}
	
	public void setIsvykimoData(Timestamp isvykimoData){this.isvykimoData = isvykimoData;}
	public Timestamp getIsvykimoData(){return this.isvykimoData;}
}
