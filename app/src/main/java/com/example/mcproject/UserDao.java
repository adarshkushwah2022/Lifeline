package com.example.mcproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertnotificationEntry(notficationEntity notfication);

    @Query("SELECT * FROM notficationEntity")
    List<notficationEntity> getNotificationsList();
}