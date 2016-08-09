package com.example.maxim.preferencesserv;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {


    final String FILENAME = "file";
    Camera camera=null ;
    Camera.Parameters params;
    int time = 0;


    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));
                    String str;
                    while ((str = br.readLine()) != null) {
                        time = Integer.parseInt(str);
                    }

                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        new Thread(new Runnable() {
            @Override
            public void run() {
                flash();

            }
        }).start();


        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void flash() {

        if (time > 0 & camera==null) {
            camera = Camera.open();
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }
}

