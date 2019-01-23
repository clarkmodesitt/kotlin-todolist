package com.example.todolist.model

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(name = "to_do_list", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("name"))))
class ToDoList (

        var name: String = "",

        @OneToMany(mappedBy = "toDoList", fetch = FetchType.LAZY)
        @Cascade(CascadeType.ALL)
        var items: List<ToDoItem> = arrayListOf(),


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1
)
{
    override fun toString(): String{
        return "name=$name"//, item=$item"
    }
}
