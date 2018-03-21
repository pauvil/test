package com.algoritmusistemos.gvdis.web.utils;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;


/**
 * Pagalbin� s�ra�� r��iavimo klas�
 */
public class Ordering  
{
    private String column;                  // Stulpelis, pagal kur� r��iuojama 
    private String direction = "desc";      // R��iavimo tvarka ("asc" | "desc")
    private String name;                    // Unikalus komponento pavadinimas
    private String urlPrefix;               // R��iavimo nuorod� prefix'as
    
    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra tik vienas s�ra�as, 
     * naudojantis r��iavim�.
     */
    public Ordering() 
    {
        this.name = "";
    }
    
    /**
     * Konstruktorius. Naudotinas tada, kai puslapyje yra keli s�ra�ai, naudojantys 
     * r��iavimus. Tokiu atveju, kiekvien� "Ordering" objekt� reikia kurti su kitokia
     * "name" atributo reik�me.
     * @param name - puslapio ribose unikalus �io komponento pavadinimas
     */
     public Ordering(String name) 
     {
        this.name = name;
     }
     
     /**
      * Inicializuoja komponent� - u�krauna parametr� reik�mes i� URL
      * @param request - request'as, i� kurio u�kraunami r��iavimo parametrai.
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
     * Vidin� proced�ra. Suformuoja prefix'� visoms r��iavimo nuorodoms.
     * @param request - request'as, i� kurio u�kraunami r��iavimo parametrai.
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
     * Gra�ina stulpel�, pagal kur� r��iuojama
     */
    public String getColumn() 
    {
        return column;
    }
    
    /**
     * Nustato stulpel�, pagal kur� r��iuojama
     * @param column - stulpelio pavadinimas
     */
    public void setColumn(String column)
    {
        this.column = column;
    }

    /**
     * Gra�ina r��iavimo tvark� ("asc" | "desc")
     */
    public String getDirection() 
    {
        return column;
    }
    
    /**
     * Nustato r��iavimo tvark�
     * @param direction - r��iavimo tvarka  ("asc" | "desc")
     */
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    /**
     * Gra�ina HTML fragment� su sugeneruotu r��iavimo nuorodos kodu konkre�iam
     * stulpeliui, paruo�t� �terpti � puslap�
     * @param columnTitle - stulpelio antra�t�
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
     * Gra�ina "ORDER BY" r��iavimo sakin�, tinkam� naudoti SQL ar HQL u�klausoje
     * @param tablePrefix - lentel�s prefix'as, kur� reikia �terpti � u�klaus�
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
     * Gra�ina "ORDER BY" r��iavimo sakin�, tinkam� naudoti SQL ar HQL u�klausoje
     */
    public String getOrderString() 
    {
        return getOrderString(null);    
    }
}