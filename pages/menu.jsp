<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,com.algoritmusistemos.gvdis.web.utils.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); %>

<script language="Javascript">
<!--
	function flipDiv(id)
	{
		for (i=1; i<=10; i++){
			var hideDiv = document.getElementById('menu' + i);
   	        if (hideDiv){
   	        	hideDiv.style.display = "none";
   	        }
		}
		var theDiv = document.getElementById(id);
		if (theDiv){
			setCookie('expMenu', id);
	        if (theDiv.style.display == "block"){
    	        theDiv.style.display = "none";
			}	
			else {
    	        theDiv.style.display = "block";
			}
		}
	}
//-->	
</script> 
<div style="border-bottom: 1px solid #D3D3D3;"></div>


<div class="menu1" onclick="flipDiv('menu4')">Gyvenamosios vietos duomen� paie�ka</div>
<div id="menu4" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("querypersonform",request)%>"><html:link action="querypersonform">Ie�koti pagal asmen�</html:link></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("queryaddressform",request)%>"><html:link action="queryaddressform">Ie�koti pagal adres�</html:link></div>
	<% } %>
</div>

<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
<div class="menu1" onclick="flipDiv('menu1')">Gyvenamosios vietos deklaracijos</div>
<div id="menu1" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("AtvDeklaracijaAsmKodas",request)%>"><html:link action="AtvDeklaracijaAsmKodas">Registruoti atvykimo deklaracij�</html:link></div>
	<% } %>
	<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaAsmKodas",request)%>"><html:link action="IsvDeklaracijaAsmKodas">Registruoti i�vykimo deklaracij�</html:link></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("gvpazymaform",request)%>"><a href="../gvpazymaform.do">Pa�yma apie deklaruot� gyvenam�j� viet�</a></div>
	<div class="<%=MenuUtil.appropriateClass("savpazymaform",request)%>"><a href="../savpazymaform.do">Pa�yma patalp� savininkams</a></div>
	<div class="<%=MenuUtil.appropriateClass("mirepazymaform",request)%>"><a href="../mirepazymaform.do">Pa�yma apie mirusio asmens paskutin� deklaruot� gyvenam�j� viet�</a></div>
	<div class="<%=MenuUtil.appropriateClass("isvykepazymaform",request)%>"><a href="../isvykepazymaform.do">Pa�yma apie i�vykusio � u�sien� asmens deklaruotus duomenis</a></div> 
	<% } %>
</div>	
<% } %>
<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
<div class="menu1" onclick="flipDiv('menu2')">Gyvenamosios vietos nedeklaravusi� asmen� apskaita</div>
<div id="menu2" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("GvnaDeklaracijaAsmKodas",request)%>"><html:link action="GvnaDeklaracijaAsmKodas">Registruoti pra�ym� �traukti � GVNA apskait�</html:link></div>
	<div class="<%=MenuUtil.appropriateClass("gvnapazymaform",request)%>"><a href="../gvnapazymaform.do">Pa�yma apie �traukim� � GVNA apskait�</a></div>
</div>
<% } %>
<div class="menu1" onclick="flipDiv('menu3')">Ataskaitos</div>
<div id="menu3" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("report01",request)%>"><a href="../report.do?reportType=01">Gyvenam�j� viet� deklarav� gyventojai</a></div>
	<% } %>
	<div class="<%=MenuUtil.appropriateClass("report02",request)%>"><a href="../report.do?reportType=02">I�vykim� i� LR deklarav� gyventojai</a></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("report03",request)%>"><a href="../report.do?reportType=03">Asmenys, kuri� pateiktos i�vykimo deklaracijos galioja</a></div>
	<% } %>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("report05",request)%>"><a href="../report.do?reportType=05">I�duota pa�ym� apie asmens gyvenam�j� viet�</a></div>
	<div class="<%=MenuUtil.appropriateClass("report07",request)%>"><a href="../report.do?reportType=07">Priimta sprendim� d�l deklaravimo duomen� keitimo, taisymo ir naikinimo</a></div>
	<div class="<%=MenuUtil.appropriateClass("report08",request)%>"><a href="../report.do?reportType=08">�traukti � GVNA apskait� gyventojai</a></div>
	<div class="<%=MenuUtil.appropriateClass("report10",request)%>"><a href="../report.do?reportType=10">I�duota pa�ym� apie �traukim� � GVNA apskait�</a></div>
	<div class="<%=MenuUtil.appropriateClass("report12",request)%>"><a href="../report.do?reportType=12">Gyventoj� skai�ius seni�nijos (savivaldyb�s) teritorijoje</a></div>
	<% } %>
	<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("report11",request)%>"><a href="../report11.do">Asmenys, kuri� deklaravimo duomenys turi b�ti naikinami</a></div>
	<div class="<%=MenuUtil.appropriateClass("report13",request)%>"><a href="../report13.do">Gyventoj� skai�ius teritorijoje</a></div>
	<% } %>
