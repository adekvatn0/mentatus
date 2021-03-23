package io.ambershogun.mentatus.core.entity.user.service

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.repo.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository
) {
    fun findOrCreateUser(chatId: Long): User {
        return userRepository.findById(chatId)
                .orElseGet { User(chatId) }
    }

    fun saveUser(user: User) {
        userRepository.save(user)
    }
}