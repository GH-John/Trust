package com.application.arenda.entities.room.converters

import androidx.room.TypeConverter
import com.application.arenda.entities.models.TypeProposal

class TypeProposalConverter {
    @TypeConverter
    fun fromType(type: TypeProposal): Int {
        return type.code
    }

    @TypeConverter
    fun toType(code: Int): TypeProposal {
        return TypeProposal.get(code)
    }
}