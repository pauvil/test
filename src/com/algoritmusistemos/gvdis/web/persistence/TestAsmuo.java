package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Date;

public class TestAsmuo {
	private long asmNr; 
	private Date asmGimData;	
	
	private Long asmKodas; 	
	
	public TestAsmuo(){}
	
	public void setAsmNr(long asmNr){this.asmNr = asmNr;}
	public long getAsmNr(){return this.asmNr;}

	public void setAsmGimData(Date asmGimData){this.asmGimData = asmGimData;}
	public Date getAsmGimData(){		
		return asmGimData;
	}	
		
	// Nezinau ar taip gerai!
	//public void setAsmKodas(long asmKodas){this.asmKodas = new Long(asmKodas);}
	public void setAsmKodas(Long asmKodas){this.asmKodas = asmKodas;}
	public Long getAsmKodas(){return this.asmKodas;}	
	
}
