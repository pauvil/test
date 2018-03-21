package lt.solver.gvdis.web.actions.zurnalai;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.CommonObjDAL;
import lt.solver.gvdis.dal.JournalDAL;
import lt.solver.gvdis.model.IdValueBean;
import lt.solver.gvdis.model.PagingInitReturnObj;
import lt.solver.gvdis.model.TableFieldsJournal;
import lt.solver.gvdis.model.UrlValueBean;
import lt.solver.gvdis.util.Helper;
import lt.solver.gvdis.util.JournalHelper;
import lt.solver.gvdis.util.PdfPageGenerator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.JournalForm;

public class ZurnalaiAction  extends Action{
	
	private final String ACTION_INIT = "init";
	private final String ACTION_ORDER = "order";
	private final String ACTION_PAGE = "page";
	
	private List getBusenaList(int journalType) {
		List retVal = null;
		
		if (journalType == JournalDAL.JOURNAL_TYPE_IN || 
				journalType == JournalDAL.JOURNAL_TYPE_OUT || 
				journalType == JournalDAL.JOURNAL_TYPE_GVNA){
			retVal = new ArrayList();
			retVal.add(new IdValueBean("0", "--- Visos ---"));
			retVal.add(new IdValueBean("1", "Galiojanèios deklaracijos"));
			retVal.add(new IdValueBean("2", "Nebegaliojanèios deklaracijos"));
			retVal.add(new IdValueBean("3", "Nebaigtos ávesti deklaracijos"));
			retVal.add(new IdValueBean("5", "Atmestos deklaracijos"));
		}
		if (journalType == JournalDAL.JOURNAL_TYPE_SPREND) {
			retVal = new ArrayList();
			retVal.add(new IdValueBean("-1", "--- Visi ---"));
			retVal.add(new IdValueBean("0", "Taisyti duomenis")); //1
			retVal.add(new IdValueBean("1", "Keisti duomenis")); //2
			retVal.add(new IdValueBean("2", "Naikinti duomenis")); //3
		}
		
		
		return retVal;
	}
	
	private List getDeklaracijaType(int journalType) {
		List retVal = null;
		if (journalType == JournalDAL.JOURNAL_TYPE_IN || 
				journalType == JournalDAL.JOURNAL_TYPE_OUT || 
				journalType == JournalDAL.JOURNAL_TYPE_GVNA){
			retVal = new ArrayList();
			retVal.add(new IdValueBean("0", "--- Visos ---"));
			retVal.add(new IdValueBean("1", "Popierinës deklaracijos"));
			retVal.add(new IdValueBean("2", "Elektroninës deklaracijos"));
		}
		return retVal;
	}
	
