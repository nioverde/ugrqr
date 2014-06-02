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
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class RutasAudio extends Activity implements OnClickListener, OnTouchListener, 
	OnCompletionListener, OnBufferingUpdateListener
{
	MediaPlayer mediaPlayer;
	int mediaFileLengthInMilliseconds;
	
	
	private SeekBar seekBarProgress;
	Button imbPlay;
	Button sigPaso, volver;
	TextView tv1, tv2, tv4;
	Bundle extras;
	String canal , concepto, codruta, codqr, destino="", qrdestino="";
	String cConceptoExtra="", cUrlExtra="",cCodqrExtra="", resumenExtra="", ddetalladaExtra="";
	
	
	private final Handler handler = new Handler();
	
	int NPASO=1, TOTALPASOS=1;
	int cnp=0;
	String sTOTALPASOS = "1";
	String vPaso;
	String cURL="";
	String cNpasos="";
	int codigo=0;
	boolean txtyaudio = false;
	String  consulta, consulta2, consulta3, consulta4;
	String textResult;
	int ene=0;
	String queTabla;
	
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rutaaudio);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		tv2 = (TextView)findViewById(R.id.textView2);
		tv4 = (TextView)findViewById(R.id.textView4);
		imbPlay = (Button)findViewById(R.id.button3);
		sigPaso = (Button)findViewById(R.id.Button01);
		volver = (Button)findViewById(R.id.button1);
		
		seekBarProgress = (SeekBar)findViewById(R.id.seekBar1);
        seekBarProgress.setMax(99);
        seekBarProgress.setOnTouchListener(this);
        
		
		vPaso="P1";
		//tv4.setText("Paso:"+NPASO+" de "+ sTOTALPASOS);
		
		mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
		
		extras = getIntent().getExtras();
		canal = extras.getString("canal");
		codigo = extras.getInt("cod");
		concepto = extras.getString("origen");
		codruta = extras.getString("codigoruta");
		codqr = extras.getString("scodqr");
		destino = extras.getString("destino");
		qrdestino = extras.getString("qrdestino");
		//Toast.makeText(getBaseContext(), String.valueOf(codigo).toString(), Toast.LENGTH_LONG).show();
		
		if (codigo == 4)
		{
			txtyaudio = false;
			canal = getResources().getString(R.string.audesp);
		}
		else if (codigo == 5)
		{
			txtyaudio = false;
			canal = getResources().getString(R.string.auding);
		}
		else if (codigo == 6)
		{
			txtyaudio = true;
			canal = getResources().getString(R.string.audesp);
		}
		else if (codigo == 7)
		{
			txtyaudio = true;
			canal = getResources().getString(R.string.auding);
		}
		if (codqr.startsWith("QR"))
		{
			codqr = codqr.replace(" ", "");
			String tmp = codqr.replace("QR","");
			
			
			ene = Integer.parseInt(tmp);
		}
		else
		{
			codqr = codqr.replace(" ", "");
			String tmp = codqr.replace("QR","");
			ene = Integer.parseInt(tmp);
		}
		
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
		
		tv2.setText(concepto+"\n"+destino);
		
		
		
		if (codigo >= 6  && codigo <= 7)
		{
			//new ConsultaExtra(getApplicationContext()).execute(KEY_121);
			new ConsultaExtra().execute("Inicio");
		}
		new RutaAudio().execute("Inicio");
		
		
		
		imbPlay.setOnClickListener(this);
		
		volver.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(RutasAudio.this, CapturaQRAudio.class);
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
				}
				else if (NPASO == 2)
				{
					NPASO += 1;
					vPaso = "P3";
				}
				else if (NPASO == 3)
				{
					NPASO += 1;
					vPaso = "P4";
				}
				else if (NPASO == 4)
				{
					NPASO += 1;
					vPaso = "P5";
				}
				else if (NPASO == 5)
				{
					NPASO += 1;
					vPaso = "P6";
				}
				else if (NPASO == 6)
				{
					NPASO += 1;
					vPaso = "P7";
				}
				if (NPASO > cnp)
				{
					NPASO = 1;
					vPaso = "P1";
				}
			}
		});
		
	} // Fin de onCreate

	 // Clase para la conexión con la Base de Datos 
    private class RutaAudio extends AsyncTask<String, Void, Void>
	{
    	
	    String auxcad, auxcad2;
	    String [] vBody, vBr, v2p;
	    StringBuilder stringBuilder;
	    
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
		         auxcad2 = textResult;
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
	    	}
	    	else
	    	{
	    		tv1.setText("No se encontró información");
	    		
	    	}
	    	//new LeeRutaTexto().execute(KEY_121);
	    	//tv1.setText( auxcad2);
		    super.onPostExecute(result);   
	    }
	}// Fin clase AsynTask
    
    
    @Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) 
	{
		// TODO Auto-generated method stub
    	seekBarProgress.setSecondaryProgress(percent);
	}

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		
	}
	
	// **** Asi evito no poder parar el audio y tener que volver a descargarlo innecesariamente
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

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if (v.getId() == R.id.button3)
    	{
			/** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
			
			if ( !cURL.equals("") )
			{
				try 
				{
					String s = cURL.replace(" ", "");
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
					//play.setText("Pause");
				}
				else 
				{
					mediaPlayer.pause();
					//buttonPlayPause.setImageResource(R.drawable.button_play);
					//play.setText("Play");
				}
				primarySeekBarProgressUpdater();
			}
			
    	}
	}

	// **** FUNCION primarySeekBarProgressUpdater *********** 
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

	//* ************************************************************************************************* */
	 	private class ConsultaExtra extends AsyncTask<String, Void, Void>
	 	{
	 	    String textResult;
	 	    String auxcad;
	 	    String [] vBody, vBr, v2p;
	 	    
	 	    @Override
	 	    protected Void doInBackground(String... arg) 
	 	    {
	 		    URL textUrl;
	 	        try 
	 	        {
	 	        	if (concepto.startsWith(" "))
		        	{
	 	        		concepto = concepto.replaceFirst(" " , "");
		        	}
	 	        	concepto = concepto.replace(" ", "%20");
	 	        	String micanal;
	 	        	
	 	        	micanal = getResources().getString(R.string.esptxt);
	 	        	
	 		         textUrl = new URL(R.string.urlConsultaRutas+"CANAL="+micanal+"&RUTA="+codruta+"&PASO="+vPaso+"&LATABLA="+queTabla);
	 		         
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
	 	    	if (vBr.length > 1)
	 	    	{
	 	    		
	 	    		for (int j=0; j<vBr.length; j++)
	 	    		{
	 	    			
	 	    			v2p = vBr[j].split(":");
		    			if (j == 0 && v2p.length >= 2)
						{
							cUrlExtra = v2p[1]+":"+v2p[2];
						}
						else if (j == 1 )
						{
							cNpasos = v2p[1];
						}
	 	    		}
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
	 	    		tv4.setText("Paso:"+NPASO+" de "+ sTOTALPASOS);
	 	    		
	 	    		//Dialog.dismiss();
	 	    		
	 	    		//tv2.setText("Estas en: "+cConcepto);
	 	    		//tv3.setText(cResumen);
	 	    		//tv4.setText("Código: "+cCodqr);

	 	    		
	 	    		if ( !cUrlExtra.equals("") )
	 	    		{
	 	    			new LeeResumenTexto().execute(cUrlExtra);
	 	    		}
	 	    		else
	 	    		{
	 	    			tv1.setText("No se encontró información asociada al resumen");
	 	    		}
	 	    		
	 	    	}
	 	    	else
	 	    	{

	 	    		tv1.setText("No se encontró información en formato texto");
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
	
} // Fin de clase
