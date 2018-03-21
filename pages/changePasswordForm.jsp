<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table width="100%" height="100%" border="0">
<tr>
    <td align="center" valign="middle">
        <table border="0" cellpadding="1" width="350px">
            <html:form action="changepassword" method="POST">
            <tr>
                <td colspan="2" class="darkbg" style="padding: 4px;">
                	Slaptaþodþio keitimas
                </td>
            </tr>
            <tr>
            	<td colspan="2" class="error">
		            <% if (request.getParameter("forced") != null){ %>
					Pasibaigë Jûsø slaptaþodþio galiojimas. Privalote já pasikeisti.<br />
		            <% } %>
		       		<logic:present name="newPasswordError">
            		<bean:write name="newPasswordError"/>
		       		</logic:present>
            	</td>
            </tr>
            <tr>
                <td><b>Dabartinis slaptaþodis:</b></td>
                <td align="right"><html:password property="currPassword" styleClass="input" style="width: 200px;"/></td>
            </tr>
            <tr>
                <td><b>Naujas slaptaþodis:</b></td>
                <td align="right"><html:password property="newPassword" styleClass="input" style="width: 200px;"/></td>
            </tr>
            <tr>
                <td><b>Pakartokite slaptaþodá:</b></td>
                <td align="right"><html:password property="newPassword2" styleClass="input" style="width: 200px;"/></td>
            </tr>
            <tr>
                <td colspan="2" align="right">
                    <html:submit styleClass="button" value="Pakeisti slaptaþodá" style="width: 200px;"/>
                </td>
            </tr>
            <tr>
                <td colspan="2"><hr /></td>
            </tr>
            </html:form>
        </table>    
    <td>
</tr>
</table>
