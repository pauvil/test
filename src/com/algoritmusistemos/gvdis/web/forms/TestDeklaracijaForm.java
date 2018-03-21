package com.algoritmusistemos.gvdis.web.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.actions.test.TestAsmuo2;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.TestAsmuo;

public class TestDeklaracijaForm extends ActionForm {
	private String asmensKodas;	
	private String gimimoMetai1;
	private String gimimoMenuo1;
	private String gimimoData1;
	private String gimimoMetai2;
	private String gimimoMenuo2;
	private String gimimoData2;
	private String gimimoMetai3;
	private String gimimoMenuo3;
	private String gimimoData3;
	private String gimimoMetai4;
	private String gimimoMenuo4;
	private String gimimoData4;
	private String gimimoMetai5;
	private String gimimoMenuo5;
	private String gimimoData5;
	private String gimimoMetai6;
	private String gimimoMenuo6;
	private String gimimoData6;
	private String gimimoMetai7;
	private String gimimoMenuo7;
	private String gimimoData7;
	
	protected SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	protected SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	protected SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	protected SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Asmuo asm = (Asmuo)session.getAttribute("asmuo");
 		Asmuo a1 = null;
 		Asmuo a2 = null; 		
 		TestAsmuo a3 = null;
 		TestAsmuo2 a4 = null;
 		TestAsmuo2 a5 = null;
 		Asmuo a6 = null;
 		Asmuo a7 = null;
		try {
			a1 = UserDelegator.getInstance().getAsmuoByAsmKodas(asm.getAsmKodas().longValue(),request);
			a2 = UserDelegator.getInstance().getTestAsmuoByAsmKodasDateDate(asm.getAsmKodas().longValue(),request);
			a3 = UserDelegator.getInstance().getTestAsmuoByAsmKodasTimestampTimestamp(asm.getAsmKodas().longValue(),request);
			a4 = UserDelegator.getInstance().getTestAsmuoByAsmKodasDateTimestamp(asm.getAsmKodas().longValue(),request);
			a5 = UserDelegator.getInstance().getTestAsmuoByAsmKodasDateDate2(asm.getAsmKodas().longValue(),request);
			a6 = UserDelegator.getInstance().getTestAsmuoByAsmKodasTimestampTimestamp2(asm.getAsmKodas().longValue(),request);
			a7 = UserDelegator.getInstance().getTestAsmuoByAsmKodasTimestampTimestamp3(asm.getAsmKodas().longValue(),request);
		} catch (ObjectNotFoundException e) {} catch (DatabaseException e) {}
  		
		setAsmensKodas(String.valueOf(asm.getAsmKodas()));			
		if (a1.getAsmGimData() != null){
			setGimimoMetai1(yearFormat.format(a1.getAsmGimData()));
			setGimimoMenuo1(monthFormat.format(a1.getAsmGimData()));
			String log1 = (String)session.getAttribute("log1");
			log1 += " reset: MM: " + monthFormat.format(a1.getAsmGimData());
			session.removeAttribute("log1"); session.setAttribute("log1", log1);
			setGimimoData1(dayFormat.format(a1.getAsmGimData()));
			
		}
		setGimimoMetai2(yearFormat.format(a2.getAsmGimData()));
		setGimimoMenuo2(monthFormat.format(a2.getAsmGimData()));
		String log2 = (String)session.getAttribute("log2");
		log2 += " reset: MM: " + monthFormat.format(a2.getAsmGimData());
		session.removeAttribute("log2"); session.setAttribute("log2", log2);		
		setGimimoData2(dayFormat.format(a2.getAsmGimData()));
		
		setGimimoMetai3(yearFormat.format(a3.getAsmGimData()));
		setGimimoMenuo3(monthFormat.format(a3.getAsmGimData()));
		String log3 = (String)session.getAttribute("log3");
		log3 += " reset: MM: " + monthFormat.format(a3.getAsmGimData());
		session.removeAttribute("log3"); session.setAttribute("log3", log3);
		setGimimoData3(dayFormat.format(a3.getAsmGimData()));
		
		setGimimoMetai4(yearFormat.format(a4.getAsmGimData()));
		setGimimoMenuo4(monthFormat.format(a4.getAsmGimData()));
		String log4 = (String)session.getAttribute("log4");
		log4 += " reset: MM: " + monthFormat.format(a4.getAsmGimData());
		session.removeAttribute("log4"); session.setAttribute("log4", log4);
		setGimimoData4(dayFormat.format(a4.getAsmGimData()));
		
		setGimimoMetai5(yearFormat.format(a5.getAsmGimData()));
		setGimimoMenuo5(monthFormat.format(a5.getAsmGimData()));
		String log5 = (String)session.getAttribute("log5");
		log5 += " reset: MM: " + monthFormat.format(a5.getAsmGimData());
		session.removeAttribute("log5"); session.setAttribute("log5", log5);
		setGimimoData5(dayFormat.format(a5.getAsmGimData()));
		
