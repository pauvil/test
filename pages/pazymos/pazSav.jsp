<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="Javascript">
<!--
	function printPazyma()
	{
		var url = 'printsavpazyma.do';
		var registerBox = document.getElementById('registerBox');
		var checkNenurodytaDeklData = document.getElementById('checkNenurodytaDeklData').value;
		if (registerBox && registerBox.checked){
			url += '?register=on';
		}
        if (!document.all){
            url = '../' + url;
        }


		if(checkNenurodytaDeklData=='T'){
        	if(confirm('Yra asmenø, kuriø deklaravimo data nenurodyta. Ar tikrai norite spausdinti paþymà?')){
	        	window.open(url, '_pazyma', 'status=yes,menubar=yes,location=yes,scrollbars=yes,resizable=yes,screenX=0,screenY=0');
    	    }
		}
		else{
        	window.open(url, '_pazyma', 'status=yes,menubar=yes,location=yes,scrollbars=yes,resizable=yes,screenX=0,screenY=0');
		}
	}
//-->
</script>

<div class="heading">Paþyma patalpos savininkams</div>
<table cellpadding="2" cellspacing="2" width="100%">
<tr>
	<td width="50"></td>
	<td width="220">Pasirinktas adresas:</td>
	<td><b><bean:write name="addressString" /></b></td>
</tr>
<tr>
	<td></td>
	<td>Data:</td>
	<td><b><bean:write name="gyvenamojiVieta" property="gvtRegData" format="yyyy-MM-dd" /></b></td>
</tr>
<tr>
	<td colspan="3"><div class="heading" /></td>
</tr>
<%-- <logic:notEmpty name="gyventojai"> --%>
<tr>
	<td></td>
	<td colspan="2"><u><b>Ðiame adrese gyvenamàjà vietà deklaravæ gyventojai:</b></u></td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td>
		<logic:iterate name="gyventojai" id="gyventojas">
			<bean:write name="gyventojas" property="vardas" />
			<bean:write name="gyventojas" property="pavarde" />
			g. <bean:write name="gyventojas" property="asmGimData" format="yyyy-MM-dd" />
			(a.k. <bean:write name="gyventojas" property="asmKodas" />)
			<br />
		</logic:iterate>
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr />
		<logic:empty name="perziura">
			<input type="checkbox" checked="checked" id="registerBox" />Registruoti paþymos iðdavimà þurnale<br /><br />
		</logic:empty>
		
		<input type="hidden" id="checkNenurodytaDeklData" value="<bean:write name="nenurodytaDeklData" />"/>
		
		<input type="button" class="button" value="Spausdinti paþymà" onclick="printPazyma()" />
	</td>
</tr>
<%-- </logic:notEmpty> --%>
</table>

<%--<logic:empty name="gyventojai">
	<br />
	<div class="heading">Informacija</div>
	<br/>
	<div align="center">
	<table cellpadding="2" cellspacing="1">
	<tr>
		<td><img src="../img/warning.gif" /></td>
		<td class="message1">Nëra asmenø, kurie ðiuo metu bûtø deklaravæ savo gyvenamàjà vietà ðiame adrese</td>
	</tr>
	</table>
	</div>
	<br/>
	<hr />
</logic:empty> --%>
<%--<a href="../savpazymaform.do">&laquo; Atgal</a> --%>
<%--<a href="javascript: history.go(-1)">&laquo; Atgal</a> --%>
<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
