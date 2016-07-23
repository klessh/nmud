package localhost.iillyyaa2033.descrstest.dictionary.words;

import localhost.iillyyaa2033.descrstest.dictionary.Word;

public class V2 extends AbstractWord {

	static String v2s[] = {
		"расположен"
	};

	static String[][] v2Endings = {
		new String[]{
			"",
			"о",
			"а",
			"ы"
		}
	};

	static int[] v2EndingsMap = {0,1,1,0};
	
	@Override
	public static int getEnding(int word) {
		return v2EndingsMap[word];
	}

	@Override
	public static String getWord(int word, int form) {
		return v2s[word - 1] + v2Endings[getEnding(word - 1)][form - 1];
	}
	
	@Override
	public static int getNeededForm(Word word, int ch_r){
		return 0;
	}
	
	@Override
	public static int getGenderCount(Word form) {
		// TODO: this
		return 0;
	}
}
