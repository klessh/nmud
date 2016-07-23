package localhost.iillyyaa2033.descrstest.dictionary.words;

import localhost.iillyyaa2033.descrstest.dictionary.Word;

public abstract class AbstractWord{
	
	public abstract int getEnding(int word);
	public abstract String getWord(int word, int form);
	public abstract int getNeededForm(Word word, int ch_r);
	public abstract int getGenderCount(Word word);
	
}
