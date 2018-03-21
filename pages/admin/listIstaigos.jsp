<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>

<div class="heading">GVDIS sistemoje registruotos savivaldybës ir seniûnijos</div>
<%
    Ordering ordering = (Ordering)session.getAttribute("istaigos_ordering");
    Paging paging = (Paging)session.getAttribute("istaigos_paging"); 
%>

<logic:present name="istaigos">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%"><%= ordering.printOrdering("id", "Numeris") %></th>
    <th width="20%"><%= ordering.printOrdering("pavadinimas", "Pavadinimas") %></th>
    <th width="20%">Priklauso</th>
    <th width="30%"><%= ordering.printOrdering("oficialusPavadinimas", "Oficialus pavadinimas") %></th>
    <th width="30%">Kiti duomenys</th>
    <th width="10%">Veiksmai</th>
</tr>
<logic:iterate name="istaigos" id="istaiga">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td><bean:write name="istaiga" property="id" /></td>
	<td><bean:write name="istaiga" property="pavadinimas" /></td>
	<td>
		<logic:present name="istaiga" property="istaiga">
			<bean:write name="istaiga" property="istaiga.pavadinimas" />
		</logic:present>
	</td>
	<td><bean:write name="istaiga" property="oficialusPavadinimas" /></td>
	<td><bean:write name="istaiga" property="rekvizSpausdinimui" /></td>
	<td align="center"><a href="../editistaigaform.do?id=<bean:write name="istaiga" property="id" />">Redaguoti</a></td>
</tr>
</logic:iterate>
</table>
<hr />
<center><% if (paging != null) out.print(paging.printPaging()); %></center>
</logic:present>