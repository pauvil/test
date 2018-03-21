package lt.solver.gvdis.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;

import lt.solver.gvdis.model.DeklaracijaJournalObj;
import lt.solver.gvdis.model.PagingInitReturnObj;
import lt.solver.gvdis.model.PazymaJournalObj;
import lt.solver.gvdis.model.SavPazymaJournalObj;
import lt.solver.gvdis.model.SprendimaiJournalObj;
import lt.solver.gvdis.model.UrlValueBean;
import oracle.jdbc.OracleTypes;

//Kopija com.algoritmusistemos.gvdis.web.delegators.JournalDelegator
//Ismestas Hibernate palaikymas- del greicio
public class JournalDAL {
	
	public static final int JOURNAL_TYPE_IN = 0;
	public static final int JOURNAL_TYPE_OUT = 1;
	public static final int JOURNAL_TYPE_GVPAZ = 2;
	public static final int JOURNAL_TYPE_SPREND = 3;
	public static final int JOURNAL_TYPE_GVNA = 4;
	public static final int JOURNAL_TYPE_GVNAPAZ = 5;
	public static final int JOURNAL_TYPE_SAVPAZ = 6;
	
	private static JournalDAL instance;
	
	public static JournalDAL getInstance() {
		if (instance == null){
			instance = new JournalDAL();
		}
		return instance;
	}
	
	private ArrayList makeGvnaDeklaracijosActions(HttpServletRequest req, String regNr, int busena, Set roles) {
		ArrayList retVal = new ArrayList();
		
		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/gvnaDeclarationView.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);
		
