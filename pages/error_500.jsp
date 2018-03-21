<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<html>
  <head>
    <meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=windows-1257"></meta>
    <html:base/>
    <title>Gyvenamosios vietos deklaravimo informacinė sistema</title>
    <link href="../css/styles.css" rel="stylesheet" type="text/css">   
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

  <table width="100%" cellpadding="0" cellspacing="0" height="100%">
  <tr>
    <td width="15"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td background="../img/bg2.gif" width="10"><img src="../img/pix.gif" width="10" height="5" /></td>
    <td width="5"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td class="line1" width="1"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td>
		<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">		        
			<tr>
		      <td class="graybg" height="40" width="60%">
		      	<html:link action="first"><img src="../img/logo2.gif" border="0" /></html:link>
		      </td>
		      <td align="right" valign="middle" class="graybg" width="40%" >
			  <tr>
			  	<td colspan="2"><img src="../img/pix.gif" width="1" height="3" /></td>
			  </tr>
		      </td>
			</tr>
			<tr>
	            <td colspan="2">
	                <table cellspacing="0" cellpadding="4" width="100%" class="darkbg" border="0">
	                <tr>
	                    <td><b><html:link action="first">Į pradžią</html:link></b></td>
	                    <td align="right">
	                    	<bean:write name="dateString" scope="request" />
						</td>
	                </tr>
	                </table>
	            </td>
	        </tr>
	        <tr>
              <td height="99%" colspan="2">
				<jsp:include page="exception.jsp" />
              </td>
            </tr>
	        <tr>
			  <td class="darkbg" colspan="2">&nbsp;</td>
			</tr>
		</table>			
    </td>
    <td class="line1" width="1"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td width="5"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td background="../img/bg2.gif" width="10"><img src="../img/pix.gif" width="10" height="5" /></td>
    <td width="15"><img src="../img/pix.gif" width="1" height="5" /></td>
  </tr>
  </table>

  </body>
</html>