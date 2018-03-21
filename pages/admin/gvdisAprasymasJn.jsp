<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">GVDIS apraðymas</div>

<html:form action="gvdisaprasymas" method="post">
<br></br>
<logic:present name="aprModulisJn" scope="session">
		<br></br>
		<br></br>
		<div class="heading">Apraðymas</div>
		<table class="form" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td height="50">
					<logic:iterate name="aprModulisJn" id="aprmodulisjn">
						<bean:write name="aprmodulisjn" property="aprasymas" filter="false"/>
				    </logic:iterate>
					</td>
				</tr>
		</table>
		<br></br>
		<div class="heading">Logikos apraðymas</div>

		<table class="form" cellpadding="2" cellspacing="1" width="100%">
			<tr>
				<td height="50">
				
					<logic:iterate name="aprModulisJn" id="aprmodulisjn">
						<bean:write name="aprmodulisjn" property="logikosAprasymas"/>
				    </logic:iterate>
				</td>
			</tr>
		</table>
		<br></br>
</logic:present>
<logic:present name="dbObjektaiJn" scope="session">
	<div class="heading">Duomenø bazës objektai</div>
	<table class="data_table" cellpadding="2" cellspacing="1" width="100%">
	<tr>
		<th align="center">Objektas</th>
		<th align="center">Parametrai</th>
		<th align="center">Tipas</th>
		<th align="center">Schema</th>
		<th align="center">Komentarai</th>
	</tr>
	<logic:iterate name="dbObjektaiJn" id="dbobjektasjn">
	<tr>
		<td align="left"><bean:write name="dbobjektasjn" property="objektas" /></td>
		<td align="left" width="200px"><bean:write name="dbobjektasjn" property="parametrai" /></td>
		<td align="left"><bean:write name="dbobjektasjn" property="tipas" /></td>
		<td align="left"><bean:write name="dbobjektasjn" property="schema" /></td>
		<td align="left"><bean:write name="dbobjektasjn" property="komentarai" /></td>
	</tr>
	</logic:iterate>
	</table>
<br></br>
</logic:present>
<table>
<tr>	
	<td colspan="1" align="left">
		<input type="button" value="&laquo; Atgal" onclick="history.go(-1);" class="button" style="width: 100px;" />
</tr>
</table>
</html:form>
<br></br>
