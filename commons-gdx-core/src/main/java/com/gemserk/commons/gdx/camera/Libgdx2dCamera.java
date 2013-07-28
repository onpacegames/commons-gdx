package com.gemserk.commons.gdx.camera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Libgdx2dCamera {
	/**
	 * Moves the camera to the specified coordinates.
	 */
	void move(float x, float y);

	/**
	 * Centers the camera in the specified coordinates.
	 */
	void center(float x, float y);

	/**
	 * Zooms the camera the specified scale factor.
	 * 
	 * @param s
	 *            The scale factor to zoom the camera.
	 */
	void zoom(float s);

	/**
	 * Rotates the camera the specified angle.
	 */
	void rotate(float angle);

	/**
	 * Converts view port coordinates to world coordinates based on this camera transformations.
	 * 
	 * @param position
	 *            The Vector2 to be converted.
	 */
	void unproject(Vector2 position);

	/**
	 * Converts world coordinates point to view port coordinates based on this camera transformations.
	 * 
	 * @param position
	 *            The Vector2 to be converted.
	 */
	void project(Vector2 position);

	/**
	 * Applies the camera to the specified sprite batch
	 */
	void apply(SpriteBatch spriteBatch);

	/**
	 * Applies the camera transformations to current OpenGL viewport.
	 */
	void apply();

	/**
	 * Returns the combined matrix between project and model view matrices.
	 */
	Matrix4 getCombinedMatrix();

	/**
	 * Returns the model view matrix used for this camera.
	 */
	Matrix4 getModelViewMatrix();

	/**
	 * Returns the projection view matrix used for this camera.
	 */
	Matrix4 getProjectionMatrix();

	/**
	 * Returns the frustum of the camera in the rectangle.
	 * 
	 * @param frustum
	 *            The Rectangle to be modified with the frustum values.
	 */
	void getFrustum(Rectangle frustum);
}