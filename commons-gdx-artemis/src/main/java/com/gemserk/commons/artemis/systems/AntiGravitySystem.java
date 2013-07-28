package com.gemserk.commons.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.PhysicsComponent;

public class AntiGravitySystem extends EntityProcessingSystem {
	private final Vector2 tmp = new Vector2();

	@SuppressWarnings("unchecked")
	public AntiGravitySystem() {
		super(Aspect.getAspectForAll(Components.antiGravityComponentClass));
	}
	
	@Override
	protected void process(Entity e) {
		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
		Body body = physicsComponent.getPhysics().getBody();
		
		Vector2 gravity = body.getWorld().getGravity();

		tmp.set(gravity).scl(-body.getMass());
		body.applyForceToCenter(tmp, true);
	}
}