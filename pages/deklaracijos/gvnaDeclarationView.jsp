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
	<td width="60%">&nbsp;</td>
	<td width="40%">
	
	 
	
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td colspan="3">Gauta:</td>
		</tr>
		<tr>
			<td width="50%">
<%
	GvnaDeklaracija gd = (GvnaDeklaracija)request.getAttribute("gvnaDeclaration");
	String[] c1 = CalendarUtils.getDateFromTimestamp(gd.getGavimoData());
	String[] c2 = null;
	if(null == gd.getAsmuo())
		c2 = CalendarUtils.getDateFromTimestamp(gd.getLaikinasAsmuo().getGimimoData());
	else
		c2 = CalendarUtils.getDateFromTimestamp(gd.getAsmuo().getAsmGimData());
	String[] c3 = CalendarUtils.getDateFromTimestamp(gd.getDokumentoData());
	String[] c4	= CalendarUtils.getDateFromTimestamp(gd.getDokumentoGaliojimas());
	String[] c6	= CalendarUtils.getDateFromTimestamp(gd.getDeklaravimoData());
	String[] c5 = CalendarUtils.getDateFromTimestamp(gd.getDeklaracijaGalioja());
%>

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
			<td width="40%"><bean:write name="gvnaDeclaration" property="istaiga.oficialusPavadinimas" />
			</td>		
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
<br/>
<div class="heading" align="center">PRAÐYMAS ÁTRAUKTI Á GYVENAMOSIOS VIETOS NEDEKLARAVUSIØ ASMENØ APSKAITÀ </div>

