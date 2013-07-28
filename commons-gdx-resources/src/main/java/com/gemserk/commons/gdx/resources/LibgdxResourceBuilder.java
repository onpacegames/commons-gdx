package com.gemserk.commons.gdx.resources;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gemserk.animation4j.FrameAnimationImpl;
import com.gemserk.animation4j.gdx.Animation;
import com.gemserk.commons.gdx.graphics.ParticleEmitterUtils;
import com.gemserk.commons.gdx.graphics.SpriteUtils;
import com.gemserk.commons.gdx.resources.dataloaders.DisposableDataLoader;
import com.gemserk.commons.gdx.resources.dataloaders.MusicDataLoader;
import com.gemserk.commons.gdx.resources.dataloaders.SoundDataLoader;
import com.gemserk.commons.gdx.resources.dataloaders.TextureDataLoader;
import com.gemserk.commons.svg.inkscape.DocumentParser;
import com.gemserk.commons.values.FloatValue;
import com.gemserk.resources.Resource;
import com.gemserk.resources.ResourceManager;
import com.gemserk.resources.dataloaders.DataLoader;

public class LibgdxResourceBuilder {
	// TODO: Define folders for each type of resource?

	protected ResourceManager<String> resourceManager;

	public LibgdxResourceBuilder(ResourceManager<String> resourceManager) {
		this.resourceManager = resourceManager;
	}

	public static FileHandle internal(String file) {
		return Gdx.files.internal(file);
	}

	public static FileHandle classPath(String file) {
		return Gdx.files.classpath(file);
	}

	public static FileHandle absolute(String file) {
		return Gdx.files.absolute(file);
	}

	public void texture(String id, String file) {
		texture(id, internal(file), true);
	}

	public void texture(String id, String file, boolean linearFilter) {
		texture(id, internal(file), linearFilter);
	}

	public void texture(String id, FileHandle fileHandle, boolean linearFilter) {
		resourceManager.add(id, new TextureDataLoader(fileHandle, linearFilter));
	}

	/**
	 * Registers a texture atlas in resources manager.
	 * 
	 * @param id
	 *            The id of the resource.
	 * @param file
	 *            The libgdx internal file path to create the texture atlas.
	 */
	public void textureAtlas(String id, String file) {
		resourceManager.add(id, new DisposableDataLoader<TextureAtlas>(Gdx.files.internal(file)) {
			@Override
			public TextureAtlas load() {
				return new TextureAtlas(fileHandle);
			}
		});
	}

	public void splitLoadingTextureAtlas(final String id, final String file) {
		FileHandle packFile = Gdx.files.internal(file);
		final TextureAtlasData textureAtlasData = new TextureAtlasData(packFile, packFile.parent(), false);

		Array<Page> pages = textureAtlasData.getPages();
		final String pageTextureSuffix = "_generated_page_";
		for (int i = 0; i < pages.size; i++) {
			Page page = pages.get(i);
			FileHandle textureFile = page.textureFile;
			resource(id + pageTextureSuffix + i, texture2(textureFile).format(page.format).useMipMaps(page.useMipMaps).magFilter(page.magFilter).minFilter(page.minFilter));
		}

		resourceManager.add(id, new DataLoader<TextureAtlas>() {
			@Override
			public TextureAtlas load() {
				Array<Page> pages = textureAtlasData.getPages();
				for (int i = 0; i < pages.size; i++) {
					Page page = pages.get(i);
					try {
						String textureResourceId = id + pageTextureSuffix + i;
						page.texture = resourceManager.getResourceValue(textureResourceId);
						if (page.texture == null) {
							throw new RuntimeException("The resource " + textureResourceId + " was not found");
						}
					} catch (Exception e) {
						throw new RuntimeException("Error while loading page for textureAtlas " + id + " - page: " + page.textureFile.path(), e);
					}
				}

				return new TextureAtlas(textureAtlasData);
			}

			@Override
			public void unload(TextureAtlas atlas) {
				atlas.dispose();
			}
		});

	}

	/**
	 * registers a new sprite resource builder returning a new sprite each time it is called.
	 */
	public void sprite(String id, final String textureId) {
		resourceManager.addVolatile(id, new DataLoader<Sprite>() {
			@Override
			public Sprite load() {
				Resource<Texture> texture = resourceManager.get(textureId);
				if (texture == null) {
					throw new RuntimeException("Failed to create Sprite from missing Texture resource " + textureId);
				}
				return new Sprite(texture.get());
			}
		});
	}

	/**
	 * registers a new sprite resource builder returning a new sprite each time it is called.
	 */
	public void sprite(String id, final String textureId, final int x, final int y, final int width, final int height) {
		resourceManager.addVolatile(id, new DataLoader<Sprite>() {
			@Override
			public Sprite load() {
				Resource<Texture> texture = resourceManager.get(textureId);
				return new Sprite(texture.get(), x, y, width, height);
			}
		});
	}

