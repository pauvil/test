<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*,
	com.algoritmusistemos.gvdis.web.delegators.*,
	java.text.SimpleDateFormat " %>

<%
 //<jsp:useBean id="deklaracija" class="com.algoritmusistemos.gvdis.web.persistence.Deklaracija" />
 //<jsp:setProperty name="deklaracija" property="*" />	
	Long id = (Long)request.getAttribute("id");
	Deklaracija deklaracija = DeklaracijosDelegator.getInstance(request).getDeklaracija(id, request);
%>
<logic:notPresent name="wserror">
	<div class="heading">Deklaracija atmesta</div>
</logic:notPresent>
<logic:present name="wserror">
	<div class="heading">Deklaracijos nepavyko atmesti</div>
</logic:present>

<table cellpadding="2" cellspacing="2" width="80%">
	<tr>
		<td width="40%" align="right">Data:</td>
		<td><b>
			<%
				if (null != deklaracija.getGavimoData()){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					out.println(sdf.format(deklaracija.getGavimoData()));
				}
			%>
		</b></td>
	</tr>
	<tr>
	    <td align="right">Asmens Kodas:</td>
	    <td><b>	
			<%	
				if(null != deklaracija.getAsmuo())out.print(deklaracija.getAsmuo().getAsmKodas());
			%>&nbsp;
		</b></td>
	</tr>
	<tr>
		<td align="right">Asmuo:</td>
		<td><b>
			<% 
				if(null != deklaracija.getAsmuo()) out.print(deklaracija.getAsmuo().getVardas()+" "+deklaracija.getAsmuo().getPavarde());
				else if(null != deklaracija.getLaikinasAsmuo()) out.print(deklaracija.getLaikinasAsmuo().getVardas()+" "+deklaracija.getLaikinasAsmuo().getPavarde());
			%>&nbsp;
		</b></td> 
    </tr>
    <tr>
		<td align="right">Tipas:</td>
		<td><b>
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
		</b></td>
	</tr>
	
	<logic:present name="wserror">
		<tr>
			<td align="right">Nepavykimo prieþastys</td>
			<td><b>
				<bean:write name="wserror"/> 
			</b></td>
		</tr>
	</logic:present>

</table>

