package com.gemserk.commons.artemis.systems;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsSame;
import org.junit.Test;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;

public class TagManagerTest {
	@Test
	public void test() {
		
	}
	
	public void shouldGetComponent() {
		String tag = "SuperUniqueEntity";
		
		World world = new World();
		Entity e = world.createEntity();
		world.getManager(TagManager.class).register(tag, e);
		e.addToWorld();
		
		world.setDelta(10);
		
		Entity e1 = world.getManager(TagManager.class).getEntity(tag);
		assertThat(e1, IsNull.notNullValue());
		assertThat(e1, IsSame.sameInstance(e));
		
		world.deleteEntity(e);
		
		world.setDelta(10);

		Entity e2 = world.getManager(TagManager.class).getEntity(tag);
		assertThat(e2, IsNull.nullValue());
	}
}