package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.gemserk.commons.artemis.triggers.Trigger;

public class HitComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(HitComponent.class);

	public static HitComponent get(Entity e) {
		return (HitComponent) e.getComponent(type);
	}

	private final Trigger trigger;

	public Trigger getTrigger() {
		return trigger;
	}

	public HitComponent(Trigger trigger) {
		this.trigger = trigger;
	}
}