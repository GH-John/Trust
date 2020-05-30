package com.application.arenda.entities.room.converters

import androidx.room.TypeConverter
import com.application.arenda.entities.models.Currency

class CurrencyConverter {
    @TypeConverter
    fun fromCurrency(currency: Currency): String {
        return currency.currency
    }

    @TypeConverter
    fun toCurrency(currency: String?): Currency {
        return Currency.get(currency)
    }
}