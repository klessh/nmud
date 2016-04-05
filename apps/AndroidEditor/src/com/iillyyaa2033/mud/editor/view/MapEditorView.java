package com.iillyyaa2033.mud.editor.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.iillyyaa2033.mud.editor.activity.EditorActivity;
import com.iillyyaa2033.mud.editor.adapter.MapEditorAdapter;
import com.iillyyaa2033.mud.editor.logic.nObject;
import java.util.ArrayList;

public class MapEditorView extends View{

	String n = "mud.editor";
	Context context;
	EditorActivity parent = null;
	
	private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;
    private float canvasSize;
    private float mScaleFactor;
	private Paint paint, rootPaint, selectionPaint;
	
	public ArrayList<nObject> objs;		// Сам массив
	private nObject stepObj;
	public int[] toAdd;
	private int[] selection;
	private int type;
	private int mode;
	private static final int FREE = 0, OBJECT_ADDING = 1, OBJECT_EDITING = 2;
	private static final int TYPE_LEFT = 0, TYPE_RIGHT = 1, TYPE_MOVING = 2;
	
	private MapEditorAdapter leftlist = null;
	
	public MapEditorView(Context c){
		super(c);
		init(c);
	}
	
	public MapEditorView (Context context, AttributeSet ats, int defStyle) { 
	    super(context, ats, defStyle);
		init(context);
	}   
	
	public MapEditorView (Context context, AttributeSet attrs) {  
		super(context, attrs); 
		init(context);
	}
	
	void init(Context c){
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
		
		objs = new ArrayList<nObject>();
		mode = FREE;
	}


	public void setAdapter(MapEditorAdapter adapter){
		leftlist = adapter;
		leftlist.addAlll(objs);
	}
	
	public void setParent(EditorActivity parent){
		this.parent = parent;
	}
	
	public void setSelectionToObject(int obj_id){
		if(obj_id>objs.size()) return;
		
		nObject obj = objs.get(obj_id);
		scrollTo(obj.x1+(obj.x1-obj.xc),obj.y1+(obj.y1-obj.yc));
		selection = new int[]{obj.x1-10,obj.y1-10,obj.x2+10,obj.y2+10};
		invalidate();
	}
	
	public void editObject(int obj_id){
		setSelectionToObject(obj_id);
		mode = OBJECT_EDITING;
		type = TYPE_MOVING;
		stepObj = objs.get(obj_id);
		toAdd = new int[]{stepObj.x1,stepObj.y1,stepObj.x2,stepObj.y2};
		objs.remove(obj_id);
		leftlist.rm(obj_id);
		selection = null;
		invalidate();
	}
	
