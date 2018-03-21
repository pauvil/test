package com.algoritmusistemos.gvdis.web.actions.journals;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.JournalForm;
import com.algoritmusistemos.gvdis.web.persistence.*;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/**
 * Pagalbin� klas� �urnal� generavimui � PDF'us
 * @author a.stonkus
 */
class PDFGenerator extends PdfPageEventHelper
{
	static BaseFont times_roman;
	static BaseFont times_italic;
	static {
		try {
			times_roman = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.CP1257,BaseFont.EMBEDDED);
			times_italic = BaseFont.createFont(BaseFont.TIMES_ITALIC,BaseFont.CP1257,BaseFont.EMBEDDED);
		}
		catch(Exception e){e.printStackTrace(); /* I.N. 2010.01.18 */}
	}
	static Font fplain = new Font(times_roman, 10);
	static Font fbold = new Font(times_roman, 11, Font.BOLD);
	
	static final int titleLength = 110;
	static final int positionLength = 35;
	static final int signatureLength = 35;
	static final int nameLength = 48;
	static final int spaceLength = 10;

	public PdfGState gstate;
    public PdfTemplate tpl;
    private HttpServletRequest request;
    private SimpleDateFormat dateFormat;
    
    /**
     * Konstruktorius
     */
    public PDFGenerator(HttpServletRequest req)
    {
    	this.request = req;
    	dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
    }
    
    private String padding(int howMany)
    {
    	StringBuffer retVal = new StringBuffer();
    	for (int i=0; i<howMany; i++){
    		retVal.append(" ");
    	}
    	return retVal.toString();
    }
    
    /**
     * Pagrindinis PDF generavimo metodas
     * @param journalType
     * @param results
     * @param os
     */
    public void generatePDF(int journalType, List results, OutputStream os)
    	throws DocumentException, IOException
    {
    	String istaiga = (String)request.getSession().getAttribute("userIstaiga");
    	String user = (String)request.getSession().getAttribute("userName") + " " + (String)request.getSession().getAttribute("userSurname");
    	String pareigos = "Vyr. specialistas"; // TODO: paimti tikras pareigas
    	
		Document d = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(d, os);
		writer.setPageEvent(this);
		d.open();
		
		// �urnalo sudarytojo pavadinimas
		Paragraph parIstaiga = new Paragraph("", fplain);
		parIstaiga.setAlignment(Paragraph.ALIGN_CENTER);
		int paddingLen = (titleLength - istaiga.length()) / 2;
		Chunk underlined = new Chunk(padding(paddingLen) + istaiga + padding(paddingLen));
		underlined.setUnderline(0.2f, -2f);
		parIstaiga.add(underlined);
		parIstaiga.add(Chunk.NEWLINE);
		parIstaiga.add(Chunk.NEWLINE);
		parIstaiga.add(Chunk.NEWLINE);
		d.add(parIstaiga);
		
		// �urnalo pavadinimas
		String journalTitle = "";
		switch (journalType){
		case JournalDelegator.JOURNAL_TYPE_IN:
			journalTitle = "GYVENAMOSIOS VIETOS DEKLARACIJ�, PILDOM� ASMENIUI PAKEITUS GYVENAM�J� VIET� LIETUVOS RESPUBLIKOJE AR ATVYKUS GYVENTI � LIETUVOS RESPUBLIK�, REGISTRACIJOS �URNALAS";
			break;
		case JournalDelegator.JOURNAL_TYPE_OUT:
			journalTitle = "GYVENAMOSIOS VIETOS DEKLARACIJ�, PILDOM� ASMENIUI I�VYKSTANT I� LIETUVOS RESPUBLIKOS, REGISTRACIJOS �URNALAS";
			break;
		case JournalDelegator.JOURNAL_TYPE_GVNA:
			journalTitle = "PRA�YM� �TRAUKTI � GYVENAMOSIOS VIETOS NEDEKLARAVUSI� ASMEN� APSKAIT� REGISTRACIJOS �URNALAS";
			break;
		case JournalDelegator.JOURNAL_TYPE_GVPAZ:
			journalTitle = "PA�YM�, PATVIRTINAN�I� ASMENS DEKLARUOT� GYVENAM�J� VIET�, REGISTRACIJOS IR I�DAVIMO �URNALAS";
			break;
		case JournalDelegator.JOURNAL_TYPE_GVNAPAZ:
			journalTitle = "PA�YM� APIE �TRAUKIM� � GYVENAMOSIOS VIETOS NEDEKLARAVUSI� ASMEN� APSKAIT� REGISTRACIJOS IR I�DAVIMO �URNALAS";
			break;
		case JournalDelegator.JOURNAL_TYPE_SPREND:
			journalTitle = "SPRENDIM� D�L DEKLARAVIMO DUOMEN� TAISYMO, KEITIMO IR NAIKINIMO REGISTRACIJOS �URNALAS";
			break;
		case JournalDelegator.JOURNAL_TYPE_SAVPAZ:
    		journalTitle = "PA�YM� I�DUOT� GYVENAMOSIOS PATALPOS SAVININKAMS REGISTRAS";
    		break;
		}
		Paragraph parTitle = new Paragraph(journalTitle, fbold);
		parTitle.setAlignment(Paragraph.ALIGN_CENTER);
		parTitle.setIndentationLeft(120);
		parTitle.setIndentationRight(120);
		parTitle.add(Chunk.NEWLINE);
		parTitle.add(Chunk.NEWLINE);
		parTitle.add(Chunk.NEWLINE);
		d.add(parTitle);
		
		// Lentel�s antra�t�
		PdfPTable table;
		if (journalType == JournalDelegator.JOURNAL_TYPE_OUT || journalType == JournalDelegator.JOURNAL_TYPE_SPREND){
			table = new PdfPTable(5); 
			float[] widths = {0.15f, 0.15f, 0.20f, 0.20f, 0.30f};
			table.setWidths(widths);
		}
		else {
			table = new PdfPTable(4);
			float[] widths = {0.17f, 0.15f, 0.28f, 0.40f};
			table.setWidths(widths);
		}
		
		table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(new Phrase("Reg. Nr.", fplain));
		switch (journalType){
		case JournalDelegator.JOURNAL_TYPE_IN:
			table.addCell(new Phrase("Deklaracijos gavimo registracijos data", fplain));
			table.addCell(new Phrase("Asmens, deklaruojan�io gyvenam�j� viet�, vardas (-ai) ir pavard�", fplain));
			break;
		case JournalDelegator.JOURNAL_TYPE_OUT:
			table.addCell(new Phrase("Deklaracijos gavimo registracijos data", fplain));
			table.addCell(new Phrase("Asmens, deklaruojan�io gyvenam�j� viet�, vardas (-ai) ir pavard�", fplain));
			table.addCell(new Phrase("Numatoma gyvenamoji vieta u�sienyje", fplain));
			break;
		case JournalDelegator.JOURNAL_TYPE_GVNA:
			table.addCell(new Phrase("Pra�ymo gavimo registracijos data ", fplain));
			table.addCell(new Phrase("Asmens, �traukto � gyvenamosios vietos nedeklaravusi� asmen� apskait�, vardas (-ai) ir pavard�", fplain));
			break;
		case JournalDelegator.JOURNAL_TYPE_GVPAZ:
			table.addCell(new Phrase("Pa�ymos registracijos data", fplain));
			table.addCell(new Phrase("Asmens, deklaravusio gyvenam�j� viet�, vardas (-ai) ir pavard�", fplain));
			break;
		case JournalDelegator.JOURNAL_TYPE_GVNAPAZ:
			table.addCell(new Phrase("Pa�ymos registracijos data", fplain));
			table.addCell(new Phrase("Asmens, �traukto � gyvenamosios vietos nedeklaravusi� asmen� apskait�, vardas (-ai) ir pavard�", fplain));
			break;
		case JournalDelegator.JOURNAL_TYPE_SPREND:
			table.addCell(new Phrase("Sprendimo registracijos data", fplain));
			table.addCell(new Phrase("Sprendimo pri�mimo data", fplain));
			table.addCell(new Phrase("Asmens, pateikusio pra�ym� i�taisyti, pakeisti ar panaikinti deklaravimo duomenis, vardas (-ai) ir pavard�", fplain));
			break;
		case JournalDelegator.JOURNAL_TYPE_SAVPAZ:
			table.addCell(new Phrase("Pa�ymos registracijos data", fplain));
			table.addCell(new Phrase("Adresas", fplain));
			break;
		}
		table.addCell(new Phrase("Pastabos", fplain));
		table.addCell(new Phrase("1", fplain));
		table.addCell(new Phrase("2", fplain));
		table.addCell(new Phrase("3", fplain));
		table.addCell(new Phrase("4", fplain));
		if (journalType == JournalDelegator.JOURNAL_TYPE_OUT || journalType == JournalDelegator.JOURNAL_TYPE_SPREND){
			table.addCell(new Phrase("5", fplain));
		}
		
		// Lentel�s duomenys
		for (Iterator it=results.iterator(); it.hasNext();){
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			switch (journalType){
			case JournalDelegator.JOURNAL_TYPE_IN:
			case JournalDelegator.JOURNAL_TYPE_GVNA:
				Deklaracija dekl = (Deklaracija)it.next();
				table.addCell(new Phrase(dekl.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(dekl.getDeklaravimoData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + dekl.getCalcAsmuo(), fplain));
				table.addCell(new Phrase("  " + dekl.getPastabos() != null ? dekl.getPastabos() : "", fplain));
				break;
			case JournalDelegator.JOURNAL_TYPE_OUT:
				IsvykimoDeklaracija isvdekl = (IsvykimoDeklaracija)it.next();
				table.addCell(new Phrase(isvdekl.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(isvdekl.getDeklaravimoData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + isvdekl.getCalcAsmuo(), fplain));
				table.addCell(new Phrase("  " + new String((isvdekl.getGyvenamojiVieta() != null)?isvdekl.getGyvenamojiVieta().getValstybe().getPavadinimas():""), fplain));
				table.addCell(new Phrase("  " + isvdekl.getPastabos() != null ? isvdekl.getPastabos() : "", fplain));
				break;
			case JournalDelegator.JOURNAL_TYPE_GVPAZ:
				GvPazyma gvPazyma = (GvPazyma)it.next();
				table.addCell(new Phrase(gvPazyma.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(gvPazyma.getPazymosData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + gvPazyma.getCalcAsmuo(), fplain));
				table.addCell(new Phrase("  " + gvPazyma.getPastabos() != null ? gvPazyma.getPastabos() : "", fplain));
				break;
			case JournalDelegator.JOURNAL_TYPE_GVNAPAZ:
				GvnaPazyma gvnaPazyma = (GvnaPazyma)it.next();
				table.addCell(new Phrase(gvnaPazyma.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(gvnaPazyma.getPazymosData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + gvnaPazyma.getCalcAsmuo(), fplain));
				table.addCell(new Phrase("  " + gvnaPazyma.getPastabos() != null ? gvnaPazyma.getPastabos() : "", fplain));
				break;
			case JournalDelegator.JOURNAL_TYPE_SPREND:
				SprendimasKeistiDuomenis sprend = (SprendimasKeistiDuomenis)it.next();
				table.addCell(new Phrase(sprend.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(sprend.getInsDate()), fplain));
				table.addCell(new Phrase(dateFormat.format(sprend.getData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase(sprend.getPrasymas() != null ? sprend.getPrasymas().getPrasytojas() : "", fplain));
				table.addCell(new Phrase("  " + sprend.getPastabos() != null ? sprend.getPastabos() : "", fplain));
				break;
			case JournalDelegator.JOURNAL_TYPE_SAVPAZ:
				SavPazyma savPazyma = (SavPazyma)it.next();
				table.addCell(new Phrase(savPazyma.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(savPazyma.getPazymosData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + savPazyma.getCalcAdresas(request), fplain));
				table.addCell(new Phrase("  " + savPazyma.getPastabos() != null ? savPazyma.getPastabos() : "", fplain));
				break;
			}
		}
		d.add(table);
		
		// Vardas, pavard� ir pareigos
		Paragraph parFooter = new Paragraph("", fplain);
		parFooter.setIndentationLeft(50);
		parFooter.add(Chunk.NEWLINE);
		parFooter.add(Chunk.NEWLINE);
		parFooter.add(Chunk.NEWLINE);
		
		paddingLen = (positionLength - pareigos.length()) / 2;
		underlined = new Chunk(padding(paddingLen) + pareigos + padding(paddingLen));
		underlined.setUnderline(0.2f, -2f);
		parFooter.add(underlined);
		parFooter.add(padding(spaceLength));
		
		underlined = new Chunk(padding(signatureLength));
		underlined.setUnderline(0.2f, -2f);
		parFooter.add(underlined);
		parFooter.add(padding(spaceLength));

		paddingLen = (nameLength - user.length()) / 2;
		underlined = new Chunk(padding(paddingLen) + user + padding(paddingLen));
		underlined.setUnderline(0.2f, -2f);
		parFooter.add(underlined);
		parFooter.add(Chunk.NEWLINE);

		paddingLen = (positionLength - "(pareigos)".length()) / 2;
		parFooter.add(new Chunk(padding(paddingLen) + "(pareigos)" + padding(paddingLen)));
		parFooter.add(padding(spaceLength));
		paddingLen = (signatureLength - "(para�as)".length()) / 2;
		parFooter.add(new Chunk(padding(paddingLen) + "(para�as)" + padding(paddingLen)));
		parFooter.add(padding(spaceLength));
		paddingLen = (nameLength - "(vardas, pavard�)".length()) / 2;
		parFooter.add(new Chunk(padding(paddingLen) + "(vardas, pavard�)" + padding(paddingLen)));
		d.add(parFooter);
		
		d.close();
		os.close();
    }
}

/**
 * �urnal� atvaizdavimo action'as 
 * @author a.stonkus
 */
public class JournalAction extends Action
{
    public JournalAction()
    {
    }
    
    private void outputPdf(HttpServletRequest req, HttpServletResponse resp, int journalType, List results)
    {
		resp.setContentType("application/pdf");
		PDFGenerator gen = new PDFGenerator(req);
		try {
			gen.generatePDF(journalType, results, resp.getOutputStream());
		}
		catch (Exception e){
			e.printStackTrace();
		}
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws DatabaseException, ObjectNotFoundException, InternalException
    {

        HttpSession session = request.getSession();
        session.setAttribute("CENTER_STATE", Constants.JOURNAL);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_MANAGE_AND_PRINT_JUORNALS);
       
        // Nustatome �urnalo tip�
	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
        int journalType;
        try {
        	journalType = Integer.parseInt(request.getParameter("journalType"));
        }
        catch (Exception e){
        	journalType = JournalDelegator.JOURNAL_TYPE_IN;
        }
        request.setAttribute("journalType", new Integer(journalType));
        
        // Puslapio antra�t�
        switch (journalType){
        	case JournalDelegator.JOURNAL_TYPE_IN:
        		request.setAttribute("journalTitle", "Atvykimo deklaracij� �urnalas");
                session.setAttribute("menu_cell","journal0");
        		break;
        	case JournalDelegator.JOURNAL_TYPE_OUT:
        		request.setAttribute("journalTitle", "I�vykimo deklaracij� �urnalas");
                session.setAttribute("menu_cell","journal1");        		
        		break;
        	case JournalDelegator.JOURNAL_TYPE_GVNA:
        		request.setAttribute("journalTitle", "Pra�ym� �traukti � GVNA apskait� �urnalas");
                session.setAttribute("menu_cell","journal4");        		
        		break;
        	case JournalDelegator.JOURNAL_TYPE_GVPAZ:
        		request.setAttribute("journalTitle", "I�duot� pa�ym� apie deklaruot� GV �urnalas");
                session.setAttribute("menu_cell","journal2");        		
        		break;
        	case JournalDelegator.JOURNAL_TYPE_GVNAPAZ:
        		request.setAttribute("journalTitle", "I�duot� pa�ym� apie �traukim� � GVNA apskait� �urnalas");
                session.setAttribute("menu_cell","journal5");        		
        		break;
        	case JournalDelegator.JOURNAL_TYPE_SPREND:
        		request.setAttribute("journalTitle", "Sprendim� d�l deklaravimo duomen� keitimo �urnalas");
                session.setAttribute("menu_cell","journal3");        		
        		break;
        	case JournalDelegator.JOURNAL_TYPE_SAVPAZ:
        		request.setAttribute("journalTitle", "Pa�ym� i�duot� gyvenamosios patalpos savininkams registras");
                session.setAttribute("menu_cell","journal6");        		
        		break;
        }
        
        // R��iavimas, puslapiavimas
        Paging paging = new Paging();
        Ordering ordering = (Ordering)session.getAttribute("journals_ordering");
        if (ordering == null){
            ordering = new Ordering();
        }
        
/*
	<% if (journalType == JournalDelegator.JOURNAL_TYPE_SAVPAZ || journalType == JournalDelegator.JOURNAL_TYPE_GVNAPAZ || journalType == JournalDelegator.JOURNAL_TYPE_GVPAZ){ %>
	    <th width="10%"><%= ordering.printOrdering("pazymos_data, id", "Data") %></th>
    <% } else if ( 1 == 1 ) { %>
	    <th width="10%"><%= ordering.printOrdering("deklaravimo_data, id", "Data") %></th>
    <% } else { %> <!-- journalType == JournalDelegator.JOURNAL_TYPE_SPREND -->
	    <th width="10%"><%= ordering.printOrdering("data", "Data") %></th>
    <% } %>

 
 */ 
        //System.out.println("order name: "+ordering.getName());
        if (session.getAttribute("menu_cell").equals("journal6" ) || session.getAttribute("menu_cell").equals("journal5" ) || session.getAttribute("menu_cell").equals("journal2" ) ) {
            if (ordering.getColumn() == null || ordering.getColumn().equals("deklaravimo_data, id") || ordering.getColumn().equals("data, id"))
            	ordering.setColumn("pazymos_data, id"); // Pagal nutyl�jim�
        } else if ( session.getAttribute("menu_cell").equals("journal4") || session.getAttribute("menu_cell").equals("journal0") || session.getAttribute("menu_cell").equals("journal1") ) {
            if (ordering.getColumn() == null || ordering.getColumn().equals("pazymos_data, id") || ordering.getColumn().equals("data, id"))
            	ordering.setColumn("deklaravimo_data, id"); // Pagal nutyl�jim�
        } else { // session.getAttribute("menu_cell").equals("journal3" )
            if (ordering.getColumn() == null || ordering.getColumn().equals("deklaravimo_data, id") || ordering.getColumn().equals("pazymos_data, id"))
            	ordering.setColumn("data, id"); // Pagal nutyl�jim�
        }
        
        
        
        
        paging.init(request);
        ordering.init(request);
        session.setAttribute("journals_paging", paging);
        session.setAttribute("journals_ordering", ordering);
        if ("pdf".equals(request.getParameter("output"))){	// Spausdindami �urnal� nepuslapiuojame 
        	paging.setPage(0);
        	paging.setPageSize(Integer.MAX_VALUE);
        }
        
        // Rezultat� s�ra�as
    	Date dataNuo = null;
    	Date dataIki = null;
    	int busena;
    	Istaiga savivaldybe = null, seniunija = null;
        List results = null;
        JournalForm jForm = (JournalForm)form;
        
        session.setAttribute("rjSavivaldybe",jForm.getSavivaldybe());
        session.setAttribute("rjDataNuo",jForm.getDataNuo());
        session.setAttribute("rjDataIki",jForm.getDataIki());      
        session.setAttribute("rjSeniunija",jForm.getSeniunija());
       
        //try {
        	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        	sdf.setLenient(false);
        	try {
        	dataNuo = sdf.parse(jForm.getDataNuo());
        	dataIki = sdf.parse(jForm.getDataIki());
        	}
        	catch (ParseException ignore) {}
        	busena = Integer.parseInt(jForm.getBusena());
        	Long savId = null, senId = null;
    	    try { 
    	    	switch (userStatus){
    	    	case UserDelegator.USER_GLOBAL: 
    	    		savId = Long.valueOf(jForm.getSavivaldybe());
    	    		break;
    	    	case UserDelegator.USER_SAV:
    	    		savId = (Long)session.getAttribute("userIstaigaId");
    	    		break;
    	    	case UserDelegator.USER_UZS:
    	    		savId = (Long)session.getAttribute("userIstaigaId");
    	    		break;
    	    	default: savId = null;
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
    	    
    	    try {
        		savivaldybe = JournalDelegator.getInstance().getIstaiga(request, savId.longValue());
        		try {
            		seniunija = JournalDelegator.getInstance().getIstaiga(request, senId.longValue());
        		}
        		catch (Exception e1){
        			//e1.printStackTrace();
        			seniunija = null;
        		}
        	}
        	catch (Exception e2){
        		//e2.printStackTrace();
        		savivaldybe = null;
        	}
        	
            // U�krauname savivaldybi� ir seni�nij� s�ra��
            request.setAttribute("journalSavivaldybes", JournalDelegator.getInstance().getSavivaldybes(request));
            List seniunijos = new ArrayList();
            try {
            	if(savivaldybe != null)
            		seniunijos = JournalDelegator.getInstance().getSeniunijos(request, savivaldybe);
            }
            catch (Exception ex){
            	throw new InternalException(ex);
            }
            request.setAttribute("journalSeniunijos", seniunijos);
        	System.out.println("Oi oi oi......................");
    	    if (request.getAttribute("journalErrors") == null){ 
	        	switch (journalType){
	    			case JournalDelegator.JOURNAL_TYPE_IN:
	    				// jornal.jcp perduoda result kaip journalResults 
	    				// result formuoja getInDeklaracijos
	    				results = JournalDelegator.getInstance().getInDeklaracijos(request, paging, 
	    						ordering, dataNuo, dataIki, busena, savivaldybe, seniunija,jForm.getDeklaracijaType());
	    				
	    				if(null == results)request.setAttribute("not_found_type","Nerasta nei viena atvykimo deklaracija");
	    				break;
	    			case JournalDelegator.JOURNAL_TYPE_OUT:
	    				results = JournalDelegator.getInstance().getOutDeklaracijos(request, paging, 
	    						ordering, dataNuo, dataIki, busena, savivaldybe, seniunija,jForm.getDeklaracijaType());
	    				if(null == results)request.setAttribute("not_found_type","Nerasta nei viena i�vykimo deklaracija");    				
	    				break;
	    			case JournalDelegator.JOURNAL_TYPE_GVNA:
	    				results = JournalDelegator.getInstance().getGvnaDeklaracijos(request, paging, 
	    						ordering, dataNuo, dataIki, busena, savivaldybe, seniunija,jForm.getDeklaracijaType());
	    				if(null == results)request.setAttribute("not_found_type","Nerastas nei vienas pra�ymas");    				
	    				break;
	    			case JournalDelegator.JOURNAL_TYPE_GVPAZ:
	    				results = JournalDelegator.getInstance().getGvPazymos(request, paging, 
	    						ordering, dataNuo, dataIki, savivaldybe, seniunija);
	    				if(null == results)request.setAttribute("not_found_type","Nerasta nei viena pa�yma");  				
	    				break;
	    			case JournalDelegator.JOURNAL_TYPE_GVNAPAZ:
	    				results = JournalDelegator.getInstance().getGvnaPazymos(request, paging, 
	    						ordering, dataNuo, dataIki, savivaldybe, seniunija);
	    				if(null == results)request.setAttribute("not_found_type","Nerasta nei viena pa�yma");    				
	    				break;
	    			case JournalDelegator.JOURNAL_TYPE_SPREND:
	    				results = JournalDelegator.getInstance().getSprendimai(request, paging, 
	    						ordering, dataNuo, dataIki, busena, savivaldybe, seniunija);
	    				if(null == results)request.setAttribute("not_found_type","Nerastas nei vienas sprendimas");    				
	    				break;
	    			case JournalDelegator.JOURNAL_TYPE_SAVPAZ:
	    				results = JournalDelegator.getInstance().getSavPazymos(request, paging, 
	    						ordering, dataNuo, dataIki, savivaldybe, seniunija);
	    				if(null == results)request.setAttribute("not_found_type","Nerasta nei viena pa�yma");    				
	    				break;
	        	}
	        	request.setAttribute("journalResults", results);
    	    }
    	    else request.setAttribute("not_found_type","Nerastas nei vienas sprendimas");
        /*}
          catch (Exception ex){       	
        	ex.printStackTrace();
        }*/
        
        if ("pdf".equals(request.getParameter("output"))){
        	outputPdf(request, response, journalType, results);
        	return null;
        }
        else {
            return mapping.findForward("continue");
        }
    }
}
