<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>
<%
    Ordering ordering = (Ordering)session.getAttribute("add_prasymai_ordering");
    Paging paging = (Paging)session.getAttribute("add_prasymai_paging"); 
%>


<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading">
	<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td style="font-size: 14px; font-weight: bold; color: #333333;">
			Praðymo pasirinkimas
		</td>
		<td align="right">
		</td>
	</tr>
	</table>
</div>

<br />
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="addPrasymas" method="get">

<tr>
	
	<td width="30%" align="right"><b>Data nuo:</b></td>
	<td width="20%" align="left">
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
	<td width="10%" align="right"><b>Data iki:</b></td>
	<td width="40%" align="left">
		<html:text property="dataIki" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.FilterPrasymaiForm.dataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		&nbsp<input type="submit" value="Rodyti" class="button" style="width: 100px;"/>
		<logic:present name="errDataIki"><span class="error">Negaliojanti data</span></logic:present>
	</td>   
</tr>
</html:form>
</table>
<br/>
<hr />
<logic:present name="prasymai"> 
<center>
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="99%">
<tr>
    <th width="10%"><%= ordering.printOrdering("data, id", "Data") %></th>
    <th width="10%"><%= ordering.printOrdering("regNr", "Reg. numeris") %></th>
    <th width="19%"><%= ordering.printOrdering("prasytojas", "Asmuo, praðantis keisti duomenis") %></th>
    <th width="15%"><%= ordering.printOrdering("istaiga.pavadinimas", "Savivaldybë, seniûnija") %></th>
    <th width="13%"><%= ordering.printOrdering("tipas", "Tipas") %></th>   
    <th width="20%">Veiksmai</th>
</tr>
<logic:iterate name="prasymai" id="prasymas">
<%
	String style = "";
	if (((PrasymasKeistiDuomenis)prasymas).isPasenes()){
		style = "style=\"color: #AA0000; font-weight: bold;\"";
	}
%>
<tr>
	<td <%= style %>><bean:write name="prasymas" property="data" format="yyyy-MM-dd" /></td>
	<td <%= style %>><bean:write name="prasymas" property="regNr" /></td>
	<td <%= style %>><bean:write name="prasymas" property="prasytojas" /></td>
	<td <%= style %>><bean:write name="prasymas" property="istaiga.pavadinimas" /></td>
	<td <%= style %>><bean:write name="prasymas" property="tipasStr" /></td>	
	<td align="center">
		<a href="../addPrasymasResult.do?id=<bean:write name="prasymas" property="id" />">Pasirinkti</a>	
	</td>
</tr>
</logic:iterate>
</table>
</center>
<center><% if (paging != null) out.print(paging.printPaging()); %></center>
</logic:present>
