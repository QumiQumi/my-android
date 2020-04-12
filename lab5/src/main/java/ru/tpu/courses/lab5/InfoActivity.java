package ru.tpu.courses.lab5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.tpu.courses.lab5.db.User;
import ru.tpu.courses.lab5.task.Observer;
import ru.tpu.courses.lab5.task.SearchTask;
import ru.tpu.courses.lab5.task.Task;

public class InfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String APP_PREFERENCES_USER = "User";
    SharedPreferences  mPrefs;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView avatar;
    TextView login;
    TextView bio;
    TextView email;
    TextView company;
    TextView repo;
    TextView favoriteRepo;

    private User user;
    private SearchTask task;
    private Boolean isSaved=false;
    private static final String TAG = "InfoActivity";

    private static Executor threadExecutor = Executors.newCachedThreadPool();
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, InfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab5_activity_info);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        mPrefs = getSharedPreferences(APP_PREFERENCES_USER, Context.MODE_PRIVATE);

        avatar = findViewById(R.id.lab5_avatar);
        login = findViewById(R.id.lab5_login);
        bio = findViewById(R.id.lab5_bio);
        email = findViewById(R.id.lab5_email);
        company = findViewById(R.id.lab5_company);
        repo = findViewById(R.id.lab5_repo);
        favoriteRepo = findViewById(R.id.lab5_favoriteRepo);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        user = getIntent().getParcelableExtra(User.class.getCanonicalName());
        isSaved=getIntent().getBooleanExtra("isSaved", false);
        setView();
        if(isSaved)
            updateView(user.login,user.password);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task!=null)
            task.unregisterObserver();
    }

    private void setView() {
        if (user.avatar_url != null) Picasso.with(this).load(user.avatar_url).into(avatar);
        if (user.login != null) login.setText("Login: " + user.login);
        if (user.bio != null) bio.setText("Bio: " + user.bio);
        if (user.email != null) email.setText("Email: " + user.email);
        if (user.company != null) company.setText("Company: " + user.company);
        if (user.repo.size() != 0) {
            String repoString = "Repositories: \n";
            for (int i = 0; i < user.repo.size(); i++) {
                repoString += user.repo.get(i).getName() + "\n";
            }
            repo.setText(repoString);
        }
        if (user.favoriteRepo.size() != 0) {
            String repoString = "Favorite repositories: \n";
            for (int i = 0; i < user.favoriteRepo.size(); i++) {
                repoString += user.favoriteRepo.get(i).getName() + "\n";
            }
            favoriteRepo.setText(repoString);
        }
        saveToPrefs(user);
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(false);
        updateView(user.login, user.password);
    }

    //Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab5_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_logout) {
            openQuitDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        openQuitDialog();
    }
    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Таки да!", (dialog, which) -> {
            clearPrefs();
            finish();
        });

        quitDialog.setNegativeButton("Нет", (dialog, which) -> {
        });

        quitDialog.show();

    }
    private void saveToPrefs(User user){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("User", json);
        prefsEditor.apply();
    }
    private void clearPrefs(){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.remove("User");
        prefsEditor.apply();
    }

    private void updateView(String username, String password){
        threadExecutor.execute(task=new SearchTask(searchObserver, username, password));
    }
    private Observer<User> searchObserver = new Observer<User>() {
        @Override
        public void onLoading(@NonNull Task<User> task) {
            Log.d(TAG, "onLoading");
        }

        @Override
        public void onSuccess(@NonNull Task<User> task, @Nullable User user) {
            setView();
            Toast.makeText(InfoActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(@NonNull Task<User> task, @NonNull Exception e) {
            Log.e(TAG, "onError", e);
            Toast.makeText(InfoActivity.this, "Невозможно подключиться для обновления", Toast.LENGTH_SHORT).show();
        }
    };

}
