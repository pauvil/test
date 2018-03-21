<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.utils.*,
				 com.algoritmusistemos.gvdis.web.delegators.*,
				 com.algoritmusistemos.gvdis.web.DTO.* " %>


<script language="javascript" type="text/javascript" src="js/popcalendar.js"></script>

<style type="text/css">

td.gvdis {
	border: 1px solid black;
}
td.gvdis_sp {
	border-left: 4px solid black;
}
</style>
<br/>
<br/>
<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td width="60%">&nbsp;</td>
	<td width="40%">
	
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td colspan="3">Gauta:</td>
		</tr>
		<tr>
			<td width="50%">
<%
	AtvykimoDeklaracija ad = (AtvykimoDeklaracija)request.getAttribute("inDeclaration");

	String[] c1 = CalendarUtils.getDateFromTimestamp(ad.getGavimoData());

	String[] c2 = null;
	if(null == ad.getAsmuo())
		c2 = CalendarUtils.getDateFromTimestamp(ad.getLaikinasAsmuo().getGimimoData());
	else
		c2 = CalendarUtils.getDateFromTimestamp(ad.getAsmuo().getAsmGimData());
	String[] c3 = CalendarUtils.getDateFromTimestamp(ad.getDokumentoData());
	String[] c4	= CalendarUtils.getDateFromTimestamp(ad.getDokumentoGaliojimas());
	String[] c5 = CalendarUtils.getDateFromTimestamp(ad.getDeklaracijaGalioja());
	String[] c6	= CalendarUtils.getDateFromTimestamp(ad.getDeklaravimoData());
	String[] c7	= CalendarUtils.getDateFromTimestamp(ad.getSavininkoParasoData());
	String[] c8	= CalendarUtils.getDateFromTimestamp(ad.getAtvykimoData());
	String c12 = "";
	String c13 = "";
	String c14 = "";
	String c15 = "";
	if (null != ad.getUnikalusPastatoNr()){
		String uniqHouseCode = ad.getUnikalusPastatoNr();
		int startBruksniukas = uniqHouseCode.indexOf("-");
	 	c12 = uniqHouseCode.substring(0,startBruksniukas);
	 	int pabaigaBruksniukas = uniqHouseCode.indexOf("-", startBruksniukas + 1);
	 	c13 = uniqHouseCode.substring(startBruksniukas + 1,pabaigaBruksniukas);
	 	startBruksniukas = pabaigaBruksniukas  ;
	 	pabaigaBruksniukas = uniqHouseCode.indexOf(":", startBruksniukas + 1);	 	
	 	if(pabaigaBruksniukas < 0)
	 		c14 = uniqHouseCode.substring(startBruksniukas +1,uniqHouseCode.length());
	 	else
			c14 = uniqHouseCode.substring(startBruksniukas + 1, pabaigaBruksniukas);
	 	startBruksniukas = pabaigaBruksniukas;
	 	pabaigaBruksniukas = uniqHouseCode.indexOf(":");	
	 	if(pabaigaBruksniukas > 0)
			c15 = uniqHouseCode.substring(startBruksniukas + 1, uniqHouseCode.length());
	}
%>
			<table border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<td width="40%">
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" readonly="true" value="<%=c1[0]%>" />
				</td>
				<td width="20" align="center">-</td>
				<td>
					<input type="text" class="inputFixed" maxlength="4" style="width:40px;" readonly="true" value="<%=c1[1]%>" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="4" style="width:40px;" readonly="true" value="<%=c1[2]%>" />
				</td>
			</tr>	
			</table>
			
			</td>
			<td width="10%">&nbsp;</td>
			<td width="40%"><bean:write name="inDeclaration" property="istaiga.oficialusPavadinimas" /></td>		
		</tr>
		<tr>
			<td width="50%"><hr></td>
			<td width="10%">&nbsp;</td>
			<td width="40%"><hr></td>		
		</tr>		
		</table>
	</td>
</tr>
</table>
<br/>
<div class="heading" align="center">GYVENAMOSIOS VIETOS DEKLARACIJA, PILDOMA ASMENIUI PAKEITUS GYVENAMÀJÀ VIETÀ LIETUVOS RESPUBLIKOJE AR ATVYKUS GYVENTI Á LIETUVOS RESPUBLIKÀ</div>
<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20%" class="gvdis">
				1. Asmens kodas<br>
				<input type="text" class="inputFixed" maxlength="11" style="width: 220px;" readonly="true" value="<% if(null != ad.getAsmuo()){ %><bean:write name="inDeclaration" property="asmuo.asmKodas" /> <% } %>"/>
			</td>
			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>

			<td width="25%" class="gvdis">
				2. Gimimo data
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td width="40%">
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" readonly="true" value="<%=c2[0]%>" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width:40px;" readonly="true" value="<%=c2[1]%>" />				
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width:40px;" readonly="true" value="<%=c2[2]%>" />				
				</td>
				</tr>
				</table>

			</td>

			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				3. Lytis<br>&nbsp;&nbsp;
