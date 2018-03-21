package com.algoritmusistemos.gvdis.web.delegators;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AprDbObjektas;
import com.algoritmusistemos.gvdis.web.persistence.AprMeniuGrupe;
import com.algoritmusistemos.gvdis.web.persistence.AprModulis;
import com.algoritmusistemos.gvdis.web.persistence.AprModulisJN;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class AprasymaiDelegator {

	public static AprasymaiDelegator instance;

	public AprasymaiDelegator() {
	}

	public static AprasymaiDelegator getInstance() {
		if (instance == null)
			instance = new AprasymaiDelegator();
		return instance;
	}

	/**
	 * Meniu grupiø sàraðas
	 */
	public List getMeniuGrupesList(HttpServletRequest req) {
		List retVal;
		retVal = HibernateUtils.currentSession(req)
				.createQuery("from AprMeniuGrupe gr").list();
		return retVal;
	}

	/**
	 * Meniu punktø sàraðas
	 */
	public List getMeniuPunktaiList(HttpServletRequest req, long grupeId) {
		List retVal;
		retVal = HibernateUtils
				.currentSession(req)
				.createQuery("from AprMeniuPunktas pn where meniuGrupeId = :grupeId")
				.setParameter("grupeId", new Long(grupeId)).list();
		return retVal;
	}

	/**
	 * Meniu punktø sàraðas
	 */
	public List getMeniuPunktaiList(HttpServletRequest req) {
		List retVal;
		retVal = HibernateUtils.currentSession(req)
				.createQuery("from AprMeniuPunktas pn").list();
		return retVal;
	}

	/**
	 * modulis
	 */
	public AprModulis getAprModulis(HttpServletRequest req, long meniuId) {
		//return (AprModulis) HibernateUtils.currentSession(req).get(AprModulis.class, new Long(id));
		return (AprModulis)HibernateUtils.currentSession(req).
		createQuery("from AprModulis mod where mod.meniuPunktasId = :meniuId").
		setLong("meniuId",meniuId).uniqueResult();
	}

	/**
	 * Duomenø bazës objektø sàraðas
	 */

	public List getDbObjektai(HttpServletRequest req, long id) {
		List retVal;

		retVal = HibernateUtils.currentSession(req)
				.createQuery("from AprDbObjektas obj where modulisId = :modulisId ").setParameter("modulisId", new Long(id)).list();

		return retVal;
	}

	public void saveAprasymas(HttpServletRequest req, AprModulis aprModulis)
			throws DatabaseException {
		Session session = HibernateUtils.currentSession(req);
		Transaction tx = session.beginTransaction();

		try {
			session.save(aprModulis);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Graþina db objekto apraðymà su duotu ID
	 */
	public AprDbObjektas getAprDbObjektas(HttpServletRequest req, long id) {
		return (AprDbObjektas) HibernateUtils.currentSession(req).get(
				AprDbObjektas.class, new Long(id));
	}

	
	public void saveAprDbObjektas(HttpServletRequest req, AprDbObjektas ob)
			throws DatabaseException {
		Session session = HibernateUtils.currentSession(req);
		Transaction tx = session.beginTransaction();
		try {
			session.save(ob);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new DatabaseException(e);
		}

	}
	
	public void deleteDbObjectDescription(HttpServletRequest req, long id)
			throws DatabaseException {
		AprDbObjektas db = this.getAprDbObjektas(req, id);
		Session session = HibernateUtils.currentSession(req);
		Transaction tx = session.beginTransaction();
		try {
			session.delete(db);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new DatabaseException(e);
		}
	}
	
	public void addDbObjectDescription(HttpServletRequest request, long modulisId, String objektas, String parametrai, String tipas, String schema, String komentarai) 
			throws DatabaseException {

		Session session = HibernateUtils.currentSession(request);
		Transaction tx = session.beginTransaction();

		AprDbObjektas db = new AprDbObjektas();

		try {
			db.setModulisId(new Long(modulisId));
			db.setObjektas(objektas);
			db.setParametrai(parametrai);
			db.setTipas(tipas);
			db.setSchema(schema);
			db.setKomentarai(komentarai);

			session.persist(db);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw new DatabaseException(e);
		}

	}
	
	/**
	 * Versijos
	 */
	public List getAprasymoVersijosList(HttpServletRequest req, long id) {
		List retVal;
		retVal = HibernateUtils
				.currentSession(req)
				.createQuery("from AprVersija v where modulisId = :id")
				.setParameter("id", new Long(id)).list();
		return retVal;
	}
	
	
	public List getDbObjektaiJN(HttpServletRequest req, long modulisId, long versijaId) {
		List retVal;

		retVal = HibernateUtils.currentSession(req)
				.createQuery("from AprDbObjektasJN obj where modulisId = :modulisId and versijaId = :versijaId").setParameter("modulisId", new Long(modulisId)).setParameter("versijaId", new Long(versijaId)).list();

		return retVal;
	}
	
	public List getAprModulisJN(HttpServletRequest req, long modulisId, long versijaId) {
		return HibernateUtils.currentSession(req).createQuery("from AprModulisJN mod where modulisId = :modulisId and versijaId = :versijaId").setParameter("modulisId", new Long(modulisId)).setParameter("versijaId", new Long(versijaId)).list();
	}
	
}
