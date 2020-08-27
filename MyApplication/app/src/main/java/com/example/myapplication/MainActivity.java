package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.skin_lb.BaseActicity;

public class MainActivity extends BaseActicity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void changeSkin(View view){
        Intent intent=new Intent(MainActivity.this,TwoActivity.class);
        startActivity(intent);
    }
}
