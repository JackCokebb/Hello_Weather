package edu.skku.cs.personalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //TextView tv;
    Button login_btn;
    Button signup_btn;
    EditText user_id_input;
    EditText user_passwd_input;
    final String lambda_URL = LAMBDA_BASE_URL + "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0 );
        }

        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.sign_up_btn);
        user_id_input = findViewById(R.id.editText_user_id);
        user_passwd_input = findViewById(R.id.editText_Password);

        login_btn.setOnClickListener(view -> {

            String id_input = user_id_input.getText().toString();
            String passwd_input = user_passwd_input.getText().toString();

            OkHttpClient cli = new OkHttpClient();

            user_account account_info = new user_account();
            account_info.setName(id_input);
            account_info.setPasswd(passwd_input);
            //Log.d("test", username_data.getUsername());

            Gson gson = new Gson();
            String json = gson.toJson(account_info, user_account.class);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(lambda_URL).newBuilder();

            String url = urlBuilder.build().toString();

            //POST + Json of user id and passwd to lambda server
            Request req = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"),json)).build();
            //Log.d("test2", req.toString());
            cli.newCall(req).enqueue(new Callback(){
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String res = response.body().string();
                    Gson res_gson = new GsonBuilder().create();
                    final login_res login_response_data = res_gson.fromJson(res, login_res.class);
                    if(login_response_data.isSuccess()){
                        Intent intent = new Intent(MainActivity.this, LocListActivity.class);
                        intent.putExtra("username", id_input);
                        startActivity(intent);
                    }
                    else{
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

            });

        });

        signup_btn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

    }
}