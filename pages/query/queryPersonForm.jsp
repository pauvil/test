<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Deklaravimo duomenø paieðka pagal asmená</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="querypersonresults" method="get">
<tr>
	<td width="10%"> </td>
	<td width="30%"><b>Asmens kodas:</b></td>
	<td width="60%">
		<html:text property="asmKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" />
		<logic:present name="error.asmKodas"><br /><span class="error">Gyventojo su tokiu asmens kodu nëra</span></logic:present>
				<logic:present name="error.neprieinamas"><br /><span class="error">Jums nesuteiktas priëjimas prie ðio asmens gyvenamosios vietos duomenø. Asmens gyvenamoji vieta deklaruota kitoje savivaldybëje.</span></logic:present> 
	</td>
</tr>
<tr>
	<td width="10%"> </td>
	<td valign="top"><b>Duomenø perþiûros tikslas:</b></td>
	<td>
		<html:select property="priezKodas" styleClass="input">
			<html:options collection="priezastys" property="kodas" labelProperty="pavadinimas" /> 
		</html:select>
	</td>
</tr>
<tr>
	<td width="10%"> </td>
	<td valign="top">Prieþastis:</td>
	<td>
		<html:textarea property="priezAprasymas" styleClass="input" rows="3" cols="20" />
	</td>
</tr>
<tr>	
	<td colspan="3" align="center">
		<hr />
		<html:submit value="Ieðkoti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>
