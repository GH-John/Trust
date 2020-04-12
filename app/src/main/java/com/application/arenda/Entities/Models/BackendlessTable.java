package com.application.arenda.Entities.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public abstract class BackendlessTable implements Serializable {
    private String objectId = "";
    private String ownerId = "";

    private Date created;
    private Date updated;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @NonNull
    @Override
    public String toString() {
        return "BackendlessTable{" +
                "objectId='" + objectId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}