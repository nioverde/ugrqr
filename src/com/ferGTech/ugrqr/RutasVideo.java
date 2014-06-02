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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class RutasVideo extends Activity implements OnClickListener ,OnCompletionListener, OnBufferingUpdateListener
{
	TextView tv1, tv2;
	Bundle extras;
	String canal , concepto, codruta, codqr;
	Button sigPaso, volver, iniciar;
	
	VideoView videoView;
	MediaController mc;
	int mediaFileLengthInMilliseconds;
	
	int NPASO=1, TOTALPASOS=1;
	int cnp=0;
	String sTOTALPASOS = "1";
	String vPaso;
	String cURL="";
	String cNpasos="";
	int codigo;
	String  consulta, consulta2, consulta3, consulta4;
	String textResult;
	int ene=0;
	String queTabla;
	String destino="", qrdestino="";
	
	
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rutavideo);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		tv2 = (TextView)findViewById(R.id.textView2);
		volver = (Button)findViewById(R.id.button1);
		sigPaso = (Button)findViewById(R.id.button2);
		iniciar = (Button)findViewById(R.id.button3);
		videoView = (VideoView)findViewById(R.id.videoView1);
		
		vPaso="P1";
		
		extras = getIntent().getExtras();
		canal = extras.getString("canal");
		codigo = extras.getInt("cod");
		concepto = extras.getString("origen");
		codruta = extras.getString("codigoruta");
		codqr = extras.getString("scodqr");
		destino = extras.getString("destino");
		qrdestino = extras.getString("qrdestino");
		
		String tmp = codqr.replace("QR", "");
		ene = Integer.parseInt(tmp);
		
		// ********** Eleccion de tabla RUTAS según edificio *************
		if (ene < 1500)
		{
			queTabla = getResources().getString(R.string.tabla_etsiitpasos);
		}
		else if ( ene >= 1500 )
		{
			queTabla = getResources().getString(R.string.tabla_ftipasos);
		}
		codruta = codruta.trim();
		
		
		
		
		
		mc = new MediaController(this);
		videoView.setMediaController(mc);
		
		new RutaVideo().execute("Inicio");
		
		volver.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(RutasVideo.this, CapturaQRVideo.class);
				i.putExtra("cod", codigo);
				i.putExtra("sconcepto", destino);
				i.putExtra("scodqr", qrdestino);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		sigPaso.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//NPASO += 1;
				// TODO Auto-generated method stub
				/*if (NPASO == 0)
				{
					NPASO += 1;
					vPaso = "P1";
					tv2.setText(vPaso);
					new MyTask().execute(KEY_121);
				}
				else*/ 
				if (NPASO == TOTALPASOS-1)
				{
					Toast.makeText(getBaseContext(), "Último paso", Toast.LENGTH_SHORT).show();
				}
				
				if (NPASO == 1)
				{
					NPASO += 1;
					vPaso = "P2";
					new RutaVideo().execute("Inicio");
				}
				else if (NPASO == 2)
				{
					NPASO += 1;
					vPaso = "P3";
					new RutaVideo().execute("Inicio");
				}
				else if (NPASO == 3)
				{
					NPASO += 1;
					vPaso = "P4";
					new RutaVideo().execute("Inicio");
				}
				else if (NPASO == 4)
				{
					NPASO += 1;
					vPaso = "P5";
					new RutaVideo().execute("Inicio");
				}
				else if (NPASO == 5)
				{
					NPASO += 1;
					vPaso = "P6";
					new RutaVideo().execute("Inicio");
				}
				else if (NPASO == 6)
				{
					NPASO += 1;
					vPaso = "P7";
					new RutaVideo().execute("Inicio");
				}
				if (NPASO > cnp)
				{
					NPASO = 1;
					vPaso = "P1";
					new RutaVideo().execute("Inicio");
				}
			}
		});
		iniciar.setOnClickListener(this);
		
	}// Fin onCreate()
	
	// *******************************************
    // Clase para la conexión con la Base de Datos 
    private class RutaVideo extends AsyncTask<String, Void, Void>
	{
    	
	    String auxcad, auxcad2;
	    String [] vBody, vBr, v2p;
	    StringBuilder stringBuilder;
	    
	    private ProgressDialog Dialog = new ProgressDialog(RutasVideo.this); 
        private Context mContext;
	    
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
	        	textUrl = new URL(getResources().getString(R.string.urlConsultaRutas)+"CANAL="+canal+"&RUTA="+codruta+"&PASO="+vPaso+"&LATABLA="+queTabla);
	        	
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
	    	int j;
	    	vBody = textResult.split("<body>");
	    	for (int i=0; i< vBody.length; i++)
	    	{
	    		if (i == 1)
	    		{
	    			auxcad = vBody[i];
	    			if (auxcad != null)
	    			{
	    				vBr = auxcad.split("<br>");
	    			}
	    		}
	    	}
	    	if (vBr.length > 1)
	    	{
	    		for (j=0; j<vBr.length; j++)
	    		{
	    			v2p = vBr[j].split(":");
	    			if (j == 0 && v2p.length >= 2)
					{
						cURL = v2p[1]+":"+v2p[2];
					}
					else if (j == 1 )
					{
						cNpasos = v2p[1];
					}
	    		}
	    		//tv5.setText(consulta+xCanal+consulta2+cod+consulta3+vPaso);
		    	//tv5.setText(cURL);
	    		//tv6.setText(cNpasos);
	    		
	    		cnp = Integer.valueOf(cNpasos);
	    		if (cnp == 1)
	    		{
	    			TOTALPASOS = 1;
	    			sTOTALPASOS = "1";
	    		}
	    		else if (cnp == 2)
	    		{
	    			TOTALPASOS = 2;
	    			sTOTALPASOS = "2";
	    		}
	    		else if (cnp == 3)
	    		{
	    			TOTALPASOS = 3;
	    			sTOTALPASOS = "3";
	    		}
	    		else if (cnp == 4)
	    		{
	    			TOTALPASOS = 4;
	    			sTOTALPASOS = "4";
	    		}
	    		else if (cnp == 5)
	    		{
	    			TOTALPASOS = 5;
	    			sTOTALPASOS = "5";
	    		}
	    		else if (cnp == 6)
	    		{
	    			TOTALPASOS = 6;
	    			sTOTALPASOS = "6";
	    		}
	    		//Toast.makeText(getBaseContext(), cURL, Toast.LENGTH_LONG).show();
		    	//tv1.setText("Paso:"+NPASO+" de "+ sTOTALPASOS);
	    	}
	    	else
	    	{
	    		tv1.setText("No se encontró información");
	    		//canal = "ESP_TXT";
	    		//new RutaVideo().execute("Inicio");
	    		
	    	}
	    	//new LeeRutaTexto().execute(KEY_121);
	    	//tv1.setText( auxcad2);
	    	tv1.setText(concepto+" - "+destino);
	    	
	    	Dialog.dismiss();
		    super.onPostExecute(result);   
	    }
	}// Fin clase AsynTask

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		// TODO Auto-generated method stub
		iniciar.setText("Play");
	}
	public void onProgressChanged(MediaPlayer mp, int percent) 
	{
		// TODO Auto-generated method stub
		videoView.seekTo(percent);
	}


	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if (v.getId() == R.id.button3)
    	{
    		
			try 
			{
				String cad = cURL.replace(" ", "");
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
				iniciar.setText("Reiniciar");
				
			/*else 
			{	videoView.pause();
				Toast.makeText(getBaseContext(), "else", Toast.LENGTH_SHORT).show();
				//buttonPlayPause.setImageResource(R.drawable.button_play);
				btn1.setText("Play");}*/
			//primarySeekBarProgressUpdater();
    	}
	}

}