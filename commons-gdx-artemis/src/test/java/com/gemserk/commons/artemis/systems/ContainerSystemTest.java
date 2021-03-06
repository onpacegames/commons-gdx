package com.gemserk.commons.artemis.systems;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Ignore;
import org.junit.Test;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.WorldWrapper;
import com.gemserk.commons.artemis.components.ContainerComponent;
import com.gemserk.commons.artemis.components.OwnerComponent;
import com.gemserk.commons.artemis.components.StoreComponent;
import com.gemserk.commons.artemis.utils.EntityStore;

public class ContainerSystemTest {
	@Test
	public void shouldBeAddedToParentContainer() {
		WorldWrapper worldWrapper = new WorldWrapper(new World());
		worldWrapper.addUpdateSystem(new ContainerSystem());
		worldWrapper.addUpdateSystem(new OwnerSystem());
		worldWrapper.init();
		
		Entity e1 = worldWrapper.getWorld().createEntity();
		e1.addComponent(new ContainerComponent());
		e1.addToWorld();

		Entity e2 = worldWrapper.getWorld().createEntity();
		e2.addComponent(new OwnerComponent(e1));
		e2.addToWorld();
		
		worldWrapper.update(10);

		ContainerComponent containerComponent = e1.getComponent(ContainerComponent.class);
		assertThat(containerComponent.getChildren().contains(e2), IsEqual.equalTo(true));
	}

	@Test
	public void shouldRemoveChildrenIfParentRemoved() {
		World world = new World();
		WorldWrapper worldWrapper = new WorldWrapper(world);
		worldWrapper.addUpdateSystem(new ContainerSystem());
		worldWrapper.addUpdateSystem(new OwnerSystem());
		worldWrapper.init();

		Entity e1 = world.createEntity();
		e1.addComponent(new ContainerComponent());
		e1.addToWorld();

		Entity e2 = world.createEntity();
		e2.addComponent(new OwnerComponent(e1));
		e2.addToWorld();

		worldWrapper.update(10);

		world.deleteEntity(e1);

		worldWrapper.update(1);
		worldWrapper.update(1);

		Entity e3 = world.getEntity(e2.getId());
		assertThat(e3, IsNull.nullValue());
	}

	// TEST: removing the container component should remove children too?

	//boolean shouldBeDisabled;

	// FIXME TODO something is wrong
	@Ignore
	@Test
	public void shouldNotProcessDisabledChildrenEntity() {
		World world = new World();

		WorldWrapper worldWrapper = new WorldWrapper(world);
		worldWrapper.addUpdateSystem(new ContainerSystem());
		worldWrapper.addUpdateSystem(new OwnerSystem());

		//shouldBeDisabled = false;

		/*worldWrapper.addUpdateSystem(new EntityProcessingSystem(Aspect.getAspectForAll(Components.storeComponentClass)) {
			@Override
			protected void process(Entity e) {
				if (shouldBeDisabled) {
					fail("should not process children");
				}
				System.out.println("processing children");
				shouldBeDisabled = true;
			}
		});*/

		worldWrapper.init();

		Entity e1 = world.createEntity();
		e1.addComponent(new ContainerComponent());
		e1.addToWorld();

		Entity e2 = world.createEntity();
		e2.addComponent(new OwnerComponent(e1));
		e2.addComponent(new StoreComponent(new EntityStore(null) {}));
		e2.addToWorld();

		worldWrapper.update(1);

		assertTrue(e1.isEnabled());
		assertTrue(e2.isEnabled());

		e1.deleteFromWorld();

		worldWrapper.update(1);

		assertFalse(e1.isEnabled());
		assertFalse(e2.isEnabled());

		worldWrapper.update(1);
	}
}