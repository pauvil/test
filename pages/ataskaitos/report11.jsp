<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Asmenys, kuriø deklaravimo duomenys turi bûti naikinami</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="report11" method="get">
<logic:equal name="userStatus" value="0">
<tr>
	<td width="10%"></td>
	<td>
		<b>Savivaldybë:</b>
	</td>
	<td colspan="2">
		<html:select property="savivaldybe" styleClass="input" onchange="document.ReportsForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="reportSavivaldybes" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
<tr>
	<td colspan="5" align="center">
		<html:submit value="Perþiûrëti" property="view" styleClass="button" />
	</td>
</tr>
</logic:equal>
</html:form>
</table>

<logic:present name="results">
<br />
<% int i = 0; %>
<div class="heading">Ataskaita</div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left" width="15%">Asmens kodas arba gimimo data</th>
	<th align="left">Vardas</th>
	<th align="left">Pavardë</th>
</tr>
<tr><td colspan="5" class="darkbg"></td></tr>
<logic:iterate name="results" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td>	
		<logic:notEqual name="result" property="asmKodas" value="0">
			<bean:write name="result" property="asmKodas"/>
		</logic:notEqual>
		<logic:equal name="result" property="asmKodas" value="0">
			<bean:write name="result" property="asmGimData" format="yyyy-MM-dd"/>
		</logic:equal>	
	</td>
		
	<td><bean:write name="result" property="vardas" /></td>
	<td><bean:write name="result" property="pavarde" /></td>
</tr>
<tr><td colspan="5" class="darkbg"></td></tr>
</logic:iterate>
</table>
<div align="right">
	<input type="button" class="button" value="Perkelti á Excel" onclick="goToUrl('report11.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
</div>
</logic:present>

<logic:notPresent name="results">
<br/>
<div align="center">
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
		<img src="../img/info.png" />
	</td>
	<td class="message1">Nëra duomenø<br /> 
	</td>
</tr>
</table>
</div>

<br/>
<hr/>

</logic:notPresent>
