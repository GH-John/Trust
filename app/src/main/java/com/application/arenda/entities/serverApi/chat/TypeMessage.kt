package com.application.arenda.entities.serverApi.chat

enum class TypeMessage(type: Int) {
    CHAT_MINE(0), CHAT_PARTNER(1);

    var code = type

    companion object {
        @JvmStatic
        operator fun get(type: Int): TypeMessage {
            return when (type) {
                0 -> CHAT_MINE
                1 -> CHAT_PARTNER
                else -> CHAT_MINE
            }
        }
    }
}