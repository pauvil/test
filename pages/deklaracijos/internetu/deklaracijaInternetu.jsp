<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>


<div class="heading">
<logic:equal parameter="deklTipas" value="atv">Atvykimo </logic:equal>
<logic:equal parameter="deklTipas" value="isv">Iðvykimo </logic:equal>
<logic:equal parameter="deklTipas" value="gvna">Átraukimo á GVNA </logic:equal>
deklaracijos internetu
</div>

<table class="data_table" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <th width="10%">Data</th>
    <th width="10%">Asmens Kodas</th>
    <th width="25%">Asmuo</th>
	<th width="15%">Tipas</th>    
	<th width="15%">Ástaiga</th>
	<th width="15%">Vartotojas (ávd. / red.)</th>	
    <th width="10%">Veiksmai</th>
</tr>

<logic:iterate name="deklaracijos" id="deklaracija" type="com.algoritmusistemos.gvdis.web.persistence.Deklaracija">
	<tr class="table_row" onmouseover="this.className='table_row_on'" onmouseout="this.className='table_row'">
	<td align="center">
		<logic:present name="deklaracija" property="gavimoData">
			<bean:write name="deklaracija" property="gavimoData" format="yyyy-MM-dd"/>
		</logic:present>
	</td>			
	<td align="center">	
		<%
			if(null != deklaracija.getAsmuo())out.print(deklaracija.getAsmuo().getAsmKodas());
		%>&nbsp;
	</td>
	<td align="center">
		<%
			if(null != deklaracija.getAsmuo()) out.print(deklaracija.getAsmuo().getVardas()+" "+deklaracija.getAsmuo().getPavarde());
			else if(null != deklaracija.getLaikinasAsmuo()) out.print(deklaracija.getLaikinasAsmuo().getVardas()+" "+deklaracija.getLaikinasAsmuo().getPavarde());
		%>&nbsp;
	</td> 
	<td align="center">
	<%
		if(deklaracija instanceof AtvykimoDeklaracija){%>
		    Atvykimo deklaracija	
	<%}
	    	else
	    	if(deklaracija instanceof IsvykimoDeklaracija){%>
	    		Iðvykimo deklaracija
	    	<% }
	    	else	    	
	    	if(deklaracija instanceof GvnaDeklaracija){ %>
	    		   GVNA deklaracija
	    	<% } %>&nbsp;
	</td>
	<td align="center">
		<% out.println(deklaracija.getIstaiga().getOficialusPavadinimasRead()); // I.N. getPilnasPavadinimas()); %>
	</td>
	<td align="center">
		<% out.println(deklaracija.getInsVartId() + " | " + deklaracija.getUpdVartId()); %>
	</td>
	<td align="center">
		<a href="<%=request.getContextPath()%>/CompleteDeclaration.do?id=<%=deklaracija.getId()%>">Tæsti ávedimà</a>
		</td>		
	</tr>
</logic:iterate>
</table>
<hr />


