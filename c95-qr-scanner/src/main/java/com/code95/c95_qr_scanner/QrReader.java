package com.code95.c95_qr_scanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Qr Reader class is a simple class used to scan Qr Codes
 * implemented based on google vision library.
 *
 * @Author Yara Abdelhakim
 * @Version 1.0.1
 * @Since 19/3/2018
 */

public class QrReader {

    private static final String TAG = QrReader.class.getSimpleName();

    private static Context mContext;
    private static SurfaceView mSurfaceView;
    private static OnQrScanned mOnQrScanned;
    private static BarcodeDetector mBarcodeDetector;
    private static CameraSource mCameraSource;

    /**
     * Method used to init the qr reader
     *
     * @param context used to make vibration
     * @param surfaceView the view that will act as camera preview
     * @param onQrScanned interface object to determine action taken after scanning Qr
     */
    public static void init(Context context, SurfaceView surfaceView, OnQrScanned onQrScanned) {
        mContext = context;
        mSurfaceView = surfaceView;
        mOnQrScanned = onQrScanned;

        mBarcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        mCameraSource = new CameraSource.Builder(context, mBarcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
    }

    /**
     * Method used to start scanning
     */
    public static void scan() {

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0) {
                    Vibrator vibrator = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(250);
                    mOnQrScanned.onResult(qrcodes.valueAt(0).rawValue);
                }

            }
        });
    }

    /**
     * Method used to start camera preview
     * must be called when camera permission is granted.
     */
    public static void startPreview() {
        if (ActivityCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                mCameraSource.start(mSurfaceView.getHolder());
            } catch (IOException exception) {
                Toast.makeText(mContext, "Unable to Start camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method used to stop the QrScanner.
     */
    public static void release() {
        mBarcodeDetector.release();
    }
    
}
