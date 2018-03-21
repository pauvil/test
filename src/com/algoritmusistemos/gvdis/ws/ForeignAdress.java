/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.algoritmusistemos.gvdis.ws;

/**
 *
 * @author t.zulgis
 */
public class ForeignAdress {
    private String country;
    private String city;
    private String address;
    private String postal_code;

    public String getAddress() {
        return address;
    } 

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    
}
