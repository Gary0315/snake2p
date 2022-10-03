package tw.gary.game;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class snakeGame  {
	
	public static void main(String[] args) {
		
		new gameFrame();
		
		new Thread() {
			@Override
			public void run() {
				File file  = new File("snake/bgm.wav");
				try {
					
					AudioInputStream audio = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);
					clip.loop(clip.LOOP_CONTINUOUSLY);
					
					clip.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				} 
			}
		}.start();
         
	}

}


