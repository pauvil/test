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
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.PrasymasForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class CreatePrasymasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		PrasymasForm pForm = (PrasymasForm)form;
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE);
		PrasymasKeistiDuomenis prasymas = new PrasymasKeistiDuomenis();
		long istaigaId = ((Long)session.getAttribute("userIstaigaId")).longValue();
		
		try {
			prasymas.setRegNr(pForm.getRegNr());
			prasymas.setBusena(0);
			prasymas.setTipas(Long.parseLong(pForm.getTipas()));
			prasymas.setData(new Timestamp(sdf.parse(pForm.getData()).getTime()));
			prasymas.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId, request));
			//Tieto Paraiska Nr. 7
			if(Long.parseLong(pForm.getTipas())==2){
				prasymas.setNaikinamasAdresas(pForm.getAddressString());

				if(!"".equals(pForm.getAddressAdr())){
					prasymas.setGvtAdvNr(new Long(pForm.getAddressAdr()));	
				} else {
					prasymas.setGvtAdvNr(null);
				}
				
				if(!"".equals(pForm.getAddressTer())){
					prasymas.setGvtAtvNr(new Long(pForm.getAddressTer()));	
				} else {
					prasymas.setGvtAtvNr(null);
				}
				
			} else {
				prasymas.setNaujasAdresas(pForm.getAdresas());
			}
			
			prasymas.setPastabos(pForm.getPastabos());
			prasymas.setPrasytojas(pForm.getPrasytojas());
			prasymas.setPrasytojoDokumentas(pForm.getDokumentas());

			for (int i=0; i<pForm.getAsmenys().length; i++){
				long id = Long.parseLong(pForm.getAsmenys()[i]);
				Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
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
