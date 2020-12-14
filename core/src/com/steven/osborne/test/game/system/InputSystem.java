package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.steven.osborne.test.game.gameobject.component.InputComponent;
import com.steven.osborne.test.game.gameobject.component.VelocityComponent;
import com.steven.osborne.test.game.input.ActionListener;
import com.steven.osborne.test.game.input.ControllerListener;
import com.steven.osborne.test.game.input.InputAction;
import org.libsdl.SDL;

import static com.steven.osborne.test.game.gameobject.component.InputComponent.CONTROLLER_DEAD_ZONE;
import static com.steven.osborne.test.game.gameobject.component.VelocityComponent.SPEED;

public class InputSystem extends EntitySystem implements ActionListener, ControllerListener {

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
            VelocityComponent velocity = velocityComponentMapper.get(entity);//TODO - Basic input system is done. It will make player move in direction of button press. Make it work properly with controller

            if (action == InputAction.UP) {
                velocity.setY(100f);
                velocity.setX(0.0f);
            } else if (action == InputAction.DOWN) {
                velocity.setY(-100f);
                velocity.setX(0.0f);
            } else if (action == InputAction.LEFT) {
                velocity.setY(0.0f);
                velocity.setX(-100f);
            } else if (action == InputAction.RIGHT) {
                velocity.setY(0.0f);
                velocity.setX(100f);
            }
        }
        return true;
    }

    @Override
    public boolean onControllerInput(int axisIndex, float value) {
        if (value < CONTROLLER_DEAD_ZONE && value > -CONTROLLER_DEAD_ZONE) {
            value = 0f;
        }
        for (Entity entity : entities) {
            VelocityComponent velocity = velocityComponentMapper.get(entity);
            if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX) {
                velocity.setX(value * SPEED);
            }
            if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY) {
                velocity.setY(-value * SPEED);
            }
        }
        return true;
    }
}
