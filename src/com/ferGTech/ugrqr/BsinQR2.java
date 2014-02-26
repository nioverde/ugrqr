package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BsinQR2 extends Activity
{

	TextView tv1;
	ListView lv;
	ArrayList<Map<String, String>> mLista;
	SimpleAdapter mAdapter;
	String canal="", concepto="", codqr="", cQR="", cDestino="", cConcepto="";
	String centro="", qrcentro="";
	String queTabla="";
	int codigo, ene=0;
	Bundle extras;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bsinqr2);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		
		extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		centro = extras.getString("scentro");
		qrcentro = extras.getString("sqrcentro");
		if (extras.getString("scanal") != null)
		{
			canal = extras.getString("scanal");
		}
		else
		{
			canal = "ESP_TXT";
		}
		
		/*
		if (qrcentro.startsWith("QR"))
		{
			String tmp = qrcentro.replace(" ", "");
			tmp = tmp.replace("QR","");
			
			
			ene = Integer.parseInt(tmp);
		}
		else
		{
			qrcentro = qrcentro.replace(" ", "");
			String tmp = qrcentro.replace("QR","");
			ene = Integer.parseInt(tmp);
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
		*/
		if ( !qrcentro.equals(null) && qrcentro.length()==11 )
		{
			String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
			tmp = qrcentro.substring(0, 3);
			tmpedificio = qrcentro.substring(3, 5);
			tmpplanta = qrcentro.substring(5, 7);
			tmpcodqr = qrcentro.substring(7,11);
			
			
			
			ene = Integer.parseInt(tmp);
			if (ene <= 1)
			{
				queTabla = "ETSIITCONTENIDOS";
			}
			else if ( ene == 2)
			{
				queTabla = "FTICONTENIDOS";
			}
					
			//tv3.setText(queTabla);
			//Toast.makeText(getBaseContext(), scodqr, Toast.LENGTH_LONG).show();
			new BuscaDestinosFinal(getApplicationContext()).execute("inicio");
		}
		
		mLista = new ArrayList<Map<String, String>>();
		
		
		
		
		lv = (ListView)findViewById(R.id.listView1);
		mAdapter = new SimpleAdapter(this, mLista, android.R.layout.simple_list_item_2, 
					new String[] {"concepto", "codqr"}, 
					new int[] {android.R.id.text1, android.R.id.text2});
		lv.setAdapter(mAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long id) 
			{
				concepto = mLista.get(posicion).get("concepto");
				codqr = mLista.get(posicion).get("codqr");
				
				if ( !concepto.equals("") && !codqr.equals("") )
				{
					if (codigo == 1 || codigo == 2)
					{
						Intent iTexto = new Intent( BsinQR2.this, CapturaQR.class );
						iTexto.putExtra("sconcepto", concepto);
						iTexto.putExtra("scodqr", codqr);
						iTexto.putExtra("cod", codigo);
						iTexto.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iTexto);
					}
					else if (codigo == 3)
					{
						Intent iVideo = new Intent(BsinQR2.this, CapturaQRVideo.class);
						iVideo.putExtra("cod", codigo);
						iVideo.putExtra("sconcepto", concepto);
						iVideo.putExtra("scodqr", codqr);
						iVideo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iVideo);
					}
					else if (codigo >= 4 && codigo <=7 )
					{
						Intent iAudio = new Intent( BsinQR2.this, CapturaQRAudio.class );
						iAudio.putExtra("sconcepto", concepto);
						iAudio.putExtra("scodqr", codqr);
						iAudio.putExtra("cod", codigo);
						iAudio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iAudio);
					}
				}
				
				
			}
		});
		
		
	}
	
	
	
	/* ************************************ */
    /* Clase para buscar el nuevo concepto */ 
	/* ********************************** */
    private class BuscaDestinosFinal extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(BsinQR2.this); 
        private Context mContext;
        
        
        public BuscaDestinosFinal(Context context) 
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
	        	
		         textUrl = new URL(R.string.urlBsinQR2+"LATABLA="+queTabla+"&CANAL="+canal);
	
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
	    	Map<String, String> datum = new HashMap<String, String>(2);
	    	for (int j=0; j<vBr.length; j++)
    		{
    			v2p = vBr[j].split(":");
    			if (v2p.length > 1)
    			{
    				if (j%2 != 0)
    				{
    					
    					cDestino = v2p[1];
    					datum.put("concepto", cDestino);
    					mLista.add(datum);
    					mAdapter.notifyDataSetChanged();
    				}
    				else
    				{
    					datum = new HashMap<String, String>(2);
    					codqr = v2p[1];
    					datum.put("codqr", codqr);
    					
    				}
    			}
    			else
    			{
    				//tv1.setText("error");
    			}
    		}
	    	//tv1.setText("Selecciona un centro");
	    	Dialog.dismiss();
	    	//Toast.makeText(getBaseContext(), textResult, Toast.LENGTH_LONG).show();
	    	
		    super.onPostExecute(result);   
	    
	    }
	}
	
	
}