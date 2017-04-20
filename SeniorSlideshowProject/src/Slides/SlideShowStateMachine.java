package Slides;
import java.util.*;

import java.io.Serializable;

public class SlideShowStateMachine implements Serializable {

	/* This is the state machine for the program.
	 * All audio and image file locations, their sequence,
	 * and the types of transitions will be stored here.
	 */

	//singleton instance of the slideShowStataeMachine
	private static SlideShowStateMachine singleton = new SlideShowStateMachine();
	private static final long serialVersionUID = 1L;

	//array list for the audio files
	private ArrayList<AudioState> audioList;
	//array list for the slide files
	private ArrayList<SlideState> slideList;
	//integer representing the audio's current index in the array
	private int audioIndex;
	//integer representing the slideshow's current index in the array
	private int slideIndex;
	//double representing the total time for the slideshow
	private double showTime;
	//boolean representing the whether the audio needs to loop or not
	private boolean audioLoopFlag;
	//boolean to see if the slideshow is paused
	private boolean isPaused;
	//boolean to see if the slideshow index needs reset
	private boolean needsReset;
	//boolean representing whether or not the slide show has ended
	private boolean showEnded;
	//integer representing what image should be shown in main mode
	private int displayIndex;

	//function to initialize the slideshowstatemachine variables
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

	//function to return the instance of the slideshowstatemachine singleton
	public static SlideShowStateMachine getInstance()
	{
		return singleton;
	}
	
	public double getSlideTime(){
		return slideList.get(0).getTransitionTime();
	}
	
	public void printTransitionTime(){
		for (SlideState x : slideList)
		{
			System.out.println(x.getTransitionTime());
		}
	}
	
	public void setSoundtracklessTransition(){
		double slideTime = 20/slideList.size();
		for (SlideState x : slideList)
		{
			x.setTransitionTime(slideTime);
		}
	}

	//function to set the slideshow's transition times
	private void setSlideTransitionTimes()
	{
		double slideTime = showTime/slideList.size();
		for (SlideState x : slideList)
		{
			x.setTransitionTime(slideTime);
		}
	}
	
	//function to set the state machine's slide state
	public void setPausedState(boolean state){
		
		isPaused = state;
	}

	//function to add a slide to the state machine
	public void addSlide(SlideState slide)
	{
		slideList.add(slide);
		setSlideTransitionTimes();
	}
	//function to set the index used for the animator thread
	public void setCurrentIndex (int newIndex)
	{
		slideIndex = newIndex;
	}

