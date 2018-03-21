<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<div class="heading">Susieti asmená su Gyvenamøjø vietø registru</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<tr>	
	<td colspan="3" align="center">
		&nbsp;
	</td>
</tr>
<logic:present name="linkDeklaracija">
	<tr>
		<td width="20%">&nbsp;</td>
		<td width="20%" align="right"><img src="../img/info.png" /></td>
		<td width="40%" align="left"><b>Asmuo <bean:write name="linkAsmuo"/> susietas su deklaracija <bean:write name="linkDeklaracija"/></b></td>
		<td width="20%">&nbsp;</td>
	</tr>
</logic:present>
<logic:present name="hasNotCompletedDeclartions">
	<tr>
		<td width="20%">&nbsp;</td>
		<td width="20%" align="right"><img src="../img/warning.gif" /></td>
		<td width="40%" align="left"><b>Asmuo <bean:write name="linkAsmuo"/> turi nebaigtø deklaracijø</b></td>
		<td width="20%">&nbsp;</td>
	</tr>
</logic:present>
</table>
<%
	session.removeAttribute("hasNotCompletedDeclartions");		
	session.removeAttribute("linkAsmuo");
	session.removeAttribute("linkDeklaracija");        	
	session.removeAttribute("hasDeclaredResidence");
%>