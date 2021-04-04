package io.ambershogun.mentatus.push

import com.fasterxml.jackson.databind.ObjectMapper
import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.PersonalData
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.push.dto.ApiError
import io.ambershogun.mentatus.push.dto.BroadcastPush
import io.ambershogun.mentatus.push.dto.DirectPush
import io.ambershogun.mentatus.push.dto.PushResult
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
class PushControllerTest : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var mentatusBot: MentatusBot

    @Test
    fun `test send direct push ok`() {
        userRepository.save(User(1).apply {
            personalData = PersonalData("Skazhi", "Privet", "skazhi_privet")
        })

        val body = DirectPush().apply {
            text = "privet"
            username = "skazhi_privet"
        }

        mvc.post("/push/direct") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isOk }
            content {
                json(objectMapper.writeValueAsString(
                        PushResult(1)
                ))
            }
        }

        verify(mentatusBot, times(1)).sendMessageText(1, "privet")
    }

    @Test
    fun `test send broadcast push ok`() {
        userRepository.saveAll(
                listOf(
                        User(1).apply {
                            personalData = PersonalData("Skazhi", "Privet", "skazhi_privet")
                        },
                        User(2).apply {
                            personalData = PersonalData("Skazhi", "Poka", "vasya_pupkin")
                        }
                )
        )

        val body = BroadcastPush().apply {
            text = "privet"
        }

        mvc.post("/push/broadcast")
        {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isOk }
            content {
                json(objectMapper.writeValueAsString(
                        PushResult(2)
                ))
            }
        }

        verify(mentatusBot, times(1)).sendMessageText(1, "privet")
        verify(mentatusBot, times(1)).sendMessageText(2, "privet")
    }


    @Test
    fun `when empty DirectPush(username) then 400`() {
        val body = DirectPush().apply {
            text = "privet"
            username = null
        }

        mvc.post("/push/direct") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isBadRequest }
            content {
                json(objectMapper.writeValueAsString(
                        ApiError(
                                HttpStatus.BAD_REQUEST.name,
                                listOf("username: must not be blank")
                        )
                ))
            }
        }
    }

    @Test
    fun `when empty DirectPush(text) then 400`() {
        val body = DirectPush().apply {
            text = null
            username = "skazhi_privet"
        }

        mvc.post("/push/direct") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isBadRequest }
            content {
                json(objectMapper.writeValueAsString(
                        ApiError(
                                HttpStatus.BAD_REQUEST.name,
                                listOf("text: must not be blank")
                        )
                ))
            }
        }
    }

    @Test
    fun `when user not found then 400`() {
        val body = DirectPush().apply {
            text = "privet"
            username = "skazhi_privet"
        }

        mvc.post("/push/direct") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isBadRequest }
            content {
                json(objectMapper.writeValueAsString(
                        ApiError(
                                HttpStatus.BAD_REQUEST.name,
                                listOf(
                                        "Пользователь skazhi_privet не найден"
                                )
                        )
                ))
            }
        }
    }

    @Test
    fun `when empty BroadcastPush(text) then 400`() {
        val body = BroadcastPush().apply {
            text = ""
        }

        mvc.post("/push/broadcast") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isBadRequest }
            content {
                json(objectMapper.writeValueAsString(
                        ApiError(
                                HttpStatus.BAD_REQUEST.name,
                                listOf("text: must not be blank")
                        )
                ))
            }
        }
    }
}