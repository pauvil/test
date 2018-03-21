<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.forms.*,
				 com.algoritmusistemos.gvdis.web.*,
				 com.algoritmusistemos.gvdis.web.delegators.*" %>
<%@ page import="java.util.*" %>

<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>
<script language="javaScript">
<!--
    function changeMode(mode)
    {
    	document.IsvDeklaracijaForm.mode.value = mode;
    }

    function switchGaliojimoData(kodas)
    {
    	if ('LL' == kodas || 'LL_EB' == kodas || 'LL_ES' == kodas){
       		document.getElementById("galiojimo_data").style.display = 'block';
    	}
    	else {
       		document.getElementById("galiojimo_data").style.display = 'block';
    	}
    }     

	<%
	if(null != session.getAttribute("print"))
	{
	%>
    var pdfwin;
    function loadf()
    {
		var path = "generatePDF.do";
		if (!document.all)path = '../' + path;
		pdfwin = window.open(path,"PDF");
    }
    window.onload = loadf;    
	<%
	}
	%>    
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
<style type="text/css">
td.gvdis {
	border: 1px solid black;
}
td.gvdis_sp {
	border-left: 4px solid black;
}
</style>
<br/>

<%
	boolean internetu = false;
	
	Long idForEdit = (Long)session.getAttribute("idForEdit");
	Deklaracija d = null;
	if (idForEdit != null) {
		try {
			d = DeklaracijosDelegator.getInstance(request).getDeklaracija(idForEdit, request);		
			if (d.getSaltinis() != null){
				internetu = d.getSaltinis().longValue() == 1;				
			}

		}
		catch(Exception e){}
	}	
%>
<html:form action="IsvDeklaracijaPerform">
<% 
	String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
	if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
<div class="heading" align="center">GYVENAMOSIOS VIETOS DEKLARACIJA, PILDOMA ASMENIUI IÐVYKSTANT IÐ LIETUVOS RESPUBLIKOS ILGESNIAM NEI ÐEÐIØ MËNESIØ LAIKOTARPIUI</div>
<% } else { %>
<div class="heading" align="center">GYVENAMOSIOS VIETOS KEITIMAS UÞSIENYJE</div>
<% } %>

