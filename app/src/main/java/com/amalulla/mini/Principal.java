package com.amalulla.mini;

import java.util.List;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.amalulla.mini.R;


public class Principal extends ListActivity {
	
	private DatabaseHandler db;
	public static boolean firstTime = true;
	private List<String> categorias;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		db = new DatabaseHandler(this);
		categorias = db.getAllCategories();
		
		// Pasamos los elementos de la lista al layout
		ListView listview = (ListView)findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, categorias);
		listview.setAdapter(adapter);
		
		registerForContextMenu(listview); 
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
							
				Intent intent = new Intent();
	    		intent.setClass(getApplicationContext(), ShowPhrases.class);
	    		intent.putExtra("category", item);
	    		
	    		startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		categorias = db.getAllCategories();
		
		// Pasamos los elementos de la lista al layout
		ListView listview = (ListView)findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, categorias);
		listview.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
	
	public void createPhrase (View view) {
		Intent intent = new Intent(this, CreateNewPhrase.class);
		startActivity(intent);
	}
	
	public void editPhrase (View view) {
		Intent intent = new Intent(this, EditPhrase.class);
		startActivity(intent);
	}
	
	public void removePhrase (View view) {
		Intent intent = new Intent(this, RemovePhrase.class);
		startActivity(intent);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
            						ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.principal_context, menu);
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
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			switch (item.getItemId()) {
			case R.id.editmenu:
				Processor.CatEditor(this, db, categorias.get(info.position), adapter);
				return true;
			case R.id.deletemenu:
				Processor.CatDeleter(this, db, categorias.get(info.position), adapter);
				return true;
			default:
				return super.onContextItemSelected(item);
		  }
	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.dialog_title_exit)
	        .setMessage(R.string.dialog_title_sure)
	        .setPositiveButton(R.string.dialog_title_yes, new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton(R.string.dialog_title_no, null)
	    .show();
	}
}
