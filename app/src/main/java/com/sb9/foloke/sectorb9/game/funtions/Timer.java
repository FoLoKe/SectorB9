package com.sb9.foloke.sectorb9.game.funtions;

public class Timer
{
	private int timerLenght;
	private static final int maxFrames=60;
	public Timer(int seconds)
	{
		timerLenght=seconds*maxFrames;
	}
	public boolean tick()
	{
		if (timerLenght>1)
		{
		timerLenght--;
		return false;
		}
		else
		{
			return true;
		}
	}
	
}
