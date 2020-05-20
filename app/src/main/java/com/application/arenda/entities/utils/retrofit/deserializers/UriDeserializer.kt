package com.application.arenda.entities.utils.retrofit.deserializers

import android.net.Uri
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class UriDeserializer : JsonDeserializer<Uri> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }
}