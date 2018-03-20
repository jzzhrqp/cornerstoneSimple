package com.aoshuotec.iron.simpleDemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aoshuotec.iron.simpleDemo.databinding.ActivityMain3Binding;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain3Binding binding= DataBindingUtil.setContentView(this,R.layout.activity_main3);
        binding.setData(new data("name","title"));


    }

}
