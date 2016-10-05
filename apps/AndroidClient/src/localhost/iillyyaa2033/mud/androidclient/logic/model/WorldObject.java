package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.util.HashMap;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class WorldObject {

	public int id;
	public int x,y,x2,y2;	// TODO: remove me
	private double[] shape;
	public Word name;
	public Material material;

	public HashMap<String, String> params = new HashMap<String, String>();

	public WorldObject() {
		id = -1;
	}

	public WorldObject(int x, int y, int x2, int y2, Word name) {
		id = -1;
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.name = name;
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
}
