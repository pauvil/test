 package com.algoritmusistemos.gvdis.web;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

import javax.servlet.http.HttpServletRequest;

 
public class Constants 
{ //
	
	public static String getDB() 
	{
		String CONNECTION_STRING = null ;
		String filename = "cfg.properties";
		Properties prop = new Properties();
		InputStream input = null;
		try
		{
			input = Constants.class.getResourceAsStream("cfg.properties");
			if(input == null){
				System.out.println("Sorry, unable to find " + filename);				
			}
			prop.load(input);
			CONNECTION_STRING = prop.getProperty("DB");
			
		} catch(Exception ex)
		{
			 
		}
		finally
		{
			try{
				input.close();
			}
			catch(Exception ex){};
		}
		return CONNECTION_STRING;
	}
	
	//Jasper reportu failai
	public static final String IN_DECLARATION_REPORT_FILE = "GVDIS_deklaracija_atv.jasper"; 
	public static final String OUT_DECLARATION_REPORT_FILE = "GVDIS_deklaracija_isv.jasper";
	public static final String GVNA_DECLARATION_REPORT_FILE = "GVDIS_deklaracija_gvna.jasper";
	
	//TODO atkomentuoti prieð siunèiant i production
	//GVDIS production connection nuo 2009-12-14   
	//VRM connection i VRM realia DB  
	// production jdbc:oracle:thin:@10.202.2.10:1521:LGR 
	//public static final String CONNECTION_STRING = "jdbc:oracle:thin:@10.202.2.10:1521:LGR";
	 // public static final String CONNECTION_STRING = "jdbc:oracle:thin:@192.168.15.21:1521:tam";
	
	
	//Versija iskelta i Version.java
	
	//Asmens statuso reiksmes
	public static final String ASMUO_STATUS_ISVYKES = "ISVYKES";
	public static final String ASMUO_STATUS_DEKLARUOTAS = "DEKLARUOTAS";
	public static final String ASMUO_STATUS_MIRES = "MIRES";
	public static final String ASMUO_STATUS_GVNA = "GVNA";
	public static final String ASMUO_STATUS_NA = "NO_USER";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final int ADULT_AGE = 16;
	public static final int FULL_ADULT_AGE = 18;

	/////////////////////////////////
	public static final String CONTINUE = "continue";
	public static final String CONTINUE_IN = "continueIN";	
	public static final String CONTINUE_OUT = "continueOUT";	
	public static final String CONTINUE_GVNA = "continueGVNA";
	public static final String ALREADY_SAVED = "ALREADY_SAVED";
	public static final String CANNOT_DELETE_DECLARATION = "CANNOT_DELETE_DECLARATION";
	public static final String CANNOT_DELETE_TEMP_CITIZEN = "CANNOT_DELETE_TEMP_CITIZEN";
	public static final String CANNOT_EDIT_DECLARATION = "CANNOT_EDIT_DECLARATION";
	
	public static final String APPLICATION = "GVDIS";
	public static final String EXCEPTION = "exception";
	
	public static final String GENERATE_PDF = "GENERATE_PDF";
	
	/////////////////////////////////   WEB
	public static final String CENTER_STATE = "CENTER_STATE";	
	public static final String CENTER_STATE_OLD = "CENTER_STATE_OLD";	
	public static final String IN_DECLARATION_CODE_FORM = "IN_DECLARATION_CODE_FORM";
	public static final String OUT_DECLARATION_CODE_FORM = "OUT_DECLARATION_CODE_FORM";	
	public static final String CHNG_OUT_DECLARATION_CODE_FORM = "CHNG_OUT_DECLARATION_CODE_FORM";	
	public static final String GVNA_DECLARATION_CODE_FORM = "GVNA_DECLARATION_CODE_FORM";	
	public static final String TEMP_CITIZEN_DECLARATION = "TEMP_CITIZEN_DECLARATION";
	
	public static final String IN_DECLARATION_FORM = "IN_DECLARATION_FORM";
	public static final String OUT_DECLARATION_FORM = "OUT_DECLARATION_FORM";
	public static final String CHNG_OUT_DECLARATION_FORM = "CHNG_OUT_DECLARATION_FORM";
	public static final String GVNA_DECLARATION_FORM = "GVNA_DECLARATION_FORM";	

	public static final String IN_DECLARATION_FORM_FILL_MESSAGE = "IN_DECLARATION_FORM_FILL_MESSAGE";
	public static final String OUT_DECLARATION_FORM_FILL_MESSAGE = "OUT_DECLARATION_FORM_FILL_MESSAGE";
	public static final String CHNG_OUT_DECLARATION_FORM_FILL_MESSAGE = "CHNG_OUT_DECLARATION_FORM_FILL_MESSAGE";	
	public static final String GVNA_DECLARATION_FORM_FILL_MESSAGE = "GVNA_DECLARATION_FORM_FILL_MESSAGE";	
	
	public static final String NOT_COMPLETED_DECLARTIONS_LIST = "NOT_COMPLETED_DECLARTIONS_LIST";	
	
