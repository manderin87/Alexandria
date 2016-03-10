package com.hydeudacityproject.alexandria.ScannerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class ScannerView extends Activity implements ZXingScannerView.ResultHandler {

    private static final String TAG = ScannerView.class.getSimpleName();

    private List<BarcodeFormat> mFormats = new ArrayList<>();

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        mFormats.add(BarcodeFormat.EAN_13);
        mFormats.add(BarcodeFormat.EAN_8);
        mScannerView.setFormats(mFormats);
        mScannerView.setFlash(true);
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent result_intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putString("isbn" , rawResult.getText());
        result_intent.putExtras(bundle);

        setResult(Activity.RESULT_OK, result_intent);

        finish();
    }


}
