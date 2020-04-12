package ru.tpu.courses.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.tpu.courses.lab1.Lab1KotlinActivity;
import ru.tpu.courses.lab2.Lab2KotlinActivity;
import ru.tpu.courses.lab3.Lab3Activity;
import ru.tpu.courses.lab4.Lab4Activity;
import ru.tpu.courses.lab5.InfoActivity;
import ru.tpu.courses.lab5.Lab5Activity;

public class MainActivity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }
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
        findViewById(R.id.lab5).setOnClickListener((v) -> startActivity(Lab5Activity.newIntent(this)));
        //findViewById(R.id.testApi).setOnClickListener((v) -> startActivity(Lab5Activity.newIntent(this)));
    }

}
