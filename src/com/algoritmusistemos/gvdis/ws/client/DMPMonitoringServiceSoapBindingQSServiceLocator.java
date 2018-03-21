/**
 * DMPMonitoringServiceSoapBindingQSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.algoritmusistemos.gvdis.ws.client;

public class DMPMonitoringServiceSoapBindingQSServiceLocator extends org.apache.axis.client.Service implements DMPMonitoringServiceSoapBindingQSService {

    public DMPMonitoringServiceSoapBindingQSServiceLocator() {
    }


    public DMPMonitoringServiceSoapBindingQSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DMPMonitoringServiceSoapBindingQSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DMPMonitoringServiceSoapBindingQSPort
    private java.lang.String DMPMonitoringServiceSoapBindingQSPort_address = "https://IVPKMS1:7002/services/DMPMonitoringService_Proxy";

    public java.lang.String getDMPMonitoringServiceSoapBindingQSPortAddress() {
        return DMPMonitoringServiceSoapBindingQSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DMPMonitoringServiceSoapBindingQSPortWSDDServiceName = "DMPMonitoringServiceSoapBindingQSPort";

    public java.lang.String getDMPMonitoringServiceSoapBindingQSPortWSDDServiceName() {
        return DMPMonitoringServiceSoapBindingQSPortWSDDServiceName;
    }

    public void setDMPMonitoringServiceSoapBindingQSPortWSDDServiceName(java.lang.String name) {
        DMPMonitoringServiceSoapBindingQSPortWSDDServiceName = name;
    }

    public DMPMonitoringService getDMPMonitoringServiceSoapBindingQSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DMPMonitoringServiceSoapBindingQSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDMPMonitoringServiceSoapBindingQSPort(endpoint);
    }

    public DMPMonitoringService getDMPMonitoringServiceSoapBindingQSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            DMPMonitoringServiceSoapBindingStub _stub = new DMPMonitoringServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDMPMonitoringServiceSoapBindingQSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDMPMonitoringServiceSoapBindingQSPortEndpointAddress(java.lang.String address) {
        DMPMonitoringServiceSoapBindingQSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (DMPMonitoringService.class.isAssignableFrom(serviceEndpointInterface)) {
                DMPMonitoringServiceSoapBindingStub _stub = new DMPMonitoringServiceSoapBindingStub(new java.net.URL(DMPMonitoringServiceSoapBindingQSPort_address), this);
                _stub.setPortName(getDMPMonitoringServiceSoapBindingQSPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DMPMonitoringServiceSoapBindingQSPort".equals(inputPortName)) {
            return getDMPMonitoringServiceSoapBindingQSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "DMPMonitoringServiceSoapBindingQSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "DMPMonitoringServiceSoapBindingQSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DMPMonitoringServiceSoapBindingQSPort".equals(portName)) {
            setDMPMonitoringServiceSoapBindingQSPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
