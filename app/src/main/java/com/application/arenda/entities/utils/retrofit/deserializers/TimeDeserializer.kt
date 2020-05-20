package com.application.arenda.entities.utils.retrofit.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.threeten.bp.LocalTime
import java.lang.reflect.Type

class TimeDeserializer : JsonDeserializer<LocalTime> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime {
        return LocalTime.parse(json.asString)
    }
}