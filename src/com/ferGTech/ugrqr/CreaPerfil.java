package com.ferGTech.ugrqr;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class CreaPerfil extends Activity
{
	
	RadioGroup rg1, rg2;
	RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9;
	Button save;
	int codigoidioma = 0;
	boolean actAudioguia, actVideoguia, actAudioTexto;
	SharedPreferences prefs;
	Editor mEditor;
	
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creaperfil);
		
		
		// ****** ELEMENTOS GRÁFICOS *********
		InitComponents();
		
		
		prefs = getSharedPreferences("PerfilOps", MODE_PRIVATE);
		if (prefs != null)
		{
			codigoidioma = prefs.getInt("codidioma", 0);
			if (codigoidioma != 0)
			{
				if (codigoidioma == 1)
				{
					rb1.setChecked(true);
				}
				else if (codigoidioma == 2)
				{
					rb2.setChecked(true);
				}
				else if (codigoidioma == 3)
				{
					rb3.setChecked(true);
				}
				else if (codigoidioma == 4)
				{
					rb4.setChecked(true);
				}
				else if (codigoidioma == 5)
				{
					rb5.setChecked(true);
				}
			}
			
			actAudioguia = prefs.getBoolean("codaudio", false);
			if (actAudioguia)
			{
				rb6.setChecked(true);
			}
			else
			{
				rb6.setChecked(false);
			}
			actVideoguia = prefs.getBoolean("codvideo", false);
			if (actVideoguia)
			{
				rb7.setChecked(true);
			}
			else
			{
				rb7.setChecked(false);
			}
			actAudioTexto = prefs.getBoolean("codaudiotxt", false);
			if (actAudioTexto)
			{
				rb9.setChecked(true);
			}
			else
			{
				rb9.setChecked(false);
			}
			if ( !actAudioguia && !actVideoguia && !actAudioTexto)
			{
				rb8.setChecked(true);
			}
		}
		
		
		
		
		
		mEditor = prefs.edit();
		rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{	
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				if (rb1.isChecked())
				{
					codigoidioma = 1; 
				}
				else if (rb2.isChecked())
				{
					codigoidioma = 2;
				}
				else if (rb3.isChecked())
				{
					codigoidioma =  3;
				}
				else if (rb4.isChecked())
				{
					codigoidioma = 4;
				}
				else if (rb5.isChecked())
				{
					codigoidioma = 5;
				}
				mEditor.putInt("codidioma", codigoidioma);
			}
			
		});
		
		rg2.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{	
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				if (rb6.isChecked())
				{
					actAudioguia = true;
					actVideoguia = false;
					actAudioTexto = false;
				}
				else if (rb9.isChecked())
				{
					actAudioTexto = true;
					actAudioguia = false;
					actVideoguia = false;
				}
				else if (rb7.isChecked())
				{
					actVideoguia = true;
					actAudioTexto = false;
					actAudioguia = false;
				}
				else if (rb8.isChecked())
				{
					actAudioguia = false;
					actVideoguia = false;
					actAudioTexto = false;
				}
				mEditor.putBoolean("codvideo", actVideoguia);
				mEditor.putBoolean("codaudio", actAudioguia);
				mEditor.putBoolean("codaudiotxt", actAudioTexto);
			}
		});
		save.setOnClickListener(new OnClickListener() 
		{		
			@Override
			public void onClick(View v) 
			{
				if (codigoidioma != 0 )
				{
					mEditor.commit();
					
					String languageToLoad="";
					if (codigoidioma == 1)
					{
						languageToLoad = "";
					}
					else if (codigoidioma == 2)
					{
						languageToLoad = "en";
					}
					  
		            Locale locale = new Locale(languageToLoad); 
		            Locale.setDefault(locale);
		            Configuration config = getApplicationContext().getResources().getConfiguration();
		            config.locale = locale;
		            getApplicationContext().getResources().updateConfiguration(config, 
		            getApplicationContext().getResources().getDisplayMetrics());
		            
					
					Intent menu = new Intent(CreaPerfil.this, SeleccionaCanal.class);
					menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(menu);
				}
				else
				{
					if (codigoidioma == 0 )
					{
						Toast.makeText(getBaseContext(), "Por favor, seleccione un idioma",  Toast.LENGTH_LONG).show();
					}
				}
				
			}
		});
		
	}// Fin onCreate
	
	
	public void InitComponents()
	{
		save = (Button)findViewById(R.id.button1);
		
		rg1 = (RadioGroup)findViewById(R.id.radioGroup1);
		rg2 = (RadioGroup)findViewById(R.id.radioGroup2);
		rb1 = (RadioButton)findViewById(R.id.radioButton1);
		rb2 = (RadioButton)findViewById(R.id.radioButton2);
		rb3 = (RadioButton)findViewById(R.id.radioButton3);
		rb4 = (RadioButton)findViewById(R.id.radioButton4);
		rb5 = (RadioButton)findViewById(R.id.radioButton5);
		rb6 = (RadioButton)findViewById(R.id.radioButton6);
		rb7 = (RadioButton)findViewById(R.id.radioButton7);
		rb8 = (RadioButton)findViewById(R.id.radioButton8);
		rb9 = (RadioButton)findViewById(R.id.radioButton9);
	}

}// Fin class CreaPerfil