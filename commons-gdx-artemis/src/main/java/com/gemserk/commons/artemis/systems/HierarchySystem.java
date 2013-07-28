package com.gemserk.commons.artemis.systems;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.components.ParentComponent;

public class HierarchySystem extends EntitySystem {
	@SuppressWarnings("unchecked")
	public HierarchySystem() {
		super(Aspect.getAspectForAll(ParentComponent.class));
	}

	@Override
	protected void removed(Entity entity) {
		ParentComponent parentComponent = entity.getComponent(ParentComponent.class);
		
		// if for some reason the entity parent component was removed before this method was called
		if (parentComponent == null) {
			return;
		}
		
		entity.removeComponent(parentComponent);
		
		ArrayList<Entity> children = parentComponent.getChildren();

		// send the component to the components heaven, so it could be reused
		
		for (Entity child : children) {
			world.deleteEntity(child);
		}

	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}
}