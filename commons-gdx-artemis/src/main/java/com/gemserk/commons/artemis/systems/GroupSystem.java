package com.gemserk.commons.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.GroupComponent;

public class GroupSystem extends EntitySystem {
	@SuppressWarnings("unchecked")
	public GroupSystem() {
		super(Aspect.getAspectForAll(Components.groupComponentClass));
	}

	@Override
	protected void initialize() {
		super.initialize();
	}
	
	@Override
	protected void inserted(Entity e) {
		GroupComponent groupComponent = Components.getGroupComponent(e);
		world.getManager(GroupManager.class).add(e, groupComponent.group);
	}
	
	@Override
	protected void removed(Entity e) {
		GroupComponent groupComponent = Components.getGroupComponent(e);
		world.getManager(GroupManager.class).remove(e, groupComponent.group);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}
}