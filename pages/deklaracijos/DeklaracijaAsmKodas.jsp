<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.*" %>
<%@ page import="java.util.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<div class="heading">Registruoti 

<% 
	String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
	String userAgent, strPath;
	strPath = request.getContextPath().substring(1); 
	userAgent = request.getHeader("User-Agent") ; 

if(state.equals(Constants.IN_DECLARATION_CODE_FORM))out.print("atvykimo deklaracij�");
   else if(state.equals(Constants.OUT_DECLARATION_CODE_FORM))out.print("i�vykimo deklaracij�");
   else if(state.equals(Constants.CHNG_OUT_DECLARATION_CODE_FORM))out.print("gyvenamosios vietos keitim� u�sienyje");
   else if(state.equals(Constants.GVNA_DECLARATION_CODE_FORM))out.print("pra�ym� �traukti � GVNA apskait�"); %>

 </div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="DeklaracijaAsmKodasPerform">
<tr>
	<td width="5%">&nbsp;</td>
	<td width="25%"><b>Asmens kodas:</b></td>
	<td width="30%">
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" /> 
		<logic:present name="error.asmKodas"><span class="error">Negaliojantis asmens kodas</span></logic:present> 
		<logic:present name="error.cantFillGVNA"><span class="error">�is asmuo negali pildyti GVNA pra�ymo, nes turi deklaruot� gyvenam�j� viet�</span></logic:present> 
		<logic:present name="error.jauIsvykes"><span class="error">�is asmuo negali pildyti i�vykimo deklaracijos, nes jau yra i�vyk�s � u�sien� </span></logic:present>
		<logic:present name="error.neraIsvykes"><span class="error">�is asmuo neu�pild�s i�vykimo deklaracijos </span></logic:present>
		<logic:present name="error.asmuoMires"><span class="error">�is asmuo yra mir�s. Deklaracijos �vesti jam negalima</span></logic:present>
		<logic:present name="error.neraDeklaraves"><span class="error">�is asmuo neturi deklaruotos gyvenamosios vietos. Deklaracijos �vesti jam negalima</span></logic:present>
	</td>
	<td align="left" width="40%">
		<html:submit styleClass="button" value="T�sti &raquo;" style="width: 120px;" />
	</td>
</tr>
</html:form>
<tr>
	<td colspan="4" align="right">
		<hr />
		<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ 
			if(!state.equals(Constants.CHNG_OUT_DECLARATION_CODE_FORM)) {
		%>
<%-- 		 	<html:link action="LaikiniAsmenysDeklaracija">Registruoti deklaracij� gyventojui, neturin�iam asmens kodo &raquo;</html:link>  --%>
				<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				 <input type="button" value="Registruoti deklaracij� gyventojui, neturin�iam asmens kodo &raquo;" onclick="goToUrl('<%= strPath%>/LaikiniAsmenysDeklaracija.do')" class="button" style="width: 350px;" />
				<%} else{ %> 
				<input type="button" value="Registruoti deklaracij� gyventojui, neturin�iam asmens kodo &raquo;" onclick="goToUrl('LaikiniAsmenysDeklaracija.do')" class="button" style="width: 350px;" />
				<%}%>
		<%  }} %>
		
	</td>
</tr>
</table>