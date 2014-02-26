package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class LugaresAudio extends Activity
{
	TextView tv3;
	ListView lv;
	List mLista;
	LugaresListAdapter mAdapter;
	Context ctx;
	
	String canal="", concepto="", codqr="", cRuta="", cDestino="", qrdestino="";
	Bundle extras;
	int ene = 0;
	String queTabla="";
	
	
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares);

		tv3 = (TextView)findViewById(R.id.textView3);
		
		ctx = this;
		
		extras = getIntent().getExtras();
		canal = extras.getString("scanal");
		concepto = extras.getString("sconcepto");
		codqr = extras.getString("scodqr");
		
		codqr = codqr.replace(" ", "");
		if (codqr.startsWith("QR"))
		{
			//codqr = codqr.replace(" ", "");
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
			queTabla = "ETSIITDESTINOS";
		}
		else if (ene >= 1500)
		{
			queTabla = "FTIDESTINOS";
		}
		mLista = new ArrayList();
		
		// Auto ejecucion de la hebra asíncrona
		new BuscaDestinos(this).execute("busqueda");
		
		
		
		lv = (ListView)findViewById(R.id.listView1);

		mAdapter = new LugaresListAdapter(ctx, R.layout.list_row, mLista);
		lv.setAdapter(mAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long id) 
			{
				// TODO Auto-generated method stub
				String lugar, cr, qrdest;
				Places posi;
				//lugar = mLista.get(posicion).get("destino");
				//cr = mLista.get(posicion).get("cruta");
				posi = (Places) mLista.get(posicion);
				
				lugar = posi.getName();		
				qrdest = posi.getqr();
				cr = posi.getRute();
				
				if (! cr.equals(" "))
				{
					Intent j = new Intent(LugaresAudio.this, RutasAudio.class);
					j.putExtra("origen", concepto);
					j.putExtra("destino", lugar);
					j.putExtra("codigoruta", cr);
					j.putExtra("canal", canal);
					j.putExtra("scodqr", codqr);
					j.putExtra("qrdestino", qrdest);
					startActivity(j);
				}
				else
				{
					Intent otrabusq = new Intent(LugaresAudio.this, BusquedaAlternativa.class);
					otrabusq.putExtra("origen", concepto);
					otrabusq.putExtra("qr", codqr);
					otrabusq.putExtra("canal", canal);
					startActivity(otrabusq);
				}
			}
		});

	}// Fin onCreate
	
	
	//************* FUNCIONES PARA EL CAMBIO DE CONFIGURACIÓN DE ORIENTACIÓN DEL MÓVIL ***************
		@Override
		public void onSaveInstanceState(Bundle savedInstanceState) 
		{
		  super.onSaveInstanceState(savedInstanceState);
		  // Save state to the savedInstanceState
		  savedInstanceState.putString("rURL", tv3.getText().toString());
		  savedInstanceState.putString("rCad", canal);
		  savedInstanceState.putString("rConcepto", concepto);
		  savedInstanceState.putString("rCodqr", codqr);
		}
		@Override
		public void onRestoreInstanceState(Bundle savedInstanceState) 
		{
		  super.onRestoreInstanceState(savedInstanceState);
		  // Restore state from savedInstanceState
		  String rurl = savedInstanceState.getString("rURL");
		  
		  canal = savedInstanceState.getString("rCad");
		  concepto = savedInstanceState.getString("rConcepto");
		  codqr = savedInstanceState.getString("rCodqr");

		  
		  tv3.setText(rurl);
		}
	
	// *******************************************
    // Clase para la conexión con la Base de Datos 
    private class BuscaDestinos extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(LugaresAudio.this); 
        private Context mContext;
        
        
        public BuscaDestinos(Context context) 
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
	        	codqr = codqr.replace(" ", "");
	        	 String micanal = "ESP_TXT";
		         textUrl = new URL(R.string.urlConsultaLugares+"QRCODE="+codqr+"&CANAL="+micanal+"&LATABLA="+queTabla);
	
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
	    	//HashMap<String, String> datum = new HashMap<String, String>();
	    	
	    	
	    	if (vBr.length >= 2)
	    	{
	    		int consecutivo1=0, consecutivo2=0;
		    	for (int j=0; j<vBr.length; j++)
	    		{
	    			v2p = vBr[j].split(":");
	    			if (v2p.length > 1)
	    			{
	    				if (j%3 == 0)
	    				{
	    					//datum = new HashMap<String, String>();
	    					cDestino = v2p[1];
	    					consecutivo1 = j + 1;
	    					consecutivo2 = j+ 2;
	    					//datum.put(TAG_LUGAR, cDestino);
	    					//mLista.add(cDestino);
	    				}
	    				else if (j == consecutivo1)
	    				{
	    					qrdestino = v2p[1];
	    					//datum.put("cruta", cRuta);
	    					//mLista.add(datum);
	    					//datum.clear();
	    				}
	    				else if ( j ==consecutivo2)
						{
							cRuta = v2p[1];
							//datum.put(TAG_RUTA, cRuta);
							mLista.add(new Places(cDestino, qrdestino, cRuta));
							mAdapter.notifyDataSetChanged();
						}
	    				//cDestino = v2p[1];
	        			
	    			}
	    		}
		    	mLista.add(new Places("No se encuentra en la lista", " ", " "));
		    	mAdapter.notifyDataSetChanged();
		    	
	    	}
	    	else
	    	{
	    		tv3.setText("No se encontró información");
	    	} 
	    	Dialog.dismiss();
		    super.onPostExecute(result); 
	    }
	
	
	}
    
    /*  */
    public class Places 
    {

        private String name;
        private String qr;
        private String route;

        public Places (String name, String qrcde, String rut) 
        {
            super();
            this.name = name;
            this.qr = qrcde;
            this.route = rut;
        }
        public String getName() 
        {
            return name;        
        }
        public void setName(String nameText) 
        {
            name = nameText;
        }
        public String getqr() 
        {
            return qr;
        }
        public void setqr(String qrcde) 
        {
            this.qr = qrcde;
        }
        public String getRute() 
        {
            return route;
        }
        public void setRute(String rut) 
        {
            this.route = rut;
        }
    }
}// Fin clase LugaresAudio