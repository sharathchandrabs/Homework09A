package com.example.surajgdesai.homework09a;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Suraj G Desai on 4/18/2017.
 */

public class Message implements Serializable {
    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

    public String getTextOrPhoto() {
        return textOrPhoto;
    }

    public void setTextOrPhoto(String textOrPhoto) {
        this.textOrPhoto = textOrPhoto;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    boolean isPic;
    String textOrPhoto;
    Date createdDate;
    User createdBy;
}
