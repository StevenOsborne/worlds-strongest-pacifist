package com.steven.osborne.test.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steven.osborne.test.game.input.ControllerActionManager;
import com.steven.osborne.test.game.input.InputActionManager;
import com.steven.osborne.test.game.screen.GameScreen;
import com.steven.osborne.test.game.screen.MainMenuScreen;
import com.steven.osborne.test.game.screen.ScreenName;
import com.steven.osborne.test.game.system.InputSystem;
import com.steven.osborne.test.game.system.RendererSystem;

public class WorldsStrongestPacifist extends Game {
	public static final float PIXELS_TO_METERS = 1.0f / 32.0f;
	public static final float VIRTUAL_WIDTH = 1920 * PIXELS_TO_METERS; //60.0
	public static final float VIRTUAL_HEIGHT = 1080 * PIXELS_TO_METERS; //33.75

	private GameScreen gameScreen;
	private MainMenuScreen mainMenuScreen;

	private Engine engine;
	private OrthographicCamera guiCamera;
	private Viewport viewport;

	private InputActionManager inputActionManager;
	private ControllerActionManager controllerActionManager;
	
	@Override
	public void create () {
		engine = new Engine();
		guiCamera = new OrthographicCamera();
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport.apply();
		inputActionManager = new InputActionManager();
		controllerActionManager = new ControllerActionManager();
		gameScreen = new GameScreen(this, engine, viewport, guiCamera);
		mainMenuScreen = new MainMenuScreen(this, engine);

		initialiseSystems();

		setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}

	public void switchScreen(ScreenName screenName) {
		switch (screenName) {
			case MAIN_MENU:
				setScreen(mainMenuScreen);
				break;
			case GAME:
				setScreen(gameScreen);
				break;
		}
	}

	private void initialiseSystems() {
		RendererSystem renderer = new RendererSystem((OrthographicCamera) viewport.getCamera(), guiCamera);
		renderer.setBackgroundColour(new Vector3(0, 0.05f, 0.1f));
		InputSystem inputSystem = new InputSystem();

		engine.addSystem(renderer);
		engine.addSystem(inputSystem);
		inputActionManager.subscribe(inputSystem);
		controllerActionManager.subscribe(inputSystem);
	}
}
