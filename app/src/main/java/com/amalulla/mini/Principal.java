package com.amalulla.mini;

import java.util.List;

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

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

public class Principal extends AppCompatActivity {

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

		ListView listview = (ListView) findViewById(R.id.listView1);
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
		ListView listview = (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, categorias);
		listview.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}

	public void createPhrase(View view) {
		startActivity(new Intent(this, CreateNewPhrase.class));
	}

	public void editPhrase(View view) {
		startActivity(new Intent(this, EditPhrase.class));
	}

	public void removePhrase(View view) {
		startActivity(new Intent(this, RemovePhrase.class));
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
		int id = item.getItemId();
		if (id == R.id.action_about) {
			Processor.About(this);
			return true;
		} else if (id == R.id.action_help) {
			startActivity(new Intent(this, Help.class));
			return true;
		} else if (id == R.id.action_privacy) {
			startActivity(new Intent(this, PrivacyPolicy.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int id = item.getItemId();
		if (id == R.id.editmenu) {
			Processor.CatEditor(this, db, categorias.get(info.position), adapter);
			return true;
		} else if (id == R.id.deletemenu) {
			Processor.CatDeleter(this, db, categorias.get(info.position), adapter);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.dialog_title_exit)
			.setMessage(R.string.dialog_title_sure)
			.setPositiveButton(R.string.dialog_title_yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setNegativeButton(R.string.dialog_title_no, null)
			.show();
	}
}
