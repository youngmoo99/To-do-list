package com.youngmoo.todolistapp.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.room.RoomDatabase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.youngmoo.todolistapp.adapter.TodoAdapter
import com.youngmoo.todolistapp.database.TodoDatabase
import com.youngmoo.todolistapp.databinding.ActivityListMainBinding
import com.youngmoo.todolistapp.databinding.DialogEditBinding
import com.youngmoo.todolistapp.model.TodoInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListMainBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var roomDatabase: TodoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //모바일 광고 sdk초기화
        MobileAds.initialize(this) {}

        //하단 배너 광고 로드
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        //어댑터 인스턴스 생성
        todoAdapter = TodoAdapter()

        //리사이클러뷰에 어댑터 세팅
        binding.rvTodo.adapter = todoAdapter

        // 룸 데이터베이스 초기화
        roomDatabase = TodoDatabase.getInstance(applicationContext)!!

        // 전체 데이터 load (비동기)
        CoroutineScope(Dispatchers.IO).launch {
            val lstTodo = roomDatabase.todoDao().getAllReadData() as ArrayList<TodoInfo>
            for (todoItem in lstTodo){
                todoAdapter.addListItem(todoItem)
            }
            // ui thread에서 처리
            runOnUiThread {
                todoAdapter.notifyDataSetChanged()
            }

        }


        //작성하기 버튼 클릭
        binding.btnWrite.setOnClickListener {
            val bindingDialog = DialogEditBinding.inflate(LayoutInflater.from(binding.root.context), binding.root,false)

            AlertDialog.Builder(this)
                .setTitle("To-Do 남기기")
                .setView(bindingDialog.root)
                .setPositiveButton("작성완료", DialogInterface.OnClickListener { dialogInterface, i ->
                    val todoItem = TodoInfo()
                    todoItem.todoContent = bindingDialog.etMemo.text.toString() //사용자가 입력한 값을 가져옴
                    todoItem.todoDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    todoAdapter.addListItem(todoItem) // 어댑터의 전역변수의 arrayList 쪽에 아이템 추가하기 위한 메소드 호출
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDatabase.todoDao().insertTodoData(todoItem)

                        runOnUiThread {
                            todoAdapter.notifyDataSetChanged() // 리스트 새로고침침
                        }
                    }

                })
                .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }
    }
}