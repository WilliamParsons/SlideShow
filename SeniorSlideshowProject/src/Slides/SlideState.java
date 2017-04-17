package Slides;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class SlideState implements Serializable {
	
	public enum Transition{ NONE, LEFT, RIGHT, UP, DOWN, CROSSFADE };		//Enumerated Types: NONE, LEFT, RIGHT, UP, DOWN, CROSSFADE
	
	private static final long serialVersionUID = 1L;						//Serial ID
	
	private Transition transitionType;
	private ImageIcon icon;
	private double transitionTime;
	
	public SlideState(ImageIcon icon)										//Function to set SlideState to passed-in ImageIcon
	{
		this.icon = icon;													//Current icon is set to passed-in icon
		transitionType = Transition.NONE;									//Set the transitiion type to NONE
	}
	
	public SlideState(ImageIcon icon, Transition type)						//Function to set SlideState with icon and transition
	{
		this.icon = icon;													//Current icon is set to passed-in icon
		transitionType = type;												//Current type is set to passed-in type
	}
	
	public void setTransitionType(Transition type)							//Function to set transition type
	{
		transitionType = type;												//Current type is set to passed-in type
	}
	
	public void setTransitionTime(double time)								//Function to set the transition time
	{
		transitionTime = time;												//Current time is set to passed-in time
	}
	
	public ImageIcon getIcon()												//Function to get icon
	{	
		return this.icon;													//Return current icon
	}
	
	public Transition getTransition()										//Function to get transition
	{
		return transitionType;												//Return current transition
	}
	
	public double getTransitionTime()										//Function to get transition time
	{
		return transitionTime;												//Return current transition time
	}
	
}