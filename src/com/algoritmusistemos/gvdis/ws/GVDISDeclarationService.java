/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.algoritmusistemos.gvdis.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AsmensDokumentas;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvdisBase;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.TeritorinisVienetas;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;


/**
 *
 * @author t.zulgis
 */
public class GVDISDeclarationService {
	 
	private HttpServletRequest request;
	private final String invalidDate = "2007-07-01";
	
	private void Connect(){
		MessageContext context = MessageContext.getCurrentContext();
    	request = (HttpServletRequest)context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    	
    	String useris = java.lang.System.getProperty("username");
    	String password = java.lang.System.getProperty("password");
    	System.out.println("useris:"+useris);
    	System.out.println("password:"+password);
    	
    	
    	request.getSession().setAttribute("loginname", useris);
    	request.getSession().setAttribute("password", password);
    	
    	HttpSession session = request.getSession();

   	
		session.setAttribute("userLogin", useris);
		session.setAttribute("userPassword", password);
		
		//Tieto 2012-04-19;
		//Kartais gaunama klaida: java.sql.SQLException: Closed Connection;
		//Pries apdorojant duomenis, padaromas recconect.
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//Connection conn = DriverManager.getConnection(Constants.CONNECTION_STRING, useris, password);
			Connection conn = DriverManager.getConnection(Constants.getDB(), useris, password);
			HibernateUtils.currentSession(request).reconnect(conn);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		try {
		
		UserDelegator.getInstance().initUserInModule(request, useris);
		Set roles = UserDelegator.getInstance().getUserRoles(request, useris);
		
		String[] istaiga = UserDelegator.getInstance().getDarbIstaiga(request);
		
		session.setAttribute("userRoles", roles);
		session.setAttribute("userIstaigaId", Long.valueOf(istaiga[0]));
		session.setAttribute("userIstaiga", istaiga[1]);
		
		String[] darbuotojas = UserDelegator.getInstance().getDarbuotojas(request);
		session.setAttribute("userName", darbuotojas[0]);
		session.setAttribute("userSurname", darbuotojas[1]);

		int userStatus = UserDelegator.getInstance().getUserStatus(request);
		session.setAttribute("userStatus", new Integer(userStatus));

		List valstybes = UtilDelegator.getInstance().getValstybes(request);
		session.setAttribute("salys", valstybes);
		List valstybesbelietuvos = UtilDelegator.getInstance().getValstybesBeLietuvos(request);
		session.setAttribute("salysbelietuvos", valstybesbelietuvos);		
		List pilietybes = UtilDelegator.getInstance().getPilietybes(request);
		session.setAttribute("pilietybes", pilietybes);
		List pilietybesbenull = UtilDelegator.getInstance().getPilietybesBeNull(request);
		session.setAttribute("pilietybesbenull", pilietybesbenull);
		
       
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	private void Session(){
		HibernateUtils.currentSession(request).clear();
		HibernateUtils.currentSession(request).flush();
	}
	
	public GVDISDeclarationService () {
		Connect();
	}
	
	public SavePlaceOfDeclarationResult SavePlaceOfDeclaration(
    		String ankstesnePavarde,
			String pilietybe,
			String asmensKodas, //asmens kodas.
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentaIsdave,
			String asmensDokumentoIsdavimoData,
			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String ankstesneGyvenamojiVietaAdresas,
		    String ankstesneGyvenamojiVietaId,

			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioPapildomai,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoData,
			
			String rysisSuGv,
			String rysysSuGvKita,//0,1,2,3
			String savininkoTipas,//0,1,2,3
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoData,		
			
			String addressId,
			String addressType,
			String processId,
			
			String deklaracijosGaliojimoData,
			String unikalusNr,
			String telefonoNr,
			String elektroninisPastoAdresas
		){
		
		HibernateUtils.currentSession(request).setCacheMode(org.hibernate.CacheMode.IGNORE);
        SavePlaceOfDeclarationResult result = new SavePlaceOfDeclarationResult();
          
        result.setResult(new Boolean(true));
        
		//Tieto 2012-04-10
        String[] _asmensDokumentoIsdavimoDataArray = {null,null,null};
		String[] _dokumentoGaliojimasArray = {null,null,null};
		String[] _savininkoParasoDataArray = {null,null,null};
		String[] _deklaracijosGaliojimoDataArray = {null,null,null};
		String[] _atvykimoDataArray = {null,null,null};
		Timestamp _deklaravimoData;
		String[] _deklaravimoDataArray = {null,null,null};
		Long istaiga = null;
		String namoKampoNr = "";
		GvdisBase asmuo = null;
		
        try {
        	//START - tikrinimas
            /*if (isEmpty(pilietybe) 
            	|| isEmpty(asmensKodas)
            	|| isEmpty(asmensDokumentoRusis) 
            	|| isEmpty(asmensDokumentoNumeris) 
            	|| isEmpty(asmensDokumentaIsdave)
            	|| isEmpty(atvykimoData) 
            	|| isEmpty(deklaracijaPateikta) 
            	|| isEmpty(pateikejoVardas) 
            	|| isEmpty(pateikejoPavarde) 
            	|| isEmpty(deklaravimoData) 
            	|| isEmpty(rysisSuGv) 
            	|| isEmpty(savininkoTipas) 
            	|| isEmpty(addressId) 
            	|| isEmpty(addressType) 
            	|| isEmpty(processId)) 
            {           	           	
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas vienas ar keli butini laukai");
    			result.setError_code("1");
    			return result;
            }//END*/
            //V.L. 2009-10-16
        	//Gaudom vietotà kurioje kas nors neuþpildyta
            if (isEmpty(pilietybe))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - pilietybe");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensKodas))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensKodas");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensDokumentoRusis))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensDokumentoRusis");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensDokumentoNumeris))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensDokumentoNumeris");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensDokumentaIsdave))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensDokumentaIsdave");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(atvykimoData))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - atvykimoData");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(deklaracijaPateikta))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - deklaracijaPateikta");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(pateikejoVardas))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - pateikejoVardas");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(pateikejoPavarde))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - pateikejoPavarde");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(deklaravimoData))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - deklaravimoData");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(rysisSuGv))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - rysisSuGv");
    			result.setError_code("1");
    			return result;
            }
            //System.out.println("savininkoTipas:"+savininkoTipas);
            if (isEmpty(savininkoTipas))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - savininkoTipas");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(addressId))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - addressId");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(addressType))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - addressType");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(processId))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - processId");
    			result.setError_code("1");
    			return result;
            }
            //
            //START - tikrinimas
            if (this.isDateBefore(deklaravimoData)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Deklaravimo data turi buti didesnë uþ "+this.invalidDate);
    			result.setError_code("1");
    			return result;
            }//END

          //START - tikrinimas
            /*
            if (this.isDateBefore(deklaracijosGaliojimoData)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Deklaracijos galiojimo data turi buti didesnë uþ "+this.invalidDate);
    			result.setError_code("1");
    			return result;
            }//END
            */
            /*
             * Tieto 2012-04-10: 
             * Pakeista: reiksmes: ankstesneGyvenamojiVieta, ankstesneGyvenamojiVietaAdresas, ankstesneGyvenamojiVietaId
             * yra neprivalomos.
            //START - tikrinimas
            if (ankstesneGyvenamojiVieta.equals("0") && (isEmpty(ankstesneGyvenamojiVietaAdresas) || isEmpty(ankstesneGyvenamojiVietaId))) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas ankstesneGyvenamojiVietaAdresas arba ankstesneGyvenamojiVietaId laukai");
    			result.setError_code("1");
    			return result;
            }else if (ankstesneGyvenamojiVieta.equals("1") && isEmpty(atvykoIsUzsienioSalis)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas atvykoIsUzsienioSalis laukas");
    			result.setError_code("1");
    			return result;
            }else if (ankstesneGyvenamojiVieta.equals("2") && isEmpty(kitaGyvenamojiVietaAprasymas)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas kitaGyvenamojiVietaAprasymas laukas");
    			result.setError_code("1");
    			return result;
            }else if (Long.parseLong(ankstesneGyvenamojiVieta) < 0 || Long.parseLong(ankstesneGyvenamojiVieta) > 2) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Lauko ankstesneGyvenamojiVieta galimos reikðmës:  0, 1, 2");
    			result.setError_code("1");
    			return result;
            }//END
            */
            
            if (ankstesneGyvenamojiVieta.equals("")){
            	ankstesneGyvenamojiVieta = "0";
            }

            //START - tikrinimas
            if (rysisSuGv.equals("3") && isEmpty(rysysSuGvKita)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas rysysSuGvKita laukas");
    			result.setError_code("1");
    			return result;
            }//END
            
        	new Long(Long.parseLong(asmensKodas));
        	Asmuo asmuo1 = QueryDelegator.getInstance().getAsmuoByCode(request, asmensKodas);
        	
        	//START - tikrinimas
    		if (asmuo1.getAsmMirtiesData() != null) {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Ðis asmuo yra miræs. Deklaracijos ávesti jam negalima");
    			result.setError_code("1");
    			return result;
    		}//END
    		
    		if("1".equals(ankstesneGyvenamojiVieta))
    		{
    			String salis = AdresaiDelegator.getInstance().checkDeklaruotaVietaIsLietuva (Long.toString(asmuo1.getAsmNr()),request);
    			if(null != salis)
    			{
    				if("LTU".equals(salis ))
    				{
    					if(!"LTU".equals(atvykoIsUzsienioSalis ))
    					{
    		    			result.setResult(new Boolean(false)); 
    		    			result.setError_message("Asmuo nëra deklaravæs iðvykimo á ðalá ið kurios atvyksta");
    		    			result.setError_code("2");
    		    			return result;   						
    					}
    				}
    			}
    		}
    		
    		//START - tikrinimas
    		/*Tieto 2012-04-10
    		if (ankstesneGyvenamojiVieta.equals("0")) {
	    		String msg = AdresaiDelegator.getInstance().checkAsmAdress(Long.toString(asmuo1.getAsmNr()), ankstesneGyvenamojiVietaAdresas, ankstesneGyvenamojiVietaId, request);
	    		if (msg != null) {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message(msg);
	    			result.setError_code("1");
	    			return result;
	    		}
	    		
	    		if (Long.parseLong(ankstesneGyvenamojiVietaAdresas) == Long.parseLong(addressId)) {
	    			result.setResult(new Boolean(false)); 
	    			//result.setError_message("Aktualus adresas negali sutapti su naujuoju");
	    			//result.setError_code("1");
	    			result.setError_message("Gyvenamoji vieta ðiuo adresu jau yra deklaruota");
	    			result.setError_code("11");

	    			return result;
	    		}
    		}//END
        	*/
    		
    		//Tieto: 2012-05-03
    		//Patikrinama ar nesutampa naujai deklaruojama gyvenamoji vieta su dabartine gyvenamaja vieta.
    		//Jei sutampa grazinamas klaidos pranesimas
    		if (ankstesneGyvenamojiVieta.equals("0")) {
    			try {
    				Set gyvenamosiosVietos = asmuo1.getGyvenamosiosVietos();
    				GyvenamojiVieta tempVieta = null;
    				GyvenamojiVieta vieta = null;
    				long nr = 0;
    				for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) {
    					tempVieta = (GyvenamojiVieta) it.next();
    					if(tempVieta.getGvtNr() > nr && tempVieta.getGvtDataIki() == null ){
    						nr = tempVieta.getGvtNr();
    						vieta = tempVieta;
    					}					
    				}
    				if (vieta != null && "R".equals(vieta.getGvtTipas())) {
    					if (addressType.equals("T")
    							&& addressId.equals(String.valueOf(vieta.getGvtAtvNr()))) {
    						result.setResult(new Boolean(false)); 
    	    				result.setError_message("Gyvenamoji vieta ðiuo adresu jau yra deklaruota");
    	    				result.setError_code("11");
    	    				return result;

    					}
    					if (addressType.equals("A")
    							&& addressId.equals(String.valueOf(vieta.getGvtAdvNr()))) {
    						result.setResult(new Boolean(false)); 
    	    				result.setError_message("Gyvenamoji vieta ðiuo adresu jau yra deklaruota");
    	    				result.setError_code("11");
    	    				return result;

    					}
    				}
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}	

    		asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(asmensKodas),request);
        	AsmensDokumentas asmDok = null;
    		asmDok = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, asmensDokumentoNumeris, asmuo1);
    		
    		if ("GL".equalsIgnoreCase(asmensDokumentoRusis)){    				
    			if (isEmpty(asmensDokumentoNumeris) || isEmpty(asmensDokumentaIsdave) || isEmpty(asmensDokumentoIsdavimoData)){ 
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message("Neuþpildytas vienas ar keli butini laukai");
	    			result.setError_code("1");
	    			return result;
    			}
    			asmensDokumentoRusis = "Kitas";
    			Timestamp asmDokIsdavimoData = CalendarUtils.string2Timestamp(asmensDokumentoIsdavimoData);
    			_asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(asmDokIsdavimoData);
    		} else {
	    		if (asmDok != null) {
	    			asmensDokumentoRusis = asmDok.getDokRusis();
	    			asmensDokumentoNumeris = asmDok.getDokNum();
	    			asmensDokumentaIsdave = asmDok.getTarnyba();
	    			
	    			if (asmDok.getDokIsdData() != null) _asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokIsdData().getTime()));
	    			if (asmDok.getDokGaliojaIki() != null) _dokumentoGaliojimasArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokGaliojaIki().getTime()));
	    		} else {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message("Toks asmens dokumentas nerastas");
	    			result.setError_code("1");
	    			return result;
	    		}
    		}
    		
    		// Nepilnametis negali deklaruoti pats
    		if ("0".equals(deklaracijaPateikta) && chkNepilnametis(asmuo1.getAsmGimData(), Constants.ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepilnametis asmuo negali pateikti deklaracijos asmeniðkai");
       			result.setError_code("1");
    			return result;
    		}	

    		if ("1".equals(deklaracijaPateikta) && !chkNepilnametis(asmuo1.getAsmGimData(), Constants.FULL_ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Pilnametis asmuo deklaracijà turi pateikti asmeniðkai");
       			result.setError_code("12");
    			return result;
    		}
    		
    		Timestamp _atvykimoData = CalendarUtils.string2Timestamp(atvykimoData);
    		_atvykimoDataArray = CalendarUtils.getDateFromTimestamp(_atvykimoData);
    		
    		_deklaravimoData = CalendarUtils.string2Timestamp(deklaravimoData);
			_deklaravimoDataArray = CalendarUtils.getDateFromTimestamp(_deklaravimoData);
			
			if (!isEmpty(savininkoParasoData)) {
				Timestamp _savininkoParasoData = CalendarUtils.string2Timestamp(savininkoParasoData);
				_savininkoParasoDataArray = CalendarUtils.getDateFromTimestamp(_savininkoParasoData);
			}

			//Long istaiga = null;//DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress(new Long(addressId), request);
			if (addressType.equals("A"))
				// I.N.
				istaiga = DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress(null, new Long(addressId), request);
			else if (addressType.equals("T")) 
				istaiga = DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress(new Long(addressId), null, request);
			

    		if (istaiga == null) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepavyko nustatyti ástaigos");
       			result.setError_code("1");
    			return result;    			
    		}
			if (!isEmpty(deklaracijosGaliojimoData)) {
				Timestamp _deklaracijosGaliojimoData = CalendarUtils.string2Timestamp(deklaracijosGaliojimoData);
				_deklaracijosGaliojimoDataArray = CalendarUtils.getDateFromTimestamp(_deklaracijosGaliojimoData);
			}
			String unikalusNr1 = "";
			String unikalusNr2 = "";
			String unikalusNr3 = "";
			String unikalusNr4 = "";
			if (null != unikalusNr){
				String uniqHouseCode = unikalusNr;
				int startBruksniukas = uniqHouseCode.indexOf("-");
				unikalusNr1 = uniqHouseCode.substring(0,startBruksniukas);
			 	int pabaigaBruksniukas = uniqHouseCode.indexOf("-", startBruksniukas + 1 );
			 	unikalusNr2 = uniqHouseCode.substring(startBruksniukas +1 ,pabaigaBruksniukas);
			 	startBruksniukas = pabaigaBruksniukas + 1;
			 	pabaigaBruksniukas = uniqHouseCode.indexOf(":", startBruksniukas);	 
			 	if(pabaigaBruksniukas < 0)
			 		unikalusNr3 = uniqHouseCode.substring(startBruksniukas, uniqHouseCode.length());
			 	else
			 		unikalusNr3 = uniqHouseCode.substring(startBruksniukas, pabaigaBruksniukas);
			 	startBruksniukas = pabaigaBruksniukas + 1;
			 	pabaigaBruksniukas = uniqHouseCode.indexOf(":");	
			 	if(pabaigaBruksniukas > 0)
			 		unikalusNr4 = uniqHouseCode.substring(startBruksniukas, uniqHouseCode.length());
			}
			
    		namoKampoNr = AdresaiDelegator.getInstance().getNamoKampoNr(addressId, request);

			AtvykimoDeklaracija atvdkl = DeklaracijosDelegator.getInstance(request).saveAtvDeclaration(asmuo, ankstesnePavarde, pilietybe, asmensDokumentoRusis, asmensDokumentoNumeris, 
					_asmensDokumentoIsdavimoDataArray[0], _asmensDokumentoIsdavimoDataArray[1], _asmensDokumentoIsdavimoDataArray[2], asmensDokumentaIsdave, 
					_dokumentoGaliojimasArray[0], _dokumentoGaliojimasArray[1], _dokumentoGaliojimasArray[2], _atvykimoDataArray[0], _atvykimoDataArray[1], _atvykimoDataArray[2], 
					pastabos, ankstesneGyvenamojiVieta, kitaGyvenamojiVietaAprasymas, atvykoIsUzsienioSalis, atvykoIsUzsienioPapildomai, deklaracijaPateikta, pateikejoVardas, pateikejoPavarde, 
					_deklaravimoDataArray[0], _deklaravimoDataArray[1], _deklaravimoDataArray[2], "0", rysisSuGv, rysysSuGvKita, savininkoTipas, savininkoIgaliotinis, 
					jurAsmKodas, jurAsmPavadinimas, jurAsmAdresas, savVardas, savAsmKodas, jurGyvenamojiVieta, 
					_savininkoParasoDataArray[0], _savininkoParasoDataArray[1], _savininkoParasoDataArray[2], addressId, addressType, namoKampoNr,
					 telefonoNr, elektroninisPastoAdresas, savVardas, savAsmKodas, unikalusNr1, unikalusNr2, unikalusNr3, unikalusNr4, _deklaracijosGaliojimoDataArray[0], _deklaracijosGaliojimoDataArray[1], _deklaracijosGaliojimoDataArray[2],
					 true, true, processId, istaiga, request);
		
			
			//DeklaracijosDelegator.getInstance(request).updateAtvDeklaracijaForWs(atvdkl,processId, istaiga, request);
			
		} catch (ParseException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Klaidingas datos formatas");
			result.setError_code("1");
		} catch (NumberFormatException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Skaièiuje rasta neleistinø simboliø");
			result.setError_code("1");
			//System.out.println("1 Skaièiuje rasta neleistinø simboliø");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ObjectNotFoundException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			e.printStackTrace();
			result.setError_code("0");
		} catch (DatabaseException e) {
			result.setResult(new Boolean(false));
			result.setError_code("0");
			if (e.getCause() instanceof ConstraintViolationException) {
				SQLException sql = ((ConstraintViolationException) e.getCause()).getSQLException();
				result.setError_message(sql.getMessage());
			} else if (e.getCause() instanceof GenericJDBCException) {
				//SQLException sql = ((GenericJDBCException) e.getCause()).getSQLException();
				//result.setError_message(sql.getMessage());
				//Tieto 2012-04-10
				SQLException sql = ((GenericJDBCException) e.getCause()).getSQLException();
				if(sql.getErrorCode() == 20003){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiam asmeniui deklaracija jau yra pateikta.");
					result.setError_code("13");
				}
				else if(sql.getErrorCode() == 20004){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiandien ðiam asmeniui deklaracija jau yra pateikta." +
											" Negalima pateikti kitos deklaracijos tà paèià dienà.");
					result.setError_code("14");
				}
				else{
					result.setError_message(sql.getMessage());
				}				
			} else result.setError_message(e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			result.setError_code("0");
		}
		Session();
        return result;
    }

    //deprecated 2015-12-23
	/*
	public SavePlaceOfDeclarationResult SavePlaceOfDeclaration(
    		String ankstesnePavarde,
			String pilietybe,
			String asmensKodas, //asmens kodas.
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentaIsdave,
			String asmensDokumentoIsdavimoData,
			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String ankstesneGyvenamojiVietaAdresas,
		    String ankstesneGyvenamojiVietaId,

			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioPapildomai,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoData,
			
			String rysisSuGv,
			String rysysSuGvKita,//0,1,2,3
			String savininkoTipas,//0,1,2,3
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoData,		
			
			String addressId,
			String addressType,
			String processId
		){
		
		HibernateUtils.currentSession(request).setCacheMode(org.hibernate.CacheMode.IGNORE);
        SavePlaceOfDeclarationResult result = new SavePlaceOfDeclarationResult();
          
        result.setResult(new Boolean(true));
        
		//Tieto 2012-04-10
        String[] _asmensDokumentoIsdavimoDataArray = {null,null,null};
		String[] _dokumentoGaliojimasArray = {null,null,null};
		String[] _savininkoParasoDataArray = {null,null,null};
		String[] _atvykimoDataArray = {null,null,null};
		Timestamp _deklaravimoData;
		String[] _deklaravimoDataArray = {null,null,null};
		Long istaiga = null;
		String namoKampoNr = "";
		GvdisBase asmuo = null;
		
        try {
        	//START - tikrinimas
            /*if (isEmpty(pilietybe) 
            	|| isEmpty(asmensKodas)
            	|| isEmpty(asmensDokumentoRusis) 
            	|| isEmpty(asmensDokumentoNumeris) 
            	|| isEmpty(asmensDokumentaIsdave)
            	|| isEmpty(atvykimoData) 
            	|| isEmpty(deklaracijaPateikta) 
            	|| isEmpty(pateikejoVardas) 
            	|| isEmpty(pateikejoPavarde) 
            	|| isEmpty(deklaravimoData) 
            	|| isEmpty(rysisSuGv) 
            	|| isEmpty(savininkoTipas) 
            	|| isEmpty(addressId) 
            	|| isEmpty(addressType) 
            	|| isEmpty(processId)) 
            {           	           	
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas vienas ar keli butini laukai");
    			result.setError_code("1");
    			return result;
            }//END*/
            //V.L. 2009-10-16
        	//Gaudom vietotà kurioje kas nors neuþpildyta
	/*
            if (isEmpty(pilietybe))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - pilietybe");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensKodas))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensKodas");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensDokumentoRusis))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensDokumentoRusis");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensDokumentoNumeris))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensDokumentoNumeris");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(asmensDokumentaIsdave))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - asmensDokumentaIsdave");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(atvykimoData))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - atvykimoData");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(deklaracijaPateikta))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - deklaracijaPateikta");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(pateikejoVardas))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - pateikejoVardas");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(pateikejoPavarde))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - pateikejoPavarde");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(deklaravimoData))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - deklaravimoData");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(rysisSuGv))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - rysisSuGv");
    			result.setError_code("1");
    			return result;
            }
            //System.out.println("savininkoTipas:"+savininkoTipas);
            if (isEmpty(savininkoTipas))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - savininkoTipas");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(addressId))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - addressId");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(addressType))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - addressType");
    			result.setError_code("1");
    			return result;
            }
            if (isEmpty(processId))
            {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas butinas laukas - processId");
    			result.setError_code("1");
    			return result;
            }
            //
            //START - tikrinimas
            if (this.isDateBefore(deklaravimoData)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Deklaravimo data turi buti didesnë uþ "+this.invalidDate);
    			result.setError_code("1");
    			return result;
            }//END

            /*
             * Tieto 2012-04-10: 
             * Pakeista: reiksmes: ankstesneGyvenamojiVieta, ankstesneGyvenamojiVietaAdresas, ankstesneGyvenamojiVietaId
             * yra neprivalomos.
            //START - tikrinimas
            if (ankstesneGyvenamojiVieta.equals("0") && (isEmpty(ankstesneGyvenamojiVietaAdresas) || isEmpty(ankstesneGyvenamojiVietaId))) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas ankstesneGyvenamojiVietaAdresas arba ankstesneGyvenamojiVietaId laukai");
    			result.setError_code("1");
    			return result;
            }else if (ankstesneGyvenamojiVieta.equals("1") && isEmpty(atvykoIsUzsienioSalis)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas atvykoIsUzsienioSalis laukas");
    			result.setError_code("1");
    			return result;
            }else if (ankstesneGyvenamojiVieta.equals("2") && isEmpty(kitaGyvenamojiVietaAprasymas)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas kitaGyvenamojiVietaAprasymas laukas");
    			result.setError_code("1");
    			return result;
            }else if (Long.parseLong(ankstesneGyvenamojiVieta) < 0 || Long.parseLong(ankstesneGyvenamojiVieta) > 2) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Lauko ankstesneGyvenamojiVieta galimos reikðmës:  0, 1, 2");
    			result.setError_code("1");
    			return result;
            }//END
            */
            /*
            if (ankstesneGyvenamojiVieta.equals("")){
            	ankstesneGyvenamojiVieta = "0";
            }

            //START - tikrinimas
            if (rysisSuGv.equals("3") && isEmpty(rysysSuGvKita)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas rysysSuGvKita laukas");
    			result.setError_code("1");
    			return result;
            }//END
            
        	new Long(Long.parseLong(asmensKodas));
        	Asmuo asmuo1 = QueryDelegator.getInstance().getAsmuoByCode(request, asmensKodas);
        	
        	//START - tikrinimas
    		if (asmuo1.getAsmMirtiesData() != null) {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Ðis asmuo yra miræs. Deklaracijos ávesti jam negalima");
    			result.setError_code("1");
    			return result;
    		}//END
    		
    		if("1".equals(ankstesneGyvenamojiVieta))
    		{
    			String salis = AdresaiDelegator.getInstance().checkDeklaruotaVietaIsLietuva (Long.toString(asmuo1.getAsmNr()),request);
    			if(null != salis)
    			{
    				if("LTU".equals(salis ))
    				{
    					if(!"LTU".equals(atvykoIsUzsienioSalis ))
    					{
    		    			result.setResult(new Boolean(false)); 
    		    			result.setError_message("Asmuo nëra deklaravæs iðvykimo á ðalá ið kurios atvyksta");
    		    			result.setError_code("2");
    		    			return result;   						
    					}
    				}
    			}
    		}
    		
    		//START - tikrinimas
    		/*Tieto 2012-04-10
    		if (ankstesneGyvenamojiVieta.equals("0")) {
	    		String msg = AdresaiDelegator.getInstance().checkAsmAdress(Long.toString(asmuo1.getAsmNr()), ankstesneGyvenamojiVietaAdresas, ankstesneGyvenamojiVietaId, request);
	    		if (msg != null) {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message(msg);
	    			result.setError_code("1");
	    			return result;
	    		}
	    		
	    		if (Long.parseLong(ankstesneGyvenamojiVietaAdresas) == Long.parseLong(addressId)) {
	    			result.setResult(new Boolean(false)); 
	    			//result.setError_message("Aktualus adresas negali sutapti su naujuoju");
	    			//result.setError_code("1");
	    			result.setError_message("Gyvenamoji vieta ðiuo adresu jau yra deklaruota");
	    			result.setError_code("11");

	    			return result;
	    		}
    		}//END
        	*/
    		/*
    		//Tieto: 2012-05-03
    		//Patikrinama ar nesutampa naujai deklaruojama gyvenamoji vieta su dabartine gyvenamaja vieta.
    		//Jei sutampa grazinamas klaidos pranesimas
    		if (ankstesneGyvenamojiVieta.equals("0")) {
    			try {
    				Set gyvenamosiosVietos = asmuo1.getGyvenamosiosVietos();
    				GyvenamojiVieta tempVieta = null;
    				GyvenamojiVieta vieta = null;
    				long nr = 0;
    				for (Iterator it = gyvenamosiosVietos.iterator(); it.hasNext();) {
    					tempVieta = (GyvenamojiVieta) it.next();
    					if(tempVieta.getGvtNr() > nr && tempVieta.getGvtDataIki() == null ){
    						nr = tempVieta.getGvtNr();
    						vieta = tempVieta;
    					}					
    				}
    				if (vieta != null && "R".equals(vieta.getGvtTipas())) {
    					if (addressType.equals("T")
    							&& addressId.equals(String.valueOf(vieta.getGvtAtvNr()))) {
    						result.setResult(new Boolean(false)); 
    	    				result.setError_message("Gyvenamoji vieta ðiuo adresu jau yra deklaruota");
    	    				result.setError_code("11");
    	    				return result;

    					}
    					if (addressType.equals("A")
    							&& addressId.equals(String.valueOf(vieta.getGvtAdvNr()))) {
    						result.setResult(new Boolean(false)); 
    	    				result.setError_message("Gyvenamoji vieta ðiuo adresu jau yra deklaruota");
    	    				result.setError_code("11");
    	    				return result;

    					}
    				}
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}	

    		asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(asmensKodas),request);
        	AsmensDokumentas asmDok = null;
    		asmDok = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, asmensDokumentoNumeris, asmuo1);
    		
    		if ("GL".equalsIgnoreCase(asmensDokumentoRusis)){    				
    			if (isEmpty(asmensDokumentoNumeris) || isEmpty(asmensDokumentaIsdave) || isEmpty(asmensDokumentoIsdavimoData)){ 
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message("Neuþpildytas vienas ar keli butini laukai");
	    			result.setError_code("1");
	    			return result;
    			}
    			asmensDokumentoRusis = "Kitas";
    			Timestamp asmDokIsdavimoData = CalendarUtils.string2Timestamp(asmensDokumentoIsdavimoData);
    			_asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(asmDokIsdavimoData);
    		} else {
	    		if (asmDok != null) {
	    			asmensDokumentoRusis = asmDok.getDokRusis();
	    			asmensDokumentoNumeris = asmDok.getDokNum();
	    			asmensDokumentaIsdave = asmDok.getTarnyba();
	    			
	    			if (asmDok.getDokIsdData() != null) _asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokIsdData().getTime()));
	    			if (asmDok.getDokGaliojaIki() != null) _dokumentoGaliojimasArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokGaliojaIki().getTime()));
	    		} else {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message("Toks asmens dokumentas nerastas");
	    			result.setError_code("1");
	    			return result;
	    		}
    		}
    		
    		// Nepilnametis negali deklaruoti pats
    		if ("0".equals(deklaracijaPateikta) && chkNepilnametis(asmuo1.getAsmGimData(), Constants.ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepilnametis asmuo negali pateikti deklaracijos asmeniðkai");
       			result.setError_code("1");
    			return result;
    		}	

    		if ("1".equals(deklaracijaPateikta) && !chkNepilnametis(asmuo1.getAsmGimData(), Constants.FULL_ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Pilnametis asmuo deklaracijà turi pateikti asmeniðkai");
       			result.setError_code("12");
    			return result;
    		}
    		
    		Timestamp _atvykimoData = CalendarUtils.string2Timestamp(atvykimoData);
    		_atvykimoDataArray = CalendarUtils.getDateFromTimestamp(_atvykimoData);
    		
    		_deklaravimoData = CalendarUtils.string2Timestamp(deklaravimoData);
			_deklaravimoDataArray = CalendarUtils.getDateFromTimestamp(_deklaravimoData);
			
			if (!isEmpty(savininkoParasoData)) {
				Timestamp _savininkoParasoData = CalendarUtils.string2Timestamp(savininkoParasoData);
				_savininkoParasoDataArray = CalendarUtils.getDateFromTimestamp(_savininkoParasoData);
			}

			//Long istaiga = null;//DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress(new Long(addressId), request);
			if (addressType.equals("A"))
				// I.N.
				istaiga = DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress(null, new Long(addressId), request);
			else if (addressType.equals("T")) 
				istaiga = DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress(new Long(addressId), null, request);
			

    		if (istaiga == null) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepavyko nustatyti ástaigos");
       			result.setError_code("1");
    			return result;    			
    		}

    		namoKampoNr = AdresaiDelegator.getInstance().getNamoKampoNr(addressId, request);

			AtvykimoDeklaracija atvdkl = DeklaracijosDelegator.getInstance(request).saveAtvDeclaration(asmuo, ankstesnePavarde, pilietybe, asmensDokumentoRusis, asmensDokumentoNumeris, 
					_asmensDokumentoIsdavimoDataArray[0], _asmensDokumentoIsdavimoDataArray[1], _asmensDokumentoIsdavimoDataArray[2], asmensDokumentaIsdave, 
					_dokumentoGaliojimasArray[0], _dokumentoGaliojimasArray[1], _dokumentoGaliojimasArray[2], _atvykimoDataArray[0], _atvykimoDataArray[1], _atvykimoDataArray[2], 
					pastabos, ankstesneGyvenamojiVieta, kitaGyvenamojiVietaAprasymas, atvykoIsUzsienioSalis, atvykoIsUzsienioPapildomai, deklaracijaPateikta, pateikejoVardas, pateikejoPavarde, 
					_deklaravimoDataArray[0], _deklaravimoDataArray[1], _deklaravimoDataArray[2], "0", rysisSuGv, rysysSuGvKita, savininkoTipas, savininkoIgaliotinis, 
					jurAsmKodas, jurAsmPavadinimas, jurAsmAdresas, savVardas, savAsmKodas, jurGyvenamojiVieta, 
					_savininkoParasoDataArray[0], _savininkoParasoDataArray[1], _savininkoParasoDataArray[2], addressId, addressType, namoKampoNr, false, true, processId, istaiga, request);
		
			
			//DeklaracijosDelegator.getInstance(request).updateAtvDeklaracijaForWs(atvdkl,processId, istaiga, request);
			
		} catch (ParseException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Klaidingas datos formatas");
			result.setError_code("1");
		} catch (NumberFormatException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Skaièiuje rasta neleistinø simboliø");
			result.setError_code("1");
			//System.out.println("1 Skaièiuje rasta neleistinø simboliø");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ObjectNotFoundException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			e.printStackTrace();
			result.setError_code("0");
		} catch (DatabaseException e) {
			result.setResult(new Boolean(false));
			result.setError_code("0");
			if (e.getCause() instanceof ConstraintViolationException) {
				SQLException sql = ((ConstraintViolationException) e.getCause()).getSQLException();
				result.setError_message(sql.getMessage());
			} else if (e.getCause() instanceof GenericJDBCException) {
				//SQLException sql = ((GenericJDBCException) e.getCause()).getSQLException();
				//result.setError_message(sql.getMessage());
				//Tieto 2012-04-10
				SQLException sql = ((GenericJDBCException) e.getCause()).getSQLException();
				if(sql.getErrorCode() == 20003){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiam asmeniui deklaracija jau yra pateikta.");
					result.setError_code("13");
				}
				else if(sql.getErrorCode() == 20004){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiandien ðiam asmeniui deklaracija jau yra pateikta." +
											" Negalima pateikti kitos deklaracijos tà paèià dienà.");
					result.setError_code("14");
				}
				else{
					result.setError_message(sql.getMessage());
				}				
			} else result.setError_message(e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			result.setError_code("0");
		}
		Session();
        return result;
    }
    */
    
    public SavePlaceOfForeignDeclarationResult SavePlaceOfForeignDeclaration (
    		String ankstesnePavarde,
			String pilietybe,//kodas is 3 radziu pvz LTU, RUS
			String asmensKodas, //asmens kodas.
			String asmensDokumentoRusis,
			String asmensDokumentoNumeris,
			String asmensDokumentaIsdave,
			String asmensDokumentoIsdavimoData,
			
			String pastabos,
			String isvykimoData,
			
			String ankstesneGyvenamojiVieta, //galimos reiksmes: 0 - (is GYVENAMOSIOS_VIETOS), 1- atvyko is uzsienio (is VALSTYBES), 2 - kita (laukas ANKSTESNE_VIETA_KITA)
			String ankstesneGyvenamojiVietaAdresas,
		    String ankstesneGyvenamojiVietaId,
			String kitaGyvenamojiVietaAprasymas,
			String numatomaUzsienioSalis,//atvykoIsUzsienioSalis,//kodas is 3 radziu pvz LTU, RUS
			String numatomaUzsienioSalisPlaciau,//atvykoIsUzsienioPastabos,
			
			String deklaracijaPateikta,//galimos reiksmes: 0 - Asmeniskai, 1 - Vienas is tevu (iteviu), 2 - Globejas (rupintojas), 3 - Kitas teisetas atstovas
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoData,
			
			String deklaravimoData,
			
			String telefonoNr,
			String elektroninisPastoAdresas,
			
			String processId
    	) {
		HibernateUtils.currentSession(request).setCacheMode(org.hibernate.CacheMode.IGNORE);
    	SavePlaceOfForeignDeclarationResult result = new SavePlaceOfForeignDeclarationResult();
    	result.setResult(new Boolean(true));
    	
        try {
        	//Solver tikrinimas testams TODO: issaiskinus istrinti!
        	String errStr = "( ";
        	if (isEmpty(pilietybe))	errStr += "pilietybe ";
        	if (isEmpty(asmensKodas)) errStr += "asmensKodas ";
        	if (isEmpty(asmensDokumentoRusis)) errStr += "asmensDokumentoRusis ";
        	if (isEmpty(asmensDokumentoNumeris)) errStr += "asmensDokumentoNumeris ";
        	if (isEmpty(asmensDokumentaIsdave)) errStr += "asmensDokumentaIsdave ";
        	if (isEmpty(isvykimoData)) errStr += "isvykimoData ";
        	if (isEmpty(numatomaUzsienioSalis)) errStr += "numatomaUzsienioSalis ";
        	if (isEmpty(deklaracijaPateikta)) errStr += "deklaracijaPateikta ";
        	if (isEmpty(pateikejoVardas)) errStr += "pateikejoVardas ";
        	if (isEmpty(pateikejoPavarde)) errStr += "pateikejoPavarde ";
        	if (isEmpty(pateikimoData)) errStr += "pateikimoData ";
        	if (isEmpty(deklaravimoData)) errStr += "deklaravimoData ";
        	if (isEmpty(processId)) errStr += "processId ";
        	errStr += " )";
        	// Solver tikrinimas pabaiga
        	
    		//START - tikrinimas
        	if (isEmpty(pilietybe) || isEmpty(asmensKodas) || isEmpty(asmensDokumentoRusis) || isEmpty(asmensDokumentoNumeris) || isEmpty(asmensDokumentaIsdave) ||
        		isEmpty(isvykimoData) || isEmpty(numatomaUzsienioSalis) ||
        		isEmpty(deklaracijaPateikta) || isEmpty(pateikejoVardas) || isEmpty(pateikejoPavarde) || isEmpty(pateikimoData) ||
        		isEmpty(deklaravimoData) || isEmpty(processId)) {
        		result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas vienas ar keli butini laukai: " + errStr);
    			result.setError_code("1");
    			return result;
        	}//END
        	
        	 //START - tikrinimas
            if (this.isDateBefore(pateikimoData)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Pateikimo data turi buti didesnë uþ "+this.invalidDate);
    			result.setError_code("1");
    			return result;
            }//END
            
        	//START - tikrinimas
            if (ankstesneGyvenamojiVieta.equals("0") && (isEmpty(ankstesneGyvenamojiVietaAdresas) || isEmpty(ankstesneGyvenamojiVietaId))) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas ankstesneGyvenamojiVietaAdresas arba ankstesneGyvenamojiVietaId laukai");
    			result.setError_code("1");
    			return result;
            }/*else if (ankstesneGyvenamojiVieta.equals("2") && isEmpty(kitaGyvenamojiVietaAprasymas)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas kitaGyvenamojiVietaAprasymas laukas");
    			result.setError_code("1");
    			return result;
            }*/
	
            else if (Long.parseLong(ankstesneGyvenamojiVieta) != 0 && Long.parseLong(ankstesneGyvenamojiVieta) != 2) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Lauko ankstesneGyvenamojiVieta galimos reikðmës: 0, 2");
    			result.setError_code("1");
    			return result;
            }//END
            
    		new Long(Long.parseLong(asmensKodas)); // I.N.
    		Asmuo asmuo1 = QueryDelegator.getInstance().getAsmuoByCode(request, asmensKodas);
    		
    		//START - tikrinimas
    		if (asmuo1.getAsmMirtiesData() != null) {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Ðis asmuo yra miræs. Deklaracijos ávesti jam negalima");
    			result.setError_code("1");
    			return result;
    		}//END
    		
    		//V.L. SATRT
    		//Tikrinam ar yra deklaruota GV
    		//	(tikrinimas analogiskas AsmKodasForm.java)
    		GyvenamojiVieta vt = null;		
	    	GyvenamojiVieta vtTikrinimui = null;
	    	GyvenamojiVieta vtPaskutineRegistruota = null;
	    	try {
				Set gv = asmuo1.getGyvenamosiosVietos();
	        	for (Iterator it=gv.iterator(); it.hasNext(); ){
	        		vt = (GyvenamojiVieta)it.next();
	        		if (vtPaskutineRegistruota==null){
	        			//pgl hibernate sarisi asmuo-gv, paskutine gv visada pirma
	        			vtPaskutineRegistruota = vt;
	        		}
	        		if (vt.getGvtDataIki() == null){
	        			vtTikrinimui = vt;
	        			break;
	        		}
	        	}
				if (vtTikrinimui!=null) {
					if ("U".equals(vtTikrinimui.getGvtTipas()) || "V".equals(vtTikrinimui.getGvtTipas())) {
						result.setResult(new Boolean(false)); 
		    			result.setError_message("Ðis asmuo negali pildyti iðvykimo deklaracijos, nes jau yra iðvykæs á uþsiená");
		    			result.setError_code("1");
		    			return result;
					}
				}
				else {
					if (vtPaskutineRegistruota!=null){
						if ("U".equals(vtPaskutineRegistruota.getGvtTipas()) || "V".equals(vtPaskutineRegistruota.getGvtTipas())) {
							result.setResult(new Boolean(false)); 
			    			result.setError_message("Ðis asmuo negali pildyti iðvykimo deklaracijos, nes jau yra iðvykæs á uþsiená");
			    			result.setError_code("1");
			    			return result;	
						}
					}
					else {
						result.setResult(new Boolean(false)); 
		    			result.setError_message("Ðis asmuo negali pildyti iðvykimo deklaracijos, nes nëra deklaravæs gyvenamosios vietos");
		    			result.setError_code("1");
		    			return result;
					}
				}
				
			}
	    	catch (NullPointerException npe) {
	    		result.setResult(new Boolean(false)); 
    			result.setError_message("Negaliojantis asmens kodas");
    			result.setError_code("1");
    			return result;
	    	}
    		//V.L. END 
    		
    		GvdisBase asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(asmensKodas),request);
    		AsmensDokumentas asmDok = null;
    		asmDok = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, asmensDokumentoNumeris, asmuo1);
    		
    		//START - tikrinimas
    		if (ankstesneGyvenamojiVieta.equals("0")) {
	    		String msg = AdresaiDelegator.getInstance().checkAsmAdress(Long.toString(asmuo1.getAsmNr()), ankstesneGyvenamojiVietaAdresas, ankstesneGyvenamojiVietaId, request);
	    		if (msg != null) {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message(msg);
	    			result.setError_code("1");
	    			return result;
	    		}
    		}//END
    		
    		String[] _asmensDokumentoIsdavimoDataArray = {null,null,null};
    		String[] _dokumentoGaliojimasArray = {null,null,null};
    		
    		if ("GL".equalsIgnoreCase(asmensDokumentoRusis)){    				
    			if (isEmpty(asmensDokumentoNumeris) || isEmpty(asmensDokumentaIsdave) || isEmpty(asmensDokumentoIsdavimoData)){ 
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message("Neuþpildytas vienas ar keli butini laukai");
	    			result.setError_code("1");
	    			return result;
    			}
    			asmensDokumentoRusis = "Kitas";
    			Timestamp asmDokIsdavimoData = CalendarUtils.string2Timestamp(asmensDokumentoIsdavimoData);
    			_asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(asmDokIsdavimoData);
    		} else {    
	    		if (asmDok != null) {
	    			asmensDokumentoRusis = asmDok.getDokRusis();
	    			asmensDokumentoNumeris = asmDok.getDokNum();
	    			asmensDokumentaIsdave = asmDok.getTarnyba();
	    			
	    			if (asmDok.getDokIsdData() != null) _asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokIsdData().getTime()));
	    			if (asmDok.getDokGaliojaIki() != null) _dokumentoGaliojimasArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokGaliojaIki().getTime()));
	    		}else {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message("Toks asmens dokumentas nerastas");
	    			result.setError_code("1");
	    			return result;
	    		}
    		}
    		
    		// Nepilnametis negali deklaruoti pats
    		if ("0".equals(deklaracijaPateikta) && chkNepilnametis(asmuo1.getAsmGimData(), Constants.ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepilnametis asmuo negali pateikti deklaracijos asmeniðkai");
       			result.setError_code("1");
    			return result;
    		}	

    		if ("1".equals(deklaracijaPateikta) && !chkNepilnametis(asmuo1.getAsmGimData(), Constants.FULL_ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Pilnametis asmuo deklaracijà turi pateikti asmeniðkai");
       			result.setError_code("12");
    			return result;
    		}
    		
    		
        	Timestamp _isvykimoData = CalendarUtils.string2Timestamp(isvykimoData);
    		String[] _isvykimoDataArray = CalendarUtils.getDateFromTimestamp(_isvykimoData);
    		
    		Timestamp _pateikimoData = CalendarUtils.string2Timestamp(pateikimoData);
    		String[] _pateikimoDataArray = CalendarUtils.getDateFromTimestamp(_pateikimoData);
    		
    		Timestamp _deklaravimoData = CalendarUtils.string2Timestamp(deklaravimoData);
			String[] _deklaravimoDataArray = CalendarUtils.getDateFromTimestamp(_deklaravimoData);

			//System.out.println("BEGIN Set istaiga");
    		Long istaigaId;

    		if (!ankstesneGyvenamojiVietaId.equals("")) { 
    			//System.out.println("BEGIN Set istaiga not null");
			istaigaId = DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress1(new Long(asmuo1.getAsmNr()), new Long(ankstesneGyvenamojiVietaId), request);
    		} else {
    			//System.out.println("BEGIN Set istaiga null");
    			istaigaId = DeklaracijosDelegator.getInstance(request).getIstaigaIdByAdress1(new Long(asmuo1.getAsmNr()), null, request); // nera ankstesnes gyvenamosios vietos
    		}
    		
    		if (istaigaId == null) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepavyko nustatyti ástaigos");
       			result.setError_code("1");
    			return result;    			
    		}
    		
			//System.out.println("END Set istaiga");
			/*
			IsvykimoDeklaracija isvdkl = DeklaracijosDelegator.getInstance(request).saveIsvDeclaration(asmuo, ankstesnePavarde, pilietybe, asmensDokumentoRusis, 
    																asmensDokumentoNumeris, _asmensDokumentoIsdavimoDataArray[0], _asmensDokumentoIsdavimoDataArray[1], 
    																_asmensDokumentoIsdavimoDataArray[2], asmensDokumentaIsdave, _dokumentoGaliojimasArray[0], _dokumentoGaliojimasArray[1], 
    																_dokumentoGaliojimasArray[2], pastabos, _isvykimoDataArray[0], _isvykimoDataArray[1], _isvykimoDataArray[2], ankstesneGyvenamojiVieta, 
    																kitaGyvenamojiVietaAprasymas, numatomaUzsienioSalis, numatomaUzsienioSalisPlaciau, deklaracijaPateikta, pateikejoVardas, 
    																pateikejoPavarde, _pateikimoDataArray[0], _pateikimoDataArray[1], _pateikimoDataArray[2], 
    																_deklaravimoDataArray[0], _deklaravimoDataArray[1], _deklaravimoDataArray[2], telefonoNr, elektroninisPastoAdresas,  
    																false, true, processId, istaigaId, request);
    		*/
    		IsvykimoDeklaracija isvdkl = DeklaracijosDelegator.getInstance(request).saveIsvDeclaration(asmuo, ankstesnePavarde, pilietybe, asmensDokumentoRusis, 
					asmensDokumentoNumeris, _asmensDokumentoIsdavimoDataArray[0], _asmensDokumentoIsdavimoDataArray[1], 
					_asmensDokumentoIsdavimoDataArray[2], asmensDokumentaIsdave, _dokumentoGaliojimasArray[0], _dokumentoGaliojimasArray[1], 
					_dokumentoGaliojimasArray[2], pastabos, _isvykimoDataArray[0], _isvykimoDataArray[1], _isvykimoDataArray[2], ankstesneGyvenamojiVieta, 
					kitaGyvenamojiVietaAprasymas, numatomaUzsienioSalis, numatomaUzsienioSalisPlaciau, deklaracijaPateikta, pateikejoVardas, 
					pateikejoPavarde, _pateikimoDataArray[0], _pateikimoDataArray[1], _pateikimoDataArray[2], 
					_deklaravimoDataArray[0], _deklaravimoDataArray[1], _deklaravimoDataArray[2], telefonoNr, elektroninisPastoAdresas,  
					true, true, processId, istaigaId, request);

			//System.out.println("BEGIN update");
    		//DeklaracijosDelegator.getInstance(request).updateIsvDeklaracijaForWs(isvdkl,processId,istaigaId, request);
			//System.out.println("END update");
		} catch (NumberFormatException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Skaièiuje rasta neleistinø simboliø");
			result.setError_code("1");
			//System.out.println("2 Skaièiuje rasta neleistinø simboliø");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ObjectNotFoundException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			e.printStackTrace();
			result.setError_code("0");
			//System.out.println("aaa1");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			result.setResult(new Boolean(false));
			result.setError_code("0");
			if (e.getCause() instanceof ConstraintViolationException) {
				SQLException sql = ((ConstraintViolationException) e.getCause()).getSQLException();
				result.setError_message(sql.getMessage());
			} else if (e.getCause() instanceof GenericJDBCException) {
				SQLException sql = ((GenericJDBCException) e.getCause()).getSQLException();
				if(sql.getErrorCode() == 20003){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiam asmeniui deklaracija jau yra pateikta.");
					result.setError_code("13");
				} else if(sql.getErrorCode() == 20004){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiandien ðiam asmeniui deklaracija jau yra pateikta." +
											" Negalima pateikti kitos deklaracijos tà paèià dienà.");
					result.setError_code("14");
				}
				else{
					result.setError_message(sql.getMessage());
				}				
			} else if(e.getCause() instanceof SQLException){
				SQLException sql = (SQLException) e.getCause();
				if(sql.getErrorCode() == 20003){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiam asmeniui deklaracija jau yra pateikta.");
					result.setError_code("13");
				} else if(sql.getErrorCode() == 20004){
					result.setResult(new Boolean(false)); 
					result.setError_message("Ðiandien ðiam asmeniui deklaracija jau yra pateikta." +
											" Negalima pateikti kitos deklaracijos tà paèià dienà.");
					result.setError_code("14");
				}
				else{
					result.setError_message(sql.getMessage());
				}				
			} else result.setError_message(e.getMessage());
			//System.out.println("aaa2");
			//System.out.println(e.getMessage());
			//e.printStackTrace();
		} catch (ParseException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Klaidingas datos formatas");
			result.setError_code("1");
			//System.out.println("aaa2");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		} /*catch (Exception e) { -- laikinai idetas nebuvo.
			System.out.println("aaa4");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}*/
	
		Session();
		return result;
    }

    
    
    
    
    
    public SaveDeclarationInHomelessRegisterResult SaveDeclarationInHomelessRegister(
    	String ankstesnePavarde,
		String pilietybe,
		String savivaldybe,
		String asmensKodas, //asmens kodas. Gali buti laikini gyventojai arba ne
		String asmensDokumentoRusis,
		String asmensDokumentoNumeris,
		String asmensDokumentaIsdave,	    		
		
		String pastabos,
		String savivaldybePastabos,
		String priezastys,			
		
		String ankstesneGyvenamojiVieta,
		String ankstesneGyvenamojiVietaAdresas,
	    String ankstesneGyvenamojiVietaId,
		String kitaGyvenamojiVietaAprasymas,
		String atvykoIsUzsienioSalis,
		String atvykoIsUzsienioPapildomai,
		
		String deklaracijaPateikta,
		String pateikejoVardas,
		String pateikejoPavarde,
		String pateikimoData,
		
		String deklaravimoData,
		String deklaracijosGaliojimoData,
		String telefonoNr,
		String elektroninisPastoAdresas,
		String processId
    	){
		HibernateUtils.currentSession(request).setCacheMode(org.hibernate.CacheMode.IGNORE);
		HibernateUtils.currentSession(request).connection();
        SaveDeclarationInHomelessRegisterResult result = new SaveDeclarationInHomelessRegisterResult();
        result.setResult(new Boolean(true));
        //iskomentuojam Algimanto Praðymu 2016-01-04
        /*
        String[] _deklaracijosGaliojimoDataArray = {null,null,null};
    	//START - tikrinimas
        if (true){
        	result.setResult(new Boolean(false)); 
			result.setError_message("Paslauga neteikiama");
			result.setError_code("1");
			return result;
        }//END     
        
        try {
        	//START - tikrinimas
            if (isEmpty(pilietybe) || isEmpty(savivaldybe) || isEmpty(asmensKodas) || isEmpty(asmensDokumentoRusis) || isEmpty(asmensDokumentoNumeris) || isEmpty(asmensDokumentaIsdave) || 
            	isEmpty(ankstesneGyvenamojiVieta) || isEmpty(deklaracijaPateikta) || 
            	isEmpty(pateikejoVardas) || isEmpty(pateikejoPavarde) || isEmpty(pateikimoData) || isEmpty(deklaravimoData) || isEmpty(processId)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas vienas ar keli butini laukai");
    			result.setError_code("1");
    			return result;
            }//END
            
            //START - tikrinimas
            if (this.isDateBefore(pateikimoData)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Pateikimo data turi buti didesnë uþ "+this.invalidDate);
    			result.setError_code("1");
    			return result;
            }//END
            
            //START - tikrinimas
            if (Long.parseLong(deklaracijaPateikta) < 0 || Long.parseLong(deklaracijaPateikta) > 1) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Lauko deklaracijaPateikta galimos reikðmës: 0, 1");
    			result.setError_code("1");
    			return result;
            }//END
            
            
            //START - tikrinimas
            if (ankstesneGyvenamojiVieta.equals("0") && (isEmpty(ankstesneGyvenamojiVietaAdresas) || isEmpty(ankstesneGyvenamojiVietaId))) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas ankstesneGyvenamojiVietaAdresas arba ankstesneGyvenamojiVietaId laukai");
    			result.setError_code("1");
    			return result;
            }else if (ankstesneGyvenamojiVieta.equals("1") && isEmpty(atvykoIsUzsienioSalis)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas atvykoIsUzsienioSalis laukas");
    			result.setError_code("1");
    			return result;
            }else if (ankstesneGyvenamojiVieta.equals("2") && isEmpty(kitaGyvenamojiVietaAprasymas)) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Neuþpildytas kitaGyvenamojiVietaAprasymas laukas");
    			result.setError_code("1");
    			return result;
            }else if (Long.parseLong(ankstesneGyvenamojiVieta) < 0 || Long.parseLong(ankstesneGyvenamojiVieta) > 2) {
            	result.setResult(new Boolean(false)); 
    			result.setError_message("Lauko ankstesneGyvenamojiVieta galimos reikðmës:  0, 1, 2");
    			result.setError_code("1");
    			return result;
            }
            
            //END
            
        	Long _asmKodas = new Long(Long.parseLong(asmensKodas));
        	Asmuo asmuo1 = QueryDelegator.getInstance().getAsmuoByCode(request, asmensKodas);
    		
        	//START - tikrinimas
    		if (asmuo1.getAsmMirtiesData() != null) {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Ðis asmuo yra miræs. Deklaracijos ávesti jam negalima");
    			result.setError_code("1");
    			return result;
    		}//END
    		
    		if(DeklaracijosDelegator.getInstance(request).isPersonHaveDeclaredResidence(request,asmuo1.getAsmNr())) {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Ðis asmuo negali pildyti GVNA praðymo, nes turi deklaruotà gyvenamàjà vietà");
    			result.setError_code("1");
    			return result;
            }
    		List savivaldybes = QueryDelegator.getInstance().getSav(request, null);
    		
    		Iterator it = savivaldybes.iterator();
    		
    		boolean _inSavList = false;
    		while (it.hasNext()) {
    			TeritorinisVienetas terV = (TeritorinisVienetas) it.next();
    			if (terV.getTerNr() == Long.parseLong(savivaldybe)) _inSavList = true;
    		}
    		
    		if (!_inSavList) {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Neteisinga savivaldybë");
    			result.setError_code("0");
    			return result;
    		}
    		
        	GvdisBase asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(asmensKodas),request);
    		AsmensDokumentas asmDok = null;
    		asmDok = DeklaracijosDelegator.getInstance(request).getAsmensDokumentas(request, asmensDokumentoNumeris, asmuo1);
    		//START - tikrinimas
    		if (ankstesneGyvenamojiVieta.equals("0")) {
	    		String msg = AdresaiDelegator.getInstance().checkAsmAdress(Long.toString(asmuo1.getAsmNr()), ankstesneGyvenamojiVietaAdresas, ankstesneGyvenamojiVietaId, request);
	    		if (msg != null) {
	    			result.setResult(new Boolean(false)); 
	    			result.setError_message(msg);
	    			result.setError_code("1");
	    			return result;
	    		}
    		}//END
    		String[] _asmensDokumentoIsdavimoDataArray = {null,null,null};
    		String[] _dokumentoGaliojimasArray = {null,null,null};
    		
    		
    		if (asmDok != null) {
    			asmensDokumentoRusis = asmDok.getDokRusis();
    			asmensDokumentoNumeris = asmDok.getDokNum();
    			asmensDokumentaIsdave = asmDok.getTarnyba();
    			
    			if (asmDok.getDokIsdData() != null) _asmensDokumentoIsdavimoDataArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokIsdData().getTime()));
    			if (asmDok.getDokGaliojaIki() != null) _dokumentoGaliojimasArray = CalendarUtils.getDateFromTimestamp(new Timestamp(asmDok.getDokGaliojaIki().getTime()));
    		}else {
    			result.setResult(new Boolean(false)); 
    			result.setError_message("Toks asmens dokumentas nerastas");
    			result.setError_code("1");
    			return result;
    		}
        	

    		// Nepilnametis negali deklaruoti pats
    		if ("0".equals(deklaracijaPateikta) && chkNepilnametis(asmuo1.getAsmGimData(), Constants.ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepilnametis asmuo negali pateikti deklaracijos asmeniðkai");
       			result.setError_code("1");
    			return result;
    		}	

    		if ("1".equals(deklaracijaPateikta) && !chkNepilnametis(asmuo1.getAsmGimData(), Constants.FULL_ADULT_AGE)) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Pilnametis asmuo deklaracijà turi pateikti asmeniðkai");
       			result.setError_code("12");
    			return result;
    		}
    		
    		
        	Timestamp _pateikimoData = CalendarUtils.string2Timestamp(pateikimoData);
    		String[] _pateikimoDataArray = CalendarUtils.getDateFromTimestamp(_pateikimoData);
    		
    		Timestamp _deklaravimoData = CalendarUtils.string2Timestamp(deklaravimoData);
			String[] _deklaravimoDataArray = CalendarUtils.getDateFromTimestamp(_deklaravimoData);
    		
    		Long istaigaId = QueryDelegator.getInstance().getIstaigaBySavivaldybe(request, new Long(savivaldybe));

    		if (istaigaId == null) {
               	result.setResult(new Boolean(false)); 
       			result.setError_message("Nepavyko nustatyti ástaigos");
       			result.setError_code("1");
    			return result;    			
    		}
    		if (!isEmpty(deklaracijosGaliojimoData)) {
				Timestamp _deklaracijosGaliojimoData = CalendarUtils.string2Timestamp(deklaracijosGaliojimoData);
				_deklaracijosGaliojimoDataArray = CalendarUtils.getDateFromTimestamp(_deklaracijosGaliojimoData);
			}
    		
    		GvnaDeklaracija gvnadkl = DeklaracijosDelegator.getInstance(request).saveGvnaDeclaration(asmuo, ankstesnePavarde, pilietybe, savivaldybe, asmensDokumentoRusis, asmensDokumentoNumeris, 
    				_asmensDokumentoIsdavimoDataArray[0], _asmensDokumentoIsdavimoDataArray[1], _asmensDokumentoIsdavimoDataArray[2], asmensDokumentaIsdave, _dokumentoGaliojimasArray[0], _dokumentoGaliojimasArray[1], 
    				_dokumentoGaliojimasArray[2], pastabos, savivaldybePastabos, priezastys, ankstesneGyvenamojiVieta, kitaGyvenamojiVietaAprasymas, atvykoIsUzsienioSalis, 
    				atvykoIsUzsienioPapildomai, deklaracijaPateikta, 
    				pateikejoVardas, pateikejoPavarde, _pateikimoDataArray[0], _pateikimoDataArray[1], 
    				_pateikimoDataArray[2], "0", _deklaravimoDataArray[0], _deklaravimoDataArray[1], _deklaravimoDataArray[2], telefonoNr, elektroninisPastoAdresas, _deklaracijosGaliojimoDataArray[0], _deklaracijosGaliojimoDataArray[1], _deklaracijosGaliojimoDataArray[2],  false, request);
    		
    		
    		DeklaracijosDelegator.getInstance(request).updateGvnaDeklaracijaForWs(gvnadkl,processId, istaigaId, request);
		} catch (NumberFormatException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Skaièiuje rasta neleistinø simboliø");
			result.setError_code("1");
			//System.out.println("3 Skaièiuje rasta neleistinø simboliø");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ObjectNotFoundException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			e.printStackTrace();
			result.setError_code("0");
		} catch (DatabaseException e) {
			result.setResult(new Boolean(false));
			if (e.getCause() instanceof ConstraintViolationException) {
				SQLException sql = ((ConstraintViolationException) e.getCause()).getSQLException();
				result.setError_message(sql.getMessage());
			} else if (e.getCause() instanceof GenericJDBCException) {
				SQLException sql = ((GenericJDBCException) e.getCause()).getSQLException();
				result.setError_message(sql.getMessage());
			} else result.setError_message(e.getMessage());
			e.printStackTrace();
			result.setError_code("0");
		} catch (ParseException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message("Klaidingas datos formatas");
			result.setError_code("0");
		} catch (NullPointerException e) {
			result.setResult(new Boolean(false)); 
			result.setError_message(e.getMessage());
			result.setError_code("0");
		}
		*/
        result.setResult(new Boolean(false));
        result.setError_message("SaveDeclarationInHomelessRegister funkcija yra iðjungta");
        result.setError_code("0");
		//Session();
        return result;
    }    
    
    private boolean isEmpty (String empty) {
    	if (empty == null || empty.length() == 0) return true;
    	else return false;
    }
    
    private boolean isDateBefore (String date) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		
		Date d1 = dateFormat.parse(date);
		Date d2 = dateFormat.parse(invalidDate);
		
		if (d1.equals(d2) || d1.before(d2)) return true;
		else return false;
	}
    
    
    private boolean chkNepilnametis(Date gimimoData, int age)
    {
    	boolean nepilnametis=false;
		if (gimimoData != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(gimimoData);
			calendar.add(Calendar.YEAR, age);
			gimimoData = calendar.getTime();
			if (gimimoData.after(new Date())){
				nepilnametis=true;
			}
		}
		return nepilnametis;
    }

    
    
}