

import java.io.Serializable;

import javax.sound.sampled.AudioInputStream;

public class AudioState implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String fileName;
	private AudioInputStream audioFile;
	private long audioTime;
	
	public AudioState(AudioInputStream inputStream, String name)
	{
		fileName = name;
		audioFile = inputStream;
		audioTime = audioFile.getFrameLength();
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