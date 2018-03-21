<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Apraðymo koregavimas</div>
<html:form action="editgvdisaprasymas" method="post">
<input type="hidden" name="modulioId" value="<bean:write name="aprmodulis" property="modulioId" />" />
<table class="form" cellpadding="2" cellspacing="1" width="100%">

 <tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Apraðymas:</b></td>
	<td width="60%" align="left">
		<textarea name="aprasymas" cols="100" rows="20" class="aprasymastext">
			<bean:write name="aprmodulis" property="aprasymas" />
		</textarea>
	</td>
</tr>
 <tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Logikos apraðymas:</b></td>
	<td width="60%" align="left">
		<textarea name="logikosAprasymas" cols="100" rows="20" class="aprasymastext">
			<bean:write name="aprmodulis" property="logikosAprasymas" />
		</textarea>
	</td>
</tr>
<tr>	
	<td colspan="1" align="left">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	<td colspan="2" align="center">
		<html:submit value="Iðsaugoti" styleClass="button" />		
	</td>
</tr>

</table>
</html:form>