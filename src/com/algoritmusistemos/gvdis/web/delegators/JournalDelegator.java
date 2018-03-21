package com.algoritmusistemos.gvdis.web.delegators;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import org.hibernate.Query;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.SavPazyma;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class JournalDelegator
{
	public static final int JOURNAL_TYPE_IN = 0;
	public static final int JOURNAL_TYPE_OUT = 1;
	public static final int JOURNAL_TYPE_GVPAZ = 2;
	public static final int JOURNAL_TYPE_SPREND = 3;
	public static final int JOURNAL_TYPE_GVNA = 4;
	public static final int JOURNAL_TYPE_GVNAPAZ = 5;
	public static final int JOURNAL_TYPE_SAVPAZ = 6;
	
	private static JournalDelegator instance;
	
	public static JournalDelegator getInstance() 
	{
		if (instance == null){
			instance =  new JournalDelegator();
		}
		return instance;
	}	
	
	/**
	 * Graþina visø registruotø savivaldybiø sàraðà
	 */
	public List getSavivaldybes(HttpServletRequest req)
	{
		List retVal;
		if (req.getAttribute("journalType") != null) {
			retVal = HibernateUtils.currentSession(req).createQuery(
				"from Istaiga ist where ist.istaiga is null order by ist.pavadinimas").list();
		}
		else {
			retVal = HibernateUtils.currentSession(req).createQuery(
			//"from Istaiga ist where ist.istaiga is null and ist.tipas>0 and ist.tipas<3 order by ist.pavadinimas").list();
			"from Istaiga ist where ist.istaiga is null order by ist.pavadinimas").list();
		}
		return retVal;
	}

	/**
	 * Graþina ástaigà su duotu identifikatoriumi
	 * @param id - ástaigos ID
	 */
	public Istaiga getIstaiga(HttpServletRequest req, long id)
	{
		Istaiga retVal = (Istaiga)HibernateUtils.currentSession(req).createQuery(
			"from Istaiga ist where ist.id=:id").setLong("id", id).uniqueResult();
		return retVal;
	}

	/**
	 * Graþina visas duotos savivaldybës seniûnijas
	 * @param savivaldybe - savivaldybë, kurios seniûnijas reikia graþinti
	 */
	/*public List getSeniunijos(HttpServletRequest req, Istaiga savivaldybe)
	{
		List retVal = HibernateUtils.currentSession(req).createQuery(
			"from Istaiga ist where ist.istaiga = :savivaldybe order by ist.pavadinimas")
			.setEntity("savivaldybe", savivaldybe)
			.list();
		return retVal;
	}*/
	
	public List getSeniunijos(HttpServletRequest req, Istaiga savivaldybe) throws DatabaseException{	
		
		List retVal = new ArrayList();
		try {
            String qry = "{ ? = call gvdis_akts.get_seniunijas_by_sav(?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setLong(2, savivaldybe.getId());			
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
            	long id = rs.getLong("id");
            	retVal.add(getIstaiga(req, id));
            }
            rs.close();
            stmt.close();
		}
		catch (SQLException sqlEx){
			throw new DatabaseException(sqlEx);
		}
		return retVal;
		
	}
	
