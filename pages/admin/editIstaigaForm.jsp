<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Savivaldybës ar seniûnijos redagavimas</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="editistaiga" method="post">
<input type="hidden" name="id" value="<bean:write name="istaiga" property="id" />" />
<tr>
	<td width="10%"></td>
	<td width="30%"><b>Pasirinkta savivaldybë ar seniûnija:</b></td>
	<td width="60%"><bean:write name="istaiga" property="pilnasPavadinimas" /></td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="30%"><b>Oficialus ðios savivaldybës ar seniûnijos pavadinimas:</b></td>
	<td width="60%">
		<input name="oficialusPavadinimas" class="input" value="<bean:write name="istaiga" property="oficialusPavadinimas" />" />
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="30%"><b>Ðios savivaldybës ar seniûnijos duomenys:</b></td>
	<td width="60%">
		<input name="rekvizSpausdinimui" class="input" value="<bean:write name="istaiga" property="rekvizSpausdinimui" />" />
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr />
	</td>
</tr>
<tr>	
	<td colspan="1" align="left">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	<td colspan="2" align="center">
		<html:submit value="Iðsaugoti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>