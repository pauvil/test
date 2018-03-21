/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.algoritmusistemos.gvdis.ws;

/**
 *
 * @author s.daujotis
 */  
public class DefaultMessage {
    private String error_id;
    private String error_code;
    private String error_message;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_id() {
        return error_id;
    }

    public void setError_id(String error_id) {
        this.error_id = error_id;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    
}
