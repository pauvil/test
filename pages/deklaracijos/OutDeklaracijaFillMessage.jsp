<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.*,
				com.algoritmusistemos.gvdis.web.*" %>


<div class="heading">Praneðimas</div>

<br/>
<div align="center">
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
	<td>
		<logic:empty name="declErrors"><img src="../img/info.png" /></logic:empty>
		<logic:notEmpty name="declErrors"><img src="../img/warning.gif" /></logic:notEmpty>
	</td>
	</td>
	<td class="message1">
		<logic:empty name="declErrors">
			<logic:notPresent name="savewserror" scope="session">
			<% 
			String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
			if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM_FILL_MESSAGE)) { %>
			Deklaracija iðsaugota sëkmingai.  
			<%} else { %>
			Gyvenamosios vietos keitimas uþsienyje iðsaugotas sëkmingai.  
			<% } %>
			<br /><br />
			</logic:notPresent>
		</logic:empty>
		<logic:notEmpty name="savewserror">
			Deklaracijos nepavyko patvirtinti: <br/>
			<bean:write name="savewserror"/>
		</logic:notEmpty>
		<logic:present name="declErrors">
		<logic:notEmpty name="declErrors">	
			Deklaracijoje yra klaidø: 
			<ul>
				<logic:iterate id="klaida" name="declErrors">
					<span class="error"><li><bean:write name="klaida"/></li></span><br/>
				</logic:iterate>
			</ul>
			Gyvenamosios vietos duomenys neáraðyti á Gyventojø registrà.<br /> 
			<% Long l = (Long)session.getAttribute("idForEdit"); 
			String saltinis;
			if (l == null) {
				saltinis = "";
			} else {
				saltinis = String.valueOf(DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(l, request).getSaltinis());
			}
			if ((l == null || DeklaracijosDelegator.getInstance(request).getIsvykimoDeklaracija(l, request).getSaltinis() == null) || !saltinis.equals("1")) {  %>
			Deklaracija patalpinta á skyriø "Nebaigtos ávesti deklaracijos".<br />
			<% } %>
			<br />
		</logic:notEmpty>
		</logic:present>

		<logic:present name="declWarnings">
			<logic:notEmpty name="declWarnings">
			Deklaracija uþpildyta nekorektiðkai. Aptiktos problemos:
			<ul>
				<logic:iterate id="klaida" name="declWarnings">
					<li><bean:write name="klaida"/></li>
				</logic:iterate>
			</ul>
			</logic:notEmpty>
		</logic:present>
	</td>
</tr>
</table>
</div>
<br/>
<hr/>
<logic:notEmpty name="declErrors">
<div align="center">
	<br /><br />
	<input type="button" class="button" value="Redaguoti deklaracijà" onclick="location.href='<%=request.getContextPath()%>/CompleteDeclaration.do?id=<%=session.getAttribute("lastDeclarationId")%>'" />
	<br /><br />	<br />
</div>
<hr />
</logic:notEmpty>
<%session.removeAttribute("idForEdit");%>