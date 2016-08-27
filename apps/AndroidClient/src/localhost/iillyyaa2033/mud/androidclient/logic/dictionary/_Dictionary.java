package localhost.iillyyaa2033.mud.androidclient.logic.dictionary;
import java.util.ArrayList;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaException;

public class _Dictionary {

	public static ArrayList<String> codes = new ArrayList<String>();

	public static String getWord(LuaState L, Word w) {
		return getWord(L, w.ch_r, w.form, w.wordId);
	}

	public static String getWord(LuaState L, int ch_r, int form, int wordID) {
		return "[NaN]";
	}

	public static Word agreeWord(LuaState L, Word from) {
		return null;
	}

	public static int getGender(LuaState L, Word w) {	// род
		return 0;
	}

	public static int getCount(LuaState L, Word w) {	// число
		return 0;
	}

	public static int getForm(LuaState L, Word w) {	// падеж
		return 0;
	}

	public static int getTime(LuaState L, Word w) {	// время
		return 0;
	}

	static String getString(LuaState L, String command, String method, String[] args) throws LuaException {
		L.setTop(0);
		int ok = L.LdoString(command);
		if (ok == 0) {
			L.getGlobal(method);

			if (L.isNil(-1)) {
				throw new LuaException("No such method:" + method);
			} else if (args == null) {
				L.pcall(0, 1, -2);
			} else {
				for (int i = 0; i < args.length; i++) {
					L.pushString(args[i]);
				}
				L.pcall(args.length, 1, -2 - args.length);
			}
			return L.toString(-1);
		} else {
			throw new LuaException(errorReason(ok));
		}
	}
	
	static int getInteger(LuaState L, String command, String method, String[] args) throws LuaException {
		L.setTop(0);
		int ok = L.LdoString(command);
		if (ok == 0) {
			L.getGlobal(method);

			if (L.isNil(-1)) {
				throw new LuaException("No such method:" + method);
			} else if (args == null) {
				L.pcall(0, 1, -2);
			} else {
				for (int i = 0; i < args.length; i++) {
					L.pushString(args[i]);
				}
				L.pcall(args.length, 1, -2 - args.length);
			}
			return L.toInteger(-1);
		} else {
			throw new LuaException(errorReason(ok));
		}
	}

	private static String errorReason(int error) {	// Перевод ошибки в словсеный режим
		switch (error) {
			case 1: return "Yield error";
			case 2: return "Runtime error";
			case 3: return "Syntax error";
			case 4: return "Out of memory";
		}
		return "Unknown error: " + error;
	}
}
