/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author albertmatyi
 */
public class GrafX {

	public static int CENTER_X = 120;
	public static int CENTER_Y = 160;
	public static int WIDTH = 240;
	public static int HEIGHT = 320;
	private static Image inputLeft = null;
	private static Image inputRight = null;
	private static Image inputBack = null;
	public static Image backgroundImage = null;
	public static Image backImage = null;
	public static Image exitImage = null;
	public static Image autobpmImage = null;
	public static Image playchordImage = null;
	private static Image arrowLeft = null;
	private static Image arrowRight = null;
	public static Image[] bgImage = new Image[3];
	public static Image[] volume = null;

	public static void initialize(MIDlet midlet) {
		Displayable displayable = Display.getDisplay(midlet).getCurrent();
		WIDTH = displayable.getWidth();
		HEIGHT = displayable.getHeight();
		CENTER_X = WIDTH / 2;
		CENTER_Y = HEIGHT / 2;

		try {
			inputLeft = Image.createImage("/images/inputbox_l.png");
			inputRight = Image.createImage("/images/inputbox_r.png");
			inputBack = Image.createImage("/images/inputbox_bg.png");
		} catch (IOException ioexp) {
			System.out.println("Could not load input box images.");
		}
		try {
			arrowLeft = Image.createImage("/images/arrow_l.png");
			arrowRight = Image.createImage("/images/arrow_r.png");
		} catch (IOException ioexp) {
			System.out.println("Could not load arrow images.");
		}

		try {
			backgroundImage = Image.createImage("/images/bg.png");
		} catch (IOException ioex) {
			System.err.println("Could not load background image.");
		}

		try {
			autobpmImage = Image.createImage("/images/autobpm.png");
			playchordImage = Image.createImage("/images/playchord.png");
		} catch (IOException ioex) {
			System.err.println("Could not load background image.");
		}

		try {
			exitImage = Image.createImage("/images/exit.png");
			backImage = Image.createImage("/images/back.png");
		} catch (IOException ioex) {
			System.err.println("Could not load back / exit images.");
		}
		try {
			bgImage[0] = Image.createImage("/images/note_bg.png");
			bgImage[1] = Image.createImage("/images/metronome_bg.png");
			bgImage[2] = Image.createImage("/images/tuner_bg.png");
		} catch (IOException ioex) {
			System.err.println("Could not load note / metronome bg images.");
		}
		volume = new Image[5];
		try {
			Image vols = Image.createImage("/images/volume.png");
			int[] buffer = new int[38 * 30];
			for (int i = 0; i < 5; i++) {
				vols.getRGB(buffer, 0, 38, i * 38, 0, 38, 30);
				volume[i] = Image.createRGBImage(buffer, 38, 30, true);
			}
		} catch (IOException ioex) {
			System.err.println("Could not load volume image.");
		}
	}

	public static void inputBox(Graphics g, int x, int y, int width) {
		g.drawImage(inputLeft, x, y, 0);
		int i;
		for (i = 0; i < width - 15; i += 5) {
			g.drawImage(inputBack, x + 5 + i, y, 0);
		}
		g.drawImage(inputBack, x + width - 10, y, 0);
		g.drawImage(inputRight, x + width - 5, y, 0);
	}

	public static void arrows(Graphics g, int x, int y, int width) {
		g.drawImage(arrowLeft, x - 20, y + 2, 0);
		g.drawImage(arrowRight, x + width, y + 2, 0);
	}

	public static void background(Graphics g) {
		//g.setColor(0xdadad0);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(GrafX.backgroundImage, 0, 0, 0);
	}
}
