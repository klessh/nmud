public class Dictionary{
	String[] nouns = {
		"кладовк",
		"cтолб",
		"коробк",
		"колод"
	};
	
	int[] nounGens = {1,2,1,3};
	
	String[][] nounEndings = {	// окончания существительных
		new String[]{}, // Iскл, м
		new String[]{"а",	"и",	"е",	"у",	"ой",	"е" }, 	// Iскл, ж
		new String[]{"",	"а",	"у",	"",		"ом",	"е"},	// IIскл, м
		new String[]{"ец",	"ца",	"цу",	"ец",	"ем",	"е"}	// IIскл, м, -ец
	};
	
	public int getNounGender(int noun){
		return nounGens[noun];
	}
	
	public String getNounForm(int noun, int form){
	//	System.out.println(noun+" "+form);
		return nouns[noun]+nounEndings[getNounGender(noun)][form];
	}
	
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
}
