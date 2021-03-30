package io.ambershogun.mentatus.core.entity.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "User")
class User(
        @Id
        var chatId: Long
) {
    var userName: String? = null
    var lastActive: LocalDateTime? = null
    var favoriteTickers: MutableSet<String> = mutableSetOf()
}