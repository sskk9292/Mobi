package com.example.photoviews;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int[] imgs = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};//图片数据
    int len    = 0;//数组第一个长度
    private ImageView mImg;//图片切换器
    private Button button;
    // private ImageButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton top = findViewById(R.id.top);
        ImageButton bottom = findViewById(R.id.bottom);
        mImg = findViewById(R.id.img);
        top.setOnClickListener(this);
        bottom.setOnClickListener(this);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.photoviews.MainActivity.this,Main2Activity.class);
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
        /**
         * 真正处理照片
         */
        if (ID == R.id.top) {
            mImg.setImageResource(imgs[len]);
        } else {
            mImg.setImageResource(imgs[len]);
        }

    }
}