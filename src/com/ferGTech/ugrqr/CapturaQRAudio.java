package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CapturaQRAudio extends Activity implements OnClickListener, OnTouchListener, 
		OnCompletionListener, OnBufferingUpdateListener
{
	MediaPlayer mediaPlayer;
	boolean playPause;
	boolean intialStage = true;
	
	int mediaFileLengthInMilliseconds;
	String miparam1="", miparam2="";
    private final Handler handler = new Handler();
	Vibrator vbtor;
	
	int ene=0;
	Button play, bRuta, otroqr, bBusqueda; 
	Bundle extras;
	private SeekBar seekBarProgress;
	int codigo = 0;
	String selcad;
	String queTabla="";
	int READ_CODE = 0;
	boolean txtyaudio = false;
	TextView tv1, tv2, tv3, tv4;
	String cCanal, cConcepto="", cURL="", cURLHD, cCodqr="", cResumen="", cDDetallada="", cImagen="";
	String cConceptoExtra="", cUrlExtra="",cCodqrExtra="", resumenExtra="", ddetalladaExtra="";
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escaneoaudio);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
        play= (Button)this.findViewById(R.id.button1);
        play.setOnClickListener(this);
        
        bRuta=(Button)this.findViewById(R.id.button2);
        otroqr = (Button)findViewById(R.id.button4);
        bBusqueda = (Button)findViewById(R.id.button5);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        tv4 = (TextView)findViewById(R.id.textView4);
        
        seekBarProgress = (SeekBar)findViewById(R.id.seekBar1);
        seekBarProgress.setMax(99);
        seekBarProgress.setOnTouchListener(this);
        
        play.setVisibility(Button.INVISIBLE);
        bRuta.setVisibility(Button.INVISIBLE);
        seekBarProgress.setVisibility(SeekBar.INVISIBLE);
        
        // Aqui se recibe el codigo del canal
        extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		// y aqui se interpreta
		if (codigo == 4)
		{
			txtyaudio = false;
			selcad = getResources().getString(R.string.audesp);
		}
		else if (codigo == 5)
		{
			txtyaudio = false;
			selcad = getResources().getString(R.string.auding);
		}
		else if (codigo == 6)
		{
			txtyaudio = true;
			selcad = getResources().getString(R.string.audesp);
			
		}
		else if (codigo == 7)
		{
			txtyaudio = true;
			selcad = getResources().getString(R.string.auding);
			
		}
		
		
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		boolean isWifi = manager.getNetworkInfo( ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        Log.v("",is3g + " ConnectivityManager Test " + isWifi);
        /*if (!is3g && !isWifi) 
        {
            Toast.makeText(getApplicationContext(),"Por favor, asegúrate de que tu conexión a Internet está activada ",
                    Toast.LENGTH_LONG).show();
        }
        else if ( isWifi && !is3g )
        {
            Toast.makeText(getBaseContext(), "Audio HD", Toast.LENGTH_LONG).show();
            //new ConsultaBD(getApplicationContext()).execute(KEY_121);

        }
        else if ( !isWifi && is3g )
        {
            Toast.makeText(getBaseContext(), "Podrás escuchar locuciones en alta calidad mediante conexión WiFi", Toast.LENGTH_LONG).show();
            //new ConsultaBD(getApplicationContext()).execute(KEY_121);
        }
        else if ( isWifi && is3g )
        {
            Toast.makeText(getBaseContext(), "ambos...", Toast.LENGTH_LONG).show();
            //new ConsultaBD(getApplicationContext()).execute(KEY_121);
        }*/
        if ( extras.containsKey("sconcepto") && extras.containsKey("scodqr") )
		{
	    	cConcepto = extras.getString("sconcepto");
	    	cConceptoExtra = cConcepto;
	    	cCodqr = extras.getString("scodqr");
	    	cCodqrExtra = cCodqr;
	    	tv2.setText("Estas en: "+cConcepto);
			//tv3.setText(scodqr);
			
	    	if ( !cCodqr.equals(null) && cCodqr.length()==11 )
			{
				String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
				tmp = cCodqr.substring(0, 3);
				tmpedificio = cCodqr.substring(3, 5);
				tmpplanta = cCodqr.substring(5, 7);
				tmpcodqr = cCodqr.substring(7,11);
				
				
				
				ene = Integer.parseInt(tmp);
				if (ene <= 1)
				{
					queTabla = "ETSIITCONTENIDOS";
				}
				else if ( ene == 2)
				{
					queTabla = "FTICONTENIDOS";
				}
				if (codigo == 6 || codigo == 7)
				{
					//new ConsultaExtra(getApplicationContext()).execute(KEY_121);
					new ConsultaExtra().execute("consulta");
				}
				new ConsultaBD(getApplicationContext()).execute("consulta");
			}
	    	
			new ConsultaBD(getApplicationContext()).execute("consulta");
		}
        else
        {
        	tv2.setText(R.string.bienvenido);
        	tv1.setText(R.string.textobienvenida);
        }
        	
        
        vbtor = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        bRuta.setOnClickListener(new OnClickListener()
        {

			public void onClick(View v)
			{
				mediaPlayer.stop();
				finish();
				vbtor.vibrate(500);
				Intent places = new Intent(CapturaQRAudio.this, Lugares.class);
				places.putExtra("sconcepto", cConcepto);
				places.putExtra("scanal", selcad);
				places.putExtra("cod", codigo);
				places.putExtra("scodqr", cCodqr);
				startActivityForResult(places, 0);
			}
        	
        });

        otroqr.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				/*Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, READ_CODE);*/
				Intent qr = new Intent(CapturaQRAudio.this, DecoderActivity.class);
				startActivityForResult(qr, READ_CODE);
			}
		});
        
        bBusqueda.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent sinqr = new Intent(CapturaQRAudio.this, SeleccionaBusqueda.class);
				sinqr.putExtra("scanal", selcad);
				sinqr.putExtra("cod", codigo);
				if ( !cConcepto.equals("") && !cCodqr.equals("") )
				{
					sinqr.putExtra("sconcepto", cConcepto);
					sinqr.putExtra("scodqr", cCodqr);
				}
				startActivity(sinqr);
			}
		});
        
        
        
        
    } // Fin onCreate ************
    
  /************* FUNCIONES PARA EL CAMBIO DE CONFIGURACIÓN DE ORIENTACIÓN DEL MÓVIL ***************/
  	@Override
  	public void onSaveInstanceState(Bundle savedInstanceState) 
  	{
  	  super.onSaveInstanceState(savedInstanceState);
  	  // Save state to the savedInstanceState
  	  //savedInstanceState.putString("rLugar", tv2.getText().toString());
  	  //savedInstanceState.putString("rBreve", tv3.getText().toString());
  	  savedInstanceState.putString("rCad", selcad);
  	  savedInstanceState.putString("rConcepto", cConcepto);
  	  savedInstanceState.putString("rCodqr", cCodqr);
  	  savedInstanceState.putInt("rCod", codigo);
  	}
  	@Override
  	public void onRestoreInstanceState(Bundle savedInstanceState) 
  	{
  	  super.onRestoreInstanceState(savedInstanceState);
  	  // Restore state from savedInstanceState
  	  //String myString = savedInstanceState.getString("rLugar");
  	  //String breve = savedInstanceState.getString("rBreve");
  	  codigo = savedInstanceState.getInt("rCod");
  	  selcad = savedInstanceState.getString("rCad");
  	  cConcepto = savedInstanceState.getString("rConcepto");
  	  cCodqr = savedInstanceState.getString("rCodqr");
  	  
  	  //tv2.setText(myString);
  	  //tv3.setText(breve);
  	  //tv4.setText("Código: "+cCodqr);
  	}
  	
    
    public void onActivityResult (int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == READ_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String contents = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				//tv3.setText(contents, TextView.BufferType.SPANNABLE);
				//tv2.setText(format, TextView.BufferType.SPANNABLE);
				
				String [] vec1;
				vec1 = contents.split("#");
				miparam1 = vec1[0];
				miparam2 = vec1[1].replace(" ","");
				
				//miparam1 = miparam1.replace(" ", "");
				//miparam2 = miparam2.replace(" ", "");
				
				cConcepto = miparam1;
				cCodqr = miparam2;
				cConceptoExtra=miparam1;
				cCodqrExtra = miparam2;
				tv2.setText("Estas en: "+cConcepto);
				
				if (cCodqr.startsWith("QR"))
				{
					cCodqr = cCodqr.replace(" ", "");
					String tmp = cCodqr.replace("QR","");
					
					
					ene = Integer.parseInt(tmp);
				}
				else
				{
					cCodqr = cCodqr.replaceFirst(" ", "");
					String tmp = cCodqr.replace("QR","");
					ene = Integer.parseInt(tmp);
				}
				
				
				/***** SELECCION DE LA TABLA PARA LUGARES DE CADA EDIFICIO ********/
				if (ene < 1500)
				{
					queTabla = "ETSIITCONTENIDOS";
				}
				else
				{
					queTabla = "FTICONTENIDOS";
				}
				
				
				//tv3.setText(cCodqr);
				//cURL = contents;
				if (codigo == 6 || codigo == 7)
				{
					//new ConsultaExtra(getApplicationContext()).execute(KEY_121);
					new ConsultaExtra().execute("consulta");
				}
				new ConsultaBD(getApplicationContext()).execute("consulta");
				//new LeeConceptoTexto().execute("txt");
			}
		}
	}
    
    /**** Asi evito no poder parar el audio y tener que volver a descargarlo innecesariamente */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
        {
            // do something on back.
        	mediaPlayer.stop();
        	finish();
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    /**** FUNCION primarySeekBarProgressUpdater ***********/
    void primarySeekBarProgressUpdater() 
    {
    	seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
    
    	if (mediaPlayer.isPlaying()) 
    	{
        	Runnable notification = new Runnable() 
        	{
	        	public void run() 
	        	{
	        		primarySeekBarProgressUpdater();
	        	}
        	};
        	handler.postDelayed(notification,1000);
    	}
	}
    
    @Override
	public void onClick(View v) 
	{
    	/************ Boton PLAY ********************/
    	if (v.getId() == R.id.button1)
    	{
			/** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
			try 
			{
				String s = cResumen.replace(" ", "");
				mediaPlayer.setDataSource(s); 
				mediaPlayer.prepare(); 
				// you must call this method after setup the 
				//datasource in setDataSource method. After calling prepare() the instance 
				//of MediaPlayer starts load data from URL to internal buffer.
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
			if(!mediaPlayer.isPlaying())
			{
				mediaPlayer.start();
				//buttonPlayPause.setImageResource(R.drawable.button_pause);
				play.setText("Pause");
			}
			else 
			{
				mediaPlayer.pause();
				//buttonPlayPause.setImageResource(R.drawable.button_play);
				play.setText("Play");
			}
			primarySeekBarProgressUpdater();
    	}
	}

    
    @Override
    public boolean onTouch(View v, MotionEvent event) 
    {
	    if(v.getId() == R.id.seekBar1)
	    {
		    /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
		    if(mediaPlayer.isPlaying())
		    {
		    	SeekBar sb = (SeekBar)v;
		    	int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
		    	mediaPlayer.seekTo(playPositionInMillisecconds);
		    }
	    }
	    return false;
    }
    @Override
    public void onCompletion(MediaPlayer mp) 
    {
	    /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
	    //buttonPlayPause.setImageResource(R.drawable.button_play);
    	play.setText("Play");
    	//Toast.makeText(getApplicationContext(), "Descarga completada", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) 
    {
	    /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
	 
	    seekBarProgress.setSecondaryProgress(percent);
    }
    /** *********************************************************************************************
     * preparing mediaplayer will take sometime to buffer the content so
     *  prepare it inside the background thread and starting it on UI thread
     *
     */
    
    /***************************************************************************************************/
    // Clase para la conexión con la Base de Datos 
    private class ConsultaBD extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(CapturaQRAudio.this); 
        private Context mContext;
        
        
        public ConsultaBD(Context context) 
        {
            mContext = context;
        }
	    
        
        protected void onPreExecute() 
        {  
            Dialog.setMessage("Buscando información");  

            Dialog.show();  
        } 
	    
	    @Override
	    protected Void doInBackground(String... arg) 
	    {
	    	
		    URL textUrl;
	        try 
	        {
	        	if (cConcepto.startsWith(" "))
	        	{
	        		cConcepto = cConcepto.replaceFirst(" " , "");
	        	}
	        	 cConcepto = cConcepto.replace(" ", "%20");
		         textUrl = new URL(R.string.urlConsultaContenido+"CANAL="+selcad+"&CONCEPTO="+cConcepto+"&QRCODE="+cCodqr+"&LATABLA="+queTabla);
	
		         BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));
		         
		         String StringBuffer;
		         String stringText = "";
		         while ((StringBuffer = bufferReader.readLine()) != null) 
		         {
		        		 stringText += StringBuffer;
		         }
		         bufferReader.close();
	
		         textResult = stringText;
	        } 
	        catch (MalformedURLException e) 
	        {
		         e.printStackTrace();
		         textResult = e.toString();   
	        } 
	        catch (IOException e) 
	        {
		         e.printStackTrace();
		         textResult = e.toString();   
	        }
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) 
	    {
	    	vBody = textResult.split("<body>");
	    	for (int i=0; i< vBody.length; i++)
	    	{
	    		if (i == 1)
	    		{
	    			auxcad = vBody[1];
			    	vBr = auxcad.split("<br>");
	    		}
	    	}
	    	if (vBr.length >= 4)
	    	{
	    		for (int j=0; j<7; j++)
	    		{
	    			if (j == 0)
	    			{
	    				v2p = vBr[0].split(":");
	    				cCodqr = v2p[1];
	    				
	    			}
	    			else if (j == 1)
	    			{
	    				v2p = vBr[j].split(":");
	    				cConcepto = v2p[1];
	    			}
	    			else if (j == 2)
	    			{
	    				v2p = vBr[j].split(":");
	    				cCanal = v2p[1];
	    			}
	    			else if (j == 3)
	    			{
	    				v2p = vBr[j].split(":");
	    				if ( v2p.length > 2 )
	    				{
	    					cURL = v2p[1]+":"+v2p[2];
	    				}
	    				
	    			}
	    			else if (j==4)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if ( v2p.length > 2 )
	    				{
	    					cResumen = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==5)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if (v2p.length > 2)
	    				{
	    					cDDetallada = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==6)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if (v2p.length > 2)
	    				{
	    					cImagen = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    		}
	    		//tv2.setText(cConcepto);
	    		//tv3.setText(cURL);
	    		//tv4.setText(cCodqr);
	    		tv2.setText("Estás en: "+"\n"+cConcepto);
	    	}
	    	else
	    	{
	    		//tv4.setText(textResult);
	    	}
	    	
	    	Dialog.dismiss();
	    	
	    	play.setVisibility(Button.VISIBLE);
	        bRuta.setVisibility(Button.VISIBLE);
	        seekBarProgress.setVisibility(SeekBar.VISIBLE);
	        
		    super.onPostExecute(result);   
	    }
	}// Fin clase AsynTask

    
    /************************************************************************************************* */
 	private class ConsultaExtra extends AsyncTask<String, Void, Void>
 	{
 	    String textResult;
 	    String auxcad;
 	    String [] vBody, vBr, v2p;
 	    
 	    /*private ProgressDialog Dialog = new ProgressDialog(CapturaQRAudio.this); 
         private Context mContext;
         
         
         public ConsultaExtra(Context context) 
         {
             mContext = context;
         }
 	    
         
         protected void onPreExecute() 
         {  
             Dialog.setMessage("Buscando información");  

             //Dialog.setTitle("Requesting Hospital Name");
             Dialog.show();  
         }  
         */
 	    @Override
 	    protected Void doInBackground(String... arg) 
 	    {
 		    URL textUrl;
 	        try 
 	        {
 	        	//Toast.makeText(getBaseContext(), sconcepto+scodqr, Toast.LENGTH_LONG).show();
 	        	if (cConcepto.startsWith(" "))
	        	{
	        		cConcepto = cConcepto.replaceFirst(" " , "");
	        	}
	        	cConcepto = cConcepto.replace(" ", "%20");
 	        	String micanal;
 	        	if (codigo == 6)
 	        	{
 	        		micanal = "ESP_TXT";
 	        	}
 	        	else
 	        	{
 	        		micanal = "ING_TXT";
 	        	}
 		         textUrl = new URL(R.string.urlConsultaContenido+"CANAL="+micanal+"&CONCEPTO="+cConceptoExtra+"&QRCODE="+cCodqrExtra+"&LATABLA="+queTabla);
 	
 		         BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));
 		         
 		         String StringBuffer;
 		         String stringText = "";
 		         while ((StringBuffer = bufferReader.readLine()) != null) 
 		         {
 		        		 stringText += StringBuffer;
 		         }
 		         bufferReader.close();
 		         textResult = stringText;
 	        } 
 	        catch (MalformedURLException e) 
 	        {
 		         e.printStackTrace();
 		         textResult = e.toString();   
 	        } 
 	        catch (IOException e) 
 	        {
 		         e.printStackTrace();
 		         textResult = e.toString();   
 	        }
 	        return null;
 	    }
 	    
 	    @Override
 	    protected void onPostExecute(Void result) 
 	    {
 	    	vBody = textResult.split("<body>");
 	    	for (int i=0; i< vBody.length; i++)
 	    	{
 	    		if (i == 1)
 	    		{
 	    			auxcad = vBody[1];
 			    	vBr = auxcad.split("<br>");
 	    		}
 	    	}
 	    	if (vBr.length >= 4)
 	    	{
 	    		
 	    		for (int j=0; j<vBr.length; j++)
 	    		{
 	    			
 	    			if (j==0)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				cCodqr = v2p[1];
 	    				cCodqrExtra = cCodqr.replace(" ", "");
 	    			}
 	    			else if (j == 1)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				cConceptoExtra = v2p[1];
 	    				//sconcepto = cConcepto;
 	    			}
 	    			/*else if (j == 2)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				cCanal = v2p[1];
 	    			}*/
 	    			else if (j == 3)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				if ( v2p.length >= 2 )
 	    				{
 	    					cUrlExtra = v2p[1]+":"+v2p[2];
 	    				}
 	    			}
 	    			else 
 	    			if (j==4)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				
 	    				if ( v2p.length > 2 )
 	    				{
 	    					resumenExtra = v2p[1]+":"+v2p[2];
 	    				}
 	    			}
 	    			else if (j==5)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				
 	    				if (v2p.length > 2)
 	    				{
 	    					ddetalladaExtra = v2p[1]+":"+v2p[2];
 	    				}
 	    			}
 	    			/*else if (j==6)
 	    			{
 	    				v2p = vBr[j].split(":");
 	    				
 	    				if (v2p.length > 2)
 	    				{
 	    					cImagen = v2p[1]+":"+v2p[2];
 	    				}
 	    			}*/
 	    		}
 	    		
 	    		//Dialog.dismiss();
 	    		
 	    		//tv2.setText("Estas en: "+cConcepto);
 	    		//tv3.setText(cResumen);
 	    		//tv4.setText("Código: "+cCodqr);

 	    		
 	    		if ( !resumenExtra.equals("") )
 	    		{
 	    			new LeeResumenTexto().execute(resumenExtra);
 	    		}
 	    		else
 	    		{
 	    			tv3.setText("No se encontró información asociada al resumen");
 	    		}
 	    		
 	    	}
 	    	else
 	    	{

 	    		tv3.setText("No se encontró información");
 	    		//tv3.setText(textResult+sconcepto+scodqr);
 	    	}
 	    	
 		    super.onPostExecute(result);   
 	    }
 	}// Fin clase ConsultaBD
    
    // ***********************************************************************************************
	private class LeeResumenTexto extends AsyncTask<String, Void, Void>
	{
	    String resultado2="";
	    @Override
	    protected Void doInBackground(String... arg) 
	    {
	    	try 
	    	{
	    	    // Create a URL for the desired page
	    	    URL url = new URL(arg[0]);

	    	    // Read all the text returned by the server
	    	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    	    String str;
	    	    StringBuilder strbldr = new StringBuilder();
	    	    while ((str = in.readLine()) != null) 
	    	    {
	    	        // str is one line of text; 
	    	    	//readLine() strips the newline character(s)
	    	    	strbldr.append(str);
	    	    }
	    	    resultado2 = strbldr.toString();
	    	    in.close();
	    	} 
	    	catch (MalformedURLException e) 
	    	{
	    		 e.printStackTrace();
	    	} 
	    	catch (IOException e) 
	    	{
	    		e.printStackTrace();
	    	}

	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) 
	    {

	    	tv1.setText(resultado2);
	    	
		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase LeeConceptoTexto
	
}