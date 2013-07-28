package com.gemserk.commons.artemis.components;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

// TODO: should use a Parameters class? should Parameters API be named something like Properties?
public class PropertiesComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(PropertiesComponent.class);

	public static PropertiesComponent get(Entity e) {
		return (PropertiesComponent) e.getComponent(type);
	}

	public Map<String, Object> properties;

	public PropertiesComponent(Map<String, Object> properties) {
		this.properties = properties;
	}

	public PropertiesComponent() {
		this(new HashMap<String, Object>());
	}
	
	public <T> T get(String id) {
		return (T) properties.get(id);
	}

	public <T> T get(String id, T defaultValue) {
		if (!properties.containsKey(id)) {
			return defaultValue;
		}
		return (T) properties.get(id);
	}
	
	public void put(String id, Object value) {
		properties.put(id, value);
	}
}