<% if(null != ad.getAsmuo()){ %>
					<logic:equal name="inDeclaration" property="asmuo.asmLytis" value="V">
						<span class="heading2">Vyras</span>
					</logic:equal>
					<logic:equal name="inDeclaration" property="asmuo.asmLytis" value="M">
						<span class="heading2">Moteris</span>
					</logic:equal>
<%}else{%>
					<logic:equal name="inDeclaration" property="laikinasAsmuo.lytis" value="V">
						<span class="heading2">Vyras</span>
					</logic:equal>
					<logic:equal name="inDeclaration" property="laikinasAsmuo.lytis" value="M">
						<span class="heading2">Moteris</span>
					</logic:equal>			
<%}%>	
			</td>
			<td rowspan="4" width="20%" align="center" valign="middle" class="gvdis">
				PASTABA. Jeigu vardai<br>ar pavardë sudaro<br>daugiau negu 31 þenklà,<br>reikia áraðyti vienà vardà<br>
				ar vienà dvigubos<br>pavardës dalá.
			</td>
		</tr>
		
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				
				4. Vardas(-ai)<br>
				<input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:present name="inDeclaration" property="asmuo"><bean:write name="inDeclaration" property="asmuo.vardas"/></logic:present><logic:notPresent name="inDeclaration" property="asmuo"><bean:write name="inDeclaration" property="laikinasAsmuo.vardas"/></logic:notPresent>"	/>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				5. Pavardë<br>
				<input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:present name="inDeclaration" property="asmuo"><bean:write name="inDeclaration" property="asmuo.pavarde"/></logic:present><logic:notPresent name="inDeclaration" property="asmuo"><bean:write name="inDeclaration" property="laikinasAsmuo.pavarde"/></logic:notPresent>" />
			</td>
		</tr>
		<tr>
			
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td style="border-top: none; border-bottom: none;" rowspan="2" width="10%" class="gvdis">6. Kontaktiniai duomenys</td>
						<td width="10%" class="gvdis" align="center">Telefono nr.</td>
						<td width="80%" ><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:notEmpty name="inDeclaration" property="telefonas"><bean:write name="inDeclaration" property="telefonas" /></logic:notEmpty>" /></td>
					</tr>
				   <tr>
				  	<td class="gvdis" align="center">El. paðto adresas:</td>
				  	<td colspan="2"><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:notEmpty name="inDeclaration" property="email"><bean:write name="inDeclaration" property="email" /></logic:notEmpty>" /></td>
				  </tr>
				
			
		</tr>
		<tr>
			<td  width="20%" class="gvdis">
				7. Pilietybë	
					<span class="heading2">
					<bean:write name="inDeclaration" property="pilietybe.pilietybe" />
					</span>
			</td>
			<td colspan="4" width="30%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr> 
		</table>
		</td>
	</tr>
	<tr>
		<td>



		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		
		<tr>
			<td width="20%" colspan="5" align="left" class="gvdis">
				8. Asmens dokumentas 
				<br>
				<div align="center">	
					<span class="heading2">
						<logic:present name="inDeclaration" property="dokumentoTipas">
							<bean:write name="inDeclaration" property="dokumentoTipas.pavadinimas" />
						</logic:present>
						<logic:present name="inDeclaration" property="dokumentoRusis">
							<bean:write name="inDeclaration" property="dokumentoRusis" />
						</logic:present>
					</span>
				</div><br/>
			</td>
		</tr>
		<tr>
			<td width="32%" colspan="2" class="gvdis">
			Numeris  <br>
					<input type="text" class="input_select"  style="width:95%;" value="<bean:write name="inDeclaration" property="dokumentoNr" />" readonly="true" />&nbsp;
					
			</td>
			<td width="10%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td width="28%" class="gvdis">
				Iðdavimo data 
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td width="40%">
					<input type="text" class="inputFixed" maxlength="4" style="width: 82px;" value="<%=c3[0]%>" readonly="true" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width: 40px;" value="<%=c3[1]%>" readonly="true" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width: 40px;" value="<%=c3[2]%>" readonly="true" />
				</td>
				</tr>
				</table>



			</td>
			<td width="30%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="100%" colspan="5" class="gvdis">
					Kas iðdavë   <br>
					<input type="text" class="input_select" style="width:50%;"  value="<bean:write name="inDeclaration" property="dokumentoIsdavejas" />" readonly />
			</td>
		</tr>
		<tr>
			<td bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td width="20%" colspan="2" class="gvdis">
				Jei pateiktas leidimas laikinai<br>apsigyventi Lietuvos Respublikoje 
			</td>
			<td  class="gvdis">
				 Galiojimo data 
				<table border="0" cellpadding="0" cellspacing="0" >
				<tr>
				<td width="40%">
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c4[0]%>" readonly="true" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c4[1]%>" readonly="true" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c4[2]%>" readonly="true" />
				</td>
				</tr>
				</table>

			</td>
			<td bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>

		</tr>




		</table>

		</td>
	</tr>
	<tr>
		<td>
			



