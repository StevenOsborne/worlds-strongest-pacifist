package com.steven.osborne.test.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steven.osborne.test.game.TestGame;
import com.steven.osborne.test.game.gameobject.component.*;
import com.steven.osborne.test.game.input.ControllerActionManager;
import com.steven.osborne.test.game.input.InputActionManager;
import com.steven.osborne.test.game.system.*;

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
        Texture playerTexture = new Texture("player.png");
        player.add(SpriteComponent.builder().withTexture(playerTexture).withVisible(true).build());//TODO - This should use a texture atlas - When we have more textures
        player.add(PositionComponent.builder().withX(0f).withY(0f).build());
        player.add(VelocityComponent.builder().withX(0.0f).withY(0.0f).build());
        player.add(BoundsComponent.builder().withBounds(new Rectangle(0f, 0f, 1, 1)).build());//TODO - need to change this
        player.add(InputComponent.builder().build());
        player.add(CameraFollowComponent.builder().build());
        player.add(CollisionComponent.builder().withTag("Player").withIsStatic(false).build());

        createBoundary();

        engine.addEntity(player);
    }

    private void initialiseSystems() {//Should this live in its own class?
        RendererSystem renderer = new RendererSystem(camera);
        renderer.setBackgroundColour(new Vector3(0, 0.05f, 0.1f));
        MovementSystem movementSystem = new MovementSystem();
        CameraSystem cameraSystem = new CameraSystem(camera);
        InputSystem inputSystem = new InputSystem();
        BoundsSystem boundsSystem = new BoundsSystem();
        CollisionSystem collisionSystem = new CollisionSystem();
        engine.addSystem(movementSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(inputSystem);
        engine.addSystem(boundsSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(renderer);
        inputActionManager.subscribe(inputSystem);
        controllerActionManager.subscribe(inputSystem);
    }

    private void createBoundary() {
        Entity leftWall = new Entity();
        leftWall.add(PositionComponent.builder().withX(-33f).withY(-19f).build());
        leftWall.add(BoundsComponent.builder().withBounds(new Rectangle(-33f, -19f, 1f, 38f)).build());
        leftWall.add(CollisionComponent.builder().withIsStatic(true).withTag("Wall").build());

        Entity rightWall = new Entity();
        rightWall.add(PositionComponent.builder().withX(32f).withY(-19f).build());
        rightWall.add(BoundsComponent.builder().withBounds(new Rectangle(32f, -19f, 1f, 38f)).build());
        rightWall.add(CollisionComponent.builder().withIsStatic(true).withTag("Wall").build());

        Entity topWall = new Entity();
        topWall.add(PositionComponent.builder().withX(-33f).withY(18f).build());
        topWall.add(BoundsComponent.builder().withBounds(new Rectangle(-33f, 18f, 66f, 1f)).build());
        topWall.add(CollisionComponent.builder().withIsStatic(true).withTag("Wall").build());

        Entity bottomWall = new Entity();
        bottomWall.add(PositionComponent.builder().withX(-33f).withY(-19f).build());
        bottomWall.add(BoundsComponent.builder().withBounds(new Rectangle(-33f, -19f, 66f, 1f)).build());
        bottomWall.add(CollisionComponent.builder().withIsStatic(true).withTag("Wall").build());

        engine.addEntity(leftWall);
        engine.addEntity(rightWall);
        engine.addEntity(topWall);
        engine.addEntity(bottomWall);
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
