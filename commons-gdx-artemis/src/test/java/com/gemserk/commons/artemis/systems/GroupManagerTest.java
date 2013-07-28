package com.gemserk.commons.artemis.systems;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.WorldWrapper;

public class GroupManagerTest {
	@Test
	public void entityNotRemovedFromGroupWhenDeleted() {
		String group = "EntityGroup";
		
		WorldWrapper worldWrapper = new WorldWrapper(new World());
		worldWrapper.getWorld().setManager(new GroupManager());
		worldWrapper.init();
		
		Entity e = worldWrapper.getWorld().createEntity();
		
		worldWrapper.getWorld().getManager(GroupManager.class).add(e, group);
		e.addToWorld();
		
		worldWrapper.update(10);
		
		ImmutableBag<Entity> entities = worldWrapper.getWorld().getManager(GroupManager.class).getEntities(group);
		assertThat(entities.size(), IsEqual.equalTo(1));
		
		worldWrapper.getWorld().deleteEntity(e);
		
		worldWrapper.update(10);

		entities = worldWrapper.getWorld().getManager(GroupManager.class).getEntities(group);
		assertThat(entities.size(), IsEqual.equalTo(0));
	}
}