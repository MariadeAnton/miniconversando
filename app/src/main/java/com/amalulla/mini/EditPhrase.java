package com.amalulla.mini;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

public class EditPhrase extends AppCompatActivity implements OnItemSelectedListener {

	private String category;
	private String phrase;
	private String newPhrase;
	private Spinner spinnerPhrase;
	private EditText editPhrase;
	private List<String> phrases;
	private Button saveButton;
	private Button editButton;
	private Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_phrase);

		DatabaseHandler db = new DatabaseHandler(this);
		List<String> categories = db.getAllCategories();

		Spinner spinnerCat = (Spinner) findViewById(R.id.spinner1);
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

		editButton = (Button) findViewById(R.id.editbutton);
		saveButton = (Button) findViewById(R.id.savebutton);
		saveButton.setVisibility(View.GONE);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setVisibility(View.GONE);
		editPhrase = (EditText) findViewById(R.id.editPhrase2);
		editPhrase.setVisibility(View.GONE);
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
		DatabaseHandler db = new DatabaseHandler(this);
		phrases = db.getAllPhrases(category);
		spinnerPhrase = (Spinner) findViewById(R.id.spinner2);
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

	public void phraseEditor(View view) {
		spinnerPhrase.setVisibility(View.GONE);
		editButton.setVisibility(View.GONE);
		saveButton.setVisibility(View.VISIBLE);
		cancelButton.setVisibility(View.VISIBLE);
		editPhrase.setText(phrase);
		editPhrase.setVisibility(View.VISIBLE);
		editPhrase.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editPhrase, InputMethodManager.SHOW_IMPLICIT);
	}

	public void phraseSave() {
		new AlertDialog.Builder(this)
			.setMessage(R.string.dialog_message_edit)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.dialog_title_edit)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					db.updatePhrase(phrase, newPhrase, category);
					Toast.makeText(getApplicationContext(), R.string.edit_toast, Toast.LENGTH_SHORT).show();
					finish();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			})
			.show();
	}

	public void checkMessage(View view) {
		newPhrase = editPhrase.getText().toString();
		if (newPhrase.isEmpty()) {
			new AlertDialog.Builder(this)
				.setMessage(R.string.error_null_message)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.error_null_title)
				.setPositiveButton(R.string.ok, null)
				.show();
		} else {
			phraseSave();
		}
	}

	public void cancelEdition(View view) {
		spinnerPhrase.setVisibility(View.VISIBLE);
		editButton.setVisibility(View.VISIBLE);
		saveButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		editPhrase.setVisibility(View.GONE);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editPhrase.getWindowToken(), 0);
	}
}
