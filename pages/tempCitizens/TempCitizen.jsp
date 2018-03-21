<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.delegators.*,
				 java.util.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>
<script language="JavaScript">
<!--
	function confirmAndDelete()
	{
		if(confirm('Ar tikrai panaikinti ðá áraðà?'))
		window.location = '<%=request.getContextPath()%>/DeleteTempCitizen.do?id=<bean:write name="tempCitizen" property="id" />';
	}
//-->
</script>
<%
	LaikinasAsmuo laikinasAsmuo = (LaikinasAsmuo)request.getAttribute("tempCitizen");
	Deklaracija deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracijaFromLaikinasAsmuo(laikinasAsmuo.getId(),request);
	String viewType = "";
	if(deklaracija instanceof AtvykimoDeklaracija)viewType = "inDeclarationView";
	else
		if(deklaracija instanceof IsvykimoDeklaracija)viewType = "outDeclarationView";
	else
		if(deklaracija instanceof GvnaDeklaracija)viewType = "gvnaDeclarationView";

	session.setAttribute("declaration",deklaracija);
%>

<div class="heading">Asmuo, deklaravæs gyvenamàjà vietà, bet neregistruotas Gyventojø registre</div>
<table cellpadding="2" cellspacing="2" width="100%">
<tr>
	<td width="30%" align="right">Asmens kodas:</td>
	<td><b><bean:write name="tempCitizen" property="asmensKodas" /></b></td>
</tr>
<tr>
	<td align="right">Vardas:</td>
	<td><b><bean:write name="tempCitizen" property="vardas" /></b></td>
</tr>
<tr>
	<td align="right">Pavardë:</td>
	<td><b><bean:write name="tempCitizen" property="pavarde" /></b></td>
</tr>
<%-- ju.k 2007.06.05
<tr>
	<td align="right">Kiti vardai:</td>
	<td><bean:write name="tempCitizen" property="kitiVardai" /></td>
</tr>
--%>
<tr>
	<td align="right">Lytis:</td>
	<td><b>
	<logic:equal name="tempCitizen" property="lytis" value="V">Vyras</logic:equal>
	<logic:equal name="tempCitizen" property="lytis" value="M">Moteris</logic:equal>	
	</b></td>
</tr>
<tr>
	<td align="right">Gimimo data:</td>
	<td><b><bean:write name="tempCitizen" property="gimimoData" format="yyyy.MM.dd"/></b></td>
</tr>
<tr>
	<td align="right" valign="top">Pastabos:</td>
	<td><b><bean:write name="tempCitizen" property="pastabos"/>		</b>
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr />
	</td>
</tr>
<tr>
	<td align="left">
<!-- 	<input type="button" class="button" value="&laquo; Atgal" onclick="window.location='<%=request.getContextPath()%>/TempCitizensList.do'" style="width: 100px;" />  -->
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	</td>
	<td align="left">
		<input type="button" class="button" value="Perþiûrëti deklaracijà" onclick="window.location='<%=request.getContextPath()%>/<%=viewType%>.do?id=<%=deklaracija.getId()%>'" style="width: 150px;" />
	<% if ((roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK"))){ %>	
		<input type="button" class="button" value="Susieti su GR" onclick="window.location='<%=request.getContextPath()%>/LinkWithGR.do'" style="width: 150px;" />
		<input type="button" class="button" value="Iðtrinti" onclick="confirmAndDelete();" style="width: 150px;" />
	<%}%>
	</td>
</tr>
</table>
