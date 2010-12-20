/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

/**
 * @author albertmatyi
 */
public class Main extends MIDlet {
    public static Main instance = null;
    public MainMenu mainMenu = null;
	public NoteMenu noteMenu = null;
	public TunerMenu tunerMenu = null;
	public MetronomeMenu metronomeMenu = null;

	public byte volume = 50;
		
    public void startApp() {
        instance = this;

		switchDisplayable(null, new SplashScreen());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		GrafX.initialize(this);
		   
		mainMenu = new MainMenu();
		noteMenu = new NoteMenu();
		metronomeMenu = new MetronomeMenu();
//		tunerMenu = new TunerMenu();
		
		switchDisplayable(null, mainMenu);

        //mainMenu.repaint();
    }

    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {       
        Display display = Display.getDisplay(this);
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }
    
    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        mainMenu = null;
    }
}
