<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*,
	com.algoritmusistemos.gvdis.web.persistence.*,
	java.util.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Praðymai keisti deklaravimo duomenis</div>
<%
    Ordering ordering = (Ordering)session.getAttribute("prasymai_ordering");
    Paging paging = (Paging)session.getAttribute("prasymai_paging"); 
%>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="prasymai" method="get">
<tr>
	<td width="15%">&nbsp;</td>
	<td width="20%"><b>Data nuo:</b></td>
	<td width="65%">
		<html:text property="dataNuo" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.FilterPrasymaiForm.dataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><b>Data iki:</b></td>
	<td>
		<html:text property="dataIki" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.FilterPrasymaiForm.dataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataIki"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Praðymo bûsena:</b></td>
	<td>
		<html:select property="busena" styleClass="input" onchange="document.FilterPrasymaiForm.submit();">
			<html:option value="-1">--- Visi ---</html:option>
			<html:option value="0">Nauji praðymai</html:option>
			<html:option value="1">Pagal kuriuos priimti sprendimai</html:option>
			<html:option value="2">Atmesti praðymai</html:option>
		</html:select>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Praðymo tipas:</b></td>
	<td>
		<html:select property="tipas" styleClass="input" onchange="document.FilterPrasymaiForm.submit();">
			<html:option value="-1">--- Visi ---</html:option>
			<html:option value="0">Taisyti duomenis</html:option>
			<html:option value="1">Keisti duomenis</html:option>
			<html:option value="2">Naikinti duomenis</html:option>
			<html:option value="3">Naikinti GVNA duomenis</html:option>
		</html:select>
	</td>
</tr>
<logic:equal name="userStatus" value="0">
<tr>
	<td></td>
	<td><b>Savivaldybë:</b></td>
	<td>
		<html:select property="savivaldybe" styleClass="input" onchange="document.FilterPrasymaiForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="savivaldybes" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:equal>
<logic:lessThan name="userStatus" value="2">
<tr>
	<td></td>
	<td><b>Seniûnija:</b></td>
	<td>
		<html:select property="seniunija" styleClass="input" onchange="document.FilterPrasymaiForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="seniunijos" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:lessThan>
<tr>	
	<td colspan="3" align="center">
		<html:submit styleClass="button" value="Rodyti" />
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>
<br />
<br />

<logic:present name="deleteError">
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1">Ðio praðymo iðtrinti negalima, nes pagal ðá praðymà jau priimtas sprendimas dël deklaravimo duomenø keitimo.</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<br />
			<input type="button" value="Atgal" class="button" onclick="goToUrl('prasymai.do');" />
		</td>
	</tr>
	</table>
	</div>
	<br/>
	<hr/>
</logic:present>

<logic:present name="prasymai">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%"><%= ordering.printOrdering("data, id", "Data") %></th>
    <th width="10%"><%= ordering.printOrdering("regNr", "Reg. numeris") %></th>
    <th width="19%"><%= ordering.printOrdering("prasytojas", "Asmuo, praðantis keisti duomenis") %></th>
    <th width="15%"><%= ordering.printOrdering("istaiga.pavadinimas", "Savivaldybë, seniûnija") %></th>
    <th width="13%"><%= ordering.printOrdering("tipas", "Tipas") %></th>
    <th width="13%"><%= ordering.printOrdering("busena", "Bûsena") %></th>
    <th width="20%">Veiksmai</th>
</tr>
<logic:iterate name="prasymai" id="prasymas">
<%
	String style = "";
	if (((PrasymasKeistiDuomenis)prasymas).isPasenes()){
		style = "style=\"color: #AA0000; font-weight: bold;\"";
	}
%>
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td <%= style %>><bean:write name="prasymas" property="data" format="yyyy-MM-dd" /></td>
	<td <%= style %>><bean:write name="prasymas" property="regNr" /></td>
	<td <%= style %>><bean:write name="prasymas" property="prasytojas" /></td>
	<td <%= style %>><bean:write name="prasymas" property="istaiga.pavadinimas" /></td>
	<td <%= style %>><bean:write name="prasymas" property="tipasStr" /></td>
	<td <%= style %>><bean:write name="prasymas" property="busenaStr" /></td>
	<td align="center">
		<a href="../viewprasymas.do?id=<bean:write name="prasymas" property="id" />">Perþiûrëti</a>
		<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
		|
		<a href="../editprasymasform.do?id=<bean:write name="prasymas" property="id" />">Redaguoti</a>
		|
		<a href="../deleteprasymas.do?id=<bean:write name="prasymas" property="id" />" onclick="return confirm('Ar tikrai norite iðtrinti ðá praðymà?');">Iðtrinti</a>
		<% } %>
	</td>
</tr>
</logic:iterate>
</table>
<hr />
<center><% if (paging != null) out.print(paging.printPaging()); %></center>
</logic:present>