import Slides.*;
import FileManager.FileManager;

import Slides.SlideState.Transition;

public class Test{
	
	public static SlideShowStateMachine stateMachine = SlideShowStateMachine.getInstance();
	
	public static void main(String []args)
	{
		SetUp();
		FileManager mgr = new FileManager();
		mgr.writeFile(stateMachine, "test.ssp");
		stateMachine = null;
		stateMachine = mgr.readFile("test.ssp");
		
		System.out.println("*****************************");
		System.out.println("***Testing_Slide_Iteration***");
		System.out.println("*****************************");
		TestSlideIteration();
		TearDown();
		
		
		SetUp();
		System.out.println("*****************************");
		System.out.println("***Testing_Audio_Iteration***");
		System.out.println("*****************************");
		TestAudioIteration();
		TearDown();

	}
	
	public static void SetUp()
	{
		SlideState slide1 = new SlideState("Test1.JPEG", Transition.UP);
		SlideState slide2 = new SlideState("Test2.JPEG", Transition.DOWN);
		SlideState slide3 = new SlideState("Test3.JPEG", Transition.LEFT);
		SlideState slide4 = new SlideState("Test4.JPEG", Transition.RIGHT);
		SlideState slide5 = new SlideState("Test5.JPEG", Transition.CROSSFADE);
		
		stateMachine.addSlide(slide1);		
		stateMachine.addSlide(slide2);		
		stateMachine.addSlide(slide3);
		stateMachine.addSlide(slide4);
		stateMachine.addSlide(slide5);
		
		AudioState audio1 = new AudioState("Test1.WAV", 60.00);
		AudioState audio2 = new AudioState("Test2.WAV", 40.00);
		AudioState audio3 = new AudioState("Test3.WAV", 20.00);
		AudioState audio4 = new AudioState("Test4.WAV", 10.00);
		AudioState audio5 = new AudioState("Test5.WAV", 5.00);
		
		stateMachine.addAudio(audio1);
		stateMachine.addAudio(audio2);
		stateMachine.addAudio(audio3);
		stateMachine.addAudio(audio4);
		stateMachine.addAudio(audio5);
		
	}
	
	public static void TearDown()
	{
		stateMachine.clearSlideShow();
	}
	
	public static void TestSlideIteration()
	{
		System.out.println("___Testing_Forward_Iteration___");
		SlideState state = stateMachine.getFirstSlide();
		while(state != null)
		{
		System.out.println("Slide name: " + state.getFileName());
		System.out.println("Transition type: " + state.getTransition());
		System.out.println("Slide transition time: " + state.getTransitionTime());
		state = stateMachine.getNextSlide();
		}
		
		System.out.println("___Testing_Reverse_Iteration___");
		state = stateMachine.getCurrentSlide();
		while(state != null)
		{
			System.out.println("Slide name: " + state.getFileName());
			System.out.println("Transition type: " + state.getTransition());
			System.out.println("Slide transition time: " + state.getTransitionTime());
			state = stateMachine.getPreviousSlide();
			
		}		
		
		System.out.println("___Testing_Forward_Iteration_After_Deletion_By_Name___");
		stateMachine.removeSlideWithFileName("Test1.JPEG");
		state = stateMachine.getFirstSlide();
		while(state != null)
		{
		System.out.println("Slide name: " + state.getFileName());
		System.out.println("Transition type: " + state.getTransition());
		System.out.println("Slide transition time: " + state.getTransitionTime());
		state = stateMachine.getNextSlide();
		}
		
		System.out.println("___Testing_Reverse_Iteration_Afer_Deletion_By_Name___");
		stateMachine.removeSlideWithFileName("Test5.JPEG");
		state = stateMachine.getCurrentSlide();
		while(state != null)
		{
			System.out.println("Slide name: " + state.getFileName());
			System.out.println("Transition type: " + state.getTransition());
			System.out.println("Slide transition time: " + state.getTransitionTime());
			state = stateMachine.getPreviousSlide();
			
		}
		
		System.out.println("___Testing_Forward_Iteration_After_Deletion_By_Index___");
		stateMachine.removeSlideAtIndex(1);
		state = stateMachine.getFirstSlide();
		while(state != null)
		{
		System.out.println("Slide name: " + state.getFileName());
		System.out.println("Transition type: " + state.getTransition());
		System.out.println("Slide transition time: " + state.getTransitionTime());
		state = stateMachine.getNextSlide();
		}
		
		System.out.println("___Testing_Reverse_Iteration_Afer_Deletion_By_Index___");
		stateMachine.removeSlideAtIndex(1);
		state = stateMachine.getCurrentSlide();
		while(state != null)
		{
			System.out.println("Slide name: " + state.getFileName());
			System.out.println("Transition type: " + state.getTransition());
			System.out.println("Slide transition time: " + state.getTransitionTime());
			state = stateMachine.getPreviousSlide();
			
		}		
	}
	
	
	public static void TestAudioIteration()
	{
		System.out.println("___Testing_Forward_Iteration___");
		AudioState state = stateMachine.getFirstAudio();
		while(state != null)
		{
		System.out.println("Audio name: " + state.getFileName());
		System.out.println("Audio transition time: " + state.getAudioTime());
		state = stateMachine.getNextAudio();
		}
		
		System.out.println("___Testing_Reverse_Iteration___");
		state = stateMachine.getCurrentAudio();
		while(state != null)
		{
			System.out.println("Audio name: " + state.getFileName());
			System.out.println("Audio transition time: " + state.getAudioTime());
			state = stateMachine.getPreviousAudio();
			
		}		
		
		System.out.println("___Testing_Forward_Iteration_After_Deletion_By_Name___");
		stateMachine.removeAudioWithFileName("Test1.WAV");
		state = stateMachine.getFirstAudio();
		while(state != null)
		{
			System.out.println("Audio name: " + state.getFileName());
			System.out.println("Audio transition time: " + state.getAudioTime());
		state = stateMachine.getNextAudio();
		}
		
		System.out.println("___Testing_Reverse_Iteration_Afer_Deletion_By_Name___");
		stateMachine.removeAudioWithFileName("Test5.WAV");
		state = stateMachine.getCurrentAudio();
		while(state != null)
		{
			System.out.println("Audio name: " + state.getFileName());
			System.out.println("Audio transition time: " + state.getAudioTime());
			state = stateMachine.getPreviousAudio();
			
		}
		
		System.out.println("___Testing_Forward_Iteration_After_Deletion_By_Index___");
		stateMachine.removeAudioAtIndex(1);
		state = stateMachine.getFirstAudio();
		while(state != null)
		{
			System.out.println("Audio name: " + state.getFileName());
			System.out.println("Audio transition time: " + state.getAudioTime());
		state = stateMachine.getNextAudio();
		}
		
		System.out.println("___Testing_Reverse_Iteration_Afer_Deletion_By_Index___");
		stateMachine.removeAudioAtIndex(1);
		state = stateMachine.getCurrentAudio();
		while(state != null)
		{
			System.out.println("Audio name: " + state.getFileName());
			System.out.println("Audio transition time: " + state.getAudioTime());
			state = stateMachine.getPreviousAudio();
			
		}		
	}
}