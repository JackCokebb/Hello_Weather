package edu.skku.cs.personalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.BuildConfig;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddLocActivity extends AppCompatActivity implements OnMapReadyCallback {

    Marker currentMarker = null;
    Button GpsBtn;
    Button searchBtn;
    Button useLocBtn;
    TextView currLoc;
    EditText editTextsearchLoc;
    GoogleMap gmap;
    double cur_lat_d;
    double cur_lon_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loc);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        cur_lat_d = 0;
        cur_lon_d = 0;

        currLoc = findViewById(R.id.resultLoc);
        editTextsearchLoc = findViewById(R.id.editTextsearchLoc);
        GpsBtn = findViewById(R.id.GpsBTn);
        searchBtn = findViewById(R.id.LocSearchBtn);
        useLocBtn = findViewById(R.id.UseLocBtn);
        GpsBtn.setOnClickListener(view -> {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddLocActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {
                gmap.setMyLocationEnabled(true);
            }
            Location loc_cur = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc_cur != null) {
                Log.d("test msg", "imininin!");
                cur_lat_d = loc_cur.getLatitude();
                cur_lon_d = loc_cur.getLongitude();
                String cur_loc_d = cur_lat_d + "," + cur_lon_d;

                Log.d("test lat", "" + cur_lat_d);
                Log.d("test lon", "" + cur_lon_d);

                LatLng currCor = new LatLng(cur_lat_d, cur_lon_d);
                if (currentMarker != null) currentMarker.remove();
                currentMarker = gmap.addMarker(new MarkerOptions()
                        .position(currCor)
                        .title("your position"));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currCor, 15);
                gmap.moveCamera(cameraUpdate);

                OkHttpClient client2 = new OkHttpClient();
                HttpUrl.Builder urlBuilder2 = HttpUrl.parse("https://maps.googleapis.com/maps/api/geocode/json").newBuilder();
                urlBuilder2.addQueryParameter("latlng", cur_loc_d);
                urlBuilder2.addQueryParameter("key", GOOGLE_MAP_API_KEY);

                String url2 = urlBuilder2.build().toString();
                Log.d("url", url2);
                Request req2 = new Request.Builder().addHeader("Connection", "close").url(url2).build();

                client2.newCall(req2).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String myResponse2 = response.body().string();
                        Log.d("res", myResponse2);
                        Gson gson = new GsonBuilder().create();
                        final loc_res loc_info = gson.fromJson(myResponse2, loc_res.class);


                        AddLocActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currLoc.setText(loc_info.getResults()[0].getFormatted_address());
                            }
                        });
                    }
                });
            } else {
                Log.e("No loc", "error!");
                currLoc.setText("Can not find your location!! Try later");
            }
        });

        searchBtn.setOnClickListener(view -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Geocoder geocoder = new Geocoder(AddLocActivity.this);
                    List<Address> addrList;
                    String address_input = editTextsearchLoc.getText().toString();
                    try {
                        addrList = geocoder.getFromLocationName(address_input, 10);
                        if (addrList != null) {
                            String city = "";
                            String country = "";
                            if (addrList.size() == 0) {
                                AddLocActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currLoc.setText("올바른 주소를 입력해주세요. ");
                                    }
                                });
                            } else {
                                Address address = addrList.get(0);
                                cur_lat_d = address.getLatitude();
                                cur_lon_d = address.getLongitude();
                                LatLng currCor = new LatLng(cur_lat_d, cur_lon_d);
                                AddLocActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ActivityCompat.checkSelfPermission(AddLocActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddLocActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(AddLocActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                                        }
                                        gmap.setMyLocationEnabled(true);
                                        if (currentMarker != null) currentMarker.remove();
                                        currentMarker = gmap.addMarker(new MarkerOptions()
                                                .position(currCor)
                                                .title("your position"));
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currCor, 15);
                                        gmap.moveCamera(cameraUpdate);
                                        currLoc.setText(address.getAddressLine(0));
                                    }
                                });

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        });
        useLocBtn.setOnClickListener(view -> {
            if(cur_lat_d==0&&cur_lon_d==0){
                AddLocActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "get or search first your location!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                OkHttpClient cli = new OkHttpClient();
                final String lambda_URL = LAMBDA_BASE_URL + "setUserLoc";
                user_account account_info = new user_account();
                loc new_loc = new loc();
                account_info.setName(username);
                new_loc.setLat(cur_lat_d);
                new_loc.setLon(cur_lon_d);
                account_info.setRecent_loc(new_loc);
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
                        Log.d("loc add",res);

                        Intent intent = new Intent(AddLocActivity.this, LocListActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                });
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        LatLng seoul = new LatLng(37.5104, 126.9960);
        if (currentMarker != null) currentMarker.remove();
        currentMarker = googleMap.addMarker(new MarkerOptions()
                .position(seoul)
                .title("Marker in Seoul"));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(seoul, 15);
        googleMap.moveCamera(cameraUpdate);
    }
}
