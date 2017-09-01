package com.ovh.milestone;

/**
 * Nichandle POJO
 */

public class Nic {

    // Attributes
    private String nichandle;
    private String name;
    private String firstName;
    private String address;
    private String tel;
    private String email;
    private String billingCountry;
    private String companyName;
    private String socialReason;
    private String siren;
    private String companyTel;
    private String companyAddress;
    private String companyType;



    // Constructor
    public Nic() {
        this.nichandle = null;
        this.name = null;
        this.firstName = null;
        this.address = null;
        this.tel = null;
        this.email = null;
        this.billingCountry = null;
        this.companyName = null;
        this.socialReason = null;
        this.siren = null;
        this.companyTel = null;
        this.companyAddress = null;
        this.companyType = null;
    }



    public Nic(String nichandle, String name, String firstName, String address, String tel,
               String email, String billingCountry, String companyName, String socialReason,
               String siren, String companyTel, String companyAddress, String companyType) {
        this.nichandle = nichandle;
        this.name = name;
        this.firstName = firstName;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.billingCountry = billingCountry;
        this.companyName = companyName;
        this.socialReason = socialReason;
        this.siren = siren;
        this.companyTel = companyTel;
        this.companyAddress = companyAddress;
        this.companyType = companyType;
    }



    public String getNichandle() {
        return nichandle;
    }



    public void setNichandle(String nichandle) {
        this.nichandle = nichandle;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getFirstName() {
        return firstName;
    }



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public String getAddress() {
        return address;
    }



    public void setAddress(String address) {
        this.address = address;
    }



    public String getTel() {
        return tel;
    }



    public void setTel(String tel) {
        this.tel = tel;
    }



    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public String getBillingCountry() {
        return billingCountry;
    }



    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }



    public String getCompanyName() {
        return companyName;
    }



    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }



    public String getSocialReason() {
        return socialReason;
    }



    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }



    public String getSiren() {
        return siren;
    }



    public void setSiren(String siren) {
        this.siren = siren;
    }



    public String getCompanyTel() {
        return companyTel;
    }



    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }



    public String getCompanyAddress() {
        return companyAddress;
    }



    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }



    public String getCompanyType() {
        return companyType;
    }



    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }



    @Override
    public String toString() {
        return "Nic{" +
            "nichandle='" + nichandle + '\'' +
            ", name='" + name + '\'' +
            ", firstName='" + firstName + '\'' +
            ", address='" + address + '\'' +
            ", tel='" + tel + '\'' +
            ", email='" + email + '\'' +
            ", billingCountry='" + billingCountry + '\'' +
            ", companyName='" + companyName + '\'' +
            ", socialReason='" + socialReason + '\'' +
            ", siren='" + siren + '\'' +
            ", companyTel='" + companyTel + '\'' +
            ", companyAddress='" + companyAddress + '\'' +
            ", companyType='" + companyType + '\'' +
            '}';
    }
}