		setGimimoMetai6(yearFormat.format(a6.getAsmGimData()));
		setGimimoMenuo6(monthFormat.format(a6.getAsmGimData()));
		String log6 = (String)session.getAttribute("log6");
		log6 += " reset: MM: " + monthFormat.format(a6.getAsmGimData());
		session.removeAttribute("log6"); session.setAttribute("log6", log6);
		setGimimoData6(dayFormat.format(a6.getAsmGimData()));
		
		setGimimoMetai7(yearFormat.format(a7.getAsmGimData()));
		setGimimoMenuo7(monthFormat.format(a7.getAsmGimData()));
		String log7 = (String)session.getAttribute("log7");
		log7 += " reset: MM: " + monthFormat.format(a7.getAsmGimData());
		session.removeAttribute("log7"); session.setAttribute("log7", log7);
		setGimimoData7(dayFormat.format(a7.getAsmGimData()));
		
		super.reset(arg0, request);
	}

	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {		
		return null;
	}

	public String getAsmensKodas() {
		return asmensKodas;
	}

	public void setAsmensKodas(String asmensKodas) {
		this.asmensKodas = asmensKodas;
	}

	public String getGimimoData1() {
		return gimimoData1;
	}

	public void setGimimoData1(String gimimoData) {
		this.gimimoData1 = gimimoData;
	}

	public String getGimimoMenuo1() {
		return gimimoMenuo1;
	}

	public void setGimimoMenuo1(String gimimoMenuo) {
		this.gimimoMenuo1 = gimimoMenuo;
	}

	public String getGimimoMetai1() {
		return gimimoMetai1;
	}

	public void setGimimoMetai1(String gimimoMetai) {
		this.gimimoMetai1 = gimimoMetai;
	}

	public String getGimimoData2() {
		return gimimoData2;
	}

	public void setGimimoData2(String gimimoData2) {
		this.gimimoData2 = gimimoData2;
	}

	public String getGimimoData3() {
		return gimimoData3;
	}

	public void setGimimoData3(String gimimoData3) {
		this.gimimoData3 = gimimoData3;
	}

	public String getGimimoData4() {
		return gimimoData4;
	}

	public void setGimimoData4(String gimimoData4) {
		this.gimimoData4 = gimimoData4;
	}

	public String getGimimoMenuo2() {
		return gimimoMenuo2;
	}

	public void setGimimoMenuo2(String gimimoMenuo2) {
		this.gimimoMenuo2 = gimimoMenuo2;
	}

	public String getGimimoMenuo3() {
		return gimimoMenuo3;
	}

	public void setGimimoMenuo3(String gimimoMenuo3) {
		this.gimimoMenuo3 = gimimoMenuo3;
	}

	public String getGimimoMenuo4() {
		return gimimoMenuo4;
	}

	public void setGimimoMenuo4(String gimimoMenuo4) {
		this.gimimoMenuo4 = gimimoMenuo4;
	}

	public String getGimimoMetai2() {
		return gimimoMetai2;
	}

	public void setGimimoMetai2(String gimimoMetai2) {
		this.gimimoMetai2 = gimimoMetai2;
	}

	public String getGimimoMetai3() {
		return gimimoMetai3;
	}

	public void setGimimoMetai3(String gimimoMetai3) {
		this.gimimoMetai3 = gimimoMetai3;
	}

	public String getGimimoMetai4() {
		return gimimoMetai4;
	}

	public void setGimimoMetai4(String gimimoMetai4) {
		this.gimimoMetai4 = gimimoMetai4;
	}

	public String getGimimoData5() {
		return gimimoData5;
	}

	public void setGimimoData5(String gimimoData5) {
		this.gimimoData5 = gimimoData5;
	}

	public String getGimimoMenuo5() {
		return gimimoMenuo5;
	}

	public void setGimimoMenuo5(String gimimoMenuo5) {
		this.gimimoMenuo5 = gimimoMenuo5;
	}

	public String getGimimoMetai5() {
		return gimimoMetai5;
	}

	public void setGimimoMetai5(String gimimoMetai5) {
		this.gimimoMetai5 = gimimoMetai5;
	}

	public String getGimimoData6() {
		return gimimoData6;
	}

	public void setGimimoData6(String gimimoData6) {
		this.gimimoData6 = gimimoData6;
	}

	public String getGimimoMenuo6() {
		return gimimoMenuo6;
	}

	public void setGimimoMenuo6(String gimimoMenuo6) {
		this.gimimoMenuo6 = gimimoMenuo6;
	}

	public String getGimimoMetai6() {
		return gimimoMetai6;
	}

	public void setGimimoMetai6(String gimimoMetai6) {
		this.gimimoMetai6 = gimimoMetai6;
	}

	public String getGimimoData7() {
		return gimimoData7;
	}

	public void setGimimoData7(String gimimoData7) {
		this.gimimoData7 = gimimoData7;
	}

	public String getGimimoMenuo7() {
		return gimimoMenuo7;
	}

	public void setGimimoMenuo7(String gimimoMenuo7) {
		this.gimimoMenuo7 = gimimoMenuo7;
	}

	public String getGimimoMetai7() {
		return gimimoMetai7;
	}

	public void setGimimoMetai7(String gimimoMetai7) {
		this.gimimoMetai7 = gimimoMetai7;
	}
}
