package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;

public class WorldObject implements Serializable{

//	public int id;			// TODO: remove me
	public int x,y,x2,y2;	// TODO: remove me
	public Word name;
	public Material material;

	public HashMap<String, String> params = new HashMap<String, String>();
	private double[] shape;
	private String idN;
	
	public WorldObject() {
//		id = -1;
	}

	public WorldObject(int x, int y, int x2, int y2, Word name) {
//		id = -1;
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.name = name;
	}

	public void tick(){
		
	}
	
	public void replaceUsingCenter(int x, int y) {
		int wx = (x2 - this.x) / 2;
		int wy = (y2 - this.y) / 2;
		this.x = x - wx;
		this.y = y - wy;
		this.x2 = x + wx;
		this.y2 = y + wy;
	}

	public double[] getShape() {
		if (shape == null) {
			String shapeStr = params.get("shape");
			if (shapeStr == null) {
				return null;
			} else {
				StringTokenizer token = new StringTokenizer(shapeStr," ");
				double[] _shape = new double[token.countTokens() * 2];
				int indx = -1;
				
				while(token.hasMoreTokens() && indx < _shape.length-2){
					String c = token.nextToken();
					StringTokenizer t = new StringTokenizer(c,":");
					if(t.countTokens() != 2) break;
					_shape[++indx] = Double.parseDouble(t.nextToken());
					_shape[++indx] = Double.parseDouble(t.nextToken());
				}
				shape = _shape;
				return _shape;
			}
		} else {
			return shape;
		}
	}

	public String getId(){
		if(idN == null){
			idN = params.get("id");
			return idN;
		}else{
			return idN;
		}
	}
	
	@Override
	public String toString() {
		String name = params.get("string-name");
		if(name == null) name = "unknown";
		return name;
	}
}
