package com.steven.osborne.test.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steven.osborne.test.game.TestGame;
import com.steven.osborne.test.game.gameobject.component.InputComponent;
import com.steven.osborne.test.game.gameobject.component.PositionComponent;
import com.steven.osborne.test.game.gameobject.component.SpriteComponent;
import com.steven.osborne.test.game.gameobject.component.VelocityComponent;
import com.steven.osborne.test.game.input.ControllerActionManager;
import com.steven.osborne.test.game.input.InputActionManager;
import com.steven.osborne.test.game.system.InputSystem;
import com.steven.osborne.test.game.system.MovementSystem;
import com.steven.osborne.test.game.system.RendererSystem;

public class GameScreen extends ScreenAdapter {
    public static final float PIXELS_TO_METERS = 1.0f / 32.0f;
    public static final float VIRTUAL_WIDTH = 1920 * PIXELS_TO_METERS; //60.0
    public static final float VIRTUAL_HEIGHT = 1080 * PIXELS_TO_METERS; //33.75

    private Engine engine;
    private OrthographicCamera camera;
    private Viewport viewport;

    private InputActionManager inputActionManager; //TODO - Should this be declared here?
    private ControllerActionManager controllerActionManager; //TODO - Should this be declared here?

    public GameScreen(TestGame testGame) {
        inputActionManager = new InputActionManager();
        controllerActionManager = new ControllerActionManager();
        engine = new Engine();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        initialiseEntities();
        initialiseSystems();
    }

    private void initialiseEntities() {//Should this live in its own class?
        Entity player = new Entity();
        player.add(SpriteComponent.builder().withTexture(new Texture("player.png")).withVisible(true).build());//TODO - This should use a texture atlas - When we have more textures
        player.add(PositionComponent.builder().withX(0f).withY(0f).build());
        player.add(VelocityComponent.builder().withX(0.0f).withY(0.0f).build());
        player.add(InputComponent.builder().build());
        engine.addEntity(player);
    }

    private void initialiseSystems() {//Should this live in its own class?
        RendererSystem renderer = new RendererSystem(camera);
        renderer.setBackgroundColour(new Vector3(0, 0.05f, 0.1f));
        MovementSystem movementSystem = new MovementSystem();
        InputSystem inputSystem = new InputSystem();
        engine.addSystem(renderer);
        engine.addSystem(movementSystem);
        engine.addSystem(inputSystem);
        inputActionManager.subscribe(inputSystem);
        controllerActionManager.subscribe(inputSystem);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height);
    }
}
