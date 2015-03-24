package com.mcochner.freeflashlight;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.pm.ActivityInfo;

public class MainActivity extends Activity {
    Parameters parameters;
    ImageButton btnSwitch;
    Boolean isFlashOn = false;

    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        btnSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFlashOn) {
                            turnOff();
                        } else {
                            turnOn();
                        }
                    }
                });
        turnOn();
    }

    private void turnOff() {

        if (camera != null) {
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            isFlashOn = false;
            btnSwitch.setImageResource(R.drawable.bulb_off);
        }
    }

    private void turnOn() {
        if (camera == null) {
            try {
                camera = Camera.open();
            } catch (RuntimeException e) {
                e.printStackTrace();
                finish();
                return;
            }
            btnSwitch.setImageResource(R.drawable.bulb_on);
            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            turnOff();
        }
    }

}
