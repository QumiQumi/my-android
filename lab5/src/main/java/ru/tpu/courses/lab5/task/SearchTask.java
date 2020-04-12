package ru.tpu.courses.lab5.task;

import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import ru.tpu.courses.lab5.auth.ServiceGenerator;
import ru.tpu.courses.lab5.db.Repo;
import ru.tpu.courses.lab5.db.User;

public class SearchTask extends Task<User> {

    private static final String TAG = "SearchTask";
    private static OkHttpClient httpClient;
    private String username;
    private  String password;

    //Настройка клиента
    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (SearchTask.class) {
                if (httpClient == null) {
                    // Логирование запросов в logcat
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC);
                    httpClient = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .build();
                }
            }
        }
        return httpClient;
    }

    public SearchTask(@Nullable Observer<User> observer,  String username, String password) {
        super(observer);

        this.username= username;
        this.password= password;
    }

    @Override
    @WorkerThread
    protected User executeInBackground() throws Exception {
        Log.d(TAG, "executeInBackground: username "+ username+" password " + password);
        User user=connect(username,password);
        user.starred_url=user.starred_url.replace("{/owner}{/repo}","");
        user.repo=searchRepo(user.repos_url);
        user.favoriteRepo=searchRepo(user.starred_url);
        return user;
    }

    /**
     * Делаем запрос к АПИ гитхаба и возвращаем тело ответа
     */
    private List<Repo> searchRepo(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = getHttpClient().newCall(request).execute();
        if (response.code() != 200) {
            throw new Exception("api returned unexpected http code: " + response.code());
        }
        String mainBody=response.body().string();
        return parseRepo(mainBody);
    }

    /**
     * Парсинг ответа от АПИ в формате json. Используется интегриорванное средство Андроид, но
     * обычно используют библиотеки вроде Gson (https://github.com/google/gson)
     */
    private List<Repo> parseRepo(String response) throws JSONException {
        JSONArray responseJson = new JSONArray(response);
        List<Repo> repos = new ArrayList<>();
       // JSONArray items = responseJson.toJSONArray(responseJson.names());
        for (int i = 0; i < responseJson.length(); i++) {
            JSONObject repoJson = responseJson.getJSONObject(i);
            Repo repo = new Repo();
            repo.fullName = repoJson.getString("full_name");
            repo.url = repoJson.getString("html_url");
            repos.add(repo);
        }
        return repos;
    }

    public User connect(String username, String password) throws Exception {

        LoginService gitHubClient = ServiceGenerator.createService(LoginService.class, username, password);  //=getGitHubClient();

        // Fetch a list of the Github repositories.
        Call<User> call = gitHubClient.basicLogin();//= gitHubClient.reposForUser(username);
        retrofit2.Response<User> response = call.execute();
        //Синхронный метод
        if (response.code() != 200) {
            throw new Exception("api returned unexpected http code: " + response.code());
        }

        if (response.isSuccessful()) {
            Log.d(TAG, "Response " + response.body().login);
            return response.body();
        } else {
            // error response, no access to resource?
            Log.d(TAG, "Response code: " + response.code());
        }

        //Асинхронный метод
        /*
        call.enqueue(new Callback<User>() {
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
         */
        return null;
    }
    public interface LoginService {
        @GET("/user")
        Call<User> basicLogin();
    }


}
