<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Registruoti asmená, neturintá asmens kodo</div>
<br />
<html:form action="LaikiniAsmenysDeklaracijaPerform">
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<tr>
	<td>&nbsp;</td>
	<td><b>Vardas:</b></td>
	<td>
		<html:text property="vardas" styleClass="input" /> <span class="error">*</span>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Pavardë:</b></td>
	<td>
		<html:text property="pavarde" styleClass="input" /> <span class="error">*</span>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Lytis:</b></td>
	<td>
		<html:radio property="lytis" value="V" />&nbsp; Vyr.&nbsp;&nbsp;&nbsp;<html:radio property="lytis" value="M" />&nbsp; Mot.&nbsp;<span class="error">*</span>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Pilietybë</b></td>
	<td>
		<html:select property="pilietybe" styleClass="input">  
		    <html:options collection="pilietybesbenull" property="kodas" labelProperty="pilietybe" /> <span class="error">*</span>
		</html:select>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Gimimo data:</b></td>
	<td>
		<html:text property="gimimoData" styleClass="input" style="width: 70px;" readonly="true" />
	
	
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.LaikiniAsmenysDeklaracijaForm.gimimoData, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script> 
		
		<span class="error">*</span>
<logic:present name="error.missingGimimoData"><br><span class="error">Nekorektiðka data</span></logic:present>
<logic:present name="error.biggerGimimoData"><br><span class="error">Gimimo data negali bûti vëlesnë nei ðios dienos data</span></logic:present>

	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td valign="top">Pastabos:</td>
	<td>
		<html:textarea property="pastabos" styleClass="input" rows="3" cols="20" />
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr />
	</td>
</tr>
<tr>	
	<td align="left" width="5%">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	</td>
	<td colspan="2" align="center">
		<html:submit styleClass="button" value="Registruoti" />
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr /><span class="error">*</span> <b>þenklu paþymëtus laukus privaloma uþpildyti</b>
	</td>
</tr>
</table>
</html:form>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>