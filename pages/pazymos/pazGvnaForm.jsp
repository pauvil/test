<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Registruoti pa�ym� apie �traukim� � GVNA apskait�</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="gvnapazyma" method="get">
<tr>
	<td width="50"></td>
	<td width="280"><b>Pa�ymos data:</b></td>
	<td>
		<html:text property="data" styleClass="input" style="width: 70px;" disabled="true"/>
		<logic:present name="errData"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="50"></td>
	<td width="280">Pra�ymo, pagal kur� i�duodama pa�yma, data:</td>
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
	<td>Pra�ymo, pagal kur� i�duodama pa�yma, registracijos numeris</td>
	<td>
		<html:text property="prasymoRegNr" styleClass="input" maxlength="50" />
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Asmens, pra�an�io pa�ymos, kodas:</b></td>
	<td>
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" />
		<logic:present name="errAsmKodas"><br /><span class="error">Negaliojantis asmens kodas</span></logic:present>
		<logic:present name="error.asmuoMires"><br /><span class="error">Negalima i�duoti pa�ymos mirusiam asmeniui</span></logic:present>
		<logic:present name="error.neprieinamas"><br /><span class="error">Negalima suformuoti pa�ymos. Asmens gyvenamoji vieta deklaruota kitoje savivaldyb�je.</span></logic:present>
		<logic:present name="error.pazymaSiandienIsduota"><br /><span class="error">�iandien pa�yma jau i�duota. Jei reikia dar vienos pa�ymos, padarykite kopij� ir patvirtinkite</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>� pa�ym� �traukti nepilname�i� vaik� duomenis</b></td>
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
		<html:submit styleClass="button" value="Formuoti pa�ym�"/>
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos �vedamos formatu <i>"metai-m�nuo-diena" (2006-09-01)</i>.
</div>