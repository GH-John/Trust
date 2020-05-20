package com.application.arenda.entities.room.converters

import androidx.room.TypeConverter
import com.application.arenda.entities.models.ModelPicture
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PicturesConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromPictures(pictures: List<ModelPicture?>?): String {
        return gson.toJson(pictures)
    }

    @TypeConverter
    fun toPictures(data: String?): List<ModelPicture> {
        if (data == null) return emptyList()
        val listType = object : TypeToken<List<ModelPicture?>?>() {}.type
        return gson.fromJson(data, listType)
    }
}