package com.algoritmusistemos.gvdis.web.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;




/**
 * Sàrağø puslapiavimo klasë
 */
public class Paging  
{
    public static final int PAGE_SIZE = 10;      // Maksimalus árağø skaièius sàrağe
    private String name;                        // Unikalus komponento pavadinimas
    private int page = 0;                       // Dabartinis puslapis
    private int totalNumber = 0;                // Viso árağø skaièius sàrağe
    private String url;                         // Puslapiavimo nuorodø prefix'as
    private int pageSize;

    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra tik vienas sàrağas, 
     * naudojantis puslapiavimà.
     */
    public Paging()
    {
        this.name = "";
    }
    
    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra keli sàrağai, naudojantys 
     * puslapiavimus. Tokiu atveju, kiekvienà "Paging" objektà reikia kurti su kitokia
     * "name" atributo reikğme.
     * @param name - puslapio ribose unikalus ğio komponenteo pavadinimas
     */
    public Paging(String name) 
    {
        this.name = name;
    }

    /**
     * Inicializuoja komponentà - uşkrauna parametrø reikğmes iğ URL
     * @param request - request'as, iğ kurio uşkraunami puslapiavimo parametrai.
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
     * Vidinë procedûra. Suformuoja prefix'à visoms puslapiavimo nuorodoms.
     * @param request - request'as, iğ kurio uşkraunami puslapiavimo parametrai.
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
     * Graşina unikalø ğio komponento pavadinimà
     */
    public String getName() 
    {
        return name;    
    }
    
    /**
     * Nustato unikalø ğio komponento pavadinimà
     * @param name - naujas pavadinimas
     */
    public void setName(String name)
    {
        this.name = name;    
    }
    
    /**
     * Graşina pilnà árağø skaièiø puslapiuojame sàrağe.
     */
    public int getTotalNumber() 
    {
        return totalNumber;    
    }

    /**
     * Nustato pilnà árağø skaièiø puslapiuojamame sàrağe.
     * Turi bûti iğkviestas prieğ kvieèiant "printPaging"
     * @param totalNumber - árağø skaièius puslapiuojamame sàrağe
     */
    public void setTotalNumber(int totalNumber) 
    {
        this.totalNumber = totalNumber;    
    }
    
    /**
     * Graşina árağø skaièiø puslapyje
     */
    public int getPageSize() 
    {
        return pageSize;
    }
    
    /**
     * Nustato  árağø skaièiø viename puslapyje
     * @param pageSize - naujas árağø skaièius puslapyje
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Graşina dabartiná puslapá
     */
    public int getPage() 
    {
        return page;
    }
    
    /**
     * Nustato dabartiná puslapá
     * @param pageSize - dabartinis puslapis
     */
    public void setPage(int page)
    {
        this.page = page;
    }

    /**
     * Graşina HTML fragmentà su sugeneruotu puslapiavimo kodu, paruoğtà 
     * áterpti á puslapá
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
     * Graşina elemento, nuo kurio pradedamas puslapiavimas, eilës numerá sàrağe
     */
    public int getFirstItem() 
    {
        return page * pageSize;
    }    
}