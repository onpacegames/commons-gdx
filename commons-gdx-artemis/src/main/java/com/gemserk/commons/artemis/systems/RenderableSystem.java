package com.gemserk.commons.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Disposable;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.OwnerComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.render.RenderLayers;
import com.gemserk.commons.artemis.render.Renderable;
import com.gemserk.commons.gdx.camera.Libgdx2dCameraTransformImpl;

public class RenderableSystem extends EntitySystem implements Disposable {
	private RenderLayers renderLayers;

	@SuppressWarnings("unchecked")
	public RenderableSystem() {
		super(Aspect.getAspectForAll(Components.renderableComponentClass));
		// default layers
		renderLayers = new RenderLayers();
		renderLayers.add("default", new RenderLayerSpriteBatchImpl(-1000, 1000, new Libgdx2dCameraTransformImpl()));
	}

	@SuppressWarnings("unchecked")
	public RenderableSystem(RenderLayers renderLayers) {
		super(Aspect.getAspectForAll(Components.renderableComponentClass));
		this.renderLayers = renderLayers;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < renderLayers.size(); i++) {
			RenderLayer renderLayer = renderLayers.get(i);
			if (!renderLayer.isEnabled()) {
				continue;
			}
			renderLayer.render();
		}
	}

	@Override
	protected void inserted(Entity e) {
		RenderableComponent renderableComponent = RenderableComponent.get(e);
		OwnerComponent ownerComponent = OwnerComponent.get(e);

		Renderable renderable = renderableComponent.renderable;

		// this could be set on construction time
		renderable.setEntity(e);

		renderable.setId(e.getId());
		// if it has owner it uses the id of the owner... that was part of the original comparator
		if (ownerComponent != null && ownerComponent.getOwner() != null) {
			renderable.setId(ownerComponent.getOwner().getId());
		}

		// order the entity in the Layer, probably the same inside the layer
		for (int i = 0; i < renderLayers.size(); i++) {
			RenderLayer renderLayer = renderLayers.get(i);
			if (renderLayer.belongs(renderable)) {
				renderLayer.add(renderable);
				return;
			}
		}
	}

	@Override
	protected void removed(Entity e) {
		RenderableComponent renderableComponent = RenderableComponent.get(e);
		Renderable renderable = renderableComponent.renderable;
		// remove the order
		for (int i = 0; i < renderLayers.size(); i++) {
			RenderLayer renderLayer = renderLayers.get(i);
			if (renderLayer.belongs(renderable)) {
				renderLayer.remove(renderable);
				return;
			}
		}
	}

	@Override
	public void initialize() {
		for (int i = 0; i < renderLayers.size(); i++) {
			RenderLayer renderLayer = renderLayers.get(i);
			renderLayer.init();
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < renderLayers.size(); i++) {
			RenderLayer renderLayerSpriteBatchImpl = renderLayers.get(i);
			renderLayerSpriteBatchImpl.dispose();
		}
	}
}