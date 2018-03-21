<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/as" prefix="as" %>
<%@ page import="java.util.*,
				 com.algoritmusistemos.gvdis.web.persistence.*,
				 com.algoritmusistemos.gvdis.web.utils.*" %>
<% Set roles = (Set)session.getAttribute("userRoles"); 
    Ordering ordering = (Ordering)session.getAttribute("not_completed_declarations_ordering");
%>
<div class="heading">Nebaigtos ávesti
<logic:equal parameter="deklTipas" value="atv">atvykimo</logic:equal>
<logic:equal parameter="deklTipas" value="isv">iðvykimo</logic:equal>
 deklaracijos</div>

<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%"><%= ordering.printOrdering("gavimoData", "Data") %></th>
    <th width="10%">Asmens Kodas</th>
    <th width="25%">Asmuo</th>
	<th width="15%">Tipas</th>    
	<th width="15%">Ástaiga</th>
	<th width="15%">Vartotojas (ávd. / red.)</th>	
    <th width="10%">Veiksmai</th>
</tr>

<%
		List l = (List)request.getAttribute("notCompletedDeklarations");
	    for(int i=0;i<l.size();i++)
	    {
			Deklaracija d = (Deklaracija)l.get(i);
			String[] s = CalendarUtils.getDateFromTimestamp(d.getGavimoData());
%>

	<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
		<td align="center"><%=s[0]%>-<%=s[1]%>-<%=s[2]%></td>
		<td align="center">

<%
			if(null != d.getAsmuo()){
				out.print(d.getAsmuo().getAsmKodas());
			}
%>
		&nbsp;
		</td>
		<td align="center">
<%
			if(null != d.getAsmuo())
			{
				Asmuo asm = new Asmuo();
				asm.setVardasPavarde(d.getAsmenvardis());
				if (null == asm.getPavarde()) {
					asm.setPavarde(d.getAsmuo().getPavarde());
					asm.setVardas(d.getAsmuo().getVardas());
				}
				out.print(asm.getVardas()+" "+asm.getPavarde());
			}
			else
			if(null != d.getLaikinasAsmuo())
			{
				out.print(d.getLaikinasAsmuo().getVardas()+" "+d.getLaikinasAsmuo().getPavarde());
			}
%>
		&nbsp;
		</td> 
		<td align="center">
<%
	    	if(d instanceof AtvykimoDeklaracija){%>
			    Atvykimo deklaracija	
	    	<%}
	    	else
	    	if(d instanceof IsvykimoDeklaracija){%>
	    		Iðvykimo deklaracija
	    	<% }
	    	else	    	
	    	if(d instanceof GvnaDeklaracija){ %>
	    		   GVNA deklaracija
	    	<% } %>
		&nbsp;
		</td>
		<td align="center">
			<%
				out.println(d.getIstaiga().getOficialusPavadinimasRead()); //I.N. getPilnasPavadinimas());
			%>
		</td>
		<td align="center">
			<%
				out.println(d.getInsVartId() + " | " + d.getUpdVartId());
			%>			
		</td>
		<td align="center">
			<a href="<%=request.getContextPath()%>/CompleteDeclaration.do?id=<%=d.getId()%>">Tæsti ávedimà</a>
			<% if (roles.contains("RL_GVDIS_GL_TVARK") || roles.contains("RL_GVDIS_SS_TVARK")){ %>
			<logic:equal parameter="deklTipas" value="atv">
				| <a href="<%=request.getContextPath()%>/DeleteDeclaration.do?id=<%=d.getId()%>&deklTipas=atv" onclick="return confirm('Ar tikrai iðtrinti ðià deklaracijà?');">Iðtrinti</a>
			</logic:equal>				
			<logic:equal parameter="deklTipas" value="isv">
				| <a href="<%=request.getContextPath()%>/DeleteDeclaration.do?id=<%=d.getId()%>&deklTipas=isv" onclick="return confirm('Ar tikrai iðtrinti ðià deklaracijà?');">Iðtrinti</a>
			</logic:equal>				
		    <% } %>
		</td>		
	</tr>
<% } %>
</table>
<hr />
<div align="right" style="font-size: 9px;">
<b>Pastaba:</b> vartotojas (ávd. / red.) - vartotojas sukûræs deklaracijà / paskutinius pakeitimus deklaracijoje padaræs vartotojas.
</div>