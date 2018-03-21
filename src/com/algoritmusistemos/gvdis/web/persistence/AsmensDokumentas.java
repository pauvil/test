package com.algoritmusistemos.gvdis.web.persistence;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;

public class AsmensDokumentas extends GvdisBase
{
	private long dokNr; 
	private String dokRusis;	
	private String dokRusiesKodas;	
	private String dokNum; 
	private Timestamp dokIsdData; 
	private Timestamp dokNegaliojaNuo; 
	private Timestamp dokGaliojaIki; 
	private String dokBusena;
	private String dokTarnyba;
	private String tarnyba;
	private String dokVlsKodas;
	private Valstybe valstybe;
	private Asmuo asmuo;
	
	public AsmensDokumentas(){}
	
	public Asmuo getAsmuo() { return asmuo; }
	public void setAsmuo(Asmuo asmuo) { this.asmuo = asmuo; }

	public String getDokBusena() { return dokBusena; }
	public void setDokBusena(String dokBusena) { this.dokBusena = dokBusena; }

	public Timestamp getDokIsdData() { return dokIsdData; }
	public void setDokIsdData(Timestamp dokIsdData) { this.dokIsdData = dokIsdData; }
	
	public Timestamp getDokNegaliojaNuo() { return dokNegaliojaNuo; }
	public void setDokNegaliojaNuo(Timestamp dokNegaliojaNuo) { this.dokNegaliojaNuo = dokNegaliojaNuo; }

	public Timestamp getDokGaliojaIki() { return dokGaliojaIki; }
	public void setDokGaliojaIki(Timestamp dokGaliojaIki) {	this.dokGaliojaIki = dokGaliojaIki; }

	public long getDokNr() { return dokNr; }
	public void setDokNr(long dokNr) { this.dokNr = dokNr; }

	public String getDokNum() { return dokNum; }
	public void setDokNum(String dokNum) { this.dokNum = dokNum; }

	public String getDokRusis() { return dokRusis; }
	public void setDokRusis(String dokRusis) { this.dokRusis = dokRusis; }

	public String getDokRusiesKodas() { return dokRusiesKodas; }
	public void setDokRusiesKodas(String dokRusiesKodas) { this.dokRusiesKodas = dokRusiesKodas; }

	public String getDokTarnyba() { return dokTarnyba; }
	public void setDokTarnyba(String dokTarnyba) { this.dokTarnyba = dokTarnyba; }

	public String getTarnyba() { return tarnyba; }
	public void setTarnyba(String tarnyba) { this.tarnyba = tarnyba; }

	public Valstybe getValstybe() { return valstybe; }
	public void setValstybe(Valstybe valstybe) { this.valstybe = valstybe; }
	
	public Valstybe getPilietybe(HttpServletRequest request)
	{
		Valstybe valstybe = getValstybe();
		if (valstybe != null){
			return valstybe;
		}
		else if ("I".equals(getDokRusiesKodas()) || "P".equals(getDokRusiesKodas())){
			return UtilDelegator.getInstance().getValstybe(QueryDelegator.HOME_COUNTRY, request);
		}
		else if (getDokNegaliojaNuo() != null || "N".equals(getDokBusena())){
			return UtilDelegator.getInstance().getValstybe(QueryDelegator.UNKNOWN_COUNTRY, request);
		}
		else {
			return UtilDelegator.getInstance().getValstybe(QueryDelegator.HOME_COUNTRY, request);
		}
	}
	
	public String getTitle()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		String title = getDokRusis() + " Nr. " + getDokNum() + ". Iðduota " 
			+ sdf.format(getDokIsdData()) + " " 
			+ (getDokTarnyba() != null?getDokTarnyba():"") 
			+ (getTarnyba() != null?getTarnyba():""); 
		if (getDokNegaliojaNuo() != null || "N".equals(getDokBusena())){
			title += " (NEBEGALIOJA)";
		}
		else if (getDokGaliojaIki() != null){
			title += " (galioja iki " + sdf.format(getDokGaliojaIki()) + ")";
		}
		return title;
	}
	
	public boolean arGaliojantis(){
		boolean rez = true;
				
		if(this.getDokGaliojaIki() != null){
			Date currentDate = new Date();
			Calendar docCal = Calendar.getInstance();
			docCal.setTime(this.getDokGaliojaIki());
			docCal.add(Calendar.DAY_OF_MONTH,1);
			Date docDate = docCal.getTime();	
			if (currentDate.getTime() > docDate.getTime())
				rez = false;			
		}	
		if("N".equals(this.getDokBusena()))
			rez = false;
		
		return rez;
	}

	public String getDokVlsKodas() {
		return dokVlsKodas;
	}

	public void setDokVlsKodas(String dokVlsKodas) {
		this.dokVlsKodas = dokVlsKodas;
	}
}
