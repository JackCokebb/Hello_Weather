package edu.skku.cs.personalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherInfoActivity extends AppCompatActivity implements QuoteContract.ContractForView{
    Button getWeatherBtn;
    Button getQuoteBtn;
    TextView userName;
    TextView userLoc;
    TextView weatherInfo;
    TextView weatherInfo2;
    TextView weatherInfo3;
    TextView weatherEmoji;
    TextView quote;
    private QuotePresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        getWeatherBtn = findViewById(R.id.getLoc_btn);
        getQuoteBtn = findViewById(R.id.getQuoteBtn);
        quote = findViewById(R.id.todaysQuote);
        userName = findViewById(R.id.user_name);
        userLoc = findViewById(R.id.user_location);
        weatherInfo = findViewById(R.id.weather_info);
        weatherInfo2 = findViewById(R.id.weather_info2);
        weatherInfo3 = findViewById(R.id.weather_info3);
        weatherEmoji = findViewById(R.id.weather_emoji);

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat",0);
        double lon = intent.getDoubleExtra("lon",0);



        int TO_GRID = 0;
        int TO_GPS = 1;
        double cur_lat_d;
        double cur_lon_d;
        LatXLngY cur_loc_xy = convertGRID_GPS(TO_GRID, 60,127);

        OkHttpClient client2 = new OkHttpClient();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( WeatherInfoActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0 );
        }
        Location loc_cur = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc_cur !=null){
            String cur_loc_d = lat+","+lon;
            cur_loc_xy = convertGRID_GPS(TO_GRID, lat,lon);

            Log.d("test lat", ""+lat);
            Log.d("test lon", ""+lon);

            HttpUrl.Builder urlBuilder2 = HttpUrl.parse("https://maps.googleapis.com/maps/api/geocode/json").newBuilder();
            urlBuilder2.addQueryParameter("latlng", cur_loc_d);
            urlBuilder2.addQueryParameter("key", GOOGLE_MAP_API_KEY);

            String url2 = urlBuilder2.build().toString();
            Log.d("url",url2);
            Request req2 = new Request.Builder().addHeader("Connection","close").url(url2).build();

            client2.newCall(req2).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse2 = response.body().string();
                    Log.d("res",myResponse2);
                    Gson gson = new GsonBuilder().create();
                    final loc_res loc_info = gson.fromJson(myResponse2, loc_res.class);


                    WeatherInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userLoc.setText(loc_info.getResults()[0].getFormatted_address());
                        }
                    });
                }
            });
        }else{
            Log.e("No loc", "error!");
            userLoc.setText("Can not find your location!! Try later");
        }

        LatXLngY finalCur_loc_xy = cur_loc_xy;
        getWeatherBtn.setOnClickListener(view -> {

            OkHttpClient client = new OkHttpClient();
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat_d = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat_t = new SimpleDateFormat("HHmm");

            String now_date = dateFormat_d.format(date);
            String now_time = dateFormat_t.format(date);
            int int_now_time = Integer.parseInt(now_time);
            if(int_now_time % 100 >=30){
                int_now_time=int_now_time-30;
            }else{
                int_now_time = int_now_time-70;
                if(int_now_time<0){
                    int_now_time = int_now_time+2400;
                }
            }
            now_time = Integer.toString(int_now_time);
            if(now_time.length()==3){
                now_time = "0"+now_time;
            }
            else if(now_time.length()==2){
                now_time = "00"+now_time;
            }else if(now_time.length()==1){
                now_time = "000"+now_time;
            }

            Log.d("test date", now_date);
            Log.d("test now_time", now_time);

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst").newBuilder();
            urlBuilder.addQueryParameter("serviceKey", WEATHER_API_KEY);
            urlBuilder.addQueryParameter("dataType", "json");
            urlBuilder.addQueryParameter("base_date", now_date);
            urlBuilder.addQueryParameter("base_time", now_time);
            urlBuilder.addQueryParameter("nx", ""+Math.round(finalCur_loc_xy.x));
            urlBuilder.addQueryParameter("ny", ""+Math.round(finalCur_loc_xy.y));
            urlBuilder.addQueryParameter("numOfRows", "1");
            urlBuilder.addQueryParameter("pageNo", "25"); //temperature data

            String url = urlBuilder.build().toString();
            Log.d("url",url);
            Request req = new Request.Builder().url(url).addHeader("Connection","close").build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Log.d("res",myResponse);
                    Gson gson = new GsonBuilder().create();
                    final weather_res temper = gson.fromJson(myResponse, weather_res.class);

                    item[] item = temper.getResponse().getBody().getItems().getItem();
                    Log.d("test loc", item.toString());
                    WeatherInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(item[0].getCategory().equals("T1H")) {
                                weatherInfo.setText("\uD83C\uDF21️Temperaute : "+item[0].getFcstValue()+"°C");
                            }
                        }
                    });
                }
            });
            //get sky info
            urlBuilder = HttpUrl.parse("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst").newBuilder();
            urlBuilder.addQueryParameter("serviceKey", WEATHER_API_KEY);
            urlBuilder.addQueryParameter("dataType", "json");
            urlBuilder.addQueryParameter("base_date", now_date);
            urlBuilder.addQueryParameter("base_time", now_time);
            urlBuilder.addQueryParameter("nx", ""+Math.round(finalCur_loc_xy.x));
            urlBuilder.addQueryParameter("ny", ""+Math.round(finalCur_loc_xy.y));
            urlBuilder.addQueryParameter("numOfRows", "1");
            urlBuilder.addQueryParameter("pageNo", "19");

            url = urlBuilder.build().toString();
            Log.d("url",url);
            req = new Request.Builder().url(url).build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Log.d("res",myResponse);
                    Gson gson = new GsonBuilder().create();
                    final weather_res temper = gson.fromJson(myResponse, weather_res.class);

                    item[] item = temper.getResponse().getBody().getItems().getItem();
                    Log.d("test loc", item.toString());
                    WeatherInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String cloudy = "";
                            String cloudy_emj="";

                            if(item[0].getCategory().equals("SKY")){

                                if(item[0].getFcstValue().equals("1")){
                                    cloudy ="Sunny";
                                    cloudy_emj="☀";

                                }
                                else if(item[0].getFcstValue().equals("3")){
                                    cloudy ="Little bit cloudy";
                                    cloudy_emj="⛅";
                                }
                                else if(item[0].getFcstValue().equals("4")){
                                    cloudy ="Cloudy";
                                    cloudy_emj="☁";
                                }
                                weatherInfo2.setText("\uD83C\uDF88 Sky : "+cloudy);
                                weatherEmoji.setText(cloudy_emj);
                            }
                        }
                    });
                }
            });
            // get rain or snow info
            urlBuilder = HttpUrl.parse("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst").newBuilder();
            urlBuilder.addQueryParameter("serviceKey", WEATHER_API_KEY);
            urlBuilder.addQueryParameter("dataType", "json");
            urlBuilder.addQueryParameter("base_date", now_date);
            urlBuilder.addQueryParameter("base_time", now_time);
            urlBuilder.addQueryParameter("nx", ""+Math.round(finalCur_loc_xy.x));
            urlBuilder.addQueryParameter("ny", ""+Math.round(finalCur_loc_xy.y));
            urlBuilder.addQueryParameter("numOfRows", "1");
            urlBuilder.addQueryParameter("pageNo", "7");

            url = urlBuilder.build().toString();
            Log.d("url",url);
            req = new Request.Builder().url(url).addHeader("Connection","close").build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Log.d("res",myResponse);
                    Gson gson = new GsonBuilder().create();
                    final weather_res temper = gson.fromJson(myResponse, weather_res.class);

                    item[] item = temper.getResponse().getBody().getItems().getItem();
                    Log.d("test loc", item.toString());
                    WeatherInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String rain="";

                            if(item[0].getCategory().equals("PTY")){

                                if(item[0].getFcstValue().equals("0")){
                                    rain ="Nope";
                                }
                                else if(item[0].getFcstValue().equals("1")){
                                    rain ="Rainy";
                                    weatherEmoji.setText("\uD83C\uDF27");
                                }
                                else if(item[0].getFcstValue().equals("2")){
                                    rain ="Rain + Snow";
                                    weatherEmoji.setText("\uD83C\uDF27"+" + "+"\uD83C\uDF28");
                                }
                                else if(item[0].getFcstValue().equals("3")){
                                    rain ="Snowy";
                                    weatherEmoji.setText("\uD83C\uDF28");
                                }
                                else if(item[0].getFcstValue().equals("5")){
                                    rain ="Rain drops";
                                    weatherEmoji.setText("\uD83D\uDCA7");
                                }
                                else if(item[0].getFcstValue().equals("6")){
                                    rain ="Rain drops with Snow";
                                    weatherEmoji.setText("\uD83D\uDCA7" +" + "+ "❄");
                                }
                                else if(item[0].getFcstValue().equals("7")){
                                    rain ="Snowy Wind";
                                    weatherEmoji.setText("❄");
                                }
                                weatherInfo3.setText("☔ Rain? : "+rain);
                            }
                        }
                    });
                }
            });
        });

        presenter = new QuotePresenter(this,new QuoteModel("No pain, No Gain") );
        presenter.onChanged();
        getQuoteBtn.setOnClickListener(view -> {
            presenter.onGetQuoteBtnClicked();
        });
    }

    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0;      // 격자 간격(km)
        double SLAT1 = 30.0;    // 투영 위도1(degree)
        double SLAT2 = 60.0;    // 투영 위도2(degree)
        double OLON = 126.0;    // 기준점 경도(degree)
        double OLAT = 38.0;     // 기준점 위도(degree)
        double XO = 43;         // 기준점 X좌표(GRID)
        double YO = 136;        // 기1준점 Y좌표(GRID)
        int TO_GRID = 0;
        int TO_GPS = 1;

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }

    @Override
    public void displayValue(String value) {
        quote.setText(value);
    }

    class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }

}