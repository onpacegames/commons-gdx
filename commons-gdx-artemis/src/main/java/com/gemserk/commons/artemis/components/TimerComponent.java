package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class TimerComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(TimerComponent.class);

	public static TimerComponent get(Entity e) {
		return (TimerComponent) e.getComponent(type);
	}

	private float totalTime;
	private float currentTime;

	public float getTotalTime() {
		return totalTime;
	}

	public float getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isFinished() {
		return currentTime <= 0;
	}

	public void reset() {
		currentTime = totalTime;
	}

	public TimerComponent(float time) {
		totalTime = time;
		currentTime = time;
	}
}