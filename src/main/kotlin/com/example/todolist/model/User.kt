package com.example.todolist.model

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("name"))))
class User (

        var name: String = "",

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        @Cascade(CascadeType.ALL)
        var lists: List<ToDoList> = arrayListOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1
)