<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.*" %>
<html>
  <head>
    <meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=windows-1257"/>
	<meta http-equiv="Cache-Control" content="no-store"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="Expires" content="0"/>
	
<meta http-equiv="cache-control" content="no-cache"> <!-- tells browser not to cache --> 
<meta http-equiv="expires" content="0"> <!-- says that the cache expires 'now' --> 
<meta http-equiv="pragma" content="no-cache"> <!-- says not to use cached stuff, if there is any -->
	
<%--	<meta http-equiv=Refresh content="1"/>  --%>
    <html:base/>
    <title>Gyvenamosios vietos deklaravimo informacinė sistema</title>
    <link href="../css/styles.css" rel="stylesheet" type="text/css">


    
    <script type="text/javascript">
    

    
    function goToUrl(url)
    {
        if (!document.all){
			
            url = '../' + url;
        }
        window.location.href = url;
        
    }
    <!--
    
    function openPopup(url)
    {
        if (!document.all){
            url = '../' + url;
        }
        window.open(url, '_popup', 'width=600,height=500,status=no,location=no,menubar=no,scrollbars=yes,resizable=yes,screenX=0,screenY=0');
        return false;
    }
    function openHelp(code)
    {
        window.open('/admh/viewer.jsp?code='+code, '_popup', 'width=1000,height=700,status=no,location=no,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,screenX=0,screenY=0');
        return false;
    }
    

	// [Cookie] Sets value in a cookie
	function setCookie(cookieName, cookieValue, expires, path, domain, secure)
	{
	 document.cookie = escape(cookieName) + '=' + escape(cookieValue)
			+ (expires ? '; expires=' + expires.toGMTString() : '')
			+ (path ? '; path=' + path : '')
			+ (domain ? '; domain=' + domain : '')
			+ (secure ? '; secure' : '');
	};

	// [Cookie] Gets a value from a cookie
	function getCookie(cookieName)
	{
	 var cookieValue = '';
	 var posName = document.cookie.indexOf(escape(cookieName) + '=');
	 if (posName != -1)
	  {
	   var posValue = posName + (escape(cookieName) + '=').length;
	   var endPos = document.cookie.indexOf(';', posValue);
	   if (endPos != -1) cookieValue = unescape(document.cookie.substring(posValue, endPos));
	    else cookieValue = unescape(document.cookie.substring(posValue));
		}
	 return (cookieValue);
	};
    //-->
    </script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%--  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="JavaScript:timedRefresh(0);"> --%>
