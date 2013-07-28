package com.gemserk.componentsengine.utils.timers;

public class PeriodicTimer implements Timer{
	int period;
	int timeLeft;

	public PeriodicTimer(int period) {
		this.period = period;
		timeLeft = period;
	}

	@Override
	public boolean update(int delta) {
		
		timeLeft-=delta;
		if(timeLeft<0){
			timeLeft+=period;
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		timeLeft = period;
	}
	
	@Override
	public String toString() {
		return String.valueOf(timeLeft);
	}
	
	public int getTimeLeft() {
		return timeLeft;
	}

	@Override
	public boolean isRunning() {
		return true;
	}

}