package com.gemserk.commons.svg.inkscape;

import com.gemserk.vecmath.Matrix3f;

public class SvgInkscapeImageImpl implements SvgInkscapeImage {
	String label;

	SvgImage svgImage;

	public SvgInkscapeImageImpl(SvgImage svgImage) {
		this.svgImage = svgImage;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getId() {
		return svgImage.getId();
	}

	@Override
	public float getX() {
		return svgImage.getX();
	}

	@Override
	public float getY() {
		return svgImage.getY();
	}

	@Override
	public float getWidth() {
		return svgImage.getWidth();
	}

	@Override
	public float getHeight() {
		return svgImage.getHeight();
	}

	@Override
	public Matrix3f getTransform() {
		return svgImage.getTransform();
	}

}