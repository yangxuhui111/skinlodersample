package com.example.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.example.skin_lb.BaseActicity;
import com.example.skin_lb.SkinManager;


public class TwoActivity extends BaseActicity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }

    public void changeSkinTwo(View view ){
        //加载皮肤包
        SkinManager.getInstance().loadApk(Environment.getExternalStorageDirectory()+"/skin.apk");
        //换肤
        apply();
    }
}