	/**
	 * Registers with id a new Sprite based on a TextureAtlas region with the same name.
	 * 
	 * @param id
	 *            The identifier of the Resource to register, also the texture region name inside the TextureAtlas.
	 * @param textureAtlasId
	 *            the TextureAtlas resource identifier.
	 */
	public void spriteAtlas(final String id, final String textureAtlasId, final String regionId) {
		resourceManager.addVolatile(id, new DataLoader<Sprite>() {

			private Sprite sprite = null;

			@Override
			public Sprite load() {
				TextureAtlas textureAtlas = resourceManager.getResourceValue(textureAtlasId);

				if (sprite == null) {
					sprite = textureAtlas.createSprite(regionId);
					if (sprite == null) {
						throw new RuntimeException("Failed to create Sprite resource " + id + " from region " + regionId + " from texture atlas " + textureAtlasId);
					}
				}

				if (sprite instanceof AtlasSprite) {
					return new AtlasSprite(((AtlasSprite) sprite).getAtlasRegion());
				} else {
					return new Sprite(sprite);
				}
			}
		});
	}

	public void animation(final String id, final String textureAtlasId, final String prefix, final boolean loop, final int time, final int... times) {
		animation(id, textureAtlasId, prefix, -1, -1, loop, time, times);
	}

	public void animation(final String id, final String textureAtlasId, final String prefix, final boolean loop, boolean removeAlias, final int time, final int... times) {
		animation(id, textureAtlasId, prefix, -1, -1, loop, removeAlias, time, times);
	}

	public void animation(final String id, final String textureAtlasId, final String prefix, final int sf, final int ef, final boolean loop, final int time, final int... times) {
		animation(id, textureAtlasId, prefix, sf, ef, loop, true, time, times);
	}

	public void animation(final String id, final String textureAtlasId, final String prefix, final int sf, final int ef, final boolean loop, final boolean removeAlias, final int time, final int... times) {
		float ftime = 0.001f * time;
		float[] ftimes = null;
		// if (times != null) {
		ftimes = new float[times.length];
		for (int i = 0; i < ftimes.length; i++) {
			ftimes[i] = 0.001f * times[i];
		}
		// }
		animation(id, textureAtlasId, prefix, sf, ef, loop, removeAlias, ftime, ftimes);
	}

	public void animation(final String id, final String textureAtlasId, final String prefix, final int sf, final int ef, final boolean loop, final boolean removeAlias, final float time, final float... times) {
		resourceManager.addVolatile(id, new DataLoader<Animation>() {

			class DuplicatedSpritesRemover {

				float[] times;
				Sprite[] frames;

				public void removeDuplicates(Sprite[] frames, float[] times) {

					ArrayList<Sprite> newSprites = new ArrayList<Sprite>();
					ArrayList<FloatValue> newTimes = new ArrayList<FloatValue>();

					Sprite lastSprite = null;
					int i = 0;

					FloatValue frameTime = new FloatValue(0);

					do {
						Sprite sprite = frames[i];

						if (SpriteUtils.isAliasSprite(lastSprite, sprite)) {
							frameTime.value += times[i];
						} else {
							newSprites.add(sprite);
							lastSprite = sprite;

							frameTime = new FloatValue(times[i]);
							newTimes.add(frameTime);
						}
						i++;
					} while (i < frames.length);

					this.frames = new Sprite[newSprites.size()];
					this.times = new float[newTimes.size()];

					newSprites.toArray(this.frames);

					for (i = 0; i < newTimes.size(); i++) {
						this.times[i] = newTimes.get(i).value;
					}
				}

			}

			FrameAnimationImpl cachedFrameAnimation = null;
			Animation cachedAnimation = null;

			@Override
			public Animation load() {
				if (cachedAnimation == null) {
					TextureAtlas textureAtlas = resourceManager.getResourceValue(textureAtlasId);

					Array<Sprite> sprites = null;

					try {
						sprites = textureAtlas.createSprites(prefix);
					} catch (GdxRuntimeException e) {
						throw new RuntimeException("Failed to create animation " + id + " from texture atlas " + textureAtlasId, e);
					}

					if (sprites.size == 0) {
						throw new IllegalArgumentException("Failed to create animation " + id + ", no regions found for prefix " + prefix);
					}

					int endFrame = ef;
					int startFrame = sf;

					if (endFrame == -1) {
						endFrame = sprites.size - 1;
					}

					if (startFrame == -1) {
						startFrame = 0;
					}

					Sprite[] frames = new Sprite[endFrame - startFrame + 1];
					int frameNumber = startFrame;

					if (endFrame >= sprites.size) {
						throw new IllegalArgumentException("Failed to create animation " + id + ", end frame " + endFrame + " couldn't be greater than sprites quantity " + sprites.size);
					}

					int framesCount = frames.length;

					float[] newTimes = new float[framesCount];
					// newTimes[0] = 0.001f * (float) time;
					newTimes[0] = time;
					float lastTime = newTimes[0];

					// added convert from int time in milliseconds to float time in seconds

					for (int i = 1; i < framesCount; i++) {
						if (i < times.length) {
							// newTimes[i] = ((float) times[i]) * 0.001f;
							newTimes[i] = times[i];
							lastTime = newTimes[i];
						} else {
							newTimes[i] = lastTime;
						}
					}

					for (int i = 0; i < frames.length; i++) {
						Sprite sprite = sprites.get(frameNumber);
						if (sprite instanceof AtlasSprite) {
							frames[i] = new AtlasSprite(((AtlasSprite) sprite).getAtlasRegion());
						} else {
							frames[i] = new Sprite(sprite);
						}
						frameNumber++;
					}

					if (removeAlias) {
						int framesBeforeRemoval = frames.length;
						DuplicatedSpritesRemover duplicatedSpritesRemover = new DuplicatedSpritesRemover();
						duplicatedSpritesRemover.removeDuplicates(frames, newTimes);
						frames = duplicatedSpritesRemover.frames;
						newTimes = duplicatedSpritesRemover.times;
						Gdx.app.log("commons-gdx", "[" + id + "] frames removed: " + (framesBeforeRemoval - frames.length));
					}

					cachedFrameAnimation = new FrameAnimationImpl(newTimes);
					cachedFrameAnimation.setLoop(loop);

					cachedAnimation = new Animation(frames, cachedFrameAnimation);
				}

				Sprite[] frames = new Sprite[cachedAnimation.getFramesCount()];

				for (int i = 0; i < frames.length; i++) {
					Sprite sprite = cachedAnimation.getFrame(i);
					if (sprite instanceof AtlasSprite) {
						frames[i] = new AtlasSprite(((AtlasSprite) sprite).getAtlasRegion());
					} else {
						frames[i] = new Sprite(sprite);
					}
				}

				return new Animation(frames, new FrameAnimationImpl(cachedFrameAnimation));
			}

		});
	}

