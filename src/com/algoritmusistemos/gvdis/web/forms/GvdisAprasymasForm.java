package com.algoritmusistemos.gvdis.web.forms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.AprMeniuGrupe;
import com.algoritmusistemos.gvdis.web.persistence.AprModulis;



public class GvdisAprasymasForm extends ActionForm {

	private static final long serialVersionUID = 1L;


		
	private Long aprasymasId;
	private Long meniuGrupesKodas;
	private Long meniuPunktoKodas;
	private Long modulioId;
	private boolean redaguotiAprasyma;
	private boolean redaguotiLogAprasyma;
	private String aprasymas;
	private String logikosAprasymas;
	
	private String redaguotiAprasymaButton;
	private String issaugotiAprasymaButton;
	private String issaugotiNaujaAprasymaButton;	

	public Long getAprasymasId() {
		return aprasymasId;
	}

	public void setAprasymasId(Long aprasymasId) {
		this.aprasymasId = aprasymasId;
	}

	public Long getMeniuGrupesKodas() {
		return meniuGrupesKodas;
	}

	public void setMeniuGrupesKodas(Long meniuGrupesKodas) {
		this.meniuGrupesKodas = meniuGrupesKodas;
	}

	public Long getMeniuPunktoKodas() {
		return meniuPunktoKodas;
	}

	public void setMeniuPunktoKodas(Long meniuPunktoKodas) {
		this.meniuPunktoKodas = meniuPunktoKodas;
	}

	public boolean isRedaguotiAprasyma() {
		return redaguotiAprasyma;
	}

	public void setRedaguotiAprasyma(boolean redaguotiAprasyma) {
		this.redaguotiAprasyma = redaguotiAprasyma;
	}

	public boolean isRedaguotiLogAprasyma() {
		return redaguotiLogAprasyma;
	}

	public void setRedaguotiLogAprasyma(boolean redaguotiLogAprasyma) {
		this.redaguotiLogAprasyma = redaguotiLogAprasyma;
	}

	public String getAprasymas() {
		return aprasymas;
	}

	public void setAprasymas(String aprasymas) {
		this.aprasymas = aprasymas;
	}
	
	public String getLogikosAprasymas() {
		return logikosAprasymas;
	}

	public void setLogikosAprasymas(String logikosAprasymas) {
		this.logikosAprasymas = logikosAprasymas;
	}
	
	public String getRedaguotiAprasymaButton() {
		return redaguotiAprasymaButton;
	}

	public void setRedaguotiAprasymaButton(String redaguotiAprasymaButton) {
		this.redaguotiAprasymaButton = redaguotiAprasymaButton;
	}

	public String getIssaugotiAprasymaButton() {
		return issaugotiAprasymaButton;
	}

	public void setIssaugotiAprasymaButton(String issaugotiAprasymaButton) {
		this.issaugotiAprasymaButton = issaugotiAprasymaButton;
	}

	public String getIssaugotiNaujaAprasymaButton() {
		return issaugotiNaujaAprasymaButton;
	}

	public void setIssaugotiNaujaAprasymaButton(String issaugotiNaujaAprasymaButton) {
		this.issaugotiNaujaAprasymaButton = issaugotiNaujaAprasymaButton;
	}

	public Long getModulioId() {
		return modulioId;
	}

	public void setModulioId(Long modulioId) {
		this.modulioId = modulioId;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{	
		
	}	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		return errors;

	}
	
}
