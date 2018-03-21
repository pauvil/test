<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
	com.algoritmusistemos.gvdis.web.delegators.*" %>

<%
 //<jsp:useBean id="deklaracija" class="com.algoritmusistemos.gvdis.web.persistence.Deklaracija" />
 //<jsp:setProperty name="deklaracija" property="*" />	
	long id = Long.parseLong(request.getParameter("id"));
	Deklaracija deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracija(new Long(id), request);
%>

<div class="heading">Deklaracijos patvirtinta</div>

<table width="100%">


	<tr>
		<td width="10%">Data</td>
	</tr>
	<tr>
	    <td width="10%">Asmens Kodas</td>	    
	    <td align="center">	
			<%
				if(null != deklaracija.getAsmuo())out.print(deklaracija.getAsmuo().getAsmKodas());
			%>&nbsp;
		</td>
	</tr>
	<tr>
		<td width="25%">Asmuo</td>
		<td align="center">
			<%
				if(null != deklaracija.getAsmuo()) out.print(deklaracija.getAsmuo().getVardas()+" "+deklaracija.getAsmuo().getPavarde());
				else if(null != deklaracija.getLaikinasAsmuo()) out.print(deklaracija.getLaikinasAsmuo().getVardas()+" "+deklaracija.getLaikinasAsmuo().getPavarde());
			%>&nbsp;
		</td> 
    </tr>
    <tr>
		<td width="15%">Tipas</td>
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
		    		Átraukimo á GVNA deklaracija
		    	<% } %>&nbsp;
		</td>
	</tr>
	<tr>		
		<td width="15%">Ástaiga</td>
			<td align="center">
			<% out.println(deklaracija.getIstaiga().getOficialusPavadinimasRead()); //I.N. getPilnasPavadinimas()); %>
		</td>
	</tr>
	<tr>
		<td width="15%">Vartotojas (ávd. / red.)</td>
	</tr>
    
</table>
<center>

</center>

