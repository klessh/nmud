package localhost.iillyyaa2033.mud.androidclient.logic;

import java.util.HashMap;

public class Dictionary{
	
	Core c;
	
	// maybe, using TreeMap is better?
	HashMap<Integer,String[]> nouns;
	String[][] verbs;
	String[][] adjs;
	
	public Dictionary(Core c){
		this.c = c;
		nouns = new HashMap<Integer,String[]>();
	}
	
	public void update(){
		nouns = c.importer.importWords("nouns");
	}
	
	// search in all known words
	public int getId(String word){
		return 0;
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
