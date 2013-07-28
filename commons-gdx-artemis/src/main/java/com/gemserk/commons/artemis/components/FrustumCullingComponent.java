package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.badlogic.gdx.math.Rectangle;

public class FrustumCullingComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(FrustumCullingComponent.class);

	public static FrustumCullingComponent get(Entity e) {
		return (FrustumCullingComponent) e.getComponent(type);
	}

	public Rectangle bounds;

	public FrustumCullingComponent(Rectangle bounds) {
		this.bounds = bounds;
	}
}