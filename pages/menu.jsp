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


<div class="menu1" onclick="flipDiv('menu4')">Gyvenamosios vietos duomenø paieðka</div>
<div id="menu4" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("querypersonform",request)%>"><html:link action="querypersonform">Ieðkoti pagal asmená</html:link></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("queryaddressform",request)%>"><html:link action="queryaddressform">Ieðkoti pagal adresà</html:link></div>
	<% } %>
</div>

<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
<div class="menu1" onclick="flipDiv('menu1')">Gyvenamosios vietos deklaracijos</div>
<div id="menu1" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("AtvDeklaracijaAsmKodas",request)%>"><html:link action="AtvDeklaracijaAsmKodas">Registruoti atvykimo deklaracijà</html:link></div>
	<% } %>
	<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaAsmKodas",request)%>"><html:link action="IsvDeklaracijaAsmKodas">Registruoti iðvykimo deklaracijà</html:link></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("gvpazymaform",request)%>"><a href="../gvpazymaform.do">Paþyma apie deklaruotà gyvenamàjà vietà</a></div>
	<div class="<%=MenuUtil.appropriateClass("savpazymaform",request)%>"><a href="../savpazymaform.do">Paþyma patalpø savininkams</a></div>
	<div class="<%=MenuUtil.appropriateClass("mirepazymaform",request)%>"><a href="../mirepazymaform.do">Paþyma apie mirusio asmens paskutinæ deklaruotà gyvenamàjà vietà</a></div>
	<div class="<%=MenuUtil.appropriateClass("isvykepazymaform",request)%>"><a href="../isvykepazymaform.do">Paþyma apie iðvykusio á uþsiená asmens deklaruotus duomenis</a></div> 
	<% } %>
</div>	
<% } %>
<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
<div class="menu1" onclick="flipDiv('menu2')">Gyvenamosios vietos nedeklaravusiø asmenø apskaita</div>
<div id="menu2" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("GvnaDeklaracijaAsmKodas",request)%>"><html:link action="GvnaDeklaracijaAsmKodas">Registruoti praðymà átraukti á GVNA apskaità</html:link></div>
	<div class="<%=MenuUtil.appropriateClass("gvnapazymaform",request)%>"><a href="../gvnapazymaform.do">Paþyma apie átraukimà á GVNA apskaità</a></div>
</div>
<% } %>
<div class="menu1" onclick="flipDiv('menu3')">Ataskaitos</div>
<div id="menu3" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("report01",request)%>"><a href="../report.do?reportType=01">Gyvenamàjà vietà deklaravæ gyventojai</a></div>
	<% } %>
	<div class="<%=MenuUtil.appropriateClass("report02",request)%>"><a href="../report.do?reportType=02">Iðvykimà ið LR deklaravæ gyventojai</a></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("report03",request)%>"><a href="../report.do?reportType=03">Asmenys, kuriø pateiktos iðvykimo deklaracijos galioja</a></div>
	<% } %>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("report05",request)%>"><a href="../report.do?reportType=05">Iðduota paþymø apie asmens gyvenamàjà vietà</a></div>
	<div class="<%=MenuUtil.appropriateClass("report07",request)%>"><a href="../report.do?reportType=07">Priimta sprendimø dël deklaravimo duomenø keitimo, taisymo ir naikinimo</a></div>
	<div class="<%=MenuUtil.appropriateClass("report08",request)%>"><a href="../report.do?reportType=08">Átraukti á GVNA apskaità gyventojai</a></div>
	<div class="<%=MenuUtil.appropriateClass("report10",request)%>"><a href="../report.do?reportType=10">Iðduota paþymø apie átraukimà á GVNA apskaità</a></div>
	<div class="<%=MenuUtil.appropriateClass("report12",request)%>"><a href="../report.do?reportType=12">Gyventojø skaièius seniûnijos (savivaldybës) teritorijoje</a></div>
	<% } %>
	<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
	<div class="<%=MenuUtil.appropriateClass("report11",request)%>"><a href="../report11.do">Asmenys, kuriø deklaravimo duomenys turi bûti naikinami</a></div>
	<div class="<%=MenuUtil.appropriateClass("report13",request)%>"><a href="../report13.do">Gyventojø skaièius teritorijoje</a></div>
	<% } %>
