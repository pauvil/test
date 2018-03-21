package com.algoritmusistemos.gvdis.web.delegators;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleTypes;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AddressRow;
import com.algoritmusistemos.gvdis.web.persistence.AsmensDokumentas;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvdisBase;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.TeritorinisVienetas; //ju.k 2007.08.24
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.utils.Ordering;

public class QueryDelegator
{
	public static final String HOME_COUNTRY = "LTU";
	public static final String UNKNOWN_COUNTRY = "XXX";
	
	private static QueryDelegator instance;
	
	private static Map addressNames = new HashMap();
	
	static {
		addressNames.put("V", "Valstybë");
		addressNames.put("RSM", "Miestas");
		addressNames.put("R", "Rajono savivaldybë");
		addressNames.put("RAM", "Rajoninio pavaldumo miestas");
		addressNames.put("MTG", "Miestelis");
		addressNames.put("A", "Seniûnija");
		addressNames.put("K", "Kaimas");
		addressNames.put("G", "Gatvë");
		addressNames.put("Z", "Inventorizacinë zona");
		addressNames.put("APS", "Apskritis");
		addressNames.put("MS", "Miesto savivaldybë");
		addressNames.put("NVT", "Valstybë");
		addressNames.put("N", "Namo nr.");
		addressNames.put("B", "Buto nr.");
		addressNames.put("VL", "Namø valda");
		addressNames.put("KR", "Korpuso nr.");
		addressNames.put("MD", "Miesto dalis");
		addressNames.put("MSE", "Miesto seniûnija");
		addressNames.put("UV", "Uþsienio CMS");
	}
	
	public static QueryDelegator getInstance() 
	{
		if (instance == null){
			instance =  new QueryDelegator();
		}
		return instance;
	}	

