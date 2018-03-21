package lt.solver.gvdis.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

import lt.solver.gvdis.model.AsmuoStatusas;
import lt.solver.gvdis.model.PilietybesObj;


public class DeklaracijosDAL {
	
	private static DeklaracijosDAL instance;
	
	public static DeklaracijosDAL getInstance()
	{
		if (instance == null){
			instance =  new DeklaracijosDAL();
		}
		return instance;
	}
	
	public PilietybesObj getAsmensGRPilietybe(HttpServletRequest req, long asmNr) throws DatabaseException {
		PilietybesObj retVal = new PilietybesObj();
		//p_asm_nr in number,p_vls_kodas out varchar2,p_vls_pav out varchar2
		String qry = "{ call GVDIS3_QUERY.get_asmens_pilietybe(?,?,?) }";
		try{
	        Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.setLong(1, asmNr);
	        stmt.registerOutParameter(2, OracleTypes.VARCHAR);
	        stmt.registerOutParameter(3, OracleTypes.VARCHAR);

	        stmt.execute();
	        
	        retVal.setKodas(stmt.getString(2));
	        retVal.setPilietybe(stmt.getString(3));
	        
	        stmt.close();
	        
			
        } catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
			throw new DatabaseException(sqlEx);
        }
        
		return retVal;
		
	}
	
	public int getAsmuoStatusas(HttpServletRequest req, String asmKodas)
		throws DatabaseException
	{
		String qry = "{ ? = call GVDIS3_UTILS.ASMENS_STATUSAS(?) }";
		try{
	        Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.registerOutParameter(1, OracleTypes.VARCHAR);
	      	stmt.setString(2, asmKodas);
	        stmt.execute();
	        
	        String asmStatus = stmt.getString(1);
	        if (asmStatus == null || asmStatus.length() <= 0)
	        	throw new DatabaseException("DB ivyko klaida");

			stmt.close();
			int st = AsmuoStatusas.NA;
			if (asmStatus.equalsIgnoreCase(Constants.ASMUO_STATUS_NA))
				st = AsmuoStatusas.NA;
			if (asmStatus.equalsIgnoreCase(Constants.ASMUO_STATUS_DEKLARUOTAS))
				st = AsmuoStatusas.DEKLARUOTAS;
			if (asmStatus.equalsIgnoreCase(Constants.ASMUO_STATUS_GVNA))
				st = AsmuoStatusas.GVNA;
			if (asmStatus.equalsIgnoreCase(Constants.ASMUO_STATUS_ISVYKES))
				st = AsmuoStatusas.ISVYKES;
			if (asmStatus.equalsIgnoreCase(Constants.ASMUO_STATUS_MIRES))
				st = AsmuoStatusas.MIRES;
			
			return st;
			
        } catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
			throw new DatabaseException(sqlEx);
        }
		
	}
}