</div>
<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
<div class="menu1" onclick="flipDiv('menu5')">Deklaravimo duomen� keitimas</div>
<div id="menu5" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
		<div class="<%=MenuUtil.appropriateClass("prasymai",request)%>"><a href="../prasymai.do">Per�i�r�ti pra�ymus d�l duomen� keitimo</a></div>
		<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
			<div class="<%=MenuUtil.appropriateClass("createprasymasform",request)%>"><a href="../createprasymasform.do">Registruoti pra�ym� d�l duomen� keitimo</a></div>
		<% } %>
		<div class="<%=MenuUtil.appropriateClass("sprendimai",request)%>"><a href="../sprendimai.do">Per�i�r�ti sprendimus d�l duomen� keitimo</a></div>
		<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
			<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaAsmKodas",request)%>"><a href="../IsvDeklaracijaAsmKodas.do?outType=1">Gyvenamosios vietos keitimas u�sienyje</a></div>
		<% } %>
	<% } else if (roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")) { %>
		<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaAsmKodas",request)%>"><a href="../IsvDeklaracijaAsmKodas.do?outType=1">Gyvenamosios vietos keitimas u�sienyje</a></div>
	<% } %>
</div>
<% } %>	
<div class="menu1" onclick="flipDiv('menu6')">�urnalai</div>
<div id="menu6" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("journal0",request)%>"><a href="../journal.do?journalType=0">Atvykimo deklaracij� �urnalas</a></div>
	<% } %>	
	<div class="<%=MenuUtil.appropriateClass("journal1",request)%>"><a href="../journal.do?journalType=1">I�vykimo deklaracij� �urnalas</a></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("journal2",request)%>"><a href="../journal.do?journalType=2">Pa�ym� apie deklaruot� GV �urnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal3",request)%>"><a href="../journal.do?journalType=3">Sprendim� d�l deklaravimo duomen� keitimo �urnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal4",request)%>"><a href="../journal.do?journalType=4">Pra�ym� �traukti � GVNA apskait� �urnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal5",request)%>"><a href="../journal.do?journalType=5">Pa�ym� apie �traukim� � GVNA apskait� �urnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal6",request)%>"><a href="../journal.do?journalType=6">Pa�ym� i�duot� gyvenamosios patalpos savininkams �urnalas</a></div>
	<% } %>	
</div>

<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
<div class="menu1" onclick="flipDiv('menu7')">Nebaigtos �vesti deklaracijos</div>
<div id="menu7" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("NebaigtosAtvDeklaracijos",request)%>"><a href="../NotCompletedDeclarationsList.do?deklTipas=atv">Atvykimo deklaracijos</a></div>	
	<div class="<%=MenuUtil.appropriateClass("NebaigtosIsvDeklaracijos",request)%>"><a href="../NotCompletedDeclarationsList.do?deklTipas=isv">I�vykimo deklaracijos</a></div>
</div>

<div class="<%=MenuUtil.headerAppropriateClass("TempCitizensList",request)%>" onclick="flipDiv('menu8')"><html:link action="TempCitizensList">Asmenys, neregistruoti gyventoj� registre</html:link></div>
<div id="menu8" style="display: none;">
</div>
<% } %>	
<% if (roles.contains("RL_GVDIS_ADMIN")){ %>
<div class="menu1" onclick="flipDiv('menu9')">Sistemos administravimas</div>
<div id="menu9" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("istaigos",request)%>"><a href="../istaigos.do">Savivaldyb�s ir seni�nijos</a></div>
	<div class="<%=MenuUtil.appropriateClass("zinynai",request)%>"><a href="../zinynai.do">Sistemos �inynai</a></div>
	<div class="<%=MenuUtil.appropriateClass("gvdisaprasymasform",request)%>"><a href="../gvdisaprasymasform.do">GVDIS apra�ymas</a></div>
</div>
<% } %>

<!--|| roles.contains("RL_GVDIS_SS_TVARK")-->
<% if (roles.contains("RL_GVDIS_GL_TVARK") ){ %>
<div class="menu1" onclick="flipDiv('menu10')">Deklaracijos pateiktos internetu</div>
<div id="menu10" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("AtvDeklaracijaInternetuAction",request)%>"><a href="../DeklaracijaInternetu.do?deklTipas=atv">Atvykimo deklaracijos</a></div>	
	<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaInternetuAction",request)%>"><a href="../DeklaracijaInternetu.do?deklTipas=isv">I�vykimo deklaracijos</a></div>
</div>
<% } %>

<script language="Javascript">
<!--
	var i=0;
	var expMenu = getCookie('expMenu');
	for (i=1; i<=10; i++){
		if (expMenu == 'menu' + i){
			flipDiv('menu' + i);
		}
	}
//-->	
</script> 
