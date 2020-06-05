package com.application.arenda.entities.models

import com.application.arenda.entities.serverApi.chat.TypeMessage
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

class ModelMessage : IModel {
    @SerializedName("idMessage")
    private var ID: Long = 0

    @SerializedName("typeMessage")
    var type: TypeMessage? = null
    var message: String? = null
    var created: LocalDateTime? = null
    var updated: LocalDateTime? = null

    constructor()
    constructor(id: Long, type: TypeMessage?, message: String?) {
        ID = id
        this.type = type
        this.message = message
    }

    override fun getID(): Long {
        return ID
    }

    override fun setID(id: Long) {
        ID = id
    }

    override fun toString(): String {
        return "ModelMessage(ID=$ID, type=$type, message=$message, created=$created, updated=$updated)"
    }
}