		if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")) {
			if (busena == 3) { //nebaigta ivesti
				//Formuojame punkta Testi ivedima
				tmpStr = MessageFormat.format(req.getContextPath() + "/CompleteDeclaration.do?id={0}", new Object[]{regNr});
				urlTmp = new UrlValueBean();
				urlTmp.setUrl(tmpStr);
				urlTmp.setMessage("Tæsti ávedimà");
				retVal.add(urlTmp);	
			} else {
				//Formuojame punkta Testi Redaguoti
				tmpStr = MessageFormat.format(req.getContextPath() + "/editDeclaration.do?id={0}&type=N", new Object[]{regNr});
				urlTmp = new UrlValueBean();
				urlTmp.setUrl(tmpStr);
				urlTmp.setMessage("Redaguoti");
				retVal.add(urlTmp);
			}
		}

		if (roles.contains("RL_GVDIS_GL_TVARK")) {
			//Formuojame punkta istrinti
			tmpStr = MessageFormat.format(req.getContextPath() + "/DeleteDeclarationBaigta.do?id={0}&journalType=4", new Object[]{regNr}); 
			urlTmp = new UrlValueBean();
			urlTmp.setUrl(tmpStr);
			urlTmp.setMessage("Iðtrinti");
			urlTmp.setYnConfirm("Ar tikrai iðtrinti ðià deklaracijà?");
			retVal.add(urlTmp);
		}
		
		return retVal;
	}
	
	private ArrayList makeAtvykimoDeklaracijosActions(HttpServletRequest req, String regNr, int busena, Set roles) {
		ArrayList retVal = new ArrayList(); 

		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/inDeclarationView.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);
		
		if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || 
										roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){
			if (busena == 3) { //nebaigta ivesti
				//Formuojame punkta Testi ivedima
				tmpStr = MessageFormat.format(req.getContextPath() + "/CompleteDeclaration.do?id={0}", new Object[]{regNr});
				urlTmp = new UrlValueBean();
				urlTmp.setUrl(tmpStr);
				urlTmp.setMessage("Tæsti ávedimà");
				retVal.add(urlTmp);
			} else {
				//Formuojame punkta Testi Redaguoti
				tmpStr = MessageFormat.format(req.getContextPath() + "/editDeclaration.do?id={0}&type=A", new Object[]{regNr});
				urlTmp = new UrlValueBean();
				urlTmp.setUrl(tmpStr);
				urlTmp.setMessage("Redaguoti");
				retVal.add(urlTmp);
			}
		}
		
		if (roles.contains("RL_GVDIS_GL_TVARK")){
			//Formuojame punkta istrinti
			tmpStr = MessageFormat.format(req.getContextPath() + "/DeleteDeclarationBaigta.do?id={0}&journalType=0", new Object[]{regNr}); 
			urlTmp = new UrlValueBean();
			urlTmp.setUrl(tmpStr);
			urlTmp.setYnConfirm("Ar tikrai iðtrinti ðià deklaracijà?");
			urlTmp.setMessage("Iðtrinti");
			retVal.add(urlTmp);
		} 


		
		return retVal;
	}
	private ArrayList makeIsvykimoDeklaracijosActions(HttpServletRequest req, String regNr, int busena, Set roles) {
		ArrayList retVal = new ArrayList();

		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/outDeclarationView.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);
		
		if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){
			if (busena == 3) { //nebaigta ivesti
				//Formuojame punkta Testi ivedima
				tmpStr = MessageFormat.format(req.getContextPath() + "/CompleteDeclaration.do?id={0}", new Object[]{regNr});
				urlTmp = new UrlValueBean();
				urlTmp.setUrl(tmpStr);
				urlTmp.setMessage("Tæsti ávedimà");
				retVal.add(urlTmp);
				
			} else {
				//Formuojame punkta Testi Redaguoti
				tmpStr = MessageFormat.format(req.getContextPath() + "/editDeclaration.do?id={0}&type=I", new Object[]{regNr});
				urlTmp = new UrlValueBean();
				urlTmp.setUrl(tmpStr);
				urlTmp.setMessage("Redaguoti");
				retVal.add(urlTmp);
			}
	
		}
		//Formuojame punkta istrinti
		if (roles.contains("RL_GVDIS_GL_TVARK")) {
			//Formuojame punkta istrinti
			tmpStr =MessageFormat.format(req.getContextPath() + "/DeleteDeclarationBaigta.do?id={0}&journalType=1", new Object[]{regNr}); 
			urlTmp = new UrlValueBean();
			urlTmp.setUrl(tmpStr);
			urlTmp.setMessage("Iðtrinti");
			urlTmp.setYnConfirm("Ar tikrai iðtrinti ðià deklaracijà?");
			retVal.add(urlTmp);
		}

		return retVal;
	}
	
	private ArrayList makeSprendimaiAction(HttpServletRequest req, String regNr, Set roles) {

		ArrayList retVal = new ArrayList(); 
		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/viewsprendimas.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);
		if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")) {
			//Formuojame punkta Redaguoti
			tmpStr = MessageFormat.format(req.getContextPath() + "/editsprendimasform.do?id={0}", new Object[]{regNr}); 
			urlTmp = new UrlValueBean();
			urlTmp.setUrl(tmpStr);
			urlTmp.setMessage("Redaguoti");
			retVal.add(urlTmp);
  
		}
				
		return retVal;
	}
	
	private ArrayList makeGVPazymosAction(HttpServletRequest req, String regNr) {
		ArrayList retVal = new ArrayList(); 
		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/pazymosredirect.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);
		
		return retVal;
	}
	
	private ArrayList makeGVNAPazymosAction(HttpServletRequest req, String regNr) {
		ArrayList retVal = new ArrayList(); 
		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/gvnapazyma.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);

		return retVal;
	}
	private ArrayList makeSavPazymosAction(HttpServletRequest req, String regNr) {
		ArrayList retVal = new ArrayList(); 
		//Formuojame punkta perziureti
		String tmpStr = MessageFormat.format(req.getContextPath() + "/savpazyma.do?id={0}", new Object[]{regNr});
		UrlValueBean urlTmp = new UrlValueBean();
		urlTmp.setUrl(tmpStr);
		urlTmp.setMessage("Perþiûrëti");
		retVal.add(urlTmp);
		  
		return retVal;
	}
	
	public PagingInitReturnObj changeJournalPageOrder(HttpServletRequest req, String orderField) throws DatabaseException {

		PagingInitReturnObj retVal = new PagingInitReturnObj();
		String qry = "begin GVDIS3_JOURNAL.changeJournalPageOrder (?,?,?,?,?); end;";
		
		try{
			Connection conn = JdbcUtils.currentSessionConnnection(req);
	        CallableStatement stmt = conn.prepareCall(qry);

			stmt.setString(1, orderField);
			stmt.registerOutParameter(2, OracleTypes.NUMBER); //IrasuKiekis
	        stmt.registerOutParameter(3, OracleTypes.NUMBER); //PuslapiuKiekis
	        stmt.registerOutParameter(4, OracleTypes.NUMBER); //p_puslapiuKiekisRodyti
	        stmt.registerOutParameter(5, OracleTypes.VARCHAR); //error
	        
	        stmt.execute();

	        String dbErr = (String) stmt.getString(5);
	        if (dbErr != null) {
	        	throw new DatabaseException(dbErr);
	        } else {
	        	retVal.setError(false);
	        	retVal.setIrasuKiekis(stmt.getLong(2));
	        	retVal.setPuslapiuKiekis(stmt.getLong(3));
	        	retVal.setPuslapiuKiekisRodyti(stmt.getLong(4));
	        }

 	        stmt.close();
	        
			
		} catch(SQLException sqlEx) {
			throw new DatabaseException(sqlEx);
        }
		
		return retVal;
				
	}

	//pageNr < 0 - grazins visus puslapius
	public List getJournalPage(HttpServletRequest req, int pageNr, int zurnaloTipas, Set roles) throws DatabaseException {

		List retVal = new ArrayList();
		String qry = "";
		int cursorIndex = 0;
		int errIndex = 0;
		if (pageNr < 0) {
			qry = "begin GVDIS3_JOURNAL.getAllRows (?,?); end;";
			cursorIndex = 1;
			errIndex = 2;
		}
		else {
			qry = "begin GVDIS3_JOURNAL.get_journalPage (?,?,?); end;";
			cursorIndex = 2;
			errIndex = 3;
		}
		
		try{

			Connection conn = JdbcUtils.currentSessionConnnection(req);
	        CallableStatement stmt = conn.prepareCall(qry);
			if (pageNr >= 0) 
				stmt.setLong(1, pageNr);
			
			stmt.registerOutParameter(cursorIndex, OracleTypes.CURSOR);
	        stmt.registerOutParameter(errIndex, OracleTypes.VARCHAR); //erroras
	        
	        stmt.execute();
	        
	        String err = (String)stmt.getObject(errIndex);
	        if (err != null) {
	        	throw new DatabaseException(err);
	        }
	        
	        ResultSet rs = (ResultSet)stmt.getObject(cursorIndex);
	        if (rs != null) {
	        	while(rs.next()){
	        		switch (zurnaloTipas){
	        			case JournalDAL.JOURNAL_TYPE_IN:
	        			case JournalDAL.JOURNAL_TYPE_OUT:
	        			case JournalDAL.JOURNAL_TYPE_GVNA:
	    	        		DeklaracijaJournalObj deklaracija = new DeklaracijaJournalObj();
	    	        		deklaracija.setId(rs.getLong("ID"));
	    	        		deklaracija.setRegNr(rs.getString("REG_NR"));
	    	        		deklaracija.setDeklaracijosData(new java.sql.Timestamp(rs.getDate("DEKLARAVIMO_DATA").getTime()));
	    	        		deklaracija.setAsmenvardis(rs.getString("Asmuo"));
	    	        		deklaracija.setBusena(rs.getInt("BusenaWeb"));
	    	        		deklaracija.setIstaigaStr(rs.getString("PILNAS_PAVADINIMAS"));
	    	        		deklaracija.setPastabos(rs.getString("pastabos"));
	    	        		//salis reikalinga tik pdf isv deklaracijai
	    	        		deklaracija.setGyvSalisKodas(rs.getString("vls_kodas"));
	    	        		deklaracija.setGyvSalisStr(rs.getString("vls_pav"));
	    	        		//Deklaracijos veiksmai bus
	    	        		//Perziureti | Redaguoti/Testi ivedima | istrinti
	    	        		ArrayList act = null;
	    	        		if (zurnaloTipas == JournalDAL.JOURNAL_TYPE_IN) {
	    	        			act = makeAtvykimoDeklaracijosActions(req, String.valueOf(deklaracija.getId()), 
	    	        															deklaracija.getBusena(), roles);
	    	        		}
	    	        		if (zurnaloTipas == JournalDAL.JOURNAL_TYPE_OUT) {
	    	        			act = makeIsvykimoDeklaracijosActions(req, String.valueOf(deklaracija.getId()), 
	    	        															deklaracija.getBusena(), roles);
	    	        		}
	    	        		if (zurnaloTipas == JournalDAL.JOURNAL_TYPE_GVNA) {
	    	        			act = makeGvnaDeklaracijosActions(req, String.valueOf(deklaracija.getId()), 
	    	        															deklaracija.getBusena(), roles);
	    	        		}
	    	        		deklaracija.setVeiksmai(act);

	    	        		retVal.add(deklaracija);
	        				break;
	        			case JournalDAL.JOURNAL_TYPE_GVPAZ:
	           			case JournalDAL.JOURNAL_TYPE_GVNAPAZ:
	           				PazymaJournalObj pazyma = new PazymaJournalObj();
	           				pazyma.setId(rs.getLong("ID"));
	           				pazyma.setRegNr(rs.getString("REG_NR"));
	           				pazyma.setPazymosData(new java.sql.Timestamp(rs.getDate("pazymos_data").getTime()));
	           				pazyma.setAsmenvardis(rs.getString("Asmuo"));
	           				pazyma.setIstaigaStr(rs.getString("PILNAS_PAVADINIMAS"));
	           				pazyma.setPastabos(rs.getString("PASTABOS"));
	    	        		//Pazymu veiksmai bus
	           				ArrayList veiksmai = null;
	           				if (zurnaloTipas == JournalDAL.JOURNAL_TYPE_GVPAZ) {
	           					veiksmai = makeGVPazymosAction(req, String.valueOf(pazyma.getId()));
	           				}
	           				if (zurnaloTipas == JournalDAL.JOURNAL_TYPE_GVNAPAZ) {
	           					veiksmai = makeGVNAPazymosAction(req, String.valueOf(pazyma.getId()));
	           				}
	           				pazyma.setVeiksmai(veiksmai);
	           				
	           				retVal.add(pazyma);
	           				break;
	           			case JournalDAL.JOURNAL_TYPE_SPREND:
	           				SprendimaiJournalObj sprendimas = new SprendimaiJournalObj();
	           				sprendimas.setId(rs.getLong("ID"));
	           				sprendimas.setRegNr(rs.getString("REG_NR"));
	           				sprendimas.setSprendimoData(new java.sql.Timestamp(rs.getDate("sprendimo_data").getTime()));
	           				sprendimas.setTipas(rs.getString("tipas"));
	           				sprendimas.setIstaigaStr(rs.getString("PILNAS_PAVADINIMAS"));
	           				sprendimas.setInsData(new java.sql.Timestamp(rs.getDate("ins_data").getTime()));
	           				sprendimas.setPastabos(rs.getString("pastabos"));
	           				sprendimas.setPrasytojas(rs.getString("prasytojas"));
	           				
	           				ArrayList veiksmaispr = makeSprendimaiAction(req, String.valueOf(sprendimas.getId()), roles);
	           				sprendimas.setVeiksmai(veiksmaispr);
	           				
	           				retVal.add(sprendimas);
	           				break;
	           			case JournalDAL.JOURNAL_TYPE_SAVPAZ:
	           				SavPazymaJournalObj savPazyma = new SavPazymaJournalObj();
	           				savPazyma.setId(rs.getLong("ID"));
	           				savPazyma.setRegNr(rs.getString("REG_NR"));
	           				savPazyma.setPazymosData(new java.sql.Timestamp(rs.getDate("pazymos_data").getTime()));
	           				savPazyma.setAdresas(rs.getString("adresas"));
	           				savPazyma.setIstaigaStr(rs.getString("PILNAS_PAVADINIMAS"));
	           				savPazyma.setPastabos(rs.getString("pastabos"));
	           				ArrayList veiksmaisav = makeSavPazymosAction(req, String.valueOf(savPazyma.getId()));
	           				savPazyma.setVeiksmai(veiksmaisav);
	           				
	           				retVal.add(savPazyma);
	           				break;
	        		}

	        	}

	        	rs.close();
	 	        stmt.close();
	        }
			
		} catch(SQLException sqlEx) {
			throw new DatabaseException(sqlEx);
        }
		
		return retVal.isEmpty() ? null : retVal;
				
	}
	
	public PagingInitReturnObj journalInit(int deklaracijosTipas, HttpServletRequest req, String orderField, 
			Date dataNuo, Date dataIki, int busena, Long savivaldybeId, Long seniunijaId, String valstybesKodas, String tipas) throws DatabaseException {
		
		PagingInitReturnObj retVal = new PagingInitReturnObj();
		String qry = "begin GVDIS3_JOURNAL.initJournalPaging (?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

		try{
			Connection conn = JdbcUtils.currentSessionConnnection(req);
	        CallableStatement stmt = conn.prepareCall(qry);

	        stmt.setInt(1, deklaracijosTipas);
	        long tm = dataNuo.getTime();
	        java.sql.Date nuoSqlDate = new java.sql.Date(tm);
	        tm = dataIki.getTime();
	        java.sql.Date ikiSqlDate = new java.sql.Date(tm);
	        stmt.setDate(2, nuoSqlDate);
	        stmt.setDate(3, ikiSqlDate);
	        if (seniunijaId == null)
	        	stmt.setNull(4, OracleTypes.NUMBER);
	        else
	        	stmt.setLong(4, seniunijaId.longValue());
	        if (savivaldybeId == null)
	        	stmt.setNull(5, OracleTypes.NUMBER);
	        else
	        	stmt.setLong(5, savivaldybeId.longValue());
	        
	        if (valstybesKodas == null || valstybesKodas == "") {
	        	stmt.setNull(6, OracleTypes.VARCHAR);
	        } else {
	        	stmt.setString(6, valstybesKodas);
	        }
	        
	        try {
				long t = Long.parseLong(tipas);
				stmt.setLong(7, t);
			} catch (NumberFormatException e) {
				stmt.setNull(7, OracleTypes.NUMBER);
			}
			
			stmt.setLong(8, busena);
			
			stmt.setString(9, orderField);
			//stmt.setString(8, orderDirection);
			
	        stmt.registerOutParameter(10, OracleTypes.NUMBER); //IrasuKiekis
	        stmt.registerOutParameter(11, OracleTypes.NUMBER); //PuslapiuKiekis
	        stmt.registerOutParameter(12, OracleTypes.NUMBER); //PuslapiuKiekisRodyti
	        stmt.registerOutParameter(13, OracleTypes.VARCHAR); //error
			
	        stmt.execute();
	        
	        String dbErr = (String) stmt.getString(13);
	        if (dbErr != null) {
	        	throw new DatabaseException(dbErr);
	        } else {
	        	retVal.setError(false);
	        	retVal.setIrasuKiekis(stmt.getLong(10));
	        	retVal.setPuslapiuKiekis(stmt.getLong(11));
	        	retVal.setPuslapiuKiekisRodyti(stmt.getLong(12));
	        }
	        stmt.close();
		
		} catch(SQLException sqlEx) {
			throw new DatabaseException(sqlEx);
	    }
		
		return retVal;
	}
	
	
	
}
