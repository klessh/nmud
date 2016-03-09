package com.iillyyaa2033.mud.editor.luaeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.iillyyaa2033.mud.editor.FileChooserActivity;
import com.iillyyaa2033.mud.editor.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class LuaEditorActivity extends Activity{

	// обозначены public для того, чтобы можно было поиграться из Lua
	public EditText source;		// Поле ввода
	public LuaState L;			// Окружение lua
	final StringBuilder output = new StringBuilder();
	ClipboardManager clipboard;	// Буфер обмена
	SharedPreferences pref;		// Настройки
	File targetDir;				// Конечная папка
	File f2save;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luaeditor);
		source = (EditText) findViewById(R.id.source);
		
		try {
			initLua();
			initLuaCmds();
		} catch (Exception e) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setMessage("Невозможно переопределить метод вывода. \nТ.е., сегодня ничего не заработает.");
			adb.show();
		}
		
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		targetDir = new File(pref.getString("PREF_LUA_ROOT","/storage/emulated/0/Download/"));
		
		if (savedInstanceState != null && savedInstanceState.containsKey(TEXTVIEW_STATE_KEY)){
			source.setText(savedInstanceState.getString(TEXTVIEW_STATE_KEY));
		}
		source.setHorizontallyScrolling(pref.getBoolean("PREF_LUA_HSCROLL",true));
		f2save = null;
	}
	
	private void initLua() throws LuaException{
		L = LuaStateFactory.newLuaState();	// Создаем новый LuaState
		L.openLibs();						// 
		L.pushJavaObject(this);		// Помещаем новый объект (нашу активность) в список
		L.setGlobal("server");	// Oбозначаем этот объект как активити

		JavaFunction assetLoader = new JavaFunction(L) {		// Эта ф-ция управляет импортом .lua файлов
			@Override
			public int execute(){
				String name = L.toString(-1);

				try {
					InputStream is = new FileInputStream(targetDir + name + ".lua");	// Откроем поток на чтение
					byte[] bytes = readAll(is);					// Запишем в байтовый массив
					L.LloadBuffer(bytes, name);					// Затем загрузим в luastate?
					return 1;									// Вернем кол-во аргументов
				} catch (Exception e) {												// При ошибке
					ByteArrayOutputStream os = new ByteArrayOutputStream();			// Откроем поток на чтение
					e.printStackTrace(new PrintStream(os));							// ...
					L.pushString("\n\nНевозможно загрузить модуль "+name+":\n"+os.toString());	// ...
					return 1;		// Вернем количество аргументов
				}
			}
		};

		L.getGlobal("package");            // package
		L.getField(-1, "loaders");         // package loaders
		int nLoaders = L.objLen(-1);       // package loaders

		L.pushJavaFunction(assetLoader);   // package loaders loader
		L.rawSetI(-2, nLoaders + 1);       // package loaders
		L.pop(1);                          // package
	}

	private void initLuaCmds() throws LuaException{

		// TODO: написать команду регистрации луа-команды

		JavaFunction print = new JavaFunction(L) {	// Создаем новую функцию

			@Override
			public int execute() throws LuaException {

				for (int i = 2; i <= L.getTop(); i++) {	// Похоже, перебираем типы данных
					int type = L.type(i);				// Похоже, берем обозначение типа данных (int'овый id'шник?)
					String stype = L.typeName(type);	// Похоже, берем строковое имя типа данных; чтобы было понятней сравнение?
					String val = null;
					if (stype.equals("userdata")) {		// Если тип принимаемых данных какие-то пользовательские данные
						Object obj = L.toJavaObject(i);	// Конвертируем в понятный яве класс Object
						if (obj != null)				// И если конвертированный объект не пуст...
							val = obj.toString();		// переведем его в строку и запишем к выводу
					} else if (stype.equals("boolean")) {			// Если это булева логика
						val = L.toBoolean(i) ? "true" : "false";	// То пишем верно или ложно (вместо true или false)
					} else {
						val = L.toString(i);
					}
					if (val == null)		// Если в итоге пусто...
						val = stype;		// просто запишем тип недопонятых данных.
					output.append(val);		// Присоединим наше значение
				}
				return 0;				// Вернем к-во аргументов (т.е. что арг не возвр)
			}
		};
		print.register("print");

		JavaFunction addmult = new JavaFunction(L) {                
			@Override
			public int execute(){
				// Параметр с индексом 1 - сама функция
				double x = L.toNumber(2);	// Второй параметр - число
				double y = L.toNumber(3);	// Третий - тоже
				L.pushNumber(x+y);	// Возвратим сумму цифр
				L.pushNumber(x*y);	// Их произведение
				L.pushNumber(x/y);	// И частное
				return 3;
			}
		};
		addmult.register("addmult");
	}
	
	private static byte[] readAll(InputStream input) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {	// Пока не завершится поток...
			output.write(buffer, 0, n);				// будем заносить в (..) по байтику
		}
		return output.toByteArray();
	}
	
	// Выполним код, заданный в src
	String evalLua(String src) throws LuaException {
		L.setTop(0);
		int ok = L.LloadString(src);			// Выполним и занесем результат работы
		if (ok == 0) {							// Если все нормально
			L.getGlobal("debug");
			L.getField(-1, "traceback");
			L.remove(-2);
			L.insert(-2);
			ok = L.pcall(0, 0, -2);
			if (ok == 0) {				
				String res = output.toString();	// Соединяем весь вывод в одну большую строку
				output.setLength(0);
				return res;						// Затем возвращаем его, т.е. завершили выполнение этого метода
			}
		}
		// Если все плохо - выбросим ошибку
		throw new LuaException(errorReason(ok) + ": " + L.toString(-1));
	}
	
	// "Безопасное" выполнение
	String safeEvalLua(String src) {
		String res = null;			// Вывод, сейчас пуст
		try {						// Попробуем...
			res = evalLua(src);		// Выполнить evalLua (блоком выше).
		} catch(Exception e) {		// Если словили ошибку...
			res = e.getMessage()+"\n";	// Запишем ее в вывод.
		}				// А потом...
		return res;		// вернем вывод, тем самым завершив этот метод.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.editor,menu);		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		
		switch(item.getItemId()){
			case R.id.item_run:
				String src = source.getText().toString();	// Берем текст из поля ввода
				String res = safeEvalLua(src);				// Прогоним программу, записав ее вывод в строку
				
				AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setMessage(res);
				adb.show();
				break;
			case R.id.item_open:
				if(pref.getBoolean("PREF_LUA_CLIPBOARD",false)) source.setText(clipboard.getText());
				else {
					Intent i = new Intent(this, FileChooserActivity.class);
					i.putExtra("requestcode",1);
					i.putExtra("target",targetDir.getAbsolutePath());
					i.putExtra("extension",".lua");
					startActivityForResult(i,1);
				}
				break;
			case R.id.item_save:
				if(pref.getBoolean("PREF_LUA_CLIPBOARD",false)){
					clipboard.setText(source.getText().toString());
					Toast.makeText(this, "Сохранено в буфер обмена", Toast.LENGTH_SHORT).show();
				} else {
					if(f2save == null){
						Toast.makeText(this,"Сорханять некуда. Нажмите \"Сохранить как\"",Toast.LENGTH_LONG).show();
					} else {
						try {
							OutputStream outputstream = new FileOutputStream(f2save);
							OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
							writer.write(source.getText().toString());
							writer.flush();
							Toast.makeText(this,"Сохранено",Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
							Toast.makeText(this,"Не сохранено",Toast.LENGTH_SHORT).show();
						}
					}
				}
				break;
			case R.id.item_save_as:
				Intent i = new Intent(this, FileChooserActivity.class);
				i.putExtra("requestcode",2);
				i.putExtra("target",targetDir.getAbsolutePath());
				i.putExtra("extension",".lua");
				i.putExtra("output",source.getText().toString());
				startActivityForResult(i,2); 
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 1: 		// Если открывали файл
				if (resultCode == Activity.RESULT_OK) {
					source.setText(data.getStringExtra("text"));
					f2save = new File(data.getStringExtra("file"));
				}
				break;
			case 2:			// Если сохраняли файл
				if (resultCode == Activity.RESULT_OK) {
					Toast.makeText(this,"Сохранено",Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this,"Не сохранено",Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
	
	private static final String TEXTVIEW_STATE_KEY = "TEXTVIEW_STATE_KEY";
	@Override public void onSaveInstanceState(Bundle saveInstanceState) {   
		saveInstanceState.putString(TEXTVIEW_STATE_KEY, source.getText().toString());
		super.onSaveInstanceState(saveInstanceState);
	}

	private String errorReason(int error) {
		switch (error) {
			case 1: return "Yield error";
			case 2: return "Ошибка при выполнении";
			case 3: return "Синтаксическая ошибка";
			case 4: return "Память закончилась"; // (Out of memory)
		}
		return "Ошибка " + error + " не опознана";
	}
	
	
	public void send2all(String text){
		output.append(text);
	}
	
	public String getAnswer(){
		return "answer";
	}
}
