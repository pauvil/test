/**
 * DMPProcessState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.algoritmusistemos.gvdis.ws.client;

public class DMPProcessState  implements java.io.Serializable {
    private java.lang.String internalProcessId;

    private DMPServiceParameter[] pdpoState;

    private DMPServiceParameter[] pkoState;

    public DMPProcessState() {
    }

    public DMPProcessState(
           java.lang.String internalProcessId,
           DMPServiceParameter[] pdpoState,
           DMPServiceParameter[] pkoState) {
           this.internalProcessId = internalProcessId;
           this.pdpoState = pdpoState;
           this.pkoState = pkoState;
    }


    /**
     * Gets the internalProcessId value for this DMPProcessState.
     * 
     * @return internalProcessId
     */
    public java.lang.String getInternalProcessId() {
        return internalProcessId;
    }


    /**
     * Sets the internalProcessId value for this DMPProcessState.
     * 
     * @param internalProcessId
     */
    public void setInternalProcessId(java.lang.String internalProcessId) {
        this.internalProcessId = internalProcessId;
    }


    /**
     * Gets the pdpoState value for this DMPProcessState.
     * 
     * @return pdpoState
     */
    public DMPServiceParameter[] getPdpoState() {
        return pdpoState;
    }


    /**
     * Sets the pdpoState value for this DMPProcessState.
     * 
     * @param pdpoState
     */
    public void setPdpoState(DMPServiceParameter[] pdpoState) {
        this.pdpoState = pdpoState;
    }


    /**
     * Gets the pkoState value for this DMPProcessState.
     * 
     * @return pkoState
     */
    public DMPServiceParameter[] getPkoState() {
        return pkoState;
    }


    /**
     * Sets the pkoState value for this DMPProcessState.
     * 
     * @param pkoState
     */
    public void setPkoState(DMPServiceParameter[] pkoState) {
        this.pkoState = pkoState;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DMPProcessState)) return false;
        DMPProcessState other = (DMPProcessState) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.internalProcessId==null && other.getInternalProcessId()==null) || 
             (this.internalProcessId!=null &&
              this.internalProcessId.equals(other.getInternalProcessId()))) &&
            ((this.pdpoState==null && other.getPdpoState()==null) || 
             (this.pdpoState!=null &&
              java.util.Arrays.equals(this.pdpoState, other.getPdpoState()))) &&
            ((this.pkoState==null && other.getPkoState()==null) || 
             (this.pkoState!=null &&
              java.util.Arrays.equals(this.pkoState, other.getPkoState())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getInternalProcessId() != null) {
            _hashCode += getInternalProcessId().hashCode();
        }
        if (getPdpoState() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPdpoState());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPdpoState(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPkoState() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPkoState());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPkoState(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DMPProcessState.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "DMPProcessState"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internalProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "internalProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pdpoState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "pdpoState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "DMPServiceParameter"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pkoState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "pkoState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "DMPServiceParameter"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ws.ivpk.erp.com", "item"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