	//function to decrement the index for the animator thread
	public void decrementIndex()
	{
		
		slideIndex--;
		
	}
	//function to increment the index for the animator thread
	public void incrementIndex()
	{
		
		slideIndex++;
		
	}
	//function to set need reset boolean
	public void setNeedsReset(boolean reset){
		needsReset = reset;
	}
	//function to set the show ended boolean
	public void setShowEnded(boolean showState){
		showEnded = showState;
	}
	//function to get the show ended boolean
	public boolean getShowEnded(){
		return showEnded;
	}
	//function to get the needs reset boolean
	public boolean getNeedsReset(){
		return needsReset;
	}
	//function to get the first slide 
	public SlideState getFirstSlide()
	{
		slideIndex = 0;
		try {
			return slideList.get(slideIndex);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

	}
	//function to get the current index for the animator index
	public int getCurrentIndex()
	{
		return slideIndex;
	}
	//function to get the display index for the main thread
	public int getDisplayIndex(){
		return displayIndex;
	}
	//function to increment the display index for the main thread
	public void incrementDisplayIndex(){
		//only increment the thread display index within the range total slides
		if (displayIndex < (slideList.size() - 1)){
			displayIndex++;
		}
	}
	//function to decrement the main thread index
	public void decrementDisplayIndex(){
		//only decrement within the slideshow indexes
		if (displayIndex > 0){
			displayIndex--;
		}
	}
	//function to reset the display index to zero
	public void resetDisplayIndex(){
		displayIndex = 0;
	}
	//function to get the current slide and all data contained within
	public SlideState getCurrentSlide()
	{
		//if the slideshows is populated then return the slide otherwise return null
		if(getSlideShowSize() > 0) {
			return slideList.get(slideIndex);
		} else {
			return null;
		}
	}
	
    //function to get the next slide
	public SlideState getNextSlide()
	{
		//if the slide's index is not greater than the size of the total slides then increment the animator index and return the slide otherwise return null
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

	//function to get the slide at the indexed slidestate
	public SlideState getSlideAtIndex(int i) {
		//if the index is within the range of total slides return the slide at that index otherwise return null
		if (i >= 0 && i < slideList.size())
		{
			return slideList.get(i);
		}
		else
		{
			return null;
		}
	}

	//function to get the previous slide
	public SlideState getPreviousSlide()
	{
		//if the slide index is within the slide state decrement and return it otherwise return null
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
	
	//return the value of the paused state
	public boolean getPausedState()
	{
		return isPaused;
	}

	//function to remove a slide at the given index
	public void removeSlideAtIndex(int i)
	{
		//remove it if it exists then move the variables to note the deletion of the slide
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

	//function to remove a slide at the current index
	public void removeSlide(SlideState state)
	{
		//remove the slide if it exists
		if(slideList.remove(state))
		{
			//if the index >= 0 decrement it otherwsie set it to 0
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

	//function to get the size of the slideshow
	public int getSlideShowSize()
	{
		return slideList.size();
	}

	//function to get the size of the playlist
	public int getAudioListSize()
	{
		return audioList.size();
	}

	//function to add an audio into soundtrack
	public void addAudio(AudioState audio)
	{
		showTime += audio.getAudioTime();
		audioList.add(audio);

		setSlideTransitionTimes();
	}

	//get the first song in the soundtrack
	public AudioState getFirstAudio()
	{
		audioIndex = 0;
		//return the first song in the soundtrack if it exists otherwise return null
		try {
			return audioList.get(audioIndex);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

	}

	//return the current song
	public AudioState getCurrentAudio()
	{
		return audioList.get(audioIndex);
	}

	//return the next song
	public AudioState getNextAudio()
	{
		// if the song index is within then the size of the soundtrack increment it otherwise return nothing
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

	//get the next song's index
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
	
	//get the previous song's index
	public int getPreviousAudioIndex()
	{
		if(audioIndex > 0)
		{
			audioIndex--;
			// return audioList.get(audioIndex);
		}
		return audioIndex;
	}
	//get the previous audio track
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

	//function to return the song at the given index i
	public AudioState getAudioAtIndex(int i)
	{
		try
		{
			return audioList.get(i);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	//function to remove a song at the index i
	public void removeAudioAtIndex(int i)
	{
		//try to remove it; if you can reset the audio data to adjust for the removed file 
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

	//remoce the audio file with a given name
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

	//remove the audio file at the given index
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
	
	//return the audio index
	public int getAudioIndex(){
		return audioIndex;
	}

	//completely clear the data in the slideshow
	public void clearSlideShow()
	{
		showTime = 0;
		audioList.clear();
		slideList.clear();
	}

	//removemultiple audio files at once
	public void removeSelectedAudios(Vector <AudioState> tmp) {
		// TODO Auto-generated method stub
		for(int i = 0;i<tmp.size(); i++) {
			showTime -= tmp.get(i).getAudioTime();
		}
		audioList.removeAll(tmp);
	}
	
	//delete all the audio files
	public void removeAllAudios() {
		// TODO Auto-generated method stub
		audioList.clear();
		showTime = 0;
	}

	//set the boolean to its opposite
	public boolean AudioLoopSwitch() {
		// TODO Auto-generated method stub
		audioLoopFlag = !audioLoopFlag;
		return audioLoopFlag;
	}

	//get the total soundtrack time
	public double getTotalTime() {
		// TODO Auto-generated method stub
		return showTime;
	}

}
