<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/as" prefix="as" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.*,java.util.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>

<%
	int journalType = ((Integer)request.getAttribute("journalType")).intValue();
	pageContext.setAttribute("journalType", new Integer(journalType));
    Ordering ordering = (Ordering)session.getAttribute("journals_ordering");
    Paging paging = (Paging)session.getAttribute("journals_paging"); 
%>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<div class="heading"><bean:write name="journalTitle"/></div>
<table class="form" cellpadding="2" cellspacing="1" width="100%" border="0">
<html:form action="journal" method="get">
<input type="hidden" name="journalType" value="<bean:write name="journalType" />" />
<tr>
	<td width="30%">&nbsp;</td>
	<td width="10%"><b>Data nuo:</b></td>
	<td width="60%">
		<html:text property="dataNuo" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.JournalForm.dataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<tr>
	<td width="30%">&nbsp;</td>
	<td width="10%"><b>Data iki:</b></td>
	<td width="60%">
		<html:text property="dataIki" style="width: 70px;" styleClass="input" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.JournalForm.dataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataIki"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<% if (journalType == JournalDelegator.JOURNAL_TYPE_IN || journalType == JournalDelegator.JOURNAL_TYPE_OUT || journalType == JournalDelegator.JOURNAL_TYPE_GVNA){ %>
<tr>
	<td></td>
	<td><b>Bûsena:</b></td>
	<td>
		<html:select property="busena" styleClass="input" onchange="document.JournalForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:option value="1">Galiojanèios deklaracijos</html:option>
			<html:option value="2">Nebegaliojanèios deklaracijos</html:option>
			<html:option value="3">Nebaigtos ávesti deklaracijos</html:option>
			<html:option value="5">Atmestos deklaracijos</html:option>
		</html:select>
	</td>
</tr>
<% } else if (journalType == JournalDelegator.JOURNAL_TYPE_SPREND){ %>
<tr>
	<td></td>
	<td><b>Sprendimo tipas:</b></td>
	<td>
		<html:select property="busena" styleClass="input" onchange="document.JournalForm.submit();">
			<html:option value="-1">--- Visi ---</html:option>
			<html:option value="0">Taisyti duomenis</html:option>
			<html:option value="1">Keisti duomenis</html:option>
			<html:option value="2">Naikinti duomenis</html:option>
		</html:select>
	</td>
</tr>
<% } else { %>
<input type="hidden" name="busena" value="0" />
<% } %>
<logic:equal name="userStatus" value="0">
<tr>
	<td></td>
	<td><b>Savivaldybë:</b></td>
	<td>
		<html:select property="savivaldybe" styleClass="input" onchange="document.JournalForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="journalSavivaldybes" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:equal>
<logic:lessThan name="userStatus" value="2">
<tr>
	<td></td>
	<td><b>Seniûnija:</b></td>
	<td>
		<html:select property="seniunija" styleClass="input" onchange="document.JournalForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="journalSeniunijos" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:lessThan>

