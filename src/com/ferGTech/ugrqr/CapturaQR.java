package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class CapturaQR extends Activity
{
	Button accion, masinf, ira, cambiaConcep;
	TextView tv1, tv2, tv3;
	ImageView img;
	ImageManager imgManager;
	Bundle extras;
	TabHost tabHost;
	
	int READ_CODE = 0;
	int codigo=0;
	String selcad = "";
	String cCanal, cConcepto, cURL="", cCodqr="", cResumen="", cDDetallada="", cImagen="";
	String sconcepto = "", scodqr="";
	String queTabla ="";
	String miparam1="", miparam2="";
	int ene=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escaneo);
		
		accion = (Button)findViewById(R.id.button1);
		masinf = (Button)findViewById(R.id.button2);
		ira = (Button)findViewById(R.id.button3);
		cambiaConcep = (Button)findViewById(R.id.button5);
		tv2 = (TextView)findViewById(R.id.textView2);
		tv3 = (TextView)findViewById(R.id.textView3);
		tv3.setMovementMethod(new ScrollingMovementMethod());
		img = (ImageView)findViewById(R.id.imageView1);
		
		imgManager = new ImageManager(this);
		
		masinf.setVisibility(Button.INVISIBLE);
		ira.setVisibility(Button.INVISIBLE);
		
		tabHost =(TabHost)findViewById(R.id.host);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Resumen", getResources().getDrawable(R.drawable.dpesta1));
		

		TabSpec spec2 = tabHost.newTabSpec("Tab 2");
		spec2.setIndicator("Imagen", getResources().getDrawable(R.drawable.dpesta2));
		spec2.setContent(R.id.tab2);

		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
		
		extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		
		
		if (codigo == 1)
		{
			selcad = getResources().getString(R.string.esptxt);
			//tv1.setText("Formato: ESPAÑOL TEXTO");
		}
		else if (codigo == 2)
		{
			selcad = getResources().getString(R.string.ingtxt);
			//tv1.setText("Format: ENGLISH TEXT");
		}
		
		if ( extras.containsKey("sconcepto") && extras.containsKey("scodqr") )
		{
			sconcepto = extras.getString("sconcepto");
			scodqr = extras.getString("scodqr");
			
			//tv2.setText(sconcepto);
			tv2.setText("Estás en: "+"\n"+sconcepto);
			scodqr = scodqr.replace(" ", "");
			
			//Toast.makeText(getBaseContext(), scodqr.length(), Toast.LENGTH_LONG).show();
			
			/*
			if (scodqr.startsWith("QR"))
			{
				String tmp = scodqr.replace(" ", "");
				tmp = tmp.replace("QR","");
				
				
				ene = Integer.parseInt(tmp);
			}
			else
			{
				scodqr = scodqr.replace(" ", "");
				String tmp = scodqr.replace("QR","");
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
			//cURL = contents;
			
			new ConsultaBD(getApplicationContext()).execute(KEY_121);
			*/
			if ( !scodqr.equals(null) && scodqr.length()==11 )
			{
				String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
				tmp = scodqr.substring(0, 3);
				tmpedificio = scodqr.substring(3, 5);
				tmpplanta = scodqr.substring(5, 7);
				tmpcodqr = scodqr.substring(7,11);
				
				
				
				ene = Integer.parseInt(tmp);
				if (ene <= 1)
				{
					queTabla = getResources().getString(R.string.tabla_etsiitcontenidos);
				}
				else if ( ene == 2)
				{
					queTabla = getResources().getString(R.string.tabla_fticontenidos);
				}
						
				//tv3.setText(queTabla);
				//Toast.makeText(getBaseContext(), scodqr, Toast.LENGTH_LONG).show();
				new ConsultaBD(getApplicationContext()).execute("consulta");
			}
			else
			{
				tv3.setText("No se ha encontrado información");
			}
		}
		else
		{
			tv3.setText(R.string.textobienvenida);
		}
		accion.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent qr = new Intent(CapturaQR.this, DecoderActivity.class);
				startActivityForResult(qr, READ_CODE);
			}
		});
		
		masinf.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if (cDDetallada.compareTo("") != 0)
				{
					Intent detalle = new Intent(CapturaQR.this, DDetallada.class);
					detalle.putExtra("scanal", selcad);
					detalle.putExtra("cod", codigo);
					detalle.putExtra("sconcepto", cConcepto);
					detalle.putExtra("scodqr", scodqr);
					detalle.putExtra("url", cDDetallada);
					startActivityForResult(detalle, 0);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Por favor, introduce un lugar válido", Toast.LENGTH_LONG).show();
				}
			}
		});
		ira.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if (!sconcepto.equals(""))
				{
					Intent places = new Intent(CapturaQR.this, Lugares.class);
					places.putExtra("scanal", selcad);
					places.putExtra("cod", codigo);
					places.putExtra("sconcepto", sconcepto);
					places.putExtra("scodqr", scodqr);
					startActivityForResult(places, 0);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Por favor, introduce un lugar válido", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		cambiaConcep.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent sinqr = new Intent(CapturaQR.this, SeleccionaBusqueda.class);
				sinqr.putExtra("scanal", selcad);
				sinqr.putExtra("cod", codigo);
				if ( !sconcepto.equals("") && !scodqr.equals("") )
				{
					sinqr.putExtra("sconcepto", sconcepto);
					sinqr.putExtra("scodqr", scodqr);
				}
				startActivity(sinqr);
			}
		});
	
		
	}
	
	
	/************* FUNCIÓN PARA EL CAMBIO DE CONFIGURACIÓN DE ORIENTACIÓN DEL MÓVIL ***************/
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		  super.onSaveInstanceState(savedInstanceState);
		  // Save state to the savedInstanceState
		  savedInstanceState.putString("rLugar", tv2.getText().toString());
		  savedInstanceState.putString("rBreve", tv3.getText().toString());
		  savedInstanceState.putString("rCad", selcad);
		  savedInstanceState.putInt("rCod", codigo);
		  savedInstanceState.putString("rConcepto", sconcepto);
		  savedInstanceState.putString("rCodqr", scodqr);
		  if ( !sconcepto.equals("") )
		  {
			  savedInstanceState.putBoolean("botones", true);
		  }
		  else
		  {
			  savedInstanceState.putBoolean("botones", false);
		  }
	}
	/************* FUNCIÓN PARA EL CAMBIO DE CONFIGURACIÓN DE ORIENTACIÓN DEL MÓVIL ***************/
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		  super.onRestoreInstanceState(savedInstanceState);
		  // Restore state from savedInstanceState
		  String myString = savedInstanceState.getString("rLugar");
		  String breve = savedInstanceState.getString("rBreve");
		  selcad = savedInstanceState.getString("rCad");
		  codigo = savedInstanceState.getInt("rCod");
		  sconcepto = savedInstanceState.getString("rConcepto");
		  scodqr = savedInstanceState.getString("rCodqr");
		  
		  Boolean actbotones = savedInstanceState.getBoolean("botones");
		  if (actbotones)
		  {
			  masinf.setVisibility(Button.VISIBLE);
  			  ira.setVisibility(Button.VISIBLE);
		  }
		  
		  tv2.setText(myString);
		  tv3.setText(breve);
		  //tv4.setText("Código: "+scodqr);
	}
	
	
	/** Esta función es la que detecta que se ha escaneado un código QR */
	public void onActivityResult (int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == READ_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String contents = data.getStringExtra("SCAN_RESULT");
				//String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				
				//CodigoQR ejemplo: 001 01 00 0602
				scodqr = contents.replace(" ", "");
				
				if ( !scodqr.equals(null) && scodqr.length()==11 )
				{
					String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
					tmp = scodqr.substring(0, 3);
					tmpedificio = scodqr.substring(3, 5);
					tmpplanta = scodqr.substring(5, 7);
					tmpcodqr = scodqr.substring(7,11);
					
					
					
					ene = Integer.parseInt(tmp);
					if (ene <= 1)
					{
						queTabla = getResources().getString(R.string.tabla_etsiitcontenidos);
					}
					else if ( ene == 2)
					{
						queTabla = getResources().getString(R.string.tabla_fticontenidos);
					}
							
					//tv3.setText(queTabla);
					new ConsultaBD(getApplicationContext()).execute("consulta");
				}
			}
		}
	}
	
	/** ********************************************************************************************** */
	private class ConsultaBD extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    private ProgressDialog Dialog = new ProgressDialog(CapturaQR.this); 
        private Context mContext;
        
        
        public ConsultaBD(Context context) 
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
	        	if ( sconcepto.startsWith(" "))
	        	{
	        		sconcepto = sconcepto.replaceFirst(" " , "");
	        	}
	        	String xconcepto = sconcepto.replace(" ", "%20");
	        	scodqr = scodqr.replace(" ", "");
		        textUrl = new URL(getResources().getString(R.string.urlConsultaContenido)+"CANAL="+selcad+"&QRCODE="+scodqr+"&LATABLA="+queTabla);
	
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
	    	if (vBr.length >= 4)
	    	{
	    		
	    		for (int j=0; j<8; j++)
	    		{
	    			if (j==0)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				scodqr = v2p[1];
	    			}
	    			else if (j == 1)
	    			{
	    				v2p = vBr[j].split(":");
	    				cConcepto = v2p[1];
	    				byte[] bytes= new byte[]{};
	    				try {
							bytes = cConcepto.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
    					try {
    						cConcepto = new String(bytes,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
    					
	    				
	    				sconcepto = cConcepto;
	    			}
	    			else if (j == 2)
	    			{
	    				v2p = vBr[j].split(":");
	    				cCanal = v2p[1];
	    			}
	    			else if (j == 3)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if ( v2p.length > 2 )
	    				{
	    					cResumen = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==4)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if (v2p.length > 2)
	    				{
	    					cDDetallada = v2p[1]+":"+v2p[2];
	    				}
	    			}
	    			else if (j==5)
	    			{
	    				v2p = vBr[j].split(":");
	    				
	    				if (v2p.length > 2)
	    				{
	    					cImagen = v2p[1]+":"+v2p[2];
	    					
	    					
	    					imgManager.fetchDrawableOnThread(cImagen, img);
	    				}
	    			}
	    		}
	    		
	    		
	    		masinf.setVisibility(Button.VISIBLE);
    			ira.setVisibility(Button.VISIBLE);
    			Animation mAnimation = new AlphaAnimation(1, 0);
			    mAnimation.setDuration(400);
			    mAnimation.setInterpolator(new LinearInterpolator());
			    mAnimation.setRepeatCount(Animation.INFINITE);
			    mAnimation.setRepeatMode(Animation.REVERSE); 
			    masinf.startAnimation(mAnimation);
			    ira.startAnimation(mAnimation);
			    
			    mAnimation.setRepeatCount(3);
	    		

	    		if ( !cURL.equals(""))
	    		{
	    			//new LeeConceptoTexto().execute(cURL);
	    		}
	    		else
	    		{
	    			tv2.setText("Estás en: "+"\n"+cConcepto);
	    		}
	    		if ( !cResumen.equals("") )
	    		{
	    			new LeeResumenTexto().execute(cResumen);
	    		}
	    		else
	    		{
	    			tv3.setText("No se encontró información asociada al resumen");
	    		}
	    		
	    	}
	    	else
	    	{

	    		tv3.setText("No se encontró información");
	    	}
	    	Dialog.dismiss();
		    super.onPostExecute(result);   
	    }
	}// Fin clase ConsultaBD
	
	/***********************************************************************************************/
	/*private class LeeConceptoTexto extends AsyncTask<String, Void, Void>
	{
	    String resultado="";
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
	    	//tv2.setText(resultado);
	    	if (resultado.isEmpty())
	    	{
	    		//Toast.makeText(getBaseContext(), resultado, Toast.LENGTH_LONG).show();
	    	}
	    	else
	    	{
	    		tv2.setText("Estás en: "+"\n"+resultado);
	    	}
		    super.onPostExecute(result);   
	    }

	    
	}// Fin clase LeeConceptoTexto
	*/
	/***********************************************************************************************/
		private class LeeResumenTexto extends AsyncTask<String, Void, Void>
		{
		    String resultado="";
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

		    	tv3.setText(resultado);
		    	
			    super.onPostExecute(result);   
		    }

		    
		}// Fin clase LeeConceptoTexto
	
}