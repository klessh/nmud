package localhost.iillyyaa2033.mud.androidclient.logic.graph;

import java.util.HashMap;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Dictionary;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.words.Adjective;

public class GraphUtils {

	public static final int rootId = -1;
	
	// Соединяет графы за счет общих частей длиной в deepness
/*	public static Graph appendAuto(int deepness, Graph... graphs) {	// TODO: нормальное имя функции
		if (graphs.length < 1) return new Graph();
		else if (graphs.length == 1) return graphs[0];
		else if (graphs.length > 2) {
			// TODO: множественное сложение по принципам a+b+c = (a+b)+c и
			// a+b+c+d = (a+b)+(c+d)
			return null;
		} else {

			// TOD0: выделить общую часть не менее deepness
			// TODO: определить режим вставки (например, ряд существительных)
			// TODO: перестроить граф, используя слова-связки

			Graph result = new Graph();
			HashMap<Integer, Integer> gr1offset = new HashMap<Integer,Integer>();
			HashMap<Integer, Integer> gr2offset = new HashMap<Integer,Integer>();

			for (int i = 0; i < graphs[0].nodes.length; i++) {
				if (!result.contains((graphs[0].nodes[i]))) {
					int addedWordId = result.addNode(graphs[0].nodes[i]);
					gr1offset.put(i, addedWordId);
				} else if (!gr1offset.containsKey(i)) {
					gr1offset.put(i, result.getId(graphs[0].nodes[i]));
				}
			}

			for (int i = 0; i < graphs[1].nodes.length; i++) {
				if (!result.contains((graphs[1].nodes[i]))) {
					int addedWordId = result.addNode(graphs[1].nodes[i]);
					gr2offset.put(i, addedWordId);
				} else if (!gr2offset.containsKey(i)) {
					gr2offset.put(i, result.getId(graphs[1].nodes[i]));
				}
			}

			gr2offset.put(-1, 1);

			for (int i = 0; i < graphs[0].links.length - 1; i++) {
				int from = (gr1offset.get(graphs[0].links[i]) != null ? gr1offset.get(graphs[0].links[i]) : -2);
				int to = (gr1offset.get(graphs[0].links[++i]) != null ? gr1offset.get(graphs[0].links[i]) : -2);
				result.addLink(from, to, -1);
			}

			for (int i = 0; i < graphs[1].links.length - 1; i++) {
				int from = (gr2offset.get(graphs[1].links[i]) != null ? gr2offset.get(graphs[1].links[i]) : -3);
				int to = (gr2offset.get(graphs[1].links[++i]) != null ? gr2offset.get(graphs[1].links[i]) : -3);
				result.addLink(from, to, -1);
			}

			int i = 0;

			return result;
		}
	} */

	// Возвращает true, если можно склеить данныe графы (т.е. если
	// достаточно общей части)
	public static boolean canAppendAuto(int deepness, Graph... graphs) {
		// TODO: this method
		return !false;
	}

	public static String graphToText(Graph<Word> from) {
		return graphToText(from, 0, 0);
	}

	// Рекурсивная ф-ция
	// from - граф, с которым работаем
	// id - id узла, с которым работаем
	// ending - окончание, которое надо привязать к текущему узлу
	public static String graphToText(Graph<Word> graph, int id, int requestedEnding) {

		String result = "";
		
		Word currentWord = graph.getWord(id);
		if (requestedEnding > 0) currentWord.form = requestedEnding;
		
		result += " " + Dictionary.getWord(currentWord);

		for (int i : graph.getTos(id)) {
			// TODO: Dictionary.agree
			// int neededForm = Dictionary.getNeededForm(currentWord, graph.getWord(i));

			// TODO: порядок слов делается примернр так, но нужно прикрутить инструкции
		/*	if (graph.getWord(i).ch_r == Dictionary.CH_R.ADJECTIVE) {
				neededForm = Adjective.getEnding(Dictionary.getGenderCount(currentWord), currentWord.form);
				result = graphToText(graph, i, neededForm) + result;
			} else 
				result += graphToText(graph, i, neededForm);
				*/
		}

		return result;
	}
}
