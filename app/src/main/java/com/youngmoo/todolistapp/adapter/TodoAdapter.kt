package com.youngmoo.todolistapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.youngmoo.todolistapp.database.TodoDatabase
import com.youngmoo.todolistapp.databinding.DialogEditBinding
import com.youngmoo.todolistapp.databinding.ListItemTodoBinding
import com.youngmoo.todolistapp.model.TodoInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private var lstTodo : ArrayList<TodoInfo> = ArrayList()
    private lateinit var roomDatabase: TodoDatabase


    fun addListItem(todoItem: TodoInfo) {
        lstTodo.add(0,todoItem)
    }
    inner class TodoViewHolder(private val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoInfo) {
            // 리스트 뷰 데이터를 UI에 연동
            binding.tvContent.setText(todoItem.todoContent)
            binding.tvDate.setText(todoItem.todoDate)

            //리스트 삭제버튼 클릭 연동
            binding.btnRemove.setOnClickListener {
                // 삭제 내부 로직 수행
                AlertDialog.Builder(binding.root.context)
                    .setTitle("[주의]")
                    .setMessage("삭제하실 경우 데이터는 복구되지 않습니다.\n정말로 삭제하시겠습니까?")
                    .setPositiveButton("제거",DialogInterface.OnClickListener { dialogInterface, i ->

                        CoroutineScope(Dispatchers.IO).launch {
                            val innerLstTodo = roomDatabase.todoDao().getAllReadData()
                            for (item in innerLstTodo){

                                if (item.todoContent == todoItem.todoContent && item.todoDate == todoItem.todoDate) {
                                    // delete to database item
                                    roomDatabase.todoDao().deleteTodoData(item)
                                }
                            }

                            //ui remove
                            lstTodo.remove(todoItem)
                            (binding.root.context as Activity).runOnUiThread {
                                notifyDataSetChanged()

                                //토스트 팝업메시지
                                Toast.makeText(binding.root.context, "제거되었습니다",Toast.LENGTH_SHORT).show()
                            }

                        }


                    })
                    .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()

            }
            //리스트 수정 클릭 연동
            binding.root.setOnClickListener {
                val bindingDialog = DialogEditBinding.inflate(LayoutInflater.from(binding.root.context), binding.root,false)
                    //기존에 작성된 데이터 보여주기
                bindingDialog.etMemo.setText(todoItem.todoContent)
                AlertDialog.Builder(binding.root.context)
                    .setTitle("To-Do 남기기")
                    .setView(bindingDialog.root)
                    .setPositiveButton("수정완료", DialogInterface.OnClickListener { dialogInterface, i ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val innerLstTodo = roomDatabase.todoDao().getAllReadData()
                            for (item in innerLstTodo){

                                if (item.todoContent == todoItem.todoContent && item.todoDate == todoItem.todoDate) {
                                    item.todoContent = bindingDialog.etMemo.text.toString() //사용자가 입력한 값을 가져옴
                                    item.todoDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                                    roomDatabase.todoDao().updateTodoData(item)
                                }
                            }
                            //ui modify
                            todoItem.todoContent = bindingDialog.etMemo.text.toString() //사용자가 입력한 값을 가져옴
                            todoItem.todoDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

                            //array list수정
                            lstTodo.set(adapterPosition, todoItem)


                            (binding.root.context as Activity).runOnUiThread {  notifyDataSetChanged() }
                        }

                    })
                    .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }

        }

    }

    //뷰홀더가 생성됨. (각 리스트 아이템 1개씩 구성될 때마다 이 오버라이드 메소드가 호출됨.)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val binding = ListItemTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        // 룸 데이터베이스 초기화
        roomDatabase = TodoDatabase.getInstance(binding.root.context)!!
        return TodoViewHolder(binding)
    }

    // 뷰홀더가 바인딩 (결합) 이루어질 때 해줘야 될 처리들을 구현현
    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        holder.bind(lstTodo[position])
    }

    //리스트 총 개수
    override fun getItemCount(): Int {
        return lstTodo.size

    }

}