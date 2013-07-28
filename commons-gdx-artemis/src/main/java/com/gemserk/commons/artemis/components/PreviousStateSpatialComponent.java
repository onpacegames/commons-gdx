package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialImpl;

/**
 * Used to store previous state of the SpatialComponent, to interpolate states when rendering.
 */
public class PreviousStateSpatialComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(PreviousStateSpatialComponent.class);

	public static PreviousStateSpatialComponent get(Entity e) {
		return (PreviousStateSpatialComponent) e.getComponent(type);
	}

	private Spatial spatial;

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public PreviousStateSpatialComponent() {
		spatial = new SpatialImpl(0, 0);
	}
}