/*	public List getSeniunijos(HttpServletRequest req)
	{
		List retVal = HibernateUtils.currentSession(req).createQuery(
			"from Istaiga ist where ist.tipas=2 order by ist.pilnasPavadinimas")			
			.list();
		return retVal;
	}
	*/
	public List getSeniunijos(HttpServletRequest req)
	{
		List retVal = HibernateUtils.currentSession(req).createSQLQuery(
			"select * from gvdisvw_istaigos ist where ist.tipas=2 " +
			"and exists (select 1 from gvdis_ist_ter ter where ter.gvist_id=ist.id) " +
			"order by ist.pilnas_pavadinimas")	
			.addEntity(Istaiga.class)
			.list();
		return retVal;
	}
	
	/**
	 * Graþina atvykimo deklaracijø þurnalà pagal duotus parametrus 
	 * @param dataNuo - data nuo
	 * @param dataIki - data iki
	 * @param busena - busena ( 0 - visos, 1 - galiojanèios, 2 - nebegaliojanèios, 3 - nebaigtos ávesti)
	 * @param savivaldybe - savivaldybë
	 * @param seniunija - seniûnija
	 */
	// JornalAction gauna rezultata List'a
	public List getInDeklaracijos(HttpServletRequest req, Paging paging, Ordering ordering, 
			Date dataNuo, Date dataIki, int busena, Istaiga savivaldybe, Istaiga seniunija,String tipas)
	{
		String queryStr = "from AtvykimoDeklaracija ad where ad.deklaravimoData >= :dataNuo and ad.deklaravimoData <= :dataIki ";
		if (seniunija != null){
			queryStr += "and ad.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (ad.istaiga = :savivaldybe or ad.istaiga.istaiga = :savivaldybe) ";
		}
		if("1".equals(tipas)) queryStr += "and (ad.saltinis is null or ad.saltinis !=1)";
		if("2".equals(tipas)) queryStr += "and (ad.saltinis is not null or ad.saltinis = 1)";
		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		List retVal = query.list();

		int i = 0;
		
		for (Iterator it = retVal.iterator(); it.hasNext(); ){
			Deklaracija deklaracija = (Deklaracija)it.next();
			if (deklaracija.getCalcTipas() != busena && busena > 0){
				it.remove();
			}
			else {
	    		if (i<paging.getFirstItem() || i>=paging.getFirstItem()+paging.getPageSize()){
	    			it.remove();
	    		}
				i++;
			}
		}
/*		
		for (Iterator iter = retVal.iterator(); iter.hasNext();) {
			Deklaracija dekl = (Deklaracija) iter.next();
			try {
				DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(req, dekl);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		*/
		paging.setTotalNumber(i);
		return retVal.isEmpty() ? null : retVal;
	}
	
	/**
	 * Graþina iðvykimo deklaracijø þurnalà pagal duotus parametrus 
	 * @param dataNuo - data nuo
	 * @param dataIki - data iki
	 * @param busena - busena ( 0 - visos, 1 - galiojanèios, 2 - nebegaliojanèios, 3 - nebaigtos ávesti)
	 * @param savivaldybe - savivaldybë
	 * @param seniunija - seniûnija
	 */
	public List getOutDeklaracijos(HttpServletRequest req, Paging paging, Ordering ordering, 
			Date dataNuo, Date dataIki, int busena, Istaiga savivaldybe, Istaiga seniunija,String tipas)
	{
		String queryStr = "from IsvykimoDeklaracija id where id.deklaravimoData >= :dataNuo and id.deklaravimoData <= :dataIki ";
		if (seniunija != null){
			queryStr += "and id.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (id.istaiga = :savivaldybe or id.istaiga.istaiga = :savivaldybe) ";
		}
		if("1".equals(tipas)) queryStr += "and (id.saltinis is null or id.saltinis !=1)";
		if("2".equals(tipas)) queryStr += "and (id.saltinis is not null or id.saltinis = 1)";
		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		List retVal = query.list();
		
		int i = 0;
		for (Iterator it = retVal.iterator(); it.hasNext(); ){
			Deklaracija deklaracija = (Deklaracija)it.next();
			if (deklaracija.getCalcTipas() != busena && busena > 0){
				it.remove();
			}
			else {
	    		if (i<paging.getFirstItem() || i>=paging.getFirstItem()+paging.getPageSize()){
	    			it.remove();
	    		}
				i++;
			}
		}
/*		
		for (Iterator iter = retVal.iterator(); iter.hasNext();) {
			Deklaracija dekl = (Deklaracija) iter.next();
			try {
				DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(req, dekl); // I.N. 2010.01.25
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
		}
*/		
		paging.setTotalNumber(i);
		
		return retVal.isEmpty() ? null : retVal;
	}

	/**
	 * Graþina GVNA deklaracijø þurnalà pagal duotus parametrus 
	 * @param dataNuo - data nuo
	 * @param dataIki - data iki
	 * @param savivaldybe - savivaldybë
	 * @param seniunija - seniûnija
	 */
	public List getGvnaDeklaracijos(HttpServletRequest req, Paging paging, Ordering ordering, 
			Date dataNuo, Date dataIki, int busena, Istaiga savivaldybe, Istaiga seniunija,String tipas)
	{
		String queryStr = "from GvnaDeklaracija d where d.deklaravimoData >= :dataNuo and d.deklaravimoData <= :dataIki ";
		if (seniunija != null){
			queryStr += "and d.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (d.istaiga = :savivaldybe or d.istaiga.istaiga = :savivaldybe) ";
		}
		if("1".equals(tipas)) queryStr += "and (id.saltinis is null or id.saltinis !=1)";
		if("2".equals(tipas)) queryStr += "and (id.saltinis is not null or id.saltinis = 1)";
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			cntQuery.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			cntQuery.setEntity("savivaldybe", savivaldybe);
		}
		paging.setTotalNumber(((Integer)cntQuery.uniqueResult()).intValue());
		
		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		//query.setFirstResult(paging.getFirstItem());
		//query.setMaxResults(paging.getPageSize());
		List retVal = query.list();
		
		int i = 0;
		for (Iterator it = retVal.iterator(); it.hasNext(); ){
			Deklaracija deklaracija = (Deklaracija)it.next();
			if (deklaracija.getCalcTipas() != busena && busena > 0){
				it.remove();
			}
			else {
	    		if (i<paging.getFirstItem() || i>=paging.getFirstItem()+paging.getPageSize()){
	    			it.remove();
	    		}
				i++;
			}
		}
