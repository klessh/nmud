package localhost.iillyyaa2033.mud.androidclient.logic.dictionary;

/**
* Класс на замену int'овому id'шнику
*/
public class Word{

	public int ch_r, form, wordId;
	
	public Word(int ch_r, int wordId){
		this.ch_r = ch_r;
		this.form = 00;
		this.wordId = wordId;
	}
	
	public Word(int ch_r, int form, int wordId){
		this.ch_r = ch_r;
		this.form = form;
		this.wordId = wordId;
	}

	@Override
	public boolean equals(Word other) {
		if(this.ch_r != other.ch_r) return false;
		if(this.wordId != other.wordId) return false;
		if(this.form != other.form) return false;
		return true;
	}

	@Override
	public String toString() {
		return Dictionary.getWord(this);
	}
}
