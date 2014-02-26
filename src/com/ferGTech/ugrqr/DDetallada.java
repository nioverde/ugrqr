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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DDetallada extends Activity
{
	Button ir;
	Bundle extras;
	String canal, codqr;
	String queTabla;
	int codigo;
	int ene=0;
	String cCanal, cConcepto, cDDetallada="";
	TextView  tv5;
	String xConcepto;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ddetallada);
		
		ir = (Button)findViewById(R.id.button1);
		tv5 = (TextView)findViewById(R.id.textView5);
		tv5.setMovementMethod(new ScrollingMovementMethod());
		
		extras = getIntent().getExtras();
		canal = extras.getString("scanal");
		codigo = extras.getInt("cod");
		xConcepto = extras.getString("sconcepto");
		codqr = extras.getString("scodqr");
		cDDetallada = extras.getString("url");
		
		
		
		
		if (codqr.startsWith("QR"))
		{
			codqr = codqr.replace(" ", "");
			String tmp = codqr.replace("QR","");
			
			
			ene = Integer.parseInt(tmp);
		}
		else
		{
			ene = Integer.parseInt(codqr);
		}
		
		
		// ***** SELECCION DE LA TABLA PARA LUGARES DE CADA EDIFICIO ********
		if (ene < 1500)
		{
			queTabla = "ETSIITCONTENIDOS";
		}
		else
		{
			queTabla = "FTICONTENIDOS";
		}
		
		// **** Si puedo me ahorro la consulta a la BD y accedo directamente al archivo txt
		if ( !cDDetallada.equals("") )
    	{
    		new LeeDescripcionTexto(getApplicationContext()).execute(cDDetallada);
    	}
    	else
    	{
    		new MyTask().execute("busqueda");
    	}
		
		
		ir.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(DDetallada.this, Lugares.class);
				intent.putExtra("scanal", canal);
				intent.putExtra("cod", codigo);
				intent.putExtra("sconcepto", xConcepto);
				intent.putExtra("scodqr", codqr);
				startActivityForResult(intent, 0);
			}
		});
	}// Fin onCreate
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	  super.onSaveInstanceState(savedInstanceState);
	  // Save state to the savedInstanceState
	  savedInstanceState.putString("rDescripcion", tv5.getText().toString());
	  savedInstanceState.putString("rCad", canal);
	  savedInstanceState.putInt("rCod", codigo);
	  savedInstanceState.putString("rConcepto", xConcepto);
	  savedInstanceState.putString("rCodqr", codqr);
	  savedInstanceState.putString("cURL", cDDetallada);

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore state from savedInstanceState
	  String myString = savedInstanceState.getString("rCanal");
	  String miDescripcion = savedInstanceState.getString("rDescripcion");
	  canal = savedInstanceState.getString("rCad");
	  codigo = savedInstanceState.getInt("cod");
	  xConcepto = savedInstanceState.getString("rConcepto");
	  codqr = savedInstanceState.getString("rCodqr");
	  cDDetallada = savedInstanceState.getString(cDDetallada);
	  tv5.setText(miDescripcion);
	}
	
	
	
	private class MyTask extends AsyncTask<String, Void, Void>
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
	        	String sconcept = xConcepto.replace(" ", "%20");
	        	String scanal = canal.replace(" ", "");
		         textUrl = new URL(R.string.urlConsultaContenido+"CANAL="+scanal+"&CONCEPTO="+sconcept+"&QRCODE="+codqr+"&LATABLA="+queTabla);
	
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
	    		for (int j=0; j<vBr.length; j++)
	    		{
	    			if (j == 2)
	    			{
	    				v2p = vBr[0].split(":");
	    				cCanal = v2p[1];
	    			}
	    			else if (j == 1)
	    			{
	    				v2p = vBr[j].split(":");
	    				cConcepto = v2p[1];
	    			}
	    			/*else if (j == 4)
	    			{
	    				v2p = vBr[j].split(":");
	    				if ( v2p.length > 2 )
	    				{
	    					cResumen = v2p[1]+":"+v2p[2];
	    				}
	    			}*/
	    			else if (j == 5)
	    			{
	    				v2p = vBr[j].split(":");
	    				if ( v2p.length > 2 )
	    				{
	    					cDDetallada = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			
	    		}
	    		//tv4.setText(cResumen);
	    		//tv5.setText(cDDetallada);
	    	}
	    	else
	    	{
	    		//tv2.setText("error flag");
	    	}
	    	
	    	if ( !cDDetallada.equals("") )
	    	{
	    		//tv2.setText("No se encontró información");
	    	}
	    	if ( !cDDetallada.equals("") )
	    	{
	    		new LeeDescripcionTexto(getApplicationContext()).execute(cDDetallada);
	    	}
	    	else
	    	{
	    		tv5.setText("No se encontró información");
	    	}
	    	
	    	

	    	//tv5.setText(textResult);
		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase AsynTask
	
	
	// *******************************************************************************************
	private class LeeDescripcionTexto extends AsyncTask<String, Void, Void>
	{
	    String resultado="";
	    
	    private ProgressDialog Dialog = new ProgressDialog(DDetallada.this); 
        private Context mContext;
        
        
        public LeeDescripcionTexto (Context context) 
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
	}// Fin clase LeeDescripcionTexto
	
	
}// Fin clase DDetallada