package lt.solver.gvdis.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lt.solver.gvdis.model.IdValueBean;
import lt.solver.gvdis.model.PilietybesObj;
import lt.solver.gvdis.model.ValstybeObj;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class CommonObjDAL {
	
	private static CommonObjDAL instance;
	
	public static CommonObjDAL getInstance() {
		if (instance == null){
			instance = new CommonObjDAL();
		}
		return instance;
	}
	
	/**
	 * Grazinamos teritorijos kurioms priklauso ter_nr
	 */
	public List getTeritorijos(HttpServletRequest req, Long ter_nr) {
		List retVal = new ArrayList();

		String qry = "begin GVDIS3_QUERY.GET_TERITORIJOS (?,?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        if (ter_nr == null) {
	        	stmt.setNull(1, OracleTypes.NUMBER);
	        } else {
	        	stmt.setLong(1, ter_nr.longValue()); 
	        }
	        
	        stmt.registerOutParameter(2, OracleTypes.CURSOR);
	        stmt.execute();
	        
	        ResultSet rs = (ResultSet)stmt.getObject(2);
	        if (rs == null) return retVal;

	        while(rs.next()){
	        	IdValueBean ter = new IdValueBean(rs.getString("TEL_TER_NR"), rs.getString("teritorija"));
	        	retVal.add(ter);
	        }
	        rs.close();
	        stmt.close();
	        
		} catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
        	retVal = new ArrayList();
        }
		
		return retVal;
	}
	
	/**
	 * Graþinamas visu pilietybiu sàraðas
	 */
	public List getPilietybes(HttpServletRequest req) {
		List retVal = new ArrayList();

		String qry = "begin GVDIS3_QUERY.GET_PILIETYBES (?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.registerOutParameter(1, OracleTypes.CURSOR);
	        stmt.execute();
	        
	        ResultSet rs = (ResultSet)stmt.getObject(1);
	        if (rs == null) return retVal;
	        
	        while(rs.next()){
	        	PilietybesObj pil = new PilietybesObj();
	        	pil.setKodas(rs.getString("VLST_KODAS"));
	        	pil.setPilietybe("PILIETYBE");
	        	retVal.add(pil);
	        }
	        rs.close();
	        stmt.close();
	        
		} catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
        	retVal = new ArrayList();
        }
		
		return retVal;
	}
	
	
	/**
	 * Graþinamas valstybiu sàraðas
	 */
	public List getValstybes(HttpServletRequest req) {
		List retVal = new ArrayList();

		String qry = "begin GVDIS3_QUERY.GET_VALSTYBES (?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.registerOutParameter(1, OracleTypes.CURSOR);
	        stmt.execute();
	        
	        ResultSet rs = (ResultSet)stmt.getObject(1);
	        if (rs == null) return retVal;
	        
	        while(rs.next()){
	        	ValstybeObj vlst = new ValstybeObj();
	        	vlst.setKodas(rs.getString("VLST_KODAS"));
	        	vlst.setPavadinimas(rs.getString("VLST_PAVADINIMAS"));
	        	retVal.add(vlst);
	        }
	        rs.close();
	        stmt.close();
	        
		} catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
        	retVal = new ArrayList();
        }
		
		return retVal;
	}
	
	/**
	 * Graþina visø registruotø savivaldybiø sàraðà
	 */
	public List getSavivaldybes(HttpServletRequest req) {
		List retVal = new ArrayList();

		String qry = "begin GVDIS3_JOURNAL.GET_SAVIVALDYBES (?,?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.setLong(1, 0); //visa sarasa
	        stmt.registerOutParameter(2, OracleTypes.CURSOR);
	        stmt.execute();
	        
	        ResultSet rs = (ResultSet)stmt.getObject(2);
	        if (rs == null) return retVal;
	        retVal = new ArrayList();
	        while(rs.next()){
	        	Istaiga ist = new Istaiga();
	        	ist.setId(rs.getLong("ID"));
	        	ist.setTipas(rs.getLong("TIPAS"));
	        	ist.setPavadinimas(rs.getString("PAVADINIMAS"));
	        	ist.setPilnasPavadinimas(rs.getString("PILNAS_PAVADINIMAS"));
	        	ist.setOficialusPavadinimas(rs.getString("OFICIALUS_PAVADINIMAS"));
	        	ist.setPastabos(rs.getString("PASTABOS"));
	        	ist.setRekvizSpausdinimui(rs.getString("IST_REKVIZ_SPAUSD"));
	        	retVal.add(ist);
	        }
	        rs.close();
	        stmt.close();
	        
		} catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
        	retVal = new ArrayList();
        }
		
		return retVal;
	}
	/**
	 * Graþina ástaigà su duotu identifikatoriumi
	 * @param id - ástaigos ID
	 */
	public Istaiga getIstaiga(HttpServletRequest req, long id) {
		Istaiga retVal = new Istaiga();	
		String qry = "begin GVDIS3_JOURNAL.GET_SAVIVALDYBES (?,?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.setLong(1, id); //konkrecia istaiga
	        stmt.registerOutParameter(2, OracleTypes.CURSOR);
	        stmt.execute();
	        
	        ResultSet rs = (ResultSet)stmt.getObject(2);
	        if (rs == null) return retVal;

	        if (rs.next()){
	        	//imame pirma istaiga, neturetu buti daugiau
	        	retVal = new Istaiga();	
	        	retVal.setId(rs.getLong("ID"));
	        	retVal.setTipas(rs.getLong("TIPAS"));
	        	retVal.setPavadinimas(rs.getString("PAVADINIMAS"));
	        	retVal.setPilnasPavadinimas(rs.getString("PILNAS_PAVADINIMAS"));
	        	retVal.setOficialusPavadinimas(rs.getString("OFICIALUS_PAVADINIMAS"));
	        	retVal.setPastabos(rs.getString("PASTABOS"));
	        	retVal.setRekvizSpausdinimui(rs.getString("IST_REKVIZ_SPAUSD"));
	        }
	        rs.close();
	        stmt.close();
	        
		} catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
        	retVal = new Istaiga();	
        }
		
		return retVal;
	}
	
	/**
	 * Graþina visas duotos savivaldybës seniûnijas
	 * @param savivaldybe - savivaldybë, kurios seniûnijas reikia graþinti
	 */
	public List getSeniunijos(HttpServletRequest req, Long savivaldybeId) throws DatabaseException{	
		
		List retVal = new ArrayList();
		try {
            String qry = "{ ? = call gvdis_akts.get_seniunijas_by_sav(?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            if (savivaldybeId == null) {
            	stmt.setNull(2, OracleTypes.NUMBER);
            } else {
            	stmt.setLong(2, savivaldybeId.longValue());
            }
            
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
	
	public List getSeniunijos(HttpServletRequest req) {
		List retVal = new ArrayList();
		
		String qry = "begin GVDIS3_JOURNAL.GET_SENIUNIJOS (?); end;";
		try{
			Connection conn = HibernateUtils.currentSession(req).connection();
	        CallableStatement stmt = conn.prepareCall(qry);
	        stmt.registerOutParameter(1, OracleTypes.CURSOR);
	        stmt.execute();
	        
	        ResultSet rs = (ResultSet)stmt.getObject(1);
	        if (rs == null) return retVal;
	        retVal = new ArrayList();
	        while(rs.next()){
	        	Istaiga ist = new Istaiga();
	        	ist.setId(rs.getLong("ID"));
	        	ist.setTipas(rs.getLong("TIPAS"));
	        	ist.setPavadinimas(rs.getString("PAVADINIMAS"));
	        	ist.setPilnasPavadinimas(rs.getString("PILNAS_PAVADINIMAS"));
	        	ist.setOficialusPavadinimas(rs.getString("OFICIALUS_PAVADINIMAS"));
	        	ist.setPastabos(rs.getString("PASTABOS"));
	        	ist.setRekvizSpausdinimui(rs.getString("IST_REKVIZ_SPAUSD"));
	        	retVal.add(ist);
	        }
	        rs.close();
	        stmt.close();
	        
		} catch(SQLException sqlEx) {
        	sqlEx.printStackTrace();
        	retVal = null;
        }
		return retVal;
	}
	
}
