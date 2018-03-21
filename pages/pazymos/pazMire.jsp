<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.GvPazyma" %>
<script language="Javascript">
<!--
	function printPazyma()
	{
		var url = 'printmirepazyma.do';
		var registerBox = document.getElementById('registerBox');
		if (registerBox && registerBox.checked){
			url += '?register=on';
		}
        if (!document.all){
            url = '../' + url;
        }
        window.open(url, '_pazyma', 'status=yes,menubar=yes,location=yes,scrollbars=yes,resizable=yes,screenX=0,screenY=0');
	}
//-->
</script>

<logic:notEmpty name="mirePazyma" property="gyvenamojiVieta">
<div class="heading">Paþyma apie paskutinæ deklaruotà gyvenamàjà vietà</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="50"></td>
	<td width="220"><b>Data:</b></td>
	<td><bean:write name="mirePazyma" property="pazymosData" format="yyyy-MM-dd" /></td>
</tr>
<tr>
	<td></td>
	<td><b>Asmuo:</b></td>
	<td>
		<bean:write name="mirePazyma" property="gyvenamojiVieta.asmuo.vardas" />
		<bean:write name="mirePazyma" property="gyvenamojiVieta.asmuo.pavarde" />,
		a. k. <% if (((GvPazyma)session.getAttribute("mirePazyma")).getGyvenamojiVieta() == null || ((GvPazyma)session.getAttribute("mirePazyma")).getGyvenamojiVieta().getAsmuo().getAsmKodas() == null) out.print("-"); %> <bean:write name="mirePazyma" property="gyvenamojiVieta.asmuo.asmKodas" />
	</td>
</tr>
<tr>
	<td width="50"></td>
	<td width="220"><b>Iðduota pagal praðymà:</b></td>
	<td>
		<logic:notEmpty name="mirePazyma" property="prasymoRegNr">
			<bean:write name="mirePazyma" property="prasymoRegNr" /> - 
		</logic:notEmpty>
		<bean:write name="mirePazyma" property="prasymoData" format="yyyy-MM-dd" />
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Pastabos:</b></td>
	<td><bean:write name="mirePazyma" property="pastabos" /></td>
</tr>
<tr>
	<td colspan="3"><div class="heading" /></td>
</tr>
<tr>
	<td></td>
	<td colspan="2"><u><b>Paskutinës deklaruotos gyvenamosios vietos adresas</b></u></td>
</tr>
<tr>
	<td colspan="2"></td>
	<td><bean:write name="addressString" /></td>
</tr>
<logic:present name="mireVaikai">
<tr>
	<td colspan="3"><div class="heading" /></td>
</tr>
<tr>
	<td></td>
	<td colspan="2"><u><b>Tame paèiame adrese gyvenamàjà vietà deklaravæ nepilnameèiai vaikai</b></u></td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td>
		<logic:iterate name="mireVaikai" id="vaikas">
			<bean:write name="vaikas" property="vardas" />
			<bean:write name="vaikas" property="pavarde" />
			g. <bean:write name="vaikas" property="asmGimData" format="yyyy-MM-dd" />
			(a.k. <bean:write name="vaikas" property="asmKodas" />)
			<br />
		</logic:iterate>
	</td>
</tr>
</logic:present>
<tr>
	<td colspan="3" align="center">
		<hr />
		<logic:equal name="mirePazyma" property="id" value="0">
			<input type="checkbox" checked="checked" id="registerBox" />Registruoti paþymos iðdavimà þurnale<br /><br />
		</logic:equal>
		<input type="button" class="button" value="Spausdinti paþymà" onclick="printPazyma()" />
	</td>
</tr>
</table>
</logic:notEmpty>

<logic:notPresent name="mirePazyma" property="gyvenamojiVieta">
<logic:present name="mirePazyma" property="deklaracija">
	<div class="heading">Praneðimas</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/info.png" /></td>
		<td class="message1">Ði paþyma buvo iðduota pagal nepilnai ávestà deklaracijà</td>
	</tr>
	</table>
	</div>
	<br/>
	<hr/>
</logic:present>
</logic:notPresent>

<logic:notPresent name="mirePazyma" property="gyvenamojiVieta">
<logic:notPresent name="mirePazyma" property="deklaracija">
	<div class="heading">Informacija</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1">Ðis asmuo nëra deklaravæs gyvenamosios vietos Lietuvoje</td>
	</tr>
	</table>
	</div>
	<br/>
	<hr/>
</logic:notPresent>
</logic:notPresent>
<%--  <a href="../mirepazymaform.do">&laquo; Atgal</a>  --%>
<input type="button" value="&laquo; Atgal" onclick="goToUrl('mirepazymaform')" class="button" style="width: 100px;" />
