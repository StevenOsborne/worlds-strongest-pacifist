package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.steven.osborne.test.game.component.InputComponent;
import com.steven.osborne.test.game.component.VelocityComponent;
import com.steven.osborne.test.game.input.ActionListener;
import com.steven.osborne.test.game.input.ControllerListener;
import com.steven.osborne.test.game.input.InputAction;
import org.libsdl.SDL;

import static com.steven.osborne.test.game.component.InputComponent.CONTROLLER_DEAD_ZONE;

public class InputSystem extends EntitySystem implements ActionListener, ControllerListener {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityComponentMapper = ComponentMapper.getFor(VelocityComponent.class);

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(InputComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime) {

    }

    @Override
    public boolean onActionDown(InputAction action) {
        for (Entity entity : entities) {
            VelocityComponent velocity = velocityComponentMapper.get(entity);

            if (action == InputAction.UP) {
                velocity.velocity.y = velocity.speed;
            } else if (action == InputAction.DOWN) {
                velocity.velocity.y = -velocity.speed;
            } else if (action == InputAction.LEFT) {
                velocity.velocity.x = -velocity.speed;
            } else if (action == InputAction.RIGHT) {
                velocity.velocity.x = velocity.speed;
            }
        }

        if (action == InputAction.MENU) {
            Gdx.app.exit();
        }
        return true;
    }

    @Override
    public boolean onActionUp(InputAction action) {
        for (Entity entity : entities) {
            VelocityComponent velocity = velocityComponentMapper.get(entity);

            if (action == InputAction.UP) {
                velocity.velocity.y = 0f;
            } else if (action == InputAction.DOWN) {
                velocity.velocity.y = 0f;
            } else if (action == InputAction.LEFT) {
                velocity.velocity.x = 0f;
            } else if (action == InputAction.RIGHT) {
                velocity.velocity.x = 0f;
            }
        }
        return true;
    }

    @Override
    public boolean onControllerInput(int axisIndex, float value) {
        if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX || axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY) {
            if (value < CONTROLLER_DEAD_ZONE && value > -CONTROLLER_DEAD_ZONE) {
                value = 0f;
            }
            for (Entity entity : entities) {
                VelocityComponent velocity = velocityComponentMapper.get(entity);
                if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX) {
                    velocity.velocity.x = value * velocity.speed;
                }
                if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY) {
                    velocity.velocity.y = -value * velocity.speed;
                }
            }
            return true;
        }
        return false;
    }
}
