<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<div class="heading">Susieti asmená su Gyventojø registru</div>
<br/>
<center>

<table class="data_table" cellpadding="2" cellspacing="1" width="90%" border="0">
<tr>
	<th width="20%">&nbsp;</th>
	<th width="40%"><b>Deklaracijà pateikæs asmuo</b></th>
	<th width="40%"><b>Asmuo Gyventojø registre</b></th>
</tr>
<tr>
	<td width="20%"><b>Vardas</b></td>
	<td width="40%"><bean:write name="laikinasAsmuo" property="vardas" /></td>
	<td width="40%"><bean:write name="asmuo" property="vardas" /></td>
</tr>
<tr>
	<td width="20%"><b>Pavardë</b></td>
	<td width="40%"><bean:write name="laikinasAsmuo" property="pavarde" /></td>
	<td width="40%"><bean:write name="asmuo" property="pavarde" /></td>
</tr>
<tr>
	<td width="20%"><b>Asmens kodas</b></td>
	<td width="40%">&nbsp;</td>
	<td width="40%"><bean:write name="asmuo" property="asmKodas" /></td>
</tr>
<tr>
	<td width="20%"><b>Gimimo data</b></td>
	<td width="40%"><bean:write name="laikinasAsmuo" property="gimimoData" format="yyyy-MM-dd"/></td>
	<td width="40%"><bean:write name="asmuo" property="asmGimData" format="yyyy-MM-dd"/></td>
</tr>
<tr>
	<td width="20%"><b>Asmens dokumentai</b></td>
	<td width="40%">
		<ul>
		<li>Nr. <bean:write name="laikinasAsmuo" property="deklaracija.dokumentoNr" />,
		Iðduota <bean:write name="laikinasAsmuo" property="deklaracija.dokumentoData" format="yyyy-MM-dd"/>
		<bean:write name="laikinasAsmuo" property="deklaracija.dokumentoIsdavejas" />
		</li>
		</ul>
	 </td>
	<td width="40%">
	<logic:notEmpty name="asmensDokumentai">
		<ul>		
		<logic:iterate name="asmensDokumentai" id="dokumentas">
			<li><bean:write name="dokumentas" property="dokRusis" />, nr. <bean:write name="dokumentas" property="dokNum" />,
			Iðduota <bean:write name="dokumentas" property="dokIsdData" format="yyyy-MM-dd" /> <bean:write name="dokumentas" property="tarnyba" />
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	<logic:empty name="asmensDokumentai">nëra</logic:empty>
	</td>
</tr>
</table>
<br />
<logic:present name="hasNotCompletedDeclartions">
	<span class="error">Nebaigtos pildyti ðio asmens deklaracijos. Jo susieti su Gyventojø registru negalima.</span><br />
</logic:present>


<logic:present name="kertasilaikotarpis">
	<span class="error">Asmuo turi deklaruotà gyvenamàjà vietà, kurios laikotarpis kertasi su deklaracijos, pagal kurià siejama.</span><br />
</logic:present>
<logic:notPresent name="kertasilaikotarpis">
<logic:notPresent name="hasNotCompletedDeclartions">


	<b style="font-size: 14px;">Ar tai tikrai tas pats asmuo?</b><br /><br />
	<input type="button" value="Taip, susieti" class="button" onclick="goToUrl('LinkWithGRPerform.do')" />
</logic:notPresent>
</logic:notPresent>
<input type="button" value="Atðaukti" class="button" onclick="goToUrl('TempCitizensList.do')" />
</center>