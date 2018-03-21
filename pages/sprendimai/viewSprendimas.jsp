<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">Sprendimas dël deklaravimo duomenø keitimo, taisymo ar naikinimo</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"></td>
	<td width="35%"><b>Sprendimo registracijos numeris:</b></td>
	<td><bean:write name="sprendimas" property="regNr" /></td>
</tr>
<tr>
	<td></td>
	<td><b>Sprendimo priëmimo data:</b></td>
	<td><bean:write name="sprendimas" property="data" format="yyyy-MM-dd"/></td>
</tr>
<logic:notEmpty name="sprendimas" property="prieme">
<tr>
	<td></td>
	<td><b>Sprendimà priëmæs asmuo/institucija:</b></td>
	<td><bean:write name="sprendimas" property="prieme" /></td>
</tr>
</logic:notEmpty>
<tr>
	<td></td>
	<td><b>Sprendimas uþregistruotas:</b></td>
	<td><bean:write name="sprendimas" property="insDate" format="yyyy-MM-dd HH:mm"/></td>
</tr>
<tr>
	<td></td>
	<td><b>Uþregistravusi ástaiga:</b></td>
	<td><bean:write name="sprendimas" property="istaiga.pavadinimas" /></td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Sprendimo tipas:</b></td>
	<td><bean:write name="sprendimas" property="calcTipasStr"/></td>
</tr>
<tr>
	<td width="10%"></td>
	<td><b>Sprendimo prieþastis:</b></td>
	<td><bean:write name="sprendimas" property="priezastis.pavadinimas"/></td>
</tr>
<logic:present name="sprendimas" property="prasymas">
<tr>
	<td width="10%"></td>
	<td><b>Priimta pagal praðymà:</b></td>
	<td>
	<%! String strPath, userAgent; %>
		<% strPath = request.getContextPath().substring(1); %>
		<% userAgent = request.getHeader("User-Agent") ; %>
		
		
		<bean:write name="sprendimas" property="prasymas.regNr"/> (<bean:write name="sprendimas" property="prasymas.data" format="yyyy-MM-dd" />)
		<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
		<input type="button" value="Perþiûrëti praðymà &raquo;" onclick="goToUrl('<%= strPath %>/viewprasymas.do?id=<bean:write name="sprendimas" property="prasymas.id"/>')" class="button" style="width: 100px;" />
		<%} else{ %>
		<input type="button" value="Perþiûrëti praðymà &raquo;" onclick="goToUrl('viewprasymas.do?id=<bean:write name="sprendimas" property="prasymas.id"/>')" class="button" style="width: 100px;" />
		<%} %>
	</td>
</tr>
</logic:present>
<tr>
	<td></td>
	<td valign="top">
		<b>Asmenys, kuriø deklaravimo duomenys yra keièiami:</b>
	</td>
	<td>
		<ul>
		<logic:iterate name="sprendimas" property="gyvenamosiosVietos" id="gvt">
		<li>
			<bean:write name="gvt" property="asmuo.vardas"/> <bean:write name="gvt" property="asmuo.pavarde"/>, a.k. <bean:write name="gvt" property="asmuo.asmKodas"/>
		</li>
		</logic:iterate>
		</ul>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Data, nuo kurios naikinami ankstesni deklaravimo duomenys:</b></td>
	<td><bean:write name="sprendimas" property="naikinimoData" format="yyyy-MM-dd" /></td>
</tr>
<logic:present name="addressString">
<tr>
	<td></td>
	<td><b>Naujas gyvenamosios vietos adresas:</b></td>
	<td>
		<bean:write name="addressString" />
	</td>
</tr>
</logic:present>
<logic:notEmpty name="sprendimas" property="pastabos">
<tr>
	<td></td>
	<td><b>Pastabos:</b></td>
	<td><bean:write name="sprendimas" property="pastabos"/></td>
</tr>
</logic:notEmpty>
<tr>	
	<td colspan="3">
		<hr />
<%--  	<input type="button" value="&laquo; Atgal" onclick="goToUrl('sprendimai.do')" class="button" style="width: 100px;" />  --%>
		<input type="button" class="button" value="&laquo; Atgal" onclick="history.go(-1)" style="width: 100px;" />
	</td>
</tr>
</table>
