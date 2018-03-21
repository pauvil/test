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

if(state.equals(Constants.IN_DECLARATION_CODE_FORM))out.print("atvykimo deklaracijà");
   else if(state.equals(Constants.OUT_DECLARATION_CODE_FORM))out.print("iðvykimo deklaracijà");
   else if(state.equals(Constants.CHNG_OUT_DECLARATION_CODE_FORM))out.print("gyvenamosios vietos keitimà uþsienyje");
   else if(state.equals(Constants.GVNA_DECLARATION_CODE_FORM))out.print("praðymà átraukti á GVNA apskaità"); %>

 </div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="DeklaracijaAsmKodasPerform">
<tr>
	<td width="5%">&nbsp;</td>
	<td width="25%"><b>Asmens kodas:</b></td>
	<td width="30%">
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" /> 
		<logic:present name="error.asmKodas"><span class="error">Negaliojantis asmens kodas</span></logic:present> 
		<logic:present name="error.cantFillGVNA"><span class="error">Ðis asmuo negali pildyti GVNA praðymo, nes turi deklaruotà gyvenamàjà vietà</span></logic:present> 
		<logic:present name="error.jauIsvykes"><span class="error">Ðis asmuo negali pildyti iðvykimo deklaracijos, nes jau yra iðvykæs á uþsiená </span></logic:present>
		<logic:present name="error.neraIsvykes"><span class="error">Ðis asmuo neuþpildæs iðvykimo deklaracijos </span></logic:present>
		<logic:present name="error.asmuoMires"><span class="error">Ðis asmuo yra miræs. Deklaracijos ávesti jam negalima</span></logic:present>
		<logic:present name="error.neraDeklaraves"><span class="error">Ðis asmuo neturi deklaruotos gyvenamosios vietos. Deklaracijos ávesti jam negalima</span></logic:present>
	</td>
	<td align="left" width="40%">
		<html:submit styleClass="button" value="Tæsti &raquo;" style="width: 120px;" />
	</td>
</tr>
</html:form>
<tr>
	<td colspan="4" align="right">
		<hr />
		<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ 
			if(!state.equals(Constants.CHNG_OUT_DECLARATION_CODE_FORM)) {
		%>
<%-- 		 	<html:link action="LaikiniAsmenysDeklaracija">Registruoti deklaracijà gyventojui, neturinèiam asmens kodo &raquo;</html:link>  --%>
				<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				 <input type="button" value="Registruoti deklaracijà gyventojui, neturinèiam asmens kodo &raquo;" onclick="goToUrl('<%= strPath%>/LaikiniAsmenysDeklaracija.do')" class="button" style="width: 350px;" />
				<%} else{ %> 
				<input type="button" value="Registruoti deklaracijà gyventojui, neturinèiam asmens kodo &raquo;" onclick="goToUrl('LaikiniAsmenysDeklaracija.do')" class="button" style="width: 350px;" />
				<%}%>
		<%  }} %>
		
	</td>
</tr>
</table>