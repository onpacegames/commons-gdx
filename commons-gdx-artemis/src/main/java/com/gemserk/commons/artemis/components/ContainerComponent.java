package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.gemserk.componentsengine.utils.RandomAccessSet;

public class ContainerComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(ContainerComponent.class);

	public static ContainerComponent get(Entity e) {
		return (ContainerComponent) e.getComponent(type);
	}

	private RandomAccessSet<Entity> children = new RandomAccessSet<Entity>();
	
	public RandomAccessSet<Entity> getChildren() {
		return children;
	}
}