<table class="form" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td valign="top">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="50%" class="gvdis" valign="top">
				&nbsp;9. Deklaruojama gyvenamoji vieta 
			</td>
		</tr>



		<tr>
			<td width="50%" class="gvdis" valign="top" height="174px">
			<br />
			<span class="heading2" id="addressString">
				<%
				
				String addressString = "";
					if(null != ad.getAsmuo())
						addressString = AdresaiDelegator.getInstance().getAsmAddress2(ad.getAsmuo().getAsmNr(),null == ad.getGyvenamojiVieta()?0:ad.getGyvenamojiVieta().getGvtNr(),request);
					else
					if(null != ad.getLaikinasAsmuo())
						if (ad.getTmpGvtAtvNr().intValue() != 0) {
							addressString = AdresaiDelegator.getInstance().getAdresoEilute(ad.getTmpGvtAtvNr().intValue(),"T", request, "");	
						}
						else if (ad.getTmpGvtAdvNr().intValue() != 0) {
							addressString = AdresaiDelegator.getInstance().getAdresoEilute(ad.getTmpGvtAdvNr().intValue(),"A", request, String.valueOf(ad.getTmpGvtKampoNr()));
						}						
					
					if (addressString == null){						
						if (ad.getTmpGvtAtvNr().intValue() != 0) {
							addressString = AdresaiDelegator.getInstance().getAdresoEilute(ad.getTmpGvtAtvNr().intValue(),"T", request, "");	
						}
						else if (ad.getTmpGvtAdvNr().intValue() != 0) {
							addressString = AdresaiDelegator.getInstance().getAdresoEilute(ad.getTmpGvtAdvNr().intValue(),"A", request, String.valueOf(ad.getTmpGvtKampoNr()));
						}
																
					}
					if(null != addressString)out.print(addressString);
				%>
			 </span>

			<div align="right">
				<br/><br/>
			</div>

			</td>
		</tr>


			<tr> 
			<td width="50%">

			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="50%" class="gvdis">
						Deklaracija galioja iki:
						<table border="0" cellpadding="0" cellspacing="0" >
						<tr>
						<td width="40%">
							<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c5[0]%>" readonly="true" />
						</td>
						<td width="20" align="center">-</td>
						<td width="20%">
							<input type="text" class="inputFixed" maxlength="4" style="width:40px;" value="<%=c5[1]%>" readonly="true" />						
						</td>
						<td width="20" align="center">-</td>
						<td width="20%">
							<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c5[2]%>" readonly="true" />						
						</td>
						</tr>
						</table>
					</td>
					<td width="50%" colspan="3" bgcolor="#bbbbbb" class="gvdis" style="border-top: 0px solid black;">
						&nbsp;
					</td>
				</tr>
			</table>

			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
				<!-- 
					<td class="gvdis">
						Kita informacija:
						<textarea style="width: 100%; height: 60px;" class="input_select"  readonly="true"><logic:present name="inDeclaration" property="pastabos"><bean:write name="inDeclaration" property="pastabos" /></logic:present></textarea>
					</td>
				-->
				</tr>
			</table>
							</td>
						</tr>									
		</table>
	</td>
	
	<td width="50%" valign="top">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
		<tr>
			<td class="gvdis">10. Ankstesnës gyvenamosios vietos adresas

						
			<td>
		</tr>
