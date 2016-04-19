package com.iillyyaa2033.mud.editor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.iillyyaa2033.mud.editor.logic.Loader;
import com.iillyyaa2033.mud.editor.logic.nDoor;
import com.iillyyaa2033.mud.editor.logic.nObject;
import com.iillyyaa2033.mud.editor.logic.nRoom;
import java.util.ArrayList;

public class BuildingEditorView extends View{
	
//	public BuildingEditorActivity parent;
	private Context context;
	private Paint wallpaint;
	
	public ArrayList<nRoom> rooms;
	public ArrayList<nObject> objs;
	public ArrayList<nDoor> doors;
	
	private GestureDetector detector;
	private int mode;
	private static final int FREE = 0, ROOM_SELECTED = 1, ROOM_ADDING = 2, OBJECT_ADDING = 3, DOOR_ADDING = 4;
	private static final int TYPE_LEFT = 0, TYPE_TOP = 1, TYPE_RIGHT = 2, TYPE_DOWN = 3, TYPE_MOVING = 4, TYPE_DOOR1 = 5, TYPE_DOOR2 = 6;
	private int[] roomSelector;
	private Paint roomSelectorPaint;
	private int[] toAdd;
	private int type;
	private int[] door1coords;
	
	private Paint objectPaint;
	
	public BuildingEditorView(Context c){
		super(c);
		context = c;
		
		rooms = new ArrayList<nRoom>();
		objs = new ArrayList<nObject>();
		doors = new ArrayList<nDoor>();
		
		wallpaint = new Paint();
		wallpaint.setColor(Color.BLACK);
		wallpaint.setStyle(Paint.Style.STROKE);
		
		roomSelectorPaint = new Paint();
		roomSelectorPaint.setColor(Color.RED);
		roomSelectorPaint.setStyle(Paint.Style.STROKE);
		
		objectPaint = new Paint();
		objectPaint.setColor(Color.BLACK);
		objectPaint.setStyle(Paint.Style.FILL);
		
		detector = new GestureDetector(c,new MyGestureListener());
		mode = FREE;
		roomSelector = new int[]{-1,-1,-1,-1};
		door1coords = new int[]{-1,-1,-1,-1};
		
		// TODO: LOAD FROM SDCARD
		
		rooms.add(new nRoom(0, new int[]{5,5,150,150}, "untamed", null));
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		for(nRoom room : rooms){
			canvas.drawRect(room.x1,room.y1,room.x2,room.y2,wallpaint);
		}
		
		for(nObject object : objs){
			canvas.drawRect(object.x1,object.y1,object.x2,object.y2,objectPaint);
		}
		
		for(nDoor door : doors){
			canvas.drawRect(door.x1,door.y1,door.x2,door.y2,objectPaint);
			canvas.drawRect(door.x3,door.y3,door.x4,door.y4,objectPaint);
			canvas.drawLine(door.xc,door.yc,door.xc2,door.yc2,objectPaint);
		}
		
		if(mode == ROOM_SELECTED){
			canvas.drawRect(roomSelector[0],roomSelector[1],roomSelector[2],roomSelector[3], roomSelectorPaint);				// central
			canvas.drawRect((roomSelector[0]-100),roomSelector[1],roomSelector[0],roomSelector[3], roomSelectorPaint);			// left
			canvas.drawRect(roomSelector[0],(roomSelector[1]-100),roomSelector[2],roomSelector[3], roomSelectorPaint);			// top
			canvas.drawRect(roomSelector[0],roomSelector[1],(roomSelector[2]+100),roomSelector[3], roomSelectorPaint);			// right
			canvas.drawRect(roomSelector[0],roomSelector[1],roomSelector[2],(roomSelector[3]+100), roomSelectorPaint);			// down
			canvas.drawRect((roomSelector[2]+100),(roomSelector[1]-150),(roomSelector[2]+150),(roomSelector[1]-100), roomSelectorPaint);	// close
			canvas.drawLine((roomSelector[2]+100),(roomSelector[1]-150),(roomSelector[2]+150),(roomSelector[1]-100), roomSelectorPaint);
			canvas.drawLine((roomSelector[2]+150),(roomSelector[1]-150),(roomSelector[2]+100),(roomSelector[1]-100), roomSelectorPaint);
			canvas.drawRect((roomSelector[2]+100),(roomSelector[1]-100),(roomSelector[2]+150),(roomSelector[1]-50), roomSelectorPaint);		// add obj
			canvas.drawText("Object",(roomSelector[2]+102),(roomSelector[1]-88),roomSelectorPaint);
			canvas.drawRect((roomSelector[2]+100),(roomSelector[1]-50),(roomSelector[2]+150),(roomSelector[1]), roomSelectorPaint);
			canvas.drawText("Door",(roomSelector[2]+102),(roomSelector[1]-38), roomSelectorPaint);
		} else if(mode == ROOM_ADDING || mode == OBJECT_ADDING){
			canvas.drawRect(toAdd[0],toAdd[1],toAdd[2],toAdd[3],roomSelectorPaint);
		} else if(mode == DOOR_ADDING){
			canvas.drawRect(toAdd[0],toAdd[1],toAdd[2],toAdd[3],roomSelectorPaint);
			canvas.drawRect(door1coords[0],door1coords[1],door1coords[2],door1coords[3],roomSelectorPaint);
		}
	}
	
