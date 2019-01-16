package com.aadya.mylibrary;

import android.content.Context;
import android.content.Intent;

public class Namo3d {
    private Context context;

    public Namo3d(Context context) {
        this.context = context;
    }

    public void startMaanKiBat(){
        Intent intent = new Intent(context, MaanKiBat.class);
        context.startActivity(intent);
    }

}
