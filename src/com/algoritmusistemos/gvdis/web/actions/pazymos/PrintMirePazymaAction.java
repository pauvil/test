package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;

public class PrintMirePazymaAction extends Action {

	public ActionForward execute(ActionMapping mapper, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws Exception 
		{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_MIRE);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});

		GvPazyma pazyma = (GvPazyma)session.getAttribute("mirePazyma");
		GyvenamojiVieta gvt = pazyma.getGyvenamojiVieta();		
		AtvykimoDeklaracija deklaracija = null;
		try {
			deklaracija = (AtvykimoDeklaracija)pazyma.getDeklaracija();
		}
		catch (ClassCastException cce) { deklaracija = null;}
		if (deklaracija == null && gvt != null){
			try {
				deklaracija = (AtvykimoDeklaracija)gvt.getDeklaracija();
			} catch (ClassCastException cce) { deklaracija = null;}
		}
		
		// Ið naujo uþkrauname objektus, paimtus ið sesijos
		if (pazyma.getId() > 0){
			pazyma = PazymosDelegator.getInstance().getGvPazyma(request, pazyma.getId());
		}
		if (deklaracija != null){
			deklaracija = DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(new Long(deklaracija.getId()), request);
		}
		if (gvt != null){
			gvt = QueryDelegator.getInstance().getGyvenamojiVieta(request, gvt.getGvtAsmNr(), gvt.getGvtNr());
		}

		// Jei deklaruoja laikinas asmuo, arba deklaracija nebaigta pildyti 
		if (gvt == null && deklaracija != null && (deklaracija.getTmpGvtAdvNr().intValue() > 0 || deklaracija.getTmpGvtAtvNr().intValue() > 0)){
			gvt = new GyvenamojiVieta();
			gvt.setAsmuo(deklaracija.getAsmuo());
			if (deklaracija.getTmpGvtAdvNr().intValue()>0) gvt.setGvtAdvNr(deklaracija.getTmpGvtAdvNr());
			if (deklaracija.getTmpGvtAtvNr().intValue()>0) gvt.setGvtAtvNr(deklaracija.getTmpGvtAtvNr());
		}
		
		
		String asmensVardas; 
		String asmensPavarde; 
		String asmensKodas; 
		Timestamp asmensGimimoData; 
		Timestamp asmensMirimoData;
		Asmuo asmuo = (gvt != null) ? gvt.getAsmuo() : deklaracija.getAsmuo();
		LaikinasAsmuo laikinasAsmuo;
		if (null != asmuo) {
			asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmuo.getAsmNr());
			asmensVardas = asmuo.getVardas();
			asmensPavarde = asmuo.getPavarde();
			asmensKodas = Long.toString(asmuo.getAsmKodas().longValue());
			asmensGimimoData = asmuo.getAsmGimData();			
		}
		else {
			laikinasAsmuo = deklaracija.getLaikinasAsmuo();
			asmensVardas = laikinasAsmuo.getVardas();
			asmensPavarde = laikinasAsmuo.getPavarde();
			asmensKodas = "";
			asmensGimimoData = laikinasAsmuo.getGimimoData();			
		}
		asmensMirimoData = asmuo.getAsmMirtiesData();
		List vaikai = (List)session.getAttribute("mireVaikai");
		
		// Workaround'as, kad sekanciai pazymai nebutu "svetimu" vaiku, del
		// "kreivo" vaiku i pazyma itraukimo mechanizmo;
		// (vienu atveju itraikua GvPazymaAcion kitu atveju PrintGvPazymaAction ??) 
		//
		// Kai vaikai perduodami per sesija (nera parametro getParameter("vaikai"))
		// tai vaikai ir lieka kintamajame vaikai
		//
		// Kai vaikai neperduodami (tik parametras) tada vaikus istraukia getVaikai()
		//
		// Taigi session.removeAttribute uztikrina, kad po pazymos suformavimo
		// neliks jokiu vaiku sesijoje;
		session.removeAttribute("mireVaikai");
		
    	Map flatAddress = QueryDelegator.getInstance().getFlatAddress(request, gvt);
    	//String userName	= (String)session.getAttribute("userName"); I.N.
    	//String userSurname	= (String)session.getAttribute("userSurname"); I.N.
		
		if ("on".equals(request.getParameter("register"))){
			if (pazyma.getIstaiga() == null){
				Long istaigaId = (Long)session.getAttribute("userIstaigaId");
				pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(), request));
			}
			PazymosDelegator.getInstance().saveGvPazyma(request, pazyma);
		}
		if ("on".equals(request.getParameter("vaikai"))){
			pazyma.setItrauktiVaikus(1);
			if (gvt != null && asmuo != null){
				vaikai = PazymosDelegator.getInstance().getVaikai(request, asmuo, pazyma.getPazymosData(), gvt);
			}
		}
		
		try {
			String s = new String();
			String separator = System.getProperty("file.separator");
			FileInputStream fr = new FileInputStream(this.getServlet().getServletConfig().getServletContext().getRealPath("") + separator + "files" + separator + "MirePazyma.xml");
			byte b;
			byte[] ba = new byte[fr.available()];
			int i = 0;
			while( (b = (byte)fr.read()) != -1)
			{
				ba[i++] = b;
			}
			fr.close();
			s = new String(ba, "UTF-8");
			String duomenys = UtilDelegator.getInstance().getIstaiga(((Long)session.getAttribute("userIstaigaId")).longValue(), request).getRekvizSpausdinimui();
			s = s.replaceAll("~~~DOKUMENTO_SUDARYTOJO_PAVADINIMAS~~~", (null == session.getAttribute("userIstaiga")) ? "" : (String)session.getAttribute("userIstaiga"));
			s = s.replaceAll("~~~DOKUMENTO_SUDARYTOJO_DUOMENYS~~~", (null == duomenys) ? "" : duomenys);
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
			s = s.replaceAll("~~~DATA~~~", sdf.format(new Date()));
			s = s.replaceAll("~~~PAZYMOS_NR~~~", (null == pazyma.getRegNr()) ? "" : pazyma.getRegNr());
			s = s.replaceAll("~~~VARDAS_PAVARDE~~~", ((null == asmensVardas) ? "" : asmensVardas) + " " + ((null == asmensPavarde) ? "" : asmensPavarde));
			s = s.replaceAll("~~~ASMENS_KODAS~~~", (null == asmensKodas || asmensKodas.equals("")) ? "-" : asmensKodas);
			s = s.replaceAll("~~~GD~~~", (null == asmensGimimoData) ? "" : sdf.format(asmensGimimoData));
			s = s.replaceAll("~~~MD~~~", (null == asmensMirimoData) ? "" : sdf.format(asmensMirimoData));
			
			String sdate = "";
			/*if(null != deklaracija)
			{
				if(null != deklaracija.getDeklaravimoData())
					sdate = sdf.format(deklaracija.getDeklaravimoData());
			}
			else sdate = sdf.format(gvt.getGvtRegData());*/
			
			if (gvt!=null)
			{
				if (gvt.getGvtDataNuo()!=null)
				//if (gvt.getGvtRegData() !=null)
				{
					//sdate = sdf.format(gvt.getGvtRegData());
					sdate = sdf.format(gvt.getGvtDataNuo());
				}
				else if (null != deklaracija)
					if (null != deklaracija.getDeklaravimoData()) {
						sdate = sdf.format(deklaracija.getDeklaravimoData());
					}
			}
			else
			{
				if (null != deklaracija) {
					if(null != deklaracija.getDeklaravimoData())
						sdate = sdf.format(deklaracija.getDeklaravimoData());
				}
			}
			if("K".equals(gvt.getGvtTipas())){
				s = s.replaceAll("DEKLARUOTÀ", "");
				s = s.replaceAll("~~~SAKINYS~~~", " átrauktas á gyvenamosios vietos nedeklaravusiø asmenø apskaità");
				s = s.replaceAll("~~~DD~~~", (null == sdate) ? "" : sdate);
				s = s.replaceAll("~~~DEKL_D_T~~~", "");
				s = s.replaceAll("~~~NUO_IKI~~~", "");
			}
			else if(!"R".equals(gvt.getGvtTipas())){
				s = s.replaceAll("~~~SAKINYS~~~", ", deklaravo savo gyvenamàjà vietà adresu:");
				s = s.replaceAll("~~~DD~~~", (null == sdate) ? "" : sdate);
				s =s.replaceAll("~~~NUO_IKI~~~", "Adresas panaikintas " + sdf.format(gvt.getGvtDataIki()));
				s = s.replaceAll("~~~DEKL_D_T~~~", "(deklaravimo data)");
			}
			else if(gvt.getGvtDataIki() != null && (gvt.getGvtDataIki().getTime() < asmensMirimoData.getTime())){
				String removeLine = "<w:pict><v:line id=\"_x0000_s1037\" style=\"position:absolute;left:0;text-align:left;z-index:5\" from=\"117pt,.6pt\" to=\"171pt,.6pt\"/></w:pict>";
				s = s.replaceAll("~~~DEKL_D_T~~~", "");
				s = s.replaceAll(removeLine, "");
				s = s.replaceAll("~~~DD~~~", "");
				s = s.replaceAll("~~~SAKINYS~~~", " Paskutinë gyvenamoji vieta buvo deklaruota adresu:");
				s = s.replaceAll("~~~NUO_IKI~~~", "nuo " + sdate + " iki " + sdf.format(gvt.getGvtDataIki()) + ".");
			}
			else{
				s = s.replaceAll("~~~SAKINYS~~~", ", deklaravo savo gyvenamàjà vietà adresu:");
				s = s.replaceAll("~~~DD~~~", (null == sdate) ? "" : sdate);
				s =s.replaceAll("~~~NUO_IKI~~~", "");
				s = s.replaceAll("~~~DEKL_D_T~~~", "(deklaravimo data)");
			}
			
			
			String sav = (String)flatAddress.get("Miesto savivaldybë");
			if (null == sav) {
				sav = (String)flatAddress.get("Rajono savivaldybë");
			}
			s = s.replaceAll("~~~SAVIVALDYBE~~~", (null == sav) ? "" : sav);
			String miestas = (String)flatAddress.get("Miestas");
			if (null == miestas) {
				miestas = (String)flatAddress.get("Rajoninio pavaldumo miestas");
			}
			if (null == miestas) {
				miestas = (String)flatAddress.get("Miestelis");
			}
			s = s.replaceAll("~~~MIESTAS~~~", (null == miestas) ? "" : miestas);
			String seniunija = (null == flatAddress.get("Seniûnija")) ? "" : (String)flatAddress.get("Seniûnija");
			String kaimas = (null == flatAddress.get("Kaimas")) ? "" : (String)flatAddress.get("Kaimas");
			if ((0 < seniunija.length()) && (0 < kaimas.length())) {
				seniunija += ", ";
			}
			if (0 < kaimas.length()) {
				seniunija += kaimas;
			}
			s = s.replaceAll("~~~SENIUNIJA~~~", (null == seniunija) ? "" : seniunija);
			s = s.replaceAll("~~~GATVE~~~", (null == flatAddress.get("Gatvë")) ? "" : (String)flatAddress.get("Gatvë"));
			s = s.replaceAll("~~~NAMO_NR~~~", (null == flatAddress.get("Namo nr.")) ? "" : (String)flatAddress.get("Namo nr."));
			s = s.replaceAll("~~~KORPUSO_NR~~~", (null == flatAddress.get("Korpuso nr.")) ? "" : (String)flatAddress.get("Korpuso nr."));
			s = s.replaceAll("~~~BUTO_NR~~~", (null == flatAddress.get("Buto nr.")) ? "" : (String)flatAddress.get("Buto nr."));
			//s = s.replaceAll("~~~DVARPAV~~~", userName + " " + userSurname); kom ju.k 2007.06.12

			String blockOneChild = "" +
				"<w:p wsp:rsidR=\"009D6193\" wsp:rsidRPr=\"00311D13\" wsp:rsidRDefault=\"009D6193\" wsp:rsidP=\"009D6193\">" +
				"<w:pPr>" +
				"<w:tabs>" +
				"<w:tab w:val=\"left\" w:pos=\"360\" /> " +
				"<w:tab w:val=\"left\" w:pos=\"3420\" /> " +
				"<w:tab w:val=\"left\" w:pos=\"5040\" /> " +
				"<w:tab w:val=\"left\" w:pos=\"6480\" /> " +
				"</w:tabs>" +
				"<w:ind w:right=\"-114\" /> " +
				"</w:pPr>" +
				"<w:r>" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:pict>" +
				"<v:line id=\"_x0000_s1028\" style=\"position:absolute;flip:y;z-index:3\" from=\"252pt,10.9pt\" to=\"306pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r>" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:pict>" +
				"<v:line id=\"_x0000_s1027\" style=\"position:absolute;flip:y;z-index:2\" from=\"171pt,10.9pt\" to=\"234pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r>" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:pict>" +
				"<v:line id=\"_x0000_s1029\" style=\"position:absolute;flip:y;z-index:4\" from=\"324pt,10.9pt\" to=\"378pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"360\" wx:tlc=\"none\" wx:cTlc=\"7\" /> " +
				"</w:r>" +
				"<w:r>" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:pict>" +
				"<v:line id=\"_x0000_s1026\" style=\"position:absolute;flip:y;z-index:1;mso-position-horizontal-relative:text;mso-position-vertical-relative:text\" from=\"0,10.9pt\" to=\"153pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r wsp:rsidRPr=\"00E6237E\">" +
				"<w:t>~~~VVARPAV~~~</w:t> " +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"1545\" wx:tlc=\"none\" wx:cTlc=\"33\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidRPr=\"00832BEF\">" +
				"<w:t>~~~VAK~~~</w:t> " +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"585\" wx:tlc=\"none\" wx:cTlc=\"12\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidRPr=\"00311D13\">" +
				"<w:t>~~~VGD~~~</w:t> " +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"435\" wx:tlc=\"none\" wx:cTlc=\"9\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidRPr=\"00311D13\">" +
				"<w:t>~~~VDD~~~</w:t> " +
				"</w:r>" +
				"</w:p>" +
				"<w:p wsp:rsidR=\"00F35594\" wsp:rsidRPr=\"00E3698F\" wsp:rsidRDefault=\"009D6193\" wsp:rsidP=\"009D6193\">" +
				"<w:pPr>" +
				"<w:tabs>" +
				"<w:tab w:val=\"left\" w:pos=\"360\" /> " +
				"<w:tab w:val=\"left\" w:pos=\"3420\" /> " +
				"<w:tab w:val=\"left\" w:pos=\"5040\" /> " +
				"<w:tab w:val=\"left\" w:pos=\"6480\" /> " +
				"</w:tabs>" +
				"<w:ind w:right=\"-114\" /> " +
				"<w:jc w:val=\"both\" /> " +
				"</w:pPr>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"360\" wx:tlc=\"none\" wx:cTlc=\"7\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00F35594\" wsp:rsidRPr=\"00E3698F\">" +
				"<w:t>(vardas (-ai) ir pavarde)</w:t> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00F35594\" wsp:rsidRPr=\"00E3698F\">" +
				"<w:tab wx:wTab=\"1200\" wx:tlc=\"none\" wx:cTlc=\"26\" /> " +
				"<w:t>(asmens kodas)</w:t> " +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"375\" wx:tlc=\"none\" wx:cTlc=\"7\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00F35594\" wsp:rsidRPr=\"00E3698F\">" +
				"<w:t>(gimimo data)</w:t> " +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"360\" wx:tlc=\"none\" wx:cTlc=\"7\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00F35594\" wsp:rsidRPr=\"00E3698F\">" +
				"<w:t>(deklaravimo data)</w:t> " +
				"</w:r>" +
				"</w:p>";
			String blockAllChildren = "";

			if (vaikai != null && vaikai.iterator().hasNext()) {
				s = s.replaceAll("~~~TITLE_VAIKAI~~~", "Ðiuo adresu deklaruota nepilnameèiø vaikø gyvenamoji vieta:");
				for (Iterator it = vaikai.iterator();it.hasNext();) {
					Asmuo vaikas = (Asmuo)it.next();
					long asmNr = vaikas.getAsmNr();
					vaikas = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmNr);
					GyvenamojiVieta vaikoGvt = (GyvenamojiVieta)vaikas.getGyvenamosiosVietos().iterator().next();
					blockAllChildren = blockAllChildren + blockOneChild;
					blockAllChildren = blockAllChildren.replaceAll("~~~VVARPAV~~~", ((null == vaikas.getVardas()) ? "" : vaikas.getVardas()) + " " + ((null == vaikas.getPavarde()) ? "" : vaikas.getPavarde()));
					blockAllChildren = blockAllChildren.replaceAll("~~~VAK~~~", Long.toString(vaikas.getAsmKodas().longValue()));
					blockAllChildren = blockAllChildren.replaceAll("~~~VGD~~~", (null == vaikas.getAsmGimData()) ? "" : sdf.format(vaikas.getAsmGimData()));
					if (vaikoGvt.getGvtDataNuo()!= null)
						blockAllChildren = blockAllChildren.replaceAll("~~~VDD~~~", sdf.format(vaikoGvt.getGvtDataNuo()));
					else blockAllChildren = blockAllChildren.replaceAll("~~~VDD~~~", "");					
				}
			}
			else {
				s = s.replaceAll("~~~TITLE_VAIKAI~~~", "");
			}
			//s = s.replaceAll("<w:p wsp:rsidR=\"001A1BDB\" wsp:rsidRPr=\"001A1BDB\" wsp:rsidRDefault=\"001A1BDB\" wsp:rsidP=\"001A1BDB\"><w:pPr><w:ind w:right=\"-114\"/></w:pPr><w:r wsp:rsidRPr=\"001A1BDB\"><w:t>~~~BLOCK_VAIKAI~~~</w:t></w:r></w:p>", blockAllChildren);  //kom ju.k 2007.06.14 nes keitesi sablonas 
			s = s.replaceAll("<w:p wsp:rsidR=\"001A1BDB\" wsp:rsidRPr=\"00FD4D46\" wsp:rsidRDefault=\"001A1BDB\" wsp:rsidP=\"001A1BDB\"><w:pPr><w:ind w:right=\"-114\"/><w:rPr><w:sz w:val=\"22\"/><w:sz-cs w:val=\"22\"/></w:rPr></w:pPr><w:r wsp:rsidRPr=\"00FD4D46\"><w:rPr><w:sz w:val=\"22\"/><w:sz-cs w:val=\"22\"/></w:rPr><w:t>~~~BLOCK_VAIKAI~~~</w:t></w:r></w:p>", blockAllChildren); //ju.k 2007.06.14
//			s = s.replaceAll("<w:p.*(~~~BLOCK_VAIKAI~~~).*</w:p>", blockAllChildren);

			response.setContentType("application/msword");
			OutputStream os = response.getOutputStream();
			os.write(s.getBytes("UTF-8"));
			os.close();
		}
		catch (IOException ioe){
			throw new InternalException(ioe);
		}

	    return null;
	}
	}


