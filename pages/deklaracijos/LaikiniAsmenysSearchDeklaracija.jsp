<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Registruoti asmen�, neturint� asmens kodo</div>
<br />
<html:form action="LaikiniAsmenysSearchDeklaracijaPerform">
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<tr>
	<td>&nbsp;</td>
	<td><b>Vardas:</b></td>
	<td>
		<html:text property="vardas" styleClass="input"  /> <span class="error">*</span>
		<logic:present name="error.missingVardas"><br><span class="error">Nenurodytas vardas</span></logic:present>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Pavard�:</b></td>
	<td>
		<html:text property="pavarde" styleClass="input" /> <span class="error">*</span>
		<logic:present name="error.missingPavarde"><br><span class="error">Nenurodyta pavard�</span></logic:present>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Lytis:</b></td>
	<td>
		<html:radio property="lytis" value="V" />&nbsp; Vyr.&nbsp;&nbsp;&nbsp;<html:radio property="lytis" value="M"/>&nbsp; Mot.&nbsp;<span class="error">*</span>
		<logic:present name="error.missingLytis"><br><span class="error">Nenurodyta lytis</span></logic:present>	
	
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Gimimo data:</b></td>
	<td>
		<html:text property="gimimoData" styleClass="input" style="width: 70px;"/>
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.LaikiniAsmenysSearchDeklaracijaForm.gimimoData, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script> <span class="error">*</span>
<logic:present name="error.missingGimimoData"><br><span class="error">Nekorekti�ka data</span></logic:present>
<logic:present name="error.biggerGimimoData"><br><span class="error">Gimimo data negali b�ti v�lesn� nei �ios dienos data</span></logic:present>

	</td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<hr />
		<html:submit styleClass="button" value="Ie�koti" />
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr /><span class="error">*</span> <b>�enklu pa�ym�tus laukus privaloma u�pildyti</b>
	</td>
</tr>
</table>
</html:form>

<br />


<logic:present name="result">
<div class="heading">Rasti pretendentai tarpiniame rinkinyje</div>
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%">Vardas</th>
    <th width="35%">Pavard�</th>
    <th width="30%">Gimimo data</th>
    <th width="30%">Lytis</th>    
    <th width="15%">Veiksmai</th>
</tr>

<logic:iterate name="result" id="asmuo">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td align="center"><bean:write name="asmuo" property="ASM_VARDAS"/></td>
	<td align="center"><bean:write name="asmuo" property="ASM_PAVARDE"/></td>
	<td align="center"><bean:write name="asmuo" property="ASM_GIM_DATA" /></td>
	<td align="center"><bean:write name="asmuo" property="ASM_LYTIS"/></td>
	<td align="center">
			<a href="../getasmfromtmp.do?asmnr=<bean:write name="asmuo" property="ASM_NR"/>">Pasirinkti</a> 
	</td>
</tr>
</logic:iterate>
</table>
</logic:present>

<logic:notPresent name="result">
<logic:present name="toCheck">
<center>
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
		<img src="../img/info.png" />
	</td>
	<td class="message1">N�ra duomen�<br /> 
	</td>
</tr>
</table>
</center>
</logic:present>
</logic:notPresent>
<%
session.removeAttribute("toCheck");
%>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos �vedamos formatu <i>"metai-m�nuo-diena" (2006-09-01)</i>.
</div>

<input type="button" class="button" value="Naujas asmuo" onclick="window.location='<%=request.getContextPath()%>/LaikiniAsmenysDeklaracija.do'" />