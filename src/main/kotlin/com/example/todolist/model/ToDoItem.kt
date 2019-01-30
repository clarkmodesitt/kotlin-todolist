package com.example.todolist.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "to_do_item", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("task"))))
class ToDoItem (

        @Column
        var category: String? = "MEDIUM",

        @Column
        var task: String? = "",

        @Column
        var done: Boolean = false,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "list_id")
        @Cascade(CascadeType.REFRESH, CascadeType.PERSIST)
        @JsonIgnore
        var toDoList: ToDoList? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1
)

{

    override fun toString(): String{
        return "category=$category, task=$task, done=$done, list=${toDoList?.name}"
    }

}

