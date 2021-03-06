package com.ferGTech.ugrqr;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

public class ImageManager 
{
	private final Map<String, Drawable> drawableMap;

	final Context context;

	public ImageManager(Context c) 
	{  
	     drawableMap = new HashMap<String, Drawable>();
	    context = c;
	}
	
	public void fetchDrawableOnThread(final String urlString, final ImageView imageView) 
	{
	     
	    
	    final Handler handler = new Handler() 
	    {
		 @Override
		 public void handleMessage(Message message) 
		 {
		       imageView.setImageDrawable((Drawable) message.obj);
		 }
		    };
		 
		    Thread thread = new Thread() {
		  @Override
		  public void run() {
		      try{
		          Drawable drawable = fetchDrawable(urlString);
		          Message message = handler.obtainMessage(1, drawable);
		          handler.sendMessage(message);
		      } catch(Exception e){
		          imageView.setVisibility(View.GONE);
		      }
		  }
		    };
		    thread.start(); 
		}
	
	 public Drawable fetchDrawable(String urlString) {
	        if (drawableMap.containsKey(urlString)) {
	            return drawableMap.get(urlString);
	        }

	        try {
	            InputStream is = fetch(urlString);
	            Drawable drawable = Drawable.createFromStream(is, "src"+urlString);
	            if (drawable != null) {
	                drawableMap.put(urlString, drawable);
	                return drawable;  
	            } else {
	             return null;
	            }         
	        } catch (MalformedURLException e) {
	           return null;
	        } catch (IOException e) {
	           return null;
	        } catch (Exception e){
	           return null;
	        }
	    }
	 
	 private InputStream fetch(String strURL) throws IOException{
		 InputStream inputStream = null;
		 URL url = new URL(strURL);
		 URLConnection conn = url.openConnection();
		 
		 try{
		  HttpURLConnection httpConn = (HttpURLConnection)conn;
		  httpConn.setRequestMethod("GET");
		  httpConn.connect();
		 
		  if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		   inputStream = httpConn.getInputStream();
		  }
		 }  catch (Exception ex){}
		 return inputStream;
		}

	
	

};
