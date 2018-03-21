<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>

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

function deleteAsm(asmNr)
{
	var delDiv = document.getElementById('asm' + asmNr);
	delDiv.parentNode.removeChild(delDiv);
	var delHidden = document.getElementById('hidden_' + asmNr);
	delHidden.name = 'deleted';
}

function addAsm(asmNr, vardas, pavarde, asmKodas, busena, adresas)
{
	var addDiv = document.getElementById('dynAsmenys');
	var str = '<div id="asm' + asmNr + '">';
	str += '<table cellpadding="2" cellspacing="2" width="100%"><tr><td valign="center">&bull;</td><td>';
	str += vardas + ' ' + pavarde + ', a.k. ' + asmKodas + '<br />';
	if(adresas) str += adresas;
	str += '</td><td align="right"><a href="javascript:deleteAsm(' + asmNr  + ')">Panaikinti &raquo;&nbsp;</a></div></td>';
	str += '</tr></table></div>';
	document.getElementById('dynAsmenys').innerHTML += str;
	
	var aForm = document.PrasymasForm;
	var aHidden = document.createElement('input');
	aHidden.setAttribute('type', 'hidden');
	aHidden.setAttribute('name', 'asmenys[]');
	aHidden.setAttribute('value', asmNr);
	aHidden.setAttribute('id', 'hidden_' + asmNr);
	aForm.appendChild(aHidden);
}
function addAsm2(asmNr, vardas, pavarde, asmKodas)
{
	var addDiv = document.getElementById('dynAsmuo');
	var str = addDiv.value;
	str += vardas + ' ' + pavarde + ', a.k. ' + asmKodas + '\n';
	addDiv.value = str;
	
	var aForm = document.PrasymasForm;
	var aHidden = document.createElement('input');
	aHidden.setAttribute('type', 'hidden');
	aHidden.setAttribute('name', 'prasytojai[]');
	aHidden.setAttribute('value', asmNr);
	aHidden.setAttribute('id', 'hidden_' + asmNr);
	aForm.appendChild(aHidden);
}

function flipAddressDiv()
{	
	var tipasVal = document.PrasymasForm.tipas.value;
	var adresasLabel = document.getElementById('adresasLabel');
	var adresasValue = document.getElementById('adresasValue');
	var naikinamasAdresasLabel = document.getElementById('naikinamasAdresasLabel');
	var naikinamasAdresasValue = document.getElementById('naikinamasAdresasValue');

	if (tipasVal == 2){
		adresasLabel.style.display = 'none';
		adresasValue.style.display = 'none';
		naikinamasAdresasLabel.style.display = 'block';
		naikinamasAdresasValue.style.display = 'block';
	} 
	else if (tipasVal == 3){
		adresasLabel.style.display = 'none';
		adresasValue.style.display = 'block';
		naikinamasAdresasLabel.style.display = 'block';
		naikinamasAdresasValue.style.display = 'none';
	}
	else {
		adresasLabel.style.display = 'block';
		adresasValue.style.display = 'block';
		naikinamasAdresasLabel.style.display = 'none';
		naikinamasAdresasValue.style.display = 'none';
	}
}
window.onload = flipAddressDiv;
-->

</script>

<div class="heading">Registruoti praðymà dël duomenø keitimo</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="createprasymas" method="post">
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Praðymo registracijos numeris:</b></td>
	<td>
		<html:text property="regNr" styleClass="input" maxlength="50" />
		<logic:present name="errRegNr"><span class="error">Bûtina uþpildyti</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Praðymo data:</b></td>
	<td>
		<html:text property="data" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.PrasymasForm.data, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Praðymo tipas:</b></td>
	<td>
		<html:select property="tipas" styleClass="input" onchange="flipAddressDiv();">
			<html:option value="0">Taisyti duomenis</html:option>
			<html:option value="1">Keisti duomenis</html:option>
			<html:option value="2">Naikinti duomenis</html:option>
			<html:option value="3">Naikinti GVNA duomenis</html:option>
		</html:select>
	</td>
</tr>
<tr>
	<td></td>
	<td>
		<b>Asmuo, praðantis keisti duomenis:</b><br />
		<small>Jei asmuo juridinis, áveskite juridinio asmens kodà ir pavadinimà</small><br/>
		<small>Jei asmuo fizinis, <u>pridëkite</u> asmená</small>
		<logic:present name="errPrasytojas"><br /><span class="error">Bûtina uþpildyti</span></logic:present>
	</td>
	<td>
		<html:textarea styleId="dynAsmuo" property="prasytojas" styleClass="input" rows="4" /><br/>
		<input type="button" value="Pridëti asmená ..." class="button" onclick="openPopup('addpersonform.do?mode=1')" style="width: 100px;" />
	<html:hidden property="prasytojas" />
	</td>	
	
</tr>
<tr>
	<td></td>
	<td>
		<b>Asmenys, kuriø duomenis praðoma keisti:</b>
		<logic:present name="errAsmenys"><br /><span class="error">Turi bûti pridëtas bent vienas asmuo</span></logic:present>
	</td>
	<td>
		<div id="dynAsmenys" style="border: 1px solid #7D7D7D; width: 300px;background-color: #dddddd;"> </div>
		<input type="button" value="Pridëti asmená ..." class="button" onclick="openPopup('addpersonform.do?mode=2')" style="width: 100px;" />
				<logic:present name="error.deletingEmtptyGvna"><br /><span class="error">Asmuo nëra átrauktas á GVNA apskaità</span></logic:present>
				<logic:present name="error.deletingOwnGvna"><br /><span class="error">Asmuo negali praðyti naikinti savo duomenis</span></logic:present>
	</td>
</tr>	
<tr>
	<td></td>
	<td>Pateikto asmens dokumento duomenys:</td>
	<td><html:textarea property="dokumentas" styleClass="input" rows="2" /></td>
</tr>

<tr>
	<td></td>
	<td>
		<div id="adresasLabel" style="display:block">
			Naujas gyvenamosios vietos adresas:
		</div>
		<div id="naikinamasAdresasLabel" style="display:none">
			<b>Naikinamas adresas:</b><br/>
			<logic:present name="badAddressString"><span class="error">Nepasirinktas galiojantis adresas</span></logic:present>
		</div>
	</td>
	<td>
		<div id="adresasValue" style="display:block">
			<html:textarea property="adresas" styleClass="input" rows="2" />
		</div>
		<div id="naikinamasAdresasValue" style="display:none">
			<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true" style="width:197px;background-color: #dddddd;"/>
			<input type="button" class="button" value="Pasirinkti adresà..." onclick="openPopup('addressbrowser.do')" style="width: 100px;"/>
			<html:hidden property="addressTer" styleId="addressTer" />
			<html:hidden property="addressAdr" styleId="addressAdr" />
			<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
			<br />
		</div>
	</td>
</tr>
<tr>
	<td></td>
	<td>Pastabos:</td>
	<td><html:textarea property="pastabos" styleClass="input" rows="3" /></td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<hr />
		<html:submit value="Iðsaugoti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>
<script language="Javascript">
<!--
<logic:iterate name="PrasymasForm" property="asmenys" id="asmId">
<%
	long id = Long.parseLong((String)pageContext.getAttribute("asmId"));
	Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
	pageContext.setAttribute("asmuo", asmuo);
%>
addAsm('<bean:write name="asmuo" property="asmNr"/>', '<bean:write name="asmuo" property="vardas"/>', '<bean:write name="asmuo" property="pavarde"/>', '<bean:write name="asmuo" property="asmKodas"/>', '<bean:write name="asmuo" property="busena"/>');
</logic:iterate>
//-->
</script>