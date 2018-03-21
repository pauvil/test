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
	
	function enableAddress(adrType)
	{
		var addressButton = document.getElementById('addressButton');
		var addressStringBox = document.getElementById('addressString');
		var valstybesBox = document.getElementById('valstybes');
		var valstybePastBox = document.getElementById('valstybePast');
		if (adrType == 'V'){
			addressButton.disabled = true;
			addressStringBox.disabled = true;
			valstybesBox.disabled = false;
			valstybePastBox.disabled = false;
		}
		else if (adrType == 'R'){	
			addressButton.disabled = false;
			addressStringBox.disabled = false;
			valstybesBox.disabled = true;
			valstybePastBox.disabled = true;
		}	
		else {
			addressButton.disabled = true;
			addressStringBox.disabled = true;
			valstybesBox.disabled = true;
			valstybePastBox.disabled = true;
		}
	}
-->
</script>

<div class="heading">Naujo gyvenamosios vietos duomenø áraðo sukûrimas</div>
<html:form action="querycreatedata" method="post">
<html:hidden property="gvtNr" />
<html:hidden property="gvtAsmNr" />
<input type="hidden" name="gvtTipas" value="R" />
<logic:present name="message">
	<html:hidden property="confirm" value="1" />
	<div style="padding: 20px; text-align: center; font-weight: bold;">
		<bean:write name="message" /><br />
		<br /><br />
	</div>
	<hr />
	<div align="center">
		<input type="button" class="button" value="Atgal" onclick="history.go(-1);" />
	</div>
</logic:present>
<logic:notPresent name="message">
	<html:hidden property="confirm" value="0" />
</logic:notPresent>
<div <logic:present name="message">style="display: none;"</logic:present>>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="25%"><b>Áraðas galioja nuo:</b></td>
	<td>
		<html:text property="dataNuo" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.QueryEditDataForm.dataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Áraðas galiojo iki:</b></td>
	<td>
		<html:text property="dataIki" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.QueryEditDataForm.dataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataIki"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Gyvenamoji vieta:</b></td>
	<td>
		<%--<html:radio property="gvtTipas" value="R" onchange="enableAddress('R');">Deklaruota gyvenamoji vieta Lietuvoje</html:radio>
		<br />--%>
		<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true"/>
		<input type="button" class="button" value="Pasirinkti adresà..." onclick="openPopup('addressbrowser.do')" style="width: 100px;" id="addressButton"/>
		<html:hidden property="addressTer" styleId="addressTer" />
		<html:hidden property="addressAdr" styleId="addressAdr" />
		<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
		<br />
		<logic:present name="errAdresas"><span class="error">Neávestas galiojantis adresas</span></logic:present>
	</td>
</tr>
<%--<tr>
	<td></td>
	<td></td>
	<td>
		<html:radio property="gvtTipas" value="V" onchange="enableAddress('V')">Iðvyko á uþsiená</html:radio>
		<br />
		<html:select property="valstybe" styleClass="input" styleId="valstybes">
			<html:options collection="valstybes" property="kodas" labelProperty="pavadinimas" />
		</html:select>
		<html:textarea property="valstybePast" styleClass="input" rows="1" styleId="valstybePast" />
	</td>	
</tr>
<tr>
	<td></td>
	<td></td>
	<td>
		<html:radio property="gvtTipas" value="K" onchange="enableAddress('K')">Átrauktas á gyv. vietos nedeklaravusiø asmenø apskaità</html:radio>
		<br />
	</td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td>
		<html:radio property="gvtTipas" value="O" onchange="enableAddress('O')">Nedeklaruota gyvenamoji vieta (panaikinti duomenys)</html:radio>
		<br />
	</td>
</tr>--%>
<tr>	
</tr>	
	<td colspan="3" align="center">
		<hr />
	</td>	
<tr>	
	<td align="left">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	</td>
	<td colspan="2" align="center">
		<html:submit value="Iðsaugoti" styleClass="button" />
	</td>
</tr>
</table>
</html:form>
<script language="Javascript">
<!--
enableAddress('R');
//-->
</script>