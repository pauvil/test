<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>

<div class="heading">Paieðkos pagal adresà rezultatai</div>
<%
    Ordering ordering = (Ordering)session.getAttribute("query_address_results_ordering");
%>
<table cellpadding="2" cellspacing="2" width="80%">
<tr>
	<td width="30%" align="right">Pasirinktas adresas:</td>
	<td><b><bean:write name="addressString" /></b></td>
</tr>
</table>

<hr />
<br />
<br />
<logic:notEmpty name="gyventojai">
<div class="heading">Ðiame adrese savo gyvenamàjà vietà yra deklaravæ ðie asmenys:</div>
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="5%">Nr.</th>
    <th width="15%"><%= ordering.printOrdering("ASM_KODAS", "Asmens Kodas arba gimimo data") %></th>
    <th width="30%"><%= ordering.printOrdering("AVD_VARDAS", "Vardas") %></th>
    <th width="30%"><%= ordering.printOrdering("AVD_PAVARDE", "Pavardë") %></th>
    <th width="10%">Deklaravimo data</th>
    <th width="10%">Veiksmai</th>
</tr>
<% int i = 1; %>
<logic:iterate name="gyventojai" id="asmuo">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td align="center"><%= i++ %>.</td>
	<td align="center">	
		<logic:present name="asmuo" property="asmKodas">
			<bean:write name="asmuo" property="asmKodas"/>
		</logic:present>
		<logic:notPresent name="asmuo" property="asmKodas">
			<bean:write name="asmuo" property="asmGimData" format="yyyy-MM-dd"/>
		</logic:notPresent>
	</td>
	<td><bean:write name="asmuo" property="vardas" /></td>
	<td><bean:write name="asmuo" property="pavarde" /></td>
	<td><logic:present name="asmuo" property="lastDeklaravimoData" >
		<bean:write name="asmuo" property="lastDeklaravimoData" />
		</logic:present>
	</td>
	<td align="center">
		<logic:present name="asmuo" property="asmKodas">
			<a href="../querypersonresults.do?asmKodas=<bean:write name="asmuo" property="asmKodas" />">Perþiûrëti</a>
		</logic:present>
	</td>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="gyventojai">
	<div class="heading">Informacija</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1">Nëra asmenø, kurie ðiuo metu bûtø deklaravæ savo gyvenamàjà vietà ðiame adrese</td>
	</tr>
	</table>
	</div>
	<br/>
</logic:empty>
<hr />
&nbsp;<input type="button" class="button" value="&laquo; Atgal" onclick="history.go(-1)" style="width: 100px;" />