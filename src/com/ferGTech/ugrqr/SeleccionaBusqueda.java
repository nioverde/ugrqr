package com.ferGTech.ugrqr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SeleccionaBusqueda extends Activity
{
	Button sinqr, introduce, existeqr; 
	TextView tv4;
	String canal, concepto="", codqr="";
	int cod;
	Bundle extras;
	
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selecciona_busqueda);
		
		sinqr = (Button)findViewById(R.id.button1);
		introduce = (Button)findViewById(R.id.button2);
		existeqr = (Button)findViewById(R.id.button3);
		tv4 = (TextView)findViewById(R.id.textView4);
		
		existeqr.setVisibility(Button.GONE);
		tv4.setVisibility(TextView.GONE);
		
		extras = getIntent().getExtras();
		cod = extras.getInt("cod");
		canal = extras.getString("scanal");
		
		if ( extras.containsKey("sconcepto") && extras.containsKey("scodqr") )
		{
			concepto = extras.getString("sconcepto");
			codqr = extras.getString("scodqr");
			
			existeqr.setVisibility(Button.VISIBLE);
			tv4.setVisibility(TextView.VISIBLE);
		}
		
		
		sinqr.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent sin = new Intent(SeleccionaBusqueda.this, BuscadorSinQR.class);
				sin.putExtra("cod", cod);
				sin.putExtra("scanal", canal);
				startActivity(sin);
			}
		});
		introduce.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				Intent intro = new Intent(SeleccionaBusqueda.this, BusquedaIntroduce.class);
				intro.putExtra("cod", cod);
				intro.putExtra("scanal", canal);
				startActivity(intro);
			}
		});
		existeqr.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				Intent exis = new Intent(SeleccionaBusqueda.this, BsinQR2.class);
				exis.putExtra("cod", cod);
				exis.putExtra("scanal", canal);
				exis.putExtra("scentro", concepto);
				exis.putExtra("sqrcentro", codqr);
				startActivity(exis);
			}
		});
		
		
	}//Fin onCreate()
	
	

	
	
	
	
	
}
