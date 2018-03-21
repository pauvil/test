<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/as" prefix="as" %>
<%@ page import="java.util.*,
				 com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.utils.*" %>
<% Ordering ordering = (Ordering)session.getAttribute("temp_citizens_ordering"); %>
<div class="heading">Asmenys, deklaravæ gyvenamàjà vietà, bet neregistruoti Gyventojø registre</div>
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="20%"><%= ordering.printOrdering("vardas", "Vardas") %></th>
    <th width="20%"><%= ordering.printOrdering("pavarde", "Pavardë") %></th>
    <th width="15%"><%= ordering.printOrdering("pilietybe.pavadinimas", "Pilietybë") %></th>
    <th width="15%"><%= ordering.printOrdering("insDate", "Reg. data") %></th>
    <th width="15%"><%= ordering.printOrdering("gimimoData", "Gimimo data") %></th>
    <th width="15%">Veiksmai</th>
</tr>

<%
		List l = (List)request.getAttribute("tempCitizens");
	    for(int i=0;i<l.size();i++)
	    {
			LaikinasAsmuo la = (LaikinasAsmuo)l.get(i);
			String[] s = CalendarUtils.getDateFromTimestamp(la.getInsDate());
			String[] s1 = CalendarUtils.getDateFromTimestamp(la.getGimimoData());			
%>
	<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td><%=la.getVardas()%></td>
	<td><%=la.getPavarde()%></td>	
	<td><%=la.getPilietybe().getPilietybe()%></td>
	<td align="center"><%=s[0]%>-<%=s[1]%>-<%=s[2]%></td>	
	<td align="center"><%=s1[0]%>-<%=s1[1]%>-<%=s1[2]%></td>
		<td align="center">
			<a href="<%=request.getContextPath()%>/TempCitizen.do?id=<%=la.getId()%>">Perþiûrëti</a>
		</td>		
	</tr>
<%}%>
</tr>
</table>
<hr />