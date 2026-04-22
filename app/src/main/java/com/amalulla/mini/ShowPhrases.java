package com.amalulla.mini;

import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShowPhrases extends AppCompatActivity implements OnInitListener {

	private TextToSpeech tts;
	private static final int TTS_CHECK_CODE = 1;
	private DatabaseHandler db;
	private List<String> phrases;
	private ArrayAdapter<String> adapter;
	private ListView listview;
	private String category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_phrases);

		db = new DatabaseHandler(this);

		Bundle param = getIntent().getExtras();
		category = param.getString("category");
		phrases = db.getAllPhrases(category);

		listview = (ListView) findViewById(R.id.listView2);
		adapter = new ArrayAdapter<String>(this, R.layout.list_item_phrase, phrases);
		listview.setAdapter(adapter);

		registerForContextMenu(listview);
		setTitle(category);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (item != null && item.length() > 0) {
					Toast.makeText(ShowPhrases.this, "Diciendo: " + item, Toast.LENGTH_SHORT).show();
					tts.speak(item, TextToSpeech.QUEUE_ADD, null, null);
				}
			}
		});

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_CHECK_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TTS_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				tts = new TextToSpeech(this, this);
			} else {
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
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

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			tts.setLanguage(new Locale("es"));
		}
		if (Principal.firstTime) {
			if (status == TextToSpeech.SUCCESS) {
				Toast.makeText(this, R.string.dialog_tts_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.dialog_tts_failure, Toast.LENGTH_SHORT).show();
			}
			Principal.firstTime = false;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int id = item.getItemId();
		if (id == R.id.createmenu) {
			Processor.NewPhrase(this, db, category, adapter);
			return true;
		} else if (id == R.id.editmenu) {
			Processor.PhraseEditor(this, db, phrases.get(info.position), category, adapter);
			return true;
		} else if (id == R.id.deletemenu) {
			Processor.PhraseDeleter(this, db, phrases.get(info.position), adapter, null);
			return true;
		} else if (id == R.id.mailmenu) {
			Processor.EmailSender(this, db, phrases.get(info.position), category, adapter);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
}
