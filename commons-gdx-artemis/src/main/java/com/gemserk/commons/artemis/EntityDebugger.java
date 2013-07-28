package com.gemserk.commons.artemis;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;

public class EntityDebugger {
	public static void debug(String message, Entity e) {
		System.out.println(message);
		
		System.out.println("e.id=" + e.getId());
		System.out.println("e.uniqueId=" + e.getUuid());
		
		Bag<Component> components = new Bag<Component>();
		e.getComponents(components);
		
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			System.out.println("e.component=" + component.getClass().getSimpleName());
		}
	}

	public static void debug(Entity e) {
		debug("", e);
	}
}