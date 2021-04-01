package io.ambershogun.mentatus.core

import io.ambershogun.mentatus.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.objects.*
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MentatusBotTest : AbstractTest() {


    @Autowired
    lateinit var mentatusBot: MentatusBot

    @Test
    fun `user created on any message`() {
        assertEquals(0, userRepository.count())

        try {
            mentatusBot.onUpdateReceived(Update().apply {
                message = Message().apply {
                    text = "123"
                    chat = Chat().apply {
                        id = 466059L
                        firstName = "Skazhi"
                        lastName = "Privet"
                        userName = "skazhi_privet"
                    }
                }
            })
        } catch (e: TelegramApiRequestException) {
            //ignored
        }

        assertEquals(1, userRepository.count())

        val user = userRepository.findById(466059L).get()
        assertEquals("Skazhi", user.personalData.firstName)
        assertEquals("Privet", user.personalData.lastName)
        assertEquals("skazhi_privet", user.personalData.userName)
        assertNotNull(user.settings)
        assertEquals(false, user.settings.isScheduledNotificationsEnabled)
        assertNotNull(user.favoriteTickers)
        assertTrue(user.favoriteTickers.isEmpty())
    }

    @Test
    fun `user created on any callback`() {
        assertEquals(0, userRepository.count())

        try {
            mentatusBot.onUpdateReceived(Update().apply {
                callbackQuery = CallbackQuery().apply {
                    data = "123"
                    from = User().apply {
                        firstName = "Skazhi"
                        lastName = "Privet"
                        userName = "skazhi_privet"
                    }
                    message = Message().apply {
                        chat = Chat().apply {
                            id = 466059L
                        }
                    }
                }
            })
        } catch (e: TelegramApiRequestException) {
            //ignored
        }

        assertEquals(1, userRepository.count())

        val user = userRepository.findById(466059L).get()
        assertEquals("Skazhi", user.personalData.firstName)
        assertEquals("Privet", user.personalData.lastName)
        assertEquals("skazhi_privet", user.personalData.userName)
        assertNotNull(user.settings)
        assertEquals(false, user.settings.isScheduledNotificationsEnabled)
        assertNotNull(user.favoriteTickers)
        assertTrue(user.favoriteTickers.isEmpty())
    }

}