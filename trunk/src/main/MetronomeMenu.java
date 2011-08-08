/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.microedition.lcdui.*;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.ToneControl;


/**
 * @author albertmatyi
 */
public class MetronomeMenu extends MyMenu {

	private int bpm = 120;
	private int bpmIdx = 15;
	private int upBeat = 4;
	private static long[] autobpm = new long[2];
	private static int autobpmIndex = 0;
	private static final String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "B", "H"};
	private int activeItem = 0;
	private int items = 3;
	private static Player player = null;
	private String toPrint = "";
	private byte[] mySequence = null;
	private static final byte headSequence[] = {
		ToneControl.VERSION, 1, // version 1
		ToneControl.TEMPO, 30, // set tempo 30=120BPM
		ToneControl.RESOLUTION, 64,
		ToneControl.SET_VOLUME, 50
	};
        private Thread beatThread;

	/**
	 * constructor
	 */
	public MetronomeMenu() {
		try {
			// Set up this canvas to listen to command events
			player = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
			player.setLoopCount(-1);
			player.realize();
			setSequence();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * paint
	 */
	public void paint(Graphics g) {

		super.paint(g);

		GrafX.inputBox(g, GrafX.CENTER_X - 50, GrafX.CENTER_Y - 70, 100);
		GrafX.inputBox(g, GrafX.CENTER_X - 50, GrafX.CENTER_Y + 0, 100);
		GrafX.inputBox(g, GrafX.CENTER_X - 50, GrafX.CENTER_Y + 70, 100);

		g.setColor(0x333333);

		Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		Font fontb = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);

		int fh = font.getHeight();
		g.setFont(font);
		g.drawString("tempo", GrafX.CENTER_X, GrafX.CENTER_Y - 90 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("bpm", GrafX.CENTER_X, GrafX.CENTER_Y - 20 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("upBeat", GrafX.CENTER_X, GrafX.CENTER_Y + 50 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.setFont(fontb);
		g.drawString(tempoNames[bpmIdx], GrafX.CENTER_X, GrafX.CENTER_Y - 70 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("" + bpm, GrafX.CENTER_X, GrafX.CENTER_Y + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("" + upBeat, GrafX.CENTER_X, GrafX.CENTER_Y + 70 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);

		GrafX.arrows(g, GrafX.CENTER_X - 55, GrafX.CENTER_Y - 70 + this.activeItem * 70, 110);

//		g.setColor(0xffffff);
		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		g.drawString(player.getState() == player.STARTED ? "STOP" : "START", GrafX.CENTER_X, GrafX.HEIGHT - 7, Graphics.BASELINE | Graphics.HCENTER);
		g.drawImage(GrafX.autobpmImage, 5, GrafX.HEIGHT - 30, 0);

		printInfo(g);
	}

	/**
	 * Called when a key is pressed.
	 */
	protected void keyPressed(int keyCode) {
		super.keyPressed(keyCode);
		treatKey(keyCode);

		if (keyCode == -6) {
			long cm = System.currentTimeMillis(), t0, t1;
			boolean calc = false;
			if (autobpmIndex == 0) {
				autobpm[0] = cm;
				autobpmIndex++;
			} else {
				if (autobpmIndex > 1) {
					t0 = (autobpm[1] - autobpm[0]) / (autobpmIndex-1);
					t1 = cm - autobpm[1];
//					System.out.println(t0 + "|" + t1+"="+(t1*1./t0)+"%");
					if (t1 *1. / t0 > 1.5 || t1*1. / t0 < 0.5) {
						autobpm[0] = cm;
						autobpmIndex = 1;
//						toPrint = "{1}";
//						System.out.println("reset0");
					} else {
						autobpm[1] = cm;
						autobpmIndex++;
//						toPrint = "{2}";
						calc = true;
					}

				} else if (cm - autobpm[0] > 4000) {
					autobpm[0] = cm;
					autobpmIndex = 1;
//					toPrint = "{3}";
//					System.out.println("reset1");
				} else {
					autobpm[1] = cm;
					autobpmIndex++;
//					toPrint = "{4}";
				}
			}
//			System.out.println(autobpmIndex + " | " + (autobpmIndex > 1 ? ""+(autobpm[1]-autobpm[0]):"n/a"));
			if (calc) {
				this.bpm = (int) (60000. / ((autobpm[1] - autobpm[0]) / (autobpmIndex-1)));
				this.bpm = this.bpm < 20 ? 20 : this.bpm > 440 ? 440 : this.bpm;
				this.bpmIdx = getBpmIndex(bpm);
				setSequence();
			}
		}

//		toPrint += autobpmIndex + " | " + (autobpmIndex == 0 ? "N/A":""+(int)((autobpm[1] - autobpm[0]) / autobpmIndex));
//			if(autobpmIndex != 0 && cm - autobpm[autobpmIndex-1] > 3000)
//				autobpmIndex = 0;
//			autobpm[autobpmIndex++] = cm;
//			if(autobpmIndex == 8) {
//				int nubpm = 0;
//				for(int i = 0; i < 7; i++)
//					nubpm += autobpm[i+1] - autobpm[i];
//				this.bpm =(int) (60000./(nubpm / 7.));
//				this.bpm = this.bpm < 20 ? 20:this.bpm > 440 ? 440:this.bpm;
//				this.bpmIdx = getBpmIndex(bpm);
//				autobpmIndex = 0;
//				setSequence();
//			}
		if (autobpmIndex != 0 && keyCode != -6) {
			autobpmIndex = 0;
		}
	}

	/**
	 * Called when a key is repeated (held down).
	 */
	protected void keyRepeated(int keyCode) {
		super.keyRepeated(keyCode);
		treatKey(
				keyCode);


	}

	protected void keyReleased(int keyCode) {
		super.keyReleased(keyCode);


		if (keyCode == -3 || keyCode == -4 || keyCode == KEY_POUND || keyCode == KEY_STAR) {
			setSequence();


		}
	}

	private void treatKey(int keyCode) {
		switch (keyCode) {
			case -2: { //DOWN
				this.activeItem++;
				if (this.activeItem >= this.items) {
					this.activeItem = 0;
				}
			}
			break;
			case -1: { //UP
				this.activeItem--;
				if (this.activeItem < 0) {
					this.activeItem = this.items - 1;
				}
			}
			break;
			case -3: { //LEFT
				switch (activeItem) {
					case 0:
						bpmIdx = Math.max(bpmIdx - 1, 0);
						if (bpmIdx < tempoBpms.length - 1) {
							bpm = (tempoBpms[bpmIdx] + tempoBpms[bpmIdx + 1]) / 2;


						} else {
							bpm = tempoBpms[bpmIdx] + 10;


						}
						break;


					case 1:
						bpm = Math.max(bpm - 1, 20);
						bpmIdx = getBpmIndex(bpm);


						break;


					case 2:
						upBeat = Math.max(upBeat - 1, 0);


						break;


				}
			}
			break;


			case -4: { //RIGHT
				switch (activeItem) {
					case 0:
						bpmIdx = Math.min(bpmIdx + 1, tempoNames.length - 1);


						if (bpmIdx < tempoBpms.length - 1) {
							bpm = (tempoBpms[bpmIdx] + tempoBpms[bpmIdx + 1]) / 2;


						} else {
							bpm = tempoBpms[bpmIdx] + 10;


						}
						break;


					case 1:
						bpm = Math.min(bpm + 1, 440);
						bpmIdx = getBpmIndex(bpm);


						break;


					case 2:
						upBeat = Math.min(upBeat + 1, 16);


						break;


				}
			}
			break;


			case -5: { // middleok
				try {
					if (player.getState() == player.STARTED || player.getState() == player.PREFETCHED) {
						player.stop();
						player.deallocate();


					} else {
						player.start();


					}
				} catch (MediaException ex) {
					ex.printStackTrace();


				}
			}
			break;


			case -7: { //rightok
				try {
					MetronomeMenu.player.stop();


				} catch (MediaException ex) {
					ex.printStackTrace();


				}
				Main.instance.switchDisplayable(null, Main.instance.mainMenu);


			}
			break;


		}
		repaint();


	}

	private void setSequence() {
		boolean start = false;


		try {
			if (player.getState() == player.STARTED || player.getState() == player.PREFETCHED) {
				start = player.getState() == player.STARTED;
				player.stop();
				player.deallocate();


			}
		} catch (MediaException mexp) {
		}
		createSequence();
		ToneControl tc = (ToneControl) player.getControl("ToneControl");
		tc.setSequence(mySequence);


		if (start) {
			try {
				player.start();


			} catch (MediaException ex) {
				ex.printStackTrace();


			}
		}
	}

	private void createSequence() {
		this.mySequence = new byte[headSequence.length + Math.max(1, this.upBeat) * 4];

		int i;
		for (i = 0; i < headSequence.length; i++) {
			this.mySequence[i] = headSequence[i];
		}
		byte[] trn = getBpmData(this.bpm);
		mySequence[3] = trn[0]; //
		mySequence[5] = trn[1]; // resolution


		byte toneD = trn[2], silenceD = (byte) (toneD * 3);
//		toPrint = "R:" + mySequence[5] + " T:" + mySequence[3] + " D:" + toneD + " t:" + ((int) ((toneD * 1. * 60000. / (mySequence[3] * 1. * mySequence[5] * 1.))));
//		System.out.println(toPrint);

		mySequence[7] = Main.instance.volume;



		if (this.upBeat == 0) {
			this.mySequence[i++] = 80;


			this.mySequence[i++] = toneD;


			this.mySequence[i++] = ToneControl.SILENCE;


			this.mySequence[i++] = silenceD;


			return;


		} else {
			this.mySequence[i++] = 100;


			this.mySequence[i++] = toneD;


			this.mySequence[i++] = ToneControl.SILENCE;


			this.mySequence[i++] = silenceD;


		}
		while (i < this.mySequence.length) {
			this.mySequence[i++] = 80;


			this.mySequence[i++] = toneD;


			this.mySequence[i++] = ToneControl.SILENCE;


			this.mySequence[i++] = silenceD;


		}
	}

	private byte[] getBpmData(int bpm) { // returns tempo, resolution, noteLength
		double r, t, n, resolution = 64, tempo = 30, note = 4,
				minDist = 100, dist;


		byte[] ret = new byte[3];


		for (r = 16; r
				<= 127; r += 16) {
			for (n = 4; n
					<= r / 4; n += 4) {
				t = Math.floor(n * bpm / r);


				if (t > 127 || t < 5) {
					continue;


				}
				if (n * bpm % r == 0) {
					ret[0] = (byte) t;
					ret[

1] = (byte) r;
					ret[

2] = (byte) (n / 4);


					return ret;


				}
				dist = n * bpm - r * t;
				dist *= dist;


				if (dist < minDist) {
					resolution = r;
					tempo = t;
					note = n;


				}
				dist = n * bpm - r * (t + 1);
				dist *= dist;


				if (dist < minDist) {
					resolution = r;
					tempo = t + 1;
					note = n;


				}
			}
		}
		ret[0] = (byte) tempo;
		ret[

1] = (byte) resolution;
		ret[

2] = (byte) (note / 4);


		return ret;


	}

	private void printInfo(Graphics g) {
		g.drawString(toPrint, GrafX.CENTER_X, GrafX.HEIGHT - 40, Graphics.HCENTER | Graphics.TOP);


	}
	private static final String[] tempoNames = {"larghissimo", "lentissimo", "adagissimo", "largo", "larghetto", "largamente",
		"adagio", "adagietto", "lento", "lentamente", "andantino", "andante", "con moto", "moderato", "allegretto", "vivace",
		"allegro", "allegramente", "presto", "allegrissimo", "vivacissimo", "prestissimo", "rapido", "veloce"};
	private static final int[] tempoBpms = {0, 40, 42, 45, 55, 63, 65, 69, 71, 73, 75, 80, 95, 105, 110, 115, 125, 150, 175, 187, 193, 200, 215, 225};

	private int getBpmIndex(int bpm) {
		int i;


		for (i = 0; i
				< tempoBpms.length; i++) {
			if (bpm < tempoBpms[i]) {
				return i - 1;


			}
		}
		return i - 1;


	}

	public int getId() {
		return 1;


	}

	public void initialize() {
		setSequence();

	}
}

