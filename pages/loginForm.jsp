<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<div align="center">
	<table cellspacing="4" cellpadding="2" width="330" border="0">
	<html:form action="login" focus="login">
    <tr>
    	<td class="darkbg" colspan="2">
    		<b>Prisijungti prie sistemos</b>
    	</td>
    </tr>
    
	<logic:notPresent name="userNotGood">

	<logic:present name="loginError">
	<tr>
		<td class="error" colspan="2">
			Nepavyko prisijungti prie sistemos. Patikrinkite, ar teisingai �ved�te vartotojo vard� ir slapta�od�.
		</td>
	</tr>
	</logic:present>

	<logic:present name="loginVersionError">
	<tr>
		<td class="error" colspan="2">
			Aplikacijos versija nesutampa su duomen� baz�s versija. Kreipkit�s � sistemos administratori�.
		</td>
	</tr>
	</logic:present>
	
	</logic:notPresent>

	
	<logic:present name="userNotGood">
	<tr>
		<td class="error" colspan="2">
			Vartotojo duomenys nekorekti�ki. Kreipkit�s � sistemos administratori�.
		</td>
	</tr>
	</logic:present>


    <tr>
    	<td><b>Vartotojo vardas:</b></td>
    	<td align="right">
    		<html:text property="login" styleClass="input" style="width: 200px;" />
    	</td>
    </tr>
    <tr>
    	<td><b>Slapta�odis:</b></td>
    	<td align="right">
    		<html:password property="password" styleClass="input" style="width: 200px;" />
    	</td>
    </tr>
    <tr>
    	<td></td>
    	<td align="right"><html:submit value="Prisijungti" styleClass="button" style="width: 150px;" /></td>
    </tr>
    <tr>
    	<td colspan="2"><hr />
    	</td>
    </tr>
    </html:form>
    </table>
</div>
<%
session.removeAttribute("userNotGood");
%>