package io.ambershogun.mentatus.core.marketmaps.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "FinvizImageHolder")
class FinvizImageHolder {
    @Id
    lateinit var id: String
    lateinit var sectorUrl: String
    lateinit var regionsUrl: String
}