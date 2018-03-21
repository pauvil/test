<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.GvnaPazyma" %>


<script language="Javascript">
<!--
	function printPazyma()
	{
		var url = 'printgvnapazyma.do';
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

<logic:present name="gvnaPazyma" property="gyvenamojiVieta">
<div class="heading">Paþyma apie átraukimà á GVNA apskaità</div>
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="50"></td>
	<td width="240"><b>Data:</b></td>
	<td width="320"><bean:write name="gvnaPazyma" property="pazymosData" format="yyyy-MM-dd" /></td>
</tr>
<tr>
     <td></td>
     <td><b>Paþymos numeris:</b></td>
     <td><bean:write name="gvnaPazyma" property="regNr" /></td>
</tr>
<tr>
	<td></td>
	<td><b>Asmuo, praðantis paþymos:</b></td>
	<td>
		<bean:write name="gvnaPazyma" property="gyvenamojiVieta.asmuo.vardas" />
		<bean:write name="gvnaPazyma" property="gyvenamojiVieta.asmuo.pavarde" />,
		a. k. <% if (((GvnaPazyma)session.getAttribute("gvnaPazyma"))== null || ((GvnaPazyma)session.getAttribute("gvnaPazyma")).getGyvenamojiVieta().getAsmuo().getAsmKodas() == null) out.print("-"); %> <bean:write name="gvnaPazyma" property="gyvenamojiVieta.asmuo.asmKodas" />
	</td>
</tr>
<tr>
	<td width="50"></td>
	<td width="220"><b>Iðduota pagal praðymà:</b></td>
	<td>
		<logic:notEmpty name="gvnaPazyma" property="prasymoRegNr">
			<bean:write name="gvnaPazyma" property="prasymoRegNr" />
	    <% if (session.getAttribute("prasymoDataYra").equals("1")) out.print("-"); %>
	    </logic:notEmpty>
		<bean:write name="gvnaPazyma" property="prasymoData" format="yyyy-MM-dd" />
	</td>
</tr>
<tr>
     <td></td>
     <td><b>Paþymà iðdavë:</b></td>
		<td><bean:write name="gvnaPazyma" property="istaiga.oficialusPavadinimas" /></td>
</tr>
<tr>
     <td></td>
     <td><b>Átrauktas á GVNA asmenø apskaità prie:</b></td>
     <td>
     <logic:notEmpty name="gvnaSavivaldybe">
     <bean:write name="gvnaSavivaldybe" />
     </logic:notEmpty>
     </td>
</tr>
<tr>
	<td></td>
	<td><b>Átraukimo á apskaità data:</b></td>
	<td>
	<logic:notEmpty name="gvnaGvtDataNuo">
	<bean:write name="gvnaGvtDataNuo" format="yyyy-MM-dd" />
	</logic:notEmpty>
	</td>
</tr>
<tr>
	<td></td>
	<td><b>Pastabos:</b></td>
	<td><bean:write name="gvnaPazyma" property="pastabos" /></td>
</tr>
<logic:present name="gvnaVaikai">
<tr>
	<td colspan="4"><div class="heading" /></td>
</tr>
<tr>
	<td></td>
	<td colspan="2"><u><b>Taip pat á GVNA apskaità átraukti nepilnameèiai vaikai</b></u></td>
    <td><u><b>Átraukimo á apskaità data</b></u></td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td>
		<logic:iterate name="gvnaVaikai" id="vaikas">
			<bean:write name="vaikas" property="vardas" />
			<bean:write name="vaikas" property="pavarde" />
			g. <bean:write name="vaikas" property="asmGimData" format="yyyy-MM-dd" />
			(a.k. <bean:write name="vaikas" property="asmKodas" />)
			<br />
		</logic:iterate>
	</td>
   <td>
        <logic:iterate name="gvnaVaikoGvt" id="vaikoGvt"> 
			<bean:write name="vaikoGvt" format="yyyy-MM-dd" />
			<br />
		</logic:iterate> 
		</td>
</tr>
</logic:present>
<tr>
	<td colspan="4" align="center">
		<hr />
	    	<logic:equal name="gvnaPazyma" property="id" value="0"> 
			<input type="checkbox" checked="checked" id="registerBox" />Registruoti paþymos iðdavimà þurnale<br /><br />
			<input type="button" class="button" value="Spausdinti paþymà" onclick="printPazyma()" />
	    	</logic:equal>
		<logic:equal name="gvnaPazymaSiand" value="true" >
			<input type="button" class="button" value="Spausdinti paþymà" onclick="printPazyma()" />
		</logic:equal>
	</td>
</tr>
</table>
</logic:present>

<logic:notPresent name="gvnaPazyma" property="gyvenamojiVieta">
<logic:present name="gvnaPazyma" property="deklaracija">
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


 <logic:notPresent name="gvnaPazyma" property="gyvenamojiVieta"> 
 <logic:notPresent name="gvnaPazyma" property="deklaracija">
	<div class="heading">Informacija</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1">Ðis asmuo nëra átrauktas á GVNA apskaità</td>
	</tr>
	</table>
	</div>
	<br/>
	<hr/>
</logic:notPresent>
</logic:notPresent>
<%-- <a href="javascript:history.go(-1);">&laquo; Atgal</a>  --%>
<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />