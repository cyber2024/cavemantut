package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;

public class MyGdxGame extends InputAdapter implements ApplicationListener {
	private static final Color BACKGROUND_COLOR = new Color(0.38f, 0.28f, 0.18f, 1f);
	private static final float WORLD_TO_SCREEN = 1f/100f;
	private static final float SCENE_WIDTH = 12.80f, SCENE_HEIGHT = 7.20f;
	private static final float FRAME_DURATION = 1f/30f;

	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batcher;
	private TextureAtlas cavemanAtlas, trexAtlas;
	private Texture background;
	private Animation cavemanWalk, dinosaurWalk;
	private float animationTime;
	private Array<Color> colors;
	private int currentColor;
	private Vector3 tmp;

	private BitmapFont font;

	int width;
	int height;
	float originX;
	float originY;


	@Override
	public void create () {
		camera = new OrthographicCamera();
		batcher = new SpriteBatch();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		background = new Texture(Gdx.files.internal("jungle-level.png"));
		cavemanAtlas = new TextureAtlas(Gdx.files.internal("caveman.atlas"));
		trexAtlas = new TextureAtlas(Gdx.files.internal("trex.atlas"));
		animationTime = 0f;

		int maxSize = GL20.GL_MAX_TEXTURE_SIZE;
		Gdx.app.log("TextureAtlasSample", "Max texture size: " + maxSize + "x" + maxSize);

		Array<TextureAtlas.AtlasRegion> cavemanRegions = new Array<TextureAtlas.AtlasRegion>(cavemanAtlas.getRegions());
		cavemanRegions.sort(new RegionComparator());
		Array<TextureAtlas.AtlasRegion> trexRegions = new Array<TextureAtlas.AtlasRegion>(trexAtlas.getRegions());
		trexRegions.sort(new RegionComparator());

		cavemanWalk = new Animation(FRAME_DURATION, cavemanRegions, Animation.PlayMode.LOOP);
		dinosaurWalk = new Animation(FRAME_DURATION, trexRegions, Animation.PlayMode.LOOP);

		tmp = new Vector3();

		Gdx.input.setInputProcessor(this);

		font = new BitmapFont(Gdx.files.internal("oswald-64.fnt"));
		font.setColor(Color.BLACK);

		camera.position.set(SCENE_WIDTH*0.5f, SCENE_HEIGHT * 0.5f, 0f);


	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		animationTime += Gdx.graphics.getDeltaTime();

		camera.update();

		batcher.setProjectionMatrix(camera.combined);

		int width = background.getWidth();
		int height = background.getHeight();

		batcher.begin();
		batcher.draw(background, 0, 0, 0, 0, width, height, WORLD_TO_SCREEN, WORLD_TO_SCREEN, 0, 0, 0, width, height, false, false);

		TextureRegion cavemanFrame = cavemanWalk.getKeyFrame(animationTime);
		width = cavemanFrame.getRegionWidth();
		height = cavemanFrame.getRegionHeight();
		float originX = width/2;
		float originY = height/2;

		batcher.draw(cavemanFrame, 1f-originX, 3.7f-originY,
				originX, originY,
				width, height,
				WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				0f);

		TextureRegion dinosaurFrame = dinosaurWalk.getKeyFrame(animationTime);
		width = dinosaurFrame.getRegionWidth();
		height = dinosaurFrame.getRegionHeight();
		originX = width/2;
		originY = height/2;

		batcher.draw(dinosaurFrame, 6.7f - originX, 4.7f - originY,
				originX, originY,
				width, height,
				WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				0f);

		batcher.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batcher.dispose();
		cavemanAtlas.dispose();
		trexAtlas.dispose();
		background.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT){
			currentColor = (currentColor+1)%colors.size;
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){

		} else {

		}
		return true;
	}

	private static class RegionComparator implements Comparator<TextureAtlas.AtlasRegion>{
		@Override
		public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
			return o1.name.compareTo(o2.name);
		}
	}
}
