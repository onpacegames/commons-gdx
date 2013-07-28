package com.gemserk.animation4j.gdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.animation4j.FrameAnimation;

public class Animation implements com.gemserk.animation4j.animations.Animation {
	private final Sprite[] sprites;
	private final FrameAnimation frameAnimation;

	boolean playing, started;

	private float speed;
	
	@Override
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	public Animation(Sprite[] frames, FrameAnimation frameAnimation) {
		sprites = frames;
		this.frameAnimation = frameAnimation;
		playing = true;
		started = true;
		speed = 1f;
	}

	public int getFramesCount() {
		return sprites.length;
	}

	public Sprite getFrame(int index) {
		return sprites[index];
	}

	public void setFrame(int index) {
		frameAnimation.setFrame(index);
	}

	public int getCurrentFrameIndex() {
		return frameAnimation.getCurrentFrame();
	}

	@Override
	public int getIteration() {
		return frameAnimation.getIteration();
	}

	public Sprite getCurrentFrame() {
		return getFrame(frameAnimation.getCurrentFrame());
	}

	@Override
	public void update(float delta) {
		if (!playing) {
			return;
		}
		frameAnimation.update(delta * speed);
		if (frameAnimation.isFinished()) {
			playing = false;
		}
	}

	@Override
	public boolean isFinished() {
		return frameAnimation.isFinished();
	}

	@Override
	public void restart() {
		start();
	}

	@Override
	public void start() {
		playing = true;
		started = true;
		frameAnimation.restart();
	}

	@Override
	public void start(int iterationCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void start(int iterationCount, boolean alternateDirection) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void stop() {
		playing = false;
		started = false;
		frameAnimation.restart();
	}

	@Override
	public void pause() {
		playing = false;
	}

	@Override
	public void resume() {
		playing = true;
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public PlayingDirection getPlayingDirection() {
		return PlayingDirection.Normal;
	}
	
	@Override
	public float getCurrentTime() {
		return frameAnimation.getCurrentTime();
	}
	
	public float getDuration() {
		return frameAnimation.getDuration();
	}

}