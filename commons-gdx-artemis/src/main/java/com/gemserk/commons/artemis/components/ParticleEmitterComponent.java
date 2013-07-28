package com.gemserk.commons.artemis.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

public class ParticleEmitterComponent extends Component {
	public static final ComponentType type = ComponentType.getTypeFor(ParticleEmitterComponent.class);

	public static ParticleEmitterComponent get(Entity e) {
		return (ParticleEmitterComponent) e.getComponent(type);
	}

	public ParticleEmitter particleEmitter;

	public ParticleEmitterComponent(ParticleEmitter particleEmitter) {
		this.particleEmitter = particleEmitter;
	}
}