<%@ page language="java" session="true" contentType="text/html; charset=Windows-1257"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="com.algoritmusistemos.gvdis.web.persistence.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.algoritmusistemos.gvdis.web.Constants" %>
<%@ page import="com.algoritmusistemos.gvdis.web.delegators.QueryDelegator" %>

<script language="Javascript">
<!--
	function closePopup()
	{
		var currentAdr = '<bean:write name="addressId"/>';
		var stringAddress = '<bean:write name="stringAddress"/>';
		var paramType = '<bean:write name="paramType"/>';
		var gvtKampoNr = '<bean:write name="gvtKampoNr"/>';
	
		if (window.opener && !window.opener.closed) {
			var opener = window.opener;
			if (paramType == 'A'){
				opener.setAddress(currentAdr, gvtKampoNr, '', stringAddress);
			}
			else {
				opener.setAddress('', '', currentAdr, stringAddress);
			}
		}
		else {
			alert('Klaida! Neteisingi adreso parametrai.');
		}
		window.close();
	}
	
//-->
</script>		

<div class="heading">
	<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td style="font-size: 14px; font-weight: bold; color: #333333;">
			Adreso formavimas (þingsnis <bean:write name="step"/>)
		</td>
		<td align="right">
		</td>
	</tr>
	</table>
</div>
<br />

<logic:greaterThan name="step" value="1">
<b>&nbsp;&nbsp;&nbsp;Pasirinktas adresas:</b><br />
<center>
<div style="width: 90%; align: center; padding: 10px; border: 2px solid <%= (request.getAttribute("allowSelect") == null) ? "red" : "green" %>;">
	<span class="addresslist"><b><bean:write name="stringAddress"/></b></span>
	<logic:present name="allowSelect">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="button" value="Pasirinkti &raquo;" onclick="closePopup();" style="width: 100px;" />
	</logic:present>
</div>
</center>
<hr />
</logic:greaterThan>

<div style="width: 90%; align: center; padding: 10px;">
<form action="addressbrowser.do" method="get">
<input type="hidden" name="currentAdr" value="<bean:write name="addressId"/>" />
<input type="hidden" name="paramType" value="<bean:write name="paramType"/>" />

	<b>Greita paieðka:</b>
	&nbsp;&nbsp;&nbsp;&nbsp;
	<input class="input" name="query" style="width: 200px;" value="<bean:write name="query"/>" />
	&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="submit" class="button" value="Ieðkoti &raquo;" style="width: 100px;" />
