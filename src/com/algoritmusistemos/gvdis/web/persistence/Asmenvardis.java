package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;

//import java.util.Date;
//import java.sql.Timestamp;
//import java.util.Set;
//import javax.servlet.http.HttpServletRequest;
//import oracle.jdbc.OracleTypes;
//import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.CallableStatement;


public class Asmenvardis implements Serializable
{
	private long avdAsmNr; 
	private long avdNr; 
	private String avdVardas;	
	private String avdPavarde;	


	public Asmenvardis(){}

	public Asmenvardis(Asmuo asmuo){
		  if (asmuo != null) {
			this.avdAsmNr = asmuo.getAsmNr(); 
			this.avdNr = asmuo.getAvdNr(); 
			this.avdVardas = asmuo.getVardas();	
			this.avdPavarde = asmuo.getPavarde();
		 }
	};

	public void setAvdAsmNr(long avdAsmNr){this.avdAsmNr = avdAsmNr;}
	public long getAvdAsmNr(){return this.avdAsmNr;}

	public void setAvdNr(long avdNr){this.avdNr = avdNr;}
	public long getAvdNr(){return this.avdNr;}

	public void setAvdVardas(String avdVardas){this.avdVardas = avdVardas;}
	public String getAvdVardas(){return this.avdVardas;}

	public void setAvdPavarde(String avdPavarde){this.avdPavarde = avdPavarde;}
	public String getAvdPavarde(){return this.avdPavarde;}

}
