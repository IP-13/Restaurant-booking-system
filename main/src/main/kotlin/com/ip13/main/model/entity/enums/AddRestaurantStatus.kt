package com.ip13.main.model.entity.enums

enum class AddRestaurantStatus(
    val code: String,
) {
    CREATED("created"),
    PROCESSING("processing"),
    ACCEPTED("accepted"),
    REJECTED("rejected");
}