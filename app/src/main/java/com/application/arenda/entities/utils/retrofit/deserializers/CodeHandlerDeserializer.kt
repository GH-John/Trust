package com.application.arenda.entities.utils.retrofit.deserializers

import com.application.arenda.entities.utils.retrofit.CodeHandler
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class CodeHandlerDeserializer : JsonDeserializer<CodeHandler> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CodeHandler {
        return CodeHandler.get(json.asInt)
    }
}