package com.example.surajgdesai.homework09a;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suraj G Desai on 4/18/2017.
 */

public class Trip implements Serializable {
    String title;
    String location;
    String tripProfilePhotoUrl;
    Date createdDate;
    String tripKey;
    String createdBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    List<Message> messages;

    public Trip(){
        messages = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTripProfilePhotoUrl() {
        return tripProfilePhotoUrl;
    }

    public void setTripProfilePhotoUrl(String tripProfilePhotoUrl) {
        this.tripProfilePhotoUrl = tripProfilePhotoUrl;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
