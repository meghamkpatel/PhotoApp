package com.example.photoeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final String[] PERMISSIONS = {
        Manifest.permission.CAMERA
    };
    private static final int REQUEST_PERMISSIONS = 34;
    private static final int PERMISSIONS_COUNT = 1;
    @SuppressLint("NewApi")
    private boolean arePermissionsDenied(){
        for(int i = 0; i < PERMISSIONS_COUNT; i++){
            if(checkSelfPermission(PERMISSIONS[i])!= PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }
    public void onRequestPermissionResult(int requestCode, String[] permiissions, int[] grantedResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
    }
}
