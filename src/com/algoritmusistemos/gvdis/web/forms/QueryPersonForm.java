package com.algoritmusistemos.gvdis.web.forms;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleTypes;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class QueryPersonForm extends ActionForm
{
	public static final String TYPE_ADR = "A";
	public static final String TYPE_TER = "T";
	
    public QueryPersonForm()
    {
    }

    public String getAsmKodas(){ return asmKodas; }
    public void setAsmKodas(String asmKodas){ this.asmKodas = asmKodas; }

    public String getPriezAprasymas(){ return priezAprasymas; }
    public void setPriezAprasymas(String priezAprasymas){ this.priezAprasymas = UtilDelegator.trim(priezAprasymas, 250); }

    public String getPriezKodas(){ return priezKodas; }
    public void setPriezKodas(String priezKodas){ this.priezKodas = priezKodas; }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
    	HttpSession session = request.getSession();
        ActionErrors errors = new ActionErrors();
		try {
			Long.parseLong(asmKodas);
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, asmKodas);
			int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
			
			if (userStatus != UserDelegator.USER_GLOBAL && userStatus != UserDelegator.USER_UZS){
			//if (userStatus== 1 || userStatus==2) {
				GyvenamojiVieta vt = null;

	        	long addr = ((Long)session.getAttribute("userIstaigaId")).longValue();        	
	        	
	            String paramType = request.getParameter("paramType");
	            if (!TYPE_ADR.equals(paramType) && !TYPE_TER.equals(paramType)){
	            	paramType = TYPE_TER;
	            }
	            
	            GyvenamojiVieta gvt = new GyvenamojiVieta();
	            if (TYPE_TER.equals(paramType)){
	            	gvt.setGvtAtvNr(new Long(addr));
	            }
	            else {
	            	gvt.setGvtAdvNr(new Long(addr));
	            }
	            
		        long s = 0; 
		        Long istId = (Long)session.getAttribute("userIstaigaId");
		        /*
	            Istaiga ist2 = JournalDelegator.getInstance().getIstaiga(request, ((Long)session.getAttribute("userIstaigaId")).longValue());
	            Long istId = new Long(ist2.getId());
	            ist2 = ist2.getIstaiga();	            
	    		if (ist2 != null){
	    			istId = new Long(ist2.getId());
	    		}
	    		*/
	    		
	            Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();	       
	            for (Iterator it=gyvenamosiosVietos.iterator(); it.hasNext(); ){
	        		vt = (GyvenamojiVieta)it.next();
	        		if (vt.getGvtDataIki() == null){
	        			try{
	        				String qry = "{? = call gvdis_utils.chk_ist_asm_gyvviet(?, ?, ?) }";
	                        Connection conn = HibernateUtils.currentSession(request).connection();
	                        CallableStatement stmt = conn.prepareCall(qry);
	                        stmt.registerOutParameter(1, OracleTypes.VARCHAR);
	                        if (istId != null && istId.longValue() > 0){
	                        	stmt.setLong(2, istId.longValue());
	                        }
	                        else {
	                        	stmt.setNull(2, OracleTypes.NUMBER);
	                        }
	                        stmt.setLong(3, vt.getGvtAsmNr());
	                        stmt.setLong(4, vt.getGvtNr());
	                        stmt.execute();
	                        s = stmt.getLong(1);
	                        stmt.close();

	                        if (s == 0){
	                        	asmuo = null;
	            				errors.add("title", new ActionMessage("error.neprieinamas"));
	            				request.setAttribute("error.neprieinamas", "");
	                		}
	        			}
	        			catch (SQLException sqlEx) {
	                        //throw new DatabaseException(sqlEx);
	                    }
	        		}
	            }
			}
		}
		catch (NumberFormatException nfe){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("error.asmKodas", "");
		}
		catch (ObjectNotFoundException onfe){
			errors.add("title", new ActionMessage("error.asmKodas"));
			request.setAttribute("error.asmKodas", "");
			////////////////////// AUDITAVIMAS  /////////////////////////////
  			try{
				long auditId = 0;
		        if (!"".equals(priezKodas)){
		        	auditId = AuditDelegator.getInstance().auditQueryByCode(request, asmKodas, priezKodas, priezKodas);
		        	session.setAttribute("lastAuditId", new Long(auditId));
		        }
		        else if (session.getAttribute("lastAuditId") == null){
		    		//throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.");
		        }
		        else try {
		        	auditId = ((Long)session.getAttribute("lastAuditId")).longValue();
		        }
		        catch (Exception e){
		        	e.printStackTrace();
		        	//throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.");
		        }
  			}
  			catch (DatabaseException dbe){
  				errors.add("title", new ActionMessage("error.asmKodas"));
  				request.setAttribute("error.asmKodas", "");
  			}
			///////////////////////////////////////////////////////
		}
		
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        asmKodas = "";
        priezKodas = "";
        priezAprasymas = "";
    }

    private String asmKodas;
    private String priezKodas;
    private String priezAprasymas;
}
