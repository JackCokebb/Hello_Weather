package edu.skku.cs.personalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignupActivity extends AppCompatActivity {

    Button submit_btn;
    Button cancel_btn;
    EditText signup_id_input;
    EditText signup_passwd_input;
    EditText signup_passwd_input_confirm;
    final String signup_URL = LAMBDA_BASE_URL + "adduser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        submit_btn = findViewById(R.id.submit);
        cancel_btn = findViewById(R.id.cancel_btn);
        signup_id_input = findViewById(R.id.editText_id_input);
        signup_passwd_input =findViewById(R.id.editText_input_Password);
        signup_passwd_input_confirm = findViewById(R.id.editText_input_Password_confirm);

        submit_btn.setOnClickListener(view -> {

            String id_input = signup_id_input.getText().toString();
            String passwd_input = signup_passwd_input.getText().toString();
            String passwd_input_confirm = signup_passwd_input_confirm.getText().toString();

            OkHttpClient cli = new OkHttpClient();

            if( passwd_input.equals(passwd_input_confirm)){
                user_account account_info = new user_account();
                account_info.setName(id_input);
                account_info.setPasswd(passwd_input);
                //Log.d("test", username_data.getUsername());

                Gson gson = new Gson();
                String json = gson.toJson(account_info, user_account.class);

                HttpUrl.Builder urlBuilder = HttpUrl.parse(signup_URL).newBuilder();

                String url = urlBuilder.build().toString();
                Request req = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                //Log.d("test2", req.toString());
                cli.newCall(req).enqueue(new Callback(){
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String res = response.body().string();
                        Gson res_gson = new GsonBuilder().create();
                        final login_res login_response_data = res_gson.fromJson(res, login_res.class);
                        if(login_response_data.isSuccess()){
                            SignupActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "sign up success!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            SignupActivity.this.runOnUiThread(new Runnable() {
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
            }
            else{  //passwd confirm wrong!!
                SignupActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "password confirm failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
        cancel_btn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);

            startActivity(intent);
        });

    }
}