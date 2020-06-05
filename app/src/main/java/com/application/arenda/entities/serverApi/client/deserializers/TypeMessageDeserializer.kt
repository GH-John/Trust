package com.application.arenda.entities.serverApi.client.deserializers

import com.application.arenda.entities.serverApi.chat.TypeMessage
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class TypeMessageDeserializer : JsonDeserializer<TypeMessage> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TypeMessage {
        return TypeMessage.get(json.asInt)
    }
}