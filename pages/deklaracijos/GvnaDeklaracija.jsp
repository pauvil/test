<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.forms.*,
				 com.algoritmusistemos.gvdis.web.delegators.*" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<style type="text/css">
td.gvdis {
	border: 1px solid black;
}
td.gvdis_sp {
	border-left: 4px solid black;
}
</style>
<html:form action="GvnaDeklaracijaPerform">
<div class="heading" align="center">PRA�YMAS �TRAUKTI � GYVENAMOSIOS VIETOS NEDEKLARAVUSI� ASMEN� APSKAIT� </div>
<SCRIPT LANGUAGE="JavaScript">
<!--
    function changeMode(mode)
    {
    	document.GvnaDeklaracijaForm.mode.value = mode;
    }   
    
    function switchGaliojimoData(kodas)
    {
    	if ('LL' == kodas || 'LL_EB' == kodas || 'LL_ES' == kodas){
       		document.getElementById("galiojimo_data").style.display = 'block';
    	}
    	else {
       		document.getElementById("galiojimo_data").style.display = 'none';
    	}
    }  
    function controlGyvVieta(value)
    {
  
 		var atvykoIsUzsienioSalis = document.GvnaDeklaracijaForm.atvykoIsUzsienioSalis;
		var atvykoIsUzsienioTextarea = document.GvnaDeklaracijaForm.atvykoIsUzsienioTextarea;
		var kitaGyvenamojiVietaAprasymas = document.GvnaDeklaracijaForm.kitaGyvenamojiVietaAprasymas;
    
    	if(0 == value)
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = true;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = true;
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = true;
    		
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.value = "";
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.value = "";
    	}
    	else
    	if(1 == value)
    	{
     		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = false;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = false;
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = true;   	

    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.value = "";
    	}
    	else
    	if(2 == value)
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = true;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = true;
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = false;
    		
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.value = "";
    	}
    	else
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = true;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = true;
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = false;
    	} 
    }
    var pdfwin;
    function loadf()
    {
    
    
         	<%
	    	if(!"".equals(request.getAttribute("ankstesneVietaTipas")))
	    	{
	    	%>
	    		controlGyvVieta(<%=request.getAttribute("ankstesneVietaTipas")%>);
	    		<%
	    	}
    	%>  
    
    
    	<%
		if(null != session.getAttribute("print"))
		{
		%>
			var path = "generatePDF.do";
			if (!document.all)path = '../' + path;
			pdfwin = window.open(path,"PDF");
		<%
		}
		%> 
    }
    window.onload = loadf; 
       
    function flipDokumentuLaukai(objektas){    	
    	if(objektas.value == -1){    		
    		document.getElementById("asmDoc1").style.display = 'inline';
    		document.getElementById("asmDoc2").style.display = 'inline';
    	<logic:notEmpty name="asmensDokumentai">	document.getElementById("asmDoc3").style.display = 'inline'; 
    	document.getElementById("asmDoc4").style.display = 'none';</logic:notEmpty>
    	}
    	else{
    		document.getElementById("asmDoc1").style.display = 'none';
    		document.getElementById("asmDoc2").style.display = 'none';
    	<logic:notEmpty name="asmensDokumentai">	document.getElementById("asmDoc3").style.display = 'none';
    	document.getElementById("asmDoc4").style.display = 'inline';</logic:notEmpty>
    	}
    }
      
    function getDocPilietybe(id) {
       	<logic:present name="asmensDokumentai">
        var pilietybes = new Object();
	    <logic:iterate name="asmensDokumentai" id="doc">
	    	<logic:present name="doc" property="valstybe" >
	        	<logic:present name="doc" property="valstybe.pilietybe" >
					pilietybes['<bean:write name="doc" property="dokNr" />'] = '<bean:write name="doc" property="valstybe.pilietybe" />';
				</logic:present>
	        </logic:present>
	        <logic:notPresent name="doc" property="valstybe" >
	    		pilietybes['<bean:write name="doc" property="dokNr" />'] = 'LIETUVOS'; // LT pilietybes kazkodel DB nezymimos
	    	</logic:notPresent>
	    </logic:iterate>
    	var retVal = pilietybes[id];
        //alert(retVal);
    	if (retVal != null)
    		document.getElementById("pilietybePavadinimas").value = retVal;

    	</logic:present>
		return;
    }
