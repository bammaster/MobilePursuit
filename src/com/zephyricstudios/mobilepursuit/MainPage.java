package com.zephyricstudios.mobilepursuit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainPage extends Activity implements OnClickListener {
	
	RelativeLayout buttonMainSnitch;
	RelativeLayout buttonMainSeeker;
	RelativeLayout buttonMainConfused;
	TextView textMainIAm;
	TextView textMainSeeker;
	TextView textMainSnitch;
	TextView textMainConfused;
	Typeface light;
	String name;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        buttonMainSnitch = (RelativeLayout)findViewById(R.id.button_main_snitch);
        buttonMainSnitch.setOnClickListener(this);
        buttonMainSeeker = (RelativeLayout)findViewById(R.id.button_main_seeker);
        buttonMainSeeker.setOnClickListener(this);
        buttonMainConfused = (RelativeLayout)findViewById(R.id.button_main_confused);
        buttonMainConfused.setOnClickListener(this);
        
        textMainIAm = (TextView)findViewById(R.id.text_main_i_am);
        textMainSeeker = (TextView)findViewById(R.id.text_main_seeker);
        textMainSnitch = (TextView)findViewById(R.id.text_main_snitch);
        textMainConfused = (TextView)findViewById(R.id.text_main_confused);
        
        light = Typeface.createFromAsset(getAssets(), "roboto_light.ttf");
    	textMainIAm.setTypeface(light);
    	textMainSeeker.setTypeface(light);
    	textMainSnitch.setTypeface(light);
    	textMainConfused.setTypeface(light);
    	
    	CmiycJavaRes.activityState = CmiycJavaRes.MAIN;
    	
    	SharedPreferences sp = getSharedPreferences(CmiycJavaRes.STORED_PREFERENCES_KEY, MODE_PRIVATE);
    	Editor spEditor = sp.edit();  //use these two lines anywhere I want to use/edit shared prefs
    	
    	if(sp.getString("username", CmiycJavaRes.SHARED_PREFS_DEFAULT).equals(CmiycJavaRes.SHARED_PREFS_DEFAULT)){
    		AlertDialog.Builder alert = new AlertDialog.Builder(this);

    		alert.setTitle("Enter User Name");
    		alert.setMessage("Please enter your name. You can change this at any time by pressing the menu button.");

    		// Set an EditText view to get user input 
    		final EditText input = new EditText(this);
    		alert.setView(input);

    		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			String value = input.getText().toString();
    			SharedPreferences sp = getSharedPreferences(CmiycJavaRes.STORED_PREFERENCES_KEY, MODE_PRIVATE);
    	    	Editor spEditor = sp.edit();
    			spEditor.putString(CmiycJavaRes.USERNAME_KEY, value);
    		  	spEditor.commit();
    		  }
    		});

    		alert.setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int whichButton) {
    		    dialog.cancel();
    		  }
    		});

    		alert.show();
    	}
    	
    	sp.getString("username", CmiycJavaRes.SHARED_PREFS_DEFAULT);
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_page, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_change_name:
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    		alert.setTitle("Enter User Name");
    		alert.setMessage("Please enter your name.");

    		// Set an EditText view to get user input 
    		final EditText input = new EditText(this);
    		alert.setView(input);

    		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			String value = input.getText().toString();
    			SharedPreferences sp = getSharedPreferences(CmiycJavaRes.STORED_PREFERENCES_KEY, MODE_PRIVATE);
    	    	Editor spEditor = sp.edit();
    			spEditor.putString(CmiycJavaRes.USERNAME_KEY, value);
    		  	spEditor.commit();
    		  }
    		});

    		alert.setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int whichButton) {
    		    dialog.cancel();
    		  }
    		});

    		alert.show();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    @Override
    public void onResume(){
    	super.onResume();
    	CmiycJavaRes.activityState = CmiycJavaRes.MAIN;
    	
    }
    
	public void onClick(View buttonChosen) {
		Intent i;
		
		// I couldn't get this to work...
		/*switch (buttonChosen.getId()) {
		case R.id.button_main_seeker:
			i = new Intent(this, SeekerMainPage.class);
		case R.id.button_main_snitch:
			i = new Intent(this, SnitchMainPage.class);
		case R.id.button_main_confused:
			i = new Intent(this, Confused.class);
		default:
			i = null;
		} */
		
		if(buttonChosen==buttonMainSnitch){
			//buttonChosen.setBackgroundColor(getResources().getColor(R.color.accent));
			i = new Intent(this, SnitchMainPage.class);
			//CmiycJavaRes.activityState = CmiycJavaRes.SNITCHMAIN;
		}else if(buttonChosen==buttonMainSeeker){
			/*saveName();
			if(saveName() == true){
				i = new Intent(this, SeekerMainPage.class);
			}*/
			i = new Intent(this, SeekerMainPage.class);
			//CmiycJavaRes.activityState = CmiycJavaRes.SEEKERMAIN;
		}else if(buttonChosen==buttonMainConfused){
			i = new Intent(this, Confused.class);
			//CmiycJavaRes.activityState = CmiycJavaRes.CONFUSED;
		}else{
			i = null;
		}
		
		this.startActivity(i);
		
	}
}