package com.gemserk.commons.artemis.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;

/**
 * Provides an implementation of Script calling a collection of other Scripts.
 * 
 * @author acoppes
 * 
 */
public class ScriptCompositeImpl extends ScriptJavaImpl {
	private Bag<Script> scripts = new Bag<Script>();

	public ScriptCompositeImpl(Script requiredScript, Script... scripts) {
		this.scripts.add(requiredScript);
		if (scripts != null) {
			for (Script script : scripts) {
				this.scripts.add(script);
			}
		}
	}
	
	@Override
	public void inserted(World world, Entity e) {
		for (int i = 0; i < scripts.size(); i++) {
			scripts.get(i).inserted(world, e);
		}
	}

	@Override
	public void update(World world, Entity e) {
		for (int i = 0; i < scripts.size(); i++) {
			scripts.get(i).update(world, e);
		}
	}
	
	@Override
	public void removed(World world, Entity e) {
		for (int i = 0; i < scripts.size(); i++) {
			scripts.get(i).removed(world, e);
		}
	}
}
