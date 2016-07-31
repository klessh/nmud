package localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class Points extends AbstractWord {

	String[] points = new String[]{
		",",
		".",
		";"
	};
	
	@Override
	public int getEnding(int word) {
		return 0;
	}

	@Override
	public String getWord(int word, int form) {
		return points[word-1];
	}

	@Override
	public int getNeededForm(Word word, int ch_r) {
		return 0;
	}
	
	@Override
	public static int getGenderCount(Word w) {
		return 0;
	}
}
