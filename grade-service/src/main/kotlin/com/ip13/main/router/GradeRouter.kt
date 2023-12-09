package com.ip13.main.router

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
    fun routes(handler: VisitorGradeHandler): RouterFunction<ServerResponse> {
        return nest(
            path("/grade/visitor"),
            nest(
                (accept(MediaType.APPLICATION_JSON)),
                route(POST(""), handler::gradeVisitor)
                    .andRoute(GET("/{username}"), handler::getGrade)
            )
        )
    }
}