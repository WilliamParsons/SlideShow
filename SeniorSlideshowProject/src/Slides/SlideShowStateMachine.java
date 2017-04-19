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
	private int displayIndex;

	private SlideShowStateMachine()
	{
		audioList = new ArrayList<AudioState>();
		slideList = new ArrayList<SlideState>();
		audioIndex = 0;
		slideIndex = 0;
		showTime = 0;
		audioLoopFlag = false;
		needsReset = false;
		isPaused = false;
		showEnded = false;
		displayIndex = 0;
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
	
	public void setPausedState(boolean state){
		
		isPaused = state;
	}

	public void addSlide(SlideState slide)
	{
		slideList.add(slide);
		setSlideTransitionTimes();
	}
	
	public void setCurrentIndex (int newIndex)
	{
		slideIndex = newIndex;
	}

	public void decrementIndex()
	{
		
		slideIndex--;
		
	}
	
	public void incrementIndex()
	{
		
		slideIndex++;
		
	}
	public void setNeedsReset(boolean reset){
		needsReset = reset;
	}
	
	public void setShowEnded(boolean showState){
		showEnded = showState;
	}
	
	public boolean getShowEnded(){
		return showEnded;
	}
	
	public boolean getNeedsReset(){
		return needsReset;
	}
	
	public SlideState getFirstSlide()
	{
		slideIndex = 0;
		try {
			return slideList.get(slideIndex);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

	}
	public int getCurrentIndex()
	{
		return slideIndex;
	}
	public int getDisplayIndex(){
		return displayIndex;
	}
	
	public void incrementDisplayIndex(){
		if (displayIndex < (slideList.size() - 1)){
			displayIndex++;
		}
	}
	
	public void decrementDisplayIndex(){
		if (displayIndex > 0){
			displayIndex--;
		}
	}
	
	public void resetDisplayIndex(){
		displayIndex = 0;
	}
	public SlideState getCurrentSlide()
	{
		if(getSlideShowSize() > 0) {
			return slideList.get(slideIndex);
		} else {
			return null;
		}
	}

	public SlideState getNextSlide()
	{
		if(slideIndex + 1 < slideList.size())
		{
			slideIndex++;
			System.out.println(slideIndex);
			return slideList.get(slideIndex);
		}
		else
		{
			return null;
		}
	}

	public SlideState getSlideAtIndex(int i) {
		if (i >= 0 && i < slideList.size())
		{
			return slideList.get(i);
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
	
	public boolean getPausedState()
	{
		return isPaused;
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
//		audioIndex++;
		if(audioIndex < audioList.size())
		{
			audioIndex++;
		}
//		else
//		{
//			if(audioLoopFlag == true)
//			{
//				audioIndex = 0;
//			}
//		}
		return audioIndex;
	}
	
	public int getPreviousAudioIndex()
	{
		if(audioIndex > 0)
		{
			audioIndex--;
			// return audioList.get(audioIndex);
		}
		return audioIndex;
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
		showTime = 0;
		audioList.clear();
		slideList.clear();
	}

	public void removeSelectedAudios(Vector <AudioState> tmp) {
		// TODO Auto-generated method stub
		for(int i = 0;i<tmp.size(); i++) {
			showTime -= tmp.get(i).getAudioTime();
		}
		audioList.removeAll(tmp);
	}

	public void removeAllAudios() {
		// TODO Auto-generated method stub
		audioList.clear();
		showTime = 0;
	}

	public boolean AudioLoopSwitch() {
		// TODO Auto-generated method stub
		audioLoopFlag = !audioLoopFlag;
		return audioLoopFlag;
	}

	public double getTotalTime() {
		// TODO Auto-generated method stub
		return showTime;
	}

}
