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
public class NoteMenu extends MyMenu {

	private byte midiNote = 69;
	private static final String[] noteNames = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B"};
	private static final int[] notes4Keys = {0, 2, 4, 5, 7, 9, 11, 12};
	private int activeItem = 0;
	private int items = 3;
	private static Player player = null;
	private static byte[] toneSequence = {
		ToneControl.VERSION, 1, // version 1
		ToneControl.TEMPO, 30, // set tempo 30=120BPM
		ToneControl.SET_VOLUME, 50,
		69, 64
	};
	private static byte[] chordSequence = {
		ToneControl.VERSION, 1, // version 1
		ToneControl.TEMPO, 30, // set tempo 30=120BPM
		ToneControl.SET_VOLUME, 50,
		69, 32, 69, 32,
		69, 32, 69, 32,
		69, 32
	};
	private int chord = 0;
	public String info = "";

	/**
	 * constructor
	 */
	public NoteMenu() {
		try {
			// Set up this canvas to listen to command events
//			setCommandListener(this);
//			this.setFullScreenMode(true);
			player = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
			player.realize();
			toneSequence[5] = Main.instance.volume;
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
		GrafX.inputBox(g, GrafX.CENTER_X - 50, GrafX.CENTER_Y, 100);
		GrafX.inputBox(g, GrafX.CENTER_X - 50, GrafX.CENTER_Y + 70, 100);

		g.setColor(0x333333);

		Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		Font fontb = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);

		int fh = font.getHeight();
		g.setFont(font);
		g.drawString("note", GrafX.CENTER_X, GrafX.CENTER_Y - 90 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("octave", GrafX.CENTER_X, GrafX.CENTER_Y - 20 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("chord: " + noteNames[midiNote % 12] + chordNames[chord], GrafX.CENTER_X, GrafX.CENTER_Y + 50 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.setFont(fontb);
		g.drawString(noteNames[midiNote % 12], GrafX.CENTER_X, GrafX.CENTER_Y - 70 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString("" + (midiNote / 12 - 1), GrafX.CENTER_X, GrafX.CENTER_Y + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);
		g.drawString(getChordString(), GrafX.CENTER_X, GrafX.CENTER_Y + 70 + (20 - fh) / 2, Graphics.HCENTER | Graphics.TOP);

		GrafX.arrows(g, GrafX.CENTER_X - 55, GrafX.CENTER_Y - 70 + activeItem * 70, 110);

		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		g.drawString("PLAY", GrafX.CENTER_X, GrafX.HEIGHT - 7, Graphics.BASELINE | Graphics.HCENTER);
		g.drawImage(GrafX.playchordImage, 5, GrafX.HEIGHT - 30, 0);
//		printInfo(g);
	}

	/**
	 * Called when a key is pressed.
	 */
	protected void keyPressed(int keyCode) {
		super.keyPressed(keyCode);
		treatKey(keyCode);
	}

	/**
	 * Called when a key is repeated (held down).
	 */
	protected void keyRepeated(int keyCode) {
		super.keyRepeated(keyCode);
		treatKey(keyCode);
	}

	private void treatKey(int keyCode) {
		info = keyCode + " pressed";
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
						midiNote = (byte) Math.max(midiNote - 1, 21);
						break;
					case 1:
						midiNote = (byte) Math.max(midiNote - 12, 21);
						break;
					case 2:
						chord = Math.max(0, chord - 1);
						break;
				}
			}
			break;
			case -4: { //RIGHT
				switch (activeItem) {
					case 0:
						midiNote = (byte) Math.min(midiNote + 1, 108);
						break;
					case 1:
						midiNote = (byte) Math.min(midiNote + 12, 108);
						break;
					case 2:
						chord = Math.min(chordNames.length - 1, chord + 1);
						break;
				}
			}
			break;
			case -5: { // CENTER OK
				try {
					player.stop();
					player.deallocate();
					toneSequence[5] = Main.instance.volume;
					toneSequence[6] = (byte) midiNote;
					ToneControl tc = (ToneControl) player.getControl("ToneControl");
					tc.setSequence(toneSequence);
					player.start();
				} catch (MediaException ex) {
					ex.printStackTrace();
				}
			}
			break;
			case -6: { // LEFT OK
				try {
					player.stop();
					player.deallocate();
					setChord();
					ToneControl tc = (ToneControl) player.getControl("ToneControl");
					tc.setSequence(chordSequence);
					player.start();
				} catch (MediaException ex) {
					ex.printStackTrace();
				}
			}
			break;
			case -7: { // RIGHT OK
				try {
					player.stop();
				} catch (MediaException ex) {
					ex.printStackTrace();
				}
				player.deallocate();
				Main.instance.switchDisplayable(null, Main.instance.mainMenu);
			}
			break;
//			case KEY_NUM0:
//			case KEY_NUM9:
			case KEY_NUM1:
			case KEY_NUM2:
			case KEY_NUM3:
			case KEY_NUM4:
			case KEY_NUM5:
			case KEY_NUM6:
			case KEY_NUM7:
			case KEY_NUM8: {
				try {
					player.stop();
					player.deallocate();
					toneSequence[5] = Main.instance.volume;
					toneSequence[6] = (byte) (midiNote + notes4Keys[(keyCode - 49)]);
					ToneControl tc = (ToneControl) player.getControl("ToneControl");
					tc.setSequence(toneSequence);
					player.start();
				} catch (MediaException ex) {
					ex.printStackTrace();
				}
			}
			break;
		}
		repaint();
	}

	private void printInfo(Graphics g) {
		g.drawString(info, 120, 120, 0);
	}

	public int getId() {
		return 0;
	}

	private String getChordString() {
		int baseNote = midiNote % 12;
		String name = noteNames[baseNote];
		for (int i = 0; i < chordNotes[chord].length; i++) {
			name += " " + noteNames[(baseNote + chordNotes[chord][i]) % 12];
		}
		return name;
	}

	private void setChord() {
		chordSequence[5] = Main.instance.volume;
		byte baseNote = (byte) (midiNote % 12);
		chordSequence[6] = midiNote;
		int i;
		for (i = 0; i < chordNotes[chord].length; i++) {
			chordSequence[8 + i * 2] = (byte) (midiNote + chordNotes[chord][i]);
		}
		for (i = 8 + chordNotes[chord].length * 2; i < chordSequence.length; i += 2) {
			chordSequence[i] = ToneControl.SILENCE;
		}
//		for(i = 6; i < chordSequence.length; i++)
//				System.out.print(chordSequence[i] + " ");
//		System.out.println("");
	}
	private static final String[] chordNames = {"", "m", "+", "ᵒ", "ᵒ7", "m7", "-Maj7", "7", "+7", "7+5", "Maj7+5", "9", "4",
		"2", "sus2", "sus4"};
        private static final int[][] chordNotes = {{4, 7}, {3, 7}, {4, 8}, {3, 6}, {3, 6, 9}, {3, 7, 10}, {3, 7, 11}, {4, 7, 10},
		{4, 7, 11}, {4, 8, 10}, {4, 8, 11}, {4, 7, 10, 14}, {4, 5, 7}, {4, 7, 14}, {2, 7}, {5, 7}};
        private static final String[] chordNames2 = { "ᵒ", "m7", "-Maj7", "7", "+7", "7+5", "Maj7+5", "9", "4",
		"2", "sus2", "sus4"};
        private static final int[][] chordNotes2 = {{3, 6, 9}, {3, 7, 10}, {3, 7, 11}, {4, 7, 10},
		{4, 7, 11}, {4, 8, 10}, {4, 8, 11}, {4, 7, 10, 14}, {4, 5, 7}, {4, 7, 14}, {2, 7}, {5, 7}};
        
        static{
            
        }


}