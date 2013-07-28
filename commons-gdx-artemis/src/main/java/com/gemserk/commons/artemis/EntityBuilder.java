package com.gemserk.commons.artemis;

import java.util.ArrayList;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;

public class EntityBuilder {
	private final World world;
	private final TagManager tagManager;
	private final GroupManager groupManager;
	private final PlayerManager playerManager;

	private ArrayList<Component> components;
	private ArrayList<String> tags;
	private ArrayList<String> groups;
	private ArrayList<String> players;

	public EntityBuilder(World world) {
		this.world = world;
		tagManager = world.getManager(TagManager.class);
		groupManager = world.getManager(GroupManager.class);
		playerManager = world.getManager(PlayerManager.class);
		
		components = new ArrayList<Component>();
		tags = new ArrayList<String>();
		groups = new ArrayList<String>();
		players = new ArrayList<String>();
		
		reset();
	}

	private void reset() {
		components.clear();
		tags.clear();
		groups.clear();
		players.clear();
	}

	public EntityBuilder component(Component component) {
		components.add(component);
		return this;
	}
	
	public EntityBuilder tag(String tag) {
		tags.add(tag);
		return this;
	}
	
	public EntityBuilder group(String group) {
		groups.add(group);
		return this;
	}
	
	public EntityBuilder player(String player) {
		players.add(player);
		return this;
	}

	public Entity build() {
		Entity e = world.createEntity();
		for (Component component : components) {
			e.addComponent(component);
		}
		for (String tag : tags) {
			tagManager.register(tag, e);
		}
		for (String group : groups) {
			groupManager.add(e, group);
		}
		for (String player : players) {
			playerManager.setPlayer(e, player);
		}
		e.addToWorld();
		reset();
		return e;
	}
}