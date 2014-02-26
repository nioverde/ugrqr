package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class BusquedaAlternativa extends Activity
{
	TextView tv1, resultado;
	EditText edt1;
	Button buscar, sugerencia;
	Bundle extras;
	int codigo;
	String origen, codqr, oCodqr, canal, destino="";
	String dcodqr, dCod, dEdificio, dPlanta;
	String cCod="", cEdificio="", cPlanta="", queTabla="", queTablaDestino="";
	
	int ene = 0;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busquedaalternativa);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		resultado = (TextView)findViewById(R.id.textView2);
		edt1 = (EditText)findViewById(R.id.editText1);
		buscar = (Button)findViewById(R.id.button1);
		sugerencia = (Button)findViewById(R.id.button2);
		
		sugerencia.setVisibility(Button.INVISIBLE);
		
		extras = getIntent().getExtras();
		origen = extras.getString("origen");
		codqr = extras.getString("qr");
		canal = extras.getString("canal");
		codigo = extras.getInt("cod");
		
		tv1.setText("Estás en: "+"\n"+origen);
		
		if ( !codqr.equals(null) && codqr.length()==11 )
		{
			String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
			tmp = codqr.substring(0, 3);
			tmpedificio = codqr.substring(3, 5);
			tmpplanta = codqr.substring(5, 7);
			tmpcodqr = codqr.substring(7,11);
			
			
			
			ene = Integer.parseInt(tmp);
			if (ene <= 1)
			{
				queTabla = "ETSIITLUGARESN2";
				queTablaDestino = "ETSIITLUGARESN2";
			}
			else if ( ene == 2)
			{
				queTabla = "FTILUGARESN2";
				queTablaDestino = "FTILUGARESN2";
			}
					
			new BuscaOrigen().execute(origen);
		}
		
		
		
		// ************* eventos **********************
		buscar.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				destino = edt1.getText().toString();
				oCodqr = codqr;
				dcodqr = "";
				
				new BuscaDestinos().execute(destino);
				
			}
		});
		
		sugerencia.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent sugerido = new Intent(BusquedaAlternativa.this, Lugares.class);
				sugerido.putExtra("scanal", canal);
				sugerido.putExtra("cod", codigo);
				sugerido.putExtra("sconcepto", origen);
				sugerido.putExtra("scodqr", codqr);
				startActivityForResult(sugerido, 0);
				
			}
		});
		
	}// Fin onCreate()


	// *******************************************
    // Clase para la conexión con la Base de Datos 
    private class BuscaOrigen extends AsyncTask<String, Void, Void>
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
	        	
	        	String xconcepto = arg[0] ;
	        	if (xconcepto.startsWith(" "))
	        	{
	        		xconcepto = xconcepto.replaceFirst(" " , "");
	        	}
	        	queTabla = queTabla.replace(" " , "");
	        	xconcepto = xconcepto.replace(" ", "%20");
		         textUrl = new URL(R.string.urlBusquedaAlternativa+"CONCEPTO="+xconcepto+"&LATABLA="+queTabla);
	
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
	    	if (vBr.length >= 3)
	    	{
	    		
	    		for (int j=0; j<4; j++)
	    		{
	    			if (j == 0)
	    			{
	    				v2p = vBr[0].split(":");
	    				cCod = v2p[1];
	    				//sconcepto = cConcepto;
	    			}
	    			else if (j == 1)
	    			{
	    				v2p = vBr[j].split(":");
	    				cEdificio = v2p[1];
	    			}
	    			else if (j == 2)
	    			{
	    				v2p = vBr[j].split(":");
	    				cPlanta = v2p[1];
	    			}
	    		}
	    		resultado.setText("QRPADRE:"+cCod+"\n"+"Edificio:"+cEdificio+"\n"+"Planta:"+cPlanta);
	    	}
	    	else
	    	{
	    		if ( queTabla.endsWith("3") )
	    		{
	    			Toast.makeText(getBaseContext(), "Busqueda iniciada3", Toast.LENGTH_SHORT).show();
	    			queTabla = queTabla.replace("3", "2");
	    			new BuscaOrigen().execute(origen);
	    		}
	    		else if ( queTabla.endsWith("2") )
	    		{
	    			Toast.makeText(getBaseContext(), "Busqueda iniciada2", Toast.LENGTH_SHORT).show();
	    			queTabla = queTabla.replace("2", "1");
	    			new BuscaOrigen().execute(origen);
	    		}
	    		else if ( queTabla.endsWith("1") )
	    		{
	    			Toast.makeText(getBaseContext(), "Busqueda iniciada1", Toast.LENGTH_SHORT).show();
	    			queTabla = queTabla.replace("1", "0");
	    			new BuscaOrigen().execute(origen);
	    		}
	    		else
	    		{
	    			resultado.setText("Ubicación no disponible");
	    		}
	    	}
	    	
	    	
		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase AsynTask
	
 // *******************************************
    // Clase para la conexión con la Base de Datos 
    private class BuscaDestinos extends AsyncTask<String, Void, Void>
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
	        	
	        	String xconcepto ;
	        	if (arg[0].startsWith(" "))
	        	{
	        		xconcepto = arg[0].replaceFirst(" " , "");
	        	}
	        	xconcepto = arg[0].replace(" ", "%20");
		         textUrl = new URL(R.string.urlBusquedaAlternativa+"CONCEPTO="+xconcepto+"&LATABLA="+queTablaDestino);
	
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
	    	if (vBr.length >= 3)
	    	{
	    		
	    		for (int j=0; j<4; j++)
	    		{
	    			if (j == 0)
	    			{
	    				v2p = vBr[0].split(":");
	    				dCod = v2p[1];
	    				//sconcepto = cConcepto;
	    			}
	    			else if (j == 1)
	    			{
	    				v2p = vBr[j].split(":");
	    				dEdificio = v2p[1];
	    			}
	    			else if (j == 2)
	    			{
	    				v2p = vBr[j].split(":");
	    				dPlanta = v2p[1];
	    			}
	    		}
	    		resultado.setText(cCod+cEdificio+cPlanta);
	    		
	    		ComparaResultados();
	    	}
	    	else
	    	{
	    		
	    		if ( queTablaDestino.endsWith("N3") )
	    		{
	    			queTablaDestino = queTablaDestino.replace("N3", "N2");
	    			new BuscaDestinos().execute(origen);
	    		}
	    		else if ( queTablaDestino.endsWith("N2") )
	    		{
	    			queTablaDestino = queTablaDestino.replace("N2", "N1");
	    			new BuscaDestinos().execute(origen);
	    		}
	    		else if ( queTablaDestino.endsWith("N1") )
	    		{
	    			queTablaDestino = queTablaDestino.replace("N1", "N0");
	    			new BuscaDestinos().execute(origen);
	    		}
	    		else
	    		{
	    			resultado.setText("Ubicación no disponible");
	    		}
	    	}
	    	
	    	
		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase AsynTask
	
    public void ComparaResultados()
    {
    	String msj="";
    	if ( !cEdificio.equals(dEdificio))
    	{
    		msj += "Usted se encuentra en el edificio: E"+cEdificio+" y ";
			msj += "su destino está en el edificio: E"+dEdificio;
    		resultado.setText(msj);
    	}
    	else
    	{
    		if ( !cPlanta.equals(dPlanta))
    		{
    			msj += "Usted se encuentra en la planta: P"+cPlanta+" y ";
    			msj += "su destino está en la planta: P"+dPlanta;
    			resultado.setText(msj);
    		}
    		else
    		{
    			resultado.setText("Sugerencia registrada");
    			sugerencia.setVisibility(Button.VISIBLE);
    		}
    	}
    	
    }
	
}// Fin clase Busqueda Alternativa 