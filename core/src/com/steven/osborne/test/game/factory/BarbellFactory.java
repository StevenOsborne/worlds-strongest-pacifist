package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.event.BarbellOnDeathEvent;

import java.util.Arrays;

public class BarbellFactory implements EntityFactory {

    @Override
    public void create(Engine engine, Vector2 position) {
        Entity barbellMiddle = new Entity();
        Texture barbellMiddleTexture = new Texture("barbell_middle.png");
        barbellMiddle.add(SpriteComponent.builder().texture(barbellMiddleTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbellMiddle.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbellMiddle.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbellMiddle.add(BoundsComponent.builder().rectangle(new Rectangle(position.x, position.y, 4, 0.25f)).build());
        barbellMiddle.add(CollisionComponent.builder().tag("Barbell").isStatic(false).collideTags(Arrays.asList("Wall")).build());
        barbellMiddle.add(HealthComponent.builder().health(1).build());

        BarbellOnDeathEvent barbellOnDeathEvent = new BarbellOnDeathEvent();
        Entity barbellLeft = new Entity();
        Texture barbellEndTexture = new Texture("barbell_end.png");
        barbellLeft.add(SpriteComponent.builder().texture(barbellEndTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbellLeft.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbellLeft.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbellLeft.add(BoundsComponent.builder().rectangle(new Rectangle(position.x, position.y, 0.75f, 0.5f)).build());
        barbellLeft.add(CollisionComponent.builder().tag("BarbellEnd").isStatic(false).destroyTags(Arrays.asList("Player")).collideTags(Arrays.asList("Wall")).build());
        barbellLeft.add(OnDeathComponent.builder().onDeathEvent(barbellOnDeathEvent).build());
        barbellLeft.add(ParentComponent.builder().parent(barbellMiddle).relativePosition(new Vector2(-0.75f, -0.125f)).build());
        barbellLeft.add(HealthComponent.builder().health(1).build());

        Entity barbellRight = new Entity();
        barbellRight.add(SpriteComponent.builder().texture(barbellEndTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbellRight.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbellRight.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbellRight.add(BoundsComponent.builder().rectangle(new Rectangle(position.x, position.y, 0.75f, 0.5f)).build());
        barbellRight.add(CollisionComponent.builder().tag("BarbellEnd").isStatic(false).destroyTags(Arrays.asList("Player")).collideTags(Arrays.asList("Wall")).build());
        barbellRight.add(OnDeathComponent.builder().onDeathEvent(barbellOnDeathEvent).build());
        barbellRight.add(ParentComponent.builder().parent(barbellMiddle).relativePosition(new Vector2(4f, -0.125f)).build());
        barbellRight.add(HealthComponent.builder().health(1).build());

        engine.addEntity(barbellMiddle);
        engine.addEntity(barbellLeft);
        engine.addEntity(barbellRight);
    }
}
