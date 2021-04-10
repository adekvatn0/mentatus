package io.ambershogun.mentatus.core.marketmaps.repo

import io.ambershogun.mentatus.core.marketmaps.entity.FinvizImageHolder
import org.springframework.data.mongodb.repository.MongoRepository

interface FinvizImageHolderRepository : MongoRepository<FinvizImageHolder, String>