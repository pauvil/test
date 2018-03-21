<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.UserDelegator,
	com.algoritmusistemos.gvdis.web.objects.*,
	java.util.List;" %>
<%
	int reportType = ((Integer)request.getAttribute("reportType")).intValue();
	int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
	String strPath, userAgent; 
%>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<div class="heading"><bean:write name="reportTitle" /></div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<html:form action="report" method="get">
<input type="hidden" name="reportType" value="<bean:write name="reportType" />" />
<tr>
	<td width="10%"></td>
	<td width="200">
		<% if (reportType != 12){ %>
		<b>Deklaravimo data nuo:</b>
		<% } else { %>
		<b>Data:</b>
		<% } %>
	</td>
	<td width="180">
		<% if (reportType != 12){ %>
		<html:text property="dataNuo" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.ReportsForm.dataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
	<td>
		<b>iki:</b>
		<html:text property="dataIki" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.ReportsForm.dataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errDataIki"><span class="error">Negaliojanti data</span></logic:present>
	<% } else { %>
		<html:select property="ataskaita12data" styleClass="input">			
			<html:options collection="laikotarpiai" property="laikotarpis" labelProperty="laikotarpis" />
		</html:select>
	<% } %>
	</td>
</tr>
<% if (reportType == 1 || reportType == 2 || reportType == 3 || reportType == 5 || reportType == 7 || reportType == 8 || reportType == 10 || reportType == 12){ %>
<logic:equal name="userStatus" value="0">
<tr>
	<td width="10%"></td>
	<td>
		<b>Savivaldyb�:</b>
	</td>
	<td colspan="2">
		<html:select property="savivaldybe" styleClass="input" onchange="document.ReportsForm.seniunija.value=0; document.ReportsForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="reportSavivaldybes" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:equal>
<logic:lessThan name="userStatus" value="2">
<tr>
	<td width="10%"></td>
	<td> 
		<b>Seni�nija:</b>
	</td>
	<td colspan="2">
		<html:select property="seniunija" styleClass="input" onchange="document.ReportsForm.submit();">
			<html:option value="0">--- Visos ---</html:option>
			<html:options collection="reportSeniunijos" property="id" labelProperty="pavadinimas" />
		</html:select>
	</td>
</tr>
</logic:lessThan>
<% } %>
<% if (reportType == 1){ %>
<tr>
	<td width="10%"></td>
	<td>
		<b>Rodyti deklaracijas:</b>
	</td>
	<td colspan="2">
		<html:select property="deklTipas" styleClass="input" onchange="document.ReportsForm.submit();">
			<html:option value="0">--- Visas ---</html:option>
			<html:option value="1">Atvykimo � LR</html:option>
			<html:option value="2">Gyvenamosios vietos pakeitimo LR</html:option>
			<html:option value="3">Kita (ankstesn� gyvenamoji vieta ne�inoma)</html:option>
		</html:select>
	</td>
</tr>
<% } %> 
<% if (reportType == 1 || reportType == 2 || reportType == 8){ %>
<tr>
	<td width="10%"></td>
	<td width="200">
		<b>�traukti gyventojus, gimusius nuo:</b>
	</td>
	<td width="180">
		<html:text property="gimDataNuo" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.ReportsForm.gimDataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errGimDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
	<td>
		<b>iki:</b>
		<html:text property="gimDataIki" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.ReportsForm.gimDataIki, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errGimDataIki"><span class="error">Negaliojanti data</span></logic:present>
	</td>
</tr>
<% } %>
<% if (reportType == 3){ %>
<tr>
	<td width="10%"></td>
	<td width="200">
		<b>Galiojimo data:</b>
	</td>
	<td width="180">
		<html:text property="gimDataNuo" styleClass="input" style="width: 70px;" />
		<script language="javascript">
		<!--
		if (!document.layers) {
			document.write("<a style='cursor:pointer;' onclick='popUpCalendar(this, document.ReportsForm.gimDataNuo, \"yyyy-mm-dd\")'><img src=\"../img/ico_cal.gif\"></a>");
		}
		//-->
		</script>
		<logic:present name="errGimDataNuo"><span class="error">Negaliojanti data</span></logic:present>
	</td>
	
</tr>
<% } 
 if (reportType == 1 || reportType == 2 || reportType == 3 || reportType == 8){ %>
<tr>
	<td width="10%"></td>
	<td>
		<b>Grupuoti pagal:</b>
	</td>
	<td colspan="2">
		<html:select property="grupavimas" styleClass="input" onchange="document.ReportsForm.submit();">
			<html:option value="0">--- Negrupuoti ---</html:option>
			<logic:lessThan name="userStatus" value="1"> <!-- Pagal apskritis ir savivaldybes grupuoti leid�iame tik globaliems -->
			<% if (reportType == 1 || reportType == 8){ %><html:option value="1">Pagal apskritis</html:option><% } %>
			<html:option value="2">Pagal savivaldybes</html:option>
			</logic:lessThan>
			<logic:lessThan name="userStatus" value="2"> <!-- Pagal seni�nijas grupuoti leid�iame tik globaliems ir seni�nijoms -->
			<html:option value="3">Pagal seni�nijas</html:option>
			</logic:lessThan>
			<html:option value="4">Pagal lytis</html:option>
			<html:option value="5">Pagal pilietybes</html:option>
			<% if (reportType != 3){ %><html:option value="6">Pagal asmens dokumento tipus</html:option><% } %>
			<html:option value="7">Pagal tautybes</html:option>
			<% if (reportType == 2 || reportType == 3){ %><html:option value="8">Pagal valstyb�</html:option><% } %>
		</html:select>
	</td>
</tr>
<% } %>
<%
if (reportType == 12 && (userStatus == UserDelegator.USER_GLOBAL || userStatus == UserDelegator.USER_SAV)){ %>
<tr>
	<td width="10%"></td>
	<td>
		<b>Grupuoti pagal:</b>
	</td>
	<td colspan="2">
		<html:select property="grupavimas" styleClass="input">
			<html:option value="0">--- Negrupuoti ---</html:option>			
			<html:option value="3">Pagal seni�nijas</html:option>			
		</html:select>
	</td>
</tr>
<% } %>
<% // Kai ataskaita 805 (isduotu gv pazymu) arba 810 (isduotu GVNA pazymu) ir
   // vartotojas globalus arba savivaldybes rodyti grupavimo
   // pasirinkimo lauka;
   if (((reportType == 5 || reportType == 10) && 
		   (userStatus == UserDelegator.USER_GLOBAL || userStatus == UserDelegator.USER_SAV)) || reportType == 7) { %>
<tr>
	<td width="10%"></td>
	<td>
		<b>Grupuoti pagal:</b>
	</td>
	<td colspan="2">
		<html:select property="grupavimas" styleClass="input" onchange="document.ReportsForm.submit();">
			<html:option value="0">--- Negrupuoti ---</html:option>
			<logic:lessThan name="userStatus" value="1"> <!-- Pagal apskritis ir savivaldybes grupuoti leid�iame tik globaliems -->
				<html:option value="2">Pagal savivaldybes</html:option>
			</logic:lessThan>
			<logic:lessThan name="userStatus" value="2"> <!-- Pagal seni�nijas grupuoti leid�iame tik globaliems ir seni�nijoms -->
				<html:option value="3">Pagal seni�nijas</html:option>
			</logic:lessThan>
		</html:select>
	</td>
</tr>
<% } %>
<tr>
	<td colspan="5" align="center">
		<html:submit value="Per�i�r�ti" styleClass="button" />
	</td>
