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
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.SprendimasForm;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;

public class EditSprendimasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		Constants.Println(request, "BEGIN EditSprendimasAction");
		SprendimasForm sForm = (SprendimasForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE);		
		SprendimasKeistiDuomenis sprendimas = new SprendimasKeistiDuomenis();
		GyvenamojiVieta gvt = new GyvenamojiVieta();
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		
	    HttpSession session = request.getSession();
    	session.setAttribute("createSprendimas", null);
		
		try {
			long id = Long.parseLong(sForm.getId());
			sprendimas = SprendimaiDelegator.getInstance().getSprendimas(request, id);

			sprendimas.setRegNr(sForm.getRegNr());
			sprendimas.setData(new Timestamp(sdf.parse(sForm.getData()).getTime()));
			sprendimas.setPriezastis(ZinynaiDelegator.getInstance().getZinynoReiksme(request, Long.parseLong(sForm.getPriezastis())));
			sprendimas.setPastabos(sForm.getPastabos());
			sprendimas.setPrieme(sForm.getPrieme());
		
			gvt.setGvtAdvNr("".equals(sForm.getAddressAdr()) ? null : Long.valueOf(sForm.getAddressAdr()));
			gvt.setGvtAtvNr("".equals(sForm.getAddressTer()) ? null : Long.valueOf(sForm.getAddressTer()));
			gvt.setGvtTipas(sprendimas.getTipas() == 2 ? "I" : (sForm.getAddressType().equals("0") ? "R" : "V"));
			//atsizvelgiama i kampo numeri
			gvt.setGvtKampoNr("".equals(sForm.getGvtKampoNr()) ? null : Long.valueOf(sForm.getGvtKampoNr()));
		}
		catch (Exception e){
			Constants.Println(request, "klaida "+e.getMessage());
			throw new InternalException(e);
		}
		
		try {
			SprendimaiDelegator.getInstance().saveSprendimas(request, sprendimas, gvt, null);
		} catch(Exception ex) {
			throw new DatabaseException(ex);
		}
		
		Constants.Println(request, "END EditSprendimasAction");
	    return mapping.findForward(Constants.CONTINUE);
	}
}
