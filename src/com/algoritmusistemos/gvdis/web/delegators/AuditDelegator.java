package com.algoritmusistemos.gvdis.web.delegators;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.Constants;

public class AuditDelegator
{
    private static AuditDelegator instance;
    
    public AuditDelegator()
    {
    }

    public static AuditDelegator getInstance()
    {
        if(instance == null)
            instance = new AuditDelegator();
        return instance;
    }

    /**
     * U�fiksuoja asmens duomen� per�i�ros u�klaus� pagal asmens kod�
     * @param reasonCode - duomen� per�i�ros prie�astis kodas
     * @param reasonExpl - duomen� per�i�ros prie�astis paai�kinimas
     * @param code - asmens kodas
     * @return - u�fiksuotos u�klausos ID, kuris naudojamas rezultat� per�i�ros fiksavimui
     * @throws DatabaseException
     */
    public long auditQueryByCode(HttpServletRequest req, String code, String reasonCode, String reasonExpl)
    	throws DatabaseException
    {
    	long retVal = 0;
        try {
            String qry = "{? = call gvdis_query.aud_uzklausa(?, ?, null, null, null, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.NUMBER);
            stmt.setString(2, "GVDIS");
            if ((code !=  null && !code.equals("") && !code.equals("null"))) {
                stmt.setLong(3, Long.valueOf(code).longValue());
            }
            else {
                stmt.setLong(3, OracleTypes.NULL);
            }
            stmt.setString(4, reasonCode);
            stmt.setString(5, reasonExpl);
            stmt.execute();
            retVal = stmt.getLong(1);
            stmt.close();
        }
        catch (SQLException sqlEx) {
        	Constants.Println(req, sqlEx.getMessage());
            throw new DatabaseException(sqlEx);
        }
		return retVal;
    }

    /**
     * U�fiksuoja asmens duomen� per�i�ros u�klaus� pagal asmens kod�
     * @param reasonCode - duomen� per�i�ros prie�astis kodas
     * @param reasonExpl - duomen� per�i�ros prie�astis paai�kinimas
     * @param code - asmens kodas
     * @return - u�fiksuotos u�klausos ID, kuris naudojamas rezultat� per�i�ros fiksavimui
     * @throws DatabaseException
     */
    public long auditQueryByAddress(HttpServletRequest req, Long terNr, Long adrNr, String valstKodas, String reasonCode, String reasonExpl)
    	throws DatabaseException
    {
    	long retVal = 0;
        try {
            String qry = "{? = call gvdis_query.aud_uzklausa(?, null, ?, ?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.NUMBER);
            stmt.setString(2, "GVDIS");
            if (terNr != null){
                stmt.setLong(3, terNr.longValue());
            }
            else {
            	stmt.setNull(3, OracleTypes.NUMBER);
            }
            if (adrNr != null){
                stmt.setLong(4, adrNr.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setString(5, valstKodas);
            stmt.setString(6, reasonCode);
            stmt.setString(7, reasonExpl);
            stmt.execute();
            retVal = stmt.getLong(1);
            stmt.close();
        }
        catch (SQLException sqlEx) {
        	Constants.Println(req, sqlEx.getMessage());
            throw new DatabaseException(sqlEx);
        }
		return retVal;
    }

    /**
     * U�fiksuoja auditui duoto asmens duomen� per�i�ros fakt�
     * @param auditId - anks�iau u�fiksuotos u�klausos ID
     * @param person - asmuo, kurio duomenys �i�rimi
     * @throws DatabaseException
     */
    public void auditPersonResult(HttpServletRequest req, long auditId, Asmuo person)
    	throws DatabaseException
    {
        try {
            String qry = "{call gvdis_query.aud_asm_rezultatas(?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.setLong(1, auditId);
            stmt.setLong(2, person.getAsmNr());
            stmt.execute();
            stmt.close();
        }
        catch (SQLException sqlEx) {
        	Constants.Println(req, sqlEx.getMessage());
            throw new DatabaseException(sqlEx);
        }
    }

    /**
     * U�fiksuoja auditui duoto asmens gyvenamosios vietos duomen� per�i�ros fakt�
     * @param auditId - anks�iau u�fiksuotos u�klausos ID
     * @param gvt - per�i�rima gyvenamoji vieta
     * @throws DatabaseException
     */
    public void auditGvtResult(HttpServletRequest req, long auditId, GyvenamojiVieta gvt)
    	throws DatabaseException
    {
        try {
            String qry = "{call gvdis_query.aud_gvt_rezultatas(?, ?, null) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.setLong(1, auditId);
            stmt.setLong(2, gvt.getGvtAsmNr());
            //stmt.setLong(3, gvt.getGvtNr());
            stmt.execute();
            stmt.close();
        }
        catch (SQLException sqlEx) {
        	Constants.Println(req, sqlEx.getMessage());
            throw new DatabaseException(sqlEx);
        }
    }
}
