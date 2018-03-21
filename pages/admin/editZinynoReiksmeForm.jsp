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
<%--  <a href="../zinynoreiksmes.do">&laquo; Atgal</a>  --%>
<input type="button" value="&laquo; Atgal" onclick="goToUrl('zinynoreiksmes.do');" class="button" style="width: 100px;" />
<hr/>
<br />
</logic:present>

<div class="heading">Þinyno reikðmës redagavimas</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="editzinynoreiksme" method="post">
<html:hidden property="id"/>
<tr>
	<td width="10%"></td>
	<td><b>Kodas:</b></td>
	<td>
		<html:text property="kodas" styleClass="input" />
		<logic:present name="errKodas"><span class="error">Bûtina uþpildyti</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Pavadinimas:</b></td>
	<td>
		<html:text property="pavadinimas" styleClass="input" />
		<logic:present name="errPavadinimas"><span class="error">Bûtina uþpildyti</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td>Apraðymas:</td>
	<td><html:textarea property="komentaras" styleClass="input" rows="5" /></td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<hr />
		<html:submit value="Iðsaugoti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>