	private String setPageTitle(HttpServletRequest request, int journalType) {
		String retVal = "";
		
	       switch (journalType){
	    	case JournalDAL.JOURNAL_TYPE_IN:
	    		request.setAttribute("journalTitle", "Atvykimo deklaracijø þurnalas");
	    		break;
	    	case JournalDAL.JOURNAL_TYPE_OUT:
	    		request.setAttribute("journalTitle", "Iðvykimo deklaracijø þurnalas");
	    		break;
	    	case JournalDAL.JOURNAL_TYPE_GVNA:
	    		request.setAttribute("journalTitle", "Praðymø átraukti á GVNA apskaità þurnalas");
	    		break;
	    	case JournalDAL.JOURNAL_TYPE_GVPAZ:
	    		request.setAttribute("journalTitle", "Iðduotø paþymø apie deklaruotà GV þurnalas");
	    		break;
	    	case JournalDAL.JOURNAL_TYPE_GVNAPAZ:
	    		request.setAttribute("journalTitle", "Iðduotø paþymø apie átraukimà á GVNA apskaità þurnalas");
	    		break;
	    	case JournalDAL.JOURNAL_TYPE_SPREND:
	    		request.setAttribute("journalTitle", "Sprendimø dël deklaravimo duomenø keitimo þurnalas");
	    		break;
	    	case JournalDAL.JOURNAL_TYPE_SAVPAZ:
	    		request.setAttribute("journalTitle", "Paþymø iðduotø gyvenamosios patalpos savininkams registras");
	    		break;
       }
	       
		return retVal;
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    						throws DatabaseException, ObjectNotFoundException, InternalException
    {
		HttpSession session = request.getSession();
        session.setAttribute("CENTER_STATE", Constants.JOURNAL);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_MANAGE_AND_PRINT_JUORNALS);
       
	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
	    // Nustatome þurnalo tipà
	    int journalType = Helper.string2int((String) request.getParameter("journalType"), JournalDAL.JOURNAL_TYPE_IN);
        request.setAttribute("journalType", new Integer(journalType)); 
        
        // Puslapio antraðtë
        setPageTitle(request, journalType);

        //Nustatome jsp listus
        request.setAttribute("appBusena", getBusenaList(journalType));
        request.setAttribute("appDeklaracijosTipai", getDeklaracijaType(journalType));
        
        // Rûðiavimas, puslapiavimas
        int pageNumber = Helper.string2int((String) request.getParameter("pageNr"), 1);
        String orderby = (String) session.getAttribute("orderby"); 
        String orderDir = (String) session.getAttribute("orderdir"); 
        
        //Nustatome kas bus daroma (puslapio keitimas/initcializavimas/rusiavimas)
        String webAction = Helper.noNulls((String) request.getParameter("act"), ACTION_INIT);
        
    	Date dataNuo = null;
    	Date dataIki = null;

        JournalForm jForm = (JournalForm)form;
        //Naudojami form objekte, kai ivyksta reset'as
        session.setAttribute("rjSavivaldybe",jForm.getSavivaldybe());
        session.setAttribute("rjDataNuo",jForm.getDataNuo());
        session.setAttribute("rjDataIki",jForm.getDataIki());
        session.setAttribute("rjSeniunija",jForm.getSeniunija());
        session.setAttribute("rjValstybe",jForm.getValstybe());
        String valstybesKodas = jForm.getValstybe();
        
        String zurnaloTipasPaieska = Helper.noNulls(jForm.getDeklaracijaType(), "0");
        
        if (journalType == JournalDAL.JOURNAL_TYPE_OUT) {
        	List vlst = CommonObjDAL.getInstance().getValstybes(request);
        	session.setAttribute("journalValstybes", vlst);
        } else {
        	session.setAttribute("journalValstybes", null);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    	sdf.setLenient(false);
    	try {
    		dataNuo = sdf.parse(jForm.getDataNuo());
    		dataIki = sdf.parse(jForm.getDataIki());
    	}
    	catch (ParseException ignore) {}
    	
    	int busena = Integer.parseInt(jForm.getBusena());
    	Long savId = null, senId = null;
    	
    	
    	request.setAttribute("journalSavivaldybes", new ArrayList());
	    try { 
	    	switch (userStatus){
	    	case UserDelegator.USER_GLOBAL:
	    		if (jForm.getSavivaldybe() != null)
	    			savId = Long.valueOf(jForm.getSavivaldybe());
	    		else
	    			savId = new Long(0);
	        	// Uþkrauname savivaldybiø ir seniûnijø sàraðà
	    		request.setAttribute("journalSavivaldybes", CommonObjDAL.getInstance().getSavivaldybes(request));
	    		break;
	    	case UserDelegator.USER_SAV:
	    		savId = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	case UserDelegator.USER_UZS:
	    		savId = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	default:
	    		savId = null;
	    	}
	    } 
	    catch (NumberFormatException ignore){}
	    
	    try {  
	    	switch (userStatus){
	    	case UserDelegator.USER_SEN:
	    		senId = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	default:
	    		senId = Long.valueOf(jForm.getSeniunija());
	    	}
	    } 
	    catch (NumberFormatException ignore){}
   
    	List seniunijos = new ArrayList();
        try {
        	if(savId != null)
        		seniunijos = CommonObjDAL.getInstance().getSeniunijos(request, savId);
        }
        catch (Exception ex){
        	throw new InternalException(ex);
        }
        request.setAttribute("journalSeniunijos", seniunijos);
    	
        TableFieldsJournal th = new TableFieldsJournal();
        List results = null;
	    if (request.getAttribute("journalErrors") == null){
	    	String tableHeaderType = "";
	    	String tableType = "";
	    	String visoIrasu = "0";
	    	String visoPuslapiu = "0";
	    	TableFieldsJournal tableHeader = new TableFieldsJournal();
	    	UrlValueBean footerPaging[] = null;
	    	PagingInitReturnObj dbReturn = new PagingInitReturnObj();
	    	
        	switch (journalType){
    			case JournalDAL.JOURNAL_TYPE_IN:
    			case JournalDAL.JOURNAL_TYPE_OUT:
    			case JournalDAL.JOURNAL_TYPE_GVNA:
    				tableType = "JOURNAL_TYPE_0_1_4";
    				tableHeaderType = "laukai6";
    				tableHeader = JournalHelper.makeTableHeaderType0_1_4();
    				break;
       			case JournalDAL.JOURNAL_TYPE_GVPAZ:
       			case JournalDAL.JOURNAL_TYPE_GVNAPAZ:
       				tableType = "JOURNAL_TYPE_2_5";
    				tableHeaderType = "laukai5";
    				tableHeader = JournalHelper.makeTableHeaderType2_5();
       				break;
       			case JournalDAL.JOURNAL_TYPE_SPREND:
       				tableType = "JOURNAL_TYPE_3";
    				tableHeaderType = "laukai5";
    				tableHeader = JournalHelper.makeTableHeaderType3();
    				break;
       			case JournalDAL.JOURNAL_TYPE_SAVPAZ:
       				tableType = "JOURNAL_TYPE_6";
    				tableHeaderType = "laukai5";
    				tableHeader = JournalHelper.makeTableHeaderType6();
    				break;
        	}
        	
			if (webAction.equalsIgnoreCase(ACTION_INIT)) {
				//default rusiavimas pagal antra tab'a
				orderby = "field2";
				String defaultSortDbDir = orderDir = "desc";
		    	String defaultSortDbField = tableHeader.getDbSortColumns(orderby, defaultSortDbDir);
		    	
				//reikia initcializuoti sarasa
				dbReturn = JournalDAL.getInstance().journalInit(journalType, request, 
						defaultSortDbField, dataNuo, dataIki, busena, savId, senId, valstybesKodas,
						zurnaloTipasPaieska);
				pageNumber = 1; //rodome pirma puslapi    
				
				session.setAttribute("orderby", orderby); 
		        session.setAttribute("orderdir", orderDir); 
		        
				//issaugojame sesijoje puslpaiavimo info
				session.setAttribute("puslapiavimasInfo", dbReturn);
			} else if (webAction.equalsIgnoreCase(ACTION_PAGE)) {
				//atsisiunciamas puslapis
				if (pageNumber <= 0) pageNumber = 1; 
				dbReturn = (PagingInitReturnObj) session.getAttribute("puslapiavimasInfo");
				if (dbReturn == null) {
					//negerai
					dbReturn = new PagingInitReturnObj();
					dbReturn.setError(true);
				}
			} else if (webAction.equalsIgnoreCase(ACTION_ORDER)) {
				//keitesi rusiavimai. Nauji rusiavimai parametruose
				orderby = Helper.noNulls((String) request.getParameter("orderby"), "field2"); 
		        orderDir = Helper.noNulls((String) request.getParameter("orderdir"), "desc");
		        if (orderby == "") orderby = "desc";
		        if (orderDir == "") orderDir = "desc";
		        //
				dbReturn = JournalDAL.getInstance().changeJournalPageOrder(request, 
											tableHeader.getDbSortColumns(orderby, orderDir));
				session.setAttribute("orderby", orderby); 
		        session.setAttribute("orderdir", orderDir); 
				pageNumber = 1; 
			}
			
			//tikrinti error is DB nera prasmes, nes klaidos atveju ivyksta exception'as
			if (dbReturn.getIrasuKiekis() > 0 && !dbReturn.isError()) {
				tableHeader = makeTableHeaderUrls(tableHeader, request, orderby, orderDir, journalType);
				
				Set roles = (Set)session.getAttribute("userRoles");
				//Paimame puslapi is DB
				if ("pdf".equals(request.getParameter("output"))){ 
					//pdfui traukiame visus puslapius
					results = JournalDAL.getInstance().getJournalPage(request, -1, journalType, roles);
				} else {
					results = JournalDAL.getInstance().getJournalPage(request, pageNumber, journalType, roles);
				}
				if (results == null) {
					//neturetu taip buti...
					request.setAttribute("not_found_type", "Nerastas nei vienas áraðas");
				} else {
					visoIrasu = String.valueOf(dbReturn.getIrasuKiekis());
					visoPuslapiu = String.valueOf(dbReturn.getPuslapiuKiekis());
					//JournalPagesObj paging = new JournalPagesObj((int) dbReturn.getPuslapiuKiekis(), (int) dbReturn.getPuslapiuKiekisRodyti(),journalType);
					//paging.getPages(request, pageNumber);
					footerPaging = Helper.makeFooterPages(request, journalType, 
							(int) dbReturn.getPuslapiuKiekis(), (int) dbReturn.getPuslapiuKiekisRodyti(), 
							pageNumber);
						
				}
				
			} else {
				//nera irasu...
				results = null;
				request.setAttribute("not_found_type","Nerastas nei vienas áraðas");
			}
			
        	request.setAttribute("footer_paging", footerPaging);
        	request.setAttribute("total_records", visoIrasu);
        	request.setAttribute("total_pages", visoPuslapiu);
        	request.setAttribute("table_type", tableType);
        	request.setAttribute("table_header", tableHeader);
        	request.setAttribute("table_header_type", tableHeaderType);
        	request.setAttribute("journalResults", results);
	    }
	    else request.setAttribute("not_found_type","Nerastas nei vienas sprendimas");
	    
        if ("pdf".equals(request.getParameter("output"))){
        	outputPdf(request, response, journalType, results);
        	return null;
        }
        else {
            return mapping.findForward("continue");
        }
    }
    
	private void outputPdf(HttpServletRequest req, HttpServletResponse resp, int journalType, List results)
    {
		resp.setContentType("application/pdf");
		PdfPageGenerator gen = new PdfPageGenerator(req);
		try {
			gen.generatePDF(journalType, results, resp.getOutputStream());
		}
		catch (Exception e){
			e.printStackTrace();
		}
    }
    
	private TableFieldsJournal makeTableHeaderUrls(TableFieldsJournal tableFlds, HttpServletRequest req, 
									String orderBy, String orderDir, int journalType) {
		String url = req.getContextPath() + 
				"/journal.do?act=order&orderby={0}&orderdir={1}&journalType=" + String.valueOf(journalType); 
		TableFieldsJournal retVal = tableFlds;
		String oBy = Helper.noNulls(orderBy, "field2"); //by default
		String oDir = Helper.noNulls(orderDir, "desc"); //jeigu default, si reiksme pasikeis if'uose
		String ascGif = "asc.gif";
		String descGif = "desc.gif";
		
		String urlDir = "";
		if (oBy.equalsIgnoreCase("field1")) {
			if (oDir.equalsIgnoreCase("asc")) {
				urlDir = "desc";
				retVal.setField1OrderPix(ascGif);
			} else {
				urlDir = "asc";
				retVal.setField1OrderPix(descGif);
			}
		}
		retVal.setField1Url(MessageFormat.format(url, new Object[]{"field1", urlDir}));
		
		urlDir = "";
		if (oBy.equalsIgnoreCase("field2")) {
			if (oDir.equalsIgnoreCase("asc")) {
				urlDir = "desc";
				retVal.setField2OrderPix(ascGif);
			} else {
				urlDir = "asc";
				retVal.setField2OrderPix(descGif);
			}
		}
		retVal.setField2Url(MessageFormat.format(url, new Object[]{"field2", urlDir}));
		
		urlDir = "";
		if (oBy.equalsIgnoreCase("field3")) {
			if (oDir.equalsIgnoreCase("asc")) {
				urlDir = "desc";
				retVal.setField3OrderPix(ascGif);
			} else {
				urlDir = "asc";
				retVal.setField3OrderPix(descGif);
			}
		}
		retVal.setField3Url(MessageFormat.format(url, new Object[]{"field3", urlDir}));
		
		urlDir = "";
		if (oBy.equalsIgnoreCase("field5")) {
			if (oDir.equalsIgnoreCase("asc")) {
				urlDir = "desc";
				retVal.setField5OrderPix(ascGif);
			} else {
				urlDir = "asc";
				retVal.setField5OrderPix(descGif);
			}
		}
		retVal.setField5Url(MessageFormat.format(url, new Object[]{"field5", urlDir}));
		
		return retVal;
	}

	
}