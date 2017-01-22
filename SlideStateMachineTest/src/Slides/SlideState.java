package Slides;
public class SlideState{
	
	public enum Transition{ LEFT, RIGHT, UP, DOWN, CROSSFADE };
	
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