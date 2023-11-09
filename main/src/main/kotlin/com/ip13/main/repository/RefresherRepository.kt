package com.ip13.main.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

@NoRepositoryBean
interface RefresherRepository<T, ID : Serializable> : CrudRepository<T, ID> {
    fun refresh(t: T)
}