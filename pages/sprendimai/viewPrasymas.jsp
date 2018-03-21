<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>
			<%!	String userAgent, strPath; %>
			

			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
<div class="heading">Praðymas keisti deklaravimo duomenis</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Praðymo registracijos numeris:</b></td>
	<td><bean:write name="prasymas" property="regNr" /></td>
</tr>
<tr>
	<td></td>
	<td><b>Praðymo data:</b></td>
	<td><bean:write name="prasymas" property="data" format="yyyy-MM-dd"/></td>
</tr>
<tr>
	<td></td>
	<td><b>Praðymas uþregistruotas:</b></td>
	<td><bean:write name="prasymas" property="insDate" format="yyyy-MM-dd HH:mm"/></td>
</tr>
<tr>
	<td></td>
	<td><b>Uþregistravusi ástaiga:</b></td>
	<td><bean:write name="prasymas" property="istaiga.pavadinimas" /></td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Praðymo tipas:</b></td>
	<td><bean:write name="prasymas" property="tipasStr"/></td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Praðymo bûsena:</b></td>
	<td>
		<bean:write name="prasymas" property="busenaStr"/>
		<logic:equal name="prasymas" property="busena" value="1">
		<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
			<input  type="button" value="Perþiûrëti sprendimà &raquo;" onclick="goToUrl('<%=strPath%>/viewsprendimas.do?id=<bean:write name="prasymas" property="sprendimas.id"/>')" class="button" style="width: 150px;" />
			<%}else{ %>
			<input  type="button" value="Perþiûrëti sprendimà &raquo;" onclick="goToUrl('viewsprendimas.do?id=<bean:write name="prasymas" property="sprendimas.id"/>')" class="button" style="width: 150px;" />
			<%} %>
		</logic:equal>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Asmuo, praðantis keisti duomenis:</b></td>
	<td><bean:write name="prasymas" property="prasytojas"/></td>
</tr>
<tr>
	<td></td>
	<td><b>Asmenys, kuriø duomenis praðoma keisti:</b></td>
	<td>
		<ul>
			<logic:iterate name="prasymas" property="asmenys" id="asmuo">
				<li>
					<bean:write name="asmuo" property="vardas" />
					<bean:write name="asmuo" property="pavarde" />,
					a.k. <bean:write name="asmuo" property="asmKodas" />
				</li>
			</logic:iterate>
		</ul>
	</td>
</tr>	
<tr>
	<td></td>
	<td><b>Pateikto asmens dokumento duomenys:</b></td>
	<td><bean:write name="prasymas" property="prasytojoDokumentas"/></td>
</tr>


<logic:equal name="prasymas" property="tipasStr" value="Naikinti duomenis">
<tr>
	<td></td>
	<td>
		<b>Naikinamas adresas:</b>
	</td>
	<td>
		<logic:present name="prasymas" property="naikinamasAdresas">
			<bean:write name="prasymas" property="naikinamasAdresas"/>
		</logic:present>
	</td>
</tr>
</logic:equal>
<logic:notEqual name="prasymas" property="tipasStr" value="Naikinti duomenis">
<tr>
	<td></td>
	<td>
		<b>Naujas gyvenamosios vietos adresas:</b>
	</td>
	<td>
		<logic:present name="prasymas" property="naujasAdresas">
			<bean:write name="prasymas" property="naujasAdresas"/>
		</logic:present>
	</td>
</tr>
</logic:notEqual>

<!--<logic:present name="prasymas" property="naujasAdresas">
<tr>
	<td></td>
	<td>
		<logic:notEqual name="prasymas" property="tipasStr" value="Naikinti duomenis">
			<b>Naujas gyvenamosios vietos adresas:</b>
		</logic:notEqual>
	</td>
	<td><bean:write name="prasymas" property="naujasAdresas"/></td>
</tr>
</logic:present>
<logic:present name="prasymas" property="naikinamasAdresas">
<tr>
	<td></td>
	<td><logic:equal name="prasymas" property="tipasStr" value="Naikinti duomenis">
			<b>Naikinamas adresas:</b>
		</logic:equal>
	</td>
	<td><bean:write name="prasymas" property="naikinamasAdresas"/></td>
</tr>
</logic:present>

-->
<tr>
	<td></td>
	<td><b>Pastabos:</b></td>
	<td><bean:write name="prasymas" property="pastabos"/></td>
</tr>
</table>
<hr />
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>	
	<td colspan="2">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	</td>
	<td align="right" nowrap>
		<logic:equal name="prasymas" property="busena" value="0">
<% if ((roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK"))){ %>

			
			<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
			
					<input type="button" value="Registruoti sprendimà ðio praðymo pagrindu" onclick="goToUrl('<%=strPath%>/createsprendimasform.do?prasymasId=<bean:write name="prasymas" property="id" />')" class="button" />
					<input type="button" value="Atmesti praðymà" onclick="if (confirm(	'Ar tikrai atmesti ðá praðymà?')) goToUrl('<%=strPath%>/rejectprasymas.do?id=<bean:write name="prasymas" property="id" />')" class="button" style="width: 200px;"/>
			
			<% }else { %>
			<input type="button" value="Registruoti sprendimà ðio praðymo pagrindu" onclick="goToUrl('createsprendimasform.do?prasymasId=<bean:write name="prasymas" property="id" />')" class="button" /> 
			<input type="button" value="Atmesti praðymà" onclick="if (confirm(	'Ar tikrai atmesti ðá praðymà?')) goToUrl('rejectprasymas.do?id=<bean:write name="prasymas" property="id" />')" class="button" style="width: 200px;"/>
			
			<% } %> 
<% }%>

		</logic:equal>
	</td>
</tr>
</table>


