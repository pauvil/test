package com.algoritmusistemos.gvdis.web.persistence;


public class AdresinisVienetas 
{
	private long advNr;
	private long advTevNr;
	private long terNr;
	private String advNum;	
	private String advTip;


	public AdresinisVienetas(){}

	public void setAdvNr(long advNr){this.advNr = advNr;}	
	public long getAdvNr(){return this.advNr;}
	
	public void setAdvTevNr(long advTevNr){this.advTevNr = advTevNr;}	
	public long getAdvTevNr(){return this.advTevNr;}		

	public void setTerNr(long terNr){this.terNr = terNr;}	
	public long getTerNr(){return this.terNr;}

	public void setAdvNum(String advNum){this.advNum = advNum;}	
	public String getAdvNum(){return this.advNum;}	
	
	public void setAdvTip(String advTip){this.advTip = advTip;}	
	public String getAdvTip(){return this.advTip;}	
	
}