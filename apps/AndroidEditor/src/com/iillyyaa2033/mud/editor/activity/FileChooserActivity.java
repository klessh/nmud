package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.Log;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import android.widget.ProgressBar;

public class FileChooserActivity extends Activity{

	String n = "mud.editor";
	CustomFilter filter;
	int requestcode;
	String output;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getExtras();
		requestcode = b.getInt("requestcode");
		output = b.getString("output");

		File targ = new File(b.getString("target"));
		if (!(targ.canRead())) fail();

		String extension = b.getString("extension");	// Определим, файл какого расширения ищем
		filter = new CustomFilter(extension);
		showDialogForFile(targ);
	}

	void showDialogForFile(final File file){
		AlertDialog.Builder ad = new AlertDialog.Builder(this);

		final String[] list = file.list(filter);
		
		ad.setItems(list, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2){
					File workfile = new File(file.getAbsolutePath(), list[p2]);
					switch (requestcode){
							// ВЫБИРАЕМ И ЧИТАЕМ
						case 1:										
							try{
								if (workfile.isDirectory()){
									showDialogForFile(workfile);
								}else{
									InputStream inputstream = new FileInputStream(workfile);
									InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
									BufferedReader bufferedreader = new BufferedReader(reader);
									StringBuilder sb = new StringBuilder();

									String line = "";
									while ((line = bufferedreader.readLine()) != null){
										sb.append(line + "\n");
									}

									Intent result = new Intent();
									result.putExtra("file", workfile.getAbsolutePath());
									result.putExtra("text", sb.toString());
									setResult(Activity.RESULT_OK, result);
									finish();
								}
							}catch (Exception e){
								fail();
							}							
							break;

							// ПИШЕМ
						case 2:
							try{
								OutputStream outputstream = new FileOutputStream(workfile);
								OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
								writer.write(output);
								writer.flush();
								setResult(Activity.RESULT_OK, null);
								finish();
							}catch (Exception e){
								fail();
							}
							break;
						case 3:	// ВЫБИРАЕМ ФАЙЛ
							try{
								Intent result = new Intent();
								result.putExtra("file", workfile.getAbsolutePath());
								setResult(Activity.RESULT_OK, result);
								finish();
							}catch (Exception e){
								fail();
							}
							break;
						default:
							fail();
					}
				}
			});
		ad.setOnCancelListener(new DialogInterface.OnCancelListener(){

				@Override
				public void onCancel(DialogInterface p1){
					fail();
				}
			});
		
		ad.show();
	}
	
	private void fail(){
		setResult(Activity.RESULT_CANCELED, null);
		finish();
	}

	class CustomFilter implements FilenameFilter{

		String ext;

		CustomFilter(String _ext){
			ext = _ext;
		}

		@Override
		public boolean accept(File dir, String name){
			if(new File(dir,name).isDirectory()) return true;
			return name.endsWith(ext);
		}
	}
}
