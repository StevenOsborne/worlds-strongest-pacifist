package com.steven.osborne.test.game.event;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.steven.osborne.test.game.component.*;

import java.util.Arrays;

public class EnemyOnDeathEvent implements OnDeathEvent {

    private final World world;
    private final MultiplierOnDeathEvent multiplierOnDeathEvent;

    public EnemyOnDeathEvent(World world) {
        this.world = world;
        multiplierOnDeathEvent = new MultiplierOnDeathEvent();
    }

    @Override
    public void execute(Engine engine, Entity entity) {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        Entity multiplier = new Entity();
        Texture multiplierTexture = new Texture("multiplier.png");
        multiplier.add(SpriteComponent.builder().texture(multiplierTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        multiplier.add(PositionComponent.builder().x(positionComponent.getX()).y(positionComponent.getY()).build());
        multiplier.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        multiplier.add(CollisionComponent.builder().tag("Multiplier").isStatic(false).collideTags(Arrays.asList("Wall")).build());
        multiplier.add(HealthComponent.builder().health(1).build());
        multiplier.add(LifetimeComponent.builder().lifetime(5f).build());
        multiplier.add(AiComponent.builder().speed(25f).range(new Circle(positionComponent.getX(), positionComponent.getY(), 5f)).build());
        multiplier.add(BodyComponent.builder().body(createBody(new Vector2(positionComponent.getX(), positionComponent.getY()),0.1f, 0.15f, multiplier)).build());
        multiplier.add(OnDeathComponent.builder().onDeathEvent(multiplierOnDeathEvent).build());

        Entity points = new Entity();
        points.add(PointsComponent.builder().basePoints(10).build());
        points.add(HealthComponent.builder().health(0).build());

        engine.addEntity(points);
        engine.addEntity(multiplier);
    }

    private Body createBody(Vector2 position, float halfX, float halfY, Entity entity) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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
