<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>


<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<div class="heading"><bean:write name="reportTitle" /></div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="report13" method="get">
<tr>
	<td width="10%"></td>
	<td width="200">
		<b>Data:</b>
	</td>
	<td width="180">
		<html:text property="raportData" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.Report13Form.raportData, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<html:errors property="raportData"/>
		<logic:present name="erraportData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>

<tr>
	<td width="10%"></td>
	<td>
		<b>Savivaldybë:</b>
	</td>
	<td colspan="2">
		<html:select property="savivaldybe" styleClass="input" onchange="document.Report13Form.seniunija.value=0; document.Report13Form.submit();">
			<html:option value="0">---</html:option>
			<html:options collection="reportSavivaldybes" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>

<tr>
	<td width="10%"></td>
	<td> 
		<b>Seniûnija:</b>
	</td>
	<td colspan="2">
		<html:select property="seniunija" styleClass="input" onchange="document.Report13Form.submit();">
			<html:option value="0">---</html:option>
			<html:options collection="reportSeniunijos" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td> 
		<b>Gyvenamoji vietovë:</b>
	</td>
	<td colspan="2">
		<html:select property="kaimas" styleClass="input" onchange="document.Report13Form.submit();">
			<html:option value="0">---</html:option>
			<html:options collection="reportKaimai" property="id" labelProperty="value" />
		</html:select>
	</td>
</tr>

<tr>
	<td colspan="5" align="center">
		<html:submit value="Perþiûrëti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2011-09-01)</i>.
</div>


<logic:present name="results">

<div class="heading">Gyventojø, deklaravusiø savo gyvenamàjà vietà, skaièius</div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="center">Grupë</th>
	<th align="right" >Vyrø</th>
	<th align="right" >Moterø</th>
	<th align="right" >Ið viso</th>
</tr>
<tr><td colspan="13" class="darkbg"></td></tr>
<logic:iterate name="results" id="result">
<tr>
	<td></td>
	<td align="left"><bean:write name="result" property="nr" /></td>
	<td align="center"><bean:write name="result" property="grupe" /></td>
	<td align="right"><bean:write name="result" property="vyru" /></td>
	<td align="right"><bean:write name="result" property="moteru" /></td>
	<td align="right"><bean:write name="result" property="viso" /></td>
</tr>
<tr><td colspan="13" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
	<input type="button" class="button" value="Perkelti á Excel" onclick="goToUrl('report13.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
</div>
<br/>
</logic:present>

<logic:notPresent name="results">
<logic:notEqual name="kaimas" value="">
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
</logic:notEqual>
</logic:notPresent>
