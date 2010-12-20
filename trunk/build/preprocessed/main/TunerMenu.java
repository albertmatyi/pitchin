/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;
import javax.microedition.media.control.ToneControl;

/**
 *
 * @author albertmatyi
 */
public class TunerMenu extends MyMenu implements Runnable {

	private static Player player = null;
	private static RecordControl record = null;
	private static ByteArrayOutputStream os = null;
	private static Image img = null;
	private static String toPrint = "";
	private static Thread thread = null;
	private byte[] bytes;

	public TunerMenu() {
		String[] encs = {"?amr", "?pcm", "?ulaw", "?gsm", "/x-wav"};
		for(int i = 0; i < encs.length; i++) {
			try {
				//player = Manager.createPlayer("capture://audio?encoding=" + encs[i]);
				//player = Manager.cre
				player = Manager.createPlayer("capture://audio");
				player.realize();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (MediaException ex) {
				ex.printStackTrace();
			}
			if(player != null)
				break;
//				throw new NullPointerException(encs[i]);
		}
		if(player == null)
			throw new NullPointerException("no playa");
		toPrint = "test";
		if((record = (RecordControl) player.getControl("RecordControl")) == null)
			throw new NullPointerException("Null recorder");
		img = Image.createImage(GrafX.WIDTH, 256);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, 0, GrafX.CENTER_Y-128, 0);
		System.out.println("toPrint: " + toPrint);
		g.drawString(toPrint, GrafX.CENTER_X, GrafX.HEIGHT, Graphics.BASELINE | Graphics.HCENTER);
	}

	public void keyReleased(int keyCode) {
		super.keyReleased(keyCode);
		switch (keyCode) {
			case -7: { // rightOK
				this.stop();
				Main.instance.switchDisplayable(null, Main.instance.mainMenu);
			}
			break;
		}
		repaint();
	}

	public int getId() {
		return 2;
	}

	public void start() {
		thread = new Thread(this);
		os = new ByteArrayOutputStream();
		os.reset();
		record.setRecordStream(os);
		record.startRecord();
		try {
			player.start();
		} catch (MediaException ex) {
			toPrint += ex.getMessage();
		}
		thread.start();
	}

	public void stop() {
		thread.interrupt();
		record.stopRecord();
		try {
			os.close();
			os = null;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			player.stop();
		} catch (MediaException ex) {
			ex.printStackTrace();
			System.out.println("stopHAHA");
		}
	}
	private void analyze() {
		double n = bytes.length-1, step = n/GrafX.WIDTH;
		int i;
		Graphics g = img.getGraphics();
		g.setColor(0xffffff);
		g.fillRect(0, 0, GrafX.WIDTH, 256);
		for(i = 0; i < GrafX.WIDTH; i++) {
			g.setColor(0x333333);
			g.drawLine(i, 128+bytes[(int)Math.floor(i*step)], i+1, 128+bytes[(int)Math.floor((i+1)*step)]);
			//g.setColor(0xcc6633);
			//g.fillArc(i-2, 128+bytes[(int)Math.floor(i*step)]-2, 4, 4, 0, 359);
		}
	}
	public void run() {
		try {
			while (true) {
				try {
					record.commit();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				bytes = os.toByteArray();
				os.reset();
				toPrint = "size: " + bytes.length;
				analyze();
				repaint();
				record.setRecordStream(os);
				record.startRecord();
				Thread.sleep(160);
			}
		} catch (InterruptedException ex) {
			try {
				record.stopRecord();
				player.stop();
				player.deallocate();
			} catch (MediaException ex1) {
				ex1.printStackTrace();
			}
		}
	}
}
