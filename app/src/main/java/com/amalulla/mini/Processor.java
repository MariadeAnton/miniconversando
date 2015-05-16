package com.amalulla.mini;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class Processor {
	
	public static void PhraseDeleter (final Context context, final DatabaseHandler db, final String phrase, 
								final ArrayAdapter<String> adapter, final RemovePhrase remove) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.dialog_message_delete)
		   .setIcon(android.R.drawable.ic_delete)
	       .setTitle(R.string.dialog_title_delete);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
	           public void onClick(DialogInterface dialog, int id) {
	        	   	db.deletePhrase(phrase);
	       			Toast toast = Toast.makeText(context, R.string.del_toast, Toast.LENGTH_SHORT);
	       			toast.show();
	       			if (adapter!=null) {
	       				adapter.remove(phrase);
	       				adapter.notifyDataSetChanged();
	       			} else {
	       				remove.finish();
	       			}
	           }
	       });
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	           }
	       });
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public static void PhraseEditor (final Context context, final DatabaseHandler db, 
									 final String oldphrase, final String category, final ArrayAdapter<String> adapter) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_CLASS_TEXT);
		input.setText(oldphrase);
		input.setSelection(input.getText().length());
		builder.setMessage(R.string.dialog_message_editphrase)
			.setView(input)
			.setIcon(android.R.drawable.ic_menu_edit)
			.setTitle(R.string.dialog_title_edit);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				db.updatePhrase(oldphrase, input.getText().toString(), category);
				adapter.remove(oldphrase);
				adapter.add(input.getText().toString());
				adapter.notifyDataSetChanged();
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}
	
	public static void NewPhrase (final Context context, final DatabaseHandler db, 
			 					 final String category, final ArrayAdapter<String> adapter) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_CLASS_TEXT);
		builder.setMessage(R.string.dialog_message_editphrase)
			.setIcon(android.R.drawable.ic_input_add)
			.setView(input)
			.setTitle(R.string.create_phrase);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				db.addPhrase(category, input.getText().toString());
				adapter.add(input.getText().toString());
				adapter.notifyDataSetChanged();
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}
	
	public static void CatDeleter (final Context context, final DatabaseHandler db, final String category, 
								final ArrayAdapter<String> adapter) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.dialog_message_deletecat)
		   .setIcon(android.R.drawable.ic_delete)
	       .setTitle(R.string.dialog_title_deletecat);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
	           public void onClick(DialogInterface dialog, int id) {
	        	   	db.deleteCategory(category);
	       			Toast toast = Toast.makeText(context, R.string.delcat_toast, Toast.LENGTH_SHORT);
	       			toast.show();
	       			adapter.remove(category);
	       			adapter.notifyDataSetChanged();
	       		}
	       });
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	           }
	       });
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public static void CatEditor (final Context context, final DatabaseHandler db, final String category, 
									final ArrayAdapter<String> adapter) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_CLASS_TEXT);
		input.setText(category);
		input.setSelection(input.getText().length());
		builder.setMessage(R.string.dialog_message_editcat)
			.setView(input)
			.setIcon(android.R.drawable.ic_menu_edit)
			.setTitle(R.string.dialog_title_editcat);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				db.updateCategory(category, input.getText().toString());
				adapter.remove(category);
				adapter.add(input.getText().toString());
       			adapter.notifyDataSetChanged();
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	           }
	       });

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}
	
	public static void EmailSender (final Context context, final DatabaseHandler db, 
			 						final String phrase, final String category, final ArrayAdapter<String> adapter) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(phrase)
			.setTitle(R.string.dialog_title_email)
			.setIcon(android.R.drawable.ic_dialog_email);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				Thread thread = new Thread (new Runnable() {
					@Override
					public void run() {
						Intent email = new Intent(Intent.ACTION_SEND);
						
						email.setType("message/rfc822");
						email.putExtra(Intent.EXTRA_SUBJECT, category);
						email.putExtra(Intent.EXTRA_TEXT, phrase);
						
						try {
						    context.startActivity(Intent.createChooser(email, "Enviar email"));
						} catch (android.content.ActivityNotFoundException ex) {
						    Toast.makeText(context, R.string.no_email_client, Toast.LENGTH_SHORT).show();
						}
					}
				});
				thread.run();
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}
	
	public static void About(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle(R.string.dialog_title_about)
			   .setIcon(android.R.drawable.ic_dialog_info)
			   .setMessage(R.string.text_about1);
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}
}
