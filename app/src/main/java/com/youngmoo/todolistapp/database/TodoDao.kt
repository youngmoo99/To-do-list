package com.youngmoo.todolistapp.database

import androidx.room.*
import com.youngmoo.todolistapp.model.TodoInfo

@Dao
interface TodoDao {
    //CRUD
    // 데이터베이스 테이블에 삽입(추가)
    @Insert
    fun insertTodoData(todoInfo: TodoInfo)

    //기존에 존재하는 데이터를 수정
    @Update
    fun updateTodoData(todoInfo: TodoInfo)

    // 기존에 존재하는 데이터 삭제
    @Delete
    fun deleteTodoData(todoInfo: TodoInfo)

    @Query("SELECT * FROM TodoInfo ORDER BY todoDate DESC")
    fun getAllReadData(): List<TodoInfo>


}