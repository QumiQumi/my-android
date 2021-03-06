package ru.tpu.courses.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ru.tpu.courses.lab3.adapter.StudentsAdapter;

public class EditStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";
    public static final String STUDENT_ID= "student_id";
    private static final String TAG = "EditStudentActivity";
    private StudentsAdapter studentsAdapter;
    private Student studentToEdit;
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, EditStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity_edit_student);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        studentToEdit = getIntent().getParcelableExtra(Student.class.getCanonicalName());
        Log.d(TAG, "onCreate: student:"+studentToEdit.firstName);
        firstName = findViewById(R.id.first_name);
        firstName.setText(studentToEdit.firstName);
        secondName = findViewById(R.id.second_name);
        secondName.setText(studentToEdit.secondName);
        lastName = findViewById(R.id.last_name);
        lastName.setText(studentToEdit.lastName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Если пользователь нажал "назад", то просто закрываем Activity
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        // Если пользователь нажал "Сохранить"
        if (item.getItemId() == R.id.action_save) {
            // Создаём объект студента из введенных
            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString()
            );

            // Проверяем, что все поля были указаны
            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName)) {
                // Класс Toast позволяет показать системное уведомление поверх всего UI
                Toast.makeText(this, R.string.lab3_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }

            // Проверяем, что точно такого же студента в списке нет
            if (studentsCache.contains(student)) {
//                Toast.makeText(this, R.string.lab3_error_already_exists, Toast.LENGTH_LONG).show();
                finish();
                return true;
            }
            studentsCache.removeStudent(studentToEdit);
            // Сохраняем Intent с инфорамцией от этой Activity, который будет передан в onActivityResult
            // вызвавшей его Activity.
            Intent data = new Intent();
            // Сохраяем объект студента. Для того, чтобы сохранить объект класса, он должен реализовывать
            // интерфейс Parcelable или Serializable, т.к. Intent передаётся в виде бинарных данных
            data.putExtra(EXTRA_STUDENT, student);
            // Указываем resultCode и сам Intent, которые будут переданы вызвавшей нас Activity в методе
            // onActivityResult
            setResult(RESULT_OK, data);
            // Закрываем нашу Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
