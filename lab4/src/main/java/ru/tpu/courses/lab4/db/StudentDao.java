package ru.tpu.courses.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Collections;
import java.util.List;

@Dao
public interface StudentDao {
    @Query("SELECT * FROM student ORDER BY LOWER(second_name), LOWER(first_name), LOWER(last_name) ASC")
    List<Student> getAll();

    @Insert
    void insert(@NonNull Student student);

    @Query(
            "SELECT COUNT(*) FROM student WHERE " +
                    "first_name = :firstName AND " +
                    "second_name = :secondName AND " +
                    "last_name = :lastName AND " +
                    "photo_path= :photoPath"
    )
    int count(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, String photoPath);

    @Delete
    void delete(@NonNull Student student);

    @Update
    void update(@NonNull Student student);
}
