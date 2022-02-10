package com.synerise.sdk.sample.ui.dev.qr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ShakeDetector;
import com.synerise.sdk.sample.util.qr.BarcodeGraphic;
import com.synerise.sdk.sample.util.qr.BarcodeGraphicTracker;
import com.synerise.sdk.sample.util.qr.BarcodeTrackerFactory;
import com.synerise.sdk.sample.util.qr.CameraSource;
import com.synerise.sdk.sample.util.qr.CameraSourcePreview;
import com.synerise.sdk.sample.util.qr.GraphicOverlay;

import java.io.IOException;

import javax.inject.Inject;

public final class QRScannerActivity extends BaseActivity implements BarcodeGraphicTracker.BarcodeUpdateListener {

    // intent request code to handle updating play services if needed.
    private static final int GOOGLE_SERVICES_CODE = 9001;

    // permission request codes need to be < 256
    private static final int CAMERA_PERMISSION = 2;

    private CameraSource cameraSource;
    private CameraSourcePreview cameraSourcePreview;
    private GraphicOverlay<BarcodeGraphic> graphicOverlay;

    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    // shake
    private Sensor sensor;
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;

    @Inject AccountManager accountManager;

    private TextInputLayout inputClient;

    public static Intent createIntent(Context context) {
        return new Intent(context, QRScannerActivity.class);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_scanner);
        ((App) getApplication()).getComponent().inject(this);

        cameraSourcePreview = findViewById(R.id.preview);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        inputClient = findViewById(R.id.input_client);
        findViewById(R.id.finish_scanning).setOnClickListener(v -> onFinishClicked());

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(count -> inputClient.getEditText().setText(R.string.synerise_client_api_key));

        int permissionResult = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
        if (sensorManager != null && sensor != null)
            sensorManager.registerListener(shakeDetector, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSourcePreview != null) cameraSourcePreview.stop();
        if (sensorManager != null) sensorManager.unregisterListener(shakeDetector);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSourcePreview != null) cameraSourcePreview.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);
        boolean c = gestureDetector.onTouchEvent(e);
        return b || c || super.onTouchEvent(e);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean onTap(float rawX, float rawY) {
        int[] location = new int[2];
        graphicOverlay.getLocationOnScreen(location);
        float x = (rawX - location[0]) / graphicOverlay.getWidthScaleFactor();
        float y = (rawY - location[1]) / graphicOverlay.getHeightScaleFactor();

        Barcode bestHit = null;
        float bestDistance = Float.MAX_VALUE;
        for (BarcodeGraphic graphic : graphicOverlay.getGraphics()) {
            Barcode barcode = graphic.getBarcode();
            if (barcode.getBoundingBox().contains((int) x, (int) y)) {
                bestHit = barcode;
                break;
            }
            float dx = x - barcode.getBoundingBox().centerX();
            float dy = y - barcode.getBoundingBox().centerY();
            float distance = (dx * dx) + (dy * dy);  // actually squared distance
            if (distance < bestDistance) {
                bestHit = barcode;
                bestDistance = distance;
            }
        }

        if (bestHit != null) {
            inputClient.getEditText().setText(bestHit.displayValue);
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    private void onFinishClicked() {
        accountManager.setClientApiKey(inputClient.getEditText().getText().toString());
        HandlerThread ht = new HandlerThread("HandlerThread");
        ht.start();
        new Handler(ht.getLooper()).post(() -> {
            ProcessPhoenix.triggerRebirth(QRScannerActivity.this);
        });
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        // scanner won't work without this method
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(graphicOverlay, this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;
            if (hasLowStorage)
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
        }

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(25.0f)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setFlashMode(null)
                .build();
    }

    private void startCameraSource() throws SecurityException {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance()
                                 .getErrorDialog(this, code, GOOGLE_SERVICES_CODE)
                                 .show();
        }
        if (cameraSource != null) {
            try {
                cameraSourcePreview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION);
            return;
        }

        View.OnClickListener listener = view -> ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION);

        findViewById(R.id.root_layout).setOnClickListener(listener);
        Snackbar.make(graphicOverlay, R.string.permission_camera_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != CAMERA_PERMISSION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
               .setMessage(R.string.no_camera_permission)
               .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
               .show();
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            cameraSource.doZoom(detector.getScaleFactor());
        }
    }
}