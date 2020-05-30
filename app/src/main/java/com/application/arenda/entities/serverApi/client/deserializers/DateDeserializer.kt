package com.application.arenda.entities.serverApi.client.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.threeten.bp.LocalDate
import java.lang.reflect.Type

class DateDeserializer : JsonDeserializer<LocalDate> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString)
    }
}