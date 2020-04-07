package ru.tpu.courses.lab4.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ru.tpu.courses.lab4.Const;
import ru.tpu.courses.lab4.R;
import ru.tpu.courses.lab4.adapter.StudentsAdapter;
import ru.tpu.courses.lab4.db.Lab4Database;
import ru.tpu.courses.lab4.db.Student;
import ru.tpu.courses.lab4.db.StudentDao;

public class EditStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";
    private static final int REQUEST_CAMERA = 0;
    private static final String TAG = "EditStudentActivity";
    private StudentsAdapter studentsAdapter;
    private Student studentToEdit;

    private StudentDao studentDao;

    private BitmapProcessor bitmapProcessor;

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private ImageView photo;

    private String photoPath;


    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, EditStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_activity_edit_student);

        bitmapProcessor = new BitmapProcessor(this);
        studentDao = Lab4Database.getInstance(this).studentDao();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        studentToEdit = getIntent().getParcelableExtra(Student.class.getCanonicalName());
        Log.d(TAG, "onCreate: student:"+studentToEdit.firstName);

        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        photo = findViewById(R.id.photo);

        firstName.setText(studentToEdit.firstName);
        secondName.setText(studentToEdit.secondName);
        lastName.setText(studentToEdit.lastName);
        photoPath = studentToEdit.photoPath;
        if (photoPath != null) {
            photo.setImageURI(Uri.parse(photoPath));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab4_add_student, menu);
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
            saveStudent();
            return true;
        }
        if (item.getItemId() == R.id.action_add_photo) {
            requestPhotoFromCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStudent() {


        // Проверяем, что все поля были указаны
        if (TextUtils.isEmpty(firstName.getText().toString()) ||
                TextUtils.isEmpty(secondName.getText().toString()) ||
                TextUtils.isEmpty(lastName.getText().toString())) {
            // Класс Toast позволяет показать системное уведомление поверх всего UI
            Toast.makeText(this, R.string.lab4_error_empty_fields, Toast.LENGTH_LONG).show();
            return;
        }

        // Проверяем, что точно такого же студента в списке нет
        if (studentDao.count(firstName.getText().toString(), secondName.getText().toString(), lastName.getText().toString(), photoPath) > 0) {
            Toast.makeText(
                    this,
                    R.string.lab4_error_already_exists,
                    Toast.LENGTH_LONG
            ).show();
            finish();

        }
        // сохраняем объект студента из введенных
        studentToEdit.firstName=firstName.getText().toString();
        studentToEdit.secondName=secondName.getText().toString();
        studentToEdit.lastName=lastName.getText().toString();
        studentToEdit.photoPath=photoPath;
//        Student student = new Student(
//                firstName.getText().toString(),
//                secondName.getText().toString(),
//                lastName.getText().toString(),
//                photoPath
//        );

        // Сохраняем Intent с инфорамцией от этой Activity, который будет передан в onActivityResult
        // вызвавшей его Activity.
        Intent data = new Intent();
        // Сохраяем объект студента. Для того, чтобы сохранить объект класса, он должен реализовывать
        // интерфейс Parcelable или Serializable, т.к. Intent передаётся в виде бинарных данных
        data.putExtra(EXTRA_STUDENT, studentToEdit);
        // Указываем resultCode и сам Intent, которые будут переданы вызвавшей нас Activity в методе
        // onActivityResult
        setResult(RESULT_OK, data);
        // Закрываем нашу Activity
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            try {
                Bitmap scaledPhoto = bitmapProcessor.readScaledBitmap(photoPath, 512, 512);
                scaledPhoto = bitmapProcessor.rotateBitmapIfNeed(scaledPhoto, photoPath);
                bitmapProcessor.saveBitmapToFile(scaledPhoto, photoPath);

                photo.setImageURI(Uri.parse(photoPath));
            } catch (IOException e) {
                Toast.makeText(this, "Не удалось получить фото", Toast.LENGTH_SHORT).show();
                photoPath = null;
                photo.setImageURI(null);
                e.printStackTrace();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Intent requestPhotoIntent(Uri photoFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
        return intent;
    }

    private void requestPhotoFromCamera() {
        try {
            if (!TextUtils.isEmpty(photoPath)) {
                new File(photoPath).delete();
            }
            File tempFile = bitmapProcessor.createTempFile();
            photoPath = tempFile.getPath();
            Uri photoUri = FileProvider.getUriForFile(
                    this,
                    Const.FILE_PROVIDER_AUTHORITY,
                    tempFile
            );
            Intent requestPhotoIntent = requestPhotoIntent(photoUri);
            startActivityForResult(requestPhotoIntent, REQUEST_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Не удалось создать файл для фотографии", Toast.LENGTH_SHORT
            ).show();
            photoPath = null;
        }
    }
}
