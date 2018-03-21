<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<%--<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="TestAsmKodasPerform1">
<br/>
<br/>

<tr>
	<td width="5%">1. Gimimo datos problema&nbsp;</td>
	<td width="30%"><b>1.&nbsp;&nbsp;&nbsp;Asmens kodas:</b></td>
	<td width="25%">
		 <html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" /> 
		<logic:present name="error.asmKodas"><span class="error">Negaliojantis asmens kodas</span></logic:present> 		
	</td>
	<td align="left" width="40%">
		<html:submit property="submitas" styleClass="button" value="Tæsti &raquo;" style="width: 120px;" />		
	</td>
</tr> 
</html:form>
</table>--%>
<br/>
<hr width="95%"/>
<br/><br/><br/>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="TestEncoding">
<br/>
<br/>
<tr>
	<%--<td width="5%">2.  Lietuvybës problema&nbsp;</td>--%>
	<td width="30%"><b>2.&nbsp;&nbsp;&nbsp;Asmens kodas:(zmogus deklaraves GVNA)</b></td>
	<td width="25%">
		 <html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" /> 
		<logic:present name="error.asmKodas"><span class="error">Negaliojantis asmens kodas</span></logic:present> 		
	</td>
	<td align="left" width="40%">
		<html:submit property="submitas" styleClass="button" value="Tæsti &raquo;" style="width: 120px;" />		
	</td>
</tr>
</html:form>
</table>

<hr width="95%"/>
<script language="Javascript">
<!--
	function menu()
	{		
		var theDiv = document.getElementById('menu1');
		theDiv.innerHTML = '';
		theDiv = document.getElementById('menu2');
		theDiv.innerHTML = '';
		theDiv = document.getElementById('menu3');
		theDiv.innerHTML = '';
	}
	menu();
//-->	
</script>