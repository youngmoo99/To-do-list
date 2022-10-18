package com.youngmoo.todolistapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youngmoo.todolistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvHello.setText("안녕하세용 반가워여")
        binding.tvTest.setText("화이팅하십쇼")

        //findViewById 뷰바인딩으로 인해 안써도됨


        //액티비티가 생성될 때 호출되며 사용자 인터페이스 초기화 할 때 이곳에 구현
        println("kim onCreate !!! ")
    }

    override fun onStart() {
        super.onStart()
        //액티비가 사용자에게 보여지기 직전에 호출됨
        println("kim onStart !!!")
    }

    override fun onPostResume() {
        super.onPostResume()
        //액티비티가 사용자랑 상호작용 하기 직전에 호출됨.
        println("kimonpostresume !!!")
    }

    override fun onPause() {
        super.onPause()
        // 다른 액티비티가 보여지게 될때 호출됨.(중지상태)
        println("kimonpause !!!")
    }

    override fun onStop() {
        super.onStop()
        //액티비티가 사용자에게 완전히 보여지지 않을 때 호출됨
        println("kimonstop !!!")
    }

    override fun onDestroy() {
        super.onDestroy()
        //액티비티가 제거될때 호출됨
        println("kimondestory !!!")
    }

    override fun onRestart() {
        super.onRestart()
        //액티비티가 멈췄다가 다시 시작되기 전에 호출됨
        println("kimonRestart !!!")
    }
}