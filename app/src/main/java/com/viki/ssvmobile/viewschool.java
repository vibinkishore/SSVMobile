package com.viki.ssvmobile;

public class viewschool {

String ssv_schoolName;
String ssv_schoolAddress;
String ssv_schoolType;
String ssv_schoolLevel;
String ssv_locationType;
String ssv_officeNumber;
String ssv_schoolEmail;
String ssv_principalEmail;
String ssv_principalNumber;
String ssv_schoolEstablishmentYear;
String ssv_adobePackage;
/*
String ssv_cProgramming;
String ssv_corelDraw;
String ssv_correspondentEmail;
String ssv_correspondentName;
String ssv_correspondentNumber;
String ssv_cplusProgramming;
String ssv_district;
String ssv_gwBasic;
String ssv_hasComputerLab;
String ssv_labPowerBackup;
String ssv_logoProgramming;
String ssv_msOffice;
String ssv_noOfComputersWorking;
String ssv_numberOfComputers;
String ssv_pageMaker;
*/




viewschool()
{


}


    viewschool(String Ssv_schoolName, String Ssv_schoolAddress, String Ssv_schoolType, String Ssv_schoolLevel, String Ssv_locationType, String Ssv_officeNumber,
               String Ssv_schoolEmail, String Ssv_principalEmail, String Ssv_principalNumber, String Ssv_schoolEstablishmentYear/*, String Ssv_adobePackage
    ,String Ssv_cProgramming,String ssv_corelDraw,String ssv_correspondentEmail,String ssv_correspondentName,String ssv_correspondentNumber*/)
{

    ssv_schoolName=Ssv_schoolName;
    ssv_schoolAddress=Ssv_schoolAddress;
    ssv_schoolType=Ssv_schoolType;
    ssv_schoolLevel=Ssv_schoolLevel;
    ssv_locationType=Ssv_locationType;
    ssv_officeNumber=Ssv_officeNumber;
    ssv_schoolEmail=Ssv_schoolEmail;
    ssv_principalEmail=Ssv_principalEmail;
    ssv_principalNumber=Ssv_principalNumber;
    ssv_schoolEstablishmentYear=Ssv_schoolEstablishmentYear;
/*
    ssv_adobePackage=Ssv_adobePackage;
*/



}

    public String getSsv_schoolName() {
        return ssv_schoolName;
    }

    public void setSsv_schoolName(String ssv_schoolName) {
        this.ssv_schoolName = ssv_schoolName;
    }

    public String getSsv_schoolAddress() {
        return ssv_schoolAddress;
    }

    public void setSsv_schoolAddress(String ssv_schoolAddress) {
        this.ssv_schoolAddress = ssv_schoolAddress;
    }

    public String getSsv_schoolType() {
        return ssv_schoolType;
    }

    public void setSsv_schoolType(String ssv_schoolType) {
        this.ssv_schoolType = ssv_schoolType;
    }

    public String getSsv_schoolLevel() {
        return ssv_schoolLevel;
    }

    public void setSsv_schoolLevel(String ssv_schoolLevel) {
        this.ssv_schoolLevel = ssv_schoolLevel;
    }

    public String getSsv_locationType() {
        return ssv_locationType;
    }

    public void setSsv_locationType(String ssv_locationType) {
        this.ssv_locationType = ssv_locationType;
    }

    public String getSsv_officeNumber() {
        return ssv_officeNumber;
    }
    public String getSsv_adobePackage() {
        return ssv_adobePackage;
    }

    public void setSsv_adobePackage(String ssv_adobePackage) {
        this.ssv_adobePackage = ssv_adobePackage;
    }

    public void setSsv_officeNumber(String ssv_officeNumber) {
        this.ssv_officeNumber = ssv_officeNumber;
    }

    public String getSsv_schoolEmail() {
        return ssv_schoolEmail;
    }

    public void setSsv_schoolEmail(String ssv_schoolEmail) {
        this.ssv_schoolEmail = ssv_schoolEmail;
    }

    public String getSsv_principalEmail() {
        return ssv_principalEmail;
    }

    public void setSsv_principalEmail(String ssv_principalEmail) {
        this.ssv_principalEmail = ssv_principalEmail;
    }

    public String getSsv_principalNumber() {
        return ssv_principalNumber;
    }

    public void setSsv_principalNumber(String ssv_principalNumber) {
        this.ssv_principalNumber = ssv_principalNumber;
    }

    public String getSsv_schoolEstablishmentYear() {
        return ssv_schoolEstablishmentYear;
    }

    public void setSsv_schoolEstablishmentYear(String ssv_schoolEstablishmentYear) {
        this.ssv_schoolEstablishmentYear = ssv_schoolEstablishmentYear;
    }
}
