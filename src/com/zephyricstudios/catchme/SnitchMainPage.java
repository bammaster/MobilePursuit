package com.zephyricstudios.catchme;

import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.ListView;

import com.zephyricstudios.catchme.SeekerAdapter;

public class SnitchMainPage extends Activity implements OnClickListener, Endable {
	
	RelativeLayout start;
	RelativeLayout settings;
	
	// Typeface
	Typeface light;
	
	SmsManager sm = SmsManager.getDefault();
	
	//Seeker Object Stuff
	ListView seekerList;
	//ArrayList<Seeker> seekerArray;
	//SeekerAdapter adapter;
	
	TextView title, startText, textSnitchSettings, textSending;
	
	boolean intervalSettingsVisible, navigating = false;
	
	RelativeLayout sendingLayout;
	LinearLayout intervalSettings;
	Button btnInterval15s, btnInterval30s, btnInterval45s, btnInterval60s;
	
	BroadcastReceiver localTextReceiver;
	IntentFilter filter;
	
	//Texting Stuff
	//String textContent;
	//Thread sendTexts;
	
	Group group;
	Game game;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snitch_main_page);
        
        //Set up start and settings button
        start = (RelativeLayout)findViewById(R.id.snitch_start_button);
        start.setOnClickListener(this);
        settings = (RelativeLayout)findViewById(R.id.snitch_settings_button);
        settings.setOnClickListener(this);
        
        // Typeface
        light = Typeface.createFromAsset(getAssets(), "roboto_light.ttf");
        title = (TextView)findViewById(R.id.text_snitch_title);
        title.setTypeface(light);
        startText = (TextView)findViewById(R.id.text_snitch_start);
        startText.setTypeface(light);
        textSending = (TextView)findViewById(R.id.text_sending);
        textSending.setTypeface(light);
        
        /*if(this.getIntent().getExtras().getParcelable(Ref.GROUP_KEY) != null) {
        	group = this.getIntent().getExtras().getParcelable(Ref.GROUP_KEY);
        } else {
        	group = new Group();
        }*/
        group = Ref.group;
        
        group.setActAdapter(new ActivityAdapter() {
        	
        	@Override
        	public void receiveGameStart(int interval) {
        		game.setInterval(interval);
        		Ref.group = group;
        		Ref.game = game;
        		SnitchMainPage.this.end();
        	}
        	
        	@Override
        	public void updateUI() {
        		if(group.imRunner()) {
        			SnitchMainPage.this.makeMeRunner();
        		} else {
        			SnitchMainPage.this.makeMeSeeker();
        		}
        	}
        });
        group.setRunning(this);
        group.setInGame(true);
        group.setContext(this);
        
        group.setSeekerAdapter(new SeekerAdapter(this, R.layout.list_item, group.getPeople(), this, light));
        seekerList = (ListView)findViewById(R.id.seeker_list);
        seekerList.setAdapter(group.getSeekerAdapter());
        
        if(Ref.game != null) {
        	game = Ref.game;
        } else {
        	game = new Game();
        	Ref.game = game;
        }
        
        intervalSettings = (LinearLayout)findViewById(R.id.interval_settings_layout);
        intervalSettings.setVisibility(View.GONE);
        intervalSettingsVisible = false;
        
        // TODO This needs to go.
        btnInterval15s = (Button)findViewById(R.id.button_15s);
        btnInterval30s = (Button)findViewById(R.id.button_30s);
        btnInterval45s = (Button)findViewById(R.id.button_45s);
        btnInterval60s = (Button)findViewById(R.id.button_60s);
        btnInterval15s.setOnClickListener(this);
        btnInterval30s.setOnClickListener(this);
        btnInterval45s.setOnClickListener(this);
        btnInterval60s.setOnClickListener(this);
        btnInterval15s.setTypeface(light);
        btnInterval30s.setTypeface(light);
        btnInterval45s.setTypeface(light);
        btnInterval60s.setTypeface(light);
        
        textSnitchSettings = (TextView)findViewById(R.id.text_snitch_settings);
        
        sendingLayout = (RelativeLayout)findViewById(R.id.loading_layout);
        
        localTextReceiver = group.getBroadcastReceiver();
        filter = new IntentFilter();
        filter.addAction(Ref.ACTION);
        this.registerReceiver(this.localTextReceiver, filter);
    }
    
    @Override
    protected void onDestroy() {
    	this.unregisterReceiver(this.localTextReceiver);
    	group.disableSeekerAdapter();
    	if(!navigating) {  // If you're not leaving on purpose, leave the group
    					   // to avoid extra texts
    		group.sendImOut();
    	}
    	super.onDestroy();
    }
    
    public void onClick(View v){
    	switch(v.getId()) {
    	case R.id.snitch_start_button:
    		if(!group.getPeople().isEmpty()){
    			group.sendGameStart(game.getInterval());
    			sendingLayout.setVisibility(View.VISIBLE);
    			
    			Ref.group = group;
    			Ref.game = game;
    			this.startActivity(new Intent(this, SnitchMap.class));
    			end();
    		} else {
    			Toast.makeText(this, "At least one seeker is required to continue",
    					Toast.LENGTH_SHORT).show();
    		}
    		break;
    	
    	case R.id.item_delete:
    		int position = (int)Integer.valueOf((String)v.getTag());
    		group.sendYoureOut(position);
    		group.removePerson(position);
    		break;
    	
    	case R.id.snitch_settings_button:
    		intervalSettings.setVisibility(View.VISIBLE);
    		intervalSettingsVisible = true;
    		break;
    		
    	case R.id.button_15s:
    		game.setInterval(15);
    		intervalSettings.setVisibility(View.GONE);
            intervalSettingsVisible = false;
            textSnitchSettings.setText(game.getInterval() + " Seconds");
            break;
            
    	case R.id.button_30s:
    		game.setInterval(30);
    		intervalSettings.setVisibility(View.GONE);
            intervalSettingsVisible = false;
            textSnitchSettings.setText(game.getInterval() + " Seconds");
            break;
            
    	case R.id.button_45s:
    		game.setInterval(45);
    		intervalSettings.setVisibility(View.GONE);
            intervalSettingsVisible = false;
            textSnitchSettings.setText(game.getInterval() + " Seconds");
            break;
            
    	case R.id.button_60s:
    		game.setInterval(60);
    		intervalSettings.setVisibility(View.GONE);
            intervalSettingsVisible = false;
            textSnitchSettings.setText(game.getInterval() + " Seconds");
            break;
    	
    	case R.id.grey_space:
    		intervalSettings.setVisibility(View.GONE);
    		intervalSettingsVisible = false;
    		break;
    	}
    		
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {

    		if(intervalSettingsVisible){
    			intervalSettings.setVisibility(View.GONE);
	            intervalSettingsVisible = false;
	        } else if(!group.getPeople().isEmpty()) {
	        	Ref.makeAlert("Quit?", "Do you want to abandon the game?",
	        			new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								group.leaveGroup();
								SnitchMainPage.this.end();
							}
						}, "Yes", 
	        			new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// Do nothing
							}
						}, "Nevermind", this);
	        } else {
	        	end();
	        }
	        return true;
	     }
	     return super.onKeyDown(keyCode, event);
	}
    
    public void makeMeRunner() {
    	// TODO makeMeRunner code, called from updateUI()
    }
    
    public void makeMeSeeker() {
    	// TODO makeMeSeeker code, called from updateUI()
    }

	@Override
	public void end() {  // What to do when the activity is closing on purpose
		navigating = true;
		finish();
	}
}
