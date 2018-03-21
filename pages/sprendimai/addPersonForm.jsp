<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Pasirinkite gyventojà</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="addperson">
<tr>
	<td width="5%">&nbsp;</td>
	<td width="25%"><b>Asmens kodas:</b></td>
	<td>
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" /> 
		<logic:present name="error.asmKodas"><br /><span class="error">Gyventojo ávestu asmens kodu nëra</span></logic:present>
		<logic:present name="error.asmuoMires"><br /><span class="error">Gyventojas ávestu asmens kodu yra miræs</span></logic:present> 
	</td>
</tr>
<tr>
	<td colspan="3"><hr /></td>
</tr>
<tr>	
	<td></td>
	<td></td>
	<td align="left">
		<html:submit styleClass="button" value="Ieðkoti &raquo;" style="width: 120px;" />
	</td>
</tr>
<input type="hidden" name="mode" value="<%=request.getParameter("mode")%>" />
</html:form>
</table>