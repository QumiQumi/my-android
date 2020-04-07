package ru.tpu.courses.lab4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.tpu.courses.lab4.adapter.StudentsAdapter;
import ru.tpu.courses.lab4.add.AddStudentActivity;
import ru.tpu.courses.lab4.add.EditStudentActivity;
import ru.tpu.courses.lab4.db.Lab4Database;
import ru.tpu.courses.lab4.db.Student;
import ru.tpu.courses.lab4.db.StudentDao;


public class Lab4Activity extends AppCompatActivity implements StudentsAdapter.OnNoteClickListener, StudentsAdapter.OnNoteLongClickListener {

    private static final int REQUEST_STUDENT_ADD = 1;
    private static final int REQUEST_STUDENT_EDIT = 2;
    private static final String TAG = "Lab4Activity";
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab4Activity.class);
    }

    private StudentDao studentDao;

    private RecyclerView list;
    private FloatingActionButton fab;

    private StudentsAdapter studentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Получаем объект для выполнения запросов к БД. См. Lab4Database.
         */
        studentDao = Lab4Database.getInstance(this).studentDao();

        setTitle(getString(R.string.lab4_title, getClass().getSimpleName()));

        setContentView(R.layout.lab4_activity);
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        initRecyclerView();

        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(list);
    }
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(studentsAdapter = new StudentsAdapter(this,this));
        studentsAdapter.setStudents(studentDao.getAll());
        studentsAdapter.notifyDataSetChanged();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentDao.insert(student);

            studentsAdapter.setStudents(studentDao.getAll());
            //studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
            studentsAdapter.notifyDataSetChanged();
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
        if (requestCode == REQUEST_STUDENT_EDIT && resultCode == RESULT_OK) {
            Student student = EditStudentActivity.getResultStudent(data);

            studentDao.update(student);//edit

            studentsAdapter.setStudents(studentDao.getAll());
            //studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
            studentsAdapter.notifyDataSetChanged();
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position=viewHolder.getAdapterPosition();
            Student student=studentsAdapter.getStudent(position);
            Log.d(TAG, "onSwiped: swiped, position: " +position+", "+student.firstName);
            studentDao.delete(student);

            studentsAdapter.setStudents(studentDao.getAll());
            studentsAdapter.notifyDataSetChanged();
            //studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }

    };

    @Override
    public void onNoteClick(int position) {
        Student student=studentsAdapter.getStudent(position);
        Log.d(TAG, "onNoteClick: clicked, position: " +position+", "+student.firstName);
    }

    @Override
    public void onNoteLongClick(int position) {
        Student student=studentsAdapter.getStudent(position);
        Log.d(TAG, "onNoteLongClick: clicked, position: " +position+", "+student.firstName);
        Intent intent = EditStudentActivity.newIntent(this);
        intent.putExtra(student.getClass().getCanonicalName(), student);
        startActivityForResult(
                intent,
                REQUEST_STUDENT_EDIT
        );
    }
}
