package io.ambershogun.mentatus.core.entity.user.repo

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, Long> {
}