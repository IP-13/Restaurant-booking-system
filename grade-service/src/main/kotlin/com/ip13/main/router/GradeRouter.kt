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
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class GradeRouter(
    private val restaurantGradeHandler: RestaurantGradeHandler,
    private val visitorGradeHandler: VisitorGradeHandler,
) {
    @Bean
    fun routesVisitor(): RouterFunction<ServerResponse> {
        return nest(
            accept(MediaType.APPLICATION_JSON),
            nest(
                path("/grade/visitor"),
                route(POST(""), visitorGradeHandler::gradeVisitor)
                    .andRoute(GET("/{username}"), visitorGradeHandler::getGrade)
            )
        )
    }

    @Bean
    fun routersRestaurant() = coRouter {
        "/grade".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                "/restaurant".nest {
                    POST("", restaurantGradeHandler::gradeRestaurant)
                    "/{id}".nest {
                        GET("", restaurantGradeHandler::getGrade)
                    }
                }
            }
        }
    }
}