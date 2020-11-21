package io.ambershogun.mentatus.notification.price.exception

class StockNotFoundException(val ticker: String) : RuntimeException()