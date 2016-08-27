package localhost.iillyyaa2033.mud.androidclient.logic.dictionary;

import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.utils.ExceptionsStorage;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import localhost.iillyyaa2033.mud.androidclient.exceptions.MudException;

public class Dictionary {
	
	public static LuaState L;
	public static ArrayList<WordScript> scripts;
	
	public static void initializeDictionary(ArrayList<WordScript> _scripts){
		if (GlobalValues.canScripts) {
			try {
				L = LuaStateFactory.newLuaState();
				L.openLibs();
			} catch (Throwable t) {
				ExceptionsStorage.exceptions.add(t);
			}
		} 
		
		scripts = _scripts;
		checkAndReport();
	}
	
	public static String getWord(Word w){
		return scripts.get(w.ch_r).getWord(L,w);
	}
	
	public static Word agree(Word from, Word to){
		return scripts.get(to.ch_r).agree(L,to,from);
	}
	
	public static int getGender(Word word){
		return scripts.get(word.ch_r).getGender(L,word);
	}
	
	public static int getCount(Word word){
		return scripts.get(word.ch_r).getCount(L,word);
	}
	
	public static boolean checkAndReport(){
		boolean result = true;
		
		if(L == null){
			ExceptionsStorage.addException(new Exception("Dictionary: LuaState is null"));
			result = !true;
			
		}
		
		if(scripts == null){
			ExceptionsStorage.addException(new Exception("Dictionary: scripts are null"));
			result = !true;
		}
		
		return result;
	}
	
	// TODO: remove all from this line to end of file
	public static class CH_R{
		public static final int NOUN = 1;
		public static final int ADJECTIVE = 2;
		public static final int VERB = 3;
		public static final int V2 = 4;
		public static final int LM = 5;
		public static final int PREPOSITION = 6;
		
		public static final int POINTS = 10;
	}
	
	public static class GenderCount{
		public static final int SINGLE_MALE = 1;
		public static final int SINGLE_MIDDLE = 2;
		public static final int SINGLE_FEMALE = 3;
		public static final int MULTIPLE = 4;
	}
	
	public static class Padezh{
		public static final int IMENIT 	= 1;
		public static final int RODIT 	= 2;
		public static final int DAT 		= 3;
		public static final int VINIT 	= 4;
		public static final int TVORIT 	= 5;
		public static final int PREDLOZH 	= 6;
	}
	
	public static class Time{
		public static final int PAST 	= 1;
		public static final int PRESENT = 2;
		public static final int FUTURE 	= 3;
	}
}
