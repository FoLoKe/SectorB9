package com.sb9.foloke.sectorb9.game.Funtions;

public class Timer
{
	private float timerLenght;
	private static final int maxFrames=60;
	public Timer(float seconds)
	{
		timerLenght=seconds*maxFrames;
	}
	public boolean tick()
	{
		if (timerLenght>0)
		{
		timerLenght--;
		return false;
		}
		else
		{
			return true;
		}
	}
	public float getTick()
	{
		return timerLenght;
	}
	public void setTimer(float sec)
	{
		timerLenght=sec*maxFrames;
	}

	public float getSecond()
    {
        return timerLenght/maxFrames;
    }
}
