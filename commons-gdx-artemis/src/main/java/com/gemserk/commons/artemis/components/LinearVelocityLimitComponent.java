package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class LinearVelocityLimitComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(LinearVelocityLimitComponent.class);

	public static LinearVelocityLimitComponent get(Entity e) {
		return (LinearVelocityLimitComponent) e.getComponent(type);
	}

	private float limit;
	
	public float getLimit() {
		return limit;
	}
	
	public void setLimit(float limit) {
		this.limit = limit;
	}

	public LinearVelocityLimitComponent(float limit) {
		this.limit = limit;
	}
}