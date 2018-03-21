package com.algoritmusistemos.gvdis.web.delegators;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.JdbcUtils;
import oracle.jdbc.OracleTypes;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.actions.test.TestAsmuo2;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvnaPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;
import com.algoritmusistemos.gvdis.web.persistence.TestAsmuo;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.Version;


public class UserDelegator
{
	private static UserDelegator instance;
	
	public static final int USER_GLOBAL = 0;
	public static final int USER_SAV = 1;
	public static final int USER_SEN = 2;
	public static final int USER_UZS = 3;
	
	public static UserDelegator getInstance() 
	{
		if (instance == null){
			instance =  new UserDelegator();
		}
		return instance;
	}	

	/**
	 * Bando incializuoti vartotojà ðiame konkreèiame modulyje
	 * @param session - Hibernate sesija
	 * @param userName - Varototjo vardas
	 * @return - graþina sàraðà roliø pavadinimø, arba null, jei vartotojas roliø neturi
	 */
	private List initUserInModule(Session session, String userName)
	{
		List retVal = new ArrayList();
		String query = "{ call gvdis_user_privs.init_user_roles(?, ?, ?, ?, ?) }";
		try {
			Connection conn = session.connection(); 
    		CallableStatement stmt = conn.prepareCall(query);
    		stmt.setString(1, Constants.APPLICATION);
    		stmt.setString(2, userName.toUpperCase());    		
    		stmt.registerOutParameter(3, OracleTypes.VARCHAR);
    		stmt.registerOutParameter(4, OracleTypes.VARCHAR);    		
    		stmt.registerOutParameter(5, OracleTypes.VARCHAR);    		
    		stmt.execute();
    		String rolesList = stmt.getString(3);
    		if (null != rolesList){
    			retVal = getRolesSplitted(rolesList);
    		}
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return null;
		}
		return retVal;
	}
	
	/**
	 * Bando incializuoti vartotojà ðiame konkreèiame modulyje
	 * @param request - HttpServletRequest
	 * @param module - Modulis, kuriame bandoma inicializuoti vartotojà
	 * @param userName - Varototjo vardas
	 * @return - graþina sàraðà roliø pavadinimø, arba null, jei vartotojas roliø neturi
	 */
	public List initUserInModule(HttpServletRequest request, String userName)
	{
		List retVal = new ArrayList();
		String query = "{ call gvdis_user_privs.init_user_roles(?, ?, ?, ?, ?) }";
		try {
			Connection conn = HibernateUtils.currentSession(request).connection();
    		CallableStatement stmt = conn.prepareCall(query);
    		stmt.setString(1, Constants.APPLICATION);
    		stmt.setString(2, userName.toUpperCase());    		
    		stmt.registerOutParameter(3, OracleTypes.VARCHAR);
    		stmt.registerOutParameter(4, OracleTypes.VARCHAR);    		
    		stmt.registerOutParameter(5, OracleTypes.VARCHAR);    		
    		stmt.execute();
    		String rolesList = stmt.getString(3);
    		if (null != rolesList){
    			retVal = getRolesSplitted(rolesList);
    		}
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return null;
		}
		return retVal;
	}
	
	/**
	 * Graþina vartotojo turimø roliø sàraðà
	 * @param userName - vartotojo vardas
	 */
	public Set getUserRoles(HttpServletRequest request, String userName)
	{
		Set retVal = new HashSet();
		Session session = HibernateUtils.currentSession(request); 
		List rolesInModule = initUserInModule(session, userName);
		if (rolesInModule != null){
			for (Iterator it=rolesInModule.iterator(); it.hasNext(); ){
				String role = (String)it.next();
				if (!retVal.contains(role)){
					retVal.add(role);
				}
			}
		}
		return retVal;
	}

	/**
	 * Suskaido eilutæ per newline simbolius (\n) ir graþina sàraðà 
	 */
	public ArrayList getRolesSplitted(String rolesList)
		throws NoSuchElementException
	{
		ArrayList al = new ArrayList();
		StringTokenizer sti = new StringTokenizer(rolesList, "\n");
		while (sti.hasMoreTokens()) al.add(sti.nextToken());
		return al;
	}
	