</tr>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos �vedamos formatu <i>"metai-m�nuo-diena" (2006-09-01)</i>.
</div>
<% if (reportType == 12){ %>
<logic:present name="results">
<% List rezList = (List)request.getAttribute("results"); 
 if(rezList.size()>0){
	Object obj = rezList.get(0);
	if(obj instanceof SkaiciausAtaskaitosEiluteGrupuota){ %>
	<% int i = 0; %>
<div class="heading">Gyventoj�, deklaravusi� savo gyvenam�j� viet�, skai�ius</div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">�staiga&nbsp;&nbsp;</th>
	<th align="right" >I� viso</th>
	<th align="right" >iki 7 m.</th>
	<th align="right" >7 - 16 m.</th>
	<th align="right" >16 - 18 m.</th>
	<th align="right" >18 - 25 m.</th>
	<th align="right" >25 - 45 m.</th>
	<th align="right" >45 - 65 m.</th>
	<th align="right" >65 - 85 m.</th>
	<th align="right" >nuo 85 m.</th>
	<th align="right" ></th>
</tr>
<tr><td colspan="13" class="darkbg"></td></tr>
<logic:iterate name="results" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td align="left"><bean:write name="result" property="grupe" /></td>
	<td align="right"><bean:write name="result" property="viso" /></td>
	<td align="right"><bean:write name="result" property="m0_7" /></td>
	<td align="right"><bean:write name="result" property="m7_16" /></td>
	<td align="right"><bean:write name="result" property="m16_18" /></td>
	<td align="right"><bean:write name="result" property="m18_25" /></td>
	<td align="right"><bean:write name="result" property="m25_45" /></td>
	<td align="right"><bean:write name="result" property="m45_65" /></td>
	<td align="right"><bean:write name="result" property="m65_85" /></td>
	<td align="right"><bean:write name="result" property="m85_0" /></td>
	<td align="right"><img src="../img/pix.gif" width="2" height="1" /></td>
</tr>
<tr><td colspan="13" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />

<div align="right">


			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
			<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%=strPath%>/report.do?<%= request.getQueryString() %>&output=csv&grouped=yes');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv&grouped=yes');" />&nbsp;
				<%} %>
</div>

<br/>


<%	}
	if(obj instanceof SkaiciausAtaskaitosEilute){%>
	<br />
<% int i = 0; %>
<div class="heading">Gyventoj�, deklaravusi� savo gyvenam�j� viet�, skai�ius</div>

<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">Grup�</th>
	<th align="right" width="15%">Vyr�</th>
	<th align="right" width="15%">Moter�</th>
	<th align="right" width="15%">Viso</th>
	<th align="right" ></th>
</tr>
<tr><td colspan="7" class="darkbg"></td></tr>
<logic:iterate name="results" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td align="left"><bean:write name="result" property="grupe" /></td>
	<td align="right"><bean:write name="result" property="vyru" /></td>
	<td align="right"><bean:write name="result" property="moteru" /></td>
	<td align="right"><bean:write name="result" property="viso" /></td>
	<td align="right"><img src="../img/pix.gif" width="100" height="1" /></td>
</tr>
<tr><td colspan="7" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
			<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%= strPath%>/report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
				<%} %>
	
