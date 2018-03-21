package com.algoritmusistemos.gvdis.web.delegators;

import javax.servlet.http.HttpServletRequest;
import org.hibernate.Hibernate;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class ParameterDelegator 
{
	private static ParameterDelegator instance;
    
	private ParameterDelegator()
    {
    }

	public static ParameterDelegator getInstance() 
	{
		if (instance == null) {
            instance = new ParameterDelegator();
		}
		return instance;
	}	

    /**
     * Graþina skaitinio parametro su nurodytu kodiniu pavadinimu reikðmæ
     * @param paramName - parametro kodas
     */
    public int getNumberParameter(HttpServletRequest req, String paramName)
    {
    	Constants.Println(req, "1paramName: "+paramName);
		Integer result = (Integer)HibernateUtils.currentSession(req).createSQLQuery(
            "SELECT par_reiksme AS value FROM gvdisvw_ad_parametrai where par_kodas=?"
        ).addScalar("value", Hibernate.INTEGER).setString(0, paramName).uniqueResult();
        return result.intValue();
    }
    
    /**
     * Graþina eilutës tipo parametro su nurodytu kodiniu pavadinimu reikðmæ
     * @param paramName - parametro kodas
     */
    public String getStringParameter(HttpServletRequest req, String paramName) 
    {
    	Constants.Println(req, "2paramName: "+paramName);
		String result = (String)HibernateUtils.currentSession(req).createSQLQuery(
            "SELECT par_reiksme AS value FROM  gvdisvw_ad_parametrai where par_kodas=?"
        ).addScalar("value", Hibernate.STRING).setString(0, paramName).uniqueResult();
        return result;
    }
}