	public Asmuo getAsmuoByAsmKodas(long asmKodas,HttpServletRequest request)
		throws ObjectNotFoundException, DatabaseException
	{
        Asmuo asmuo = null;
        //String qry = "{call gvdis_akts.get_asm_avd_aktualus(?,?,?,?,?,?,?,?) }"; I.N 2010.01.25
        String qry2 = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?,?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();	
        		
   		try {
   			/*CallableStatement stmt = conn.prepareCall(qry); I.N 2010.01.25
        		stmt.setLong(1, asmKodas);
        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
        		stmt.registerOutParameter(5, OracleTypes.DATE);        		
        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
        		stmt.execute();
        		
        		if ((stmt.getString(3) != null) && (stmt.getTimestamp(5) != null)) {
	        		asmuo = new Asmuo();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));
	        		asmuo.setVardas(stmt.getString(3));
	        		asmuo.setPavarde(stmt.getString(4));
	        		String log = ""; //"getByKodas: dbTT: " + stmt.getTimestamp(5).getTime() + " dbTN: " + stmt.getTimestamp(5).getNanos() + " dbTS: " + stmt.getTimestamp(5).toString();        		
	        		asmuo.setAsmGimData(stmt.getTimestamp(5));  
	        		//log += " inAsmDT: " + asmuo.gautiGimDate().getTime();
	        		//log += " asmTT: " + asmuo.getAsmGimData().getTime() + " asmTN: " + asmuo.getAsmGimData().getNanos() + " asmTS: " + asmuo.getAsmGimData().toString();
	        		asmuo.setAsmLytis(stmt.getString(6));
	        		asmuo.setPavardePrev(stmt.getString(7));
	        		asmuo.setAsmPilietybe(stmt.getString(8));
	        		request.getSession().setAttribute("log1", log);
	        		stmt.close();
        		}
        		
        		else {
        			stmt.close();
        			*/
   					CallableStatement stmt = conn.prepareCall(qry2);   			
        			stmt = conn.prepareCall(qry2);
            		stmt.setLong(1, asmKodas);
            		stmt.registerOutParameter(1, OracleTypes.NUMBER);
            		stmt.registerOutParameter(2, OracleTypes.NUMBER);
            		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
            		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
            		stmt.registerOutParameter(5, OracleTypes.DATE);        		
            		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
            		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
            		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
            		stmt.registerOutParameter(9, OracleTypes.NUMBER);
            		stmt.execute();
            		

    	        	asmuo = new Asmuo();
    	        	asmuo.setAsmKodas(new Long(stmt.getLong(1)));
    	        	asmuo.setAsmNr(stmt.getLong(2));
    	        	asmuo.setVardas(stmt.getString(3));
    	        	asmuo.setPavarde(stmt.getString(4));
    	        	String log = ""; /*"getByKodas: dbTT: " + stmt.getTimestamp(5).getTime() + " dbTN: " + stmt.getTimestamp(5).getNanos() +
    	        			" dbTS: " + stmt.getTimestamp(5).toString();   */     		
    	        	asmuo.setAsmGimData(stmt.getTimestamp(5));  
    	        	/*log += " inAsmDT: " + asmuo.gautiGimDate().getTime();
    	        	log += " asmTT: " + asmuo.getAsmGimData().getTime() + " asmTN: " + asmuo.getAsmGimData().getNanos() + " asmTS: " + asmuo.getAsmGimData().toString();*/
    	        	asmuo.setAsmLytis(stmt.getString(6));
    	        	asmuo.setPavardePrev(stmt.getString(7));
    	        	asmuo.setAsmPilietybe(stmt.getString(8));
    	        	asmuo.setAvdNr(stmt.getLong(9));
    	        	request.getSession().setAttribute("log1", log);
    	        	stmt.close();
        		
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   		}
   		return asmuo;
	}	
	
