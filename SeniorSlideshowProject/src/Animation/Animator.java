package Animation;

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

	private ImagePanel imgPan;
	private SlideShowStateMachine slideStateMachine;
	private SlideState currentSlide, nextSlide;

	public Animator (ImagePanel imgPan) {
		this.imgPan = imgPan;
		slideStateMachine = SlideShowStateMachine.getInstance();
		currentSlide = slideStateMachine.getFirstSlide();
		nextSlide = slideStateMachine.getNextSlide();
		SlideState slideResetter = slideStateMachine.getPreviousSlide();
	}
	@Override
	public void run() {
		
		

		boolean isPaused = slideStateMachine.getPausedState();
		
		if(currentSlide != null && nextSlide != null && !isPaused) {

			double animationTime;
			double slideTime = currentSlide.getTransitionTime();
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

			if (nextSlide.getTransition() == SlideState.Transition.NONE && !isPaused)
			{
				Transition transition = new SwipeDown();
				transition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
			}
			else if (nextSlide.getTransition() == SlideState.Transition.DOWN && !isPaused)
			{
				SwipeDown swipeDownTransition = new SwipeDown();
				swipeDownTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
			}
			else if (nextSlide.getTransition() == SlideState.Transition.UP && !isPaused)
			{
				SwipeUp swipeUpTransition = new SwipeUp();
				swipeUpTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
			}
			else if (nextSlide.getTransition() == SlideState.Transition.LEFT && !isPaused)
			{
				SwipeLeft swipeLeftTransition = new SwipeLeft();
				swipeLeftTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
			}
			else if (nextSlide.getTransition() == SlideState.Transition.RIGHT && !isPaused)
			{
				SwipeRight swipeRightTransition = new SwipeRight();
				swipeRightTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
			}
			else if (nextSlide.getTransition() == SlideState.Transition.CROSSFADE && !isPaused)
			{
				CrossFade crossFadeTransition = new CrossFade();
				crossFadeTransition.DrawImageTransition(imgPan, currentImg, nextImg, animationTime);
			}
			else
			{
				System.out.println("error with transition type");
			}	
			if(!isPaused){
				System.out.print("current index is" + slideStateMachine.getCurrentIndex() + "\n");
				currentSlide = nextSlide;
				nextSlide = slideStateMachine.getNextSlide();
				imgPan.setImage(currentSlide.getIcon().getImage());
			}
			if (isPaused){
				SlideState previousSlide = slideStateMachine.getPreviousSlide();
				nextSlide = currentSlide;
				currentSlide = previousSlide;
				imgPan.setImage(currentSlide.getIcon().getImage());
				
			}
			if (nextSlide != null) {
				run();
			}
			
		}
	}

}
