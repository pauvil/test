<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="Javascript">
<!--
	function setAddress(adrVienetas, dummy, terVienetas, addressString)
	{
		var adrBox = document.getElementById('addressAdr');
		var terBox = document.getElementById('addressTer');
		var stringBox = document.getElementById('addressString');
		adrBox.value = adrVienetas;
		terBox.value = terVienetas;
		stringBox.value = addressString;
	}
//-->
</script>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Paþyma patalpø savininkams</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="savpazyma" method="get">
<tr>
	<td width="50"></td>
	<td width="280"><b>Paþymos data:</b></td>
	<td>
		<html:text property="data" styleClass="input" style="width: 70px;" disabled="true"/>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="25%"><b>Patalpos adresas:</b></td>
	<td width="65%">
		<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true" style="background-color: #dddddd;"/>
		<input type="button" class="button" value="Pasirinkti adresà..." onclick="openPopup('addressbrowser.do')" style="width: 100px;"/>
		<html:hidden property="addressTer" styleId="addressTer" />
		<html:hidden property="addressAdr" styleId="addressAdr" />
		<br />
		<span class="error">
		<logic:present name="badAddressString">Nepasirinktas galiojantis adresas</logic:present>
		<logic:present name="error.pazymaSiandienIsduota"><br /><span class="error">Ðiandien paþyma jau iðduota. Jei reikia dar vienos paþymos, padarykite kopijà ir patvirtinkite</span></logic:present>
		<logic:present name="error.pazymaNetikslusAdresas"><br /><span class="error">Paþyma patalpos savininkui neformuojama, jei pasirinktas adresas netikslus. (Tai yra kaimas be namo numerio, ar gatvës ir namo numerio).</span></logic:present>
		</span>
	</td>
</tr>

<tr>
	<td colspan="3" align="center">
		<hr />
		<html:submit styleClass="button" value="Formuoti paþymà"/>
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>