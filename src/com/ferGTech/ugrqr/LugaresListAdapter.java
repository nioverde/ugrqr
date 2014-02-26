package com.ferGTech.ugrqr;
import java.util.List;

import com.ferGTech.ugrqr.Lugares.Places;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LugaresListAdapter extends ArrayAdapter
{

    private int resource;
    private LayoutInflater inflater;
    private Context context;
    String tmp="", tmpplanta="", tmpedificio="", tmpcodqr="";
    
	public LugaresListAdapter ( Context ctx, int resourceId, List l) {

        super( ctx, resourceId, l );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {

        /* create a new view of my layout and inflate it in the row */
        convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        /* Extract the city's object to show */
        Places lugar = (Places) getItem( position );

        /* Take the TextView from layout and set the places's name */
        TextView txtName = (TextView) convertView.findViewById(R.id.place);
        txtName.setText(lugar.getName());

        

        /* Take the ImageView from layout and set the place's qr*/
        TextView txtQr = (TextView) convertView.findViewById(R.id.qr);
        
        /* */
        String scodqr=lugar.getqr().toString();
        
        scodqr = scodqr.replace(" ", "");
        if ( scodqr.length() == 11 )
        {
        	
    		tmp = scodqr.substring(0, 3);
    		tmpedificio = scodqr.substring(3, 5);
    		tmpplanta = scodqr.substring(5, 7);
    		tmpcodqr = scodqr.substring(7,11);
    		
    		if (tmpplanta.equals("00"))
    		{
    			txtQr.setText("\t"+"Planta 0");
    		}
    		else if (tmpplanta.equals("01"))
    		{
    			txtQr.setText("\t"+"Planta 1");
    		}
    		else if (tmpplanta.equals("02"))
    		{
    			txtQr.setText("\t"+"Planta 2");
    		}
    		else if (tmpplanta.equals("03"))
    		{
    			txtQr.setText("\t"+"Planta 3");
    		}
    		else if (tmpplanta.equals("04"))
    		{
    			txtQr.setText("\t"+"Planta 4");
    		}
    		else if (tmpplanta.equals("91"))
    		{
    			txtQr.setText("\t"+"Planta -1");
    		}
    		else if (tmpedificio.equals("92"))
    		{
    			txtQr.setText("\t"+"Planta -2");
    		}
        }
        else
        {
        	txtQr.setText(lugar.getqr());
        }
        /* Take the TextView from layout and set the city's wiki link */
        TextView txtRuta = (TextView) convertView.findViewById(R.id.rute);
        //txtRuta.setText(lugar.getRute());
        if (scodqr.length() == 11)
        {
        	if ( tmpedificio.equals("00") )
            {
            	txtRuta.setText("");
            }
            else if ( tmpedificio.equals("01") )
            {
            	txtRuta.setText("\t"+"Edificio Principal");
            }
            else if ( tmpedificio.equals("02") )
            {
            	txtRuta.setText("\t"+"Segundo edificio");
            }
            else if ( tmpedificio.equals("03") )
            {
            	txtRuta.setText("\t"+"Tercer edificio");
            }
            else if ( tmpedificio.equals("04") )
            {
            	txtRuta.setText("\t"+"Cuarto edificio");
            }
        }
        else
        {
        	txtRuta.setText(lugar.getRute());
        }
        
        //txtQr.setText(lugar.getqr());
        
        return convertView;
    }
}