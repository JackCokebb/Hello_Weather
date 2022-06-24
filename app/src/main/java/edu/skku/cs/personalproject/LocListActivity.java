package edu.skku.cs.personalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class LocListActivity extends AppCompatActivity {

    TextView listUserName;
    TextView listInfo;
    Button addLocBtn;
    ListView locList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_list);
        listUserName = findViewById(R.id.listUserName);
        listInfo = findViewById(R.id.listInfo);
        addLocBtn = findViewById(R.id.addListBtn);
        locList = findViewById(R.id.locListview);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        listUserName.setText("HELLO  "+username+"!!");

        addLocBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent(LocListActivity.this, AddLocActivity.class);
            intent2.putExtra("username", username);
            startActivity(intent2);
        });

        OkHttpClient cli = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(LAMBDA_BASE_URL + "getUserLoc").newBuilder();
        urlBuilder.addQueryParameter("name", username);

        String url = urlBuilder.build().toString();
        //POST + Json of user id and passwd to lambda server
        Request req = new Request.Builder().addHeader("Connection","close").url(url).build();
        Log.d("test2", req.toString());
        cli.newCall(req).enqueue(new Callback(){
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String res = response.body().string();
                Log.d("test res", res);
                Gson res_gson = new GsonBuilder().create();
                final recent_locs recent_locs_list = res_gson.fromJson(res, recent_locs.class);

                LoclistAdapter lAdapter = new LoclistAdapter(getApplicationContext(),recent_locs_list.getRecent_locs());
                LocListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locList.setAdapter(lAdapter);
                        //lAdapter.notifyDataSetChanged();
                    }
                });


            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

        });




    }
}