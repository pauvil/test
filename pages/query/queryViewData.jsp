<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>
<%@ page import="java.util.*" %>

<div class="heading">Gyvenamosios vietos duomenys</div>
<table cellpadding="2" cellspacing="2" width="100%">
<tr>
	<td width="30%" align="right"><b>Asmens kodas:</b></td>
	<td><bean:write name="asmuo" property="asmKodas" /></td>
</tr>
<tr>
	<td align="right"><b>Vardas, pavardë:</b></td>
	<td><bean:write name="asmuo" property="vardas" /> <bean:write name="asmuo" property="pavarde" /></td>
</tr>
<tr>
	<td align="right"><b>Duomenø ðaltinis:</b></td>
	<td valign="middle">
		<bean:write name="gyvenamojiVieta" property="saltinis" />
		&nbsp;
		<%! String strPath, userAgent; %>
		<% strPath = request.getContextPath().substring(1); %>
		<% userAgent = request.getHeader("User-Agent") ; %>
		
		<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
		<%   GyvenamojiVieta gyvenamojiVieta = (GyvenamojiVieta)request.getAttribute("gyvenamojiVieta");
		   if (gyvenamojiVieta.getSprendimas() != null){ 
		%>
		<input type="button" class="button" onclick="goToUrl('<%= strPath %>/viewsprendimas.do?id=<%= gyvenamojiVieta.getSprendimas().getId() %>')" value="Perþiûrëti sprendimà &raquo;" style="width: 150px;"> &nbsp;
		<% } 
		   if (gyvenamojiVieta.getDeklaracija() != null){
		     if (gyvenamojiVieta.getDeklaracija() instanceof AtvykimoDeklaracija){
		%>
		<input type="button" class="button" onclick="goToUrl('<%= strPath %>/inDeclarationView.do?id=<%= gyvenamojiVieta.getDeklaracija().getId() %>')" value="Perþiûrëti deklaracijà &raquo;" style="width: 150px;">
		<%   } else if (gyvenamojiVieta.getDeklaracija() instanceof IsvykimoDeklaracija){ %>
		<input type="button" class="button" onclick="goToUrl('<%= strPath %>/outDeclarationView.do?id=<%= gyvenamojiVieta.getDeklaracija().getId() %>')" value="Perþiûrëti deklaracijà &raquo;" style="width: 150px;">
		<%   } else if (gyvenamojiVieta.getDeklaracija() instanceof GvnaDeklaracija){ %>
		<input type="button" class="button" onclick="goToUrl('<%= strPath %>/gvnaDeclarationView.do?id=<%= gyvenamojiVieta.getDeklaracija().getId() %>')" value="Perþiûrëti praðymà &raquo;" style="width: 150px;">
		<%   }
		   }
		   
		   
		}else{%>
		<%   GyvenamojiVieta gyvenamojiVieta = (GyvenamojiVieta)request.getAttribute("gyvenamojiVieta");
		   if (gyvenamojiVieta.getSprendimas() != null){ 
		%>
		<input type="button" class="button" onclick="goToUrl('viewsprendimas.do?id=<%= gyvenamojiVieta.getSprendimas().getId() %>')" value="Perþiûrëti sprendimà &raquo;" style="width: 150px;"> &nbsp;
		<% } 
		   if (gyvenamojiVieta.getDeklaracija() != null){
		     if (gyvenamojiVieta.getDeklaracija() instanceof AtvykimoDeklaracija){
		%>
		<input type="button" class="button" onclick="goToUrl('inDeclarationView.do?id=<%= gyvenamojiVieta.getDeklaracija().getId() %>')" value="Perþiûrëti deklaracijà &raquo;" style="width: 150px;">
		<%   } else if (gyvenamojiVieta.getDeklaracija() instanceof IsvykimoDeklaracija){ %>
		<input type="button" class="button" onclick="goToUrl('outDeclarationView.do?id=<%= gyvenamojiVieta.getDeklaracija().getId() %>')" value="Perþiûrëti deklaracijà &raquo;" style="width: 150px;">
		<%   } else if (gyvenamojiVieta.getDeklaracija() instanceof GvnaDeklaracija){ %>
		<input type="button" class="button" onclick="goToUrl('gvnaDeclarationView.do?id=<%= gyvenamojiVieta.getDeklaracija().getId() %>')" value="Perþiûrëti praðymà &raquo;" style="width: 150px;">
		<%   }
		   }%>
		<%} %>
		
		

	
	</td>
</tr>
<tr>
	<td align="right"><b>Santykis su gyvenamàjà vieta:</b></td>
	<td><bean:write name="gyvenamojiVieta" property="gvtTipasString" /></td>
</tr>
<tr>
	<td align="right"><b>Ásigaliojo:</b></td>
	<td><bean:write name="gyvenamojiVieta" property="gvtDataNuo" format="yyyy-MM-dd" /></td>
</tr>
<logic:present name="gyvenamojiVieta" property="gvtDataIki">
<tr>
	<td align="right"><b>Galiojo iki:</b></td>
	<td><bean:write name="gyvenamojiVieta" property="gvtDataIki" format="yyyy-MM-dd" /></td>
</tr>
</logic:present>
<logic:present name="stringAddress">
<tr>
	<td colspan="2"><div class="heading" /></td>
</tr>
<tr>
	<td align="right"><b>Adresas:</b></td>
	<td><bean:write name="stringAddress"/>&nbsp;&nbsp;&nbsp;&nbsp;<logic:present name="savivaldybe"><b><bean:write name="savivaldybe"/></b></logic:present> </td>
</tr>
</logic:present>
<% 
	Map flatAddress = (Map)request.getAttribute("flatAddress");
	if (flatAddress.size() > 1){
%>
<tr>
	<td colspan="2"><div class="heading" /></td>
</tr>
<%
	for (Iterator it=flatAddress.keySet().iterator(); it.hasNext();){
	    String key = (String)it.next();
	    if (flatAddress.get(key)!=null) {
%>
<tr>
	<td align="right"><b><%= key %>:</b></td>
	<td><%= flatAddress.get(key) %></td>
</tr>
<%  
	} 
	}
	}
%>
</table>

<hr />
&nbsp;
<%-- <a href="javascript:history.go(-1);">&laquo; Atgal</a> --%>
<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
