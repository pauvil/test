package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;

public class LinkWithGRPerformAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_ASSIGN_GR);
		HttpSession session = request.getSession();
		Deklaracija deklaracija = (Deklaracija)session.getAttribute("declaration");
		Asmuo asm = (Asmuo)session.getAttribute("asmuo");

		boolean can = true;
		Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request,String.valueOf(asm.getAsmKodas()));
        //if(0 != asmuo.getGyvenamosiosVietos().size()) //kom ju.k 2007.09.27
        //{
        //	session.setAttribute("hasDeclaredResidence","");
        //	can = false;
        //}
		
		Date minData = QueryDelegator.getInstance().minValidDeclDate(request, (Asmuo)asmuo); //ju.k 2007.09.27
		Date dekldata = deklaracija.getDeklaravimoData();
		if (dekldata.before(minData)){
			session.setAttribute("kertasilaikotarpis","");
			can = false;
		}
		
		if(DeklaracijosDelegator.getInstance(request).hasNotCompletedDeclartions(asmuo,request))
		{
			session.setAttribute("hasNotCompletedDeclartions","");
        	can = false;
		}
		String ret = "";
		if(can)ret = DeklaracijosDelegator.getInstance(request).
		switchDeclarationToAsmuo(asmuo.getAsmKodas().longValue() ,deklaracija.getId() ,request);
		if( "ok".equals(ret))
		{
			session.setAttribute("linkAsmuo",String.valueOf(asmuo.getAsmKodas()));
			session.setAttribute("linkDeklaracija",String.valueOf(deklaracija.getId()));
		}
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
