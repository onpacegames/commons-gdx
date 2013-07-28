package com.gemserk.commons.svg.inkscape;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.gemserk.vecmath.Matrix3f;
import com.gemserk.vecmath.Vector2f;
import com.gemserk.vecmath.Vector3f;


public class SvgParseGroupTest {
	private Document document;

	@Before
	public void setup() {
		try (InputStream svgStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-groups.svg")) {
			document = new DocumentParser().parse(svgStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetGroupTransform() {
		SvgParser svgParser = new SvgParser();
		
		svgParser.addHandler(new SvgInkscapeGroupHandler() {
			@Override
			protected void handle(SvgParser svgParser, SvgInkscapeGroup svgInkscapeGroup, Element element) {
				String id = svgInkscapeGroup.getId();
				if (!"group-path".equals(id)) {
					return;
				}
				Matrix3f transform = svgInkscapeGroup.getTransform();
				
				Matrix3f expectedMatrix = new Matrix3f(new float[] { 1, 0, 20, 0, 1, 10, 0, 0, 1 });
				assertThat(transform, IsEqual.equalTo(expectedMatrix));
			}
		});
		
		svgParser.parse(document);
	}
	
	@Test
	public void testApplyGroupTransformToPath() {
		SvgParser svgParser = new SvgParser();
		
		final ArrayList<SvgPath> paths = new ArrayList<SvgPath>();
		
		final Matrix3f groupTransform = new Matrix3f();
		
		svgParser.addHandler(new SvgInkscapeGroupHandler() {
			@Override
			protected void handle(SvgParser svgParser, SvgInkscapeGroup svgInkscapeGroup, Element element) {
				Matrix3f transform = svgInkscapeGroup.getTransform();
				groupTransform.set(transform);
			}

			@Override
			public void postHandle(SvgParser svgParser, SvgElement svgElement, Element element) {
				
			}
		});
		svgParser.addHandler(new SvgInkscapePathHandler() {
			
			Vector3f tmp = new Vector3f();
			
			@Override
			protected void handle(SvgParser svgParser, SvgPath svgPath, Element element) {
				Vector2f[] points = svgPath.getPoints();
				for (Vector2f point : points) {
					tmp.set(point.x, point.y, 1f);
					groupTransform.transform(tmp);
					
					point.set(tmp.x, tmp.y);
				}
				
				paths.add(svgPath);
			}

			@Override
			public void postHandle(SvgParser svgParser, SvgElement svgElement, Element element) {
				
			}
		});
		
		svgParser.parse(document);
		
		assertThat(paths.size(), IsEqual.equalTo(2));

		SvgPath svgPath = paths.get(0);
		
		assertThat(svgPath.getPoints()[0].x, IsEqual.equalTo(23f));
		assertThat(svgPath.getPoints()[0].y, IsEqual.equalTo(22f));

		for (SvgPath path : paths) {
			System.out.println(path.getId());
			Vector2f[] points = path.getPoints();
			for (Vector2f point : points) {
				System.out.println(point);
			}
		}
	}

}
