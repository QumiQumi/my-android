package ru.tpu.courses.lab5.db;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Для передачи самописных объектов через {@link android.content.Intent} или
 * {@link android.os.Bundle}, необходимо реализовать интерфейс {@link Parcelable}. В нём описывается
 * как сохранить и восстановить объект используя примитивные свойства (String, int и т.д.).
 */
public class User implements Parcelable {// Comparable<User> ,

    @NonNull
    public String login;
    @NonNull
    public String avatar_url;
    @NonNull
    public String bio;
    @NonNull
    public String email;
    @NonNull

    public List<Repo> repo;
    @NonNull
    public List<Repo> favoriteRepo;
    @NonNull
    public String repos_url;
    @NonNull
    public String starred_url;
    @NonNull
    public String company;
    @NonNull
    public String password;


    public User(@NonNull String login,
                @NonNull String avatar,
                @NonNull String bio,
                @NonNull String email,
                @NonNull List<Repo> repo,
                @NonNull List<Repo> favoriteRepo,
                @NonNull String repos_url,
                @NonNull String starred_url,
                @NonNull String company,
                @NonNull String password

    ) {
        this.login = login;
        this.avatar_url=avatar;
        this.bio =bio;
        this.email=email;
        this.repo=repo;
        this.favoriteRepo=favoriteRepo;
        this.repos_url=repos_url;
        this.starred_url=starred_url;
        this.company=company;
        this.password=password;
    }

    protected User(Parcel in) {
        login = in.readString();
        avatar_url = in.readString();
        bio = in.readString();
        email = in.readString();
        if (in.readByte() == 0x01) {
            repo = new ArrayList<Repo>();
            in.readList(repo, Repo.class.getClassLoader());
        } else {
            repo = null;
        }
        if (in.readByte() == 0x01) {
            favoriteRepo = new ArrayList<Repo>();
            in.readList(favoriteRepo, Repo.class.getClassLoader());
        } else {
            favoriteRepo = null;
        }
        repos_url = in.readString();
        starred_url = in.readString();
        company=in.readString();
        password=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(avatar_url);
        dest.writeString(bio);
        dest.writeString(email);
        if (repo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(repo);
        }
        if (favoriteRepo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(favoriteRepo);
        }
        dest.writeString(repos_url);
        dest.writeString(starred_url);
        dest.writeString(company);
        dest.writeString(password);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof User)) return false;
//        User student = (User) o;
//        return lastName.equals(student.lastName) &&
//                firstName.equals(student.firstName) &&
//                secondName.equals(student.secondName);
//    }

//    @Override
//    public int hashCode() {
//        return ObjectsCompat.hash(lastName, firstName, secondName);
//    }

//    @Override
//    public int compareTo(User student) {
//        String fullName=secondName+firstName+lastName;
//        String concat=student.secondName+student.firstName+student.lastName;
//        return fullName.compareTo(concat);
////		return secondName.compareTo(student.secondName);
//    }

