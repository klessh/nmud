package localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Dictionary;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class Noun extends AbstractWord {

	static String[] nouns = {
		"кладовк",	// 1
		"cтолб",	// 2
		"коробк",	// 3
		"колод",	// 4
		"уг",		// 5
		"стен",		// 6
		"центр",	// 7
	};

	// TODO: окончания множественного числа
	static String[][] nounEndings = {	// окончания существительных
		new String[]{}, // Iскл, м
		new String[]{"а",	"и",	"е",	"у",	"ой",	"е" }, 	// Iскл, ж
		new String[]{"а",	"ы",	"е",	"у",	"ой",	"е"},	// Iскл, ж
		new String[]{"",	"а",	"у",	"",		"ом",	"е"},	// IIскл, м
		new String[]{"ец",	"ца",	"цу",	"ец",	"ем",	"е"},	// IIскл, м, -ец
		new String[]{"ол",	"ла",	"лy",	"ол",	"лом",	"лy"}	// IIскл, м, -ол
	};

	static int[][] neededForms = {
			// NOUN ADJ
		new int[]{0, 0},	// 1
		new int[]{0, 0},
		new int[]{0, 0},	// 3
		new int[]{0, 0},
		new int[]{2, 0},	// 5
		new int[]{2, 0},
		new int[]{2, 0},	// 7
	};
	
	static int[] nounEndingsMap = {1,3,1,4,5,2,3};
	static int[] nounGenderMap = {3,1,3,1,1,3,1};
	
	
	public static int getEnding(int word) {
		return nounEndingsMap[word];
	}

	
	public static String getWord(int word, int form) {
		return nouns[word - 1] + nounEndings[getEnding(word - 1)][form - 1];
	}
	
	
	public static int getNeededForm(Word word, int ch_r){
		int id = word.wordId-1;
		
		switch(ch_r){
			case Dictionary.CH_R.NOUN: return neededForms[id][0];
			case Dictionary.CH_R.ADJECTIVE: return neededForms[id][1];
		}
		return 0;
	}
	
	public static int getGender(int noun) {
		return nounGenderMap[noun];
	}
	
	
	public static int getGenderCount(Word w) {
		return getGender(w.wordId-1);
	}
}
