package com.ip13.main.provider

import com.ip13.main.model.entity.*
import com.ip13.main.model.entity.enums.RestaurantAddStatus
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.security.entity.User
import java.time.LocalDateTime
import java.time.Month

object EntitiesProvider {
    fun getDefaultUser(
        id: Int = 13,
        username: String = "username",
        password: String = "password",
        numOfGrades: Int = 0,
        sumOfGrades: Int = 0,
        roles: List<Role> = listOf(),
    ): User {
        return User(
            id = id,
            username = username,
            password = password,
            numOfGrades = numOfGrades,
            sumOfGrades = sumOfGrades,
            roles = roles,
        )
    }

    fun getDefaultRestaurantAddTicket(
        id: Int = 13,
        name: String = "restaurant",
        country: String = "country",
        city: String = "city",
        street: String = "street",
        building: Int = 13,
        entrance: Int? = 13,
        floor: Int = -2,
        description: String? = "this is a restaurant in the parking lot",
        userId: Int = 13,
        createDate: LocalDateTime = getDefaultLocalDateTime(),
    ): RestaurantAddTicket {
        return RestaurantAddTicket(
            id = id,
            name = name,
            country = country,
            city = city,
            street = street,
            building = building,
            entrance = entrance,
            floor = floor,
            description = description,
            userId = userId,
            createDate = createDate,
        )
    }

    fun getDefaultLocalDateTime(): LocalDateTime {
        return LocalDateTime.of(
            2002,
            Month.APRIL,
            19,
            13,
            13,
        )
    }

    fun getDefaultRestaurantAddTicketResult(
        id: Int = 13,
        restaurantAddTicketId: Int = 13,
        adminId: Int = 13,
        result: RestaurantAddStatus = RestaurantAddStatus.ACCEPTED,
        createDate: LocalDateTime = LocalDateTime.now(),
        adminComment: String? = "live long die young",
    ): RestaurantAddTicketResult {
        return RestaurantAddTicketResult(
            id = id,
            restaurantAddTicketId = restaurantAddTicketId,
            adminId = adminId,
            result = result,
            creationDate = createDate,
            adminComment = adminComment,
        )
    }

    fun getDefaultRestaurant(
        id: Int = 13,
        name: String = "restaurant",
        address: Address = getDefaultAddress(),
        restaurantAddTicket: RestaurantAddTicket = getDefaultRestaurantAddTicket(),
        description: String? = "live long die young",
        managers: List<Manager> = listOf(),
    ): Restaurant {
        return Restaurant(
            id = id,
            name = name,
            address = address,
            restaurantAddTicket = restaurantAddTicket,
            description = description,
            managers = managers
        )
    }

    fun getDefaultAddress(
        id: Int = 13,
        country: String = "country",
        city: String = "city",
        street: String = "street",
        building: Int = 13,
        entrance: Int? = 13,
        floor: Int? = -2,
    ): Address {
        return Address(
            id = id,
            country = country,
            city = city,
            street = street,
            building = building,
            entrance = entrance,
            floor = floor,
        )
    }

    fun getDefaultManager(
        id: Int = 13,
        user: User = getDefaultUser(),
        restaurant: Restaurant = getDefaultRestaurant(),
    ): Manager {
        return Manager(
            id = id,
            user = user,
            restaurant = restaurant,
        )
    }

    fun getDefaultTableReserveTicket(
        id: Int = 13,
        restaurant: Restaurant = getDefaultRestaurant(),
        user: User = getDefaultUser(),
        creationDate: LocalDateTime = getDefaultLocalDateTime(),
        userComment: String? = "live long die young",
    ): TableReserveTicket {
        return TableReserveTicket(
            id = id,
            restaurant = restaurant,
            user = user,
            creationDate = creationDate,
            userComment = userComment,
        )
    }
}