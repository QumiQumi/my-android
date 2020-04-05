package ru.tpu.courses.lab3;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.tpu.courses.lab3.adapter.StudentHolder;
import ru.tpu.courses.lab3.adapter.StudentsAdapter;

/**
 * <b>RecyclerView, взаимодействие между экранами. Memory Cache.</b>
 * <p>
 * View, добавленные в {@link android.widget.ScrollView} отрисовываются все разом, при этом выводится
 * пользователю только та часть, до которой доскроллил пользователь. Соответственно, это замедляет
 * работу приложения и в случае с особо большим количеством View может привести к
 * {@link OutOfMemoryError}, краша приложение, т.к. система не может уместить все View в памяти.
 * </p>
 * <p>
 * {@link RecyclerView} - компонент для работы со списками, содержащими большое количество данных,
 * который призван исправить эту проблему. Это точно такой же {@link android.view.ViewGroup}, как и
 * ScrollView, но он содержит только те View, которые сейчас видимы пользователю. Работать с ним
 * намного сложнее, чем с ScrollView, поэтому если известно, что контент на экране статичен и не
 * содержит много элементов, то для простоты лучше воспользоваться ScrollView.
 * </p>
 * <p>
 * Для работы RecyclerView необходимо подключить отдельную библиотеку (см. build.gradle)
 * </p>
 */
public class Lab3Activity extends AppCompatActivity implements StudentsAdapter.OnNoteClickListener, StudentsAdapter.OnNoteLongClickListener {

    private static final int REQUEST_STUDENT_ADD = 1;
    private static final int REQUEST_STUDENT_EDIT = 2;
    private static final String TAG = "MY";
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab3Activity.class);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private RecyclerView list;
    private FloatingActionButton fab;

    private StudentsAdapter studentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab3_title, getClass().getSimpleName()));

        setContentView(R.layout.lab3_activity);
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
        studentsAdapter.setStudents(studentsCache.getStudents());
        studentsAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode " + requestCode + " resultCode" + resultCode);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentsCache.addStudent(student);

            studentsAdapter.setStudents(studentsCache.getStudents());
            studentsAdapter.notifyDataSetChanged();
            //studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
        if (requestCode == REQUEST_STUDENT_EDIT && resultCode == RESULT_OK) {
            Student student = EditStudentActivity.getResultStudent(data);

            studentsCache.editStudent(student);

            studentsAdapter.setStudents(studentsCache.getStudents());
            studentsAdapter.notifyDataSetChanged();
            //studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
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
            studentsCache.removeStudent(student);

            studentsAdapter.setStudents(studentsCache.getStudents());
            studentsAdapter.notifyDataSetChanged();
            studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
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
