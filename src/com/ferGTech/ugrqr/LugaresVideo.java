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
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LugaresVideo extends Activity
{
	
	ListView lv;
	List mLista;
	Context ctx;
	LugaresListAdapter mAdapter;
	Bundle extras;
	TextView  tv3;
	String xCanal;
	String cDestino = "", cRuta = "", codqr="", qrdestino="";
	String[] vDestino, vRuta;
	String sconcepto, queTabla;
	int ene=0, codigo = 0;

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares);
		
		ctx = this;
		
		
		tv3 = (TextView)findViewById(R.id.textView3);
		
		extras = getIntent().getExtras();
		xCanal = extras.getString("scanal");
		sconcepto = extras.getString("sconcepto");
		codqr = extras.getString("scodqr");
		codigo = extras.getInt("cod");
		
		//Toast.makeText(getBaseContext(), sconcepto, Toast.LENGTH_LONG).show();
		
		
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
		else
		{
			queTabla = "FTIDESTINOS";
		}
		
		tv3.setText("Estás en: "+sconcepto);
		
		mLista = new ArrayList();
		
		// Auto ejecucion de la hebra asíncrona
		new BuscaDestinos(this).execute("busqueda");
		
		
		
		lv = (ListView)findViewById(R.id.listView1);
		
		//mAdapter = new LugaresListAdapter(ctx, R.layout.list_row, mLista);
		//lv.setAdapter(mAdapter);
		
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
					Intent j = new Intent(LugaresVideo.this, RutasVideo.class);
					j.putExtra("origen", sconcepto);
					j.putExtra("destino", lugar);
					j.putExtra("codigoruta", cr);
					j.putExtra("canal", xCanal);
					j.putExtra("scodqr", codqr);
					j.putExtra("qrdestino", qrdest);
					j.putExtra("cod", codigo);
					startActivity(j);
				}
				else
				{
					Intent otrabusq = new Intent(LugaresVideo.this, BusquedaAlternativa.class);
					otrabusq.putExtra("origen", sconcepto);
					otrabusq.putExtra("qr", codqr);
					otrabusq.putExtra("canal", xCanal);
					startActivity(otrabusq);
				}
			}
		});
	}
	
	
	// *******************************************
    // Clase para la conexión con la Base de Datos 
    private class BuscaDestinos extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(LugaresVideo.this); 
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
	        	// Prueba	
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
		    	
		    	
		    	//mAdapter.notifyDataSetChanged();
	    	}
	    	else
	    	{
	    		tv3.setText("No se encontró información");
	    	}
	    	
	    	
	    	//datum = new HashMap<String, String>();
	    	//datum.put(TAG_LUGAR, "No se encuentra en la lista");
	    	//datum.put(TAG_RUTA, " ");
	    	//datum.put("cruta", " ");
	    	
	    	
			//
	    	Dialog.dismiss();
		    super.onPostExecute(result);   
	    }
	}// Fin clase AsynTask
	
    
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
}