	public void removeObject(int position){
		objs.remove(position);
		leftlist.rm(position);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.scale(mScaleFactor, mScaleFactor);

		rootPaint.setColor(Color.WHITE);
		canvas.drawRect(0,0,canvasSize,canvasSize,rootPaint);
		canvas.drawRect(0,0,canvasSize,canvasSize,rootPaint);

		// DRAWING LINES
		rootPaint.setColor(Color.BLACK);
		for(int stepx = 0; stepx<101; stepx++){
			canvas.drawLine(stepx*50,0,stepx*50,canvasSize,rootPaint);
		}

		for(int stepy = 0; stepy<101; stepy++){
			canvas.drawLine(0,stepy*50,canvasSize,stepy*50,rootPaint);
		}

		if(mode == OBJECT_ADDING || mode == OBJECT_EDITING){
			canvas.drawRect(toAdd[0],toAdd[1],toAdd[2],toAdd[3],paint);
		}
		
		// DRAWING OBJECTS
		for(nObject obj : objs){
			canvas.drawRect(obj.x1,obj.y1,obj.x2,obj.y2,paint);
		}
		
		if(selection != null){
			canvas.drawRect(selection[0],selection[1],selection[2],selection[3],selectionPaint);
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
            if(mScaleFactor*scaleFactor > 0.25 && mScaleFactor*scaleFactor < 3){
                mScaleFactor *= scaleGestureDetector.getScaleFactor();
				
                int scrollX=(int)((getScrollX()+focusX)*scaleFactor-focusX);
                int scrollY=(int)((getScrollY()+focusY)*scaleFactor-focusY);
                scrollTo(scrollX, scrollY);
				invalidate();
            }
            return true;
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

		public void showDescriptionDialog(int x, int y){
			// TODO: descr gen
		}
		
        @Override	// При движении пальцем
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
			switch(mode){
				case FREE:
					scrollBy((int) distanceX, (int)distanceY);
					break;
				case OBJECT_ADDING:
				case OBJECT_EDITING:
					switch(type){
						case TYPE_MOVING:
							toAdd[0] -= distanceX/(mScaleFactor*2);
							toAdd[2] -= distanceX/(mScaleFactor*2);
							toAdd[1] -= distanceY/(mScaleFactor*2);
							toAdd[3] -= distanceY/(mScaleFactor*2);
							break;
						case TYPE_LEFT:
							toAdd[0] -= distanceX/(mScaleFactor*2);
							toAdd[1] -= distanceY/(mScaleFactor*2);
							if(toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_RIGHT;
							break;
						case TYPE_RIGHT:
							toAdd[2] -= distanceX/(mScaleFactor*2);
							toAdd[3] -= distanceY/(mScaleFactor*2);
							if(toAdd[0] > toAdd[2] || toAdd[1] > toAdd[3]) type = TYPE_LEFT;
							break;
					}
					invalidate();
					break;
					// do nothing
			}
            return true;
        }

        @Override 	// Одиночный тап
        public boolean onSingleTapConfirmed(MotionEvent event){
			final float x = (event.getX() + getScrollX()) / mScaleFactor; //получаем координаты ячейки, по которой тапнули
            final float y = (event.getY() + getScrollY()) / mScaleFactor;

			if (x < 0 || y < 0) return false;


			int objectid = -1;
			for (nObject obj: objs){
				int l = (int) Math.sqrt((obj.xc - x) * (obj.xc - x) + (obj.yc - y) * (obj.yc - y));
				if (l < 10){
					objectid = obj.id;
					break;

				}
			}

			switch (mode){
				case FREE:
		/*			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
					String[] items = new String[]{"Посмотреть описание","OBJECT ADD/EDIT","(AI-граф)"};
					if (objectid == -1){
						dialog.setTitle("<.,.>");
						items[1] = "Разместить объект";
					}else{
						dialog.setTitle("OBJECT N " + objectid);
						items[1] = "Редактировать объект";
					}
					dialog.setItems(items, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface p1, int p2){
								switch (p2){
									case 0:
										showDescriptionDialog((int)x, (int)y);
										break;
									case 1:*/
										mode = OBJECT_ADDING;
										type = TYPE_MOVING;
										toAdd = new int[]{(int) x - 15,(int) y - 15,(int) x + 15,(int) y + 15};
										invalidate();	/*
										break;
								}
							}
						});
					dialog.show(); */
					break;
				case OBJECT_ADDING:
					if (type == TYPE_MOVING) 
						type = TYPE_RIGHT; 
					else{
			/*			AlertDialog.Builder td = new AlertDialog.Builder(context);
						td.setTitle("Choose type:");
						td.setItems(new String[]{"Building","Object"}, new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2){
									switch(p2){
										case 0:
										//	parent.startBuildingEditor(new nObject(objs.size(), toAdd, null));
											break;
										case 1:
										//	parent.startObjectEditor(new nObject(objs.size(), toAdd,null));
											break;
									}
								}
							});
						td.show(); */
						objs.add(new nObject(0,toAdd,null));
						mode = FREE;
						invalidate();
					}
					break;
				case OBJECT_EDITING:
					if (type == TYPE_MOVING) 
						type = TYPE_RIGHT; 
					else{
						stepObj.setCoords(toAdd);
						objs.add(stepObj);
						leftlist.add(stepObj);
						mode = FREE;
					}
					break;
				default:
					// do nothing
			}
			return true;
        }
    }
}
