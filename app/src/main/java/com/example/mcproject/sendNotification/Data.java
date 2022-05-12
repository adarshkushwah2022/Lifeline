package com.example.mcproject.sendNotification;

public class Data {
    String Name, bloodGroup, mobileNo, quantity, requirement, userID;
    Double latitude, longitude;
    Boolean sendByReceiver = false;

    public Data(String name, String bloodGroup, String mobileNo, String quantity, String requirement, String userID, Double latitude, Double longitude) {
        Name = name;
        this.bloodGroup = bloodGroup;
        this.mobileNo = mobileNo;
        this.quantity = quantity;
        this.requirement = requirement;
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Data(boolean sendByReceiver)
    {
        this.sendByReceiver = sendByReceiver;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Boolean getSendByReceiver() {
        return sendByReceiver;
    }

    public void setSendByReceiver(Boolean sendByReceiver) {
        this.sendByReceiver = sendByReceiver;
    }
}
