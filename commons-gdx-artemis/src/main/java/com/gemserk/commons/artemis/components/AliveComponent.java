package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class AliveComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(AliveComponent.class);

	public static AliveComponent get(Entity e) {
		return (AliveComponent) e.getComponent(type);
	}
	
	private float time;
	
	public float getTime() {
		return time;
	}
	
	public void setTime(float time) {
		this.time = time;
	}

	public AliveComponent(float time) {
		this.time = time;
	}
}