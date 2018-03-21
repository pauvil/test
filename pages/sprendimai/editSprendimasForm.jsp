<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
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
	function disableSaugotiButton(){
		var isButton = document.getElementById('saugotiButton');
		isButton.disabled = true;	
		return true;		
	}
	function enableAddress(adrType)
	{
		var addressButton = document.getElementById('addressButton');
		var addressStringBox = document.getElementById('addressString');
		var valstybesBox = document.getElementById('valstybes');
		var valstybePastBox = document.getElementById('valstybePast');
		if (adrType == 1){
			addressButton.disabled = true;
			addressStringBox.disabled = true;
			valstybesBox.disabled = false;
			valstybePastBox.disabled = false;
		}
		else {	
			if (addressButton!=null) {
				addressButtton.disabled = false;
			}
			if (addressStringBox!=null) {
				addressStringBox.disabled = false;
			}
			if (valstybesBox!=null) {
				valstybesBox.disabled = true;
			}
			if (valstybePastBox!=null) {
				valstybePastBox.disabled = true;
			}
		}	
	}
-->
</script>

<div class="heading">Redaguoti spendimà dël deklaravimo duomenø keitimo</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="editsprendimas" method="post" onsubmit="disableSaugotiButton();">
<html:hidden property="id" />
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Sprendimo registracijos numeris:</b></td>
	<td>
		<html:text property="regNr" styleClass="input" maxlength="50" />
		<logic:present name="errRegNr"><span class="error">Bûtina uþpildyti</span></logic:present>
		<logic:present name="errRegNrDouble"><span class="error">Toks registracijos numeris duomenø bazëje jau yra</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Sprendimo data:</b></td>
	<td>
		<bean:write name="actSprendimas" property="data" format="yyyy-MM-dd" />
<%-- 
		<html:text property="data" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.SprendimasForm.data, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}  
		//-->
 	</script>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
 --%>		
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Prieþastis:</b></td>
	<td>
		<html:select property="priezastis" styleClass="input">
			<html:options collection="priezastys" property="id" labelProperty="pavadinimas" /> 
		</html:select>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Sprendimo tipas:</b></td>
	<td><bean:write name="actSprendimas" property="calcTipasStr" /></td>
</tr>
<tr>
	<td></td>
	<td>
		<b>Asmenys, kuriø deklaravimo duomenys yra keièiami:</b>
	</td>
	<td>
		<div id="dynAsmenys" style="border: 1px solid #7D7D7D; width: 400px;background-color: #dddddd;" > 
		<span class="error">	
			<logic:present name="error.neprieinamasNaikinimuiNepriklausoIstaigai">
				Asmens/asmenø gyvenamoji vieta nepriklauso ástaigos teritorijai arba asmuo nëra deklaravæs gyvenamosios vietos. Sprendime palikite tik tuos asmenis, kuriø duomenis galima naikinti/keisti.
			</logic:present> 
			<logic:present name="error.deletingEmtptyGvna">
				Asmuo nëra átrauktas á GVNA apskaità. Sprendime palikite tik tuos asmenis, kurie yra átraukti á GVNA apskaità.
			</logic:present> 
			<logic:present name="error.neprieinamasKeitimuiTrukstaDeklaracijos">
					Asmuo/asmenys (
					<bean:write name="SprendimasForm" property="asmenysSuNeteisingaisDuomenimis" />
					) nëra deklaravæ gyvenamosios vietos. Duomenø negalima keisti, taisyti ar naikinti, kol nebus pateiktos gyvenamosios vietos deklaracijos.
			</logic:present>
		</span>
		<%-- <logic:present name="error.neprieinamasNaikinimui"><span class="error">Asmens/asmenø gyvenamoji vieta nepriklauso ástaigos teritorijai arba asmuo nëra deklaravæs gyvenamosios vietos. Sprendime palikite tik tuos asmenis, kuriø duomenis galima naikinti/keisti.</span></logic:present> --%> 
		<ul>
		<logic:iterate name="SprendimasForm" property="asmenys" id="asmId">
		<%
			long id = Long.parseLong((String)pageContext.getAttribute("asmId"));
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
			pageContext.setAttribute("asmuo", asmuo);
		%>
		<li>
			<bean:write name="asmuo" property="vardas"/> <bean:write name="asmuo" property="pavarde"/>, a.k. <bean:write name="asmuo" property="asmKodas"/>
		</li>
		</logic:iterate>
		</ul>
		</div>
	</td>
</tr>
<logic:notEqual name="actSprendimas" property="tipas" value="2">
<tr>
	<td width="10%"></td>
	<td width="35%">Naujas adresas:</td>
	<td>
<%-- 		<html:radio property="addressType" value="0" onchange="enableAddress(0);">Adresas Lietuvoje</html:radio>
		<br />  --%>
		<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true"/>
		<input type="button" class="button" value="Pasirinkti adresà..." onclick="openPopup('addressbrowser.do')" style="width: 100px;" id="addressButton"/>
		<html:hidden property="addressTer" styleId="addressTer" />
		<html:hidden property="addressAdr" styleId="addressAdr" />
		<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
		<br />
		<logic:present name="errAdresas"><span class="error">Neávestas galiojantis adresas</span></logic:present>
	</td>
</tr>

<%-- 
<tr>
	<td width="10%"></td>
	<td width="35%"></td>
	<td>
		<html:radio property="addressType" value="1" onchange="enableAddress(1)">Iðvyko á uþsiená</html:radio>
		<br />
		<html:select property="valstybe" styleClass="input" styleId="valstybes">
			<html:options collection="valstybes" property="kodas" labelProperty="pavadinimas" />
		</html:select>
		<html:textarea property="valstybePast" styleClass="input" rows="1" styleId="valstybePast" />
	</td>	
</tr>
 --%>
 
</logic:notEqual>
<tr>
	<td width="10%"></td>
	<td width="35%">Data, nuo kurios naikinami ankstesni deklaravimo duomenys:</td>
	<td>
		<bean:write name="actSprendimas" property="naikinimoData" format="yyyy-MM-dd" />
		<logic:present name="error.decizionDateLessThanDeclaration"><span class="error">Negalima registruoti sprendimo nuo ðios datos - yra deklaracijø, ávestø vëliau.</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="35%">Sprendimà priëmæs asmuo/institucija:</td>
	<td><html:textarea property="prieme" styleClass="input" rows="2" /></td>
</tr>
<tr>
	<td></td>
	<td>Pastabos:</td>
	<td><html:textarea property="pastabos" styleClass="input" rows="3" /></td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<html:submit value="Iðsaugoti" styleClass="button" styleId="saugotiButton"/>
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>
<script language="Javascript">
<!--
enableAddress(<bean:write name="SprendimasForm" property="addressType" />);
//-->
</script>