package com.amalulla.mini;

import java.util.List;

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

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

public class CreateNewPhrase extends AppCompatActivity {

	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_phrase);

		db = new DatabaseHandler(this);
		List<String> categories = db.getAllCategories();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, categories);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteCat1);
		textView.setAdapter(adapter);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

	public void sendCreatePhrase(View view) {
		EditText editCategory = (EditText) findViewById(R.id.autoCompleteCat1);
		String category = editCategory.getText().toString();

		EditText editPhrase = (EditText) findViewById(R.id.insertPhrase);
		String phrase = editPhrase.getText().toString();

		if (category.isEmpty() || phrase.isEmpty()) {
			new AlertDialog.Builder(this)
				.setMessage(R.string.error_null_message)
				.setIcon(android.R.drawable.ic_input_add)
				.setTitle(R.string.error_null_title)
				.setPositiveButton(R.string.ok, null)
				.show();
		} else {
			db.addPhrase(category, phrase);
			Toast.makeText(getApplicationContext(), R.string.save_toast, Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