	@Override	// Если было нажатие
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override	// При движении пальцем
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
			switch(mode){
				case FREE:
					scrollBy((int) distanceX, (int)distanceY);
					break;
				case ROOM_SELECTED:
					// ничего не делаем
					break;
				case ROOM_ADDING:
					switch(type){
						case TYPE_LEFT:
						case TYPE_TOP:
							toAdd[0] -= distanceX/2;
							toAdd[1] -= distanceY/2;
							if(toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_RIGHT;
							invalidate();
							break;
						case TYPE_RIGHT:
						case TYPE_DOWN:
							toAdd[2] -= distanceX/2;
							toAdd[3] -= distanceY/2;
							if(toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_LEFT;
							invalidate();
							break;
					}
					break;
				case OBJECT_ADDING:
					switch(type){
						case TYPE_MOVING:
							toAdd[0] -= distanceX/2;
							toAdd[2] -= distanceX/2;
							toAdd[1] -= distanceY/2;
							toAdd[3] -= distanceY/2;
							break;
						case TYPE_LEFT:
							toAdd[0] -= distanceX/2;
							toAdd[1] -= distanceY/2;
							if(toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_RIGHT;
							break;
						case TYPE_RIGHT:
							toAdd[2] -= distanceX/2;
							toAdd[3] -= distanceY/2;
							if(toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_LEFT;
							break;
					}
					invalidate();
					break;
				case DOOR_ADDING:
					toAdd[0] -= distanceX/2;
					toAdd[2] -= distanceX/2;
					toAdd[1] -= distanceY/2;
					toAdd[3] -= distanceY/2;
					invalidate();
					break;
				default:
					// do nothing
			}
            return true;
        }

        @Override 	// Одиночный тап
        public boolean onSingleTapConfirmed(MotionEvent event){
			final float x = (event.getX()+getScrollX()); //получаем координаты ячейки, по которой тапнули
            final float y = (event.getY()+getScrollY());
			
			switch(mode){
				case FREE:
					for(nRoom room : rooms){
						if(room.x1<x && x<room.x2 && room.y1 < y && y < room.y2){
							scrollTo(room.xc-250, room.yc-250);
							roomSelector = new int[]{room.x1-15,room.y1-15,room.x2+15,room.y2+15};
							mode = ROOM_SELECTED;
						}
					}
					break;
				case ROOM_SELECTED:
					if(x > (roomSelector[2]+100) && y > (roomSelector[1]-150) && x < (roomSelector[2]+150) && y < (roomSelector[1]-100)){
						mode = FREE;
					}else if(x > roomSelector[2]+100 && y > roomSelector[1]-100 && x < roomSelector[2]+150 && y < roomSelector[1]-50){
						mode = OBJECT_ADDING;
						type = TYPE_MOVING;
						toAdd = new int[]{roomSelector[0]+25,roomSelector[1]+25,roomSelector[0]+40,roomSelector[1]+40};
					}else if(x > roomSelector[2]+100 && y > roomSelector[1]-50 && x < roomSelector[2]+150 && y < roomSelector[1]){
						mode = DOOR_ADDING;
						type = TYPE_DOOR1;
						toAdd = new int[]{roomSelector[0]+25,roomSelector[1]+25,roomSelector[0]+40,roomSelector[1]+40};
					}else if(x > (roomSelector[0]-100) && y > roomSelector[1] && x < roomSelector[0] && y < roomSelector[3]){
						mode = ROOM_ADDING;
						type = TYPE_LEFT;
						toAdd = new int[]{roomSelector[0],roomSelector[1]+15,roomSelector[0]+15,roomSelector[3]-15};
					}else if(x > roomSelector[0] && y > (roomSelector[1]-100) && x < roomSelector[2] && y < roomSelector[3]){
						mode = ROOM_ADDING;
						type = TYPE_TOP;
						toAdd = new int[]{roomSelector[0]+15,roomSelector[1],roomSelector[2]-15,roomSelector[1]+15};
					}else if(x > roomSelector[0] && y > roomSelector[1] && x < (roomSelector[2]+100) && y < roomSelector[3]){
						mode = ROOM_ADDING;
						type = TYPE_RIGHT;
						toAdd = new int[]{roomSelector[2]-15,roomSelector[1]+15,roomSelector[2],roomSelector[3]-15};
					}else if(x > roomSelector[0] && y > roomSelector[1] && x < roomSelector[2] && y < (roomSelector[3]+100)){
						mode = ROOM_ADDING;
						type = TYPE_DOWN;
						toAdd = new int[]{roomSelector[0]+15,roomSelector[3]-15,roomSelector[2]-15,roomSelector[3]};
					}
					break;
				case ROOM_ADDING:
					rooms.add(new nRoom(rooms.size(), toAdd, "untamed", null));
					mode = FREE;
					break;
				case OBJECT_ADDING:
					if(type == TYPE_MOVING) 
						type = TYPE_RIGHT; 
					else{
						if(toAdd[0]>=roomSelector[0]+15 && toAdd[1]>=roomSelector[1]+15 && toAdd[2]<=roomSelector[2]-15 && toAdd[3]<=roomSelector[3]-15)
							objs.add(new nObject(objs.size(),toAdd,"untamed"));
						mode = FREE;
			//			parent.objEdit();
					}
					break;
				case DOOR_ADDING:
					if(type == TYPE_DOOR1 && toAdd[0]>=roomSelector[0]+15 && toAdd[1]>=roomSelector[1]+15 && toAdd[2]<=roomSelector[2]-15 && toAdd[3]<=roomSelector[3]-15){
						door1coords = new int[]{toAdd[0],toAdd[1],toAdd[2],toAdd[3]};
						Log.d("mud.editor","door1:"+door1coords[0]+","+door1coords[1]+","+door1coords[2]+","+door1coords[3]);
						type = TYPE_DOOR2;
					}else if(type == TYPE_DOOR2){
						Log.d("mud.editor","door1:"+door1coords[0]+","+door1coords[1]+","+door1coords[2]+","+door1coords[3]);
						doors.add(new nDoor(doors.size(),door1coords,toAdd,null));
						door1coords = new int[]{-1,-1,-1,-1};
						mode = FREE;
					}
					invalidate();
					break;
				default:
					mode = FREE;
			}
			invalidate();
			return true;
        }
    }
}
