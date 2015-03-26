package com.amalulla.mini;

import java.util.List;

import com.amalulla.mini.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RemovePhrase extends Activity implements OnItemSelectedListener{
	
	private String category;
	private String phrase;
	private DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_phrase);
		
		db = new DatabaseHandler(this);
		List<String> CATEGORIES = db.getAllCategories();
		
		
		Spinner spinnerCat = (Spinner) findViewById(R.id.spinnerCat2);
		ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, CATEGORIES);
		spinnerCat.setAdapter(adapterCat);
		
		spinnerCat.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, 
					int position, long id) {
	    		category = (String) parent.getItemAtPosition(position);
	    		resetPhrase(category);
	    	}
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.action_about:
	    	Processor.About(this);
	        return true;
	 
	    case R.id.action_help:
	    	Intent intent = new Intent(this, Help.class);
	  		startActivity(intent);
	        return true;
	 
	     default:
	    	 return super.onOptionsItemSelected(item);
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, 
			int position, long id) {
	}
	
	public void onNothingSelected(AdapterView<?> parent){	
	}

	public void resetPhrase(String category) {
		List<String> PHRASES = db.getAllPhrases(category);
	
		Spinner spinnerPhrase = (Spinner) findViewById(R.id.spinnerPhras2);
		ArrayAdapter<String> adapterPhrase = new ArrayAdapter<String>(this, 
			android.R.layout.simple_spinner_dropdown_item, PHRASES);
		spinnerPhrase.setAdapter(adapterPhrase);
	
		spinnerPhrase.setOnItemSelectedListener(new OnItemSelectedListener() {
		
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, 
				int position, long id) {
				phrase = (String) parent.getItemAtPosition(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent){
			}
		});
	}
	
	public void removePhrase(View view) {
		Processor.PhraseDeleter(this, db, phrase, null, this);
	}

}
