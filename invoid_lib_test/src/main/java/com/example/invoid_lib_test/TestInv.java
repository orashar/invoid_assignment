package com.example.invoid_lib_test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TestInv {
    public static void t(Context c, String message){
        Log.v("TEST", "entered in invoid lib");
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
