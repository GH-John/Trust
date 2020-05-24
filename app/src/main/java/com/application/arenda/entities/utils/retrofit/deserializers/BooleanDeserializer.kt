package com.application.arenda.entities.utils.retrofit.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class BooleanDeserializer : JsonDeserializer<Boolean> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
        if(json.asString == null)
            return false

        return when (json.asString.toLowerCase(Locale.ROOT)){
            "1" -> true
            "true" -> true
            else -> false
        }
    }
}