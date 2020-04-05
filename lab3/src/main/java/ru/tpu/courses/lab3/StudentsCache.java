package ru.tpu.courses.lab3;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class StudentsCache {
    private static final String TAG = "StudentsCache";
    private static StudentsCache instance;

    public static StudentsCache getInstance() {
        if (instance == null) {
            synchronized (StudentsCache.class) {
                if (instance == null) {
                    instance = new StudentsCache();
                }
            }
        }
        return instance;
    }

    private Set<Student> students = new LinkedHashSet<>();

    private StudentsCache() {
    }
    @NonNull
    public List<Student> getStudents() {

        List<Student> students = new LinkedList<>(this.students);
        //
        Collections.sort(students);
        for (Student p : students) {
            Log.d(TAG, p.secondName+" "+p.firstName+" "+p.lastName);
        }
        //sortStudents();
        return students;
    }

    public void addStudent(@NonNull Student student) {
        students.add(student);
    }
    public void editStudent(@NonNull Student student) {

        students.add(student);
    }
    public void removeStudent(@NonNull Student student) {
        students.remove(student);
    }
    public boolean contains(@NonNull Student student) {
        return students.contains(student);
    }
    public void sortStudents(){
        Log.d(TAG, "List: ");
        List<Student> peopleList = new LinkedList<Student>();
        peopleList.addAll(this.students);
        Collections.<Student>sort(peopleList);
        for (Student p : peopleList) {
            Log.d(TAG, p.secondName+" "+p.firstName+" "+p.lastName);
        }
        Log.d(TAG, "TreeSet: ");
        TreeSet<Student> treeSet = new TreeSet<Student>();
        treeSet.addAll(this.students);
        for (Student p : treeSet) {
            Log.d(TAG, p.secondName+" "+p.firstName+" "+p.lastName);
        }



    }
}
