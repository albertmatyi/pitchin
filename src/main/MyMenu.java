/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.microedition.lcdui.*;

/**
 * @author albertmatyi
 */
public abstract class MyMenu extends Canvas implements CommandListener {

	private static String volumeLabel = "";
	private static int volumeStep = 1;
	private static int volumeStepChange = 0;
	private static final int VOL_CHG_ACTIVATION = 3;
	private static final int VOL_IMG_CHG_STEP = 25;

	/**
	 * constructor
	 */
	public MyMenu() {
		try {
			// Set up this canvas to listen to command events
			setCommandListener(this);
			this.setFullScreenMode(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * paint
	 */
	public void paint(Graphics g) {
		GrafX.background(g);
//		g.drawImage(Main.instance.mainMenu.menuItems[this.getId()].imageSelected, 5, 5, 0);
		g.drawImage(GrafX.bgImage[this.getId()], GrafX.WIDTH - GrafX.bgImage[this.getId()].getWidth(), GrafX.HEIGHT - GrafX.bgImage[this.getId()].getHeight(), 0);

		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		int imageId = Main.instance.volume == 0 ? 0:(Main.instance.volume-1) / 33 + 1;
		g.drawImage(GrafX.volume[imageId], GrafX.WIDTH - 38, 0, 0);
		g.drawString(volumeLabel, GrafX.WIDTH - 19, 38, Graphics.TOP | Graphics.HCENTER);

		//g.drawString("back", GrafX.WIDTH - 14, GrafX.HEIGHT - 5, Graphics.HCENTER | Graphics.BASELINE);
		g.drawImage(GrafX.backImage, GrafX.WIDTH - 50, GrafX.HEIGHT - 40, 0);
//		g.drawImage(GrafX.startImage, GrafX.CENTER_X-50, GrafX.HEIGHT - 30, 0);
	}

	/**
	 * Called when a key is pressed.
	 */
	protected void keyPressed(int keyCode) {
//		if(keyCode == KEY_STAR || keyCode == KEY_POUND) {
//			volumeLabel = Main.instance.volume+"%";
//		}
	}

	/**
	 * Called when a key is released.
	 */
	protected void keyReleased(int keyCode) {
		if (volumeStepChange == 0) {
			switch (keyCode) {
				case KEY_STAR:
					if(Main.instance.volume%VOL_IMG_CHG_STEP != 0)
						Main.instance.volume += VOL_IMG_CHG_STEP-Main.instance.volume%VOL_IMG_CHG_STEP;
					Main.instance.volume = (byte) Math.max(0, Main.instance.volume - VOL_IMG_CHG_STEP);
					break;
				case KEY_POUND:
					Main.instance.volume -= Main.instance.volume%VOL_IMG_CHG_STEP;
					Main.instance.volume = (byte) Math.min(100, Main.instance.volume + VOL_IMG_CHG_STEP);
					break;
			}
		}
		volumeLabel = "";//Main.instance.volume+"%";
		volumeStepChange = 0;
		volumeStep=1;
		repaint();
	}

	/**
	 * Called when a key is repeated (held down).
	 */
	protected void keyRepeated(int keyCode) {
		if(keyCode == KEY_STAR || keyCode == KEY_POUND) {
			
			if(volumeStepChange == VOL_CHG_ACTIVATION) {
				volumeStepChange = 0;
				volumeStep++;
			}
			volumeStepChange++;
		}
		switch (keyCode) {
			case KEY_STAR:
				Main.instance.volume = (byte) Math.max(0, Main.instance.volume - volumeStep);
				break;
			case KEY_POUND:
				Main.instance.volume = (byte) Math.min(100, Main.instance.volume + volumeStep);
				break;
		}
		if(keyCode == KEY_STAR || keyCode == KEY_POUND)
			volumeLabel = Main.instance.volume + "%";
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
	}

	public abstract int getId();
}
