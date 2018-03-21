package com.algoritmusistemos.gvdis.web.actions.test;

import java.util.Date;

public class TestAsmuo2 {
	private long asmNr; 
	private Date asmGimData;	
	
	private Long asmKodas; 	
	
	public TestAsmuo2(){}
	
	public void setAsmNr(long asmNr){this.asmNr = asmNr;}
	public long getAsmNr(){return this.asmNr;}

	public void setAsmGimData(Date asmGimData){this.asmGimData = asmGimData;}
	public Date getAsmGimData(){		
		return asmGimData;
	}	
		
	//public void setAsmKodas(long asmKodas){this.asmKodas = new Long(asmKodas);}
	public void setAsmKodas(Long asmKodas){this.asmKodas = asmKodas;}
	public Long getAsmKodas(){return this.asmKodas;}	
}
