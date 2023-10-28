package com.ip13.main.model.entity

import jakarta.persistence.*

@Entity
class Restaurant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    @OneToOne
    @JoinColumn(name = "address_id")
    val address: Address = Address(),
    @OneToOne
    @JoinColumn(name = "restaurant_add_ticket_id")
    val restaurantAddTicket: RestaurantAddTicket = RestaurantAddTicket(),
    val description: String? = null,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    val managers: List<Manager> = emptyList()
)