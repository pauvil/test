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
td.test{
	border: 6px solid black;
}
</style>
<html:form action="AtvDeklaracijaPerform">
<bean:define id="forma" name="AtvDeklaracijaForm"/>
<div class="heading" align="center">GYVENAMOSIOS VIETOS DEKLARACIJA, PILDOMA ASMENIUI PAKEITUS GYVENAMÀJÀ VIETÀ LIETUVOS RESPUBLIKOJE AR ATVYKUS GYVENTI Á LIETUVOS RESPUBLIKÀ</div>
<script language="javascript">
<!--

	function setAddress(adrVienetas, gvtKampoNr, terVienetas, stringAddress)
	{
		document.getElementById('addressString').innerHTML = '<div class="heading2">'+stringAddress+'</div>';
		document.getElementById('addressStringError').innerHTML = '';
		
		if('' == adrVienetas && '' != terVienetas)
		{
			document.AtvDeklaracijaForm.addressId.value = terVienetas;
			document.AtvDeklaracijaForm.addressType.value = 'T';	
		}
		if('' != adrVienetas && '' == terVienetas)
		{
			document.AtvDeklaracijaForm.addressId.value = adrVienetas;
			document.AtvDeklaracijaForm.addressType.value = 'A';
			document.AtvDeklaracijaForm.gvtKampoNr.value = gvtKampoNr;
		}
	}
    function switchGaliojimoData(kodas)
    {
    	if ('LL' == kodas || 'LL_EB' == kodas || 'LL_ES' == kodas || documentId == -1){
       		document.getElementById("galiojimo_data").style.display = 'block';
    	}
    	else { 
       		document.getElementById("galiojimo_data").style.display = 'none';
    	}
    }     

    function changeMode(mode)
    {
    	document.AtvDeklaracijaForm.mode.value = mode;
    }


    function controlGyvVieta(value)
    {
	    var atvykoIsUzsienioSalis = document.AtvDeklaracijaForm.atvykoIsUzsienioSalis;
	    var atvykoIsUzsienioTextarea = document.AtvDeklaracijaForm.atvykoIsUzsienioTextarea;
	    var kitaGyvenamojiVietaAprasymas = document.AtvDeklaracijaForm.kitaGyvenamojiVietaAprasymas;
    
    	if(0 == value)
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = true;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = true;
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = true;
    		
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.value=""; 
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.value=""; 
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.value=""; 
    	}
    	else
    	if(1 == value)
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = false;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = false;    		
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = true;
    		
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.value="";     		
    	}
    	else
    	if(2 == value)
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = true;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = true;    		
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = false;      		
    	
    	
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.value=""; 
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.value="";  	
    	}
    	else
    	{
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.disabled = true;
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.disabled = true;    		
    		if(kitaGyvenamojiVietaAprasymas)kitaGyvenamojiVietaAprasymas.disabled = false;      		
    	
    	
    		if(atvykoIsUzsienioSalis)atvykoIsUzsienioSalis.value=""; 
    		if(atvykoIsUzsienioTextarea)atvykoIsUzsienioTextarea.value="";      	
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
	Deklaracija d = null;
	Long idForEdit = (Long)session.getAttribute("idForEdit");
	String saltinis = null;
	if (idForEdit != null) {
		try {
			d = DeklaracijosDelegator.getInstance(request).getDeklaracija(idForEdit, request);		
			if (d.getSaltinis() != null){
				internetu = d.getSaltinis().longValue() == 1;				
				saltinis = "1";
			}

		}
		catch(Exception e){}
	}	
%>
<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
<tr>

	<td colspan="2">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20%" class="gvdis">
				1. Asmens kodas<br>
				<html:text property="asmensKodas" styleClass="inputFixed" maxlength="11" style="width: 220px;" readonly="true" />				
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
				3. Lytis<br>
				<html:radio property="lytis" disabled="true" value="V" />&nbsp; Vyr.&nbsp;&nbsp;&nbsp;<html:radio property="lytis" disabled="true" value="M" />&nbsp; Mot.
			</td>
			<td rowspan="3" width="20%" align="center" valign="middle" class="gvdis">
				PASTABA. Jeigu vardai<br>ar pavardë sudaro<br>daugiau negu 31 þenklà,<br>reikia áraðyti vienà vardà<br>
				ar vienà dvigubos<br>pavardës dalá.
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				4. Vardas(-ai)<br>
				<html:text property="vardas" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				5. Pavardë<br>
				<html:text property="pavarde" styleClass="inputFixed" maxlength="31" style="width:620px;" readonly="true" />
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
			<td colspan="5" width="10%" class="gvdis">
				7. Pilietybë	
				<logic:present name="prohibited">
					
					<html:select property="pilietybe" styleClass="input" disabled="true">  
					    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe"/> <logic:present name="error.missingPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
					    </html:select>
				<logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
				</logic:present>
				<logic:notPresent name="prohibited">
					<html:select property="pilietybe" styleClass="input">  
					    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe"/> <logic:present name="error.missingPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
					     </html:select>
			    <logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
				</logic:notPresent>				
			</td>
			<td colspan="4" width="30%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
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
			<td colspan="4" class="gvdis">
				7. Pilietybë	
				<html:select property="pilietybe" styleClass="input">  
				    <html:options collection="pilietybes" property="kodas" labelProperty="pilietybe" /> <logic:present name="error.missingPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
				     </html:select>
			 <logic:present name="error.nenurodytaPilietybe"><span class="error">Nenurodyta pilietybë</span></logic:present>
			</td>
			<td bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>  
		</logic:notEmpty>
		</table>
	</td>
</tr>
<tr>
	<td colspan="2">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="100%" colspan="5" align="left" class="gvdis">
				8. Asmens dokumentas <span class="error">*</span> <logic:present name="error.dokumentoTipas"><span class="error">Nepasirinktas asmens dokumento tipas</span></logic:present>
					<logic:present name="error.negaliojantisDokumentas"><span class="error">Negaliojantis dokumentas</span></logic:present>
					<logic:present name="error.deklaracijaNepateiktasAsmensDokumentas"><span class="error">Asmenys nuo 16 m. privalo pateikti asmens tapatybës kortelæ arba pasà</span></logic:present>
					<logic:notEmpty name="asmensDokumentai">
						<br />
						
						<%-- <div id="asmDoc" style="display:inline;">	--%>										
							<html:select property="documentId" styleClass="input" style="width: 720px; " onchange="flipDokumentuLaukai(this);getDocPilietybe(this[selectedIndex].value);"   disabled='<%= internetu %>'> 								
								<html:options collection="asmensDokumentai" property="dokNr" labelProperty="title" />
								<html:option value="-1">&lt Ávedamas uþpildant asmens dokumento laukus &gt</html:option>									
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
								<html:option value="-1">&lt Ávedamas uþpildant asmens dokumento laukus &gt</html:option>								
							</html:select>								
						<br /><br />
					</logic:empty>
			</td>
		</tr>
		
	
	  	<logic:notEmpty name="asmensDokumentai">			
	  		<logic:equal name="AtvDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc1"  >
			</logic:equal>
			<logic:notEqual name="AtvDeklaracijaForm" property="documentId" value="-1">						
				<tr id="asmDoc1" style="display: none;  " >
			</logic:notEqual>
		</logic:notEmpty>
		<logic:empty name="asmensDokumentai">	
		<tr id="asmDoc1" >
		</logic:empty>		
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
			<td width="100"  bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			
		
		
		 
		<logic:notEmpty name="asmensDokumentai">			
			<logic:notEqual name="AtvDeklaracijaForm" property="documentId" value="-1">
				<tr id="asmDoc2" style="display: none;">
			</logic:notEqual>
			<logic:equal name="AtvDeklaracijaForm" property="documentId" value="-1">
				<tr id="asmDoc2">
			</logic:equal>
		</logic:notEmpty>
		<logic:empty name="asmensDokumentai">	
		<tr id="asmDoc2">
		</logic:empty>		
			<td width="420px"  class="gvdis">
				Kas iðdavë
				<logic:present name="error.asmensDokumentoIsdave">
					<span class="error">
						Nenurodyta asmens dokumentà iðdavusi ástaiga
					</span>
				</logic:present>
				<br />
				<html:text property="asmensDokumentoIsdave"
					styleClass="input_select" style="width:95%;" maxlength="240"
					readonly='<%= internetu %>' />
			</td>		
		<td width="100"  bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td width="420px" class="gvdis">
				<div id="galiojimo_data">
				 Galiojimo data <!-- (jei pateiktas leidimas laikinai gyventi Lietuvos Respublikoje) -->
				 <div style="white-space: nowrap;">
					<html:text property="leidimoGaliojimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
					<html:text property="leidimoGaliojimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
					<html:text property="leidimoGaliojimoData" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
				 </div>
				 <logic:present name="error.leidimoGaliojimoData"><span class="error">Nenurodyta galiojimo data</span></logic:present>
				</div>
			</td>
			<td width="100" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>
		
		
	</td>
	</table>
</tr>
<tr>
	<td valign="top">
		<table class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td valign="top" colspan="2" class="gvdis">
				&nbsp;9. Deklaruojama gyvenamoji vieta <logic:present name="error.galiojantisAdresas"><span class="error">Ðiuo adresu asmuo jau yra deklaravæs gyvenamàjà vietà. Deklaracija nebus iðsaugota. Pasirinkite kità adresà.</span></logic:present><span id="addressStringError"><logic:present name="error.addressId"><span class="error">Nepasirinktas gyvenamosios vietos adresas</span></logic:present></span>
				<logic:present name="error.gatveBeAdreso"><span class="error">Negalima nurodyti adreso kaip gatvës be namo numerio</span></logic:present>
			</td>
		</tr>
		<tr>
			<td class="gvdis" valign="top" colspan="2">
				<html:hidden property="addressId" styleId="addressId" />
				<html:hidden property="addressType" styleId="addressType" />
				<html:hidden property="gvtKampoNr" styleId="gvtKampoNr" />
				<br />
				<span id="addressString">
				<%
					String addressId = ((AtvDeklaracijaForm)forma).getAddressId();
					String addressType = ((AtvDeklaracijaForm)forma).getAddressType();
					String gvtKampoNr = ((AtvDeklaracijaForm)forma).getGvtKampoNr();
					String addressString = "";
					try {
							addressString = AdresaiDelegator.getInstance().getAdresoEilute(Long.parseLong(addressId), addressType, request, gvtKampoNr);	
					}
					catch (Exception e){
						//e.printStackTrace();
					}
					pageContext.setAttribute("aString", addressString);
				%>
				<logic:present name="aString">
					<div class="heading2"><bean:write name="aString" scope="page" /></div>
				</logic:present>
				</span>
				<br /><br />
				<div align="right">
					<logic:present name="prohibited">
						<input type="button" class="button" onclick="openPopup('addressbrowser.do');" value="Iðsirinkite gyvenamàjà vietà &raquo;" style="width: 150px;" disabled/>
					</logic:present>
					<logic:notPresent name="prohibited">
						<input type="button" class="button" onclick="openPopup('addressbrowser.do');" value="Iðsirinkite gyvenamàjà vietà &raquo;" style="width: 150px;" />
					</logic:notPresent>					
				</div>
			</td>
		</tr>
		<tr>
			<td class="gvdis" colspan="2">
				11. Unikalaus pastato (patalpos) numeris:&nbsp;
				<div style="white-space: nowrap;">
					<html:text property="unikPastatoNr1" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> - 
					<html:text property="unikPastatoNr2" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
					<html:text property="unikPastatoNr3" styleClass="inputFixed" maxlength="4" style="width:87px;"  readonly='<%= internetu %>'/> :
					<html:text property="unikPastatoNr4" styleClass="inputFixed" maxlength="4" style="width:87px;"  readonly='<%= internetu %>'/>
				</div>
			</td>
		</tr>		
		<tr> 
			<td class="gvdis" colspan="2"  bgcolor="#bbbbbb">&nbsp;</td>
		</tr>
		
		</table>
	</td>
	<td width="50%" valign="top">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
		<tr>
			<td class="gvdis">10. Ankstesnës gyvenamosios vietos adresas <span class="error">*</span>
				<logic:present name="error.ankstesneGyvenamojiVieta"><span class="error">Nepasirinktas ankstesnës gyvenamosios vietos adresas</span></logic:present>
			<td>
		</tr>
		<logic:present name="ankstesnisAdresas">
		<tr>
			<td class="gvdis">
				<html:radio property="ankstesneGyvenamojiVieta" value="0" onclick="controlGyvVieta(this.value);" disabled='<%= internetu %>'/>&nbsp;Ankstesnë deklaruota gyvenamoji vieta:<br />
				<b><bean:write name="ankstesnisAdresas" property="adr" /></b><br />
				<html:hidden property="gvtNrAnkstesne" />
			</td>
		</tr>
		<logic:equal name="ankstesnisAdresas" property="type" value="V">
		<tr>
			<td class="gvdis">
				<html:radio property="ankstesneGyvenamojiVieta" value="1" onclick="controlGyvVieta(this.value);" disabled='<%= internetu %>'/>&nbsp;Atvyko ið uþsienio:<br />
				<html:select property="atvykoIsUzsienioSalis" styleClass="input_select" style="width: 100%;" disabled='<%= internetu %>'>
					<html:options collection="salys" property="kodas" labelProperty="pavadinimas" />	
				</html:select>
				<% if (internetu) { %>
					<html:hidden property="ankstesneGyvenamojiVieta" />
					<html:hidden property="atvykoIsUzsienioSalis" />
				<% } %>
				<html:textarea property="atvykoIsUzsienioTextarea" style="width: 100%; height: 30px;" styleClass="input_select" readonly='<%= internetu %>'/>
			</td>
		</tr>
		</logic:equal>
		<logic:equal name="ankstesnisAdresas" property="type" value="U">
		<tr>
			<td class="gvdis">
				<html:radio property="ankstesneGyvenamojiVieta" value="1" onclick="controlGyvVieta(this.value);"  disabled='<%= internetu %>' />&nbsp;Atvyko ið uþsienio:<br />
				<html:select property="atvykoIsUzsienioSalis" styleClass="input_select" style="width: 100%;"  disabled='<%= internetu %>'>
					<html:options collection="salys" property="kodas" labelProperty="pavadinimas" />	
				</html:select>
				<% if (internetu) { %>
					<html:hidden property="ankstesneGyvenamojiVieta" />
					<html:hidden property="atvykoIsUzsienioSalis" />
				<% } %>
				<html:textarea property="atvykoIsUzsienioTextarea" style="width: 100%; height: 30px;" styleClass="input_select" readonly='<%= internetu %>'/>
			</td>
		</tr>
		</logic:equal>
		</logic:present>
		<logic:notPresent name="ankstesnisAdresas">
		<tr>
			<td class="gvdis">
				<html:radio property="ankstesneGyvenamojiVieta" value="2" onclick="controlGyvVieta(this.value);"   disabled='<%= internetu %>'/>&nbsp;Kita gyvenamoji vieta:
				<logic:present name="error.kitaGyvenamojiVietaAprasymas">'><span class="error">Nenurodyta ankstesnë gyvenamoji vieta</span></logic:present>
				<br />
				<html:textarea property="kitaGyvenamojiVietaAprasymas" style="width: 100%; height: 30px;" styleClass="input_select" readonly='<%= internetu %>'/>
				<% if (internetu) { %>
					<html:hidden property="ankstesneGyvenamojiVieta" />
				<% } %>
			</td>
		</tr>
		</logic:notPresent>
		</table>
	</td>
</tr>
<tr>
	<td colspan="2">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td colspan="5" class="gvdis">
				12. Deklaracija pateikta&nbsp;&nbsp;
				<logic:present name="error.deklaracijaPateikta"><span class="error">Nenurodyta, kas pateikë deklaracijà</span></logic:present>
				<logic:present name="error.deklaracijaPateiktaVaiko"><span class="error">Nepilnametis asmuo negali pateikti deklaracijos asmeniðkai</span></logic:present>
				<logic:present name="error.deklaracijaPateiktaTevu"><span class="error">Pilnametis asmuo deklaracijà turi pateikti asmeniðkai</span></logic:present>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<html:radio property="deklaracijaPateikta"  value="0"  disabled='<%= internetu %>'/>&nbsp; Asmeniðkai&nbsp;&nbsp;&nbsp;
				<html:radio property="deklaracijaPateikta"  value="1"  disabled='<%= internetu %>'/>&nbsp; Vieno ið tëvø(átëviø)&nbsp;&nbsp;&nbsp;
				<html:radio property="deklaracijaPateikta"  value="2"  disabled='<%= internetu %>'/>&nbsp; Globëjo(rûpintojo) ar neveiksnaus asmens atstovo&nbsp;&nbsp;&nbsp;
				<% if (internetu) {%>
					<html:hidden property="deklaracijaPateikta"/>
				<% } %>
			</td>
		</tr>
		<tr>
			<td width="50%" colspan="3" class="gvdis">
				Vardas<br />
				<html:text property="pateikejoVardas" styleClass="input_select" style="width: 300px;" maxlength="240" readonly='<%= internetu %>'/><logic:present name="error.pateikejoVardas"><span class="error">Nenurodytas pateikëjo vardas</span></logic:present>
			</td>
			<td width="50%" colspan="2" class="gvdis">
				Pavardë<br />
				<html:text property="pateikejoPavarde" styleClass="input_select" style="width: 300px;" maxlength="240" readonly='<%= internetu %>'/><logic:present name="error.pateikejoPavarde"><span class="error">Nenurodyta pateikëjo pavardë</span></logic:present>
			</td>
		</tr>
		<tr>
			<td bgcolor="#bbbbbb" class="gvdis" width="10%">
				&nbsp;
			</td>
			<td class="gvdis" width="30%">
				Deklaracijos pateikimo data&nbsp;
				<logic:present name="error.deklaravimoData"><span class="error">Neteisingai nurodyta deklaravimo data.</span></logic:present>
				<logic:present name="error.deklaravimoDataMin"><span class="error">Negalima deklaruoti gyvenamosios vietos nuo<br/> ðios datos - yra deklaracijø, uþregistruotø vëliau.</span></logic:present>
				<logic:present name="error.deklaravimoDataMax"><span class="error">Negalima deklaruoti gyvenamosios vietos ateities data.</span></logic:present>
				<logic:present name="error.didelisSkirtumas"><span class="error">Deklaracija negali bûti pateikta anksèiau<br/> nei prieð 7 dienas.</span></logic:present>
				<logic:present name="error.sistemaNeveike"><span class="error">Negalima deklaruoti gyvenamosios vietos data, ankstesne uþ 2007-07-01.</span></logic:present>
				<logic:present name="error.dataEgzistuoja"><span class="error">Gyvenamosios vietos áraðas su tokia data jau egzistuoja</span></logic:present>
				<%String redag_data = (String)request.getAttribute("redaguoti_data");
		         if (redag_data != null) {
		         %>
		        <div style="white-space: nowrap;">
					<html:text property="deklaravimoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly="true"/> -
					<html:text property="deklaravimoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly="true"/> -
					<html:text property="deklaravimoData" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly="true"/>
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

			<td width="10%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td width="30%" class="gvdis">
				Paraðas<br />&nbsp;
			</td>
			<td width="20%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td width="15%" colspan="5" class="gvdis">
		Pageidauju gauti dokumentà, patvirtinantá deklaruotà gyvenamàjà vietà&nbsp;<logic:present name="error.pageidaujuDokumenta"><span class="error">Nenurodytas pasirinkimas dël paþymos</span></logic:present>
		<div align="center">
			<html:radio property="pageidaujuDokumenta"  value="1" disabled='<%= internetu %>' />&nbsp; Taip&nbsp;&nbsp;&nbsp;
			<html:radio property="pageidaujuDokumenta"   value="0" disabled='<%= internetu %>'  />&nbsp; Ne&nbsp;&nbsp;&nbsp;
			<% if (internetu) {%>
				<html:hidden property="pageidaujuDokumenta"/>
			<% } %>
		</div>
	</td>
</tr>
<tr>
	<td width="15%" colspan="5" class="gvdis">
		Asmens, deklaruojanèio gyvenamàjà vietà, ryðys su deklaruojama gyvenamàja vieta<logic:present name="error.rysisSuGv"><span class="error">Nenurodytas santykis su gyvenamàja vieta</span></logic:present>
		<div align="center">
			<html:radio property="rysisSuGv" value="0" disabled='<%= internetu %>'/>&nbsp; Savininkas&nbsp;&nbsp;&nbsp;
			<html:radio property="rysisSuGv" value="1" disabled='<%= internetu %>'/>&nbsp; Nesavininkas&nbsp;&nbsp;&nbsp;
			
			<% if (internetu) {%>
				<html:hidden property="rysisSuGv"/>
			<% } %>
		</div>
	</td>
</tr>

<tr>
	<td colspan="5" class="gvdis">
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td class="gvdis" rowspan="7" width="20%">
				13. Gyvenamosios patalpos savininko (bendraturèiø) sutikimas
			</td>
			<td class="gvdis" width="40%" colspan="2">
				Deklaracija galioja iki: 
				<logic:present name="error.deklaravimoGaliojimoDataBefore"><span class="error">Deklaracijos galiojimo data iki turi bûti velësne 30 dienø nuo dabartinës</span></logic:present>
				<logic:present name="error.deklaravimoGaliojimoDataEdit"><span class="error">Deklaracijos galiojimo data iki redaguojant negali bûti ankstesnë negu prieð tai nurodyta</span></logic:present>				
				<div style="white-space: nowrap;">
					<html:text property="deklaracijaGaliojaMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
					<html:text property="deklaracijaGaliojaMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
					<html:text property="deklaracijaGaliojaDiena" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
				</div>
			</td>
		</tr>
		<!-- 
		<tr>
			<td class="gvdis" width="50%" >Vardas, pavardë (juridinio asmens pavadinimas)</td>
			<td class="gvdis" width="50%">Asmens kodas (ámonës kodas)</td> 
		</tr>
		 -->
		<tr>
		<!-- 
			<td class="gvdis" width="40%">1. <html:text property="savininkas1" styleClass="inputFixed" maxlength="50" style="width:600px;" readonly='<%= internetu %>'/></td>
			<td class="gvdis" width="40%"><html:text property="savininkasKodas1" styleClass="inputFixed" maxlength="20" style="width:400px;" readonly='<%= internetu %>'/></td>
			-->			
			<td class="gvdis" width="50%" height="80px">Vardas, pavardë (juridinio asmens pavadinimas)<br><html:textarea property="savVardas" styleClass="input_select" style="width:100%; height:50px" readonly='<%= internetu %>'/></td>					
			<td class="gvdis" width="50%" height="80px">Asmens kodas (ámonës kodas)<br><html:textarea property="savAsmKodas" styleClass="input_select" style="width:100%; height:50px" readonly='<%= internetu %>'/></td>
			
		</tr>
		<!--
		<tr>
			<td class="gvdis" width="40%">2. <html:text property="savininkas2" styleClass="inputFixed" maxlength="50" style="width:600px;" readonly='<%= internetu %>'/></td>
			<td class="gvdis" width="40%"><html:text property="savininkasKodas2" styleClass="inputFixed" maxlength="20" style="width:400px;" readonly='<%= internetu %>'/></td>
		</tr>
		<tr>
			<td class="gvdis" width="40%">3. <html:text property="savininkas3" styleClass="inputFixed" maxlength="50" style="width:600px;" readonly='<%= internetu %>'/></td>
			<td class="gvdis" width="40%"><html:text property="savininkasKodas3" styleClass="inputFixed" maxlength="20" style="width:400px;" readonly='<%= internetu %>'/></td>
		</tr>
		<tr>
			<td class="gvdis" width="40%">4. <html:text property="savininkas4" styleClass="inputFixed" maxlength="50" style="width:600px;" readonly='<%= internetu %>'/></td>
			<td class="gvdis" width="40%"><html:text property="savininkasKodas4" styleClass="inputFixed" maxlength="20" style="width:400px;" readonly='<%= internetu %>'/></td>
		</tr>								
			-->
		<logic:notPresent name="activeDeclaration">
		<tr>
			<td colspan="2">
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td width="10%" bgcolor="#bbbbbb" class="gvdis">&nbsp;</td>
					<td width="35%">
						Data <logic:present name="error.savaninkoParasoData"><span class="error">&nbsp;*</span></logic:present>
						<div style="white-space: nowrap;">
							<html:text property="savininkoParasoMetai" styleClass="inputFixed" maxlength="4" style="width:87px;" readonly='<%= internetu %>'/> -
							<html:text property="savininkoParasoMenuo" styleClass="inputFixed" maxlength="2" style="width:45px;" readonly='<%= internetu %>'/> -
							<html:text property="savininkoParasoData" styleClass="inputFixed" maxlength="2" style="width:45px;"  readonly='<%= internetu %>'/>
						</div>
					</td>
					<td width="10%" bgcolor="#bbbbbb" class="gvdis">&nbsp;</td>
					<td width="35%">Paraðas</td>
					<td width="10%" bgcolor="#bbbbbb" class="gvdis">&nbsp;</td>
				</tr>
				</table>									
			</td>
		</tr>
		</logic:notPresent>
		</table>
	</td>
</tr>
<% if (internetu == true) {%>
		<tr>
			<td colspan="3">
				Atmetimo prieþastys<logic:present name="error.atmetimoPriezastis"><span class="error">&nbsp; Bûtina nurodyti atmetimo prieþastá.</span></logic:present><br />
				<html:textarea property="atmetimoPriezastys" style="width: 100%; height: 30px;" styleClass="input_select"/>
			</td>
		</tr>
	<% } %>
<tr>
	<td colspan="3" align="center">
		<hr /><input type="hidden" name="mode" value="print">
		<html:submit styleClass="button" value="Iðsaugoti deklaracijà" onclick="changeMode('save');"/>&nbsp;
		<% if (internetu == true) {%>
			<html:submit styleClass="button" value="Atmesti" onclick="changeMode('reject');"/>				
		<%}	%>
		<% if (false == internetu){ %>
		<html:submit styleClass="button" value="Spausdinti deklaracijà" onclick="changeMode('print');"/>
		<% } %>
	</td>
</tr>
<tr>
	<td colspan="3" align="center">
		<hr /><span class="error">*</span> <b>þenklu paþymëtus laukus privaloma uþpildyti</b>
	</td>
</tr>
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
-->
</script>
</logic:empty>

</table>
</html:form>
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> Datos ávedamos formatu <i>"metai-mënuo-diena" (2006-09-01)</i>.
</div>