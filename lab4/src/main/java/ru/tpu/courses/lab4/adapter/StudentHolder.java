package ru.tpu.courses.lab4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab4.R;

public class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    public final TextView student;
    public final ImageView photo;

    StudentsAdapter.OnNoteClickListener onNoteClickListener;
    StudentsAdapter.OnNoteLongClickListener onNoteLongClickListener;

    public StudentHolder(ViewGroup parent, StudentsAdapter.OnNoteClickListener onNoteClickListener,
                         StudentsAdapter.OnNoteLongClickListener onNoteLongClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab4_item_student, parent, false));
        student = itemView.findViewById(R.id.student);
        photo = itemView.findViewById(R.id.student_photo);

        this.onNoteClickListener=onNoteClickListener;
        student.setOnClickListener(this);

        this.onNoteLongClickListener=onNoteLongClickListener;
        student.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onNoteClickListener.onNoteClick(getAdapterPosition());
    }


    @Override
    public boolean onLongClick(View view) {
        onNoteLongClickListener.onNoteLongClick(getAdapterPosition());
        return false;
    }
}
