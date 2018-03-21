<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<link href="css/styles.css" rel="stylesheet" type="text/css">


<div class="heading">I�duot� �iam asmeniui asmens dokument� s�ra�as</div>
<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="20%">Tipas</th>
    <th width="10%">Pilietyb�</th>    
    <th width="10%">Numeris</th>
    <th width="15%">I�davimo data</th>
    <th width="30%">Kas i�dav�</th>
    <th width="10%">Galiojimas</th>
    <th width="10%">Galioja iki</th>
</tr>

<logic:iterate name="asmensDokumentai" id="asmensDokumentas">
	<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td align="center"><bean:write name="asmensDokumentas" property="dokRusis" /></td>
	<td align="center">
		<logic:present name="asmensDokumentas" property="valstybe">
			<bean:write name="asmensDokumentas" property="valstybe.pavadinimas" />
		</logic:present>
		<logic:notPresent name="asmensDokumentas" property="valstybe">
			LIETUVA
		</logic:notPresent>
	</td>
	<td align="center"><bean:write name="asmensDokumentas" property="dokNum" /></td>
	<td align="center"><bean:write name="asmensDokumentas" property="dokIsdData" format="yyyy-MM-dd" /></td>
	<td align="center">
		<bean:write name="asmensDokumentas" property="dokTarnyba" />
		<bean:write name="asmensDokumentas" property="tarnyba" />
	</td>
	<td align="center">
		<logic:equal name="asmensDokumentas" property="dokBusena" value="N">
			<span style="color: red; font-weight: bold;">Negalioja</span>
		</logic:equal>
		<logic:equal name="asmensDokumentas" property="dokBusena" value="G">
			Galioja
		</logic:equal>	
	</td>
	<td align="center"><bean:write name="asmensDokumentas" property="dokGaliojaIki" format="yyyy-MM-dd" /></td>
	</tr>
</logic:iterate>

<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td colspan="3" align="center">
		<hr />
		<input type="button" class="button" value="U�daryti" onclick="window.close();" />
	</td>
</tr>
</table>