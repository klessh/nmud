package localhost.iillyyaa2033.mud.androidclient.logic.dictionary;
import org.keplerproject.luajava.LuaState;

public class WordScript{
	
	private String code;	// скрипт этой части речи
	
	public WordScript(String code){
		this.code = code;
	}
	
	public String getWord(LuaState L, Word w) {
		
		return null;
	}
	
	// Метод согласует зависимое слово (Word to) с главным (Word from)
	public Word agree(LuaState L, Word to, Word from) {
		
		return null;
	}
	
	public int getGender(LuaState L, Word word) {

		return 0;
	}
	
	public int getCount(LuaState L, Word word) {
		
		return 0;
	}
	
}
