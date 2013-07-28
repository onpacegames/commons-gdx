package com.gemserk.commons.gdx;

import com.gemserk.componentsengine.utils.Parameters;

/**
 * Provides a delegate implementation of GameState with a fixed timestep, based on <a href="http://gafferongames.com/game-physics/fix-your-timestep/">fix your timestep</a>.
 * 
 */
public class GameStateDelegateFixedTimestepImpl implements GameState {
	protected float delta;

	protected final float dt;
	protected final float maxFrameTime;

	protected float accumulator;

	protected GameState gameState;

	public GameStateDelegateFixedTimestepImpl(GameState gameState) {
		this(gameState, 0.01f, 0.25f);
	}

	public GameStateDelegateFixedTimestepImpl(GameState gameState, float dt, float maxFrameTime) {
		this.gameState = gameState;
		this.dt = dt;
		this.maxFrameTime = maxFrameTime;
	}

	@Override
	public void init() {
		gameState.init();
		accumulator = dt * 2;
	}

	@Override
	public void dispose() {
		gameState.dispose();
	}

	@Override
	public void resume() {
		gameState.resume();
	}

	@Override
	public void pause() {
		gameState.pause();
	}

	@Override
	public void show() {
		gameState.show();
	}

	@Override
	public void hide() {
		gameState.hide();
	}

	@Override
	public void update() {
		// float t = 0f;
		float frameTime = delta;

		// note: max frame time to avoid spiral of death
		if (frameTime > maxFrameTime) {
			frameTime = maxFrameTime;
		}

		accumulator += frameTime;

		while (accumulator >= dt) {
			GlobalTime.setDelta(dt);

			gameState.setDelta(dt);
			gameState.update();

			accumulator -= dt;
		}

		float alpha = accumulator / dt;
		GlobalTime.setAlpha(alpha);

		gameState.setAlpha(alpha);
	}

	@Override
	public void render() {
		gameState.render();
	}

	@Override
	public void setDelta(float delta) {
		this.delta = delta;
	}

	@Override
	public void setAlpha(float alpha) {
		gameState.setAlpha(alpha);
	}

	@Override
	public Parameters getParameters() {
		return gameState.getParameters();
	}

	@Override
	public void resize(int width, int height) {
		gameState.resize(width, height);
	}

}
