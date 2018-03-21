package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import org.hibernate.Hibernate;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.PazymaForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SavPazyma;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class PrintSavPazymaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException, ObjectNotFoundException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_GV);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});

		List gyventojai = (List)session.getAttribute("gyventojai");
		GyvenamojiVieta gvt = (GyvenamojiVieta)session.getAttribute("gyvenamojiVieta");
		gvt.setGvtKampoNr((Long)session.getAttribute("gvtKampoNr"));
		Map flatAddress = QueryDelegator.getInstance().getFlatAddress(request, gvt);
//    	String stringAddress = QueryDelegator.getInstance().getAddressString(request, gvt);
    	String userName	= (String)session.getAttribute("userName");
    	String userSurname	= (String)session.getAttribute("userSurname");
		String regNr = "";
		
		
		Constants.Println(request, "st1");		
		if(session.getAttribute("registrNr") != null)
			regNr = (String) session.getAttribute("registrNr");
		Constants.Println(request, "st2");		
				   	
    	if ("on".equals(request.getParameter("register"))){    		    	
    			// Tieto 2012-09-13
            	//String istaigaId = UserDelegator.getInstance().getDarbIstaiga(request)[0];
            	Long istaigaId = (Long)session.getAttribute("userIstaigaId");
            	SavPazyma pazyma = new SavPazyma();
            	pazyma.setGvtAdvNr(gvt.getGvtAdvNr());
            	pazyma.setGvtKampoNr(gvt.getGvtKampoNr());
            	pazyma.setGvtAtvNr(gvt.getGvtAtvNr());
            	pazyma.setPazymosData(gvt.getGvtRegData());
    			//pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(new Long(istaigaId).longValue(), request));
            	pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(), request));
    			Constants.Println(request, "st3");		
    			try{
    				PazymosDelegator.getInstance().saveSavininkoPazyma(request, pazyma);           
    				Constants.Println(request, "st4");		
    			}catch (Exception e){
    				Constants.Println(request, "st5");		
    				throw new InternalException(e);
    	    	}
    			if(pazyma.getRegNr() != null){
    				regNr = pazyma.getRegNr();
    				Constants.Println(request, "st6");		
    			}
    	}
    	
    	
		try {
			Constants.Println(request, "st7");		
			String s = new String();
			String separator = System.getProperty("file.separator");
			FileInputStream fr = new FileInputStream(this.getServlet().getServletConfig().getServletContext().getRealPath("") + separator + "files" + separator + "SavPazyma.xml");
			Constants.Println(request, "st8");		
			byte b;
			byte[] ba = new byte[fr.available()];
			int i = 0;
			while( (b = (byte)fr.read()) != -1)
			{
				ba[i++] = b;
			}
			fr.close();
			s = new String(ba, "UTF-8");
			Constants.Println(request, "st9");		

			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
			String duomenys = UtilDelegator.getInstance().getIstaiga(((Long)session.getAttribute("userIstaigaId")).longValue(), request).getRekvizSpausdinimui();
			s = s.replaceAll("~~~DOKUMENTO_SUDARYTOJO_PAVADINIMAS~~~", (null == session.getAttribute("userIstaiga")) ? "" : (String)session.getAttribute("userIstaiga"));
			s = s.replaceAll("~~~DOKUMENTO_SUDARYTOJO_DUOMENYS~~~", (null == duomenys) ? "" : duomenys);
			s = s.replaceAll("~~~PAZYMOS_NR~~~",  regNr);
			s = s.replaceAll("~~~DATA~~~", sdf.format(new Date()));
			String sav = (String)flatAddress.get("Miesto savivaldybë");
			Constants.Println(request, "st10");		
			if (null == sav) {
				Constants.Println(request, "st11");		
				sav = (String)flatAddress.get("Rajono savivaldybë");
			}
			Constants.Println(request, "st12");		
			s = s.replaceAll("~~~SAVIVALDYBE~~~", (null == sav) ? "" : sav);
			String miestas = (String)flatAddress.get("Miestas");
			if (null == miestas) {
				Constants.Println(request, "st13");		
				miestas = (String)flatAddress.get("Rajoninio pavaldumo miestas");
			}
			Constants.Println(request, "st14");		
			if (null == miestas) {
				Constants.Println(request, "st15");		
				miestas = (String)flatAddress.get("Miestelis");
			}
			Constants.Println(request, "st15");		
			s = s.replaceAll("~~~MIESTAS~~~", (null == miestas) ? "" : miestas);
			String seniunija = (null == flatAddress.get("Seniûnija")) ? "" : (String)flatAddress.get("Seniûnija");
			String kaimas = (null == flatAddress.get("Kaimas")) ? "" : (String)flatAddress.get("Kaimas");
			Constants.Println(request, "st16");		
			if ((0 < seniunija.length()) && (0 < kaimas.length())) {
				seniunija += ", ";
			}
			if (0 < kaimas.length()) {
				seniunija += kaimas;
			}
			Constants.Println(request, "st17");		
			s = s.replaceAll("~~~SENIUNIJA~~~", (null == seniunija) ? "" : seniunija);
			s = s.replaceAll("~~~GATVE~~~", (null == flatAddress.get("Gatvë")) ? "" : (String)flatAddress.get("Gatvë"));
			s = s.replaceAll("~~~NAMO_NR~~~", (null == flatAddress.get("Namo nr.")) ? "" : (String)flatAddress.get("Namo nr."));
			s = s.replaceAll("~~~KORPUSO_NR~~~", (null == flatAddress.get("Korpuso nr.")) ? "" : (String)flatAddress.get("Korpuso nr."));
			s = s.replaceAll("~~~BUTO_NR~~~", (null == flatAddress.get("Buto nr.")) ? "" : (String)flatAddress.get("Buto nr."));
			s = s.replaceAll("~~~DVARPAV~~~", ((null == userName) ? "" : userName) + " " + ((null == userSurname) ? "" : userSurname));
//			s = s.replaceAll("~~~DATA~~~", sdf.format(gvt.getGvtRegData()));

			s = s.replaceAll("~~~REPLACE_SAKINYS_1~~~", (gyventojai != null && gyventojai.size()>0)? "savo gyvenamàjà vietà deklaravo:":"nëra asmenø, deklaravusiø savo gyvenamàjà vietà.");
			Constants.Println(request, "st18");		
			
			String blockOneResidentTikVardas = "" +
					"<w:p wsp:rsidR=\"00AA75D0\" wsp:rsidRPr=\"00E3698F\" wsp:rsidRDefault=\"00144342\" wsp:rsidP=\"00144342\">" +
					"<w:pPr>" +
					"<w:tabs>" +					
					"<w:tab w:val=\"left\" w:pos=\"360\"/>" +
					"<w:tab w:val=\"left\" w:pos=\"3420\"/>" +
					"<w:tab w:val=\"left\" w:pos=\"5040\"/>" +
					"<w:tab w:val=\"left\" w:pos=\"6480\"/>" +
					"</w:tabs>" +
					"<w:ind w:right=\"-114\"/>" +
					"</w:pPr>" +
					"<w:r>" +
					"<w:tab wx:wTab=\"360\" wx:tlc=\"none\" wx:cTlc=\"7\"/>" +
					"</w:r>" +
					"<w:r wsp:rsidRPr=\"00350138\">" +
					"<w:rPr>" +
					"<w:noProof/>" +
					"</w:rPr>" +
					"<w:t>~~~VARPAV~~~</w:t></w:r>" +
					"</w:p>";
			
			String blockOneResident = "" +
				"<w:p wsp:rsidR=\"00AA75D0\" wsp:rsidRPr=\"00350138\" wsp:rsidRDefault=\"00234314\" wsp:rsidP=\"00AA75D0\">" +
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
				"<v:line id=\"_x0000_s1048\" style=\"position:absolute;flip:y;z-index:16\" from=\"252pt,10.9pt\" to=\"306pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r>" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:pict>" +
				"<v:line id=\"_x0000_s1047\" style=\"position:absolute;flip:y;z-index:15\" from=\"171pt,10.9pt\" to=\"225pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00AA75D0\">" +
				"<w:tab wx:wTab=\"360\" wx:tlc=\"none\" wx:cTlc=\"7\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00AA75D0\">" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:pict>" +
				"<v:line id=\"_x0000_s1045\" style=\"position:absolute;flip:y;z-index:14;mso-position-horizontal-relative:text;mso-position-vertical-relative:text\" from=\"0,10.9pt\" to=\"153pt,10.9pt\" /> " +
				"</w:pict>" +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00350138\" wsp:rsidRPr=\"00350138\">" +
				"<w:rPr>" +
				"<w:noProof /> " +
				"</w:rPr>" +
				"<w:t>~~~VARPAV~~~</w:t> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00AA75D0\">" +
				"<w:tab wx:wTab=\"1665\" wx:tlc=\"none\" wx:cTlc=\"36\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00350138\" wsp:rsidRPr=\"00350138\">" +
				"<w:t>~~~GD~~~</w:t> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00AA75D0\">" +
				"<w:tab wx:wTab=\"735\" wx:tlc=\"none\" wx:cTlc=\"15\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidR=\"00350138\" wsp:rsidRPr=\"00350138\">" +
				"<w:t>~~~DD~~~</w:t> " +
				"</w:r>" +
				"</w:p>" +
				"<w:p wsp:rsidR=\"00AA75D0\" wsp:rsidRPr=\"00E3698F\" wsp:rsidRDefault=\"00AA75D0\" wsp:rsidP=\"00AA75D0\">" +
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
				"<w:r wsp:rsidRPr=\"00E3698F\">" +
				"<w:t>(vardas (-ai) ir pavardë)</w:t> " +
				"</w:r>" +
				"<w:r wsp:rsidRPr=\"00E3698F\">" +
				"<w:tab wx:wTab=\"1200\" wx:tlc=\"none\" wx:cTlc=\"26\" /> " +
				"<w:t>(gimimo data)</w:t> " +
				"</w:r>" +
				"<w:r>" +
				"<w:tab wx:wTab=\"540\" wx:tlc=\"none\" wx:cTlc=\"11\" /> " +
				"</w:r>" +
				"<w:r wsp:rsidRPr=\"00E3698F\">" +
				"<w:t>(deklaravimo data)</w:t> " +
				"</w:r>" +
				"</w:p>";
			String blockAllResidents = "";
			Constants.Println(request, "st19");		
			if (gyventojai != null && gyventojai.iterator().hasNext()) {
				Iterator it = gyventojai.iterator();
				Constants.Println(request, "st20");		
				for (;it.hasNext();) {
					Constants.Println(request, "st21");		
					Asmuo gyventojas = (Asmuo)it.next();
					long asmNr = gyventojas.getAsmNr();
					gyventojas = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmNr);
					
					GyvenamojiVieta gyventojoGvt = (GyvenamojiVieta)HibernateUtils.currentSession(request).createQuery(
							"from GyvenamojiVieta gvt where " +
							"gvt.gvtAsmNr = :gvtAsmNr and " +
							"gvt.gvtAdvNr = :adrNr and " +
							"gvt.gvtAtvNr is null and " +
							"gvt.gvtTipas = 'R' and " +
							"((gvt.gvtDataNuo <= :data and nvl(gvt.gvtDataIki, sysdate) >= :data or :data is null and gvt.gvtDataIki is null)" + 
							  " or (gvt.gvtDataNuo is null and gvt.gvtRegData <=:data and nvl(gvt.gvtDataIki, sysdate) >= :data) )"
									)
							    .setParameter("gvtAsmNr",new Long(asmNr), Hibernate.LONG)
							    .setParameter("adrNr", gvt.getGvtAdvNr(), Hibernate.LONG)
								.setTimestamp("data", gvt.getGvtRegData())
								.setMaxResults(1)
								.uniqueResult();
					
					//GyvenamojiVieta gyventojoGvt = (GyvenamojiVieta)gyventojas.getGyvenamosiosVietos().iterator().next();
					if(((gyventojas.getVardas() != null)?gyventojas.getVardas().length():0) + ((gyventojas.getPavarde() != null)?gyventojas.getPavarde().length():0) > 24){
						Constants.Println(request, "st22");		
						blockAllResidents = blockAllResidents + blockOneResidentTikVardas;
						blockAllResidents = blockAllResidents.replaceAll("~~~VARPAV~~~", ((null == gyventojas.getVardas()) ? "" : gyventojas.getVardas()));
						blockAllResidents = blockAllResidents + blockOneResident;
						blockAllResidents = blockAllResidents.replaceAll("~~~VARPAV~~~", ((null == gyventojas.getPavarde()) ? "" : gyventojas.getPavarde()));
					}
					else{
						Constants.Println(request, "st23");		
						blockAllResidents = blockAllResidents + blockOneResident;
						blockAllResidents = blockAllResidents.replaceAll("~~~VARPAV~~~", ((null == gyventojas.getVardas()) ? "" : gyventojas.getVardas()) + " " + ((null == gyventojas.getPavarde()) ? "" : gyventojas.getPavarde()));
					}
					Constants.Println(request, "st24");		
					blockAllResidents = blockAllResidents.replaceAll("~~~GD~~~", (null == gyventojas.getAsmGimData()) ? "" : sdf.format(gyventojas.getAsmGimData()));
					if (gyventojoGvt.getGvtDataNuo()!= null){
						Constants.Println(request, "st25");		
						blockAllResidents = blockAllResidents.replaceAll("~~~DD~~~", sdf.format(gyventojoGvt.getGvtDataNuo()));
					}
					else blockAllResidents = blockAllResidents.replaceAll("~~~DD~~~", "");		
					Constants.Println(request, "st26");		
				}
			}
			Constants.Println(request, "st27");		
			s = s.replaceAll("<w:p wsp:rsidR=\"00351DE9\" wsp:rsidRDefault=\"00351DE9\" wsp:rsidP=\"004C10BC\"><w:pPr><w:ind w:right=\"-114\"/></w:pPr><w:r wsp:rsidRPr=\"00351DE9\"><w:t>~~~BLOCK_GYVENTOJAI~~~</w:t></w:r></w:p>", blockAllResidents);
//			s = s.replaceAll("<w:p.*(~~~BLOCK_GYVENTOJAI~~~).*</w:p>", blockAllResidents);

			Constants.Println(request, "st28");		
			response.setContentType("application/msword");
			Constants.Println(request, "st29");		
			OutputStream os = response.getOutputStream();
			Constants.Println(request, "st30");		
			os.write(s.getBytes("UTF-8"));
			os.close();
		}
		catch (IOException ioe){
			Constants.Println(request, "st31");		
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
			out.println("---------------------");
			out.println("Adresas:");
			for (Iterator it=flatAddress.keySet().iterator(); it.hasNext();){
			    String key = (String)it.next();
			    String value = (String)flatAddress.get(key);
				out.println(key + ": " + value);
			}
			out.println("---------------------");
			out.println("Adresas: " + stringAddress);
			out.println("Data: " + gvt.getGvtRegData());
			out.println("---------------------");
			if (gyventojai != null) for (Iterator it=gyventojai.iterator(); it.hasNext();){
				Asmuo gyventojas = (Asmuo)it.next();
				out.println(gyventojas.getVardas() + " " + gyventojas.getPavarde());
			}
*/