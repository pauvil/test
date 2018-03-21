<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.*" %>



<script language="Javascript">
<!--
	function printPazyma()
	{
		var url = 'printgvpazyma.do?register=on';
		var vaikaiBox = document.getElementById('vaikaiBox');
		if (vaikaiBox && vaikaiBox.checked){
			url += '&vaikai=on';
		}
        if (!document.all){
            url = '../' + url;
        }
        window.open(url, '_pazyma', 'status=yes,menubar=yes,location=yes,scrollbars=yes,resizable=yes,screenX=0,screenY=0');
	}
//-->
</script>

<div class="heading">Prane�imas</div>

<br/>
<div align="center">
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
		<logic:empty name="declErrors"><img src="../img/info.png" /></logic:empty>
		<logic:notEmpty name="declErrors"><img src="../img/warning.gif" /></logic:notEmpty>
	</td>
	<td class="message1">
		<logic:empty name="declErrors">
		<logic:notPresent name="savewserror" scope="session">
			Deklaracija i�saugota s�kmingai.
			<br /><br >  
		</logic:notPresent>
		</logic:empty>
		<logic:notEmpty name="declErrors">
			Deklaracijoje yra klaid�: 
			<ul>
				<logic:iterate id="klaida" name="declErrors">
					<span class="error"><li><bean:write name="klaida"/></li></span><br />
				</logic:iterate>
			</ul>
			Gyvenamosios vietos duomenys ne�ra�yti � Gyventoj� registr�.<br /> 
			
			
			<% Long l = (Long)session.getAttribute("idForEdit"); 
			String saltinis;
			if (l == null) {
				saltinis = "";
			} else {
				saltinis = String.valueOf(DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(l, request).getSaltinis());
			}
			if (( l == null || DeklaracijosDelegator.getInstance(request).getAtvykimoDeklaracija(l, request).getSaltinis() == null) || !saltinis.equals("1")) {  %>
			Deklaracija patalpinta � skyri� "Nebaigtos �vesti deklaracijos".<br />
			<% } %>
			<br /><br />
			<logic:present name="gvPazyma">
			Pastaba: <span style="font-weight: normal;">Spausdinant pa�ym�, tr�kstamus laukus u�pildykite ranka.</span><br />
			<br />
			</logic:present>
		</logic:notEmpty>
		<logic:notEmpty name="savewserror">
			Deklaracijos nepavyko patvirtinti: <br/>
			<bean:write name="savewserror"/>
		</logic:notEmpty>
		<logic:notEmpty name="declWarnings">
			Deklaracija u�pildyta nekorekti�kai. Aptiktos problemos:
			<ul>
				<logic:iterate id="klaida" name="declWarnings">
					<li><bean:write name="klaida"/></li>
				</logic:iterate>
			</ul>
		</logic:notEmpty>
	</td>
</tr>
</table>
</div>
<br/>
<hr/>
<logic:present name="gvPazyma">
<div align="center">
	<u><b>Pa�yma apie deklaruot� gyvenam�j� viet�:</b></u>
	<br /><br />
	<input type="checkbox" checked="checked" id="vaikaiBox" />� pa�ym� �traukti nepilname�i� vaik� duomenis
	<br /><br />
	<input type="button" class="button" value="Formuoti pa�ym�" onclick="printPazyma()" />
</div>
<hr />
</logic:present>
<logic:notEmpty name="declErrors">
<div align="center">
	<br /><br />
	<input type="button" class="button" value="Redaguoti deklaracij�" onclick="location.href='<%=request.getContextPath()%>/CompleteDeclaration.do?id=<%=session.getAttribute("lastDeclarationId")%>'" />
	<br /><br />	<br />
</div>
<hr />
</logic:notEmpty>
<%session.removeAttribute("idForEdit");%>