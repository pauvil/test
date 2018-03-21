<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div class="heading">GVDIS apraðymas
	<logic:present name="aprModulis" scope="session"> - 
		<bean:write name="aprModulis" property="pavadinimas"/> (Versija: <bean:write name="aprModulis" property="versija"/>. Apraðymas sukurtas: <bean:write name="aprModulis" property="sukurimoData"/>)
	</logic:present>
</div>
<html:form action="gvdisaprasymas" method="post">
<table class="form" cellpadding="2" cellspacing="1" width="100%">
<tr>
	<td width="10%"> </td>
	<td width="30%" align="right"><b>Meniu grupë:</b></td>
	<td width="60%" align="left">
		<html:select property="meniuGrupesKodas" styleClass="input" onchange="document.GvdisAprasymasForm.meniuPunktoKodas.selectedIndex=0; document.GvdisAprasymasForm.submit();">
			<html:option value="">  --- Pasirinkite ---</html:option>
			<html:options collection="meniuGrupes" property="id" labelProperty="pavadinimas" /> 
		</html:select>
	</td>
</tr>
<tr>
	<td width="10%"> </td>
	<td width="30%" align="right"><b>Meniu punktas:</b></td>
	<td width="60%" align="left">
		<html:select property="meniuPunktoKodas" styleClass="input" onchange="document.GvdisAprasymasForm.submit();">
			<html:option value="">  --- Pasirinkite ---</html:option>
			<logic:present name="meniuPunktai">
				<html:options collection="meniuPunktai" property="id" labelProperty="pavadinimas" /> 
			</logic:present>
			</html:select>

	</td>
</tr>
</table>
<br></br>
<br></br>
<logic:present name="aprModulis" scope="session">
		<br></br>
				<table class="form" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td align="right">
						<input type="button" class="button" style="width: 160px;" value="Sukurti naujà apraðymo versijà" onclick="goToUrl('creategvdisaprasymasform.do?modulioId=<bean:write name="aprModulis" property="modulioId" />');">
					</td>
				</tr>
				</table>
		<br></br>
		<div class="heading">Apraðymas</div>
		<table class="form" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td height="50">
						<bean:write name="aprModulis" property="aprasymas" filter="false"/>
						
					</td>
				</tr>
				<tr>
					<td align="right">
						<input type="button" class="button" value="Redaguoti" style="width: 100px;" onclick="goToUrl('editgvdisaprasymasform.do?modulioId=<bean:write name="aprModulis" property="modulioId" />');" />
					</td>
				</tr>
		</table>
		<br></br>
		<div class="heading">Logikos apraðymas</div>

		<table class="form" cellpadding="2" cellspacing="1" width="100%">
			<tr>
				<td height="50">
					<bean:write name="aprModulis" property="logikosAprasymas" filter="false" />
				</td>
			</tr>
			<tr>
				<td align="right">
					<input type="button" class="button" style="width: 100px;" value="Redaguoti" onclick="goToUrl('editgvdisaprasymasform.do?modulioId=<bean:write name="aprModulis" property="modulioId" />');" />
				</td>
			</tr>
		</table>
		<br></br>
		<input type="hidden" name="modulioId" value="<bean:write name="aprModulis" property="modulioId" />" />
</logic:present>

<logic:present name="dbObjektai" scope="session">
	<div class="heading">Duomenø bazës objektai</div>
	<table class="data_table" cellpadding="2" cellspacing="1" width="100%">
	<tr>
		<th align="center">Objektas</th>
		<th align="center">Parametrai</th>
		<th align="center">Tipas</th>
		<th align="center">Schema</th>
		<th align="center">Komentarai</th>
		<th align="center">Veiksmai</th>
	</tr>
	<logic:iterate name="dbObjektai" id="dbobjektas">
	<tr>
		<td align="left"><bean:write name="dbobjektas" property="objektas" /></td>
		<td align="left" width="200px"><bean:write name="dbobjektas" property="parametrai" /></td>
		<td align="left"><bean:write name="dbobjektas" property="tipas" /></td>
		<td align="left"><bean:write name="dbobjektas" property="schema" /></td>
		<td align="left"><bean:write name="dbobjektas" property="komentarai" /></td>
		<td align="center">
		<logic:present name="aprModulis" scope="session">
			<a href="../editgvdisaprdbobjektaiform.do?id=<bean:write name="dbobjektas" property="id" />&modulioId=<bean:write name="aprModulis" property="modulioId" />">Redaguoti</a>
			|<a href="../deletegvdisaprdb.do?id=<bean:write name="dbobjektas" property="id" />" onclick="return confirm('Ar tikrai iðtrinti ðá áraðà?');">Iðtrinti</a>
			</logic:present>	
		</td>	
		</tr>
	</logic:iterate>
	<tr>
		<td colspan="6" align="right">
			<logic:present name="aprModulis" scope="session">
				<input type="button" class="button" style="width: 160px;" value="Pridëti naujà DB objektà" onclick="goToUrl('editgvdisaprdbobjektaiform.do?id=0&modulioId=<bean:write name="aprModulis" property="modulioId" />');">
			</logic:present>
		</td>
	</tr>
	</table>
<br></br>


<div class="heading">Pakeitimø istorija</div>
<table class="data_table" cellpadding="2" cellspacing="1" width="100%">
	<tr>
		<th align="center">Versija</th>
		<th align="center">Data</th>
		<th align="center">Veiksmai</th>
	</tr>
	<logic:iterate name="aprVersijos" id="aprversijos">
		<tr>
			<td align="left"><bean:write name="aprversijos" property="versija" /></td>
			<td align="left"><bean:write name="aprversijos" property="data" /></td>
			<td align="center">
			<a href="../gvdisaprasymasjn.do?versijosId=<bean:write name="aprversijos" property="versijosId" />&modulioId=<bean:write name="aprModulis" property="modulioId" />">Perþiûrëti</a>	
		</td>	
		</tr>
	</logic:iterate>
</table>
</logic:present>
</html:form>

<br></br>
