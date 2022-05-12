package com.example.mcproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class notficationEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;


    @ColumnInfo(name = "userName")
    public String userName;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "requirement")
    public String requirement;

    @ColumnInfo(name = "blood_group")
    public String bloodGroup;

    @ColumnInfo(name = "mobileNo")
    public String mobileNo;

    @ColumnInfo(name = "quantity")
    public String quantity;

    @ColumnInfo(name = "currentTime")
    public String currentTime;

    @ColumnInfo(name = "userID")
    public String userID;


    public notficationEntity(String userName, double latitude, double longitude, String requirement, String bloodGroup, String mobileNo, String quantity, String currentTime, String userID) {
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.requirement = requirement;
        this.bloodGroup = bloodGroup;
        this.mobileNo = mobileNo;
        this.quantity = quantity;
        this.currentTime = currentTime;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
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

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
