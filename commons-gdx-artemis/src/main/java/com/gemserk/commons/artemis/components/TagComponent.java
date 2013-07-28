package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class TagComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(TagComponent.class);

	public static TagComponent get(Entity e) {
		return (TagComponent) e.getComponent(type);
	}
	
	private final String tag;
	
	public String getTag() {
		return tag;
	}
	
	public TagComponent(String tag) {
		this.tag = tag;
	}
}