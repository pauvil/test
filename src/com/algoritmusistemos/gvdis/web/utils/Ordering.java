package com.algoritmusistemos.gvdis.web.utils;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;


/**
 * Pagalbinë sàraðø rûðiavimo klasë
 */
public class Ordering  
{
    private String column;                  // Stulpelis, pagal kurá rûðiuojama 
    private String direction = "desc";      // Rûðiavimo tvarka ("asc" | "desc")
    private String name;                    // Unikalus komponento pavadinimas
    private String urlPrefix;               // Rûðiavimo nuorodø prefix'as
    
    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra tik vienas sàraðas, 
     * naudojantis rûðiavimà.
     */
    public Ordering() 
    {
        this.name = "";
    }
    
    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra keli sàraðai, naudojantys 
     * rûðiavimus. Tokiu atveju, kiekvienà "Ordering" objektà reikia kurti su kitokia
     * "name" atributo reikðme.
     * @param name - puslapio ribose unikalus ðio komponento pavadinimas
     */
     public Ordering(String name) 
     {
        this.name = name;
     }
     
     /**
      * Inicializuoja komponentà - uþkrauna parametrø reikðmes ið URL
      * @param request - request'as, ið kurio uþkraunami rûðiavimo parametrai.
      */
     public void init(HttpServletRequest request) 
     {
        if (request.getParameter(name + "_orderby") != null){
            column = request.getParameter(name + "_orderby");
        }
        if (request.getParameter(name + "_orderdir") != null){
            direction = request.getParameter(name + "_orderdir");
            if(! ("asc".equals(direction) || "desc".equals(direction)))direction = "asc";
            
        }
        parseUrl(request);      
     }
     
    /**
     * Vidinë procedûra. Suformuoja prefix'à visoms rûðiavimo nuorodoms.
     * @param request - request'as, ið kurio uþkraunami rûðiavimo parametrai.
     */
    private void parseUrl(HttpServletRequest request) 
    {
        urlPrefix = request.getRequestURI() + "?";
        for (Enumeration e=request.getParameterNames(); e.hasMoreElements();){
            String paramName = (String)e.nextElement();
            if (!paramName.equals(name + "_orderby") && !paramName.equals(name + "_orderdir")){
            	String[] values = request.getParameterValues(paramName); 
            	if (values != null && values.length > 0){
            		for (int i=0; i<values.length; i++){
                		urlPrefix += "&" + paramName + "=" + values[i];
            		}
            	}
            	else {
            		urlPrefix += "&" + paramName + "=" + request.getParameter(paramName);
            	}
            }
        }
    }
    
    /**
     * Graþina unikalø ðio komponento pavadinimà
     */
    public String getName() 
    {
        return name;    
    }
    
    /**
     * Nustato unikalø ðio komponento pavadinimà
     * @param name - naujas pavadinimas
     */
    public void setName(String name)
    {
        this.name = name;    
    }

    /**
     * Graþina stulpelá, pagal kurá rûðiuojama
     */
    public String getColumn() 
    {
        return column;
    }
    
    /**
     * Nustato stulpelá, pagal kurá rûðiuojama
     * @param column - stulpelio pavadinimas
     */
    public void setColumn(String column)
    {
        this.column = column;
    }

    /**
     * Graþina rûðiavimo tvarkà ("asc" | "desc")
     */
    public String getDirection() 
    {
        return column;
    }
    
    /**
     * Nustato rûðiavimo tvarkà
     * @param direction - rûðiavimo tvarka  ("asc" | "desc")
     */
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    /**
     * Graþina HTML fragmentà su sugeneruotu rûðiavimo nuorodos kodu konkreèiam
     * stulpeliui, paruoðtà áterpti á puslapá
     * @param columnTitle - stulpelio antraðtë
     * @param columnName - stulpelio pavadinimas
     */
    public String printOrdering(String columnName, String columnTitle) 
    {
        StringBuffer out = new StringBuffer();
        String image;
        out.append("<a href=\"");
        out.append(urlPrefix);
        out.append("&" + name + "_orderby=" + columnName);
        if (column != null && column.equals(columnName)){
            if (direction != null && direction.equals("desc")){
                out.append("&" + name + "_orderdir=asc");
                image = "desc.gif";
            }
            else {                
                out.append("&" + name + "_orderdir=desc");
                image = "asc.gif";
            }
        }
        else {
            out.append("&" + name + "_orderdir=desc");            
            image = null;
        }
        out.append("\">");
        out.append(columnTitle);
        out.append("</a>");
        if (image != null){
            out.append("<img src=\"../img/");
            out.append(image);
            out.append("\" align=\"top\"/>");
        }
        
        return out.toString();
    }

    /**
     * Graþina "ORDER BY" rûðiavimo sakiná, tinkamà naudoti SQL ar HQL uþklausoje
     * @param tablePrefix - lentelës prefix'as, kurá reikia áterpti á uþklausà
     */
    public String getOrderString(String tablePrefix) 
    {
        StringBuffer out = new StringBuffer();
        try{
	        if (direction == null){
	            direction = "desc";
	        }
	        if (column != null){
	            out.append(" ORDER BY ");
	            if (tablePrefix != null){
	                out.append(tablePrefix + ".");
	            }
		        String column_order = column.replaceAll(",", " "+direction+",");

	            out.append(column_order);
	            out.append(" ");
	            out.append(direction);

	        }
        } catch (Exception e){
	        System.out.println("order exception");
	        e.printStackTrace();
        }
	        
        return out.toString();
    }
    
    /**
     * Graþina "ORDER BY" rûðiavimo sakiná, tinkamà naudoti SQL ar HQL uþklausoje
     */
    public String getOrderString() 
    {
        return getOrderString(null);    
    }
}