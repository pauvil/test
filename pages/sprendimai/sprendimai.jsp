<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*,java.util.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">Sprendimai keisti deklaravimo duomenis</div>
<%
    Ordering ordering = (Ordering)session.getAttribute("sprendimai_ordering");
    Paging paging = (Paging)session.getAttribute("sprendimai_paging"); 
%>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="sprendimai" method="get">
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
	<td><b>Sprendimo tipas:</b></td>
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

<logic:present name="sprendimai">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%"><%= ordering.printOrdering("data, id", "Data") %></th>
    <th width="10%"><%= ordering.printOrdering("regNr", "Reg. numeris") %></th>
    <th width="25%"><%= ordering.printOrdering("prieme", "Sprendimà priëmë") %></th>
    <th width="25%"><%= ordering.printOrdering("istaiga.pavadinimas", "Savivaldybë, seniûnija") %></th>
    <th width="15%"><%= ordering.printOrdering("tipas", "Tipas") %></th>
    <th width="15%">Veiksmai</th>
</tr>
<logic:iterate name="sprendimai" id="sprendimas">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td><bean:write name="sprendimas" property="data" format="yyyy-MM-dd" /></td>
	<td><bean:write name="sprendimas" property="regNr" /></td>
	<td><bean:write name="sprendimas" property="prieme" /></td>
	<td><bean:write name="sprendimas" property="istaiga.pavadinimas" /></td>
	<td><bean:write name="sprendimas" property="calcTipasStr" /></td>
	<td align="center">
		<a href="../viewsprendimas.do?id=<bean:write name="sprendimas" property="id" />">Perþiûrëti</a>
		<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
		|
		<a href="../editsprendimasform.do?id=<bean:write name="sprendimas" property="id" />">Redaguoti</a>
		<% } 
		   if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
		|
		<a href="../DeleteSprendimas.do?id=<bean:write name="sprendimas" property="id" />" onclick="return confirm('Ar tikrai iðtrinti ðá sprendimà?');">Iðtrinti</a>	
		<%	}	%>
	</td>
</tr>
</logic:iterate>
</table>
<hr />
<center><% if (paging != null) out.print(paging.printPaging()); %></center>
</logic:present>