/*		
		for (Iterator iter = retVal.iterator(); iter.hasNext();) {
			Deklaracija dekl = (Deklaracija) iter.next();
			try {
				DeklaracijosDelegator.getInstance(request).updateDeklaracijosAsmenvardzius(req, dekl); // I.N. 2010.01.25
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
*/		
		paging.setTotalNumber(i);
		
		return retVal.isEmpty() ? null : retVal;
	}
	
	/**
	 * Graþina paþymø apie deklaruotà gyvenamàjà vietà þurnalà pagal duotus parametrus 
	 * @param dataNuo - data nuo
	 * @param dataIki - data iki
	 * @param savivaldybe - savivaldybë
	 * @param seniunija - seniûnija
	 */
	public List getGvPazymos(HttpServletRequest req, Paging paging, Ordering ordering, 
			Date dataNuo, Date dataIki, Istaiga savivaldybe, Istaiga seniunija)
	{
		String queryStr = "from GvPazyma paz where paz.pazymosData >= :dataNuo and paz.pazymosData <= :dataIki ";
		if (seniunija != null){
			queryStr += "and paz.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (paz.istaiga = :savivaldybe or paz.istaiga.istaiga = :savivaldybe) ";
		}
		
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			cntQuery.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			cntQuery.setEntity("savivaldybe", savivaldybe);
		}
		paging.setTotalNumber(((Integer)cntQuery.uniqueResult()).intValue());

		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		query.setFirstResult(paging.getFirstItem());
		query.setMaxResults(paging.getPageSize());
		List retVal = query.list();			
		return retVal.isEmpty() ? null : retVal;
	}

	/**
	 * Graþina paþymø apie átraukimà á GVNA apskaità þurnalà pagal duotus parametrus 
	 * @param dataNuo - data nuo
	 * @param dataIki - data iki
	 * @param savivaldybe - savivaldybë
	 * @param seniunija - seniûnija
	 */
	public List getGvnaPazymos(HttpServletRequest req, Paging paging, Ordering ordering, 
			Date dataNuo, Date dataIki, Istaiga savivaldybe, Istaiga seniunija)
	{
		String queryStr = "from GvnaPazyma paz where paz.pazymosData >= :dataNuo and paz.pazymosData <= :dataIki ";
		if (seniunija != null){
			queryStr += "and paz.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (paz.istaiga = :savivaldybe or paz.istaiga.istaiga = :savivaldybe) ";
		}
		
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			cntQuery.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			cntQuery.setEntity("savivaldybe", savivaldybe);
		}
		paging.setTotalNumber(((Integer)cntQuery.uniqueResult()).intValue());

		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		query.setFirstResult(paging.getFirstItem());
		query.setMaxResults(paging.getPageSize());
		List retVal = query.list();
		return retVal.isEmpty() ? null : retVal;
	}

	/**
	 * Graþina iðvykimo deklaracijø þurnalà pagal duotus parametrus 
	 * @param dataNuo - data nuo
	 * @param dataIki - data iki
	 * @param busena - busena ( 0 - visos, 1 - galiojanèios, 2 - nebegaliojanèios, 3 - nebaigtos ávesti)
	 * @param savivaldybe - savivaldybë
	 * @param seniunija - seniûnija
	 */
	public List getSprendimai(HttpServletRequest req, Paging paging, Ordering ordering, 
			Date dataNuo, Date dataIki, int tipas, Istaiga savivaldybe, Istaiga seniunija)
	{
		String queryStr = "from SprendimasKeistiDuomenis spr where spr.data >= :dataNuo and spr.data <= :dataIki ";
		if (seniunija != null){
			queryStr += "and spr.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (spr.istaiga = :savivaldybe or spr.istaiga.istaiga = :savivaldybe) ";
		}
		if (tipas >= 0){
			queryStr += "and spr.tipas = :tipas ";
		}
		
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			cntQuery.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			cntQuery.setEntity("savivaldybe", savivaldybe);
		}
		if (tipas >= 0){
			cntQuery.setInteger("tipas", tipas);
		}
		paging.setTotalNumber(((Integer)cntQuery.uniqueResult()).intValue());

		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		if (tipas >= 0){
			query.setInteger("tipas", tipas);
		}
		query.setFirstResult(paging.getFirstItem());
		query.setMaxResults(paging.getPageSize());
		List retVal = query.list();
		
		return retVal.isEmpty() ? null : retVal;
	}

	public List getSavPazymos(HttpServletRequest req, Paging paging, Ordering ordering, Date dataNuo, Date dataIki, Istaiga savivaldybe, Istaiga seniunija) {
		String queryStr = "from SavPazyma paz where paz.pazymosData >= :dataNuo and paz.pazymosData <= :dataIki ";
		if (seniunija != null){
			queryStr += "and paz.istaiga = :seniunija ";
		}
		else if (savivaldybe != null){
			queryStr += "and (paz.istaiga = :savivaldybe or paz.istaiga.istaiga = :savivaldybe) ";
		}
		
		Query cntQuery = HibernateUtils.currentSession(req).createQuery("select count(*) " + queryStr);
		cntQuery.setTimestamp("dataNuo", dataNuo);
		cntQuery.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			cntQuery.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			cntQuery.setEntity("savivaldybe", savivaldybe);
		}
		paging.setTotalNumber(((Integer)cntQuery.uniqueResult()).intValue());

		Query query = HibernateUtils.currentSession(req).createQuery(queryStr + ordering.getOrderString());
		query.setTimestamp("dataNuo", dataNuo);
		query.setTimestamp("dataIki", dataIki);
		if (seniunija != null){
			query.setEntity("seniunija", seniunija);
		}
		else if (savivaldybe != null){
			query.setEntity("savivaldybe", savivaldybe);
		}
		query.setFirstResult(paging.getFirstItem());
		query.setMaxResults(paging.getPageSize());
		List retVal = query.list();		
		if(!retVal.isEmpty()){
			for (Iterator iter = retVal.iterator(); iter.hasNext();) {
				SavPazyma element = (SavPazyma) iter.next();
				GyvenamojiVieta gvt = new GyvenamojiVieta();
				gvt.setGvtAdvNr(element.getGvtAdvNr());
				gvt.setGvtKampoNr(element.getGvtKampoNr());
				gvt.setGvtAtvNr(element.getGvtAtvNr());
				try {
					element.setCalcAdresas(QueryDelegator.getInstance().getAddressString(req, gvt));
				} catch (DatabaseException e) {					
					e.printStackTrace();
				}
				
			}
		}
		return retVal.isEmpty() ? null : retVal;
	}
}
