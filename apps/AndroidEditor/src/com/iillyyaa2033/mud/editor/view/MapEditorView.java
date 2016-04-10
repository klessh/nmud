package com.iillyyaa2033.mud.editor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.iillyyaa2033.mud.editor.activity.EditorActivity;
import com.iillyyaa2033.mud.editor.logic.nObject;
import java.util.ArrayList;
import com.iillyyaa2033.mud.editor.logic.nRoom;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MapEditorView extends View {

	String n = "mud.editor";
	Context context;
	EditorActivity parent = null;

	private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;
    private float canvasSize;
    private float mScaleFactor;
	private Paint paint, rootPaint, selectionPaint;

	public ArrayList<nRoom> rooms;		// Сам массив
	private nObject stepObj;
	private int[] toAdd;
	private int[] selectionBorder;
	private int selectedRoomId;
	private int selectedObjId;
	private int type;
	private int mode;
	private static final int FREE = 0, OBJECT_ADDING = 1, OBJECT_EDITING = 2, ROOM_EDITING = 3;
	private static final int TYPE_LEFT = 0, TYPE_RIGHT = 1, TYPE_MOVING = 2;

	public MapEditorView(Context c) {
		super(c);
		init(c);
	}

	public MapEditorView(Context context, AttributeSet ats, int defStyle) { 
	    super(context, ats, defStyle);
		init(context);
	}   

	public MapEditorView(Context context, AttributeSet attrs) {  
		super(context, attrs); 
		init(context);
	}

	void init(Context c) {
		context = c;
		canvasSize = 5000;		// Сторона квадрата
        mScaleFactor = 1f;		// Значение зума по умолчанию

		scaleGestureDetector = new ScaleGestureDetector(c, new MyScaleGestureListener());
        detector = new GestureDetector(c, new MyGestureListener());

		rootPaint = new Paint();
		rootPaint.setColor(Color.WHITE);
		rootPaint.setStyle(Paint.Style.FILL);

		paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
		paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setSubpixelText(true);
		paint.setLinearText(true);
		paint.setFilterBitmap(true);

		selectionPaint = new Paint();
		selectionPaint.setColor(Color.RED);
		selectionPaint.setStyle(Paint.Style.STROKE);
		selectionPaint.setStrokeWidth(4);
		
		rooms = new ArrayList<nRoom>();
		mode = FREE;
	}

	public void setSelectionToRoom(int room_id) {
		if (room_id > rooms.size()) return;

		nRoom obj = rooms.get(room_id);
		selectedRoomId = room_id;
	//	scrollTo(getDisplay().getHeight()+obj.xc, getDisplay().getWidth() + obj.yc);
		selectionBorder = new int[]{obj.x1, obj.y1,obj.x2, obj.y2};
		invalidate();
	}

	public void editObject(int obj_id) {
		setSelectionToRoom(obj_id);
		mode = OBJECT_EDITING;
		type = TYPE_MOVING;
		stepObj = rooms.get(obj_id);
		toAdd = new int[]{stepObj.x1,stepObj.y1,stepObj.x2,stepObj.y2};
		rooms.remove(obj_id);
		selectionBorder = null;
		invalidate();
	}

	public void removeObject(int position) {
		rooms.remove(position);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.scale(mScaleFactor, mScaleFactor);

		rootPaint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, canvasSize, canvasSize, rootPaint);
		canvas.drawRect(0, 0, canvasSize, canvasSize, rootPaint);

		// DRAWING GRID
		rootPaint.setColor(Color.argb(50,0,0,0));
		for (int stepx = 0; stepx < 51; stepx++) {
			canvas.drawLine(stepx * 100, 0, stepx * 100, canvasSize, rootPaint);
		}

		for (int stepy = 0; stepy < 51; stepy++) {
			canvas.drawLine(0, stepy * 100, canvasSize, stepy * 100, rootPaint);
		}

		paint.setStyle(Paint.Style.STROKE);
		if (mode == OBJECT_ADDING || mode == OBJECT_EDITING) {
			canvas.drawRect(toAdd[0], toAdd[1], toAdd[2], toAdd[3], paint);
			canvas.drawCircle(toAdd[0],toAdd[1],10,paint);
			canvas.drawCircle(toAdd[2],toAdd[3],10,paint);
		} 

		// DRAWING OBJECTS
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.argb(70,0,0,0));
		for (nObject obj : rooms) {
			canvas.drawRect(obj.x1, obj.y1, obj.x2, obj.y2, paint);
			canvas.drawText(""+obj.id,obj.x2,obj.y1,paint);
			
			if(obj.id == selectedRoomId)
				canvas.drawText("Room selected",obj.x2,obj.y1+10,paint);
		}

		if (selectionBorder != null) {
			canvas.drawRect(selectionBorder[0], selectionBorder[1], selectionBorder[2], selectionBorder[3], selectionPaint);
		}
	}

	@Override	// Если было нажатие
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

	private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override	// Если пользователь сделал щипок 
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor=scaleGestureDetector.getScaleFactor();	//получаем значение зума относительно предыдущего состояния

            float focusX = scaleGestureDetector.getFocusX();	//получаем координаты фокальной точки - точки между пальцами
            float focusY = scaleGestureDetector.getFocusY();

            // следим чтобы канвас не уменьшили меньше половины исходного размера 
			// и не допускаем увеличения больше чем в три раза
            if (mScaleFactor * scaleFactor > 0.25 && mScaleFactor * scaleFactor < 3) {
                mScaleFactor *= scaleGestureDetector.getScaleFactor();

                int scrollX=(int)((getScrollX() + focusX) * scaleFactor - focusX);
                int scrollY=(int)((getScrollY() + focusY) * scaleFactor - focusY);
                scrollTo(scrollX, scrollY);
				invalidate();
            }
            return true;
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override	// При движении пальцем
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			switch (mode) {
				case FREE:
					scrollBy((int) distanceX, (int)distanceY);
					break;
				case OBJECT_ADDING:
				case OBJECT_EDITING:
					switch (type) {
						case TYPE_MOVING:
							toAdd[0] -= distanceX / (mScaleFactor * 2);
							toAdd[2] -= distanceX / (mScaleFactor * 2);
							toAdd[1] -= distanceY / (mScaleFactor * 2);
							toAdd[3] -= distanceY / (mScaleFactor * 2);
							break;
						case TYPE_LEFT:
							toAdd[0] -= distanceX / (mScaleFactor * 2);
							toAdd[1] -= distanceY / (mScaleFactor * 2);
							if (toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_RIGHT;
							break;
						case TYPE_RIGHT:
							toAdd[2] -= distanceX / (mScaleFactor * 2);
							toAdd[3] -= distanceY / (mScaleFactor * 2);
							if (toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_LEFT;
							break;
					}
					invalidate();
					break;
			}
            return true;
        }

        @Override 	// Одиночный тап
        public boolean onSingleTapConfirmed(MotionEvent event) {
			final float x = (event.getX() + getScrollX()) / mScaleFactor;
            final float y = (event.getY() + getScrollY()) / mScaleFactor;
			
			if (x < 0 || y < 0) return false;

			switch (mode) {
				case FREE:
					for(nObject blank : rooms){
						if(x>blank.x1 && x<blank.x2 && y>blank.y1 && y<blank.y2){
							setSelectionToRoom(blank.id);
							return true;
						}
					}
					mode = OBJECT_ADDING;
					type = TYPE_MOVING;
					toAdd = new int[]{(int) x - 15,(int) y - 15,(int) x + 15,(int) y + 15};
					invalidate();
					break;
				case OBJECT_ADDING:
					if (type == TYPE_MOVING) 
						type = TYPE_RIGHT; 
					else {
						rooms.add(new nRoom(rooms.size(), toAdd, null,null));
						mode = FREE;
						invalidate();
					}
					break;
				case OBJECT_EDITING:
					if (type == TYPE_MOVING) 
						type = TYPE_RIGHT; 
					else {
						stepObj.setCoords(toAdd);
						rooms.add(new nRoom(stepObj,null));
						mode = FREE;
					}
					break;
				default:
					// do nothing
			}
			return true;
        }

		@Override
		public void onLongPress(MotionEvent event) {
			
			switch(mode){
				case FREE:
					(new AlertDialog.Builder(context))
						.setTitle("Obj id is "+selectedRoomId)
						.setItems(new String[]{"Edit this room","Connect with other rooms"}, new AlertDialog.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2) {
								switch(p2){
									
								}
							}
						})
						.show();
			}
			
		}
		
		
    }
}
