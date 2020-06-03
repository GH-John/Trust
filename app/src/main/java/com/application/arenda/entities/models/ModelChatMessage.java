package com.application.arenda.entities.models;

import com.application.arenda.entities.serverApi.chat.TypeMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

public class ModelChatMessage implements IModel {
    @SerializedName("idRoom")
    private long ID;

    @Expose(deserialize = false, serialize = false)
    private TypeMessage type;

    private String message;
    private LocalDateTime created;
    private LocalDateTime updated;

    public ModelChatMessage() {
    }

    public ModelChatMessage(TypeMessage type, String message) {
        this.type = type;
        this.message = message;
    }

    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        ID = id;
    }
}