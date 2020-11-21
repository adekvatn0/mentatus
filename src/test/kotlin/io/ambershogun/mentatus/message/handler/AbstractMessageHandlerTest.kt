package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource

abstract class AbstractMessageHandlerTest : AbstractTest() {

    @Autowired
    protected lateinit var messageSource: MessageSource

    @Test
    abstract fun `test message regex`()

    @Test
    abstract fun `test handle message`()
}