//package com.ip13.main.kafka
//
//import com.ip13.main.event.RestaurantCreatedEvent
//import com.ip13.main.model.dto.request.RoleAddRequest
//import com.ip13.main.model.enums.Role
//import com.ip13.main.service.UserService
//import org.springframework.kafka.annotation.KafkaListener
//import org.springframework.stereotype.Service
//
//@Service
//class RestaurantCreatedListener(
//    private val userService: UserService,
//) {
//    @KafkaListener(
//        topics = [RESTAURANT_CREATED_TOPIC],
//        containerFactory = "restaurantCreatedEventConsumer"
//    )
//    fun addUser(event: RestaurantCreatedEvent) {
//        userService.addRole(RoleAddRequest(username = event.managerName, role = Role.MANAGER))
//    }
//
//    companion object {
//        private const val RESTAURANT_CREATED_TOPIC = "restaurant-created"
//    }
//}