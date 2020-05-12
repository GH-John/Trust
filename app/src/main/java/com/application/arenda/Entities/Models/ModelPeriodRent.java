package com.application.arenda.Entities.Models;

import com.application.arenda.UI.Components.CalendarView.models.ModelEvent;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class ModelPeriodRent extends ModelEvent implements IModel {

    @SerializedName("idRent")
    private long ID;

    @SerializedName("idUser")
    private long idUser;

    @SerializedName("idAnnouncement")
    private long idAnnouncement;

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(long idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        this.ID = id;
    }
}