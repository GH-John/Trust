package com.application.arenda.entities.serverApi.client.deserializers

import com.application.arenda.entities.models.Currency
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class CurrencyDeserializer : JsonDeserializer<Currency> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Currency {
        return Currency.get(json.asString.toUpperCase(Locale.ROOT))
    }
}