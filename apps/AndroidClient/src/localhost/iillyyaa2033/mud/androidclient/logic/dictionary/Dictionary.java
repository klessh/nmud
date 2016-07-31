package localhost.iillyyaa2033.mud.androidclient.logic.dictionary;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.Adjective;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.LM;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.Noun;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.Preposition;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.V2;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.Verb;

public class Dictionary {

	public static Word getWord(int id){
		return null;
	}
	
	public static String getWord(Word w){
		return getWord(w.ch_r, w.form, w.wordId);
	}
	
	public static String getWord(int ch_r, int form, int wordID) {
		switch (ch_r) {
			case CH_R.NOUN:			return Noun.getWord(wordID, (form != 0 ? form : form + 1));
			case CH_R.ADJECTIVE:	return Adjective.getWord(wordID, (form != 0 ? form : form + 1));
			case CH_R.VERB:			return Verb.getWord(wordID, (form != 0 ? form : form + 1));
			case CH_R.V2:			return V2.getWord(wordID, (form != 0 ? form : form + 1));
			case CH_R.LM:			return LM.getWord(wordID, (form != 0 ? form : form + 1));
			case CH_R.PREPOSITION:	return Preposition.getWord(wordID, (form != 0 ? form : form + 1));
				
			default:	return "[NOT_WORD]";
		}
	}
	
	public static int getNeededForm(Word from, Word to){
		switch(from.ch_r){
			case CH_R.NOUN:			return Noun.getNeededForm(from,to.ch_r);
			case CH_R.ADJECTIVE:	return Adjective.getNeededForm(from,to.ch_r);
			case CH_R.VERB:			return Verb.getNeededForm(from,to.ch_r);
			case CH_R.V2:			return V2.getNeededForm(from,to.ch_r);
			case CH_R.LM:			return LM.getNeededForm(from,to.ch_r);
			case CH_R.PREPOSITION:	return Preposition.getNeededForm(from,to.ch_r);
		}
		return 0;
	}
	
	public static int getGenderCount(Word word){
		switch(word.ch_r){
			case CH_R.NOUN:			return Noun.getGenderCount(word);
			case CH_R.ADJECTIVE:	return Adjective.getGenderCount(word);
			case CH_R.VERB:			return Verb.getGenderCount(word);
			case CH_R.V2:			return V2.getGenderCount(word);
			case CH_R.LM:			return LM.getGenderCount(word);
			case CH_R.PREPOSITION:	return Preposition.getGenderCount(word);
		}
		return 0;
	}
	
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
