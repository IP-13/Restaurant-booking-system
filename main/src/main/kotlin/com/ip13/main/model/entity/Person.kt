package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Person(
    @Id
    val id: Int,
    val name: String,
    val role: Role,
    // TODO()
    // перенести рейтинг в таблицу посетителей
    // и добавить рейтинг для ресторанов
    val rating: Float,
)