package lt.solver.gvdis.reports;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;

public class PDFReports {
	
	private Deklaracija deklaracija = null;
	private static String declarationType = null; 
	private String deklaracijaFile = "";
	private AtvykimoDeklaracija atvykimoDeklaracija = null;
	private IsvykimoDeklaracija isvykimoDeklaracija = null;
	private GvnaDeklaracija gvnaDeklaracija = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	private String darbuotojas = "";
	private String deklaracijaGaliojaIki;
	//GVNA papildomi kintamieji is session
	private String savivaldybes_pav = "";
	private String ankstesne_gv_pavadinimas = "";
	
	public void setDeklaracijaGaliojaIki(String deklaracijaGalioja)
	{
		this.deklaracijaGaliojaIki = deklaracijaGalioja;
	}
	
	public PDFReports(Deklaracija deklaracija, String pathToJaspersFolder, String darbuotojas,
			String savivaldybes_pav, String ankstesne_gv_pavadinimas) {
		this.deklaracija = deklaracija;
		this.darbuotojas = darbuotojas;
		this.ankstesne_gv_pavadinimas = ankstesne_gv_pavadinimas;
		if(deklaracija instanceof AtvykimoDeklaracija)
		{
			atvykimoDeklaracija = (AtvykimoDeklaracija)deklaracija;
			declarationType = Constants.IN_DECLARATION_FORM;
			deklaracijaFile = pathToJaspersFolder + Constants.IN_DECLARATION_REPORT_FILE;
		}
		else
		if(deklaracija instanceof IsvykimoDeklaracija)
		{
			isvykimoDeklaracija = (IsvykimoDeklaracija)deklaracija;
			declarationType = Constants.OUT_DECLARATION_FORM;
			deklaracijaFile = pathToJaspersFolder + Constants.OUT_DECLARATION_REPORT_FILE;
		}
		else
		if(deklaracija instanceof GvnaDeklaracija)
		{
			this.savivaldybes_pav = savivaldybes_pav;
			gvnaDeklaracija = (GvnaDeklaracija)deklaracija;
			declarationType = Constants.GVNA_DECLARATION_FORM;
			deklaracijaFile = pathToJaspersFolder + Constants.GVNA_DECLARATION_REPORT_FILE;
		}
	}
	
