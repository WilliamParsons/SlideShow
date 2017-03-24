package Slides;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioState implements Serializable {

	private static final long serialVersionUID = 1L;
	private String fileName; // the filename used to match the current playing audio.
	private Object audio;
	private double audioTime;

	public AudioState(Object object)
	{
		audio = object;
		if(audio instanceof File)
		{
			File file = (File)audio;
			fileName = file.getName();
			// audioTime = getAudioTime();
		}
	}

//	public AudioState(String name, double time)
//	{
//		audioFile = name;
//		audioTime = time;
//	}

	public String getFileName()
	{
		return fileName;
	}

	public Object getAudio()
	{
		return audio;
	}
	public double getAudioTime()
	{
		double seconds = 0.0;
        if (audio instanceof Clip)
        {
            Clip clip = (Clip) audio;
            seconds = clip.getFramePosition() / clip.getFormat().getFrameRate();
        }
        else if ( (audio instanceof Sequence) || (audio instanceof BufferedInputStream) )
        {
            try
            {
            	Sequence sequencer = (Sequence)audio;
                seconds = sequencer.getMicrosecondLength() / 1000000.0;
            }
            catch (IllegalStateException e)
            {
                System.out.println("TEMP: IllegalStateException "+
                    "on sequencer.getMicrosecondPosition(): " + e);
            }
        }
        return seconds;
	}

}
