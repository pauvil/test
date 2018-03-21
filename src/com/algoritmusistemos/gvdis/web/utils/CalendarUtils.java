package com.algoritmusistemos.gvdis.web.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.algoritmusistemos.gvdis.web.Constants;

public class CalendarUtils 
{
	public static Timestamp getParsedDate(String metai,String menuo,String data)
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	    dateFormat.setLenient(false);
	    Timestamp date = null;
	    try {
	    	date = new Timestamp(dateFormat.parse(metai+"-"+menuo+"-"+data).getTime()); 
	    }
	    catch (ParseException pe){
	    	//no message, it's no mistake
	    } 
		return date;
	}
	
	public static String[] getDateFromTimestamp(Timestamp data)
	{
		String [] ret = new String[3];
		if(null == data)
		{
			ret[0] = "";ret[1] = "";ret[2] = "";			
		}
		else
		{
			ret[0] = (new SimpleDateFormat("yyyy")).format(data);
			ret[1] = (new SimpleDateFormat("MM")).format(data);		
			ret[2] = (new SimpleDateFormat("dd")).format(data);
		}
		return ret;
	}
	
	public static Timestamp string2Timestamp (String value) throws ParseException {
		return new Timestamp(getDateFormat().parse(value).getTime());
	}
	
	public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        df.setLenient(false);
        return df;
    }
}
