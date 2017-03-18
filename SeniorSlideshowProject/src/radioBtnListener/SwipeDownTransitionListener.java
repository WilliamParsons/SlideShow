package radioBtnListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Slides.SlideState;
import Slides.SlideShowStateMachine;

public class SwipeDownTransitionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		SlideShowStateMachine slideShow = SlideShowStateMachine.getInstance();
		SlideState currentSlide = slideShow.getCurrentSlide();
		currentSlide.setTransitionType(SlideState.Transition.DOWN);
		
	}

}
