<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.utils.*,
				 com.algoritmusistemos.gvdis.web.delegators.*,
				 com.algoritmusistemos.gvdis.web.DTO.*" %>

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
	<td>
<%
	IsvykimoDeklaracija deklaracija = (IsvykimoDeklaracija)request.getAttribute("outDeclaration");
	String[] c1 = CalendarUtils.getDateFromTimestamp(deklaracija.getGavimoData());
	String[] c2 = null;	
	if(null == deklaracija.getAsmuo())
		c2 = CalendarUtils.getDateFromTimestamp(deklaracija.getLaikinasAsmuo().getGimimoData());
	else
		c2 = CalendarUtils.getDateFromTimestamp(deklaracija.getAsmuo().getAsmGimData());
	String[] c3 = CalendarUtils.getDateFromTimestamp(deklaracija.getDokumentoData());
	String[] c4	= CalendarUtils.getDateFromTimestamp(deklaracija.getDokumentoGaliojimas());
	String[] c6	= CalendarUtils.getDateFromTimestamp(deklaracija.getDeklaravimoData());
	String[] c7	= CalendarUtils.getDateFromTimestamp(deklaracija.getIsvykimoData());
%>	


	</td>
	<td width="60%">
    	&nbsp;
	</td>
	<td width="40%">
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td colspan="3">Gauta:</td>
		</tr>
		<tr>
			<td width="50%">

			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="40%">
					<input type="text" name="code" class="inputFixed" maxlength="4" style="width: 80px;" readonly value="<%=c1[0]%>" />
				</td>
				<td width="20" align="center">&nbsp;-</td>
				<td width="20%">
					<input type="text" name="code" class="inputFixed" maxlength="2" style="width: 40px;" readonly value="<%=c1[1]%>"/>
				</td>
				<td width="20" align="center">&nbsp;-</td>
				<td width="20%">
					<input type="text" name="code" class="inputFixed" maxlength="2" style="width: 40px;" readonly value="<%=c1[2]%>"/>
				</td>
				</tr>
			</table>





			</td>
			<td width="10%">&nbsp;</td>
			<td width="40%"><bean:write name="outDeclaration" property="istaiga.oficialusPavadinimas" /></td>		
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
<div class="heading" align="center">GYVENAMOSIOS VIETOS DEKLARACIJA, PILDOMA ASMENIUI IÐVYKSTANT IÐ LIETUVOS RESPUBLIKOS ILGESNIAM NEI ÐEÐIØ MËNESIØ LAIKOTARPIUI</div>
<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
		<table border="1" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20%" class="gvdis">
				1. Asmens kodas<br><input type="text" class="inputFixed" maxlength="11" style="width: 220px;" readonly="true" value="<% if(null != deklaracija.getAsmuo()){ %><bean:write name="outDeclaration" property="asmuo.asmKodas" /> <% } %>"/>
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

			<td width="5%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;&nbsp;&nbsp;
			</td>
			<td width="25%" class="gvdis">
				3. Lytis<br>&nbsp;&nbsp;
