package com.example.photoviews;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    private TextView textView;
    private MediaPlayer mediaPlayer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Date: 2019/5/17 16:12\n" +
                "Address: Sangyeok 3(sam)-dong, Buk-gu, Daegu 35.890615, 128.612013\n" +
                "Resolution: 6000*4000\n" +
                "Camera model: Canon EOS 200D\n" +
                "Aperture value: f/4\n"+"\n"+"\n"+"Diary: The camera lens that I bought for a long time finally arrived today, so I took the camera and came to the campus with a new lens. Many flowers on the campus were opened. So I took this picture with my camera. The photo is beautiful and my mood is very good.\n");
        mediaPlayer = MediaPlayer.create(this,R.raw.music);
    }
    public void startMusic(View view){
        mediaPlayer.start();
    }
}
