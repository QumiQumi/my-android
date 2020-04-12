package com.example.testapi;

import androidx.annotation.NonNull;

public class User {

    @NonNull
    public String login;
    @NonNull
    public String avatar;
    @NonNull
    public String bio;
    @NonNull
    public String email;
//    @NonNull
//    public List<Repo> repo;
//    @NonNull
//    public List<Repo> favoriteRepo;

    public User(@NonNull String login,
                @NonNull String avatar,
                @NonNull String bio,
                @NonNull String email
//                @NonNull List<Repo> repo,
//                @NonNull List<Repo> favoriteRepo
    ) {
        this.login = login;
        this.avatar=avatar;
        this.bio =bio;
        this.email=email;
//        this.repo=repo;
//        this.favoriteRepo=favoriteRepo;
    }
}
