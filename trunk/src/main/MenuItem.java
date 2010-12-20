/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author albertmatyi
 */
public abstract class MenuItem {
	private Image image = null;
	public Image imageSelected = null;
	public boolean selected = false;
	public MenuItem(String name) {
		
		try {
			image = Image.createImage("/images/"+name + ".png");
			imageSelected = Image.createImage("/images/"+name + "Selected.png");
		} catch (IOException ex) {
			System.out.println("Could not load \"/images/"+name+".png\"");
		}
	}

	public void paint(Graphics g, int x, int y)
	{
		if(this.selected)
			g.drawImage(imageSelected, x, y, 0);
		else
			g.drawImage(image, x, y, 0);
	}

	public abstract void execute();
}
