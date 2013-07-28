package com.gemserk.commons.artemis.systems;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.Script;
import com.gemserk.componentsengine.utils.RandomAccessMap;

// TODO loop cleanup in this and all classes
public class ScriptSystem extends EntitySystem {
	class EntityComponents {
		public ArrayList<Script> scripts;
	}

	class Factory extends EntityComponentsFactory<EntityComponents> {
		@Override
		public EntityComponents newInstance() {
			return new EntityComponents();
		}

		@Override
		public void free(EntityComponents entityComponent) {
			entityComponent.scripts = null;
		}

		@Override
		public void load(Entity e, EntityComponents entityComponent) {
			entityComponent.scripts = Components.getScriptComponent(e).getScripts();
		}
	}

	private Factory factory;

	@SuppressWarnings("unchecked")
	public ScriptSystem() {
		super(Aspect.getAspectForAll(Components.scriptComponentClass));
		factory = new Factory();
	}
	
	@Override
	protected void inserted(Entity e) {
		ArrayList<Script> scripts = ScriptComponent.get(e).getScripts();
		for (Script script : scripts) {
			script.inserted(world, e);
		}
	}
	
	@Override
	protected void removed(Entity e) {
		super.removed(e);
		ArrayList<Script> scripts = ScriptComponent.get(e).getScripts();
		for (Script script : scripts) {
			script.removed(world, e);
		}
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		RandomAccessMap<Entity, EntityComponents> allTheEntityComponents = factory.entityComponents;
		int entitiesSize = allTheEntityComponents.size();
		for (int entityIndex = 0; entityIndex < entitiesSize; entityIndex++) {
			EntityComponents entityComponents = allTheEntityComponents.get(entityIndex);
			ArrayList<Script> scripts = entityComponents.scripts;
			for (Script script : scripts) {
				Entity entity = allTheEntityComponents.getKey(entityIndex);
				script.update(world, entity);
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}