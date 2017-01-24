package pkgSlideShow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JOptionPane;

//===============================================================================================================
/**
* File: ImageThumbnail.java
* 
* This class defines one of the audio clips on the audio tab or in the show timeline.
* Created July 2016
* @author Rick Coleman
*/
//===============================================================================================================
public class AudioClip
{

	/** Name of this audio file */
	private String m_sFileName;
	
	/** Display name of this audio file */
	private String m_sDisplayName;
	
	/** Fill path to the project folder */
	private String m_sFullPathName;
	
	/** The Clip for this audio file */
	private Clip m_theClip;
	
	/** Flag if the clip is loaded and active */
	private boolean m_bClipActive;
	
	/** Clip length in microseconds */
	private long m_lMSecLen;
	
	/** Starting index of slides */
	private int m_iStartSlideIndex;
	
	/** Starting index fraction - 
	 	0 = full coverage, 25 = one quarter covered, 50 = half covered,
	 	75 = three quarters covered. */
	private int m_iStartIndexFraction;
	
	/** Ending index of slides */
	private int m_iEndSlideIndex;
	
	/** Ending index fraction - 
 		0 = full coverage, 25 = one quarter covered, 50 = half covered,
 		75 = three quarters covered. */
	private int m_iEndIndexFraction;
	
	//---------------------------------------------------------
	/** Default constructor */
	//---------------------------------------------------------
	public AudioClip(String fileName, String fullPath, long time)
	{
		m_sFileName = fileName;
		m_sFullPathName = fullPath;
		m_sDisplayName = fileName;
		m_theClip = null;
		m_lMSecLen = time;
		m_bClipActive = false;
		m_iStartSlideIndex = 0;
		m_iEndSlideIndex = 0;
		m_iStartIndexFraction = 0;
		m_iEndIndexFraction = 0;
		// FYI: We actually never have the fullPath after a save and reload, but
		//		no matter as we copy the audio file into the project folder.
	}
	
	//---------------------------------------------------------
	/** Initialize an AudioClip given a file object for it. */
	//---------------------------------------------------------
	public void initAudioClip()
	{
	   	 try
	   	 {
	   		 File aFile = new File(m_sFullPathName+"/"+m_sFileName);
	   		 if(!aFile.canRead()) throw new IOException();
	   		activateClip(aFile);
	   	 }
	     catch(IOException e1)
	     {
				String message = "Error: Unable to init audio clip " + m_sFileName;
				JOptionPane.showMessageDialog(null, 
						(Object)message, "Message",
						JOptionPane.ERROR_MESSAGE);
				return;
	     }

		
		// Open the clip so we can get the time
		
	}
	
	//---------------------------------------------------------
	/**  Set the file name */
	//---------------------------------------------------------
	public void setAudioFileName(String fName)
	{
		m_sFileName = fName;
	}

	//---------------------------------------------------------
	/**  Get the file name */
	//---------------------------------------------------------
	public String getAudioFileName()
	{
		return m_sFileName;
	}

	//---------------------------------------------------------
	/**  Get the file name */
	//---------------------------------------------------------
	public String getFullPathName()
	{
		return m_sFullPathName;
	}

	//---------------------------------------------------------
	/**  Set the file name */
	//---------------------------------------------------------
	public void setDisplayName(String fName)
	{
		m_sDisplayName = fName;
	}

	//---------------------------------------------------------
	/**  Get the file name */
	//---------------------------------------------------------
	public String getDisplayName()
	{
		return m_sDisplayName;
	}

	//---------------------------------------------------------
	/**  Get the file name for the list model */
	//---------------------------------------------------------
	public String toString()
	{
		return m_sDisplayName;
	}

	//---------------------------------------------------------
	/**  Set the clip length in microseconds */
	//---------------------------------------------------------
	public void setClipTimeLength(long time)
	{
		m_lMSecLen = time;
	}

	//---------------------------------------------------------
	/**  Get the clip length in microseconds */
	//---------------------------------------------------------
	public long getClipTimeLength()
	{
		return m_lMSecLen;
	}

	//---------------------------------------------------------
	/**  Is the clip loaded and active */
	//---------------------------------------------------------
	public boolean isClipActive()
	{
		return m_bClipActive;
	}

	//---------------------------------------------------------
	/** Load the clip */
	//---------------------------------------------------------
	public boolean activateClip(File audioFile)
	{
		Object m_CurrentSound = null;
       if (audioFile instanceof File) 
        {
            try 
            {
            	m_CurrentSound = AudioSystem.getAudioInputStream(audioFile);
            } 
            catch(Exception e1) 
            {
                m_CurrentSound = null;
                return false;
            }
        }

        if ((m_CurrentSound != null) && (m_CurrentSound instanceof AudioInputStream))
        {
          try 
           {
                AudioInputStream stream = (AudioInputStream) m_CurrentSound;
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
                DataLine.Info info = new DataLine.Info(
                                          Clip.class, 
                                          stream.getFormat(), 
                                          ((int) stream.getFrameLength() *
                                              format.getFrameSize()));

                m_theClip = (Clip) AudioSystem.getLine(info);
//                clip.addLineListener(this);
                m_theClip.open(stream);
//                m_CurrentSound = clip;
                m_lMSecLen = m_theClip.getMicrosecondLength();
            } 
           catch (Exception ex) 
           { 
        	   ex.printStackTrace(); 
        	   m_CurrentSound = null;
       		   m_bClipActive = false;
       		   m_theClip = null;
        	   return false;
           }
        } 
        else
        {
        	m_bClipActive = false;
    		m_theClip = null;
     	    return false;

        }
        	
		m_bClipActive = true;		
		return true;  
	}
	
	//---------------------------------------------------------
	/** Deactivate the clip */
	//---------------------------------------------------------
	public void deactivateClip()
	{
		if(this.m_bClipActive)
			m_theClip.close();
		m_bClipActive = false;
		m_theClip = null;
	}
	
	//---------------------------------------------------------
	/** Set the starting slide index. */
	//---------------------------------------------------------
	public void setStartSlideIndex(int idx)
	{
		m_iStartSlideIndex = idx;
	}
	
	//---------------------------------------------------------
	/** Get the starting slide index. */
	//---------------------------------------------------------
	public int getStartSlideIndex()
	{
		return m_iStartSlideIndex;
	}
	
	//---------------------------------------------------------
	/** Set the ending slide index. */
	//---------------------------------------------------------
	public void setEndSlideIndex(int idx)
	{
		m_iEndSlideIndex = idx;
	}
	
	//---------------------------------------------------------
	/** Get the ending slide index. */
	//---------------------------------------------------------
	public int getEndSlideIndex()
	{
		return m_iEndSlideIndex;
	}
	
	//---------------------------------------------------------
	/** Set the starting slide index fraction. */
	//---------------------------------------------------------
	public void setStartSlideIndexFraction(int idx)
	{
		m_iStartIndexFraction = idx;
	}
	
	//---------------------------------------------------------
	/** Get the starting slide index fraction. */
	//---------------------------------------------------------
	public int getStartSlideIndexFraction()
	{
		return m_iStartIndexFraction;
	}
	
	//---------------------------------------------------------
	/** Set the ending slide index fraction. */
	//---------------------------------------------------------
	public void setEndSlideIndexFraction(int idx)
	{
		m_iEndIndexFraction = idx;
	}
	
	//---------------------------------------------------------
	/** Get the ending slide index fraction. */
	//---------------------------------------------------------
	public int getEndSlideIndexFraction()
	{
		return m_iEndIndexFraction;
	}

}
