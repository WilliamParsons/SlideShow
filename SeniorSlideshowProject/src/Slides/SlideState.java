package Slides;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class SlideState implements Serializable {
	
	public enum Transition{ NONE, LEFT, RIGHT, UP, DOWN, CROSSFADE };
	
	private static final long serialVersionUID = 1L;
	
	private Transition transitionType;
	private ImageIcon icon;
	private double transitionTime;
	
	public SlideState(ImageIcon icon)
	{
		this.icon = icon;
		transitionType = Transition.NONE;
	}
	
	public SlideState(ImageIcon icon, Transition type)
	{
		this.icon = icon;
		transitionType = type;
	}
	
	public void setTransitionType(Transition type)
	{
		transitionType = type;
	}
	public void setTransitionTime(double time)
	{
		transitionTime = time;
	}
	
	public ImageIcon getIcon()
	{
		return this.icon;
	}
	
	public Transition getTransition()
	{
		return transitionType;
	}
	
	public double getTransitionTime()
	{
		return transitionTime;
	}
	
}