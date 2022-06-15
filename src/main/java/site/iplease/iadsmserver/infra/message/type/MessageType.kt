package site.iplease.iadsmserver.infra.message.type

enum class MessageType(val routingKey: String) {
    IP_ASSIGN_DEMAND_CREATE("ipAssignDemandCreate"),
    IP_ASSIGN_DEMAND_CANCEL("ipAssignDemandCancel"),
    DEMAND_ERROR_ON_STATUS("ipAssignDemandErrorOnStatus"),
    SEND_ALARM("sendAlarm"),
    UNKNOWN("");

    companion object {
        fun of(routingKey: String) =
            kotlin.runCatching { values().first { it.routingKey == routingKey } }
            .getOrElse { UNKNOWN }
    }
}
