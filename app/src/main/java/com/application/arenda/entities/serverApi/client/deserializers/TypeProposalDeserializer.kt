package com.application.arenda.entities.serverApi.client.deserializers

import com.application.arenda.entities.models.TypeProposal
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class TypeProposalDeserializer : JsonDeserializer<TypeProposal> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TypeProposal {
        return TypeProposal.get(json.asInt)
    }
}