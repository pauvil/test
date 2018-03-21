package com.algoritmusistemos.gvdis.web.actions.query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.persistence.AddressRow;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.TeritorinisVienetas;

public class AddressBrowserAction extends Action
{
	public static final String TYPE_ADR = "A";
	public static final String TYPE_TER = "T";
	
	public static final int DEFAULT_START_ID = 1;

    public AddressBrowserAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws DatabaseException
    {
        HttpSession session = request.getSession();
        session.setAttribute("POPUP_STATE", Constants.ADDRESS_BROWSER);
        
        // Pasiimame parametrus
        long currentAdr = 0;
        
        Integer userStatus = (Integer)session.getAttribute("userStatus");
        try {
        	currentAdr = Long.parseLong(request.getParameter("currentAdr"));
        }
        catch (Exception e){
        	if (userStatus != null && userStatus.intValue() == 1){
        		currentAdr = ((Long)session.getAttribute("userIstaigaId")).longValue();
        	}
        	else if (userStatus != null && userStatus.intValue() == 2){
        		Istaiga ist = JournalDelegator.getInstance().getIstaiga(request, ((Long)session.getAttribute("userIstaigaId")).longValue());
        		ist = ist.getIstaiga();
        		if (ist != null){
        			currentAdr = ist.getId();
        		}
        		else {
            		currentAdr = DEFAULT_START_ID;
        		}
        	}
        	else { 
        		currentAdr = DEFAULT_START_ID;
        	}
        }
               
        String paramType = request.getParameter("paramType");
        boolean paramChanged  = false;
        if (!TYPE_ADR.equals(paramType) && !TYPE_TER.equals(paramType)){
        	paramType = TYPE_TER;
        	paramChanged = true;
        }
        
        String query = request.getParameter("query");
        if (query == null){
        	query = "";
        }
        request.setAttribute("query", query);
                
		//-->>ju.k 2007.08.28   
        long paramCall = 0;
        Long istId = null;
        
        if (userStatus != null && userStatus.intValue() == UserDelegator.USER_GLOBAL){
	    	istId = null;
	    	paramCall = 1;
        }      
    	else {istId  = (Long)session.getAttribute("userIstaigaId");}
        
        if (userStatus.intValue() != 0){
        try
        {paramCall = Long.parseLong(request.getParameter("paramCall"));}
        catch (Exception e){paramCall = 0;}
        }
        
		if (paramCall != 1) 
	    {session.setAttribute("adr_brows_state", Constants.ADDR_BROWS_IST);
        List addresses_ist = QueryDelegator.getInstance().getSubAddr_ist(request, istId);
        request.setAttribute("addr_ist", addresses_ist);
        request.setAttribute("paramType",paramType);
        request.setAttribute("istaiga", istId);
	    }
		else
		{
	    paramCall = 0;
		//<<--
        
        // Gauname gilesnius adresus ðiam adresui
        List addresses = QueryDelegator.getInstance().getSubAddresses(request, paramType, currentAdr);
        if (!"".equals(query) && addresses != null){
        	for (Iterator it=addresses.iterator(); it.hasNext(); ){
        		AddressRow row = (AddressRow)it.next();
        		if (row.getPavadinimas().indexOf(query) == -1){
        			it.remove();
        		}
        	}
        }
        request.setAttribute("addresses", addresses);
        if(!paramChanged && "T".equals(paramType)){
			TeritorinisVienetas ter = AdresaiDelegator.getInstance().getTerVieneta(new Long(currentAdr), request);
			if("G".equals(ter.getTerTipas()))
				request.setAttribute("currentIsGatve", "True");
		}
		}
		
		//<%if (size == 0 && "".equals(request.getAttribute("query"))){%>
		//<script language="Javascript">
	//	<!--
		//	closePopup();
////		-->
	//	</script>
		//<% }  %>
		//<%	if (size == 0 && !"".equals(request.getAttribute("query"))){ %>
		//<center><b>Nieko nerasta</b></center>
	//	<%	} %>
		
        // Perduodame kitus parametrus
        request.setAttribute("addressId", new Long(currentAdr));
        request.setAttribute("paramType", paramType);
        
        session.setAttribute("elementType", paramType);
        session.setAttribute("addressId", new Long(currentAdr));
        
        String gvtKampoNr = "";
        
       	gvtKampoNr = request.getParameter("gvtKampoNr");
       	
       	Constants.Println(request, "kampo nr:   " + gvtKampoNr);
       	if (gvtKampoNr == null) request.setAttribute("gvtKampoNr", "");
       	else request.setAttribute("gvtKampoNr", gvtKampoNr);
       	Constants.Println(request, "kampo nr after setting:  " + request.getAttribute("gvtKampoNr"));
       	
        GyvenamojiVieta gvt = new GyvenamojiVieta();
        if (TYPE_TER.equals(paramType)){
        	gvt.setGvtAtvNr(new Long(currentAdr));
        }
        else {
        	gvt.setGvtAdvNr(new Long(currentAdr));
        	if (!gvtKampoNr.equals(""))
        		gvt.setGvtKampoNr(new Long(gvtKampoNr));
        }
        String stringAddress = QueryDelegator.getInstance().getAddressString(request, gvt);
    	request.setAttribute("stringAddress", stringAddress);
    	Constants.Println(request, "" + stringAddress + "  " + gvt.getGvtAdvNr());

    	Map addressMap = QueryDelegator.getInstance().getFlatAddress(request, gvt);
    	request.setAttribute("step", new Integer(addressMap.size()+1));
    	
    	// Iðimtis: kaime ir name gali bûti galima deklaruoti gyvenamàjà vietà, net jei jie yra 
    	// suskirstyti á smulkesnius vienetus
    	// TODO: reikëtø perdayti, kad nebûtø tikrinama pagal pavadinimà!
    	if (addressMap.size() > 0){
    		String lastItem = (String)addressMap.keySet().toArray()[addressMap.keySet().size()-1];
    		if ("Kaimas".equals(lastItem) || "Namo nr.".equals(lastItem)){
    			request.setAttribute("allowSelect", new Boolean(true));
    			
    		}
    	}
    	
        return mapping.findForward("continue");
    }
}