</div>
<%	}
}
%>
</logic:present>

<logic:present name="resultsGVNA">
<% List rezList = (List)request.getAttribute("resultsGVNA"); 
 if(rezList.size()>0){
	Object obj = rezList.get(0);
	if(obj instanceof SkaiciausAtaskaitosEiluteGrupuota){ %>
	<% int i = 0; %>
		<div class="heading">Gyventoj�, �traukt� � gyvenamosios vietos nedeklaravusi� asmen� apskait�, skai�ius </div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">�staiga&nbsp;&nbsp;</th>
	<th align="right" >I� viso</th>
	<th align="right" >iki 7 m.</th>
	<th align="right" >7 - 16 m.</th>
	<th align="right" >16 - 18 m.</th>
	<th align="right" >18 - 25 m.</th>
	<th align="right" >25 - 45 m.</th>
	<th align="right" >45 - 65 m.</th>
	<th align="right" >65 - 85 m.</th>
	<th align="right" >nuo 85 m.</th>
	<th align="right" ></th>
</tr>
<tr><td colspan="13" class="darkbg"></td></tr>
<logic:iterate name="resultsGVNA" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td align="left"><bean:write name="result" property="grupe" /></td>
	<td align="right"><bean:write name="result" property="viso" /></td>
	<td align="right"><bean:write name="result" property="m0_7" /></td>
	<td align="right"><bean:write name="result" property="m7_16" /></td>
	<td align="right"><bean:write name="result" property="m16_18" /></td>
	<td align="right"><bean:write name="result" property="m18_25" /></td>
	<td align="right"><bean:write name="result" property="m25_45" /></td>
	<td align="right"><bean:write name="result" property="m45_65" /></td>
	<td align="right"><bean:write name="result" property="m65_85" /></td>
	<td align="right"><bean:write name="result" property="m85_0" /></td>
	<td align="right"><img src="../img/pix.gif" width="2" height="1" /></td>
</tr>
<tr><td colspan="13" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
			<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%= strPath%>/report.do?<%= request.getQueryString() %>&output=csv&gvnt=yes&grouped=yes');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv&gvnt=yes&grouped=yes');" />&nbsp;
				<%} %>
	
</div>
<%	}
	if(obj instanceof SkaiciausAtaskaitosEilute){%>
	<% int i = 0; %>
	<div class="heading">Gyventoj�, �traukt� � gyvenamosios vietos nedeklaravusi� asmen� apskait�
											<logic:present name="savPavadinimas"> prie
											<bean:write name="savPavadinimas"/>
											</logic:present>, skai�ius </div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">Grup�</th>
	<th align="right" width="15%">Vyr�</th>
	<th align="right" width="15%">Moter�</th>
	<th align="right" width="15%">Viso</th>
	<th align="right" ></th>
</tr>
<tr><td colspan="7" class="darkbg"></td></tr>
<logic:iterate name="resultsGVNA" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td align="left"><bean:write name="result" property="grupe" /></td>
	<td align="right"><bean:write name="result" property="vyru" /></td>
	<td align="right"><bean:write name="result" property="moteru" /></td>
	<td align="right"><bean:write name="result" property="viso" /></td>
	<td align="right"><img src="../img/pix.gif" width="100" height="1" /></td>
</tr>
<tr><td colspan="7" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%= strPath%>/report.do?<%= request.getQueryString() %>&output=csv&gvnt=yes');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv&gvnt=yes');" />&nbsp;
				<%} %>
	
