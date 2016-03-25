package com.iillyyaa2033.mud.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.iillyyaa2033.nmud.abstractserver.model.WorldObject;

public class MapView extends JPanel {

	Main instance;
	
	int size_x = 501;
	int size_y = 501;
	
	ArrayList<WorldObject> objects;
	private WorldObject selectedObj = null;
	private WorldObject lastShown = null;
	boolean shadow_is = false;
	int shadow_x = 0, shadow_y = 0, shadow_x2 = 10, shadow_y2 = 10;
	
	private static final long serialVersionUID = -8946289455183273903L;

	public MapView(Main instance) {
		this.instance = instance;
		
		setBorder(BorderFactory.createLineBorder(Color.black));
		objects = instance.io.load();
		objects.add(new WorldObject(50, 50, 50+20, 50+20, "unnamed"));

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				processClick(e);
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if(selectedObj != null) repaintShadow(e.getX(), e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// nothing
			}
		});
	}

	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int he = Math.min(this.getHeight(), size_x);
		int wi = Math.min(this.getWidth(), size_y);
		
		for(int i = 100; i < wi; i = i+100){
			g.drawLine(i, 0, i, he);
		}
		
		for(int j = 100; j < he; j = j +100){
			g.drawLine(0, j, wi, j);
		}
		
		for (WorldObject obj : objects) {
			g.setColor(Color.RED);
			g.fillRect(obj.x, obj.y, obj.x2 - obj.x, obj.y2 - obj.y);
			g.setColor(Color.BLACK);
			g.drawRect(obj.x, obj.y, obj.x2 - obj.x, obj.y2 - obj.y);
		}
		
		if(shadow_is){
			g.setColor(Color.BLACK);
			g.drawRect(shadow_x, shadow_y, shadow_x2 - shadow_x, shadow_y2 - shadow_y);
		}
	}

	public WorldObject getLastShownObj(){
		return lastShown;
	}
	
	private void processClick(MouseEvent e) {
		WorldObject currObj = chooseObj(e.getX(), e.getY());
		
		switch(e.getButton()){
			case 1:	// LEFT
				if(selectedObj == null){
					if(currObj == null){
						hideShadow();
					} else {
						selectedObj = currObj;
						showShadow(selectedObj);
					}
				} else {
					selectedObj.replaceUsingCenter(e.getX(), e.getY());
					selectedObj = null;
					hideShadow();
				}
				
				break;
			case 3:	// RIGHT
				// TODO normal shadow
				if(currObj == null){
					showShadow(e.getX(), e.getY());
				} else {
					showShadow(currObj);
				}
				break;
			default:
				
		}
		
		currObj = null;
	}
	
	private WorldObject chooseObj(int x, int y){
		
		for(WorldObject o : objects){
			if(x > o.x && x < o.x2 && y > o.y && y < o.y2) return o;
		}
		return null;
	}

	public void addObj(WorldObject obj){
		objects.add(obj);
		shadow_is = false;
		repaint();
	}

	/* Shows shadow for non-created object */
	private void showShadow(int x, int y){
		shadow_is = true;
		shadow_x = x-10;
		shadow_y = y-10;
		shadow_x2 = x+10;
		shadow_y2 = y+10;
		lastShown = new WorldObject(shadow_x,shadow_y,shadow_x2,shadow_y2, "unnamed");
		repaint();
	}

	/* Shows shadow for given object */
	private void showShadow(WorldObject o){
		shadow_is = true;
		shadow_x = o.x - 5;
		shadow_y = o.y - 5;
		shadow_x2 = o.x2 + 5;
		shadow_y2 = o.y2 + 5;
		repaint();
	}
	
	private void showShadowAsIs(WorldObject o){
		shadow_is = true;
		shadow_x = o.x;
		shadow_y = o.y;
		shadow_x2 = o.x2;
		shadow_y2 = o.y2;
		repaint();
	}
	
	/* Repaints shadow to given center */
	private void repaintShadow(int x, int y){
		if(shadow_is){
			int width = (shadow_x2 - shadow_x)/2;
			int height = (shadow_y2 - shadow_y)/2;
			shadow_x = x-width;
			shadow_y = y-height;
			shadow_x2 = x+width;
			shadow_y2 = y+height;
			repaint();
		}
	}
	
	private void hideShadow(){
		shadow_is = false;
		repaint();
	}

	public void save(){
		instance.io.save(objects);
	}
}
