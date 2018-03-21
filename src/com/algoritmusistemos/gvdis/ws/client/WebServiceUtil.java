package com.algoritmusistemos.gvdis.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.AxisFault;

import com.algoritmusistemos.gvdis.web.delegators.ParameterDelegator;

public class WebServiceUtil {
	private DMPMonitoringServiceSoapBindingStub wsStub;
	
	private static WebServiceUtil webServiceUtil;
	
	private WebServiceUtil(HttpServletRequest req) throws AxisFault{		
		try {//
			String endPoint = ParameterDelegator.getInstance().getStringParameter(req, "GVDIS_EL_VALDZIOS_VARTU_WS");
			wsStub = new DMPMonitoringServiceSoapBindingStub(new URL(endPoint), null);
		} catch (MalformedURLException e){}

	}
	
	public static WebServiceUtil getInstance(HttpServletRequest req) throws AxisFault{
		if (webServiceUtil == null) {
			webServiceUtil = new WebServiceUtil(req);
		}
		return webServiceUtil;
	}
	
	public String changeProcessStateResultResume(String internalProcessId,
			String state, String result) throws RemoteException {
		return wsStub.changeProcessStateResultResume(internalProcessId, state,result);
	}
}
