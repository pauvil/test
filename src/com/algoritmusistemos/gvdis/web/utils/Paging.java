package com.algoritmusistemos.gvdis.web.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;




/**
 * S�ra�� puslapiavimo klas�
 */
public class Paging  
{
    public static final int PAGE_SIZE = 10;      // Maksimalus �ra�� skai�ius s�ra�e
    private String name;                        // Unikalus komponento pavadinimas
    private int page = 0;                       // Dabartinis puslapis
    private int totalNumber = 0;                // Viso �ra�� skai�ius s�ra�e
    private String url;                         // Puslapiavimo nuorod� prefix'as
    private int pageSize;

    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra tik vienas s�ra�as, 
     * naudojantis puslapiavim�.
     */
    public Paging()
    {
        this.name = "";
    }
    
    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra keli s�ra�ai, naudojantys 
     * puslapiavimus. Tokiu atveju, kiekvien� "Paging" objekt� reikia kurti su kitokia
     * "name" atributo reik�me.
     * @param name - puslapio ribose unikalus �io komponenteo pavadinimas
     */
    public Paging(String name) 
    {
        this.name = name;
    }

    /**
     * Inicializuoja komponent� - u�krauna parametr� reik�mes i� URL
     * @param request - request'as, i� kurio u�kraunami puslapiavimo parametrai.
     */
    public void init(HttpServletRequest request) 
    {
        try {
            pageSize = PAGE_SIZE;
            page = Integer.parseInt(request.getParameter(name + "_page"));
        }
        catch (NumberFormatException nfe){
        }      
        parseUrl(request);
        
        if (request.getAttribute("printVersion") != null){
            pageSize = Integer.MAX_VALUE;
            page = 0;
        }
    }
    
    /**
     * Vidin� proced�ra. Suformuoja prefix'� visoms puslapiavimo nuorodoms.
     * @param request - request'as, i� kurio u�kraunami puslapiavimo parametrai.
     */
    private void parseUrl(HttpServletRequest request) 
    {
        url = request.getRequestURI() + "?";// + request.getQueryString();
        for (Enumeration e=request.getParameterNames(); e.hasMoreElements();){
            String paramName = (String)e.nextElement();
            if (!paramName.equals(name + "_page")) try {
            	String[] values = request.getParameterValues(paramName); 
            	if (values != null && values.length > 0){
            		for (int i=0; i<values.length; i++){
                		url += "&" + paramName + "=" + URLEncoder.encode(values[i], "Cp1257");
            		}
            	}
            	else {
            		url += "&" + paramName + "=" + URLEncoder.encode(request.getParameter(paramName), "Cp1257");
            	}
            }
            catch (UnsupportedEncodingException uee){
            }
        }
    }
    
    /**
     * Gra�ina unikal� �io komponento pavadinim�
     */
    public String getName() 
    {
        return name;    
    }
    
    /**
     * Nustato unikal� �io komponento pavadinim�
     * @param name - naujas pavadinimas
     */
    public void setName(String name)
    {
        this.name = name;    
    }
    
    /**
     * Gra�ina piln� �ra�� skai�i� puslapiuojame s�ra�e.
     */
    public int getTotalNumber() 
    {
        return totalNumber;    
    }

    /**
     * Nustato piln� �ra�� skai�i� puslapiuojamame s�ra�e.
     * Turi b�ti i�kviestas prie� kvie�iant "printPaging"
     * @param totalNumber - �ra�� skai�ius puslapiuojamame s�ra�e
     */
    public void setTotalNumber(int totalNumber) 
    {
        this.totalNumber = totalNumber;    
    }
    
    /**
     * Gra�ina �ra�� skai�i� puslapyje
     */
    public int getPageSize() 
    {
        return pageSize;
    }
    
    /**
     * Nustato  �ra�� skai�i� viename puslapyje
     * @param pageSize - naujas �ra�� skai�ius puslapyje
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Gra�ina dabartin� puslap�
     */
    public int getPage() 
    {
        return page;
    }
    
    /**
     * Nustato dabartin� puslap�
     * @param pageSize - dabartinis puslapis
     */
    public void setPage(int page)
    {
        this.page = page;
    }

    /**
     * Gra�ina HTML fragment� su sugeneruotu puslapiavimo kodu, paruo�t� 
     * �terpti � puslap�
     */
    public String printPaging()
    {
        StringBuffer out = new StringBuffer();
        String link = url + "&" + name + "_page" + "=";
        
        int step = 1 + totalNumber / (pageSize * 6);
        int last = totalNumber / pageSize;
        if ((double)last < (double)totalNumber / (double)pageSize) last++;
        if (totalNumber <= pageSize){
            out.append("[Viso:&nbsp;" + totalNumber + "]");
        	return out.toString();
        }
        boolean pages[] = new boolean[last + 1];
        
        for(int i = 0; i < last; i++){
            pages[i] = false;
        }

        pages[0] = true;
        pages[last - 1] = true;
        pages[last] = true;
        for (int i = 0; i < last; i++){
            if (i % step == 0){
                pages[i] = true;
            }
        }

        for (int i = page - 2; i < page + 3; i++){
            if (i > 0 && i < last){
                pages[i] = true;
            }
        }

        String link2 = link + (page - 1);
        if (page == 0){
            out.append("");
        }
        else {
            out.append("<a href=\"" + link2 + "\">&laquo;</a>");
        }
        for (int i = 0; i < last; i++) {
            int pageNumber = i + 1;
            if (pages[i] && !pages[i + 1]) {
                link2 = link + i;
                out.append(" <a href=\" " + link2 + "\">" + pageNumber + "</a> &middot;&middot;&middot;");
            } 
            else if (pages[i]){
                if (i == page){
                    out.append(" <b>" + (i + 1) + "</b>");
                } 
                else {
                    link2 = link + i;
                    out.append(" <a href=\"" + link2 + "\"> " + (i + 1) + "</a>");
                }
            }
        }

        link2 = link + (page + 1);
        if (last != page + 1){
            out.append(" <a href=\"" + link2 + "\">&raquo;</a>");
        }
        out.insert(0, "<span>");
        out.append("</span>");
        out.append("<br />[Viso:&nbsp;" + totalNumber + "]");
        return out.toString();
    }
    
    /**
     * Gra�ina elemento, nuo kurio pradedamas puslapiavimas, eil�s numer� s�ra�e
     */
    public int getFirstItem() 
    {
        return page * pageSize;
    }    
}