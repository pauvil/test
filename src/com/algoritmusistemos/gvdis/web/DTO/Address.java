package com.algoritmusistemos.gvdis.web.DTO;

import java.io.Serializable;

public class Address implements Serializable
{
	private long id;
	private String adr;
	private String country;
	private char type;
	
	public Address(long id, String adr)
	{
		this.id = id;
		this.adr = adr;	
	}
	
	public Address(long id, String adr, String country)
	{
		this(id, adr);
		this.country = country;	
	}

	public long getId(){ return this.id;}
	public String getCountry(){ return this.country;}

	public String getAdr(){ return this.adr;}
	public void setAdr(String adr){ this.adr = adr;}	

	public char getType() {	return type; }
	public void setType(char type) { this.type = type; }
}

