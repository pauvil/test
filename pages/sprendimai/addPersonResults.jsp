<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script language="Javascript">
<!--
	function closePopup()
	{
		var asmNr = '<bean:write name="asmuo" property="asmNr" />';
		var vardas = '<bean:write name="asmuo" property="vardas" />';
		var pavarde = '<bean:write name="asmuo" property="pavarde" />';
		var kodas = '<bean:write name="asmuo" property="asmKodas" />';
		var busena = '<bean:write name="asmuo" property="busena" />';
		<logic:present name="adresas">
			var adresas = '<bean:write name="adresas"/>';
			if (window.opener && !window.opener.closed) {
				var opener = window.opener;
				opener.addAsm(asmNr, vardas, pavarde, kodas, busena, adresas);
			}
		</logic:present>
		<logic:notPresent name="adresas">
			if (window.opener && !window.opener.closed) {
				var opener = window.opener;
				opener.addAsm(asmNr, vardas, pavarde, kodas, busena);
			}
		</logic:notPresent>
		else {
			alert('Klaida!');
		}
		window.close();
	}
	function closePopupVienasAsmuo()
	{
		var asmNr = '<bean:write name="asmuo" property="asmNr" />';
		var vardas = '<bean:write name="asmuo" property="vardas" />';
		var pavarde = '<bean:write name="asmuo" property="pavarde" />';
		var kodas = '<bean:write name="asmuo" property="asmKodas" />';
		if (window.opener && !window.opener.closed) {
			var opener = window.opener;
			opener.addAsm2(asmNr, vardas, pavarde, kodas);
		}
		else {
			alert('Klaida!');
		}
		window.close();
	}	
	
	<% if("1".equals(request.getParameter("mode"))) {%>closePopupVienasAsmuo(); <%} else {%> closePopup(); <%}%>

//-->
</script>