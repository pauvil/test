<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/as" prefix="as" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading"><bean:write name="journalTitle"/></div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="journal" method="get">
<input type="hidden" name="journalType" value="<bean:write name="journalType" />" />
<tr>
	<td width="30%">&nbsp;</td>
	<td width="10%"><b>Data nuo:</b></td>
	<td width="60%">
		<html:text property="dataNuo" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.JournalForm.dataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="30%">&nbsp;</td>
	<td width="10%"><b>Data iki:</b></td>
	<td width="60%">
		<html:text property="dataIki" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.JournalForm.dataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataIki"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>

<logic:notPresent name="appBusena" ><input type="hidden" name="busena" value="0" /></logic:notPresent>
<logic:present name="appBusena">
<tr>
	<td></td>
	<td><b>Bûsena:</b></td>
	<td>
		<html:select property="busena" styleClass="input">
			<html:options collection="appBusena" property="id" labelProperty="value" />
		</html:select>
	</td>
</tr>
</logic:present>

<logic:equal name="userStatus" value="0">
<tr>
	<td></td>
	<td><b>Savivaldybë:</b></td>
	<td>
		<html:select property="savivaldybe" styleClass="input" onchange="document.JournalForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="journalSavivaldybes" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:equal>
<logic:lessThan name="userStatus" value="2">
<tr>
	<td></td>
	<td><b>Seniûnija:</b></td>
	<td>
		<html:select property="seniunija" styleClass="input" onchange="document.JournalForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="journalSeniunijos" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:lessThan>

<logic:present name="appDeklaracijosTipai">
<tr>
	<td></td>
	<td><b>Deklaracijos tipas:</b></td>
	<td>
		<html:select property="deklaracijaType" styleClass="input">
			<html:options collection="appDeklaracijosTipai" property="id" labelProperty="value" />
		</html:select>
	</td>
</tr>
</logic:present>

<logic:present name="journalValstybes">
<tr>
	<td></td>
	<td><b>Valstybë:</b></td>
	<td>
		<html:select property="valstybe" styleClass="input">
			<html:option value="">--- Visos ---</html:option>
			<html:options collection="journalValstybes" property="kodas" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:present>

<tr>	
	<td colspan="3" align="center">
		<html:submit styleClass="button" value="Rodyti" />
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;"><b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2010-12-01)</i>.</div>
<br />
<br />
<logic:present name="journalResults">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
	<logic:equal name="table_header_type" value="laukai6">
		<th width="10%"><A href="<bean:write name="table_header" property="field1Url"/>"><bean:write name="table_header" property="field1"/></A><logic:present name="table_header" property="field1OrderPix"><img src="../img/<bean:write name="table_header" property="field1OrderPix"/>"/></logic:present></th>
		<th width="8%"><A href="<bean:write name="table_header" property="field2Url"/>"><bean:write name="table_header" property="field2"/></A><logic:present name="table_header" property="field2OrderPix"><img src="../img/<bean:write name="table_header" property="field2OrderPix"/>"/></logic:present></th>
		<th width="25%"><A href="<bean:write name="table_header" property="field3Url"/>"><bean:write name="table_header" property="field3"/></A><logic:present name="table_header" property="field3OrderPix"><img src="../img/<bean:write name="table_header" property="field3OrderPix"/>"/></logic:present></th>
		<th width="10%"><bean:write name="table_header" property="field4"/></th>
		<th width="20%"><A href="<bean:write name="table_header" property="field5Url"/>"><bean:write name="table_header" property="field5"/></A><logic:present name="table_header" property="field5OrderPix"><img src="../img/<bean:write name="table_header" property="field5OrderPix"/>"/></logic:present></th>
		<th width="15%"><bean:write name="table_header" property="field6"/></th>
	</logic:equal>

    <logic:equal name="table_header_type" value="laukai5">
		<th width="10%"><A href="<bean:write name="table_header" property="field1Url"/>"><bean:write name="table_header" property="field1"/></A><logic:present name="table_header" property="field1OrderPix"><img src="../img/<bean:write name="table_header" property="field1OrderPix"/>"/></logic:present></th>
		<th width="8%"><A href="<bean:write name="table_header" property="field2Url"/>"><bean:write name="table_header" property="field2"/></A><logic:present name="table_header" property="field2OrderPix"><img src="../img/<bean:write name="table_header" property="field2OrderPix"/>"/></logic:present></th>
		<th width="25%"><A href="<bean:write name="table_header" property="field3Url"/>"><bean:write name="table_header" property="field3"/></A><logic:present name="table_header" property="field3OrderPix"><img src="../img/<bean:write name="table_header" property="field3OrderPix"/>"/></logic:present></th>
		<th width="20%"><A href="<bean:write name="table_header" property="field5Url"/>"><bean:write name="table_header" property="field5"/></A><logic:present name="table_header" property="field5OrderPix"><img src="../img/<bean:write name="table_header" property="field5OrderPix"/>"/></logic:present></th>
		<th width="15%"><bean:write name="table_header" property="field6"/></th>
	</logic:equal>