<% if(null != deklaracija.getAsmuo()){ %>
					<logic:equal name="outDeclaration" property="asmuo.asmLytis" value="V">
						<span class="heading2">Vyras</span>
					</logic:equal>
					<logic:equal name="outDeclaration" property="asmuo.asmLytis" value="M">
						<span class="heading2">Moteris</span>
					</logic:equal>
<%}else{%>
					<logic:equal name="outDeclaration" property="laikinasAsmuo.lytis" value="V">
						<span class="heading2">Vyras</span>
					</logic:equal>
					<logic:equal name="outDeclaration" property="laikinasAsmuo.lytis" value="M">
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
				
				4. Vardas(-ai)<br><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:present name="outDeclaration" property="asmuo"><bean:write name="outDeclaration" property="asmuo.vardas"/></logic:present><logic:notPresent name="outDeclaration" property="asmuo"><bean:write name="outDeclaration" property="laikinasAsmuo.vardas"/></logic:notPresent>"	/>
				
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				5. Pavardë<br><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:present name="outDeclaration" property="asmuo"><bean:write name="outDeclaration" property="asmuo.pavarde"/></logic:present><logic:notPresent name="outDeclaration" property="asmuo"><bean:write name="outDeclaration" property="laikinasAsmuo.pavarde"/></logic:notPresent>" />
			</td>
		</tr>
		<tr>
			
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td style="border-top: none; border-bottom: none;" rowspan="2" width="10%" class="gvdis">6. Kontaktiniai duomenys</td>
						<td width="10%" class="gvdis" align="center">Telefono nr.</td>
						<td width="80%" ><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:notEmpty name="outDeclaration" property="telefonas"><bean:write name="outDeclaration" property="telefonas" /></logic:notEmpty>" /></td>
					</tr>
				   <tr>
				  	<td class="gvdis" align="center">El. paðto adresas:</td>
				  	<td colspan="2"><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:notEmpty name="outDeclaration" property="email"><bean:write name="outDeclaration" property="email" /></logic:notEmpty>" /></td>
				  </tr>
			
		</tr>


		<tr>
			<td  width="20%" class="gvdis">
				7. Pilietybë	
					<span class="heading2"><bean:write name="outDeclaration" property="pilietybe.pilietybe" />
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
						<logic:present name="outDeclaration" property="dokumentoTipas">
							<bean:write name="outDeclaration" property="dokumentoTipas.pavadinimas" />
						</logic:present>
						<logic:present name="outDeclaration" property="dokumentoRusis">
							<bean:write name="outDeclaration" property="dokumentoRusis" />
						</logic:present>
					</span>
				</div><br/>
			</td>
		</tr>
		<tr>
			<td width="32%" colspan="2" class="gvdis">
			Numeris  <br>
					<input type="text" class="input_select"  style="width:95%;" value="<bean:write name="outDeclaration" property="dokumentoNr" />" readonly="true" />&nbsp;
					
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
					<input type="text" class="input_select" style="width:50%;"  value="<bean:write name="outDeclaration" property="dokumentoIsdavejas" />" readonly />
			</td>
		</tr>

		<tr>
			<td bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td colspan="2" class="gvdis">
				Jei pateiktas leidimas laikinai<br>apsigyventi Lietuvos Respublikoje 
			</td>
			<td  class="gvdis">
				 Galiojimo data 
				<table border="0" cellpadding="0" cellspacing="0"">
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
			


		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="50%" valign="top">
				<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td class="gvdis" colspan="2">&nbsp;9. Gyvenamoji vieta, ið kurios iðvykstama
					
			   </td>
				</tr>
				<tr>
					<td class="gvdis" colspan="2">
						<br />
					 &nbsp;Ankstesnë deklaruota gyvenamoji vieta:<br />
			<logic:equal name="outDeclaration" property="ankstesneVietaTipas" value="0"> 
				<%
					if(null != deklaracija.getAsmuo())
					{
						Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(deklaracija.getAsmuo().getAsmNr(),deklaracija.getId(),request);						
						if(null != adr)
						{
							if(!"Negaliojantis adresas".equals(adr.getAdr()) && null != adr.getAdr()) {
								out.print("<b>"+adr.getAdr()+"</b>");
							}
						}
					}
				%>
			</logic:equal> 
			<logic:equal name="outDeclaration" property="ankstesneVietaTipas" value="2"> 
				<%
					if(null != deklaracija.getAsmuo())
					{
						Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(deklaracija.getAsmuo().getAsmNr(),deklaracija.getId(),request);						
						if(null != adr)
						{
							out.print("<b>"+adr.getAdr()+"</b>");
						}
					}
				%>
			</logic:equal> 
						<br /><br />
					</td>
				</tr>
				<tr>
					<td class="gvdis" colspan="2">
						<br />
						&nbsp;Kita gyvenamoji vieta:
						<br />
							<textarea style="width: 100%; height: 60px;" class="input_select" readonly><logic:equal name="outDeclaration" property="ankstesneVietaTipas" value="2"><bean:write name="outDeclaration" property="ankstesneVietaKita"/></logic:equal></textarea>
						
						<br /><br />
					</td>
				</tr>

				<tr>
					<td nowrap class="gvdis">
						Iðvykimo data 
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
						<td width="40%">
							<input type="text" class="inputFixed" maxlength="4" style="width:82px;" readonly value="<%=c7[0]%>"/>
						</td>
						<td width="20" align="center">-</td>
						<td width="20%">
							<input type="text" class="inputFixed" maxlength="2" style="width:40px;" readonly value="<%=c7[1]%>"/>
						</td>
						<td width="20" align="center">-</td>
						<td width="20%">
							<input type="text" class="inputFixed" maxlength="2" style="width:40px;" readonly value="<%=c7[2]%>"/>
						</td>
						</tr>
						</table> 
					</td>
					<td width="40%" bgcolor="#bbbbbb" class="gvdis" style="border-top: 0px solid black;">&nbsp;</td>
				</tr>
				</table>
			</td>
			<td width="50%" class="gvdis" valign="top">
				10. Numatoma gyvenamoji vieta uþsienyje<br>
				<span class="heading2"><bean:write name="outDeclaration" property="ankstesneGV.pavadinimas" /></span>
					<br>
				<textarea style="width: 100%; height: 60px;" class="input_select" readonly><bean:write name="outDeclaration" property="ankstesneVietaValstybesPastabos"/></textarea>

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
					11. Deklaracija pateikta&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />
<div align="center"><span class="heading2">
		<logic:equal name="outDeclaration" property="pateike" value="0">&nbsp;&nbsp;Asmeniðkai</logic:equal>
		<logic:equal name="outDeclaration" property="pateike" value="1">&nbsp;&nbsp;Vieno ið tëvø(átëviø)</logic:equal>
		<logic:equal name="outDeclaration" property="pateike" value="2">&nbsp;&nbsp;Globëjo(rûpintojo)</logic:equal>
		<logic:equal name="outDeclaration" property="pateike" value="3">&nbsp;&nbsp;Kito teisëto atstovo</logic:equal></span>
</div><br/>
				</td>
			</tr>


			<tr>
				<td width="50%" colspan="3" class="gvdis">
					Vardas<br />
		<logic:equal name="outDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;" readonly="true"  />
		</logic:equal>
		<logic:notEqual name="outDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;" value="<logic:present name="outDeclaration" property="pateikeVardas"><bean:write name="outDeclaration" property="pateikeVardas" /></logic:present>"  readonly="true" />
		</logic:notEqual>
				</td>
				<td width="50%" colspan="2" class="gvdis">
					Pavardë<br />
		<logic:equal name="outDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;"  readonly="true" />
		</logic:equal>
		<logic:notEqual name="outDeclaration" property="pateike" value="0">				
					<input type="text" class="input_select" style="width: 300px;" value="<logic:present name="outDeclaration" property="pateikePavarde"><bean:write name="outDeclaration" property="pateikePavarde" /></logic:present>" readonly="true" />
		</logic:notEqual>

				</td>
			</tr>
			<tr>
				<td width="100%" colspan="5" class="gvdis">
					<br>
				</td>
			</tr>	
			<tr>
				<td class="gvdis" colspan="5">
					Pastabos<br />
						<textarea style="width: 100%;height: 70px;" class="input_select" readonly><logic:present name="outDeclaration" property="pastabos"><bean:write name="outDeclaration" property="pastabos"/></logic:present></textarea>
				</td>
			</tr>


			<tr>
				<td bgcolor="#bbbbbb" class="gvdis" width="10%">
					&nbsp;
				</td>
				<td class="gvdis" width="30%">
					Data&nbsp;
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
						<input type="text" class="inputFixed" maxlength="2" style="width:40px;" value="<%=c6[2]%>" readonly="true" />
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

			<%--<tr>
				<td width="15%" colspan="5" class="gvdis">
					12.
				
						<div align="center">
	<span class="heading2">
	<logic:equal name="outDeclaration" property="pageidaujaPazymos" value="1">				
		Pageidauju gauti dokumentà, patvirtinantá deklaruotà gyvenamàjà vietà
	</logic:equal>
	<logic:equal name="outDeclaration" property="pageidaujaPazymos" value="0">				
		Nepageidauju gauti dokumentà, patvirtinantá deklaruotà gyvenamàjà vietà
	</logic:equal>
	</span>
						</div>
				</td>
			</tr>--%>

		   
     <tr>
      <td width="50%" colspan="3" class="gvdis">
       <br>
         <% if ( deklaracija != null && deklaracija.getBusena() == 2 ) {%>
         Atmetimo prieþastys: 
	     <div class="heading2" align="left"><bean:write name="outDeclaration" property="atmetimoPriezastys" /></div>
         <% } %>

       </td>
      <td width="50%" colspan="2" class="gvdis" align="right" >
       <br>
       Deklaracijos numeris:
       <div class="heading2"><bean:write name="outDeclaration" property="regNr" /></div>
      </td>
     </tr>

         </table>
       </td>
	</tr>

</table>