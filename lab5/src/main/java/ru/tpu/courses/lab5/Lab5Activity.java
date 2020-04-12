package ru.tpu.courses.lab5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.tpu.courses.lab5.cache.TempUserPref;
import ru.tpu.courses.lab5.cache.UserCache;
import ru.tpu.courses.lab5.db.Repo;
import ru.tpu.courses.lab5.db.User;
import ru.tpu.courses.lab5.task.Observer;
import ru.tpu.courses.lab5.task.SearchTask;
import ru.tpu.courses.lab5.task.Task;

public class Lab5Activity extends AppCompatActivity {

    private static final String TAG = Lab5Activity.class.getSimpleName();
    private static Executor threadExecutor = Executors.newCachedThreadPool();
    private String username;
    private String password;

    private EditText login;
    private EditText passwordForLogin;

    public ProgressBar progressBar;
    private Button btnLogIn;
    private SearchTask task;

    private TempUserPref userPref;
    SharedPreferences mPrefs;
    public static final String APP_PREFERENCES_USER = "User";

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }
    public void onButtonClick(View view) {
        if (isConnectedToInternet()) {
            username = login.getText().toString();
            password = passwordForLogin.getText().toString();

            if (username.trim().isEmpty() || password.trim().isEmpty())
                Toast.makeText(this, "Заполните пустые поля.", Toast.LENGTH_SHORT).show();
            else
                threadExecutor.execute(task=new SearchTask(searchObserver, username, password));
        }
        else
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));
        setContentView(R.layout.lab5_activity);
        mPrefs = getSharedPreferences(APP_PREFERENCES_USER, MODE_PRIVATE);
        userPref=new TempUserPref(this);
        progressBar= findViewById(R.id.lab5_progressbar);
        login=findViewById(R.id.lab5_login);
        passwordForLogin=findViewById(R.id.lab5_password);
        btnLogIn=findViewById(R.id.lab5_button);
        progressBar.setVisibility(View.GONE);

        login.setText(userPref.getLogin());
        passwordForLogin.setText(userPref.getPassword());


        //Если уже есть юзер в преференсах
        //Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        User user = new Gson().fromJson(json, User.class);
        if(user!=null){
            Log.d(TAG, "onCreate: Юзер из SharedPreferences! " + user.login);

            Intent intent = InfoActivity.newIntent(Lab5Activity.this);
            intent.putExtra(user.getClass().getCanonicalName(), user);
            intent.putExtra("isSaved", true);
            startActivity(intent);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
            userPref.setPasswods(login.getText().toString(), passwordForLogin.getText().toString());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task!=null)
        task.unregisterObserver();
    }

    private Observer<User> searchObserver = new Observer<User>() {
        @Override
        public void onLoading(@NonNull Task<User> task) {
            Log.d(TAG, "onLoading");
            loadingTrue();
        }

        @Override
        public void onSuccess(@NonNull Task<User> task, @Nullable User user) {
            Log.d(TAG, "onSuccess. User: "+user.login);
            user.password=passwordForLogin.getText().toString();
            Intent intent = InfoActivity.newIntent(Lab5Activity.this);
            intent.putExtra(user.getClass().getCanonicalName(), user);
            intent.putExtra("isSaved", false);
            loadingFalse();
            startActivity(intent);
        }

        @Override
        public void onError(@NonNull Task<User> task, @NonNull Exception e) {
            Log.d(TAG, "onError "+ e.toString()+"|");
            Toast.makeText(Lab5Activity.this, "Неправильный логин или пароль.", Toast.LENGTH_SHORT).show();
            loadingFalse();
        }
    };

    private void loadingTrue() {
        btnLogIn.setClickable(false);
        login.setEnabled(false);
        passwordForLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void loadingFalse() {
        btnLogIn.setClickable(true);
        login.setEnabled(true);
        passwordForLogin.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

}
