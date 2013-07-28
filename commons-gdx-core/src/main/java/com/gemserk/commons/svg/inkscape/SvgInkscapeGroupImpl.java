package com.gemserk.commons.svg.inkscape;

import com.gemserk.vecmath.Matrix3f;

public class SvgInkscapeGroupImpl implements SvgInkscapeGroup {
	String groupMode;
	String label;
	SvgGroup svgGroup;

	public SvgInkscapeGroupImpl(SvgGroup svgGroup) {
		this.svgGroup = svgGroup;
	}
	
	@Override
	public String getGroupMode() {
		return groupMode;
	}

	public void setGroupMode(String groupMode) {
		this.groupMode = groupMode;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Matrix3f getTransform() {
		return svgGroup.getTransform();
	}

	@Override
	public String getId() {
		return svgGroup.getId();
	}

}