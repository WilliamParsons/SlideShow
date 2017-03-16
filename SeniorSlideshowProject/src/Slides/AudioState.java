package Slides;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioState implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String fileName; // the filename used to match the current playing audio.
	private AudioInputStream audioFile;
	private long audioTime;
	
	public AudioState(AudioInputStream inputStream, String name)
	{
		fileName = name;
		audioFile = inputStream;
		audioTime = audioFile.getFrameLength();
	}
	
	public AudioState(File object){
		fileName = object.getName();
		try {
			audioFile = AudioSystem.getAudioInputStream(object);
			audioTime = audioFile.getFrameLength();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public AudioState(String name, double time)
//	{
//		audioFile = name;
//		audioTime = time;
//	}
	
	public String getFileName(){
		return fileName;
	}
	
	public Object getAudioFile()
	{
		return audioFile;
	}
	
	public long getAudioTime()
	{
		return audioTime;
	}
	
}