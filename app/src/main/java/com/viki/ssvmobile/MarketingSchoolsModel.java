package com.viki.ssvmobile;

class MarketingSchoolsModel {

    private String msName,msContactPerson,msContactNumber,msContactEmail,msRemarks;

    public MarketingSchoolsModel(String msName, String msContactPerson, String msContactNumber, String msContactEmail, String msRemarks) {
        this.msName = msName;
        this.msContactPerson = msContactPerson;
        this.msContactNumber = msContactNumber;
        this.msContactEmail = msContactEmail;
        this.msRemarks = msRemarks;
    }

    public MarketingSchoolsModel () {}

    public String getMsName() {
        return msName;
    }

    public void setMsName(String msName) {
        this.msName = msName;
    }

    public String getMsContactPerson() {
        return msContactPerson;
    }

    public void setMsContactPerson(String msContactPerson) {
        this.msContactPerson = msContactPerson;
    }

    public String getMsContactNumber() {
        return msContactNumber;
    }

    public void setMsContactNumber(String msContactNumber) {
        this.msContactNumber = msContactNumber;
    }

    public String getMsContactEmail() {
        return msContactEmail;
    }

    public void setMsContactEmail(String msContactEmail) {
        this.msContactEmail = msContactEmail;
    }

    public String getMsRemarks() {
        return msRemarks;
    }

    public void setMsRemarks(String msRemarks) {
        this.msRemarks = msRemarks;
    }

    /*public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }*/
}
