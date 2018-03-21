package com.algoritmusistemos.gvdis.web.delegators;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;
import com.algoritmusistemos.gvdis.web.utils.Ordering;
import com.algoritmusistemos.gvdis.web.utils.Paging;

public class ZinynaiDelegator
{
    private static ZinynaiDelegator instance;

    public ZinynaiDelegator()
    {
    }

    public static ZinynaiDelegator getInstance()
    {
        if(instance == null)
            instance = new ZinynaiDelegator();
        return instance;
    }

    /**
     * Graþina þinynà su duotu ID
     */
    public Zinynas getZinynas(HttpServletRequest req, long id)
    {
    	return (Zinynas)HibernateUtils.currentSession(req).load(Zinynas.class, new Long(id));
    }

    /**
     * Graþina þinyno reikðmæ su duotu ID
     */
    public ZinynoReiksme getZinynoReiksme(HttpServletRequest req, long id)
    {
    	return (ZinynoReiksme)HibernateUtils.currentSession(req).load(ZinynoReiksme.class, new Long(id));
    }
    /**
     * Graþina þinyno reikðmæ su duotu pavadinimu
     */
    public ZinynoReiksme getZinynoReiksmeByPavadinimas(HttpServletRequest req, String pavadinimas)
    {
    	return (ZinynoReiksme)HibernateUtils.
    	currentSession(req).createQuery("from ZinynoReiksme zr where zr.pavadinimas = :pavadinimas").
    	setString("pavadinimas",pavadinimas).uniqueResult();
    }
    /**
     * Graþina visø registruotø sisteminiø þinynø sàraðà
     */
    public List getZinynai(HttpServletRequest req, Paging paging, Ordering ordering)
    {
    	Integer count = (Integer)HibernateUtils.currentSession(req).createQuery("select count(*) from Zinynas").uniqueResult();
    	paging.setTotalNumber(count.intValue());
    	List retVal = HibernateUtils.currentSession(req)
    		.createQuery("from Zinynas " + ordering.getOrderString())
    		.setFirstResult(paging.getFirstItem())
    		.setMaxResults(paging.getPageSize())
    		.list();
    	return retVal.isEmpty() ? null : retVal;
    }

    /**
     * Graþina visas duoto þinyno reikðmes 
     * @param zinynoKodas - þinymno kodas
     * @throws ObjectNotFoundException - jei tokio þinyno nëra
     */
    public Set getZinynoReiksmes(HttpServletRequest req, String zinynoKodas)
        throws ObjectNotFoundException
    {
        Zinynas zin = (Zinynas)HibernateUtils.currentSession(req).createQuery("from Zinynas zin where zin.kodas = :kodas").setString("kodas", zinynoKodas).uniqueResult();
        if(zin == null){
            throw new ObjectNotFoundException("Þinynas su kodu " + zinynoKodas + " neregistruotas sistemoje");
        }
        else {
            return zin.getZinynoReiksmes();
        }
    }

    /**
     * Graþina visø registruotø ástaigø sàraðà
     */
    public List getIstaigos(HttpServletRequest req, Paging paging, Ordering ordering)
    {
    	Integer count = (Integer)HibernateUtils.currentSession(req).createQuery("select count(*) from Istaiga").uniqueResult();
    	paging.setTotalNumber(count.intValue());
    	List retVal = HibernateUtils.currentSession(req)
    		.createQuery("from Istaiga ist " + ordering.getOrderString())
    		.setFirstResult(paging.getFirstItem())
    		.setMaxResults(paging.getPageSize())
    		.list();
    	return retVal.isEmpty() ? null : retVal;
    }

    /**
     * Iðsaugo duotos ástaigos duomenis DB
     * @param istaiga
     */
    public void saveIstaiga(HttpServletRequest req, Istaiga istaiga)
    	throws DatabaseException
    {
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	try {
    		session.save(istaiga);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
    }

    /**
     * Iðsaugo duotos þinyno reikðmës duomenis DB
     * @param istaiga
     */
    public void saveZinynoReiksme(HttpServletRequest req, ZinynoReiksme zr)
    	throws DatabaseException
    {
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	try {
    		session.save(zr);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
    }
    
    /**
     * Iðtrina duotà þinyno reikðmæ
     * @throws DatabaseException
     */
    public void deleteZinynoReiksme(HttpServletRequest req, long id)
    	throws DatabaseException
    {
    	ZinynoReiksme zr = this.getZinynoReiksme(req, id);
    	Session session = HibernateUtils.currentSession(req);
    	Transaction tx = session.beginTransaction();
    	try {
    		session.delete(zr);
    		tx.commit();
    	}
    	catch (Exception e){
    		tx.rollback();
    		throw new DatabaseException(e);
    	}
    }
}
