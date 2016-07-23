package localhost.iillyyaa2033.descrstest.dictionary.words;

import localhost.iillyyaa2033.descrstest.dictionary.Word;

public class Verb extends AbstractWord {

	static String[] verbs = {
		"нахо"
	};

	static String[][] verbEndings = {
		new String[]{
			"дилcя", "дилось", "дилась",	"дились", "дились", "дились",	// прошедшее
			"жусь", "дишься", "дится",	"димся", "дитесь", "дятся",	// настоящее
			"", "", "", 			"", "", "", 			// будущее
		}
	};

	static int[] verbEndingsMap = {0};
	static boolean[] simpleFuture = {false};

	static String[] verbPrefixes = {
		"буду", "будешь", "будет",
		"будем", "будете", "будут"
	};
	
	@Override
	public static int getEnding(int word) {
		return verbEndingsMap[word];
	}

	@Override
	public static String getWord(int word, int form) {
		if (form < 20)	return verbs[word - 1] + verbEndings[getEnding(word - 1)][form - 1];
		else {
			if (simpleFuture[word])	return verbs[word - 1] + verbEndings[getEnding(word - 1)][form - 1];
			else return verbPrefixes[form - 21] + " " + verbs[word - 1];
		}
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
