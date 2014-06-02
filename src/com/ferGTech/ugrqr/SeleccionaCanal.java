package com.ferGTech.ugrqr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SeleccionaCanal extends Activity 
{

	TextView tv1, tv2, tv3, tv4;
	int codigo = 0, REQUEST_CODE = 1;
	TextView t;
	SharedPreferences misPreferencias=null;
	Button binicio, bdefineperfil;
	int codidioma, codusuario;
	boolean codvideo, codaudio, codaudiotxt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecciona_canal);
		
		InitComponents();
		
		
		misPreferencias = getSharedPreferences("PerfilOps", MODE_PRIVATE);
		if (misPreferencias != null)
		{
			codidioma = misPreferencias.getInt("codidioma", 0);
			codusuario = misPreferencias.getInt("codusuario", 0);
			codaudio = misPreferencias.getBoolean("codaudio", codaudio);
			codvideo = misPreferencias.getBoolean("codvideo", codvideo);
			codaudiotxt = misPreferencias.getBoolean("codaudiotxt", codaudiotxt);
			
			if (codidioma == 0)
			{
				if ( (!codaudio))
				{
					Animation mAnimation = new AlphaAnimation(1, 0);
				    mAnimation.setDuration(400);
				    mAnimation.setInterpolator(new LinearInterpolator());
				    mAnimation.setRepeatCount(Animation.INFINITE);
				    mAnimation.setRepeatMode(Animation.REVERSE); 
				    bdefineperfil.startAnimation(mAnimation);
				    
				    Toast.makeText(this, "Por favor, configura tu perfil", Toast.LENGTH_LONG).show();
				}
			}
		}
		else
		{
			Toast.makeText(this, "Por favor, configura tu perfil", Toast.LENGTH_LONG).show();
		}
		
		
		// *************** Inicia actividad CREA_PERFIL **********************
		bdefineperfil.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(SeleccionaCanal.this, CreaPerfil.class);
				startActivity(i);
				
			}
		});
		// *************** Inicia actividad CAPTURA QR **********************
		binicio.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				v.clearAnimation();
				
				if (codidioma !=0)
				{
					if (codvideo)
					{
						codigo = 3;
						Intent i = new Intent(SeleccionaCanal.this, CapturaQRVideo.class);
						i.putExtra("cod", codigo);
						startActivity(i);
					}
					else
					{
						if (codaudio)
						{
							if (codidioma == 1)
							{
								codigo = 4;
								Intent i = new Intent(SeleccionaCanal.this, CapturaQRAudio.class);
								i.putExtra("cod", codigo);
								startActivity(i);
							}
							else if (codidioma == 2)
							{
								codigo = 5;
								Intent i = new Intent(SeleccionaCanal.this, CapturaQRAudio.class);
								i.putExtra("cod", codigo);
								startActivity(i);
							}
							else
							{
								Toast.makeText(getBaseContext(), "Información no disponible para idioma seleccionado", Toast.LENGTH_LONG).show();
							}

						}
						else if ( codaudiotxt )
						{
							if (codidioma == 1)
							{
								codigo = 6;
								Intent i = new Intent(SeleccionaCanal.this, CapturaQRAudio.class);
								i.putExtra("cod", codigo);
								startActivity(i);
							}
							else if (codidioma == 2)
							{
								codigo = 7;
								Intent i = new Intent(SeleccionaCanal.this, CapturaQRAudio.class);
								i.putExtra("cod", codigo);
								startActivity(i);
							}
							else
							{
								Toast.makeText(getBaseContext(), "Información no disponible para idioma seleccionado", Toast.LENGTH_LONG).show();
							}
						}
						else
						{
							if (codidioma == 1)
							{
								codigo = 1;
								Intent i = new Intent(SeleccionaCanal.this, CapturaQR.class);
								i.putExtra("cod", codigo);
								startActivity(i);
							}
							else if (codidioma == 2)
							{
								codigo = 2;
								Intent i = new Intent(SeleccionaCanal.this, CapturaQR.class);
								i.putExtra("cod", codigo);
								startActivity(i);
							}
						}
					}
					
				}
				else
				{
					Toast.makeText(getBaseContext(), "Configura tu perfil", Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}
	
	
	public void InitComponents ()
	{
		binicio = (Button)findViewById(R.id.button6);
		bdefineperfil = (Button)findViewById(R.id.button7);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		tv2 = (TextView)findViewById(R.id.textView2);
		tv3 = (TextView)findViewById(R.id.textView3);
		tv4 = (TextView)findViewById(R.id.textView4);
		
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selecciona_canal, menu);
		
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent help = new Intent(SeleccionaCanal.this, Ayuda.class);
		startActivity(help);
	    return true;
	}

}
