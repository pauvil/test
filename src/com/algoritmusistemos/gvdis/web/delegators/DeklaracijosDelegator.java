package com.algoritmusistemos.gvdis.web.delegators;
 
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import java.lang.Long;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.DeklaracijosDAL;
import oracle.jdbc.OracleTypes;


import org.apache.axis.AxisFault;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;
import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.DTO.Address;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.DeklaracijaInternetuForm;
import com.algoritmusistemos.gvdis.web.persistence.AsmensDokumentas;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvdisBase;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;
import com.algoritmusistemos.gvdis.web.persistence.TeritorinisVienetas;
import com.algoritmusistemos.gvdis.web.persistence.Valstybe;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.ws.client.WebServiceUtil;
import com.algoritmusistemos.gvdis.web.persistence.Asmenvardis;

public class DeklaracijosDelegator
{
	private static DeklaracijosDelegator instance;
	
	public static DeklaracijosDelegator getInstance(HttpServletRequest request) 
	{
		//request.getSession().removeAttribute("savewserror"); // tam tikrais atvejais gali isvalyti be reikalo
		
		if (instance == null){
			instance =  new DeklaracijosDelegator();
		}
		return instance;
	}	

    private boolean chkAfterDate(Date data, int days)
    {
    	boolean busena = true;
		if (data != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);
			System.out.println(data);
			calendar.add(Calendar.DATE, days);
			data = calendar.getTime();
			System.out.println(new Date());
			if (data.after(new Date())){
				busena = false;
			}
		}
		return busena;
    }    
	
	
	public LaikinasAsmuo addLaikinasAsmuo(
			String vardas,String pavarde,
			//String kitiVardai,
			String lytis,String pilietybe,
			Timestamp gimimoData,String pastabos,
			HttpServletRequest request)
	throws DatabaseException
	{
		// LaikinasAsmuo l = null; I.N.
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();	
		LaikinasAsmuo la = null;
		try {
			Valstybe valstybe = (Valstybe)session.load(Valstybe.class,pilietybe);
			
			la = new LaikinasAsmuo();
			la.setVardas(vardas);
			la.setPavarde(pavarde);
			//la.setKitiVardai(kitiVardai);
			la.setLytis(lytis);
			la.setGimimoData(gimimoData);
			la.setPilietybe(valstybe);
			session.save(la);
			tx.commit();
			request.getSession().setAttribute("asm_id",new Long(la.getId()));
		}
		catch (Exception e){
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e);
		}
		return la;
	}
	
	
	public List getAsmenys(HttpServletRequest request)
	{
		List l = HibernateUtils.currentSession(request).createQuery("from LaikinasAsmuo la order by la.id").list();
		return l;
	}
	public Valstybe getValstybe(String id,HttpServletRequest request)
	{
		Valstybe v = (Valstybe)HibernateUtils.currentSession(request).load(Valstybe.class,id);
		return v;
	}	
	
	
	public LaikinasAsmuo getLaikinasAsmuo(Long asmid,HttpServletRequest request)
	throws DatabaseException,ObjectNotFoundException
	{
		LaikinasAsmuo asmuo = (LaikinasAsmuo)HibernateUtils.
		currentSession(request).load(LaikinasAsmuo.class,asmid);
		if(null == asmuo)
		{
			throw new ObjectNotFoundException("LaikinasAsmuo WITH ["+asmid+"] ID NOT FOUND");
		}
		else return asmuo;
	}
	public Deklaracija getDeklaracijaFromLaikinasAsmuo(long laikinasasmuoid,HttpServletRequest request)
	throws DatabaseException,ObjectNotFoundException
	{
		return (Deklaracija)HibernateUtils.currentSession(request).
		createQuery("from Deklaracija d where d.laikinasAsmuo.id = :laikinasasmuoid").
		setLong("laikinasasmuoid",laikinasasmuoid).uniqueResult();
	}
	
	/**
	 * Graþina asmenys, deklaravæ gyvenamàjà vietà, bet neregistruoti Gyventojø registre
	 */
	
	public List getTempCitizens(HttpServletRequest request,Ordering ordering, Long savivaldybe, Long seniunija)
	{   
		HttpSession session = request.getSession();
		//ju.k
		long p_id = 0;
		
		if (((Integer)session.getAttribute("userStatus")).intValue() !=UserDelegator.USER_GLOBAL) {					
	        if (seniunija != null && seniunija.longValue() > 0){
	        	p_id = seniunija.longValue();
	        }
	        else if (savivaldybe != null && savivaldybe.longValue() > 0){
	        	p_id = savivaldybe.longValue();
	        }
		}
        //
        
		return HibernateUtils.currentSession(request).
		createQuery("from LaikinasAsmuo la where la.deklaracija.busena = 1 and  ( "+p_id+" = 0 or la.deklaracija.istaiga.id = "+p_id+" or la.deklaracija.istaiga.id in (select id from Istaiga where gvist_id_tev = "+p_id+" )) "+ordering.getOrderString()).
		list();
	}	
	
	/*Deprecated 2015-12
	 * *2 sita daro commita
	 * Sukuria naujà atvykimo deklaracijà
	 * @throws DatabaseException
	 */
	public AtvykimoDeklaracija saveAtvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePav,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String atvykimoMetai,
			String atvykimoMenuo,			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			String pageidaujuDokumenta,
			
			String rysisSuGv,
			String rysysSuGvKita,
			String savininkoTipas,
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoMetai,
			String savininkoParasoMenuo,
			String savininkoParasoData,			
			
			String addressId,
			String addressType,
			String gvtKampoNr,
			
			Session session,
			String[] s,
			Istaiga ist,
			String userIstaigaId,
			boolean print,			
			boolean isCompleted,
			//internetu pateiktos deklaracijos duomenys
			boolean isInternetu,
			String procesoid,
			Long istaigaId
	)
	throws DatabaseException
	{	
		Transaction tx = session.beginTransaction();
		AtvykimoDeklaracija ad = new AtvykimoDeklaracija();
		
		
		Valstybe pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,session);
		Valstybe kitaSalis = null ;
		if(null != atvykoIsUzsienioSalis)
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,session);
		
		int resident = Constants.IS_NOT_RESIDENT;
		Asmuo asmuo = null;
		LaikinasAsmuo laikinasAsmuo = null;
		if (pasmuo instanceof LaikinasAsmuo){
			resident = Constants.IS_NOT_RESIDENT;
			laikinasAsmuo = (LaikinasAsmuo)pasmuo;
		}
		else if (pasmuo instanceof Asmuo){
			resident = Constants.IS_RESIDENT;
			asmuo = (Asmuo)pasmuo;
		}
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			Long kampnr = null;
			
			GyvenamojiVieta gv = null;
			if ("A".equals(addressType)) try {
				adrv = Long.parseLong(addressId);
				kampnr = new Long(gvtKampoNr);
			}
			catch (NumberFormatException nfe){}
			if ("T".equals(addressType)) try {
				terv = Long.parseLong(addressId);
			}
			catch (NumberFormatException nfe){}

			ad.setDeklaravimoData(CalendarUtils.getParsedDate(deklaravimoMetai,deklaravimoMenuo,deklaravimoData)); 
			
			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if (Constants.IS_RESIDENT == resident){
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), 0, session);
					ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
				}
				if (Constants.IS_NOT_RESIDENT == resident){
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(laikinasAsmuo.getId(), 0, session);						
					ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(laikinasAsmuo.getId())));
				}
				if (adr != null){
					ad.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					if(0 == adr.getId()){
						ad.setGvtAsmNrAnkstesne(null);
						ad.setGvtNrAnkstesne(null);
					}
				}
			}
			//ad.setBusena(0); 	
			//if (!isCompleted) 
			//{
				ad.setTmpGvtAdvNr(new Long(adrv));
				ad.setTmpGvtAtvNr(new Long(terv));
				ad.setTmpGvtKampoNr(kampnr);
			//}
			
			
			/*
			if (Constants.IS_NOT_RESIDENT == resident){
				if(isCompleted)ad.setBusena(1); else ad.setBusena(0);
				isCompleted = false;
			}*/
			
			if (Constants.IS_RESIDENT == resident){
				ad.setAsmuo(asmuo);
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				ad.setAsmenvardis(asmenvardis);
			}
			if (Constants.IS_NOT_RESIDENT == resident){
				ad.setLaikinasAsmuo(laikinasAsmuo);
				if ("0".equals(ankstesneGyvenamojiVieta)){
					Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), 0, session);						
					if (adr != null){
						ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						ad.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
			}

			ad.setDokumentoNr(asmensDokumentoNumeris);
			ad.setDokumentoIsdavejas(asmensDokumentoIsdave);
			ad.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			ad.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			ad.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			ad.setPateikeVardas(pateikejoVardas);
			ad.setPateikePavarde(pateikejoPavarde);
			ad.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			ad.setDokumentoRusis(asmensDokumentoRusis);
			ad.setPilietybe(pilietis);   
			ad.setAnkstesnePavarde(ankstesnePav);
			ad.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			ad.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			ad.setAtvykimoData(CalendarUtils.getParsedDate(atvykimoMetai,atvykimoMenuo,atvykimoData));
			ad.setPastabos(pastabos);
			ad.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			ad.setAnkstesneGV(kitaSalis);
			ad.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			
			long l = Long.parseLong(rysisSuGv);
			ad.setRysysSuGv(l);
			if(l == 3) ad.setRysysSuGvKita(rysysSuGvKita);
			ad.setSavininkoTipas(Long.parseLong(savininkoTipas));    		
			if("on".equals(savininkoIgaliotinis))ad.setSavininkoIgaliotinis(0);
			else ad.setSavininkoIgaliotinis(1);
			ad.setIstaiga(ist); 
			ad.setJaKodas(jurAsmKodas);
			ad.setJaPavadinimas(jurAsmPavadinimas);
			ad.setJaBuveine(jurAsmAdresas);
			ad.setFaAdresas(jurGyvenamojiVieta);
			ad.setFaKodas(savAsmKodas);
			ad.setFaVardasPavarde(savVardas);
			ad.setGavimoData(new Timestamp(System.currentTimeMillis()));
			//if(null == request.getSession().getAttribute("print"))
			ad.setSavininkoParasoData(CalendarUtils.getParsedDate(savininkoParasoMetai,savininkoParasoMenuo,savininkoParasoData));
			/*
			if(null == request.getSession().getAttribute("print"))
			ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "AD"));
			*/
			//ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "AD"));//ju.k-Zurnalo numeracijos pakeitimas 2007.08.13
		
			if(true == print)
			{	
				ad.setBusena(0);
				//Tieto: 201-11-28
				//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateAtvDeklaracijaForWs
				if(isInternetu){
					ad.setProcesoid(procesoid);
					ad.setSaltinis(new Long(1));
					ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "AD",true, istaigaId.toString()));
				}else{
					ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "AD",false, userIstaigaId));
				}

				session.save(ad);
				if (isCompleted)
				{
					if (resident == Constants.IS_RESIDENT)
					{
						String igaliotinis = "0";
						if (!"on".equals(savininkoIgaliotinis)) igaliotinis = "1";
						long savAsmKodasN = 0;
						try {
							savAsmKodasN = Long.parseLong(savAsmKodas);
						}
						catch(Exception e){}
						
						long jurAsmKodasN = 0;
						try {
							jurAsmKodasN = Long.parseLong(jurAsmKodas);
						}
						catch(Exception e){}
						
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"R",
								QueryDelegator.HOME_COUNTRY,
								ad.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"0".equals(pageidaujuDokumenta) ? "N" : "T",
								"", 
								Long.parseLong(ankstesneGyvenamojiVieta),
								null != kitaSalis?kitaSalis.getKodas():"",
								0,
								ad.getId(),
								session,
								ist.getId(),
								kampnr,
								ad.getDeklaracijaGalioja()
						);
						ad.setGyvenamojiVieta(gv);
					}

					//hsession.setAttribute("declarationComplete", "");
					ad.setBusena(1);
					session.update(ad);
				}
			}
			tx.commit();
		}
		catch (Exception e){
			e.printStackTrace();
			
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		return ad;
	}

	public AtvykimoDeklaracija saveAtvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePav,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String atvykimoMetai,
			String atvykimoMenuo,			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			String pageidaujuDokumenta,
			
			String rysisSuGv,
			String rysysSuGvKita,
			String savininkoTipas,
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoMetai,
			String savininkoParasoMenuo,
			String savininkoParasoData,			
			
			String addressId,
			String addressType,
			String gvtKampoNr,
			String telefonas,
			String email,
			
			String savininkas1,
			String savininkasKodas1,
			String unikPastatoNr1,
			String unikPastatoNr2,
			String unikPastatoNr3,
			String unikPastatoNr4,
			String deklaracijosGaliojimoMetai,
			String deklaracijosGaliojimoMenuo,
			String deklaracijosGaliojimoDiena,
			
			Session session,
			String[] s,
			Istaiga ist,
			String userIstaigaId,
			boolean print,			
			boolean isCompleted,
			//internetu pateiktos deklaracijos duomenys
			boolean isInternetu,
			String procesoid,
			Long istaigaId
	)
	throws DatabaseException
	{	
		Transaction tx = session.beginTransaction();
		AtvykimoDeklaracija ad = new AtvykimoDeklaracija();
		
		
		Valstybe pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,session);
		Valstybe kitaSalis = null ;
		if(null != atvykoIsUzsienioSalis)
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,session);
		
		int resident = Constants.IS_NOT_RESIDENT;
		Asmuo asmuo = null;
		LaikinasAsmuo laikinasAsmuo = null;
		if (pasmuo instanceof LaikinasAsmuo){
			resident = Constants.IS_NOT_RESIDENT;
			laikinasAsmuo = (LaikinasAsmuo)pasmuo;
		}
		else if (pasmuo instanceof Asmuo){
			resident = Constants.IS_RESIDENT;
			asmuo = (Asmuo)pasmuo;
		}
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			Long kampnr = null;
			
			GyvenamojiVieta gv = null;
			if ("A".equals(addressType)) try {
				adrv = Long.parseLong(addressId);
				kampnr = new Long(gvtKampoNr);
			}
			catch (NumberFormatException nfe){}
			if ("T".equals(addressType)) try {
				terv = Long.parseLong(addressId);
			}
			catch (NumberFormatException nfe){}

			ad.setDeklaravimoData(CalendarUtils.getParsedDate(deklaravimoMetai,deklaravimoMenuo,deklaravimoData)); 
			
			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if (Constants.IS_RESIDENT == resident){
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), 0, session);
					ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
				}
				if (Constants.IS_NOT_RESIDENT == resident){
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(laikinasAsmuo.getId(), 0, session);						
					ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(laikinasAsmuo.getId())));
				}
				if (adr != null){
					ad.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					if(0 == adr.getId()){
						ad.setGvtAsmNrAnkstesne(null);
						ad.setGvtNrAnkstesne(null);
					}
				}
			}
			//ad.setBusena(0); 	
			//if (!isCompleted) 
			//{
				ad.setTmpGvtAdvNr(new Long(adrv));
				ad.setTmpGvtAtvNr(new Long(terv));
				ad.setTmpGvtKampoNr(kampnr);
			//}
			
			
			/*
			if (Constants.IS_NOT_RESIDENT == resident){
				if(isCompleted)ad.setBusena(1); else ad.setBusena(0);
				isCompleted = false;
			}*/
			
			if (Constants.IS_RESIDENT == resident){
				ad.setAsmuo(asmuo);
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				ad.setAsmenvardis(asmenvardis);
			}
			if (Constants.IS_NOT_RESIDENT == resident){
				ad.setLaikinasAsmuo(laikinasAsmuo);
				if ("0".equals(ankstesneGyvenamojiVieta)){
					Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), 0, session);						
					if (adr != null){
						ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						ad.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
			}

			ad.setDokumentoNr(asmensDokumentoNumeris);
			ad.setDokumentoIsdavejas(asmensDokumentoIsdave);
			ad.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			ad.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			ad.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			ad.setPateikeVardas(pateikejoVardas);
			ad.setPateikePavarde(pateikejoPavarde);
			ad.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			ad.setDokumentoRusis(asmensDokumentoRusis);
			ad.setPilietybe(pilietis);   
			ad.setAnkstesnePavarde(ankstesnePav);
			ad.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			ad.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			ad.setAtvykimoData(CalendarUtils.getParsedDate(atvykimoMetai,atvykimoMenuo,atvykimoData));
			ad.setPastabos(pastabos);
			ad.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			ad.setAnkstesneGV(kitaSalis);
			ad.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			
			long l = Long.parseLong(rysisSuGv);
			ad.setRysysSuGv(l);
			if(l == 3) ad.setRysysSuGvKita(rysysSuGvKita);
			ad.setSavininkoTipas(Long.parseLong(savininkoTipas));    		
			if("on".equals(savininkoIgaliotinis))ad.setSavininkoIgaliotinis(0);
			else ad.setSavininkoIgaliotinis(1);
			ad.setIstaiga(ist); 
			ad.setJaKodas(jurAsmKodas);
			ad.setJaPavadinimas(jurAsmPavadinimas);
			ad.setJaBuveine(jurAsmAdresas);
			ad.setFaAdresas(jurGyvenamojiVieta);
			ad.setFaKodas(savAsmKodas);
			ad.setFaVardasPavarde(savVardas);
			ad.setGavimoData(new Timestamp(System.currentTimeMillis()));
			//if(null == request.getSession().getAttribute("print"))
			ad.setSavininkoParasoData(CalendarUtils.getParsedDate(savininkoParasoMetai,savininkoParasoMenuo,savininkoParasoData));
			/*
			if(null == request.getSession().getAttribute("print"))
			ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "AD"));
			*/
			//ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "AD"));//ju.k-Zurnalo numeracijos pakeitimas 2007.08.13
			ad.setTelefonas(telefonas);
			ad.setEmail(email);
			ad.setSavininkas1(savininkas1);
			ad.setSavininkasKodas1(savininkasKodas1);
			if(null != unikPastatoNr1 || null != unikPastatoNr2 || null != unikPastatoNr3  ){
				String unikalus = unikPastatoNr1 + "-" + unikPastatoNr2 + "-"+ unikPastatoNr3;
				if(unikPastatoNr4 != null)
					unikalus = unikalus + ":"+ unikPastatoNr4;
				ad.setUnikalusPastatoNr(unikalus);
			}
			else
			{
				ad.setUnikalusPastatoNr("");
			}
			if (ad.getRysysSuGv() > 0)
				ad.setDeklaracijaGalioja(CalendarUtils.getParsedDate(deklaracijosGaliojimoMetai,deklaracijosGaliojimoMenuo,deklaracijosGaliojimoDiena));
			if(true == print)
			{	
				ad.setBusena(0);
				//Tieto: 201-11-28
				//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateAtvDeklaracijaForWs
				if(isInternetu){
					ad.setProcesoid(procesoid);
					ad.setSaltinis(new Long(1));
					ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "AD",true, istaigaId.toString()));
				}else{
					ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "AD",false, userIstaigaId));
				}

				session.save(ad);
				if (isCompleted)
				{
					if (resident == Constants.IS_RESIDENT)
					{
						String igaliotinis = "0";
						if (!"on".equals(savininkoIgaliotinis)) igaliotinis = "1";
						long savAsmKodasN = 0;
						try {
							savAsmKodasN = Long.parseLong(savAsmKodas);
						}
						catch(Exception e){}
						
						long jurAsmKodasN = 0;
						try {
							jurAsmKodasN = Long.parseLong(jurAsmKodas);
						}
						catch(Exception e){}
						
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"R",
								QueryDelegator.HOME_COUNTRY,
								ad.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"0".equals(pageidaujuDokumenta) ? "N" : "T",
								"", 
								Long.parseLong(ankstesneGyvenamojiVieta),
								null != kitaSalis?kitaSalis.getKodas():"",
								0,
								ad.getId(),
								session,
								ist.getId(),
								kampnr,
								ad.getDeklaracijaGalioja()
						);
						ad.setGyvenamojiVieta(gv);
					}

					//hsession.setAttribute("declarationComplete", "");
					ad.setBusena(1);
					session.update(ad);
				}
			}
			tx.commit();
		}
		catch (Exception e){
			e.printStackTrace();
			
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		return ad;
	}

	/*	
	public void updateAtvDeklaracijaForWs (AtvykimoDeklaracija atvdkl,String procesoid,Long istaigaId, HttpServletRequest request) throws DatabaseException {
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		session.clear();
		session.flush();
		Transaction tx = session.beginTransaction();
		try {
			
	
			String regNr = PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "AD", istaigaId.toString());
			Istaiga istaiga = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
			session.createQuery("update AtvykimoDeklaracija atv set atv.saltinis = 1, atv.procesoid = :procesoid, atv.regNr = :regNr, atv.istaiga = :istaiga where atv.id = :id")
				.setString("procesoid", procesoid)
				.setLong("id", atvdkl.getId())
				.setString("regNr",regNr)
				.setParameter("istaiga", istaiga).executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		
	}
*/
	
	
	public void updateAtvDeklaracijaForWs (AtvykimoDeklaracija atvdkl,String procesoid,Long istaigaId, HttpServletRequest request) throws DatabaseException {
		//HttpSession hsession = request.getSession(); I.N.
		Session session = HibernateUtils.currentSession(request);
		session.clear();
		session.flush();
		Transaction tx = session.beginTransaction();
		try {
			
			AtvykimoDeklaracija atDekl = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(new Long(atvdkl.getId()), request);
			String regNr = PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "AD",true, istaigaId.toString());
			atDekl.setRegNr(regNr);
			
			
			Istaiga istaiga = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
			atDekl.setProcesoid(procesoid);
			atDekl.setIstaiga(istaiga);
			atDekl.setSaltinis(new Long(1));
			session.save(atDekl);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		session.clear();
		session.flush();
	}	
	
	/**1 jei yra atrib "print", uzdeda isComplete, kviecia 2
	 * Atnaujina egzistuojanèià atvykimo ar gyvenamosios vietos pakeitimo deklaracijà
	 * @throws DatabaseException
	 */
	public AtvykimoDeklaracija saveAtvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePav,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String atvykimoMetai,
			String atvykimoMenuo,			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			String pageidaujuDokumenta,
			
			String rysisSuGv,
			String rysysSuGvKita,
			String savininkoTipas,
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoMetai,
			String savininkoParasoMenuo,
			String savininkoParasoData,			
			
			String addressId,
			String addressType,
			String gvtKampoNr,
			
			boolean isCompleted,
			/// parametrai internetu pateikiamai deklaracijai
			boolean isInternetu,
			String procesoid,
			Long istaigaId,
			HttpServletRequest request
	)
	throws DatabaseException
	{
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		

		String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		Istaiga ist = null;// UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		String userIstaigaId = String.valueOf(request.getSession().getAttribute("userIstaigaId"));
		
		//Tieto: 201-11-28
		//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateAtvDeklaracijaForWs
		if(isInternetu){
			ist = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		} else {
			ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		}
		
		boolean print = (null == request.getSession().getAttribute("print")); 	

		Constants.Println(request, "------------ ATV ----------------- ");
		if (ist != null) { 
			Constants.Println(request, "ist "+ ist.getId());
		}
		Constants.Println(request, "userIstaigaId "+ userIstaigaId);
		Constants.Println(request, "---------------------------------- ");

		
		System.out.println("kitaGyvenamojiVietaAprasymas " + kitaGyvenamojiVietaAprasymas);		
		System.out.println("addressId " + addressId);		
		
		AtvykimoDeklaracija ad = saveAtvDeclaration(pasmuo,ankstesnePav,pilietybe,asmensDokumentoRusis,asmensDokumentoNumeris,
				asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData,
				asmensDokumentoIsdave,leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData,
				atvykimoMetai,atvykimoMenuo,atvykimoData,pastabos,				
				ankstesneGyvenamojiVieta,kitaGyvenamojiVietaAprasymas,atvykoIsUzsienioSalis,atvykoIsUzsienioTextarea,
				deklaracijaPateikta,pateikejoVardas,pateikejoPavarde,deklaravimoMetai,
				deklaravimoMenuo,deklaravimoData,pageidaujuDokumenta,				
				rysisSuGv,rysysSuGvKita,savininkoTipas,savininkoIgaliotinis,			
				jurAsmKodas,jurAsmPavadinimas,jurAsmAdresas,				
				savVardas,savAsmKodas,jurGyvenamojiVieta,				
				savininkoParasoMetai,savininkoParasoMenuo,savininkoParasoData,								
				addressId,addressType,gvtKampoNr, session,				
				s,ist, userIstaigaId,print,isCompleted,isInternetu,procesoid,istaigaId);
		if (true == print) hsession.setAttribute("declarationComplete", ""); 
		return ad;
	}
	
	public AtvykimoDeklaracija saveAtvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePav,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String atvykimoMetai,
			String atvykimoMenuo,			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			String pageidaujuDokumenta,
			
			String rysisSuGv,
			String rysysSuGvKita,
			String savininkoTipas,
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoMetai,
			String savininkoParasoMenuo,
			String savininkoParasoData,			
			
			String addressId,
			String addressType,
			String gvtKampoNr,
			
			String telefonas,
			String email,
			
			String Savininkas1,
			String SavininkasKodas1,
			String UnikPastatoNr1,
			String UnikPastatoNr2,
			String UnikPastatoNr3,
			String UnikPastatoNr4,

			String deklaracijosGaliojimoMetai,
			String deklaracijosGaliojimoMenuo,
			String deklaracijosGaliojimoDiena,
			
			
			boolean isCompleted,
			/// parametrai internetu pateikiamai deklaracijai
			boolean isInternetu,
			String procesoid,
			Long istaigaId,
			HttpServletRequest request
	)
	throws DatabaseException
	{
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		

		String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		Istaiga ist = null;// UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		String userIstaigaId = String.valueOf(request.getSession().getAttribute("userIstaigaId"));
		
		//Tieto: 201-11-28
		//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateAtvDeklaracijaForWs
		if(isInternetu){
			ist = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		} else {
			ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		}
		
		boolean print = (null == request.getSession().getAttribute("print")); 	

		Constants.Println(request, "------------ ATV ----------------- ");
		if (ist != null) { 
			Constants.Println(request, "ist "+ ist.getId());
		}
		Constants.Println(request, "userIstaigaId "+ userIstaigaId);
		Constants.Println(request, "---------------------------------- ");

		
		System.out.println("kitaGyvenamojiVietaAprasymas " + kitaGyvenamojiVietaAprasymas);		
		System.out.println("addressId " + addressId);		
		
		AtvykimoDeklaracija ad = saveAtvDeclaration(pasmuo,ankstesnePav,pilietybe,asmensDokumentoRusis,asmensDokumentoNumeris,
				asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData,
				asmensDokumentoIsdave,leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData,
				atvykimoMetai,atvykimoMenuo,atvykimoData,pastabos,				
				ankstesneGyvenamojiVieta,kitaGyvenamojiVietaAprasymas,atvykoIsUzsienioSalis,atvykoIsUzsienioTextarea,
				deklaracijaPateikta,pateikejoVardas,pateikejoPavarde,deklaravimoMetai,
				deklaravimoMenuo,deklaravimoData,pageidaujuDokumenta,				
				rysisSuGv,rysysSuGvKita,savininkoTipas,savininkoIgaliotinis,			
				jurAsmKodas,jurAsmPavadinimas,jurAsmAdresas,				
				savVardas,savAsmKodas,jurGyvenamojiVieta,				
				savininkoParasoMetai,savininkoParasoMenuo,savininkoParasoData,								
				addressId,addressType,gvtKampoNr, telefonas, email, Savininkas1,
				SavininkasKodas1,  UnikPastatoNr1, UnikPastatoNr2, UnikPastatoNr3, UnikPastatoNr4,
				deklaracijosGaliojimoMetai, deklaracijosGaliojimoMenuo, deklaracijosGaliojimoDiena, session, 			
				s,ist, userIstaigaId,print,isCompleted,isInternetu,procesoid,istaigaId);
		if (true == print) hsession.setAttribute("declarationComplete", ""); 
		return ad;
	}
	
	
	/**
	 * Atnaujina egzistuojanèià atvykimo ar gyvenamosios vietos pakeitimo deklaracijà
	 * @throws DatabaseException
	 * +boolean perform update
	 */
	public AtvykimoDeklaracija updateAtvDeclaration(
			Long declid,
			String ankstesnePav,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String atvykimoMetai,
			String atvykimoMenuo,			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			String pageidaujuDokumenta,
			
			String rysisSuGv,
			String rysysSuGvKita,
			String savininkoTipas,
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoMetai,
			String savininkoParasoMenuo,
			String savininkoParasoData,			
			
			String addressId,
			String addressType,
			String gvtKampoNr,
			
			boolean isCompleted,
			boolean performUpdate,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		
		String[] s = UserDelegator.getInstance().getDarbIstaiga(request); //atkomentuota ju.k 2008.01.10
		Istaiga ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request); //atkomentuota ju.k 2008.01.10
		Valstybe pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,request);
		
		Valstybe kitaSalis = null ;
		if(null != atvykoIsUzsienioSalis)
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,request);
		AtvykimoDeklaracija ad = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(declid,request);
		Asmuo asmuo = ad.getAsmuo();
		int resident = Constants.IS_NOT_RESIDENT;
		if (null != asmuo && null ==  ad.getLaikinasAsmuo())resident = Constants.IS_RESIDENT;
		
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			Long kampnr = null;

			ad.setDeklaravimoData(CalendarUtils.getParsedDate(deklaravimoMetai,deklaravimoMenuo,deklaravimoData));
			
			GyvenamojiVieta gv = null;
			if ("A".equals(addressType)) try {
				adrv = Long.parseLong(addressId);
				kampnr = new Long(gvtKampoNr);
			}
			catch (NumberFormatException nfe){}
			if ("T".equals(addressType)) try {
				terv = Long.parseLong(addressId);
			}
			catch (NumberFormatException nfe){}
			
			//ad.setBusena(0);
			//if (!isCompleted)
			//{ 
				ad.setTmpGvtAdvNr(new Long(adrv));
				ad.setTmpGvtAtvNr(new Long(terv));
				ad.setTmpGvtKampoNr(kampnr);
			//}			
			
			/*
			if (Constants.IS_NOT_RESIDENT == resident)
			{
				if(isCompleted)ad.setBusena(1); else ad.setBusena(0);
				isCompleted = false;
			}
			*/

			if (performUpdate){
				if (ad != null && ad.getGyvenamojiVieta() != null){
					next = ad.getGyvenamojiVieta().getGvtNr();
				}
//				Deklaracija deklaracija = DeklaracijosDelegator.getInstance().getDeklaracija(declid, request);
//				if (deklaracija != null && deklaracija.getGyvenamojiVieta() != null){
//					next = deklaracija.getGyvenamojiVieta().getGvtNr();
//				}
			}

			/*
			if ("0".equals(ankstesneGyvenamojiVieta) && 
					ad.getGvtAsmNrAnkstesne() != null &&
					ad.getGvtNrAnkstesne() != null){
				
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,request);
					if (adr != null){
						ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						ad.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != ad.getLaikinasAsmuo())
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(ad.getLaikinasAsmuo().getId(),0,request);						
						if (adr != null){
							ad.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(ad.getLaikinasAsmuo().getId())));
							ad.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}
			*/
			
			ad.setDokumentoIsdavejas(asmensDokumentoIsdave);
			ad.setDokumentoNr(asmensDokumentoNumeris);
			ad.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			ad.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			ad.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			ad.setPateikeVardas(pateikejoVardas);
			ad.setPateikePavarde(pateikejoPavarde);    		
			ad.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			ad.setDokumentoRusis(asmensDokumentoRusis);
			ad.setAnkstesnePavarde(ankstesnePav);
			ad.setPilietybe(pilietis);    		
			ad.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			ad.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			ad.setAtvykimoData(CalendarUtils.getParsedDate(atvykimoMetai,atvykimoMenuo,atvykimoData));
			ad.setPastabos(pastabos);
			ad.setAnkstesneGV(kitaSalis);
			ad.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			long l = Long.parseLong(rysisSuGv);
			ad.setRysysSuGv(l);
			if (l == 3) ad.setRysysSuGvKita(rysysSuGvKita);
			if ("on".equals(savininkoIgaliotinis)){
				ad.setSavininkoIgaliotinis(0);
			}
			else {
				ad.setSavininkoIgaliotinis(1);
			}
			//ad.setIstaiga(ist);    		

			ad.setSavininkoTipas(Long.parseLong(savininkoTipas));
			
			ad.setJaKodas(jurAsmKodas);
			ad.setJaPavadinimas(jurAsmPavadinimas);
			ad.setJaBuveine(jurAsmAdresas);
			
			ad.setFaAdresas(jurGyvenamojiVieta);
			ad.setFaKodas(savAsmKodas);
			ad.setFaVardasPavarde(savVardas);
			
			if (ad.getAsmenvardis() == null) {
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				ad.setAsmenvardis(asmenvardis);
			} else {
				ad.setAsmenvardis(ad.getAsmenvardis());
			}
			
			ad.setSavininkoParasoData(CalendarUtils.getParsedDate(savininkoParasoMetai,savininkoParasoMenuo,savininkoParasoData));
			//if(null == ad.getRegNr()) //ju.k 2007.06.27
			//ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "AD"));
			//ad.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "AD")); //ju.k - Zurnalo numeracijos pakeitimas 2007.08.13
			if(null == request.getSession().getAttribute("print"))
			{
				if (isCompleted){
					if (resident == Constants.IS_RESIDENT)
					{
						String igaliotinis = "0";
						if (!"on".equals(savininkoIgaliotinis)){
							igaliotinis = "1";
						}
						long savAsmKodasN = 0;
						
						try {
							savAsmKodasN = Long.parseLong(savAsmKodas);
						}
						catch(Exception e){}
						
						long jurAsmKodasN = 0;
						try {
							jurAsmKodasN = Long.parseLong(jurAsmKodas);
						}
						catch(Exception e){}
						
						Long nulis = new Long(0);
						ad.setTmpGvtAdvNr(nulis);
						ad.setTmpGvtKampoNr(null);
						ad.setTmpGvtAtvNr(nulis);
						
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"R",
								QueryDelegator.HOME_COUNTRY,
								ad.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"0".equals(pageidaujuDokumenta)?"N":"T",
								"", 
								Long.parseLong(ankstesneGyvenamojiVieta),
								null == kitaSalis? "":kitaSalis.getKodas(),
								performUpdate ? 1 : 0,
								ad.getId(),
								session,
								ist.getId(),
								kampnr,
								ad.getDeklaracijaGalioja()
						);
						ad.setGyvenamojiVieta(gv);
					}
					ad.setBusena(1);
					hsession.setAttribute("declarationComplete","");
				}
				
				hsession.removeAttribute("savewserror");

				//TODO: isjungti siunciami pranesimai i ivpk
				/*
				String wsRezult = "";
				if (new Long(1).equals(ad.getSaltinis())){ 
					try {
						wsRezult = WebServiceUtil.getInstance(request).changeProcessStateResultResume(ad.getProcesoid().toString(), "Deklaracija patvirtinta", "");
					} catch (AxisFault ae){
						ae.printStackTrace();
						hsession.setAttribute("savewserror", ae.getMessage());
						throw ae;
					} catch (RemoteException re){
						re.printStackTrace();
						hsession.setAttribute("savewserror", re.getMessage());
						throw re;
					}
					
					if (!"SUCCESS".equals(wsRezult))
						if (!chkAfterDate(ad.getDeklaravimoData(), 7)){ // nera 7 dienu po deklaravimo tada pranesimas
							hsession.setAttribute("savewserror", "Nepavyko nusiøsti patvirtinimo á el. valdþios portalà");
							return ad;			
						}
						else {
							hsession.removeAttribute("savewserror");
						}

				}
				*/
				session.update(ad);
			}
			tx.commit();
		}
		catch (Exception e){
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}				
		return ad;
	}	
	
	public AtvykimoDeklaracija updateAtvDeclaration(
			Long declid,
			String ankstesnePav,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String atvykimoMetai,
			String atvykimoMenuo,			
			String atvykimoData,
			String pastabos,
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			String pageidaujuDokumenta,
			
			String rysisSuGv,
			String rysysSuGvKita,
			String savininkoTipas,
			String savininkoIgaliotinis,			
			String jurAsmKodas,
			String jurAsmPavadinimas,
			String jurAsmAdresas,
			
			String savVardas,
			String savAsmKodas,
			String jurGyvenamojiVieta,
			
			String savininkoParasoMetai,
			String savininkoParasoMenuo,
			String savininkoParasoData,			
			
			String addressId,
			String addressType,
			String gvtKampoNr,
			String telefonas,
			String email,
			String deklaracijaGaliojaMetai,
			String deklaracijaGaliojaMenuo,
			String deklaracijaGaliojaDiena,
			
			String Savininkas1,
			String Savininkas2,
			String Savininkas3,
			String Savininkas4,
			String SavininkasKodas1,
			String SavininkasKodas2,
			String SavininkasKodas3,
			String SavininkasKodas4,
			String unikPastatoNr1,
			String unikPastatoNr2,
			String unikPastatoNr3,
			String unikPastatoNr4,
			
			boolean isCompleted,
			boolean performUpdate,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		
		String[] s = UserDelegator.getInstance().getDarbIstaiga(request); //atkomentuota ju.k 2008.01.10
		Istaiga ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request); //atkomentuota ju.k 2008.01.10
		Valstybe pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,request);
		
		Valstybe kitaSalis = null ;
		if(null != atvykoIsUzsienioSalis)
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,request);
		AtvykimoDeklaracija ad = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(declid,request);
		Asmuo asmuo = ad.getAsmuo();
		int resident = Constants.IS_NOT_RESIDENT;
		if (null != asmuo && null ==  ad.getLaikinasAsmuo())resident = Constants.IS_RESIDENT;
		
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			Long kampnr = null;

			ad.setDeklaravimoData(CalendarUtils.getParsedDate(deklaravimoMetai,deklaravimoMenuo,deklaravimoData));
			
			GyvenamojiVieta gv = null;
			if ("A".equals(addressType)) try {
				adrv = Long.parseLong(addressId);
				kampnr = new Long(gvtKampoNr);
			}
			catch (NumberFormatException nfe){}
			if ("T".equals(addressType)) try {
				terv = Long.parseLong(addressId);
			}
			catch (NumberFormatException nfe){}
			ad.setTmpGvtAdvNr(new Long(adrv));
			ad.setTmpGvtAtvNr(new Long(terv));
			ad.setTmpGvtKampoNr(kampnr);
			if (performUpdate){
				if (ad != null && ad.getGyvenamojiVieta() != null){
					next = ad.getGyvenamojiVieta().getGvtNr();
				}
			}
			ad.setDokumentoIsdavejas(asmensDokumentoIsdave);
			ad.setDokumentoNr(asmensDokumentoNumeris);
			ad.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			ad.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			ad.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			ad.setPateikeVardas(pateikejoVardas);
			ad.setPateikePavarde(pateikejoPavarde);    		
			ad.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			ad.setDokumentoRusis(asmensDokumentoRusis);
			ad.setAnkstesnePavarde(ankstesnePav);
			ad.setPilietybe(pilietis);    		
			ad.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			ad.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			ad.setAtvykimoData(CalendarUtils.getParsedDate(atvykimoMetai,atvykimoMenuo,atvykimoData));
			ad.setPastabos(pastabos);
			ad.setAnkstesneGV(kitaSalis);
			ad.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			long l = Long.parseLong(rysisSuGv);
			ad.setRysysSuGv(l);
			if (l == 3) ad.setRysysSuGvKita(rysysSuGvKita);
			if ("on".equals(savininkoIgaliotinis)){
				ad.setSavininkoIgaliotinis(0);
			}
			else {
				ad.setSavininkoIgaliotinis(1);
			}
			ad.setSavininkoTipas(Long.parseLong(savininkoTipas));
			
			ad.setJaKodas(jurAsmKodas);
			ad.setJaPavadinimas(jurAsmPavadinimas);
			ad.setJaBuveine(jurAsmAdresas);
			
			ad.setFaAdresas(jurGyvenamojiVieta);
			ad.setFaKodas(savAsmKodas);
			ad.setFaVardasPavarde(savVardas);
			ad.setTelefonas(telefonas);
			ad.setEmail(email);
			
			ad.setSavininkas1(Savininkas1);
			ad.setSavininkas2(Savininkas2);
			ad.setSavininkas3(Savininkas3);
			ad.setSavininkas4(Savininkas4);
			ad.setSavininkasKodas1(SavininkasKodas1);
			ad.setSavininkasKodas2(SavininkasKodas2);
			ad.setSavininkasKodas3(SavininkasKodas4);
			ad.setSavininkasKodas4(SavininkasKodas4);
			if(null != unikPastatoNr1 || null != unikPastatoNr2 || null != unikPastatoNr3 ){
				String unikalus = unikPastatoNr1 + "-" + unikPastatoNr2 + "-"+ unikPastatoNr3;
				if(unikPastatoNr4 != null)
					unikalus = unikalus + ":"+ unikPastatoNr4;
				ad.setUnikalusPastatoNr(unikalus);
			}
			else
			{
				ad.setUnikalusPastatoNr("");
			}
			if (ad.getRysysSuGv() > 0)
				ad.setDeklaracijaGalioja(CalendarUtils.getParsedDate(deklaracijaGaliojaMetai, deklaracijaGaliojaMenuo, deklaracijaGaliojaDiena));
			
			if (ad.getAsmenvardis() == null) {
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				ad.setAsmenvardis(asmenvardis);
			} else {
				ad.setAsmenvardis(ad.getAsmenvardis());
			}
			
			ad.setSavininkoParasoData(CalendarUtils.getParsedDate(savininkoParasoMetai,savininkoParasoMenuo,savininkoParasoData));
			if(null == request.getSession().getAttribute("print"))
			{
				if (isCompleted){
					if (resident == Constants.IS_RESIDENT)
					{
						String igaliotinis = "0";
						if (!"on".equals(savininkoIgaliotinis)){
							igaliotinis = "1";
						}
						long savAsmKodasN = 0;
						
						try {
							savAsmKodasN = Long.parseLong(savAsmKodas);
						}
						catch(Exception e){}
						
						long jurAsmKodasN = 0;
						try {
							jurAsmKodasN = Long.parseLong(jurAsmKodas);
						}
						catch(Exception e){}
						
						Long nulis = new Long(0);
						ad.setTmpGvtAdvNr(nulis);
						ad.setTmpGvtKampoNr(null);
						ad.setTmpGvtAtvNr(nulis);
						
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"R",
								QueryDelegator.HOME_COUNTRY,
								ad.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"0".equals(pageidaujuDokumenta)?"N":"T",
								"", 
								Long.parseLong(ankstesneGyvenamojiVieta),
								null == kitaSalis? "":kitaSalis.getKodas(),
								performUpdate ? 1 : 0,
								ad.getId(),
								session,
								ist.getId(),
								kampnr,
								ad.getDeklaracijaGalioja()
						);
						ad.setGyvenamojiVieta(gv);
					}
					ad.setBusena(1);
					hsession.setAttribute("declarationComplete","");
				}
				
				hsession.removeAttribute("savewserror");
				//TODO: isjungti siunciami pranesimai i ivpk
				/*
				String wsRezult = "";
				if (new Long(1).equals(ad.getSaltinis())){ 
					try {
						wsRezult = WebServiceUtil.getInstance(request).changeProcessStateResultResume(ad.getProcesoid().toString(), "Deklaracija patvirtinta", "");
					} catch (AxisFault ae){
						ae.printStackTrace();
						hsession.setAttribute("savewserror", ae.getMessage());
						throw ae;
					} catch (RemoteException re){
						re.printStackTrace();
						hsession.setAttribute("savewserror", re.getMessage());
						throw re;
					}
					
					if (!"SUCCESS".equals(wsRezult))
						if (!chkAfterDate(ad.getDeklaravimoData(), 7)){ // nera 7 dienu po deklaravimo tada pranesimas
							hsession.setAttribute("savewserror", "Nepavyko nusiøsti patvirtinimo á el. valdþios portalà");
							return ad;			
						}
						else {
							hsession.removeAttribute("savewserror");
						}

				}
				*/
				session.update(ad);
			}
			tx.commit();
		}
		catch (Exception e){
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}				
		return ad;
	}	
	
	
	/**
	 * Iðsaugo (naujai kuriamà) iðvykimo deklaracijà
	 * @throws DatabaseException - jei iðsaugant ávyko klaida
	 */
	/*
	public IsvykimoDeklaracija saveIsvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String isvykimoMetai,
			String isvykimoMenuo,
			String isvykimoData,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,			
			
			//AsmensDokumentas asmDok,
			
			boolean isCompleted,
			/// parametrai internetu pateikiamai deklaracijai
			boolean isInternetu,
			String procesoid,
			Long istaigaId,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		//HttpSession hsession = request.getSession(); I.N.
		Session session = HibernateUtils.currentSession(request);		
		IsvykimoDeklaracija id = null;		
		
		Istaiga ist = null;		
		String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		//ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);		// I.N. cia kazkas negerai nustatoma kartais

		//Tieto: 201-11-28
		//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateIsvDeklaracijaForWs
		if(isInternetu){
			ist = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		} else {
			ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		}
		
		boolean print = (null == request.getSession().getAttribute("print"));
		String userIstaigaId = String.valueOf(request.getSession().getAttribute("userIstaigaId")); // I.N. cia gerai nustato.

		Constants.Println(request, "------------ ISV ----------------- ");
		if (ist != null) { 
			Constants.Println(request, "ist "+ ist.getId());
		}
		Constants.Println(request, "userIstaigaId "+ userIstaigaId);
		Constants.Println(request, "---------------------------------- ");
		
		Constants.Println(request, "saveIsvDeclaration");
		id = saveIsvDeclaration(
				pasmuo,ankstesnePavarde,pilietybe,asmensDokumentoRusis,
				asmensDokumentoNumeris,asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData,
				asmensDokumentoIsdave,leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData,
				pastabos,isvykimoMetai,isvykimoMenuo,isvykimoData,
				ankstesneGyvenamojiVieta,kitaGyvenamojiVietaAprasymas,atvykoIsUzsienioSalis,atvykoIsUzsienioTextarea,
				deklaracijaPateikta,pateikejoVardas,pateikejoPavarde,pateikimoMetai,pateikimoMenuo,pateikimoData,				
				deklaravimoMetai,deklaravimoMenuo,deklaravimoData,
				ist,s,userIstaigaId,//asmDok,
				print,isCompleted, isInternetu,procesoid, istaigaId, session);
		return id;
	}	
*/
	public IsvykimoDeklaracija saveIsvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String isvykimoMetai,
			String isvykimoMenuo,
			String isvykimoData,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,		
			String telefonas,
			String email,
			
			//AsmensDokumentas asmDok,
			
			boolean isCompleted,
			/// parametrai internetu pateikiamai deklaracijai
			boolean isInternetu,
			String procesoid,
			Long istaigaId,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		//HttpSession hsession = request.getSession(); I.N.
		Session session = HibernateUtils.currentSession(request);		
		IsvykimoDeklaracija id = null;		
		
		Istaiga ist = null;		
		String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		//ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);		// I.N. cia kazkas negerai nustatoma kartais

		//Tieto: 201-11-28
		//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateIsvDeklaracijaForWs
		if(isInternetu){
			ist = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		} else {
			ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		}
		
		boolean print = (null == request.getSession().getAttribute("print"));
		String userIstaigaId = String.valueOf(request.getSession().getAttribute("userIstaigaId")); // I.N. cia gerai nustato.

		Constants.Println(request, "------------ ISV ----------------- ");
		if (ist != null) { 
			Constants.Println(request, "ist "+ ist.getId());
		}
		Constants.Println(request, "userIstaigaId "+ userIstaigaId);
		Constants.Println(request, "---------------------------------- ");
		
		Constants.Println(request, "saveIsvDeclaration");
		id = saveIsvDeclaration(
				pasmuo,ankstesnePavarde,pilietybe,asmensDokumentoRusis,
				asmensDokumentoNumeris,asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData,
				asmensDokumentoIsdave,leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData,
				pastabos,isvykimoMetai,isvykimoMenuo,isvykimoData,
				ankstesneGyvenamojiVieta,kitaGyvenamojiVietaAprasymas,atvykoIsUzsienioSalis,atvykoIsUzsienioTextarea,
				deklaracijaPateikta,pateikejoVardas,pateikejoPavarde,pateikimoMetai,pateikimoMenuo,pateikimoData,				
				deklaravimoMetai,deklaravimoMenuo,deklaravimoData, telefonas, email,
				ist,s,userIstaigaId,//asmDok,
				print,isCompleted, isInternetu,procesoid, istaigaId, session);
		return id;
	}	

	
	/*	
	public void updateIsvDeklaracijaForWs (IsvykimoDeklaracija isvdkl,String procesoid,Long istaigaId,HttpServletRequest request) {
		HttpSession hsession = request.getSession();

		Session session = HibernateUtils.currentSession(request);		
		session.clear();
		session.flush();
		Transaction tx = session.beginTransaction();
		
		String regNr = PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ID", istaigaId.toString());
		Istaiga istaiga = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		
		session.createQuery("update IsvykimoDeklaracija set saltinis = 1, procesoid = :procesoid, regNr = :regNr, istaiga = :istaiga where id = :id")
		.setString("procesoid", procesoid)
		.setLong("id", isvdkl.getId())
		.setString("regNr",regNr)
		.setParameter("istaiga", istaiga).executeUpdate();
		
		tx.commit();
	}
*/
	
	public void updateIsvDeklaracijaForWs (IsvykimoDeklaracija isvdkl,String procesoid,Long istaigaId,HttpServletRequest request)
	throws DatabaseException	
	{
		// HttpSession hsession = request.getSession(); I.N.
		Session session = HibernateUtils.currentSession(request);		
		session.clear();
		session.flush();
		Transaction tx = session.beginTransaction();		
		
		try
		{



		
		IsvykimoDeklaracija isDekl = DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(new Long(isvdkl.getId()),request);
		
		String regNr = PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ID",true, istaigaId.toString());
		Istaiga istaiga = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		
		boolean isIsvDeklAteityje = false;
		try {
			if (isDekl.getIsvykimoData() != null)
				isIsvDeklAteityje = isDekl.getIsvykimoData().after(Calendar.getInstance().getTime());
		} catch (Exception e) {	}
		
		if (isIsvDeklAteityje)
			isDekl.setIsvykimasAteityje("E"); //taps 'T' kai bus patvirtinta 
		else
			isDekl.setIsvykimasAteityje("N");
		
		isDekl.setSaltinis(new Long(1));
		isDekl.setProcesoid(procesoid);
		isDekl.setRegNr(regNr);
		isDekl.setIstaiga(istaiga);

		session.save(isDekl);
		
		

		
		tx.commit();
		session.clear();
		session.flush();
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
	}
	
	public IsvykimoDeklaracija saveIsvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String isvykimoMetai,
			String isvykimoMenuo,
			String isvykimoData,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,	
			
			String telefonas,
			String email,
			
			Istaiga ist,
			String[] s,			
			String userIstaigaId,
			
			//AsmensDokumentas asmDok,
			
			boolean print,
			boolean isCompleted,
			//internetu pateiktos deklaracijos duomenys
			boolean isInternetu,
			String procesoid,
			Long istaigaId,
			Session session
	)
	throws DatabaseException, ObjectNotFoundException
	{			
		IsvykimoDeklaracija id = null;
		Transaction tx = session.beginTransaction();
				
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		
		if (pilietybe != null) pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,session);
		if (atvykoIsUzsienioSalis != null) kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,session);
		
		int resident = Constants.IS_NOT_RESIDENT;
		Asmuo asmuo = null;
		LaikinasAsmuo laikinasAsmuo = null;
		if (pasmuo instanceof LaikinasAsmuo){
			resident = Constants.IS_NOT_RESIDENT;
			laikinasAsmuo = (LaikinasAsmuo)pasmuo;
		}
		else if(pasmuo instanceof Asmuo){
			resident = Constants.IS_RESIDENT;
			asmuo = (Asmuo)pasmuo;
		}
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			GyvenamojiVieta gv = null;
			id = new IsvykimoDeklaracija();	 		
			id.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			/*
			id.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident)
			{
				if(isCompleted)id.setBusena(1); else id.setBusena(0);
				isCompleted = false;
			}
			*/

			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,session);
					if (adr != null){
						id.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						id.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != id.getLaikinasAsmuo())
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(id.getLaikinasAsmuo().getId(),0,session);						
						if (adr != null){
							id.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(id.getLaikinasAsmuo().getId())));
							id.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}
			id.setDokumentoNr(asmensDokumentoNumeris);
			id.setDokumentoIsdavejas(asmensDokumentoIsdave);
			id.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			id.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
	   		id.setDokumentoRusis(asmensDokumentoRusis);
	   		//id.setAsmensDokumentas(asmDok);
			
	   		id.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			id.setPateikeVardas(pateikejoVardas);
			id.setPateikePavarde(pateikejoPavarde); 
			id.setPilietybe(pilietis);  
			id.setAnkstesnePavarde(ankstesnePavarde);
			id.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			id.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			id.setPastabos(pastabos);
			id.setAnkstesneGV(kitaSalis);
			id.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			id.setIstaiga(ist);    		
			id.setGavimoData(new Timestamp(System.currentTimeMillis()));
			id.setIsvykimoData(CalendarUtils.getParsedDate(isvykimoMetai,isvykimoMenuo,isvykimoData));
			id.setTelefonas(telefonas);
			id.setEmail(email);
			//Pozymis, kad isvykimo dekl. yra ateityje (nebus pildomas GR ir deklaracijos statusas bus "nebaigtas)"
			boolean isIsvDeklAteityje = false;
			try {
				if (id.getIsvykimoData() != null)
					isIsvDeklAteityje = id.getIsvykimoData().after(Calendar.getInstance().getTime());
			} catch (Exception e) {
				//e.printStackTrace();
			}

			//Tieto: 201-11-28
			//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateIsvDeklaracijaForWs
			if (isIsvDeklAteityje)
				//TODO: 2016-01-11 iðkomentuota vieta tam kad nereikëtu papildomo user interactiono per GVDISa
			/*
				if(isInternetu){
					id.setIsvykimasAteityje("E");
				} else {
					id.setIsvykimasAteityje("T");
				}
			*/
				id.setIsvykimasAteityje("T");
			else
				id.setIsvykimasAteityje("N");
			
			if(resident == Constants.IS_RESIDENT){
				id.setAsmuo(asmuo);
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				id.setAsmenvardis(asmenvardis);
			}
			else id.setLaikinasAsmuo(laikinasAsmuo);
			//if(null == request.getSession().getAttribute("print"))id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "ID")); //ju.k 2007.08.13
			if(true == print)
			{
				id.setBusena(0);

				//Tieto: 201-11-28
				//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateIsvDeklaracijaForWs
				if(isInternetu){
					id.setProcesoid(procesoid);
					id.setSaltinis(new Long(1));
					id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ID",true, istaigaId.toString()));
				} else {
					id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ID",false, userIstaigaId));
				}

				session.save(id);

				if (isCompleted){
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;
					
					if (resident == Constants.IS_RESIDENT && !isIsvDeklAteityje)
					{
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"V",
								atvykoIsUzsienioSalis,
								id.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								"",//savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"N",
								"",  
								Long.parseLong(ankstesneGyvenamojiVieta),
								kitaSalis.getKodas(),						
								0,
								id.getId(),
								session,
								0,
								id.getDeklaracijaGalioja()
						);
						id.setGyvenamojiVieta(gv);
					}
					
					if (!isIsvDeklAteityje)
					{
						id.setBusena(1); //busena pasikeis, jei deklaracija ne ateityje
					}
					
					session.update(id);

				}
			}
			tx.commit();    		
		}
		catch (GenericJDBCException e){
			tx.rollback();
			throw new DatabaseException(e.getCause().getMessage(),e.getCause());
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		return id;
	}	
	
	public IsvykimoDeklaracija saveIsvDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String isvykimoMetai,
			String isvykimoMenuo,
			String isvykimoData,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			Istaiga ist,
			String[] s,			
			String userIstaigaId,
			
			//AsmensDokumentas asmDok,
			
			boolean print,
			boolean isCompleted,
			//internetu pateiktos deklaracijos duomenys
			boolean isInternetu,
			String procesoid,
			Long istaigaId,
			Session session
	)
	throws DatabaseException, ObjectNotFoundException
	{			
		IsvykimoDeklaracija id = null;
		Transaction tx = session.beginTransaction();
				
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		
		if (pilietybe != null) pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,session);
		if (atvykoIsUzsienioSalis != null) kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,session);
		
		int resident = Constants.IS_NOT_RESIDENT;
		Asmuo asmuo = null;
		LaikinasAsmuo laikinasAsmuo = null;
		if (pasmuo instanceof LaikinasAsmuo){
			resident = Constants.IS_NOT_RESIDENT;
			laikinasAsmuo = (LaikinasAsmuo)pasmuo;
		}
		else if(pasmuo instanceof Asmuo){
			resident = Constants.IS_RESIDENT;
			asmuo = (Asmuo)pasmuo;
		}
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			GyvenamojiVieta gv = null;
			id = new IsvykimoDeklaracija();	 		
			id.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			/*
			id.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident)
			{
				if(isCompleted)id.setBusena(1); else id.setBusena(0);
				isCompleted = false;
			}
			*/

			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,session);
					if (adr != null){
						id.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						id.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != id.getLaikinasAsmuo())
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(id.getLaikinasAsmuo().getId(),0,session);						
						if (adr != null){
							id.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(id.getLaikinasAsmuo().getId())));
							id.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}
			id.setDokumentoNr(asmensDokumentoNumeris);
			id.setDokumentoIsdavejas(asmensDokumentoIsdave);
			id.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			id.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
	   		id.setDokumentoRusis(asmensDokumentoRusis);
	   		//id.setAsmensDokumentas(asmDok);
			
	   		id.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			id.setPateikeVardas(pateikejoVardas);
			id.setPateikePavarde(pateikejoPavarde); 
			id.setPilietybe(pilietis);  
			id.setAnkstesnePavarde(ankstesnePavarde);
			id.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			id.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			id.setPastabos(pastabos);
			id.setAnkstesneGV(kitaSalis);
			id.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			id.setIstaiga(ist);    		
			id.setGavimoData(new Timestamp(System.currentTimeMillis()));
			id.setIsvykimoData(CalendarUtils.getParsedDate(isvykimoMetai,isvykimoMenuo,isvykimoData));
			//Pozymis, kad isvykimo dekl. yra ateityje (nebus pildomas GR ir deklaracijos statusas bus "nebaigtas)"
			boolean isIsvDeklAteityje = false;
			try {
				if (id.getIsvykimoData() != null)
					isIsvDeklAteityje = id.getIsvykimoData().after(Calendar.getInstance().getTime());
			} catch (Exception e) {
				//e.printStackTrace();
			}

			//Tieto: 201-11-28
			//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateIsvDeklaracijaForWs
			if (isIsvDeklAteityje)
				if(isInternetu){
					id.setIsvykimasAteityje("E");
				} else {
					id.setIsvykimasAteityje("T");
				}
			else
				id.setIsvykimasAteityje("N");
			
			if(resident == Constants.IS_RESIDENT){
				id.setAsmuo(asmuo);
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				id.setAsmenvardis(asmenvardis);
			}
			else id.setLaikinasAsmuo(laikinasAsmuo);
			//if(null == request.getSession().getAttribute("print"))id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "ID")); //ju.k 2007.08.13
			if(true == print)
			{
				id.setBusena(0);

				//Tieto: 201-11-28
				//Atkeltas internetiniu deklaraciju pateikimo funkcionalumas is updateIsvDeklaracijaForWs
				if(isInternetu){
					id.setProcesoid(procesoid);
					id.setSaltinis(new Long(1));
					id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ID",true, istaigaId.toString()));
				} else {
					id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ID",false, userIstaigaId));
				}

				session.save(id);

				if (isCompleted){
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;
					
					if (resident == Constants.IS_RESIDENT && !isIsvDeklAteityje)
					{
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"V",
								atvykoIsUzsienioSalis,
								id.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								"",//savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"N",
								"",  
								Long.parseLong(ankstesneGyvenamojiVieta),
								kitaSalis.getKodas(),						
								0,
								id.getId(),
								session,
								0,
								null
						);
						id.setGyvenamojiVieta(gv);
					}
					
					if (!isIsvDeklAteityje)
					{
						id.setBusena(1); //busena pasikeis, jei deklaracija ne ateityje
					}
					
					session.update(id);

				}
			}
			tx.commit();    		
		}
		catch (GenericJDBCException e){
			tx.rollback();
			throw new DatabaseException(e.getCause().getMessage(),e.getCause());
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		return id;
	}	
	
	
	public void rejectDeclaration(Long declid, String atmetimoPriezastys, HttpServletRequest request) throws DatabaseException, ObjectNotFoundException, AxisFault, RemoteException{				
		Deklaracija deklaracija = this.getDeklaracija(declid, request);
		String result = "";

		try {
			result = WebServiceUtil.getInstance(request).changeProcessStateResultResume(deklaracija.getProcesoid().toString(), "Deklaracija atmesta", "");
		} 
		catch (AxisFault ae)
		{
			String endPoint = ParameterDelegator.getInstance().getStringParameter(request,"GVDIS_EL_VALDZIOS_VARTU_WS");
			Constants.Println(request, "real WS path:");
			Constants.Println(request, endPoint);
			request.setAttribute("wserror", ae.getMessage());
			ae.printStackTrace();			
			throw ae;
		} 
		catch (RemoteException re)
		{
			String endPoint = ParameterDelegator.getInstance().getStringParameter(request, "GVDIS_EL_VALDZIOS_VARTU_WS");
			Constants.Println(request, "real WS path:");
			Constants.Println(request, endPoint);			
			request.setAttribute("wserror", re.getMessage());
			re.printStackTrace();
			this.getDeklaracija(declid, request);			
			throw re;
		}
		catch (Exception e)
		{
			String endPoint = ParameterDelegator.getInstance().getStringParameter(request, "GVDIS_EL_VALDZIOS_VARTU_WS");
			Constants.Println(request, "real WS path:");
			Constants.Println(request, endPoint);			
			e.printStackTrace();
		}
		
		if (!"SUCCESS".equals(result))
			if ( !chkAfterDate(deklaracija.getDeklaravimoData(), 7)){ // nera 7 dienu po deklaravimo tada pranesimas
				request.setAttribute("wserror", "Nepavyko nusiøsti atmetimo á el. valdþios portalà");
				return;
			}
			else {
				request.removeAttribute("wserror");
			}

		deklaracija.setBusena(2);
		deklaracija.setAtmetimoPriezastys(atmetimoPriezastys);
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		session.update(deklaracija);
		tx.commit();
	}
			
	
	/**
	 * Iðsaugo jau egzistuojanèià iðvykimo deklaracijà
	 * @throws DatabaseException - jei ávyko klaida
	 */
	public IsvykimoDeklaracija updateIsvDeclaration(
			Long declid,
			String ankstesnePavarde,
			String pilietybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String isvykimoMetai,
			String isvykimoMenuo,
			String isvykimoData,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,			
			
			String telefonas,
			String email,
			
			boolean isCompleted,
			boolean performUpdate,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		String wsRezult = "";
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		
		//Istaiga ist = null;
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		IsvykimoDeklaracija id = null;
		id = DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(declid, request);
		//String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		//ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]), request);
		pilietis = UtilDelegator.getInstance().getValstybe(pilietybe, request);
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis, request);
		Asmuo asmuo = id.getAsmuo();
		int resident = Constants.IS_NOT_RESIDENT;
		if (null != asmuo && null ==  id.getLaikinasAsmuo())resident = Constants.IS_RESIDENT;
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = 0;
			GyvenamojiVieta gv = null;
			id.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			/*
			id.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident)
			{
				if(isCompleted)id.setBusena(1); else id.setBusena(0);
				isCompleted = false;
			}
			*/

			if (performUpdate){
				if (id != null && id.getGyvenamojiVieta() != null){
					next = id.getGyvenamojiVieta().getGvtNr();
				}
					
//				Deklaracija deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracija(declid, request);
//				if (deklaracija != null && deklaracija.getGyvenamojiVieta() != null){
//					next = deklaracija.getGyvenamojiVieta().getGvtNr();
//				}
			}

			id.setDokumentoNr(asmensDokumentoNumeris);
			id.setDokumentoIsdavejas(asmensDokumentoIsdave);
			id.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			id.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			id.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			id.setPateikeVardas(pateikejoVardas);
			id.setPateikePavarde(pateikejoPavarde);    		
			id.setDokumentoRusis(asmensDokumentoRusis);
			id.setPilietybe(pilietis);
			id.setAnkstesnePavarde(ankstesnePavarde);
			id.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			id.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			id.setPastabos(pastabos);
			id.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			id.setAnkstesneGV(kitaSalis);
			id.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			//id.setIstaiga(ist);    		
			//id.setGavimoData(new Timestamp(System.currentTimeMillis()));
			id.setIsvykimoData(CalendarUtils.getParsedDate(isvykimoMetai,isvykimoMenuo,isvykimoData));
			id.setTelefonas(telefonas);
			id.setEmail(email);
			//Pozymis, kad isvykimo dekl. yra ateityje (nebus pildomas GR ir deklaracijos statusas bus "nebaigtas)"
			boolean isIsvDeklAteityje = false;
			try {
				if (id.getIsvykimasAteityje() != null) {
					if (id.getIsvykimasAteityje().equalsIgnoreCase("E")) {
						isIsvDeklAteityje = true; //elektronine isv dekl i ateiti
					}
				}
				if (id.getIsvykimoData() != null && !isIsvDeklAteityje)
					isIsvDeklAteityje = id.getIsvykimoData().after(Calendar.getInstance().getTime());
			} catch (Exception e) {	}
			
			if (isIsvDeklAteityje)
				id.setIsvykimasAteityje("T");
			else
				id.setIsvykimasAteityje("N");
			
			id.setAsmuo(asmuo);
			if (id.getAsmenvardis() == null) {
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				id.setAsmenvardis(asmenvardis);
			} else {
				id.setAsmenvardis(id.getAsmenvardis());
			}
			
			/*if(null == id.getRegNr())
				id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "ID"));*/ 
				//id.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "ID"));//ju.k -Zurnalo numeracijos pakeitimas 2007.08.13 
			if(null == request.getSession().getAttribute("print"))
			{
				if (isCompleted){
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;

					if (resident == Constants.IS_RESIDENT && !isIsvDeklAteityje)
					{
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"V",
								atvykoIsUzsienioSalis,
								id.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								"",//savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"N",
								"", 
								Long.parseLong(ankstesneGyvenamojiVieta),
								kitaSalis.getKodas(),						
								performUpdate ? 1 : 0,
								id.getId(),
								session,
								0,
								null
						);
						id.setGyvenamojiVieta(gv);
					}
					hsession.setAttribute("declarationComplete","");
					if (!isIsvDeklAteityje)
						id.setBusena(1); //busena pasikeis, jei deklaracija ne ateityje
					else
						id.setBusena(0);
					
				}
				hsession.removeAttribute("savewserror");
				//TODO: isjungti siunciami pranesimai i ivpk
				/*
				if (new Long(1).equals(id.getSaltinis())){ 
					try {
						wsRezult = WebServiceUtil.getInstance(request).changeProcessStateResultResume(id.getProcesoid().toString(), "Deklaracija patvirtinta", "");
					} catch (AxisFault ae){
						ae.printStackTrace();
						hsession.setAttribute("savewserror", ae.getMessage());
						throw ae;
					} catch (RemoteException re){
						re.printStackTrace();
						hsession.setAttribute("savewserror", re.getMessage());
						throw re;
					}
					
					if (!"SUCCESS".equals(wsRezult)) 
						if (!chkAfterDate(id.getDeklaravimoData(), 7)){ // nera 7 dienu po deklaravimo tada pranesimas
							hsession.setAttribute("savewserror", "Nepavyko nusiøsti patvirtinimo á el. valdþios portalà");
							return id;			
						}
						else {
							request.removeAttribute("savewserror");
						}

				}
				*/
				session.update(id);
			}
			
			tx.commit();    
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		
		return id;
	}		
	
	
	/**
	 * Deprecated 2015-12-18
	 * Atnaujina duotà GVNA deklaracijà
	 * @param declid - deklaracijos ID
	 * @param isCompleted - poþymis, ar deklaracijoje nëra klaidø
	 * @throws DatabaseException
	 */
	public GvnaDeklaracija updateGvnaDeclaration(
			Long declid,
			String ankstesnePavarde,
			String pilietybe,
			String savivaldybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String savivaldybePastabos,
			String priezastys,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			String pageidaujuDokumenta,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,			
			
			boolean isCompleted,
			boolean performUpdate,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		
		//Istaiga ist = null;
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		TeritorinisVienetas saviv = null;
		GvnaDeklaracija gd = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(declid,request);
		
		//String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		//ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,request);
		if(null != atvykoIsUzsienioSalis)
			kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,request);
		if(!"-1".equals(savivaldybe))
		saviv =  AdresaiDelegator.getInstance().getTerVieneta(Long.valueOf(savivaldybe),request);			
		
		Asmuo asmuo = gd.getAsmuo();
		int resident = Constants.IS_NOT_RESIDENT;
		if (null != asmuo && null ==  gd.getLaikinasAsmuo())resident = Constants.IS_RESIDENT;
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = saviv.getTerNr();
			gd.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			GyvenamojiVieta gv = null;
			/*
			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,request);
					if (adr != null){
						gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != gd.getLaikinasAsmuo())
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(gd.getLaikinasAsmuo().getId(),0,request);
						if (adr != null){
							gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(gd.getLaikinasAsmuo().getId())));
							gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}*/
			
			/*
			gd.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident){
				if(isCompleted)gd.setBusena(1); else gd.setBusena(0);
				isCompleted = false;
			}
			*/
			if (performUpdate){
				if (gd != null && gd.getGyvenamojiVieta() != null){
					next = gd.getGyvenamojiVieta().getGvtNr();
				}
//				Deklaracija deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracija(declid, request);
//				if (deklaracija != null && deklaracija.getGyvenamojiVieta() != null){
//					next = deklaracija.getGyvenamojiVieta().getGvtNr();
//				}
			}

			gd.setDokumentoNr(asmensDokumentoNumeris);
			gd.setDokumentoIsdavejas(asmensDokumentoIsdave);
			gd.setSavivaldybe(saviv);
			gd.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			gd.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			gd.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			gd.setPateikeVardas(pateikejoVardas);
			gd.setPateikePavarde(pateikejoPavarde);    		
			gd.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			gd.setDokumentoRusis(asmensDokumentoRusis);
			gd.setPilietybe(pilietis);   
			gd.setAnkstesnePavarde(ankstesnePavarde);
			gd.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setPastabos(pastabos);
			gd.setSavivaldybePastabos(savivaldybePastabos);
			gd.setPriezastys(priezastys);
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setAnkstesneGV(kitaSalis);
			gd.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			
			if (gd.getAsmenvardis() == null) {
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				gd.setAsmenvardis(asmenvardis);
			} else {
				gd.setAsmenvardis(gd.getAsmenvardis());
			}
			
			//gd.setIstaiga(ist);    		
			//gd.setGavimoData(new Timestamp(System.currentTimeMillis()));
			if(null == gd.getRegNr())
				gd.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "ND",false)); //ju.k 2007.08.13
			if(null == request.getSession().getAttribute("print"))
			{
				if (isCompleted){
					gd.setBusena(1);
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;
					
					if (resident == Constants.IS_RESIDENT){
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"K",
								QueryDelegator.HOME_COUNTRY,
								gd.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								"",		// savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"0".equals(pageidaujuDokumenta)? "N" : "T",
								"",  
								Long.parseLong(ankstesneGyvenamojiVieta),
								null!= kitaSalis?kitaSalis.getKodas():"",
								performUpdate ? 1 : 0,
								gd.getId(),
								session,
								0,
								gd.getDeklaracijaGalioja()
						); 
						hsession.setAttribute("declarationComplete","");
						gd.setGyvenamojiVieta(gv);
					}
				}
				else
				{
					// Kai deklaracija nebaigta busena - 0
					gd.setBusena(0);
				}
				
				String wsRezult = null;
				hsession.removeAttribute("savewserror");
				if (new Long(1).equals(gd.getSaltinis())){ 
					try {
						wsRezult = WebServiceUtil.getInstance(request).changeProcessStateResultResume(gd.getProcesoid().toString(), "Deklaracija patvirtinta", "");
					} catch (AxisFault ae){
						ae.printStackTrace();
						hsession.setAttribute("savewserror", ae.getMessage());
						throw ae;
					} catch (RemoteException re){
						re.printStackTrace();
						hsession.setAttribute("savewserror", re.getMessage());
						throw re;
					}
					
					if (!"SUCCESS".equals(wsRezult))
						if (!chkAfterDate(gd.getDeklaravimoData(), 7)){ // nera 7 dienu po deklaravimo tada pranesimas
							hsession.setAttribute("savewserror", "Nepavyko nusiøsti patvirtinimo á el.valdþios portalà");
							return gd;			
						}
						else {
							hsession.removeAttribute("savewserror");
						}

				}
				session.update(gd);				
			}
			
			tx.commit();    		
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}								
		return gd;
	}		
	
	/**
	 * Atnaujina duotà GVNA deklaracijà
	 * @param declid - deklaracijos ID
	 * @param isCompleted - poþymis, ar deklaracijoje nëra klaidø
	 * @throws DatabaseException
	 */
	public GvnaDeklaracija updateGvnaDeclaration(
			Long declid,
			String ankstesnePavarde,
			String pilietybe,
			String savivaldybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String savivaldybePastabos,
			String priezastys,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			String pageidaujuDokumenta,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,			
			
			String telefonas,
			String email,
			String deklaracijaGaliojaMetai,
			String deklaracijaGaliojaMenuo,
			String deklaracijaGaliojaDiena,
			
			boolean isCompleted,
			boolean performUpdate,
			HttpServletRequest request
	)
	throws DatabaseException, ObjectNotFoundException
	{
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		
		//Istaiga ist = null;
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		TeritorinisVienetas saviv = null;
		GvnaDeklaracija gd = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(declid,request);
		
		//String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		//ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);
		pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,request);
		if(null != atvykoIsUzsienioSalis)
			kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,request);
		if(!"-1".equals(savivaldybe))
		saviv =  AdresaiDelegator.getInstance().getTerVieneta(Long.valueOf(savivaldybe),request);			
		
		Asmuo asmuo = gd.getAsmuo();
		int resident = Constants.IS_NOT_RESIDENT;
		if (null != asmuo && null ==  gd.getLaikinasAsmuo())resident = Constants.IS_RESIDENT;
		
		try {		
			long next = 0;
			long adrv = 0;
			long terv = saviv.getTerNr();
			gd.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			GyvenamojiVieta gv = null;
			/*
			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,request);
					if (adr != null){
						gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != gd.getLaikinasAsmuo())
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(gd.getLaikinasAsmuo().getId(),0,request);
						if (adr != null){
							gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(gd.getLaikinasAsmuo().getId())));
							gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}*/
			
			/*
			gd.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident){
				if(isCompleted)gd.setBusena(1); else gd.setBusena(0);
				isCompleted = false;
			}
			*/
			if (performUpdate){
				if (gd != null && gd.getGyvenamojiVieta() != null){
					next = gd.getGyvenamojiVieta().getGvtNr();
				}
//				Deklaracija deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracija(declid, request);
//				if (deklaracija != null && deklaracija.getGyvenamojiVieta() != null){
//					next = deklaracija.getGyvenamojiVieta().getGvtNr();
//				}
			}

			gd.setDokumentoNr(asmensDokumentoNumeris);
			gd.setDokumentoIsdavejas(asmensDokumentoIsdave);
			gd.setSavivaldybe(saviv);
			gd.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			gd.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			gd.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			gd.setPateikeVardas(pateikejoVardas);
			gd.setPateikePavarde(pateikejoPavarde);    		
			gd.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			gd.setDokumentoRusis(asmensDokumentoRusis);
			gd.setPilietybe(pilietis);   
			gd.setAnkstesnePavarde(ankstesnePavarde);
			gd.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setPastabos(pastabos);
			gd.setSavivaldybePastabos(savivaldybePastabos);
			gd.setPriezastys(priezastys);
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setAnkstesneGV(kitaSalis);
			gd.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			gd.setTelefonas(telefonas);
			gd.setEmail(email);
			gd.setDeklaracijaGalioja(CalendarUtils.getParsedDate(deklaracijaGaliojaMetai, deklaracijaGaliojaMenuo, deklaracijaGaliojaDiena));
			if (gd.getAsmenvardis() == null) {
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				gd.setAsmenvardis(asmenvardis);
			} else {
				gd.setAsmenvardis(gd.getAsmenvardis());
			}
			
			//gd.setIstaiga(ist);    		
			//gd.setGavimoData(new Timestamp(System.currentTimeMillis()));
			if(null == gd.getRegNr())
				gd.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, session, "Deklaracija", "ND",false)); //ju.k 2007.08.13
			if(null == request.getSession().getAttribute("print"))
			{
				if (isCompleted){
					gd.setBusena(1);
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;
					
					if (resident == Constants.IS_RESIDENT){
						gv = createGyvenamojiVietaFull(
								asmuo.getAsmNr(),
								next,
								"K",
								QueryDelegator.HOME_COUNTRY,
								gd.getDeklaravimoData(),
								adrv,
								terv,
								igaliotinis,              
								"",		// savininkoTipas,
								savAsmKodasN,
								jurAsmKodasN,
								null,
								"0".equals(pageidaujuDokumenta)? "N" : "T",
								"",  
								Long.parseLong(ankstesneGyvenamojiVieta),
								null!= kitaSalis?kitaSalis.getKodas():"",
								performUpdate ? 1 : 0,
								gd.getId(),
								session,
								0,
								gd.getDeklaracijaGalioja()
						); 
						hsession.setAttribute("declarationComplete","");
						gd.setGyvenamojiVieta(gv);
					}
				}
				else
				{
					// Kai deklaracija nebaigta busena - 0
					gd.setBusena(0);
				}
				
				String wsRezult = null;
				hsession.removeAttribute("savewserror");
				if (new Long(1).equals(gd.getSaltinis())){ 
					try {
						wsRezult = WebServiceUtil.getInstance(request).changeProcessStateResultResume(gd.getProcesoid().toString(), "Deklaracija patvirtinta", "");
					} catch (AxisFault ae){
						ae.printStackTrace();
						hsession.setAttribute("savewserror", ae.getMessage());
						throw ae;
					} catch (RemoteException re){
						re.printStackTrace();
						hsession.setAttribute("savewserror", re.getMessage());
						throw re;
					}
					
					if (!"SUCCESS".equals(wsRezult))
						if (!chkAfterDate(gd.getDeklaravimoData(), 7)){ // nera 7 dienu po deklaravimo tada pranesimas
							hsession.setAttribute("savewserror", "Nepavyko nusiøsti patvirtinimo á el.valdþios portalà");
							return gd;			
						}
						else {
							hsession.removeAttribute("savewserror");
						}

				}
				session.update(gd);				
			}
			
			tx.commit();    		
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}								
		return gd;
	}		
	
	
	/**
	 * Deprecated 2015-12-15
	 * Iðsaugo (pirmà kartà) duotà GVNA praðymà. 
	 * @param isCompleted - poþymis, ar praðyme nëra klaidø
	 * @throws DatabaseException - jei ávyko klaida iðsaugant
	 
	public GvnaDeklaracija saveGvnaDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String savivaldybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String savivaldybePastabos,
			String priezastys,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			String pageidaujuDokumenta,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,			
			
			
			
			boolean isCompleted,
			HttpServletRequest request
	)
	throws DatabaseException
	{
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		
		Istaiga ist = null;
		String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		boolean print = (null == request.getSession().getAttribute("print"));
		String userIstaigaId = String.valueOf(request.getSession().getAttribute("userIstaigaId"));
		
		ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);		
		
		GvnaDeklaracija gvna = saveGvnaDeclaration(
				pasmuo, ankstesnePavarde, pilietybe,savivaldybe,asmensDokumentoRusis,				
				asmensDokumentoNumeris, asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData,				
				asmensDokumentoIsdave,leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData,
				pastabos,savivaldybePastabos,priezastys,			
				ankstesneGyvenamojiVieta,kitaGyvenamojiVietaAprasymas,
				atvykoIsUzsienioSalis,atvykoIsUzsienioTextarea,				
				deklaracijaPateikta,pateikejoVardas,pateikejoPavarde,pateikimoMetai,
				pateikimoMenuo,pateikimoData,pageidaujuDokumenta,				
				deklaravimoMetai,deklaravimoMenuo,deklaravimoData,							
				ist,s,userIstaigaId, print,isCompleted,								
				session);
		if(true == print) hsession.setAttribute("declarationComplete","");
		return gvna;
		
	}			
	*/
	/**
	 * Iðsaugo (pirmà kartà) duotà GVNA praðymà. 
	 * @param isCompleted - poþymis, ar praðyme nëra klaidø
	 * @throws DatabaseException - jei ávyko klaida iðsaugant
	 */	
	public GvnaDeklaracija saveGvnaDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String savivaldybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String savivaldybePastabos,
			String priezastys,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			String pageidaujuDokumenta,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,
			
			String telefonas,
			String email,		
			String deklaracijaGaliojaMetai,
			String deklaracijaGaliojaMenuo,
			String deklaracijaGaliojaDiena,
			
			boolean isCompleted,
			HttpServletRequest request
	)
	throws DatabaseException
	{
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);		
		
		Istaiga ist = null;
		String[] s = UserDelegator.getInstance().getDarbIstaiga(request);
		boolean print = (null == request.getSession().getAttribute("print"));
		String userIstaigaId = String.valueOf(request.getSession().getAttribute("userIstaigaId"));
		
		ist = UtilDelegator.getInstance().getIstaiga(Long.parseLong(s[0]),request);		
		
		GvnaDeklaracija gvna = saveGvnaDeclaration(
				pasmuo, ankstesnePavarde, pilietybe,savivaldybe,asmensDokumentoRusis,				
				asmensDokumentoNumeris, asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData,				
				asmensDokumentoIsdave,leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData,
				pastabos,savivaldybePastabos,priezastys,			
				ankstesneGyvenamojiVieta,kitaGyvenamojiVietaAprasymas,
				atvykoIsUzsienioSalis,atvykoIsUzsienioTextarea,				
				deklaracijaPateikta,pateikejoVardas,pateikejoPavarde,pateikimoMetai,
				pateikimoMenuo,pateikimoData,pageidaujuDokumenta, 				
				deklaravimoMetai,deklaravimoMenuo,deklaravimoData, telefonas, email, deklaracijaGaliojaMetai, deklaracijaGaliojaMenuo, deklaracijaGaliojaDiena,							
				ist,s,userIstaigaId, print,isCompleted,								
				session);
		if(true == print) hsession.setAttribute("declarationComplete","");
		return gvna;
		
	}			

	/*	
	public void updateGvnaDeklaracijaForWs (GvnaDeklaracija gvnadkl, String procesoid, Long istaigaId,HttpServletRequest request) {
		HttpSession hsession = request.getSession();
		Session session = HibernateUtils.currentSession(request);	
		session.clear();
		session.flush();
		Transaction tx = session.beginTransaction();
		
		String regNr = PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ND", istaigaId.toString());
		Istaiga istaiga = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
		
		session.createQuery("update GvnaDeklaracija set saltinis = 1, procesoid = :procesoid, regNr = :regNr, istaiga = :istaiga where id = :id")
		.setString("procesoid", procesoid)
		.setLong("id", gvnadkl.getId())
		.setString("regNr",regNr)
		.setParameter("istaiga", istaiga).executeUpdate();
		tx.commit();
		
	}
*/	 
	
	public void updateGvnaDeklaracijaForWs (GvnaDeklaracija gvnadkl, String procesoid, Long istaigaId,HttpServletRequest request)
	throws DatabaseException
	{
		// HttpSession hsession = request.getSession(); I.N.
		Session session = HibernateUtils.currentSession(request);	
		session.clear();
		session.flush();
		Transaction tx = session.beginTransaction();
		try{
		
			GvnaDeklaracija gvnaDekl = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(new Long(gvnadkl.getId()), request);
			
			
			String regNr = PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija", "ND",true, istaigaId.toString());
			Istaiga istaiga = UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(),request);
			
			gvnaDekl.setSaltinis(new Long(1));
			gvnaDekl.setProcesoid(procesoid);
			gvnaDekl.setRegNr(regNr);
			gvnaDekl.setIstaiga(istaiga);
			
			session.save(gvnaDekl);
			
			tx.commit();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
	}	
	
	/*
	/**
	 * Deprecated 2015-12-15
	 * Iðsaugo (pirmà kartà) duotà GVNA praðymà. 
	 * @param isCompleted - poþymis, ar praðyme nëra klaidø
	 * @throws DatabaseException - jei ávyko klaida iðsaugant
	 
	public GvnaDeklaracija saveGvnaDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String savivaldybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String savivaldybePastabos,
			String priezastys,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			String pageidaujuDokumenta,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,			
			
			Istaiga ist,
			String[] s,			
			String userIstaigaId,
			
			boolean print,
			boolean isCompleted,
			
			
			Session session
	)
	throws DatabaseException
	{			
		
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		TeritorinisVienetas saviv = null;
		GvnaDeklaracija gd = new GvnaDeklaracija();	 		
		Transaction tx = session.beginTransaction();
		
		pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,session);
	
		if(null != atvykoIsUzsienioSalis)
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,session);
		if(!"-1".equals(savivaldybe))
		saviv =  AdresaiDelegator.getInstance().getTerVieneta(Long.valueOf(savivaldybe),session);

		int resident = Constants.IS_NOT_RESIDENT;
		Asmuo asmuo = null;
		LaikinasAsmuo laikinasAsmuo = null;
		if (pasmuo instanceof LaikinasAsmuo){
			resident = Constants.IS_NOT_RESIDENT;
			laikinasAsmuo = (LaikinasAsmuo)pasmuo;
		}
		else if(pasmuo instanceof Asmuo){
			resident = Constants.IS_RESIDENT;
			asmuo = (Asmuo)pasmuo;
		}
		
		try {		
			long next = 0;		
			long adrv = 0;
			long terv;
			try {
				terv = saviv.getTerNr();
			} catch (NullPointerException npe) {terv = 0;}			
			
			GyvenamojiVieta gv = null;
			gd.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,session);						
					if (adr != null){
						gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != laikinasAsmuo)
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(laikinasAsmuo.getId(),0,session);
						if (adr != null){
							gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(laikinasAsmuo.getId())));
							gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}    		
			
			/*gd.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident)
			{
				if(isCompleted)gd.setBusena(1); else gd.setBusena(0);
				isCompleted = false;
			}
			*/
	/*
			if (resident == Constants.IS_RESIDENT){
				gd.setAsmuo(asmuo);
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				gd.setAsmenvardis(asmenvardis);
			}
			else gd.setLaikinasAsmuo(laikinasAsmuo);
			gd.setDokumentoNr(asmensDokumentoNumeris);
			gd.setDokumentoIsdavejas(asmensDokumentoIsdave);
			gd.setSavivaldybe(saviv);
			gd.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			gd.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			gd.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			gd.setPateikeVardas(pateikejoVardas);
			gd.setPateikePavarde(pateikejoPavarde);    		
			gd.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			gd.setDokumentoRusis(asmensDokumentoRusis);
			gd.setPilietybe(pilietis);  
			gd.setAnkstesnePavarde(ankstesnePavarde);
			gd.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setPastabos(pastabos);
			gd.setSavivaldybePastabos(savivaldybePastabos);
			gd.setPriezastys(priezastys);
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setAnkstesneGV(kitaSalis);
			gd.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			gd.setIstaiga(ist);    		
			gd.setGavimoData(new Timestamp(System.currentTimeMillis()));
			if(true == print) gd.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija","ND",false, userIstaigaId)); //ju.k 2007.08.13
			
			if(true == print) 
			{
				session.save(gd);
				if (isCompleted){
					gd.setBusena(1);
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;
					
					if (resident == Constants.IS_RESIDENT)
					{
						if (terv==0) try {
							terv=gd.getSavivaldybe().getTerNr();
						} catch (NullPointerException npe) {}
							gv = createGyvenamojiVietaFull(
									asmuo.getAsmNr(),
									next,
									"K",
									QueryDelegator.HOME_COUNTRY,
									gd.getDeklaravimoData(),
									adrv,
									terv,
									igaliotinis,              
									"",//savininkoTipas,
									savAsmKodasN,
									jurAsmKodasN,
									pastabos,
									"0".equals(pageidaujuDokumenta)?"N":"T",
									"",  
									Long.parseLong(ankstesneGyvenamojiVieta),
									null != atvykoIsUzsienioSalis?kitaSalis.getKodas():"",
									0,
									gd.getId(),
									session,
									0,
									gd.getDeklaracijaGalioja()
							);    			
							//hsession.setAttribute("declarationComplete","");
							gd.setGyvenamojiVieta(gv);
					}
					session.update(gd);
				}
			}
			
			tx.commit();    		
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		return gd;
	}	
	*/
	/**
	 * Iðsaugo (pirmà kartà) duotà GVNA praðymà. 
	 * @param isCompleted - poþymis, ar praðyme nëra klaidø
	 * @throws DatabaseException - jei ávyko klaida iðsaugant
	 */
	public GvnaDeklaracija saveGvnaDeclaration(
			GvdisBase pasmuo,
			String ankstesnePavarde,
			String pilietybe,
			String savivaldybe,
			String asmensDokumentoRusis,
			
			String asmensDokumentoNumeris,
			String asmensDokumentoIsdavimoMetai,
			String asmensDokumentoIsdavimoMenuo,
			String asmensDokumentoIsdavimoData,
			
			String asmensDokumentoIsdave,	    		
			String leidimoGaliojimoMetai,
			String leidimoGaliojimoMenuo,
			String leidimoGaliojimoData,
			
			String pastabos,
			String savivaldybePastabos,
			String priezastys,			
			
			String ankstesneGyvenamojiVieta,
			String kitaGyvenamojiVietaAprasymas,
			String atvykoIsUzsienioSalis,
			String atvykoIsUzsienioTextarea,
			
			String deklaracijaPateikta,
			String pateikejoVardas,
			String pateikejoPavarde,
			String pateikimoMetai,
			String pateikimoMenuo,
			String pateikimoData,
			String pageidaujuDokumenta,
			
			String deklaravimoMetai,
			String deklaravimoMenuo,
			String deklaravimoData,		
			
			String telefonas,
			String email,
			String deklaracijaGaliojaMetai,
			String deklaracijaGaliojaMenuo,
			String deklaracijaGaliojaDiena,
			
			Istaiga ist,
			String[] s,			
			String userIstaigaId,
			
			boolean print,
			boolean isCompleted,
			
			
			Session session
	)
	throws DatabaseException
	{			
		
		Valstybe pilietis = null;
		Valstybe kitaSalis = null;
		TeritorinisVienetas saviv = null;
		GvnaDeklaracija gd = new GvnaDeklaracija();	 		
		Transaction tx = session.beginTransaction();
		
		pilietis = UtilDelegator.getInstance().getValstybe(pilietybe,session);
	
		if(null != atvykoIsUzsienioSalis)
		kitaSalis = UtilDelegator.getInstance().getValstybe(atvykoIsUzsienioSalis,session);
		if(!"-1".equals(savivaldybe))
		saviv =  AdresaiDelegator.getInstance().getTerVieneta(Long.valueOf(savivaldybe),session);

		int resident = Constants.IS_NOT_RESIDENT;
		Asmuo asmuo = null;
		LaikinasAsmuo laikinasAsmuo = null;
		if (pasmuo instanceof LaikinasAsmuo){
			resident = Constants.IS_NOT_RESIDENT;
			laikinasAsmuo = (LaikinasAsmuo)pasmuo;
		}
		else if(pasmuo instanceof Asmuo){
			resident = Constants.IS_RESIDENT;
			asmuo = (Asmuo)pasmuo;
		}
		
		try {		
			long next = 0;		
			long adrv = 0;
			long terv;
			try {
				terv = saviv.getTerNr();
			} catch (NullPointerException npe) {terv = 0;}			
			
			GyvenamojiVieta gv = null;
			gd.setDeklaravimoData(CalendarUtils.getParsedDate(pateikimoMetai,pateikimoMenuo,pateikimoData));    		
			
			if ("0".equals(ankstesneGyvenamojiVieta)){
				Address adr = null;
				if(null != asmuo)
				{
					adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(),0,session);						
					if (adr != null){
						gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(asmuo.getAsmNr())));
						gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
					}
				}
				else
					if(null != laikinasAsmuo)
					{
						adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(laikinasAsmuo.getId(),0,session);
						if (adr != null){
							gd.setGvtAsmNrAnkstesne(Long.valueOf(String.valueOf(laikinasAsmuo.getId())));
							gd.setGvtNrAnkstesne(Long.valueOf(String.valueOf(adr.getId())));
						}
					}
			}    		
			
			/*gd.setBusena(0);
			if (Constants.IS_NOT_RESIDENT == resident)
			{
				if(isCompleted)gd.setBusena(1); else gd.setBusena(0);
				isCompleted = false;
			}*/
			
			if (resident == Constants.IS_RESIDENT){
				gd.setAsmuo(asmuo);
				Asmenvardis asmenvardis = new Asmenvardis(asmuo);
				gd.setAsmenvardis(asmenvardis);
			}
			else gd.setLaikinasAsmuo(laikinasAsmuo);
			gd.setDokumentoNr(asmensDokumentoNumeris);
			gd.setDokumentoIsdavejas(asmensDokumentoIsdave);
			gd.setSavivaldybe(saviv);
			gd.setDokumentoData(CalendarUtils.getParsedDate(asmensDokumentoIsdavimoMetai,asmensDokumentoIsdavimoMenuo,asmensDokumentoIsdavimoData));
			gd.setDokumentoGaliojimas(CalendarUtils.getParsedDate(leidimoGaliojimoMetai,leidimoGaliojimoMenuo,leidimoGaliojimoData));
			gd.setPateike(new Long(Long.parseLong(deklaracijaPateikta)));
			gd.setPateikeVardas(pateikejoVardas);
			gd.setPateikePavarde(pateikejoPavarde);    		
			gd.setPageidaujaPazymos(new Long(Long.parseLong(pageidaujuDokumenta)));
			gd.setDokumentoRusis(asmensDokumentoRusis);
			gd.setPilietybe(pilietis);  
			gd.setAnkstesnePavarde(ankstesnePavarde);
			gd.setAnkstesneVietaTipas(new Long(Long.parseLong(ankstesneGyvenamojiVieta)));   
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setPastabos(pastabos);
			gd.setSavivaldybePastabos(savivaldybePastabos);
			gd.setPriezastys(priezastys);
			gd.setAnkstesneVietaKita(kitaGyvenamojiVietaAprasymas);
			gd.setAnkstesneGV(kitaSalis);
			gd.setAnkstesneVietaValstybesPastabos(atvykoIsUzsienioTextarea);
			gd.setIstaiga(ist);    		
			gd.setGavimoData(new Timestamp(System.currentTimeMillis()));
			gd.setTelefonas(telefonas);
			gd.setEmail(email);
			gd.setDeklaracijaGalioja(CalendarUtils.getParsedDate(deklaracijaGaliojaMetai, deklaracijaGaliojaMenuo, deklaracijaGaliojaDiena));
			if(true == print) gd.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(session, "Deklaracija","ND",false, userIstaigaId)); //ju.k 2007.08.13
			
			if(true == print) 
			{
				session.save(gd);
				if (isCompleted){
					gd.setBusena(1);
					String igaliotinis = "0";
					long savAsmKodasN = 0;
					long jurAsmKodasN = 0;
					
					if (resident == Constants.IS_RESIDENT)
					{
						if (terv==0) try {
							terv=gd.getSavivaldybe().getTerNr();
						} catch (NullPointerException npe) {}
							gv = createGyvenamojiVietaFull(
									asmuo.getAsmNr(),
									next,
									"K",
									QueryDelegator.HOME_COUNTRY,
									gd.getDeklaravimoData(),
									adrv,
									terv,
									igaliotinis,              
									"",//savininkoTipas,
									savAsmKodasN,
									jurAsmKodasN,
									null,
									"0".equals(pageidaujuDokumenta)?"N":"T",
									"",  
									Long.parseLong(ankstesneGyvenamojiVieta),
									null != atvykoIsUzsienioSalis?kitaSalis.getKodas():"",
									0,
									gd.getId(),
									session,
									0,
									gd.getDeklaracijaGalioja()
							);    			
							//hsession.setAttribute("declarationComplete","");
							gd.setGyvenamojiVieta(gv);
					}
					session.update(gd);
				}
			}
			
			tx.commit();    		
		}
		catch (Exception e){
			tx.rollback();
			throw new DatabaseException(e.getMessage(), e);
		}
		return gd;
	}	
	
	
	public List getNebaigtosIvestiDeklaracijos(HttpServletRequest request,Ordering ordering, String deklTipas, Long savivaldybe, Long seniunija)
	{
		HttpSession session = request.getSession();
		long p_id = 0;
		
		if (((Integer)session.getAttribute("userStatus")).intValue() !=UserDelegator.USER_GLOBAL) {					
	        if (seniunija != null && seniunija.longValue() > 0){
	        	p_id = seniunija.longValue();
	        }
	        else if (savivaldybe != null && savivaldybe.longValue() > 0){
	        	p_id = savivaldybe.longValue();
	        }
		}
		
		String qry = "";
		
		if ("atv".equals(deklTipas)){
			qry = "from Deklaracija d where d.busena=0 and d.saltinis is null and d.gvIsvDklId is null "
					+"and  ( "+p_id+" = 0 or d.istaiga.id = "+p_id+" or d.istaiga.id in (select id from Istaiga where gvist_id_tev = "+p_id+" ))"+ ordering.getOrderString();
		} else if ("isv".equals(deklTipas)){
			qry = "from IsvykimoDeklaracija d where d.busena=0 and d.saltinis is null "
					+"and  ( "+p_id+" = 0 or d.istaiga.id = "+p_id+" or d.istaiga.id in (select id from Istaiga where gvist_id_tev = "+p_id+" ))"+ ordering.getOrderString();
		}
		
		List l = HibernateUtils.currentSession(request).createQuery(qry).list();
		/*
		for (Iterator it=l.iterator(); it.hasNext();){
			Deklaracija d = (Deklaracija)it.next();
			if (d.getGyvenamojiVieta() != null){
				it.remove();
			}
		}*/
		return l;
	}
	
	
	public Deklaracija getDeklaracija(Long id,HttpServletRequest request)
	throws ObjectNotFoundException
	{
		Deklaracija d = (Deklaracija)HibernateUtils.currentSession(request).createQuery("from Deklaracija d where d.id= :id").
		setLong("id",id.longValue()).uniqueResult();
		if(null == d)throw new ObjectNotFoundException("Deklaracija with id ["+id+"] not exist");
		return d;
	}
	
	
	public AtvykimoDeklaracija getAtvykimoDeklaracija(Long id,HttpServletRequest request)
	throws ObjectNotFoundException
	{
		AtvykimoDeklaracija d = (AtvykimoDeklaracija)HibernateUtils.currentSession(request).createQuery("from AtvykimoDeklaracija d where d.id= :id").
		setLong("id",id.longValue()).uniqueResult();
		if(null == d)throw new ObjectNotFoundException("AtvykimoDeklaracija with id ["+id+"] not exist");
		return d;
	}
	
	public List getAtvykimoDeklaracijaInternetu(HttpServletRequest request, DeklaracijaInternetuForm form)
	throws ObjectNotFoundException
	{
		Date dataNuo = null;
		Date dataIki = null;
		
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		
		String appendQuerry = "";
//		if  (form != null){
//			if (!"".equals(form.getDataNuo())){
//				try {					
//					dataNuo = format.parse(form.getDataNuo());					
//				}
//		        catch (ParseException pe){
//		        	pe.printStackTrace();
//		        }		        
//				appendQuerry += " and d.deklaravimoData >= :datanuo";
//			}
//			
//			if (!"".equals(form.getDataIki())){
//				try {					
//					dataNuo = format.parse(form.getDataIki());					
//				}
//		        catch (ParseException pe){
//		        	pe.printStackTrace();
//		        }		        
//				appendQuerry += " and d.deklaravimoData <= :dataiki";
//			}
//		}
		Query query = HibernateUtils.currentSession(request).createQuery("from AtvykimoDeklaracija d where d.saltinis = 1 and d.busena = 0" + appendQuerry);
		if (dataNuo != null){
			query = query.setDate("datanuo", dataNuo);
		}
		if (dataIki != null){
			query = query.setDate("dataiki", dataIki);
		}
		
		return query.list();		
	}
	
	public IsvykimoDeklaracija getIsvykimoDeklaracija(Long id,HttpServletRequest request)
	throws ObjectNotFoundException
	{
		IsvykimoDeklaracija d = (IsvykimoDeklaracija)HibernateUtils.currentSession(request).createQuery("from IsvykimoDeklaracija d where d.id= :id").
		setLong("id",id.longValue()).uniqueResult();
		if(null == d)throw new ObjectNotFoundException("IsvykimoDeklaracija with id ["+id+"] not exist");
		return d;
	}
	
	public List getIsvykimoDeklaracijaInternetu(HttpServletRequest request, DeklaracijaInternetuForm form)
	throws ObjectNotFoundException
	{
		Date dataNuo = null;
		Date dataIki = null;
		
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		
		String appendQuerry = "";
		/*if  (form != null){
			if (!"".equals(form.getDataNuo())){
				try {					
					dataNuo = format.parse(form.getDataNuo());					
				}
		        catch (ParseException pe){
		        	pe.printStackTrace();
		        }		        
				appendQuerry += " and d.deklaravimoData >= :datanuo";
			}
			
			if (!"".equals(form.getDataIki())){
				try {					
					dataIki = format.parse(form.getDataIki());					
				}
		        catch (ParseException pe){
		        	pe.printStackTrace();
		        }		        
				appendQuerry += " and d.deklaravimoData <= :dataiki";
			}
		}*/
		//return HibernateUtils.currentSession(request).createQuery("from IsvykimoDeklaracija d where d.saltinis = 1").list();
		//Solver 2010.10.11: nerodo deklaraciju, kurios apdorotos ir yra isvykimas ateityje
		Query query = HibernateUtils.currentSession(request).createQuery("from IsvykimoDeklaracija d " +
				"where d.saltinis = 1 and d.busena = 0 and d.isvykimasAteityje <> 'T' " + appendQuerry);
		if (dataNuo != null){
			query = query.setDate("datanuo", dataNuo);
		}
		if (dataIki != null){
			query = query.setDate("dataiki", dataIki);
		}
		
		return query.list();
		
		//setLong("id",id.longValue()).uniqueResult();
		//if(null == d)throw new ObjectNotFoundException("AtvykimoDeklaracija with id ["+id+"] not exist");
		//return d;
	}
	
	public GvnaDeklaracija getGvnaDeklaracija(Long id,HttpServletRequest request)
	throws ObjectNotFoundException
	{
		GvnaDeklaracija d = (GvnaDeklaracija)HibernateUtils.currentSession(request).createQuery("from GvnaDeklaracija d where d.id= :id").
		setLong("id",id.longValue()).uniqueResult();
		if(null == d)throw new ObjectNotFoundException("GvnaDeklaracija with id ["+id+"] not exist");
		return d;
	}		
	public List getGvnaDeklaracijaInternetu(HttpServletRequest request, DeklaracijaInternetuForm form)
	throws ObjectNotFoundException
	{	
		Date dataNuo = null;
		Date dataIki = null;
		
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		format.setLenient(false);
		
		String appendQuerry = "";
//		if  (form != null){
//			if (!"".equals(form.getDataNuo())){
//				try {					
//					dataNuo = format.parse(form.getDataNuo());					
//				}
//		        catch (ParseException pe){
//		        	pe.printStackTrace();
//		        }		        
//				appendQuerry += " and d.deklaravimoData >= :datanuo";
//			}
//			
//			if (!"".equals(form.getDataIki())){
//				try {					
//					dataNuo = format.parse(form.getDataIki());					
//				}
//		        catch (ParseException pe){
//		        	pe.printStackTrace();
//		        }		        
//				appendQuerry += " and d.deklaravimoData <= :dataiki";
//			}
//		}
		Query query = HibernateUtils.currentSession(request).createQuery("from GvnaDeklaracija d where d.saltinis = 1 and d.busena = 0" + appendQuerry);
		if (dataNuo != null){
			query = query.setDate("datanuo", dataNuo);
		}
		if (dataIki != null){
			query = query.setDate("dataiki", dataIki);
		}
		
		return query.list();		
	}
	/**
	 * Sukuria naujà gyvenamosios vietos áraðà 
	 
	public GyvenamojiVieta createGyvenamojiVietaFull(
			long p_gvt_asm_nr,
			long p_gvt_nr,
			String p_gvt_tipas,
			String p_gvt_vls_kodas,
			Timestamp p_gvt_data_nuo,
			long p_gvt_adv_nr,
			long p_gvt_atv_nr,
			String p_gvt_sav_ar_igl,              
			String p_gvt_sav_rusis,
			long p_gvt_sav_asm_nr,
			long p_gvt_sav_jur_nr,
			String p_gvt_lka,
			String p_gvt_pazyma,
			String p_gvt_adr_uzsienyje,
            long p_ankstesne_vieta_tipas,
            String p_valst_ankstesne_vieta_id,
			long p_update,
			long p_dkl_id,
			Session session,
			long p_ist_id)
	throws SQLException
	{
		Connection conn = session.connection();
		String qry = "{call gvdis_akts.set_gyv_vieta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		CallableStatement stmt = conn.prepareCall(qry);
		
		stmt.setLong(1, p_gvt_asm_nr);
		stmt.setLong(2, p_gvt_nr);
		stmt.setString(3, p_gvt_tipas);
		stmt.setString(4, p_gvt_vls_kodas);
		
		if (p_gvt_data_nuo == null)
			stmt.setNull(5, java.sql.Types.DATE);
		else
			stmt.setTimestamp(5, p_gvt_data_nuo);
		
		if(0 == p_gvt_adv_nr)stmt.setNull(6,java.sql.Types.INTEGER);
		else stmt.setLong(6,p_gvt_adv_nr);
		if(0 == p_gvt_atv_nr)stmt.setNull(7,java.sql.Types.INTEGER);
		else stmt.setLong(7,p_gvt_atv_nr); 
		
		if ("R".equals(p_gvt_tipas)){
			if ("0".equals(p_gvt_sav_rusis)) stmt.setString(8, "J");
			if ("1".equals(p_gvt_sav_rusis)) stmt.setString(8, "F");
			else stmt.setNull(8,java.sql.Types.VARCHAR);
		}
		if ("V".equals(p_gvt_tipas) || "K".equals(p_gvt_tipas)){
			stmt.setNull(8,java.sql.Types.VARCHAR);	
		}
		else stmt.setNull(8,java.sql.Types.VARCHAR);
		
		stmt.setString(9,p_gvt_sav_ar_igl);
		stmt.setLong(10,p_gvt_sav_asm_nr);
		
		stmt.setLong(11,p_gvt_sav_jur_nr);
		stmt.setString(12,p_gvt_lka);
		
		if("N".equals(p_gvt_pazyma))
			stmt.setNull(13,java.sql.Types.VARCHAR);
		if("T".equals(p_gvt_pazyma))
			stmt.setString(13,p_gvt_pazyma);        		
		
		stmt.setString(14,p_gvt_adr_uzsienyje);
		stmt.setLong(15,p_ankstesne_vieta_tipas);
		stmt.setString(16,p_valst_ankstesne_vieta_id);
		
		stmt.setLong(17,p_update);
		if (p_dkl_id == 0) {
			stmt.setNull(18,OracleTypes.NUMBER);
		}
		else {
			stmt.setLong(18,p_dkl_id);
		}
		
		stmt.setNull(19,java.sql.Types.INTEGER);
		
		stmt.registerOutParameter(1, OracleTypes.NUMBER);
		stmt.registerOutParameter(2, OracleTypes.NUMBER);        		
		
		stmt.execute();
		
		long gvtNr = stmt.getLong(2);
		
		//-->ju.k 2008.01.09
		if (0 != p_gvt_adv_nr)
		{String qry2 = "{call gvdis_utils.atnaujinti_ak_ter_adr_el_h(?,?) }";
		 CallableStatement stmt2 = conn.prepareCall(qry2);
		 stmt2.setLong(1, p_gvt_adv_nr);
		 stmt2.setLong(2, p_ist_id);
		 stmt2.execute();};
        //<--ju.k 2008.01.09
		
		return SprendimaiDelegator.getInstance().getGyvenamojiVieta(session, p_gvt_asm_nr, gvtNr);
	}    
*/
	public GyvenamojiVieta createGyvenamojiVietaFull(
			long p_gvt_asm_nr,
			long p_gvt_nr,
			String p_gvt_tipas,
			String p_gvt_vls_kodas,
			Timestamp p_gvt_data_nuo,
			long p_gvt_adv_nr,
			long p_gvt_atv_nr,
			String p_gvt_sav_ar_igl,              
			String p_gvt_sav_rusis,
			long p_gvt_sav_asm_nr,
			long p_gvt_sav_jur_nr,
			String p_gvt_lka,
			String p_gvt_pazyma,
			String p_gvt_adr_uzsienyje,
            long p_ankstesne_vieta_tipas,
            String p_valst_ankstesne_vieta_id,
			long p_update,
			long p_dkl_id,
			Session session,
			long p_ist_id,
			Timestamp p_dkl_galioja_iki )
	throws SQLException
	{
		Connection conn = session.connection();
		String qry = "{call gvdis_akts.set_gyv_vieta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		CallableStatement stmt = conn.prepareCall(qry);
		
		stmt.setLong(1, p_gvt_asm_nr);
		stmt.setLong(2, p_gvt_nr);
		stmt.setString(3, p_gvt_tipas);
		stmt.setString(4, p_gvt_vls_kodas);
		
		if (p_gvt_data_nuo == null)
			stmt.setNull(5, java.sql.Types.DATE);
		else{
			Date utilDate = new Date();		
			stmt.setTimestamp(5, new Timestamp(utilDate.getTime())); 
			//stmt.setTimestamp(5, p_gvt_data_nuo);
		}
		
		if(0 == p_gvt_adv_nr)stmt.setNull(6,java.sql.Types.INTEGER);
		else stmt.setLong(6,p_gvt_adv_nr);
		if(0 == p_gvt_atv_nr)stmt.setNull(7,java.sql.Types.INTEGER);
		else stmt.setLong(7,p_gvt_atv_nr); 
		
		if ("R".equals(p_gvt_tipas)){
			if ("0".equals(p_gvt_sav_rusis)) stmt.setString(8, "J");
			if ("1".equals(p_gvt_sav_rusis)) stmt.setString(8, "F");
			else stmt.setNull(8,java.sql.Types.VARCHAR);
		}
		if ("V".equals(p_gvt_tipas) || "K".equals(p_gvt_tipas)){
			stmt.setNull(8,java.sql.Types.VARCHAR);	
		}
		else stmt.setNull(8,java.sql.Types.VARCHAR);
		
		stmt.setString(9,p_gvt_sav_ar_igl);
		stmt.setLong(10,p_gvt_sav_asm_nr);
		
		stmt.setLong(11,p_gvt_sav_jur_nr);
		stmt.setString(12,p_gvt_lka);
		
		if("N".equals(p_gvt_pazyma))
			stmt.setNull(13,java.sql.Types.VARCHAR);
		if("T".equals(p_gvt_pazyma))
			stmt.setString(13,p_gvt_pazyma);        		
		
		stmt.setString(14,p_gvt_adr_uzsienyje);
		stmt.setLong(15,p_ankstesne_vieta_tipas);
		stmt.setString(16,p_valst_ankstesne_vieta_id);
		
		stmt.setLong(17,p_update);
		if (p_dkl_id == 0) {
			stmt.setNull(18,OracleTypes.NUMBER);
		}
		else {
			stmt.setLong(18,p_dkl_id);
		}
		
		stmt.setNull(19,java.sql.Types.INTEGER);
		stmt.setTimestamp(20, p_dkl_galioja_iki );
		stmt.registerOutParameter(1, OracleTypes.NUMBER);
		stmt.registerOutParameter(2, OracleTypes.NUMBER);        		
		
		stmt.execute();
		
		long gvtNr = stmt.getLong(2);
		
		//-->ju.k 2008.01.09
		if (0 != p_gvt_adv_nr)
		{String qry2 = "{call gvdis_utils.atnaujinti_ak_ter_adr_el_h(?,?) }";
		 CallableStatement stmt2 = conn.prepareCall(qry2);
		 stmt2.setLong(1, p_gvt_adv_nr);
		 stmt2.setLong(2, p_ist_id);
		 stmt2.execute();};
        //<--ju.k 2008.01.09
		
		return SprendimaiDelegator.getInstance().getGyvenamojiVieta(session, p_gvt_asm_nr, gvtNr);
	}    

	
	
	/**
	 * Sukuria naujà gyvenamosios vietos áraðà 
	 * Overloaded: pridetas adresinio vieneto kampo numeris
	 * naudojama set_gyv_vieta su kampo numeriu parametruose
	 */
	public GyvenamojiVieta createGyvenamojiVietaFull(
			long p_gvt_asm_nr,
			long p_gvt_nr,
			String p_gvt_tipas,
			String p_gvt_vls_kodas,
			Timestamp p_gvt_data_nuo,
			long p_gvt_adv_nr,
			long p_gvt_atv_nr,
			String p_gvt_sav_ar_igl,              
			String p_gvt_sav_rusis,
			long p_gvt_sav_asm_nr,
			long p_gvt_sav_jur_nr,
			String p_gvt_lka,
			String p_gvt_pazyma,
			String p_gvt_adr_uzsienyje,
            long p_ankstesne_vieta_tipas,
            String p_valst_ankstesne_vieta_id,
			long p_update,
			long p_dkl_id,
			Session session,
			long p_ist_id,
			Long p_gvt_kampo_nr,
			Timestamp p_dkl_galioja_iki)
	throws SQLException
	{
		Connection conn = session.connection();
		String qry = "{call gvdis_akts.set_gyv_vieta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		CallableStatement stmt = conn.prepareCall(qry);
		
		stmt.setLong(1, p_gvt_asm_nr);
		stmt.setLong(2, p_gvt_nr);
		stmt.setString(3, p_gvt_tipas);
		stmt.setString(4, p_gvt_vls_kodas);
		//stmt.setTimestamp(5, p_gvt_data_nuo);
		Date utilDate = new Date();		
		stmt.setTimestamp(5, new Timestamp(utilDate.getTime()));
		
		if(0 == p_gvt_adv_nr){
			stmt.setNull(6,java.sql.Types.INTEGER);
			stmt.setNull(19,java.sql.Types.INTEGER);
		}
		else { 
			stmt.setLong(6,p_gvt_adv_nr);
			if (p_gvt_kampo_nr == null) stmt.setNull(19,java.sql.Types.INTEGER);
			else stmt.setLong(19,p_gvt_kampo_nr.longValue());
		}
		if(0 == p_gvt_atv_nr) stmt.setNull(7,java.sql.Types.INTEGER);
		else stmt.setLong(7,p_gvt_atv_nr); 
		
		if ("R".equals(p_gvt_tipas)){
			if ("0".equals(p_gvt_sav_rusis)) stmt.setString(8, "J");
			if ("1".equals(p_gvt_sav_rusis)) stmt.setString(8, "F");
			else stmt.setNull(8,java.sql.Types.VARCHAR);
		}
		if ("V".equals(p_gvt_tipas) || "K".equals(p_gvt_tipas)){
			stmt.setNull(8,java.sql.Types.VARCHAR);	
		}
		else stmt.setNull(8,java.sql.Types.VARCHAR);
		
		stmt.setString(9,p_gvt_sav_ar_igl);
		stmt.setLong(10,p_gvt_sav_asm_nr);
		
		stmt.setLong(11,p_gvt_sav_jur_nr);
		stmt.setString(12,p_gvt_lka);
		
		if("N".equals(p_gvt_pazyma))
			stmt.setNull(13,java.sql.Types.VARCHAR);
		if("T".equals(p_gvt_pazyma))
			stmt.setString(13,p_gvt_pazyma);        		
		
		stmt.setString(14,p_gvt_adr_uzsienyje);
		stmt.setLong(15,p_ankstesne_vieta_tipas);
		stmt.setString(16,p_valst_ankstesne_vieta_id);
		
		stmt.setLong(17,p_update);	
		stmt.setLong(18,p_dkl_id);
		stmt.setTimestamp(20, p_dkl_galioja_iki);
		
		stmt.registerOutParameter(1, OracleTypes.NUMBER);
		stmt.registerOutParameter(2, OracleTypes.NUMBER);        		
		
		stmt.execute();
		
		long gvtNr = stmt.getLong(2);
		
		//-->ju.k 2008.01.09
		if (0 != p_gvt_adv_nr)
		{String qry2 = "{call gvdis_utils.atnaujinti_ak_ter_adr_el_h(?,?) }";
		 CallableStatement stmt2 = conn.prepareCall(qry2);
		 stmt2.setLong(1, p_gvt_adv_nr);
		 stmt2.setLong(2, p_ist_id);
		 stmt2.execute();};
        //<--ju.k 2008.01.09
		
		return SprendimaiDelegator.getInstance().getGyvenamojiVieta(session, p_gvt_asm_nr, gvtNr);
	}	
	
	/**
	 * Graþina ðiam asmeniui iðduotø asmens dokumentø sàraðà
	 * jei yra galiojantys (sarasas)
	 * jei
	 */
	public List getAsmensDokumentai(HttpServletRequest request, Asmuo asmuo, String dekldoknr)
	{
		ArrayList al = new ArrayList();
		Set processedRusys = new HashSet();
		Iterator it = HibernateUtils.currentSession(request).createQuery(
			"from AsmensDokumentas d where d.asmuo=:asmuo order by d.dokBusena, d.dokIsdData desc"
		).setEntity("asmuo", asmuo).list().iterator();
		while (it.hasNext()){
			AsmensDokumentas ad = (AsmensDokumentas)it.next();

			if (ad.getDokNegaliojaNuo() != null || "N".equals(ad.getDokBusena())){ // negalioja
				if (al.size() == 0 ){ // paima pirma negaliojanti is selecto
					processedRusys.add(ad.getDokRusis());
					al.add(ad);
					if (dekldoknr == null || dekldoknr.equals("") || dekldoknr.equals(ad.getDokNum())) {
						break;
					}
				}
				if (dekldoknr != null && dekldoknr.equals(ad.getDokNum())){ 
					processedRusys.add(ad.getDokRusis());
					al.add(ad);
					break;
				}
			}
			else { // galioja
				processedRusys.add(ad.getDokRusis());
				al.add(ad);
			}
			
		}
		return al;
	}	
	/**
	 * Graþina asmens dokumentà su duotu ID 
	 */
	public AsmensDokumentas getAsmensDokumentas(HttpServletRequest request, long id)
	{
		AsmensDokumentas dok = (AsmensDokumentas)HibernateUtils.currentSession(request).load(AsmensDokumentas.class, new Long(id));
		return dok;
	}
	
	/**
	 * Gauti sekanti gyvenamosuios vietos numerá 
	 * @param id - asmens numeris
	 * @throws DatabaseException
	 */
	public long getNextGvtNr(long id,HttpServletRequest req)
	throws DatabaseException
	{
		long retVal = 0;
		try {
			String qry = "{? = call gvdis_akts.get_next_gvt_nr(?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			stmt.setLong(2,id);
			stmt.execute();
			retVal = stmt.getLong(1);
			stmt.close();
		}
		catch (SQLException sqlEx) {
			throw new DatabaseException(sqlEx);
		}
		return retVal;
	}
	
	
	public String[] chkAsmDekl(long asmkodas,long istaigosid,String tipas,HttpServletRequest req)
	throws DatabaseException
	{
		long retVal = 0;
		String retVal2 = "";
		try {
			String qry = "{? = call gvdis_akts.chk_asm_dekl(?,?,?,?) }";
			Connection conn = HibernateUtils.currentSession(req).connection();
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			stmt.setLong(2,asmkodas);
			stmt.setLong(3,istaigosid);	        
			stmt.setString(4,tipas);   
			stmt.registerOutParameter(5, OracleTypes.VARCHAR);        
			stmt.execute();
			retVal = stmt.getLong(1);
			retVal2 = stmt.getString(5);        
			stmt.close();
		}
		catch (SQLException sqlEx) {
			throw new DatabaseException(sqlEx);
		}
		String[] st = new String[2];
		st[0] = String.valueOf(retVal);
		st[1] = retVal2;
		return st;
	}    
	
	public String switchDeclarationToAsmuo(long asmid,long deklid,HttpServletRequest request)
	throws ObjectNotFoundException,DatabaseException
	{
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		try{
			
			LaikinasAsmuo laikinasAsmuo = null;
			Asmuo asmuo = null;
			Deklaracija deklaracija = null;
			asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(asmid,request);

			deklaracija = getDeklaracija(new Long(deklid),request);
			
		/*	if(hasNotCompletedDeclartions(asmuo,request))
			{
				return "hasNotCompletedDeclartions";
			}
			*/
				//////////////////////////////////////////////
				
	/*			
		    String[] st = DeklaracijosDelegator.getInstance(request).chkAsmDekl(Long.parseLong(akf.getAsmKodas()),((Long)request.getSession().getAttribute("userIstaigaId")).longValue(),type,request);
		    
		    if(!"0".equals(st[0]))
		    {
		        return null;
		    }
		    if(null != st[1])
		    {
		    	request.setAttribute("warning",st[1]);
		    	session.setAttribute(Constants.CENTER_STATE, Constants.HAVE_NOT_COMPLETED_DECLARATION);
		    	return (mapping.findForward(Constants.CONTINUE_WARNING));
		    }
*/
				
				
				//////////////////////////////////////////////
			
			laikinasAsmuo = deklaracija.getLaikinasAsmuo();
			
			String type = null;
			long p_gvt_sav_asm_nr = 0;
			long p_gvt_sav_jur_nr = 0;
			String p_gvt_sav_ar_igl = "";             
			String p_gvt_sav_rusis = "";
			String vlst_kodas = "";
			long p_gvt_adv_nr = 0;
			long p_gvt_atv_nr = 0;
			Long p_gvt_kampo_nr = null;
			long ankstesneGyvenamojiVieta = deklaracija.getAnkstesneVietaTipas().intValue();
			String kitaSalis = deklaracija.getPilietybe().getKodas();
			
			if(deklaracija instanceof AtvykimoDeklaracija)
			{
				type = "R";
				AtvykimoDeklaracija ad = (AtvykimoDeklaracija)deklaracija;
				
				try{
				p_gvt_sav_asm_nr = Long.parseLong(ad.getJaKodas());
				}catch(NumberFormatException nfre)
				{p_gvt_sav_asm_nr = 0;}
				
				try{
				p_gvt_sav_jur_nr = Long.parseLong(ad.getFaKodas());
				}catch(NumberFormatException nfre)
				{p_gvt_sav_jur_nr = 0;}
				
				p_gvt_sav_ar_igl = ad.getJaKodas();
				p_gvt_sav_rusis = ad.getFaKodas();
				p_gvt_adv_nr = ad.getTmpGvtAdvNr().intValue();
				p_gvt_atv_nr = ad.getTmpGvtAtvNr().intValue();
				p_gvt_kampo_nr = ad.getTmpGvtKampoNr();
				p_gvt_sav_ar_igl = String.valueOf(ad.getSavininkoIgaliotinis());
				p_gvt_sav_rusis = String.valueOf(ad.getRysysSuGv());
				vlst_kodas = QueryDelegator.HOME_COUNTRY;
			}
			if(deklaracija instanceof IsvykimoDeklaracija)
			{
				type = "V";	
				
				p_gvt_sav_asm_nr = 0;
				p_gvt_sav_jur_nr = 0;				
				
				p_gvt_sav_ar_igl = "";
				p_gvt_sav_rusis = "";
				p_gvt_adv_nr = 0;
				p_gvt_atv_nr = 0;
				p_gvt_sav_ar_igl = "";
				p_gvt_sav_rusis = "";
				vlst_kodas = deklaracija.getAnkstesneGV().getKodas();
			}
			if(deklaracija instanceof GvnaDeklaracija)
			{
				type = "K";	
				p_gvt_sav_asm_nr = 0;
				p_gvt_sav_jur_nr = 0;				
				p_gvt_sav_ar_igl = "";
				p_gvt_sav_rusis = "";
				p_gvt_adv_nr = 0;
				if (((GvnaDeklaracija)deklaracija).getSavivaldybe() != null)
					p_gvt_atv_nr = ((GvnaDeklaracija)deklaracija).getSavivaldybe().getTerNr();
				p_gvt_sav_ar_igl = "";
				p_gvt_sav_rusis = "";
				vlst_kodas = QueryDelegator.HOME_COUNTRY;				
			}			
			
			
			deklaracija.setAsmuo(asmuo);
			deklaracija.setAsmenvardis(deklaracija.getAsmenvardis());
			
			deklaracija.setLaikinasAsmuo(null);
			if(null != laikinasAsmuo)session.delete(laikinasAsmuo);
			long next = 0;
			
			//-->> ju.k 2007.09.27
			long asm_nr = (long)asmuo.getAsmNr();
			try {
				long gvtNr = QueryDelegator.getInstance().getAsmGyvenamojiVieta(request, asm_nr);
				GyvenamojiVieta gv_esama = QueryDelegator.getInstance().getGyvenamojiVieta(request, asm_nr, gvtNr);
				Valstybe vls = (Valstybe)gv_esama.getValstybe();
				if (gv_esama.getGvtDataIki() == null){
				
				Calendar data_iki = Calendar.getInstance();
				data_iki.setTime(deklaracija.getDeklaravimoData());
				data_iki.add(Calendar.DATE, -1);
				Date dekl_data_iki = data_iki.getTime();
		
				String message = QueryDelegator.getInstance().alterGyvenamojiVieta(request,
						                                                       gvtNr,
						                                                       asm_nr,
						                                                       gv_esama.getGvtDataNuo(),
						                                                       dekl_data_iki,
						                                                       gv_esama.getGvtTipas(),
						                                                       gv_esama.getGvtAtvNr(),
						                                                       gv_esama.getGvtAdvNr(),
						                                                       gv_esama.getGvtKampoNr(),
						                                                       vls.getKodas(),
						                                                       gv_esama.getGvtAdrUzsenyje(),
						                                                       true);
			    request.setAttribute("message", message);
		                                         };
			} catch (ObjectNotFoundException onfe)
			{
			}
            //<<--ju.k 2007.09.27

			GyvenamojiVieta gv = createGyvenamojiVietaFull(
					asmuo.getAsmNr(),
					next,
					type,
					vlst_kodas,//deklaracija.getPilietybe().getKodas(),
					deklaracija.getDeklaravimoData(),
					
					p_gvt_adv_nr,
					p_gvt_atv_nr,
					
					p_gvt_sav_ar_igl,              
					p_gvt_sav_rusis,
					
					p_gvt_sav_asm_nr,
					p_gvt_sav_jur_nr,
					deklaracija.getPastabos(),
					"N",
					"",
					ankstesneGyvenamojiVieta,
					kitaSalis,
					0,
					deklaracija.getId(),
					session,
					0,
					p_gvt_kampo_nr,
					deklaracija.getDeklaracijaGalioja() );
			deklaracija.setGyvenamojiVieta(gv);
			deklaracija.setBusena(1);
			session.update(deklaracija);			
			tx.commit();
		}
		catch (Exception e){
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e);
		}
		return "ok";
	}
	
	/**
	 * Iðtrina nurodytà deklaracijà.
	 */
	public int deleteDeclaration(HttpServletRequest request, Deklaracija d)
		//throws DatabaseException
	{
		Session session = HibernateUtils.currentSession(request);		
		Transaction tx = session.beginTransaction();
		try {
			CallableStatement stmt = session.connection().prepareCall("{ call gvdis_akts.delete_declaration(?) }");
			stmt.setLong(1, d.getId());
			stmt.execute();
			stmt.close();
			tx.commit();
		}
		catch (Exception e){
			tx.rollback();
			//throw new DatabaseException(e);
			return -1;
		}
		return 0;
	}
	public int deleteDeclarationWithoutTransaction(HttpServletRequest request, Deklaracija d)
	//throws DatabaseException
{
	Session session = HibernateUtils.currentSession(request);		
	try {
		CallableStatement stmt = session.connection().prepareCall("{ call gvdis_akts.delete_declaration(?) }");
		stmt.setLong(1, d.getId());
		stmt.execute();
		stmt.close();
	}
	catch (Exception e){
		return -1;
	}
	return 0;
}	
	public boolean hasNotCompletedDeclartions(Asmuo asmuo,HttpServletRequest request)
	{
		List l = HibernateUtils.currentSession(request).
			createQuery("from Deklaracija d where d.asmuo.asmNr = :asmid and d.busena = 0").
				setLong("asmid",asmuo.getAsmNr()).list();
		if(0 != l.size())return true;
		else return false;
	}
	public List getDeklaracijosPazymos(long declid,Session session)
	throws DatabaseException,ObjectNotFoundException
	{
		return session.createQuery("from GvPazyma gvp where gvp.deklaracija.id = :declid").
		setLong("declid",declid).list();
	}
	
	public String getDocumentIdByNr(HttpServletRequest req, String nr)
	{
		String retVal = "";
		try {
			AsmensDokumentas dok = (AsmensDokumentas)HibernateUtils.currentSession(req).createQuery("from AsmensDokumentas where dokNum = :nr").setString("nr", nr).uniqueResult();
			if (dok != null){
				retVal = String.valueOf(dok.getDokNr());
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return retVal;
	}
	
	public AsmensDokumentas getAsmensDokumentas (HttpServletRequest req, String nr, Asmuo asmuo) {
		AsmensDokumentas dok = null;
		try {
			dok = (AsmensDokumentas)HibernateUtils.currentSession(req).createQuery("from AsmensDokumentas where dokNum = :nr and asmuo=:asmuo").setString("nr", nr).setEntity("asmuo", asmuo).uniqueResult();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return dok;
	}
    public boolean isPersonHaveDeclaredResidence(HttpServletRequest req, long asmid)
    {
		Long result = (Long)HibernateUtils.currentSession(req).createSQLQuery(
	        "select count(gvt_asm_nr) as value from gvdisvw_gyvenamosios_vietos d "+
	        "where d.GVT_ASM_NR = ? and (d.GVT_DATA_IKI is null or d.GVT_DATA_IKI > sysdate) and (d.GVT_TIPAS='R')").
	        addScalar("value", Hibernate.LONG).setLong(0,asmid).uniqueResult();
		//TODO: atkeisti i true 12-01
		if(0 != result.longValue())
			return true;
		else return false;
    }

    public Asmuo getAsmuo(long asmNr,HttpServletRequest req)
	throws ObjectNotFoundException,DatabaseException
	{
	    return (Asmuo)HibernateUtils.currentSession(req).load(Asmuo.class,new Long(asmNr));
    }		
    
    public boolean chkNepilnametisReturn (HttpServletRequest req, int age)
    {
    	boolean nepilnametis=false;
		Date gimimoData = null;
		GvdisBase asmuo = (GvdisBase)req.getSession().getAttribute("asmuo");
		if (asmuo instanceof Asmuo){
			gimimoData = ((Asmuo)asmuo).getAsmGimData();
		}
		else if (asmuo instanceof LaikinasAsmuo){
			gimimoData = ((LaikinasAsmuo)asmuo).getGimimoData();
		}
		//System.out.println("Gimimo data: "+gimimoData);
		if (gimimoData != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(gimimoData);
			calendar.add(Calendar.YEAR, age);
			gimimoData = calendar.getTime();
			if ("save".equals(req.getParameter("mode"))){
				if (gimimoData.after(new Date())){
					nepilnametis=true;
				}
			}
		}
		return nepilnametis;
    }
   
    public boolean chkNepilnametis (HttpServletRequest req)
    {
	    boolean nepilnametis=false;
	    nepilnametis=chkNepilnametisReturn(req,Constants.ADULT_AGE);
	    return nepilnametis;
    }
    
    public boolean chkNepilnametisFull (HttpServletRequest req)
    {
    	boolean nepilnametis=false;
    	nepilnametis=chkNepilnametisReturn(req,Constants.FULL_ADULT_AGE);
		return nepilnametis;
    }
    
    public boolean isPersonResidenceInIstaiga(HttpServletRequest request, Asmuo asmuo) {
		HttpSession session = request.getSession();
		Long istId = (Long)session.getAttribute("userIstaigaId");
		long s = 0; 
		boolean rez = false;			
		Set gyvenamosiosVietos = asmuo.getGyvenamosiosVietos();	
		
		for (Iterator it=gyvenamosiosVietos.iterator(); it.hasNext(); ){
			GyvenamojiVieta vt = (GyvenamojiVieta)it.next();			        		
			if (null == vt.getGvtDataIki())
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
                    	rez = true;        				
            		}
    			}
    			catch (SQLException sqlEx) {
                    //throw new DatabaseException(sqlEx);
                }		        		
	}
		return rez;
	}

	public int deleteDeclarationBaigta(HttpServletRequest request, Deklaracija d, String journalType) 
		throws Exception {
		Session session = HibernateUtils.currentSession(request);	
		GyvenamojiVieta currentGv = d.getGyvenamojiVieta();
		GyvenamojiVieta ankstesneGv = null;
		
		if (currentGv == null) {
			Constants.Println(request, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		}
		
		if(d.getLaikinasAsmuo() != null){
			 if(-1 == UserDelegator.getInstance().DeleteTempCitizen(d.getLaikinasAsmuo().getId(),request))
		    	{
		    	    return -1;
		    	}
		}
		else{
		try {
			if (d.getGvtAsmNrAnkstesne() != null && d.getGvtNrAnkstesne() != null)
				ankstesneGv = QueryDelegator.getInstance().getGyvenamojiVieta(request,
						d.getGvtAsmNrAnkstesne().longValue(), d.getGvtNrAnkstesne().longValue());
		} catch (ObjectNotFoundException e) {
		}
		
		Transaction tx = session.beginTransaction();		
		try {
			Deklaracija dekl = (Deklaracija) session.createQuery("from Deklaracija d where d.gvtAsmNrAnkstesne=:asmNr and d.gvtNrAnkstesne=:nr")
				.setLong("asmNr", currentGv.getGvtAsmNr())
				.setLong("nr", currentGv.getGvtNr())
				.uniqueResult();
			if(dekl != null){
				dekl.setGvtAsmNrAnkstesne((ankstesneGv == null)? null:new Long(ankstesneGv.getGvtAsmNr()));
				dekl.setGvtNrAnkstesne((ankstesneGv == null)? null:new Long(ankstesneGv.getGvtNr()));
				session.update(dekl);				
			}
			
			if(!"4".equals(journalType))
				PazymosDelegator.getInstance().removeGvPazymaByGVT(request, currentGv);		
			else
				PazymosDelegator.getInstance().removeGVNAPazymaByGVT(request, currentGv);	
			
			session.flush(); //!!!
			
			CallableStatement stmt = session.connection().prepareCall("{ call gvdis_akts.delete_declaration(?) }");
			stmt.setLong(1, d.getId());
			stmt.execute();
			stmt.close();			
			
			int i = d.getCalcTipas();
			if (i != Deklaracija.TYPE_OLD)
				QueryDelegator.getInstance().naikintiAndAtstatytiGyvenamojiVieta(request, currentGv,
						ankstesneGv);
			else 
				QueryDelegator.getInstance().naikintiAndAtstatytiGyvenamojiVieta(request, currentGv,
						null);
			
			tx.commit();		
		}
		catch (Exception e){
			tx.rollback();			
			throw new Exception(e);				
		}
		}
		return 0;
	}
	
	/**
	 * Gauti istaigos ID pagal adresa, naudojama deklaracijas pateikiant per WS, 
  		-- is adreso bandome nustatyti priklausyma istaigai.
	 * @param terNr
	 * @param advNr
	 * @param request
	 * @return
	 */
	public Long getIstaigaIdByAdress (Long terNr, Long advNr, HttpServletRequest request) {
		String qry = "{? = call gvdis_akts.get_ist_id_by_address(?,?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();
   		Long ret = null;
   		try {
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			if (terNr != null) stmt.setLong(2, terNr.longValue());
			else stmt.setNull(2, OracleTypes.NUMBER);
			
			if (advNr != null)stmt.setLong(3, advNr.longValue());
			else stmt.setNull(3, OracleTypes.NUMBER);
			
			stmt.execute();
			ret = new Long(stmt.getLong(1));
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
   		return ret;
	}
	
	public Long getIstaigaIdByAdress1(Long asmNr, Long gvNr, HttpServletRequest request) {
		
		//System.out.println("BEGIN getIstaigaIdByAdress1");
		String qry = "{? = call gvdis_akts.get_ist_id_by_address1(?,?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();
   		Long ret = null;
	   		try {
				CallableStatement stmt = conn.prepareCall(qry);
				stmt.registerOutParameter(1, OracleTypes.NUMBER);
				if (asmNr == null) {
					stmt.setNull(2, OracleTypes.NUMBER);
				} else {
					stmt.setLong(2, asmNr.longValue());
				}
				
				if (gvNr == null) {
					stmt.setNull(3, OracleTypes.NUMBER);
				} else {
					stmt.setLong(3, gvNr.longValue());
				}
				
				stmt.execute();
				ret = new Long(stmt.getLong(1));
				stmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		//System.out.println("END getIstaigaIdByAdress1");
		return ret;
	}
	/*public Long getIstaigaIdByAdress (Long addressId, HttpServletRequest request) {
		String qry = "{? = call gvdis_akts.get_ist_id_by_address(?) }";
   		Connection conn = HibernateUtils.currentSession(request).connection();
   		Long ret = null;
   		try {
			CallableStatement stmt = conn.prepareCall(qry);
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			stmt.setLong(2, addressId.longValue());
			stmt.execute();
			ret = new Long(stmt.getLong(1));
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
   		return ret;
	}*/

	/*
	public void updateDeklaracijosAsmenvardzius(HttpServletRequest request, Deklaracija deklaracija) throws DatabaseException{
		if(deklaracija.getAsmuo() != null){
			String[] asmv = deklaracija.getAsmuo().getVardasPavardeByDate(request, deklaracija.getDeklaravimoData(), deklaracija.getDokumentoNr());
			deklaracija.getAsmuo().setVardas(asmv[0]);
			deklaracija.getAsmuo().setPavarde(asmv[1]);
		}		
	}
	*/ // I.N. 2010.01.25	
}