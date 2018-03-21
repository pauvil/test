<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.utils.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>

<script language="Javascript">
<!--
	function closePopup(){		
		if (window.opener && !window.opener.closed) {	
			var opener = window.opener;		
			var regnr = '<bean:write name="prasymas" property="regNr"/>';
			opener.clearAsmenys();	
			opener.setPrasymas(regnr, '<bean:write name="prasymas" property="id"/>', '<bean:write name="prasymas" property="tipas"/>');				
			<logic:iterate name="asmenys" id="asmId">
				<%
					long id = Long.parseLong((String)pageContext.getAttribute("asmId"));
					Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, id);
					pageContext.setAttribute("adresas", QueryDelegator.getInstance().getAsmGvtAdresa(request, asmuo.getAsmNr()));
					pageContext.setAttribute("asmuo", asmuo);
				%>
				opener.addAsm('<bean:write name="asmuo" property="asmNr"/>', '<bean:write name="asmuo" property="vardas"/>', '<bean:write name="asmuo" property="pavarde"/>', '<bean:write name="asmuo" property="asmKodas"/>', '<bean:write name="asmuo" property="busena"/>', '<logic:present name="adresas"><bean:write name="adresas"/></logic:present>');
			</logic:iterate>
		}
		else {
			alert('Klaida!');
		}
		window.close();
	}
	closePopup();
//-->
</script>		
	