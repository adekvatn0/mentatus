package io.ambershogun.mentatus.message.localization

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource


@Configuration
class LocalizationConfiguration {

    @Bean
    fun messageSource(): ResourceBundleMessageSource? {
        return ResourceBundleMessageSource().apply {
            setBasename("message")
            setDefaultEncoding("UTF-8")
        }
    }
}