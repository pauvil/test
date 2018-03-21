package lt.solver.gvdis.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lt.solver.gvdis.dal.JournalDAL;
import lt.solver.gvdis.model.DeklaracijaJournalObj;
import lt.solver.gvdis.model.PazymaJournalObj;
import lt.solver.gvdis.model.SavPazymaJournalObj;
import lt.solver.gvdis.model.SprendimaiJournalObj;

import com.algoritmusistemos.gvdis.web.Constants;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

//Pasinaudota com.algoritmusistemos.gvdis.web.actions.journals.PDFGenerator
public class PdfPageGenerator  extends PdfPageEventHelper {

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
	
    public PdfPageGenerator(HttpServletRequest req) {
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
    
    public void generatePDF(int journalType, List results, OutputStream os)
		throws DocumentException, IOException {
    	
    	String istaiga = (String)request.getSession().getAttribute("userIstaiga");
    	String user = (String)request.getSession().getAttribute("userName") + " " + (String)request.getSession().getAttribute("userSurname");
    	String pareigos = "Vyr. specialistas"; // TODO: paimti tikras pareigas
    	
		Document d = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(d, os);
		writer.setPageEvent(this);
		d.open();
		
		// Þurnalo sudarytojo pavadinimas
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
		
		// Þurnalo pavadinimas
		String journalTitle = "";
		switch (journalType){
		case JournalDAL.JOURNAL_TYPE_IN:
			journalTitle = "GYVENAMOSIOS VIETOS DEKLARACIJØ, PILDOMØ ASMENIUI PAKEITUS GYVENAMÀJÀ VIETÀ LIETUVOS RESPUBLIKOJE AR ATVYKUS GYVENTI Á LIETUVOS RESPUBLIKÀ, REGISTRACIJOS ÞURNALAS";
			break;
		case JournalDAL.JOURNAL_TYPE_OUT:
			journalTitle = "GYVENAMOSIOS VIETOS DEKLARACIJØ, PILDOMØ ASMENIUI IÐVYKSTANT IÐ LIETUVOS RESPUBLIKOS, REGISTRACIJOS ÞURNALAS";
			break;
		case JournalDAL.JOURNAL_TYPE_GVNA:
			journalTitle = "PRAÐYMØ ÁTRAUKTI Á GYVENAMOSIOS VIETOS NEDEKLARAVUSIØ ASMENØ APSKAITÀ REGISTRACIJOS ÞURNALAS";
			break;
		case JournalDAL.JOURNAL_TYPE_GVPAZ:
			journalTitle = "PAÞYMØ, PATVIRTINANÈIØ ASMENS DEKLARUOTÀ GYVENAMÀJÀ VIETÀ, REGISTRACIJOS IR IÐDAVIMO ÞURNALAS";
			break;
		case JournalDAL.JOURNAL_TYPE_GVNAPAZ:
			journalTitle = "PAÞYMØ APIE ÁTRAUKIMÀ Á GYVENAMOSIOS VIETOS NEDEKLARAVUSIØ ASMENØ APSKAITÀ REGISTRACIJOS IR IÐDAVIMO ÞURNALAS";
			break;
		case JournalDAL.JOURNAL_TYPE_SPREND:
			journalTitle = "SPRENDIMØ DËL DEKLARAVIMO DUOMENØ TAISYMO, KEITIMO IR NAIKINIMO REGISTRACIJOS ÞURNALAS";
			break;
		case JournalDAL.JOURNAL_TYPE_SAVPAZ:
    		journalTitle = "PAÞYMØ IÐDUOTØ GYVENAMOSIOS PATALPOS SAVININKAMS REGISTRAS";
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
		
		// Lentelës antraðtë
		PdfPTable table;
		if (journalType == JournalDAL.JOURNAL_TYPE_OUT || journalType == JournalDAL.JOURNAL_TYPE_SPREND){
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
		case JournalDAL.JOURNAL_TYPE_IN:
			table.addCell(new Phrase("Deklaracijos gavimo registracijos data", fplain));
			table.addCell(new Phrase("Asmens, deklaruojanèio gyvenamàjà vietà, vardas (-ai) ir pavardë", fplain));
			break;
		case JournalDAL.JOURNAL_TYPE_OUT:
			table.addCell(new Phrase("Deklaracijos gavimo registracijos data", fplain));
			table.addCell(new Phrase("Asmens, deklaruojanèio gyvenamàjà vietà, vardas (-ai) ir pavardë", fplain));
			table.addCell(new Phrase("Numatoma gyvenamoji vieta uþsienyje", fplain));
			break;
		case JournalDAL.JOURNAL_TYPE_GVNA:
			table.addCell(new Phrase("Praðymo gavimo registracijos data ", fplain));
			table.addCell(new Phrase("Asmens, átraukto á gyvenamosios vietos nedeklaravusiø asmenø apskaità, vardas (-ai) ir pavardë", fplain));
			break;
		case JournalDAL.JOURNAL_TYPE_GVPAZ:
			table.addCell(new Phrase("Paþymos registracijos data", fplain));
			table.addCell(new Phrase("Asmens, deklaravusio gyvenamàjà vietà, vardas (-ai) ir pavardë", fplain));
			break;
		case JournalDAL.JOURNAL_TYPE_GVNAPAZ:
			table.addCell(new Phrase("Paþymos registracijos data", fplain));
			table.addCell(new Phrase("Asmens, átraukto á gyvenamosios vietos nedeklaravusiø asmenø apskaità, vardas (-ai) ir pavardë", fplain));
			break;
		case JournalDAL.JOURNAL_TYPE_SPREND:
			table.addCell(new Phrase("Sprendimo registracijos data", fplain));
			table.addCell(new Phrase("Sprendimo priëmimo data", fplain));
			table.addCell(new Phrase("Asmens, pateikusio praðymà iðtaisyti, pakeisti ar panaikinti deklaravimo duomenis, vardas (-ai) ir pavardë", fplain));
			break;
		case JournalDAL.JOURNAL_TYPE_SAVPAZ:
			table.addCell(new Phrase("Paþymos registracijos data", fplain));
			table.addCell(new Phrase("Adresas", fplain));
			break;
		}
		table.addCell(new Phrase("Pastabos", fplain));
		table.addCell(new Phrase("1", fplain));
		table.addCell(new Phrase("2", fplain));
		table.addCell(new Phrase("3", fplain));
		table.addCell(new Phrase("4", fplain));
		if (journalType == JournalDAL.JOURNAL_TYPE_OUT || journalType == JournalDAL.JOURNAL_TYPE_SPREND){
			table.addCell(new Phrase("5", fplain));
		}
		
		// Lentelës duomenys
		for (Iterator it=results.iterator(); it.hasNext();){
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			switch (journalType){
			case JournalDAL.JOURNAL_TYPE_IN:
			case JournalDAL.JOURNAL_TYPE_GVNA:
			case JournalDAL.JOURNAL_TYPE_OUT:
				DeklaracijaJournalObj dekl = (DeklaracijaJournalObj) it.next();
				table.addCell(new Phrase(dekl.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(dekl.getDeklaracijosData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + dekl.getAsmenvardis(), fplain));
				if (journalType == JournalDAL.JOURNAL_TYPE_OUT) { //ppildomas laukas
					table.addCell(new Phrase("  " + new String((dekl.getGyvSalisStr() != null)?dekl.getGyvSalisStr():""), fplain));
				}
				table.addCell(new Phrase("  " + dekl.getPastabos() != null ? dekl.getPastabos() : "", fplain));

				break;
			case JournalDAL.JOURNAL_TYPE_GVPAZ:
			case JournalDAL.JOURNAL_TYPE_GVNAPAZ:
				PazymaJournalObj pazyma = (PazymaJournalObj)it.next();
				table.addCell(new Phrase(pazyma.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(pazyma.getPazymosData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + pazyma.getAsmenvardis(), fplain));
				table.addCell(new Phrase("  " + pazyma.getPastabos() != null ? pazyma.getPastabos() : "", fplain));
				break;
			case JournalDAL.JOURNAL_TYPE_SPREND:
				SprendimaiJournalObj sprend = (SprendimaiJournalObj) it.next();
				table.addCell(new Phrase(sprend.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(sprend.getInsData()), fplain));
				table.addCell(new Phrase(dateFormat.format(sprend.getSprendimoData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase(sprend.getPrasytojas() != null ? sprend.getPrasytojas() : "", fplain));
				table.addCell(new Phrase("  " + sprend.getPastabos() != null ? sprend.getPastabos() : "", fplain));
				
				break;
			case JournalDAL.JOURNAL_TYPE_SAVPAZ:
				SavPazymaJournalObj savPazyma = (SavPazymaJournalObj)it.next();
				table.addCell(new Phrase(savPazyma.getRegNr(), fplain));
				table.addCell(new Phrase(dateFormat.format(savPazyma.getPazymosData()), fplain));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(new Phrase("  " + savPazyma.getAdresas(), fplain));
				table.addCell(new Phrase("  " + savPazyma.getPastabos() != null ? savPazyma.getPastabos() : "", fplain));

				break;
			}
		}
		d.add(table);
		
		// Vardas, pavardë ir pareigos
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
		paddingLen = (signatureLength - "(paraðas)".length()) / 2;
		parFooter.add(new Chunk(padding(paddingLen) + "(paraðas)" + padding(paddingLen)));
		parFooter.add(padding(spaceLength));
		paddingLen = (nameLength - "(vardas, pavardë)".length()) / 2;
		parFooter.add(new Chunk(padding(paddingLen) + "(vardas, pavardë)" + padding(paddingLen)));
		d.add(parFooter);
		
		d.close();
		os.close();
    }
}
