package com.gemserk.commons.gdx.g2d;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NinePatchSpriteHack extends NinePatch {
	private Sprite sprite = null;
	private float originalWidth;
	private float originalHeight;

	public float scaleFactor;

	public NinePatchSpriteHack(Sprite sprite) {
		this(sprite, 1);
	}

	public NinePatchSpriteHack(Sprite sprite, float scaleFactor) {
		super(sprite);
		// if (sprite instanceof AtlasSprite)
		// this.atlasSprite = (AtlasSprite) sprite;
		// else
		this.sprite = sprite;
		this.scaleFactor = scaleFactor;
		originalHeight = sprite.getWidth();
		originalWidth = sprite.getHeight();
	}

	@Override
	public void draw(SpriteBatch batch, float x, float y, float width, float height) {
		float scaledWidth = scaleFactor * width;
		float scaledHeight = scaleFactor * height;
		float newX = x + (width - scaledWidth) / 2f;
		float newY = y + (height - scaledHeight) / 2f;

		if (sprite.getX() != newX || sprite.getY() != newY || sprite.getWidth() != scaledWidth || sprite.getHeight() != scaledHeight) {
			sprite.setBounds(newX, newY, scaledWidth, scaledHeight);
		}

		sprite.setColor(batch.getColor());
		sprite.draw(batch);
	}

	@Override
	public float getTotalHeight() {
		return originalWidth;
	}

	@Override
	public float getTotalWidth() {
		return originalHeight;
	}
}