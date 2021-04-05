package io.ambershogun.mentatus.core.entity.user.repo

import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository : MongoRepository<User, Long> {

    @Query(value = "{ 'settings.?0': ?1} }")
    fun findBySetting(settingName: Setting, value: Boolean): List<User>

    @Query(value = "{ 'personalData.username': ?0 }")
    fun findByUsername(username: String): User?
}