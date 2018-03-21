package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.PrasymasForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class EditPrasymasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		PrasymasForm pForm = (PrasymasForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE);
		PrasymasKeistiDuomenis prasymas = new PrasymasKeistiDuomenis();
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		
		try {
			long id = Long.parseLong(pForm.getId());
			prasymas = SprendimaiDelegator.getInstance().getPrasymas(request, id);
			
			prasymas.setRegNr(pForm.getRegNr());
			prasymas.setTipas(Long.parseLong(pForm.getTipas()));
			prasymas.setData(new Timestamp(sdf.parse(pForm.getData()).getTime()));
			
			//Tieto Paraiska Nr. 7
			if(Long.parseLong(pForm.getTipas())==2){
				prasymas.setNaikinamasAdresas(pForm.getAddressString());
				
				if(pForm.getAddressAdr()==null || "".equals(pForm.getAddressAdr())){
					prasymas.setGvtAdvNr(null);
				} else {
					prasymas.setGvtAdvNr(new Long(pForm.getAddressAdr()));
				}
				
				if(pForm.getAddressTer()==null || "".equals(pForm.getAddressTer())){
					prasymas.setGvtAtvNr(null);
				} else {
					prasymas.setGvtAtvNr(new Long(pForm.getAddressTer()));
				}
				
				prasymas.setNaujasAdresas("");
			} else {
				prasymas.setNaujasAdresas(pForm.getAdresas());
				prasymas.setNaikinamasAdresas("");
				prasymas.setGvtAdvNr(null);
				prasymas.setGvtAtvNr(null);
			}
			
			prasymas.setPastabos(pForm.getPastabos());
			prasymas.setPrasytojas(pForm.getPrasytojas());
			prasymas.setPrasytojoDokumentas(pForm.getDokumentas());
			

			prasymas.getAsmenys().clear();
			for (int i=0; i<pForm.getAsmenys().length; i++){
				long asmId = Long.parseLong(pForm.getAsmenys()[i]);
				Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmId);
				prasymas.getAsmenys().add(asmuo);
			}
		}
		catch (Exception e){
			throw new InternalException(e.getMessage(), e);
		}
		SprendimaiDelegator.getInstance().savePrasymas(request, prasymas);
		
	    return mapping.findForward(Constants.CONTINUE);
	}
}
