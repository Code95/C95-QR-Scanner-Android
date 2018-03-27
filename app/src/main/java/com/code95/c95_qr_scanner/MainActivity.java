package com.code95.c95_qr_scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnQrScanned {

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 101;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    //Surface view used to show camera preview.
    SurfaceView surfaceView;
    //Text view to show result of qr after scanning.
    TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        resultTv = (TextView) findViewById(R.id.result_tv);

        //Initializing qr with context, SurfaceView and OnQrScanned object.
        QrReader.init(this, surfaceView, this);

        /*Qr scanner uses the camera , so the camera permission must be added to manifest and requested at runtime*/

        //Checking if camera permission is granted.
        if (ActivityCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            //Start scanning
            QrReader.scan();
        } else {
            //Request camera permission.
            ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION}, REQUEST_CAMERA_PERMISSION_CODE);
        }

    }

    //Called when user respond to requesting permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            //If camera permission is granted start the scanner.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Start camera preview
                QrReader.startPreview();
                //Start scanning
                QrReader.scan();
            }
        }
    }

    //Method implemented from OnQrScanned
    @Override
    public void onResult(final String barcodeValue) {
        resultTv.post(new Runnable() {
            @Override
            public void run() {
                //Setting Qr value to textView
                resultTv.setText(barcodeValue);
                //Stop scanning
                QrReader.release();
            }
        });
    }
}
