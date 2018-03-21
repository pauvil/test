<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>

<div class="heading">GVDIS sistemos þinynai</div>
<%
    Ordering ordering = (Ordering)session.getAttribute("zinynai_ordering");
    Paging paging = (Paging)session.getAttribute("zinynai_paging"); 
%>

<logic:present name="zinynai">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="15%"><%= ordering.printOrdering("kodas", "Kodas") %></th>
    <th width="35%"><%= ordering.printOrdering("pavadinimas", "Pavadinimas") %></th>
    <th width="35%"><%= ordering.printOrdering("komentaras", "Apraðymas") %></th>
    <th width="15%">Veiksmai</th>
</tr>
<logic:iterate name="zinynai" id="zinynas">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td><bean:write name="zinynas" property="kodas" /></td>
	<td><bean:write name="zinynas" property="pavadinimas" /></td>
	<td><bean:write name="zinynas" property="komentaras" /></td>
	<td align="center"><a href="../zinynoreiksmes.do?id=<bean:write name="zinynas" property="id" />">Redaguoti reikðmes</a></td>
</tr>
</logic:iterate>
</table>
<hr />
<center><% if (paging != null) out.print(paging.printPaging()); %></center>
</logic:present>