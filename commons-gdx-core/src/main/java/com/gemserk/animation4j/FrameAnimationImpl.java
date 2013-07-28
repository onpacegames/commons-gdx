package com.gemserk.animation4j;

/**
 * An implementation of the state of an animation based on frames (without knowing nothing about graphics).
 */
public class FrameAnimationImpl implements FrameAnimation {
	/**
	 * Represents each frame time
	 */
	float[] framesTimes;

	int currentFrame;
	int currentIteration;
	float currentTime;

	float iterationTime;

	float duration;

	// add all play/stop/pause/etc?

	boolean loop;

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public FrameAnimationImpl(FrameAnimationImpl frameAnimationImpl) {
		framesTimes = new float[frameAnimationImpl.framesTimes.length];
		System.arraycopy(frameAnimationImpl.framesTimes, 0, framesTimes, 0, frameAnimationImpl.framesTimes.length);
		currentFrame = frameAnimationImpl.currentFrame;
		currentTime = frameAnimationImpl.currentTime;
		currentIteration = 1;
		loop = frameAnimationImpl.loop;
		iterationTime = frameAnimationImpl.iterationTime;
		duration = frameAnimationImpl.duration;
	}

	public FrameAnimationImpl(float f0, float... framesTimes) {
		currentFrame = 0;
		currentTime = 0;
		currentIteration = 1;
		loop = false;
		iterationTime = 0f;
		duration = 0f;
		set(f0, framesTimes);
	}

	public FrameAnimationImpl(float... framesTimes) {
		currentFrame = 0;
		currentTime = 0;
		currentIteration = 1;
		loop = false;
		iterationTime = 0f;
		duration = 0f;
		set(framesTimes);
	}

	public FrameAnimationImpl(boolean loop, float... framesTimes) {
		// we could use the pattern 1+
		assert framesTimes.length > 0;
		currentFrame = 0;
		currentTime = 0;
		currentIteration = 1;
		this.loop = loop;
		iterationTime = 0f;
		duration = 0f;
		set(framesTimes);
	}

	private float truncate(float number, int precision) {
		return (float) (Math.floor(number * precision + .5) / precision);
	}

	private float truncate(float number) {
		return truncate(number, 1000);
	}

	@Override
	public FrameAnimationImpl clone() {
		return new FrameAnimationImpl(this);
	}

	private void set(float f0, float... frames) {
		framesTimes = new float[frames.length + 1];
		framesTimes[0] = truncate(f0);
		duration += framesTimes[0];
		for (int i = 1; i < frames.length + 1; i++) {
			framesTimes[i] = truncate(frames[i - 1]);
			duration += framesTimes[i];
		}
	}

	private void set(float... frames) {
		framesTimes = new float[frames.length];
		for (int i = 0; i < frames.length; i++) {
			framesTimes[i] = truncate(frames[i]);
			duration += framesTimes[i];
		}
	}

	@Override
	public int getFramesCount() {
		return framesTimes.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gemserk.games.taken.FrameAnimation#getCurrentFrame()
	 */
	@Override
	public int getCurrentFrame() {
		return currentFrame;
	}

	@Override
	public void setFrame(int frame) {
		if (currentFrame < 0 || currentFrame >= framesTimes.length) {
			throw new IllegalStateException("Frame animation with index " + frame + " not found.");
		}
		currentFrame = frame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gemserk.games.taken.FrameAnimation#update(int)
	 */
	@Override
	public void update(float delta) {
		currentTime += delta;
		iterationTime += delta;
		float currentFrameTime = getCurrentFrameTime();
		while (currentTime >= currentFrameTime && !isFinished()) {
			nextFrame();
			currentTime -= currentFrameTime;
			currentFrameTime = getCurrentFrameTime();
		}
	}

	private float getCurrentFrameTime() {
		return framesTimes[currentFrame];
	}

	private void nextFrame() {

		if (currentFrame < getFramesCount() - 1) {
			currentFrame++;
		} else if (loop) {
			currentFrame = 0;
			currentIteration++;
			iterationTime -= duration;
			iterationTime = truncate(iterationTime);
			if (iterationTime <= 0f) {
				iterationTime = 0f;
			}
		}

	}

	@Override
	public boolean isFinished() {
		if (loop) {
			return false;
		}
		if (currentFrame != getFramesCount() - 1) {
			return false;
		}
		return currentTime >= getCurrentFrameTime();
	}

	@Override
	public void restart() {
		currentFrame = 0;
		currentTime = 0;
		currentIteration++;
		iterationTime = 0f;
	}

	@Override
	public int getIteration() {
		return currentIteration;
	}

	@Override
	public float getCurrentTime() {
		return iterationTime;
	}

	@Override
	public float getDuration() {
		return duration;
	}

}
