package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvnaPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;

public class PrintGvnaPazymaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException, ObjectNotFoundException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_GVNA);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});

		GvnaPazyma pazyma = (GvnaPazyma)session.getAttribute("gvnaPazyma");
		
		GyvenamojiVieta gvt = pazyma.getGyvenamojiVieta();
		GvnaDeklaracija deklaracija = pazyma.getDeklaracija();
		if (deklaracija == null && gvt != null){
			deklaracija = (GvnaDeklaracija)gvt.getDeklaracija();
		}
		
		// Ið naujo uþkrauname objektus, paimtus ið sesijos
		if (pazyma.getId() > 0){
			pazyma = PazymosDelegator.getInstance().getGvnaPazyma(request, pazyma.getId());
		}
		if (deklaracija != null){
			deklaracija = DeklaracijosDelegator.getInstance(request).getGvnaDeklaracija(new Long(deklaracija.getId()), request);
		}
		if (gvt != null){
			gvt = QueryDelegator.getInstance().getGyvenamojiVieta(request, gvt.getGvtAsmNr(), gvt.getGvtNr());
			
		}
		
		String asmensVardas; 
		String asmensPavarde; 
		String asmensKodas; 
		Timestamp asmensGimimoData; 
		Asmuo asmuo = (gvt != null) ? gvt.getAsmuo() : deklaracija.getAsmuo();
		LaikinasAsmuo laikinasAsmuo = null;
		if (null != asmuo) {
			asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmuo.getAsmNr());
			asmensVardas = asmuo.getVardas();
			asmensPavarde = asmuo.getPavarde();
			asmensKodas = Long.toString(asmuo.getAsmKodas().longValue()) +",";
			asmensGimimoData = asmuo.getAsmGimData();
		}
		else {
			laikinasAsmuo = deklaracija.getLaikinasAsmuo();
			asmensVardas = laikinasAsmuo.getVardas();
			asmensPavarde = laikinasAsmuo.getPavarde();
			asmensKodas = "           ";
			asmensGimimoData = laikinasAsmuo.getGimimoData();
		}
		//deklaracija.setDeklaracijaGalioja(QueryDelegator.getInstance().getGyvenamojiVGaliojimas(session, asmuo.getAsmNr()));
		List vaikai = (List)session.getAttribute("gvnaVaikai");
    	//String userName	= (String)session.getAttribute("userName"); I.N.
    	//String userSurname	= (String)session.getAttribute("userSurname"); I.N.
		
		if ("on".equals(request.getParameter("register"))){
			if (pazyma.getIstaiga() == null){
				Long istaigaId = (Long)session.getAttribute("userIstaigaId");
				pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(), request));
			}
			PazymosDelegator.getInstance().saveGvnaPazyma(request, pazyma);
		}
		if ("on".equals(request.getParameter("vaikai"))){
			pazyma.setItrauktiVaikus(1);
			if (gvt != null && asmuo != null){
				vaikai = PazymosDelegator.getInstance().getVaikai(request, asmuo, pazyma.getPazymosData(), gvt);
			}
		}
		//System.out.println(gvt.getGvtGaliojaIki().toString());
		
		// Wordinës paþymos formavimas 
		try {
			String s = new String();
			String separator = System.getProperty("file.separator");
			FileInputStream fr = new FileInputStream(this.getServlet().getServletConfig().getServletContext().getRealPath("") + separator + "files" + separator + "GvnaPazyma.xml");
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
			s = s.replaceAll("~~~AKODAS~~~", asmensKodas);
			s = s.replaceAll("~~~GDATA~~~", (null == asmensGimimoData) ? "" : sdf.format(asmensGimimoData));
			if (gvt != null) 
				s = s.replaceAll("~~~GVTDATANUO~~~", (null == gvt.getGvtDataNuo()) ? "" : sdf.format(gvt.getGvtDataNuo()));
			else if(laikinasAsmuo == null) 
				s = s.replaceAll("~~~GVTDATANUO~~~", "");
			else if(laikinasAsmuo != null && deklaracija != null) 
				s = s.replaceAll("~~~GVTDATANUO~~~", sdf.format(deklaracija.getDeklaravimoData()));
			if (gvt.getGvtGaliojaIki() != null)
				s = s.replaceAll("~~~GVTDATAGALIOJAIKI~~~", " iki " + gvt.getGvtGaliojaIki().toString().substring(0, 10));
			else 
				s = s.replaceAll("~~~GVTDATAGALIOJAIKI~~~", "");
			/*
			String savivaldybe = "                                   ";
			if (deklaracija != null) {
				if (deklaracija.getSavivaldybe() != null){
					savivaldybe = deklaracija.getSavivaldybe().getTerPav();
				}
			}
			*/
			String savivaldybe = new String();
			if (gvt != null) 
				savivaldybe = QueryDelegator.getInstance().getAddressString(request, gvt);
			else if(laikinasAsmuo != null && deklaracija != null) 
				savivaldybe = deklaracija.getSavivaldybe().getTerPav();
			
			if(savivaldybe != null)
				savivaldybe = savivaldybe.replaceAll("sav.", "");
			s = s.replaceAll("~~~SAVIVALDYBE~~~", (null == savivaldybe) ? "                                   " : savivaldybe);
//			s = s.replaceAll("~~~DVARPAV~~~", userName + " " + userSurname); kom ju.k 2007.06.12
//			s = s.replaceAll("~~~DATA~~~", sdf.format(pazyma.getPazymosData()));

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
				s = s.replaceAll("~~~TITLE_VAIKAI~~~", "Á gyvenamosios vietos nedeklaravusiø asmenø apskaità átraukti ðioje savivaldybëje kartu gyvenantys nepilnameèiai vaikai:");
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
			//s = s.replaceAll("<w:p wsp:rsidR=\"00BF7919\" wsp:rsidRDefault=\"00617B9B\" wsp:rsidP=\"00BF7919\"><w:pPr><w:tabs><w:tab w:val=\"left\" w:pos=\"3744\"/><w:tab w:val=\"left\" w:pos=\"7404\"/></w:tabs><w:ind w:right=\"-114\"/></w:pPr><w:r wsp:rsidRPr=\"00617B9B\"><w:t>~~~BLOCK_VAIKAI~~~</w:t></w:r></w:p>", blockAllChildren); //kom ju.k 2007.06.14 nes keitesi sablonas
			s = s.replaceAll("<w:p wsp:rsidR=\"00BF7919\" wsp:rsidRPr=\"0014125C\" wsp:rsidRDefault=\"00617B9B\" wsp:rsidP=\"00BF7919\"><w:pPr><w:tabs><w:tab w:val=\"left\" w:pos=\"3744\"/><w:tab w:val=\"left\" w:pos=\"7404\"/></w:tabs><w:ind w:right=\"-114\"/><w:rPr><w:sz w:val=\"22\"/><w:sz-cs w:val=\"22\"/></w:rPr></w:pPr><w:r wsp:rsidRPr=\"0014125C\"><w:rPr><w:sz w:val=\"22\"/><w:sz-cs w:val=\"22\"/></w:rPr><w:t>~~~BLOCK_VAIKAI~~~</w:t></w:r></w:p>", blockAllChildren); //ju.k 2007.06.14
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

/*
OutputStream os = response.getOutputStream();
PrintStream out = new PrintStream(os);
response.setContentType("text/plain");
out.println(session.getAttribute("userIstaiga"));
out.println(gvt.getGvtDataNuo());
out.println(pazyma.getRegNr());
out.println("---------------------");
out.println(asmuo.getVardas());
out.println(asmuo.getPavarde());
out.println(pazyma.getPazymosData());
out.println(pazyma.getPastabos());
out.println("---------------------");
if (vaikai != null) for (Iterator it=vaikai.iterator(); it.hasNext();){
	Asmuo vaikas = (Asmuo)it.next();
	out.println(vaikas.getVardas() + " " + vaikas.getPavarde());
}
*/