</div>
<%	}
}
%>
</logic:present>
<% } %>

<logic:present name="results">
<% if (reportType != 12){ %>
<br />
<% int i = 0; %>
<div class="heading">Ataskaita</div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">Grup�</th>
	<th align="left">Skai�ius grup�je</th>
</tr>
<tr><td colspan="4" class="darkbg"></td></tr>
<logic:iterate name="results" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td><bean:write name="result" property="name" /></td>
	<td><img src="../img/pix.gif" width="40" height="1" /><bean:write name="result" property="count" /></td>
</tr>
<tr><td colspan="4" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%=strPath%>/report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
				<%} %>
	
</div>

<% } %>
</logic:present>
<logic:present name="results02">
<br />
<% int i = 0; %>
<div class="heading">Ataskaita</div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">Grup�</th>
	<th align="left">Deklaracijas pateik� deklaravimo �staigose</th>
	<th align="left">Deklaracijas pateik� elektroniniu b�du</th>
	<th align="left">Viso</th>
</tr>
<tr><td colspan="6" class="darkbg"></td></tr>
<logic:iterate name="results02" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td><bean:write name="result" property="name" /></td>
	<td><img src="../img/pix.gif" width="40" height="1" /><bean:write name="result" property="perGvdis" /></td>
	<td><img src="../img/pix.gif" width="20" height="1" /><bean:write name="result" property="elektroninis" /></td>
	<td><img src="../img/pix.gif" width="20" height="1" /><bean:write name="result" property="count" /></td>
</tr>
<tr><td colspan="6" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
	<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%= strPath%>/report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
				<%} %>
	
</div>

</logic:present>

<logic:present name="results08">
<br />
<% int i = 0; %>
<div class="heading">Ataskaita</div>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">Grup�</th>
	<th align="left">Pateikta pra�ym�</th>
	<th align="left">Galioja laikotarpio pabaigoje</th>
</tr>
<tr><td colspan="5" class="darkbg"></td></tr>
<logic:iterate name="results08" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td><bean:write name="result" property="name" /></td>
	<td><img src="../img/pix.gif" width="40" height="1" /><bean:write name="result" property="count" /></td>
	<td><img src="../img/pix.gif" width="40" height="1" /><bean:write name="result" property="galioja" /></td>
</tr>
<tr><td colspan="5" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
	<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%= strPath%>/report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
				<%} %>
	
</div>

</logic:present>

<logic:present name="results07">
<% int i = 0; %>
</br>
<table cellspacing="3" cellpadding="0" width="100%" border="0">
<tr>
	<th width="30">&nbsp;</th>
	<th align="left">Nr.</th>
	<th align="left">Grup�</th>
	<th align="right" width="15%">Taisymo</th>
	<th align="right" width="15%">Keitimo</th>
	<th align="right" width="15%">Naikinimo</th>
	<th align="right" width="15%">Viso</th>
	<th align="right" ></th>
</tr>
<tr><td colspan="8" class="darkbg"></td></tr>
<logic:iterate name="results07" id="result">
<tr>
	<td></td>
	<td><%= ++i %>.</td>
	<td align="left"><bean:write name="result" property="name" /></td>
	<td align="right"><bean:write name="result" property="taisymo" /></td>
	<td align="right"><bean:write name="result" property="keitimo" /></td>
	<td align="right"><bean:write name="result" property="naikinimo" /></td>
	<td align="right"><bean:write name="result" property="viso" /></td>
	<td align="right"><img src="../img/pix.gif" width="100" height="1" /></td>
</tr>
<tr><td colspan="8" class="darkbg"></td></tr>
</logic:iterate>
</table>
<hr />
<div align="right">
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
	<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('<%= strPath%>/report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
			<%}else { %>	
				<input type="button" class="button" value="Perkelti � Excel" onclick="goToUrl('report.do?<%= request.getQueryString() %>&output=csv');" />&nbsp;
				<%} %>
	
</div>
<br />

</logic:present>
<logic:notPresent name="results02">
<logic:notPresent name="results08">
<logic:notPresent name="results07">
<logic:notPresent name="results">
<logic:notPresent name="results0812Grouped">
<br/>
<div align="center">
<table cellpadding="2" cellspacing="1">
<tr>
	<td>
		<img src="../img/info.png" />
	</td>
	<td class="message1">N�ra duomen�<br /> 
	</td>
</tr>
</table>
</div>

<br/>
<hr/>
</logic:notPresent>
</logic:notPresent>
</logic:notPresent>
</logic:notPresent>
</logic:notPresent>