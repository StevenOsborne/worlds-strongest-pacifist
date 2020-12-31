package com.steven.osborne.test.game.listener;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.steven.osborne.test.game.component.CollisionComponent;

public class CollisionListener implements ContactListener {

    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Entity entityA = (Entity)bodyA.getUserData();
        Entity entityB = (Entity)bodyB.getUserData();

        CollisionComponent collisionComponentA = collisionComponentMapper.get(entityA);
        collisionComponentA.getCollidingWith().add(entityB);
        CollisionComponent collisionComponentB = collisionComponentMapper.get(entityB);
        collisionComponentB.getCollidingWith().add(entityA);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
