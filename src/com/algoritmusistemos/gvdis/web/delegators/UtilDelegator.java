package com.algoritmusistemos.gvdis.web.delegators;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.hibernate.Session;

import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.Valstybe;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;


public class UtilDelegator
{
	private static UtilDelegator instance;
	
	public static UtilDelegator getInstance() 
	{
		if (instance == null){
			instance =  new UtilDelegator();
		}
		return instance;
	}	

	public static void setError(String error,ActionErrors errors,HttpServletRequest request) 
    {
		errors.add("title", new ActionMessage(error));
		request.setAttribute(error, "");
    }
	
	public static String trim(String string, int length)
	{
		if (string == null){
			return null;
		}
		else if (length >= string.length()){
			return string;
		}
		else {
			return string.substring(0, length);
		}
	}
	
	public List getValstybes(HttpServletRequest request)
	{
		List l = HibernateUtils.currentSession(request).createQuery("from Valstybe v order by v.pavadinimas").list();
		return l;
	}
	public List getValstybesBeLietuvos(HttpServletRequest request)
	{
		List l = HibernateUtils.currentSession(request).createQuery("from Valstybe v where lower(v.pavadinimas) not like 'lietuva' order by v.pavadinimas").list();
		return l;
	}
	
	public List getPilietybes(HttpServletRequest request)
	{
		List l = getPilietybesBeNull(request);
  	    Valstybe v = new Valstybe();
	    v.setKodas("-1");
  	    v.setPavadinimas("");
	    v.setPilietybe("");
	    l.add(0, v);
		return l;
	}
	public List getPilietybesBeNull(HttpServletRequest request)
	{
		List l = HibernateUtils.currentSession(request).createQuery("from Valstybe v where v.pilietybe is not null order by v.pilietybe").list();
		return l;
	}
	
	
	public Valstybe getValstybe(String id,HttpServletRequest request)
	{
		Valstybe v = (Valstybe)HibernateUtils.currentSession(request).load(Valstybe.class,id);
		return v;
	}
	
	public Valstybe getValstybe(String id,Session session)
	{
		Valstybe v = (Valstybe)session.load(Valstybe.class,id);
		return v;
	}
	
    /**
     * Gra�ina �inyno reik�m� su duotu ID
     * @param id - rei�m�s ID
     * @throws DocumentNotFoundException - jei n�ra tokios reik�m�s
     */
    public ZinynoReiksme getZinynoReiksme(long id,HttpServletRequest request)
    {
    	return (ZinynoReiksme)HibernateUtils.currentSession(request).load(ZinynoReiksme.class, new Long(id));
    }
    /**
     * Gra�ina �inyno reik�m� su duotu ID
     * @param id - rei�m�s ID
     * @throws DocumentNotFoundException - jei n�ra tokios reik�m�s
     */
    public Set getZinynoReiksmes(String name,HttpServletRequest request)
    	throws ObjectNotFoundException
    {
    	Zinynas zin = (Zinynas)HibernateUtils.currentSession(request).createQuery(
		"from Zinynas zin where zin.kodas = :kodas").setString("kodas", name).uniqueResult();
		if (zin == null){
			throw new ObjectNotFoundException("�inynas su kodu " + name + " neregistruotas sistemoje");
		}
    	if (zin == null)return null;
    	return zin.getZinynoReiksmes();
    }   
    /**
     * Gra�ina �inyno reik�m� su duotu ID
     * @param id - rei�m�s ID
     * @throws DocumentNotFoundException - jei n�ra tokios reik�m�s
     */
    public ZinynoReiksme getZinynoReiksme(String value,HttpServletRequest request)
	throws ObjectNotFoundException    
    {
    	ZinynoReiksme zr = (ZinynoReiksme)HibernateUtils.currentSession(request).createQuery(
		"from ZinynoReiksme zr where zr.pavadinimas = :value").setString("value", value).uniqueResult();
    	if(null != zr) return zr;
    	else throw new ObjectNotFoundException("ZinynoReiksme su pavadinimu " + value + " neregistruotas sistemoje");
    }	
	public List getIstaigos(HttpServletRequest request)
	{
		List l = HibernateUtils.currentSession(request).createQuery("from Istaiga i order by i.pavadinimas").list();
		return l;
	}	
	public Istaiga getIstaiga(long id,HttpServletRequest request)
	{
		Istaiga l = (Istaiga)HibernateUtils.currentSession(request).createQuery(
		"from Istaiga ist where ist.id = :id").setLong("id", id).uniqueResult();
		return l;
	}	
}
