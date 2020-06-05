package com.application.arenda.entities.models

import android.net.Uri
import com.google.gson.annotations.SerializedName

class ModelChat : IModel {
    @SerializedName("idChat")
    private var ID: Long = 0

    @SerializedName("idUser")
    var idUser: Long = 0

    @SerializedName("login")
    var login = ""

    @SerializedName("userLogo")
    var avatar: Uri? = null

    override fun getID(): Long {
        return ID
    }

    override fun setID(id: Long) {
        ID = id
    }

    override fun toString(): String {
        return "ModelChat(ID=$ID, idUser=$idUser, login='$login', avatar=$avatar)"
    }
}