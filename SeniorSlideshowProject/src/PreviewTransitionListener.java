
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import Slides.SlideState;
import Slides.SlideShowStateMachine;

public class PreviewTransitionListener implements ActionListener{

	// 	public enum Transition{ NONE, LEFT, RIGHT, UP, DOWN, CROSSFADE };
	@Override
	public void actionPerformed(ActionEvent e) {
		SlideShowStateMachine slideShow = SlideShowStateMachine.getInstance();
		SlideState currentSlide = slideShow.getCurrentSlide();
		SlideState nextSlide = slideShow.getNextSlide();
		if (currentSlide !=null && nextSlide != null)
			{
			    ImageIcon currentSlideIcon = currentSlide.getIcon();
			    ImageIcon nextSlideIcon = nextSlide.getIcon();
			    SlideState.Transition currentTransition = currentSlide.getTransition();
			    if (currentTransition == SlideState.Transition.LEFT){
			    	Transitions.SwipeLeft previewSwipeLeft = new Transitions.SwipeLeft();
			    }
			}
		else{
			//create popup explaining that they need a currentimage and next image for the transition preview to work
		}
	}

}
