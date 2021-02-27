package com.steven.osborne.test.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steven.osborne.test.game.WorldsStrongestPacifist;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.factory.BarbellFactory;
import com.steven.osborne.test.game.factory.EnemyFactory;
import com.steven.osborne.test.game.listener.CollisionListener;
import com.steven.osborne.test.game.system.*;

import java.util.Arrays;
import java.util.Collections;

public class GameScreen extends ScreenAdapter {

    private Array<EntitySystem> systems;
    private Engine engine;
    private OrthographicCamera guiCamera;
    private Viewport viewport;
    private World world;

    public GameScreen(WorldsStrongestPacifist worldsStrongestPacifist, Engine engine, Viewport viewport, OrthographicCamera guiCamera) {//TODO - initialise method and reset
        this.engine = engine;
        this.viewport = viewport;
        this.guiCamera = guiCamera;
        systems = new Array<>();
        world = new World(new Vector2(0f, 0f), true);
        world.setContactListener(new CollisionListener());

        createSystems();
    }

    @Override
    public void show () {
        initialiseEntities();
        initialiseSystems();
    }

    private void initialiseEntities() {
        createPlayer();
        createSpawners();
        createBoundary();
    }

    private void createSystems() {
//        systems.add(new PhysicsDebugSystem(world, (OrthographicCamera) viewport.getCamera()));
        systems.add(new PhysicsSystem(world));
        systems.add(new ParentSystem());
        systems.add(new ExplosionSystem(world));
        systems.add(new CollisionSystem());
        systems.add(new ScoreSystem());
        systems.add(new DeathSystem(world));
        systems.add(new MovementSystem());
        systems.add(new SpawnSystem());
        systems.add(new AiSystem());
        systems.add(new CameraSystem((OrthographicCamera) viewport.getCamera()));
        systems.add(new LifetimeSystem());
    }

    private void initialiseSystems() {
        systems.forEach(engine::addSystem);
    }

    private void createPlayer() {
        Entity player = new Entity();
        Texture playerTexture = new Texture("sprites/player.png");
        player.add(SpriteComponent.builder().texture(playerTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        player.add(PositionComponent.builder().x(0f).y(0f).build());
        player.add(VelocityComponent.builder().speed(20f).velocity(new Vector2()).build());
        player.add(InputComponent.builder().controllerDeadZone(0.2f).build());
        player.add(CameraFollowComponent.builder().build());
        player.add(CollisionComponent.builder().tag("Player").isStatic(false).destroyTags(Arrays.asList("Barbell", "Multiplier")).build());
        player.add(HealthComponent.builder().health(1).build());
        player.add(ScoreComponent.builder().score(0L).multiplier(1L).build());

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
        float delay = 5f;
        float delayDecrement = 0.1f;
        Entity enemySpawner = new Entity();
        enemySpawner.add(SpawnComponent.builder()
                .factory(new EnemyFactory(world))
                .amount(5)
                .delay(delay)
                .amountIncrement(1)
                .delayDecrement(delayDecrement)
                .minimumDelay(1f)
                .maximumAmount(20)
                .spawnAreas(Arrays.asList(new Rectangle(-32.5f, 12f, 5f, 5f),//TOP LEFT
                        new Rectangle(26f, 12f, 5f, 5f), //TOP RIGHT
                        new Rectangle(-32.5f, -17.5f, 5f, 5f), //BOTTOM LEFT
                        new Rectangle(26f, -17.5f, 5f, 5f))) //BOTTOM RIGHT
                .build());

        Entity barbellSpawner = new Entity();
        barbellSpawner.add(SpawnComponent.builder()
                .factory(new BarbellFactory(world))
                .amount(1)
                .delay(delay)
                .amountIncrement(0)
                .delayDecrement(delayDecrement)
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
        leftWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        leftWall.add(BodyComponent.builder().body(createBody(new Vector2(-33f, 0f), 1f, 18f, leftWall)).build());

        Entity rightWall = new Entity();
        rightWall.add(PositionComponent.builder().x(32f).y(-19f).build());
        rightWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        rightWall.add(BodyComponent.builder().body(createBody(new Vector2(33f, 0f), 1f, 18f, rightWall)).build());

        Entity topWall = new Entity();
        topWall.add(PositionComponent.builder().x(-33f).y(18f).build());
        topWall.add(CollisionComponent.builder().isStatic(true).tag("Wall").build());
        topWall.add(BodyComponent.builder().body(createBody(new Vector2(0f, 19f), 32f, 1f, topWall)).build());

        Entity bottomWall = new Entity();
        bottomWall.add(PositionComponent.builder().x(-33f).y(-19f).build());
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
//        Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height);
        guiCamera.setToOrtho(false, width, height);
    }
}