	public void animation(String id, final String spriteSheetId, final int x, final int y, final int w, final int h, final int framesCount, //
			final boolean loop, final int time, final int... times) {
		resourceManager.addVolatile(id, new DataLoader<Animation>() {

			@Override
			public Animation load() {
				Texture spriteSheet = resourceManager.getResourceValue(spriteSheetId);
				Sprite[] frames = new Sprite[framesCount];

				int xOffset = 0;
				int yOffset = 0;

				for (int i = 0; i < frames.length; i++) {
					frames[i] = new Sprite(spriteSheet, x + xOffset, y + yOffset, w, h);

					xOffset += w;

					if (xOffset >= spriteSheet.getWidth()) {
						yOffset += h;
						xOffset = 0;
					}
				}

				float[] newTimes = new float[framesCount - 1];
				int lastTime = time;

				// added convert from int time in milliseconds to float time in seconds

				for (int i = 0; i < framesCount - 1; i++) {
					if (i < times.length) {
						newTimes[i] = times[i] * 0.001f;
						lastTime = times[i];
					} else {
						newTimes[i] = lastTime * 0.001f;
					}
				}

				FrameAnimationImpl frameAnimation = new FrameAnimationImpl(0.001f * time, newTimes);
				frameAnimation.setLoop(loop);

				return new Animation(frames, frameAnimation);
			}

		});
	}

	public void font(String id, String imageFile, String fontFile) {
		font(id, imageFile, fontFile, false);
	}

	public void font(String id, final String imageFile, final String fontFile, final boolean linearFilter) {
		resourceManager.add(id, new DisposableDataLoader<BitmapFont>(internal(imageFile)) {
			@Override
			public BitmapFont load() {
				Texture texture = new Texture(internal(imageFile));
				if (linearFilter) {
					texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				}
				return new BitmapFont(internal(fontFile), new Sprite(texture), false);
			}
		});
	}

	public void sound(String id, String file) {
		sound(id, internal(file));
	}

	public void sound(String id, FileHandle fileHandle) {
		resourceManager.add(id, new SoundDataLoader(fileHandle, resourceManager));
	}

	public void sound(String id, String metadataResourceId, String file) {
		sound(id, metadataResourceId, internal(file));
	}

	public void sound(String id, String metadataResourceId, FileHandle fileHandle) {
		resourceManager.add(id, new SoundDataLoader(fileHandle, resourceManager).durationMetadata(metadataResourceId));
	}

	public void music(String id, String file) {
		music(id, internal(file));
	}

