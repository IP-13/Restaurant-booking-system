package com.ip13.main.router

import com.ip13.main.handler.RestaurantGradeHandler
import com.ip13.main.handler.VisitorGradeHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class GradeRouter {
    @Bean
    fun routes(
        visitorGradeHandler: VisitorGradeHandler,
        restaurantGradeHandler: RestaurantGradeHandler,
    ): RouterFunction<ServerResponse> {
        return nest(
            accept(MediaType.APPLICATION_JSON),
            nest(
                path("/grade"),
                nest(
                    path("/visitor"),
                    route(POST(""), visitorGradeHandler::gradeVisitor)
                        .andRoute(GET("/{username}"), visitorGradeHandler::getGrade)
                ).andNest(
                    path("/restaurant"),
                    route(POST(""), restaurantGradeHandler::gradeRestaurant)
                        .andRoute(GET("{id}"), restaurantGradeHandler::getGrade)
                )
            )
        )
    }
}