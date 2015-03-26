package com.amalulla.mini;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "phraseManager";
	private static final String KEY_CAT = "categoria";
	private static final String KEY_PHRASE = "frase";
	
	public DatabaseHandler (Context context) {
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//Create tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + DATABASE_NAME + " (" + 
				KEY_CAT + " TEXT, " + KEY_PHRASE + " TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	//Upgrade table
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		onCreate(db);
	}
	
	//Add phrase
	public void addPhrase(String category, String frase) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_CAT, category);
	    values.put(KEY_PHRASE, frase);
	 
	    // Inserting Row
	    db.insert(DATABASE_NAME, null, values);
	    db.close(); // Closing database connection
	}
	
	//Search Category
	public List<String> getAllCategories() {
		List<String> categoryList = new ArrayList<String>();
		HashSet<String> hs = new HashSet<String>();
		
		String selectQuery = "SELECT " + KEY_CAT + " FROM " + DATABASE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				categoryList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		db.close();
		
		hs.addAll(categoryList);
		categoryList.clear();
		categoryList.addAll(hs);
		return categoryList;
	}
	
	//Search phrases in category
	public List<String> getAllPhrases(String category) {
		List<String> phraseList = new ArrayList<String>();
			
		String selectQuery = "SELECT " + KEY_PHRASE + " FROM " + DATABASE_NAME + 
				" WHERE " + KEY_CAT + "= '" + category + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
			
		if (cursor.moveToFirst()) {
			do {
				phraseList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		db.close();
		return phraseList;
	}
	
	//Delete Phrase
	public void deletePhrase(String frase) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_NAME, KEY_PHRASE + " =?", new String[] {String.valueOf(frase) });
		db.close();
	}
	
	public void deleteCategory(String category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_NAME, KEY_CAT + " =?", new String[] {String.valueOf(category) });
		db.close();
	}
	
	public void updatePhrase(String oldphrase, String newphrase, String category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_NAME, KEY_PHRASE + " =?", new String[] {String.valueOf(oldphrase) });
		ContentValues values = new ContentValues();
	    values.put(KEY_CAT, category);
	    values.put(KEY_PHRASE, newphrase);
	 
	    // Inserting Row
	    db.insert(DATABASE_NAME, null, values);
		db.close();
	}
	
	public void updateCategory(String oldcat, String newcat) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT " + KEY_PHRASE + " FROM " + DATABASE_NAME + 
				" WHERE " + KEY_CAT + "= '" + oldcat + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		ContentValues values = new ContentValues();	
		values.put(KEY_CAT, newcat);
		if (cursor.moveToFirst()) {
			do {
				values.put(KEY_PHRASE, cursor.getString(0));
			} while (cursor.moveToNext());
		}
	    
	    db.delete(DATABASE_NAME, KEY_CAT + " =?", new String[] {String.valueOf(oldcat) });
	    // Inserting Row
	    db.insert(DATABASE_NAME, null, values);
		db.close();
	}
	
	public void borrarTabla() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		db.close();
		
	}
}
