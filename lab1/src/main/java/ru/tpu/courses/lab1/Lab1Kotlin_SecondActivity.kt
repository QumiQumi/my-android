package ru.tpu.courses.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lab1_kotlin__second.*
import kotlin.random.Random

class Lab1Kotlin_SecondActivity : AppCompatActivity() {

    companion object{
        const val TOTAL_COUNT ="total_count"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab1_kotlin__second)
        showRandomNumber()
    }

    fun showRandomNumber(){
        val count=intent.getIntExtra(TOTAL_COUNT,0)
        var randomInt=0
        if(count>0){
            randomInt= Random.nextInt(count+1)
        }
        lab1_textview4.text= randomInt.toString()
        lab1_textview3.text=getString(R.string.random_heading,count)


    }
}
