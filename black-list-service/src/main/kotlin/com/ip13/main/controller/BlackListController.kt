package com.ip13.main.controller

import com.ip13.main.model.dto.BlackListRequest
import com.ip13.main.model.entity.BlackList
import com.ip13.main.service.BlackListService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/black-list")
class BlackListController(
    private val blackListService: BlackListService,
) {

    @GetMapping("/get/{userId}")
    fun getByUserId(
        @PathVariable
        userId: Int
    ): Flux<BlackList> {
        return Flux.fromIterable(blackListService.findByUserId(userId))
    }

    @PostMapping("/add")
    fun add(
        @RequestBody
        request: BlackListRequest,
    ): Mono<BlackList> {
        return Mono.just(blackListService.save(request))
    }

    @GetMapping("/get-all", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getAll(): Flux<BlackList> {
        return Flux.fromIterable(blackListService.findAll()).delayElements(Duration.ofSeconds(3))
    }
}