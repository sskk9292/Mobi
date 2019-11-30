//http://warguss.blogspot.com/2016/01/openweather-2.html 코드참조
package com.example.weather_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    TextView tv_WeatherInfo;

    ForeCastManager mForeCast;

    private LocationManager locationManager;
    MainActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
    Date date = new Date();
    int hour = Integer.parseInt(simpleDateFormat.format(date));

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }

    public void Initialize() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            String lon = Double.toString(location.getLongitude());
            String lat = Double.toString(location.getLatitude());
            tv_WeatherInfo = (TextView) findViewById(R.id.tv_WeatherInfo);
            mWeatherInfomation = new ArrayList<>();
            mThis = this;
            mForeCast = new ForeCastManager(lon, lat, mThis);
            mForeCast.run();
        }
    }

    public String PrintValue() {
        String mData = "";
        for (int i = 0; i < mWeatherInfomation.size(); i++) {
            mWeatherInfomation.get(i).getWeather_Name();
            mData = mData
                    + mWeatherInfomation.get(i).getCloud_Name()
                    + " /구름양: " + mWeatherInfomation.get(i).getCloud_Amount() + "%" + "\r\n"
                    + mWeatherInfomation.get(i).getWind_Name()
                    + " /풍속: " + mWeatherInfomation.get(i).getWind_Speed() + " mps" + "\r\n"
                    +"현재 기온: "+ mWeatherInfomation.get(i).getTemp_Now() + "℃"
                    + "/최고 기온: " + mWeatherInfomation.get(i).getTemp_Max() + "℃"
                    + " /최저 기온: " + mWeatherInfomation.get(i).getTemp_Min() + "℃" + "\r\n"
                    + "습도: " + mWeatherInfomation.get(i).getHumidity() + "%";
            mData = mData + "\r\n" + "----------------------------------------------" + "\r\n";
        }
        return mData;
    }

    public void DataChangedToHangeul()
    {
        for(int i = 0 ; i <mWeatherInfomation.size(); i ++)
        {
            WeatherToHangeul mHangeul = new WeatherToHangeul(mWeatherInfomation.get(i));
            mWeatherInfomation.set(i,mHangeul.getHangeulWeather());
        }
    }

    public void DataToInformation()
    {
        for(int i = 0; i < mWeatherData.size(); i++)
        {
            mWeatherInfomation.add(new WeatherInfo(
                            String.valueOf(mWeatherData.get(i).get("temp_Now")), String.valueOf(mWeatherData.get(i).get("temp_Min")),  String.valueOf(mWeatherData.get(i).get("temp_Max")),
                            String.valueOf(mWeatherData.get(i).get("humidity")), String.valueOf(mWeatherData.get(i).get("wind_Speed")), String.valueOf(mWeatherData.get(i).get("wind_Name")),
                            String.valueOf(mWeatherData.get(i).get("wind_Direction")), String.valueOf(mWeatherData.get(i).get("wind_SortNumber")), String.valueOf(mWeatherData.get(i).get("wind_SortCode")),
                            String.valueOf(mWeatherData.get(i).get("cloud_Amount")), String.valueOf(mWeatherData.get(i).get("cloud_Name")), String.valueOf(mWeatherData.get(i).get("weather_Sort")),
                            String.valueOf(mWeatherData.get(i).get("weather_Name"))
                            )
                    );
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case THREAD_HANDLER_SUCCESS_INFO :
                    mForeCast.getmWeather();
                    mWeatherData = mForeCast.getmWeather();
                    if(mWeatherData.size() ==0)
                        tv_WeatherInfo.setText("데이터가 없습니다");

                    DataToInformation(); // 자료 클래스로 저장,

                    String data = "";
                    DataChangedToHangeul();
                    data = PrintValue();

                    tv_WeatherInfo.setText(data);
                    break;
                default:
                    break;
            }
        }
    };

    public void WeatherIcon(String weather) {
        int w = Integer.parseInt(weather);
        image = (ImageView)findViewById(R.id.imageView);
        if(w>=200 && w<= 232){
            if(w >= 202 && w <= 212){//thunder
                image.setImageResource(R.drawable.thunder);
            } else{//thunder and rain
                image.setImageResource(R.drawable.thunder_rain);
            }
        } else if((w >= 300 && w <= 321) || (w >= 500 && w <= 531)){//rain
            if((w >= 300 && w <= 312) || (w >= 500 && w <= 501) || w == 520){//rain weak
                image.setImageResource(R.drawable.rain_weak);
            } else{//rain strong
                image.setImageResource(R.drawable.rain_strong);
            }
        } else if(w >= 600 && w<= 622){//snow
            if(w == 602 || w >= 621){//snow strong
                image.setImageResource(R.drawable.snow_strong);
            } else {//snow weak

                image.setImageResource(R.drawable.snow_weak);
            }
        } else if(w == 800){//sunny
            if(hour<19 && hour>5){
                image.setImageResource(R.drawable.sunny_day);
            } else{
                image.setImageResource(R.drawable.sunny_night);
            }
        } else if(w >= 801 && w<= 803){//cloud
            if(hour<19 && hour>5){
                image.setImageResource(R.drawable.cloud_day);
            } else{
                image.setImageResource(R.drawable.cloud_night);
            }
        } else if(w == 804){//cloud strong
            image.setImageResource(R.drawable.cloud_strong);
        } else if(w >= 951 && w<= 955){//wind weak
            image.setImageResource(R.drawable.wind_weak);
        } else if((w >= 956 && w <= 962) || w == 771 || w == 781){//wind strong
            image.setImageResource(R.drawable.wind_strong);
        } else if(w >= 701 && w <= 762){//mist
            image.setImageResource(R.drawable.mist);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void onClickButton(View view) {
        Initialize();
    }
}
