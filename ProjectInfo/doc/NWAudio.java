// NWAudio made by Michael Strömberg - debugged by Rasmus Kaj

import java.awt.*;
import java.io.*;
import java.applet.*;
import java.net.*;
import java.lang.String;

/**
 * This Applet plays an audio clip according to the parameters set in the web
 * page. Both AudioClip features in the applet package are supported in this
 * audio applet as parameters (duration and sound filename).
 *
 * @author Michael Strömberg <mikaels@it.kth.se>
 */
public class NWAudio extends Applet {
        AudioClip       au;
	String 		duration;

        public void init() {
		String sound = getParameter("sound");

		duration = getParameter("duration");
		System.out.println("Loading " + sound );
		if (duration == null) duration = "loop";
                au = getAudioClip(getDocumentBase(), sound);
	}

        public synchronized void start() {
                if (au != null) {
			System.out.println("Deciding which play mode..." );
			if ( duration.equals ("loop"))  {
				System.out.println("Looping." );
                        	au.loop();
			} else {
				System.out.println("Playing once." );
				au.play();
			}
                } else System.out.println("No sound...  :-(");
        }

        public synchronized void stop() {
		System.out.println("Stopping the sound..." );
                if (au != null) au.stop();
        }
}

// end NWAudio.java
