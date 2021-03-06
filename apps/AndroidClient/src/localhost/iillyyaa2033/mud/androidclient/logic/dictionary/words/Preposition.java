package localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Dictionary;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class Preposition extends AbstractWord {

	static String[] prep = {
		"в",	// 1
		"около"	// 2
	};
	
	static int[][] neededEnding = {
			  // NOUN  ADJ  VERB
		new int[]{6,    0,    0},
		new int[]{2,    0,    0}
	};
	
	
	public static int getEnding(int word) {
		return 0;
	}

	
	public static String getWord(int word, int form) {
		return prep[word-1];
	}
	
	
	public static int getNeededForm(Word word, int ch_r){
		int id = word.wordId-1;
		
		switch(ch_r){
			case Dictionary.CH_R.NOUN:
				return neededEnding[id][0];
			case Dictionary.CH_R.ADJECTIVE:
				return neededEnding[id][1];
			case Dictionary.CH_R.VERB:
				return neededEnding[id][2];
		}
		return -1;
	}
	
	
	public static int getGenderCount(Word form) {
		return 0;
	}
}
