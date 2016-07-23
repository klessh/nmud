package localhost.iillyyaa2033.descrstest.graph;

import java.util.ArrayList;
import java.util.Arrays;
import localhost.iillyyaa2033.descrstest.dictionary.Dictionary;
import localhost.iillyyaa2033.descrstest.dictionary.Word;

public class Graph {
	
	public Word[] words;
	public int[] links;

	public Graph() {
		words = new Word[0];
		links = new int[0];
	}

	public int add(int idFrom, Word wordTo) {
		return add(idFrom, wordTo, -1);
	}


	public int add(int idFrom, Word wordTo, int args) {

		int addedWordId = addWord(wordTo);
		addLink(idFrom, words.length - 1, args);

		return addedWordId;
	}

	// Возвращает id добавленного слова
	public int addWord(Word word) {
		Word[] newWords = Arrays.copyOf(words, words.length + 1);
		newWords[words.length] = word;
		words = newWords;
		return words.length - 1;
	}

	public void addLink(int from, int to, int args) {
		int[] newLinks = Arrays.copyOf(links, links.length + 3);
		newLinks[links.length] = from;
		newLinks[links.length + 1] = to;
		newLinks[links.length + 2] = args;
//		System.out.println(" # added link "+from+"->"+to);
		links = newLinks;
	}

	// idFrom - id слова в графе wich
	// idTo - id слова в этом графе
	// wich - присоединяемый граф
	public void append(int idFrom, int idTo, Graph wich) {
		Word[] newWords = Arrays.copyOf(words, (words.length + wich.words.length));

		for (int i = 0; i < wich.words.length; i++) {
			newWords[words.length + i] = wich.words[i];
		}

		int[] newLinks = Arrays.copyOf(links, links.length + wich.links.length);
		
		for (int i = 0; i < wich.links.length; i++) {
			
			if (wich.links[i] == idFrom) {
				newLinks[links.length + i] = idTo; 
			} else {
				newLinks[links.length + i] = words.length + wich.links[i];
			}
			i++;
			newLinks[links.length + i] = words.length + wich.links[i];
			i++;
			// TODO: аргументы
		}	

		words = newWords;
		links = newLinks;
	}

	public boolean contains(Word w) {
		for (Word word : words) 
			if (word.equals(w)) 
				return true;
		return false;
	}

	public int getId(Word word) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].equals(word)) return i;
		}

		return -1;
	}

	public Word getWord(int id) {
		if (id < 0 || id > words.length - 1) return new Word(0, 0, 0);
		else return words[id];
	}

	public Integer[] getTos(int id) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < links.length; i++) {

			if (links[i] == id) {
				res.add(links[++i]);
				i++;
			} else {
				i += 2;
			}
		}
		Integer[] result = new Integer[res.size()];
		result = res.toArray(result);
		return result;
	} 

	void delete(int id) {
		// TODO: delete links
		// TODO: delete word only if no other links, points to this word
	}
}
