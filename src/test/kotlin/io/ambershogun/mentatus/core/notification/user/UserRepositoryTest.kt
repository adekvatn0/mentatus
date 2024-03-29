package io.ambershogun.mentatus.core.notification.user

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.core.entity.user.PersonalData
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserRepositoryTest : AbstractTest() {

    @Test
    fun `test find user by setting`() {
        userRepository.saveAll(
                listOf(
                        User(1).apply {
                            settings = mutableMapOf(
                                    Setting.MARKET_OVERVIEW to true,
                                    Setting.PRICE_ALERT to true,
                                    Setting.DIVIDEND_CALENDAR to true
                            )
                        },
                        User(2).apply {
                            settings = mutableMapOf(Setting.MARKET_OVERVIEW to false)
                        },
                        User(3).apply {
                            settings = mutableMapOf(Setting.MARKET_OVERVIEW to true)
                        }
                )
        )

        val usersToNotify = userRepository.findBySetting(Setting.MARKET_OVERVIEW, true)

        assertEquals(2, usersToNotify.size)
        assertEquals(1, usersToNotify[0].chatId)
        assertEquals(3, usersToNotify[1].chatId)
    }

    @Test
    fun `test find user by username`() {
        userRepository.save(
                User(1).apply {
                    personalData = PersonalData("Skazhi", "Privet", "skazhi_privet")
                }
        )

        val user = userRepository.findByUsername("skazhi_privet")

        assertNotNull(user)
        assertEquals(1, user.chatId)
        assertEquals("Skazhi", user.personalData.firstName)
        assertEquals("Privet", user.personalData.lastName)
    }
}