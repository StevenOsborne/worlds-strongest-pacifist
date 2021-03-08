package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.steven.osborne.test.game.component.*;

import java.util.Arrays;

public class PlayerFactory implements EntityFactory {

    private final World world;
    private final Texture playerTexture;

    public PlayerFactory(World world) {
        this.world = world;
        playerTexture = new Texture("sprites/player.png");
    }

    @Override
    public void create(Engine engine, Vector2 position) {
        Entity player = new Entity();
        player.add(SpriteComponent.builder().texture(playerTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        player.add(PositionComponent.builder().x(position.x).y(position.y).build());
        player.add(VelocityComponent.builder().speed(20f).velocity(new Vector2()).build());
        player.add(InputComponent.builder().controllerDeadZone(0.2f).build());
        player.add(CameraFollowComponent.builder().build());
        player.add(CollisionComponent.builder().tag("Player").isStatic(false).destroyTags(Arrays.asList("Barbell", "Multiplier")).build());
        player.add(HealthComponent.builder().health(1).build());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

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

    @Override
    public void dispose() {
        playerTexture.dispose();
    }
}
