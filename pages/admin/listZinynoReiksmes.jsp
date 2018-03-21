<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">GVDIS sistemos þinynai</div>
<logic:present name="actZinynas">
<br/>
<br />
<div align="center">
	<b>Pasirinktas þinynas:</b> 
	<bean:write name="actZinynas" property="kodas" /> -
	<bean:write name="actZinynas" property="pavadinimas" />
</div>
<br/>
<%--  <a href="../zinynai.do">&laquo; Atgal</a>  --%>
<input type="button" value="&laquo; Atgal" onclick="goToUrl('zinynai.do');" class="button" style="width: 100px;" />
<hr/>
<br />
</logic:present>

<logic:present name="deleteError">
	<div class="heading">Klaida</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1">Ðios þinyno reikðmës iðtrinti negalima, nes yra jà naudojanèiø áraðø.</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<br />
			<input type="button" value="Atgal" class="button" onclick="goToUrl('zinynoreiksmes.do');" />
		</td>
	</tr>
	</table>
	</div>
	<br/>
	<hr/>
</logic:present>

<logic:present name="reiksmes">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%">Kodas</th>
    <th width="35%">Pavadinimas</th>
    <th width="40%">Apraðymas</th>
    <th width="15%">Veiksmai</th>
</tr>
<logic:iterate name="reiksmes" id="reiksme">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td><bean:write name="reiksme" property="kodas" /></td>
	<td><bean:write name="reiksme" property="pavadinimas" /></td>
	<td><bean:write name="reiksme" property="komentaras" /></td>
	<td align="center">
		<a href="../editzinynoreiksmeform.do?id=<bean:write name="reiksme" property="id" />">Redaguoti</a>
		|
		<a href="../deletezinynoreiksme.do?id=<bean:write name="reiksme" property="id" />" onclick="return confirm('Ar tikrai iðtrinti ðià þinyno reikðmæ?');">Panaikinti</a>
	</td>
</tr>
</logic:iterate>
</table>
</logic:present>
<logic:notPresent name="deleteError">
<div align="right">
<hr />
	<input type="button" class="button" value="Nauja þinyno reikðmë" onclick="goToUrl('createzinynoreiksmeform.do');" />
</div>
</logic:notPresent>
