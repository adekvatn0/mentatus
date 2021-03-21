package io.ambershogun.mentatus.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("mentatus")
class AppProperties {
    var adminId: Long = 0
    var bot = BotProperties()
}