package com.algoritmusistemos.gvdis.web.persistence;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.sql.Timestamp;
import java.util.Set;
import com.algoritmusistemos.gvdis.web.persistence.Asmenvardis;

import javax.servlet.http.HttpServletRequest;
import oracle.jdbc.OracleTypes;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;


public class Asmuo extends GvdisBase
{
	private long asmNr; 
	private Date asmGimData;	
	private String asmLytis; 
	private Long asmKodas; 
	private Timestamp asmMirtiesData; 
	
	private String vardas;
	private String pavarde;
	private String pavardePrev;	
	
	private String busena; 
	
	private String asmPilietybe;
	private Set gyvenamosiosVietos;
	private String lastDeklaravimoData; //paskutine gyv vietos reg data is gyvenamosiosVietos
	
	private long eilNr;
	private long avdNr;
	
	public Asmuo(){}
	
	public void setAsmNr(long asmNr){this.asmNr = asmNr;}
	public long getAsmNr(){return this.asmNr;}

	public void setAsmGimData(Date asmGimData){this.asmGimData = new Date(asmGimData.getTime() + 3600000L); }
	public Timestamp getAsmGimData(){		
		return new Timestamp(asmGimData.getTime());
	}	
	public Date gautiGimDate(){return asmGimData;}
	public void setAsmLytis(String asmLytis){this.asmLytis = asmLytis;}
	public String getAsmLytis(){return this.asmLytis;}
	
	public void setAsmKodas(Long asmKodas){this.asmKodas = asmKodas;}
	public Long getAsmKodas(){return this.asmKodas;}

	public Timestamp getAsmMirtiesData() {return asmMirtiesData;}
	public void setAsmMirtiesData(Timestamp asmMirtiesData) {this.asmMirtiesData = asmMirtiesData;}

	public void setVardas(String vardas){this.vardas = vardas;}
	public String getVardas(){return this.vardas;}	
	
	public void setPavarde(String pavarde){this.pavarde = pavarde;}
	public String getPavarde(){return this.pavarde;}
	
	public void setPavardePrev(String pavardePrev){this.pavardePrev = pavardePrev;}
	public String getPavardePrev(){return this.pavardePrev;}

	
	public String getBusena() {	return busena;	}
	public void setBusena(String busena) { this.busena = busena;	}

	public void setAsmPilietybe(String asmPilietybe){this.asmPilietybe = asmPilietybe;}
	public String getAsmPilietybe(){return this.asmPilietybe;}	
	
	public Set getGyvenamosiosVietos() {return gyvenamosiosVietos; }
	public void setGyvenamosiosVietos(Set gyvenamosiosVietos) {this.gyvenamosiosVietos = gyvenamosiosVietos;}
	
	public void setEilNr(long eilNr){this.eilNr = eilNr;}
	public long getEilNr(){return this.eilNr;}	

	public void setAvdNr(long avdNr){this.avdNr = avdNr;}
	public long getAvdNr(){return this.avdNr;}
	
	
	
	public void setVardasPavarde(Asmenvardis asmenvardis){
		if (asmenvardis != null && asmenvardis.getAvdVardas() != null && !asmenvardis.getAvdVardas().equals("")){
			this.setVardas(asmenvardis.getAvdVardas());
			this.setPavarde(asmenvardis.getAvdPavarde());
		}
	}
	
	// pagal data nustato to laiko asmenvardi
	public void setVardasPavarde(HttpServletRequest req, Timestamp data){
		try {
            String qry = "{ ? = call gvdis_akts.getVardasPavarde(?, ?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setLong(2, this.asmNr);		
			stmt.setTimestamp(3, data);		
			
            stmt.execute();
            
            ResultSet rs = (ResultSet)stmt.getObject(1);
            if(rs.next()){
            	if (rs.getString("pavarde") != null ){
            		this.setVardas(rs.getString("vardas"));
            		this.setPavarde(rs.getString("pavarde"));
            	}
            }
            rs.close();
            stmt.close();
		}
		catch (SQLException sqlEx){
			sqlEx.printStackTrace();
		}	
	}

	public String getLastDeklaravimoData() {
		//irasome paskutine gyv vieta.
		Iterator itt = gyvenamosiosVietos.iterator();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(Long.MIN_VALUE));
		Date lastDate = cal.getTime();
		
