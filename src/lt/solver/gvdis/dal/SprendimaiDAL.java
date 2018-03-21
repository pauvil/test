package lt.solver.gvdis.dal;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class SprendimaiDAL {

	private static SprendimaiDAL instance;
	
	public static SprendimaiDAL getInstance() {
		if (instance == null){
			instance = new SprendimaiDAL();
		}
		return instance;
	}
	
	/*
	 * Patikrinama ar prasymas nera uzimtas sprendimo
	 * true- prasymas turi sprendima
	 * false- visais kitais atvejais (nera tokio prasymo, prasymas 'neuzimatas' ir t.t.)
	 */
	public boolean checkPrasymaiUsage(HttpServletRequest req, long prasymaiId) {
		boolean retValue = false;
		String qry = "begin GVDIS3_QUERY.checkPrasymaiUsage (?,?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.setLong(1, prasymaiId); 
	        stmt.registerOutParameter(2, OracleTypes.VARCHAR);
	        stmt.execute();
	        
	        String dbRet = stmt.getString(2);
	        if (dbRet != null) {
	        	if (dbRet.equalsIgnoreCase("T")) {
	        		retValue = true; //Prasymas uzimtas!
	        	}
	        }
	        
		} catch(Exception sqlEx) {
        	sqlEx.printStackTrace();
        }
		
		return retValue;
	}
	
	/*
	 * Patikrinama ar sprendimuose yra regNr dubliu
	 * true- dubliu yra
	 */
	public boolean checkDoubleSprendimai(HttpServletRequest req, String regNr) {
		boolean retValue = false;
		
		String qry = "begin GVDIS3_QUERY.CHECKSPRENDIMAIPAZNR (?,?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.setString(1, regNr); 
	        stmt.registerOutParameter(2, OracleTypes.VARCHAR);
	        stmt.execute();
	        
	        String dbRet = stmt.getString(2);
	        if (dbRet != null) {
	        	if (dbRet.equalsIgnoreCase("T")) {
	        		retValue = true; //dubliu yra!
	        	}
	        }
	        
		} catch(Exception sqlEx) {
        	sqlEx.printStackTrace();
        }
		
		return retValue;
	}

	
}
