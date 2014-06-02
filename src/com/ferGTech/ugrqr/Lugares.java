package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Lugares extends Activity
{
	
	ListView lv;
	List mLista;
	Context ctx;
	LugaresListAdapter mAdapter;
	Bundle extras;
	TextView tv3;
	String xCanal;
	int codigo;
	String cDestino = "", cRuta = "", codqr="", qrdestino="";
	String[] vDestino, vRuta;
	String sconcepto, queTabla;
	int ene=0;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares);
		
		ctx = this;


		tv3 = (TextView)findViewById(R.id.textView3);
		
		extras = getIntent().getExtras();
		
		if (extras.getString("scanal") != null)
		{
			xCanal = extras.getString("scanal");
		}
		else
		{
			xCanal = "ESP_TXT";
		}
		
		codigo = extras.getInt("cod");
		sconcepto = extras.getString("sconcepto");
		codqr = extras.getString("scodqr");
		
		if ( codqr.compareTo("")!=0 )
		{
			String codqr2 = codqr;
			codqr2 = codqr2.replace(" ", "");
			String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
			tmp = codqr2.substring(0, 3);
			tmpedificio = codqr2.substring(3, 5);
			tmpplanta = codqr2.substring(5, 7);
			tmpcodqr = codqr2.substring(7,11);
			
			
			
			ene = Integer.parseInt(tmp);
			if (ene <= 1)
			{
				//queTabla = getResources().getString(R.string.tabla_etsiitrutas);
				queTabla = "EtsiitRutas";
			}
			else if ( ene == 2)
			{
				queTabla = getResources().getString(R.string.tabla_ftirutas);
			}
		}
		
		tv3.setText("Estás en: "+"\n"+sconcepto);
		
		mLista = new ArrayList();
		
		// Auto ejecucion de la hebra asíncrona
		new BuscaDestinos().execute("busqueda");
		
		
		
		lv = (ListView)findViewById(R.id.listView1);
		mAdapter = new LugaresListAdapter(ctx, R.layout.list_row, mLista);
		lv.setAdapter(mAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long id) 
			{
				String lugar, cr, qrdest;
				Places posi;
				posi = (Places) mLista.get(posicion);
				
				lugar = posi.getName();		
				qrdest = posi.getqr();
				cr = posi.getRute();
						
				
				if ( !cr.equals(" ") 	)
				{
					if ( codigo==1 || codigo==2 )
					{
						Intent j = new Intent(Lugares.this, Rutas.class);
						j.putExtra("origen", sconcepto);
						j.putExtra("destino", lugar);
						j.putExtra("codigoruta", cr);
						j.putExtra("canal", xCanal);
						j.putExtra("cod", codigo);
						j.putExtra("scodqr", codqr);
						j.putExtra("qrdestino", qrdest);
						startActivity(j);
					}
					else if (codigo==3)
					{
						Intent irutas  = new Intent(Lugares.this, RutasVideo.class);
						irutas .putExtra("origen", sconcepto);
						irutas.putExtra("destino", lugar);
						irutas .putExtra("codigoruta", cr);
						irutas.putExtra("canal", xCanal);
						irutas.putExtra("cod", codigo);
						irutas.putExtra("scodqr", codqr);
						irutas.putExtra("qrdestino", qrdest);
						startActivity(irutas);
					}
					else if ( codigo>=4 && codigo<=7 )
					{
						Intent irutas  = new Intent(Lugares.this, RutasAudio.class);
						irutas .putExtra("origen", sconcepto);
						irutas.putExtra("cod", codigo);
						irutas.putExtra("destino", lugar);
						irutas .putExtra("codigoruta", cr);
						irutas.putExtra("canal", xCanal);
						irutas.putExtra("scodqr", codqr);
						irutas.putExtra("qrdestino", qrdest);
						startActivity(irutas);
					}
					
				}
				else
				{
					Intent otrabusq = new Intent(Lugares.this, BusquedaAlternativa.class);
					otrabusq.putExtra("origen", sconcepto);
					otrabusq.putExtra("qr", codqr);
					otrabusq.putExtra("canal", xCanal);
					otrabusq.putExtra("cod", codigo);
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
	    
	    @Override
	    protected Void doInBackground(String... arg) 
	    {
	    	
		    URL textUrl;
	        try 
	        {
	        	//String xconcepto = sconcepto.replace(" ", "%20");
	        	codqr = codqr.replace(" ", "");
		         textUrl = new URL(getResources().getString(R.string.urlConsultaLugares)+"QRCODE="+codqr+"&CANAL="+xCanal+"&LATABLA="+queTabla);
	
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
	    	int consecutivo1=0, consecutivo2=0;
	    	for (int j=0; j<vBr.length; j++)
    		{
    			v2p = vBr[j].split(":");
    			if (v2p.length > 1)
    			{
    				if (j%3 == 0)
    				{
    					
    					byte[] bytes= new byte[]{};
    					cDestino = v2p[1];
    					try 
    					{
							bytes = cDestino.getBytes("UTF-8");
						} 
    					catch (UnsupportedEncodingException e) 
    					{
							e.printStackTrace();
						}
    					try 
    					{
							cDestino = new String(bytes,"UTF-8");
						} 
    					catch (UnsupportedEncodingException e) 
    					{
							e.printStackTrace();
						}
    					consecutivo1 = j + 1;
    					consecutivo2 = j+ 2;
    				}
    				else if (j == consecutivo1)
    				{
    					qrdestino = v2p[1];
    				}
    				else if ( j ==consecutivo2)
					{
						cRuta = v2p[1];
						mLista.add(new Places(cDestino, qrdestino, cRuta));
						mAdapter.notifyDataSetChanged();
					}
        			
    			}
    		}
	    	mLista.add(new Places("No se encuentra en la lista", " ", " "));
	    	mAdapter.notifyDataSetChanged();	    	
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