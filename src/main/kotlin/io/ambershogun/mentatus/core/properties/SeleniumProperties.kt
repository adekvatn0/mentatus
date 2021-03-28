package io.ambershogun.mentatus.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("selenium")
class SeleniumProperties {
    var screenWidth: Int? = 0
    var screenHeight: Int? = 0
    var userAgent: String? = null
}