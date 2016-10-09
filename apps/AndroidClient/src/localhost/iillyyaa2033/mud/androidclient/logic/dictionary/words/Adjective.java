package localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Dictionary;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class Adjective extends AbstractWord {

	static String[] adjectives = {
		"нижн",		// 1
		"лев",		// 2
		"прав",		// 3
		"верхн",	// 4
		"черн",		// 5
		"сер",		// 6
		"прозрачн",	// 7
	};

	static String adjectiveEndings[][] ={
		new String[]{
			"ий",	"его",	"ему",	"ий",	"им",	"ем",
			"ее",	"его",	"ему",	"ее",	"ем",	"ем",
			"яя",	"ей",	"ей",	"юю",	"ей",	"ей",
			"ие",	"их",	"им",	"ие",	"ими",	"их"
		},
		new String[]{
			"ый",	"ого",	"ому",	"ый",	"ым",	"ом",
			"ое",	"ого",	"ому",	"ое",	"ом",	"ым",
			"ая",	"ой",	"ой",	"ую",	"ой",	"ой",
			"ые",	"ых",	"ым",	"ые",	"ыми",	"ых"
		},
		// TODO: +большой
	};

	static int[] adjectiveEndingsMap = {0,1,1,0,1,1,1};
	
	
	public static int getEnding(int word) {
		return adjectiveEndingsMap[word];
	}

	public static int getEnding(int gender, int padezh){
		int result = 0;
		switch(gender){
			case Dictionary.GenderCount.SINGLE_MALE:
				result = 0;
				break;
			case Dictionary.GenderCount.SINGLE_MIDDLE:
				result = 6;
				break;
			case Dictionary.GenderCount.SINGLE_FEMALE:
				result = 13;
				break;
			case Dictionary.GenderCount.MULTIPLE:
				result = 20;
				break;
		}
		
		result += padezh;
		return result;
	}
	
	
	public static String getWord(int word, int form) {
		return adjectives[word - 1] + adjectiveEndings[getEnding(word - 1)][form - 1];
	}
	
	
	public static int getNeededForm(Word word, int ch_r){
		return 0;
	}
	
	
	public static int getGenderCount(Word word) {
		if(word.form < 7) return Dictionary.GenderCount.SINGLE_MALE;
		if(word.form < 14) return Dictionary.GenderCount.SINGLE_MIDDLE;
		if(word.form < 21) return Dictionary.GenderCount.SINGLE_FEMALE;
		if(word.form < 28) return Dictionary.GenderCount.MULTIPLE;
		return 0;
	}
}
