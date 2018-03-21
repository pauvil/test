<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Registruoti paþymà apie átraukimà á GVNA apskaità</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="gvnapazyma" method="get">
<tr>
	<td width="50"></td>
	<td width="280"><b>Paþymos data:</b></td>
	<td>
		<html:text property="data" styleClass="input" style="width: 70px;" disabled="true"/>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="50"></td>
	<td width="280">Praðymo, pagal kurá iðduodama paþyma, data:</td>
	<td>
		<html:text property="prasymoData" styleClass="input" style="width: 70px;"/>
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.PazymaForm.prasymoData, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errPrasymoData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td>Praðymo, pagal kurá iðduodama paþyma, registracijos numeris</td>
	<td>
		<html:text property="prasymoRegNr" styleClass="input" maxlength="50" />
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Asmens, praðanèio paþymos, kodas:</b></td>
	<td>
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" />
		<logic:present name="errAsmKodas"><br /><span class="error">Negaliojantis asmens kodas</span></logic:present>
		<logic:present name="error.asmuoMires"><br /><span class="error">Negalima iðduoti paþymos mirusiam asmeniui</span></logic:present>
		<logic:present name="error.neprieinamas"><br /><span class="error">Negalima suformuoti paþymos. Asmens gyvenamoji vieta deklaruota kitoje savivaldybëje.</span></logic:present>
		<logic:present name="error.pazymaSiandienIsduota"><br /><span class="error">Ðiandien paþyma jau iðduota. Jei reikia dar vienos paþymos, padarykite kopijà ir patvirtinkite</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Á paþymà átraukti nepilnameèiø vaikø duomenis</b></td>
	<td>
		<html:checkbox property="vaikai" />
	</td>

</tr>
<tr>
	<td></td>
	<td>Pastabos:</td>
	<td>
		<html:textarea property="pastabos" styleClass="input" cols="25" rows="3" />
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