<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20%" class="gvdis">
				1. Asmens kodas<br><input type="text" class="inputFixed" maxlength="11" style="width: 220px;" readonly="true" value="<% if(null != gd.getAsmuo()){ %><bean:write name="gvnaDeclaration" property="asmuo.asmKodas" /> <% } %>"/>
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
<% if(null != gd.getAsmuo()){ %>
					<logic:equal name="gvnaDeclaration" property="asmuo.asmLytis" value="V">
						<span class="heading2">Vyras</span>
					</logic:equal>
					<logic:equal name="gvnaDeclaration" property="asmuo.asmLytis" value="M">
						<span class="heading2">Moteris</span>
					</logic:equal>
<%}else{%>
					<logic:equal name="gvnaDeclaration" property="laikinasAsmuo.lytis" value="V">
						<span class="heading2">Vyras</span>
					</logic:equal>
					<logic:equal name="gvnaDeclaration" property="laikinasAsmuo.lytis" value="M">
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
				4. Vardas(-ai)<br><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:present name="gvnaDeclaration" property="asmuo"><bean:write name="gvnaDeclaration" property="asmuo.vardas"/></logic:present><logic:notPresent name="gvnaDeclaration" property="asmuo"><bean:write name="gvnaDeclaration" property="laikinasAsmuo.vardas"/></logic:notPresent>"	/>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" class="gvdis">
				5. Pavardë<br><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:present name="gvnaDeclaration" property="asmuo"><bean:write name="gvnaDeclaration" property="asmuo.pavarde"/></logic:present><logic:notPresent name="gvnaDeclaration" property="asmuo"><bean:write name="gvnaDeclaration" property="laikinasAsmuo.pavarde"/></logic:notPresent>" />
			</td>
		</tr>
		<tr>
			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td style="border-top: none; border-bottom: none;" rowspan="2" width="10%" class="gvdis">6. Kontaktiniai duomenys</td>
						<td width="10%" class="gvdis" align="center">Telefono nr.</td>
						<td width="80%" ><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:notEmpty name="gvnaDeclaration" property="telefonas"><bean:write name="gvnaDeclaration" property="telefonas" /></logic:notEmpty>" /></td>
					</tr>
				   <tr>
				  	<td class="gvdis" align="center">El. paðto adresas:</td>
				  	<td colspan="2"><input type="text" class="inputFixed" maxlength="31" style="width:620px;" readonly="true" value="<logic:notEmpty name="gvnaDeclaration" property="email"><bean:write name="gvnaDeclaration" property="email" /></logic:notEmpty>" /></td>
				  </tr>
		</tr>

		<tr>
			<td  width="20%" class="gvdis">
				7. Pilietybë	
					<span class="heading2">
					<bean:write name="gvnaDeclaration" property="pilietybe.pilietybe" />
					</span>
			</td>

			<td colspan="5" width="80%" class="gvdis">
				8. Asmens dokumentas 
				<br>
				<div align="center">				
					<span class="heading2">
						<logic:present name="gvnaDeclaration" property="dokumentoTipas">
							<bean:write name="gvnaDeclaration" property="dokumentoTipas.pavadinimas" />
						</logic:present>
						<logic:present name="gvnaDeclaration" property="dokumentoRusis">
							<bean:write name="gvnaDeclaration" property="dokumentoRusis" />
						</logic:present>
					</span>
				</div>
			</td>
		</tr> 
		</table>
		
		</td>
	</tr>

	<tr>
		<td>
		<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td colspan="2" class="gvdis">
			Numeris <br/> <input type="text" class="input_select"  style="width:95%;" value="<bean:write name="gvnaDeclaration" property="dokumentoNr" />" readonly="true" />&nbsp;
			</td>
			<td bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td class="gvdis">
				Iðdavimo data <logic:present name="error.asmensDokumentoIsdavimoData"><span class="error">*</span></logic:present>
				<table border="0" cellpadding="0" cellspacing="0" >
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
			<td bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
		</tr>

		<tr>
			<td width="100%" colspan="5" class="gvdis">
					Kas iðdavë <br>
					<input type="text" class="input_select" style="width: 50%;"  value="<bean:write name="gvnaDeclaration" property="dokumentoIsdavejas" />" readonly />
			</td>
		</tr>
	
		<tr>
			<td width="20%" bgcolor="#bbbbbb" class="gvdis">
				&nbsp;
			</td>
			<td width="20%" colspan="2" class="gvdis">
				Jei pateiktas leidimas laikinai<br>apsigyventi Lietuvos Respublikoje 
			</td>
			<td width="30%" class="gvdis">
				 Galiojimo data <logic:present name="error.leidimoGaliojimoData"><span class="error">*</span></logic:present>
				<table border="0" cellpadding="0" cellspacing="0">
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
			<td width="30%" bgcolor="#bbbbbb" class="gvdis">
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
				<td width="40%" class="gvdis" valign="top">
					9. Savivaldybë, kurioje asmuo gyvena<br />
					<span class="heading2"><bean:write name="gvnaDeclaration" property="savivaldybe.terPav" /></span>


					<textarea style="width: 100%;height: 250px;" class="input_select" readonly ><logic:present name="gvnaDeclaration" property="savivaldybePastabos"><bean:write name="gvnaDeclaration" property="savivaldybePastabos"/></logic:present></textarea>

				</td>
				<td width="60%">
					<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td class="gvdis">10. Ankstesnës gyvenamosios vietos adresas<td>
					</tr>

<logic:equal name="gvnaDeclaration" property="ankstesneVietaTipas" value="0">	
					<tr>
						<td class="gvdis">
							<br />
							&nbsp;Ankstesnë deklaruota gyvenamoji vieta:<br />

				<%
					if(null != gd.getAsmuo())
					{
						Address adr = AdresaiDelegator.getInstance().getAsmDeklGvtNr(gd.getAsmuo().getAsmNr(),gd.getId(),request);						
					    if(null != adr && !"Negaliojantis adresas".equals(adr.getAdr())) out.print("<b>"+adr.getAdr()+"</b>"); 
					}
				%>			<br /><br />
						</td>
					</tr>
</logic:equal>	
<logic:equal name="gvnaDeclaration" property="ankstesneVietaTipas" value="2">
					<tr>
						<td class="gvdis">
							<br />

							&nbsp;Kita gyvenamoji vieta:
							
							<br />
							<textarea style="width: 100%; height: 60px;" class="input_select" readonly><logic:present name="gvnaDeclaration" property="ankstesneVietaKita"><bean:write name="gvnaDeclaration" property="ankstesneVietaKita"/></logic:present></textarea><br /><br />

						</td>
					</tr>
</logic:equal>					
<logic:equal name="gvnaDeclaration" property="ankstesneVietaTipas" value="1">						
					<tr>
						<td class="gvdis">
							<br />
					&nbsp;Atvyko ið uþsienio:<br />
						<logic:present name="gvnaDeclaration" property="ankstesneGV.pavadinimas"><span class="heading2"><bean:write name="gvnaDeclaration" property="ankstesneGV.pavadinimas"/></span></logic:present>
							<textarea style="width: 100%; height: 60px;" class="input_select" readonly><logic:present name="gvnaDeclaration" property="ankstesneVietaValstybesPastabos"><bean:write name="gvnaDeclaration" property="ankstesneVietaValstybesPastabos"/></logic:present></textarea>
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
		<td class="gvdis">
			11. Gyvenamosios vietos deklaravimo ástatymo 6 straipsnio dalis (punktas), kuriuo vadovaujantis átraukiama á GVNA apskaità<br />
				<textarea style="width: 100%;height: 70px;" class="input_select" readonly><logic:present name="gvnaDeclaration" property="priezastys"><bean:write name="gvnaDeclaration" property="priezastys"/></logic:present></textarea>
		</td>
	</tr>
	

	<tr>
		<td class="gvdis">
					12. <div align="center">
	<span class="heading2">
	<logic:equal name="gvnaDeclaration" property="pageidaujaPazymos" value="1">				
		Pageidaujama paþymos apie átraukimà á gyvenamosios vietos nedeklaravusiø asmenø apskaità
	</logic:equal>
	<logic:equal name="gvnaDeclaration" property="pageidaujaPazymos" value="0">				
		Nepageidaujama paþymos apie átraukimà á gyvenamosios vietos nedeklaravusiø asmenø apskaità
	</logic:equal>
	</span>
						</div>
		</td>
	</tr>

	<tr>
		<td class="gvdis">
					13. Praðymas pateiktas&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<div align="center"><span class="heading2">
		<logic:equal name="gvnaDeclaration" property="pateike" value="0">&nbsp;&nbsp;Asmeniðkai</logic:equal>
		<logic:equal name="gvnaDeclaration" property="pateike" value="1">&nbsp;&nbsp;Vieno ið tëvø(átëviø)</logic:equal>
		<logic:equal name="gvnaDeclaration" property="pateike" value="2">&nbsp;&nbsp;Globëjo(rûpintojo)</logic:equal>
		<logic:equal name="gvnaDeclaration" property="pateike" value="3">&nbsp;&nbsp;Kito teisëto atstovo</logic:equal></span>
</div>
		</td>
	</tr> 
	<td class="gvdis">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
				<td width="10%"  bgcolor="#bbbbbb" class="gvdis" style="border-top: 0px solid black;">
						&nbsp;
					</td>
				<td width="30%" class="gvdis">

					Data&nbsp;
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
					<td width="40%">
						<input type="text" class="inputFixed" maxlength="4" style="width:82px;" value="<%=c6[0]%>" readonly="true" />
						</td>
						<td wwidth="20" align="center">&nbsp;-</td>
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
					
				<td width="10%"  bgcolor="#bbbbbb" class="gvdis" style="border-top: 0px solid black;">
						&nbsp;
					</td>
					<td width="30%" class="gvdis">
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
					<td width="10%"  bgcolor="#bbbbbb" class="gvdis" style="border-top: 0px solid black;">
						&nbsp;
					</td>
					
				</tr>
			</table>
			</td>
	<tr>
		<td class="gvdis">

			<table border="0" class="form" cellpadding="0" cellspacing="0" width="100%">
			
			<tr>
				<td width="50%" colspan="3" class="gvdis">
					Vardas<br />
		<logic:equal name="gvnaDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;" readonly="true"  />
		</logic:equal>
		<logic:notEqual name="gvnaDeclaration" property="pateike" value="0">
				
					<input type="text" class="input_select" style="width: 300px;" value="<logic:present name="gvnaDeclaration" property="pateikeVardas"><bean:write name="gvnaDeclaration" property="pateikeVardas" /></logic:present>" readonly="true" />
		</logic:notEqual>
				</td>
				<td width="50%" colspan="2" class="gvdis">
					Pavardë<br />
		<logic:equal name="gvnaDeclaration" property="pateike" value="0">
					<input type="text" class="input_select" style="width: 300px;"  readonly="true" />
		</logic:equal>
		<logic:notEqual name="gvnaDeclaration" property="pateike" value="0">
				
					<input type="text" class="input_select" style="width: 300px;" value="<logic:present name="gvnaDeclaration" property="pateikePavarde"><bean:write name="gvnaDeclaration" property="pateikePavarde" /></logic:present>" readonly="true" />
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
						<textarea style="width: 100%;height: 70px;" class="input_select" readonly><logic:present name="gvnaDeclaration" property="pastabos"><bean:write name="gvnaDeclaration" property="pastabos"/></logic:present></textarea>
				</td>
			</tr> 
			
			<tr>
				<td width="10%" bgcolor="#bbbbbb" class="gvdis">
					&nbsp;
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

			<tr>
      <td width="50%" colspan="3" class="gvdis">
       <br>
         <% if ( gd != null && gd.getBusena() == 2 ) {%>
         Atmetimo prieþastys: 
	     <div class="heading2" align="left"><bean:write name="gvnaDeclaration" property="atmetimoPriezastys" /></div>
          <% } %>

       </td>
      <td width="50%" colspan="2" class="gvdis" align="right" >
       <br>
       Deklaracijos numeris:
       <div class="heading2"><bean:write name="gvnaDeclaration" property="regNr" /></div>
      </td>
     </tr>

         </table>
       </td>
	</tr>

</table>