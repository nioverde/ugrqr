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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class BuscadorSinQR extends Activity
{

	TextView tv1;
	ListView lv;
	ArrayList<Map<String, String>> mLista;
	SimpleAdapter mAdapter;
	Bundle extras;
	
	String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
	int ene = 0, codigo;
	String queTabla="";
	String centro="", qrcentro="";
	String canal="", codqr="", cDestino="";
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscadorsinqr);

		tv1 = (TextView)findViewById(R.id.textView1);
		
		
		extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		if (extras.getString("scanal") != null)
		{
			canal = extras.getString("scanal");
		}
		else
		{
			canal = "ESP_TXT";
		}
		
		mLista = new ArrayList<Map<String, String>>();
		new RellenaLista(getApplicationContext()).execute("buscador");
		
		lv = (ListView)findViewById(R.id.listView1);
		mAdapter = new SimpleAdapter(this, mLista, R.layout.centros_item, 
					new String[] {"xcentro", "xqrcentro"}, 
					new int[] {R.id.tv_centro});
		lv.setAdapter(mAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long id) 
			{
				// TODO Auto-generated method stub
				
				centro = mLista.get(posicion).get("xcentro");
				qrcentro = mLista.get(posicion).get("xqrcentro");				
				
				qrcentro = qrcentro.replace(" ", "");
				
				if ( !qrcentro.equals(null) && qrcentro.length()==11 )
				{
					
					tmp = qrcentro.substring(0, 3);
					tmpedificio = qrcentro.substring(3, 5);
					tmpplanta = qrcentro.substring(5, 7);
					tmpcodqr = qrcentro.substring(7,11);
					
					ene = Integer.parseInt(tmp);
					
					if ( ene <= 2)
					{
						Intent busca2 = new Intent(BuscadorSinQR.this, BsinQR2.class);
						busca2.putExtra("cod", codigo);
						busca2.putExtra("scanal", canal);
						busca2.putExtra("scentro", centro);
						busca2.putExtra("sqrcentro", qrcentro);
						startActivity(busca2);
					}	
					else
					{
						Toast.makeText(getBaseContext(), "El centro no contiene información", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getBaseContext(), "El centro no está dado de alta", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
	}
	
	

	/* **************************************** */
    /* Clase para rellenar la lista con Centros */ 
	/* **************************************** */
    private class RellenaLista extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    
	    private ProgressDialog Dialog = new ProgressDialog(BuscadorSinQR.this); 
        private Context mContext;
        
        
        public RellenaLista(Context context) 
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
	        	String latabla=getResources().getString(R.string.tabla_centros);
	        	latabla = latabla.trim();
		         textUrl = new URL(getResources().getString(R.string.urlBuscadorSinQR)+"LATABLA="+latabla);
	
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
    				if (j%2 == 0)
    				{
    					datum = new HashMap<String, String>(2);
    					cDestino = v2p[1];
    					datum.put("xcentro", cDestino);
    					//mLista.add(cDestino);
    				}
    				else
    				{
    					codqr = v2p[1];
    					datum.put("xqrcentro", codqr);
    					mLista.add(datum);
    					mAdapter.notifyDataSetChanged();
    				}
    			}
    			/*else
    			{
    				tv1.setText("error");
    			}*/
    		}
	    	//tv1.setText("Selecciona un centro");
	    	Dialog.dismiss();
	    	
		    super.onPostExecute(result);   
	    }
	}// Fin de la clase RellenaLista
	
    
	
	
}


