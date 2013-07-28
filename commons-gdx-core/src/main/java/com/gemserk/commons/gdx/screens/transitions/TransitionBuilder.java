package com.gemserk.commons.gdx.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.gemserk.commons.gdx.Game;
import com.gemserk.commons.gdx.GameTransitions.ScreenTransition;
import com.gemserk.commons.gdx.GameTransitions.TransitionHandler;
import com.gemserk.commons.gdx.GameTransitions.TransitionScreen;
import com.gemserk.commons.gdx.Screen;

public class TransitionBuilder {
	// should be named ScreenTransitionBuilder because it builds a ScreenTransition

	private final Screen screen;
	private final Game game;

	float leaveTime;
	float enterTime;

	boolean shouldDisposeCurrentScreen;
	boolean shouldRestartNextScreen;

	TransitionHandler leaveTransitionHandler = new TransitionHandler();
	TransitionHandler enterTransitionHandler = new TransitionHandler();

	private boolean transitioning = false;

	public TransitionBuilder leaveTime(float leaveTime) {
		this.leaveTime = leaveTime;
		return this;
	}

	public TransitionBuilder enterTime(float enterTime) {
		this.enterTime = enterTime;
		return this;
	}

	public TransitionBuilder leaveTime(int leaveTime) {
		return leaveTime(leaveTime * 0.001f);
	}

	public TransitionBuilder enterTime(int enterTime) {
		return enterTime(enterTime * 0.001f);
	}

	public TransitionBuilder disposeCurrent() {
		shouldDisposeCurrentScreen = true;
		return this;
	}

	public TransitionBuilder disposeCurrent(boolean disposeCurrent) {
		shouldDisposeCurrentScreen = disposeCurrent;
		return this;
	}

	public TransitionBuilder restartScreen() {
		shouldRestartNextScreen = true;
		return this;
	}
	
	public TransitionBuilder restartScreen(boolean restart) {
		shouldRestartNextScreen = restart;
		return this;
	}

	public TransitionBuilder leaveTransitionHandler(TransitionHandler transitionHandler) {
		leaveTransitionHandler = transitionHandler;
		return this;
	}
	
	public TransitionBuilder enterTransitionHandler(TransitionHandler transitionHandler) {
		enterTransitionHandler = transitionHandler;
		return this;
	}

	public TransitionBuilder parameter(String key, Object value) {
		screen.getParameters().put(key, value);
		return this;
	}

	public TransitionBuilder(final Game game, final Screen screen) {
		this.game = game;
		this.screen = screen;
		leaveTransitionHandler = new TransitionHandler();
		leaveTime = 0.25f;
		enterTime = 0.25f;
	}

	public void start() {
		if (transitioning) {
			Gdx.app.log("Commons-gdx", "Can't start a new transition if already in a transition");
			return;
		}

		transitioning = true;

		final Screen currentScreen = game.getScreen();
		game.setScreen(new TransitionScreen(new ScreenTransition( //
				new FadeOutTransition(currentScreen, leaveTime, new TransitionHandler() {
					
					@Override
					public void onBegin() {
						super.onBegin();
						leaveTransitionHandler.onBegin();
					}

					@Override
					public void onEnd() {
						super.onEnd();
						leaveTransitionHandler.onEnd();
						if (shouldRestartNextScreen) {
							screen.dispose();
						}
					}
					
				}), //
				new FadeInTransition(screen, enterTime, new TransitionHandler() {
					
					@Override
					public void onBegin() {
						super.onBegin();
						enterTransitionHandler.onBegin();
					}
					
					@Override
					public void onEnd() {
						super.onEnd();
						enterTransitionHandler.onEnd();
						game.setScreen(screen, true);
						// disposes current transition screen, not previous screen.
						if (shouldDisposeCurrentScreen) {
							currentScreen.dispose();
						}
						transitioning = false;
					}
					
				}))) {
			@Override
			public void resume() {
				super.resume();
				Gdx.input.setCatchBackKey(true);
			}
		});
	}

}