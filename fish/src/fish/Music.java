package fish;

import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
 
@SuppressWarnings("deprecation")
public class Music {
	void playMusic (String location) {
		try {
			File path = new File (location);
			if(path.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(path);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioInput);
	            clip.start();
	            clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			else {
				System.out.println("no music"+path);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
}