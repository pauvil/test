<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Susieti asmená su Gyvenamøjø vietø registru</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="compareperson">
<tr>
	<td width="5%">&nbsp;</td>
	<td width="20%"><b>Asmens kodas:</b></td>
	<td width="65%">
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" />
		<logic:present name="error.asmKodas"><br /><span class="error">Asmuo nesurastas</span> </logic:present>
		<logic:present name="error.asmuoMires"><br /><span class="error">Gyventojas ávestu asmens kodu yra miræs</span></logic:present>
	</td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<html:submit value="Ieðkoti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>