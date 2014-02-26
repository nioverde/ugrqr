package com.ferGTech.ugrqr;



import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

public class DecoderActivity extends Activity implements OnQRCodeReadListener {

    private TextView myTextView;
	private QRCodeReaderView mydecoderview;
	
	int READ_CODE = 0;
	
	Bundle options;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decoder);
        
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        
        myTextView = (TextView) findViewById(R.id.exampleTextView);
        
        
        options = getIntent().getExtras();
    }

    
    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
	@Override
	public void onQRCodeRead(String text, PointF[] points) 
	{
		myTextView.setText(text);
		
		
		//Intent inten = new Intent(DecoderActivity.this, CapturaQR.class);
		//inten.putExtra("SCAN_RESULT", text);
		//setResult(RESULT_OK, inten);
		
		Intent resul = new Intent(DecoderActivity.this, CapturaQR.class);
		resul.putExtra("SCAN_RESULT", text.toString());
		//resul.putExtra("SCAN_RESULT_FORMAT", "QR");
		setResult(RESULT_OK, resul);
		finish();
		//startActivityForResult(resul, READ_CODE);
		//finishActivity(RESULT_OK);
		
		
		
		//setResult(RESULT_OK);
		//finish();
	}


	// Called when your device have no camera
	@Override
	public void cameraNotFound() {

	}

	// Called when there's no QR codes in the camera preview image
	@Override
	public void QRCodeNotFoundOnCamImage() {

	}
    
	@Override
	protected void onResume() {
		super.onResume();
		mydecoderview.getCameraManager().startPreview();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mydecoderview.getCameraManager().stopPreview();
	}
}