<% if (journalType == JournalDelegator.JOURNAL_TYPE_IN || journalType == JournalDelegator.JOURNAL_TYPE_OUT || journalType == JournalDelegator.JOURNAL_TYPE_GVNA){ %>
<tr>
	<td></td>
	<td><b>Deklaracijos tipas:</b></td>
	<td>
		<html:select property="deklaracijaType" styleClass="input">
			<html:option value="0">--- Visos ---</html:option>
			<html:option value="1">Popierinës deklaracijos</html:option>
			<html:option value="2">Elektroninës deklaracijos</html:option>
		</html:select>
	</td>
</tr>
<% } %>


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
<logic:present name="journalResults">
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%"><%= ordering.printOrdering("regNr", "Reg. nr.") %></th>
	<% if (journalType == JournalDelegator.JOURNAL_TYPE_SAVPAZ || journalType == JournalDelegator.JOURNAL_TYPE_GVNAPAZ || journalType == JournalDelegator.JOURNAL_TYPE_GVPAZ ){ %>
	    <th width="8%;"><%= ordering.printOrdering("pazymos_data, id", "Data") %></th>
    <% } else if ( journalType == JournalDelegator.JOURNAL_TYPE_GVNA || journalType == JournalDelegator.JOURNAL_TYPE_IN || journalType == JournalDelegator.JOURNAL_TYPE_OUT) { %>
	    <th width="8%;"><%= ordering.printOrdering("deklaravimo_data, id", "Data") %></th>
    <% } else { %> <%-- journalType == JournalDelegator.JOURNAL_TYPE_SPREND --%>
	    <th width="8%"><%= ordering.printOrdering("data, id", "Data") %></th>
    <% } %>

	<% if (journalType == JournalDelegator.JOURNAL_TYPE_SAVPAZ){ %>
    <th width="25%">Adresas</th>   
    <% } else if (journalType != JournalDelegator.JOURNAL_TYPE_SPREND){ %>
    <th width="25%">Asmuo</th>   
	<% } else { %>
    <th width="15%"><%= ordering.printOrdering("tipas", "Tipas") %></th>
    <% } %>
    <% if (journalType != JournalDelegator.JOURNAL_TYPE_GVPAZ && journalType != JournalDelegator.JOURNAL_TYPE_SAVPAZ &&journalType != JournalDelegator.JOURNAL_TYPE_GVNAPAZ && journalType != JournalDelegator.JOURNAL_TYPE_SPREND){ %>
    <th width="10%">Bûsena</th>
    <% } %>
    <th width="20%"><%= ordering.printOrdering("gvist_id", "Savivaldybë, seniûnija") %></th>
    <th width="15%">Veiksmai</th>
