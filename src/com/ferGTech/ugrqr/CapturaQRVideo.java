package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class CapturaQRVideo extends Activity implements OnClickListener, OnCompletionListener, OnBufferingUpdateListener
{
	MediaController mediaController;
	//private final Handler handler = new Handler();
	int mediaFileLengthInMilliseconds;
	Cursor mediaCursor=null;
	VideoView videoView=null;
	int dataIdx=0;
	int READ_CODE = 0;
	int ene=0;
	String queTabla;
	//SeekBar seekb;
	Button biniciar, brutas, blugar, bQR;
	TextView  tv2;
	Bundle extras;
	int codigo = 0;
	String selcad;
	String cCanal="", cConcepto="", cURL="", cResumen="", cDDetallada="", cImagen="", cCodqr="";
	String scodqr="";
	 
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escaneovideo);
		
		biniciar = (Button)findViewById(R.id.button1);
		brutas = (Button)findViewById(R.id.button2);
		blugar = (Button)findViewById(R.id.button3);
		bQR = (Button)findViewById(R.id.button4);
		tv2 = (TextView)findViewById(R.id.textView2);
		
		biniciar.setVisibility(Button.INVISIBLE);
		brutas.setVisibility(Button.INVISIBLE);
		
        
		extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		
		if (codigo == 3)
		{
			selcad = getResources().getString(R.string.videolse);
		}
		
		//new ConsultaBD().execute(KEY_121);
		
		videoView = (VideoView) findViewById(R.id.videoView1);
		//mediaController = (MediaController)findViewById(R.id.mediaController1);
	    mediaController = new MediaController(this);
	    //mediaController.setAnchorView(mediaController);
	    
	    videoView.setMediaController(mediaController);
	    
	    if ( extras.containsKey("sconcepto") && extras.containsKey("scodqr") )
		{
	    	cConcepto = extras.getString("sconcepto");
			scodqr = extras.getString("scodqr");
			
			tv2.setText("Estás en: "+"\n"+cConcepto);
			scodqr = scodqr.replace(" ", "");
			
			//tv2.setText(sconcepto);
			//tv3.setText(scodqr);
			
			if ( !scodqr.equals(null) && scodqr.length()==11 )
			{
				String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
				tmp = scodqr.substring(0, 3);
				tmpedificio = scodqr.substring(3, 5);
				tmpplanta = scodqr.substring(5, 7);
				tmpcodqr = scodqr.substring(7,11);
				
				
				
				ene = Integer.parseInt(tmp);
				if (ene <= 1)
				{
					queTabla = getResources().getString(R.string.tabla_etsiitcontenidos);
				}
				else if ( ene == 2)
				{
					queTabla = getResources().getString(R.string.tabla_fticontenidos);
				}
				
				new ConsultaBD(getApplicationContext()).execute("consulta");
			}
			
		}
	    else
        {
        	tv2.setText(R.string.bienvenido);
        }
	    
	    
	    // Reproducir video
	    biniciar.setOnClickListener(this);
	    
	    // IR a menu Lugares para Video
	    brutas.setOnClickListener(new OnClickListener() 
	    {	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if (cConcepto.equals(""))
				{
					Toast.makeText(getBaseContext(), "Concepto incorrecto", Toast.LENGTH_LONG).show();
				}
				else if (selcad.equals(""))
				{
					Toast.makeText(getBaseContext(), "Canal incorrecto", Toast.LENGTH_LONG).show();
				}
				else if (scodqr.equals(""))
				{
					Toast.makeText(getBaseContext(), "Codigo QR incorrecto", Toast.LENGTH_LONG).show();
				}
				else
				{
					Intent vLugares = new Intent(CapturaQRVideo.this, Lugares.class);
					vLugares.putExtra("sconcepto", cConcepto);
					vLugares.putExtra("scanal", selcad);
					vLugares.putExtra("cod", codigo);
					vLugares.putExtra("scodqr", scodqr);
					startActivityForResult(vLugares, 0);
				}
				
			}
		});
	    // Ir a menu Descripción Detallada para Video
	    blugar.setOnClickListener(new OnClickListener()
	    {	
			@Override
			public void onClick(View v) 
			{
				Intent sinqr = new Intent(CapturaQRVideo.this, SeleccionaBusqueda.class);
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
	    // Iniciar el lector de códigos QR
	    bQR.setOnClickListener(new OnClickListener() 
	    {	
			@Override
			public void onClick(View v) 
			{
				Intent qr = new Intent(CapturaQRVideo.this, DecoderActivity.class);
				startActivityForResult(qr, READ_CODE);
			}
		});
	}// FIn onCreate
	
	
	//************* FUNCIONES PARA EL CAMBIO DE CONFIGURACIÓN DE ORIENTACIÓN DEL MÓVIL ***************
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	  super.onSaveInstanceState(savedInstanceState);
	  // Save state to the savedInstanceState
	  savedInstanceState.putInt("rCod", codigo);
	  savedInstanceState.putString("rResumen", cResumen);
	  savedInstanceState.putString("rCad", selcad);
	  savedInstanceState.putString("rConcepto", cConcepto);
	  savedInstanceState.putString("rCodqr", scodqr);
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore state from savedInstanceState
	  codigo = savedInstanceState.getInt("rCod");
	  cResumen = savedInstanceState.getString("rResumen");
	  selcad = savedInstanceState.getString("rCad");
	  cConcepto = savedInstanceState.getString("rConcepto");
	  scodqr = savedInstanceState.getString("rCodqr");

	  tv2.setText(cConcepto);
	  videoView.setVideoPath(cResumen);
	}
	
	public void onActivityResult (int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == READ_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String contents = data.getStringExtra("SCAN_RESULT");

				scodqr = contents.replace(" ", "");
				
				if ( !scodqr.equals(null) && scodqr.length()==11 )
				{
					String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
					tmp = scodqr.substring(0, 3);
					tmpedificio = scodqr.substring(3, 5);
					tmpplanta = scodqr.substring(5, 7);
					tmpcodqr = scodqr.substring(7,11);
					
					
					
					ene = Integer.parseInt(tmp);
					if (ene <= 1)
					{
						queTabla = getResources().getString(R.string.tabla_etsiitcontenidos);
					}
					else if ( ene == 2)
					{
						queTabla = getResources().getString(R.string.tabla_fticontenidos);
					}
					new ConsultaBD(getApplicationContext()).execute("consulta");
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) 
	{
    	if (v.getId() == R.id.button1)
    	{
			/** ImageButton onClick event handler. Method which start/pause mediaplayer playing */	
    			try 
    			{
    				String cad = cResumen.replace(" ", "");
    				videoView.setVideoPath(cad);
    				// you must call this method after setup the 
    				//datasource in setDataSource method. After calling prepare() the instance 
    				//of MediaPlayer starts load data from URL to internal buffer.
    			} 
    			catch (Exception e) 
    			{
    				e.printStackTrace();
    			}
    			mediaFileLengthInMilliseconds = videoView.getDuration(); // gets the song length in milliseconds from URL
    			
    			//if( videoView.isPlaying() == false)
    				videoView.start();
    				//Toast.makeText(getBaseContext(), "pulse video para posicionarte en un instante concreto", Toast.LENGTH_SHORT).show();
    				//buttonPlayPause.setImageResource(R.drawable.button_pause);
    				biniciar.setText("Reiniciar");
    				
    			/*else 
    			{	videoView.pause();
    				Toast.makeText(getBaseContext(), "else", Toast.LENGTH_SHORT).show();
    				//buttonPlayPause.setImageResource(R.drawable.button_play);
    				btn1.setText("Play");}*/
    			//primarySeekBarProgressUpdater();
    		}
    		
    		
    
	}
	
	// ***************************************************************************************
    // Clase para la conexión con la Base de Datos 
    private class ConsultaBD extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(CapturaQRVideo.this); 
        private Context mContext;
        
        
        public ConsultaBD(Context context) 
        {
            mContext = context;
        }
	    
        
        protected void onPreExecute() 
        {  
            Dialog.setMessage("Buscando información");  

            //Dialog.setTitle("Requesting Hospital Name");
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
	        		cConcepto = cConcepto.replaceFirst(" " , "" );
	        	}
	        	String xconcepto = cConcepto.replace(" ", "%20");
		         textUrl = new URL(getResources().getString(R.string.urlConsultaContenido)+"CANAL="+selcad+"&QRCODE="+scodqr+"&LATABLA="+queTabla);
	
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
	    		for (int j=0; j<8; j++)
	    		{
	    			if (j==0)
	    			{
	    				v2p = vBr[j].split(":");
	    				cConcepto = v2p[1];
	    				byte[] bytes= new byte[]{};
	    				try {
							bytes = cConcepto.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
    					try {
    						cConcepto = new String(bytes,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
    					
	    				
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
	    					cResumen = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==4)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if (v2p.length >= 2)
	    				{
	    					cDDetallada = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==5)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if (v2p.length >= 2)
	    				{
	    					cImagen = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==6)
	    			{

	    			}
	    		
	    		}
	    		tv2.setText("Estás en: "+cConcepto);
	    	}
	    	else
	    	{
	    		tv2.setText(cResumen);
	    		Toast.makeText(getBaseContext(), "Vídeo no encontrado", Toast.LENGTH_SHORT).show();
	    	}
	    	Dialog.dismiss();
	    	
	    	biniciar.setVisibility(Button.VISIBLE);
	    	brutas.setVisibility(Button.VISIBLE);
			Animation mAnimation = new AlphaAnimation(1, 0);
		    mAnimation.setDuration(400);
		    mAnimation.setInterpolator(new LinearInterpolator());
		    mAnimation.setRepeatCount(Animation.INFINITE);
		    mAnimation.setRepeatMode(Animation.REVERSE); 
		    biniciar.startAnimation(mAnimation);
		    brutas.startAnimation(mAnimation);
		    
		    mAnimation.setRepeatCount(3);
		    
		    super.onPostExecute(result);   
	    }
	}// Fin clase ConsultaBD
    
    
    

	public void onProgressChanged(MediaPlayer mp, int percent) 
	{
		videoView.seekTo(percent);
	}

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		biniciar.setText("Play");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) 
	{
		
	}

}