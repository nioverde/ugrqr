package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
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
import android.widget.VideoView;


public class DDetalladaVideo extends Activity implements OnClickListener ,OnCompletionListener, OnBufferingUpdateListener
{
	TextView tv2;
	Button play, bdesc;
	VideoView videoDescripcion;
	MediaController mc1;
	Bundle extras;
	String canal="";
	String concepto="";
	String codqr="";
	int codigo;
	String cCanal, cConcepto, cCodqr, cResumen, cDDetallada="", consulta;
	int mediaFileLengthInMilliseconds;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ddetalladavideo);
		
		videoDescripcion = (VideoView)findViewById(R.id.videoView1);
		play = (Button)findViewById(R.id.button2);
		bdesc = (Button)findViewById(R.id.button3);
		tv2 = (TextView)findViewById(R.id.textView2);
		
		extras = getIntent().getExtras();
		canal = extras.getString("scanal");
		codigo = extras.getInt("cod");
		concepto = extras.getString("sconcepto");
		codqr = extras.getString("scodqr");
		cDDetallada = extras.getString("url");
		
		//tcabecera.setText("Lugar:"+concepto);
		//tv1.setText(canal);
		//tv2.setText(codqr);
		
		
		// **** Si puedo me ahorro la consulta a la BD y accedo directamente al archivo txt
		if ( !cDDetallada.equals("") )
    	{
			videoDescripcion.setMediaController(mc1);
    		try 
			{
				String cad = cDDetallada.replace(" ", "");
				//Toast.makeText(getBaseContext(), cad, Toast.LENGTH_LONG).show();
				videoDescripcion.setVideoPath(cad);
				// you must call this method after setup the 
				//datasource in setDataSource method. After calling prepare() the instance 
				//of MediaPlayer starts load data from URL to internal buffer.
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			mediaFileLengthInMilliseconds = videoDescripcion.getDuration(); // gets the song length in milliseconds from URL
			
			//if( videoView.isPlaying() == false)
			videoDescripcion.start();
				//Toast.makeText(getBaseContext(), "pulse video para posicionarte en un instante concreto", Toast.LENGTH_SHORT).show();
				//buttonPlayPause.setImageResource(R.drawable.button_pause);
			play.setText("Reiniciar");
    	}
    	else
    	{
    		new BuscaVideos().execute("consulta");
    	}
		
		
		mc1 = new MediaController(this);
		
		
		// **** Iniciar Activity LugaresVideo **********
		bdesc.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(DDetalladaVideo.this, LugaresVideo.class);
				i.putExtra("scanal", canal);
				i.putExtra("sconcepto", concepto);
				i.putExtra("scodqr", codqr);
				startActivity(i);
			}
		});
		// ******** Inicio Descripcion Detallada ***************
		play.setOnClickListener(this);
		
		
		
	}// Fin onCreate()
	
	@Override
	public void onClick(View v) 
	{
    	if (v.getId() == R.id.button2)
    	{
    		videoDescripcion.setMediaController(mc1);
    		try 
			{
				String cad = cDDetallada.replace(" ", "");
				//Toast.makeText(getBaseContext(), cad, Toast.LENGTH_LONG).show();
				videoDescripcion.setVideoPath(cad);
				// you must call this method after setup the 
				//datasource in setDataSource method. After calling prepare() the instance 
				//of MediaPlayer starts load data from URL to internal buffer.
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			mediaFileLengthInMilliseconds = videoDescripcion.getDuration(); // gets the song length in milliseconds from URL
			
			//if( videoView.isPlaying() == false)
			videoDescripcion.start();
				//Toast.makeText(getBaseContext(), "pulse video para posicionarte en un instante concreto", Toast.LENGTH_SHORT).show();
				//buttonPlayPause.setImageResource(R.drawable.button_pause);
			play.setText("Reiniciar");
    	}
	}
	private class BuscaVideos extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String  auxcad;
	    String [] vBody, vBr, v2p;
	    
	    @Override
	    protected Void doInBackground(String... arg) 
	    {
		    URL textUrl;
	        try 
	        {
	        	String sconcept;
	        	if ( concepto.startsWith(" ") )
	        	{
	        		sconcept = concepto.replace(" ", "");
	        	}
	        	else
	        	{
	        		sconcept = concepto.replace(" ", "%20");
	        	}
	        	 
	        	 
	        	// String sqr = codqr.replace(" ", "");
		         textUrl = new URL(R.string.urlConsultaContenido+"CANAL="+canal+"&CONCEPTO="+sconcept);
	
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
	    	//consulta = textResult;
	    	vBody = textResult.split("<body>");
	    	for (int i=0; i< vBody.length; i++)
	    	{
	    		if (i == 1)
	    		{
	    			auxcad = vBody[1];
			    	vBr = auxcad.split("<br>");
	    		}
	    	}
	    	if (vBr.length >= 3)
	    	{
	    		for (int j=0; j<4; j++)
	    		{
	    			if (j == 0)
	    			{
	    				v2p = vBr[0].split(":");
	    				cCanal = v2p[1];
	    			}
	    			else if (j == 1)
	    			{
	    				v2p = vBr[j].split(":");
	    				cConcepto = v2p[1];
	    			}
	    			else if (j == 2)
	    			{
	    				v2p = vBr[j].split(":");
	    				cResumen = v2p[1]+":"+v2p[2];
	    			}
	    			else if (j == 3)
	    			{
	    				v2p = vBr[j].split(":");
	    				cDDetallada = v2p[1]+":"+v2p[2];
	    			}
	    		}
	    		//tv1.setText(cResumen);
	    		//tv2.setText(cDDetallada);
	    		//Toast.makeText(getBaseContext(), cResumen, Toast.LENGTH_LONG).show();
	    	}
	    	else
	    	{
	    		//tcabecera.setText("error flag");
	    	}
	    	//new LeeResumenVideo().execute();
	    	//new LeeDescripcionVideo().execute();

		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase BuscaVideos


	public void onProgressChanged(MediaPlayer mp, int percent) 
	{
		// TODO Auto-generated method stub
		videoDescripcion.seekTo(percent);
	}

	
	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) 
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCompletion(MediaPlayer arg0) 
	{
		// TODO Auto-generated method stub
		//bres.setText("Carga finalizada");
	}

	
	

}