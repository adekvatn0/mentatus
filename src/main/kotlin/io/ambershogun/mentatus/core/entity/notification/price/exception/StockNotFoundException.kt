package io.ambershogun.mentatus.core.entity.notification.price.exception

class StockNotFoundException(val ticker: String) : RuntimeException()