package Slides;
import java.util.*;
import java.io.Serializable;

public class SlideShowStateMachine implements Serializable {
	
	/* This is the state machine for the program.
	 * All audio and image file locations, their sequence,
	 * and the types of transitions will be stored here.
	 */
	
	private static SlideShowStateMachine singleton = new SlideShowStateMachine();
	private static final long serialVersionUID = 1L;
	
	private ArrayList<AudioState> audioList;
	private ArrayList<SlideState> slideList;
	private int audioIndex;
	private int slideIndex;
	private double showTime;
	
	private SlideShowStateMachine()
	{
		audioList = new ArrayList<AudioState>();
		slideList = new ArrayList<SlideState>();
		audioIndex = 0;
		slideIndex = 0;
		showTime = 0;
	}
	
	public static SlideShowStateMachine getInstance()
	{
		return singleton;
	}
	
	private void setSlideTransitionTimes()
	{
		double slideTime = showTime/slideList.size();
		for (SlideState x : slideList)
		{		
			x.setTransitionTime(slideTime);
		}		
	}
	
	public void addSlide(SlideState slide)
	{
		slideList.add(slide);
		setSlideTransitionTimes();
	}
	
	public SlideState getFirstSlide()
	{
		slideIndex = 0;
		return slideList.get(slideIndex);
	}
	
	public SlideState getCurrentSlide()
	{
			return slideList.get(slideIndex);
	}		
	
	public SlideState getNextSlide()
	{
		if(slideIndex + 1 < slideList.size())
		{
			slideIndex++;
			return slideList.get(slideIndex);
		}
		else
		{
			return null;
		}
	}
	
	public SlideState getPreviousSlide()
	{
		if(slideIndex > 0)
		{
			slideIndex--;
			return slideList.get(slideIndex);
		}
		else
		{
			return null;
		}
		
	}
	
	public void removeSlideAtIndex(int i)
	{
		try 
		{
			slideList.remove(i);
			
			if(slideIndex - 1 >= 0)
			{
				slideIndex--;
			}
			else
			{
				slideIndex = 0;
			}			
			setSlideTransitionTimes();
		} 
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("Exception thrown : " + e);
		}
	}
	
	public void removeSlideWithFileName(String name)
	{
		for (int i = 0; i < slideList.size(); i++)
		{
			SlideState slide = slideList.get(i);
			if(slide.getFileName().equals(name))
			{
				slideList.remove(slide);
				
				if(slideIndex - 1 >= 0)
				{
					slideIndex--;
				}
				else
				{
					slideIndex = 0;
				}
				setSlideTransitionTimes();
				break;
			}
		}
	}
	
	public void removeSlide(SlideState state)
	{
		if(slideList.remove(state))
		{
			if(slideIndex - 1 >= 0)
			{
				slideIndex--;
			}
			else
			{
				slideIndex = 0;
			}	
			setSlideTransitionTimes();
		}
	}
	
	public void addAudio(AudioState audio)
	{
		showTime += audio.getAudioTime();		
		audioList.add(audio);
		
		setSlideTransitionTimes();
	}
	
	public AudioState getFirstAudio()
	{
		audioIndex = 0;
		return audioList.get(audioIndex);
	}
	
	public AudioState getCurrentAudio()
	{
		return audioList.get(audioIndex);
	}
	
	public AudioState getNextAudio()
	{
		if(audioIndex + 1 < audioList.size())
		{
			audioIndex++;
			return audioList.get(audioIndex);
		}
		else
		{
			return null;
		}
	}
	
	public AudioState getPreviousAudio()
	{
		if(audioIndex > 0)
		{
			audioIndex--;
			return audioList.get(audioIndex);
		}
		else
		{
			return null;
		}
		
	}
	
	public void removeAudioAtIndex(int i)
	{
		try
		{
			showTime -= audioList.get(i).getAudioTime();
			audioList.remove(i);
			
			if(audioIndex - 1 >= 0)
			{
				audioIndex--;
			}
			else
			{
				audioIndex = 0;
			}
			setSlideTransitionTimes();
		} 
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("Exception thrown : " + e);
		}
	}
	
	public void removeAudioWithFileName(String name)
	{
		for (int i = 0; i < audioList.size(); i++)
		{
			AudioState audio = audioList.get(i);
			if(audio.getFileName().equals(name))
			{
				showTime -= audio.getAudioTime();
				audioList.remove(audio);
				
				if(audioIndex - 1 >= 0)
				{
					audioIndex--;
				}
				else
				{
					audioIndex = 0;
				}
				setSlideTransitionTimes();
			}
		}
	}
	
	public void removeAudio(AudioState state)
	{
		if(audioList.contains(state))
		{
			showTime -= state.getAudioTime();			
			audioList.remove(state);
			
			if(audioIndex - 1 >= 0)
			{
				audioIndex--;
			}
			else
			{
				audioIndex = 0;
			}
			setSlideTransitionTimes();
			
		}
		
	}
	
	public void clearSlideShow()
	{
		audioList.clear();
		slideList.clear();
	}
	
	
}