package ru.tpu.courses.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.tpu.courses.lab1.Lab1KotlinActivity;
import ru.tpu.courses.lab2.Lab2KotlinActivity;
import ru.tpu.courses.lab3.Lab3Activity;
import ru.tpu.courses.lab4.Lab4Activity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lab1KotlinActivity lab1KotlinActivity = new Lab1KotlinActivity();
        Lab2KotlinActivity lab2KotlinActivity = new Lab2KotlinActivity();

        findViewById(R.id.lab1).setOnClickListener((v) -> startActivity(lab1KotlinActivity.newIntent(this)));
        findViewById(R.id.lab2).setOnClickListener((v) -> startActivity(lab2KotlinActivity.newIntent(this)));
        findViewById(R.id.lab3).setOnClickListener((v) -> startActivity(Lab3Activity.newIntent(this)));
        findViewById(R.id.lab4).setOnClickListener((v) -> startActivity(Lab4Activity.newIntent(this)));
        findViewById(R.id.lab5).setOnClickListener((v) -> { });
        findViewById(R.id.lab6).setOnClickListener((v) -> { });
    }

}
