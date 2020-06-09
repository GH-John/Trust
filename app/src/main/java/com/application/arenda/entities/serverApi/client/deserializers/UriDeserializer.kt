package com.application.arenda.entities.serverApi.client.deserializers

import android.net.Uri
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class UriDeserializer : JsonDeserializer<Uri> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Uri {
        if (json == null || json.asString.isEmpty())
            return Uri.Builder().build()

        return Uri.parse(json.asString)
    }
}