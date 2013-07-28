package com.gemserk.commons.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.TagComponent;

public class TagSystem extends EntitySystem {
	@SuppressWarnings("unchecked")
	public TagSystem() {
		super(Aspect.getAspectForAll(Components.tagComponentClass));
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	protected void inserted(Entity e) {
		TagComponent tagComponent = e.getComponent(Components.tagComponentClass);
		world.getManager(TagManager.class).register(tagComponent.getTag(), e);
	}

	@Override
	protected void removed(Entity e) {
		TagComponent tagComponent = e.getComponent(Components.tagComponentClass);
		Entity entityWithTag = world.getManager(TagManager.class).getEntity(tagComponent.getTag());
		if (entityWithTag == null) {
			return;
		}
		if (entityWithTag != e) {
			return;
		}
		world.getManager(TagManager.class).unregister(tagComponent.getTag());
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}
}