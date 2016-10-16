package com.iillyyaa2033.mud.editor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;

public class MapEditorView extends View {

	Context context;

	private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;
    private Paint paint, rootPaint, selectionPaint, coords, coordsBold, zonePaint;


	float scaleFactor = 40;					// коэффициент приближения/удаления
	float[] offset = new float[]{0.2f,0.2f};	// смещение для левого нижнего угла прямоугольника

	WorldObject selected = null;

	public MapEditorView(Context c) {
		super(c);
		init(c);
	}

	public MapEditorView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	void init(Context c) {
		context = c;

		scaleGestureDetector = new ScaleGestureDetector(c, new MyScaleGestureListener());
        detector = new GestureDetector(c, new MyGestureListener());

		rootPaint = new Paint();

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

		coords = new Paint();
		coords.setColor(Color.GRAY);
		coords.setStyle(Paint.Style.STROKE);
		coords.setStrokeWidth(0);
		coords.setAlpha(50);

		coordsBold = new Paint();
		coordsBold.setColor(Color.GRAY);
		coordsBold.setStyle(Paint.Style.STROKE);
		coordsBold.setStrokeWidth(4);
		coordsBold.setAlpha(100);
		
		zonePaint = new Paint();
		zonePaint.setColor(Color.GRAY);
		zonePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		zonePaint.setStrokeWidth(4);
		zonePaint.setAlpha(100);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// сколько координат влезло
		float fromX = 0, fromY = 0;
		float toX = 10, toY = 10;
		float step = 1f;

		for (float i = fromX; i <= toX; i += step) {
			float gX = globalToScreenX(i);
			if (i % 10 == 0) canvas.drawLine(gX, 0, gX, getHeight(), coordsBold);
			else canvas.drawLine(gX, 0, gX, getHeight(), coords);
		}
		
		for (float j = fromY; j <= toY; j += step) {
			float gY = globalToScreenY(j);
			if (j % 10 == 0) canvas.drawLine(0, gY, getWidth(), gY, coordsBold);
			else canvas.drawLine(0, gY, getWidth(), gY, coords);
		}

		for (Zone zone : World.zones) {
			drawObject(canvas, zone, null);
			for (WorldObject object : zone.objects) {
				drawObject(canvas, object, null);
			}
		}

		if (selected != null) drawObject(canvas, selected, selectionPaint);
	}

	void drawObject(Canvas canvas, WorldObject object, Paint paint) {
		double[] objectShape = object.getShape();

		Paint curr = paint;
		if(object instanceof Zone) curr = zonePaint;
		if(paint == null) curr = paint;
		
		if (objectShape == null) return;
		if (objectShape.length < 2) return;
		for (int i = 2; i < objectShape.length - 1; i += 2) {
			canvas.drawLine(globalToScreenX(objectShape[i - 2]), globalToScreenY(objectShape[i - 1]),
							globalToScreenX(objectShape[i]), globalToScreenY(objectShape[i + 1]), curr);
			//	canvas.drawPoint(globalToScreenX(++i),globalToScreenY(++i),paint);
		}
		canvas.drawLine(globalToScreenX(objectShape[objectShape.length - 2]), globalToScreenY(objectShape[objectShape.length - 1]),
						globalToScreenX(objectShape[0]), globalToScreenY(objectShape[1]), curr);

	}

	public void selectObject(WorldObject select) {
		selected = select;
		invalidate();
	}

	float globalToScreenX(double which) {
		int canvasSize = getWidth();
		float local = (float) ((which * scaleFactor + offset[0] * scaleFactor));
		return local;
	}

	float globalToScreenY(double which) {
		int canvasSize = getHeight();
		float local = (float) (canvasSize - (which * scaleFactor + offset[1] * scaleFactor));
		return local;
	}

	@Override	// Если было нажатие
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override	// При движении пальцем
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			offset = new float[]{offset[0] -= (distanceX / scaleFactor),offset[1] += (distanceY / scaleFactor)};
			invalidate();
            return true;
        }

        @Override 	// Одиночный тап
        public boolean onSingleTapConfirmed(MotionEvent event) {
			//	final float x = (event.getX() + getScrollX()) / mScaleFactor;
			//    final float y = (event.getY() + getScrollY()) / mScaleFactor;


			return true;
        }

		@Override
		public void onLongPress(MotionEvent event) {

		}
    }

	private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override	// Если пользователь сделал щипок 
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
			float scale = scaleGestureDetector.getScaleFactor();

			if (scale < 0.5f) return false;

            if (scaleGestureDetector.getCurrentSpan() - scaleGestureDetector.getPreviousSpan() > 0)
				scaleFactor += scale;
			else scaleFactor -= scale;


			//    float focusX = scaleGestureDetector.getFocusX();	//получаем координаты фокальной точки - точки между пальцами
			//    float focusY = scaleGestureDetector.getFocusY();
			//	offset = new float[]{offset[0]+=focusX,offset[1]+=focusY};

			invalidate();
            return true;
        }
    }
}