	public void music(String id, FileHandle fileHandle) {
		resourceManager.add(id, new MusicDataLoader(fileHandle));
	}

	public void xmlDocument(String id, final String file) {
		resourceManager.add(id, new DataLoader<Document>() {
			@Override
			public Document load() {
				return new DocumentParser().parse(internal(file).read());
			}
		});
	}

	public void particleEffect(String id, final String effectFile, final String imagesDir) {
		resourceManager.add(id, new DataLoader<ParticleEffect>() {
			@Override
			public ParticleEffect load() {
				ParticleEffect particleEffect = new ParticleEffect();
				particleEffect.load(Gdx.files.internal(effectFile), Gdx.files.internal(imagesDir));
				return particleEffect;
			}

			@Override
			public void unload(ParticleEffect t) {
				t.dispose();
			}
		});
	}

	public void particleEmitter(String id, final String particleEffectId, final String particleEmitterId) {
		this.particleEmitter(id, particleEffectId, particleEmitterId, 1f);
	}

	public void particleEmitter(String id, final String particleEffectId, final String particleEmitterId, final float scale) {
		resourceManager.addVolatile(id, new DataLoader<ParticleEmitter>() {

			private ParticleEmitter cachedEmitter;

			@Override
			public ParticleEmitter load() {
				ParticleEffect particleEffect = resourceManager.getResourceValue(particleEffectId);
				if (cachedEmitter == null) {
					cachedEmitter = particleEffect.findEmitter(particleEmitterId);
					ParticleEmitterUtils.scaleEmitter(cachedEmitter, scale);
				}
				return new ParticleEmitter(cachedEmitter);
			}
		});
	}

	// / TESTING STUFF

	@SuppressWarnings({ "rawtypes" })
	public void resource(final String id, final ResourceBuilder resourceBuilder) {

		DataLoader dataLoader = new DataLoader() {
			@Override
			public Object load() {
				try {
					return resourceBuilder.build();
				} catch (Exception e) {
					throw new RuntimeException("Failed to load resource " + id, e);
				}
			}

			@Override
			public void unload(Object data) {
				if (data instanceof Disposable) {
					((Disposable) data).dispose();
				}
			}
		};

		if (resourceBuilder.isVolatile()) {
			resourceManager.addVolatile(id, dataLoader);
		} else {
			resourceManager.add(id, dataLoader);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void resource(final String id, final DataLoaderBuilder dataLoaderBuilder) {
		if (dataLoaderBuilder.isVolatile()) {
			resourceManager.addVolatile(id, dataLoaderBuilder.build(resourceManager));
		} else {
			resourceManager.add(id, dataLoaderBuilder.build(resourceManager));
		}
	}

	public SpriteResourceBuilder sprite2() {
		return new SpriteResourceBuilder(resourceManager);
	}

	public static TextureResourceBuilder texture2(FileHandle fileHandle) {
		return new TextureResourceBuilder(fileHandle);
	}

	public AnimationResourceBuilder animation2(String textureId) {
		return new AnimationResourceBuilder(resourceManager, textureId);
	}

	public AnimationFromTextureAtlasResourceBuilder animationFromTextureAtlas(String textureAtlasId) {
		return new AnimationFromTextureAtlasResourceBuilder(resourceManager, textureAtlasId);
	}

	public XmlDocumentResourceBuilder xmlDocument(String file) {
		return xmlDocument(internal(file));
	}

	public XmlDocumentResourceBuilder xmlDocument(FileHandle file) {
		return new XmlDocumentResourceBuilder(resourceManager).file(file);
	}

	public XmlSchemaResourceBuilder xmlSchema() {
		return new XmlSchemaResourceBuilder();
	}

	public FontResourceBuilder font2(String imageFile, String fontFile) {
		return new FontResourceBuilder(resourceManager).imageFile(internal(imageFile)).fontFile(internal(fontFile));
	}

	public FontResourceBuilder font2(FileHandle imageFile, FileHandle fontFile) {
		return new FontResourceBuilder(resourceManager).imageFile(imageFile).fontFile(fontFile);
	}

	public FontResourceBuilder font2() {
		return new FontResourceBuilder(resourceManager);
	}

	public SkinResourceBuilder skin(String skinFile, String textureAtlasResourceId) {
		return new SkinResourceBuilder(resourceManager) //
				.skinFile(internal(skinFile)) //
				.textureAtlas(textureAtlasResourceId); //
		// .textureFile(internal(textureFile) //
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> ResourceBuilder<T> alias(String resourceId) {
		return new AliasResourceBuilder(resourceManager, resourceId);
	}

	public <T> DataLoaderBuilder<T> alias2(final String resourceId) {
		return new AliasDataLoaderBuilder<T>(resourceId);
	}

}