	public void makePdf(OutputStream os) {
		
		try {			
			Map jparams = mapParams();			
//			jparams.put("param1", "Mano parametras1");
//			jparams.put("param2", "Mano parametras2");
//			deklaracijaFile = "C:/Users/Romas/Darbai/GVDIS/3.7.1/test1.jasper";
			JasperPrint jasperPrint = JasperFillManager.fillReport(deklaracijaFile, jparams, new JREmptyDataSource());
			JRPdfExporter exporter = new JRPdfExporter(); 
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "CP1257");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            exporter.exportReport();
            
            // Kodas skirtas naujesniai jasper versijai
//			JasperPrint jasperPrint = null;
//			Map jparams = mapParams();
//			jasperPrint = JasperFillManager.fillReport(deklaracijaFile, jparams, new JREmptyDataSource());
//			SimplePdfReportConfiguration repConfig = new SimplePdfReportConfiguration();
//			JRPdfExporter exporter = new JRPdfExporter();
//			exporter.setConfiguration(repConfig);
//			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
//			exporter.exportReport();
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private Map generateAsmInfo() {
		Map retValue = new HashMap();
		
		boolean asmuoDataOk = (deklaracija.getAsmuo() != null);
		
		String asmensKodas = "";
		if(asmuoDataOk)asmensKodas = String.valueOf(deklaracija.getAsmuo().getAsmKodas());
		retValue.put("p_pateike", asmensKodas);
		
		String data = "";
		if (asmuoDataOk) data = dateFormat.format(deklaracija.getAsmuo().getAsmGimData());
		else data = dateFormat.format(deklaracija.getLaikinasAsmuo().getGimimoData());
		retValue.put("p_pateikeGimData", data);
		
		String lytis = "0"; //vyras
		String l = "";
		if (asmuoDataOk) l = deklaracija.getAsmuo().getAsmLytis();
		else l = deklaracija.getLaikinasAsmuo().getLytis();
		if("M".equals(l))lytis = "1";
		
		retValue.put("p_lytis", lytis);
		
		String vardas = "";
		if (asmuoDataOk) vardas = deklaracija.getAsmuo().getVardas();
		else vardas = deklaracija.getLaikinasAsmuo().getVardas();
		retValue.put("p_pateikeVardas", vardas.toUpperCase());
		
		String pavarde = "";
		if (asmuoDataOk) pavarde = deklaracija.getAsmuo().getPavarde();
		else pavarde = deklaracija.getLaikinasAsmuo().getPavarde();
		retValue.put("p_pateikePavarde", pavarde.toUpperCase());
		
		return retValue;
	}

	private String generatePilietybe() {
		String retVal = "";
		// kazkodel generuojama ankstesniame kode buvo taip...
		try {
			retVal = deklaracija.getPilietybe().getPilietybe();
		} catch (NullPointerException e){}		
		
		return retVal;
	}
	
	private String generatePateiktasDoc() {
		String retVal = "";
//		dokumento rusis:
//			0,1-pasas;
//			2-ATK;
//			3,4,5,6,7,8-leitimas gyventi;
//			9-gimimo liudijimas
		if (deklaracija.getDokumentoRusis() != null) {
			//logika paimta is ankstesion kodo: PDFGenerator.generateFirstInnerSecondTable()
			//TODO: issiaiskinti ar cia ok!
			String type = deklaracija.getDokumentoRusis();
			if("LR pilieèio pasas".equals(type) )retVal = "0";
			if("Pasas".equals(type) || "PASAS".equals(type) ) retVal = "1";			
			if("ATK".equals(type) || "ASMENS TAPATYBËS KORTELË".equals(type))  retVal = "2"; 
			if("PL".equals(type)) retVal = "3";
			if("LL".equals(type)) retVal = "4";
			if("PL (EB)".equals(type)) retVal = "5";
			if("LL (EB)".equals(type)) retVal = "6";
			if("PL (ES)".equals(type)) retVal = "7";
			if("LL (ES)".equals(type)) retVal = "8";
			if("Kitas".equals(type)) retVal = "9"; 
		} 
		
		return retVal;
	}
	
	private Map mapParams() { 
		//datos eina kaip stringas, su skirtuku - ar . 
		Map retValue = new HashMap();
		String tmpString = "";
		/////////////////////////////////////////////////////////
		//////  bendri parametrai
		 
		/// headeris
		retValue.put("p_istaiga", deklaracija.getIstaiga().getOficialusPavadinimasRead());
		retValue.put("p_regNr", null != deklaracija.getRegNr()? deklaracija.getRegNr(): " ");
		String gavimoData = dateFormat.format(deklaracija.getGavimoData());
		retValue.put("p_gavimoData", gavimoData);
		retValue.put("p_pateikimoData", gavimoData);
		/// Asmens informacija
		retValue.putAll(generateAsmInfo());
		/// Kontaktiniai duomenys
		retValue.put("p_telefonas", deklaracija.getTelefonas());
		retValue.put("p_email", deklaracija.getEmail());
		retValue.put("p_gyvVietaAnksciau", ankstesne_gv_pavadinimas );
		/// Piloietybe
		retValue.put("p_pilietybe", generatePilietybe());
		//Po susitikimo su Mikënu 2015-11-27
		//retValue.put("p_gautiPazyma", deklaracija.getPageidaujaPazymos().toString());
		//retValue.put("p_pateikimoBudas", deklaracija.getPateike().toString());
		/// Pateiktas dokumentas 
		retValue.put("p_dokumentoRusis", generatePateiktasDoc());
		String dokumentoNum = deklaracija.getDokumentoNr();
		if(null != dokumentoNum)			
			retValue.put("p_dokumentoNr", dokumentoNum); 
		if(null != deklaracija.getDokumentoData())
			retValue.put("p_dokumentoData", dateFormat.format(deklaracija.getDokumentoData()));
		
		java.sql.Timestamp galiojimas = deklaracija.getDokumentoGaliojimas();
		if(null != galiojimas)
			retValue.put("p_dokumentoGaliojimas", dateFormat.format(galiojimas));
		
		tmpString = deklaracija.getDokumentoIsdavejas() != null ? deklaracija.getDokumentoIsdavejas().toUpperCase():"";
		retValue.put("p_dokumentoIsdavejas", tmpString);
		
		///
		//// Duomenys generuojami kiekvienai deklaracijai atskirai
		///
		///Footeris
		retValue.put("p_darbuotojoVardas", darbuotojas);
		//deklaracija.getDeklaravimoData()
		/////// Bendri paramtrai pabaiga
		//////////////////////////////////////////////////////////
		
		// retValue.put("", );
		//
		if (declarationType == Constants.IN_DECLARATION_FORM) {
			retValue.putAll(mapInDeclarationParams());
		} else if (declarationType == Constants.OUT_DECLARATION_FORM) {
			retValue.putAll(mapOutDeclarationParams());
		} else if (declarationType == Constants.GVNA_DECLARATION_FORM) {
			retValue.putAll(mapGVNADeclarationParams());
		}
//		switch(declarationType) {
//			case Constants.IN_DECLARATION_FORM:
//				retValue.putAll(mapInDeclarationParams());
//				break;
//			case Constants.OUT_DECLARATION_FORM:
//				retValue.putAll(mapOutDeclarationParams());
//				break;
//			case Constants.GVNA_DECLARATION_FORM:
//				retValue.putAll(mapGVNADeclarationParams());
//				break;
//		}
		
		return retValue;
	}
	
	private Map mapGVNADeclarationParams() {
		Map retValue = new HashMap();
		//"9. Savivaldybë, kurioje asmuo gyvena" 
		retValue.put("savivaldybes_pav", savivaldybes_pav);
		String tmpStr = ankstesne_gv_pavadinimas;
		/*
		if (gvnaDeklaracija.getAnkstesneVietaTipas().intValue() == 1) {
			tmpStr = ankstesne_gv_pavadinimas.toUpperCase() + "\n" + deklaracija.getAnkstesneVietaValstybesPastabos();
		} else if (gvnaDeklaracija.getAnkstesneVietaTipas().intValue() == 2) {
			tmpStr = gvnaDeklaracija.getAnkstesneVietaKita();
		}
		*/
		String galiojimas = "";
		if(null != deklaracijaGaliojaIki){
			galiojimas = " iki " + deklaracijaGaliojaIki;
		}
		// 10. Ankstesnës gyvenamosios vietos adresas ar savivaldybë, ið kurios atvyko: 
		retValue.put("p_gyvVietaAnksciau", tmpStr + galiojimas);
		
		//11.?? Turetu buti priezastys?,gvnaDeklaracija.getPriezastys()??
		
		//p_savivaldybe
		// retValue.put("", );
		return retValue;
	}
	private Map mapOutDeclarationParams() {
		Map retValue = new HashMap();
		String tmpStr = "";
		if (isvykimoDeklaracija.getIsvykimoData()!=null)
			tmpStr = dateFormat.format(isvykimoDeklaracija.getIsvykimoData());
		retValue.put("p_isvykimoData", tmpStr);
		
		// retValue.put("", );
		return retValue;
	}
	
	private Map mapInDeclarationParams() {
		Map retValue = new HashMap();

		String tmpStr = atvykimoDeklaracija.getRysysSuGv()==0? "0" : "1";
		//retValue.put("p_esuSavininkas", tmpStr);
		tmpStr = "";
		/*
		if (atvykimoDeklaracija.getAtvykimoData()!=null)
			tmpStr = dateFormat.format(atvykimoDeklaracija.getAtvykimoData());
		retValue.put("p_pateikimoData", tmpStr);
		*/
		
		// retValue.put("", );
		return retValue;
	}
}
