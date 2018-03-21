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
			if (addressButton) addressButton.disabled = true;
			if (addressStringBox) addressStringBox.disabled = true;
			if (valstybesBox) valstybesBox.disabled = false;
			if (valstybePastBox) valstybePastBox.disabled = false;
		}
		else if (adrType == 'R'){	
			if (addressButton) addressButton.disabled = false;
			if (addressStringBox) addressStringBox.disabled = false;
			if (valstybesBox) valstybesBox.disabled = true;
			if (valstybePastBox)valstybePastBox.disabled = true;
		}	
		else {
			if (addressButton) addressButton.disabled = true;
			if (addressStringBox) addressStringBox.disabled = true;
			if (valstybesBox) valstybesBox.disabled = true;
			if (valstybePastBox) valstybePastBox.disabled = true;
		}
	}
-->
</script>

<div class="heading">Gyvenamosios vietos duomenø koregavimas</div>
<html:form action="queryeditdata" method="post">
<html:hidden property="gvtNr" />
<html:hidden property="gvtAsmNr" />
<logic:equal name="gv_keitimas_mode" value="show_confirm">
	<html:hidden property="confirm" value="1" />
	<div style="padding: 20px; text-align: center; font-weight: bold;">
		<br />
		Ar tikrai norite atlikti ðá pakeitimà?
		<br /><br />
	</div>
	<hr />
	<div align="center">
		<html:submit value="Taip, atlikti pakeitimà" styleClass="button" />
		<input type="button" class="button" value="Atðaukti" onclick="goToUrl('querypersonform.do')" />
	</div>
</logic:equal>
<logic:notEqual name="gv_keitimas_mode" value="show_confirm">
	<html:hidden property="confirm" value="0" />
</logic:notEqual>
<logic:equal name="gv_keitimas_mode" value="show_error">
	<div class="error">
		<bean:write name="message" /><br />
		<br /><br />
	</div>
	<hr />
	<div align="center">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	</div>
</logic:equal>

<logic:equal name="gv_keitimas_mode" value="show_input">
	<div>
</logic:equal>
<logic:notEqual name="gv_keitimas_mode" value="show_input">
	<div style="display: none;">
</logic:notEqual>

<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="25%"><b>Áraðas galiojo nuo:</b></td>
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
	<logic:equal name="actGyvenamojiVieta" property="gvtTipas" value="R">
		<html:radio property="gvtTipas" value="R" onclick="enableAddress('R');">Deklaruota gyvenamoji vieta Lietuvoje</html:radio>
		<br />
		<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true"/>
		<input type="button" class="button" value="Pasirinkti adresà..." onclick="openPopup('addressbrowser.do')" style="width: 100px;" id="addressButton"/>
		<html:hidden property="addressTer" styleId="addressTer" />
		<html:hidden property="addressAdr" styleId="addressAdr" />
		<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
		<br />
		<logic:present name="errAdresas"><span class="error">Neávestas galiojantis adresas</span></logic:present>
	</logic:equal>
	</td>
</tr>
<logic:equal name="actGyvenamojiVieta" property="gvtTipas" value="V">
<tr>
	<td></td>
	<td></td>
	<td>
		<html:radio property="gvtTipas" value="V" onclick="enableAddress('V')">Iðvyko á uþsiená</html:radio>
		<br />
		<html:select property="valstybe" styleClass="input" styleId="valstybes">
			<html:options collection="valstybes" property="kodas" labelProperty="pavadinimas" />
		</html:select>
		<html:textarea property="valstybePast" styleClass="input" rows="1" styleId="valstybePast" />
	</td>	
</tr>
</logic:equal>
<logic:equal name="actGyvenamojiVieta" property="gvtTipas" value="K">
<tr>
	<td></td>
	<td></td>
	<td>
		<html:radio property="gvtTipas" value="K" onclick="enableAddress('K')">Átrauktas á gyv. vietos nedeklaravusiø asmenø apskaità</html:radio>
		<br />
	</td>
</tr>
</logic:equal>
<tr>
	<td></td>
	<td></td>
	<td>
		<html:radio property="gvtTipas" value="O" onclick="enableAddress('O')">Nedeklaruota gyvenamoji vieta (panaikinti duomenys)</html:radio>
		<br />
	</td>
</tr>
<tr>
	<td colspan="3" align="center">	
		<hr /> 
	</td>
</tr>
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
enableAddress('<bean:write name="QueryEditDataForm" property="gvtTipas" />');
//-->
</script>