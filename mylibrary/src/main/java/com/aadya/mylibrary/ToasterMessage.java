package com.aadya.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ToasterMessage {

    public static void show(Context c, String message){

        Toast.makeText(c,message, Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, MainA)

    }

    public static void startMaanKiBat(Context c)
    {
        Intent intent = new Intent(c, MaanKiBat.class);
        c.startActivity(intent);
    }
}
