package com.algoritmusistemos.gvdis.web.delegators;

import gnu.regexp.RE;
import gnu.regexp.REMatch;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class SprendimaiDelegator
{
    private static SprendimaiDelegator instance;

    public SprendimaiDelegator()
    {
    }

    public static SprendimaiDelegator getInstance()
    {
        if(instance == null)
            instance = new SprendimaiDelegator();
        return instance;
    }


    public PrasymasKeistiDuomenis getPrasymas(HttpServletRequest req, long id)
    {
    	return (PrasymasKeistiDuomenis)HibernateUtils.currentSession(req).load(PrasymasKeistiDuomenis.class, new Long(id));
    }

    /**
     * Graþina sprendimà su duotu ID
     */
    public SprendimasKeistiDuomenis getSprendimas(HttpServletRequest req, long id)
    {
    	return (SprendimasKeistiDuomenis)HibernateUtils.currentSession(req).load(SprendimasKeistiDuomenis.class, new Long(id));
    }
    
    /**
     * Graþina visø praðymø keisti duomenis sàraðà
     */
    public List getPrasymai(HttpServletRequest req, Paging paging, Ordering ordering,
    		Date dataNuo, Date dataIki, int busena, int tipas, 
    		Istaiga savivaldybe, Istaiga seniunija)
    {
    	String queryStr = "from PrasymasKeistiDuomenis where (data >= :dataNuo or :dataNuo is null) " + 
    		"and (data <= :dataIki or :dataIki is null) " + 
    		"and (tipas = :tipas or :tipas = -1) " + 
    		"and (busena = :busena or :busena = -1) " +
    		"and (istaiga = :istaiga or istaiga.istaiga = :istaiga or :istaiga is null)";
    	
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		cntQuery.setInteger("tipas", tipas);
		cntQuery.setInteger("busena", busena);
		if (seniunija != null || savivaldybe != null){
			cntQuery.setEntity("istaiga", seniunija != null ? seniunija : savivaldybe);
		}
		else {
			cntQuery.setParameter("istaiga", null, Hibernate.entity(Istaiga.class));
		}
		Integer count = (Integer)cntQuery.uniqueResult();
    	paging.setTotalNumber(count.intValue());

    	Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		query.setInteger("tipas", tipas);
		query.setInteger("busena", busena);
		if (seniunija != null || savivaldybe != null){
			query.setEntity("istaiga", seniunija != null ? seniunija : savivaldybe);
		}
		else {
			query.setParameter("istaiga", null, Hibernate.entity(Istaiga.class));
		}
    	
    	List retVal = query
    		.setFirstResult(paging.getFirstItem())
    		.setMaxResults(paging.getPageSize())
    		.list();
    	return retVal.isEmpty() ? null : retVal;
    }
    
    /**
     * Graþina naujà registracijos numerá pagal nutylëjimà naujai kuriamam praðymui   
     */
    private String getDefaultRegNr(HttpServletRequest req, Session session, String type, String trumpinys)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	String istaiga = String.valueOf(req.getSession().getAttribute("userIstaigaId"));
    	
//    	>>ankstesnio reg nr formato esamo didþiausio skaitliuko nustatymui ju.k 2007.08.14
    	String regPrefix_tmp = sdf.format(new Date()) + "-" + istaiga + "-";
    	int maxNumber_tmp = 0;
    	List l_tmp = session.createQuery("select regNr from " + type + " where regNr like '" + regPrefix_tmp + "%'").list();
    	for (Iterator it=l_tmp.iterator(); it.hasNext(); ){
    		String regNr_tmp = (String)it.next();
    		try {
    	    	RE pattern = new RE("[0-9]*-[0-9]*-([0-9]*)");
    			REMatch match = pattern.getMatch(regNr_tmp);
    			if (match != null){
    				int n = Integer.parseInt(match.toString(1));
    				if (n > maxNumber_tmp) maxNumber_tmp = n;
    			}
    		}
    		catch (Exception e){
    		}
    	}
        //<<ankstesnio reg nr formato esamo didþiausio skaitliuko nustatymui ju.k 2007.08.14
    	
    	
    	String regPrefix = "(" + sdf.format(new Date()) + "-" + istaiga + "-" + trumpinys + ")-"; //ju.k 2007.08.13
    	int maxNumber = maxNumber_tmp; //0; ju.k 2007.08.14, laikinam nustatymui 2007 metams, po to gali buti 0
    	List l = session.createQuery("select regNr from " + type + " where regNr like '" + regPrefix + "%'").list();
    	for (Iterator it=l.iterator(); it.hasNext(); ){
    		String regNr = (String)it.next();
    		try {
    	    	//RE pattern = new RE("[0-9]*-[0-9]*-([0-9]*)"); kom ju.k 2007.08.13
    			RE pattern = new RE("[(]*[0-9]*-[0-9]*-[a-zA-Z]*[)]*-([0-9]*)"); //ju.k 2007.08.13
    			REMatch match = pattern.getMatch(regNr);
    			if (match != null){
    				int n = Integer.parseInt(match.toString(1));
    				if (n > maxNumber) maxNumber = n;
    			}
    		}
    		catch (Exception e){
    		}
    	}
    	return regPrefix + String.valueOf(maxNumber + 1);
    }
    
    /**
     * Iðsaugo duotà praðymà duomenø bazëje 
     */
    public void savePrasymas(HttpServletRequest req, PrasymasKeistiDuomenis prasymas)
    	throws DatabaseException
    {
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	if (prasymas.getRegNr() == null || "".equals(prasymas.getRegNr())){
    		prasymas.setRegNr(getDefaultRegNr(req, session, "PrasymasKeistiDuomenis", "PRK"));
    	}
    	try {
    		session.save(prasymas);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
    }

    /**
     * Iðtrina duotà praðymà
     * @throws DatabaseException
     */
    public void deletePrasymas(HttpServletRequest req, long id)
    	throws DatabaseException
    {
    	PrasymasKeistiDuomenis prasymas = this.getPrasymas(req, id);
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	try {
    		session.delete(prasymas);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
    }
    
    /**
     * Graþina visø sprendimø keisti duomenis sàraðà
     */
    public List getSprendimai(HttpServletRequest req, Paging paging, Ordering ordering,
    		Date dataNuo, Date dataIki, int tipas, 
    		Istaiga savivaldybe, Istaiga seniunija)
    {
    	String queryStr = "from SprendimasKeistiDuomenis where (data >= :dataNuo or :dataNuo is null) " + 
    		"and (data <= :dataIki or :dataIki is null) " + 
    		"and (tipas = :tipas or :tipas = -1) " + 
    		"and (istaiga = :istaiga or istaiga.istaiga = :istaiga or :istaiga is null)";
    	
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		cntQuery.setInteger("tipas", tipas);
		if (seniunija != null || savivaldybe != null){
			cntQuery.setEntity("istaiga", seniunija != null ? seniunija : savivaldybe);
		}
		else {
			cntQuery.setParameter("istaiga", null, Hibernate.entity(Istaiga.class));
		}
		Integer count = (Integer)cntQuery.uniqueResult();
    	paging.setTotalNumber(count.intValue());

    	Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		query.setInteger("tipas", tipas);
		if (seniunija != null || savivaldybe != null){
			query.setEntity("istaiga", seniunija != null ? seniunija : savivaldybe);
		}
		else {
			query.setParameter("istaiga", null, Hibernate.entity(Istaiga.class));
		}
    	
    	List retVal = query
    		.setFirstResult(paging.getFirstItem())
    		.setMaxResults(paging.getPageSize())
    		.list();
    	return retVal.isEmpty() ? null : retVal;
    }
 
   
    /**
     * Graþina gyvenamàjà vietà pagal duotus parametrus
     */
    public GyvenamojiVieta getGyvenamojiVieta(Session session, long gvtAsmNr, long gvtNr)
    {
    	return (GyvenamojiVieta)session.createQuery(
    		"from GyvenamojiVieta where gvtAsmNr=:gvtAsmNr and gvtNr=:gvtNr")
    		.setLong("gvtAsmNr", gvtAsmNr)
    		.setLong("gvtNr", gvtNr)
    		.uniqueResult();
    }
    public List getGyvenamosiosVietos(Session session, long gvtAsmNr)
    {
    	//buvo 
		// "from GyvenamojiVieta g where g.gvtAsmNr=:gvtAsmNr order by g.gvtNr desc")
    	// pakeista 2011.05.20 romasj - gvtnr yra neprasminis ir jis gali eiti ne is eiles!
    	return session.createQuery(
    		"from GyvenamojiVieta g where g.gvtAsmNr=:gvtAsmNr order by g.gvtDataIki desc")
    		.setLong("gvtAsmNr", gvtAsmNr)
    		.list();
    }    
    /**
     * Atnaujina gyvenamosios vietos áraðà 
     */
 
    private void updateGyvenamojiVieta(Session session, GyvenamojiVieta oldGvt, GyvenamojiVieta newGvt)
	throws SQLException
{
	Connection conn = session.connection();
	CallableStatement stmt = conn.prepareCall("{call gvdis_akts.set_gyv_vieta(?,?,?,?,?,?,?,null,null,null,null,null,null,?,?,?,1,?,?,?) }"); // gal
	stmt.setLong(1, oldGvt.getAsmuo().getAsmNr());
	stmt.setLong(2, oldGvt.getGvtNr());
	//stmt.setString(3, oldGvt.getGvtTipas());
	// paredagavus priezasti turi buti keiciamas ir tipas
	// del to gvtTipas reikia naudoti ne is oldGvt o is newGvt
	stmt.setString(3, newGvt.getGvtTipas());
	stmt.setString(4, newGvt.getValstybe() != null ? newGvt.getValstybe().getKodas() : QueryDelegator.HOME_COUNTRY);
	stmt.setTimestamp(5, oldGvt.getGvtDataNuo());
	if (newGvt.getGvtAdvNr() != null){
		stmt.setLong(6, newGvt.getGvtAdvNr().longValue());
	}
	else {
		stmt.setNull(6, OracleTypes.NUMBER);
	}
	if (newGvt.getGvtAtvNr() != null){
		stmt.setLong(7, newGvt.getGvtAtvNr().longValue());
	}
	else {
		stmt.setNull(7, OracleTypes.NUMBER);
	}
	stmt.setString(8, newGvt.getGvtAdrUzsenyje());
	stmt.setNull(9, OracleTypes.NUMBER);
	stmt.setNull(10, OracleTypes.VARCHAR);
	stmt.setNull(11, OracleTypes.VARCHAR);
	if (newGvt.getGvtKampoNr() != null) {
		stmt.setLong(12, newGvt.getGvtKampoNr().longValue());
	}
	else {
		stmt.setNull(12, OracleTypes.NUMBER);
	}
	
	Timestamp p_dkl_galioja_iki = null;
	
	if (null != oldGvt)
		if ( null != oldGvt.getGvtGaliojaIki())
		{
			p_dkl_galioja_iki = oldGvt.getGvtGaliojaIki();
		}
	stmt.setTimestamp(13, p_dkl_galioja_iki );
	stmt.execute();
	
}
    /**
     * Sukuria naujà gyvenamosios vietos áraðà 
     * --Nenaudojama
     */
/* I.N.   
    private GyvenamojiVieta createGyvenamojiVieta(Session session, GyvenamojiVieta gvt, SprendimasKeistiDuomenis sprendimas)
    	throws SQLException
    {
    	Connection conn = session.connection();
		CallableStatement stmt = conn.prepareCall("{call gvdis_akts.set_gyv_vieta(?,?,?,?,?,?,?,null,null,null,null,null,null,?,?,?) }");
		stmt.setLong(1, gvt.getAsmuo().getAsmNr());
		stmt.setString(3, gvt.getGvtTipas());
		stmt.setString(4, gvt.getValstybe().getKodas());
		stmt.setTimestamp(5, gvt.getGvtDataNuo());
		if (gvt.getGvtAdvNr() != null){
			stmt.setLong(6, gvt.getGvtAdvNr().longValue());
		}
		else {
			stmt.setNull(6, OracleTypes.NUMBER);
		}
		if (gvt.getGvtAtvNr() != null){
			stmt.setLong(7, gvt.getGvtAtvNr().longValue());
		}
		else {
			stmt.setNull(7, OracleTypes.NUMBER);
		}
		stmt.setString(8, gvt.getGvtAdrUzsenyje());
		stmt.setNull(9, OracleTypes.NUMBER);
		stmt.setNull(10, OracleTypes.VARCHAR);
		
		stmt.registerOutParameter(1, OracleTypes.NUMBER);
		stmt.registerOutParameter(2, OracleTypes.NUMBER);
		
		stmt.execute();
		
		long gvtNr = stmt.getLong(2);
		
		//-->ju.k 2008.01.18
		if (gvt.getGvtAdvNr() != null){
         String qry2 = "{call gvdis_utils.atnaujinti_ak_ter_adr_el_h(?,?) }";
		 CallableStatement stmt2 = conn.prepareCall(qry2);
		 stmt2.setLong(1, gvt.getGvtAdvNr().longValue());
		 stmt2.setLong(2, (sprendimas.getIstaiga()).getId());
		 stmt2.execute();};
        //<--ju.k 2008.01.18
		
		return getGyvenamojiVieta(session, gvt.getAsmuo().getAsmNr(), gvtNr);
    }
  */  
    public void saveSprendimas(HttpServletRequest req, SprendimasKeistiDuomenis sprendimas, 
    		GyvenamojiVieta gvt, Long prasymasId) throws Exception {
    	
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	/*
    	if (sprendimas.getTipas() == 2) {
    		String priezKodas = sprendimas.getPriezastis().getKodas();
    		int kodas = Integer.parseInt(priezKodas);
			switch (kodas) {
				case 1: gvt.setGvtTipas("D"); break; 
				case 2: gvt.setGvtTipas("I"); break;
				case 3: gvt.setGvtTipas("S"); break;
				case 4: gvt.setGvtTipas("O"); break;
				case 5: gvt.setGvtTipas("C"); break;
				case 6: gvt.setGvtTipas("G"); break;
				case 7: gvt.setGvtTipas("H"); break;
				case 8: gvt.setGvtTipas("Z"); break;
				default:
					throw new InternalException("Nurodytas prieþasties kodas negali bûti apdorotas." +
							" Apdorojami kodai 1..8. Patikrinkite SPRENDIMO_PRIEZASTIS þinyno reikðmes.");
			}
    	} else if(sprendimas.getTipas() == 0 || sprendimas.getTipas() == 1) {
    		gvt.setGvtTipas("R"); 		
    	} else if(sprendimas.getTipas() == 3) {
    		gvt.setGvtTipas("K");
    	}
    	 */
    	//naujas sprendimas
    	if (sprendimas.getId() == 0) {
    		try {
    			//iraso sprendima
    			CallableStatement stmt1 = session.connection().prepareCall(
    					"{call gvdis_akts.ins_sprendimas(?,?,?,?,?,?,?,?,?,?)}"); // ne
    			stmt1.setLong(1, sprendimas.getPriezastis().getId());
    			stmt1.setLong(2, sprendimas.getIstaiga().getId());
    			if (sprendimas.getRegNr() == null) 
    				sprendimas.setRegNr(getDefaultRegNr(req, session, "SprendimasKeistiDuomenis", "SPR"));
    			else if (sprendimas.getRegNr().equals("")) {
    				sprendimas.setRegNr(getDefaultRegNr(req, session, "SprendimasKeistiDuomenis", "SPR"));
    				
    			}
    			
    			stmt1.setString(3, sprendimas.getRegNr());
    			
    			stmt1.setTimestamp(4, sprendimas.getData());
   
    			stmt1.setLong(5, sprendimas.getTipas());
    			stmt1.setTimestamp(6, sprendimas.getNaikinimoData());
    			stmt1.setLong(7, prasymasId.longValue());
    			stmt1.setString(9, sprendimas.getPrieme());
    			stmt1.setString(10, sprendimas.getPastabos()); 
    				
    			stmt1.registerOutParameter(8, OracleTypes.NUMBER);
    			stmt1.execute();
    				
    			long sprendimoId = stmt1.getLong(8);
    			
    			
    			if (sprendimoId != 0) {
    				//jei sprendimas naikinti
    				if (sprendimas.getTipas() == 2 || sprendimas.getTipas() == 3) {    					
    					for (Iterator it = sprendimas.getAsmenys().iterator(); it.hasNext();) {
    						Asmuo asmuo = (Asmuo) it.next();

    						List gvList = getGyvenamosiosVietos(session, asmuo.getAsmNr());
    						gvt = (GyvenamojiVieta) gvList.get(0);    
    						/*GyvenamojiVieta gvtjkkk = new GyvenamojiVieta(); 
    						gvtjkkk = sprendimas.getGyvenamojiVieta();
    						//PrasymasKeistiDuomenis praskeist =  ;
    						String s = sprendimas.getPrasymas().getNaikinamasAdresas();
    						if(s == gvt.toString())
    						{*/
    							System.out.print("sutampa");
    							CallableStatement stmt2 = session.connection().prepareCall(
    									"{call gvdis_akts.sprendimas_naikinti_gv(?,?,?,?)}"); //gal
    							stmt2.setLong(1, sprendimoId);
    							stmt2.setLong(2, asmuo.getAsmNr());
    							stmt2.setLong(3, gvt.getGvtNr());
    							stmt2.setTimestamp(4, sprendimas.getNaikinimoData());

    							stmt2.execute();
    						/*}
    						else
    						{
    							System.out.print("NeSutampa");    							
    						}*/
    					}
    				}
    				//jei sprendimas keisti/taisyti
    				else {    					
        				for (Iterator it = sprendimas.getAsmenys().iterator(); it.hasNext();) {
        					Asmuo asmuo = (Asmuo) it.next();
        					
        					CallableStatement stmt = session.connection().prepareCall(
        							"{call gvdis_akts.set_gyv_vieta(?,?,?,?,?,?,?,null,null,null,null,null,null,?,?,?, null,null,?,?) }"); //gal
        					
        					stmt.setLong(1, asmuo.getAsmNr());
        					stmt.setNull(2, OracleTypes.NUMBER);
        					stmt.setString(3, gvt.getGvtTipas());
        					stmt.setString(4, gvt.getValstybe().getKodas());
        					stmt.setTimestamp(5, gvt.getGvtDataNuo());
        					
        					if (gvt.getGvtAdvNr() != null){
        						stmt.setLong(6, gvt.getGvtAdvNr().longValue());
        						if (gvt.getGvtKampoNr() != null) stmt.setLong(11, gvt.getGvtKampoNr().longValue());
        						else stmt.setNull(11, OracleTypes.NUMBER);
        					}        					
        					else {
        						stmt.setNull(6, OracleTypes.NUMBER);
        						stmt.setNull(11, OracleTypes.NUMBER);
        					}
        					
        					if (gvt.getGvtAtvNr() != null){
        						stmt.setLong(7, gvt.getGvtAtvNr().longValue());
        					}
        					else {
        						stmt.setNull(7, OracleTypes.NUMBER);
        					}
        					
        					stmt.setString(8, gvt.getGvtAdrUzsenyje());
        					stmt.setNull(9, OracleTypes.NUMBER);
        					stmt.setNull(10, OracleTypes.VARCHAR);        				  					
        					stmt.registerOutParameter(2, OracleTypes.NUMBER);
        					
        					List gvList1 = getGyvenamosiosVietos(session, asmuo.getAsmNr());
    						GyvenamojiVieta gvt1 = (GyvenamojiVieta) gvList1.get(0);
    						Timestamp p_dkl_galioja_iki = null;
    						
    						if (null != gvt1)
    							if ( null != gvt1.getGvtGaliojaIki())
    							{
    								p_dkl_galioja_iki = gvt1.getGvtGaliojaIki();
    							}
    						stmt.setTimestamp(12, p_dkl_galioja_iki );
    						
        					stmt.execute();
        					
        					long gvtNr = stmt.getLong(2);
        					if (gvtNr != 0) {
        						List gvList = getGyvenamosiosVietos(session, asmuo.getAsmNr());
        						gvt = (GyvenamojiVieta) gvList.get(0);  
        					
        						CallableStatement stmt3 = session.connection().prepareCall("" +
        								"{call gvdis_akts.sprendimas_keisti_taisyti_gv(?,?,?)}"); // ne
        						stmt3.setLong(1, sprendimoId);
        						stmt3.setLong(2, asmuo.getAsmNr());
        						//stmt3.setLong(3, gvt.getGvtNr());
        						stmt3.setLong(3, gvtNr);
        						//stmt3.setTimestamp(4, sprendimas.getData());
        						
        						stmt3.execute();
        						
        						if (gvt.getGvtAdvNr() != null){
        							 Connection conn = HibernateUtils.currentSession(req).connection();
        					         String qry2 = "{call gvdis_utils.atnaujinti_ak_ter_adr_el_h(?,?) }"; // ne
        							 CallableStatement stmt4 = conn.prepareCall(qry2);
        							 stmt4.setLong(1, gvt.getGvtAdvNr().longValue());
        							 stmt4.setLong(2, (sprendimas.getIstaiga()).getId());
        							 stmt4.execute();
        						}
        						
        					} else {        	
        						throw new InternalException("Nepavyko áraðyti gyvenamosios vietos!");
        					}
        					
        				}
    				}
    			}
    			
    			tx.commit();
    		} catch(Exception ex) {
    			tx.rollback();
    			throw new DatabaseException(ex);
    		}
    	} 
    	
    	//egzistuojantis sprendimas
    	else {
    		try {
    			CallableStatement stmt = session.connection().prepareCall(
					"{call gvdis_akts.upd_sprendimas(?,?,?,?,?)}"); // ne
    			stmt.setLong(1, sprendimas.getId());
    			stmt.setLong(2, sprendimas.getPriezastis().getId());
    			stmt.setTimestamp(3, sprendimas.getData());    			
    			stmt.setString(4, sprendimas.getPrieme());
    			stmt.setString(5, sprendimas.getPastabos()); 
    			
    			stmt.execute();
    			
    			if (sprendimas.getTipas() < 2) {  
    				for (Iterator it=sprendimas.getGyvenamosiosVietos().iterator(); it.hasNext(); ){
    					GyvenamojiVieta oldGvt = (GyvenamojiVieta)it.next();
    	    			updateGyvenamojiVieta(session, oldGvt, gvt);
    				}    				
    			}
    			
    			tx.commit();
    		} catch (Exception ex) {
    			tx.rollback();
    			throw new DatabaseException(ex);    			
    		}    		
    	}     	   	
    }
        
    public int deleteSprendimas(HttpServletRequest request, SprendimasKeistiDuomenis spr) throws Exception {
		Set gyvenamosiosVietos = spr.getGyvenamosiosVietos();
		Session session = HibernateUtils.currentSession(request);
    	Transaction tx = session.beginTransaction();
    	try{    		
    		session.delete(spr);    		
    		try{    			
    			PrasymasKeistiDuomenis prasymas = SprendimaiDelegator.getInstance().getPrasymas(request, spr.getPrasymas().getId());
        		prasymas.setBusena(0);
        		prasymas.setSprendimas(null);
        		session.update(prasymas);
    		}catch(Exception e){}
    		List trinamosGvt = new ArrayList();
    		for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) { 				
					GyvenamojiVieta gvt = (GyvenamojiVieta) it.next();
					trinamosGvt.add(gvt);	
					
					if (spr.getTipas() == 1 ||  spr.getTipas() == 0) {					
						Deklaracija dekl = (Deklaracija) session
						.createQuery(
						"from Deklaracija d where d.gvtAsmNrAnkstesne=:asmNr and d.gvtNrAnkstesne=:nr")
						.setLong("asmNr", gvt.getGvtAsmNr()).setLong("nr",
								gvt.getGvtNr()).uniqueResult();

						GyvenamojiVieta ankstesneGv = ankstesneGyvenamojiVieta(request, gvt, true);

						if (dekl != null) {
							dekl.setGvtAsmNrAnkstesne((ankstesneGv == null) ? null : new Long(ankstesneGv
									.getGvtAsmNr()));
							dekl.setGvtNrAnkstesne((ankstesneGv == null) ? null : new Long(ankstesneGv
									.getGvtNr()));
							session.update(dekl);
						}

						PazymosDelegator.getInstance().removeGvPazymaByGVT(request, gvt);
						//if (gvt.getGvtDataIki() == null)
						//atstatytiGyvenamojiVieta(gvt, request);
					
					}
			}
    		session.flush();
    		
    			for (Iterator it=trinamosGvt.iterator();it.hasNext();) {
     				GyvenamojiVieta gvtTemp = (GyvenamojiVieta)it.next();
     				
     				CallableStatement stmt2;
     				if (spr.getTipas() == 1 ||  spr.getTipas() == 0) {
     					stmt2 = session.connection().prepareCall("{call gvdis_akts.delete_gyvenamoji_vieta(?,?) }");
        				stmt2.setLong(1, gvtTemp.getGvtAsmNr());
        				stmt2.setLong(2, gvtTemp.getGvtNr());
     				} else {
     					if (QueryDelegator.getInstance().getAsmGyvenamojiVieta(request, gvtTemp.getGvtAsmNr()) == gvtTemp.getGvtNr()) { 
     						stmt2 = session.connection().prepareCall("{call gvdis_akts.del_sprendimas_naikinti(?,?,?) }");
     						stmt2.setLong(1, gvtTemp.getGvtAsmNr());
     						stmt2.setLong(2, gvtTemp.getGvtNr());
     						stmt2.setLong(3, spr.getTipas());
     					}
     					else {
         					stmt2 = session.connection().prepareCall("{call gvdis_akts.set_gyv_gvt_tipas(?,?,?) }");
            				stmt2.setLong(1, gvtTemp.getGvtAsmNr());
            				stmt2.setLong(2, gvtTemp.getGvtNr());
            				stmt2.setString(3, "R");
     					}
     				}
     				
    				stmt2.execute();
    				stmt2.close();
    				//Tieto 2011-11-07: Apkeista vietomis gyvenamosios vietos istrynimas su atstatytiGyvenamojiVieta
        			if (spr.getTipas() == 1 ||  spr.getTipas() == 0) {
        				atstatytiGyvenamojiVieta(gvtTemp, request);
        			}

    			}
    		 
    		
    		tx.commit();
    	}catch(Exception e){
    		tx.rollback();
    		throw new Exception(e);    		
    	}
		return 0;		
	}
     	 
	private void atstatytiGyvenamojiVieta(GyvenamojiVieta gvt, HttpServletRequest request) throws DatabaseException{		
		GyvenamojiVieta sekantiGvt = velesneGyvenamojiVieta(request, gvt);
		GyvenamojiVieta restoreGvt = null;
		Timestamp atstatytiDataIki = null;
		if (sekantiGvt != null) {
			restoreGvt = ankstesneGyvenamojiVieta(request, gvt, false);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(sekantiGvt.getGvtDataNuo());
			cal.add(Calendar.DATE, -1);
			atstatytiDataIki = new Timestamp(cal.getTimeInMillis());
		}
		else {
			restoreGvt = ankstesneGyvenamojiVieta(request, gvt, true);
		}
		////////////////////		
		if(restoreGvt != null){				
			try {
				QueryDelegator.getInstance().restoreGyvenamojiVieta(request,
						restoreGvt.getGvtNr(), restoreGvt.getGvtAsmNr(),                        
						restoreGvt.getGvtDataNuo(),
						atstatytiDataIki,
				        restoreGvt.getGvtTipas()
				        );
			} catch (DatabaseException e) {				
				throw new DatabaseException(e);
			}
		}
	}

	private GyvenamojiVieta ankstesneGyvenamojiVieta(HttpServletRequest request, GyvenamojiVieta gvt, boolean vienosDienosSkirtumas) {
		Asmuo asmuo = null;
		try {
			asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, gvt.getAsmuo().getAsmKodas().toString());
		} catch (ObjectNotFoundException e) {		
		}
		GyvenamojiVieta restoreGvt = null;
		if (asmuo != null) { // I.N. 2010.02.08
			Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();
			for(Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();){
				GyvenamojiVieta gvtSena = (GyvenamojiVieta) it.next();
				if(gvtSena.getGvtDataNuo() != null && gvtSena.getGvtDataNuo().getTime() < gvt.getGvtDataNuo().getTime() 
						&& gvtSena.getGvtDataIki() != null && gvtSena.getGvtNr() < gvt.getGvtNr()){
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(gvtSena.getGvtDataIki());
					cal.add(Calendar.DATE, 1);
					Date time2 = cal.getTime();
					cal.setTime(gvt.getGvtDataNuo());
					Date time1 = cal.getTime();
					if (time1.equals(time2)) {	
						if(restoreGvt == null)
							restoreGvt = gvtSena;
						else if(restoreGvt.getGvtNr() < gvtSena.getGvtNr())
							restoreGvt = gvtSena;
					}
				}			
			}
			if (vienosDienosSkirtumas == false)
				if (restoreGvt == null) {
					for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) {
						GyvenamojiVieta gvtSena = (GyvenamojiVieta) it.next();
						if (gvtSena.getGvtDataNuo() != null && gvt.getGvtDataNuo() != null) {
							if (gvtSena.getGvtDataNuo().getTime() <= gvt.getGvtDataNuo().getTime()
									&& gvtSena.getGvtDataIki() != null
									&& gvtSena.getGvtNr() < gvt.getGvtNr()) {
								if (restoreGvt == null) {
									restoreGvt = gvtSena;
								} else if (restoreGvt.getGvtNr() > gvtSena.getGvtNr())
									restoreGvt = gvtSena;
							}
						}
					}
				}
		}	
		return restoreGvt;
	}
	private GyvenamojiVieta velesneGyvenamojiVieta(HttpServletRequest request, GyvenamojiVieta gvt) {
		Asmuo asmuo = null;
		try {
			asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, gvt.getAsmuo().getAsmKodas().toString());
		} catch (ObjectNotFoundException e) {		
		}
		GyvenamojiVieta sekantiGvt = null;
		if (asmuo != null) {
			Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();
			for(Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();){
				GyvenamojiVieta gvtNauja = (GyvenamojiVieta) it.next();
				if(gvtNauja.getGvtDataNuo() != null && gvt.getGvtDataNuo() != null 
						&& gvtNauja.getGvtDataNuo().getTime() >= gvt.getGvtDataNuo().getTime() 
						&& gvtNauja.getGvtNr() > gvt.getGvtNr()){
					/*GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(gvtNauja.getGvtDataNuo());
					cal.add(Calendar.DATE, -1);
					Date time2 = cal.getTime();
					cal.setTime(gvt.getGvtDataIki());
					Date time1 = cal.getTime();
					if (time1.equals(time2)) {*/	
						if(sekantiGvt == null)
							sekantiGvt = gvtNauja;
						else if(sekantiGvt.getGvtNr() < gvtNauja.getGvtNr())
							sekantiGvt = gvtNauja;
					//}
				}			
			}
		}	
		return sekantiGvt;
	}
}