package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.event.BarbellOnDeathEvent;

import java.util.Arrays;

public class BarbellFactory implements EntityFactory {

    private World world;
    private BarbellOnDeathEvent barbellOnDeathEvent;

    public BarbellFactory(World world) {
        this.world = world;
        barbellOnDeathEvent = new BarbellOnDeathEvent();
    }

    @Override
    public void create(Engine engine, Vector2 position) {
        Entity barbellMiddle = new Entity();
        Texture barbellMiddleTexture = new Texture("sprites/barbell_middle.png");
        barbellMiddle.add(SpriteComponent.builder().texture(barbellMiddleTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbellMiddle.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbellMiddle.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbellMiddle.add(CollisionComponent.builder().tag("Barbell").isStatic(false).build());
        barbellMiddle.add(HealthComponent.builder().health(1).build());
        barbellMiddle.add(BodyComponent.builder().body(createBody(position, 3f, 0.125f, barbellMiddle)).build());

        Entity barbellLeft = new Entity();
        Texture barbellEndTexture = new Texture("sprites/barbell_end.png");
        barbellLeft.add(SpriteComponent.builder().texture(barbellEndTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbellLeft.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbellLeft.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbellLeft.add(CollisionComponent.builder().tag("BarbellEnd").isStatic(false).destroyTags(Arrays.asList("Player")).build());
        barbellLeft.add(OnDeathComponent.builder().onDeathEvent(barbellOnDeathEvent).build());
        barbellLeft.add(HealthComponent.builder().health(1).build());
        barbellLeft.add(ParentComponent.builder().parent(barbellMiddle).build());
        barbellLeft.add(BodyComponent.builder().body(createBody(new Vector2(position.x -3.375f, position.y), 0.375f, 0.25f, barbellLeft)).build());

        Entity barbellRight = new Entity();
        barbellRight.add(SpriteComponent.builder().texture(barbellEndTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbellRight.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbellRight.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbellRight.add(CollisionComponent.builder().tag("BarbellEnd").isStatic(false).destroyTags(Arrays.asList("Player")).build());
        barbellRight.add(OnDeathComponent.builder().onDeathEvent(barbellOnDeathEvent).build());
        barbellRight.add(HealthComponent.builder().health(1).build());
        barbellRight.add(ParentComponent.builder().parent(barbellMiddle).build());
        barbellRight.add(BodyComponent.builder().body(createBody(new Vector2(position.x + 3.375f, position.y), 0.375f, 0.25f, barbellRight)).build());

        engine.addEntity(barbellMiddle);
        engine.addEntity(barbellLeft);
        engine.addEntity(barbellRight);
    }

    private Body createBody(Vector2 position, float halfX, float halfY, Entity entity) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position.x, position.y);

        PolygonShape box = new PolygonShape();
        box.setAsBox(halfX, halfY);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.isSensor = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(entity);

        box.dispose();

        return body;
    }
}
