package localhost.iillyyaa2033.mud.androidclient.logic.model;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class Material{
	// этот класс содержит ВСЕ свойства объекта для КАЖДОГО уровня абстракции
	
	// TODO: заменить строковый цвет на int RGBA
	public Word color;
	public String madeFrom;
	
	// TODO: наследование в материалах
	// TODO: заменить аргументы на одну HashMap
	public Material(Word color, String madeFrom){
		this.color = color;
		this.madeFrom = madeFrom;
	}
}
