package com.example.todolist.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.example.todolist.model.ToDoList
import com.example.todolist.model.ToDoItem
import com.example.todolist.repository.ToDoListRepository
import com.example.todolist.repository.ToDoItemRepository
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@RestController
@RequestMapping("/api")
class RestAPIController {

    @Autowired
    lateinit var itemRepository: ToDoItemRepository

    @Autowired
    lateinit var listRepository: ToDoListRepository

    @RequestMapping("/save")
    fun save(): String{

        //prepare ToDoList data
        val work = ToDoList("Work")
        val home = ToDoList("Home")

        //ToDoItem data
        val item0 = ToDoItem("MEDIUM","Go to office",true, work)
        val item1 = ToDoItem("MEDIUM", "submit PR", false, work)

        val item2 = ToDoItem("LOW", "Walk the dog", true, home)
        val item3 = ToDoItem("HIGH", "Eat dinner", false, home)

        //set items in lists
        work.items = listOf(item0, item1)
        home.items = listOf(item2, item3)

        //save to do lists with items to database
        listRepository.saveAll(listOf(work, home))

        return "Success!"

    }

    @RequestMapping("/createitem")
    fun createItem(@RequestParam params: Map<String, String>): String {
        var categoryParam = params.get("category")
        var taskParam = params.get("task")
        var doneParam = params.get("done")!!.toBoolean()
        var listIdParam = params.get("list")!!.toLong()

        //get ToDoList using the id that was given with listIDParam. This will be used to save a new item.
        var listParam = listRepository.findById(listIdParam)

        itemRepository.save(ToDoItem(categoryParam, taskParam, doneParam, listParam.get().items.first().toDoList))
        return "New task was created for list #$listIdParam."
    }

    @RequestMapping("/createlist")
    fun createList(@RequestParam params: Map<String, String>): String {
        var listNameParam = params.get("listName")
        var categoryParam = params.get("category")
        var taskParam = params.get("task")
        var doneParam = params.get("done")!!.toBoolean()

        //Creating new list to add first item into
        var newList = ToDoList(listNameParam!!)

        //Add item into new list and save it
        newList.items = listOf(ToDoItem(categoryParam, taskParam, doneParam, newList))
        listRepository.save(newList)
        return "New list created: $listNameParam"
    }

    @RequestMapping("/findall")
    fun findAll(): List<ToDoList>{
        var result: List<ToDoList> = ArrayList()

        for(item in listRepository.findAll()){
            result += item
        }

        return result
    }

    @RequestMapping("/findlistbyname")
    fun fetchListByName(@RequestParam("name")name: String): Optional<ToDoList>{

        return listRepository.findByName(name)
    }

    @RequestMapping("/findlistbyid")
    fun fetchListById(@RequestParam("id")id: Long): Optional<ToDoList> {
        return listRepository.findById(id)
    }

    @RequestMapping("/findbydone")
    fun fetchDataByDone(@RequestParam params: Map<String, String>): List<ToDoItem> {
        var result: List<ToDoItem> = ArrayList()
        var listParam = params.get("list")!!.toLong()
        var doneParam = params.get("done")!!.toBoolean()

        for (item in itemRepository.findByDone(doneParam)) {
            if (item.toDoList?.id == listParam) {
                result += item
            }
        }

        return result
    }

    @RequestMapping("/findbycategory")
    fun fetchDataByCategory(@RequestParam params: Map<String, String>): List<ToDoItem>{
        var result: List<ToDoItem> = ArrayList()
        var listParam = params.get("list")!!.toLong()
        var categoryParam = params.get("category")

        for(item in itemRepository.findByCategory(categoryParam)){
            if (item.toDoList?.id == listParam) {
                result += item
            }
        }

        return result
    }

    @RequestMapping("/updatedone")
    fun markItemAsDone(@RequestParam("id") id: Long): String{
        var originalItem = itemRepository.findById(id)

        var updatedDoneStatus: Boolean = true

        if(originalItem.get().done == true) {
            updatedDoneStatus = false
        }

        itemRepository.save(ToDoItem(originalItem.get().category, originalItem.get().task, updatedDoneStatus, originalItem.get().toDoList, originalItem.get().id))
        return "Updated done status to $updatedDoneStatus"

    }

    @RequestMapping("/upgradecategory")
    fun upgradeCategory(@RequestParam("id") id: Long): String{
        var originalItem = itemRepository.findById(id)

        var newCategory: String = "MEDIUM"

        if(originalItem.get().category == "MEDIUM") {
            newCategory = "HIGH"
        }
        else if(originalItem.get().category == "HIGH") {
            newCategory = "TOO HIGH"
        }

        if(newCategory == "TOO HIGH") {
            return "Category can't be upgraded past HIGH"
        }
        else {
            itemRepository.save(ToDoItem(newCategory, originalItem.get().task, originalItem.get().done, originalItem.get().toDoList, originalItem.get().id))
            return "Category upgraded to $newCategory"
        }
    }

    @RequestMapping("/downgradecategory")
    fun downgradeCategory(@RequestParam("id") id: Long): String{
        var originalItem = itemRepository.findById(id)

        var newCategory: String = "MEDIUM"

        if(originalItem.get().category == "MEDIUM") {
            newCategory = "LOW"
        }
        else if(originalItem.get().category == "LOW") {
            newCategory = "TOO LOW"
        }

        if(newCategory == "TOO LOW") {
            return "Category can't be downgraded past LOW"
        }
        else {
            itemRepository.save(ToDoItem(newCategory, originalItem.get().task, originalItem.get().done, originalItem.get().toDoList, originalItem.get().id))
            return "Category downgraded to $newCategory"
        }
    }

    @RequestMapping("/deleteall")
    fun deleteAll(): String{
        listRepository.deleteAll()
        return "Items Deleted"
    }

    @RequestMapping("deletebyid")
    fun deleteById(@RequestParam("id") id: Long): String{
        itemRepository.deleteById(id)
        return "Item set to id #$id was deleted."
    }

    @RequestMapping("deletelist")
    fun deleteList(@RequestParam("id") id: Long): String{
        listRepository.deleteById(id)
        return "List set to id #$id was deleted."
    }

}

