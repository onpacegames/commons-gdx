package com.gemserk.commons.artemis.components;

import com.artemis.ComponentType;
import com.artemis.Entity;

/**
 * Utility class to retrieve common Components from an Entity, for example, SpatialComponent.
 */
// TODO make this more consistent
// TODO check which method of component getting is better (e.getComponent or ComponentMappers)
public class Components {
	public static final Class<AntiGravityComponent> antiGravityComponentClass = AntiGravityComponent.class;
	public static final ComponentType antiGravityComponentType = ComponentType.getTypeFor(antiGravityComponentClass);
	
	public static final Class<ContainerComponent> containerComponentClass = ContainerComponent.class;
	public static final ComponentType containerComponentType = ComponentType.getTypeFor(containerComponentClass);
	
	public static final Class<HitComponent> hitComponentClass = HitComponent.class;
	public static final ComponentType hitComponentType = ComponentType.getTypeFor(hitComponentClass);
	
	public static final Class<SpriteComponent> spriteComponentClass = SpriteComponent.class;
	public static final ComponentType spriteComponentType = ComponentType.getTypeFor(spriteComponentClass);

	public static final Class<RenderableComponent> renderableComponentClass = RenderableComponent.class;
	public static final ComponentType renderableComponentType = ComponentType.getTypeFor(renderableComponentClass);

	public static final Class<SpatialComponent> spatialComponentClass = SpatialComponent.class;
	public static final ComponentType spatialComponentType = ComponentType.getTypeFor(spatialComponentClass);

	public static final Class<ScriptComponent> scriptComponentClass = ScriptComponent.class;
	public static final ComponentType scriptComponentType = ComponentType.getTypeFor(scriptComponentClass);

	public static final Class<PhysicsComponent> physicsComponentClass = PhysicsComponent.class;
	public static final ComponentType physicsComponentType = ComponentType.getTypeFor(physicsComponentClass);

	public static final Class<CameraComponent> cameraComponentClass = CameraComponent.class;
	public static final ComponentType cameraComponentType = ComponentType.getTypeFor(cameraComponentClass);

	public static final Class<PreviousStateCameraComponent> previousStateCameraComponentClass = PreviousStateCameraComponent.class;
	public static final ComponentType previousStateCameraComponentType = ComponentType.getTypeFor(previousStateCameraComponentClass);

	public static final Class<TextComponent> textComponentClass = TextComponent.class;
	public static final ComponentType textComponentType = ComponentType.getTypeFor(textComponentClass);

	public static final Class<PreviousStateSpatialComponent> previousStateSpatialComponentClass = PreviousStateSpatialComponent.class;
	public static final ComponentType previousStateSpatialComponentType = ComponentType.getTypeFor(previousStateSpatialComponentClass);

	public static final Class<SoundSpawnerComponent> soundSpawnerComponentClass = SoundSpawnerComponent.class;
	public static final ComponentType soundSpawnerComponentType = ComponentType.getTypeFor(soundSpawnerComponentClass);

	public static final Class<MovementComponent> movementComponentClass = MovementComponent.class;
	public static final ComponentType movementComponentType = ComponentType.getTypeFor(movementComponentClass);

	public static final Class<AnimationComponent> animationComponentClass = AnimationComponent.class;
	public static final ComponentType animationComponentType = ComponentType.getTypeFor(animationComponentClass);

	public static final Class<PropertiesComponent> propertiesComponentClass = PropertiesComponent.class;
	public static final ComponentType propertiesComponentType = ComponentType.getTypeFor(propertiesComponentClass);

	public static final Class<ParticleEmitterComponent> particleEmitterComponentClass = ParticleEmitterComponent.class;
	public static final ComponentType particleEmitterComponentType = ComponentType.getTypeFor(particleEmitterComponentClass);
	
	public static final Class<TagComponent> tagComponentClass = TagComponent.class;
	public static final ComponentType tagComponentType = ComponentType.getTypeFor(tagComponentClass);

