<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<style type="text/css">
td.gvdis {
	border: 1px solid black;
}
td.gvdis_sp {
	border-left: 4px solid black;
}
</style>
<html:form action="TestAtvDeklaracijaPerform">
<bean:define id="forma" name="TestDeklaracijaForm"/>
<div class="heading" align="center">GYVENAMOSIOS VIETOS DEKLARACIJA, PILDOMA ASMENIUI PAKEITUS GYVENAMÀJÀ VIETÀ LIETUVOS RESPUBLIKOJE AR ATVYKUS GYVENTI Á LIETUVOS RESPUBLIKÀ</div>
<script language="javascript">
<!--	
//  -->
</script>
<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="2">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20%" class="gvdis">
				1. Asmens kodas<br>
				<html:text property="asmensKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" readonly="true" />
			</td>			
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				1. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai1" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo1" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData1" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>
			
		<tr>
			<td width="20%" class="gvdis">	
			&nbsp;&nbsp;&nbsp;2.			
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				2. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai2" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo2" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData2" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>
		
		<tr>
			<td width="20%" class="gvdis">	
			&nbsp;&nbsp;&nbsp;3.			
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				3. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai3" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo3" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData3" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>		
		<tr>
			<td width="20%" class="gvdis">	
			&nbsp;&nbsp;&nbsp;4.			
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				4. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai4" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo4" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData4" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>
		<tr>
			<td width="20%" class="gvdis">	
			&nbsp;&nbsp;&nbsp;5.			
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				5. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai5" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo5" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData5" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>	
		<tr>
			<td width="20%" class="gvdis">	
			&nbsp;&nbsp;&nbsp;6.			
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				6. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai6" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo6" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData6" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>
		<tr>
			<td width="20%" class="gvdis">	
			&nbsp;&nbsp;&nbsp;7.			
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				7. Gimimo data
				<div style="white-space: nowrap;">
					<html:text property="gimimoMetai7" styleClass="inputFixed" maxlength="4" style="width:82px;" readonly="true" /> - 
					<html:text property="gimimoMenuo7" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" /> -				
					<html:text property="gimimoData7" styleClass="inputFixed" maxlength="2" style="width:40px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>			
		</tr>					
		</table>
		<br/>
		&nbsp;&nbsp;1.&nbsp;&nbsp;<bean:write name="log1"/>	<br/>	
		&nbsp;&nbsp;2.&nbsp;&nbsp;<bean:write name="log2"/>	<br/>
		&nbsp;&nbsp;3.&nbsp;&nbsp;<bean:write name="log3"/>	<br/>
		&nbsp;&nbsp;4.&nbsp;&nbsp;<bean:write name="log4"/>	<br/>
		&nbsp;&nbsp;5.&nbsp;&nbsp;<bean:write name="log5"/>	<br/>
		&nbsp;&nbsp;6.&nbsp;&nbsp;<bean:write name="log6"/>	<br/>
		&nbsp;&nbsp;7.&nbsp;&nbsp;<bean:write name="log7"/>	<br/>			
	</td>
</tr>
<logic:empty name="asmensDokumentai">
<script language="Javascript">
<!--

switchGaliojimoData('<%= "" %>');
//-->
</script>
</logic:empty>
</html:form>
</table>
<br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript: history.go(-1)">&laquo;Atgal</a>
<script language="Javascript">
<!--
	function menu()
	{		
		var theDiv = document.getElementById('menu1');
		theDiv.innerHTML = '';
		theDiv = document.getElementById('menu2');
		theDiv.innerHTML = '';
		theDiv = document.getElementById('menu3');
		theDiv.innerHTML = '';
	}
	menu();
//-->	
</script>