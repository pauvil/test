package com.algoritmusistemos.gvdis.web.actions.query.copy;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import java.util.Set;

public class QueryPersonFormAction extends Action
{
	
    public QueryPersonFormAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ObjectNotFoundException
    {
        HttpSession session = request.getSession();
        session.setAttribute("menu_cell","querypersonform");
        session.setAttribute("CENTER_STATE", "QUERY_PERSON_FORM");
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
        Set zinreik = ZinynaiDelegator.getInstance().getZinynoReiksmes(request, "AUDITO_PRIEZASTIS");
        
/*        if (((Integer)session.getAttribute("userStatus")).intValue() == 3 || ((Integer)session.getAttribute("userStatus")).intValue() == 2) {
        	Set kodai = new HashSet();
        	// 'F', 'G', 'I', 'J', 'K', 'N', 'P', 'S', 'T'
        	kodai.add("F");
        	kodai.add("G");
        	kodai.add("I");
        	kodai.add("J");
        	kodai.add("K");
        	kodai.add("N");
        	kodai.add("P");
        	kodai.add("S");
        	kodai.add("T");
        
        	for (Iterator it=zinreik.iterator(); it.hasNext();){
        		ZinynoReiksme zreiksme = (ZinynoReiksme)it.next();
        		if (!kodai.contains(zreiksme.getKodas())){
        			it.remove();
        		}
        	}
        }
*/    
        request.setAttribute("priezastys", zinreik);
        return mapping.findForward("continue");
    }
}
