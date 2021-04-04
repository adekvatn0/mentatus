package io.ambershogun.mentatus.core.entity.notification.user

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import org.junit.Test
import kotlin.test.assertEquals

class UserRepositoryTest : AbstractTest() {

    @Test
    fun `test find user to notify by schedule`() {
        userRepository.saveAll(
                listOf(
                        User(1).apply {
                            settings = mutableMapOf(Setting.MARKET_OVERVIEW to true)
                        },
                        User(2).apply {
                            settings = mutableMapOf(Setting.MARKET_OVERVIEW to false)
                        },
                        User(3).apply {
                            settings = mutableMapOf(Setting.MARKET_OVERVIEW to true)
                        }
                )
        )

        val usersToNotify = userRepository.findUserToNotifyBySchedule()

        assertEquals(2, usersToNotify.size)
        assertEquals(1, usersToNotify[0].chatId)
        assertEquals(3, usersToNotify[1].chatId)
    }
}