<logic:equal name="inDeclaration" property="ankstesneVietaTipas" value="0">		
		<tr>
			<td class="gvdis">
				<br />
				&nbsp;Ankstesnë deklaruota gyvenamoji vieta:<br />
				
				<%
					if (null != ad.getAsmuo()){
						Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(ad.getAsmuo().getAsmNr(),ad.getId(),request);
						if (adr != null){
							out.print("<b>"+adr.getAdr()+"</b>");
						}
					} 
				%>

				<br /><br />
			</td>
		</tr>
</logic:equal>
				
<logic:equal name="inDeclaration" property="ankstesneVietaTipas" value="2">						
		<tr>
			<td class="gvdis">
				<br />
				&nbsp;Kita gyvenamoji vieta:
				<br />
				<textarea  style="width: 100%; height: 60px;" class="input_select"  readonly="true" ><bean:write name="inDeclaration" property="ankstesneVietaKita" /></textarea><br /><br />
			</td>
		</tr>
</logic:equal>		
		
<logic:equal name="inDeclaration" property="ankstesneVietaTipas" value="1">		
		<tr>
			<td class="gvdis">
				<br />
				&nbsp;Atvyko ið uþsienio:<br />
				<bean:write name="inDeclaration" property="ankstesneGV.pavadinimas" />
				<textarea style="width: 100%; height: 60px;" class="input_select"  readonly="true"><logic:present name="inDeclaration" property="ankstesneVietaValstybesPastabos"><bean:write name="inDeclaration" property="ankstesneVietaValstybesPastabos" /></logic:present></textarea>
			</td>
		</tr>
</logic:equal>		
		</table>
	</td>	
</tr>
</table>

		</td>
	</tr>
	<tr>
		<td  class="gvdis" colspan="5">		
				11. Unikalaus pastato (patalpos) numeris:&nbsp;
				<div style="white-space: nowrap;">
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c12%>" readonly="true">-
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c13%>" readonly="true">-
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c14%>" readonly="true">:
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c15%>" readonly="true">  
					
				</div>
				
			</td>
	</tr>
	 <tr>
		<td>
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td colspan="5" class="gvdis">
					12. Deklaracija pateikta&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />
<div align="center"><span class="heading2">
		<logic:equal name="inDeclaration" property="pateike" value="0">&nbsp;&nbsp;Asmeniðkai</logic:equal>
		<logic:equal name="inDeclaration" property="pateike" value="1">&nbsp;&nbsp;Vieno ið tëvø(átëviø)</logic:equal>
		<logic:equal name="inDeclaration" property="pateike" value="2">&nbsp;&nbsp;Globëjo(rûpintojo)</logic:equal>
		<logic:equal name="inDeclaration" property="pateike" value="3">&nbsp;&nbsp;Kito teisëto atstovo</logic:equal></span>
</div><br/>
				</td>
			</tr>
			<tr>
				<td width="50%" colspan="3" class="gvdis">
					Vardas<br />
		<logic:equal name="inDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;" readonly="true"  />
		</logic:equal>
		<logic:notEqual name="inDeclaration" property="pateike" value="0">
				
					<input type="text" class="input_select" style="width: 300px;" value="<logic:present name="inDeclaration" property="pateikeVardas"><bean:write name="inDeclaration" property="pateikeVardas" /></logic:present>" readonly="true" />
		</logic:notEqual>
				</td>
				<td width="50%" colspan="2" class="gvdis">
					Pavardë<br />
		<logic:equal name="inDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;"  readonly="true" />
		</logic:equal>
		<logic:notEqual name="inDeclaration" property="pateike" value="0">
				
					<input type="text" class="input_select" style="width: 300px;" value="<logic:present name="inDeclaration" property="pateikePavarde"><bean:write name="inDeclaration" property="pateikePavarde" /></logic:present>" readonly="true" />
		</logic:notEqual>

				</td>
			</tr>
			<tr>
				<td bgcolor="#bbbbbb" class="gvdis" width="10%">
					&nbsp;
				</td>
				<td class="gvdis" width="30%">
					Deklaracijos pateikimo data&nbsp;
					<table border="0" cellpadding="0" cellspacing="0">
					<tr>
					<td width="40%">
						<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c6[0]%>" readonly="true" />
						</td>
						<td width="20" align="center">&nbsp;-</td>
						<td width="20%">
						<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c6[1]%>" readonly="true" />
						</td>
						<td width="20" align="center">&nbsp;-</td>
						<td width="20%">
						<input type="text" class="inputFixed" maxlength="4" style="width:40px;" value="<%=c6[2]%>" readonly="true" />
						</td>
						</tr>
						</table>
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
					
				
						<div align="center">
	<span class="heading2">
	<logic:equal name="inDeclaration" property="pageidaujaPazymos" value="1">				
		Pageidauju gauti dokumentà, patvirtinantá deklaruotà gyvenamàjà vietà
	</logic:equal>
	<logic:equal name="inDeclaration" property="pageidaujaPazymos" value="0">				
		Nepageidauju gauti dokumentà, patvirtinantá deklaruotà gyvenamàjà vietà
	</logic:equal>
	</span>
						</div>
				</td>
			</tr>

			<tr>
				<td width="15%" colspan="5" class="gvdis">
					 Asmens, deklaruojanèio gyvenamàjà vietà, ryðys su deklaruojama gyvenamàja vieta
						<div align="center">
	<span class="heading2">
							<logic:equal name="inDeclaration" property="rysysSuGv" value="0">&nbsp;&nbsp;Savininkas</logic:equal>
							<logic:equal name="inDeclaration" property="rysysSuGv" value="1">&nbsp;&nbsp;Nuomininkas</logic:equal>
							<logic:equal name="inDeclaration" property="rysysSuGv" value="2">&nbsp;&nbsp;Subnuomininkas</logic:equal>
							<logic:equal name="inDeclaration" property="rysysSuGv" value="3">&nbsp;&nbsp;Kita</logic:equal>
	</span>						
						</div>
				</td>
			</tr>
			<tr>
				<td width="15%" colspan="5" class="gvdis">
					13. Gyvenamosios patalpos savininkas <logic:present name="error.savininkoTipas"><span class="error">*</span></logic:present>
				
						<div align="center">
	<span class="heading2">
