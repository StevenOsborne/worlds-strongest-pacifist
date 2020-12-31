package com.steven.osborne.test.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steven.osborne.test.game.TestGame;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.factory.BarbellFactory;
import com.steven.osborne.test.game.factory.EnemyFactory;
import com.steven.osborne.test.game.input.ControllerActionManager;
import com.steven.osborne.test.game.input.InputActionManager;
import com.steven.osborne.test.game.listener.CollisionListener;
import com.steven.osborne.test.game.system.*;

import java.util.Arrays;
import java.util.Collections;

public class GameScreen extends ScreenAdapter {
    public static final float PIXELS_TO_METERS = 1.0f / 32.0f;
    public static final float VIRTUAL_WIDTH = 1920 * PIXELS_TO_METERS; //60.0
    public static final float VIRTUAL_HEIGHT = 1080 * PIXELS_TO_METERS; //33.75

    private Engine engine;
    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;

    private InputActionManager inputActionManager;
    private ControllerActionManager controllerActionManager;

    public GameScreen(TestGame testGame) {
        inputActionManager = new InputActionManager();
        controllerActionManager = new ControllerActionManager();
        engine = new Engine();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        world = new World(new Vector2(0f, 0f), true);
        world.setContactListener(new CollisionListener());

        initialiseEntities();
        initialiseSystems();
    }

    private void initialiseEntities() {
        createPlayer();
        createSpawners();
        createBoundary();
    }

    private void initialiseSystems() {
        RendererSystem renderer = new RendererSystem(camera);
        renderer.setBackgroundColour(new Vector3(0, 0.05f, 0.1f));
        MovementSystem movementSystem = new MovementSystem();
        CameraSystem cameraSystem = new CameraSystem(camera);
        InputSystem inputSystem = new InputSystem();
        BoundsSystem boundsSystem = new BoundsSystem();
        CollisionSystem collisionSystem = new CollisionSystem();
        DeathSystem deathSystem = new DeathSystem(world);
        SpawnSystem spawnSystem = new SpawnSystem();
        AiSystem aiSystem = new AiSystem();
        ExplosionSystem explosionSystem = new ExplosionSystem(world);
        ParentSystem parentSystem = new ParentSystem();
        LifetimeSystem lifetimeSystem = new LifetimeSystem();
        PhysicsSystem physicsSystem = new PhysicsSystem(world);
        PhysicsDebugSystem physicsDebugSystem = new PhysicsDebugSystem(world, camera);
        engine.addSystem(renderer);
//        engine.addSystem(physicsDebugSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(inputSystem);
        engine.addSystem(parentSystem);
        engine.addSystem(explosionSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(deathSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(boundsSystem);
        engine.addSystem(spawnSystem);
        engine.addSystem(aiSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(lifetimeSystem);
        inputActionManager.subscribe(inputSystem);
        controllerActionManager.subscribe(inputSystem);
    }

    private void createPlayer() {
        Entity player = new Entity();
        Texture playerTexture = new Texture("player.png");
        player.add(SpriteComponent.builder().texture(playerTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        player.add(PositionComponent.builder().x(0f).y(0f).build());
        player.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        player.add(BoundsComponent.builder().rectangle(new Rectangle(0f, 0f, 1, 1)).build());
        player.add(InputComponent.builder().build());
        player.add(CameraFollowComponent.builder().build());
        player.add(CollisionComponent.builder().tag("Player").isStatic(false).collideTags(Arrays.asList("Wall")).destroyTags(Arrays.asList("Barbell", "Multiplier")).build());
        player.add(HealthComponent.builder().health(1).build());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0f, 0f);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.friction = 0.0f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(player);

        BodyComponent bodyComponent = BodyComponent.builder()
                .body(body)
                .build();

        box.dispose();

        player.add(bodyComponent);
        engine.addEntity(player);
    }

    private void createSpawners() {
        Entity enemySpawner = new Entity();
        enemySpawner.add(SpawnComponent.builder()
                .factory(new EnemyFactory(world))
                .amount(5)
                .delay(5f)
                .amountIncrement(1)
                .delayDecrement(0.5f)
                .minimumDelay(1f)
                .maximumAmount(20)
                .spawnAreas(Arrays.asList(new Rectangle(-31.5f, 7.5f, 10f, 10f),
                        new Rectangle(21.5f, 7.5f, 10f, 10f),
                        new Rectangle(-31.5f, -17.5f, 10f, 10f),
                        new Rectangle(21.5f, -17.5f, 10f, 10f)))
                .build());

        Entity barbellSpawner = new Entity();
        barbellSpawner.add(SpawnComponent.builder()
                .factory(new BarbellFactory(world))
                .amount(1)
                .delay(5f)
                .amountIncrement(0)
                .delayDecrement(0.5f)
                .minimumDelay(1f)
                .maximumAmount(1)
                .spawnAreas(Collections.singletonList(new Rectangle(-31f, -17f, 62f, 34f)))
                .build());

        engine.addEntity(enemySpawner);
        engine.addEntity(barbellSpawner);
    }

    private void createBoundary() {
        Entity leftWall = new Entity();
        leftWall.add(PositionComponent.builder().x(-33f).y(-19f).build());
        leftWall.add(BoundsComponent.builder().rectangle(new Rectangle(-33f, -19f, 1f, 38f)).build());
        leftWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        leftWall.add(BodyComponent.builder().body(createBody(new Vector2(-33f, 0f), 1f, 18f, leftWall)).build());

        Entity rightWall = new Entity();
        rightWall.add(PositionComponent.builder().x(32f).y(-19f).build());
        rightWall.add(BoundsComponent.builder().rectangle(new Rectangle(32f, -19f, 1f, 38f)).build());
        rightWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        rightWall.add(BodyComponent.builder().body(createBody(new Vector2(33f, 0f), 1f, 18f, rightWall)).build());

        Entity topWall = new Entity();
        topWall.add(PositionComponent.builder().x(-33f).y(18f).build());
        topWall.add(BoundsComponent.builder().rectangle(new Rectangle(-33f, 18f, 66f, 1f)).build());
        topWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        topWall.add(BodyComponent.builder().body(createBody(new Vector2(0f, 19f), 32f, 1f, topWall)).build());

        Entity bottomWall = new Entity();
        bottomWall.add(PositionComponent.builder().x(-33f).y(-19f).build());
        bottomWall.add(BoundsComponent.builder().rectangle(new Rectangle(-33f, -19f, 66f, 1f)).build());
        bottomWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        bottomWall.add(BodyComponent.builder().body(createBody(new Vector2(0f, -19f), 32f, 1f, bottomWall)).build());

        engine.addEntity(leftWall);
        engine.addEntity(rightWall);
        engine.addEntity(topWall);
        engine.addEntity(bottomWall);
    }

    private Body createBody(Vector2 position, float halfX, float halfY, Entity entity) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(halfX, halfY);
        body.createFixture(box, 0.0f);
        body.setUserData(entity);
        box.dispose();

        return body;
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
