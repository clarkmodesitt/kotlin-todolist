package com.example.todolist.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.example.todolist.model.ToDoItem
import com.example.todolist.model.ToDoList
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ToDoItemRepository: JpaRepository<ToDoItem, Long> {
    fun findByCategory(category: String?): List<ToDoItem>

    fun findByDone(done: Boolean): List<ToDoItem>

}