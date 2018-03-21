<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Duomenø bazës objektai</div>
<html:form action="editgvdisaprdbobjektai" method="post">
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Objektas:</b></td>
	<td width="60%" align="left">
		<logic:present name="dbobjektas">
			<input type="hidden" name="id" value="<bean:write name="dbobjektas" property="id" />" />
			<input type="hidden" name="modulioId" value="<bean:write name="dbobjektas" property="modulisId" />" />
			<input name="objektas" class="input" value="<bean:write name="dbobjektas" property="objektas" />" />
		</logic:present>
		<logic:notPresent name="dbobjektas">
			<input type="hidden" name="id" value=<%=request.getParameter("id")%> />
			<input type="hidden" name="modulioId" value=<%=request.getParameter("modulioId")%> />
			<input name="objektas" class="input" />
		</logic:notPresent>
		
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Parametrai:</b></td>
	<td width="60%" align="left">
		<logic:present name="dbobjektas">
			<input name="parametrai" class="input" value="<bean:write name="dbobjektas" property="parametrai" />" />
		</logic:present>
		<logic:notPresent name="dbobjektas">
			<input name="parametrai" class="input"/> 
		</logic:notPresent>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Tipas:</b></td>
	<td width="60%" align="left">
		<logic:present name="dbobjektas">
			<input name="tipas" class="input" value="<bean:write name="dbobjektas" property="tipas" />"  />
		</logic:present>
		<logic:notPresent name="dbobjektas">
			<input name="tipas" class="input" />
		</logic:notPresent>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Schema:</b></td>
	<td width="60%" align="left">
	<logic:present name="dbobjektas">
		<input name="schema" class="input" value="<bean:write name="dbobjektas" property="schema" />" />
	</logic:present>
	<logic:notPresent name="dbobjektas">
		<input name="schema" class="input" />
	</logic:notPresent>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="30%" align="right"><b>Komentarai:</b></td>
	<td width="60%" align="left">
		<logic:present name="dbobjektas">
			<input name="komentarai" class="input" value="<bean:write name="dbobjektas" property="komentarai" />" />
		</logic:present>
		<logic:notPresent name="dbobjektas">
			<input name="komentarai" class="input"/>
		</logic:notPresent>
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

</table>
</html:form>