	/**
	 * Graþina ástaigà, kurioje dirba darbuotojas
	 * @param request - HTTP request'as
	 * @return - masyvà ið dviejø stringø: [0] - ástaigos ID, [1] - ástaigos pavadinimas 
	 * @throws DatabaseException - jei ávyko DB klaida
	 */
	public String[] getDarbIstaiga(HttpServletRequest request)
		throws DatabaseException
	{
        String[] ret = null;
        String qry = "{call gvdis_utils.get_darb_istaiga(?,?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();	
        		
   		try {
   			CallableStatement stmt = conn.prepareCall(qry);
   			stmt.registerOutParameter(1, OracleTypes.NUMBER);
   			stmt.registerOutParameter(2, OracleTypes.VARCHAR);
   			stmt.execute();
   			ret = new String[]{String.valueOf(stmt.getLong(1)),stmt.getString(2)};
   			stmt.close();
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}
        return ret;
	}		

	/**
	 * Graþina prisijungusio vartotojo vardà ir pavardæ
	 * @param request - HTTP request'as
	 * @return - masyvà ið dviejø stringø: [0] - vartotojo vardas, [1] - vartotojo pavardë 
	 * @throws DatabaseException - jei ávyko DB klaida
	 */
	public String[] getDarbuotojas(HttpServletRequest request)
		throws DatabaseException
	{
        String[] ret = null;
        String qry = "{call gvdis_utils.get_darbuotojas(?,?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();	
        		
   		try {
   			CallableStatement stmt = conn.prepareCall(qry);
   			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
   			stmt.registerOutParameter(2, OracleTypes.VARCHAR);
   			stmt.execute();
   			ret = new String[]{String.valueOf(stmt.getString(1)), String.valueOf(stmt.getString(2))};
   			stmt.close();
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}
        return ret;
	}		

