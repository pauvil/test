package com.algoritmusistemos.gvdis.web.actions.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.forms.TestAsmKodasForm1;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;

public class TestEncoding extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse arg3) throws Exception {
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});	
		HttpSession session = request.getSession();		
		session.setAttribute(Constants.CENTER_STATE, Constants.TEST_ENCODING);
		TestAsmKodasForm1 akf = (TestAsmKodasForm1) form;
		String rez = "";
		String[] st = DeklaracijosDelegator.getInstance(request).chkAsmDekl(Long.parseLong(akf.getAsmKodas()),((Long)request.getSession().getAttribute("userIstaigaId")).longValue(),"A",request);
	    
		Asmuo asmuo = UserDelegator.getInstance().getAsmuoByAsmKodas(Long.parseLong(akf.getAsmKodas()),request);
		if (null != asmuo){
			session.setAttribute("asmuo",asmuo);
			/*rez += "1. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 1).getAdr() + "<br/>";
			rez += "2. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 2).getAdr() + "<br/>";
			rez += "3. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 3).getAdr() + "<br/>";
			rez += "4. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 4).getAdr() + "<br/>";
			rez += "5. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 5).getAdr() + "<br/>";
			rez += "6. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 6).getAdr() + "<br/>";
			rez += "7. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 7).getAdr() + "<br/>";
			rez += "8. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 8).getAdr() + "<br/>";
			rez += "9. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 9).getAdr() + "<br/>";
			rez += "10. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 10).getAdr() + "<br/>";
			rez += "11. "+AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 11).getAdr() + "<br/>";
			*/
			String tempRez = AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 1).getAdr();
			rez += "<br/>1. "+tempRez + "&nbsp;&nbsp;<b>Hex:</b> ";
			for (int i = 0; i < tempRez.length(); i++) {
				String hex = Integer.toHexString(tempRez.charAt(i));
				rez += " " +hex;
				
			}
			tempRez = AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 2).getAdr();
			rez += "<br/>2. "+tempRez + "&nbsp;&nbsp;<b>Hex:</b> ";
			for (int i = 0; i < tempRez.length(); i++) {
				String hex = Integer.toHexString(tempRez.charAt(i));
				rez += " " +hex;
				
			}
			tempRez = AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 3).getAdr();
			rez += "<br/>3. "+tempRez + "&nbsp;&nbsp;<b>Hex:</b> ";
			for (int i = 0; i < tempRez.length(); i++) {
				String hex = Integer.toHexString(tempRez.charAt(i));
				rez += " " +hex;
				
			}
			tempRez = AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 4).getAdr();
			rez += "<br/>4. "+tempRez + "&nbsp;&nbsp;<b>Hex:</b> ";
			for (int i = 0; i < tempRez.length(); i++) {
				String hex = Integer.toHexString(tempRez.charAt(i));
				rez += " " +hex;
				
			}
			tempRez = AdresaiDelegator.getInstance().getTestAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request, 5).getAdr();
			rez += "<br/>5. "+tempRez + "&nbsp;&nbsp;<b>Hex:</b> ";
			for (int i = 0; i < tempRez.length(); i++) {
				String hex = Integer.toHexString(tempRez.charAt(i));
				rez += " " +hex;
				
			}
			
			
			tempRez = AdresaiDelegator.getInstance().getAsmDeklGvtNr(asmuo.getAsmNr(), Long.parseLong(st[0]) , request).getAdr();
			rez += "<br/>Senas. "+tempRez + "&nbsp;&nbsp;<b>Hex:</b> ";
			for (int i = 0; i < tempRez.length(); i++) {
				String hex = Integer.toHexString(tempRez.charAt(i));
				rez += " " +hex;
				
			}
			
		}
		session.setAttribute("text",rez);
		return mapping.findForward(Constants.CONTINUE);
	}

}
