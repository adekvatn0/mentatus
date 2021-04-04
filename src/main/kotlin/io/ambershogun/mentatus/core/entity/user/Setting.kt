package io.ambershogun.mentatus.core.entity.user

enum class
Setting(
        val defaultValue: Boolean,
        val messageName: String
) {
    MARKET_OVERVIEW(true, "setting.market.overview"),
    PRICE_ALERT(true, "setting.price.alert");
}