	public static SpatialComponent getSpatialComponent(Entity e) {
		return (SpatialComponent) e.getComponent(spatialComponentType);
	}
	
	public static RenderableComponent getRenderableComponent(Entity e) {
		return (RenderableComponent) e.getComponent(renderableComponentType);
	}

	public static SpriteComponent getSpriteComponent(Entity e) {
		return (SpriteComponent) e.getComponent(spriteComponentType);
	}

	public static ScriptComponent getScriptComponent(Entity e) {
		return (ScriptComponent) e.getComponent(scriptComponentType);
	}

	public static PhysicsComponent getPhysicsComponent(Entity e) {
		return (PhysicsComponent) e.getComponent(physicsComponentType);
	}

	public static CameraComponent getCameraComponent(Entity e) {
		return (CameraComponent) e.getComponent(cameraComponentType);
	}

	public static PreviousStateCameraComponent getPreviousStateCameraComponent(Entity e) {
		return (PreviousStateCameraComponent) e.getComponent(previousStateCameraComponentType);
	}
	
	public static TextComponent getTextComponent(Entity e) {
		return (TextComponent) e.getComponent(textComponentType);
	}

	public static PreviousStateSpatialComponent getPreviousStateSpatialComponent(Entity e) {
		return (PreviousStateSpatialComponent) e.getComponent(previousStateSpatialComponentType);
	}

	public static SoundSpawnerComponent getSoundSpawnerComponent(Entity e) {
		return (SoundSpawnerComponent) e.getComponent(soundSpawnerComponentType);
	}

	public static MovementComponent getMovementComponent(Entity e) {
		return (MovementComponent) e.getComponent(movementComponentType);
	}

	public static AnimationComponent getAnimationComponent(Entity e) {
		return (AnimationComponent) e.getComponent(animationComponentType);
	}

	public static PropertiesComponent getPropertiesComponent(Entity e) {
		return (PropertiesComponent) e.getComponent(propertiesComponentType);
	}
	
	public static ParticleEmitterComponent getParticleEmitterComponent(Entity e) {
		return (ParticleEmitterComponent) e.getComponent(particleEmitterComponentType);
	}
	
	public static final Class<FrustumCullingComponent> frustumCullingComponentClass = FrustumCullingComponent.class;
	public static final ComponentType frustumCullingComponentType = ComponentType.getTypeFor(frustumCullingComponentClass);

	public static FrustumCullingComponent getFrustumCullingComponent(Entity e) {
		return (FrustumCullingComponent) e.getComponent(frustumCullingComponentType);
	}
	
	public static final Class<GroupComponent> groupComponentClass = GroupComponent.class;
	public static final ComponentType groupComponentType = ComponentType.getTypeFor(groupComponentClass);
	
	public static GroupComponent getGroupComponent(Entity e) {
		return (GroupComponent) e.getComponent(groupComponentType);
	}
	
	public static final Class<LinearVelocityLimitComponent> linearVelocityLimitComponentClass = LinearVelocityLimitComponent.class;
	public static final ComponentType linearVelocityLimitComponentType = ComponentType.getTypeFor(linearVelocityLimitComponentClass);
	
	public static LinearVelocityLimitComponent getLinearVelocityLimitComponent(Entity e) {
		return (LinearVelocityLimitComponent) e.getComponent(linearVelocityLimitComponentType);
	}
	
	public static final Class<OwnerComponent> ownerComponentClass = OwnerComponent.class;
	public static final ComponentType ownerComponentType = ComponentType.getTypeFor(ownerComponentClass);
	
	public static final Class<AliveComponent> aliveComponentClass = AliveComponent.class;
	public static final ComponentType aliveComponentType = ComponentType.getTypeFor(aliveComponentClass);
	
	public static final Class<StoreComponent> storeComponentClass = StoreComponent.class;
	public static final ComponentType storeComponentType = ComponentType.getTypeFor(storeComponentClass);
}