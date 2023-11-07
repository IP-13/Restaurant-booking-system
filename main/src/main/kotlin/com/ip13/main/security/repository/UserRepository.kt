package com.ip13.main.security.repository

import com.ip13.main.security.entity.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    // TODO() вынести функции в отдельный репозиторий, не в пакете security
    @Modifying
    @Transactional
    @Query("update user_t set roles = array_append(roles, :role) where user_t.id = :id", nativeQuery = true)
    fun addRole(
        @Param(value = "id")
        userId: Int,
        @Param(value = "role")
        newRole: String
    ): Int

    @Query("select (select count(*) from user_t where user_t.id = :id and :role = ANY(roles)) > 0", nativeQuery = true)
    fun checkRole(
        @Param(value = "id")
        userId: Int,
        @Param(value = "role")
        role: String
    ): Boolean

    @Query("select (select count(*) from user_t where id = :id) > 0", nativeQuery = true)
    fun checkUser(
        @Param(value = "id")
        userId: Int,
    ): Boolean

    @Query("select till_date from black_list where user_id = :user_id", nativeQuery = true)
    fun getExpirationDateFromBlackList(
        @Param(value = "user_id")
        userId: Int,
    ): LocalDateTime?

    @Transactional
    @Modifying
    @Query(
        "update user_t set num_of_grades = num_of_grades + 1, sum_of_grades = sum_of_grades + :grade " +
                "where id = :user_id",
        nativeQuery = true
    )
    fun addGrade(
        @Param("user_id")
        userId: Int,
        @Param("grade")
        grade: Int
    ): Int

    @Query(
        "select sum_of_grades from user_t where id = :user_id",
        nativeQuery = true
    )
    fun getSumOfGrades(
        @Param("user_id")
        userId: Int,
    ): Int

    @Query(
        "select num_of_grades from user_t where id = :user_id",
        nativeQuery = true
    )
    fun getNumOfGrades(
        @Param("user_id")
        userId: Int,
    ): Int

}