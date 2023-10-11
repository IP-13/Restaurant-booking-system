package com.ip13.main.repository

import com.ip13.main.model.MyEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface ReserveRepository: CrudRepository<MyEntity, Int>