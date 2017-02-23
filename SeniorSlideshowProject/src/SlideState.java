

import java.io.Serializable;

public class SlideState implements Serializable {
	
	public enum Transition{ LEFT, RIGHT, UP, DOWN, CROSSFADE };
	
	private static final long serialVersionUID = 1L;
	
	private Transition transitionType;
	private String filename;
	private double transitionTime;
	
	public SlideState(String name)
	{
		filename = name;
	}
	
	public SlideState(String name, Transition type)
	{
		filename = name;
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
	
	public String getFileName()
	{
		return filename;
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