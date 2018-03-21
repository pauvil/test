package lt.solver.gvdis.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;






import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.Constants;

public class JdbcUtils {
	
	public static void makeDbMark(Connection conn) throws SQLException {
		String qry = "exec ad_ok.mark_current_session(?,?,?,'false');";
		//String qry = "begin ad_ok.procedure mark_current_session(?,?,?,?) end;";
		//p_marker in varchar2,p_date in date,p_rasta_kitu out number,p_reikia_iseiti out boolean) 
		try {
			CallableStatement stmt = conn.prepareCall(qry);
			Calendar cal = Calendar.getInstance();
			Date currentDate = new Date(cal.getTime().getTime()); 
			//boolean p_reikia_iseiti = false;
			stmt.setString(1, Constants.APPLICATION);
			stmt.setDate(2, currentDate);
			stmt.registerOutParameter(3, OracleTypes.NUMBER); //p_rasta_kitu
	        stmt.registerOutParameter(4, OracleTypes.NUMBER);  //4, p_reikia_iseiti); //p_reikia_iseiti
	        stmt.execute();	        
		} catch (SQLException e) {
			throw e;
		}
        
	}
	public static void markDisSession(Connection conn) throws SQLException, ParseException {
		//String qry = "exec GVDIS_AUT.mark_dis_session(?,?,?,?);";
		String qry = "declare "				
					+"p_rasta_kitu number;"
					//+"p_reikia_iseiti varchar2(6);"
					+"begin "
					+"GVDIS_AUT.mark_dis_session(?,?,?,?);"
					
					+"end;";
		//p_marker in varchar2,p_date in date,p_rasta_kitu out number,p_reikia_iseiti out boolean) 
		try {
			CallableStatement stmt = conn.prepareCall(qry);
			Calendar cal = Calendar.getInstance();
			Date currentDate = new Date(cal.getTime().getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
			
			//System.out.println(currentDate + " " + sdf.format(currentDate));
			stmt.setString(1, Constants.APPLICATION);
			stmt.setString(2, sdf.format(currentDate));
			
			stmt.registerOutParameter(3, OracleTypes.NUMBER); //p_rasta_kitu
	        stmt.registerOutParameter(4, OracleTypes.VARCHAR); //p_reikia_iseiti
	        stmt.execute();
	        String str = stmt.getString(4);
	        //String gaunamaData = sdf.format(currentDate);
	        //java.sql.Date dt = sdf.parse(gaunamaData);
	        System.out.println(stmt.getInt(3));
	        if (str.equals("true") || stmt.getInt(3) > 0)
	        {
	        	qry = "ad_ok.kill_excess_sessions(?,?,?)";
	        	stmt.setString(1, Constants.APPLICATION);
				stmt.setDate(2, currentDate);
				stmt.registerOutParameter(3, OracleTypes.NUMBER);
				stmt.setInt(3, 1);
				stmt.execute();
				int i = stmt.getInt(3);
				i = i;
	        }
		} catch (SQLException e) {
			throw e;
		}
        
	}
	
	private static void initUserInModule(Connection conn, String userName) {
		String qry = "{ call gvdis_user_privs.init_user_roles(?, ?, ?, ?, ?) }";
		try {
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.setString(1, Constants.APPLICATION);
    		stmt.setString(2, userName.toUpperCase());    		
    		stmt.registerOutParameter(3, OracleTypes.VARCHAR);
    		stmt.registerOutParameter(4, OracleTypes.VARCHAR);    		
    		stmt.registerOutParameter(5, OracleTypes.VARCHAR);    		
    		stmt.execute();
			
		} catch (SQLException e) {
		}
        
	}
	
	public static Connection currentSessionConnnection(HttpServletRequest request) {
		
		Connection conn = (Connection) request.getSession().getAttribute("connectionJdbc");
		if (conn == null) {
			try {
				String login = (String)request.getSession().getAttribute("userLogin");
				String password = (String)request.getSession().getAttribute("userPassword");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn = DriverManager.getConnection(Constants.CONNECTION_STRING, login, password);
				conn = DriverManager.getConnection(Constants.getDB(), login, password);
				request.getSession().setAttribute("connectionJdbc", conn);
				//uzsimarkiname sesija
				//makeDbMark(conn);
				markDisSession(conn);
				//initcializuojame roles
				initUserInModule(conn, login);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new ExceptionInInitializerError(e);
			}
		}
		
		return conn;
	}
	
	public static void closeSessionConnnection(HttpServletRequest request) {
		Connection conn = (Connection) request.getSession().getAttribute("connectionJdbc");
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
			request.getSession().setAttribute("connectionJdbc", null);
		}
		
		
	}
}
