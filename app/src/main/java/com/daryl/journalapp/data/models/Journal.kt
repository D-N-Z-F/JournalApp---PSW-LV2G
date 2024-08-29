package com.daryl.journalapp.data.models

data class Journal(
    val id: String? = null,
    val title: String,
    val description: String,
    val dateTime: String,
    val location: String,
    val tags: List<String>
) {
    fun toMap(): Map<String, Any?> =
        mapOf(
            "title" to title,
            "desc" to description,
            "dateTime" to dateTime,
            "location" to location,
            "tags" to tags
        )
    companion object {
        fun fromMap(map: Map<*, *>): Journal =
            Journal(
                title = map["title"].toString(),
                description = map["desc"].toString(),
                dateTime = map["dateTime"].toString(),
                location = map["location"].toString(),
                tags = (map["tags"] as? List<*>)?.map { it.toString() } ?: emptyList()
            )
    }
}
