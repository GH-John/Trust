package com.application.arenda.entities.utils.retrofit.deserializers

import com.application.arenda.entities.utils.Utils
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

class DateTimeDeserializer : JsonDeserializer<LocalDateTime> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.asString, DateTimeFormatter.ofPattern(Utils.DatePattern.yyyy_MM_dd_HH_mm_ss.pattern))
    }
}