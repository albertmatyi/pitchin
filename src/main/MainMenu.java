/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.microedition.lcdui.*;
import javax.microedition.media.Manager;

/**
 * @author albertmatyi
 */
public class MainMenu extends Canvas implements CommandListener {

	public MenuItem[] menuItems = new MenuItem[2];
	private int selectedIndex = 0;
	
	/**
	 * initializes components
	 */
	private void initialize() {
		menuItems[0] = new MenuItem("note") {
			public void execute()
			{
				Main.instance.switchDisplayable(null, Main.instance.noteMenu);
			}
		};
		
		menuItems[1] = new MenuItem("metronome") {
			public void execute()
			{
				Main.instance.metronomeMenu.initialize();
				Main.instance.switchDisplayable(null, Main.instance.metronomeMenu);
			}
		};
//		menuItems[2] = new MenuItem("tuner") {
//			public void execute()
//			{
//				Main.instance.metronomeMenu.initialize();
//				Main.instance.switchDisplayable(null, Main.instance.tunerMenu);
//				Main.instance.tunerMenu.start();
//			}
//		};
		this.selectedIndex = 0;
		menuItems[0].selected = true;

		this.setFullScreenMode(true);
	}

	/**
	 * paint
	 */
	public void paint(Graphics g) {
		GrafX.background(g);
//		menuItems[0].paint(g, GrafX.CENTER_X-40, GrafX.CENTER_Y-120);
//		menuItems[1].paint(g, GrafX.CENTER_X-40, GrafX.CENTER_Y-30);
//		menuItems[2].paint(g, GrafX.CENTER_X-17, GrafX.CENTER_Y+60);

		menuItems[0].paint(g, GrafX.CENTER_X-40, GrafX.CENTER_Y-85);
		menuItems[1].paint(g, GrafX.CENTER_X-40, GrafX.CENTER_Y+5);

		g.drawImage(GrafX.exitImage, GrafX.WIDTH - 50, GrafX.HEIGHT - 40, 0);
//		g.drawString(System.getProperty("audio.encodings"), 0, 0, 0);
	}

	/**
	 * Called when a key is pressed.
	 */
	protected void keyPressed(int keyCode) {
		
		switch(keyCode) {
			case -2: { //DOWN
				this.menuItems[this.selectedIndex].selected = false;
				this.selectedIndex++;
				if(this.selectedIndex >= this.menuItems.length)
					this.selectedIndex = 0;
			} break;
			case -1: { //UP
				this.menuItems[this.selectedIndex].selected = false;
				this.selectedIndex--;
				if(this.selectedIndex < 0)
					this.selectedIndex = this.menuItems.length-1;
			} break;
			case -5: {
				this.menuItems[this.selectedIndex].execute();
			} break;
			case -7: {
				Main.instance.switchDisplayable(null, null);
				Main.instance.notifyDestroyed();
				Main.instance.destroyApp(true);
			} break;
		}
		this.menuItems[this.selectedIndex].selected = true;
		repaint();
	}

	/**
	 * Called when a key is released.
	 */
	protected void keyReleased(int keyCode) {
	}

	/**
	 * Called when a key is repeated (held down).
	 */
	protected void keyRepeated(int keyCode) {
	}

	/**
	 * Called when the pointer is dragged.
	 */
	protected void pointerDragged(int x, int y) {
	}

	/**
	 * Called when the pointer is pressed.
	 */
	protected void pointerPressed(int x, int y) {
	}

	/**
	 * Called when the pointer is released.
	 */
	protected void pointerReleased(int x, int y) {
	}

	/**
	 * Called when action should be handled
	 */
	public void commandAction(Command command, Displayable displayable) {
		String label = command.getLabel();
		if ("Exit".equals(label)) {
			Main.instance.switchDisplayable(null, null);
			Main.instance.destroyApp(true);
			Main.instance.notifyDestroyed();
		}
	}

	/**
	 * constructor
	 */
	public MainMenu() {
		try {
			initialize();
			// Set up this canvas to listen to command events
			setCommandListener(this);
			// Add the Exit command
			//addCommand(new Command("Exit", Command.EXIT, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}

}
