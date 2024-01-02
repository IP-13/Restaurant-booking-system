package com.ip13.main.router

import com.ip13.main.handler.RestaurantGradeHandler
import com.ip13.main.handler.VisitorGradeHandler
import com.ip13.main.model.dto.request.VisitorGradeRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
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

    @RouterOperations(
        RouterOperation(
            path = "/grade/visitor",
            produces = [MediaType.APPLICATION_JSON_VALUE],
            method = [RequestMethod.POST],
            beanClass = VisitorGradeHandler::class,
            beanMethod = "gradeVisitor",
            operation = Operation(
                operationId = "gradeVisitor",
                requestBody = RequestBody(
                    description = "request body for grade visitor method",
                    required = true,
                    content = [
                        Content(
                            schema = Schema(implementation = VisitorGradeRequest::class)
                        )
                    ]
                ),
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        description = "successful operation",
                        content = [
                            Content(schema = Schema(implementation = Float::class))
                        ]
                    )
                ]
            )
        ),
        RouterOperation(
            path = "/grade/visitor/{username}",
            produces = [MediaType.APPLICATION_JSON_VALUE],
            method = [RequestMethod.GET],
            beanClass = VisitorGradeHandler::class,
            beanMethod = "getGrade",
            operation = Operation(
                operationId = "getGrade",
                parameters = [
                    Parameter(
                        `in` = ParameterIn.PATH,
                        name = "username",
                    )
                ],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        description = "successful operation",
                        content = [
                            Content(schema = Schema(implementation = Float::class))
                        ]
                    )
                ]
            )
        ),
    )
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