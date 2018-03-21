package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.SprendimasForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;

public class CreateSprendimasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		SprendimasForm sForm = (SprendimasForm)form;
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE);
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		SprendimasKeistiDuomenis sprendimas = new SprendimasKeistiDuomenis();
		// PrasymasKeistiDuomenis prasymas = null;
		GyvenamojiVieta gvt = new GyvenamojiVieta();
		long istaigaId = ((Long)session.getAttribute("userIstaigaId")).longValue();
		
		try {
			sprendimas.setRegNr(sForm.getRegNr());
			sprendimas.setTipas(Long.parseLong(sForm.getTipas()));
			sprendimas.setData(new Timestamp(sdf.parse(sForm.getData()).getTime()));

			if (sForm.getPriezastis().equals("35") || UserDelegator.chkPermission(request, "RL_GVDIS_GL_TVARK") ) {
				sprendimas.setNaikinimoData(new Timestamp(sdf.parse(sForm.getNaikinimoData()).getTime()));
			} else {
				sprendimas.setNaikinimoData(new Timestamp(sdf.parse(sForm.getData()).getTime()));
			}

			//Constants.Println(request, "sprendimas.getNaikinimoData " + sprendimas.getNaikinimoData());
			//Constants.Println(request, "sprendimas.getData " + sprendimas.getData());

			sprendimas.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId, request));
			sprendimas.setPriezastis(ZinynaiDelegator.getInstance().getZinynoReiksme(request, Long.parseLong(sForm.getPriezastis())));
			sprendimas.setPastabos(sForm.getPastabos());
			sprendimas.setPrieme(sForm.getPrieme());

			
			
			for (int i=0; i<sForm.getAsmenys().length; i++){
				long id = Long.parseLong(sForm.getAsmenys()[i]);
				Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
				sprendimas.getAsmenys().add(asmuo);
			}
			
			gvt.setGvtAdvNr("".equals(sForm.getAddressAdr()) ? null : Long.valueOf(sForm.getAddressAdr()));
			Constants.Println(request, sForm.getGvtKampoNr());
			if (sForm.getGvtKampoNr() == null || sForm.getGvtKampoNr().equals("")) gvt.setGvtKampoNr(null);
			else gvt.setGvtKampoNr(Long.valueOf(sForm.getGvtKampoNr()));
			gvt.setGvtKampoNr("".equals(sForm.getGvtKampoNr()) ? null : Long.valueOf(sForm.getGvtKampoNr()));
			gvt.setGvtAtvNr("".equals(sForm.getAddressTer()) ? null : Long.valueOf(sForm.getAddressTer()));
			
			if (sForm.getPriezastis().equals("35") || UserDelegator.chkPermission(request, "RL_GVDIS_GL_TVARK")) /* TEISMO SPRENDIMU - 35*/ {
				gvt.setGvtDataNuo(sprendimas.getNaikinimoData());
			}
			else {
				gvt.setGvtDataNuo(sprendimas.getData());
			}
			
			gvt.setGvtTipas(sprendimas.getTipas() == 2 ? "I" : (sForm.getAddressType().equals("0") ? "R" : "V"));
			gvt.setValstybe(UtilDelegator.getInstance().getValstybe(sForm.getAddressType().equals("0") ? QueryDelegator.HOME_COUNTRY : sForm.getValstybe(), request));
			gvt.setGvtAdrUzsenyje(sForm.getAddressType().equals("0") ? null : sForm.getValstybePast());
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		

		Long prasymasId = null;
		if(sForm.getPrasymas() != null && !"".equals(sForm.getPrasymas()))
			prasymasId= new Long(sForm.getPrasymas());
		if (session.getAttribute("actPrasymasId") != null && prasymasId == null ){
			prasymasId = (Long)session.getAttribute("actPrasymasId");			
		}		
		
		try {
			SprendimaiDelegator.getInstance().saveSprendimas(request, sprendimas, gvt,prasymasId);
		} catch(Exception ex) {
			throw new DatabaseException(ex);
		}	

	    return mapping.findForward(Constants.CONTINUE);
	}
}
