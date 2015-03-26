package com.amalulla.mini;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNewPhrase extends Activity {
	
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_phrase);
		
		db = new DatabaseHandler(this);
		final List<String> CATEGORIES = db.getAllCategories();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, CATEGORIES);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteCat1);
		textView.setAdapter(adapter);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
	
	public void sendCreatePhrase(View view) {
		
		EditText editCategory = (EditText) findViewById(R.id.autoCompleteCat1);
		String category = editCategory.getText().toString();
		
		EditText editPhrase = (EditText) findViewById(R.id.insertPhrase);
		String phrase = editPhrase.getText().toString();
		
		if (category.matches("") || phrase.matches("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.error_null_message)
			   .setIcon(android.R.drawable.ic_input_add)
		       .setTitle(R.string.error_null_title)
		       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int id) {
		    	   }
		    	})
			   .show();
		} else {
			db.addPhrase(category, phrase);
		
			Toast toast = Toast.makeText(getApplicationContext(), R.string.save_toast, Toast.LENGTH_SHORT);
			toast.show();
			this.finish();
		}
	}
}

