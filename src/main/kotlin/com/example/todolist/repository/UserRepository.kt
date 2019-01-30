package com.example.todolist.repository

import com.example.todolist.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByName(name: String?): Optional<User>
}