package com.application.arenda.entities.room.converters

import androidx.room.TypeConverter
import com.application.arenda.entities.models.TypeProposalAnnouncement

class TypeProposalConverter {
    @TypeConverter
    fun fromType(type: TypeProposalAnnouncement): Int {
        return type.code
    }

    @TypeConverter
    fun toType(code: Int): TypeProposalAnnouncement {
        return TypeProposalAnnouncement.get(code)
    }
}