package com.gemserk.commons.gdx.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.gemserk.commons.gdx.graphics.SpriteBatchUtils;
import com.gemserk.commons.gdx.input.LibgdxPointer;
import com.gemserk.commons.gdx.math.MathUtils2;

public class ImageButton extends ControlImpl {
	ButtonHandler buttonHandler;
	Sprite sprite;
	LibgdxPointer libgdxPointer;
	Color color;

	float width, height;
	float cx, cy;

	Rectangle bounds;
	
	public void setColor(Color color) {
		this.color.set(color);
	}

	public void setColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}
	
	public Color getColor() {
		return color;
	}

	public void setCenter(float cx, float cy) {
		this.cx = cx;
		this.cy = cy;
		invalidate();
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setSize(float w, float h) {
		width = w;
		height = h;
		invalidate();
	}

	public ButtonHandler getButtonHandler() {
		return buttonHandler;
	}
	
	public void setButtonHandler(ButtonHandler buttonHandler) {
		this.buttonHandler = buttonHandler;
	}

	public ImageButton(Sprite sprite) {
		this("", sprite);
	}

	public ImageButton(String id, Sprite sprite) {
		this.id = id;
		this.sprite = sprite;
		buttonHandler = new ButtonHandler();
		color = new Color(1f, 1f, 1f, 1f);
		libgdxPointer = new LibgdxPointer(0);
		width = sprite.getWidth();
		height = sprite.getHeight();
		bounds = new Rectangle(0, 0, width, height);
		cx = 0.5f;
		cy = 0.5f;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		if (!isVisible()) {
			return;
		}
		sprite.setColor(color);
		sprite.setSize(width, height);
		SpriteBatchUtils.drawCentered(spriteBatch, sprite, getX(), getY(), width, height, 0f, cx, cy);
	}
	
	boolean wasInside;

	@Override
	public void update() {

		if (!isValid()) {
			recalculateBounds();
			validate();
		}

		libgdxPointer.update();

		boolean pressed = false;
		boolean released = false;
		
		boolean inside = MathUtils2.inside(bounds, libgdxPointer.getPosition());
		
		if (inside && !wasInside) {
			buttonHandler.onOver(this);
			wasInside = true;
		} else if (!inside && wasInside) {
			buttonHandler.onLeave(this);
			wasInside = false;
		}

		if (libgdxPointer.wasPressed) {
			pressed = MathUtils2.inside(bounds, libgdxPointer.getPressedPosition());
		}

		if (libgdxPointer.wasReleased) {
			released = MathUtils2.inside(bounds, libgdxPointer.getReleasedPosition());
		}

		if (pressed) {
			buttonHandler.onPressed(this);
		}

		if (released) {
			buttonHandler.onReleased(this);
		}

	}

	public void recalculateBounds() {
		bounds.set(getX() - width * cx, getY() - height * cy, width, height);
	}

}