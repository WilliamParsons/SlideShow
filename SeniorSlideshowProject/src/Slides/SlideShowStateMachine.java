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
	private boolean audioLoopFlag;
	private boolean isPaused;
	private boolean needsReset;
	private boolean showEnded;

	private SlideShowStateMachine()													//Constructor for SlideShowStateMachine
	{
		audioList = new ArrayList<AudioState>();									//New array for audio
		slideList = new ArrayList<SlideState>();									//New array for slides
		audioIndex = 0;																//Set audio index to 0
		slideIndex = 0;																//Set slide index to 0
		showTime = 0;																//Set show time to 0
		audioLoopFlag = false;														//Set loop flag to false
		needsReset = false;															//Set needs reset to false
		isPaused = false;															//Set is paused to false
		showEnded = false;															//Set show ended to false
	}

	public static SlideShowStateMachine getInstance()								//Function to get a singleton of SlideShowStateMachine
	{
		return singleton;
	}

	private void setSlideTransitionTimes()											//Function to set the slide transition times
	{
		double slideTime = showTime/slideList.size();								//Slide time is equal to showtime divided by slideList size
		for (SlideState x : slideList)												//For each slide in the slide list
		{
			x.setTransitionTime(slideTime);											//Set the transition time for the slide state
		}
	}
	
	public void setPausedState(boolean state){										//Function to set paused state
		
		isPaused = state;															//Set to is paused to passed-in state
	}

	public void addSlide(SlideState slide)											//Function to add a slide to the slide list
	{
		slideList.add(slide);														//Add passed-in slide to the slide list
		setSlideTransitionTimes();													//Call setSlideTransitionTimes();
	}
	
	public void setCurrentIndex (int newIndex)										//Function to set the current slide show index
	{
		slideIndex = newIndex;														//Set slide index to passed-in index
	}

	public void decrementIndex()													//Function to decrease the slide index
	{
		
		slideIndex--;																//Decrement the value of the slide index
		
	}
	
	public void incrementIndex()													//Function to increase the slide index
	{
		
		slideIndex++;																//Increment the value of the slide index
		
	}
	public void setNeedsReset(boolean reset){										//Function to set the needs reset value
		needsReset = reset;															//Set the needsreset value to passed-in boolean value
	}
	
	public void setShowEnded(boolean showState){									//Function to set the show ended value
		showEnded = showState;														//Set the showEnded value to passed-in boolean value
	}
	
	public boolean getShowEnded(){													//Function to return show ended value
		return showEnded;															//Return showEnded value
	}
	
	public boolean getNeedsReset(){													//Function to return needs reset value
		return needsReset;															//Return needsReset value
	}
	
	public SlideState getFirstSlide()												//Function to get the first slide of the array
	{
		slideIndex = 0;																//Set slideIndex to 0
		try {
			return slideList.get(slideIndex);										//Return the slideIndex of the slide list
		} catch (IndexOutOfBoundsException e) {										
			return null;															//Return null if out of bounds
		}

	}
	public int getCurrentIndex()													//Function to return the current slide index
	{		
		return slideIndex;															//Return the value of slideIndex
	}

	public SlideState getCurrentSlide()												//Function to get the current slide of the slide list
	{
		if(getSlideShowSize() > 0) {												//If the slide show size is greater than 0, do...
			return slideList.get(slideIndex);										//Return the current slideIndex of the slide list
		} else {
			return null;															//Return
		}
	}

	public SlideState getNextSlide()												//Function to get the next slide of the slide list
	{
		if(slideIndex + 1 < slideList.size())										//If the slideIndex+1 is less than the slide list size
		{
			slideIndex++;															//Increment the slideIndex
			System.out.println(slideIndex);											
			return slideList.get(slideIndex);										//Return the current index of the slide list
		}
		else
		{
			return null;															//Return null
		}
	}

	public SlideState getSlideAtIndex(int i) {										//Function to return slide at certain index
		if (i >= 0 && i < slideList.size())											//If index is greater than or equal to 0 AND is less than slide list size, do...
		{
			return slideList.get(i);												//Return index
		}
		else
		{
			return null;															//Return null
		}
	}

	public SlideState getPreviousSlide()											//Function to get the previous slide of the slide list
	{
		if(slideIndex > 0)															//If slideIndex is greater than 0, do...
		{
			slideIndex--;															//Decrement slideIndex
			return slideList.get(slideIndex);										//Return the current index of the slide list
		}
		else
		{
			return null;															//Return null
		}

	}
	
	public boolean getPausedState()													//Function to return getPausedState state
	{
		return isPaused;															//Return isPaused
	}

	public void removeSlideAtIndex(int i)											//Function to remove slide at index
	{
		try
		{
			slideList.remove(i);													//Remove at index

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

	public int getSlideShowSize()
	{
		return slideList.size();
	}

	public int getAudioListSize()
	{
		return audioList.size();
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
		try {
			return audioList.get(audioIndex);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

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

	public int getNextAudioIndex()
	{
		if(audioIndex + 1 < audioList.size())
		{
			return audioIndex++;
			// return audioList.get(audioIndex);
		}
		else
		{
			if(audioLoopFlag == true)
			{
				audioIndex = 0;
			}
			return audioIndex;
		}
	}
	
	public int getPreviousAudioIndex()
	{
		if(audioIndex > 0)
		{
			return audioIndex--;
			// return audioList.get(audioIndex);
		}
		else
		{
			return audioIndex;
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

	public AudioState getAudioAtIndex(int i)
	{
		try
		{
			return audioList.get(i);
		}
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("Exception thrown : " + e);
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
	
	public int getAudioIndex(){
		return audioIndex;
	}

	public void clearSlideShow()
	{
		audioList.clear();
		slideList.clear();
	}

	public void removeSelectedAudios(Vector tmp) {
		// TODO Auto-generated method stub
		audioList.removeAll(tmp);
	}

	public void removeAllAudios() {
		// TODO Auto-generated method stub
		audioList.clear();
	}

	public void setAudioLoopFlag() {
		// TODO Auto-generated method stub
		audioLoopFlag = !audioLoopFlag;
	}

}
