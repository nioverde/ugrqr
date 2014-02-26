package com.ferGTech.ugrqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class BusquedaIntroduce extends Activity
{
	EditText edt1;
	Button btn1;
	ListView lv;
	SimpleAdapter mAdapter;
	String canal="", codqr="", cDestino="";
	String codqr2="", destino2="";
	String centro="", qrcentro="";
	String latabla2="";
	String cad="";
	
	int codigo, ene;
	Bundle extras;
	ArrayList<Map<String, String>> mLista;
	
	SharedPreferences misPreferencias=null;
	String ultimo="";
	Editor miEditor;
	
	MiAdapter adapter;
	ArrayList<ConceptoCheckbox> conceptoList;
	
	
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busqueda_introduce);
		
		edt1 = (EditText)findViewById(R.id.editText1);
		btn1 = (Button)findViewById(R.id.button1);
		lv = (ListView)findViewById(R.id.listView1);
		

		misPreferencias = getSharedPreferences("Centros", MODE_PRIVATE);
		if ( misPreferencias != null)
		{
			ultimo = misPreferencias.getString("ultimoCentro", "");
			//Toast.makeText(this, ultimo, Toast.LENGTH_LONG).show();
		}
		 
		extras = getIntent().getExtras();
		codigo = extras.getInt("cod");
		canal = extras.getString("scanal");
		ene = 0;
		
		new RellenaLista(getApplicationContext()).execute("busqueda");
		
		conceptoList = new ArrayList<ConceptoCheckbox>();
		
		mLista = new ArrayList<Map<String, String>>();
		
		
		btn1.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				boolean existealguno = false;
				ArrayList<ConceptoCheckbox> countryList = adapter.countryList;
	    	    for(int i=0;i<countryList.size();i++)
	    	    {
		    	     ConceptoCheckbox country = countryList.get(i);
		    	     if(country.isSelected())
		    	     {
		    	    	 centro = country.getConcepto();
		    	    	 qrcentro = country.getCodqr();
		    	    	 existealguno = true;
		    	    	 
		    	     }
	    	    }
	    	    
	    	    if (existealguno)
	    	    {
	    	    	if ( !qrcentro.equals(null) && qrcentro.length()==11 )
	    			{
	    				String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
	    				tmp = qrcentro.substring(0, 3);
	    				tmpedificio = qrcentro.substring(3, 5);
	    				tmpplanta = qrcentro.substring(5, 7);
	    				tmpcodqr = qrcentro.substring(7,11);
	    				
	    				
	    				
	    				ene = Integer.parseInt(tmp);
	    				
	    						
	    				//tv3.setText(queTabla);
	    				//Toast.makeText(getBaseContext(), scodqr, Toast.LENGTH_LONG).show();
	    			}
					
					
					
					cad = edt1.getText().toString();
					
					// ***** SELECCION DE LA TABLA PARA LUGARES DE CADA EDIFICIO ********
					if (ene <= 2)
					{
						if ( !cad.equals("") )
						{
							if (ene <= 1)
		    				{
		    					latabla2 = "ETSIITCONTENIDOS";
		    				}
		    				else if ( ene == 2)
		    				{
		    					latabla2 = "FTICONTENIDOS";
		    				}
							
							new BuscaDestino(getApplicationContext()).execute("busqueda");
								
						}
						else
						{
							Toast.makeText(getBaseContext(), "Introduce un lugar en el campo de texto", Toast.LENGTH_LONG).show();
						}
						
						
						
						/*if ( !centro.equals("") && !qrcentro.equals("") )
						{
							Intent busca2 = new Intent(BusquedaIntroduce.this, CapturaQR.class);
							busca2.putExtra("cod", codigo);
							busca2.putExtra("scanal", canal);
							busca2.putExtra("scentro", centro);
							busca2.putExtra("sqrcentro", qrcentro);
							startActivity(busca2);
						}*/
					}
					else
					{
						Toast.makeText(getBaseContext(), "No existen lugares para este centro", Toast.LENGTH_LONG).show();
					}
	    	    }
	    	    else
	    	    {
	    	    	Toast.makeText(getBaseContext(), "Debes seleccionar al menos un centro", Toast.LENGTH_LONG).show();
	    	    }
	    	    
			}
		});
		
		
		
	}//Fin onCreate
	
	
	/* **************************************** */
    /* Clase para rellenar la lista con Centros */ 
	/* **************************************** */
    private class RellenaLista extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    
	    private ProgressDialog Dialog = new ProgressDialog(BusquedaIntroduce.this); 
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
	        	String latabla="CENTROS";
		         textUrl = new URL(R.string.urlBusquedaIntroduce+"LATABLA="+latabla);
	
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
	    	ConceptoCheckbox cc;
	    	for (int j=0; j<vBr.length; j++)
    		{
    			v2p = vBr[j].split(":");
    			if (v2p.length > 1)
    			{
    				if (cDestino.equals(ultimo))
    				{
    					if (j%2 == 0)
        				{
        					cDestino = v2p[1];
        				}
        				else
        				{
        					codqr = v2p[1];
        					cc = new ConceptoCheckbox(cDestino, codqr, true);
        					conceptoList.add(cc);
    						 
        				}
    				}
    				else
    				{
    					if (j%2 == 0)
        				{
        					cDestino = v2p[1];
        				}
        				else
        				{
        					codqr = v2p[1];
        					cc = new ConceptoCheckbox(cDestino, codqr, false);
        					conceptoList.add(cc);
        				}
    				}
    			}
    			/*else
    			{
    				tv1.setText("error");
    			}*/
    			adapter = new MiAdapter(getBaseContext(), R.layout.item_list_selectable, conceptoList);
    			lv.setAdapter(adapter);
    			
    		}
	    	//tv1.setText("Selecciona un centro");
	    	Dialog.dismiss();
	    	//Toast.makeText(getBaseContext(), textResult, Toast.LENGTH_LONG).show();
	    	
		    super.onPostExecute(result);   
	    }
	}// Fin de la clase RellenaLista ##########################################################################
    
    
    
    /* **************************************** */
    /* Clase para buscar un destino introduciendolo */ 
	/* **************************************** */
    private class BuscaDestino extends AsyncTask<String, Void, Void>
	{
	    String textResult;
	    String auxcad;
	    String [] vBody, vBr, v2p;
	    
	    
	    private ProgressDialog Dialog = new ProgressDialog(BusquedaIntroduce.this); 
        private Context mContext;
        
        
        public BuscaDestino(Context context) 
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
	        	String slimpio;
	        	slimpio = cad.replace(" ", "%20");
		         textUrl = new URL(R.string.urlBusquedaIntroduceOrig+"LATABLA="+latabla2+"&CONCEPTO="+slimpio);
	
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
	    	mLista.clear();
	    	Map<String, String> datum = new HashMap<String, String>(2);
	    	
	    	for (int j=0; j<vBr.length; j++)
    		{
    			v2p = vBr[j].split(":");
    			if (v2p.length > 1)
    			{
    				if (j%2 == 0)
    				{
    					datum = new HashMap<String, String>(2);
    					destino2 = v2p[1];
    					datum.put("xcentro", destino2);
    					
    				}
    				else
    				{
    					codqr2 = v2p[1];
    					//cc = new ConceptoCheckbox(destino2, codqr2, false);
    					//conceptoList.add(cc);
    					datum.put("xqrcentro", codqr2);
    					mLista.add(datum);
    				}
    			}
    			/*else
    			{
    				tv1.setText("error");
    			}*/
    			
    		}
	    	//tv1.setText("Selecciona un centro");
	    	Dialog.dismiss();

	    	// Este caso equivale a que tan sólo haya un resultado en la búsqueda
	    	if (mLista.size() == 1)
	    	{
	    		if ( !destino2.equals("") && !codqr2.equals("") )
				{
	    			
	    			if (codigo >= 1 && codigo <= 2)
					{
						Intent iTexto = new Intent( BusquedaIntroduce.this, CapturaQR.class );
						iTexto.putExtra("sconcepto", destino2);
						iTexto.putExtra("scodqr", codqr2);
						iTexto.putExtra("cod", codigo);
						iTexto.putExtra("scanal", canal);
						iTexto.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iTexto);
					}
					else if (codigo == 3)
					{
						Intent iVideo = new Intent(BusquedaIntroduce.this, CapturaQRVideo.class);
						iVideo.putExtra("cod", codigo);
						iVideo.putExtra("sconcepto", destino2);
						iVideo.putExtra("scodqr", codqr2);
						iVideo.putExtra("scanal", canal);
						iVideo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iVideo);
					}
					else if (codigo >= 4 && codigo <=7 )
					{
						Intent iAudio = new Intent( BusquedaIntroduce.this, CapturaQRAudio.class );
						iAudio.putExtra("sconcepto", destino2);
						iAudio.putExtra("scodqr", codqr2);
						iAudio.putExtra("scanal", canal);
						iAudio.putExtra("cod", codigo);
						iAudio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iAudio);
					}
					/*Intent busca2 = new Intent(BusquedaIntroduce.this, CapturaQR.class);
					busca2.putExtra("cod", codigo);
					busca2.putExtra("scanal", canal);
					busca2.putExtra("sconcepto", destino2);
					busca2.putExtra("scodqr", codqr2);
					busca2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(busca2);*/
				}
	    	}
	    	// En este caso la búsqueda se ha resuelto con múltiples destinos 
	    	else
	    	{
	    		
	    		String[] items = new String[mLista.size()];
	    		for (int i=0; i<mLista.size(); i++)
	    		{
	    			Map<String, String> mimap = new HashMap<String, String>(2);
	    			mimap = mLista.get(i);
	    			items[i] = mimap.get("xcentro");
	    		}
	            AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaIntroduce.this);
	            builder.setTitle("Múltiples destinos");
	            builder.setItems(items, new DialogInterface.OnClickListener() 
	            {
	                public void onClick(DialogInterface dialog, int item) 
	                {
	                	Map<String, String> mimap = new HashMap<String, String>(2);
		    			mimap = mLista.get(item);
		    			destino2 = mimap.get("xcentro");
		    			codqr2 = mimap.get("xqrcentro");
		    			
		    			if (codigo >= 1 && codigo <= 2)
						{
							Intent iTexto = new Intent( BusquedaIntroduce.this, CapturaQR.class );
							iTexto.putExtra("sconcepto", destino2);
							iTexto.putExtra("scodqr", codqr2);
							iTexto.putExtra("cod", codigo);
							iTexto.putExtra("scanal", canal);
							iTexto.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(iTexto);
						}
						else if (codigo == 3)
						{
							Intent iVideo = new Intent(BusquedaIntroduce.this, CapturaQRVideo.class);
							iVideo.putExtra("cod", codigo);
							iVideo.putExtra("sconcepto", destino2);
							iVideo.putExtra("scodqr", codqr2);
							iVideo.putExtra("scanal", canal);
							iVideo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(iVideo);
						}
						else if (codigo >= 4 && codigo <=7 )
						{
							Intent iAudio = new Intent( BusquedaIntroduce.this, CapturaQRAudio.class );
							iAudio.putExtra("sconcepto", destino2);
							iAudio.putExtra("scodqr", codqr2);
							iAudio.putExtra("scanal", canal);
							iAudio.putExtra("cod", codigo);
							iAudio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(iAudio);
						}
		    			
		    			/*Intent busca2 = new Intent(BusquedaIntroduce.this, CapturaQR.class);
						busca2.putExtra("cod", codigo);
						busca2.putExtra("scanal", canal);
						busca2.putExtra("sconcepto", destino2);
						busca2.putExtra("scodqr", codqr2);
						busca2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(busca2);*/
	                }
	            });
	            AlertDialog alert = builder.create();
	            alert.show();
	    		
	    		
	    		
	    	}
	    	
		    super.onPostExecute(result);   
	    }
	}// Fin de la clase RellenaLista **************************************************************************
	
	
    private class MiAdapter extends ArrayAdapter<ConceptoCheckbox> 
    {
    	 
    	  private ArrayList<ConceptoCheckbox> countryList;
    	 
    	  public MiAdapter(Context context, int textViewResourceId,  ArrayList<ConceptoCheckbox> countryList) 
    	  {
    	   super(context, textViewResourceId, countryList);
    	   this.countryList = new ArrayList<ConceptoCheckbox>();
    	   this.countryList.addAll(countryList);
    	  }
    	 
    	  private class ViewHolder 
    	  {
    	   TextView code;
    	   CheckBox name;
    	  }
    	 
    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	 
    	   if (convertView == null) 
    	   {
    	   LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.item_list_selectable, null);
    	 
    	   holder = new ViewHolder();
    	   holder.code = (TextView) convertView.findViewById(R.id.textView1);
    	   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
    	   convertView.setTag(holder);
    	   holder.code.setTextSize(getResources().getDimension(R.dimen.textoescaneoqr));
    	 
    	   // Evento para click sobre el boton CheckBox
    	    holder.name.setOnClickListener( new View.OnClickListener() 
    	    {  
	    	     public void onClick(View v) 
	    	     {  
		    	      CheckBox cb = (CheckBox) v;  
		    	      ConceptoCheckbox country = (ConceptoCheckbox) cb.getTag();
		    	      
		    	      ConceptoCheckbox comprueba;
		    	      for (int i=0; i<countryList.size(); i++)
		    	      {
		    	    	  String aux = countryList.get(i).getConcepto();
		    	    	  comprueba = new ConceptoCheckbox(aux, countryList.get(i).codqr, countryList.get(i).isSelected());
		    	    	  
		    	    	  if ( !aux.equals(country.getConcepto()) && countryList.get(i).isSelected() )
		    	    	  {
		    	    		  countryList.get(i).setSelected(false);
		    	    		  countryList.get(i).selected = false;
		    	    	  }
		    	      }
		    	      //countryList.notify();
		    	      
		    	      country.setSelected(cb.isChecked());
		    	      miEditor = misPreferencias.edit();
	    	    	  miEditor.putString("ultimoCentro", country.getConcepto());
	    	    	  miEditor.commit();
	    	     }  
    	    });  
    	   } 
    	   else 
    	   {
    		   holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   ConceptoCheckbox country = countryList.get(position);
    	   //holder.code.setText(" (" +  country.getConcepto() + ")");
    	   holder.name.setText(country.getConcepto());
    	   holder.name.setChecked(country.isSelected());
    	   /*if (country.isSelected())
    	   {
    		   holder.name.setSelected(true);
    	   }*/
    	   holder.name.setTag(country);
    	 
    	   return convertView;
    	 
    	  }
    	 
    	 }
    
    
    public class GPListPreference extends ListPreference 
    {
        

        public GPListPreference(Context context) 
        {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
        protected void onPrepareDialogBuilder(AlertDialog.Builder builder) 
        {
            builder.setNegativeButton(null, null);
            builder.setPositiveButton(null, null);
        }

        private int getValueIndex() 
        {
            return findIndexOfValue(getValue());
        }

        @Override
        protected View onCreateDialogView() 
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ListView lv = (ListView) inflater.inflate(R.layout.listrowlay, null);

            TextView header = (TextView) inflater.inflate(R.layout.listrowlay_unico, null);
            header.setText(getDialogMessage()); // you should set the header text as android:dialogMessage in the preference XML
            lv.addHeaderView(header);

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.listrowlay_singlechoice, getEntries());
            lv.setAdapter(adapter);

            lv.setClickable(true);
            lv.setEnabled(true);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setItemChecked(getValueIndex() + 1, true);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setValueIndex(position - 1);
                    getDialog().dismiss();
                }
            });

            return lv;
        }
    }
    

	

}//Fin class BusquedaIntroduce

