package com.ip13.main.repository

import com.ip13.main.model.entity.BookingConstraint
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BookingConstraintRepository : CrudRepository<BookingConstraint, Int>