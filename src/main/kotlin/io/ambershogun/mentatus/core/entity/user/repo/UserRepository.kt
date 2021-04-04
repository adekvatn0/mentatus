package io.ambershogun.mentatus.core.entity.user.repo

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository : MongoRepository<User, Long> {

    @Query(value = "{ 'settings' : {'MARKET_OVERVIEW': true}}")
    fun findUserToNotifyBySchedule(): List<User>
}