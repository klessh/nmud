package localhost.iillyyaa2033.mud.androidclient.utils;

import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;

public class DescriptionFactory{
	
	Zone currentZone;
	
	public DescriptionFactory(Zone z){
		currentZone = z;
	}
	
	public void prepare(){
		if(currentZone == null) throw new NullPointerException("DescriptionFactory: current zone is null");
		return;
	}
	
	public String getSampleDescr(){
		String result = "";
		result += currentZone.params.get("string-name").toUpperCase()+". ";
		result += currentZone.params.get("string-descr");
		return result;
	//	return "В течение всей зимы 1927-28 годов официальные представители федерального правительства проводили довольно необычное и строго секретное изучение состояния находящегося в Массачусетсе старого иннсмаутского порта. Широкая общественность впервые узнала обо всем этом лишь в феврале, когда была проведена широкая серия облав и арестов, за которыми последовало полное уничтожение -- посредством осуществленных с соблюдением необходимых мер безопасности взрывов и поджогов -- громадного числа полуразвалившихся, пришедших в почти полную негодность и, по всей видимости, опустевших зданий, стоявших вдоль береговой линии. Не отличающиеся повышенным любопытством граждане отнеслись к данной акции всего лишь как к очередной, пусть даже и достаточно массированной, но все же совершенно спонтанной попытке властей поставить заслон на пути контрабандной поставки в страну спиртных напитков. Более же любознательные люди обратили внимание на небывало широкие масштабы проведенных арестов, многочисленность задействованных в них сотрудников полиции, а также на обстановку секретности, в которой проходил вывоз арестованных.";
	}
}