//  -->
</script>

<%
	boolean internetu = false;
	
	Long idForEdit = (Long)session.getAttribute("idForEdit");
	if (idForEdit != null) {
		try {
			Deklaracija d = DeklaracijosDelegator.getInstance(request).getDeklaracija(idForEdit, request);		
			if (d.getSaltinis() != null){
				internetu = d.getSaltinis().longValue() == 1;				
			}

		}
		catch(Exception e){}
	}	
%>
<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">


	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20%" class="gvdis">
				1. Asmens kodas<br><html:text property="asmensKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" readonly="true" />
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				2. Gimimo data
				<div style="white-space: nowrap;">
				<html:text property="gimimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly="true" /> -
				<html:text property="gimimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true" /> -
				<html:text property="gimimoData" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true" />				
				</div>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				3. Lytis<br><html:radio property="lytis" disabled="true" value="V" />&nbsp; Vyr.&nbsp;&nbsp;&nbsp;<html:radio property="lytis" disabled="true" value="M" />&nbsp; Mot.
			</td>
			<td rowspan="3" width="20%" align="center" valign="middle" class="gvdis">
				PASTABA. Jeigu vardai<br>ar pavard� sudaro<br>daugiau negu 31 �enkl�,<br>reikia �ra�yti vien� vard�<br>
				ar vien� dvigubos<br>pavard�s dal�.
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				4. Vardas(-ai)<br><html:text property="vardas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				5. Pavard�<br><html:text property="pavarde" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
		</tr>
		<tr>
			<td colspan="6" class="gvdis">
				<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td style="border-top: none; border-bottom: none;" rowspan="2" width="10%" class="gvdis">6. Kontaktiniai duomenys</td>
						<td width="10%" class="gvdis" align="center">Telefono nr.</td>
						<td width="80%" ><html:text property="telefonas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly='<%= internetu %>'/></td>
					</tr>
				   <tr>
				  	<td class="gvdis" align="center">El. pa�to adresas:</td>
				  	<td colspan="2"><html:text property="email" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly='<%= internetu %>'/></td>
				  </tr>
				</table>
			</td>
		</tr>
		<logic:empty name="asmensDokumentai">
		<tr>
			<td  width="20%" class="gvdis" colspan="6">
				7. Pilietyb�	
				<html:select property="pilietybe" styleClass="input" style="width: 150px;">  
				    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe" /> <logic:present name="error.missingPilietybe"><span class="error">Nepasirinkta pilietyb�</span></logic:present>
				</html:select>
			<logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietyb�</span></logic:present>
			</td>
		</tr> 
		</logic:empty>
	<%--	<logic:notEmpty name="asmensDokumentai">
		<tr>
			<td colspan="5" class="gvdis">
				7. Pilietyb�				
				<html:text property="pilietybePavadinimas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
			<td colspan="3" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr> 
		</logic:notEmpty> --%>
		<logic:notEmpty name="asmensDokumentai">
		<tr id="asmDoc4">
			<td colspan="5" class="gvdis">
				7. Pilietyb�				
				<html:text property="pilietybePavadinimas" styleId="pilietybePavadinimas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
			<td colspan="3" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>
		<tr id="asmDoc3" style="display: none;">
			<td colspan="4" width="70%" class="gvdis">
				7. Pilietyb�	
				<html:select property="pilietybe" styleClass="input">  
				    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe" /> <logic:present name="error.missingPilietybe"><span class="error">Nenurodyta pilietyb�</span></logic:present>
				</html:select>
			<logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietyb�</span></logic:present>
			</td>
			<td colspan="4" width="30%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>  
		</logic:notEmpty> 
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
			<%-- <tr>
				<td colspan="5" width="80%" class="gvdis">
					8. Asmens dokumentas <span class="error">*</span>
					<logic:present name="error.dokumentoTipas"><span class="error">Nepasirinktas asmens dokumento tipas</span></logic:present>
					<logic:notEmpty name="asmensDokumentai">
						<br />
						<html:select property="documentId" styleClass="input" style="width: 720px;" disabled='<%= internetu %>'>
							<html:options collection="asmensDokumentai" property="dokNr" labelProperty="title"/>
						</html:select>
						<br /><br />
					</logic:notEmpty>
					<% if (internetu) {%>
						<html:hidden property="documentId"/>
					<% } %>
					</div>
				</td>
			</tr> --%>
			<tr>
			<td width="80%" colspan="5" align="left" class="gvdis">
				8. Asmens dokumentas <span class="error">*</span> <logic:present name="error.dokumentoTipas"><span class="error">Nepasirinktas asmens dokumento tipas</span></logic:present>
					<logic:present name="error.negaliojantisDokumentas"><span class="error">Negaliojantis dokumentas</span></logic:present>
					<logic:present name="error.deklaracijaNepateiktasAsmensDokumentas"><span class="error">Asmenys nuo 16 m. privalo pateikti asmens tapatyb�s kortel� arba pas�</span></logic:present>
					<logic:notEmpty name="asmensDokumentai">
						<br />
						<%-- <div id="asmDoc" style="display:inline;">	--%>										
							<html:select property="documentId" styleClass="input" style="width: 720px; " onchange="flipDokumentuLaukai(this);getDocPilietybe(this[selectedIndex].value);"   disabled='<%= internetu %>'> 								
								<html:options collection="asmensDokumentai" property="dokNr" labelProperty="title" />
								<html:option value="-1">&lt �vedamas u�pildant asmens dokumento laukus &gt</html:option>									
								<%-- <html:option value="0" >&nbsp;</html:option> //ju.k 2007.06.29 --%>
							 </html:select> 
							 <% if (internetu) {%>
								<html:hidden property="documentId"/>
							<% } %>
							<script type="text/javascript">
								getDocPilietybe(document.getElementById('documentId')[0].value);
							</script>
						<br /><br />
					</logic:notEmpty>
					<logic:empty name="asmensDokumentai">
						<br />											
							<html:select property="documentId" styleClass="input" disabled="true" style="width: 720px; "> 
								<html:option value="-1">&lt �vedamas u�pildant asmens dokumento laukus &gt</html:option>								
							</html:select>								
						<br /><br />
					</logic:empty>
			</td>
			</tr>

		
	  	<logic:notEmpty name="asmensDokumentai">			
			<logic:notEqual name="GvnaDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc1" style="display: none;">
			</logic:notEqual>
			<logic:equal name="GvnaDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc1">
			</logic:equal>
		</logic:notEmpty>
		
	<%--	<logic:empty name="asmensDokumentai"> --%>
			<logic:empty name="asmensDokumentai">	
			<tr id="asmDoc1">
			</logic:empty>		
				<td width="420px"  class="gvdis" width="45%">
				Numeris <logic:present name="error.asmensDokumentoNumeris"><span class="error">Nenurodytas asmens dokumento numeris</span></logic:present> <br>
					<html:text property="asmensDokumentoNumeris" styleClass="input_select"  style="width:95%;" maxlength="240" />&nbsp;
				</td>
				<td width="100" bgcolor="#bbbbbb" class="gvdis" width="2%">
					&nbsp;
				</td>
				<td width="420px" class="gvdis" width="49%">
					I�davimo data <logic:present name="error.asmensDokumentoIsdavimoData"><span class="error">Nenurodyta asmens dokumento i�davimo data</span></logic:present>
					<div style="white-space: nowrap;">
						<html:text property="asmensDokumentoIsdavimoMetai" styleClass="inputFixed" maxlength="4" style="width: 87px;" /> -
						<html:text property="asmensDokumentoIsdavimoMenuo" styleClass="inputFixed" maxlength="2" style="width: 45px;"  /> -
						<html:text property="asmensDokumentoIsdavimoData" styleClass="inputFixed" maxlength="2" style="width: 45px;"  />
					</div>
				</td>
				<td width="100" bgcolor="#bbbbbb" class="gvdis" width="2%">
					&nbsp;
				</td>
			</tr>
		
		<logic:notEmpty name="asmensDokumentai">			
			<logic:notEqual name="GvnaDeklaracijaForm" property="documentId" value="-1">
				<tr id="asmDoc2" style="display: none;">
			</logic:notEqual>
			<logic:equal name="GvnaDeklaracijaForm" property="documentId" value="-1">
				<tr id="asmDoc2">
			</logic:equal>
		</logic:notEmpty>
		<logic:empty name="asmensDokumentai">	
		<tr id="asmDoc2">
		</logic:empty>	
				<td width="420px"  class="gvdis">
					Kas i�dav� <logic:present name="error.asmensDokumentoIsdave"><span class="error">Nenurodyta asmens dokument� i�davusi �staiga</span></logic:present>  
					<br />
					<html:text property="asmensDokumentoIsdave" styleClass="input_select" style="width:95%;" maxlength="240" />
				</td>
				<td width="100" bgcolor="#bbbbbb" class="gvdis">
					&nbsp;
				</td>
				<td width="420px" class="gvdis">
					<div id="galiojimo_data">
					 Galiojimo data (jei pateiktas leidimas laikinai gyventi Lietuvos Respublikoje)
					 <div style="white-space: nowrap;">
						<html:text property="leidimoGaliojimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" /> -
						<html:text property="leidimoGaliojimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" /> -
						<html:text property="leidimoGaliojimoData" styleClass="inputFixed" maxlength="2" style="width:45px;" />
					 </div>
					 <logic:present name="error.leidimoGaliojimoData"><span class="error">Nenurodyta galiojimo data</span></logic:present>
					</div>
				</td>
				<td width="100" bgcolor="#bbbbbb" class="gvdis">
					&nbsp;
				</td>
			</tr>
	<%--	</logic:empty> --%>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="40%" class="gvdis" valign="top">
				9. Savivaldyb�, kurioje asmuo gyvena ir kiti �statymo 7 str. 3 d. 6 punkte nurodyti duomenys:
				<logic:present name="error.savivaldybeNotSelected"><span class="error">Nepasirinkta savivaldyb�</span></logic:present><br />
				
				<html:select property="savivaldybe" styleClass="input_select" style="width: 100%;" disabled='<%= internetu %>'>
					<%--
						Jeigu sarase yra daugiau nei 1 savivaldybe
						pasirinkimo saraso pradzioje rodyti ----- (nepasirinkta)
						kitu atveju automatiskai parenkama vienintele savivaldybe
					--%>
					<bean:size id="savivaldybesCount" name="savivaldybes" />
					<logic:greaterThan name="savivaldybesCount" value="1" >
						<option value="-1">-------------</option>
					</logic:greaterThan>
					
					<html:options collection="savivaldybes" property="terNr" labelProperty="terPav" />
				</html:select>
				<logic:present name="error.galiojantisAdresas"><span class="error">�ioje savivaldyb�je asmuo jau yra �trauktas � apskait�</span></logic:present>
				<html:textarea property="savivaldybePastabos" style="width: 100%;height: 100px;" styleClass="input_select" readonly='<%= internetu %>'/>
				<% if (internetu) {%>
						<html:hidden property="savivaldybe"/>
				<% } %>
			</td>
			<td width="60%" valign="top">
				<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr>
					<td class="gvdis">10. Ankstesn�s gyvenamosios vietos adresas ar savivaldyb�, i� kurios atvyko<span class="error">*</span>
						<logic:present name="error.ankstesneGyvenamojiVieta"><span class="error">Nepasirinktas ankstesn�s gyvenamosios vietos adresas</span></logic:present>
					<td>
				</tr>
				<logic:present name="ankstesnisAdresas">
				<tr>
					<td class="gvdis">
						<html:radio property="ankstesneGyvenamojiVieta" value="0" onclick="controlGyvVieta(this.value);" disabled='<%= internetu %>'/>&nbsp;Ankstesn� deklaruota gyvenamoji vieta:<br />
						<b><bean:write name="ankstesnisAdresas" property="adr" /></b><br />
					</td>
				</tr>
				<% if (internetu) {%>
						<html:hidden property="ankstesneGyvenamojiVieta"/>
				<% } %>
				</logic:present>
				<%--<tr>
					<td class="gvdis">
						<html:radio property="ankstesneGyvenamojiVieta" value="1" onclick="controlGyvVieta(this.value);" />&nbsp;Atvyko i� u�sienio:<br />
						<html:select property="atvykoIsUzsienioSalis" styleClass="input_select" style="width: 100%;">
							<html:options collection="salys" property="kodas" labelProperty="pavadinimas" />	
						</html:select>
						<html:textarea property="atvykoIsUzsienioTextarea" style="width: 100%; height: 30px;" styleClass="input_select" />
					</td>
				</tr>--%>
				<logic:notPresent name="ankstesnisAdresas">
				<tr>
					<td class="gvdis">
						<html:radio property="ankstesneGyvenamojiVieta" value="2" onclick="controlGyvVieta(this.value);" disabled='<%= internetu %>'/>&nbsp;Kita gyvenamoji vieta:
						<% if (internetu) {%>
							<html:hidden property="ankstesneGyvenamojiVieta"/>
						<% } %>
						<logic:present name="error.kitaGyvenamojiVietaAprasymas"><span class="error">Nenurodyta ankstesn� gyvenamoji vieta</span></logic:present>
						<br />						
						<html:textarea property="kitaGyvenamojiVietaAprasymas" style="width: 100%; height: 30px;" styleClass="input_select" readonly='<%= internetu %>'/>
					</td>
				</tr>
				</logic:notPresent>
				</table>
				</td>				
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="gvdis">
			11. Gyvenamosios vietos deklaravimo �statymo 6 straipsnio 1 dalies punktas, kuriuo vadovaujantis �traukiama �  GVNA apskait�<br />
			<html:textarea property="priezastys" style="width: 100%;height: 30px;" styleClass="input_select" readonly='<%= internetu %>'/>
		</td>
	</tr>
	<tr>
		<td class="gvdis">
			12. Pra�ymas pateiktas&nbsp;&nbsp;
			<logic:present name="error.deklaracijaPateikta"><span class="error">Nenurodyta, kas pateik� pa�ym�</span></logic:present>
			<logic:present name="error.deklaracijaPateiktaVaiko"><span class="error">Nepilnametis asmuo negali pateikti pra�ymo asmeni�kai</span></logic:present>
			<logic:present name="error.deklaracijaPateiktaTevu"><span class="error">Pilnametis asmuo deklaracij� turi pateikti asmeni�kai</span></logic:present>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:radio property="deklaracijaPateikta"  value="0" disabled='<%= internetu %>'/>&nbsp; Asmeni�kai&nbsp;&nbsp;&nbsp;
			<html:radio property="deklaracijaPateikta"  value="1" disabled='<%= internetu %>'/>&nbsp; Vieno i� t�v�(�t�vi�)&nbsp;&nbsp;&nbsp;
				<% if (internetu) { %>
					<html:hidden property="deklaracijaPateikta" />
				<% } %>
