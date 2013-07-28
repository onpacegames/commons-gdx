package com.gemserk.commons.artemis;

import java.util.ArrayList;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.badlogic.gdx.utils.Disposable;

// FIXME I'm not sure the way the artmeis world is processed will work with separating update and render systems - needs looking in to.
public class WorldWrapper {
	protected World world;

	protected ArrayList<EntitySystem> updateSystems;
	protected ArrayList<EntitySystem> renderSystems;

	protected ArrayList<WorldSystem> worldUpdateSystems;
	protected ArrayList<WorldSystem> worldRenderSystems;

	public World getWorld() {
		return world;
	}

	public WorldWrapper(World world) {
		this.world = world;
		updateSystems = new ArrayList<EntitySystem>();
		renderSystems = new ArrayList<EntitySystem>();

		worldUpdateSystems = new ArrayList<WorldSystem>();
		worldRenderSystems = new ArrayList<WorldSystem>();
	}

	public void addUpdateSystem(EntitySystem entitySystem) {
		world.setSystem(entitySystem);
		updateSystems.add(entitySystem);
	}
	
	public void addUpdateSystem(WorldSystem worldSystem) {
		worldUpdateSystems.add(worldSystem);
	}

	public void addRenderSystem(EntitySystem entitySystem) {
		world.setSystem(entitySystem);
		renderSystems.add(entitySystem);
	}
	
	public void addRenderSystem(WorldSystem worldSystem) {
		worldRenderSystems.add(worldSystem);
	}

	public void init() {
		world.initialize();
		
		for (WorldSystem worldUpdateSystem : worldUpdateSystems) {
			worldUpdateSystem.init(world);
		}
		
		for (WorldSystem worldRenderSystem : worldRenderSystems) {
			worldRenderSystem.init(world);
		}
	}

	public void update(int delta) {
		world.setDelta(delta);
		world.process();
		
		for (EntitySystem updateSystem : updateSystems) {
			updateSystem.process();
		}
		
		for (WorldSystem worldUpdateSystem : worldUpdateSystems) {
			worldUpdateSystem.process(world);
		}
	}

	public void render() {
		for (EntitySystem renderSystem : renderSystems) {
			renderSystem.process();
		}
		
		for (WorldSystem worldRenderSystem : worldRenderSystems) {
			worldRenderSystem.process(world);
		}
	}

	/**
	 * Called to dispose the world and all entity systems, be aware you can't use is again without reinitializing it.
	 */
	public void dispose() {
		for (EntitySystem updateSystem : updateSystems) {
			if (updateSystem instanceof Disposable) {
				((Disposable) updateSystem).dispose();
			}
		}

		for (EntitySystem renderSystem : renderSystems) {
			if (renderSystem instanceof Disposable) {
				((Disposable) renderSystem).dispose();
			}
		}
		
		for (WorldSystem worldUpdateSystem : worldUpdateSystems) {
			worldUpdateSystem.dispose(world);
		}

		for (WorldSystem worldRenderSystem : worldRenderSystems) {
			worldRenderSystem.dispose(world);
		}
	}
}