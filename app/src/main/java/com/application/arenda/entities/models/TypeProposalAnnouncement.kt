package com.application.arenda.entities.models

enum class TypeProposalAnnouncement(val type: Int) {
    OUTGOING(0), INCOMING(1);

    var code = type

    companion object {
        @JvmStatic
        operator fun get(type: Int): TypeProposalAnnouncement {
            return when (type) {
                0 -> OUTGOING
                1 -> INCOMING
                else -> OUTGOING
            }
        }
    }
}