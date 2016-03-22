package com.iillyyaa2033.mud.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class Main{

	static Main instance;
	static MapView editorView;
	static InOut io;
	static Database db;
	static String path;
	
	public Main(){
		instance = this;
	}
	
	private static void createAndShowGUI() {
		io = new InOut(instance);
		db = new Database(instance);
		
	}

	public static void main(String[] args) {
		path = args[0];
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
				editorView = new MapView(instance);
				new MainGUI(instance);
			}
		});
	}

}

class MainGUI{
	Main instance;
	MapView editorView;
	
	public MainGUI(Main instance){
		this.instance = instance;
		editorView = instance.editorView;
		
		JFrame frame = new JFrame("nMud editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = createMenuBar();
		frame.setJMenuBar(menuBar);
		
		createRMMenuForEditor();
		frame.setContentPane(editorView);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar(); 
		
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem item = new JMenuItem("[New]");
		menu.add(item);
		
		item = new JMenuItem("[Open file...]");
		// TODO integration with windows
		menu.add(item);
		
		item = new JMenuItem("Save");
		item.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editorView.save();
			}
			
		});
		menu.add(item);
		
		item = new JMenuItem("[Save as...]");
		// TODO integration with windows
		menu.add(item);
		
		item = new JMenuItem("[Close]");
		menu.add(item);
		
		item = new JMenuItem("[Exit]");
		menu.add(item);
		
		
		menu = new JMenu("[Edit]");
		menuBar.add(menu);
		
		item = new JMenuItem("Nothing");
		menu.add(item);
		
		
		menu = new JMenu("[Help]");
		menuBar.add(menu);
		
		item = new JMenuItem("Nothing");
		menu.add(item);
		
		return menuBar;
	}

	public void createRMMenuForEditor(){
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem item = new JMenuItem("Add new");
		item.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(editorView.getLastShownObj()!=null)editorView.addObj(editorView.getLastShownObj());
				JFrame frame = new JFrame("nMud editor");
				// TODO normal param edit
				frame.pack();
				frame.setVisible(true);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Edit current");
		menu.add(item);
		
		item = new JMenuItem("Select other");
		menu.add(item);
		
		MouseListener menuListener = new RMListener(menu);
		editorView.addMouseListener(menuListener);
	}
	
	class RMListener extends MouseAdapter{
		JPopupMenu menu;

		RMListener(JPopupMenu popupMenu) {
			menu = popupMenu;
        }
		
		public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
	}

}

