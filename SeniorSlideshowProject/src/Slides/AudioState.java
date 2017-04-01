package Slides;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
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
		Object currentSound = null;
		try
		{
			currentSound = AudioSystem.getAudioInputStream((File) audio);

		}
		catch(Exception e1)
		{
//                load midi & rmf as inputstreams for now
			try {
				currentSound = MidiSystem.getSequence((File) audio);
			} catch (Exception e2) {
				try
				{
					FileInputStream is = new FileInputStream((File) audio);
					currentSound = new BufferedInputStream(is, 1024);
				}
				catch (Exception e3)
				{
					currentSound = null;
				}
			}
		}
		double seconds = 0.0;
		if (currentSound instanceof AudioInputStream)
        {
           try
           {
                // rentAudio = new audioStateMachine((AudioInputStream)currentSound, currentName);
                // ioStateMachine.addAudio(currentAudio);
                AudioInputStream stream = (AudioInputStream) currentSound;
                AudioFormat format = stream.getFormat();

                /**
                 * we can't yet open the device for ALAW/ULAW playback,
                 * convert ALAW/ULAW to PCM
                 */
                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
                    (format.getEncoding() == AudioFormat.Encoding.ALAW))
                {
                    AudioFormat tmp = new AudioFormat(
                                              AudioFormat.Encoding.PCM_SIGNED,
                                              format.getSampleRate(),
                                              format.getSampleSizeInBits() * 2,
                                              format.getChannels(),
                                              format.getFrameSize() * 2,
                                              format.getFrameRate(),
                                              true);
                    stream = AudioSystem.getAudioInputStream(tmp, stream);
                    format = tmp;
                }
                seconds = stream.getFrameLength() / format.getFrameRate();
            }
           catch (Exception ex)
           {
        	   currentSound = null;
           }
        }
        else if (currentSound instanceof Clip)
        {
            Clip clip = (Clip) currentSound;
            seconds = clip.getFramePosition() / clip.getFormat().getFrameRate();
        }
        else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) )
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
