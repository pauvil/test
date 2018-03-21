package com.algoritmusistemos.gvdis.web.persistence;

import java.io.Serializable;


public class TeritorinisVienetas implements Serializable
{
	private long terNr;
	private Long terTevNr;
	private String terPav;
	private String terTipas;


	public TeritorinisVienetas(){}

	public void setTerNr(long terNr){this.terNr = terNr;}	
	public long getTerNr(){return this.terNr;}
	
	public void setTerTevNr(Long terTevNr){this.terTevNr = terTevNr;}	
	public Long getTerTevNr(){return this.terTevNr;}		

	public void setTerPav(String terPav){this.terPav = terPav;}	
	public String getTerPav(){return this.terPav;}
	
	public void setTerTipas(String terTipas){this.terTipas = terTipas;}	
	public String getTerTipas(){return this.terTipas;}	
	
}
