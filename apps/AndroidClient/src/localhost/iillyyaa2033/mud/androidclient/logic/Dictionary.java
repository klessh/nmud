package localhost.iillyyaa2033.mud.androidclient.logic;

import java.util.HashMap;

public class Dictionary{
	
	// maybe, using TreeMap is better?
	HashMap<Integer,String[]> nouns;
	String[][] verbs;
	String[][] adjs;
	
	public Dictionary(Core c){
	//	nouns = c.importer.importWords("nouns");
	}
	
	// search in all known words
	public String getPrimaryForm(String word){
		return null;
	}
	
	public String getNoun(String infinitive, int form, boolean num){
		return null;
	}
	
	public String getVerb(String infinitive, int form, boolean num){
		return null;
	}
	
	public String getAdj(String infinitive, int form, boolean num){
		return null;
	}
}
