package Slides;
public class AudioState{
	
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