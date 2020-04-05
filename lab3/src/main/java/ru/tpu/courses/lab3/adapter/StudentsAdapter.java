package ru.tpu.courses.lab3.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.tpu.courses.lab3.Lab3Activity;
import ru.tpu.courses.lab3.Student;


public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_STUDENT = 1;

    private List<Student> students = new ArrayList<>();
    private  OnNoteClickListener mOnNoteClickListener;
    private  OnNoteLongClickListener mOnNoteLongClickListener;

    public StudentsAdapter(OnNoteClickListener onNoteClickListener, OnNoteLongClickListener onNoteLongClickListener){
        this.mOnNoteClickListener=onNoteClickListener;
        this.mOnNoteLongClickListener=onNoteLongClickListener;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NUMBER:
                return new NumberHolder(parent);
            case TYPE_STUDENT:
                return new StudentHolder(parent, mOnNoteClickListener, mOnNoteLongClickListener);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NUMBER:
                NumberHolder numberHolder = (NumberHolder) holder;
                // Высчитыванием номер студента начиная с 1
                numberHolder.bind((position + 1) / 2);

                break;
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                Student student = students.get(position / 2);
                studentHolder.student.setText(
                        student.secondName + " " + student.firstName + " " + student.lastName
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return students.size() * 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_NUMBER : TYPE_STUDENT;
    }

    public void setStudents(List<Student> students) {
        //Collections.sort(students);
        this.students = students;
    }
    public Student getStudent(int position){
        return this.students.get(position/2);
    }
    public interface OnNoteClickListener{
        void onNoteClick(int position);
    }
    public interface OnNoteLongClickListener{
        void onNoteLongClick(int position);
    }
}
