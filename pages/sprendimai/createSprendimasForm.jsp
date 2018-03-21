<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>
<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<script language="Javascript">
<!--
	function deleteAsm(asmNr)
	{
		var delDiv = document.getElementById('asm' + asmNr);
		delDiv.parentNode.removeChild(delDiv);
		var delHidden = document.getElementById('hidden_' + asmNr);
		delHidden.name = 'deleted';
	}
	
	function disablePrasymasButton(){
		var prButton = document.getElementById('prasymasButton');
		prButton.disabled = true;			
	}
	function disableSaugotiButton(){
		var isButton = document.getElementById('saugotiButton');
		isButton.disabled = true;	
		return true;		
	}
	function setTipasSelect(tipas){
		var selectTipas = document.getElementById('tipas');	
		selectTipas.options.length=0
		var arr = new Array("Taisyti duomenis", "Keisti duomenis", "Naikinti duomenis", "Naikinti GVNA duomenis");		
		selectTipas.options[0]=new Option(arr[tipas], tipas, true, true);
	}
	function setPriezastisSelect(priezastis){
		var selectPriezastis = document.getElementById('priezastis');	
		var tekstai = new Array();
		for(var i = 0; i< selectPriezastis.options.length; i++){
			//tekstai[i] = selectPriezastis.options[i].firstChild.data;	
			if(selectPriezastis.options[i].value == priezastis)	
				selectPriezastis.options[i] = new Option(selectPriezastis.options[i].firstChild.data, priezastis, true, true);
		}
		//selectPriezastis.options.length=0
		
		
	}
	function clearAsmenys(){			
		var x = document.getElementsByTagName("input");
		//var test = 0;		
		for(var i = 0; i< x.length; i++){
			if(x[i].name == 'asmenys[]' && x[i].type == 'hidden'){	
				//test++;	
				x[i].name = 'deleted';	
			}	
		}	
		//alert(test);
		document.getElementById('dynAsmenys').innerHTML = "";		
	}
	
	function addAsm(asmNr, vardas, pavarde, asmKodas, busena, adresas, denyRemove)
	{
		var addDiv = document.getElementById('dynAsmenys');
		var str = '<div id="asm' + asmNr + '">';
		str += '<table cellpadding="2" cellspacing="2" width="100%"><tr>';
		str += '<td valign="center">&bull;</td>';
		str += '<td>' + vardas + ' ' + pavarde + ', a.k. ' + asmKodas + '<br />' + busena +  '<br />';
		if(adresas) str += adresas;
		str +='</td>';
		str += '<td align="right" valign="center">';
		if(!denyRemove){
			str += '<a href="javascript:deleteAsm(' + asmNr  + ')">Panaikinti&nbsp;&raquo;&nbsp;</a>';			
		}
		
		str += '</div></td></tr></table></div>';
		document.getElementById('dynAsmenys').innerHTML += str;
	
		var aForm = document.SprendimasForm;
		var aHidden = document.createElement('input');
		aHidden.setAttribute('type', 'hidden');
		aHidden.setAttribute('name', 'asmenys[]');
		aHidden.setAttribute('value', asmNr);
		aHidden.setAttribute('id', 'hidden_' + asmNr);
		aForm.appendChild(aHidden);
	}

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
	function setPrasymas(regNr, id, tipas)
	{
		var pt = document.getElementById('praText');
		var ph = document.getElementById('praHid');						
		pt.value = regNr;
		ph.value = id;		
		setTipasSelect(tipas);
		if(tipas == 2)
			setPriezastisSelect('36'); //Savininko prasimu
		var fh = document.getElementById('fixTipasHid');	
		fh.value = tipas;		
		flipAddressDiv();			
	}
	function flipAddressDiv()
	{
		var tipasBox = document.getElementById('tipas');
		var tipasVal = tipasBox.value;
		var keitDiv = document.getElementById('keit');
		var naikDiv = document.getElementById('naikinamasAdresas');
		if (tipasVal == 2 || tipasVal == 3){
			keitDiv.style.display = 'none';
			naikDiv.style.display = 'block';
		}
		else {
			keitDiv.style.display = 'block';
			naikDiv.style.display = 'none';
		}
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
				addressButton.disabled = false;
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

	function popUpC(ctl, ctl2, ctl3, format) {
		var priezastisBox = document.getElementById('priezastis');
		var priezastisVal = priezastisBox.value;
		<% if (!roles.contains("RL_GVDIS_GL_TVARK")){ %>
			if (priezastisVal != 35) {
				popUpCalendar1(ctl, ctl2, ctl3, format);
			}
			else {
				popUpCalendar(ctl, ctl2, format);	
			}
		<% } else { %>
			popUpCalendar(ctl, ctl2, format);
		<% } %>
	}
-->
</script>

<% if (!roles.contains("RL_GVDIS_GL_TVARK")){ %>
<script language="Javascript">
<!--

	function changeDate() 
	{
		var naikinimoData = document.getElementById('naikinimoData');
		var sprendimoData = document.getElementById('data');
		var priezastisBox = document.getElementById('priezastis');
		var priezastisVal = priezastisBox.value;
		if (priezastisVal != 35) {
			naikinimoData.value = sprendimoData.value;
		}
	}
	
	function flipDate()
	{
		var priezastisBox = document.getElementById('priezastis');
		var priezastisVal = priezastisBox.value;
		var popUpCal = document.getElementById('popUpCal');
		var naikinimoData = document.getElementById('naikinimoData');
		var data = document.getElementById('data');

		if (priezastisVal != 35) {
			/*da = new Date();
			ys = new String(da.getFullYear());
			ms = new String(da.getMonth() + 1); 	 
			ds = new String(da.getDate()); 	 
			if ( ms.length == 1 ) ms = "0" + ms;
			if ( ds.length == 1 ) ds = "0" + ds; 	 	
			naikinimoData.value = ys + "-" + ms + "-" + ds;*/
			naikinimoData.value = data.value;
			naikinimoData.disabled = true;
			popUpCal.style.display = 'none';
		}
		else {
			naikinimoData.disabled = false;
			popUpCal.style.display = 'inline';		
		}
	}
-->
</script>
<% } else { %>
<script language="Javascript">
<!--
	function flipDate()
	{
	}
	function changeDate()
	{
	}
-->
</script>
<% } %>

<html:form action="createsprendimas" method="post" onsubmit="disableSaugotiButton();" >
<div class="heading">Registruoti spendimà dël deklaravimo duomenø keitimo</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:hidden property="fixTipas" styleId="fixTipasHid"/>
<tr>
	<td width="10%"> 
	</td>
	<td width="35%"><b>Sprendimo registracijos numeris:</b></td>
	<td>
		<html:text property="regNr" styleClass="input" maxlength="50" />
		<logic:present name="errRegNr"><span class="error">Bûtina uþpildyti</span></logic:present>
		<logic:present name="errRegNrDouble"><span class="error">Toks registracijos numeris duomenø bazëje jau yra</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Praðymas:</b></td>
	<td>
		<html:text styleId="praText" property="prasymoPavadinimas" readonly="true" styleClass="input" style="width:197px;background-color: #dddddd;" maxlength="50" />
		<input type="button" value="Pasirinkti praðymà" class="button" onclick="openPopup('addPrasymas.do')" style="width: 100px;" id="prasymasButton"/>	
		<html:hidden styleId="praHid" property="prasymas"/>		
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td width="35%"></td>
	<td>
		<logic:present name="errPrasymas"><span class="error">Pasirinkite praðymà</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Sprendimo data:</b></td>
	<td>
		<html:text property="data" styleId="data" styleClass="input" style="width: 70px;" onkeyup="changeDate()"/>
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a id='popUpCal1' style='cursor:pointer;' onclick='popUpC(this, document.SprendimasForm.data, document.SprendimasForm.naikinimoData, \"yyyy-mm-dd\");'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
		<logic:present name="error.biggerCreateData"><br><span class="error">Sprendimo data negali bûti vëlesnë nei ðios dienos data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Prieþastis:</b></td>
	<td>
		<html:select property="priezastis" styleClass="input" styleId="priezastis" onchange="flipDate();">
			<html:options collection="priezastys" property="id" labelProperty="pavadinimas" /> 
		</html:select>
	</td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Sprendimo tipas:</b></td>
	<td>		
			<logic:empty name="SprendimasForm" property="fixTipas">
				<html:select property="tipas" styleClass="input" styleId="tipas" onchange="flipAddressDiv();">
					<html:option value="0">Taisyti duomenis</html:option>
					<html:option value="1">Keisti duomenis</html:option>
					<html:option value="2">Naikinti duomenis</html:option>
				</html:select>
			</logic:empty>
			<logic:notEmpty name="SprendimasForm" property="fixTipas">
				<html:select property="tipas" styleClass="input" styleId="tipas" onchange="flipAddressDiv();">					
				</html:select>
				<script language="Javascript">
				<!--				
					setTipasSelect(<bean:write name="SprendimasForm" property="fixTipas"/>);
				//-->
				</script>
			</logic:notEmpty>		
	</td>
</tr>
<tr>
	<td></td>
	<td>
		<b>Asmenys, kuriø deklaravimo duomenys yra keièiami:</b>
		<logic:present name="errAsmenys"><br /><span class="error">Turi bûti pridëtas bent vienas asmuo</span></logic:present>
		
	</td>

	<td>		
		<div id="dynAsmenys" style="border: 1px solid #7D7D7D; width: 400px;" > </div>		
		<span class="error">	
			<logic:present name="error.neprieinamasNaikinimuiNepriklausoIstaigai">
				Asmens/asmenø gyvenamoji vieta nepriklauso ástaigos teritorijai. Sprendime palikite tik tuos asmenis, kuriø duomenis galima naikinti/keisti.
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
<%--
		<logic:present name="error.neprieinamasNaikinimui"><span class="error">Asmens/asmenø gyvenamoji vieta nepriklauso ástaigos teritorijai arba asmuo nëra deklaravæs gyvenamosios vietos. Sprendime palikite tik tuos asmenis, kuriø duomenis galima naikinti/keisti.</span></logic:present> 
		<input type="button" value="Pridëti asmená ..." class="button" onclick="openPopup('addpersonform.do')" style="width: 100px;" />
		<logic:present name="error.deletingEmtptyGv"><br /><span class="error">Negalima naikinti deklaravimo duomenø asmeniui kuris neturi deklaruotos gyvenamosios vietos</span></logic:present>
--%>
	</td>

</tr>
</table>


<div id="keit">
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Naujas adresas:</b></td>
	<td>
		<%--
		<html:radio property="addressType" value="0" onclick="enableAddress(0);">Adresas Lietuvoje</html:radio>
		<br />
		--%>
		<html:text property="addressString" styleClass="input" styleId="addressString" readonly="true" style="width:197px;background-color: #dddddd;"/>
		<input type="button" class="button" value="Pasirinkti adresà..." onclick="openPopup('addressbrowser.do')" style="width: 100px;" id="addressButton"/>
		<html:hidden property="addressTer" styleId="addressTer" />
		<html:hidden property="addressAdr" styleId="addressAdr" />
		<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
		<br />
		<logic:present name="errAdresas"><span class="error">Neávestas galiojantis adresas</span></logic:present>
		<logic:present name="error.galiojantisAdresas"><span class="error">Ðiuo adresu asmuo/asmenys jau yra deklaravæs gyvenamàjà vietà.</span></logic:present> 
	</td>
</tr>
<%--
<tr>
	<td width="10%"></td>
	<td width="35%"></td>
	<td>
		<html:radio property="addressType" value="1" onclick="enableAddress(1)">Iðvyko á uþsiená</html:radio>
		<br />
		<html:select property="valstybe" styleClass="input" styleId="valstybes">
			<html:options collection="valstybes" property="kodas" labelProperty="pavadinimas" />
		</html:select>
		<html:textarea property="valstybePast" styleClass="input" rows="3" styleId="valstybePast" />
	</td>
</tr>
--%>
</table>
</div>


<div id="naikinamasAdresas">
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Naikinamas adresas:</b></td>
	<td>
		<bean:write name="SprendimasForm" property="addressStringNaikinamas" />
		<html:hidden property="addressAdrNaikinamas" styleId="addressAdr"/>
		<html:hidden property="addressTerNaikinamas" styleId="addressTer"/>
		<br/>
		<logic:present name="error.nesutampaNaikinamasAdresas">
			<span class="error">Galiojantis adresas nesutampa su naikinamu adresu</span>
		</logic:present>
	</td>
</tr>
</table>
</div>
<div id="naik">
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Data, nuo kurios naikinami ankstesni deklaravimo duomenys:</b></td>
	<td>
		<html:text property="naikinimoData" styleClass="input" styleId="naikinimoData" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a id='popUpCal' style='cursor:pointer;' onclick='popUpCalendar(this, document.SprendimasForm.naikinimoData, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>

<% if (!roles.contains("RL_GVDIS_GL_TVARK")){ %>
		<script language="javascript">
		<!--
		changeDate();
		if (document.getElementById('priezastis').value != 35) {
			document.getElementById('popUpCal').style.display = 'none';
			document.getElementById('naikinimoData').disabled = true;
		} else {
			document.getElementById('popUpCal').style.display = 'inline';
			document.getElementById('naikinimoData').disabled = false;
		}
		//-->
		</script>
<% } %>
		<logic:present name="errNaikinimoData"><span class="error">Negaliojanti data</span></logic:present>
		<logic:present name="error.decizionDateLessThanDeclaration"><span class="error">Negalima registruoti sprendimo nuo ðios datos - yra deklaracijø, ávestø vëliau.</span></logic:present>
		<logic:present name="error.biggerNaikData"><br><span class="error">Data negali bûti vëlesnë nei ðios dienos data</span></logic:present>
	</td>
</tr>
</table>
</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="35%">Sprendimà priëmæs asmuo/institucija:</td>
	<td><html:textarea onkeypress="return imposeMaxLength(this, 260);" property="prieme" styleClass="input" rows="2" />
	
	<script language="javascript" type="text/javascript">
	<!--
	function imposeMaxLength(Object, MaxLen)
	{
	  return (Object.value.length <= MaxLen);
	}
	//-->
	</script> 
	
	</td>
</tr>
<tr>
	<td></td>
	<td>Pastabos:</td>
	<td><html:textarea onkeypress="return imposeMaxLength(this, 1000);" property="pastabos" styleClass="input" rows="3" /></td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<logic:present name="errPrasymasOccupied"><span class="error">Praðymui jau yra priimtas sprendimas, praðome perþiûrëti praðymus dël duomenø keitimo ið naujo!</span></logic:present>
	</td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<html:submit value="Iðsaugoti" styleClass="button" styleId="saugotiButton"/>
	</td>
</tr>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>
</html:form>
	
<script language="Javascript">
<!--
<logic:iterate name="SprendimasForm" property="asmenys" id="asmId">
<%
	long id = Long.parseLong((String)pageContext.getAttribute("asmId"));
	Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
	pageContext.setAttribute("adresas", QueryDelegator.getInstance().getAsmGvtAdresa(request, asmuo.getAsmNr()));
	pageContext.setAttribute("asmuo", asmuo);
%>
addAsm('<bean:write name="asmuo" property="asmNr"/>', '<bean:write name="asmuo" property="vardas"/>', '<bean:write name="asmuo" property="pavarde"/>', '<bean:write name="asmuo" property="asmKodas"/>', '<bean:write name="asmuo" property="busena"/>', '<logic:present name="adresas"><bean:write name="adresas"/></logic:present>');
</logic:iterate>
<logic:present name="actPrasymasId">
	disablePrasymasButton();
	setTipasSelect(<bean:write name="SprendimasForm" property="tipas"/>);
	var fiksuoti = document.getElementById('fixTipasHid');	
	fiksuoti.value = <bean:write name="SprendimasForm" property="tipas"/>;	
</logic:present>
flipAddressDiv();
enableAddress(<bean:write name="SprendimasForm" property="addressType" />);

//-->
</script>