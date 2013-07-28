package com.gemserk.commons.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.TextComponent;
import com.gemserk.commons.gdx.games.Spatial;

/**
 * Updates the TextComponent location using the Spatial information from the SpatialComponent.
 */
public class TextLocationUpdateSystem extends EntityProcessingSystem {
	@SuppressWarnings("unchecked")
	public TextLocationUpdateSystem() {
		super(Aspect.getAspectForAll(Components.spatialComponentClass, Components.textComponentClass));
	}

	@Override
	protected void process(Entity e) {
		SpatialComponent spatialComponent = Components.getSpatialComponent(e);
		TextComponent textComponent = Components.getTextComponent(e);
		Spatial spatial = spatialComponent.getSpatial();
		textComponent.x = spatial.getX();
		textComponent.y = spatial.getY();
	}
}