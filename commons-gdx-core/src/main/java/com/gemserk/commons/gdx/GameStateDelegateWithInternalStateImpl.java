package com.gemserk.commons.gdx;

import com.gemserk.componentsengine.utils.Parameters;

/**
 * Holds internal state to know if the GameState is initialized, visible and paused or not.
 * 
 * @author acoppes
 * 
 */
public class GameStateDelegateWithInternalStateImpl implements GameState {
	boolean paused = true;
	boolean visible = false;
	boolean initialized = false;

	GameState gameState;

	public GameStateDelegateWithInternalStateImpl(GameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		gameState.init();
	}

	@Override
	public void dispose() {
		if (!initialized) {
			return;
		}
		initialized = false;
		gameState.dispose();
	}

	@Override
	public void resume() {
		if (!paused || !initialized) {
			return;
		}
		paused = false;
		gameState.resume();
	}

	@Override
	public void pause() {
		if (paused || !initialized) {
			return;
		}
		paused = true;
		gameState.pause();
	}

	@Override
	public void show() {
		if (visible || !initialized) {
			return;
		}
		visible = true;
		gameState.show();
	}

	@Override
	public void hide() {
		if (!visible || !initialized) {
			return;
		}
		visible = false;
		gameState.hide();
	}

	@Override
	public void update() {
		if (paused || !initialized) {
			return;
		}
		gameState.update();
	}

	@Override
	public void render() {
		if (!visible || !initialized) {
			return;
		}
		gameState.render();
	}

	@Override
	public void setDelta(float delta) {
		gameState.setDelta(delta);
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
