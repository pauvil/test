package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;

public abstract class GvdisBase// implements Serializable
{
	private long id;
	private Timestamp insDate;
	private Timestamp updDate;
	private String insVartId;
	private String updVartId;
	
	public void setId(long id){this.id = id;}
	public long getId(){return this.id;}
	
	public void setInsDate(Timestamp insDate){this.insDate = insDate;}	
	public Timestamp getInsDate(){return this.insDate;}

	public void setUpdDate(Timestamp updDate){this.updDate = updDate;}	
	public Timestamp getUpdDate(){return this.updDate;}
	
	public void setInsVartId(String id){this.insVartId = id;}	
	public String getInsVartId(){return this.insVartId;}
	
	public void setUpdVartId(String id){this.updVartId = id;}	
	public String getUpdVartId(){return this.updVartId;}
}