<%--
			<html:radio property="deklaracijaPateikta"  value="2" />&nbsp; Glob�jo(r�pintojo)&nbsp;&nbsp;&nbsp;
			<html:radio property="deklaracijaPateikta"  value="3" />&nbsp; Kito teis�to atstovo&nbsp;&nbsp;&nbsp;
--%>
		</td>
	</tr>
	<tr>
		<td class="gvdis">
			13. Pageidaujama pa�ymos apie �traukim� � gyvenamosios vietos nedeklaravusi� asmen� apskait�&nbsp;<logic:present name="error.pageidaujuDokumenta"><span class="error">Nenurodytas pasirinkimas d�l pa�ymos</span></logic:present>
			<div align="center">
				<html:radio property="pageidaujuDokumenta"  value="1" disabled='<%= internetu %>'/>&nbsp; Taip&nbsp;&nbsp;&nbsp;
				<html:radio property="pageidaujuDokumenta"  value="0" disabled='<%= internetu %>'/>&nbsp; Ne&nbsp;&nbsp;&nbsp;
				<% if(internetu) {%>
					<html:hidden property="pageidaujuDokumenta"/>
				<% } %>
			</div>
		</td>
	</tr>	 
	<tr>
		<td>
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="50%" class="gvdis">
					Pra�ymo pateikimo data:&nbsp;
					<logic:present name="error.deklaravimoData"><span class="error">Negaliojanti data</span></logic:present>
					<logic:present name="error.deklaravimoDataMin"><span class="error">Negalima deklaruoti gyvenamosios vietos nuo<br/> �ios datos - yra deklaracij�, u�registruot� v�liau.</span></logic:present>
					<logic:present name="error.deklaravimoDataMax"><span class="error">Negalima deklaruoti gyvenamosios vietos ateities data.</span></logic:present>
					<logic:present name="error.didelisSkirtumas"><span class="error">Pra�ymas negali b�ti pateiktas anks�iau<br/> nei prie� 7 dienas.</span></logic:present>
					<logic:present name="error.dataEgzistuoja"><span class="error">Gyvenamosios vietos �ra�as su tokia data jau egzistuoja</span></logic:present>
										<logic:present name="error.sistemaNeveike"><span class="error">Negalima deklaruoti gyvenamosios vietos data, ankstesne u� 2007-07-01.</span></logic:present>					
				<%String redag_data = (String)request.getAttribute("redaguoti_data");
		         if (redag_data != null) {
		         %>
		        <div style="white-space: nowrap;">
					<html:text property="deklaravimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly="true"/> -
					<html:text property="deklaravimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true"/> -
					<html:text property="deklaravimoData" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true"/>
				</div>
		         <%}
		         else {
		         %>
		        <div style="white-space: nowrap;">
					<html:text property="deklaravimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
					<html:text property="deklaravimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
					<html:text property="deklaravimoData" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
				</div>
		         <%}%>
				</td>
				<td width="50%" class="gvdis">
				� GVNA apskait� �traukiama iki:
				<div style="white-space: nowrap;">
					<html:text property="deklaracijaGaliojaMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
					<html:text property="deklaracijaGaliojaMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
					<html:text property="deklaracijaGaliojaDiena" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
				</div>
				</td>
			</tr>
			<tr>
				<td class="gvdis">
					Vardas<br />
					<html:text property="pateikejoVardas" styleClass="input_select" style="width: 300px;" maxlength="240" readonly='<%= internetu %>'/><logic:present name="error.pateikejoVardas"><span class="error">Nenurodytas pateik�jo vardas</span></logic:present>
				</td>
				<td class="gvdis">
					Pavard�<br />
					<html:text property="pateikejoPavarde" styleClass="input_select" style="width: 300px;" maxlength="240" readonly='<%= internetu %>'/><logic:present name="error.pateikejoPavarde"><span class="error">Nenurodyta pateik�jo pavard�</span></logic:present>
				</td>
			</tr>
			</table>
		</td>
	</tr>	
		<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td class="gvdis"><b>Pastabos</b>
					<html:textarea property="pastabos" style="width: 100%; height: 100px;" styleClass="input_select" readonly='<%= internetu %>'/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% if (internetu == true) {%>
		<tr>
			<td>
				Atmetimo prie�astys<br/>
				<html:textarea property="atmetimoPriezastys" style="width: 100%; height: 30px;" styleClass="input_select"/>
			</td>
		</tr>
	<% } %>
	<tr>	
	<td colspan="3" align="center">
		<hr /><input type="hidden" name="mode" value="print">
		<html:submit styleClass="button" value="I�saugoti pra�ym�" onclick="changeMode('save');"/>&nbsp;
		<% if (internetu == true) {%>
			<html:submit styleClass="button" value="Atmesti" onclick="changeMode('reject');"/>				
		<%}	%>		
		<html:submit styleClass="button" value="Spausdinti pra�ym�" onclick="changeMode('print');"/>

				
	</td>
	</tr>
	<tr>
	<td colspan="3" align="center">
		<hr /><span class="error">*</span> <b>�enklu pa�ym�tus laukus privaloma u�pildyti</b>
	</td>
	</tr>
<bean:define id="forma" name="GvnaDeklaracijaForm"/>
<logic:empty name="asmensDokumentai">
<script language="Javascript">
<!--
<% 
	String kodas = "";
	try {
		ZinynoReiksme zr = ZinynaiDelegator.getInstance().getZinynoReiksme(request, Long.parseLong(((DeklaracijaForm)forma).getDokumentoTipas()));
		kodas = zr.getKodas();
	}
	catch (Exception e){} 
%>
switchGaliojimoData('<%= kodas %>');
//-->
</script>
</logic:empty>
</html:form>
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos �vedamos formatu <i>"metai-m�nuo-diena" (2006-09-01)</i>.
</div>