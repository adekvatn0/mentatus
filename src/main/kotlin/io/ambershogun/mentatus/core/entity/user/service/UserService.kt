package io.ambershogun.mentatus.core.entity.user.service

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.repo.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository
) {
    fun findOrCreateUser(chatId: Long, languageCode: String): User {
        return userRepository.findById(chatId)
                .orElseGet {
                    User(chatId, languageCode)
                }
    }

    fun saveUser(user: User) {
        userRepository.save(user)
    }
}