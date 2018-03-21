package lt.solver.gvdis.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lt.solver.gvdis.model.KaimaiObj;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;

public class AtaskaitosDAL {
	private static AtaskaitosDAL instance;
	
	public static AtaskaitosDAL getInstance() {
		if (instance == null){
			instance = new AtaskaitosDAL();
		}
		return instance;
	}
	
	public List getTeritorijosReport13(HttpServletRequest req, Long ter_nr, Date skaiciusDatai) throws DatabaseException {
		List retVal = new ArrayList();
		//p_ter_nr in number, p_data in date,p_cursor in out cursorType, p_err out varchar2
		String qry = "begin GVDIS3_query.skaiciuok_gyventojus_datai (?,?,?,?); end;";
		
		try {
			Connection conn = JdbcUtils.currentSessionConnnection(req);
			CallableStatement stmt = conn.prepareCall(qry);
			
			if (ter_nr == null) {
	        	stmt.setNull(1, OracleTypes.NUMBER);
	        } else {
	        	stmt.setLong(1, ter_nr.longValue()); 
	        }
			stmt.setDate(2, new java.sql.Date(skaiciusDatai.getTime()));
			stmt.registerOutParameter(3, OracleTypes.CURSOR);
			stmt.registerOutParameter(4, OracleTypes.VARCHAR);
			
	        stmt.execute();
			
	        String dbRet = stmt.getString(4);
	        if (dbRet != null) throw new DatabaseException(dbRet);
	        
	        ResultSet rs = (ResultSet)stmt.getObject(3);
	        if (rs != null) {
	        	while(rs.next()) {
	        		KaimaiObj kaimas = new KaimaiObj();
	        		kaimas.setNr(rs.getString("nr"));
	        		kaimas.setGrupe(rs.getString("grupe"));
	        		kaimas.setVyru(rs.getString("vyru"));
	        		kaimas.setMoteru(rs.getString("moteru"));
	        		kaimas.setViso(rs.getString("viso"));
	        		retVal.add(kaimas);
	        	}
	        	rs.close();
	        }
	        
			stmt.close();

		} catch (SQLException sqlEx) {
			throw new DatabaseException(sqlEx);
		}
        
		return retVal.isEmpty() ? null : retVal;
	}
}
