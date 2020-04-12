package ru.tpu.courses.lab5.cache;

import androidx.annotation.NonNull;

import ru.tpu.courses.lab5.db.User;

public class UserCache {
    private static UserCache instance;

    public static UserCache getInstance() {
        if (instance == null) {
            synchronized (UserCache.class) {
                if (instance == null) {
                    instance = new UserCache();
                }
            }
        }
        return instance;
    }

    private User user;
    public void saveUser(@NonNull User user){
        this.user=user;
    }
    public User getUser(){
        return user;
    }
    public void deleteUser(){
        this.user=null;
    }

}
