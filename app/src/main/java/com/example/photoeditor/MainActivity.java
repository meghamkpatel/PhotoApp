package com.example.photoeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    Camera mCamera;
    private boolean isCameraInitialized;
    private ConstraintLayout canvasLayout = null;
    CameraPreview mPreview = null;
    Button capture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        canvasLayout = (ConstraintLayout)findViewById(R.id.container);

        mPreview = new CameraPreview(this, mCamera);
        mCamera = android.hardware.Camera.open();
        // Set this as the onTouchListener to process custom surfaceview ontouch event.
        mPreview.setOnTouchListener(this);

        // Add the custom surfaceview object to the layout.
        canvasLayout.addView(mPreview);
        capture = (Button) findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPreview.captureImage();
            }
        });
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view instanceof SurfaceView) {
            view.invalidate();

            float x = motionEvent.getX();
            float y = motionEvent.getY();

            mPreview.setStartX(x);

            mPreview.setStartY(y);
            // Create and set a red paint to custom surfaceview.
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            mPreview.setPaint(paint);

            mPreview.drawGraph();

            // Tell android os the onTouch event has been processed.
            return true;
        }
        else{
            // Tell android os the onTouch event has not been processed.
            return false;
        }
    }

    // Permission to use camera
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
    @SuppressLint("NewApi")
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS && grantResults.length > 0){
            if(arePermissionsDenied()){
                ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }else{
                onResume();
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if(isCameraInitialized){
            mPreview = new CameraPreview(this, mCamera);
            mPreview.setOnTouchListener(this);
            canvasLayout = findViewById(R.id.container);
            canvasLayout.addView(mPreview);
        }
    }


}