<% if (!"on".equals(request.getParameter("print"))){ %>
  <table width="100%" cellpadding="0" cellspacing="0" height="100%">
  <tr>
    <td width="15"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td background="../img/bg2.gif" width="10"><img src="../img/pix.gif" width="10" height="5" /></td>
    <td width="5"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td class="line1" width="1"><img src="../img/pix.gif" width="1" height="5" /></td>
    <td>
        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
        <tr><td colspan="2"><img src="../img/pix.gif" width="1" height="3" /></td></tr>
        <tr>
            <td class="graybg" height="40" width="60%">
            	<html:link action="first"><img src="../img/logo2.gif" border="0" /></html:link>
            </td>
            <td align="right" valign="middle" class="graybg" width="40%" >
            	<div align="center">
				<% if(null != session.getAttribute("userLogin")){ %>
             		 <b><bean:write name="userName"/> <bean:write name="userSurname"/></b> 
             		(<bean:write name="userIstaiga"/>)
		           	<hr />
        	    	<img src="../img/help.gif" align="absmiddle" /> <a href="" onclick="return openHelp('<bean:write name="HELP_CODE" />');">Pagalba</a>
            		&nbsp;|&nbsp;
				    <%
				        String url = "" + request.getAttribute("currentURL") + "?";
				        if (request.getQueryString() != null){
				            url += request.getQueryString();
				        }
				        url += "&print=on";
				        pageContext.setAttribute("url", url);
				    %>
            		<img src="../img/printer.gif" align="absmiddle"/ > <a href="<bean:write name="url" scope="page" filter="true" />">Spausdinti</a>
	            	&nbsp;|&nbsp;
	            	<img src="../img/key.gif" align="absmiddle"/ > <html:link action="changepasswordform">Keisti slaptažodį</html:link>&nbsp;
	            	&nbsp;|&nbsp;
	            	<img src="../img/logoff.gif" align="absmiddle"/ > <html:link action="logout">Atsijungti</html:link>&nbsp;
		        <% } %>
        	    </div>
            </td>
        </tr>
        <tr><td colspan="2"><img src="../img/pix.gif" width="1" height="3" /></td></tr>
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
				<% if(null != session.getAttribute("userLogin")){ %>
            	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="200" valign="top">
                    <!-- BEGIN MENU SECTION -->
               	    <jsp:include page="menu.jsp"/>
                    <!-- END MENU SECTION -->
                    </td>
                    <td width="1" class="line1"><img src="../img/pix.gif" width="1" height="1" /></td>
                    <td valign="top">
                <% } %>
<% } %>
				<% if(null != session.getAttribute("userLogin")){ %>
                    <!-- BEGIN CONTENT SECTION -->
						<%
                    	String pageName = (String)session.getAttribute("CENTER_STATE");

						if( Constants.IN_DECLARATION_CODE_FORM.equals(pageName)||
							Constants.OUT_DECLARATION_CODE_FORM.equals(pageName)||
							Constants.GVNA_DECLARATION_CODE_FORM.equals(pageName)||
							Constants.CHNG_OUT_DECLARATION_CODE_FORM.equals(pageName))
						{
							%><jsp:include page="deklaracijos/DeklaracijaAsmKodas.jsp"/><%
						}
						else
						if( Constants.TEMP_CITIZEN_DECLARATION.equals(pageName))
						{
							%><jsp:include page="deklaracijos/LaikiniAsmenysDeklaracija.jsp"/><%
						}	
						else					
						if( Constants.IN_DECLARATION_FORM.equals(pageName))
						{
							%><jsp:include page="deklaracijos/InDeklaracija.jsp"/><%
						}						
						else					
						if( Constants.IN_DECLARATION_FORM_FILL_MESSAGE.equals(pageName))
						{
							%><jsp:include page="deklaracijos/InDeklaracijaFillMessage.jsp"/><%
						}						
						else					
						if( Constants.OUT_DECLARATION_FORM.equals(pageName))
						{
							%><jsp:include page="deklaracijos/OutDeklaracija.jsp"/><%
						}						
						if( Constants.CHNG_OUT_DECLARATION_FORM.equals(pageName))
						{
							%><jsp:include page="deklaracijos/OutDeklaracija.jsp"/><%
						}						
						else					
						if( Constants.OUT_DECLARATION_FORM_FILL_MESSAGE.equals(pageName) )
						{
							%><jsp:include page="deklaracijos/OutDeklaracijaFillMessage.jsp"/><%
						}					
						else
						if( Constants.CHNG_OUT_DECLARATION_FORM_FILL_MESSAGE.equals(pageName) )
						{
							%><jsp:include page="deklaracijos/OutDeklaracijaFillMessage.jsp"/><%
						}					
						else					
						if( Constants.GVNA_DECLARATION_FORM.equals(pageName))
						{
							%><jsp:include page="deklaracijos/GvnaDeklaracija.jsp"/><%
						}					
						else					
						if( Constants.GVNA_DECLARATION_FORM_FILL_MESSAGE.equals(pageName))
						{
							%><jsp:include page="deklaracijos/GvnaDeklaracijaFillMessage.jsp"/><%
						}						
						else					
						if( Constants.NOT_COMPLETED_DECLARTIONS_LIST.equals(pageName))
						{
							%><jsp:include page="nebaigtosPildytiDeklaracijos/NebaigtosPildytiDeklaracijos.jsp"/><%
						}						
						else					
						if( Constants.REPORT.equals(pageName))
						{
							%><jsp:include page="ataskaitos/report.jsp"/><%
						}
						else
						if (Constants.REPORT13.equals(pageName))
						{
							%><jsp:include page="ataskaitos/report13.jsp"/><%
						}
						else 					
						if( Constants.CHANGE_PASSWORD_FORM.equals(pageName))
						{
							%><jsp:include page="changePasswordForm.jsp"/><%
						}						
						else					
						if( Constants.CHANGE_PASSWORD_DONE.equals(pageName))
						{
							%><jsp:include page="changePasswordDone.jsp"/><%
						}						
						else					
						if( Constants.QUERY_PERSON_FORM.equals(pageName))
						{
							%><jsp:include page="query/queryPersonForm.jsp"/><%
						}						
						else					
						if( Constants.QUERY_PERSON_RESULTS.equals(pageName))
						{
							%><jsp:include page="query/queryPersonResults.jsp"/><%
						}						
						else					
						if( Constants.QUERY_ADDRESS_FORM.equals(pageName))
						{
							%><jsp:include page="query/queryAddressForm.jsp"/><%
						}						
						else					
						if( Constants.QUERY_ADDRESS_RESULTS.equals(pageName))
						{
							%><jsp:include page="query/queryAddressResults.jsp"/><%
						}						
						else					
						if( Constants.QUERY_VIEW_DATA.equals(pageName))
						{
							%><jsp:include page="query/queryViewData.jsp"/><%
						}						
						else					
						if( Constants.QUERY_EDIT_DATA.equals(pageName))
						{
							%><jsp:include page="query/queryEditData.jsp"/><%
						}						
						else					
						if( Constants.QUERY_CREATE_DATA.equals(pageName))
						{
							%><jsp:include page="query/queryCreateData.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_LIST_ISTAIGOS.equals(pageName))
						{
							%><jsp:include page="admin/listIstaigos.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_EDIT_ISTAIGA_FORM.equals(pageName))
						{
							%><jsp:include page="admin/editIstaigaForm.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_LIST_ZINYNAI.equals(pageName))
						{
							%><jsp:include page="admin/listZinynai.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_LIST_ZINYNO_REIKSMES.equals(pageName))
						{
							%><jsp:include page="admin/listZinynoReiksmes.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_CREATE_ZINYNO_REIKSME_FORM.equals(pageName))
						{
							%><jsp:include page="admin/createZinynoReiksmeForm.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_EDIT_ZINYNO_REIKSME_FORM.equals(pageName))
						{
							%><jsp:include page="admin/editZinynoReiksmeForm.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_GVDIS_APRASYMAS.equals(pageName))
						{
							%><jsp:include page="admin/gvdisAprasymasForm.jsp"/><%
						}						
						else
						if( Constants.ADMIN_GVDIS_APRASYMAS_JN.equals(pageName))
						{
							%><jsp:include page="admin/gvdisAprasymasJn.jsp"/><%
						}						
						else
						if( Constants.ADMIN_GVDIS_APRASYMAS_EDIT_DB.equals(pageName))
						{
							%><jsp:include page="admin/editGvdisAprDbObjektaiForm.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_GVDIS_APRASYMAS_EDIT.equals(pageName))
						{
							%><jsp:include page="admin/editGvdisAprasymasForm.jsp"/><%
						}						
						else					
						if( Constants.ADMIN_GVDIS_APRASYMAS_CREATE.equals(pageName))
						{
							%><jsp:include page="admin/createGvdisAprasymasForm.jsp"/><%
						}						
						else					
						if( Constants.SPR_LIST_SPRENDIMAI.equals(pageName))
						{
							%><jsp:include page="sprendimai/sprendimai.jsp"/><%
						}						
						else					
						if( Constants.SPR_CREATE_SPRENDIMAS_FORM.equals(pageName))
						{
							%><jsp:include page="sprendimai/createSprendimasForm.jsp"/><%
						}						
						else					
						if( Constants.SPR_EDIT_SPRENDIMAS_FORM.equals(pageName))
						{
							%><jsp:include page="sprendimai/editSprendimasForm.jsp"/><%
						}						
						else					
						if( Constants.SPR_LIST_PRASYMAI.equals(pageName))
						{
							%><jsp:include page="sprendimai/prasymai.jsp"/><%
						}						
						else					
						if( Constants.SPR_CREATE_PRASYMAS_FORM.equals(pageName))
						{
							%><jsp:include page="sprendimai/createPrasymasForm.jsp"/><%
						}						
						else					
						if( Constants.SPR_EDIT_PRASYMAS_FORM.equals(pageName))
						{
							%><jsp:include page="sprendimai/editPrasymasForm.jsp"/><%
						}						
						else					
						if( Constants.SPR_VIEW_PRASYMAS.equals(pageName))
						{
							%><jsp:include page="sprendimai/viewPrasymas.jsp"/><%
						}						
						else					
						if( Constants.SPR_VIEW_SPRENDIMAS.equals(pageName))
						{
							%><jsp:include page="sprendimai/viewSprendimas.jsp"/><%
						}						
						else					
						if( Constants.JOURNAL.equals(pageName))
						{
							%><jsp:include page="journal/zurnalai.jsp"/><%
						}	
						else					
						if( Constants.REPORT11.equals(pageName))
						{
							%><jsp:include page="ataskaitos/report11.jsp"/><%
						}						
						else					
						if( Constants.TEMP_CITIZENS.equals(pageName))
						{
							%><jsp:include page="tempCitizens/TempCitizens.jsp"/><%
						}
						else					
						if( Constants.TEMP_CITIZEN.equals(pageName))
						{
							%><jsp:include page="tempCitizens/TempCitizen.jsp"/><%
						}
						else					
						if( Constants.PAZ_GVNA_FORM.equals(pageName))
						{
							%><jsp:include page="pazymos/pazGvnaForm.jsp"/><%
						}	
						else					
						if( Constants.PAZ_GVNA.equals(pageName))
						{
							%><jsp:include page="pazymos/pazGvna.jsp"/><%
						}
						else					
						if( Constants.PAZ_GV_FORM.equals(pageName))
						{
							%><jsp:include page="pazymos/pazGvForm.jsp"/><%
						}	
						else					
						if( Constants.PAZ_GV.equals(pageName))
						{
							%><jsp:include page="pazymos/pazGv.jsp"/><%
						}
						else					
						if( Constants.PAZ_MIRE_FORM.equals(pageName))
						{
							%><jsp:include page="pazymos/pazMireForm.jsp"/><%
						}
						else					
						if( Constants.PAZ_MIRE.equals(pageName))
						{
							%><jsp:include page="pazymos/pazMire.jsp"/><%
						}						
						else					
						if( Constants.PAZ_ISVYKE_FORM.equals(pageName))
						{
							%><jsp:include page="pazymos/pazIsvykeForm.jsp"/><%
						}
						else					
						if( Constants.PAZ_ISVYKE.equals(pageName))
						{
							%><jsp:include page="pazymos/pazIsvyke.jsp"/><%
						}						
						else					
						if( Constants.PAZ_SAV_FORM.equals(pageName))
						{
							%><jsp:include page="pazymos/pazSavForm.jsp"/><%
						}	
						else					
						if( Constants.PAZ_SAV.equals(pageName))
						{
							%><jsp:include page="pazymos/pazSav.jsp"/><%
						}
						else					
						if( Constants.HAVE_NOT_COMPLETED_DECLARATION.equals(pageName))
						{
							%><jsp:include page="deklaracijos/haveNotCompletedDeclaration.jsp"/><%
						}
						else					
						if( Constants.IN_DECLARATION_VIEW.equals(pageName))
						{
							%><jsp:include page="deklaracijos/inDeclarationView.jsp"/><%
						}
						else					
						if( Constants.OUT_DECLARATION_VIEW.equals(pageName))
						{
							%><jsp:include page="deklaracijos/outDeclarationView.jsp"/><%
						}
						else					
						if( Constants.GVNA_DECLARATION_VIEW.equals(pageName))
						{
							%><jsp:include page="deklaracijos/gvnaDeclarationView.jsp"/><%
						}
						else					
						if( Constants.LINK_WITH_GR.equals(pageName))
						{
							%><jsp:include page="tempCitizens/LinkWithGR.jsp"/><%
						}
						else					
						if( Constants.LINK_WITH_GR_PERFORM.equals(pageName))
						{
							%><jsp:include page="tempCitizens/LinkWithGRPerform.jsp"/><%
						}
						else					
						if( Constants.EXCEPTION.equals(pageName))
						{
							%><jsp:include page="exception.jsp"/><%
						}
						else					
						if( Constants.CANNOT_DELETE_DECLARATION.equals(pageName))
						{
							%><jsp:include page="nebaigtosPildytiDeklaracijos/cannotDeleteDeclaration.jsp"/><%
						}
						else					
						if( Constants.CANNOT_DELETE_TEMP_CITIZEN.equals(pageName))
						{
							%><jsp:include page="nebaigtosPildytiDeklaracijos/cannotDeleteTempCitizen.jsp"/><%
						}
						else					
						if( Constants.CANNOT_EDIT_DECLARATION.equals(pageName))
						{
							%><jsp:include page="deklaracijos/cannotEditDeclaration.jsp"/><%
						}
						else					
						if( Constants.COMPARE_PERSON.equals(pageName))
						{
							%><jsp:include page="tempCitizens/comparePerson.jsp"/><%
						}
						else					
						if( Constants.TEST_MODE.equals(pageName))
						{
							%><jsp:include page="deklaracijos/test.jsp"/><%
						}
						else					
						if( Constants.TEST_IN_DECLARATION_VIEW.equals(pageName))
						{
							%><jsp:include page="deklaracijos/testInDeclaration.jsp"/><%
						}
						else					
						if( Constants.TEST_ENCODING.equals(pageName))
						{
							%><jsp:include page="deklaracijos/testEncoding.jsp"/><%
						}
						else					
						if( Constants.DEKLARACIJA_INTERNETU.equals(pageName))
						{
							%><jsp:include page="deklaracijos/internetu/deklaracijaInternetu.jsp"/><%
						}
						else
						if( Constants.CONFIRM_DEKLARACIJA.equals(pageName))
						{
							%>
							<jsp:include page="deklaracijos/internetu/deklaracijaInternetuConfirm.jsp"/><%
						}
						else if( Constants.REJECT_DEKLARACIJA.equals(pageName))
						{
							%>
							<jsp:include page="deklaracijos/internetu/deklaracijaInternetuReject.jsp"/><%
						}
						String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
						if (state.equals(Constants.EXCEPTION)) {
							request.getSession().setAttribute(Constants.CENTER_STATE, String.valueOf(session.getAttribute(Constants.CENTER_STATE_OLD)));		
						} else {
							request.getSession().setAttribute(Constants.CENTER_STATE_OLD, String.valueOf(session.getAttribute(Constants.CENTER_STATE)));		
						}
                    %>
                    <!-- END CONTENT SECTION -->
                    &nbsp;
                    </td>
                </tr>
                </table>
		        <% } else { %>
	    		    <jsp:include page="/pages/loginForm.jsp" />
		        <% } %>
        	</td>
        </tr>
        <tr>
        	<td class="darkbg" colspan="2" align="right" style="font-size: 9px;">Aplikacijos versija: <% out.print(Version.VERSION); %></td>
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