	public static final String CHANGE_PASSWORD_FORM = "CHANGE_PASSWORD_FORM";
	public static final String CHANGE_PASSWORD_DONE = "CHANGE_PASSWORD_DONE";

    public static final String QUERY_PERSON_FORM = "QUERY_PERSON_FORM";
    public static final String QUERY_PERSON_RESULTS = "QUERY_PERSON_RESULTS";
    public static final String QUERY_ADDRESS_FORM = "QUERY_ADDRESS_FORM";
    public static final String QUERY_ADDRESS_RESULTS = "QUERY_ADDRESS_RESULTS";
    public static final String QUERY_VIEW_DATA = "QUERY_VIEW_DATA";
    public static final String QUERY_EDIT_DATA = "QUERY_EDIT_DATA";
    public static final String QUERY_CREATE_DATA = "QUERY_CREATE_DATA";
    
    public static final String ADMIN_LIST_ISTAIGOS = "ADMIN_LIST_ISTAIGOS";
    public static final String ADMIN_EDIT_ISTAIGA_FORM = "ADMIN_EDIT_ISTAIGA_FORM";
    public static final String ADMIN_LIST_ZINYNAI = "ADMIN_LIST_ZINYNAI";
    public static final String ADMIN_LIST_ZINYNO_REIKSMES = "ADMIN_LIST_ZINYNO_REIKSMES";
    public static final String ADMIN_CREATE_ZINYNO_REIKSME_FORM = "ADMIN_CREATE_ZINYNO_REIKSME_FORM";
    public static final String ADMIN_EDIT_ZINYNO_REIKSME_FORM = "ADMIN_EDIT_ZINYNO_REIKSME_FORM";
    
    //2011-11-11 Tieto: GVDIS Aprasymas
    public static final String ADMIN_GVDIS_APRASYMAS = "ADMIN_GVDIS_APRASYMAS"; 
    public static final String ADMIN_GVDIS_APRASYMAS_JN = "ADMIN_GVDIS_APRASYMAS_JN"; 
    public static final String ADMIN_GVDIS_APRASYMAS_EDIT = "ADMIN_GVDIS_APRASYMAS_EDIT";
    public static final String ADMIN_GVDIS_APRASYMAS_EDIT_DB = "ADMIN_GVDIS_APRASYMAS_EDIT_DB"; 
    public static final String ADMIN_GVDIS_APRASYMAS_CREATE = "ADMIN_GVDIS_APRASYMAS_CREATE";
    
    
    public static final String SPR_LIST_SPRENDIMAI = "SPR_LIST_SPRENDIMAI";
    public static final String SPR_VIEW_SPRENDIMAS = "SPR_VIEW_SPRENDIMAS";
    public static final String SPR_CREATE_SPRENDIMAS_FORM = "SPR_CREATE_SPRENDIMAS_FORM";
    public static final String SPR_EDIT_SPRENDIMAS_FORM = "SPR_EDIT_SPRENDIMAS_FORM";
    public static final String SPR_LIST_PRASYMAI = "SPR_LIST_PRASYMAI";
    public static final String SPR_VIEW_PRASYMAS = "SPR_VIEW_PRASYMAS";
    public static final String SPR_CREATE_PRASYMAS_FORM = "SPR_CREATE_PRASYMAS_FORM";
    public static final String SPR_EDIT_PRASYMAS_FORM = "SPR_EDIT_PRASYMAS_FORM";
    public static final String SPR_ADD_PERSON_FORM = "SPR_ADD_PERSON_FORM";
    public static final String SPR_ADD_PERSON_RESULTS = "SPR_ADD_PERSON_RESULTS";
    
    public static final String JOURNAL = "JOURNAL";
	public static final String REPORT = "REPORT";
	public static final String REPORT11 = "REPORT11";
	public static final String REPORT13 = "REPORT11";
	
	public static final String PAZ_GVNA_FORM = "PAZ_GVNA_FORM";
	public static final String PAZ_GVNA = "PAZ_GVNA";
	public static final String PAZ_GV_FORM = "PAZ_GV_FORM";
	public static final String PAZ_GV = "PAZ_GV";
	public static final String PAZ_SAV_FORM = "PAZ_SAV_FORM";
	public static final String PAZ_SAV = "PAZ_SAV";
	public static final Object PAZ_MIRE_FORM = "PAZ_MIRE_FORM";	
	public static final Object PAZ_MIRE = "PAZ_MIRE";
	public static final String PAZ_ISVYKE_FORM = "PAZ_ISVYKE_FORM";
	public static final String PAZ_ISVYKE = "PAZ_ISVYKE";
	
    public static final String ADDRESS_BROWSER = "ADDRESS_BROWSER";
    public static final String ADDR_BROWS_IST = "FIRST";