	/**
	 * Graþina asmená pagal asmens kodà
	 * @param code - asmens kodas
	 * @throws ObjectNotFoundException - jei tokio asmens nëra
	 * @throws DatabaseException 
	 */
	public Asmuo getAsmuoByCode(HttpServletRequest req, String code)
		throws ObjectNotFoundException
	{
/*		
		Asmuo asmuo = (Asmuo)HibernateUtils.currentSession(req).createQuery(
			"from Asmuo asm where asm.asmKodas = :kodas")
			.setString("kodas", code)
			.uniqueResult();
		if (asmuo == null){
			throw new ObjectNotFoundException("Asmuo su duotu kodu nerastas");
		}
		asmuo.setBusena(getAsmBusena(req, asmuo.getAsmNr()));
		//asmuo.updateAvdByDok(req);
		return asmuo;
*/

		
		// paieska per asmens koda letai veikia viewe GVDISVW_ASMENYS_VARDAI
        String qry = "{ ? = call gvdis_akts.Get_Asm_nr(?) }";
        try{
	        Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.registerOutParameter(1, OracleTypes.NUMBER);
	      	stmt.setString(2, code);
	        stmt.execute();
	        
	        long asmNr = stmt.getLong(1);
	        if (asmNr == 0 ) throw new ObjectNotFoundException("Asmuo su duotu kodu nerastas");
			stmt.close();
			return getAsmuoByAsmNr(req, asmNr);
        } catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
			throw new ObjectNotFoundException(sqlEx);
        }
        
	}
	
	/**
	 * Graþina asmená pagal duotà vidiná asmens numerá
	 * @param asmNr - vidinis asmens numeris
	 * @throws ObjectNotFoundException - jei tokio asmens nëra
	 * @throws DatabaseException 
	 */
	public Asmuo getAsmuoByAsmNr(HttpServletRequest req, long asmNr)
		throws ObjectNotFoundException
	{
		Asmuo asmuo = (Asmuo)HibernateUtils.currentSession(req).createQuery(
			"from Asmuo asm where asm.asmNr = :asmNr")
			.setLong("asmNr", asmNr)
			.uniqueResult();
		if (asmuo == null){
			throw new ObjectNotFoundException("Asmuo su duotu kodu nerastas");
		}
		asmuo.setBusena(getAsmBusena(req, asmuo.getAsmNr()));
		//asmuo.updateAvdByDok(req);
		
		return asmuo;
	}
	
	/**
	 * Graþina gyvenamàjà vietà pagal vidiná numerá
	 * @param gvtNr - vidinis gyvenamosios vietos numeris
	 * @throws ObjectNotFoundException - jei tokio asmens nëra
	 */
	public GyvenamojiVieta getGyvenamojiVieta(HttpServletRequest req, long gvtAsmNr, long gvtNr)
		throws ObjectNotFoundException
	{
		return getGyvenamojiVieta(HibernateUtils.currentSession(req), gvtAsmNr, gvtNr);		
	}
	/**
	 * Graï¿½ina gyvenamï¿½jï¿½ vietï¿½ pagal vidinï¿½ numerï¿½
	 * @param gvtNr - vidinis gyvenamosios vietos numeris
	 * @throws ObjectNotFoundException - jei tokio asmens nï¿½ra
	 */
	public GyvenamojiVieta getGyvenamojiVieta(Session session, long gvtAsmNr, long gvtNr)
		throws ObjectNotFoundException
	{
		GyvenamojiVieta gvt = (GyvenamojiVieta)session.createQuery(
			"from GyvenamojiVieta gvt where gvt.gvtNr = :gvtNr and gvt.gvtAsmNr = :gvtAsmNr")
			.setLong("gvtNr", gvtNr)
			.setLong("gvtAsmNr", gvtAsmNr)
			.uniqueResult();
		if (gvt == null){
			throw new ObjectNotFoundException("Gyvenamosios vietos su tokiu numeriu nëra");
		}
		return gvt;
	}
	
	public List getGyvenamosiosVietos(HttpServletRequest req, long gvtAsmNr)	
	{
	return  HibernateUtils.currentSession(req).createQuery(
		"from GyvenamojiVieta gvt where gvt.gvtAsmNr = :gvtAsmNr order by gvt.gvtDataNuo desc")		
		.setLong("gvtAsmNr", gvtAsmNr)
		.list();	
	}


	
	/**
	 * Graþina asmens busena
	 * @throws ObjectNotFoundException - jei tokio asmens nëra
	 */
	public String getAsmBusena(HttpServletRequest request, long asmid)	
	{
		String retVal = "Gyvenamoji vieta nedeklaruota";
		//HibernateUtils.currentSession(request).setFlushMode(FlushMode.COMMIT);
	   
		GyvenamojiVieta gvt = (GyvenamojiVieta)HibernateUtils.currentSession(request).createQuery(
		"from GyvenamojiVieta gvt where gvt.gvtAsmNr = :gvtAsmNr and gvt.gvtDataIki is null order by gvt.gvtDataNuo desc")
		.setLong("gvtAsmNr", asmid)
		.setMaxResults(1)
		.uniqueResult();
		//HibernateUtils.currentSession(request).setFlushMode(FlushMode.AUTO);
		if ((null != gvt) && (false == ("".equals(gvt.getGvtTipasString())))) retVal = gvt.getGvtTipasString();
	   
		return retVal;
	}
	
	public String getAsmGvtAdresa(HttpServletRequest request, long asmid)	
	{
		String retVal = "";			   
		GyvenamojiVieta gvt = (GyvenamojiVieta)HibernateUtils.currentSession(request).createQuery(
		"from GyvenamojiVieta gvt where gvt.gvtAsmNr = :gvtAsmNr and gvt.gvtDataIki is null order by gvt.gvtDataNuo desc")
		.setLong("gvtAsmNr", asmid)
		.setMaxResults(1)
		.uniqueResult();		
		try {
			if (null != gvt) retVal = getAddressString(request, gvt);
		} catch (DatabaseException e) {}		
		return retVal;
	}    
	/**
	 * Graþina vidiná numerá paskutinës asmens gyvenamosios vietos
	 * @throws ObjectNotFoundException - jei tokio asmens nëra
	 */
	public long getAsmGyvenamojiVieta(HttpServletRequest req, long gvtAsmNr)
		throws ObjectNotFoundException
	{
		GyvenamojiVieta gvt = (GyvenamojiVieta)HibernateUtils.currentSession(req).createQuery(
			"from GyvenamojiVieta gvt where gvt.gvtAsmNr = :gvtAsmNr order by nvl(gvt.gvtDataIki, '9999.01.01') desc")
			.setLong("gvtAsmNr", gvtAsmNr)
			.setMaxResults(1)
			.uniqueResult();
		if (gvt == null){
			throw new ObjectNotFoundException("Gyvenamosios vietos toks asmuo neturi");
		}
		long GvtNr = gvt.getGvtNr();
		return GvtNr;
	}
	
	/**
	 * Graþina adresà, iðskaidytà komponentais 
	 * @param gvt - gyvenamoji vieta, turinti ðá adresà
	 * @throws DatabaseException - jei nepavyko iðskaidyti ðio adreso
	 */
	public Map getFlatAddress(HttpServletRequest req, GyvenamojiVieta gvt)
		throws DatabaseException
	{
		Map retVal = new LinkedHashMap();
        if (gvt != null) try {
	            String qry = "{ ? = call gvdis_akts.get_flat_address(?, ?, ?) }";
	            Connection conn = HibernateUtils.currentSession(req).connection();
	            CallableStatement stmt = conn.prepareCall(qry);
	            stmt.registerOutParameter(1, OracleTypes.CURSOR);
	            stmt.setLong(2, gvt.getGvtAtvNr() == null ? 0 : gvt.getGvtAtvNr().longValue());
	            stmt.setLong(3, gvt.getGvtAdvNr() == null ? 0 : gvt.getGvtAdvNr().longValue());
	            if (gvt.getGvtKampoNr() == null)
	            	stmt.setNull(4, OracleTypes.NUMBER);
	            else {
	            	stmt.setLong(4, gvt.getGvtKampoNr().longValue());
	            }
	            stmt.execute();
	            ResultSet rs = (ResultSet)stmt.getObject(1);
	            while(rs.next()){
	            	String typeStr = (String)addressNames.get(rs.getString("TIP_TIPAS"));
	            	if (typeStr == null){
	            		typeStr = rs.getString("TIPAS");
	            	}
	            	retVal.put(typeStr, rs.getString("PAVADINIMAS"));
	            }
	            rs.close();
	            stmt.close();        		
        	
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
        return retVal;
	}
	
	/**
	 * Graþina adreso tekstinæ reprezentacijà - visà adresà vienoje eilutëje. 
	 * @param gvt - gyvenamoji vieta, turinti ðá adresà
	 * @throws DatabaseException - jei nepavyko iðskaidyti ðio adreso
	 */
	public String getAddressString(HttpServletRequest req, GyvenamojiVieta gvt)
		throws DatabaseException
	{
		String retVal = null;
		if (gvt.getValstybe() != null && !gvt.getValstybe().getKodas().equals(HOME_COUNTRY)){
			retVal = gvt.getValstybe().getPavadinimas();
			if (gvt.getGvtAdrUzsenyje() != null){
				retVal += " (" + gvt.getGvtAdrUzsenyje() + ")"; 
			}
		}
		else try {
	            String qry = "{ ? = call gvdis_akts.get_string_address(?, ?, ?) }";
	            Connection conn = HibernateUtils.currentSession(req).connection();
	            CallableStatement stmt = conn.prepareCall(qry);
	            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
	            if (gvt.getGvtAtvNr() == null){
	            	stmt.setNull(2, OracleTypes.NUMBER);
	            }
	            else {
	            	stmt.setLong(2, gvt.getGvtAtvNr().longValue());
	            }
	            if (gvt.getGvtAdvNr() == null){
	            	stmt.setNull(3, OracleTypes.NUMBER);
	            	stmt.setNull(4, OracleTypes.NUMBER);
	            }
	            else {
	            	stmt.setLong(3, gvt.getGvtAdvNr().longValue());
	            	req.getSession().setAttribute("gvtKampoNr", gvt.getGvtKampoNr());
	            	if (gvt.getGvtKampoNr() == null) stmt.setNull(4, OracleTypes.NUMBER);
	            	else stmt.setLong(4, gvt.getGvtKampoNr().longValue());
	            }
	            stmt.execute();
	            retVal = (String)stmt.getString(1);
	            stmt.close();        		
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
        return retVal;
	}
	
	/**
	 * Graþina visus duoto tëvinio adreso sub-adresus, t.y. rajonams - kaimus, miestams - gatves ir pan.
	 * @param paramType - tëvinio tipas. 'A' - adresuojamas vienetas, 'T' - teritorinis vienetas
	 * @param currentAdr - tëvinio adreso ID
	 * @throws DatabaseException - jei ávyko klaida duomenø bazëje
	 */
	public List getSubAddresses(HttpServletRequest req, String paramType, long currentAdr //,Long istId
			)
		throws DatabaseException
	{
		ArrayList retVal = new ArrayList();
        try {
            String qry = "{ ? = call gvdis_akts.get_sub_addresses(?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setLong(2, currentAdr);
            stmt.setString(3, paramType);
            //ju.k 2007.08.07
            //if (istId != null && istId.longValue() > 0){
            	//stmt.setLong(4, istId.longValue());
            //}
            //else {
            	//stmt.setNull(4, OracleTypes.NUMBER);
            //}
            //
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
            	AddressRow row = new AddressRow();
            	row.setNr(rs.getLong("NR"));
            	row.setPavadinimas(rs.getString("PAVADINIMAS"));
            	row.setTipas(rs.getString("TIPAS"));
            	row.setTipNr(rs.getString("TIP_NR"));
            	if (row.getTipas().equals("Namas")) {
            		if (rs.getString("KAMPO_NR") != null)
            			row.setGvtKampoNr(Long.valueOf(rs.getString("KAMPO_NR")));          		
            	}
            	retVal.add(row);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
        return retVal;
	}
	
	
	/** ju.k 2007.08.28
	 * Graþina visus duoto tëvinio adreso teritorijas is gvdis_ist_ter
	 * @throws DatabaseException - jei ávyko klaida duomenø bazëje
	 */
	public List getSubAddr_ist(HttpServletRequest req, Long istId)
		throws DatabaseException
	{
		ArrayList retVal = new ArrayList();
        try {
            String qry = "{ ? = call gvdis_akts.get_sub_addr_ist(?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            //ju.k 2007.08.07
            if (istId != null && istId.longValue() > 0){
            	stmt.setLong(2, istId.longValue());
            }
            else {
            	stmt.setNull(2, OracleTypes.NUMBER);
            }
            //
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
            	AddressRow row = new AddressRow();
            	row.setNr(rs.getLong("NR"));
            	row.setPavadinimas(rs.getString("PAVADINIMAS"));
            	retVal.add(row);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
        return retVal;
	}
	
	
	/** ju.k 2007.08.24
	 * Graþina savivaldybes sarasa pagal istaiga 
	 * @throws DatabaseException - jei ávyko klaida duomenø bazëje
	 */
	public List getSav(HttpServletRequest req, Long istId)
		throws DatabaseException
	{
		ArrayList retVal = new ArrayList();
        try {
            String qry = "{ ? = call gvdis_akts.get_sav_pgl_ist(?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            if (istId != null && istId.longValue() > 0
            		){
            	stmt.setLong(2, istId.longValue());
            }
            else {
            	stmt.setNull(2, OracleTypes.NUMBER);
            }
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
            	TeritorinisVienetas row = new TeritorinisVienetas();
            	row.setTerNr(rs.getLong("TER_NR"));
            	row.setTerTevNr(new Long(rs.getLong("TER_TEV_NR")));
            	row.setTerPav(rs.getString("TER_PAV"));
            	row.setTerTipas(rs.getString("TER_TIPAS"));
            	retVal.add(row);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
        return retVal;
	}
	
	public Long getIstaigaBySavivaldybe (HttpServletRequest req, Long savivaldybeId) throws DatabaseException {
		Long retVal = null;
        try {
        	String qry = "{ ? = call gvdis_akts.get_ist_pgl_sav(?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			stmt.setLong(2, savivaldybeId.longValue());
			stmt.execute();
			retVal = new Long(stmt.getLong(1));
			stmt.close();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return retVal;
	}
	
	/**
	 * Graþina visus asmenis, gyvenanèius duotame adrese 
	 * @param adrNr - adreso adresinis vienetas 
	 * @param terNr - adreso teritorinis vienetas 
	 * @param data = data, kuriai norima perþiûrëti duomenis
	 */
	public List getResidents(HttpServletRequest req, Ordering ordering, Long adrNr, Long terNr, Date data)
		throws DatabaseException
	{
		List retVal = null;
		if (adrNr == null && terNr != null){
/*			retVal = HibernateUtils.currentSession(req).createQuery(
			"select  asmuo from GyvenamojiVieta gvt where " +
			"gvt.gvtAdvNr is null and gvt.gvtAtvNr = :terNr or gvt.adkTerNr = :terNr and " +
			"gvt.gvtTipas = 'R' and " +
			"(gvt.gvtDataNuo <= :data and nvl(gvt.gvtDataIki, sysdate) >= :data or :data is null and gvt.gvtDataIki is null)" + ordering.getOrderString()
					)
				.setParameter("terNr", terNr, Hibernate.LONG)
				.setTimestamp("data", data)
				.list();
*/
			retVal = HibernateUtils.currentSession(req).createSQLQuery(
					/* Sitas SQL letokas, be + FIRST_ROWS is vis prastai, bet prie tam tikru duomenu kiekiu gali buti geras */
/*					"Select /*+ FIRST_ROWS */ /* asmuo.ASM_NR, asmuo.AVD_VARDAS, asmuo.AVD_PAVARDE, asmuo.ASM_GIM_DATA, asmuo.ASM_LYTIS, asmuo.ASM_KODAS, asmuo.ASM_MIRTIES_DATA " +
					"	From Gvdisvw_Gyvenamosios_Vietos Gyvenamoji0_ " +
					" 		Inner Join Gvdisvw_Asmenys_Vardai asmuo On Gyvenamoji0_.Gvt_Asm_Nr = asmuo.Asm_Nr " +
					" Where " +
					" 	((Gyvenamoji0_.Gvt_Adv_Nr Is Null And Gyvenamoji0_.Gvt_Atv_Nr =  :terNr)) Or Gyvenamoji0_.Adk_Ter_Nr =  :terNr " +
					"	And Gyvenamoji0_.Gvt_Tipas = 'R' " +
					"	And (Gyvenamoji0_.Gvt_Data_Nuo <= :data And Nvl(Gyvenamoji0_.Gvt_Data_Iki, sysdate) >= :data Or " +
					"		 (:data Is Null) And (Gyvenamoji0_.Gvt_Data_Iki Is Null)) " + ordering.getOrderString()
*/
					// 1 dalis VALDOMS, 2 dalis visiems kitiems, kazkodel DB naudojamas RULE (bent jau pas mus bet baze choose) kaip pas juos neaisku
					"Select asmuo.ASM_NR, asmuo.AVD_VARDAS, asmuo.AVD_PAVARDE, asmuo.ASM_GIM_DATA, asmuo.ASM_LYTIS, asmuo.ASM_KODAS, asmuo.ASM_MIRTIES_DATA, asmuo.AVD_NR " +
					"	From Gvdisvw_Gyvenamosios_Vietos Gyvenamoji0_ " +
					" 		Inner Join Gvdisvw_Asmenys_Vardai asmuo On Gyvenamoji0_.Gvt_Asm_Nr = asmuo.Asm_Nr " +
					" 		Inner Join Gvdisvw_Ak_Adr_Komponentai k On k.adk_adv_nr = Gyvenamoji0_.Gvt_Adv_Nr " +
					" Where " +
					" 	k.Adk_Ter_Nr =  :terNr " +
					" 	and nvl(k.adk_kampo_nr, 1) = nvl(gyvenamoji0_.gvt_kampo_nr, 1) " +
					"   and nvl(k.adk_data_nuo, gvdis_utils.min_data) <= trunc(gvdis_akts.akts_data) " +
					"   and nvl(k.adk_data_iki, gvdis_utils.max_data) > trunc(gvdis_akts.akts_data)" +
					"	And Gyvenamoji0_.Gvt_Tipas = 'R' " +
					"	And (Gyvenamoji0_.Gvt_Data_Nuo <= :data And Nvl(Gyvenamoji0_.Gvt_Data_Iki, sysdate) >= :data Or " +
					"		 :data Is Null And Gyvenamoji0_.Gvt_Data_Iki Is Null) " +
					" Union " +
					" Select asmuo.ASM_NR, asmuo.AVD_VARDAS, asmuo.AVD_PAVARDE, asmuo.ASM_GIM_DATA, asmuo.ASM_LYTIS, asmuo.ASM_KODAS, asmuo.ASM_MIRTIES_DATA, asmuo.AVD_NR " +
					"	From Gvdisvw_Gyvenamosios_Vietos Gyvenamoji0_ " +
					" 		Inner Join Gvdisvw_Asmenys_Vardai asmuo On Gyvenamoji0_.Gvt_Asm_Nr = asmuo.Asm_Nr " +
					" Where " +
					" 	Gyvenamoji0_.Gvt_Adv_Nr Is Null And Gyvenamoji0_.Gvt_Atv_Nr =  :terNr " +
					"	And Gyvenamoji0_.Gvt_Tipas = 'R' " +
					"	And (Gyvenamoji0_.Gvt_Data_Nuo <= :data And Nvl(Gyvenamoji0_.Gvt_Data_Iki, sysdate) >= :data Or " +
					"		 :data Is Null And Gyvenamoji0_.Gvt_Data_Iki Is Null) "  + ordering.getOrderString()
					
					
			)
						.addEntity(Asmuo.class)
						.setParameter("terNr", terNr, Hibernate.LONG)
						.setTimestamp("data", data)
						.list();
		}
		else if (adrNr != null && terNr == null){
			// 2012-10-02 Tieto: I pazymas reikia itraukti irasus, kuriu data_nuo = null.
			
			/*
			retVal = HibernateUtils.currentSession(req).createQuery(
			"select asmuo from GyvenamojiVieta gvt where " +
			"gvt.gvtAdvNr = :adrNr and " +
			"gvt.gvtAtvNr is null and " +
			"gvt.gvtTipas = 'R' and " +
			"(gvt.gvtDataNuo <= :data and nvl(gvt.gvtDataIki, sysdate) >= :data or :data is null and gvt.gvtDataIki is null)" + ordering.getOrderString()
					)
				.setParameter("adrNr", adrNr, Hibernate.LONG)
				.setTimestamp("data", data)
				.list();
            */			
			
			retVal = HibernateUtils.currentSession(req).createQuery(
			"select asmuo from GyvenamojiVieta gvt where " +
			"gvt.gvtAdvNr = :adrNr and " +
			"gvt.gvtAtvNr is null and " +
			"gvt.gvtTipas = 'R' and " +
			"((gvt.gvtDataNuo <= :data and nvl(gvt.gvtDataIki, sysdate) >= :data or :data is null and gvt.gvtDataIki is null)" + 
			  " or (gvt.gvtDataNuo is null and gvt.gvtRegData <=:data and nvl(gvt.gvtDataIki, sysdate) >= :data) )" + ordering.getOrderString()
					)
			    .setParameter("adrNr", adrNr, Hibernate.LONG)
				.setTimestamp("data", data)
				.list();
		}

		return retVal;
	}

    /**
     *  Atnaujina nurodytà gyvenamosios vietos áraðà Gyventojø registre
     */
    public String alterGyvenamojiVieta(HttpServletRequest request, long gvtNr, 
    	long gvtAsmNr, Date dataNuo, Date dataIki, String gvtTipas,
    	Long terNr, Long adrNr, Long gvtKampoNr,
    	String vlsKodas, String vlsPastabos, boolean commit)
    	throws DatabaseException
    {
    	String retVal = "";
    	Session session = HibernateUtils.currentSession(request);
    	try {
    		Connection conn = session.connection();
    		//CallableStatement stmt = conn.prepareCall("{call gvdis_akts.set_gyv_vieta_alter(?,?,?,?,?,?,?,?,?,?,?,?) }");
    		CallableStatement stmt = conn.prepareCall("{call gvdis_akts.gvt_tvarkyk_istorija(?,?,?,?,?,?,?,?,?,?,?,?) }");
    		stmt.setLong(1, gvtAsmNr);
    		stmt.setLong(2, gvtNr);
    		if(dataNuo != null)
    			stmt.setTimestamp(3, new Timestamp(dataNuo.getTime()));
    		else
    			stmt.setNull(3, OracleTypes.TIMESTAMP);
    		//stmt.setTimestamp(3, new Timestamp(dataNuo.getTime()));
    		if(dataIki != null)
    			stmt.setTimestamp(4, new Timestamp(dataIki.getTime()));
    		else
    			stmt.setNull(4, OracleTypes.TIMESTAMP);
    		stmt.setString(5, gvtTipas);
    		if (adrNr == null){
    			stmt.setNull(6, OracleTypes.NUMBER);
    		}
    		else {
    			stmt.setLong(6, adrNr.longValue());
    		}
    		if (terNr == null){
    			stmt.setNull(7, OracleTypes.NUMBER);
    		}
    		else {
    			stmt.setLong(7, terNr.longValue());
    		}
    		stmt.setString(8, vlsKodas);
    		stmt.setString(9, vlsPastabos);
    		if (gvtKampoNr == null){
    			stmt.setNull(10, OracleTypes.NUMBER);
    		}
    		else {
    			stmt.setLong(10, gvtKampoNr.longValue());
    		}
    		stmt.registerOutParameter(11, OracleTypes.VARCHAR);
    		stmt.setInt(12, commit ? 1 : 0);
    		
    		stmt.execute();
    		if (stmt.getString(11) != null) {
    			retVal = stmt.getString(11);
    		}
    		stmt.close();
    		/*
    		if (stmt.execute()){
    			retVal = stmt.getString(11);
    			stmt.close();
    		}*/
    	}
    	catch (SQLException e){
    		throw new DatabaseException(e);
    	}
    	return retVal;
    }
    
    /**
     * Sukuria naujà korekciná gyvenamosios vietos áraðà
     * @param request
     * @param asmNr
     * @param gvtTipas
     * @param valstybe
     * @param dataNuo
     * @param advNr
     * @param atvNr
     * @param adrUzsienyje
     * @throws DatabaseException
     */
    public void createGyvenamojiVieta(HttpServletRequest request, long asmNr, String gvtTipas, String valstybe, 
    		Timestamp dataNuo, Timestamp dataIki, Long advNr, Long atvNr, Long gvtKampoNr, String adrUzsienyje)
    	throws DatabaseException
    {
    	Connection conn = HibernateUtils.currentSession(request).connection();
    	try {
    		// 1. Sukuriam vietà
    		CallableStatement stmt = conn.prepareCall("{call gvdis_akts.set_gyv_vieta_insert(?,?,?,?,?,?,?,?,?,?) }");
    		stmt.setLong(1, asmNr);
    		stmt.setString(3, gvtTipas);
    		stmt.setString(4, valstybe);
    		stmt.setTimestamp(5, dataNuo);
    		if (dataIki != null){
    			stmt.setTimestamp(6, dataIki);
    		}
    		else {
    			stmt.setNull(6, OracleTypes.TIMESTAMP);
    		}
    		
    		if (advNr != null){
    			stmt.setLong(7, advNr.longValue());
    		}
    		else {
    			stmt.setNull(7, OracleTypes.NUMBER);
    		}
    		if (atvNr != null){
    			stmt.setLong(8, atvNr.longValue());
    		}
    		else {
    			stmt.setNull(8, OracleTypes.NUMBER);
    		}
    		stmt.setString(9, adrUzsienyje);
    		if (gvtKampoNr != null){
    			stmt.setLong(10, gvtKampoNr.longValue());
    		}
    		else {
    			stmt.setNull(10, OracleTypes.NUMBER);
    		}
    		
    		stmt.registerOutParameter(1, OracleTypes.NUMBER);
    		stmt.registerOutParameter(2, OracleTypes.NUMBER);
    		
    		stmt.execute();
    		//long gvtNr = stmt.getLong(2); I.N.
    		
    		// 2. Pastumdom datas
    		/*stmt = conn.prepareCall("{call gvdis_akts.set_gyv_vieta_alter(?,?,?,?,?,?,?,?,?,?,?) }");
    		stmt.setLong(1, asmNr);
    		stmt.setLong(2, gvtNr);
    		stmt.setTimestamp(3, dataNuo);
    		if (dataIki == null){
    			stmt.setNull(4, OracleTypes.TIMESTAMP);
    		}
    		else {
    			stmt.setTimestamp(4, dataIki);
    		}
    		stmt.setString(5, gvtTipas);
    		if (advNr == null){
    			stmt.setNull(6, OracleTypes.NUMBER);
    		}
    		else {
    			stmt.setLong(6, advNr.longValue());
    		}
    		if (atvNr == null){
    			stmt.setNull(7, OracleTypes.NUMBER);
    		}
    		else {
    			stmt.setLong(7, atvNr.longValue());
    		}
    		stmt.setString(8, valstybe);
    		stmt.setString(9, adrUzsienyje);
    		stmt.registerOutParameter(10, OracleTypes.VARCHAR);
    		stmt.setInt(11, 1);
    		stmt.execute();*/
    		stmt.close();
    	}
    	catch (SQLException e){
    		throw new DatabaseException(e);
    	}
    }
    //trinant deklaracijas
    public void naikintiAndAtstatytiGyvenamojiVieta(HttpServletRequest request, GyvenamojiVieta current, GyvenamojiVieta ankstesne)
    throws DatabaseException, SQLException{
    	
    	boolean restore = false;  
    	if (ankstesne != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(ankstesne.getGvtDataIki());
			cal.add(Calendar.DATE, 1);
			Date time2 = cal.getTime();
			cal.setTime(current.getGvtDataNuo());
			Date time1 = cal.getTime();
			if (time1.equals(time2)) {
				restore = true;
				//ankstesne.setGvtDataIki(null);
			}
		}
    	//current.setGvtTipas("O");
    	// Timestamp timeTempIki = current.getGvtDataIki(); I.N. 2010.01.29 
    	/*if(current.getGvtDataIki() == null){
    		//current.setGvtDataIki(new Timestamp(new Date().getTime()));
    		timeTempIki = new Timestamp(new Date().getTime());
    	}
    	*/
    		
    	Session session = HibernateUtils.currentSession(request);		
		try {
			//Tieto 2011-10-20: Apkeista vietomis gyvenamosios vietos istrynimas su restoreGyvenamojiVieta
			CallableStatement stmt = session.connection().prepareCall("{ call gvdis_akts.delete_gyvenamoji_vieta(?,?) }");
			stmt.setLong(1, current.getGvtAsmNr());
			stmt.setLong(2, current.getGvtNr());
			stmt.execute();
			stmt.close();
			
			if (restore){
				QueryDelegator.getInstance().restoreGyvenamojiVieta(request, ankstesne.getGvtNr(), ankstesne.getGvtAsmNr(), ankstesne.getGvtDataNuo(), null, ankstesne.getGvtTipas());
			}
			else {
				// o gal yra data iki - 1
				QueryDelegator.getInstance().restoreGyvenamojiVieta1(request, current.getGvtAsmNr(), current.getGvtNr(), current.getGvtDataNuo());
			}
			//session.delete(current);
			
		} catch (DatabaseException e) {
			throw new DatabaseException(e);
		}
    	catch (SQLException e) {
    		throw new DatabaseException(e);
    	}
    }
    
    /**
     * Graþina, ar nurodytam asmeniui buvo iðduotas asmens dokumentas su tokiu numeriu
     * @param asmuo - tikrinamas asmuo
     * @param nr - asmens dokumento numeris 
     */
    public boolean hasDocument(HttpServletRequest req, GvdisBase asm/* Gali buti Asmuo arba LaikinasAsmuo*/, String nr)
    {
    	if (!(asm instanceof Asmuo)){	// Laikiniems asmensims dokumentø nevaliduojame
    		return true;
    	}
    	boolean retVal = false;
    	Session session = HibernateUtils.currentSession(req);
    	
    	Asmuo asmuo = UserDelegator.getInstance().getAsmuoByAsmNr(((Asmuo)asm).getAsmNr(),req);
    	
		Iterator it = session.createQuery(
			"from AsmensDokumentas d where d.asmuo=:asmuo and d.dokBusena='G' and d.dokNegaliojaNuo is null "
		).setEntity("asmuo", asmuo).list().iterator();
		while (it.hasNext()){
			AsmensDokumentas dok = (AsmensDokumentas)it.next();
			if (nr != null && nr.equals(dok.getDokNum())){
				retVal = true;
			}
		}
    	return retVal;
    }
    
    /**
     * Graþina minimalià deklaravimo datà, nuo kurios galima deklaruoti naujà 
     *   gyvenamàjà vietà - kad nepersidengtø su jau ávestais gyvenamosios vietos 
     *   áraðais.
     * @param asmuo - Asmuo, kurio deklaravimo duomenys tikrinami
     */
    public Date minValidDeclDate(HttpServletRequest req, Asmuo asmuo)
    	throws DatabaseException
    {
    	Session session = HibernateUtils.currentSession(req);
    	Date retVal = new Date();
    	try {
    		Connection conn = session.connection();
    		CallableStatement stmt = conn.prepareCall("{ ? = call gvdis_akts.chk_asm_gvt_data(?) }");
    		stmt.registerOutParameter(1, OracleTypes.TIMESTAMP);
    		stmt.setLong(2, asmuo.getAsmNr());
    		stmt.execute();
   			retVal = stmt.getTimestamp(1);
    		stmt.close();
    	}
    	catch (SQLException sqlEx){
    		throw new DatabaseException(sqlEx);
    	}    
    	return retVal;
    }
    
    public boolean isGvtDataNuo(HttpServletRequest req, Asmuo asmuo, Date data){
    	boolean result = false;
    	Asmuo asm = UserDelegator.getInstance().getAsmuoByAsmNr(asmuo.getAsmNr(), req);
    	Set gvtSet = asm.getGyvenamosiosVietos();
    	Iterator iter = gvtSet.iterator();
    	while(iter.hasNext()){
    		GyvenamojiVieta gvt = (GyvenamojiVieta) iter.next();
    		Date temp = gvt.getGvtDataNuo();
    		if (temp != null) {
				if (temp.getTime() == data.getTime()) {
					result = true;
					break;
				}
			}
    	}
    	return result;
    }
    
    /**
     * Patikrina, ar galima sukurti naujà áraðà tarp ðiø datø 
     * @param request
     * @param dataNuo
     * @param dataIki
     */
    public boolean checkNewRecord(HttpServletRequest request, long asmNr, Date dataNuo, Date dataIki)
    	throws DatabaseException
    {
    	boolean retVal = false;
    	Session session = HibernateUtils.currentSession(request);
    	try {
    		Connection conn = session.connection();
    		CallableStatement stmt = conn.prepareCall("{ ? = call gvdis_akts.check_space_for_new_record(?, ?, ?) }");
    		stmt.registerOutParameter(1, OracleTypes.NUMBER);
    		stmt.setLong(2, asmNr);
    		stmt.setTimestamp(3, new Timestamp(dataNuo.getTime()));
    		if (dataIki == null){
    			stmt.setNull(4, OracleTypes.TIMESTAMP);
    		}
    		else {
        		stmt.setTimestamp(4, new Timestamp(dataIki.getTime()));
    		}
    		stmt.execute();
   			int rows = stmt.getInt(1);
   			retVal = (rows == 0);
    		stmt.close();
    	}
    	catch (SQLException sqlEx){
    		throw new DatabaseException(sqlEx);
    	}
    	return retVal;
    }

	public int restoreGyvenamojiVieta(HttpServletRequest request, long gvtNr, long gvtAsmNr, Timestamp dataNuo, Timestamp dataIki, String gvtTipas) 
	throws DatabaseException {
		Session session = HibernateUtils.currentSession(request);
		int kiek = 0;
    	try {
    		Connection conn = session.connection();
    		CallableStatement stmt = conn.prepareCall("{? = call gvdis_akts.set_gyv_vieta_restore(?,?,?,?,?) }");
    		stmt.registerOutParameter(1, OracleTypes.NUMBER);
    		stmt.setLong(2, gvtAsmNr);
    		stmt.setLong(3, gvtNr);
    		if(dataNuo != null)
    			stmt.setTimestamp(4, new Timestamp(dataNuo.getTime()));
    		else
    			stmt.setNull(4, OracleTypes.TIMESTAMP);
    		if(dataIki != null)
    			stmt.setTimestamp(5, new Timestamp(dataIki.getTime()));
    		else
    			stmt.setNull(5, OracleTypes.TIMESTAMP);
    		stmt.setString(6, gvtTipas);    		
    		stmt.execute();    			
   			kiek = stmt.getInt(1);
    		stmt.close();
    	}
    	catch (SQLException e){
    		throw new DatabaseException(e);
    	}
    	return kiek;
	}

	// reigu nebuvo nurodyta gyvenamoji vieta tai gal yra data_nuo - 1 tai tokia data_iki yra apnulinama
	public void restoreGyvenamojiVieta1(HttpServletRequest request, long gvtAsmNr, long gvtNr, Timestamp dataNuo) 
	throws DatabaseException {
		Session session = HibernateUtils.currentSession(request);
    	try {
    		Connection conn = session.connection();
    		CallableStatement stmt = conn.prepareCall("{call gvdis_akts.set_gyv_vieta_update(?,?,?) }");
    		stmt.setLong(1, gvtAsmNr);
    		stmt.setLong(2, gvtNr);
    		if(dataNuo != null)
    			stmt.setTimestamp(3, new Timestamp(dataNuo.getTime()));
    		else
    			stmt.setNull(3, OracleTypes.TIMESTAMP);

    		stmt.execute();    
    		stmt.close();
    	}
    	catch (SQLException e){
    		throw new DatabaseException(e);
    	}
	}
/*
	public Timestamp getGyvenamojiVGaliojimas(HttpSession session, long id ) {
		Object username = session.getAttribute("userLogin");
		Object userPassword = session.getAttribute("userPassword");
		try {
			Connection conn = DriverManager.getConnection(Constants.getDB(), username.toString(), userPassword.toString() );
			String qry = "select gvt_dkl_galioja_iki from gyven"
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		
		return null;
	}
	*/

}