<!-- 
							<logic:equal name="inDeclaration" property="savininkoTipas" value="0">&nbsp;&nbsp;Juridinis asmuo</logic:equal>
							<logic:equal name="inDeclaration" property="savininkoTipas" value="1">&nbsp;&nbsp;Fizinis asmuo</logic:equal> 
-->
							Fizinis ar juridinis asmuo
	</span>
						</div>
				</td>
			</tr>



			<tr>
				<td width="15%" colspan="5" class="gvdis">

					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td class="gvdis" rowspan="4" width="20%">14. Gyvenamosios patalpos savininko ar jo ágalioto asmens sutikimas</td>
							<td class="gvdis" width="40%">Vardas, pavardë (juridinio asmens pavadinimas) <br />
							
								<textarea style="width: 300px;height: 30px;" class="input_select" readonly="true"><logic:present name="inDeclaration" property="faVardasPavarde"><bean:write name="inDeclaration" property="faVardasPavarde" /></logic:present></textarea>
								<br /><br />
							</td>
							<td class="gvdis" width="40%">
								Asmens kodas (ámonës kodas) 
								<br />
								<textarea style="width: 300px;height: 30px;" class="input_select" readonly="true"><logic:present name="inDeclaration" property="faKodas"><bean:write name="inDeclaration" property="faKodas" /></logic:present></textarea><br /><br />
							</td>
						</tr>
						<tr>

						</tr>
						<tr>


						</tr>						
						<tr>
							<td colspan="2">
							
								<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td width="10%" bgcolor="#bbbbbb" class="gvdis">&nbsp;</td>
									<td width="35%">
									
				Savininko paraðo data 
				<table border="0" cellpadding="0" cellspacing="0" >
				<tr>
				<td width="40%">
					<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c7[0]%>" readonly="true" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c7[1]%>" readonly="true" />
				</td>
				<td width="20" align="center">-</td>
				<td width="20%">
					<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c7[2]%>" readonly="true" />
				</td>
				</tr>
				</table>									
									
									</td>
									<td width="10%" bgcolor="#bbbbbb" class="gvdis">&nbsp;</td>
									<td width="35%">Paraðas</td>
									<td width="10%" bgcolor="#bbbbbb" class="gvdis">&nbsp;</td>
								</tr>
								</table>
							
							</td>
						</tr>
	<tr>
      <td colspan="2" class="gvdis">
       <br>
         <% if ( ad != null && ad.getBusena() == 2 ) {%>
         Atmetimo prieþastys: 
	     <div class="heading2" align="left"><bean:write name="inDeclaration" property="atmetimoPriezastys" /></div>
          <% } %>

       </td>
      <td colspan="1" class="gvdis" align="right" >
       <br>
       Deklaracijos numeris:
       <div class="heading2"><bean:write name="inDeclaration" property="regNr" /></div>
      </td>
    </tr>

         </table>
       </td>
	</tr>

</table>