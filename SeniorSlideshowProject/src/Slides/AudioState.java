package Slides;

import java.io.Serializable;

public class AudioState implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String filename;
	private double audioTime;
	
	public AudioState(String name)
	{
		filename = name;
	}
	
	public AudioState(String name, double time)
	{
		filename = name;
		audioTime = time;
	}
	
	public String getFileName()
	{
		return filename;
	}
	
	public double getAudioTime()
	{
		return audioTime;
	}
	
}