<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
		<table border="1" class="form" cellpadding="0" cellspacing="0" width="100%">
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
				PASTABA. Jeigu vardai<br>ar pavardë sudaro<br>daugiau negu 31 þenklà,<br>reikia áraðyti vienà vardà<br>
				ar vienà dvigubos<br>pavardës dalá.
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				4. Vardas(-ai)<br><html:text property="vardas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				5. Pavardë<br><html:text property="pavarde" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
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
				  	<td class="gvdis" align="center">El. paðto adresas:</td>
				  	<td colspan="2"><html:text property="email" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly='<%= internetu %>'/></td>
				  </tr>
				</table>
			</td>
		</tr>
		
		
		<logic:empty name="asmensDokumentai">
		<tr>
			<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
			<td colspan="4" width="70%" class="gvdis">
				7. Pilietybë				
				<html:select property="pilietybe" styleClass="input">  
				    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe" /> <logic:present name="error.missingPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
				</html:select>
			 <logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
			</td>
			<td colspan="4" width="30%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<% } else { %>
			<td colspan="5" class="gvdis">
				7. Pilietybë				
				<html:text property="pilietybePavadinimas" styleId="pilietybePavadinimas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
			<td colspan="3" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<% } %>
		</tr> 
		</logic:empty>
		
		<logic:notEmpty name="asmensDokumentai">
		<tr id="asmDoc4">
			<td colspan="5" class="gvdis">
				7. Pilietybë				
				<html:text property="pilietybePavadinimas" styleId="pilietybePavadinimas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
			<td colspan="3" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>
		<tr id="asmDoc3" style="display: none;">
			<td colspan="4" width="70%" class="gvdis">
				7. Pilietybë	
				<html:select property="pilietybe" styleClass="input">  
				    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe" /> <logic:present name="error.missingPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
				</html:select>
			 <logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
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
		<tr>
			<td width="100%" colspan="5" align="left" class="gvdis">
				8. Asmens dokumentas <span class="error"><% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>*<% } %></span> <logic:present name="error.dokumentoTipas"><span class="error">Nepasirinktas asmens dokumento tipas</span></logic:present>
                    <logic:present name="error.deklaracijaNepateiktasAsmensDokumentas"><span class="error">Asmenys nuo 16 m. privalo pateikti asmens tapatybës kortelæ arba pasà</span></logic:present>
					<logic:notEmpty name="asmensDokumentai">
						<br />
						<%-- <div id="asmDoc" style="display:inline;">	--%>										
						<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
							<html:select property="documentId" styleClass="input" style="width: 720px; " onchange="flipDokumentuLaukai(this);getDocPilietybe(this[selectedIndex].value);" disabled='<%= internetu %>' > 
								<html:options collection="asmensDokumentai" property="dokNr" labelProperty="title" />
								<html:option value="-1">&lt Ávedamas uþpildant asmens dokumento laukus &gt</html:option>								
								<%-- <html:option value="0" >&nbsp;</html:option> //ju.k 2007.06.29 --%>
							 </html:select> 						
						<% } else { %>
							<html:select property="documentId" styleClass="input" style="width: 720px; " onchange="flipDokumentuLaukai(this);getDocPilietybe(this[selectedIndex].value);" disabled="true" > 
								<html:options collection="asmensDokumentai" property="dokNr" labelProperty="title" />
								<html:option value="-1">&lt Ávedamas uþpildant asmens dokumento laukus &gt</html:option>								
								<%-- <html:option value="0" >&nbsp;</html:option> //ju.k 2007.06.29 --%>
							 </html:select> 						
						<% } %>
						<script type="text/javascript">
								getDocPilietybe(document.getElementById('documentId')[0].value);
						</script>
						<br /><br />
					</logic:notEmpty>
					<logic:empty name="asmensDokumentai">
						<br />											
							<html:select property="documentId" styleClass="input" disabled="true" style="width: 720px; "> 
								<html:option value="-1">&lt Ávedamas uþpildant asmens dokumento laukus &gt</html:option>								
							</html:select>								
						<br /><br />
					</logic:empty>
					<% if (internetu) {%>
						<html:hidden property="documentId"/>
					<% } %>
			</td>
		</tr>
		
		
	  	<logic:notEmpty name="asmensDokumentai">			
			<logic:notEqual name="IsvDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc1" style="display: none;">
			</logic:notEqual>
			<logic:equal name="IsvDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc1">
			</logic:equal>
		</logic:notEmpty>
		<logic:empty name="asmensDokumentai">	
		<tr id="asmDoc1">
		</logic:empty>				
		<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
			<td width="420px"  class="gvdis">
				Numeris <logic:present name="error.asmensDokumentoNumeris"><span class="error">Nenurodytas asmens dokumento numeris</span></logic:present> <br>
				<html:text property="asmensDokumentoNumeris" styleClass="input_select"  style="width:95%;" maxlength="240" readonly='<%= internetu %>'/>&nbsp;
				
			</td>
			
			<td width="100" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
	
			<td width="420px" class="gvdis">
					
				Iðdavimo data <logic:present name="error.asmensDokumentoIsdavimoData"><span class="error">Nenurodyta asmens dokumento iðdavimo data</span></logic:present>
				<div style="white-space: nowrap;">
					<html:text property="asmensDokumentoIsdavimoMetai" styleClass="inputFixed" maxlength="4" style="width: 87px;" readonly='<%= internetu %>'/> -
					<html:text property="asmensDokumentoIsdavimoMenuo" styleClass="inputFixed" maxlength="2" style="width: 45px;" readonly='<%= internetu %>'/> -
					<html:text property="asmensDokumentoIsdavimoData" styleClass="inputFixed" maxlength="2" style="width: 45px;"  readonly='<%= internetu %>'/>
				
			</div>
			</td>
			<td width="100" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		
		</tr>
		
		<logic:notEmpty name="asmensDokumentai">			
			<logic:notEqual name="IsvDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc2" style="display: none;"> 
			</logic:notEqual>
			<logic:equal name="IsvDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc2">
			</logic:equal>
		</logic:notEmpty>
		<logic:empty name="asmensDokumentai">	
		<tr id="asmDoc2">
		</logic:empty>	
			<td width="420px"  class="gvdis">
				Kas iðdavë <logic:present name="error.asmensDokumentoIsdave"><span class="error">Nenurodyta asmens dokumentà iðdavusi ástaiga</span></logic:present>  
				<br />
				<html:text property="asmensDokumentoIsdave" styleClass="input_select" style="width:95%;" maxlength="240" readonly='<%= internetu %>'/>
			</td>
			<td  width="100" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td width="420px" class="gvdis">
				<div id="galiojimo_data">
				 Galiojimo data (jei pateiktas leidimas laikinai gyventi Lietuvos Respublikoje)
				 <div style="white-space: nowrap;">
					<html:text property="leidimoGaliojimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
					<html:text property="leidimoGaliojimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
					<html:text property="leidimoGaliojimoData" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
				 </div>
				 <logic:present name="error.leidimoGaliojimoData"><span class="error">Nenurodyta galiojimo data</span></logic:present>
				</div>
			</td>
			<td  width="100" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
	<% } %>
		</tr>
		</table>
		</td>
	</tr>
	
	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="50%" class="gvdis" valign="top">
				<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
				9. Numatoma gyvenamoji vieta uþsienyje <span class="error">*</span> <br>
				<% } else { %>
				9. Dabartinë gyvenamoji vieta (valstybë) <span class="error">*</span> <br>
				<% } %>
				<html:select property="atvykoIsUzsienioSalis" styleClass="input" disabled='<%= internetu %>'>  
					<html:option value=""></html:option>
				    <html:options collection="salysbelietuvos" property="kodas" labelProperty="pavadinimas" /> 
				</html:select>
				<logic:present name="error.numatomaSalis"><span class="error">Nenurodyta gyvenamoji vieta uþsienyje</span></logic:present>
				<logic:present name="error.vienodaSalis"><span class="error">Nurodyta gyvenamoji vieta uþsienyje negali sutapti su ankstesne gyvenamàjà vieta</span></logic:present>
				<% if (internetu) { %>
					<html:hidden property="atvykoIsUzsienioSalis" />
				<% } %>
				<br />
				<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
				<html:textarea property="atvykoIsUzsienioTextarea" style="width: 100%; height: 60px;" styleClass="input_select" readonly='<%= internetu %>'/>
				<% } %>
			</td>
			<td width="50%" valign="top">
				<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td class="gvdis" colspan="2">
						<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
						&nbsp;10. Gyvenamoji vieta, ið kurios iðvykstama <span class="error">*</span>
						<% } else { %>
						&nbsp;10. Ankstesnë gyvenamoji vieta (valstybë)
						<% } %>
						<logic:present name="error.ankstesneGyvenamojiVieta"><span class="error">Nenurodyta gyvenamoji vieta, ið kurios iðvykstama</span></logic:present>
					</td>
				</tr>
				<logic:present name="ankstesnisAdresas">
				<tr>
					<td class="gvdis" colspan="2">
						<html:radio property="ankstesneGyvenamojiVieta" value="0" disabled='<%= internetu %>'/>&nbsp;Ankstesnë deklaruota gyvenamoji vieta:<br />
						<b><bean:write name="ankstesnisAdresas" property="adr" /></b><br />
					</td>
				</tr>
				</logic:present>
				<logic:notPresent name="ankstesnisAdresas">
				<tr>
					<td class="gvdis" colspan="2" rowspan="2">
						<html:radio property="ankstesneGyvenamojiVieta" value="2" disabled='<%= internetu %>'/>&nbsp;Kita gyvenamoji vieta:
						<logic:present name="error.kitaGyvenamojiVietaAprasymas"><span class="error">Nenurodyta ankstesnë gyvenamoji vieta</span></logic:present>
						<br />
						<html:textarea property="kitaGyvenamojiVietaAprasymas" style="width: 100%; height: 30px;" styleClass="input_select" readonly='<%= internetu %>'/>
					</td>
				</tr>
				</logic:notPresent>
				<% if(internetu) {%>
					<html:hidden property="ankstesneGyvenamojiVieta"/>
				<% } %>
				
				</table>
			</td>			
		</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td colspan="5" class="gvdis">
					<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
						11. Deklaracija pateikta&nbsp;&nbsp;
						<logic:present name="error.deklaracijaPateikta"><span class="error">Nenurodyta, kas pateikë deklaracijà</span></logic:present>
						<logic:present name="error.deklaracijaPateiktaVaiko"><span class="error">Nepilnametis asmuo negali pateikti deklaracijos asmeniðkai</span></logic:present>
						<logic:present name="error.deklaracijaPateiktaTevu"><span class="error">Pilnametis asmuo deklaracijà turi pateikti asmeniðkai</span></logic:present>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<html:radio property="deklaracijaPateikta"  value="0" disabled='<%= internetu %>'/>&nbsp; Asmeniðkai&nbsp;&nbsp;&nbsp;
						<html:radio property="deklaracijaPateikta"  value="1" disabled='<%= internetu %>' />&nbsp; Vieno ið tëvø(átëviø)&nbsp;&nbsp;&nbsp;
						<html:radio property="deklaracijaPateikta"  value="2" disabled='<%= internetu %>' />&nbsp; Globëjo(rûpintojo)&nbsp;&nbsp;&nbsp;
						<html:hidden property="deklaracijaPateikta"/>
					<% } %>
				</td>
			</tr>
			<tr>
				<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
					<td colspan="3" class="gvdis">
						Vardas<br /><html:text property="pateikejoVardas" styleClass="input_select" style="width: 300px;" maxlength="240" readonly='<%= internetu %>'/><logic:present name="error.pateikejoVardas"><span class="error">Nenurodytas pateikëjo vardas</span></logic:present>
					</td>
					<td colspan="2" class="gvdis">
						Pavardë<br /><html:text property="pateikejoPavarde" styleClass="input_select" style="width: 300px;" maxlength="240" readonly='<%= internetu %>'/><logic:present name="error.pateikejoPavarde"><span class="error">Nenurodyta pateikëjo pavardë</span></logic:present>
					</td>
				<% } %>	
			</tr>
			<tr>
				<td width="5%" bgcolor="#bbbbbb" class="gvdis">
					&nbsp;
				</td>
				<td width="40%" class="gvdis">
					Deklaracijos pateikimo data:&nbsp;
					<logic:present name="error.deklaravimoData"><span class="error">Nenurodyta deklaravimo data</span></logic:present>
					<logic:present name="error.deklaravimoDataMin"><span class="error">Negalima deklaruoti gyvenamosios vietos nuo ðios datos - yra deklaracijø, uþregistruotø vëliau.</span></logic:present>
					<logic:present name="error.deklaravimoDataMax"><span class="error">Negalima deklaruoti gyvenamosios vietos ateities data.</span></logic:present>
					<logic:present name="error.didelisSkirtumas"><span class="error">Deklaracija negali bûti pateikta anksèiau<br/> nei prieð 7 dienas.</span></logic:present>
					<logic:present name="error.sistemaNeveike"><span class="error">Negalima deklaruoti gyvenamosios vietos data, ankstesne uþ 2007-07-01.</span></logic:present>					
					<logic:present name="error.dataEgzistuoja"><span class="error">Gyvenamosios vietos áraðas su tokia data jau egzistuoja</span></logic:present>
				<%String redag_data = (String)request.getAttribute("redaguoti_data");
		         if (redag_data != null) {
		         %>
		        	<div style="white-space: nowrap;">
						<html:text property="deklaravimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly="true" /> -
						<html:text property="deklaravimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true" /> -
						<html:text property="deklaravimoData" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true" />
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
				<td width="5%" bgcolor="#bbbbbb" class="gvdis">
					&nbsp;
				</td>
				<td width="40%" class="gvdis">
					<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
					12.Iðvykimo data <span class="error">*</span> 
							<logic:present name="error.isvykimoData"><span class="error">&nbsp;Nenurodyta iðvykimo data</span></logic:present>
							<logic:present name="error.isvykimoDataMax"><span class="error">Iðvykimo data negali bûti vëlesnë nei 7 d. nuo einamosios dienos</span></logic:present>
						<div style="white-space: nowrap;">
							<html:text property="isvykimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
							<html:text property="isvykimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
							<html:text property="isvykimoData" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
						</div>
					<% } else { %>
					<br />&nbsp;
					<% } %>
				</td>
				<td width="10%" bgcolor="#bbbbbb" class="gvdis">
					&nbsp;
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
					<td class="gvdis">13. <b>Pastabos</b>
					<html:textarea property="pastabos" style="width: 100%; height: 100px;" styleClass="input_select" readonly='<%= internetu %>'/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% if (internetu == true) {%>
		<tr>
			<td>
				Atmetimo prieþastys<logic:present name="error.atmetimoPriezastis"><span class="error">&nbsp; Bûtina nurodyti atmetimo prieþastá.</span></logic:present><br/>
				<html:textarea property="atmetimoPriezastys" style="width: 100%; height: 30px;" styleClass="input_select"/>
			</td>
		</tr>
	<% } %>
	<tr>
	<td colspan="3" align="center">
	<hr />
		<input type="hidden" name="mode" value="print">
		<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
			<html:submit styleClass="button" value="Iðsaugoti deklaracijà" onclick="changeMode('save');"/>&nbsp;
			<% if (internetu == true) {%> 
				<html:submit styleClass="button" value="Atmesti" onclick="changeMode('reject');"/>
			<%}	%>
	
			<% if (false == internetu){ %>
			<html:submit styleClass="button" value="Spausdinti deklaracijà" onclick="changeMode('print');"/>
			<% }%>
		<% } else { %>
			<% if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { %>
				<html:submit styleClass="button" value="Iðsaugoti gyv. vietà" onclick="changeMode('save');"/>&nbsp;
				<html:submit styleClass="button" value="Spausdinti gyv. vietà" onclick="changeMode('print');"/>
			<% } else { %>	
			<html:submit styleClass="button" value="Iðsaugoti" onclick="changeMode('save');"/>&nbsp;
			<% } %>
		<% } %>
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr /><span class="error">*</span> <b>þenklu paþymëtus laukus privaloma uþpildyti</b>
	</td>
</tr>
<bean:define id="forma" name="IsvDeklaracijaForm"/>
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
</table>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>
</html:form>
