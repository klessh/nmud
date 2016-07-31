package com.iillyyaa2033.mud.editor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import java.util.ArrayList;
import android.util.Log;
import android.graphics.Paint;

public class GrVV extends View {

	ArrayList<MonPoint> monpon;

	public GrVV(Context c) {
		super(c);
		monpon = new ArrayList<MonPoint>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		Paint paint = new Paint();
		for(MonPoint po : monpon){
			canvas.drawCircle(po.x,po.y,10,paint);
			canvas.drawText(po.label, po.x+10, po.y, paint);
			
			for(int i : po.linksTo){
				MonPoint npo = monpon.get(i);
				canvas.drawLine(po.x,po.y,npo.x,npo.y,paint);
			}
		}
	}

	public void updateGr(ArrayList<Integer> links, ArrayList<String> points) {
		monpon.clear();
		
		for (int i = 0; i < points.size(); i++) {
			MonPoint p = new MonPoint();
			p.label = points.get(i);
			monpon.add(p);
		}

		for (int i = 0; i < links.size(); i++) {
			if(links.get(i) > monpon.size() || links.get(i+1) > monpon.size()) 
				Log.e("WTF MF","i - "+links.get(i)+"; i+1 - "+links.get(i+1));
				
			if(links.get(i) >= 0) monpon.get(links.get(i)).linksTo.add(links.get(++i));
			else i++;
		}
		
		recalculatePositions();
		invalidate();
	}
	
	public void recalculatePositions(){
		recursive(monpon.get(0), 0, getWidth(), 1);
	}

	public void recursive(MonPoint p, int offset, int availWidth, int curLvl){
		p.y = curLvl * 25;
		p.x = offset + availWidth/2;
		
		int _offset = offset;
		for(int lnk : p.linksTo){
			MonPoint np = monpon.get(lnk);
			recursive(np, _offset, availWidth/p.linksTo.size(),curLvl+1);
			_offset += availWidth/p.linksTo.size();
		}
	}
	
	class MonPoint {
		int x, y;
		String label;
		ArrayList<Integer> linksTo;
		
		public MonPoint(){
			linksTo = new ArrayList<Integer>();
		}
	}
}
