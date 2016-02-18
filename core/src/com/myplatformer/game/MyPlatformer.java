package com.myplatformer.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;

import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MyPlatformer extends ApplicationAdapter {

	SceneLoader sceneLoader;
	Viewport viewport;
	ItemWrapper root;
	Player player;
	End end;
	BitmapFont font;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	String currentScene;
	DecimalFormat df;
	Vector3 position;
	RayHandler rayHandler;

	private UIStage uiStage;
	float lerp = 0.1f;
	public GameState currentState;

	public enum GameState { PLAY, PAUSE, MENU }
	private Vector2 camera;


	@Override
	public void create () {
		viewport = new FitViewport(240,135);
		font = new BitmapFont();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		df = new DecimalFormat("#");
		df.setRoundingMode(RoundingMode.CEILING);
		currentScene = "MainScene";
		loadLevel();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (currentState) {
			case PAUSE:
				pauseRender();
				if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
					currentState = GameState.PLAY;
			break;

			case PLAY:
			default:
				playRender();
				if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
					currentState = GameState.PAUSE;
				break;
		}

		batch.begin();
		if(currentState == GameState.PAUSE)
			font.draw(batch, "PAUSED!", 50, 170);
		font.draw(batch, "Momentum: " + player.getMomentum(), 50, 150);
		font.draw(batch, "PlayerX: " + df.format(player.getX()), 50, 130);
		font.draw(batch, "PlayerY: " + df.format(player.getY()), 50, 110);
		font.draw(batch, "EndX: " + df.format(end.getX()), 50, 90);
		font.draw(batch, "EndY: " + df.format(end.getY()), 50, 70);
		batch.end();

		//Smooth camera
		position = viewport.getCamera().position;
		position.x += (player.getX()+8 - position.x) * lerp;
		position.y += (player.getY()+8 - position.y) * lerp;
		((OrthographicCamera)viewport.getCamera()).position.set(position.x, position.y, 0);

	}

	private void playRender() {
		sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
		rayHandler.updateAndRender();

		if(player.getY() < 0 || (Math.round(player.getX()) == Math.round(end.getX()) && Math.round(player.getY()) == Math.round(end.getY())))
			loadLevel();

	}

	private void pauseRender() {
		sceneLoader.getEngine().update(0);
		rayHandler.updateAndRender();

		if(!uiStage.isPaused()) {
			currentState = GameState.PLAY;
		}


		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.35f);
		shapeRenderer.rect(0,0,960,540);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		uiStage.act();
		uiStage.draw();
		uiStage.drawText();



		//Sharp camera
		//((OrthographicCamera)viewport.getCamera()).position.set(player.getX() + 8, player.getY() + 8, 0);
	}

	private void loadLevel() {
		sceneLoader = new SceneLoader();
		uiStage = new UIStage(sceneLoader.getRm());
		currentState = GameState.PLAY;
		sceneLoader.loadScene(currentScene, viewport);

		root = new ItemWrapper(sceneLoader.getRoot());

		/*if(currentScene == "MenuScene") {
			sceneLoader.addComponentsByTagName("button", ButtonComponent.class);
			root.getChild("play").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
				@Override
				public void touchUp() {

				}

				@Override
				public void touchDown() {

				}

				@Override
				public void clicked() {

					currentScene = "MainScene";
					loadLevel();
				}
			});

		}*/

		rayHandler = new RayHandler(sceneLoader.world);

		rayHandler.setAmbientLight(1.0f, 3.83137256f, 1.36862746f, 0.9f);

		player = new Player(sceneLoader.world);
		root.getChild("player").addScript(player);
		end = new End(sceneLoader.world);
		root.getChild("end").addScript(end);

		camera = new Vector2(player.getX(),player.getY());
	}


}
