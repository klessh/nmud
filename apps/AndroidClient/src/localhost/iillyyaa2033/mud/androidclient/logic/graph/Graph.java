package localhost.iillyyaa2033.mud.androidclient.logic.graph;

import java.util.ArrayList;
import java.util.Arrays;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class Graph<T extends Object> {
	
	public T[] nodes;
	public int[] links;

	public Graph() {
		nodes = null;
		links = new int[0];
	}

	public int add(int idFrom, T nodeTo) {
		return add(idFrom, nodeTo, -1);
	}


	public int add(int idFrom, T nodeTo, int args) {

		int addedWordId = addNode(nodeTo);
		addLink(idFrom, nodes.length - 1, args);

		return addedWordId;
	}

	// Возвращает id добавленного слова
	public int addNode(T node) {
		T[] newNodes = Arrays.copyOf(nodes, nodes.length + 1);
		newNodes[nodes.length] = node;
		nodes = newNodes;
		return nodes.length - 1;
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
		T[] newNodes = Arrays.copyOf(nodes, (nodes.length + wich.nodes.length));

		for (int i = 0; i < wich.nodes.length; i++) {
	//		newNodes[nodes.length + i] = wich.nodes[i];
		}

		int[] newLinks = Arrays.copyOf(links, links.length + wich.links.length);
		
		for (int i = 0; i < wich.links.length; i++) {
			
			if (wich.links[i] == idFrom) {
				newLinks[links.length + i] = idTo; 
			} else {
				newLinks[links.length + i] = nodes.length + wich.links[i];
			}
			i++;
			newLinks[links.length + i] = nodes.length + wich.links[i];
			i++;
			// TODO: аргументы
		}	

		nodes = newNodes;
		links = newLinks;
	}

	public boolean contains(Word w) {
		for (T node : nodes) 
			if (node.equals(w)) 
				return true;
		return false;
	}

	public int getId(Word word) {
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].equals(word)) return i;
		}

		return -1;
	}

	public T getWord(int id) {
		if (id < 0 || id > nodes.length - 1) return null;
		else return nodes[id];
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
