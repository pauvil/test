<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="javascript">
<!--
	function flip()
	{
		var addInfoDiv = document.getElementById("stackTraceView");
		if (addInfoDiv.style.display == "block"){
			addInfoDiv.style.display = "none";
		}
		else {
			addInfoDiv.style.display = "block";
		}
		return false;
	}
    
//-->
</script>    



<table cellspacing="0" cellpadding="3" width="80%" class="header" border="0" height="150" align="center">
<tr>
	<div class="heading">Klaida</div>
</tr>
<tr>
	<td align="center" valign="middle">
		<img src="../img/warning.gif" />
	</td>
	<td align="center" valign="middle">
		<b><bean:write name="errorMessage" filter="false" /></b>
		<br />
		<logic:notPresent name="removeKreiptis">
		Kreipkitës á sistemos administratoriø.
		</logic:notPresent>
		<br /><br />
		<logic:present name="lastException">
		<b>Klaidos praneðimas: </b>
			<%-- <logic:present name="removeKreiptis"> <font color="RED"> </logic:present> --%>
				<bean:write name="friendlyErrorMessage"  filter="false"/>
				<bean:write name="lastException" property="message"/>	
			<%-- <logic:present name="removeKreiptis"> </font> </logic:present>	--%>	
        <br /><br />
		</logic:present>
		<logic:present name="e3Exception">		
		Papildoma klaida: <bean:write name="e3Exception" property="message"/>
		</logic:present>
		<logic:notPresent name="removeKreiptis">
		
			<p><b>Nenumatyta klaida</b></p>
			<p align="left">
			Spauskite narðyklës mygtukà „back“ (atgal) ir pabandykite pakartoti veiksmà atnaujinus duomenis formoje.
			Jeigu klaida kartojasi, praneðkite apie jà.

			<p align="left">Praneðant apie klaidà bûtina:</p>
			<div align="left">
				<ol>
					<li>Átraukti visà klaidos tekstà (kuris bus parodytas paspaudus þemiau
					esanèià nuorodà „rodyti pilnà klaidos praneðimà“);</li>
					<li>Apraðyti veiksmus kuriuos atlikus ávyko klaida;</li>
					<li>Jei buvo pildoma forma – átraukti duomenis kurie buvo ávedinëjami
					formoje (galima prisegti ekrano vaizdà kurá pamatysite paspaudus
					narðyklës „back“ (atgal) mygtukà);</li>
				</ol>
			</div>
		
		</logic:notPresent>

		<logic:present name="stackTrace">
			<a href="#" onclick="return flip();">
				<small>Rodyti pilnà klaidos praneðimà &raquo;</small>
			</a>
		</logic:present>
    </td>
</tr>		
<tr>		
    <td>
    </td>
    <td>
    	<logic:present name="stackTrace">
		<div id="stackTraceView" style="padding-left: 20px; border: 1px solid #CCCCCC; display: none; color: red;">
			<bean:write name="stackTrace" filter="false"/>
			<logic:present name="e3Exception">
				<br/>
				Papildoma klaida: <bean:write name="e3StackTrace" filter="false"/>
			</logic:present>
		</div>
		</logic:present>
				
    </td>
</tr>
</table>

<hr width="80%" />
<br /><br />
<center>
 <input type="button" class="button"  value="&laquo; Atgal" onclick="history.go(-1);" style="width: 150px;" /> 
<%--  <img src="../img/logoff.gif" align="absmiddle"/ > <html:link action="logout">Atsijungti</html:link>&nbsp;  --%>
</center>


