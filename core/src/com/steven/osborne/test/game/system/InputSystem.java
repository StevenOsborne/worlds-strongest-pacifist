package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.steven.osborne.test.game.ActionListener;
import com.steven.osborne.test.game.InputAction;
import com.steven.osborne.test.game.gameobject.component.InputComponent;
import com.steven.osborne.test.game.gameobject.component.VelocityComponent;

public class InputSystem extends EntitySystem implements ActionListener {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<InputComponent> inputComponentMapper = ComponentMapper.getFor(InputComponent.class);
    private ComponentMapper<VelocityComponent> velocityComponentMapper = ComponentMapper.getFor(VelocityComponent.class);

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(InputComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime) {

    }

    @Override
    public boolean onAction(InputAction action) {

        for (Entity entity : entities) {
            VelocityComponent velocity = velocityComponentMapper.get(entity);//TODO - Basic input system is done. It will make player move right when any button pressed. Make movement work properly

            velocity.setX(100f);
        }
        return true;
    }

}
