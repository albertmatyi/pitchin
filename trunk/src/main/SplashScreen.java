/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author albertmatyi
 */
class SplashScreen extends Canvas {

	public void paint(Graphics g) {
		this.setFullScreenMode(true);
		int w = getWidth(), h = getHeight();
		g.setColor(0xffffff);
		g.fillRect(0, 0, w, h);
		try {
			Image img = Image.createImage("/images/logo.png");
			g.drawImage(img, w / 2 - img.getWidth()/2, h / 2  - img.getHeight()/2, 0);
		} catch (IOException ioexp) {
			System.out.println("Could not load logo and/or loading image");
		}
	}
}