</div>
<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT") || roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")){ %>
<div class="menu1" onclick="flipDiv('menu5')">Deklaravimo duomenø keitimas</div>
<div id="menu5" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
		<div class="<%=MenuUtil.appropriateClass("prasymai",request)%>"><a href="../prasymai.do">Perþiûrëti praðymus dël duomenø keitimo</a></div>
		<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
			<div class="<%=MenuUtil.appropriateClass("createprasymasform",request)%>"><a href="../createprasymasform.do">Registruoti praðymà dël duomenø keitimo</a></div>
		<% } %>
		<div class="<%=MenuUtil.appropriateClass("sprendimai",request)%>"><a href="../sprendimai.do">Perþiûrëti sprendimus dël duomenø keitimo</a></div>
		<% if (roles.contains("RL_GVDIS_GL_TVARK")){ %>
			<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaAsmKodas",request)%>"><a href="../IsvDeklaracijaAsmKodas.do?outType=1">Gyvenamosios vietos keitimas uþsienyje</a></div>
		<% } %>
	<% } else if (roles.contains("RL_GVDIS_UZ_REIK_MINIST_TVARK")) { %>
		<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaAsmKodas",request)%>"><a href="../IsvDeklaracijaAsmKodas.do?outType=1">Gyvenamosios vietos keitimas uþsienyje</a></div>
	<% } %>
</div>
<% } %>	
<div class="menu1" onclick="flipDiv('menu6')">Þurnalai</div>
<div id="menu6" style="display: none;">
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("journal0",request)%>"><a href="../journal.do?journalType=0">Atvykimo deklaracijø þurnalas</a></div>
	<% } %>	
	<div class="<%=MenuUtil.appropriateClass("journal1",request)%>"><a href="../journal.do?journalType=1">Iðvykimo deklaracijø þurnalas</a></div>
	<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
	<div class="<%=MenuUtil.appropriateClass("journal2",request)%>"><a href="../journal.do?journalType=2">Paþymø apie deklaruotà GV þurnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal3",request)%>"><a href="../journal.do?journalType=3">Sprendimø dël deklaravimo duomenø keitimo þurnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal4",request)%>"><a href="../journal.do?journalType=4">Praðymø átraukti á GVNA apskaità þurnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal5",request)%>"><a href="../journal.do?journalType=5">Paþymø apie átraukimà á GVNA apskaità þurnalas</a></div>
	<div class="<%=MenuUtil.appropriateClass("journal6",request)%>"><a href="../journal.do?journalType=6">Paþymø iðduotø gyvenamosios patalpos savininkams þurnalas</a></div>
	<% } %>	
</div>

<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK") || roles.contains("RL_GVDIS_GL_SKAIT") || roles.contains("RL_GVDIS_SS_SKAIT")){ %>
<div class="menu1" onclick="flipDiv('menu7')">Nebaigtos ávesti deklaracijos</div>
<div id="menu7" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("NebaigtosAtvDeklaracijos",request)%>"><a href="../NotCompletedDeclarationsList.do?deklTipas=atv">Atvykimo deklaracijos</a></div>	
	<div class="<%=MenuUtil.appropriateClass("NebaigtosIsvDeklaracijos",request)%>"><a href="../NotCompletedDeclarationsList.do?deklTipas=isv">Iðvykimo deklaracijos</a></div>
</div>

<div class="<%=MenuUtil.headerAppropriateClass("TempCitizensList",request)%>" onclick="flipDiv('menu8')"><html:link action="TempCitizensList">Asmenys, neregistruoti gyventojø registre</html:link></div>
<div id="menu8" style="display: none;">
</div>
<% } %>	
<% if (roles.contains("RL_GVDIS_ADMIN")){ %>
<div class="menu1" onclick="flipDiv('menu9')">Sistemos administravimas</div>
<div id="menu9" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("istaigos",request)%>"><a href="../istaigos.do">Savivaldybës ir seniûnijos</a></div>
	<div class="<%=MenuUtil.appropriateClass("zinynai",request)%>"><a href="../zinynai.do">Sistemos þinynai</a></div>
	<div class="<%=MenuUtil.appropriateClass("gvdisaprasymasform",request)%>"><a href="../gvdisaprasymasform.do">GVDIS apraðymas</a></div>
</div>
<% } %>

<!--|| roles.contains("RL_GVDIS_SS_TVARK")-->
<% if (roles.contains("RL_GVDIS_GL_TVARK") ){ %>
<div class="menu1" onclick="flipDiv('menu10')">Deklaracijos pateiktos internetu</div>
<div id="menu10" style="display: none;">
	<div class="<%=MenuUtil.appropriateClass("AtvDeklaracijaInternetuAction",request)%>"><a href="../DeklaracijaInternetu.do?deklTipas=atv">Atvykimo deklaracijos</a></div>	
	<div class="<%=MenuUtil.appropriateClass("IsvDeklaracijaInternetuAction",request)%>"><a href="../DeklaracijaInternetu.do?deklTipas=isv">Iðvykimo deklaracijos</a></div>
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