</tr>

<logic:iterate name="journalResults" id="result">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">

<logic:equal name="table_type" value="JOURNAL_TYPE_0_1_4">
	<td><bean:write name="result" property="regNr" /></td>
	<td align="center"><bean:write name="result" property="deklaracijosData" format="yyyy-MM-dd" /></td>
	<td><bean:write name="result" property="asmenvardis" /></td>
	<td><bean:write name="result" property="busenaStr" /></td>
	<td><bean:write name="result" property="istaigaStr" /></td>
	<td>
		<logic:iterate name="result" property="veiksmai" id="act">
			<logic:equal name="act" property="ynConfirm" value="">
				<A href="<bean:write name="act" property="url" />"><bean:write name="act" property="message"/></A>&nbsp;&nbsp;
			</logic:equal>
			<logic:notEqual name="act" property="ynConfirm" value="">
				<A href="<bean:write name="act" property="url" />" onclick="return confirm('<bean:write name="act" property="ynConfirm" />');"><bean:write name="act" property="message"/></A>&nbsp;&nbsp;
			</logic:notEqual>				
		</logic:iterate>
	</td>
</logic:equal>

<logic:equal name="table_type" value="JOURNAL_TYPE_2_5" >
	<td><bean:write name="result" property="regNr" /></td>
	<td align="center"><bean:write name="result" property="pazymosData" format="yyyy-MM-dd" /></td>
	<td><bean:write name="result" property="asmenvardis" /></td>
	<td><bean:write name="result" property="istaigaStr" /></td>
	<td align="center">
		<logic:iterate name="result" property="veiksmai" id="act">
			<A href="<bean:write name="act" property="url" />"><bean:write name="act" property="message"/></A>&nbsp;&nbsp;
		</logic:iterate></td>
</logic:equal>
<logic:equal name="table_type" value="JOURNAL_TYPE_6" >
	<td><bean:write name="result" property="regNr" /></td>
	<td align="center"><bean:write name="result" property="pazymosData" format="yyyy-MM-dd" /></td>
	<td><bean:write name="result" property="adresas" /></td>
	<td><bean:write name="result" property="istaigaStr" /></td>
	<td align="center">
		<logic:iterate name="result" property="veiksmai" id="act">
			<A href="<bean:write name="act" property="url" />"><bean:write name="act" property="message"/></A>&nbsp;&nbsp;
		</logic:iterate>
	</td>
</logic:equal>
<logic:equal name="table_type" value="JOURNAL_TYPE_3" >
	<td align="center"><bean:write name="result" property="regNr" /></td>
	<td align="center"><bean:write name="result" property="sprendimoData" format="yyyy-MM-dd" /></td>
	<td align="center"><bean:write name="result" property="tipas" /></td>
	<td><bean:write name="result" property="istaigaStr" /></td>
	<td align="center"> 
		<logic:iterate name="result" property="veiksmai" id="act">
			<A href="<bean:write name="act" property="url" />"><bean:write name="act" property="message"/></A>&nbsp;&nbsp;
		</logic:iterate>
	</td>
</logic:equal>

</tr>
</logic:iterate>
</table>
<hr />

<table cellpadding="2" cellspacing="1" border="0" width="100%">
<tr>
	<td align="center" width="90%">
	<logic:present name="footer_paging">
		<logic:iterate name="footer_paging" id="puslapiavimas">
			<logic:equal value="" name="puslapiavimas" property="url">
				<span style="color: #333333;font-weight: bold;font-size: 20px;text-decoration: underline;"><bean:write name="puslapiavimas" property="message" /></span>&nbsp;&nbsp;
			</logic:equal>
			<logic:notEqual value="" name="puslapiavimas" property="url">
				<A href="<bean:write name="puslapiavimas" property="url" />">
				<bean:write name="puslapiavimas" property="message" /></A>&nbsp;&nbsp;
			</logic:notEqual>
		</logic:iterate>
		<br />[Viso áraðø: <bean:write name="total_records"/>, viso puslapiø: <bean:write name="total_pages"/>]
	</logic:present>
	</td>
	<td align="right">
		<input type="button" class="button" value="Spausdinti þurnalà" onclick="window.open('<%= request.getContextPath() %>/journal.do?<%= request.getQueryString() %>&output=pdf')" style="width: 150px;" />&nbsp;
	</td>
</tr>	
</table>

</logic:present>

<logic:notPresent name="journalResults">
<br/>
<div align="center">
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
		<img src="../img/info.png" />
	</td>
	<td class="message1">
				<bean:write name="not_found_type" /><br /> 
	</td>
</tr>
</table>
</div>

<br/>
<hr/>

</logic:notPresent>
