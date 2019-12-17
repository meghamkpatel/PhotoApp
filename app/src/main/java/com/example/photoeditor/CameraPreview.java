package com.example.photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback{
    private SurfaceHolder mHolder = null;
    private SurfaceView mView;
    private Camera.PictureCallback rawCallback;
    private Camera.PictureCallback jpegCallback;
    private Camera.ShutterCallback shutterCallback;
    private Camera mCamera;
    private Paint paint = null;
    private float startX = -50;
    private float startY = -50;
    private float stopX = 50;
    private float stopY = 50;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        mCamera = camera;
        mView = (SurfaceView) findViewById(R.id.surfaceviewprev);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };
        shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            @SuppressLint("SdCardPath")
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format(
                            "/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(bytes);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + bytes.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }
        };
    }
    public void drawGraph()
    {
        mHolder = getHolder();
        // Get and lock canvas object from surfaceHolder.
        Canvas canvas = mHolder.lockCanvas();

        Paint surfaceBackground = new Paint();
        // Set the surfaceview background color.
        surfaceBackground.setColor(Color.BLACK);
        // Draw the surfaceview background color.
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), surfaceBackground);

        // Draw the circle.
        paint.setColor(Color.RED);
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        canvas.drawCircle(startX, startY, 10, paint);

        // Unlock the canvas object and post the new draw.
        mHolder.unlockCanvasAndPost(canvas);
    }

    public void captureImage(){
        mCamera.takePicture(this, null, null, this);
    }

    // surfaceView class
    public void surfaceCreated(SurfaceHolder holder){
        drawGraph();
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        mCamera = camera;
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(mCamera);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        paint = null;
    }
    public void setStartX(float x){this.startX = x; this.stopX = -x;}
    public void setStartY(float y){this.startY = y; this.stopY = -y;}
    public double getDistance(){return Math.sqrt(Math.pow(stopX-startX,2) + Math.pow(stopY-startY, 2));}
    public float getX(){return startX;}
    public float getY(){return -startY;}
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void onShutter() {
        Toast.makeText(getContext(), "Click!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

    }
}