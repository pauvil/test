/**
 * DMPMonitoringService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.algoritmusistemos.gvdis.ws.client;

public interface DMPMonitoringService extends java.rmi.Remote {
    public Child[] getChildren() throws java.rmi.RemoteException;
    public java.lang.String performAction(DMPServiceParameter[] params, DMPServiceParameter[] params2) throws java.rmi.RemoteException;
    public java.lang.String initDMPMonitoringProcess(DMPServiceParameter[] pdpoParameters, DMPServiceParameter[] pkoParameters, java.lang.String idmpPaslaugosAprasasId) throws java.rmi.RemoteException;
    public GetProcessStateResult getProcessState(java.lang.String internalProcessId) throws java.rmi.RemoteException;
    public String setProcessState(java.lang.String internalProcessId, DMPServiceParameter[] pdpoParameters, DMPServiceParameter[] pkoParameters) throws java.rmi.RemoteException;
    public String changeProcessState(java.lang.String internalProcessId, java.lang.String state) throws java.rmi.RemoteException;
    public String resultsAddress(java.lang.String internalProcessId, java.lang.String resultsAddress) throws java.rmi.RemoteException;
    public String refreshTaskForCustomer(java.lang.String userName, java.lang.String processId) throws java.rmi.RemoteException;
    public String changeProcessStateSetResult(java.lang.String internalProcessId, java.lang.String state, java.lang.String result) throws java.rmi.RemoteException;
    public String changeProcessStateResultResume(java.lang.String internalProcessId, java.lang.String state, java.lang.String result) throws java.rmi.RemoteException;
}
