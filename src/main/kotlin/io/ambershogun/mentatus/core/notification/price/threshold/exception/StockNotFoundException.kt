package io.ambershogun.mentatus.core.notification.price.threshold.exception

class StockNotFoundException(val ticker: String) : RuntimeException()