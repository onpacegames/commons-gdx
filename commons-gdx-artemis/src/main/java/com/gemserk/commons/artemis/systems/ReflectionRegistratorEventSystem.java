package com.gemserk.commons.artemis.systems;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.events.reflection.EventListenerReflectionRegistrator;
import com.gemserk.commons.artemis.scripts.Script;

public class ReflectionRegistratorEventSystem extends EntitySystem {
	private final EventListenerReflectionRegistrator eventListenerReflectionRegistrator;

	@SuppressWarnings("unchecked")
	public ReflectionRegistratorEventSystem(EventManager eventManager) {
		super(Aspect.getAspectForAll(Components.scriptComponentClass));
		eventListenerReflectionRegistrator = new EventListenerReflectionRegistrator(eventManager);
	}

	@Override
	protected void inserted(Entity e) {
		ArrayList<Script> scripts = e.getComponent(Components.scriptComponentClass).getScripts();
		for (int i = 0; i < scripts.size(); i++) {
			Script script = scripts.get(i);
			eventListenerReflectionRegistrator.registerEventListeners(script);
		}
	}

	@Override
	protected void removed(Entity e) {
		ArrayList<Script> scripts = e.getComponent(Components.scriptComponentClass).getScripts();
		for (int i = 0; i < scripts.size(); i++) {
			Script script = scripts.get(i);
			eventListenerReflectionRegistrator.unregisterEventListeners(script);
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