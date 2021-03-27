package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import java.io.File

@Component
class MarketMessageHandler : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "\uD83D\uDDFA Рынки"
    }

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        val sectorsMedia = InputMediaPhoto()
        sectorsMedia.setMedia(File("./marketmaps/sectors.png"), "sectors.png")

        val regionsMedia = InputMediaPhoto()
        regionsMedia.setMedia(File("./marketmaps/regions.png"), "regions.png")

        val sendMediaGroup = SendMediaGroup()
        sendMediaGroup.chatId = user.chatId.toString()
        sendMediaGroup.medias = listOf(
                sectorsMedia, regionsMedia
        )

        return listOf(sendMediaGroup)
    }
}