package com.amalulla.mini;

import java.util.List;

import com.amalulla.mini.R;

import android.app.Activity;
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

public class ShowPhrases extends Activity implements OnInitListener {
	
	private TextToSpeech tts;
	private int MY_DATA_CHECK_CODE = 0;
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
		
		Intent intent = getIntent();
		Bundle param = intent.getExtras();
		
		category = param.getString("category");
		phrases = db.getAllPhrases(category);		
		
		listview = (ListView)findViewById(R.id.listView2);
		
		adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, phrases);
		
		listview.setAdapter(adapter);
		
		registerForContextMenu(listview); 
		setTitle(category);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				
				
				if (item!=null && item.length()>0) {
					Toast.makeText(ShowPhrases.this, "Diciendo: " + item, Toast.LENGTH_LONG).show();
					tts.speak(item, TextToSpeech.QUEUE_ADD, null);
				}
			}
		});
		
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				//crea la instancia del TTS
				
				tts = new TextToSpeech(this, this);
			} 
			else {
				// falta información, instala
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
	
	@Override
	public void onInit(int status) {
		if(Principal.firstTime == true) {
			if (status == TextToSpeech.SUCCESS) {
				Toast.makeText(ShowPhrases.this, 
						"Text-To-Speech engine se ha inicializado", Toast.LENGTH_SHORT).show();
			}
			else if (status == TextToSpeech.ERROR) {
				Toast.makeText(ShowPhrases.this, 
						"Error incicializando Text-To-Speech engine", Toast.LENGTH_SHORT).show();
			}
			Principal.firstTime=false;
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
			switch (item.getItemId()) {
			case R.id.createmenu:
				Processor.NewPhrase(this, db, category, adapter);
				return true;
			case R.id.editmenu:
				Processor.PhraseEditor(this, db, phrases.get(info.position), category, adapter);
				return true;
			case R.id.deletemenu:
				Processor.PhraseDeleter(this, db, phrases.get(info.position), adapter, null);
				return true;
			case R.id.mailmenu:
				Processor.EmailSender(this, db, phrases.get(info.position), category, adapter);
				return true;
			default:
				return super.onContextItemSelected(item);
		  }
	}
	
	@Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