	/**
	 * Patikrina, ar su duotais parametrais galima prisijungti prie DB
	 * @param username - DB vartotojo vardas
	 * @param password - DB vartotojo slaptaþodis
	 * @throws DatabaseException - jei prisijungti nepavyksta
	 */
	public void checkLoginParameters(String username, String password)
		throws DatabaseException
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			JdbcUtils utilai = new JdbcUtils();
			//DriverManager.getConnection(Constants.CONNECTION_STRING, username, password);
			Connection conn = DriverManager.getConnection(Constants.getDB(), username, password);
			utilai.markDisSession(conn);

		}
		catch (Exception e){
			throw new DatabaseException(e);
		}
	}
	
	/**
	 * Tikrina, ar vartotojui privalu pasikeisti slaptaþodá
	 * @param request
	 * @throws DatabaseException
	 */
	public boolean hasToChangePassword(HttpServletRequest request)
		throws DatabaseException
	{
        String ret = null;
        String qry = "{call gvdis_utils.check_password_validity(?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();	
        		
   		try {
   			CallableStatement stmt = conn.prepareCall(qry);
   			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
   			stmt.execute();
   			ret = stmt.getString(1);
   			stmt.close();
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}
        return ret == null ? false : true;
	}		

	/**
	 * Pakeièia aktyvaus vartotojo slaptaþodá á nurodytà
	 * @param request
	 * @param newPassword
	 */
	public void changePassword(HttpServletRequest request, String newPassword)
		throws DatabaseException
	{
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("userLogin");
        String ret = null;
        String qry = "{call gvdis_utils.set_user_password(?, ?, ?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();	
        		
   		try {
   			CallableStatement stmt = conn.prepareCall(qry);
   			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
   			stmt.setString(2, username);
   			stmt.setString(3, newPassword);
   			stmt.execute();
   			ret = stmt.getString(1);
   			stmt.close();
   		}
   		catch (SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}
   		if (ret != null){
   			throw new DatabaseException(ret);
   		}
	}
	
	/**
	 * Graþina aktyvaus vartotojo darbovietës bûsenà: 0 - globalus vartotojas, 
	 * 			1 - savivaldybës vartotojas, 2 - seniûnijos vartotojas
	 * @throws DatabaseException - jei ávyko DB klaida.
	 */
	public int getUserStatus(HttpServletRequest req)
		throws DatabaseException
	{
        String qry = "{ ? = call gvdis_utils.chk_vart_dirba() }";
   		Connection conn = HibernateUtils.currentSession(req).connection();	
        int ret = -1;
   		try {
   			CallableStatement stmt = conn.prepareCall(qry);
   			stmt.registerOutParameter(1, OracleTypes.INTEGER);
   			stmt.execute();
   			ret = stmt.getInt(1);
   			stmt.close();
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}
   		return ret;
	}
	/**
	 * Graþina ástaigos ID, kurioje ásidarbintas vartotojas 
	 * @throws DatabaseException - jei ávyko DB klaida.
	 */
	public long getTarKodas(long istid,HttpServletRequest req)
		throws DatabaseException
	{
        String qry = "{ ? = call gvdis_utils.get_tar_kodas(?) }";
   		Connection conn = HibernateUtils.currentSession(req).connection();	
        long ret = -1;
   		try {
   			CallableStatement stmt = conn.prepareCall(qry);
   			stmt.registerOutParameter(1, OracleTypes.INTEGER);
   			stmt.setLong(2,istid);
   			stmt.execute();
   			ret = stmt.getLong(1);
   			stmt.close();
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}
   		return ret;
	}	
	/**
	 * Graþina asmenys, deklaravæ gyvenamàjà vietà, bet neregistruoti Gyventojø registre
	 */
	public List getTempCitizens(HttpServletRequest req)
	{
		return HibernateUtils.currentSession(req).createQuery(
			"from LaikinasAsmuo la order by id").list();
	}	
	/**
	 * Graþina asmená, deklaravæ gyvenamàjà vietà, bet neregistruoti Gyventojø registre
	 */
	public LaikinasAsmuo getTempCitizen(long id,HttpServletRequest req)
	{
		return (LaikinasAsmuo)HibernateUtils.currentSession(req).createQuery(
			"from LaikinasAsmuo la where la.id = :id").setLong("id",id).uniqueResult();
	}	
	/**
	 * Naikina laikinà asmená
	 */
	public int DeleteTempCitizen(long id,HttpServletRequest req)
	throws ObjectNotFoundException,DatabaseException
	{
	    Session sess = HibernateUtils.currentSession(req);
	    Transaction tx = sess.beginTransaction();
	    try
	    {
		    LaikinasAsmuo la = (LaikinasAsmuo)sess.load(LaikinasAsmuo.class,new Long(id));
		    if(null == la) throw new ObjectNotFoundException("LaikinasAsmuo with id ["+id+"] not found");
		    
		    try{
		    	UserDelegator.checkPermission(req, "RL_GVDIS_GL_TVARK");
		    	GvnaPazyma pazyma = PazymosDelegator.getInstance().getGvnaPazymaByDeklaracija(req, la.getDeklaracija().getId());
		    	if (pazyma != null) {
					sess.delete(pazyma);
					sess.flush();
				}
		    } catch(PermissionDeniedException ex){ }
		   
		    if(-1 == DeklaracijosDelegator.getInstance(req).deleteDeclarationWithoutTransaction(req,la.getDeklaracija()))return -1;
		    sess.delete(la);
		    tx.commit();
	    }
	    catch (Exception e){
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e);
		}
	    return 0;
    }		
	
	/**
	 * Patikrina, ar dabar prisijungæs vartotojas turi bent vienà ið reikalingø roliø. 
	 * @param requiredRoles - masyvas roliø. Vartotojas turi turëti bent vienà ið jø tam, kad atlikti dabartiná veiksmà.
	 * @throws PermissionDeniedException - jei vartotojas neturi nei vienos reikalingos rolës
	 */
	public static void checkPermission(HttpServletRequest req, String [] requiredRoles)
		throws PermissionDeniedException
	{
		HttpSession session = req.getSession();
		Set roles = (Set)session.getAttribute("userRoles");
		String userName = (String)session.getAttribute("userLogin");
		boolean ok = false;
		for (int i = 0; i < requiredRoles.length; i++){
			if (roles.contains(requiredRoles[i])){
				ok = true;
			}
		}
		if (!ok){
			String msg = requiredRoles[0];
			for (int i = 1; i < requiredRoles.length; i++){
				msg += ", " + requiredRoles[i];
			}
			throw new PermissionDeniedException(msg, userName);
		}
	}

	public static void checkPermission(HttpServletRequest req, String requiredRole)
		throws PermissionDeniedException
	{
		String roles[] = new String[1];
		roles[0] = requiredRole;
		checkPermission(req, roles);
	}

	
	public  static boolean chkPermission(HttpServletRequest req, String requiredRole)
{
	String roles[] = new String[1];
	roles[0] = requiredRole;
	try {
		checkPermission(req, roles);
		return true;
	} catch (PermissionDeniedException e) {
		return false;
	}
	
}	
	
	public boolean isAsmuoInIstaiga(HttpServletRequest request, Asmuo asmuo, Long istId) {
		Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();	
		long s = 0;
		GyvenamojiVieta vt = null;
		boolean result = true;
		for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) {
			vt = (GyvenamojiVieta) it.next();
			if (vt.getGvtDataIki() == null) {
				try {
					String qry = "{? = call gvdis_utils.chk_ist_asm_gyvviet(?, ?, ?) }";
					Connection conn = HibernateUtils.currentSession(request).connection();
					CallableStatement stmt = conn.prepareCall(qry);
					stmt.registerOutParameter(1, OracleTypes.VARCHAR);
					if (istId != null && istId.longValue() > 0) {
						stmt.setLong(2, istId.longValue());
					} else {
						stmt.setNull(2, OracleTypes.NUMBER);
					}
					stmt.setLong(3, vt.getGvtAsmNr());
					stmt.setLong(4, vt.getGvtNr());
					stmt.execute();
					s = stmt.getLong(1);
					stmt.close();

					if (s == 0) {
						result = false;
					}
				} catch (SQLException sqlEx) {
					// throw new DatabaseException(sqlEx);
				}
			}
		}
		return result;
	}
	
	public Asmuo getAsmuoByAsmNr(long asmnr,HttpServletRequest req)
	{
		return (Asmuo)HibernateUtils.currentSession(req).createQuery(
			"from Asmuo a where a.asmNr = :asmnr").setLong("asmnr",asmnr).uniqueResult();
		//asmuo.updateADV...
	}

	public TestAsmuo2 getTestAsmuoByAsmKodasDateTimestamp(long asmKodas, HttpServletRequest request) {
		  TestAsmuo2 asmuo = null;
	        String qry = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?) }";
	   		Connection conn = HibernateUtils.currentSession(request).connection();	
	        		
	   		try {
	   			CallableStatement stmt = conn.prepareCall(qry);
	        		stmt.setLong(1, asmKodas);
	        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
	        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
	        		stmt.registerOutParameter(5, OracleTypes.DATE);        		
	        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
	        		stmt.execute();
	        		String log = "getByKodasDT: dbTT: " + stmt.getTimestamp(5).getTime() + " dbTN: " + stmt.getTimestamp(5).getNanos() +
					" dbTS: " + stmt.getTimestamp(5).toString();       
	        		asmuo = new TestAsmuo2();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));	        		
	        		asmuo.setAsmGimData(stmt.getTimestamp(5));	    
	        		log += " inAsmDT: " + asmuo.getAsmGimData().getTime();
	        		log += " asmDT: " + asmuo.getAsmGimData().getTime() + " asmDS: " + asmuo.getAsmGimData().toString();
	        		request.getSession().setAttribute("log4", log);
	                stmt.close();
	   		}
	   		catch(SQLException sqle){
	   			sqle.printStackTrace();
	   		}	        
	   		return asmuo;
	}	
	public TestAsmuo getTestAsmuoByAsmKodasTimestampTimestamp(long asmKodas, HttpServletRequest request) {
		  TestAsmuo asmuo = null;
	        String qry = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?) }";
	   		Connection conn = HibernateUtils.currentSession(request).connection();	
	        		
	   		try {
	   			CallableStatement stmt = conn.prepareCall(qry);
	        		stmt.setLong(1, asmKodas);
	        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
	        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
	        		stmt.registerOutParameter(5, OracleTypes.TIMESTAMP);        		
	        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
	        		stmt.execute();
	        		String log = "getByKodasTT: dbTT: " + stmt.getTimestamp(5).getTime() + " dbTN: " + stmt.getTimestamp(5).getNanos() +
    					" dbTS: " + stmt.getTimestamp(5).toString();       
	        		asmuo = new TestAsmuo();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));	        	
	        		asmuo.setAsmGimData(new java.sql.Date(stmt.getTimestamp(5).getTime()));
	        		log += " inAsmDT: " + asmuo.getAsmGimData().getTime();
	        		log += " asmDT: " + asmuo.getAsmGimData().getTime() + " asmDS: " + asmuo.getAsmGimData().toString();
	        		request.getSession().setAttribute("log3", log);
	        		stmt.close();
	   		}
	   		catch(SQLException sqle){
	   			sqle.printStackTrace();
	   		}	        
	   		return asmuo;
	}
	public Asmuo getTestAsmuoByAsmKodasDateDate(long asmKodas, HttpServletRequest request) {
		  Asmuo asmuo = null;
	        String qry = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?) }";
	   		Connection conn = HibernateUtils.currentSession(request).connection();	
	        		
	   		try {
	   			CallableStatement stmt = conn.prepareCall(qry);
	        		stmt.setLong(1, asmKodas);
	        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
	        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
	        		stmt.registerOutParameter(5, OracleTypes.DATE);        		
	        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
	        		stmt.execute();
	        		String log = "getByKodasDD: dbDT: " + stmt.getDate(5).getTime() + " dbDS: " + stmt.getDate(5).toString();       
	        		asmuo = new Asmuo();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));	        		
	        		asmuo.setAsmGimData(stmt.getDate(5));	 
	        		log += " inAsmDT: " + asmuo.gautiGimDate().getTime();
	        		log += " asmTT: " + asmuo.getAsmGimData().getTime() + " asmTN: " + asmuo.getAsmGimData().getNanos() + " asmTS: " + asmuo.getAsmGimData().toString();
	        		request.getSession().setAttribute("log2", log);
	        		stmt.close();
	   		}
	   		catch(SQLException sqle){
	   			sqle.printStackTrace();
	   		}	        
	   		return asmuo;
	}
	public TestAsmuo2 getTestAsmuoByAsmKodasDateDate2(long asmKodas, HttpServletRequest request) {
		TestAsmuo2 asmuo = null;
	        String qry = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?) }";
	   		Connection conn = HibernateUtils.currentSession(request).connection();	
	        		
	   		try {
	   			CallableStatement stmt = conn.prepareCall(qry);
	        		stmt.setLong(1, asmKodas);
	        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
	        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
	        		stmt.registerOutParameter(5, OracleTypes.DATE);        		
	        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
	        		stmt.execute();
	        		String log = "getByKodasDD2: dbDT: " + stmt.getDate(5).getTime() + " dbDS: " + stmt.getDate(5).toString();       
	        		asmuo = new TestAsmuo2();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));	        		
	        		asmuo.setAsmGimData(stmt.getDate(5));		        		
	        		log += " asmDT: " + asmuo.getAsmGimData().getTime() + " asmDS: " + asmuo.getAsmGimData().toString();
	        		request.getSession().setAttribute("log5", log);
	        		stmt.close();
	   		}
	   		catch(SQLException sqle){
	   			sqle.printStackTrace();
	   		}	        
	   		return asmuo;
	}
	public Asmuo getTestAsmuoByAsmKodasTimestampTimestamp2(long asmKodas, HttpServletRequest request) {
		  Asmuo asmuo = null;
	        String qry = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?) }";
	   		Connection conn = HibernateUtils.currentSession(request).connection();	
	        		
	   		try {
	   			CallableStatement stmt = conn.prepareCall(qry);
	        		stmt.setLong(1, asmKodas);
	        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
	        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
	        		stmt.registerOutParameter(5, OracleTypes.TIMESTAMP);        		
	        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
	        		stmt.execute();
	        		String log = "getByKodasTT2: dbTT: " + stmt.getTimestamp(5).getTime() + " dbTN: " + stmt.getTimestamp(5).getNanos() +
  					" dbTS: " + stmt.getTimestamp(5).toString();       
	        		asmuo = new Asmuo();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));	        	
	        		asmuo.setAsmGimData(stmt.getTimestamp(5));
	        		log += " inAsmDT: " + asmuo.gautiGimDate().getTime();
	        		log += " asmTT: " + asmuo.getAsmGimData().getTime() + " asmTN: " +asmuo.getAsmGimData().getNanos()+" asmTS: " + asmuo.getAsmGimData().toString();
	        		request.getSession().setAttribute("log6", log);
	        		stmt.close();
	   		}
	   		catch(SQLException sqle){
	   			sqle.printStackTrace();
	   		}	        
	   		return asmuo;
	}
	public Asmuo getTestAsmuoByAsmKodasTimestampTimestamp3(long asmKodas, HttpServletRequest request) {
		  Asmuo asmuo = null;
	        String qry = "{call gvdis_akts.get_asm_asmenvardis(?,?,?,?,?,?,?,?) }";
	   		Connection conn = HibernateUtils.currentSession(request).connection();	
	        		
	   		try {
	   			CallableStatement stmt = conn.prepareCall(qry);
	        		stmt.setLong(1, asmKodas);
	        		stmt.registerOutParameter(1, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(2, OracleTypes.NUMBER);
	        		stmt.registerOutParameter(3, OracleTypes.VARCHAR);  
	        		stmt.registerOutParameter(4, OracleTypes.VARCHAR);        		
	        		stmt.registerOutParameter(5, OracleTypes.TIMESTAMP);        		
	        		stmt.registerOutParameter(6, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(7, OracleTypes.VARCHAR);
	        		stmt.registerOutParameter(8, OracleTypes.VARCHAR);
	        		stmt.execute();
	        		String log = "getByKodasTT3: dbTT: " + stmt.getTimestamp(5).getTime() + " dbTN: " + stmt.getTimestamp(5).getNanos() +
					" dbTS: " + stmt.getTimestamp(5).toString();       
	        		asmuo = new Asmuo();
	        		asmuo.setAsmKodas(new Long(stmt.getLong(1)));
	        		asmuo.setAsmNr(stmt.getLong(2));	        	
	        		asmuo.setAsmGimData(new Date(stmt.getTimestamp(5).getTime()));
	        		log += " inAsmDT: " + asmuo.gautiGimDate().getTime();
	        		log += " asmTT: " + asmuo.getAsmGimData().getTime() + " asmTN: " +asmuo.getAsmGimData().getNanos()+" asmTS: " + asmuo.getAsmGimData().toString();
	        		request.getSession().setAttribute("log7", log);
	        		stmt.close();
	   		}
	   		catch(SQLException sqle){
	   			sqle.printStackTrace();
	   		}	        
	   		return asmuo;
	}


	/**
	 * Tikrina, ar aplikacijos versija sutampa su DB versija
	 */
	public boolean chkVersija(String login,String password)
		throws DatabaseException
	{
		if (!Version.CHKVERSION) {return true;}
        int ret = 0;
        String qry = "{? = call gvdis_utils.Chk_Versija(?) }";
        try {
        	//Connection conn = DriverManager.getConnection(Constants.CONNECTION_STRING, login, password);
        	Connection conn = DriverManager.getConnection(Constants.getDB(), login, password);
        	try {
       			CallableStatement stmt = conn.prepareCall(qry);
       			stmt.registerOutParameter(1, OracleTypes.INTEGER);
       			stmt.setString(2, Version.VERSION);
       			stmt.execute();
       			ret = stmt.getInt(1);
       			stmt.close();
       		}
       		catch(SQLException sqle){
       			sqle.printStackTrace();
       			throw new DatabaseException(sqle);
       		}        
        }
		catch (Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
        return ret == 0 ? false : true;
	}


	/**
	 * Tikrina, ar aplikacijos versija sutampa su DB versija
	 */
	public boolean chkVersija(HttpServletRequest request)
		throws DatabaseException
	{
		if (!Version.CHKVERSION) {return true;}

		int ret = 0;
        String qry = "{? = call gvdis_utils.Chk_Versija(?) }";
       	Connection conn = HibernateUtils.currentSession(request).connection();
       	
       	try {
       		
   			CallableStatement stmt = conn.prepareCall(qry);
      		stmt.registerOutParameter(1, OracleTypes.INTEGER);
   			stmt.setString(2, Version.VERSION);
   			stmt.execute();
   			ret = stmt.getInt(1);
   			stmt.close();
   		}
   		catch(SQLException sqle){
   			sqle.printStackTrace();
   			throw new DatabaseException(sqle);
   		}        
        return ret == 0 ? false : true;
	}

}

