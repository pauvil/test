<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<script language="Javascript">
<!--
	function setAddress(adrVienetas, gvtKampoNrP, terVienetas, addressString)
	{
		var adrBox = document.getElementById('addressAdr');
		var terBox = document.getElementById('addressTer');
		var stringBox = document.getElementById('addressString');
		var gvtKampoNr = document.getElementById('gvtKampoNr');
		adrBox.value = adrVienetas;
		terBox.value = terVienetas;
		stringBox.value = addressString;
		gvtKampoNr.value = gvtKampoNrP;
	}
//-->
</script>

<div class="heading">Deklaravimo duomen� paie�ka pagal adres�</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="queryaddressresults" method="get">
<tr>
	<td width="10%"> </td>
	<td><b>Data:</b></td>
	<td>
		<html:text property="data" styleClass="input" style="width: 70px;"/>
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.QueryAddressForm.data, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"> </td>
	<td width="30%"><b>Adresas:</b></td>
	<td width="60%">
		<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true" style="width:197px;background-color: #dddddd;"/>
		<input type="button" class="button" value="Pasirinkti adres�..." onclick="openPopup('addressbrowser.do')" style="width: 100px;"/>
		<html:hidden property="addressTer" styleId="addressTer" />
		<html:hidden property="addressAdr" styleId="addressAdr" />
		<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
		<br />
		<logic:present name="badAddressString"><span class="error">Nepasirinktas galiojantis adresas</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"> </td>
	<td valign="top"><b>Duomen� per�i�ros tikslas:</b></td>
	<td>
		<html:select property="priezKodas" styleClass="input">
			<html:options collection="priezastys" property="kodas" labelProperty="pavadinimas" /> 
		</html:select>
	</td>
</tr>
<tr>
	<td width="10%"> </td>
	<td valign="top">Prie�astis:</td>
	<td>
		<html:textarea property="priezAprasymas" styleClass="input" rows="3" cols="20" />
	</td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<hr />
		<html:submit value="Ie�koti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos �vedamos formatu <i>"metai-m�nuo-diena" (2006-09-01)</i>.
</div>