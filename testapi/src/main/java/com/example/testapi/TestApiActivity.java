package com.example.testapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TestApiActivity extends AppCompatActivity {
    private static final String TAG = "TestApiActivity";
    private static OkHttpClient httpClient;
    //private static  GitHubClient gitHubClient;
    static final String username="Qumiqumi";
    static final String password="MyMeizuHD50";
    static String API_BASE_URL = "https://api.github.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        findViewById(R.id.button).setOnClickListener(
                v -> connect(username, password)
        );

    }
    public static GitHubClient getGitHubClient() {

                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                    Retrofit.Builder builder = new Retrofit.Builder()
                                    .baseUrl(API_BASE_URL)
                                    .addConverterFactory(
                                            GsonConverterFactory.create()
                                    );

                    Retrofit retrofit = builder
                                    .client(httpClient.build())
                                    .build();

                    GitHubClient client =  retrofit.create(GitHubClient.class);

        return client;
    }
    public void connect(String username, String password){
        LoginService gitHubClient= ServiceGenerator.createService(LoginService.class, username, password);  //=getGitHubClient();

        // Fetch a list of the Github repositories.
        Call<User> call =gitHubClient.basicLogin();//= gitHubClient.reposForUser(username);
        call.enqueue(new Callback<User >() {
             @Override
             public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                 if (response.isSuccessful()) {
                     Log.d(TAG, "Response " + response.body().login);
                 } else {
                     // error response, no access to resource?
                     Log.d(TAG, "Response code: " + response.code());
                 }
             }

             @Override
             public void onFailure(Call<User> call, Throwable t) {
                 // something went completely south (like no internet connection)
                 Log.d("Error", t.getMessage());
             }
        });

        // Execute the call asynchronously. Get a positive or negative callback.
//        call.enqueue(new Callback<List<GitHubRepo>>() {
//            @Override
//            public void onResponse(Call<List<GitHubRepo>> call, retrofit2.Response<List<GitHubRepo>> response) {
//                // The network call was a success and we got a response
//                // TODO: use the repository list and display it
//                //Log.d(TAG, "Response " + response.body().size());
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "Response: " + response.body().size());
//                } else {
//                    Log.d(TAG, "Response code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
//                // the network call was a failure
//                // TODO: handle error
//                Log.d(TAG, "Failure: "+ t);
//            }
//        });

    }


    public interface UserApi {

        @GET("user")
        Call<User> user();

    }
    public interface LoginService {
        @GET("/user")
        Call<User> basicLogin();
    }
    public interface GitHubClient {
        @GET("/users/{user}/repos")
        Call<List<GitHubRepo>> reposForUser(
                @Path("user") String user
        );
    }

    public class GitHubRepo {
        private int id;
        private String name;

        public GitHubRepo() {
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


}

