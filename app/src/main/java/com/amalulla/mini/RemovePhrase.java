package com.amalulla.mini;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RemovePhrase extends AppCompatActivity implements OnItemSelectedListener {

	private String category;
	private String phrase;
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_phrase);

		db = new DatabaseHandler(this);
		List<String> categories = db.getAllCategories();

		Spinner spinnerCat = (Spinner) findViewById(R.id.spinnerCat2);
		ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, categories);
		spinnerCat.setAdapter(adapterCat);

		spinnerCat.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				category = (String) parent.getItemAtPosition(position);
				resetPhrase(category);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
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

	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
	public void onNothingSelected(AdapterView<?> parent) {}

	public void resetPhrase(String category) {
		List<String> phrases = db.getAllPhrases(category);
		Spinner spinnerPhrase = (Spinner) findViewById(R.id.spinnerPhras2);
		ArrayAdapter<String> adapterPhrase = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, phrases);
		spinnerPhrase.setAdapter(adapterPhrase);
		spinnerPhrase.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				phrase = (String) parent.getItemAtPosition(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	public void removePhrase(View view) {
		Processor.PhraseDeleter(this, db, phrase, null, this);
	}
}
