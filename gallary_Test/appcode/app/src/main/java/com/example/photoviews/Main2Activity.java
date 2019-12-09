package com.example.photoviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    private TextView textView;
    private MediaPlayer mediaPlayer = null;
    private SeekBar mSeekBar = null;
    private EditText editText;
    private Button SaveBtn,GetBtn;
    private SharedPreferences sp ;

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
        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Date: 2019/5/17 16:12\n" +
                "Address: Sangyeok 3(sam)-dong, Buk-gu, Daegu 35.890615, 128.612013\n" +
                "Resolution: 6000*4000\n" +
                "Camera model: Canon EOS 200D\n" +
                "Aperture value: f/4\n");
        initMusic();
        editText = (EditText)findViewById(R.id.edittext1);

        SaveBtn = (Button)findViewById(R.id.savebutton);
        GetBtn = (Button)findViewById(R.id.getbutton);
    }
    public void Click(View view){
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        switch (view.getId()){
            case R.id.savebutton:
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("Value",editText.getText().toString().trim());
                edit.commit();
                break;
            case R.id.getbutton:
                String value = sp.getString("Value","Null");
                editText.setText(value);
                break;
        }
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
}
