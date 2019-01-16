package com.aadya.testlibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aadya.mylibrary.Namo3d;

public class MainActivity extends AppCompatActivity {

    private Namo3d namo3d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        namo3d = new Namo3d(this);

    }

    public void onClickStart(View v){
        //ToasterMessage.startMaanKiBat(MainActivity.this);
        namo3d.startMaanKiBat();
    }

}
