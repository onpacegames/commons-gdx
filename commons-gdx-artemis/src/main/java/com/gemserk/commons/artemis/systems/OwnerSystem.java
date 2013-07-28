package com.gemserk.commons.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.ContainerComponent;
import com.gemserk.commons.artemis.components.OwnerComponent;

public class OwnerSystem extends EntitySystem {
	@SuppressWarnings("unchecked")
	public OwnerSystem() {
		super(Aspect.getAspectForAll(Components.ownerComponentClass));
	}

	@Override
	protected void inserted(Entity e) {
		OwnerComponent ownerComponent = OwnerComponent.get(e);
		if (ownerComponent.getOwner() == null) {
			return;
		}
		ContainerComponent containerComponent = ContainerComponent.get(ownerComponent.getOwner());
		if (containerComponent == null) {
			return;
		}
		containerComponent.getChildren().add(e);
	}

	@Override
	protected void removed(Entity e) {
		OwnerComponent ownerComponent = OwnerComponent.get(e);
		if (ownerComponent.getOwner() == null) {
			return;
		}
		ContainerComponent containerComponent = ContainerComponent.get(ownerComponent.getOwner());
		if (containerComponent == null) {
			return;
		}
		containerComponent.getChildren().remove(e);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}
}