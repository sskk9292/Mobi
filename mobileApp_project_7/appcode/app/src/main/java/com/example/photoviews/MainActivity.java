package com.example.photoviews;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int[] imgs = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};//图片数据
    int len = 0;//数组第一个长度
    private ImageView mImg;//图片切换器
    private Button button;
    // private ImageButton button1;

    //----------weather------------
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    //TextView tv_WeatherInfo;
    ForeCastManager mForeCast;
    private LocationManager locationManager;
    MainActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;
    String weather = "";
    String data = "";
    double[][] loc ={{0,0},{4,-12},
            {13,-12},{5,-20},{0,11}};
    //------------weather------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------weather------------
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
        //------------weather------------

        ImageButton top = findViewById(R.id.top);
        ImageButton bottom = findViewById(R.id.bottom);
        mImg = findViewById(R.id.img);
        top.setOnClickListener(this);
        bottom.setOnClickListener(this);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Initialize();
                Intent intent = new Intent();
                intent.setClass(com.example.photoviews.MainActivity.this,Main2Activity.class);
                intent.putExtra("weather",weather);
                intent.putExtra("data",data);
                intent.putExtra("number",Integer.toString(len));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int ID = v.getId();

        if (R.id.bottom == ID) {
            //当点击下一张的时候,长度变成+1
            len = len + 1;
            //如果下一张图片超过最大数量的图片便开始重置为0
            if (len >= imgs.length) {
                len = 0;
            }
        } else {
            //当点击上一张的时候，长度变为-1
            len = len - 1;
            //不说了，道理谁   都懂
            if (len < 0) {
                len = imgs.length - 1;
            }
        }
        mImg.setImageResource(imgs[len]);
        /**
         * 真正处理照片
         */
    }

    public void Initialize() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            String lon = Double.toString(location.getLongitude()+loc[len][0]);
            String lat = Double.toString(location.getLatitude()+loc[len][1]);
            //tv_WeatherInfo = (TextView) findViewById(R.id.textView);
            mWeatherInfomation = new ArrayList<>();
            mThis = this;
            mForeCast = new ForeCastManager(lon, lat, mThis);
            mForeCast.run();
            mForeCast.getmWeather();
            mWeatherData = mForeCast.getmWeather();

            DataToInformation(); // 자료 클래스로 저장,

            DataChangedToHangeul();
            data = PrintValue();
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

    void Icon(String weather){
        this.weather = weather;
    }
}