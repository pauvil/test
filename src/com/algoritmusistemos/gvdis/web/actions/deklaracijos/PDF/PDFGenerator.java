package com.algoritmusistemos.gvdis.web.actions.deklaracijos.PDF;


import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.persistence.AtvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.IsvykimoDeklaracija;
import com.algoritmusistemos.gvdis.web.utils.CalendarUtils;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator 
{
	public PDFGenerator(Deklaracija deklaracija,HttpServlet servlet,HttpServletRequest request)
	{														  
		this.deklaracija = deklaracija;
		if(deklaracija instanceof AtvykimoDeklaracija)
		{
			atvykimoDeklaracija = (AtvykimoDeklaracija)deklaracija;
			declarationType = Constants.IN_DECLARATION_FORM;
		}
		else
		if(deklaracija instanceof IsvykimoDeklaracija)
		{
			isvykimoDeklaracija = (IsvykimoDeklaracija)deklaracija;
			declarationType = Constants.OUT_DECLARATION_FORM;
		}
		else
		if(deklaracija instanceof GvnaDeklaracija)
		{
			gvnaDeklaracija = (GvnaDeklaracija)deklaracija;
			declarationType = Constants.GVNA_DECLARATION_FORM;
		}
			
		
		this.session = request.getSession();
		this.servlet = servlet;
	}
	static BaseFont arial;
	static Font arial12;
	static Font arial10;
	static Font arial7;
	static Font arial9;	//ju.k 2007.06.26
	static Font arial8;	
	static Font arial6;	
	static String separator = System.getProperty("file.separator");
	static{
		try{
		arial =
			  BaseFont.createFont(
				BaseFont.HELVETICA,
			    BaseFont.CP1257,
			    BaseFont.NOT_EMBEDDED);	
		arial12 = new Font(arial,12);
		arial10 = new Font(arial,10);
		arial7 = new Font(arial,7);
		arial9 = new Font(arial,9);
		arial8 = new Font(arial,8);
		arial6 = new Font(arial,6);
		arial10.setStyle(Font.BOLD);
		
		}catch(DocumentException de){de.printStackTrace();}
		catch(IOException ioe){ioe.printStackTrace();}
		}	
	

	static float GRAY_FILL = 0.7f;
	static int WIDTH = 25;
	static Color DATA_COLOR = new Color(255,0,0);
	
	HttpServlet servlet = null;
	HttpSession session = null;	
	Deklaracija deklaracija = null;
	AtvykimoDeklaracija atvykimoDeklaracija = null;
	IsvykimoDeklaracija isvykimoDeklaracija = null;
	GvnaDeklaracija gvnaDeklaracija = null;
	PdfWriter writer = null;
	
	static String declarationType = null; 
	
	
    public void generateDeklaracijaPDF(HttpServletResponse resp)
    {
		resp.setContentType("application/pdf");
		try {
			generatePDF(resp.getOutputStream());
		}
		catch (Exception e){
			e.printStackTrace();
		}
    }
	
	private void generatePDF(OutputStream os)
	{
		try{
			Document d = new Document(PageSize.A4, 10, 10, 10, 10);
			writer = PdfWriter.getInstance(d, os);
			d.open();
			generateTitle(d);
			generateSpace(d);
			generateFirstPart(d);		
			generateSecondPart(d);
			fixFirstPageTables();			
			generateThirdPart(d);
			fixThirdPageTables();			
			generateFourthPart(d);
			if(Constants.IN_DECLARATION_FORM.equals(declarationType))
			{
				generateSpace(d);		
				generateFifthPart(d);
				generateSpace(d);		
				generateSixthPart(d);
				generateSpace(d);		
				generateSeventhPart(d);
				generateSpace(d);
				generateEithPart(d);
				generatePildo3(118,550);
			}
			d.close();	
		}catch(Exception e){e.printStackTrace();}
		
	}
	public void fixFirstPageTables()
	{
	    PdfContentByte cb = writer.getDirectContent();
	    cb.setLineWidth(2f);
	    
        PdfTemplate template = cb.createTemplate(500, 200);
        template.beginText();
        template.setFontAndSize(arial,9);
        template.setTextMatrix(100, 100);
        template.showText("4-10 eilutës pildomos didþiosiomis raidëmis");
        template.endText();
        cb.addTemplate(template, 0, 1, -1, 0, 120, 260);
        

        
	    if(Constants.IN_DECLARATION_FORM.equals(declarationType))
		{
	        cb.addTemplate(genNuoroda("1"), 1, 0, 0, 1, 150, 463);
	        cb.addTemplate(genNuoroda("2"), 1, 0, 0, 1, 204, 463);
	        cb.addTemplate(genNuoroda("3"), 1, 0, 0, 1, 258, 463);        
	        cb.addTemplate(genNuoroda("4"), 1, 0, 0, 1, 312, 463);        
	        cb.addTemplate(genNuoroda("5"), 1, 0, 0, 1, 366, 463);
	        cb.addTemplate(genNuoroda("6"), 1, 0, 0, 1, 420, 463); 
		    cb.rectangle(27,125,558,569);				
		}
		else
		if(Constants.OUT_DECLARATION_FORM.equals(declarationType))
		{
	        cb.addTemplate(genNuoroda("1"), 1, 0, 0, 1, 150, 463);
	        cb.addTemplate(genNuoroda("2"), 1, 0, 0, 1, 204, 463);
	        cb.addTemplate(genNuoroda("3"), 1, 0, 0, 1, 258, 463);        
	        cb.addTemplate(genNuoroda("4"), 1, 0, 0, 1, 312, 463);        
	        cb.addTemplate(genNuoroda("5"), 1, 0, 0, 1, 366, 463);
	        cb.addTemplate(genNuoroda("6"), 1, 0, 0, 1, 420, 463); 
			cb.rectangle(27,194,558,500);
		}
		else
		if(Constants.GVNA_DECLARATION_FORM.equals(declarationType))
		{
	        cb.addTemplate(genNuoroda("1"), 1, 0, 0, 1, 193, 497);
	        cb.addTemplate(genNuoroda("2"), 1, 0, 0, 1, 238, 497);
	        cb.addTemplate(genNuoroda("3"), 1, 0, 0, 1, 286, 497);        
	        cb.addTemplate(genNuoroda("4"), 1, 0, 0, 1, 332, 497);        
	        cb.addTemplate(genNuoroda("5"), 1, 0, 0, 1, 378, 497);
	        cb.addTemplate(genNuoroda("6"), 1, 0, 0, 1, 426, 497);			
		    cb.rectangle(27,231,558,471);				
		}
	    cb.stroke();
	    cb.closePathFillStroke();
	}
	public PdfTemplate genNuoroda(String text)
	{
		PdfContentByte cb = writer.getDirectContent();		
        PdfTemplate t1 = cb.createTemplate(500, 200);
        t1.beginText();
        t1.setFontAndSize(arial,5);
        t1.setTextMatrix(100, 100);
        t1.showText(text);
        t1.endText();
        return t1;
	}	
	public void generatePildo2(int x,int y)
	{
	    PdfContentByte cb = writer.getDirectContent();
	    
        PdfTemplate template1 = cb.createTemplate(200, 150);
        template1.beginText();
        template1.setFontAndSize(arial,9);
        template1.setTextMatrix(100, 100);
        template1.showText("Pildo ágaliotas");
        template1.endText();
        
        PdfTemplate template2 = cb.createTemplate(200, 150);
        template2.beginText();
        template2.setFontAndSize(arial,9);
        template2.setTextMatrix(100, 100);
        template2.showText("deklaravimo");
        template2.endText();
        
        PdfTemplate template3 = cb.createTemplate(200, 150);
        template3.beginText();
        template3.setFontAndSize(arial,9);
        template3.setTextMatrix(100, 100);
        template3.showText("ástaigos valstybës");
        template3.endText();
        
        PdfTemplate template4 = cb.createTemplate(200, 150);
        template4.beginText();
        template4.setFontAndSize(arial,9);
        template4.setTextMatrix(100, 100);
        template4.showText("tarnautojas ar");
        template4.endText();
        
        PdfTemplate template5 = cb.createTemplate(200, 150);
        template5.beginText();
        template5.setFontAndSize(arial,9);
        template5.setTextMatrix(100, 100);
        template5.showText("darbuotojas");
        template5.endText();
        
        
        cb.addTemplate(template1, 0, 1, -1, 0, x, y+5);
        cb.addTemplate(template2, 0, 1, -1, 0, x+10, y+10);
        cb.addTemplate(template3, 0, 1, -1, 0, x+20, y);
        cb.addTemplate(template4, 0, 1, -1, 0, x+30, y+5);
        cb.addTemplate(template5, 0, 1, -1, 0, x+40, y+10);
	}
	public void generatePildo3(int x,int y)
	{
	    PdfContentByte cb = writer.getDirectContent();
	    
        PdfTemplate template1 = cb.createTemplate(300, 150);
        template1.beginText();
        template1.setFontAndSize(arial,7);
        template1.setTextMatrix(100, 100);
        template1.showText("Pildo gyvenamosios patalpos");
        template1.endText();
        
        PdfTemplate template2 = cb.createTemplate(300, 150);
        template2.beginText();
        template2.setFontAndSize(arial,7);
        template2.setTextMatrix(100, 100);
        template2.showText("savininkas ar jo ágaliotas asmuo.");
        template2.endText();
        
        PdfTemplate template3 = cb.createTemplate(300, 150);
        template3.beginText();
        template3.setFontAndSize(arial,7);
        template3.setTextMatrix(100, 100);
        template3.showText("Pildoma didþiosiomis raidëmis.");
        template3.endText();
        
        
        
        cb.addTemplate(template1, 0, 1, -1, 0, x, y+5);
        cb.addTemplate(template2, 0, 1, -1, 0, x+8, y);
        cb.addTemplate(template3, 0, 1, -1, 0, x+16, y+5);

	}	
	public void fixThirdPageTables()
	{
		if(Constants.IN_DECLARATION_FORM.equals(declarationType))generatePildo2(130,-80);
		if(Constants.OUT_DECLARATION_FORM.equals(declarationType))generatePildo2(130,-10);
		if(Constants.GVNA_DECLARATION_FORM.equals(declarationType))generatePildo2(130,26);
	}
	public void generateSpace(Document d)
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		PdfPCell c = new PdfPCell(new Paragraph(" ",arial7));
		c.setFixedHeight(20);
		c.setBorderWidth(0);
		table.addCell(c);
		d.add(table);
	}
	public void generateTitle(Document d)
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{75,25});
		PdfPCell cell1 = new PdfPCell(new Paragraph("",arial10));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell1.setBorderWidth(0);
		table.addCell(cell1);
		String str1 = "";
		if(Constants.IN_DECLARATION_FORM.equals(declarationType))str1 = "Gyvenamosios vietos deklaravimo ir \n deklaravimo duomenø tvarkymo taisykliø \n 1 priedas\n\n";
		else
		if(Constants.OUT_DECLARATION_FORM.equals(declarationType))str1 = "Gyvenamosios vietos deklaravimo ir \n deklaravimo duomenø tvarkymo taisykliø \n 2 priedas\n\n";
		else
		if(Constants.CHNG_OUT_DECLARATION_FORM.equals(declarationType))str1 = "Gyvenamosios vietos deklaravimo ir \n deklaravimo duomenø tvarkymo taisykliø \n 2 priedas\n\n";
		else
		if(Constants.GVNA_DECLARATION_FORM.equals(declarationType))str1 = "Gyvenamosios vietos nedeklaravusiø \n asmenø apskaitos taisykliø \n 1 priedas\n\n";
		PdfPCell cell2 = new PdfPCell(new Paragraph(str1,arial7));
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell2.setBorderWidth(0);
		table.addCell(cell2);

		String str2 = "";
		if(Constants.IN_DECLARATION_FORM.equals(declarationType))str2 = "GYVENAMOSIOS VIETOS DEKLARACIJA,\n PILDOMA ASMENIUI PAKEITUS GYVENAMÀJÀ VIETÀ \n LIETUVOS RESPUBLIKOJE AR ATVYKUS GYVENTI Á LIETUVOS RESPUBLIKÀ";
		else
		if(Constants.OUT_DECLARATION_FORM.equals(declarationType))str2 = "GYVENAMOSIOS VIETOS DEKLARACIJA,\n PILDOMA ASMENIUI IÐVYKSTANT \n IÐ LIETUVOS RESPUBLIKOS ILGESNIAM NEI ÐEÐIØ MËNESIØ LAIKOTARPIUI";
		else
		if(Constants.GVNA_DECLARATION_FORM.equals(declarationType))str2 = "PRAÐYMAS ÁTRAUKTI Á GYVENAMOSIOS VIETOS \n NEDEKLARAVUSIØ ASMENØ APSKAITÀ";
		
		PdfPCell cell3 = new PdfPCell(new Paragraph(str2,arial12));
		cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell3.setBorderWidth(0);
		table.addCell(cell3);		

		PdfPCell cell4 = new PdfPCell(generateGautaTable());
		cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell4.setBorderWidth(0);
		table.addCell(cell4);
		
		String str3 = "";
		if(Constants.GVNA_DECLARATION_FORM.equals(declarationType))str3 = "     Asmuo, uþpildæs praðymà, atsako uþ pateiktø duomenø teisingumà ástatymø nustatyta tvarka";
		else str3 = "    Asmuo, uþpildæs deklaracijà, atsako uþ pateiktø duomenø teisingumà ástatymø nustatyta tvarka (Lietuvos Respublikos gyvenamosios vietos deklaravimo\nástatymo 15 str.)";
		
		
		PdfPCell cell5 = new PdfPCell(new Paragraph(str3,arial8));
		if(!Constants.GVNA_DECLARATION_FORM.equals(declarationType))
			cell5.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		else cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell5.setBorderWidth(0);
		cell5.setColspan(2);
		cell5.setPaddingTop(10);
		table.addCell(cell5);		
		
		d.add(table);
			
	}
	public PdfPTable generateGautaTable()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new float[]{45,10,45});

		PdfPCell cell0 = new PdfPCell(new Paragraph(String.valueOf(session.getAttribute("deklaracija_oficialus_pavadinimas")),arial7));
		
		cell0.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell0.setBorderWidth(0);
		cell0.setColspan(3);
		table.addCell(cell0);

		
		PdfPCell cell1 = new PdfPCell(new Paragraph("Deklaravimo ástaigos pavadinimas\n ",arial7));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell1.setBorderWidth(0);
		cell1.setBorderWidthTop(1);
		cell1.setColspan(3);
		table.addCell(cell1);

		
	
		String[] c1 = CalendarUtils.getDateFromTimestamp(deklaracija.getGavimoData());
		String rez = " ";
		if(!"".equals(c1[0]))rez = c1[0]+"-"+c1[1]+"-"+c1[2];
		PdfPCell cell2 = new PdfPCell(new Paragraph("GAUTA\n"+rez,arial7));
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell2.setBorderWidth(0);
		cell2.setBorderWidthBottom(1);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Paragraph(" ",arial7));
		cell3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell3.setBorderWidth(0);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Paragraph("Nr. "+(null != deklaracija.getRegNr()?deklaracija.getRegNr():" "),arial7));
		cell4.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell4.setBorderWidth(0);
		cell4.setBorderWidthBottom(1);
		table.addCell(cell4);		
		
		PdfPCell cell5 = new PdfPCell(new Paragraph("(data)",arial7));
		cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell5.setBorderWidth(0);
		table.addCell(cell5);

		table.addCell(cell3);

		PdfPCell cell6 = new PdfPCell(new Paragraph("(reg. Nr.)",arial7));
		cell6.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell6.setBorderWidth(0);
		table.addCell(cell6);		
		
		return table;
	}

	public void generateFirstPart(Document d)
	throws DocumentException
	{
		Image img410 = null;
        try{
        	FileInputStream fr = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") + separator+"pdfimages"+separator+"4-10.gif");
        	byte[] bytes = new byte[fr.available()];
        	fr.read(bytes);
        	img410 = Image.getInstance(bytes);
        	img410.scaleAbsolute(10,180);
        }
        catch(BadElementException bee){bee.printStackTrace();}        
        catch(IOException ioe){ioe.printStackTrace();}
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{3,97});	
		PdfPCell pc = new PdfPCell(img410);
		pc.setBorderWidth(2);
		pc.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		pc.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(pc);
		PdfPCell cell = new PdfPCell(generateFirstInnerTable());
		cell.setBorderWidth(2);
		cell.setPadding(0);
		table.addCell(cell);
		d.add(table);
	}
	public PdfPTable generateFirstInnerTable()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(1);
		
		PdfPCell cell1 = new PdfPCell(generateFirstInnerFirstTable());		
		cell1.setPadding(0);
		table.addCell(cell1);
		
		PdfPCell cell2 = new PdfPCell(generateFirstInnerSecondTable());
		cell2.setPadding(0);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(generateFirstInnerThirdTable()); //generuojami adreso laukai
		cell3.setPadding(0);
		table.addCell(cell3);		
		
		PdfPCell cell4 = new PdfPCell(generateFirstInnerFourTable());
		cell4.setPadding(0);
		table.addCell(cell4);		
		return table;
	}
	public PdfPTable generateFirstInnerThirdTable()
	throws DocumentException
	{
		PdfPTable table = null;
		if(Constants.IN_DECLARATION_FORM == declarationType)
		{
			table = new PdfPTable(2);
			PdfPCell cell1 = new PdfPCell(generateTable2("9. Deklaruojama gyvenamoji vieta"," ",false));	
			cell1.setPadding(0);
			table.addCell(cell1);			
			
			
			
			PdfPTable table3 = new PdfPTable(1);
			table3.addCell(generateTable2("10. Ankstesnës gyvenamosios vietos adresas"," ",false));	
			table3.addCell(generateTable2("  Valstybë"," ",false));	
			
			
			PdfPCell cell2 = new PdfPCell(table3);
			cell2.setPadding(0);
			table.addCell(cell2);			

			PdfPCell cell3 = new PdfPCell(generateTable2("  Savivaldybë"," ",false));	
			cell3.setFixedHeight(WIDTH);
			cell3.setPadding(0);
			table.addCell(cell3);				
	
			PdfPCell cell4 = new PdfPCell(generateTable2("  Savivaldybë"," ",false));	
			cell4.setPadding(0);
			table.addCell(cell4);			

			PdfPCell cell5 = new PdfPCell(generateTable2("  Miestas"," ",false));	
			cell5.setPadding(0);
			cell5.setFixedHeight(WIDTH);
			table.addCell(cell5);			
			
			PdfPCell cell6 = new PdfPCell(generateTable2("  Miestas"," ",false));	
			cell6.setPadding(0);
			table.addCell(cell6);
			
			PdfPCell cell7 = new PdfPCell(generateTable2("  Seniûnija"," ",false));	
			cell7.setPadding(0);
			cell7.setFixedHeight(WIDTH);			
			table.addCell(cell7);			
			
			PdfPCell cell8 = new PdfPCell(generateTable2("  Seniûnija"," ",false));	
			cell8.setPadding(0);
			table.addCell(cell8);		
			
			PdfPCell cell9 = new PdfPCell(generateTable2("  Miestelis / kaimas"," ",false));	
			cell9.setPadding(0);
			cell9.setFixedHeight(WIDTH);			
			table.addCell(cell9);		
	
			PdfPCell cell10 = new PdfPCell(generateTable2("  Miestelis / kaimas"," ",false));	
			cell10.setPadding(0);
			table.addCell(cell10);
		
			PdfPCell cell11 = new PdfPCell(generateTable2("  Gatvë"," ",false));	
			cell11.setPadding(0);
			cell11.setFixedHeight(WIDTH);			
			table.addCell(cell11);	
			
			PdfPCell cell12 = new PdfPCell(generateTable2("  Gatvë"," ",false));	
			cell12.setPadding(0);
			table.addCell(cell12);
			
			
					
			//////////////////////////////////////////
			PdfPTable table1 = new PdfPTable(6);
			table1.setWidths(new float[]{30,5,20,10,5,30});
			PdfPCell celld1 = new PdfPCell(generateTable2("Namo Nr."," ",false));	
			celld1.setPadding(0);
			celld1.setFixedHeight(WIDTH);
			table1.addCell(celld1);
			table1.addCell(generateGrayCell());			

			PdfPCell celld2 = new PdfPCell(generateTable2("Korpuso Nr."," ",false));	
			celld2.setPadding(0);
			celld2.setColspan(2);
			table1.addCell(celld2);
			
			table1.addCell(generateGrayCell());

			PdfPCell celld3 = new PdfPCell(generateTable2("Buto Nr."," ",false));	
			celld3.setPadding(0);
			table1.addCell(celld3);
			
			PdfPCell celld4;
			if (atvykimoDeklaracija.getAtvykimoData()!=null){
				celld4 = new PdfPCell(generateDataCell("Atvykimo data",CalendarUtils.getDateFromTimestamp(atvykimoDeklaracija.getAtvykimoData())));
			}
			else celld4 = new PdfPCell(generateDataCell("Atvykimo data",new String[]{"","",""}));
 			celld4.setFixedHeight(WIDTH);
			celld4.setPadding(0);
			celld4.setColspan(3);
			table1.addCell(celld4);
			
			PdfPCell celld5 = new PdfPCell(generateGrayCell());
			celld5.setPadding(0);
			celld5.setColspan(3);
			table1.addCell(celld5);
			//////////////////////////////////////////
			PdfPCell cell13 = new PdfPCell(table1);
			cell13.setPadding(0);
			table.addCell(cell13);
			//////////////////////////////////////////
			
			
			
			//////////////////////////////////////////
			PdfPTable table2 = new PdfPTable(5);
			table2.setWidths(new float[]{30,5,30,5,30});
			PdfPCell celld7 = new PdfPCell(generateTable2("Namo Nr."," ",false));	
			celld7.setFixedHeight(WIDTH);			
			celld7.setPadding(0);
			table2.addCell(celld7);
			table2.addCell(generateGrayCell());			

			PdfPCell celld8 = new PdfPCell(generateTable2("Korpuso Nr."," ",false));	
			celld8.setPadding(0);
			table2.addCell(celld8);
			
			table2.addCell(generateGrayCell());

			PdfPCell celld9 = new PdfPCell(generateTable2("Buto Nr."," ",false));	
			celld9.setPadding(0);
			table2.addCell(celld9);
			
			
			PdfPCell celld10 = new PdfPCell(generateGrayCell());
			celld10.setPadding(0);
			celld10.setColspan(5);
			table2.addCell(celld10);
			//////////////////////////////////////////
			PdfPCell cell14 = new PdfPCell(table2);
			cell14.setPadding(0);
			table.addCell(cell14);
			//////////////////////////////////////////			

		}
		if(Constants.OUT_DECLARATION_FORM == declarationType)
		{
			table = new PdfPTable(2);
			PdfPTable tablein = new PdfPTable(1);
			PdfPCell cell1 = new PdfPCell(generateTable2("9. Gyvenamoji vieta, ið kurios iðvykstama"," ",false));	
			cell1.setFixedHeight(WIDTH);
			cell1.setPadding(0);
			tablein.addCell(cell1);			
			
			PdfPCell cell3 = new PdfPCell(generateTable2("  Savivaldybë"," ",false));	
			cell3.setFixedHeight(WIDTH);			
			cell3.setPadding(0);
			tablein.addCell(cell3);				
	

			PdfPCell cell5 = new PdfPCell(generateTable2("  Miestas"," ",false));	
			cell5.setFixedHeight(WIDTH);			
			cell5.setPadding(0);
			tablein.addCell(cell5);			
			
			PdfPCell cell7 = new PdfPCell(generateTable2("  Seniûnija"," ",false));	
			cell7.setFixedHeight(WIDTH);			
			cell7.setPadding(0);
			tablein.addCell(cell7);			
			
			PdfPCell cell10 = new PdfPCell(generateTable2("  Miestelis/kaimas"," ",false));	
			cell10.setFixedHeight(WIDTH);			
			cell10.setPadding(0);
			tablein.addCell(cell10);
		
			PdfPCell cell12 = new PdfPCell(generateTable2("  Gatvë"," ",false));	
			cell12.setFixedHeight(WIDTH);			
			cell12.setPadding(0);
			tablein.addCell(cell12);
			
			
					
			
			//////////////////////////////////////////
			PdfPTable table1 = new PdfPTable(7);
			table1.setWidths(new float[]{30,5,20,10,5,30,2});
			PdfPCell celld1 = new PdfPCell(generateTable2("Namo Nr."," ",false));	
			celld1.setFixedHeight(WIDTH);
			celld1.setPadding(0);
			table1.addCell(celld1);
			table1.addCell(generateGrayCell()); //2			

			PdfPCell celld2 = new PdfPCell(generateTable2("Korpuso Nr."," ",false));	
			celld2.setPadding(0);
			celld2.setColspan(2);
			table1.addCell(celld2); //3
			
			table1.addCell(generateGrayCell());

			PdfPCell celld3 = new PdfPCell(generateTable2("Buto Nr."," ",false));	
			celld3.setPadding(0);
			celld3.setColspan(2);
			table1.addCell(celld3);
			
			
			
			PdfPCell celld4;
			if (isvykimoDeklaracija.getIsvykimoData()!=null){
				celld4 = new PdfPCell(generateDataCell("Iðvykimo data",CalendarUtils.getDateFromTimestamp(isvykimoDeklaracija.getIsvykimoData())));
			}
			else celld4 = new PdfPCell(generateDataCell("Iðvykimo data",new String[]{"","",""}));
			celld4.setPadding(0);
			celld4.setFixedHeight(WIDTH);
			celld4.setColspan(3);
			table1.addCell(celld4);
			
			PdfPCell celld5 = new PdfPCell(generateGrayCell());
			celld5.setPadding(0);
			celld5.setColspan(4);
			table1.addCell(celld5);
			//////////////////////////////////////////
			PdfPCell cell13 = new PdfPCell(table1);
			cell13.setPadding(0);
			tablein.addCell(cell13);
			//////////////////////////////////////////

			PdfPCell cellf = new PdfPCell(tablein);
			cellf.setPadding(0);
			table.addCell(cellf);
		
			table.addCell(generateTable2("10. Numatoma gyvenamoji vieta uþsienyje",/*String.valueOf(session.getAttribute("ankstesne_gv")).toUpperCase()+"\n"+
					String.valueOf(session.getAttribute("ankstesne_gv_pastabos"))*/"",false));	
		}
		if(Constants.GVNA_DECLARATION_FORM == declarationType)
		{
			table = new PdfPTable(2);
			table.setWidths(new float[]{33,67});
			
			PdfPCell cell1 = new PdfPCell(generateTable2("9. Savivaldybë, kurioje asmuo gyvena",String.valueOf(session.getAttribute("savivaldybes_pav")).toUpperCase()+"\n"+
					gvnaDeklaracija.getSavivaldybePastabos(),false));	
			cell1.setPadding(0);
			cell1.setFixedHeight(120);
			table.addCell(cell1);

			
			String adr = "";
			if(1 == gvnaDeklaracija.getAnkstesneVietaTipas().intValue())
			{			
				
				adr = String.valueOf(session.getAttribute("ankstesne_gv_pavadinimas")).toUpperCase()+"\n"+
				deklaracija.getAnkstesneVietaValstybesPastabos();
			}
			else
			if(2 == gvnaDeklaracija.getAnkstesneVietaTipas().intValue())
			{
				adr = gvnaDeklaracija.getAnkstesneVietaKita();
			}
			
			PdfPCell cell2 = new PdfPCell(generateTable2("10. Ankstesnës gyvenamosios vietos adresas / savivaldybë, kurioje asmuo gyveno",adr.toUpperCase(),false));	
			cell2.setPadding(0);
			table.addCell(cell2);
			
			PdfPCell cell3 = new PdfPCell(generateTable2("11. Prieþastys, dël kuriø asmuo negali deklaruoti gyvenamosios vietos",gvnaDeklaracija.getPriezastys(),false));	
			cell3.setPadding(0);
			cell3.setFixedHeight(70);			
			cell3.setColspan(2);
			table.addCell(cell3);
			
			
		}
		return table;
	}
	
	public PdfPTable generateFirstInnerFirstTable()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(2);
		table.setWidths(new float[]{80,20});
		PdfPCell cell1 = new PdfPCell(generateAsmInfoTable());
		cell1.setPadding(0);
		table.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Paragraph("PASTABA. Jeigu vardai\nar pavardë sudaro\ndaugiau negu 31 þenklà,\nreikia áraðyti vienà vardà\nar vienà dvigubos\npavardës dalá.",arial6));
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell2.setIndent(20);
		table.addCell(cell2);
		if(Constants.GVNA_DECLARATION_FORM != declarationType)
		{
			PdfPCell cellp = new PdfPCell(generatePilietybe());
			cellp.setColspan(2);
			table.addCell(cellp);
		}
		return table;
	}
	public PdfPTable generatePilietybe()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(2);
		table.setWidths(new float[]{60,40});

		String pilietybe = "";
		try {
			pilietybe = deklaracija.getPilietybe().getPilietybe();
		} catch (NullPointerException e){}			
		PdfPCell cell1 = new PdfPCell(generateTable2("7. Pilietybë",pilietybe.toUpperCase(),false));
		
		cell1.setFixedHeight(WIDTH);
		cell1.setPadding(0);
		table.addCell(cell1);
		table.addCell(generateGrayCell());
		return table;
	}
	public PdfPTable generateAsmInfoTable()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(5);
		table.setWidths(new float[]{30,4,33,3,25});
		String asmensKodas = "";
		if(null != deklaracija.getAsmuo())asmensKodas = String.valueOf(deklaracija.getAsmuo().getAsmKodas());
		PdfPCell cell1 = generateInputCell("1. Asmens kodas",asmensKodas.toUpperCase(),11);
		cell1.setPadding(0);
		cell1.setFixedHeight(WIDTH);
		table.addCell(cell1);
		
		
		
		PdfPCell tcell1 = generateGrayCell();
		tcell1.setBorderWidth(2);
		table.addCell(tcell1);
	
		String[] data = null;
		if(null != deklaracija.getAsmuo())data = CalendarUtils.getDateFromTimestamp(deklaracija.getAsmuo().getAsmGimData());
		else data = CalendarUtils.getDateFromTimestamp(deklaracija.getLaikinasAsmuo().getGimimoData());		
		PdfPCell cell2 = generateDataCell("2. Gimimo data",data);
		cell2.setPadding(0);
		table.addCell(cell2);

		table.addCell(generateGrayCell());
		int lytis = 0;
		String l = "";
		if(null != deklaracija.getAsmuo())
			l = deklaracija.getAsmuo().getAsmLytis();
		else l = deklaracija.getLaikinasAsmuo().getLytis();
		if("M".equals(l))lytis = 1;
		table.addCell(generateTable1("3. Lytis",new String[]{"Vyr.","Mot."},lytis));

		
	
		String vardas = "";
		if(null != deklaracija.getAsmuo())vardas = deklaracija.getAsmuo().getVardas();
		else vardas = deklaracija.getLaikinasAsmuo().getVardas();		
		PdfPCell cell4 = generateInputCell("4. Vardas (-ai)",vardas.toUpperCase(),31);
		cell4.setColspan(5);
		cell4.setPadding(0);
		cell4.setFixedHeight(WIDTH);
		table.addCell(cell4);
		
		String pavarde = "";
		if(null != deklaracija.getAsmuo())pavarde = deklaracija.getAsmuo().getPavarde();
		else pavarde = deklaracija.getLaikinasAsmuo().getPavarde();		
		
		PdfPCell cell5 = generateInputCell("5. Pavardë",pavarde.toUpperCase(),31);
		cell5.setColspan(5);
		cell5.setPadding(0);
		cell5.setFixedHeight(WIDTH);
		table.addCell(cell5);

		String pavardePrev = "";
		/*if(null != deklaracija.getAsmuo())
		{
			if(null != deklaracija.getAsmuo().getPavardePrev())	pavardePrev = deklaracija.getAsmuo().getPavardePrev().toUpperCase();
		}
		else pavardePrev = "";	*/	
		if(deklaracija.getAnkstesnePavarde() != null)
				pavardePrev = deklaracija.getAnkstesnePavarde().toUpperCase();
		PdfPCell cell6 = generateInputCell("6. Ankstesnë pavardë",pavardePrev,31);
		cell6.setColspan(5);
		cell6.setPadding(0);
		cell6.setFixedHeight(WIDTH);
		table.addCell(cell6);
		
		
		return table;	
	}
	
	public PdfPTable generateFirstInnerFourTable()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(5);
		table.setWidths(new float[]{16,26,16,26,16});
		
		if(Constants.GVNA_DECLARATION_FORM != declarationType)
		{
			
			PdfPCell cell1 = new PdfPCell(generateTable1("11. Deklaracija pateikta",
					new String[]{"Asmeniðkai","Vieno ið tëvø\n(átëviø)","Globëjo\n(rûpintojo)","Kito teisëto atstovo"},deklaracija.getPateike().intValue()));
			cell1.setColspan(5);
			cell1.setFixedHeight(WIDTH);
			table.addCell(cell1);
		}
		else
		{
		
			PdfPTable tablein = new PdfPTable(2);			
			
			
			PdfPCell cell1 = new PdfPCell(generateTable2("12. Pageidaujama paþymos apie apie átraukimà á gyvenamosios vietos nedeklaravusiø asmenø apskaità","",false));	
			cell1.setBorderWidth(0);
			cell1.setBorderWidthBottom(1);
			cell1.setFixedHeight(15);
			tablein.addCell(cell1);
			PdfPCell cell2 = new PdfPCell(generateTable1("",new String[]{"Taip","Ne"},1 == deklaracija.getPageidaujaPazymos().intValue()?0:1));
			cell2.setBorderWidth(0);
			cell2.setBorderWidthBottom(1);
			tablein.addCell(cell2);			
			
			PdfPCell cell3 = new PdfPCell(generateTable1("13. Praðymas pateiktas",
					new String[]{"Asmeniðkai","Vieno ið tëvø\n(átëviø)"},deklaracija.getPateike().intValue()));
			cell3.setBorderWidth(0);
			cell3.setFixedHeight(15);			
			tablein.addCell(cell3);
			
			PdfPCell cell4 = new PdfPCell(new Paragraph(" "));
			cell4.setBorderWidth(0);
			tablein.addCell(cell4);
			PdfPCell pc = new PdfPCell(tablein);
			pc.setColspan(5);
			table.addCell(pc);
			
		}
		
		String pateikeVardas = "";
		if(null != deklaracija.getPateikeVardas()) pateikeVardas = deklaracija.getPateikeVardas();
		PdfPCell cell2 = new PdfPCell(generateTable2("Vardas",pateikeVardas,false));	
		cell2.setColspan(3);
		table.addCell(cell2);		

		String pateikePavarde = "";
		if(null != deklaracija.getPateikePavarde()) pateikePavarde = deklaracija.getPateikePavarde();
		PdfPCell cell3 = new PdfPCell(generateTable2("Pavardë",pateikePavarde,false));	
		cell3.setFixedHeight(WIDTH);
		cell3.setColspan(2);
		table.addCell(cell3);	
		
		table.addCell(generateGrayCell());
		String[] data;
		if (deklaracija.getDeklaravimoData() != null) {
			data = CalendarUtils.getDateFromTimestamp(deklaracija.getDeklaravimoData());
		}
		else data = new String[]{"","",""};
		table.addCell(generateDataCell("Data",data));
		table.addCell(generateGrayCell());		
		table.addCell(generateTable2("Paraðas","",false));	
		table.addCell(generateGrayCell());	
		
		if(Constants.IN_DECLARATION_FORM == declarationType)
		{
			int taipNe = 0;
			if(0 == deklaracija.getPageidaujaPazymos().intValue()) taipNe = 1;
			if(1 == deklaracija.getPageidaujaPazymos().intValue()) taipNe = 0;			
			PdfPCell cell12 = new PdfPCell(generateTable1("12. Pageidauju gauti dokumentà, patvirtinantá\ndeklaruotà gyvenamàjà vietà",new String[]{"Taip","Ne"},taipNe));
			cell12.setColspan(5);
			cell12.setFixedHeight(WIDTH);
			table.addCell(cell12);

			PdfPCell cell13 = new PdfPCell(generateTable1("13. Asmens, deklaruojanèio gyvenamàjà\nvietà, ryðys su deklaruojama\ngyvenamàja vieta",new String[]{"Savininkas","Nuomininkas","Subnuomininkas","Kita\n(áraðyti)"},(int)atvykimoDeklaracija.getRysysSuGv()));
			cell13.setColspan(5);
			cell13.setFixedHeight(WIDTH);
			table.addCell(cell13);
			
		}
		return table;
	}	
	public PdfPTable generateFirstInnerSecondTable()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(Constants.GVNA_DECLARATION_FORM ==declarationType?6:5);
		if(Constants.GVNA_DECLARATION_FORM == declarationType)
			table.setWidths(new float[]{12,10,13,10,25,30});
		else table.setWidths(new float[]{22,12,8,25,33});

		
		if(Constants.GVNA_DECLARATION_FORM == declarationType){
			String pilietybe = "";
			try {
				pilietybe = deklaracija.getPilietybe().getPilietybe();
			} catch (NullPointerException e){}	
			PdfPCell cell1 = new PdfPCell(generateTable2("7. Pilietybë",pilietybe.toUpperCase(),false));
			table.addCell(cell1);
		}
		
		int docType = -1;
		if(null == deklaracija.getDokumentoTipas())docType = -1;
		else
		{ 
			String type = deklaracija.getDokumentoRusis();
			if("LR pilieèio pasas".equals(type))docType = 0;
			if("Pasas".equals(type))docType = 1;
			if("ATK".equals(type))docType = 2;
			if("PL".equals(type))docType = 3;
			if("LL".equals(type))docType = 4;
			if("PL (EB)".equals(type))docType = 5;
			if("LL (EB)".equals(type))docType = 6;
			if("PL (ES)".equals(type))docType = 7;
			if("LL (ES)".equals(type))docType = 8;
			//if("Kitas".equals(type))docType = 9;
		}
		PdfPCell cell1 = null;
		
		
		if(null != deklaracija.getLaikinasAsmuo())
		{
			cell1 = new PdfPCell(generateTable1("8. Asmens\ndokumentas",
					new String[]{"LR Pilieèio\npasas","Pasas","ATK","PL","LL","PL\n(EB)","LL\n(EB)","PL\n(ES)","LL\n(ES)","Kitas"},9));
		}
		else 
		{
			cell1 = new PdfPCell(generateTable1("8. Asmens\ndokumentas",
					new String[]{"LR Pilieèio\npasas","Pasas","ATK","PL","LL","PL\n(EB)","LL\n(EB)","PL\n(ES)","LL\n(ES)","Kitas"},docType));
		}
		
		cell1.setFixedHeight(WIDTH);
		cell1.setColspan(5);
		table.addCell(cell1);
	
		PdfPCell cell2 = new PdfPCell(generateTable3("Numeris",deklaracija.getDokumentoNr(),false)); //ju.k 2007.06.26
		cell2.setColspan(Constants.GVNA_DECLARATION_FORM ==declarationType?3:2);
		table.addCell(cell2);	
		table.addCell(generateGrayCell());
						
		String[] isdavimoData = CalendarUtils.getDateFromTimestamp(deklaracija.getDokumentoData());
	
		
		table.addCell(generateDataCell("Iðdavimo data",isdavimoData));
		table.addCell(generateGrayCell());		
		
		
		PdfPCell cell3 = new PdfPCell(generateTable3("Kas iðdavë",null != deklaracija.getDokumentoIsdavejas()?deklaracija.getDokumentoIsdavejas().toUpperCase():"",false));
		cell3.setFixedHeight(WIDTH);
		//cell3.setColspan(Constants.GVNA_DECLARATION_FORM ==declarationType?6:5);
		cell3.setColspan(Constants.GVNA_DECLARATION_FORM ==declarationType?6:5);
		table.addCell(cell3);	
		
		PdfPCell celld = new PdfPCell(generateGrayCell());
		if(Constants.GVNA_DECLARATION_FORM == declarationType)celld.setColspan(2);
		table.addCell(celld);
		
		PdfPCell cell4 = new PdfPCell(generateTable2("Jei pateiktas LL, LL(EB), LL(ES)","",false));	
		cell4.setColspan(2);
		table.addCell(cell4);
		
		String[] galiojimoData = CalendarUtils.getDateFromTimestamp(deklaracija.getDokumentoGaliojimas());
		table.addCell(generateDataCell("Galiojimo data",galiojimoData));
		table.addCell(generateGrayCell());		
		
		return table;
	}		
	
	public void generateSecondPart(Document d)
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		PdfPCell cell1 = new PdfPCell(new Paragraph("      Pagal Lietuvos Respublikos asmens duomenø teisinës apsaugos ástatymà (Þin., 1996, Nr. 63-1479; 2003, Nr. 15-597) Jûs galite nesutikti dël savo asmens\n" +
				"duomenø teikimo ið Lietuvos Respublikos gyventojø registro. Atsisakymà teikti savo asmens duomenis galite pateikti raðtu Gyventojø registro tarnybai prie\n" +
				"Vidaus reikalø ministerijos (A. Vivulskio g. 4A, LT-03220 Vilnius)",arial8));
		cell1.setBorderWidth(0);
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		table.addCell(cell1);
		d.add(table);
	}
	public void generateFourthPart(Document d)
	throws DocumentException
	{
		PdfPTable table = null;
		if(!Constants.IN_DECLARATION_FORM.equals(declarationType))
		{
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(new float[]{2,48,2,48});
	
			
			PdfPCell cell1 = new PdfPCell(new Paragraph("1",arial6));
			cell1.setBorderWidth(0);
			cell1.setPaddingTop(0);
			table.addCell(cell1);
	
			PdfPCell cell2 = new PdfPCell(new Paragraph("Leidimas nuolat gyventi Lietuvos Respublikoje arba Lietuvos Respublikos ilgalaikio\n" +
					"gyventojo leidimas gyventi Europos Bendrijoje",arial6));
			cell2.setBorderWidth(0);
			cell2.setPadding(0);
			table.addCell(cell2);
	
			PdfPCell cell3 = new PdfPCell(new Paragraph("4",arial6));
			cell3.setBorderWidth(0);
			cell3.setPaddingTop(0);
			table.addCell(cell3);
	
			PdfPCell cell4 = new PdfPCell(new Paragraph("Europos Bendrijø valstybës narës pilieèio leidimas gyventi",arial6));
			cell4.setBorderWidth(0);
			cell4.setPadding(0);			
			table.addCell(cell4);
			
			PdfPCell cell5 = new PdfPCell(new Paragraph("2",arial6));
			cell5.setBorderWidth(0);
			cell5.setPaddingTop(0);
			table.addCell(cell5);
	
			PdfPCell cell6 = new PdfPCell(new Paragraph("Leidimas laikinai gyventi Lietuvos Respublikoje",arial6));
			cell6.setBorderWidth(0);
			cell6.setPadding(0);			
			table.addCell(cell6);
	
			PdfPCell cell7 = new PdfPCell(new Paragraph("5",arial6));
			cell7.setBorderWidth(0);
			cell7.setPaddingTop(0);
			table.addCell(cell7);
	
			PdfPCell cell8 = new PdfPCell(new Paragraph("Europos Sàjungos valstybës narës pilieèio ðeimos nario leidimas gyventi nuolat",arial6));
			cell8.setBorderWidth(0);
			cell8.setPadding(0);			
			table.addCell(cell8);
			
	
			PdfPCell cell9 = new PdfPCell(new Paragraph("3",arial6));
			cell9.setBorderWidth(0);
			cell9.setPaddingTop(0);
			table.addCell(cell9);
	
			PdfPCell cell10 = new PdfPCell(new Paragraph("Europos Bendrijø valstybës narës pilieèio leidimas gyventi nuolat",arial6));
			cell10.setBorderWidth(0);
			cell10.setPadding(0);
			table.addCell(cell10);
	
			PdfPCell cell11 = new PdfPCell(new Paragraph("6",arial6));
			cell11.setBorderWidth(0);
			cell11.setPaddingTop(0);
			table.addCell(cell11);
	
			PdfPCell cell12 = new PdfPCell(new Paragraph("Europos Sàjungos valstybës narës pilieèio ðeimos nario leidimas gyventi",arial6));
			cell12.setBorderWidth(0);
			cell12.setPadding(0);			
			table.addCell(cell12);
		}
		else
		if(Constants.IN_DECLARATION_FORM.equals(declarationType))
		{
			table = new PdfPTable(2);
			table.setWidthPercentage(100);			
			table.setWidths(new float[]{50,50});
			
			PdfPCell cell1 = new PdfPCell(new Paragraph("(Tæsinys)",arial7));
			cell1.setBorderWidth(0);
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell1.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			table.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(new Paragraph("Gyvenamosios vietos deklaravimo ir\n" +
					"dekaravimo duomenø tvarkymo taisykliø\n" +
					"1 priedo tæsinys",arial7));
			cell2.setBorderWidth(0);
			cell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell2.setVerticalAlignment(PdfPCell.ALIGN_TOP);
			cell2.setIndent(150);
			table.addCell(cell2);
			
			
		}
			
		d.add(table);
	}
	public void generateFifthPart(Document d)
	throws DocumentException
	{
        if(!Constants.IN_DECLARATION_FORM.equals(declarationType))return;		
		Image pildo2 = null;
        try{
        	//pildo2= Image.getInstance("pildo2.gif");
        	FileInputStream fr1 = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") + separator+"pdfimages"+separator+"pildo2.gif");
        	byte[] bytes1 = new byte[fr1.available()];
        	fr1.read(bytes1);
        	pildo2 = Image.getInstance(bytes1);
        }
        catch(BadElementException bee){bee.printStackTrace();}        
        catch(IOException ioe){ioe.printStackTrace();}


		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{5,95});

		table.addCell(pildo2);
		PdfPCell cell1 = new PdfPCell(generateFifthInner());
		cell1.setPadding(0);
		table.addCell(cell1);
		d.add(table);
		
		
	}
	public PdfPTable generateFifthInner()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new float[]{15,40,45});	
		PdfPCell cell1 = new PdfPCell(generateTable1("15. Gyvenamosios patalpos savininkas",new String[]{"Juridinis asmuo","Fizinis asmuo"},(int)atvykimoDeklaracija.getSavininkoTipas()));
		cell1.setPadding(0);
		cell1.setFixedHeight(WIDTH);
		cell1.setColspan(3);
		table.addCell(cell1);		
		PdfPCell cell2 = new PdfPCell(new Paragraph("16. Gyvenamosios\npatalpos savininko\nar jo ágalioto\nasmens sutikimas",arial6));
		cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell2.setBorderWidthBottom(0);
		table.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(generateFifthInner2());
		cell3.setColspan(2);
		table.addCell(cell3);	
		PdfPCell cell4 = new PdfPCell(new Paragraph(" "));
		cell4.setBorderWidthTop(0);		
		table.addCell(cell4);	
		PdfPCell cell5 = new PdfPCell(generateFifthInner3());
		cell5.setColspan(2);
		table.addCell(cell5);		
		return table;
	}
	public PdfPTable generateFifthInner2()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(2);
		table.setWidths(new float[]{50,50});
		
		String JaPavadinimas = atvykimoDeklaracija.getJaPavadinimas();
		if (JaPavadinimas == null) JaPavadinimas = "";	
		PdfPCell cell1 = new PdfPCell(generateTable2("Juridinio asmens pavadinimas",JaPavadinimas,false));		
		cell1.setFixedHeight(WIDTH*2);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(generateTable2("Savininko ar jo ágalioto asmens vardas ir pavardë arba juridinio asmens\nágalioto atstovo pareigos, vardas ir pavardë",atvykimoDeklaracija.getFaVardasPavarde(),true));	
		table.addCell(cell2);		

		String JaKodas = atvykimoDeklaracija.getJaKodas();
		if (JaKodas == null) JaKodas = "";	
		PdfPCell cell3 = new PdfPCell(generateTable2("Kodas",JaKodas,false));		
		cell3.setFixedHeight(WIDTH);		
		table.addCell(cell3);		

		PdfPCell cell4 = new PdfPCell(generateTable2("Asmens kodas ar gimimo data",atvykimoDeklaracija.getFaKodas(),false));	
		table.addCell(cell4);

		String JaBuveine = atvykimoDeklaracija.getJaBuveine();
		if (JaBuveine == null) JaBuveine = "";	
		PdfPCell cell5 = new PdfPCell(generateTable2("Buveinës adresas",JaBuveine,false));	
		cell5.setFixedHeight(WIDTH*2);		
		table.addCell(cell5);
	
		PdfPCell cell6 = new PdfPCell(generateTable2("Savininko gyvenamoji vieta",atvykimoDeklaracija.getFaAdresas(),false));	
		table.addCell(cell6);
		
		return table;
	}	
	public PdfPTable generateFifthInner3()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(5);
		table.setWidths(new float[]{10,30,10,30,10});
		
		PdfPCell cell1 = new PdfPCell(generateGrayCell());	
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(generateDataCell("Data",CalendarUtils.getDateFromTimestamp(atvykimoDeklaracija.getSavininkoParasoData())));	
		table.addCell(cell2);		
		
		PdfPCell cell3 = new PdfPCell(generateGrayCell());	
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(generateTable2("Paraðas"," ",false));	
		table.addCell(cell4);
		
		
		PdfPCell cell5 = new PdfPCell(generateGrayCell());	
		table.addCell(cell5);

		
		return table;
	}
	public PdfPCell generateGrayCell()
	throws DocumentException
	{
		PdfPCell cell = new PdfPCell();
		cell.setNoWrap(true);
		cell.setPadding(0);
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(0);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;		
	}
	public PdfPCell generateDataCell(String title,String[] data)
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(10);
		PdfPCell cell0 = new PdfPCell(new Paragraph(title,arial7));
		cell0.setBorderWidthBottom(0);
		cell0.setColspan(10);
		table.addCell(cell0);

		if("" != data[0])
		{
			table.addCell(generateOneCell(String.valueOf(data[0].charAt(0))));
			table.addCell(generateOneCell(String.valueOf(data[0].charAt(1))));
			table.addCell(generateOneCell(String.valueOf(data[0].charAt(2))));
			table.addCell(generateOneCell(String.valueOf(data[0].charAt(3))));
		}
		else
		{
			table.addCell(generateOneCell(" "));
			table.addCell(generateOneCell(" "));
			table.addCell(generateOneCell(" "));
			table.addCell(generateOneCell(" "));
		}
		table.addCell(generateOneCell("-"));		
		
		if("" != data[1])
		{
			table.addCell(generateOneCell(String.valueOf(data[1].charAt(0))));
			table.addCell(generateOneCell(String.valueOf(data[1].charAt(1))));
		}
		else
		{
			table.addCell(generateOneCell(" "));
			table.addCell(generateOneCell(" "));
		}
		
		table.addCell(generateOneCell("-"));	
		
		if("" != data[2])
		{
			table.addCell(generateOneCell(String.valueOf(data[2].charAt(0))));
			table.addCell(generateOneCell(String.valueOf(data[2].charAt(1))));
		}
		else
		{
			table.addCell(generateOneCell(" "));
			table.addCell(generateOneCell(" "));
		}

		PdfPCell cell = new PdfPCell(table);
		return cell;		
	}
	public PdfPCell generateInputCell(String title,String value,int count)
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(count);
		PdfPCell cell0 = new PdfPCell(new Paragraph(title,arial7));
		cell0.setBorderWidthBottom(0);
		cell0.setColspan(count);
		table.addCell(cell0);

		for(int i = 0;i < count;i++)
		{
			if(null == value)table.addCell(generateOneCell(" "));
			else
			{
				if(i < value.length())table.addCell(generateOneCell(String.valueOf(value.charAt(i))));
				else table.addCell(generateOneCell(" "));
			}
		}
		PdfPCell cell = new PdfPCell(table);
		return cell;		
	}	
	
	public PdfPCell generateOneCell(String title)
	throws DocumentException
	{
		PdfPCell cell = new PdfPCell(new Paragraph(title,arial10));
		cell.setBorderColor(DATA_COLOR);
		cell.setBorderWidthTop(0);
		cell.setBorderWidth(0.1f);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	
	
	public void generateThirdPart(Document d)
	throws DocumentException
	{
		
		Image pildo = null;
        try{
        	//pildo= Image.getInstance("pildo.gif");
        	FileInputStream fr1 = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") +separator+"pdfimages"+separator+"pildo.gif");
        	byte[] bytes1 = new byte[fr1.available()];
        	fr1.read(bytes1);
        	pildo = Image.getInstance(bytes1);
        	
        	
        	pildo.scaleAbsolute(60,80);
        }
        catch(BadElementException bee){bee.printStackTrace();}        
        catch(IOException ioe){ioe.printStackTrace();}		
		
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{15,15,70});
		
		PdfPCell cell1 = new PdfPCell(pildo);
		cell1.setPadding(1);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Paragraph((Constants.OUT_DECLARATION_FORM == declarationType? "12":"14")+". Duomenys\nsutikrinti, "+(Constants.GVNA_DECLARATION_FORM == declarationType? "praðymas\npriimtas":"deklaracija\npriimta"),arial7));
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell2);


		PdfPCell cc = new PdfPCell(generateThirdInner());
		cc.setPadding(0);
		table.addCell(cc);
		
		if(Constants.IN_DECLARATION_FORM != declarationType)
		{
			PdfPCell cell4 = new PdfPCell(new Paragraph("Pastabos",arial7));
			cell4.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell4.setVerticalAlignment(PdfPCell.ALIGN_TOP);
			cell4.setFixedHeight(20);
			cell4.setColspan(3);
			table.addCell(cell4);		
		}
		
		d.add(table);
	}
	public PdfPTable generateThirdInner()
	throws DocumentException
	{
		Image signed_big = null;
		Image notsigned_big = null;
		try{
        	//signed_big= Image.getInstance("signed_big.gif");
        	FileInputStream fr1 = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") + separator+"pdfimages"+separator+"signed_big.gif");
        	byte[] bytes1 = new byte[fr1.available()];
        	fr1.read(bytes1);
        	signed_big = Image.getInstance(bytes1);
        	signed_big.scaleAbsolute(20,18);

        	FileInputStream fr2 = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") + separator+"pdfimages"+separator+"notsigned_big.gif");
        	byte[] bytes2 = new byte[fr2.available()];
        	fr2.read(bytes2);
        	notsigned_big = Image.getInstance(bytes2);
        	notsigned_big.scaleAbsolute(20,18);
        	
        	
        }
        catch(BadElementException bee){bee.printStackTrace();}        
        catch(IOException ioe){ioe.printStackTrace();}				
		
		PdfPTable table = new PdfPTable(4);
		table.setWidths(new float[]{5,40,5,50});
		PdfPCell cell1 = new PdfPCell(generateTable2("Pareigø pavadinimas, vardas ir pavardë",session.getAttribute("userName")+" "
				+session.getAttribute("userSurname"),false));	
		cell1.setFixedHeight(20);
		cell1.setColspan(4);
		table.addCell(cell1);
		table.addCell(generateGrayCell());
		if (deklaracija.getDeklaravimoData()!=null) {
			table.addCell(generateDataCell("Data",CalendarUtils.getDateFromTimestamp(deklaracija.getDeklaravimoData())));
		}
		else table.addCell(generateDataCell("Data",new String[]{"","",""}));
		table.addCell(generateGrayCell());
		table.addCell(generateTable2("Paraðas","",false));	

		PdfPTable table2 = new PdfPTable(2);
		table2.setWidths(new float[]{20,80});
		PdfPCell cell11 = null;
		
		if(null != deklaracija.getAsmuo() &&
		   null == deklaracija.getLaikinasAsmuo() &&
		   1 == deklaracija.getBusena())
			cell11 = new PdfPCell(signed_big);
		else
			cell11 = new PdfPCell(notsigned_big);
		cell11.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell11.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell11.setBorderWidth(0);
		table2.addCell(cell11);
		PdfPCell cell12 = new PdfPCell(new Paragraph("Duomenys áraðyti á Gyventojø registro centrinæ duomenø bazæ",arial7));
		cell12.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell12.setBorderWidth(0);
		table2.addCell(cell12);
		
		
		PdfPCell cell3 = new PdfPCell(table2);
		cell3.setColspan(4);
		
		table.addCell(cell3);
		return table;
	}
	public void generateSixthPart(Document d)
	throws DocumentException
	{

		if(!Constants.IN_DECLARATION_FORM.equals(declarationType)) return;
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{20,80});
		
		PdfPCell cell1 = new PdfPCell(new Paragraph("17.Gyvenamosios\npatalpos savininko\nar jo ágalioto asmens\ntapatybæ ir paraðà\ntvirtinu",arial7));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell1);


		PdfPCell cc = new PdfPCell(generateSixthInner());
		cc.setPadding(0);
		table.addCell(cc);
		d.add(table);
	}
	public PdfPTable generateSixthInner()
	throws DocumentException
	{
		PdfPTable table = new PdfPTable(4);
		table.setWidths(new float[]{5,35,15,45});
		//PdfPCell cell1 = new PdfPCell(generateTable2("Pareigø pavadinimas","",false));	
		//cell1.setFixedHeight(30);
		//cell1.setColspan(4);
		//table.addCell(cell1);

		
	
		PdfPCell cell2 = new PdfPCell(generateTable2("Vardas ir pavardë",session.getAttribute("userName")+" "
				+session.getAttribute("userSurname"),false));	
		cell2.setFixedHeight(30);
		cell2.setColspan(4);
		table.addCell(cell2);
		
		table.addCell(generateGrayCell());
		//table.addCell(generateDataCell("Data",new String[]{"","",""})); //ju.k
		if (deklaracija.getDeklaravimoData()!=null) {
			table.addCell(generateDataCell("Data",CalendarUtils.getDateFromTimestamp(deklaracija.getDeklaravimoData())));
		}
		else table.addCell(generateDataCell("Data",new String[]{"","",""}));
		
		//PdfPCell cell2 = new PdfPCell(generateDataCell("Data",CalendarUtils.getDateFromTimestamp(atvykimoDeklaracija.getDeklaravimoData())));
		//CalendarUtils.getDateFromTimestamp(atvykimoDeklaracija.getDeklaravimoData())
		
		table.addCell(generateGrayCell());
		table.addCell(generateTable2("Paraðas","",false));	
		return table;
	}
	public void generateSeventhPart(Document d)
	throws DocumentException
	{
        if(Constants.IN_DECLARATION_FORM != declarationType)return;
        
		PdfPTable table = new PdfPTable(1);		
	    table.setWidthPercentage(100);
	    
		PdfPCell cell1 = new PdfPCell(new Paragraph("Pastabos",arial7));
		cell1.setNoWrap(true);
		cell1.setBorderWidthBottom(0);
		cell1.setPaddingTop(1);
		table.addCell(cell1);
	
		PdfPCell cell2 = new PdfPCell(new Paragraph(atvykimoDeklaracija.getPastabos(),arial7));
		cell2.setBorderWidthTop(0);
		cell2.setFixedHeight(380);
		table.addCell(cell2);
	
		PdfPCell cell = new PdfPCell(table);
		cell.setBorderWidth(1);
		
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);
		t.addCell(cell);
		
		d.add(t);
        
	}
	public void generateEithPart(Document d)
	throws DocumentException
	{
		if(!Constants.IN_DECLARATION_FORM.equals(declarationType))return;
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{2,98});

		
		PdfPCell cell1 = new PdfPCell(new Paragraph("1",arial6));
		cell1.setBorderWidth(0);
		cell1.setPaddingTop(0);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Paragraph("Leidimas nuolat gyventi Lietuvos Respublikoje arba Lietuvos Respublikos ilgalaikio " +
				"gyventojo leidimas gyventi Europos Bendrijoje",arial6));
		cell2.setBorderWidth(0);
		table.addCell(cell2);

		PdfPCell cell5 = new PdfPCell(new Paragraph("2",arial6));
		cell5.setBorderWidth(0);
		cell5.setPaddingTop(0);
		table.addCell(cell5);

		PdfPCell cell6 = new PdfPCell(new Paragraph("Leidimas laikinai gyventi Lietuvos Respublikoje",arial6));
		cell6.setBorderWidth(0);
		table.addCell(cell6);		
		
		PdfPCell cell9 = new PdfPCell(new Paragraph("3",arial6));
		cell9.setBorderWidth(0);
		cell9.setPaddingTop(0);
		table.addCell(cell9);

		PdfPCell cell10 = new PdfPCell(new Paragraph("Europos Bendrijø valstybës narës pilieèio leidimas gyventi nuolat",arial6));
		cell10.setBorderWidth(0);
		table.addCell(cell10);		
		
		
		PdfPCell cell3 = new PdfPCell(new Paragraph("4",arial6));
		cell3.setBorderWidth(0);
		cell3.setPaddingTop(0);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Paragraph("Europos Bendrijø valstybës narës pilieèio leidimas gyventi",arial6));
		cell4.setBorderWidth(0);
		table.addCell(cell4);
		


		PdfPCell cell7 = new PdfPCell(new Paragraph("5",arial6));
		cell7.setBorderWidth(0);
		cell7.setPaddingTop(0);
		table.addCell(cell7);

		PdfPCell cell8 = new PdfPCell(new Paragraph("Europos Sàjungos valstybës narës pilieèio ðeimos nario leidimas gyventi nuolat",arial6));
		cell8.setBorderWidth(0);
		table.addCell(cell8);
		



		PdfPCell cell11 = new PdfPCell(new Paragraph("6",arial6));
		cell11.setBorderWidth(0);
		cell11.setPaddingTop(0);
		table.addCell(cell11);

		PdfPCell cell12 = new PdfPCell(new Paragraph("Europos Sàjungos valstybës narës pilieèio ðeimos nario leidimas gyventi",arial6));
		cell12.setBorderWidth(0);
		table.addCell(cell12);
		d.add(table);
		
	}
	public PdfPTable generateTable1(String title,String[] names,int selected)
	throws DocumentException
	{
		Image notSigned = null;
		Image signed = null;
        try{
        	//notSigned= Image.getInstance("notsigned.gif");
        	FileInputStream fr1 = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") + separator+"pdfimages"+separator+"notsigned.gif");
        	byte[] bytes1 = new byte[fr1.available()];
        	fr1.read(bytes1);
        	notSigned = Image.getInstance(bytes1);

        	
        	//signed= Image.getInstance("signed.gif");
        	FileInputStream fr2 = new FileInputStream(this.servlet.getServletConfig().getServletContext().getRealPath("") + separator+"pdfimages"+separator+"signed.gif");
        	byte[] bytes2 = new byte[fr2.available()];
        	fr2.read(bytes2);
        	signed = Image.getInstance(bytes2);
        }
        catch(BadElementException bee){bee.printStackTrace();}        
        catch(IOException ioe){ioe.printStackTrace();}
		
        PdfPTable second1 = new PdfPTable(names.length*2+1);
        second1.setSpacingAfter(0);
		
		PdfPCell cell1 = new PdfPCell(new Paragraph(title,arial6));
		cell1.setNoWrap(true);
		cell1.setPaddingTop(1);
		
		cell1.setBorder(0);
		second1.addCell(cell1);

		PdfPCell cellFlag = null;
		PdfPCell cellValue = null;
		for(int i = 0; i < names.length;i++)
		{
			cellFlag = new PdfPCell(selected == i?signed:notSigned);
			cellFlag.setBorder(0);		
			cellFlag.setPaddingTop(5);
			cellFlag.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			second1.addCell(cellFlag);
			
			cellValue = new PdfPCell(new Paragraph(names[i],arial6));
			cellValue.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cellValue.setBorder(0);
			second1.addCell(cellValue);
		}
		return second1;		
	}
	public PdfPTable generateTable2(String title,String value,boolean two)
	throws DocumentException
	{
		
        PdfPTable second1 = new PdfPTable(50);
		
		PdfPCell cell1 = new PdfPCell(new Paragraph(title,arial7));
		cell1.setNoWrap(true);
		cell1.setPaddingTop(1);
		cell1.setBorder(0);
		second1.addCell(cell1);

		String newLines = "\n";
		if(two)newLines = "\n\n";
		PdfPCell cell2 = new PdfPCell(new Paragraph(newLines+value,arial7));
		cell2.setNoWrap(true);
		cell2.setColspan(49);
		cell2.setBorder(0);
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		second1.addCell(cell2);
		
		return second1;		
	}	
	
	public PdfPTable generateTable3(String title,String value,boolean two) //ju.k 2007.06.26
	throws DocumentException
	{
		
        PdfPTable second1 = new PdfPTable(1);
		
		PdfPCell cell1 = new PdfPCell(new Paragraph(title,arial7));
		cell1.setNoWrap(true);
		cell1.setPaddingTop(1);
		cell1.setBorder(0);
		second1.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Paragraph(value,arial9));
		cell2.setNoWrap(true);
	    cell2.setColspan(2);
		cell2.setBorder(0);
		second1.addCell(cell2);
		
		return second1;		
	}
	
}
