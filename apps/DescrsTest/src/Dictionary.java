public class Dictionary{
	
	String[] nouns = {
		"кладовк",	// 1
		"cтолб",	// 2
		"коробк",	// 3
		"колод",	// 4
		"уг",		// 5
		"стен",		// 6
		"центр",	// 7
	};

	String[][] nounEndings = {	// окончания существительных
		new String[]{}, // Iскл, м
		new String[]{"а",	"и",	"е",	"у",	"ой",	"е" }, 	// Iскл, ж
		new String[]{"а",	"ы",	"е",	"у",	"ой",	"е"},	// Iскл, ж
		new String[]{"",	"а",	"у",	"",		"ом",	"е"},	// IIскл, м
		new String[]{"ец",	"ца",	"цу",	"ец",	"ем",	"е"},	// IIскл, м, -ец
		new String[]{"ол",	"ла",	"лю",	"ол",	"лом",	"ле"}	// IIскл, м, -ол
	};

	int[] nounGens = {1,3,1,4,5,2,3};	// TODO пересчитать
	
	public int getNounGender(int noun){	// TODO normal gender
		return nounGens[noun];
	}
	
	public String getNounForm(int noun, int form){
		return nouns[noun] + nounEndings[getNounGender(noun)][form-1];
	}
	
	
	String[] adjectives = {
			"нижн",		// 1
			"лев",		// 2
			"прав",		// 3
			"верхн",	// 4
	};
	
	String adjectiveEndings[][] ={
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
	};
	
	int[] andjectiveGens = {0,1,1,0};
	
	
	String[][] m = {	// личные местоимения
		new String[]{"я",	"меня",	"мне",	"меня",	"мной",	"мне"},
		new String[]{"мы",	"нас",	"нам",	"нас",	"нами",	"нас"},
		new String[]{"ты",	"тебя",	"тебе",	"тебя",	"тобой","тебе"},
		new String[]{"вы",	"вас",	"вам",	"вас",	"вами",	"вас"},
		new String[]{"он",	"его",	"ему",	"его",	"им",	"нем"},
		new String[]{"она",	"ее",	"ей",	"ее",	"ею",	"ней"},
		new String[]{"оно",	"него",	"ему",	"него",	"ним",	"нем"},
		new String[]{"они",	"их",	"им",	"их",	"ими",	"них"},
	};
	
	public String getM(int gen, int form){
		return m[gen][form];
	}
	
	public String getWord(int id){
		String toParse = Integer.toString(id);
		int CH_R = Integer.decode(toParse.substring(1, 3));
		int form = Integer.decode(toParse.substring(3, 5));
		int wordId = Integer.decode(toParse.substring(5));
		return getWord(CH_R, form, wordId);
	}
	
	public String getWord(int CH_R, int form, int wordID){
		switch(CH_R){
			case 1:
				return getNounForm(wordID, (form !=0 ? form : form+1));
		}
		return "[NOT_WORD]";
	}
}
