<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,com.algoritmusistemos.gvdis.web.persistence.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>

<logic:notPresent name="asmuo">
	<div class="heading">Informacija</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1"><bean:write name="errorMessage"/></td>
	</tr>
	</table>
	</div>
	<br/>
	<hr/>
</logic:notPresent>

<logic:present name="asmuo">
<div class="heading">Surastas asmuo</div>
	<table cellpadding="2" cellspacing="2" width="80%">
	<tr>
		<td width="40%" align="right">Asmens kodas:</td>
		<td><b><bean:write name="asmuo" property="asmKodas" /></b></td>
	</tr>
	<tr>
		<td align="right">Vardas, pavardë:</td>
		<td><b><bean:write name="asmuo" property="vardas" /> <bean:write name="asmuo" property="pavarde" /></b></td>
	</tr>
	<logic:present name="aktVieta">
	<tr>
		<td align="right">Dokumento tipas:</td>
		<td><b><bean:write name="aktVieta" property="saltinis" /></b></td>
	</tr>
	<tr>
		<td align="right">Santykis su gyvenamàja vieta:</td>
		<td><b><bean:write name="aktVieta" property="gvtTipasString" /></b></td>
	</tr>
	<tr>
		<td align="right">Aktuali gyvenamoji vieta deklaruota:</td>
		<td><b><bean:write name="aktVieta" property="gvtDataNuo" format="yyyy-MM-dd" /></b></td>
	</tr>
	</logic:present>
	<logic:notPresent name="aktVieta">
	<tr>
		<td align="center" colspan="2" class="error">Gyventojas nëra deklaravæs savo gyvenamosios vietos</td>
	</tr>
	</logic:notPresent>
	</table>
	<hr />
	<logic:present name="aktVieta">
	<div align="center">
			<%! String strPath, userAgent; %>
			<% strPath = request.getContextPath().substring(1); %>
			<% userAgent = request.getHeader("User-Agent") ; %>
			
			<% if (userAgent.indexOf(" rv:11.0") >= 1) { %>
				<input type="button" class="button" value="Perþiûrëti aktualius gyvenamosios vietos deklaravimo duomenis" style="width: 400px;" onclick="goToUrl('<%= strPath %>/queryviewdata.do?gvtNr=<bean:write name="aktVieta" property="gvtNr"/>&gvtAsmNr=<bean:write name="aktVieta" property="gvtAsmNr"/>')"/>
			<%}else { %>	
				<input type="button" class="button" value="Perþiûrëti aktualius gyvenamosios vietos deklaravimo duomenis" style="width: 400px;" onclick="goToUrl('queryviewdata.do?gvtNr=<bean:write name="aktVieta" property="gvtNr"/>&gvtAsmNr=<bean:write name="aktVieta" property="gvtAsmNr"/>')"/>
				<%} %>
		
	</div>
	</logic:present>
</logic:present>

<logic:present name="gyvenamosiosVietos">
<br />
<br />
<div class="heading">Ankstesni gyvenamosios vietos deklaravimo duomenys</div>
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%">Galiojo nuo</th>
    <th width="10%">Galiojo iki</th>
    <th width="35%">Dokumento tipas</th>
    <th width="30%">Santykis su gyvenamàja vieta</th>
    <th width="15%">Veiksmai</th>
</tr>
<logic:iterate id="vieta" name="gyvenamosiosVietos">
<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td align="center"><bean:write name="vieta" property="gvtDataNuo" format="yyyy-MM-dd" /></td>
	<td align="center"><bean:write name="vieta" property="gvtDataIki" format="yyyy-MM-dd" /></td>
	<td><bean:write name="vieta" property="saltinis" /></td>
	<td><bean:write name="vieta" property="gvtTipasString" /></td>
	<td align="center">
		<logic:notEqual name="vieta" property="gvtTipas" value="O">
			<a href="../queryviewdata.do?gvtNr=<bean:write name="vieta" property="gvtNr"/>&gvtAsmNr=<bean:write name="vieta" property="gvtAsmNr"/>">Perþiûrëti</a> 
		<% 
		String gvtTipas = ((GyvenamojiVieta)vieta).getGvtTipas();
		if (roles.contains("RL_GVDIS_GL_TVARK") || (!"R".equals(gvtTipas) && !"U".equals(gvtTipas) && !"V".equals(gvtTipas) && !"K".equals(gvtTipas) )){ %>
		|
		<a href="../queryeditdataform.do?gvtNr=<bean:write name="vieta" property="gvtNr"/>&gvtAsmNr=<bean:write name="vieta" property="gvtAsmNr"/>">Koreguoti</a>
		<% } %> 
		</logic:notEqual>
	</td>
</tr>
</logic:iterate>
</table>
</logic:present>
<hr />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td>
<%--  	<a href="javascript:history.go(-1);">&laquo; Atgal</a>  --%>
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
	</td>
	<td align="right">
		<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
<%-- 		<a href="../querycreatedataform.do?&gvtAsmNr=<bean:write name="asmuo" property="asmNr"/>">Daryti naujà gyvenamosios vietos áraðà</a> &raquo;  --%>
		<input type="button" value="Daryti naujà gyvenamosios vietos áraðà &raquo;" onclick="goToUrl('querycreatedataform.do?&gvtAsmNr=<bean:write name="asmuo" property="asmNr"/>')" class="button" style="width: 250px;" />
		<% } %> 
	</td>
</tr>
</table>