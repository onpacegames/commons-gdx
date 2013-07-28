package com.gemserk.commons.gdx.gui;

public abstract class ControlImpl implements Control {
	String id;
	float x, y;
	Control parent = new NullControl();
	boolean dirty = false;
	boolean visible = true;

	public boolean isValid() {
		return !dirty;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public float getX() {
		return x + parent.getX();
	}

	@Override
	public float getY() {
		return y + parent.getY();
	}

	@Override
	public void setX(float x) {
		this.x = x;
		invalidate();
	}

	@Override
	public void setY(float y) {
		this.y = y;
		invalidate();
	}

	@Override
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setParent(Control parent) {
		this.parent = parent;
		invalidate();
	}

	@Override
	public void invalidate() {
		dirty = true;
	}

	@Override
	public void validate() {
		dirty = false;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}
}