    public static final String TEMP_CITIZENS = "TEMP_CITIZENS";
    public static final String TEMP_CITIZEN = "TEMP_CITIZEN";
    public static final String LINK_WITH_GR = "LINK_WITH_GR";
    public static final String LINK_WITH_GR_PERFORM = "LINK_WITH_GR_PERFORM";   
    public static final String COMPARE_PERSON = "COMPARE_PERSON";    
    
    public static final String HAVE_NOT_COMPLETED_DECLARATION = "HAVE_NOT_COMPLETED_DECLARATION";
    public static final String CONTINUE_WARNING = "CONTINUE_WARNING";  
    public static final String IN_DECLARATION_VIEW = "IN_DECLARATION_VIEW"; 
    public static final String OUT_DECLARATION_VIEW = "OUT_DECLARATION_VIEW";     
    public static final String GVNA_DECLARATION_VIEW = "GVNA_DECLARATION_VIEW";    
    //
    public static final int IS_NOT_RESIDENT = 0; 
    public static final int IS_RESIDENT = 1;    
    //
    // Audito kodai
    public static final String AUDIT_ASM_KODAS = "AK";
    public static final String AUDIT_SPREND_PERZIURA = "SV";
    public static final String AUDIT_SPREND_REDAGAVIMAS = "SE";
    public static final String AUDIT_GVNA_PAZ_PERZIURA = "GN";
    public static final String AUDIT_GV_PAZ_PERZIURA = "GV";
    public static final String AUDIT_SAV_PAZ_PERZIURA = "GS";
    public static final String AUDIT_DEKL_PERZIURA = "DV";
    public static final String AUDIT_DEKL_REDAGAVIMAS = "DE";
    
    // Help kodai    
    public static final String HELP_CODE = "HELP_CODE";
   
    public static final String HLP_GVDIS_DEFAULT = "HLP_GVDIS_DEFAULT";     
    public static final String HLP_GVDIS_VIEW_RESIDENCE_DATA = "HLP_GVDIS_VIEW_RESIDENCE_DATA";    
    public static final String HLP_GVDIS_REGISTER_DECLARATION = "HLP_GVDIS_REGISTER_DECLARATION";    
    public static final String HLP_GVDIS_INFORMATION_ABOUT_DECLARED_RESIDENCE = "HLP_GVDIS_INFORMATION_ABOUT_DECLARED_RESIDENCE";    
    public static final String HLP_GVDIS_REGISTER_GVNA_REQUEST = "HLP_GVDIS_REGISTER_GVNA_REQUEST";
    public static final String HLP_GVDIS_GVNA_INFORMATION = "HLP_GVDIS_GVNA_INFORMATION";    
    public static final String HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE = "HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE";
    public static final String HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE = "HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE";    
    public static final String HLP_GVDIS_MANAGE_AND_PRINT_JUORNALS = "HLP_GVDIS_MANAGE_AND_PRINT_JUORNALS";
    public static final String HLP_GVDIS_VIEW_STATISTICS = "HLP_GVDIS_VIEW_STATISTICS";    
    public static final String HLP_GVDIS_EDIT_RESIDENCE_DATA_ERRORS = "HLP_GVDIS_EDIT_RESIDENCE_DATA_ERRORS";    
    public static final String HLP_GVDIS_GV_PAZ = "HLP_GVDIS_GV_PAZ";    
    public static final String HLP_GVDIS_GVNA_PAZ = "HLP_GVDIS_GVNA_PAZ";    
    public static final String HLP_GVDIS_GVNA = "HLP_GVDIS_GVNA";    
    public static final String HLP_GVDIS_ASSIGN_GR = "HLP_GVDIS_ASSIGN_GR";    
    public static final String HLP_GVDIS_UNFINISHED_DECLARATIONS = "HLP_GVDIS_UNFINISHED_DECLARATIONS";
    public static final String HLP_GVDIS_MIRE_PAZ = "HLP_GVDIS_MIRE_PAZ";
    public static final Object SPR_ADD_PRASYMAS = "SPR_ADD_PRASYMAS";
    public static final Object SPR_ADD_PRASYMAS_RESULT = "SPR_ADD_PRASYMAS_RESULT";	
	public static final String HLP_GVDIS_ISVYKE_PAZ = "HLP_GVDIS_ISVYKE_PAZ";
	public static final String HLP_GVDIS_WEB_DEKL = "HLP_GVDIS_WEB_DEKL";	
	
	
	
	
	public static final String DEKLARACIJA_INTERNETU = "DEKLARACIJA_INTERNETU";
	public static final String CONFIRM_DEKLARACIJA = "CONFIRM_DEKLARACIJA";
	public static final String REJECT_DEKLARACIJA = "REJECT_DEKLARACIJA";
	
	public static final Object TEST_MODE = "TEST_MODE";
	public static final String TEST_IN_DECLARATION_VIEW = "TEST_IN_DECLARATION_VIEW";
	public static final Object TEST_ENCODING = "TEST_ENCODING";

	public static void Println(HttpServletRequest request, String msg){
		String login = (String)request.getSession().getAttribute("userLogin");
		System.out.println(login +": "+ msg);
	}

	
}