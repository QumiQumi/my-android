package ru.tpu.courses.lab1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.lab1_kotlin_activity.*

class Lab1KotlinActivity : AppCompatActivity() {


    fun newIntent(context: Context): Intent {
        return Intent(context, Lab1KotlinActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab1_kotlin_activity)
    }

    fun toastMe(view: View){
        val myToast=Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT)
        myToast.show()
    }
    fun countMe(view: View){

        var count:Int=Integer.parseInt(lab1_textview.text.toString())
        count++
        lab1_textview.text=count.toString()
    }
    fun rundomMe(view: View){
        val randomIntent=Intent(this,Lab1Kotlin_SecondActivity::class.java)
        val count=Integer.parseInt(lab1_textview.text.toString())
        randomIntent.putExtra(Lab1Kotlin_SecondActivity.TOTAL_COUNT, count)
        startActivity(randomIntent)
    }

}
