import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Slides.SlideShowStateMachine;
import Slides.SlideState;
import Transitions.CrossFade;
import Transitions.SwipeDown;
import Transitions.SwipeLeft;
import Transitions.SwipeRight;
import Transitions.SwipeUp;
import Transitions.Transition;
import pkgImageTransitions.ImagePanel;

public class Animator extends Thread {

	//panel to draw the animations into
	private ImagePanel imgPan;
	//instance of the slideshowstateMachien
	private SlideShowStateMachine slideStateMachine;
	//slideStates representing the current and next slides
	private SlideState currentSlide, nextSlide;
	//private SlideshowMaker creator;
	
	//animator constructor
	public Animator (ImagePanel imgPan) {
		this.imgPan = imgPan;
		slideStateMachine = SlideShowStateMachine.getInstance();
		currentSlide = slideStateMachine.getFirstSlide();
		nextSlide = slideStateMachine.getNextSlide();
	}
	//function run when thread start is called
	@Override
	public void run() {

		SlideshowPresenter presenter = SlideshowPresenter.getInstance();
		presenter.initializeShow();
		//while there is another slide to transition to continue the thread
		while (nextSlide != null){
			//if the current slide or the next slide is null dont do anything otherwise continue the loop
			if(currentSlide != null && nextSlide != null) {
				
				double animationTime;
				double slideTime = currentSlide.getTransitionTime();
				//do math to determine the needed animation time
				if (slideTime - 1 > 0) {
					slideTime = slideTime--;
					animationTime = 1;
				}
				else
				{
					animationTime = slideTime;
					slideTime = 0;
				}

				try {
					Thread.sleep((long)slideTime*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				BufferedImage currentImg = new BufferedImage(
						imgPan.getWidth(),
						imgPan.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g1 = currentImg.createGraphics();
				g1.drawImage(currentSlide.getIcon().getImage(), 0, 0, imgPan.getWidth(), imgPan.getHeight(), null);

				BufferedImage nextImg = new BufferedImage(
						imgPan.getWidth(),
						imgPan.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g2 = nextImg.createGraphics();
				g2.drawImage(nextSlide.getIcon().getImage(), 0, 0, imgPan.getWidth(), imgPan.getHeight(), null);
				
				//find out what transition is set and then perform the approprate transition 
				if (nextSlide.getTransition() == SlideState.Transition.NONE)
				{
					Transition transition = new SwipeDown();
					transition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
				}
				else if (nextSlide.getTransition() == SlideState.Transition.DOWN)
				{
					SwipeDown swipeDownTransition = new SwipeDown();
					swipeDownTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
				}
				else if (nextSlide.getTransition() == SlideState.Transition.UP)
				{
					SwipeUp swipeUpTransition = new SwipeUp();
					swipeUpTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
				}
				else if (nextSlide.getTransition() == SlideState.Transition.LEFT)
				{
					SwipeLeft swipeLeftTransition = new SwipeLeft();
					swipeLeftTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
				}
				else if (nextSlide.getTransition() == SlideState.Transition.RIGHT)
				{
					SwipeRight swipeRightTransition = new SwipeRight();
					swipeRightTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
				}
				else if (nextSlide.getTransition() == SlideState.Transition.CROSSFADE)
				{
					CrossFade crossFadeTransition = new CrossFade();
					crossFadeTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
				}
				else
				{
					System.out.println("error with transition type");
				}	
				//if the slidemachine is not paused set the index to the next in the slideshow
				if(!slideStateMachine.getPausedState()){
					
					currentSlide = nextSlide;
					nextSlide = slideStateMachine.getNextSlide();
					if (nextSlide == null) {
						try {
							Thread.sleep((long)currentSlide.getTransitionTime()*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			
		}
		slideStateMachine.setShowEnded(true);
		presenter.resetPlayButton();
		 
		
	}
	//function to initialize the slides if switched between manual and automatic modes
	public void reintializeSlides(){

		currentSlide = slideStateMachine.getFirstSlide();
		nextSlide = slideStateMachine.getNextSlide();
	}
	

}
