package com.algoritmusistemos.gvdis.web.delegators;

import gnu.regexp.RE;
import gnu.regexp.REMatch;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GvnaPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SavPazyma;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class PazymosDelegator 
{
	private static PazymosDelegator instance;
	
	public static PazymosDelegator getInstance() 
	{
		if (instance == null){
			instance =  new PazymosDelegator();
		}
		return instance;
	}
	
    /**
     * Graþina GVNA paþymà su duotu ID
     */
    public GvnaPazyma getGvnaPazyma(HttpServletRequest req, long id)
    {
    	return (GvnaPazyma)HibernateUtils.currentSession(req).load(GvnaPazyma.class, new Long(id));
    }

    /**
     * Graþina GV paþymà su duotu ID
     */
    public GvPazyma getGvPazyma(HttpServletRequest req, long id)
    {
    	return (GvPazyma)HibernateUtils.currentSession(req).load(GvPazyma.class, new Long(id));
    }
    public GvPazyma getGvPazyma2(HttpServletRequest req, long id)
    {
    	Object obj = HibernateUtils.currentSession(req).createQuery("from GvPazyma gp where gp.id=:id").setLong("id", id).uniqueResult();
    	return (GvPazyma) obj;
    	
    }

    /**
	 * Graþina gyvenamàjà vietà, pagal kurià galima iðduoti asmeniui paþymà apie átraukimà á GVNA apskaità 
	 * @param asmuo - asmuo, kuro duomenis tikrinsim
	 * @param data - data, kuriai formuojama paþyma
	 */
	public GyvenamojiVieta getGvnaGyvVieta(HttpServletRequest request, Asmuo asmuo, Date data)
	{
		List l = HibernateUtils.currentSession(request).createQuery(
			"from GyvenamojiVieta where gvtAsmNr=:asmNr and gvtTipas='K' and gvtDataNuo <= :dataNuo and nvl(gvtDataIki, sysdate) >= :dataIki order by gvtNr desc")
			.setLong("asmNr", asmuo.getAsmNr())
			.setTimestamp("dataNuo", new Timestamp(data.getTime() + 60000*(59+23*60)))
			.setTimestamp("dataIki", new Timestamp(data.getTime()))
			.list();
		GyvenamojiVieta gvt = null;
		if (!l.isEmpty()){
			gvt = (GyvenamojiVieta)l.iterator().next();
		}
		return gvt;
	}

    /**
	 * Graþina gyvenamàjà vietà, pagal kurià galima iðduoti asmeniui paþymà apie deklaruotà GV 
	 * @param asmuo - asmuo, kuro duomenis tikrinsim
	 * @param data - data, kuriai formuojama paþyma
	 */
	public GyvenamojiVieta getGvGyvVieta(HttpServletRequest request, Asmuo asmuo, Date data, String tipas)
	{
		List l = HibernateUtils.currentSession(request).createQuery(
			"from GyvenamojiVieta where gvtAsmNr=:asmNr and gvtTipas= :tipas and gvtDataNuo <= :dataNuo and nvl(gvtDataIki, sysdate) >= :dataIki order by gvtNr desc")
			.setLong("asmNr", asmuo.getAsmNr())
			.setString("tipas", tipas)
			.setTimestamp("dataNuo", new Timestamp(data.getTime() + 60000*(59+23*60)))
			.setTimestamp("dataIki", new Timestamp(data.getTime()))
			.list();
		GyvenamojiVieta gvt = null;
		if (!l.isEmpty()){
			gvt = (GyvenamojiVieta)l.iterator().next();
		}	
				
		return gvt;
	}

	public void removeGvPazymaByGVT(HttpServletRequest request, GyvenamojiVieta gvt){
		
		Session sess = HibernateUtils.currentSession(request);
		List list = sess.createQuery("from GvPazyma where gyvenamojiVieta.gvtNr = :gvtnr and gyvenamojiVieta.gvtAsmNr = :gvtasmnr")		
		.setLong("gvtnr", gvt.getGvtNr())
		.setLong("gvtasmnr", gvt.getGvtAsmNr())
		.list();
		
		ListIterator liter = list.listIterator();
		while(liter.hasNext()){
			sess.delete(liter.next());
		}		
	}
	
	public void removeGVNAPazymaByGVT(HttpServletRequest request, GyvenamojiVieta gvt)throws DatabaseException,SQLException{		
		Session sess = HibernateUtils.currentSession(request);

		PreparedStatement stmt = sess.connection().prepareStatement(
				"delete from gvdis_gvna_pazymos where GR_GVT_NR = ? " + "and GR_GVT_ASM_NR = ?");
		stmt.setLong(1, gvt.getGvtNr());
		stmt.setLong(2, gvt.getGvtAsmNr());
		stmt.executeUpdate();
		
//		sess.createQuery("delete from gvdis_gvna_pazymos where GR_GVT_NR = :gvtnr and GR_GVT_ASM_NR = :gvtasmnr")		
//		.setLong("gvtnr", gvt.getGvtNr())
//		.setLong("gvtasmnr", gvt.getGvtAsmNr())
//		.executeUpdate();
		
//		ListIterator liter = list.listIterator();
//		while(liter.hasNext()){
//			sess.delete(liter.next());
//		}		
	}
	
	public GvnaPazyma getGvnaPazymaByDeklaracija(HttpServletRequest request, long deklId){
		return (GvnaPazyma) HibernateUtils.currentSession(request).createQuery("from GvnaPazyma where deklaracija=:id")
		.setLong("id", deklId).uniqueResult();		
	}
	
	public GyvenamojiVieta getMireGyvVieta(HttpServletRequest request, Asmuo asmuo)
	{
		List l = HibernateUtils.currentSession(request).createQuery(
			"from GyvenamojiVieta where gvtAsmNr=:asmNr and (gvtTipas='R' or gvtTipas='K' or (gvtTipas in ('C','D', 'G', 'H', 'I', 'S', 'Z')" +
			" and ((gvtAdvNr is not null) or gvtAtvNr is not null) )) order by gvtNr desc")
			.setLong("asmNr", asmuo.getAsmNr()).list();
		GyvenamojiVieta gvt = null;		
		if (!l.isEmpty()){
			gvt = (GyvenamojiVieta)l.iterator().next();
		}
		return gvt;
	}
	/**
	 * Graþina sàraðà asmens vaikø, kurie duotai datai buvo deklaravæ gyvenamàjà vietà tame paèiame adrese  
	 * @param tevas - asmuo, kurio vaikus norime gauti
	 * @param data - data, kuriai formuojama paþyma
	 * @param gvt - tëvo gyvenamoji vieta
	 */
	public List getVaikai(HttpServletRequest request, Asmuo tevas, Date data, GyvenamojiVieta gvt)
		throws ObjectNotFoundException, DatabaseException
	{
		List retVal = new ArrayList();
		try {
            String qry = "{ ? = call gvdis_akts.get_asm_childrens(?, ?) }";
			Connection conn = HibernateUtils.currentSession(request).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setLong(2, tevas.getAsmNr());
			stmt.setTimestamp(3, new Timestamp(data.getTime()));
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
            	long id = rs.getLong("asm_nr");
            	retVal.add(QueryDelegator.getInstance().getAsmuoByAsmNr(request, id));
            }
            rs.close();
            stmt.close();
		}
		catch (SQLException sqlEx){
			throw new DatabaseException(sqlEx);
		}
		return retVal;
	}

	/**
     * Graï¿½ina naujï¿½ registracijos numerï¿½ pagal nutylï¿½jimà naujai kuriamai paï¿½ymai   
     */
	public String getDefaultRegNr(HttpServletRequest req, Session session, String type, String trumpinys,boolean internetu){
		String istaiga = String.valueOf(req.getSession().getAttribute("userIstaigaId"));
		return getDefaultRegNr(session, type, trumpinys,internetu, istaiga);
	}

    /**
     * Graï¿½ina naujï¿½ registracijos numerï¿½ pagal nutylï¿½jimï¿½ naujai kuriamai paï¿½ymai   
     */		
    String getDefaultRegNr(Session session, String type, String trumpinys,boolean internetu, String istaiga)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");    
    	
    	//>>ankstesnio reg nr formato esamo didþiausio skaitliuko nustatymui ju.k 2007.08.14
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
    	
    	String regPrefix = "(" + sdf.format(new Date()) + "-" + istaiga + "-" + trumpinys + ")-";
    	int maxNumber = maxNumber_tmp;// + 1;//0; ju.k 2007.08.14, laikinam nustatymui 2007 metams, po to gali buti 0
    	
    	String qry = "{call gvdis_security.isjungti_matomuma()}";
    	CallableStatement stmt;
		try {
			stmt = session.connection().prepareCall(qry);
			stmt.execute();	
	        stmt.close();
		} catch (HibernateException e1) {e1.printStackTrace();} 
		  catch (SQLException e1) {e1.printStackTrace();}		
    	
    	List l = session.createQuery("select regNr from " + type + " where regNr like '" + regPrefix + "%'").list();
    	
    	qry = "{call gvdis_security.ijungti_matomuma()}";    	
		try {
			stmt = session.connection().prepareCall(qry);
			stmt.execute();	
	        stmt.close();
		} catch (HibernateException e1) {e1.printStackTrace();} 
		  catch (SQLException e1) {e1.printStackTrace();}			
    	
    	for (Iterator it=l.iterator(); it.hasNext(); ){
    		String regNr = (String)it.next();
    		try {
    	    	//RE pattern = new RE("[0-9]*-[0-9]*-([0-9]*)"); //kom ju.k
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
    	return regPrefix + String.valueOf(maxNumber + 1)+(internetu?"E":"");
    }

    /**
	 * Iðsaugo duotà paþymà duomenø bazëje
	 * @throws DatabaseException - jei nepavyko iðsaugoti
	 */
	public void saveGvnaPazyma(HttpServletRequest req, GvnaPazyma pazyma)
		throws DatabaseException
	{
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	if (pazyma.getRegNr() == null || "".equals(pazyma.getRegNr())){
    		pazyma.setRegNr(getDefaultRegNr(req, session, "GvnaPazyma","PN",false)); //ju.k 2007.08.13
    	}
    	try {
    		session.save(pazyma);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
	}
	
    /**
	 * Iðsaugo duotà paþymà duomenø bazëje
	 * @throws DatabaseException - jei nepavyko iðsaugoti
	 */
	public void saveGvPazyma(HttpServletRequest req, GvPazyma pazyma)
		throws DatabaseException
	{
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	if (pazyma.getRegNr() == null || "".equals(pazyma.getRegNr())){
    		pazyma.setRegNr(getDefaultRegNr(req, session, "GvPazyma", "PD",false)); //ju.k 2007.08.13
    	}
    	try {
    		session.save(pazyma);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
	}

	public void saveSavininkoPazyma(HttpServletRequest req, SavPazyma pazyma) throws DatabaseException{
		Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	if (pazyma.getRegNr() == null || "".equals(pazyma.getRegNr())){
    		pazyma.setRegNr(getDefaultRegNr(req, session, "SavPazyma", "PS",false)); //ju.k 2007.08.13
    	}
    	try {
    		session.save(pazyma);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
		
	}

	public SavPazyma getSavPazyma(HttpServletRequest req, long id) {
		return (SavPazyma)HibernateUtils.currentSession(req).load(SavPazyma.class, new Long(id));
	}
	
	/**
	 * Patikrinama ar asmeniui siandien yra isduota pazyma apie deklaruota gyvenamaja vieta.
	 * Pazyma apie deklaruota gyvenamaja vieta.
	 */
	public boolean getSiosDienosGvPazyma (HttpServletRequest req, long gvtAsmNr, int itrauktiVaikus , Date pazymosData) throws DatabaseException {
		boolean retVal = false;
		String tmpVal = "N";
        try {
        	String qry = "{ ? = call gvdis3_utils.getSiosDienosGvPazyma(?,?,?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
			stmt.setLong(2, gvtAsmNr);
			stmt.setInt(3, itrauktiVaikus);
			stmt.setTimestamp(4, new Timestamp(pazymosData.getTime()));
			stmt.execute();
			tmpVal = stmt.getString(1);
			System.out.println("tmpVal: " + tmpVal);
			stmt.close();
			
			if("T".equals(tmpVal)){
				retVal = true;
			}
			else{
				retVal = false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
			
		}
		return retVal;
	}
	/**
	 * Patikrinama ar asmeniui siandien yra isduota pazyma apie deklaruota gyvenamaja vieta.
	 * Pazyma apie itraukima i GVNA apskaita.
	 */
	public boolean getSiosDienosGvnaPazyma (HttpServletRequest req, long gvtAsmNr, int itrauktiVaikus, Date pazymosData) throws DatabaseException {
		boolean retVal = false;
		String tmpVal = "N";
        try {
        	String qry = "{ ? = call gvdis3_utils.getSiosDienosGvnaPazyma(?,?,?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
			stmt.setLong(2, gvtAsmNr);
			stmt.setInt(3, itrauktiVaikus);
			stmt.setTimestamp(4, new Timestamp(pazymosData.getTime()));
			stmt.execute();
			tmpVal = stmt.getString(1);
			System.out.println("tmpVal: " + tmpVal);
			stmt.close();
			
			if("T".equals(tmpVal)){
				retVal = true;
			}
			else{
				retVal = false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
			
		}
		return retVal;
	}
	
	/**
	 * Patikrinama ar asmeniui siandien yra isduota pazyma apie deklaruota gyvenamaja vieta.
	 * Pazyma patalpu savininkams. 
	 */
	public boolean getSiosDienosSavPazyma (HttpServletRequest req, long gvtAdvNr, Date pazymosData) throws DatabaseException {
		boolean retVal = false;
		String tmpVal = "N";
        try {
        	String qry = "{ ? = call gvdis3_utils.getSiosDienosSavPazyma(?,?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
			stmt.setLong(2, gvtAdvNr);
			stmt.setTimestamp(3, new Timestamp(pazymosData.getTime()));
			stmt.execute();
			tmpVal = stmt.getString(1);
			System.out.println("tmpVal: " + tmpVal);
			stmt.close();
			
			if("T".equals(tmpVal)){
				retVal = true;
			}
			else{
				retVal = false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
			
		}
		return retVal;
	}

}
