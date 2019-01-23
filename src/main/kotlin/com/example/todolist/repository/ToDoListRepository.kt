package com.example.todolist.repository

import com.example.todolist.model.ToDoList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ToDoListRepository: JpaRepository<ToDoList, Long> {
    fun findByName(name: String?): Optional<ToDoList>
}