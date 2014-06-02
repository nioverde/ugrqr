package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Rutas extends Activity
{
	
	Button fin, next, back;
	TextView tv2, tv5, tv7;
	Bundle extras;
	String orig, destino, cod, xCanal, vPaso;
	int NPASO=1, TOTALPASOS=1;
	int cnp=0;
	String sTOTALPASOS = "1";
	String cURL="";
	int codigo;
	String cNpasos="";
	String  consulta, consulta2, consulta3, consulta4;
	String textResult;
	String codqr, qrdestino;
	int ene =0;
	String queTabla;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ruta);
		
		fin = (Button)findViewById(R.id.button1);
		next = (Button)findViewById(R.id.button2);
		back = (Button)findViewById(R.id.button3);
		tv2 = (TextView)findViewById(R.id.textView2);
		tv5 = (TextView)findViewById(R.id.textView5);
		tv7 = (TextView)findViewById(R.id.textView7);
		tv5.setMovementMethod(new ScrollingMovementMethod());
		
		vPaso="P1";
		extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		orig = extras.getString("origen");
		destino = extras.getString("destino");
		cod = extras.getString("codigoruta");
		codqr = extras.getString("scodqr");
		qrdestino = extras.getString("qrdestino");
		
		if ( !codqr.equals(null) && codqr.length()==11 )
		{
			String tmp2="", tmpplanta="", tmpedificio="", tmpcodqr="";
			tmp2 = codqr.substring(0, 3);
			tmpedificio = codqr.substring(3, 5);
			tmpplanta = codqr.substring(5, 7);
			tmpcodqr = codqr.substring(7,11);
			
			
			
			ene = Integer.parseInt(tmp2);
			if (ene <= 1)
			{
				queTabla = getResources().getString(R.string.tabla_etsiitpasos);
			}
			else if ( ene == 2)
			{
				queTabla = getResources().getString(R.string.tabla_ftipasos);
			}
					
			cod = cod.trim();
			xCanal = extras.getString("canal");
			
			tv2.setText(orig+"\n"+destino);
			
			new ConsultaBD().execute("pasos");
		}
		
		fin.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(Rutas.this, CapturaQR.class);
				i.putExtra("cod", codigo);
				i.putExtra("sconcepto", destino);
				i.putExtra("scodqr", qrdestino);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		next.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				if (NPASO == TOTALPASOS)
				{
					Toast.makeText(getBaseContext(), "Pulse fin para ir al destino", Toast.LENGTH_SHORT).show();
				}
				
				else
				{
					if (NPASO == TOTALPASOS-1)
					{
						Toast.makeText(getBaseContext(), "Último paso", Toast.LENGTH_SHORT).show();
					}
					if (NPASO == 1)
					{
						NPASO += 1;
						vPaso = "P2";
						new ConsultaBD().execute("pasos");
					}
					else if (NPASO == 2)
					{
						NPASO += 1;
						vPaso = "P3";
						new ConsultaBD().execute("pasos");
					}
					else if (NPASO == 3)
					{
						NPASO += 1;
						vPaso = "P4";
						new ConsultaBD().execute("pasos");
					}
					else if (NPASO == 4)
					{
						NPASO += 1;
						vPaso = "P5";
						new ConsultaBD().execute("pasos");
					}
					else if (NPASO == 5)
					{
						NPASO += 1;
						vPaso = "P6";
						new ConsultaBD().execute("pasos");
					}
					else if (NPASO == 6)
					{
						NPASO += 1;
						vPaso = "P7";
						new ConsultaBD().execute("pasos");
					}
				}
				
			}
		});
		back.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if (NPASO == 1)
				{
					Toast.makeText(getBaseContext(), "Primer paso", Toast.LENGTH_SHORT).show();
				}
				
				/*if (NPASO == 1)
				{
					NPASO += 1;
					vPaso = "P2";
					new MyTask().execute(KEY_121);
				}*/
				else if (NPASO == 2)
				{
					NPASO -= 1;
					vPaso = "P1";
					new ConsultaBD().execute("pasos");
				}
				else if (NPASO == 3)
				{
					NPASO -= 1;
					vPaso = "P2";
					new ConsultaBD().execute("pasos");
				}
				else if (NPASO == 4)
				{
					NPASO -= 1;
					vPaso = "P3";
					new ConsultaBD().execute("pasos");
				}
				else if (NPASO == 5)
				{
					NPASO -= 1;
					vPaso = "P4";
					new ConsultaBD().execute("pasos");
				}
				else if (NPASO == 6)
				{
					NPASO -= 1;
					vPaso = "P5";
					new ConsultaBD().execute("pasos");
				}
				else if (NPASO == 7)
				{
					NPASO -= 1;
					vPaso = "P6";
					new ConsultaBD().execute("pasos");
				}
				/*if (NPASO > cnp)
				{
					NPASO = 1;
					vPaso = "P1";
					new MyTask().execute(KEY_121);
				}*/
			}
		});
		
	}// Fin onCreate
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putString("rPaso", tv5.getText().toString());
	  savedInstanceState.putString("rPasoDe", tv7.getText().toString());
	  savedInstanceState.putInt("rCod", codigo);
	  savedInstanceState.putString("rCodruta", cod);
	  savedInstanceState.putString("rOrig", orig);
	  savedInstanceState.putString("rCodqr", codqr);
	  savedInstanceState.putInt("rNPASO", NPASO);
	  savedInstanceState.putString("rVPaso", vPaso);

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore state from savedInstanceState
	  String myString = savedInstanceState.getString("rCanal");
	  String miResumen = savedInstanceState.getString("rPaso");
	  String miPasode = savedInstanceState.getString("rPasoDe");
	  cod = savedInstanceState.getString("rCodruta");
	  orig = savedInstanceState.getString("rOrig");
	  codigo = savedInstanceState.getInt("rCod");
	  codqr = savedInstanceState.getString("rCodqr");
	  NPASO = savedInstanceState.getInt("rNPASO");
	  vPaso = savedInstanceState.getString("rVPaso");

	  tv5.setText(miResumen);
	  tv7.setText(miPasode);
	}
	
	// *******************************************
    // Clase para la conexión con la Base de Datos 
    private class ConsultaBD extends AsyncTask<String, Void, Void>
	{
    	
	    String auxcad;
	    String consultaFinal;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(Rutas.this); 
        private Context mContext;
	    
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
	        	
		         textUrl = new URL(getResources().getString(R.string.urlConsultaRutas)+"CANAL="+xCanal+"&RUTA="+cod+"&PASO="+vPaso+"&LATABLA="+queTabla);
	
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
		    	tv7.setText("Paso:"+NPASO+" de "+ sTOTALPASOS);
	    	}
	    	else
	    	{
	    		//tv7.setText("No se encontró información");
	    		tv7.setText(textResult);
	    	}
	    	new LeeRutaTexto().execute("pasos");
	    	
	    	Dialog.dismiss();
		    super.onPostExecute(result);   
	    }
	}// Fin clase AsynTask

    //*****************************************************************************************
    private class LeeRutaTexto extends AsyncTask<String, Void, Void>
	{
	    String resultado="";
	    private ProgressDialog Dialog = new ProgressDialog(Rutas.this); 
	    
	    protected void onPreExecute() 
        {  
            Dialog.setMessage("Buscando información");  

            //Dialog.setTitle("Requesting Hospital Name");
            Dialog.show();  
        }  
	    
	    @Override
	    protected Void doInBackground(String... arg) 
	    {
	    	try 
	    	{
	    	    // Create a URL for the desired page
	    		cURL = cURL.replace(" ", "");
	    	    URL url = new URL(cURL);

	    	    // Read all the text returned by the server
	    	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
	    	    String str;
	    	    StringBuilder strbldr = new StringBuilder();
	    	    while ((str = in.readLine()) != null) 
	    	    {
	    	        // str is one line of text; 
	    	    	//readLine() strips the newline character(s)
	    	    	strbldr.append(URLDecoder.decode(str, "UTF-8"));
	    	    }
	    	    resultado = strbldr.toString();
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
	    	tv5.setText(resultado);
	    	Dialog.dismiss();
	    	
		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase LeeRutaTexto
    
}