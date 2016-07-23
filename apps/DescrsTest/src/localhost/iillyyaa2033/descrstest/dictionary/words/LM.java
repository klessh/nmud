package localhost.iillyyaa2033.descrstest.dictionary.words;
import localhost.iillyyaa2033.descrstest.dictionary.Word;

public class LM extends AbstractWord {

	static String[][] m = {	// личные местоимения
		new String[]{"я",	"меня",	"мне",	"меня",	"мной",	"мне"},
		new String[]{"мы",	"нас",	"нам",	"нас",	"нами",	"нас"},
		new String[]{"ты",	"тебя",	"тебе",	"тебя",	"тобой","тебе"},
		new String[]{"вы",	"вас",	"вам",	"вас",	"вами",	"вас"},
		new String[]{"он",	"его",	"ему",	"его",	"им",	"нем"},
		new String[]{"она",	"ее",	"ей",	"ее",	"ею",	"ней"},
		new String[]{"оно",	"него",	"ему",	"него",	"ним",	"нем"},
		new String[]{"они",	"их",	"им",	"их",	"ими",	"них"},
	};
	
	@Override
	public static int getEnding(int word) {
		return 0;
	}

	@Override
	public static String getWord(int gen, int form) {
		return m[gen - 1][form - 1];
	}
	
	@Override
	public static int getNeededForm(Word word, int ch_r){
		return 0;
	}
	
	@Override
	public static int getGenderCount(Word form) {
		return 0;
	}
}
