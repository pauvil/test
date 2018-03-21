<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/as" prefix="as" %>
<%@ page import="com.algoritmusistemos.gvdis.web.*" %>

<html>
  <head>
    <meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=windows-1257"></meta>
    <title>Gyvenamosios vietos deklaravimo informacinė sistema</title>
    <link href="css/styles.css" rel="stylesheet" type="text/css">
	 <%
      String pageName = (String)session.getAttribute("POPUP_STATE");
      if (Constants.SPR_ADD_PRASYMAS.equals(pageName)){
         %><html:base/><%
      }	%>	
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <%
      pageName = (String)session.getAttribute("POPUP_STATE");
      if (Constants.ADDRESS_BROWSER.equals(pageName)){
         %><jsp:include page="query/addressBrowser.jsp"/><%
      }	
      else if (Constants.SPR_ADD_PERSON_FORM.equals(pageName)){
         %><jsp:include page="sprendimai/addPersonForm.jsp"/><%
      }	
      else if (Constants.SPR_ADD_PERSON_RESULTS.equals(pageName)){
         %><jsp:include page="sprendimai/addPersonResults.jsp"/><%
      }	
	  else if (Constants.SPR_ADD_PRASYMAS.equals(pageName)){
         %><jsp:include page="sprendimai/addPrasymas.jsp"/><%
      }	
	   else if (Constants.SPR_ADD_PRASYMAS_RESULT.equals(pageName)){
         %><jsp:include page="sprendimai/addPrasymasResult.jsp"/><%
      }
  %>
  </body>
</html>  