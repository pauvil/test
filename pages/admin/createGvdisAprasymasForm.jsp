<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.*" %>

<div class="heading">Nauja apraðymo versija</div>
<html:form action="creategvdisaprasymas" method="post">
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"> </td>
	<td width="30%" align="right"><b>Naujos versijos numeris:</b></td>
	<td width="60%" align="left">
		<input type="hidden" name="modulioId" value="<bean:write name="aprmodulis" property="modulioId" />" />
	 	<input type="text" name="versija" value="<%=Version.VERSION%>"/>
	</td>
</tr>
<tr>	
	<td colspan="1" align="left">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	<td colspan="2" align="center">
		<html:submit value="Iðsaugoti naujà versijà" styleClass="button" />
		
	</td>
</tr>
</table>
</html:form>