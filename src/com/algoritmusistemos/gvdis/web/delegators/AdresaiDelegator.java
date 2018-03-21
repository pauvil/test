package com.algoritmusistemos.gvdis.web.delegators;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import org.hibernate.Session;

import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AdresinisVienetas;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.TeritorinisVienetas;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class AdresaiDelegator 
{
	private static AdresaiDelegator instance;
	
	public static AdresaiDelegator getInstance() 
	{
		if (instance == null){
			instance =  new AdresaiDelegator();
		}
		return instance;
	}	

	public List getRajonoSavivaldybes(HttpServletRequest request)
	{
		return HibernateUtils.currentSession(request).createSQLQuery(
			"select {tervnt.*} from gvdisvw_adr_ter_vnt tervnt where tervnt.TER_TEV_NR = 1 " +
			"and (tervnt.TER_TIPAS = 'R' or tervnt.TER_TIPAS = 'RSM') order by ter_pav").
       		addEntity("tervnt", TeritorinisVienetas.class).list();
	}	

	public AdresinisVienetas getAdrVieneta(Long id,HttpServletRequest request)
	{
			return (AdresinisVienetas)HibernateUtils.currentSession(request).createSQLQuery(
				"select {adrvnt.*} from gvdisvw_adr_adv_vnt adrvnt where adrvnt.ADV_NR = :id ").
				addEntity("adrvnt", AdresinisVienetas.class).setLong("id",id.longValue()).uniqueResult();
	}	
	
	public TeritorinisVienetas getTerVieneta(Long id,Session session)
	{
			return (TeritorinisVienetas)session.createSQLQuery(
				"select {tervnt.*} from gvdisvw_adr_ter_vnt tervnt where tervnt.TER_NR = :id ").
				addEntity("tervnt", TeritorinisVienetas.class).setLong("id",id.longValue()).uniqueResult();
	}

	public TeritorinisVienetas getTerVieneta(Long id,HttpServletRequest request)
	{
		
			return getTerVieneta(id,HibernateUtils.currentSession(request)); 			
	}

	
	public String getAdresoEilute(long terid,String type,HttpServletRequest request, String gvtKampoNr)
	{
 		String qry = "{? = call gvdis_akts.get_string_address(?,?, ?) }";
        String s = null;
        	try {
        		Connection conn = HibernateUtils.currentSession(request).connection(); 
        		CallableStatement stmt = conn.prepareCall(qry);
        		stmt.registerOutParameter(1, OracleTypes.VARCHAR);
        		
        		if("A".equals(type))
        		{
        			stmt.setNull(2,java.sql.Types.INTEGER);
        			stmt.setLong(3,terid);
        			if (gvtKampoNr ==  null || gvtKampoNr.equals("") || gvtKampoNr.equals("null")) stmt.setNull(4, OracleTypes.NUMBER);
        			else stmt.setLong(4, Long.parseLong(gvtKampoNr));
        		}
  
        		if("T".equals(type))
        		{
        			stmt.setLong(2,terid);
        			stmt.setNull(3,java.sql.Types.INTEGER);
        			stmt.setNull(4, OracleTypes.NUMBER);
        		}        		
        		stmt.execute();
        		
        		s = stmt.getString(1);
                stmt.close();
            }
            catch (SQLException e){
            	e.printStackTrace();
            }
        return s;		
	}
/*	Kam ji reikalinga?
 * public String getAdresoEilute(long terid,long adrid,HttpServletRequest request)
	{
 		String qry = "{? = call gvdis_akts.get_string_address(?,?) }";
        String s = null;
        	try {
        		Connection conn = HibernateUtils.currentSession(request).connection(); 
        		CallableStatement stmt = conn.prepareCall(qry);
        		stmt.registerOutParameter(1, OracleTypes.VARCHAR);
       			stmt.setLong(2,terid);
       			stmt.setLong(3,adrid);
        		stmt.execute();
        		
        		s = stmt.getString(1);
                stmt.close();
            }
            catch (SQLException e){
            	e.printStackTrace();
            }
        return s;		
	}
*/	

	public String getAsmAddress(long asmNr, long gvtNr, HttpServletRequest request) {
		String qry = "{? = call gvdis_akts.get_asm_address(?,?,?,?) }";
		String adr = null;
		try {
			Connection conn = HibernateUtils.currentSession(request).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
			stmt.setLong(2, asmNr);
			stmt.setLong(3, gvtNr);

			stmt.registerOutParameter(4, OracleTypes.NUMBER);
			stmt.registerOutParameter(5, OracleTypes.VARCHAR);

			stmt.execute();
			adr = stmt.getString(1);
			stmt.close();
			if (adr != null) {
				RE pattern;
				try {
					pattern = new RE("GVNA apskait");
					REMatch match = pattern.getMatch(adr);
					if (match != null) {
						String adresas = "Átrauktas á GVNA apskaità"; // vorkaraoundas
																		// lietuvybej...
						adr = adresas;
					}
				} catch (REException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return adr;
	}
	
	public String getAsmAddress2(long asmNr, long gvtNr, HttpServletRequest request) {
		String qry = "{? = call gvdis_akts.get_asm_address2(?,?,?,?) }";
		String adr = null;
		try {
			Connection conn = HibernateUtils.currentSession(request).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
			stmt.setLong(2, asmNr);
			stmt.setLong(3, gvtNr);

			stmt.registerOutParameter(4, OracleTypes.NUMBER);
			stmt.registerOutParameter(5, OracleTypes.VARCHAR);

			stmt.execute();
			adr = stmt.getString(1);
			stmt.close();
			if (adr != null) {
				RE pattern;
				try {
					pattern = new RE("GVNA apskait");
					REMatch match = pattern.getMatch(adr);
					if (match != null) {
						String adresas = "Átrauktas á GVNA apskaità"; // vorkaraoundas
																		// lietuvybej...
						adr = adresas;
					}
				} catch (REException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return adr;
	}

	public Address getAsmDeklGvtNr(long asmid, long deklid, HttpServletRequest request)
	{
		Session session = HibernateUtils.currentSession(request);
		return getAsmDeklGvtNr(asmid, deklid, session);		
	}

	/**
	 * Namo kampo numeris
	 * @return - Grazina null arba namo kamopo numeri
	 * 
	 */
	public String getNamoKampoNr (String addressId, HttpServletRequest request) throws DatabaseException {
		String qry = "{? = call gvdis_akts.get_namo_kampo_nr(?) }";
		String msg = null;
		
		try {
			Connection conn = HibernateUtils.currentSession(request).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			stmt.setString(2, addressId);
			stmt.execute();
			msg = stmt.getString(1);
			stmt.close();
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return msg;
	}
	
	/**
	 * Tikrina ar duotojo asmens adresas yra visdar aktualus
	 * @return - grazina null arba pranesima
	 */
	public String checkAsmAdress (String asmNr, String gvAdress, String gvtId ,HttpServletRequest request) throws DatabaseException {
		String qry = "{? = call gvdis_akts.chk_asm_address(?,?,?) }";
		String msg = null;
		
		try {
			Connection conn = HibernateUtils.currentSession(request).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.VARCHAR);
			stmt.setString(2, asmNr);
			stmt.setString(3, gvAdress);
			stmt.setString(4, gvtId);
			stmt.execute();
			msg = stmt.getString(1);
			stmt.close();
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return msg;
	}
	public String checkDeklaruotaVietaIsLietuva (String asmNr,HttpServletRequest request) throws DatabaseException {

		String msg = null;
		
		try {
			Connection conn = HibernateUtils.currentSession(request).connection();
			
			String query = "select gv.GVT_VLS_KODAS from gvdis_deklaracijos d"
	+" inner join GVDISVW_GYVENAMOSIOS_VIETOS gv on (gv.gvt_asm_nr = d.gr_gvt_asm_nr and gv.gvt_nr = d.gr_gvt_nr)"
	+" inner join GVDISVW_ASMENYS asm on d.gr_asm_nr = asm.asm_nr"
	+" where d.busena=1 and asm.asm_nr = ? Order By gv.gvt_nr desc ";
/* I.N. 2009-12-17
			String query = "select gv.GVT_VLS_KODAS from gvdis_deklaracijos d"
				+" inner join pilietis.gyvenamosios_vietos gv on (gv.gvt_asm_nr = d.gr_gvt_asm_nr and gv.gvt_nr = d.gr_gvt_nr)"
				+" inner join pilietis.asmenys asm on d.gr_asm_nr = asm.asm_nr"
				+" where d.busena=1 and asm.asm_nr = ?";
*/			
			
	   		PreparedStatement ps = conn.prepareStatement(query);
	   		ps.setString(1, asmNr);
			ResultSet rs = ps.executeQuery();
			int i = 0;
			if(rs.next()) 
			{
				msg = rs.getString(1);
				i++;
			}
			ps.close();
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return msg;
	}	
	
	public Address getAsmDeklGvtNr(long asmid, long deklid, Session session)
	{
		String qry = "{ call gvdis_akts.get_asm_dekl_gvt_nr(?,?,?,?,?) }";
		Address adr = null;
		
		try {
			Connection conn = session.connection(); 
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.setLong(1,asmid);
			if(0 == deklid)stmt.setNull(2,java.sql.Types.INTEGER);
			else stmt.setLong(2,deklid);       			
			
			stmt.registerOutParameter(3, OracleTypes.NUMBER);
			stmt.registerOutParameter(4, OracleTypes.VARCHAR);
			stmt.registerOutParameter(5, OracleTypes.VARCHAR);
			
			stmt.execute();
			if (stmt.getLong(3) > 0 && stmt.getString(4) != null){
				adr = new Address(stmt.getLong(3), stmt.getString(4));
				String type = stmt.getString(5);
				if (type != null && type.length() == 1){
					adr.setType(type.charAt(0));
				}
			}
			stmt.close();
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		// Jei tai GVNA adresas, prie jo reikia prikabinti savivaldybæ
		if (adr != null && 'K' == adr.getType()) try {
			String adresas = "Átrauktas á GVNA apskaità"; //vorkaraoundas lietuvybej...
			adr.setAdr(adresas);
			GyvenamojiVieta gvt = QueryDelegator.getInstance().getGyvenamojiVieta(session, asmid, adr.getId());
			if (gvt != null && gvt.getDeklaracija() != null && gvt.getDeklaracija() instanceof GvnaDeklaracija){
				GvnaDeklaracija deklaracija = (GvnaDeklaracija)gvt.getDeklaracija();
				if (deklaracija.getSavivaldybe() != null){					
					adr.setAdr(adr.getAdr() + " (" + deklaracija.getSavivaldybe().getTerPav() + ")");
				}
				else{
					if (null != gvt.getGvtAtvNr()){
            			String savivaldybe;
            			savivaldybe = AdresaiDelegator.getInstance().getTerVieneta(gvt.getGvtAtvNr(), session).getTerPav();
            			adr.setAdr(adr.getAdr() + " (" + savivaldybe + ")");
            		}
				}
			}
			else{ // iki 2007-07-01 nera deklaraciju
				if (gvt != null && null != gvt.getGvtAtvNr()){
        			String savivaldybe;
        			savivaldybe = AdresaiDelegator.getInstance().getTerVieneta(gvt.getGvtAtvNr(), session).getTerPav();
        			adr.setAdr(adr.getAdr() + " (" + savivaldybe + ")");
        		}
			}
		}
		catch (ObjectNotFoundException onfe){
		}
		return adr;		
	}
	
	
	
	
	
	public Address getTestAsmDeklGvtNr(long asmid, long deklid, HttpServletRequest request, int tipas)
	{
		String qry = "{ call gvdis_akts.get_asm_dekl_gvt_nr(?,?,?,?,?) }";
		Address adr = null;
		Session session = HibernateUtils.currentSession(request);
		try {
			Connection conn = session.connection(); 
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.setLong(1,asmid);
			if(0 == deklid)stmt.setNull(2,java.sql.Types.INTEGER);
			else stmt.setLong(2,deklid);       			
			
			stmt.registerOutParameter(3, OracleTypes.NUMBER);
			stmt.registerOutParameter(4, OracleTypes.VARCHAR);
			stmt.registerOutParameter(5, OracleTypes.VARCHAR);
			
			stmt.execute();
			if (stmt.getLong(3) > 0 && stmt.getString(4) != null){
				adr = new Address(stmt.getLong(3), stmt.getString(4));
				String type = stmt.getString(5);
				if (type != null && type.length() == 1){
					adr.setType(type.charAt(0));
				}
			}
			stmt.close();
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		// Jei tai GVNA adresas, prie jo reikia prikabinti savivaldybæ
		if (adr != null && 'K' == adr.getType()) try {
			GyvenamojiVieta gvt = QueryDelegator.getInstance().getGyvenamojiVieta(request, asmid, adr.getId());
			if (gvt != null && gvt.getDeklaracija() != null && gvt.getDeklaracija() instanceof GvnaDeklaracija){
				GvnaDeklaracija deklaracija = (GvnaDeklaracija)gvt.getDeklaracija();
				if (deklaracija.getSavivaldybe() != null){
					String rezStr = "";
					try {
						switch(tipas){
						case 1: rezStr = new String(adr.getAdr().getBytes("utf8"), "Windows-1257");break;
						case 2: rezStr = new String(adr.getAdr().getBytes("utf8"), "utf8");break;
						case 3: rezStr = new String(adr.getAdr().getBytes("Windows-1257"), "Windows-1257");break;
						case 4: rezStr = new String(adr.getAdr().getBytes("ISO-8859-1"), "Windows-1257");break;
						case 5: rezStr = new String(adr.getAdr().getBytes("ISO-8859-1"), "ISO-8859-1");break;
						case 6: rezStr = new String(adr.getAdr().getBytes("Windows-1257"), "utf8");break;
						case 7: rezStr = new String(adr.getAdr().getBytes("ISO-8859-13"), "ISO-8859-13");break;	
						case 8: rezStr = new String(adr.getAdr().getBytes("ISO-8859-13"), "Windows-1257");break;	
						case 9: rezStr = new String(adr.getAdr().getBytes("Windows-1257"), "ISO-8859-13");break;	
						case 10: rezStr = new String(adr.getAdr().getBytes("ISO-8859-13"), "utf8");break;
						case 11: rezStr = new String(adr.getAdr().getBytes("utf8"), "ISO-8859-13");break;						
						}
												
					} catch (UnsupportedEncodingException e) {}
					
					adr.setAdr(rezStr + " (" + deklaracija.getSavivaldybe().getTerPav() + ")");
				}
			}
		}
		catch (ObjectNotFoundException onfe){
		}
		return adr;		
	}
}
