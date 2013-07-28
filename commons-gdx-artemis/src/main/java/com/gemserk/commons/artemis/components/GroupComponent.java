package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class GroupComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(GroupComponent.class);

	public static GroupComponent get(Entity e) {
		return (GroupComponent) e.getComponent(type);
	}

	public String group;

	public GroupComponent(String group) {
		this.group = group;
	}
}