package com.example.skin_lb;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

public class BaseActicity extends Activity {
    SkinFactory skinFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().setContext(this);
        skinFactory=new SkinFactory();
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),skinFactory);
    }
    public void apply(){
        skinFactory.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        skinFactory.apply();
    }
}
