package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.persistence.SprendimasKeistiDuomenis;

public class DeleteSprendimasAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws Exception 
	{
		Long l = new Long(request.getParameter("id"));
		SprendimasKeistiDuomenis spr = SprendimaiDelegator.getInstance().getSprendimas(request, l.longValue());
		/*
		Set asmenys = new HashSet();
		Set gvts = spr.getGyvenamosiosVietos();
		for (Iterator it = gvts.iterator(); it.hasNext();) {
			GyvenamojiVieta gvt = (GyvenamojiVieta) it.next();
			Asmuo asmuo = gvt.getAsmuo();
			asmenys.add(asmuo);			
		}	
		
		Set gvtsSprendimo = spr.getGyvenamosiosVietos();		
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		System.out.println("Gyv. vietø sk: " + gvtsSprendimo.size());
		System.out.println("Asmenu sk: " + asmenys.size());
		
		for (Iterator it = asmenys.iterator(); it.hasNext();) {
			GyvenamojiVieta gvtSprendimoForAsmuo = new GyvenamojiVieta();
			Asmuo asmuo = (Asmuo) it.next();
			Set gvtsAsmens = asmuo.getGyvenamosiosVietos();
			
			System.out.println("+++++++++");
			System.out.println("Asmens gyv. vietø sk: " + gvtsAsmens.size());
			
			for (Iterator it1 = gvtsSprendimo.iterator(); it1.hasNext();) {
				GyvenamojiVieta gvtSprendimo = (GyvenamojiVieta) it1.next();
				if (gvtSprendimo.getGvtAsmNr() == asmuo.getAsmNr()) {					
					gvtSprendimoForAsmuo = gvtSprendimo;
					System.out.println("Surado gyv. vietà asmeniui su asm. nr: " + asmuo.getAsmNr());
					break;
				}
			}
			
			System.out.println("+++++++++");
			
			if (gvtSprendimoForAsmuo != null) {
				for (Iterator it2 = gvtsAsmens.iterator(); it2.hasNext();) {
					GyvenamojiVieta gvtForAsmuo = (GyvenamojiVieta) it2.next();
					int comp = gvtForAsmuo.getGvtDataNuo().compareTo(gvtSprendimoForAsmuo.getGvtDataNuo());
					System.out.println("Sulyginimo reikðmë: " + comp);
					if (comp > 0) {
						throw new InternalException("Negalima iðtrinti sprendimo. Deklaruota nauja gyvenamoji vieta");
					}				
				}
			}
		}
		*/
		
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.SPR_VIEW_SPRENDIMAS);
		
		if (-1 == SprendimaiDelegator.getInstance().deleteSprendimas(request, spr)){
			session.setAttribute(Constants.CENTER_STATE, Constants.CANNOT_DELETE_DECLARATION);			
		}
		
		return mapping.findForward(Constants.CONTINUE);
	}

}
