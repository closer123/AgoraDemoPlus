package com.example.agorademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.agorademo.fragment.SwichExternalVideoFragment;

public class LocalVideoMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video_main);
        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment_container,new SwichExternalVideoFragment())   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();

    }


}