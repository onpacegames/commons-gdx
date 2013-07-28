package com.gemserk.commons.svg.inkscape;

import com.gemserk.vecmath.Matrix3f;

public class SvgImageImpl implements SvgImage {
	String id;

	float x, y, width, height;

	Matrix3f transform;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	@Override
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public Matrix3f getTransform() {
		return transform;
	}

	public void setTransform(Matrix3f transform) {
		this.transform = transform;
	}

}