</tr>
<logic:iterate name="journalResults" id="result">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td><bean:write name="result" property="regNr" /></td>
	<td align="center">
		<logic:present name="result" property="deklaravimoData">
			<bean:write name="result" property="deklaravimoData" format="yyyy-MM-dd" />
		</logic:present>
		<logic:present name="result" property="pazymosData">
			<bean:write name="result" property="pazymosData" format="yyyy-MM-dd" />
		</logic:present>
		<logic:present name="result" property="data">
			<bean:write name="result" property="data" format="yyyy-MM-dd" />
		</logic:present>
	</td>

	 <% if (journalType == JournalDelegator.JOURNAL_TYPE_SAVPAZ){ %>
		<td><bean:write name="result" property="calcAdresas" /></td>
	 <% } else if (journalType != JournalDelegator.JOURNAL_TYPE_SPREND){ %>
	<td><bean:write name="result" property="calcAsmuo" /></td>
    <% } %>

    <% if (journalType != JournalDelegator.JOURNAL_TYPE_GVPAZ && journalType != JournalDelegator.JOURNAL_TYPE_SAVPAZ && journalType != JournalDelegator.JOURNAL_TYPE_GVNAPAZ){ %>
    <td><bean:write name="result" property="calcTipasStr" /></td>
    <% } %>

	<td><bean:write name="result" property="istaiga.pilnasPavadinimas" /></td>
	<td align="center">
		<logic:equal name="journalType" value="0">
			<a href="../inDeclarationView.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
			<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
				
				
				<logic:equal name="result" property="calcTipasStr" value="Nebaigta ávesti">
					| <a href="<%=request.getContextPath()%>/CompleteDeclaration.do?id=<bean:write name="result" property="id" />">Tæsti ávedimà</a>
				</logic:equal>
				
				
				<logic:notEqual name="result" property="calcTipasStr" value="Nebaigta ávesti">
					<logic:notEqual name="result" property="calcTipasStr" value="Atmesta">
						<logic:notEqual name="result" property="saltinis" value="1">
							| <a href="../editDeclaration.do?id=<bean:write name="result" property="id" />&type=A">Redaguoti</a>
						</logic:notEqual>
					</logic:notEqual>
				</logic:notEqual>
			 <% } 
				if (roles.contains("RL_GVDIS_GL_TVARK")){	 %>			
					| <a href="../DeleteDeclarationBaigta.do?id=<bean:write name="result" property="id" />&journalType=0" onclick="return confirm('Ar tikrai iðtrinti ðià deklaracijà?');">Iðtrinti</a>	 
		    <% } %>
		</logic:equal>
		<logic:equal name="journalType" value="1">
			<a href="../outDeclarationView.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
			<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
				<logic:equal name="result" property="calcTipasStr" value="Nebaigta ávesti">
					| <a href="<%=request.getContextPath()%>/CompleteDeclaration.do?id=<bean:write name="result" property="id" />">Tæsti ávedimà</a>
				</logic:equal>
				<logic:notEqual name="result" property="calcTipasStr" value="Nebaigta ávesti">
					<logic:notEqual name="result" property="calcTipasStr" value="Atmesta">
					| <a href="../editDeclaration.do?id=<bean:write name="result" property="id" />&type=I">Redaguoti</a>
					</logic:notEqual>
				</logic:notEqual>
 			 <% } 
				if (roles.contains("RL_GVDIS_GL_TVARK")){	%>			
					| <a href="../DeleteDeclarationBaigta.do?id=<bean:write name="result" property="id" />&journalType=1" onclick="return confirm('Ar tikrai iðtrinti ðià deklaracijà?');">Iðtrinti</a>
		    <% } %>
		</logic:equal>
		<logic:equal name="journalType" value="2">
			<a href="../pazymosredirect.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
		</logic:equal>
		<logic:equal name="journalType" value="3">
			<a href="../viewsprendimas.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
			<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
				| <a href="../editsprendimasform.do?id=<bean:write name="result" property="id" />">Redaguoti</a>
		    <% } %>
		</logic:equal>
		<logic:equal name="journalType" value="4">
			<a href="../gvnaDeclarationView.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
			<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
				<logic:equal name="result" property="calcTipasStr" value="Nebaigta ávesti">
					| <a href="<%=request.getContextPath()%>/CompleteDeclaration.do?id=<bean:write name="result" property="id" />">Tæsti ávedimà</a>
				</logic:equal>
				<logic:notEqual name="result" property="calcTipasStr" value="Nebaigta ávesti">
					| <a href="../editDeclaration.do?id=<bean:write name="result" property="id" />&type=N">Redaguoti</a>
				</logic:notEqual>
			 <% } 
				if (roles.contains("RL_GVDIS_GL_TVARK")){	%>			
					| <a href="../DeleteDeclarationBaigta.do?id=<bean:write name="result" property="id" />&journalType=4" onclick="return confirm('Ar tikrai iðtrinti ðá praðymà?');">Iðtrinti</a>	
		    <% } %>
		</logic:equal>
		<logic:equal name="journalType" value="5">
			<a href="../gvnapazyma.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
		</logic:equal>
		<logic:equal name="journalType" value="6">
			<a href="../savpazyma.do?id=<bean:write name="result" property="id" />">Perþiûrëti</a> 
		</logic:equal>
	</td>
</tr>
</logic:iterate>
</table>
<hr />

<table cellpadding="2" cellspacing="1" border="0" width="100%">
<tr>
	<td align="center" width="90%">
		<% if (paging != null) out.print(paging.printPaging()); %>
	</td>
	<td align="right">
		<input type="button" class="button" value="Spausdinti þurnalà" onclick="window.open('<%= request.getContextPath() %>/journal.do?<%= request.getQueryString() %>&output=pdf')" style="width: 150px;" />&nbsp;
	</td>
</tr>	
</table>

</logic:present>

<logic:notPresent name="journalResults">
<br/>
<div align="center">
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
		<img src="../img/info.png" />
	</td>
	<td class="message1">
				<bean:write name="not_found_type" /><br /> 
	</td>
</tr>
</table>
</div>

<br/>
<hr/>

</logic:notPresent>
