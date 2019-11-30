package com.example.photoviews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    private TextView textView;
    private MediaPlayer mediaPlayer = null;
    private SeekBar mSeekBar = null;

    //----------weather------------
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
    Date date = new Date();
    int hour = Integer.parseInt(simpleDateFormat.format(date));
    private ImageView image;
    //----------weather------------

public void showProgress(){
    if(mediaPlayer != null && mediaPlayer.isPlaying()){
        mSeekBar.setProgress(mediaPlayer.getCurrentPosition());

    }
}
    class ProgressThread extends Thread {
        @SuppressLint("HandlerLeak")
        public Handler mHandler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 1) {
                    showProgress();
                }
            }
        };
        public void run() {
            super.run();
            while (mediaPlayer != null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                mHandler.sendMessage(Message.obtain(mHandler, 1));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        textView = (TextView)findViewById(R.id.textView);
        String weather = intent.getExtras().getString("weather");
        String data = intent.getExtras().getString("data");
        this.WeatherIcon(weather);
        textView.setText(data);
        /*textView.setText("Date: 2019/5/17 16:12\n" +
                "Address: Sangyeok 3(sam)-dong, Buk-gu, Daegu 35.890615, 128.612013\n" +
                "Resolution: 6000*4000\n" +
                "Camera model: Canon EOS 200D\n" +
                "Aperture value: f/4\n"+"\n"+"\n"+"Diary: The camera lens that I bought for a long time finally arrived today, so I took the camera and came to the campus with a new lens. Many flowers on the campus were opened. So I took this picture with my camera. The photo is beautiful and my mood is very good.\n");
        */initMusic();
    }
    protected void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
    private void initMusic(){
        mediaPlayer = MediaPlayer.create(this,R.raw.music);
        mSeekBar = (SeekBar)findViewById(R.id.seekbar);
        mSeekBar.setProgress(0);
        mSeekBar.setMax(0);

        ProgressThread thread = new ProgressThread();
        thread.setDaemon(true);
        thread.start();

    }
    public void startMusic(View view){
        Button button;
        button = (Button)findViewById(R.id.button1);
        button.setEnabled(false);
        button = (Button)findViewById(R.id.button2);
        button.setEnabled(true);

        mSeekBar.setProgress(0);
        mSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
    public void stopMusic(View view){
        Button button;
        button = (Button)findViewById(R.id.button1);
        button.setEnabled(true);
        button = (Button)findViewById(R.id.button2);
        button.setEnabled(false);

        mSeekBar.setProgress(0);
        mSeekBar.setMax(0);
        mediaPlayer.stop();
        initMusic();
    }
    public void pauseMusic(View view) {
        if (mediaPlayer.isPlaying()) {

            mediaPlayer.pause();

        } else {
            mediaPlayer.start();
            Button button = (Button) findViewById(R.id.button3);

        }
    }

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
}