</form>
</div>
<hr />
	<%
	  if (session.getAttribute("adr_brows_state") != Constants.ADDR_BROWS_IST) //--ju.k 2007.08.28
	    {%>
	    
	    <table class="form" cellpadding="2" cellspacing="1" border="0" width="100%">
        <tr>
	    <td width="50%" class="addresslist" valign="top">
	    
    	<%session.setAttribute("adr_brows_state", null);
		List addresses = (List)request.getAttribute("addresses");
		String prevType = "";
		int size = addresses.size();
		int i = 0;
		for (Iterator it = addresses.iterator(); it.hasNext();){
			i++;
			AddressRow row = (AddressRow)it.next();
			if (!prevType.equals(row.getTipas())){
				prevType = row.getTipas();
				%></ul><h2><%= row.getTipas() %></h2><ul><%
			}
			if(row.getPavadinimas() != null){
				long paramCall = 1;
				if (row.getTipas().equals("Namas")) {
					String knr = "";
					if (row.getGvtKampoNr() != null) {
						knr = String.valueOf(row.getGvtKampoNr().longValue());
					}
					//System.out.println("AdrBroJSP kamp nr:   " + row.getGvtKampoNr());
					%><li><a href="addressbrowser.do?currentAdr=<%= row.getNr() %>&paramType=<%= row.getTipNr() %>&paramCall=<%= paramCall%>&gvtKampoNr=<%= knr%>"><%= row.getPavadinimas() %> &raquo;</a></li>
					<%
				}
				else if (row.getTipas().equals("Butas") || row.getTipas().equals("Korpusas")) {
					%><li><a href="addressbrowser.do?currentAdr=<%= row.getNr() %>&paramType=<%= row.getTipNr() %>&paramCall=<%= paramCall%>&gvtKampoNr=<%= request.getAttribute("gvtKampoNr")%>"><%= row.getPavadinimas() %> &raquo;</a></li>
					<%					
				}
				else {
					%><li><a href="addressbrowser.do?currentAdr=<%= row.getNr() %>&paramType=<%= row.getTipNr() %>&paramCall=<%= paramCall%>"><%= row.getPavadinimas() %> &raquo;</a></li>
					<%					
				}
				
				if (i == size / 2){
					%></ul></td><td width="50%" valign="top" class="addresslist"><h2><%= prevType %> (tæs.)</h2><ul><%
				}
			}
		}
		%>
		</ul></td>
		</tr>
		</table>
		<%
		
	if (size == 0 && "".equals(request.getAttribute("query")) && !"True".equals(request.getAttribute("currentIsGatve"))){ %>
<script language="Javascript">
<!--
	closePopup();
//-->
</script>
<%	} 
	if(size == 0 && "True".equals(request.getAttribute("currentIsGatve"))){ 
		%>
		&nbsp <b>Ðioje gatvëje namø nëra</b>
		 <%
	}
	if (size == 0 && !"".equals(request.getAttribute("query"))){ %>
<center><b>Nieko nerasta</b></center>
<%	} %>

</td>
</tr>	
</table>

	  <%}
		else
		{
		session.setAttribute("adr_brows_state", null);
        List addresses2 = (List)request.getAttribute("addr_ist");
        String paramType = (String)request.getAttribute("paramType");
		int size2 = addresses2.size();
		int i2 = 0;
		for (Iterator it2 = addresses2.iterator(); it2.hasNext();){
			i2++;
			AddressRow row2 = (AddressRow)it2.next();
			
		    String query3 = (String)request.getAttribute("query");
			List addresses3 = QueryDelegator.getInstance().getSubAddresses(request, paramType,  row2.getNr());
	        if (!"".equals(query3) && addresses3 != null){
	        	for (Iterator it3=addresses3.iterator(); it3.hasNext(); ){
	        		AddressRow row3 = (AddressRow)it3.next();
	        		if (row3.getPavadinimas().indexOf(query3) == -1){
	        			it3.remove();
	        		}
            	}
            }
	        
	        int size3 = addresses3.size();
	        
	        // Rodyti istaigos teritorijos adresus tik tuo atveju kai
	        // yra bent vienas (saraso dydis > 0);
	       if(size3 > 0) {
	        %>
				  <h3 class="heading3"><%= row2.getPavadinimas() %></h3>
	        		 
                  <table class="form" cellpadding="2" cellspacing="1" border="0" width="100%">
                  <td width="50%" class="addresslist" valign="top">
	        <%
	        
		        String prevType3 = "";
				int i3 = 0;
				for (Iterator it4 = addresses3.iterator(); it4.hasNext();){
					i3++;
					AddressRow row4 = (AddressRow)it4.next();
					if (!prevType3.equals(row4.getTipas())){
						prevType3 = row4.getTipas();
						%></ul><h2>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp <%= row4.getTipas() %></h2><ul><%
					}
					long paramCall = 1;
					String knr = "";
					if (row4.getGvtKampoNr() != null) {
						knr = String.valueOf(row4.getGvtKampoNr().longValue());
					}
					if (row4.getTipas().equals("Namas")) {
						//System.out.println("AdrBroJSP kamp nr4:   " + row4.getGvtKampoNr());
						%><li><a href="addressbrowser.do?currentAdr=<%= row4.getNr() %>&paramType=<%= row4.getTipNr() %>&paramCall=<%= paramCall%>&gvtKampoNr=<%= knr %>"><%= row4.getPavadinimas() %> &raquo;</a></li>
						<%
					}
					else if (row4.getTipas().equals("Butas") || row4.getTipas().equals("Korpusas")) {
						%><li><a href="addressbrowser.do?currentAdr=<%= row4.getNr() %>&paramType=<%= row4.getTipNr() %>&paramCall=<%= paramCall%>&gvtKampoNr=<%= request.getAttribute("gvtKampoNr")%>"><%= row4.getPavadinimas() %> &raquo;</a></li>
						<%					
					}					
					else {
						%><li><a href="addressbrowser.do?currentAdr=<%= row4.getNr() %>&paramType=<%= row4.getTipNr() %>&paramCall=<%= paramCall%>"><%= row4.getPavadinimas() %> &raquo;</a></li>
						<%					
					}
					if (i3 == size3 / 2 && size3 > 5){
						%></ul></td><td width="50%" valign="top" class="addresslist"><h2>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp <%= prevType3 %> (tæs.)</h2><ul><%
					}
				}
				  %>
				  </ul></td>
				  </tr>
                  </table>
                  <%
			}
			if(size3 == 0 && "True".equals(request.getAttribute("currentIsGatve"))){ 
				%>
				&nbsp <b>Ðioje gatvëje namø nëra</b>
				 <%
			}
		}
		
if (size2 == 0 && "".equals(request.getAttribute("query")) && !"True".equals(request.getAttribute("currentIsGatve"))){ %>
<script language="Javascript">
<!--
	closePopup();
//-->
</script>
<%	} 
	if (size2 == 0 && !"".equals(request.getAttribute("query"))){ %>
<center><b>Nieko nerasta</b></center>
<%	} 
		
		}
	%>
<hr />

<logic:greaterThan name="step" value="1">
<div align="right">
	<input type="button" class="button" style="width: 100px;" value="<< Atgal" onclick="history.go(-1);" />&nbsp;&nbsp;
</div>
</logic:greaterThan>