		while (itt.hasNext()) {
   		 	GyvenamojiVieta gv = (GyvenamojiVieta) itt.next();
   		 	if (gv.getGvtDataIki() == null) {
   		 		lastDeklaravimoData = new SimpleDateFormat("yyyy-MM-dd").format(gv.getGvtDataNuo());
   		 		break;
   		 	} else if (gv.getGvtDataIki().after(lastDate)){
   		 		lastDate = gv.getGvtDataIki();
   		 		lastDeklaravimoData = new SimpleDateFormat("yyyy-MM-dd").format(gv.getGvtDataNuo());
   		 	}

   	 	}
		return lastDeklaravimoData;
	}	

	
	
	/*	
	public void getDirectVardasPavarde(HttpServletRequest req) {
		try {
            String qry = "{ ? = call gvdis_akts.getDirectVardasPavarde(?, ?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setLong(2, getAsmNr());		
			stmt.setLong(3, this.getAvdNr());		
			
            stmt.execute();
            
            ResultSet rs = (ResultSet)stmt.getObject(1);
            if(rs.next()){
            	this.setVardas(rs.getString("vardas"));
            	this.setPavarde(rs.getString("pavarde"));
            }
            rs.close();
            stmt.close();
		}
		catch (SQLException sqlEx){
			sqlEx.printStackTrace();
		}	
	}	
*/	
/*	I.N. 2010.01.25
	public String[] getVardasPavardeByDate(HttpServletRequest req, Date date, String dokNr) throws DatabaseException{		
		String[] rez = new String[2];
		
    	rez[0] = "AlioVardas";
    	rez[1] = "AlioPavarde";

		return rez;
		/*List list = HibernateUtils.currentSession(req).createSQLQuery("SELECT a.avd_vardas, a.avd_pavarde " +
				" from pilietis.asmenys asm, pilietis.asmenvardziai a " +
				" left join GVDISVW_ASMENVARDZIAI b on a.avd_asm_nr = b.avd_asm_nr and a.avd_nr = b.avd_avd_nr" +
				" where a.avd_asm_nr = asm.asm_nr and asm.asm_nr = :asmNr and " +
				" ((b.avd_data_nuo > :date or b.avd_data_nuo is null) and (a.avd_data_nuo <= :date or a.avd_data_nuo is null))")
				.setLong("asmNr", getAsmNr())
				.setTimestamp("date", new Timestamp(date.getTime())).list();
		if(list.size() > 0){
			Object[] arr = (Object[]) list.iterator().next();
			rez[0] = (String)arr[0];
			rez[1] = (String)arr[1];
		}*/
		
		
/*		try {
            String qry = "{ ? = call gvdis_akts.get_asmenvardis_by_dok(?, ?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setLong(2, getAsmNr());		
			stmt.setString(3, dokNr);
			
            stmt.execute();
            
            ResultSet rs = (ResultSet)stmt.getObject(1);
            if(rs.next()){
            	rez[0] = rs.getString("vardas");
            	rez[1] = rs.getString("pavarde");
            }
            rs.close();
            stmt.close();
		}
		catch (SQLException sqlEx){
			throw new DatabaseException(sqlEx);
		}	
		if (rez[0] == null || rez[1] == null) {
			try {
	            String qry = "{ ? = call gvdis_akts.get_asm_asmenvardis_by_date(?, ?) }";
				Connection conn = HibernateUtils.currentSession(req).connection();
	            CallableStatement stmt = conn.prepareCall(qry);
	            stmt.registerOutParameter(1, OracleTypes.CURSOR);
				stmt.setLong(2, getAsmNr());		
				stmt.setTimestamp(3, new Timestamp(date.getTime()));
	            stmt.execute();
	            
	            ResultSet rs = (ResultSet)stmt.getObject(1);
	            if(rs.next()){
	            	rez[0] = rs.getString("vardas");
	            	rez[1] = rs.getString("pavarde");
	            }
	            rs.close();
	            stmt.close();
			}
			catch (SQLException sqlEx){
				throw new DatabaseException(sqlEx);
			}
		}
		return rez;
		*/
//	}
	

/* I.N. 2010.01.25	
	public void updateAvdByDok(HttpServletRequest req) {
		try {
            String qry = "{ ? = call gvdis_akts.get_asmenvardis_by_dok2(?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setLong(2, getAsmNr());		
			
            stmt.execute();
            
            ResultSet rs = (ResultSet)stmt.getObject(1);
            if(rs.next()){
            	this.setVardas(rs.getString("vardas"));
            	this.setPavarde(rs.getString("pavarde"));
            }
            rs.close();
            stmt.close();
		}
		catch (SQLException sqlEx){
			sqlEx.printStackTrace();
		}	
	}
*/	
}
